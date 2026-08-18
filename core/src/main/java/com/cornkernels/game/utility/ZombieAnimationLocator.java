package com.cornkernels.game.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.ClipRef;
import pvz.libpvz.pam.PamPlayer;

import java.util.List;

public final class ZombieAnimationLocator {

    private static final String DEFAULT_PAM_PATH = "768/FULL/ZOMBIE/ZOMBIE_FUTURE_BASIC/ZOMBIE_FUTURE_BASIC.PAM";
    private static final String[] PAM_PATH_TEMPLATES = {
        "768/FULL/ZOMBIE/%1$s/%1$s.PAM",
        "768/FULL/ZOMBIE/ZOMBIE_%1$s/ZOMBIE_%1$s.PAM",
    };

    private ZombieAnimationLocator() {
    }

    public static @NonNull String findPamPath(@NonNull ZombieDef zombieDef) {
        String slug = slugify(zombieDef.name());
        for (String template : PAM_PATH_TEMPLATES) {
            String path = String.format(template, slug);
            if (Gdx.files.internal("IMAGES/" + path).exists()) {
                return path;
            }
        }
        return DEFAULT_PAM_PATH;
    }

    public static void applyClip(@NonNull PamPlayer pamPlayer, @NonNull PamAnimationComponent anim,
                                 @NonNull ZombieDef zombieDef, @NonNull String preferredClip) {
        String path = findPamPath(zombieDef);
        pamPlayer.loadSync(path);

        ClipRef clip = pamPlayer.getClip(path, preferredClip);
        if (clip == null) {
            List<String> available = pamPlayer.clips(path);
            if (available == null || available.isEmpty()) return;
            clip = pamPlayer.getClip(path, available.get(0));
        }
        if (clip == null) return;

        anim.currentClip = clip;
        anim.stateTime = clip.duration > 0f ? MathUtils.random(0f, clip.duration) : 0f;
        anim.isLooping = true;
        anim.upcomingClips.clear();
    }

    private static @NonNull String slugify(@NonNull String name) {
        StringBuilder builder = new StringBuilder(name.length());
        for (char c : name.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                builder.append(Character.toUpperCase(c));
            }
        }
        return builder.toString();
    }
}
