package com.cornkernels.game.entities.types.zombies;

import com.cornkernels.game.entities.types.zombies.armors.ArmorType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum ZombieDef {

    DEFAULT("ZombieDefault", "ZombiePropertySheet", 190, 0.185f, 100, 150, 100, 1000, true, 1, 2, List.of()),
    ARMOR1("ZombieArmor1", "ZombiePropertySheet", 190, 0.185f, 100, 150, 200, 3000, true, 3, 2, List.of(ArmorType.CONE)),
    ARMOR2("ZombieArmor2", "ZombiePropertySheet", 190, 0.185f, 100, 150, 400, 4000, true, 5, 2, List.of(ArmorType.BUCKET)),
    ARMOR4("ZombieArmor4", "ZombiePropertySheet", 190, 0.185f, 100, 150, 700, 3000, true, 6, 2, List.of(ArmorType.BRICK)),
    DARK_ARMOR3("ZombieDarkArmor3", "ZombiePropertySheet", 190, 0.185f, 100, 150, 550, 4500, true, 6, 2, List.of(ArmorType.SHOULDER, ArmorType.CROWN)),
    GARGANTUAR("ZombieGargantuar", "ZombieGargantuarProps", 3600, 0.24f, 0, 150, 1500, 3000, false, 7, 3, List.of()),
    IMP("ZombieImp", "ZombiePropertySheet", 190, 0.22f, 100, 150, 100, 1000, false, 1, 3, List.of()),
    RA("ZombieRa", "ZombieRaProps", 190, 0.2f, 100, 150, 100, 700, true, 1, 2, List.of()),
    EXPLORER("ZombieExplorer", "ZombieExplorerProps", 250, 0.25f, 100, 150, 250, 3000, true, 2, 3, List.of()),
    TOMB_RAISER("ZombieTombRaiser", "ZombieTombRaiserProps", 380, 0.185f, 100, 150, 300, 2000, true, 4, 2, List.of()),
    ICE_AGE_DODO("ZombieIceAgeDodo", "ZombieIceAgeDodoProps", 490, 0.3f, 100, 150, 600, 3500, true, 3, 4, List.of()),
    ICE_AGE_HUNTER("ZombieIceAgeHunter", "ZombieIceAgeHunterProps", 700, 0.12f, 100, 150, 500, 3500, true, 4, 0, List.of()),
    ICE_AGE_TROGLOBITE("ZombieIceAgeTroglobite", "ZombieIceAgeTroglobiteProps", 470, 0.185f, 100, 150, 600, 3500, true, 4, 2, List.of()),
    BEACH_FISHERMAN("ZombieBeachFisherman", "ZombieBeachFishermanProps", 1000, 0.185f, 100, 150, 700, 2500, true, 5, -1, List.of()),
    BEACH_OCTOPUS("ZombieBeachOctopus", "ZombieBeachOctopusProps", 910, 0.12f, 100, 150, 900, 3500, true, 5, 0, List.of()),
    BEACH_SNORKEL("ZombieBeachSnorkel", "ZombieBeachSnorkelProps", 350, 0.185f, 100, 150, 200, 3000, true, 3, 2, List.of()),
    DARK_JUGGLER("ZombieDarkJuggler", "ZombieDarkJugglerProps", 420, 0.2f, 100, 150, 450, 3500, true, 3, 2, List.of()),
    WIZARD("ZombieWizard", "ZombieDarkWizardProps", 490, 0.12f, 100, 150, 800, 3500, true, 4, 0, List.of()),
    DARK_KING("ZombieDarkKing", "ZombieDarkKingProps", 1000, 0.185f, 100, 150, 750, 2000, true, 5, -1, List.of()),
    DARK_IMP_DRAGON("ZombieDarkImpDragon", "ZombiePropertySheet", 190, 0.185f, 100, 150, 150, 2000, false, 1, 2, List.of()),
    MODERN_ALL_STAR("ZombieModernAllStar", "ZombieModernAllStarProps", 1100, 0.16f, 100, 150, 1000, 3500, true, 5, 5, List.of()),
    LOST_CITY_JANE("ZombieLostCityJane", "ZombieLostCityJaneProps", 350, 0.25f, 100, 150, 200, 3000, true, 3, 3, List.of()),
    CRYSTAL_SKULL("ZombieCrystalSkull", "ZombieCrystalSkullProps", 250, 0.185f, 100, 150, 500, 3000, true, 3, 2, List.of()),
    PROSPECTOR("ZombieProspector", "ZombieProspectorProps", 190, 0.16f, 100, 150, 200, 3000, true, 1, 1, List.of()),
    PIANO("ZombiePiano", "ZombiePianoProps", 840, 0.12f, 4000, 150, 450, 2000, true, 5, 0, List.of()),
    NEWSPAPER("ZombieNewspaper", "ZombieNewspaperProps", 460, 0.22f, 200, 150, 700, 4000, true, 5, 3, List.of(ArmorType.NEWSPAPER)),
    ARCADE("ZombieArcade", "ZombieArcadeProps", 490, 0.19f, 100, 150, 600, 1000, true, 4, 3, List.of());

    public final String id;

    public final String objClass;

    public final int baseHp;
    public final float baseSpeed;
    public final int eatDps;
    public final int cost;
    public final int wavePointCost;
    public final int weight;
    public final boolean canSpawnPlantFood;

    public final int toughnessTier;

    public final int speedTier;

    public final List<ArmorType> armors;

    ZombieDef(String id, String objClass, int baseHp, float baseSpeed, int eatDps, int cost,
              int wavePointCost, int weight, boolean canSpawnPlantFood, int toughnessTier,
              int speedTier, List<ArmorType> armors) {
        this.id = id;
        this.objClass = objClass;
        this.baseHp = baseHp;
        this.baseSpeed = baseSpeed;
        this.eatDps = eatDps;
        this.cost = cost;
        this.wavePointCost = wavePointCost;
        this.weight = weight;
        this.canSpawnPlantFood = canSpawnPlantFood;
        this.toughnessTier = toughnessTier;
        this.speedTier = speedTier;
        this.armors = armors;
    }

    public static @NotNull ZombieDef byId(String id) {
        for (ZombieDef z : values()) {
            if (z.id.equals(id)) return z;
        }
        throw new IllegalArgumentException("Unknown zombie id: " + id);
    }

    public boolean hasArmor() {
        return !armors.isEmpty();
    }
}
