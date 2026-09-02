package com.cornkernels.game.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.cornkernels.game.entities.components.ArmorComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.ZombieLimbs;
import com.cornkernels.game.entities.types.zombies.armors.ArmorType;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.ClipRef;
import pvz.libpvz.pam.PamPlayer;

import java.util.List;
import java.util.Map;

public final class ZombieAnimationLocator {

    private static final String DEFAULT_PAM_PATH = "768/FULL/ZOMBIE/ZOMBIE_FUTURE_BASIC/ZOMBIE_FUTURE_BASIC.PAM";

    private static final String[] PAM_PATH_TEMPLATES = {
        "768/FULL/ZOMBIE/%1$s/%1$s.PAM",
        "768/FULL/ZOMBIE/ZOMBIE_%1$s/ZOMBIE_%1$s.PAM",
        "768/INITIAL/ZOMBIE/%1$s/%1$s.PAM",
        "768/INITIAL/ZOMBIE/ZOMBIE_%1$s/ZOMBIE_%1$s.PAM",
    };

    private static final Map<ZombieDef, String> PAM_PATH_OVERRIDES = Map.ofEntries(
        Map.entry(ZombieDef.DEFAULT, "768/INITIAL/ZOMBIE/ZOMBIE_TUTORIAL/ZOMBIE_TUTORIAL.PAM"),
        Map.entry(ZombieDef.ARMOR1, "768/INITIAL/ZOMBIE/ZOMBIE_TUTORIAL/ZOMBIE_TUTORIAL.PAM"),
        Map.entry(ZombieDef.ARMOR2, "768/INITIAL/ZOMBIE/ZOMBIE_TUTORIAL/ZOMBIE_TUTORIAL.PAM"),
        Map.entry(ZombieDef.ARMOR4, "768/INITIAL/ZOMBIE/ZOMBIE_TUTORIAL/ZOMBIE_TUTORIAL.PAM"),
        Map.entry(ZombieDef.DARK_ARMOR3, "768/INITIAL/ZOMBIE/ZOMBIE_TUTORIAL/ZOMBIE_TUTORIAL.PAM"),
        Map.entry(ZombieDef.IMP, "768/INITIAL/ZOMBIE/ZOMBIE_TUTORIAL_IMP/ZOMBIE_TUTORIAL_IMP.PAM"),
        Map.entry(ZombieDef.RA, "768/INITIAL/ZOMBIE/ZOMBIE_EGYPT_RA/ZOMBIE_EGYPT_RA.PAM"),
        Map.entry(ZombieDef.TOMB_RAISER, "768/INITIAL/ZOMBIE/ZOMBIE_EGYPT_TOMBRAISER/ZOMBIE_EGYPT_TOMBRAISER.PAM"),
        Map.entry(ZombieDef.ICE_AGE_DODO, "768/FULL/ZOMBIE/ZOMBIE_ICEAGE_DODORIDER/ZOMBIE_ICEAGE_DODORIDER.PAM"),
        Map.entry(ZombieDef.ICE_AGE_HUNTER, "768/FULL/ZOMBIE/ZOMBIE_ICEAGE_HUNTER/ZOMBIE_ICEAGE_HUNTER.PAM"),
        Map.entry(ZombieDef.ICE_AGE_TROGLOBITE, "768/FULL/ZOMBIE/ZOMBIE_ICEAGE_TROGLOBITE/ZOMBIE_ICEAGE_TROGLOBITE.PAM"),
        Map.entry(ZombieDef.BEACH_FISHERMAN, "768/FULL/ZOMBIE/ZOMBIE_BEACH_FISHERMAN/ZOMBIE_BEACH_FISHERMAN.PAM"),
        Map.entry(ZombieDef.BEACH_OCTOPUS, "768/FULL/ZOMBIE/ZOMBIE_BEACH_OCTOPUS/ZOMBIE_BEACH_OCTOPUS.PAM"),
        Map.entry(ZombieDef.BEACH_SNORKEL, "768/FULL/ZOMBIE/ZOMBIE_BEACH_SNORKELER/ZOMBIE_BEACH_SNORKELER.PAM"),
        Map.entry(ZombieDef.DARK_JUGGLER, "768/FULL/ZOMBIE/ZOMBIE_DARK_JESTER/ZOMBIE_DARK_JESTER.PAM"),
        Map.entry(ZombieDef.WIZARD, "768/FULL/ZOMBIE/ZOMBIE_DARK_WIZARD/ZOMBIE_DARK_WIZARD.PAM"),
        Map.entry(ZombieDef.DARK_KING, "768/FULL/ZOMBIE/ZOMBIE_DARK_KING/ZOMBIE_DARK_KING.PAM"),
        Map.entry(ZombieDef.DARK_IMP_DRAGON, "768/FULL/ZOMBIE/ZOMBIE_DARK_IMP_DRAGON/ZOMBIE_DARK_IMP_DRAGON.PAM"),
        Map.entry(ZombieDef.MODERN_ALL_STAR, "768/FULL/ZOMBIE/ZOMBIE_MODERN_ALLSTAR/ZOMBIE_MODERN_ALLSTAR.PAM"),
        Map.entry(ZombieDef.LOST_CITY_JANE, "768/FULL/ZOMBIE/ZOMBIE_LOSTCITY_JANE/ZOMBIE_LOSTCITY_JANE.PAM"),
        Map.entry(ZombieDef.CRYSTAL_SKULL, "768/FULL/ZOMBIE/ZOMBIE_LOSTCITY_CRYSTALSKULL/ZOMBIE_LOSTCITY_CRYSTALSKULL.PAM"),
        Map.entry(ZombieDef.NEWSPAPER, "768/FULL/ZOMBIE/ZOMBIE_MODERN_NEWSPAPER/ZOMBIE_MODERN_NEWSPAPER.PAM"),
        Map.entry(ZombieDef.ARCADE, "768/FULL/ZOMBIE/ZOMBIE_80S_ARCADE/ZOMBIE_80S_ARCADE.PAM")
    );
    private static final String[] FALLBACK_CLIP_NAMES = {"idle", "loop"};
    private static final String[] DEATH_CLIP_NAMES = {"death", "die", "dying", "Death", "Die", "Dying"};

