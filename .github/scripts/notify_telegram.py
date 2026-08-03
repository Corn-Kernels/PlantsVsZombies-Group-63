import os
import subprocess
import requests

ZERO_SHA = "0" * 40
MAX_DIFF_CHARS = 12000


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


def raise_with_body(response: requests.Response) -> None:
    """Surface the actual error body (Google's reason code/message), not just the HTTP status."""
    if not response.ok:
        print(f"Gemini API error {response.status_code}: {response.text}")
    response.raise_for_status()


def get_available_model(api_key: str) -> str:
    """
    Gemini model names get renamed/retired fairly often (gemini-pro,
    gemini-1.5-flash, and various preview models have all 404'd at different
    points), and models can even show up in ListModels as supporting
    generateContent yet still 404 on v1beta - a known inconsistency. Using
    the stable v1 endpoint for both listing and calling avoids that.
    """
    url = f"{GEMINI_API_BASE}/models?key={api_key}"
    response = requests.get(url, timeout=15)
    raise_with_body(response)
    models = response.json().get("models", [])

    candidates = [
        m["name"] for m in models
        if "generateContent" in m.get("supportedGenerationMethods", [])
    ]

    for name in candidates:
        if "flash" in name.lower():
            return name  # e.g. "models/gemini-2.5-flash"

    if candidates:
        return candidates[0]

    raise RuntimeError("No models supporting generateContent are available for this API key.")


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

    model = get_available_model(api_key)
    url = f"{GEMINI_API_BASE}/{model}:generateContent?key={api_key}"
    response = requests.post(
        url,
        headers={"content-type": "application/json"},
        json={"contents": [{"parts": [{"text": prompt}]}]},
        timeout=30,
    )
    raise_with_body(response)
    data = response.json()
    return data["candidates"][0]["content"]["parts"][0]["text"].strip()


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
