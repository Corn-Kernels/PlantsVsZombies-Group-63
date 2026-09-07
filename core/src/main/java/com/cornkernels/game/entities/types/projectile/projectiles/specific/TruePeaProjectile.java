package com.cornkernels.game.entities.types.projectile.projectiles.specific;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.DamageComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
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
    public int heat;

    public String pamPath;
    public String clipName;

    public TruePeaProjectile(int damage, Vec2d startPosition) {
        this(damage, startPosition, 0, 0);
    }

    public TruePeaProjectile(int damage, Vec2d startPosition, int heat) {
        this(damage, startPosition, heat, 200);
    }

    public TruePeaProjectile(int damage, Vec2d startPosition, int heat, int chillDurationTicks) {
        // AbstractProjectile already adds the PamAnimationComponent[cite: 26]
        super(damage, new Vec2d(PEA_SPEED, 0), startPosition);
        this.heat = heat;
        this.chillDurationTicks = chillDurationTicks;
        updateVisuals();
    }

    private void updateVisuals() {
        int damage = this.get(DamageComponent.class).amount;
        int effectiveDamage = (heat == 1) ? damage * 2 : damage;

        this.clipName = effectiveDamage > 20 ? "animation3" : "animation";

        if (heat == 1) {
            this.pamPath = "768/INITIAL/EFFECTS/T_FIRE_PEA/T_FIRE_PEA.PAM";
        } else if (heat == -1) {
            this.pamPath = "768/INITIAL/EFFECTS/T_SNOW_PEA/T_SNOW_PEA.PAM";
        } else {
            this.pamPath = "768/INITIAL/EFFECTS/T_PEA_PROJECTILE/T_PEA_PROJECTILE.PAM";
        }

        PamAnimationComponent pamAnim = this.get(PamAnimationComponent.class);
        if (pamAnim != null) {
            // Nulling the clip forces PamRenderSystem to reload the updated path/clip
            pamAnim.currentClip = null;
        }
    }

    public void checkTorchwood(Field field) {
        if (this.heat == 1) return; // Already fiery, save CPU

        GridPosition gridPos = GridPosition.fromContinuous(this.get(PositionComponent.class).position);
        PlantInstance plant = field.getPlantAt(gridPos.lane(), gridPos.column());

        if (plant != null) {
            PlantDefComponent defComp = plant.get(PlantDefComponent.class);
            // ID ending in 52 is Torchwood, 18 is Fire Peashooter
            if (defComp != null && (defComp.def().getId() % 1000 == 52 || defComp.def().getId() % 1000 == 18)) {

                // Passing a Snow Pea through fire turns it into a Normal Pea
                if (this.heat == -1) {
                    this.heat = 0;
                } else {
                    this.heat = 1;
                }
                updateVisuals();
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
