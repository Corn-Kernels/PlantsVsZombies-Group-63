package com.cornkernels.game.systems.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.lawnmower.LawnMower;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.data.MapData;
import com.cornkernels.game.systems.entity.SunSystem;
import com.cornkernels.game.utility.LawnMowerAnimationLocator;
import com.cornkernels.game.utility.ProjectileAnimationLocator;
import com.cornkernels.game.utility.SunAnimationLocator;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.ClipRef;
import pvz.libpvz.pam.PamPlayer;

import java.util.Comparator;
import java.util.List;

public class PamRenderSystem extends RenderSystem {

    private final PamPlayer pamPlayer;
    private final MapData mapData;
    private float scale = 0.5f;

    public PamRenderSystem(SpriteBatch batch, PamPlayer pamPlayer, MapData mapData) {
        super(batch);
        this.pamPlayer = pamPlayer;
        this.mapData = mapData;
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
            if (anim.currentClip == null && entity instanceof AbstractProjectile projectile) {
                ProjectileAnimationLocator.assignClip(pamPlayer, anim, projectile);
            }

            anim.stateTime += delta;
            advanceSequence(anim);

            Rectangle bounds = entity instanceof LawnMower mower
                ? mower.getWorldBounds()
                : entity instanceof AbstractProjectile || entity instanceof ZombieInstance
                ? continuousWorldPositionOf(positionComponent.position)
                : worldCellOf(positionComponent.position);
            float x = bounds.x + bounds.width / 2f;
            float y = bounds.y + bounds.height / 2f;

            if (entity instanceof SunInstance sun) {
                y = SunSystem.currentDrawY(sun, y, bounds.height, mapData);
            }

            if (scale == 1.0f) {
                pamPlayer.draw(batch, anim.currentClip, anim.stateTime, x, y, anim.isLooping, anim.visibilityMap);
                continue;
            }

            Matrix4 original = batch.getTransformMatrix().cpy();
            Matrix4 scaled = original.cpy()
                .translate(x, y, 0)
                .scale(scale, scale, 1f)
                .translate(-x, -y, 0);

            batch.setTransformMatrix(scaled);
            pamPlayer.draw(batch, anim.currentClip, anim.stateTime, x, y, anim.isLooping, anim.visibilityMap);
            batch.setTransformMatrix(original);
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

    private @NonNull Rectangle worldCellOf(@NonNull Vec2d gridPosition) {
        int lane = Math.clamp(Math.round(gridPosition.getY()), 0, mapData.cellBounds.length - 1);
        Rectangle[] row = mapData.cellBounds[lane];
        int maxColumn = row.length - 1;

        float column = Math.clamp(gridPosition.getX(), 0f, (float) maxColumn);
        int floorColumn = (int) Math.floor(column);
        int ceilColumn = Math.min(floorColumn + 1, maxColumn);
        float t = column - floorColumn;

        Rectangle from = row[floorColumn];
        Rectangle to = row[ceilColumn];
        float centerX = MathUtils.lerp(from.x + from.width / 2f, to.x + to.width / 2f, t);
        float width = MathUtils.lerp(from.width, to.width, t);

        return new Rectangle(centerX - width / 2f, from.y, width, from.height);
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
