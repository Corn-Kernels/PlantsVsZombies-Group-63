package com.cornkernels.game.systems.controller.plants;

import com.badlogic.gdx.Gdx;
import com.cornkernels.engine.renderer.camera.GameplayCamera;
import com.cornkernels.engine.utility.InputSnapshot;
import com.cornkernels.game.entities.components.HealthComponent;
import com.cornkernels.game.entities.components.PamAnimationComponent;
import com.cornkernels.game.entities.components.plant_specific.PlantFoodComponent;
import com.cornkernels.game.entities.types.plants.PlantCategory;
import com.cornkernels.game.entities.types.plants.PlantDef;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.plants.behavior.PlantAttackBehaviors;
import com.cornkernels.game.entities.types.plants.behavior.behaviors.TrapExplosiveBehavior;
import com.cornkernels.game.hud.HighlightAnimationSet;
import com.cornkernels.game.hud.cursor.CursorAttachment;
import com.cornkernels.game.hud.cursor.CursorToolController;
import com.cornkernels.game.hud.cursor.CursorToolState;
import com.cornkernels.game.map.Field;
import com.cornkernels.game.map.data.MapData;
import com.cornkernels.game.map.grid.GridObject;
import com.cornkernels.game.utility.PlantAnimationLocator;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;

public class PlantingController {

    private final CursorToolState toolState = new CursorToolState();

    private final CursorToolController toolController;
    private final Field field;
    private final PamPlayer pamPlayer;

    private int currentSun;

    public PlantingController(Field field, MapData mapData, GameplayCamera camera, int startingSun,
                              PamPlayer pamPlayer) {
        this.field = field;
        this.toolController = new CursorToolController(mapData, camera, toolState);
        this.pamPlayer = pamPlayer;
        this.currentSun = startingSun;
    }

    public void update(float delta, InputSnapshot inputSnapshot) {
        toolController.update(inputSnapshot);
    }

    public void addSun(int amount) {
        currentSun = Math.clamp(currentSun + amount, 0, 5000);
    }

    public int getCurrentSun() {
        return currentSun;
    }

    public boolean canAfford(int sunCost) {
        return currentSun >= sunCost;
    }

    public void beginPlantPlacement(@NonNull SeedSlot slot, CursorAttachment thumbnail,
                                    HighlightAnimationSet highlightSet, Runnable onPlanted) {
        int sunCost = slot.getPlantDef().getCost();
        toolState.active = true;
        toolState.attachment = thumbnail;
        toolState.highlightSet = highlightSet;
        toolState.eligibility = cell -> {
            GridObject fieldCell = field.getGridObjectAt(cell.getPosition().lane(), cell.getPosition().column());
            return fieldCell.getPlant() == null && fieldCell.getObstacle() == null;
        };
        toolState.onConfirm = gridPosition -> {
            if (field.getPlantAt(gridPosition.lane(), gridPosition.column()) == null) {
                PlantDef plantDef = slot.getPlantDef();
                if (plantDef != null) {
                    PlantInstance plant = new PlantInstance(plantDef, gridPosition);
                    applyIdleAnimation(plant, plantDef);
                    wireDamageStageAnimation(plant, plantDef);
                    field.addPlant(plant);
                    currentSun -= sunCost;
                    toolState.active = false;

                    // Boost is a one-time-use perk: the first plant placed from a boosted slot
                    // gets Plant Food applied immediately for free, then the boost is spent (its
                    // seed packet reverts to its default texture on its own, since that's driven
                    // live off SeedSlot.isBoosted()).
                    if (slot.isBoosted()) {
                        PlantFoodComponent pf = plant.get(PlantFoodComponent.class);
                        if (pf != null) {
                            pf.activate();
                        }
                        slot.setBoosted(false);
                    }

                    if (onPlanted != null) onPlanted.run();
                } else {
                    throw new NullPointerException("plantDef is null.");
                }
            }
        };
    }

    private void applyIdleAnimation(@NonNull PlantInstance plant, @NonNull PlantDef plantDef) {
        String pamPath = PlantAnimationLocator.findPamPath(plantDef);
        if (pamPath == null) {
            Gdx.app.error("PlantingController", "No PAM animation found for " + plantDef);
            return;
        }
        if (PlantAttackBehaviors.get(plantDef) instanceof TrapExplosiveBehavior) {
            PlantAnimationLocator.applyClip(pamPlayer, plant.get(PamAnimationComponent.class), plantDef, "plant_idle");
            return;
        }
        PlantAnimationLocator.applySpawnAnimation(pamPlayer, plant.get(PamAnimationComponent.class), pamPath);
    }

    private void wireDamageStageAnimation(@NonNull PlantInstance plant, @NonNull PlantDef plantDef) {
        if (plantDef.getCategory() != PlantCategory.WALL_NUT) return;
        HealthComponent health = plant.get(HealthComponent.class);
        if (health == null) return;

        health.addListener(new HealthComponent.OnHealthChangedListener() {
            @Override
            public void OnHealthChanged(int currentHealth, int maxHealth, int delta) {
                float fraction = maxHealth > 0 ? (float) currentHealth / maxHealth : 1f;
                PlantAnimationLocator.applyDamageStageClip(pamPlayer, plant.get(PamAnimationComponent.class), plantDef, fraction);
            }

            @Override
            public void onMaxHealthChanged(int maxHealth, int delta) {
            }
        });
    }

    public void toggleShovel(CursorAttachment shovelIcon) {
        if (toolState.active) {
            toolState.active = false;
            toolState.attachment = null;
            toolState.highlightSet = null;
            toolState.eligibility = null;
            toolState.onConfirm = null;
        } else {
            toolState.active = true;
            toolState.attachment = shovelIcon;
            toolState.highlightSet = null;
            toolState.eligibility = cell ->
                field.getPlantAt(cell.getPosition().lane(), cell.getPosition().column()) != null;
            toolState.onConfirm = gridPosition -> {
                field.removePlantAt(gridPosition);
                toolState.active = false;
            };
        }

    }

    public void togglePlantFoodTool(CursorAttachment leafIcon, Runnable onUsed) {
        if (toolState.active) {
            toolState.active = false;
            toolState.attachment = null;
            toolState.highlightSet = null;
            toolState.eligibility = null;
            toolState.onConfirm = null;
        } else {
            toolState.active = true;
            toolState.attachment = leafIcon;
            toolState.highlightSet = null;
            toolState.eligibility = cell -> {
                PlantInstance plant = field.getPlantAt(cell.getPosition().lane(), cell.getPosition().column());
                if (plant == null) return false;
                PlantFoodComponent pf = plant.get(PlantFoodComponent.class);
                return pf != null && !pf.isActive();
            };
            toolState.onConfirm = gridPosition -> {
                PlantInstance plant = field.getPlantAt(gridPosition.lane(), gridPosition.column());
                if (plant != null) {
                    PlantFoodComponent pf = plant.get(PlantFoodComponent.class);
                    if (pf != null && !pf.isActive()) {
                        pf.activate();
                        if (onUsed != null) onUsed.run();
                    }
                }
                toolState.active = false;
            };
        }
    }

    public void cancelActiveTool() {
        toolState.active = false;
    }

    public CursorToolState getToolState() {
        return toolState;
    }
}
