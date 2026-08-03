package com.cornkernels.game.map.grid;

import com.cornkernels.game.entities.types.lawnmower.LawnMower;
import com.cornkernels.game.entities.types.obstacles.AbstractObstacle;
import com.cornkernels.game.entities.types.plants.PlantInstance;
import com.cornkernels.game.entities.types.sun.SunInstance;
import com.cornkernels.game.entities.types.zombies.ZombieInstance;

import java.util.ArrayList;
import java.util.List;

public class GridObject {

    private final List<ZombieInstance> zombiesInGrid = new ArrayList<>();
    private final GridPosition position;
    private PlantInstance plant;
    private SunInstance sun;
    private AbstractObstacle obstacle;
    private LawnMower lawnMower;

    public GridObject(GridPosition position) {
        this.position = position;
    }

    public GridPosition getPosition() {
        return position;
    }

    public void update() {
        if (plant != null && plant.isMarkedForRemoval()) {
            plant = null;
        }
        if (sun != null && sun.isMarkedForRemoval()) {
            sun = null;
        }
        if (obstacle != null && obstacle.isMarkedForRemoval()) {
            obstacle = null;
        }
        if (lawnMower != null && lawnMower.isMarkedForRemoval()) {
            lawnMower = null;
        }
    }

    public void providePlant(PlantInstance plant) {
        this.plant = plant;
    }

    public void removePlant() {
        plant = null;
    }

    public PlantInstance getPlant() {
        return plant;
    }

    public void provideObstacle(AbstractObstacle obstacle) {
        this.obstacle = obstacle;
    }

    public void removeObstacle() {
        obstacle = null;
    }

    public AbstractObstacle getObstacle() {
        return obstacle;
    }

    public void provideSun(SunInstance sun) {
        this.sun = sun;
    }

    public void removeSun() {
        sun = null;
    }

    public void provideLawnMower(LawnMower lawnMower) {
        this.lawnMower = lawnMower;
    }

    public void removeLawnMower() {
        lawnMower = null;
    }

    public SunInstance getSun() {
        return sun;
    }

    public void addZombie(ZombieInstance zombie) {
        zombiesInGrid.add(zombie);
    }

    public void removeZombieFromGrid(ZombieInstance zombie) {
        zombiesInGrid.remove(zombie);
    }

    public void clearZombies() {
        zombiesInGrid.clear();
    }

    public List<ZombieInstance> getZombiesInGrid() {
        return zombiesInGrid;
    }
}
