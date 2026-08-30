// WizardBehavior.java
package com.cornkernels.game.entities.types.zombies.behavior.behaviors;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.components.VelocityComponent;
import com.cornkernels.game.entities.components.plant_specific.OctoedComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFreezeComponent;
import com.cornkernels.game.entities.components.plant_specific.SheepedComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieDefComponent;
import com.cornkernels.game.entities.components.zombie_specific.ZombieStateComponent;
import com.cornkernels.game.entities.components.zombie_specific.specific_specific.WizardComponent;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.entities.types.zombies.behavior.ZombieBehavior;
import com.cornkernels.game.map.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public class WizardBehavior implements ZombieBehavior {

    private final int cooldownTicks;
    private final int windupTicks;
    private final int recoveryTicks;
    private final RandomGenerator rng = RandomGenerator.getDefault();

    public WizardBehavior(float cooldownSeconds, float windupSeconds, float recoverySeconds) {
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

        WizardComponent comp = zombie.get(WizardComponent.class);
        if (comp == null) {
            comp = new WizardComponent();
            zombie.add(comp);
        }

        if (state.state == ZombieStateComponent.State.ACTION) {
            vel.velocityPerTick = new Vec2d(0, 0);

            if (state.stateTicks == windupTicks && comp.currentTargetPlant != null) {
                if (!comp.currentTargetPlant.isMarkedForRemoval() && !comp.currentTargetPlant.has(SheepedComponent.class)) {
                    comp.currentTargetPlant.add(new SheepedComponent(zombie));
                }
            } else if (state.stateTicks > windupTicks + recoveryTicks) {
                state.changeState(ZombieStateComponent.State.WALKING);
                vel.velocityPerTick = new Vec2d(-def.baseSpeed, 0f);
                comp.currentTargetPlant = null;
            }
            return;
        }

        comp.tickCounter++;

        if (comp.tickCounter >= cooldownTicks && state.state == ZombieStateComponent.State.WALKING) {
            List<PlantInstance> rightMostPlants = new ArrayList<>();
            List<PlantInstance> secondRightPlants = new ArrayList<>();

            int rightMostCol = -1;
            int secondRightCol = -1;

            for (int col = field.getTotalColumns() - 1; col >= 0; col--) {
                List<PlantInstance> validInCol = getValidPlantsInColumn(field, col);
                if (!validInCol.isEmpty()) {
                    if (rightMostCol == -1) {
                        rightMostCol = col;
                        rightMostPlants = validInCol;
                    } else if (secondRightCol == -1) {
                        secondRightCol = col;
                        secondRightPlants = validInCol;
                        break;
                    }
                }
            }

            List<PlantInstance> chosenList = null;

            if (!rightMostPlants.isEmpty() && !secondRightPlants.isEmpty()) {
                if (rng.nextDouble() < 0.75) {
                    chosenList = rightMostPlants;
                } else {
                    chosenList = secondRightPlants;
                }
            } else if (!rightMostPlants.isEmpty()) {
                chosenList = rightMostPlants;
            }

            if (chosenList != null && !chosenList.isEmpty()) {
                comp.currentTargetPlant = chosenList.get(rng.nextInt(chosenList.size()));
                state.changeState(ZombieStateComponent.State.ACTION);
                vel.velocityPerTick = new Vec2d(0, 0);
                comp.tickCounter = 0;
            }
        }
    }

    private List<PlantInstance> getValidPlantsInColumn(Field field, int col) {
        List<PlantInstance> valid = new ArrayList<>();
        for (int lane = 0; lane < field.getTotalLanes(); lane++) {
            PlantInstance plant = field.getPlantAt(lane, col);
            if (plant != null) {
                PlantFreezeComponent freeze = plant.get(PlantFreezeComponent.class);
                boolean isFrozen = freeze != null && freeze.frozenHp > 0;

                if (!plant.has(SheepedComponent.class) && !plant.has(OctoedComponent.class) && !isFrozen) {
                    valid.add(plant);
                }
            }
        }
        return valid;
    }
}
