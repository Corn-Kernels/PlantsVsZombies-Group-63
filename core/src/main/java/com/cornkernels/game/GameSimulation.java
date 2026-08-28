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
    private final LawnMowersSystem lawnMowersSystem;

    public GameSimulation(Field field, @NonNull GameAttributes gameAttributes, PamPlayer pamPlayer) {
        this.field = field;
        this.attributes = gameAttributes;
        this.random = new Random();
        this.lawnMowersSystem = new LawnMowersSystem();

        addSystem(new CombatSystem());
        addSystem(new PlantAttackSystem(pamPlayer));
        addSystem(new SunSystem(random));
        addSystem(new ZombieSystem(pamPlayer));
        addSystem(lawnMowersSystem);
        addSystem(new WaveSystem(1, random, gameAttributes.eligibleZombies, pamPlayer));
        addSystem(new DebuffSystem());
        addSystem(new MovementSystem());

    }

    public void update(float deltaTick) {
        field.update();
        for (EntitySystem entitySystem : systems) {
            entitySystem.update(deltaTick);
        }
        attributes.update(deltaTick);
    }

    public boolean isGameLost() {
        return lawnMowersSystem.isGameLost();
    }

    private void addSystem(@NonNull EntitySystem system) {
        system.registerSystem(field);
        systems.add(system);
    }

}
