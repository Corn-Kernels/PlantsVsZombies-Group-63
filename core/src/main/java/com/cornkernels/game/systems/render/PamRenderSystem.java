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
import com.cornkernels.game.entities.types.obstacles.AbstractObstacle;
import com.cornkernels.game.entities.types.obstacles.ArcadeMachine;
import com.cornkernels.game.entities.types.obstacles.Glacier;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.obstacles.Redirector;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.AbstractZombieProjectile;
import com.cornkernels.game.entities.types.projectile.ZombieProjectiles.BoneProjectile;
import com.cornkernels.game.entities.types.projectile.ZombieProjectiles.OctopusProjectile;
import com.cornkernels.game.entities.types.projectile.ZombieProjectiles.SnowballProjectile;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.entities.types.sun.SunType;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.ZombieLimbs;
import com.cornkernels.game.map.data.MapData;
import com.cornkernels.game.systems.entity.SunSystem;
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

    private final PamPlayer pamPlayer;
    private final MapData mapData;
    private float scale = 0.5f;

    private final Map<Entity, Float> zombieProjectileTimes = new HashMap<>();

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
            PositionComponent positionComponent = entity.get(PositionComponent.class);
            PamAnimationComponent anim = entity.get(PamAnimationComponent.class);

            if (anim.currentClip == null && entity instanceof SunInstance) {
                assignSunClip(anim);
            }
            if (anim.currentClip == null && entity instanceof LawnMower) {
                assignLawnMowerClip(anim);
            }
            if (anim.currentClip == null && entity instanceof AbstractObstacle obstacle) {
                assignObstacleClip(anim, obstacle);
            }
            if (anim.currentClip == null && entity instanceof AbstractProjectile projectile) {
                ProjectileAnimationLocator.assignClip(pamPlayer, anim, projectile);
            }

            anim.stateTime += delta;
            advanceSequence(anim);

            Rectangle bounds = entity instanceof LawnMower mower
                ? lawnMowerBounds(mower, positionComponent)
                : continuousWorldPositionOf(positionComponent.position);

            float x = bounds.x + bounds.width / 2f;
            float y = bounds.y + bounds.height / 2f;

            if (entity instanceof SunInstance sun) {
                y = SunSystem.currentDrawY(sun, y, bounds.height, mapData);
            }

            float alpha = 1f;
            if (entity instanceof ZombieInstance zombie) {
                ZombieDeathComponent death = zombie.get(ZombieDeathComponent.class);
                if (death != null) alpha = death.getAlpha();
            } else if (entity instanceof ZombieLimbs limbs) {
                alpha = limbs.alpha;
            }

            float entityScale = scale;
            if (entity instanceof SunInstance sun) {
                entityScale *= sunVisualScale(sun.getSunType());
            }

            float r = 1f, g = 1f, b = 1f;
            if (anim.tint != null && (anim.tint.r != 1f || anim.tint.g != 1f || anim.tint.b != 1f)) {
                r = anim.tint.r;
                g = anim.tint.g;
                b = anim.tint.b;
                if (!(entity instanceof ZombieLimbs)) {
                    alpha = anim.tint.a;
                }
            }

            float currentScaleX = anim.flipX ? -entityScale : entityScale;
            Matrix4 original = batch.getTransformMatrix().cpy();

            boolean isHiddenByDebuff = false;
            if (entity instanceof PlantInstance plant) {
                if (plant.has(SheepedComponent.class) || plant.has(OctoedComponent.class)) {
                    isHiddenByDebuff = true;
                }
            }

            if (entity instanceof ZombieInstance zombie) {
                ZombieAnimationLocator.updateArmorVisibility(zombie);

                boolean hasButter = zombie.has(ButterComponent.class);
                anim.visibilityMap.put("butter", hasButter);

                boolean hasPoison = zombie.has(PoisonComponent.class);
                anim.visibilityMap.put("ink", hasPoison);
            }

            if (!isHiddenByDebuff) {
                if (currentScaleX == 1.0f && scale == 1.0f) {
                    batch.setColor(r, g, b, alpha);
                    pamPlayer.draw(batch, anim.currentClip, anim.stateTime, x, y, anim.isLooping, anim.visibilityMap);
                    batch.setColor(1f, 1f, 1f, 1f);
                } else {
                    Matrix4 scaled = original.cpy()
                        .translate(x, y, 0)
                        .scale(currentScaleX, entityScale, 1f)
                        .translate(-x, -y, 0);

                    batch.setTransformMatrix(scaled);
                    batch.setColor(r, g, b, alpha);
                    pamPlayer.draw(batch, anim.currentClip, anim.stateTime, x, y, anim.isLooping, anim.visibilityMap);
                    batch.setColor(1f, 1f, 1f, 1f);
                    batch.setTransformMatrix(original);
                }
            }

            if (entity instanceof PlantInstance plant) {
                if (plant.has(SheepedComponent.class)) {
                    ClipRef sheepClip = pamPlayer.getClip(SHEEP_PAM, "idle");
                    if (sheepClip != null) {
                        Matrix4 scaled = original.cpy().translate(x, y, 0).scale(entityScale, entityScale, 1f).translate(-x, -y, 0);
                        batch.setTransformMatrix(scaled);
                        pamPlayer.draw(batch, sheepClip, anim.stateTime, x, y, true, null);
                        batch.setTransformMatrix(original);
                    }
                }
                else if (plant.has(OctoedComponent.class)) {
                    ClipRef octopusClip = pamPlayer.getClip(OCTOPUS_PAM, "animation3");
                    if (octopusClip != null) {
                        Matrix4 scaled = original.cpy().translate(x, y, 0).scale(entityScale, entityScale, 1f).translate(-x, -y, 0);
                        batch.setTransformMatrix(scaled);
                        pamPlayer.draw(batch, octopusClip, anim.stateTime, x, y, true, null);
                        batch.setTransformMatrix(original);
                    }
                }

                PlantFreezeComponent freeze = plant.get(PlantFreezeComponent.class);
                if (freeze != null && freeze.freezeLayers > 0) {
                    ClipRef freezeClip = null;
                    if (freeze.freezeLayers == 1) {
                        freezeClip = pamPlayer.getClip(CHILL_PAM, "chill_stage1");
                    } else if (freeze.freezeLayers >= 2) {
                        freezeClip = pamPlayer.getClip(FREEZE_PAM, "freeze_idle");
                    }

                    if (freezeClip != null) {
                        Matrix4 scaled = original.cpy().translate(x, y, 0).scale(entityScale, entityScale, 1f).translate(-x, -y, 0);
                        batch.setTransformMatrix(scaled);
                        pamPlayer.draw(batch, freezeClip, anim.stateTime, x, y, true, null);
                        batch.setTransformMatrix(original);
                    }
                }
            }
        }

        // Render Zombie Projectiles directly on top without adding PamAnimationComponent
        for (Entity entity : field.getEntitiesWith(PositionComponent.class)) {
            if (entity instanceof AbstractZombieProjectile zp) {
                float time = zombieProjectileTimes.compute(zp, (k, v) -> (v == null ? 0f : v) + delta);

                PositionComponent posComp = zp.get(PositionComponent.class);
                Rectangle bounds = continuousWorldPositionOf(posComp.position);
                float x = bounds.x + bounds.width / 2f;
                float y = bounds.y + bounds.height / 2f;

                ClipRef clip = null;
                if (zp instanceof BoneProjectile) {
                    clip = pamPlayer.getClip(SUN_BOMB_PAM, "animation");
                } else if (zp instanceof SnowballProjectile) {
                    clip = pamPlayer.getClip(ELECTROBALL_PAM, "animation2");
                } else if (zp instanceof OctopusProjectile) {
                    clip = pamPlayer.getClip(OCTOPUS_PAM, "animation");
                }

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
