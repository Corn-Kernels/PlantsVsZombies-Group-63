package com.cornkernels.game.utility;

import com.badlogic.gdx.Gdx;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.types.plants.PlantDef;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import pvz.libpvz.pam.ClipRef;
import pvz.libpvz.pam.PamPlayer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class PlantAnimationLocator {

    private static final String[] PAM_PATH_TEMPLATES = {
        "768/INITIAL/PLANT/%1$s/%1$s.PAM",
        "768/FULL/PLANT/%1$s/%1$s.PAM",
        "768/INITIAL/EMPOWERMINTS/PLANT/%1$s/%1$s.PAM",
        "768/FULL/EMPOWERMINTS/PLANT/%1$s/%1$s.PAM",
    };

    private PlantAnimationLocator() {
    }

    public static @Nullable String findPamPath(@NonNull PlantDef plantDef) {
        String slug = slugify(plantDef.getPlantName());
        for (String template : PAM_PATH_TEMPLATES) {
            String path = String.format(template, slug);
            if (Gdx.files.internal("IMAGES/" + path).exists()) {
                return path;
            }
        }
        return null;
    }

    public static void applySpawnAnimation(@NonNull PamPlayer pamPlayer, @NonNull PamAnimationComponent anim,
                                           @NonNull String pamPath) {
        pamPlayer.loadSync(pamPath);
        List<String> availableClips = pamPlayer.clips(pamPath);

        Deque<ClipRef> queue = new ArrayDeque<>();
        for (String clipName : resolveSpawnSequence(availableClips)) {
            ClipRef clip = pamPlayer.getClip(pamPath, clipName);
            if (clip != null) queue.add(clip);
        }
        if (queue.isEmpty()) return;

        anim.currentClip = queue.poll();
        anim.stateTime = 0f;
        anim.upcomingClips = queue;
        anim.isLooping = queue.isEmpty();
    }

    public static void applyClip(@NonNull PamPlayer pamPlayer, @NonNull PamAnimationComponent anim,
                                 @NonNull PlantDef plantDef, @NonNull String preferredClip) {
        String path = findPamPath(plantDef);
        if (path == null) return;
        pamPlayer.loadSync(path);

        List<String> available = pamPlayer.clips(path);
        if (available == null || available.isEmpty()) return;

        String clipName = available.contains(preferredClip) ? preferredClip
            : available.contains("idle") ? "idle" : available.get(0);
        ClipRef clip = pamPlayer.getClip(path, clipName);
        if (clip == null) return;

        anim.currentClip = clip;
        anim.stateTime = 0f;
        anim.isLooping = true;
        anim.upcomingClips.clear();
    }

    private static @NonNull List<String> resolveSpawnSequence(@Nullable List<String> availableClips) {
        List<String> sequence = new ArrayList<>();
        if (availableClips == null || availableClips.isEmpty()) return sequence;

        if (availableClips.contains("intro")) sequence.add("intro");

        if (availableClips.contains("loop")) sequence.add("loop");
        else if (availableClips.contains("idle")) sequence.add("idle");
        else if (sequence.isEmpty()) sequence.add(availableClips.get(0));

        return sequence;
    }

    private static @NonNull String slugify(@NonNull String plantName) {
        StringBuilder builder = new StringBuilder(plantName.length());
        for (char c : plantName.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                builder.append(Character.toUpperCase(c));
            }
        }
        return builder.toString();
    }
}
