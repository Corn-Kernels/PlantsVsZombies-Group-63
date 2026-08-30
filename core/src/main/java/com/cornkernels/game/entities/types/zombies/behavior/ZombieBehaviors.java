package com.cornkernels.game.entities.types.zombies.behavior;

import com.cornkernels.game.entities.types.zombies.ZombieDef;
import com.cornkernels.game.entities.types.zombies.behavior.behaviors.*;

import java.util.EnumMap;
import java.util.Map;

public class ZombieBehaviors {

    private static final Map<ZombieDef, ZombieBehavior> REGISTRY = new EnumMap<>(ZombieDef.class);

    static {
        register(ZombieDef.GARGANTUAR, new CustomMeleeBehavior(40, 10, 10000));
        register(ZombieDef.EXPLORER, new CustomMeleeBehavior(1, 0, 10000));
        // King Zombie: Promotes a peasant every 3.5 seconds
        register(ZombieDef.DARK_KING, new KingBehavior(3.5f));
        // Cooldown 7s, Windup 1s, Recovery 0.5s
        register(ZombieDef.TOMB_RAISER, new TombRaiserBehavior(7.0f, 1.0f, 0.5f));
        // Dodo: Walks for 12 seconds, then flies over plants for 12 seconds at half speed
        register(ZombieDef.ICE_AGE_DODO, new DodoBehavior(12.0f, 12.0f, 0.5f));
        // Hunter Zombie: Throws a snowball every 6 seconds
        register(ZombieDef.ICE_AGE_HUNTER, new HunterBehavior(6.0f));
        // Wizard: Casts a sheep spell every 6 seconds (1s windup, 0.5s recovery)
        register(ZombieDef.WIZARD, new WizardBehavior(6.0f, 1.0f, 0.5f));
        // Prospector Zombie: 6-second fuse, 2-second stun upon landing
        register(ZombieDef.PROSPECTOR, new ProspectorBehavior(18.0f, 2.0f));
        // Fisherman Zombie: Pulls a plant every 10 seconds
        register(ZombieDef.BEACH_FISHERMAN, new FishermanBehavior(10.0f));
        // Turquoise Skull Zombie: 8s cooldown, 2.5s windup, 1s recovery
        register(ZombieDef.CRYSTAL_SKULL, new TurquoiseSkullBehavior(8.0f, 8.5f, 1.0f));
        // Newspaper Zombie: Enrages when armor breaks
        register(ZombieDef.NEWSPAPER, new NewspaperBehavior());
        // Juggler Zombie: Reflects PeaProjectiles within 1.5 tiles, pauses 0.25s per juggle
        register(ZombieDef.DARK_JUGGLER, new JugglerBehavior(0.5,0.25f));
        // All-Star Zombie: Sprints at 3.5x speed, 1s recovery after impact
        register(ZombieDef.MODERN_ALL_STAR, new AllStarBehavior(3.5f, 1.0f));
        // Bully Zombies: Pushes stack of obstacles, 1.5s push time, 0.5s pause
        register(ZombieDef.ICE_AGE_TROGLOBITE, new BullyBehavior(1.5f, 0.5f));
        register(ZombieDef.ARCADE, new BullyBehavior(1.5f, 0.5f));

        // Piano Zombie: 1-second windup, 1-second recovery, insta-kill damage
        register(ZombieDef.PIANO, new CustomMeleeBehavior(20, 20, 9999));

        // Basic Zombies (Add to any zombie that should dance when the piano plays)
        DefaultZombieBehavior defaultBehavior = new DefaultZombieBehavior();
        register(ZombieDef.DEFAULT, defaultBehavior);
        register(ZombieDef.ARMOR1, defaultBehavior);
        register(ZombieDef.ARMOR2, defaultBehavior);
        register(ZombieDef.ARMOR4, defaultBehavior);
    }


    private static void register(ZombieDef def, ZombieBehavior behavior) {
        REGISTRY.put(def, behavior);
    }

    public static ZombieBehavior get(ZombieDef def) {
        return REGISTRY.get(def);
    }
}
