package com.cornkernels.game.utility;

import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.*;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.*;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.ClipRef;
import pvz.libpvz.pam.PamPlayer;

import java.util.List;

public final class ProjectileAnimationLocator {

    private static final List<Entry> ENTRIES = List.of(
        new Entry(GooPeaProjectile.class,
            "768/INITIAL/EFFECTS/GOOPEASHOOTER_PROJECTILES/GOOPEASHOOTER_PROJECTILES.PAM", "projectile_t1"),
        new Entry(HypnoHomingProjectile.class,
            "768/INITIAL/EFFECTS/CAULIPOWER_PROJECTILE/CAULIPOWER_PROJECTILE.PAM", "animation"),
        new Entry(GrapeshotProjectile.class,
            "768/INITIAL/EFFECTS/GRAPESHOT_PROJECTILE/GRAPESHOT_PROJECTILE.PAM", "animation_forward"),
        new Entry(PeaProjectile.class,
            "768/INITIAL/EFFECTS/SLINGPEA_PROJECTILE/SLINGPEA_PROJECTILE.PAM", "tier1"),
        new Entry(TruePeaProjectile.class,
            "768/INITIAL/EFFECTS/SLINGPEA_PROJECTILE/SLINGPEA_PROJECTILE.PAM", "tier1"),
        new Entry(StrikeThroughProjectile.class,
            "768/INITIAL/EFFECTS/CACTUS_PROJECTILE/CACTUS_PROJECTILE.PAM", "idle"),
        new Entry(BowlingProjectile.class,
            "768/FULL/EFFECTS/BOWLINGBULB_PROJECTILE1/BOWLINGBULB_PROJECTILE1.PAM", "animation"),
        new Entry(LightningCloudProjectile.class,
            "768/INITIAL/EFFECTS/ELECTRICBLUEBERRY_CLOUD_PROJECTILE/ELECTRICBLUEBERRY_CLOUD_PROJECTILE.PAM", "idle"),
        new Entry(LobProjectile.class,
            "768/INITIAL/EFFECTS/T_CABBAGEPULT_PROJECTILE/T_CABBAGEPULT_PROJECTILE.PAM", "animation"),
        new Entry(AreaOfDamage.class,
            "768/FULL/EFFECTS/CHERRYBOMB_EXPLOSION_TOP/CHERRYBOMB_EXPLOSION_TOP.PAM", "explosion"),
        new Entry(LineOfDamage.class,
            "768/INITIAL/EFFECTS/JALAPENO_FIRE/JALAPENO_FIRE.PAM", "idle")
    );

    private ProjectileAnimationLocator() {
    }

    public static void assignClip(@NonNull PamPlayer pamPlayer, @NonNull PamAnimationComponent anim,
                                  @NonNull AbstractProjectile projectile) {
        for (Entry entry : ENTRIES) {
            if (entry.type().isInstance(projectile)) {
                applyClip(pamPlayer, anim, entry.path(), entry.preferredClip());
                return;
            }
        }
    }

    private static void applyClip(@NonNull PamPlayer pamPlayer, @NonNull PamAnimationComponent anim,
                                  @NonNull String path, @NonNull String preferredClip) {
        pamPlayer.loadSync(path);
        List<String> available = pamPlayer.clips(path);
        if (available == null || available.isEmpty()) return;

        String clipName = available.contains(preferredClip) ? preferredClip : available.get(0);
        ClipRef clip = pamPlayer.getClip(path, clipName);
        if (clip == null) return;

        anim.currentClip = clip;
        anim.stateTime = 0f;
        anim.isLooping = true;
    }

    private record Entry(Class<? extends AbstractProjectile> type, String path, String preferredClip) {
    }
}
