package com.cornkernels.game.entities.types.projectile.projectiles.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.debuffs.IceComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.projectiles.AreaOfDamage;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.grid.GridPosition;
import com.cornkernels.game.systems.entity.CombatSystem;
import org.jspecify.annotations.NonNull;

public class TruePeaProjectile extends AbstractProjectile {

    private static final float PEA_SPEED = 1f;
    public final int chillDurationTicks;
    public int heat; // Removed 'final' so Torchwood can ignite it

    public TruePeaProjectile(int damage, Vec2d startPosition) {
        this(damage, startPosition, 0, 0);
    }

    public TruePeaProjectile(int damage, Vec2d startPosition, int heat) {
        this(damage, startPosition, heat, 200);
    }

    public TruePeaProjectile(int damage, Vec2d startPosition, int heat, int chillDurationTicks) {
        super(damage, new Vec2d(PEA_SPEED, 0), startPosition);
        this.heat = heat;
        this.chillDurationTicks = chillDurationTicks;
    }

    public void checkTorchwood(Field field) {
        if (this.heat == 1) return; // Already fiery, save CPU

        GridPosition gridPos = GridPosition.fromContinuous(this.get(PositionComponent.class).position);
        PlantInstance plant = field.getPlantAt(gridPos.lane(), gridPos.column());

        if (plant != null) {
            PlantDefComponent defComp = plant.get(PlantDefComponent.class);
            if (defComp != null && defComp.def().getId() % 1000 == 52) {
                this.heat = 1; // Instant transition to fiery!
            }
        }
    }

    @Override
    public boolean hit(@NonNull Entity target, Field field) {
        if (super.hit(target, field)) {
            int baseDamage = this.get(DamageComponent.class).amount;

            if (heat == 1) {
                int finalDamage = baseDamage * 2;
                int splashDamage = (int) Math.ceil(baseDamage * 0.20);

                CombatSystem.applyDamage(target, finalDamage - splashDamage, false, field);

                if (target.has(IceComponent.class)) target.get(IceComponent.class).melt();
                field.addProjectile(new AreaOfDamage(this.get(PositionComponent.class).position, 0.3f, splashDamage, true, 0));

            } else if (heat == -1) {
                CombatSystem.applyDamage(target, baseDamage, false, field);
                if (target.has(IceComponent.class)) target.get(IceComponent.class).applyChill(chillDurationTicks);

            } else {
                CombatSystem.applyDamage(target, baseDamage, false, field);
            }
            return true;
        }
        return false;
    }

    @Override
    public AbstractProjectile clone(Vec2d newPosition) {
        return new TruePeaProjectile(this.get(DamageComponent.class).amount, newPosition, this.heat, this.chillDurationTicks);
    }
}