    private static final Map<ArmorType, String[]> ARMOR_NODES = Map.of(
        ArmorType.CONE, new String[]{"zombie_armor_cone_norm", "zombie_armor_cone_damage_01", "zombie_armor_cone_damage_02"},
        ArmorType.BUCKET, new String[]{"zombie_armor_bucket_norm", "zombie_armor_bucket_damage_01", "zombie_armor_bucket_damage_02"},
        ArmorType.BRICK, new String[]{"zombie_armor_brick_norm", "zombie_armor_brick_damage_01", "zombie_armor_brick_damage_02"},
        ArmorType.CROWN, new String[]{"zombie_armor_crown_norm", "zombie_armor_crown_damage_01", "zombie_armor_crown_damage_02"},
        ArmorType.SHOULDER, new String[]{"zombie_shoulder_armor_norm", "zombie_shoulder_armor_damage_01", "zombie_shoulder_armor_damage_02"},
        ArmorType.NEWSPAPER, new String[]{"_zombie_newspaper", "_zombie_newspaper_dmg1", "_zombie_newspaper_dmg2"}
    );

    private ZombieAnimationLocator() {
    }

    public static void applyArmorVisibility(@NonNull ZombieDef zombieDef, @NonNull PamAnimationComponent anim) {
        for (ArmorType armorType : zombieDef.armors) {
            String[] nodes = ARMOR_NODES.get(armorType);
            if (nodes != null) {
                anim.visibilityMap.put(nodes[0], true);
            }
        }
    }

    public static @NonNull String findPamPath(@NonNull ZombieDef zombieDef) {
        String override = PAM_PATH_OVERRIDES.get(zombieDef);
        if (override != null && Gdx.files.internal("IMAGES/" + override).exists()) {
            return override;
        }

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
        applyClip(pamPlayer, anim, path, preferredClip);
    }

    // Secondary overload to load clips directly using a custom PAM file path string
    public static void applyClip(@NonNull PamPlayer pamPlayer, @NonNull PamAnimationComponent anim,
                                 @NonNull String path, @NonNull String preferredClip) {
        pamPlayer.loadSync(path);

        List<String> available = pamPlayer.clips(path);
        if (available == null || available.isEmpty()) return;

        String clipName = resolveClipName(available, preferredClip);
        ClipRef clip = pamPlayer.getClip(path, clipName);
        if (clip == null) return;

        anim.currentClip = clip;
        anim.stateTime = clip.duration > 0f ? MathUtils.random(0f, clip.duration) : 0f;
        anim.isLooping = true;
        anim.upcomingClips.clear();
    }

