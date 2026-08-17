import os
import random
import subprocess
import requests

ZERO_SHA = "0" * 40
MAX_DIFF_CHARS = 12000

# Failures worth retrying on a second pass: rate-limited or overloaded, or a
# network-level hiccup. NOT worth retrying: 404 (model unavailable to this
# key), 400 (bad request), 403 (forbidden) - those will just fail identically
# every time, so retrying them only wastes time.
TRANSIENT_STATUS_CODES = {429, 500, 502, 503, 504}


def run(cmd: str) -> str:
    return subprocess.run(cmd, shell=True, capture_output=True, text=True, check=False).stdout.strip()


def get_diff(before_sha: str, after_sha: str) -> str:
    if before_sha == ZERO_SHA:
        # New branch - there's no "before" state to diff against.
        return run(f"git show {after_sha} --stat") + "\n\n(New branch - showing latest commit stats only)"
    return run(f"git diff {before_sha} {after_sha}")


def get_commit_log(before_sha: str, after_sha: str) -> str:
    if before_sha == ZERO_SHA:
        return run(f"git log -1 --pretty=format:'- %h %s (%an)' {after_sha}")
    return run(f"git log {before_sha}..{after_sha} --pretty=format:'- %h %s (%an)'")


GEMINI_API_BASE = "https://generativelanguage.googleapis.com/v1"  # v1beta has shown model listing/serving inconsistencies
REQUEST_TIMEOUT = 20  # seconds per attempt - kept modest since we may try many candidate models


def raise_with_body(response: requests.Response) -> None:
    """Surface the actual error body (Google's reason code/message), not just the HTTP status."""
    if not response.ok:
        print(f"Gemini API error {response.status_code}: {response.text}")
    response.raise_for_status()


def get_candidate_models(api_key: str) -> list:
    """
    Returns every model this API key can call generateContent on - no
    preference ordering. This used to sort "flash" and newer-version models
    first, but that was likely counterproductive: the newest, most popular
    model is exactly the one most likely to be under heavy load (this is
    what happened in practice - the newest flash model returned a 503 for
    "high demand" while it was being tried first). summarize_with_gemini
    shuffles this list and tries broadly across all of it instead of always
    reaching for the same "best" one first.
    """
    url = f"{GEMINI_API_BASE}/models?key={api_key}"
    try:
        response = requests.get(url, timeout=REQUEST_TIMEOUT)
    except requests.exceptions.RequestException as exc:
        raise RuntimeError(f"Could not reach Gemini's ListModels endpoint: {exc}") from exc

    raise_with_body(response)
    models = response.json().get("models", [])

    return [
        m["name"] for m in models
        if "generateContent" in m.get("supportedGenerationMethods", [])
    ]


def _try_model(model: str, prompt: str, api_key: str):
    """
    Attempts one model. Returns (text, None) on success, or
    (None, (status_code_or_None, message, is_transient)) on failure.
    """
    url = f"{GEMINI_API_BASE}/{model}:generateContent?key={api_key}"
    try:
        response = requests.post(
            url,
            headers={"content-type": "application/json"},
            json={"contents": [{"parts": [{"text": prompt}]}]},
            timeout=REQUEST_TIMEOUT,
        )
    except requests.exceptions.RequestException as exc:
        # Network-level failure (timeout, connection reset, etc.) rather than
        # an HTTP error response - always worth retrying, since it says
        # nothing about whether the model itself would actually refuse.
        print(f"Model {model} unavailable (network error): {exc}")
        return None, (None, str(exc), True)

    if response.ok:
        data = response.json()
        return data["candidates"][0]["content"]["parts"][0]["text"].strip(), None

    print(f"Model {model} unavailable ({response.status_code}): {response.text}")
    is_transient = response.status_code in TRANSIENT_STATUS_CODES
    return None, (response.status_code, response.text, is_transient)


def summarize_with_gemini(diff_text: str, commit_log: str, api_key: str) -> str:
    truncated = diff_text[:MAX_DIFF_CHARS]
    if len(diff_text) > MAX_DIFF_CHARS:
        truncated += "\n\n[diff truncated]"

    prompt = (
        "Summarize this git diff for a short Telegram notification to teammates. "
        "3-5 concise bullet points, plain language, no code blocks. "
        "Focus on what changed and why it matters, not line-by-line detail.\n\n"
        f"Commit messages:\n{commit_log}\n\n"
        f"Diff:\n{truncated}"
    )

    candidates = get_candidate_models(api_key)
    if not candidates:
        raise RuntimeError("No models supporting generateContent are available for this API key.")

    # Shuffle rather than always trying the same "best" model first - if every
    # run of this script reaches for the same model first, that model becomes
    # a hotspot, which is plausibly part of why the newest/most popular one
    # tends to be the one returning 503s.
    random.shuffle(candidates)

    last_failure = None
    retry_worthy = []

    for model in candidates:
        text, failure = _try_model(model, prompt, api_key)
        if text is not None:
            return text
        last_failure = failure
        if failure[2]:  # is_transient
            retry_worthy.append(model)

    # Every candidate failed on the first pass. Before giving up, retry the
    # ones that failed for reasons likely to be temporary - by now some time
    # has passed while trying the others, and an overloaded model may have
    # freed up. Skip the ones that failed for a permanent reason (404/400) -
    # retrying those would just fail identically again.
    if retry_worthy:
        print(f"First pass failed for all {len(candidates)} models. Retrying {len(retry_worthy)} that failed transiently...")
        for model in retry_worthy:
            text, failure = _try_model(model, prompt, api_key)
            if text is not None:
                return text
            last_failure = failure

    status_code, message, _ = last_failure
    if status_code is not None:
        raise RuntimeError(f"All {len(candidates)} candidate models failed. Last: HTTP {status_code}: {message}")
    raise RuntimeError(f"All {len(candidates)} candidate models failed. Last error: {message}")


def send_telegram_message(bot_token: str, chat_id: str, text: str) -> None:
    url = f"https://api.telegram.org/bot{bot_token}/sendMessage"
    resp = requests.post(
        url,
        data={
            "chat_id": chat_id,
            "text": text,
            "parse_mode": "HTML",
            "disable_web_page_preview": True,
        },
        timeout=15,
    )
    resp.raise_for_status()


def main() -> None:
    before_sha = os.environ["BEFORE_SHA"]
    after_sha = os.environ["AFTER_SHA"]
    repo = os.environ["REPO_NAME"]
    branch = os.environ["BRANCH_NAME"]
    actor = os.environ["ACTOR"]

    telegram_token = os.environ["TELEGRAM_BOT_TOKEN"]
    telegram_chat_id = os.environ["TELEGRAM_CHAT_ID"]
    gemini_key = os.environ["GEMINI_API_KEY"]

    diff_text = get_diff(before_sha, after_sha)
    commit_log = get_commit_log(before_sha, after_sha)

    if not diff_text.strip() and not commit_log.strip():
        print("Nothing to report, skipping.")
        return

    summary = summarize_with_gemini(diff_text, commit_log, gemini_key)

    message = (
        f"<b>New push to {repo}</b> (branch: {branch}) by {actor}\n\n"
        f"<b>Commits:</b>\n{commit_log}\n\n"
        f"<b>Summary:</b>\n{summary}"
    )

    send_telegram_message(telegram_token, telegram_chat_id, message)
    print("Notification sent.")


if __name__ == "__main__":
    main()
