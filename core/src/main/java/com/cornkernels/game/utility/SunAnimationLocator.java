package com.cornkernels.game.utility;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import pvz.libpvz.pam.ClipRef;
import pvz.libpvz.pam.PamPlayer;

import java.util.List;

public final class SunAnimationLocator {

    private static final String SUN_PAM_PATH = "768/INITIAL/EFFECTS/SUN/SUN.PAM";
    private static final String[] PREFERRED_CLIP_NAMES = {"idle", "loop", "animation"};

    private SunAnimationLocator() {
    }

    public static @Nullable ClipRef loadClip(@NonNull PamPlayer pamPlayer) {
        pamPlayer.loadSync(SUN_PAM_PATH);
        List<String> availableClips = pamPlayer.clips(SUN_PAM_PATH);
        if (availableClips == null || availableClips.isEmpty()) return null;

        for (String preferred : PREFERRED_CLIP_NAMES) {
            if (availableClips.contains(preferred)) {
                ClipRef clip = pamPlayer.getClip(SUN_PAM_PATH, preferred);
                if (clip != null) return clip;
            }
        }
        return pamPlayer.getClip(SUN_PAM_PATH, availableClips.get(0));
    }
}
