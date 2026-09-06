package com.cornkernels.game.entities.types.projectile.ZombieProjectiles;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.obstacles.Grave;
import com.cornkernels.game.entities.types.projectile.AbstractZombieProjectile;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;
import org.jspecify.annotations.NonNull;

public class BoneProjectile extends AbstractZombieProjectile {

    private final GridPosition targetTile;
    private final double targetX;

    public BoneProjectile(Vec2d startPosition, @NonNull GridPosition targetTile) {
        // Moves left at 2.0 speed, 0 damage (it just spawns a grave)
        super(0, new Vec2d(-0.5f, 0), startPosition);
        this.targetTile = targetTile;
        this.targetX = targetTile.column(); // Center of the target column
    }

    @Override
    public boolean hit(@NonNull Entity target, Field field) {
        Vec2d currentPos = get(PositionComponent.class).position;

        // Since it travels left, it hits when its X is less than or equal to the target X
        if (currentPos.getX() <= targetX + 0.3 && currentPos.getX() >= targetX - 0.3) {

            // Ensure the tile is still empty (no plant was placed while the bone was flying)
            if (field.getPlantAt(targetTile.lane(), targetTile.column()) == null) {
                Grave grave = new Grave(new Vec2d(targetTile.column(), targetTile.lane()));
                field.addObstacle(grave, targetTile.lane(), targetTile.column());
            }

            return true; // Mark for removal
        }
        return false;
    }

    public GridPosition getTargetTile() {
        return targetTile;
    }
}
