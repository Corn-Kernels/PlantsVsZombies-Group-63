package com.cornkernels.game.systems.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.OctoedComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFreezeComponent;
import com.cornkernels.game.entities.components.plant_specific.SheepedComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDeathComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.ButterComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.PoisonComponent;
import com.cornkernels.game.entities.types.lawnmower.LawnMower;
import com.cornkernels.game.entities.types.obstacles.*;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.AbstractZombieProjectile;
import com.cornkernels.game.entities.types.projectile.ZombieProjectiles.BoneProjectile;
import com.cornkernels.game.entities.types.projectile.ZombieProjectiles.OctopusProjectile;
import com.cornkernels.game.entities.types.projectile.ZombieProjectiles.SnowballProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.specific.TruePeaProjectile;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.entities.types.sun.SunType;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.ZombieLimbs;
import com.cornkernels.game.map.data.MapData;
import com.cornkernels.game.utility.LawnMowerAnimationLocator;
import com.cornkernels.game.utility.ProjectileAnimationLocator;
import com.cornkernels.game.utility.SunAnimationLocator;
import com.cornkernels.game.utility.ZombieAnimationLocator;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.ClipRef;
import pvz.libpvz.pam.PamPlayer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PamRenderSystem extends RenderSystem {

    private static final String CHILL_PAM = "768/FULL/EFFECTS/FROSTBITE_CHILL_PLANT/FROSTBITE_CHILL_PLANT.PAM";
    private static final String FREEZE_PAM = "768/FULL/EFFECTS/FROSTBITE_ICE_BLOCK_PLANT/FROSTBITE_ICE_BLOCK_PLANT.PAM";
    private static final String SHEEP_PAM = "768/FULL/EFFECTS/DARK_WIZARD_SHEEPENING/DARK_WIZARD_SHEEPENING.PAM";
    private static final String OCTOPUS_PAM = "768/FULL/EFFECTS/ZOMBIE_OCTOPUS_PROJECTILE/ZOMBIE_OCTOPUS_PROJECTILE.PAM";
    private static final String GLACIER_PAM = "768/FULL/EFFECTS/FROSTBITE_ICE_BLOCK_ZOMBIE/FROSTBITE_ICE_BLOCK_ZOMBIE.PAM";
    private static final String ARCADE_PAM = "768/FULL/EFFECTS/80S_ARCADE_CABINET/80S_ARCADE_CABINET.PAM";
    private static final String REDIRECTOR_UP_PAM = "768/FULL/EFFECTS/TILESLIDER_ICEAGE_UP/TILESLIDER_ICEAGE_UP.PAM";
    private static final String REDIRECTOR_DOWN_PAM = "768/FULL/EFFECTS/TILESLIDER_ICEAGE_DOWN/TILESLIDER_ICEAGE_DOWN.PAM";
    private static final String GRAVE_PAM = "768/INITIAL/GRAVESTONES/TUTORIAL_GRAVESTONE/TUTORIAL_GRAVESTONE.PAM";
    private static final String SUN_BOMB_PAM = "768/FULL/EFFECTS/SUN_BOMB/SUN_BOMB.PAM";
    private static final String ELECTROBALL_PAM = "768/INITIAL/EFFECTS/ELECTRIC_PEASHOOTER_ELECTROBALL/ELECTRIC_PEASHOOTER_ELECTROBALL.PAM";
    private static final String FIRE_PEA_PAM = "768/INITIAL/EFFECTS/T_FIRE_PEA/T_FIRE_PEA.PAM";
    private static final String SNOW_PEA_PAM = "768/INITIAL/EFFECTS/T_SNOW_PEA/T_SNOW_PEA.PAM";
    private static final String NORMAL_PEA_PAM = "768/INITIAL/EFFECTS/T_PEA_PROJECTILE/T_PEA_PROJECTILE.PAM";
    private final PamPlayer pamPlayer;
    private final MapData mapData;
    private final Map<Entity, Float> zombieProjectileTimes = new HashMap<>();
    private float scale = 0.5f;

    public PamRenderSystem(SpriteBatch batch, PamPlayer pamPlayer, MapData mapData) {
        super(batch);
        this.pamPlayer = pamPlayer;
        this.mapData = mapData;

        pamPlayer.loadSync(CHILL_PAM);
        pamPlayer.loadSync(FREEZE_PAM);
        pamPlayer.loadSync(SHEEP_PAM);
        pamPlayer.loadSync(OCTOPUS_PAM);

        pamPlayer.loadSync(GLACIER_PAM);
        pamPlayer.loadSync(ARCADE_PAM);
        pamPlayer.loadSync(REDIRECTOR_UP_PAM);
        pamPlayer.loadSync(REDIRECTOR_DOWN_PAM);
        pamPlayer.loadSync(GRAVE_PAM);
        pamPlayer.loadSync(SUN_BOMB_PAM);
        pamPlayer.loadSync(ELECTROBALL_PAM);

        pamPlayer.loadSync(FIRE_PEA_PAM);
        pamPlayer.loadSync(SNOW_PEA_PAM);
        pamPlayer.loadSync(NORMAL_PEA_PAM);
    }

    private static float sunVisualScale(@NonNull SunType sunType) {
        float ratio = sunType.value <= 0 ? 1f : sunType.value / (float) SunType.NORMAL.value;
        return MathUtils.clamp((float) Math.sqrt(ratio), 0.4f, 2.2f);
    }

    @Override
    public void render(float delta) {
        List<Entity> renderableEntities = field.getEntitiesWith(PositionComponent.class, PamAnimationComponent.class);
        renderableEntities.sort(Comparator.comparingDouble(e -> -e.get(PositionComponent.class).position.getY()));

        for (Entity entity : renderableEntities) {
            processEntityAnimation(entity, delta);
            renderEntity(entity);
        }

        renderZombieProjectiles(delta);
    }

    private void processEntityAnimation(Entity entity, float delta) {
        PamAnimationComponent anim = entity.get(PamAnimationComponent.class);
        if (anim.currentClip == null) {
            assignInitialClip(entity, anim);
        }
        anim.stateTime += delta;
        advanceSequence(anim);
    }

    private void assignInitialClip(Entity entity, PamAnimationComponent anim) {
        if (entity instanceof SunInstance) {
            assignSunClip(anim);
        } else if (entity instanceof LawnMower) {
            assignLawnMowerClip(anim);
        } else if (entity instanceof AbstractObstacle obstacle) {
            assignObstacleClip(anim, obstacle);
        } else if (entity instanceof AbstractProjectile projectile) {
            if (projectile instanceof TruePeaProjectile pea) {
                anim.currentClip = getSafeClip(pea.pamPath, pea.clipName);
                anim.isLooping = true;
            } else {
                ProjectileAnimationLocator.assignClip(pamPlayer, anim, projectile);
            }
        }
    }

    private void renderEntity(Entity entity) {
        PositionComponent posComp = entity.get(PositionComponent.class);
        PamAnimationComponent anim = entity.get(PamAnimationComponent.class);

        Rectangle bounds = getEntityBounds(entity, posComp);
        float x = bounds.x + bounds.width / 2f;
        float y = bounds.y + bounds.height / 2f;

        float alpha = calculateAlpha(entity, anim);
        float entityScale = calculateEntityScale(entity);
        float currentScaleX = anim.flipX ? -entityScale : entityScale;

        updateZombieVisibility(entity, anim);

        if (!isHiddenByDebuff(entity)) {
            drawPamClip(anim, x, y, currentScaleX, entityScale, alpha);
        }

        if (entity instanceof PlantInstance plant) {
            drawPlantDebuffs(plant, anim, x, y, entityScale);
        }
    }

    private Rectangle getEntityBounds(Entity entity, PositionComponent posComp) {
        return entity instanceof LawnMower mower
            ? lawnMowerBounds(mower, posComp)
            : continuousWorldPositionOf(posComp.position);
    }

    private float calculateAlpha(Entity entity, PamAnimationComponent anim) {
        float alpha = 1f;
        if (entity instanceof ZombieInstance zombie) {
            ZombieDeathComponent death = zombie.get(ZombieDeathComponent.class);
            if (death != null) alpha = death.getAlpha();
        } else if (entity instanceof ZombieLimbs limbs) {
            alpha = limbs.alpha;
        }

        if (anim.tint != null && (anim.tint.r != 1f || anim.tint.g != 1f || anim.tint.b != 1f)) {
            if (!(entity instanceof ZombieLimbs)) {
                alpha = anim.tint.a;
            }
        }
        return alpha;
    }

    private float calculateEntityScale(Entity entity) {
        float entityScale = scale;
        if (entity instanceof SunInstance sun) {
            entityScale *= sunVisualScale(sun.getSunType());
        }
        return entityScale;
    }

    private boolean isHiddenByDebuff(Entity entity) {
        if (entity instanceof PlantInstance plant) {
            return plant.has(SheepedComponent.class) || plant.has(OctoedComponent.class);
        }
        return false;
    }

    private void updateZombieVisibility(Entity entity, PamAnimationComponent anim) {
        if (entity instanceof ZombieInstance zombie) {
            ZombieAnimationLocator.updateArmorVisibility(zombie);
            anim.visibilityMap.put("butter", zombie.has(ButterComponent.class));
            anim.visibilityMap.put("ink", zombie.has(PoisonComponent.class));
        }
    }

    private void drawPamClip(PamAnimationComponent anim, float x, float y, float currentScaleX, float entityScale, float alpha) {
        float r = 1f, g = 1f, b = 1f;
        if (anim.tint != null) {
            r = anim.tint.r;
            g = anim.tint.g;
            b = anim.tint.b;
        }

        Matrix4 original = batch.getTransformMatrix().cpy();
        if (currentScaleX == 1.0f && scale == 1.0f) {
            batch.setColor(r, g, b, alpha);
            pamPlayer.draw(batch, anim.currentClip, anim.stateTime, x, y, anim.isLooping, anim.visibilityMap);
            batch.setColor(1f, 1f, 1f, 1f);
        } else {
            Matrix4 scaled = original.cpy().translate(x, y, 0).scale(currentScaleX, entityScale, 1f).translate(-x, -y, 0);
            batch.setTransformMatrix(scaled);
            batch.setColor(r, g, b, alpha);
            pamPlayer.draw(batch, anim.currentClip, anim.stateTime, x, y, anim.isLooping, anim.visibilityMap);
            batch.setColor(1f, 1f, 1f, 1f);
            batch.setTransformMatrix(original);
        }
    }

    private void drawPlantDebuffs(PlantInstance plant, PamAnimationComponent anim, float x, float y, float entityScale) {
        Matrix4 original = batch.getTransformMatrix().cpy();
        Matrix4 scaled = original.cpy().translate(x, y, 0).scale(entityScale, entityScale, 1f).translate(-x, -y, 0);

        if (plant.has(SheepedComponent.class)) {
            drawSpecificPlantDebuff(SHEEP_PAM, "idle", anim.stateTime, x, y, scaled, original);
        } else if (plant.has(OctoedComponent.class)) {
            drawSpecificPlantDebuff(OCTOPUS_PAM, "animation3", anim.stateTime, x, y, scaled, original);
        }

        PlantFreezeComponent freeze = plant.get(PlantFreezeComponent.class);
        if (freeze != null && freeze.freezeLayers > 0) {
            String freezePam = freeze.freezeLayers == 1 ? CHILL_PAM : FREEZE_PAM;
            String freezeClip = freeze.freezeLayers == 1 ? "chill_stage1" : "freeze_idle";
            drawSpecificPlantDebuff(freezePam, freezeClip, anim.stateTime, x, y, scaled, original);
        }
    }

    private void drawSpecificPlantDebuff(String pamPath, String clipName, float stateTime, float x, float y, Matrix4 scaled, Matrix4 original) {
        ClipRef clip = pamPlayer.getClip(pamPath, clipName);
        if (clip != null) {
            batch.setTransformMatrix(scaled);
            pamPlayer.draw(batch, clip, stateTime, x, y, true, null);
            batch.setTransformMatrix(original);
        }
    }

    private void renderZombieProjectiles(float delta) {
        for (Entity entity : field.getEntitiesWith(PositionComponent.class)) {
            if (entity instanceof AbstractZombieProjectile zp) {
                float time = zombieProjectileTimes.compute(zp, (k, v) -> (v == null ? 0f : v) + delta);
                PositionComponent posComp = zp.get(PositionComponent.class);
                Rectangle bounds = continuousWorldPositionOf(posComp.position);
                float x = bounds.x + bounds.width / 2f;
                float y = bounds.y + bounds.height / 2f;

                ClipRef clip = getZombieProjectileClip(zp);
                if (clip != null) {
                    Matrix4 original = batch.getTransformMatrix().cpy();
                    Matrix4 scaled = original.cpy().translate(x, y, 0).scale(scale, scale, 1f).translate(-x, -y, 0);
                    batch.setTransformMatrix(scaled);
                    batch.setColor(1f, 1f, 1f, 1f);
                    pamPlayer.draw(batch, clip, time, x, y, true, null);
                    batch.setTransformMatrix(original);
                }
            }
        }
        zombieProjectileTimes.keySet().removeIf(Entity::isMarkedForRemoval);
    }

    private ClipRef getZombieProjectileClip(AbstractZombieProjectile zp) {
        if (zp instanceof BoneProjectile) return pamPlayer.getClip(SUN_BOMB_PAM, "animation");
        if (zp instanceof SnowballProjectile) return pamPlayer.getClip(ELECTROBALL_PAM, "animation2");
        if (zp instanceof OctopusProjectile) return pamPlayer.getClip(OCTOPUS_PAM, "animation");
        return null;
    }

    private @NonNull Rectangle lawnMowerBounds(@NonNull LawnMower mower, @NonNull PositionComponent positionComponent) {
        if (!mower.isTriggered()) {
            return mower.getWorldBounds();
        }
        Rectangle idle = mower.getWorldBounds();
        Rectangle moving = continuousWorldPositionOf(positionComponent.position);
        return new Rectangle(moving.x, idle.y + idle.height / 2f, 0f, 0f);
    }

    private ClipRef getSafeClip(String pamPath, String preferred) {
        ClipRef clip = pamPlayer.getClip(pamPath, preferred);
        if (clip != null) return clip;
        List<String> available = pamPlayer.clips(pamPath);
        if (available != null && !available.isEmpty()) {
            return pamPlayer.getClip(pamPath, available.get(0));
        }
        return null;
    }

    private void assignObstacleClip(PamAnimationComponent anim, AbstractObstacle obstacle) {
        if (obstacle instanceof Glacier) {
            anim.currentClip = getSafeClip(GLACIER_PAM, "idle");
            anim.isLooping = true;
        } else if (obstacle instanceof ArcadeMachine) {
            anim.currentClip = getSafeClip(ARCADE_PAM, "idle");
            anim.isLooping = true;
        } else if (obstacle instanceof Redirector redirector) {
            String pam = redirector.direction == Redirector.Direction.UP ? REDIRECTOR_UP_PAM : REDIRECTOR_DOWN_PAM;
            anim.currentClip = getSafeClip(pam, "idle");
            anim.isLooping = true;
        } else if (obstacle instanceof Grave) {
            anim.currentClip = getSafeClip(GRAVE_PAM, "undamaged");
            anim.isLooping = true;
        }
    }

    private void assignSunClip(@NonNull PamAnimationComponent anim) {
        ClipRef clip = SunAnimationLocator.loadClip(pamPlayer);
        if (clip == null) return;
        anim.currentClip = clip;
        anim.isLooping = true;
    }

    private void assignLawnMowerClip(@NonNull PamAnimationComponent anim) {
        ClipRef clip = LawnMowerAnimationLocator.loadClip(pamPlayer);
        if (clip == null) return;
        anim.currentClip = clip;
        anim.isLooping = true;
    }

    private void advanceSequence(@NonNull PamAnimationComponent anim) {
        if (anim.isLooping || anim.currentClip == null || anim.upcomingClips.isEmpty()) return;
        if (anim.stateTime < anim.currentClip.duration) return;

        anim.currentClip = anim.upcomingClips.poll();
        anim.stateTime = 0f;
        anim.isLooping = anim.upcomingClips.isEmpty();
    }

    private @NonNull Rectangle continuousWorldPositionOf(@NonNull Vec2d gridPosition) {
        Rectangle origin = mapData.cellBounds[0][0];
        float columnStep = mapData.cellBounds[0].length > 1
            ? mapData.cellBounds[0][1].x - origin.x
            : origin.width;
        float laneStep = mapData.cellBounds.length > 1
            ? mapData.cellBounds[1][0].y - origin.y
            : origin.height;

        float centerX = origin.x + origin.width / 2f + gridPosition.getX() * columnStep;
        float centerY = origin.y + origin.height / 2f + gridPosition.getY() * laneStep;
        return new Rectangle(centerX, centerY, 0f, 0f);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
