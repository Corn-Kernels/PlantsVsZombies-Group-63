package com.cornkernels.game.entities.types.zombies;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.utility.ZombieAnimationLocator;
import pvz.libpvz.pam.PamPlayer;

public class ZombieLimbs extends Entity {

    private static final float GRAVITY = -0.01f;
    private static final float WAIT_DURATION = 10f;
    private static final float FADE_DURATION = 8f;
    private final float rotationSpeed;
    private final float groundY;
    public float rotation;
    public float alpha = 1.0f;
    private float velocityY;
    private float velocityX;
    private boolean hasLanded = false;
    private float waitTimer = 0f;
    private float fadeTimer = 0f;

    public ZombieLimbs(LimbType type, Vec2d spawnPosition, float groundY, ZombieDef def, PamPlayer pamPlayer) {
        super();
        if (type == LimbType.HEAD)
            this.groundY = groundY - 0.8f;
        else
            this.groundY = groundY - 0.4f;
        add(new PositionComponent(new Vec2d(spawnPosition.getX(), spawnPosition.getY())));
        this.velocityY = type.initialYVelocity;
        this.velocityX = (float) (Math.random() * 0.06 - 0.03);
        this.rotation = 0f;
        this.rotationSpeed = (float) (Math.random() * 0.2 - 0.1);

        PamAnimationComponent pamAnim = new PamAnimationComponent();
        add(pamAnim);
        ZombieAnimationLocator.applyClip(pamPlayer, pamAnim, def, "particles");

        if (type == LimbType.HEAD) {
            pamAnim.visibilityMap.put("particle_head", true);
            pamAnim.visibilityMap.put("particle_arm", false);
        } else if (type == LimbType.ARM) {
            pamAnim.visibilityMap.put("particle_arm", true);
            pamAnim.visibilityMap.put("particle_head", false);
        }
    }

    public void update(float deltaTick) {
        deltaTick *= 20;
        PositionComponent posComp = get(PositionComponent.class);
        if (posComp == null) return;

        if (!hasLanded) {
            velocityY += GRAVITY * deltaTick;
            float newX = posComp.position.getX() + (velocityX * deltaTick);
            float newY = posComp.position.getY() + (velocityY * deltaTick);

            if (newY <= groundY) {
                newY = groundY;
                hasLanded = true;
            }
            posComp.position = new Vec2d(newX, newY);
            rotation += rotationSpeed * deltaTick;
        } else {
            waitTimer += deltaTick;
            if (waitTimer >= WAIT_DURATION) {
                fadeTimer += deltaTick;
                alpha = 1.0f - (fadeTimer / FADE_DURATION);

                if (alpha <= 0f) {
                    markForRemoval();
                }
            }
        }
    }

    public enum LimbType {
        HEAD("particle_head", 0.07f),
        ARM("particle_arm", 0.05f);

        public final String nodeName;
        public final float initialYVelocity;

        LimbType(String nodeName, float initialYVelocity) {
            this.nodeName = nodeName;
            this.initialYVelocity = initialYVelocity;
        }
    }
}