    public static float applyDeathClip(@NonNull PamPlayer pamPlayer, @NonNull PamAnimationComponent anim,
                                       @NonNull ZombieDef zombieDef) {
        String path = findPamPath(zombieDef);
        pamPlayer.loadSync(path);

        List<String> available = pamPlayer.clips(path);
        if (available != null) {
            for (String candidate : DEATH_CLIP_NAMES) {
                if (!available.contains(candidate)) continue;
                ClipRef clip = pamPlayer.getClip(path, candidate);
                if (clip == null) continue;

                anim.currentClip = clip;
                anim.stateTime = 0f;
                anim.isLooping = false;
                anim.upcomingClips.clear();
                return clip.duration;
            }
        }

        anim.isLooping = false;
        anim.upcomingClips.clear();
        return 0f;
    }

    private static @NonNull String resolveClipName(@NonNull List<String> available, @NonNull String preferredClip) {
        if (available.contains(preferredClip)) {
            return preferredClip;
        }
        for (String fallback : FALLBACK_CLIP_NAMES) {
            if (available.contains(fallback)) {
                return fallback;
            }
        }
        return available.get(0);
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

    private static void toggleNodeSafe(@NonNull PamAnimationComponent anim, @NonNull String node, boolean state) {
        anim.visibilityMap.put(node, state);
    }

    public static ZombieLimbs applyArmLossVisibility(@NonNull ZombieInstance zombie, PamPlayer pamPlayer) {
        PamAnimationComponent anim = zombie.get(PamAnimationComponent.class);
        if (anim == null) return null;

        if (Boolean.TRUE.equals(anim.visibilityMap.get("zombie_arm_outer_upper_bone"))) {
            return null;
        }

        toggleNodeSafe(anim, "zombie_hand_outer_01", false);
        toggleNodeSafe(anim, "zombie_arm_outer_lower", false);
        toggleNodeSafe(anim, "zombie_arm_outer_upper", false);
        toggleNodeSafe(anim, "zombie_arm_outer_upper_bone", true);

        ZombieDef def = zombie.get(ZombieDefComponent.class).def();
        PositionComponent pos = zombie.get(PositionComponent.class);
        return new ZombieLimbs(ZombieLimbs.LimbType.ARM, pos.position, pos.position.getY(), def, pamPlayer);
    }

    public static void updateArmorVisibility(@NonNull ZombieInstance zombie) {
        PamAnimationComponent anim = zombie.get(PamAnimationComponent.class);
        ZombieDefComponent defComp = zombie.get(ZombieDefComponent.class);
        if (anim == null || defComp == null) return;

        ZombieDef def = defComp.def();

        for (ArmorType armorType : def.armors) {
            String[] nodes = ARMOR_NODES.get(armorType);
            if (nodes != null) {
                toggleNodeSafe(anim, nodes[0], false);
                toggleNodeSafe(anim, nodes[1], false);
                toggleNodeSafe(anim, nodes[2], false);

                if (armorType == ArmorType.NEWSPAPER) {
                    toggleNodeSafe(anim, "_zombie_newspaper_flame", false);
                }
            }
        }

        for (ArmorComponent armorComp : zombie.getAll(ArmorComponent.class)) {
            if (armorComp.currentArmorHealth <= 0) continue;

            String[] nodes = ARMOR_NODES.get(armorComp.armorType);
            if (nodes != null) {
                float healthRatio = (float) armorComp.currentArmorHealth / armorComp.armorType.getArmorDamage();

                if (healthRatio > 0.66f) {
                    toggleNodeSafe(anim, nodes[0], true);
                } else if (healthRatio > 0.33f) {
                    toggleNodeSafe(anim, nodes[1], true);
                } else {
                    toggleNodeSafe(anim, nodes[2], true);

                    if (armorComp.armorType == ArmorType.NEWSPAPER) {
                        toggleNodeSafe(anim, "_zombie_newspaper_flame", true);
                    }
                }
            }
        }
    }
}
