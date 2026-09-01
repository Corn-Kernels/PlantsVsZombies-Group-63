package com.cornkernels.game.map;

import com.cornkernels.engine.utility.math.Vec2d;
import com.cornkernels.game.entities.Entity;
import com.cornkernels.game.entities.components.GridPositionComponent;
import com.cornkernels.game.entities.components.PositionComponent;
import com.cornkernels.game.entities.types.effects.PlantFoodEffect;
import com.cornkernels.game.entities.types.lawnmower.LawnMower;
import com.cornkernels.game.entities.types.obstacles.AbstractObstacle;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.projectile.AbstractProjectile;
import com.cornkernels.game.entities.types.projectile.AbstractZombieProjectile;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;
import com.cornkernels.game.map.data.LawnMowerSlot;
import com.cornkernels.game.map.grid.GridObject;
import com.cornkernels.game.map.grid.GridPosition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Field {
    protected GridObject[][] grids;

    protected List<PlantInstance> activePlants;
    protected List<ZombieInstance> activeZombies;
    protected List<SunInstance> activeSuns;
    protected List<AbstractObstacle> activeObstacles;
    protected List<AbstractProjectile> activeProjectiles;
    protected List<AbstractZombieProjectile> activeZombieProjectiles;
    protected List<LawnMower> activeLawnMowers;
    protected int totalLawnMowerCount;
    protected List<PlantFoodEffect> activeEffects;

    protected int totalLanes;
    protected int totalColumns;

    public Field(int lanes, int columns, List<LawnMowerSlot> lawnMowerSlots) {
        this.totalLanes = lanes;
        this.totalColumns = columns;

        this.grids = new GridObject[lanes][columns];
        for (int l = 0; l < lanes; l++) {
            for (int c = 0; c < columns; c++) {
                this.grids[l][c] = new GridObject(new GridPosition(l, c));
            }
        }

        this.activePlants = new ArrayList<>();
        this.activeZombies = new ArrayList<>();
        this.activeSuns = new ArrayList<>();
        this.activeObstacles = new ArrayList<>();
        this.activeProjectiles = new ArrayList<>();
        this.activeLawnMowers = new ArrayList<>(5);
        this.activeZombieProjectiles = new ArrayList<>();
        this.activeEffects = new ArrayList<>();
        initializeLawnMowers(lawnMowerSlots);
    }

    public void update() {
        activePlants.removeIf(PlantInstance::isMarkedForRemoval);
        activeZombies.removeIf(ZombieInstance::isMarkedForRemoval);
        activeSuns.removeIf(SunInstance::isMarkedForRemoval);
        activeProjectiles.removeIf(AbstractProjectile::isMarkedForRemoval);
        activeLawnMowers.removeIf(LawnMower::isMarkedForRemoval);
        activeObstacles.removeIf(AbstractObstacle::isMarkedForRemoval);
        activeEffects.removeIf(PlantFoodEffect::isMarkedForRemoval);

        for (int i = 0; i < totalLanes; i++) {
            for (int j = 0; j < totalColumns; j++) {
                grids[i][j].clearZombies();
            }
        }

        for (ZombieInstance zombie : activeZombies) { // TODO: MIGHT HAVE TO ADD PROJECTILES DYNAMICALLY TOO
            GridPosition pos = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);
            if (pos.lane() < 0 || pos.lane() >= totalLanes || pos.column() < 0 || pos.column() >= totalColumns) {
                continue;
            }
            grids[pos.lane()][pos.column()].addZombie(zombie);
        }

        for (int i = 0; i < totalLanes; i++) {
            for (int j = 0; j < totalColumns; j++) {
                grids[i][j].update();
            }
        }
    }

    public List<Entity> getEntitiesWith(Class<?>... componentTypes) {
        List<Entity> match = new ArrayList<>();

        for (Entity entity : getEntities()) {
            if (entity.hasComponents(componentTypes)) {
                match.add(entity);
            }
        }
        return match;
    }

    public void addPlant(@NotNull PlantInstance plant) {
        GridPosition pos = GridPosition.fromContinuous(plant.get(PositionComponent.class).position);
        activePlants.add(plant);
        grids[pos.lane()][pos.column()].providePlant(plant);
    }

    public void removePlantAt(GridPosition pos) {
        activePlants.stream()
            .filter(p -> p.get(GridPositionComponent.class).position.equals(pos))
            .findFirst().ifPresent(Entity::markForRemoval);
    }

    public void addZombie(ZombieInstance zombie) {
        activeZombies.add(zombie);
    }

    public void addObstacle(AbstractObstacle obstacle, int lane, int column) {
        activeObstacles.add(obstacle);
        grids[lane][column].provideObstacle(obstacle);
    }

    public void addProjectile(AbstractProjectile projectile) {
        activeProjectiles.add(projectile);
    }

    public void addZombieProjectile(AbstractZombieProjectile projectile) {
        activeZombieProjectiles.add(projectile);
    }

    public void addSun(SunInstance sun) {
        activeSuns.add(sun);
    }

    public GridObject getGridObjectAt(int lane, int column) {
        return grids[lane][column];
    }

    public PlantInstance getPlantAt(int lane, int column) {
        if (lane < 0 || lane >= totalLanes || column < 0 || column >= totalColumns) return null;
        PlantInstance plant = grids[lane][column].getPlant();
        if (plant != null && !plant.isMarkedForRemoval()) {
            return plant;
        }
        return null;
    }

    public void removeZombiesInLane(int lane) {
        for (ZombieInstance zombie : activeZombies) {
            GridPosition pos = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);
            if (pos.lane() == lane) {
                zombie.markForRemoval();
            }
        }
    }

    public List<ZombieInstance> getZombiesInLane(int lane) {
        List<ZombieInstance> zombies = new ArrayList<>();
        if (lane > 4 || lane < 0)
            return zombies;
        for (ZombieInstance zombie : activeZombies) {
            GridPosition pos = GridPosition.fromContinuous(zombie.get(PositionComponent.class).position);
            if (pos.lane() == lane) {
                zombies.add(zombie);
            }
        }
        return zombies;
    }

    public LawnMower getLawnMowerAt(int lane) {
        LawnMower lawnMower = null;
        for (LawnMower lm : activeLawnMowers) {
            GridPosition pos = GridPosition.fromContinuous(lm.get(PositionComponent.class).position);
            if (pos.lane() == lane) {
                if (lm.isMarkedForRemoval()) return null;
                lawnMower = lm;
            }
        }
        return lawnMower;
    }

    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        entities.addAll(activePlants);
        entities.addAll(activeZombies);
        entities.addAll(activeProjectiles);
        entities.addAll(activeSuns);
        entities.addAll(activeLawnMowers);
        entities.addAll(activeObstacles);
        entities.addAll(activeZombieProjectiles);
        entities.addAll(activeEffects);
        return entities;
    }

    public void addEffect(@NotNull PlantFoodEffect effect) {
        activeEffects.add(effect);
    }

    public List<PlantFoodEffect> getActiveEffects() {
        return this.activeEffects;
    }

    public List<PlantInstance> getActivePlants() {
        return this.activePlants;
    }

    public List<ZombieInstance> getActiveZombies() {
        return this.activeZombies;
    }

    public List<AbstractProjectile> getActiveProjectiles() {
        return this.activeProjectiles;
    }

    public List<AbstractZombieProjectile> getActiveZombieProjectiles() {
        return this.activeZombieProjectiles;
    }


    public List<AbstractObstacle> getActiveObstacles() {
        return this.activeObstacles;
    }

    public List<SunInstance> getActiveSuns() {
        return this.activeSuns;
    }

    public List<LawnMower> getActiveLawnMowers() {
        return this.activeLawnMowers;
    }

    public int getTotalLawnMowerCount() {
        return this.totalLawnMowerCount;
    }

    public int getTotalLanes() {
        return totalLanes;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    private void initializeLawnMowers(@NotNull List<LawnMowerSlot> lawnMowerSlots) {
        for (LawnMowerSlot slot : lawnMowerSlots) {
            int lane = slot.getLane();
            if (lane < 0 || lane >= totalLanes) continue;

            LawnMower lawnMower = new LawnMower(new Vec2d(0, lane), slot.getBounds());
            activeLawnMowers.add(lawnMower);
            grids[lane][0].provideLawnMower(lawnMower);
            slot.provideLawnMower(lawnMower);
        }
        this.totalLawnMowerCount = activeLawnMowers.size();
    }

}
