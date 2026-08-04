package com.cornkernels.game;

import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

public class GameSimulation {

    private final Field field;
    private final RandomGenerator random;
    private List<EntitySystem> systems = new ArrayList<>();

    public GameSimulation(Field field, GameAttributes gameAttributes) {
        this.field = field;
        this.random = new Random();

        addSystem(new MovementSystem());
        addSystem(new CombatSystem());
        addSystem(new PlantAttackSystem());
        addSystem(new SunSystem(random));
        addSystem(new LawnMowersSystem());
        addSystem(new WaveSystem(1, random, gameAttributes.eligibleZombies));
    }

    public void update(float deltaTick) {
        for (EntitySystem entitySystem : systems) {
            entitySystem.update(deltaTick);
        }
    }

    private void addSystem(EntitySystem system) {
        system.registerSystem(field);
        systems.add(system);
    }

}
