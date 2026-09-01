package com.cornkernels.game.utility;

import com.cornkernels.game.entities.components.PamAnimationComponent;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.ClipRef;
import pvz.libpvz.pam.PamPlayer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public final class PlantFoodEffectAnimationLocator {

    private static final String FX_PAM_PATH = "768/INITIAL/EFFECTS/PLANTFOOD_FX/PLANTFOOD_FX.PAM";

    private PlantFoodEffectAnimationLocator() {
    }

    public static void playOn(@NonNull PamPlayer pamPlayer, @NonNull PamAnimationComponent anim) {
        pamPlayer.loadSync(FX_PAM_PATH);
        List<String> available = pamPlayer.clips(FX_PAM_PATH);
        if (available == null || available.isEmpty()) return;

        Deque<ClipRef> queue = new ArrayDeque<>();
        if (available.contains("plantfood_on")) {
            ClipRef intro = pamPlayer.getClip(FX_PAM_PATH, "plantfood_on");
            if (intro != null) queue.add(intro);
        }
        if (available.contains("plantfood")) {
            ClipRef loop = pamPlayer.getClip(FX_PAM_PATH, "plantfood");
            if (loop != null) queue.add(loop);
        }
        if (queue.isEmpty()) return;

        anim.currentClip = queue.poll();
        anim.stateTime = 0f;
        anim.upcomingClips = queue;
        anim.isLooping = queue.isEmpty();
    }

    public static float playOff(@NonNull PamPlayer pamPlayer, @NonNull PamAnimationComponent anim) {
        pamPlayer.loadSync(FX_PAM_PATH);
        List<String> available = pamPlayer.clips(FX_PAM_PATH);
        if (available == null || !available.contains("plantfood_off")) return 0f;

        ClipRef outro = pamPlayer.getClip(FX_PAM_PATH, "plantfood_off");
        if (outro == null) return 0f;

        anim.currentClip = outro;
        anim.stateTime = 0f;
        anim.isLooping = false;
        anim.upcomingClips.clear();
        return outro.duration;
    }
}
