package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.plant_specific.OctoedComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.projectile.ZombieProjectiles.OctopusProjectile;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public class OctoZombieBehavior implements ZombieBehavior {

    private final int cooldownTicks;
    private final int windupTicks;
    private final int recoveryTicks;
    private int tickCounter = 0;

    private PlantInstance currentTargetPlant = null;
    private final RandomGenerator rng = RandomGenerator.getDefault();

    public OctoZombieBehavior(float cooldownSeconds, float windupSeconds, float recoverySeconds) {
        this.cooldownTicks = (int) (cooldownSeconds * 20);
        this.windupTicks = (int) (windupSeconds * 20);
        this.recoveryTicks = (int) (recoverySeconds * 20);
    }

    @Override
    public void update(ZombieInstance zombie, Field field) {
        ZombieStateComponent state = zombie.get(ZombieStateComponent.class);
        if (state.state == ZombieStateComponent.State.DEAD) return;

        VelocityComponent vel = zombie.get(VelocityComponent.class);
        ZombieDef def = zombie.get(ZombieDefComponent.class).def();

        if (state.state == ZombieStateComponent.State.ACTION) {
            vel.velocityPerTick = new Vec2d(0, 0); // Lock velocity to prevent sliding

            if (state.stateTicks == windupTicks && currentTargetPlant != null) {
                Vec2d spawnPos = zombie.get(PositionComponent.class).position;
                field.addZombieProjectile(new OctopusProjectile(spawnPos, currentTargetPlant));
            } else if (state.stateTicks > windupTicks + recoveryTicks) {
                state.changeState(ZombieStateComponent.State.WALKING);
                vel.velocityPerTick = new Vec2d(-def.baseSpeed, 0f);
                currentTargetPlant = null;
            }
            return;
        }

        tickCounter++;

        if (tickCounter >= cooldownTicks && state.state == ZombieStateComponent.State.WALKING) {

            // Scan the entire board for valid plants that aren't already Octoed
            List<PlantInstance> validTargets = new ArrayList<>();
            for (PlantInstance plant : field.getActivePlants()) {
                if (!plant.has(OctoedComponent.class)) {
                    validTargets.add(plant);
                }
            }

            if (!validTargets.isEmpty()) {
                // Pick a random plant on the board!
                currentTargetPlant = validTargets.get(rng.nextInt(validTargets.size()));

                state.changeState(ZombieStateComponent.State.ACTION);
                vel.velocityPerTick = new Vec2d(0, 0);
                tickCounter = 0;
            }
        }
    }
}
