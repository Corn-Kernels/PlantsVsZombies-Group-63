package com.cornkernels.game;

import com.cornkernels.game.map.Field;
import com.cornkernels.game.systems.entity.*;
import org.jspecify.annotations.NonNull;
import pvz.libpvz.pam.PamPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

public class GameSimulation {

    private final Field field;
    private final RandomGenerator random;
    private final GameAttributes attributes;
    private final List<EntitySystem> systems = new ArrayList<>();

    public GameSimulation(Field field, @NonNull GameAttributes gameAttributes, PamPlayer pamPlayer) {
        this.field = field;
        this.attributes = gameAttributes;
        this.random = new Random();

        addSystem(new MovementSystem());
        addSystem(new CombatSystem());
        addSystem(new PlantAttackSystem());
        addSystem(new SunSystem(random));
        addSystem(new ZombieSystem(pamPlayer));
        addSystem(new LawnMowersSystem());
        addSystem(new WaveSystem(1, random, gameAttributes.eligibleZombies, pamPlayer));
    }

    public void update(float deltaTick) {
        field.update(deltaTick);
        for (EntitySystem entitySystem : systems) {
            entitySystem.update(deltaTick);
            attributes.update(deltaTick);
        }
    }

    private void addSystem(@NonNull EntitySystem system) {
        system.registerSystem(field);
        systems.add(system);
    }

}
