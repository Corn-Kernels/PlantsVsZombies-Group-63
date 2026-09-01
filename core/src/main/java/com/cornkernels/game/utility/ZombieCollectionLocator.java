package com.cornkernels.game.utility;

import com.cornkernels.game.entities.types.zombies.ZombieDef;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public final class ZombieCollectionLocator {

    private static final Map<ZombieDef, String> MENU_ALIASES = Map.ofEntries(
        Map.entry(ZombieDef.DEFAULT, "ZombieTutorialDefault"),
        Map.entry(ZombieDef.ARMOR1, "ZombieTutorialArmor1Default"),
        Map.entry(ZombieDef.ARMOR2, "ZombieTutorialArmor2Default"),
        Map.entry(ZombieDef.ARMOR4, "ZombieTutorialArmor4Default"),
        Map.entry(ZombieDef.DARK_ARMOR3, "ZombieDarkArmor3Default"),
        Map.entry(ZombieDef.GARGANTUAR, "ZombieGargantuarBasic"),
        Map.entry(ZombieDef.IMP, "ZombieTutorialImpDefault"),
        Map.entry(ZombieDef.RA, "ZombieRaDefault"),
        Map.entry(ZombieDef.EXPLORER, "ZombieExplorerDefault"),
        Map.entry(ZombieDef.TOMB_RAISER, "ZombieTombRaiserDefault"),
        Map.entry(ZombieDef.ICE_AGE_DODO, "ZombieIceAgeDodo"),
        Map.entry(ZombieDef.ICE_AGE_HUNTER, "ZombieIceAgeHunter"),
        Map.entry(ZombieDef.ICE_AGE_TROGLOBITE, "ZombieIceAgeTroglobite"),
        Map.entry(ZombieDef.BEACH_FISHERMAN, "ZombieBeachFisherman"),
        Map.entry(ZombieDef.BEACH_OCTOPUS, "ZombieBeachOctopus"),
        Map.entry(ZombieDef.BEACH_SNORKEL, "ZombieBeachSnorkel"),
        Map.entry(ZombieDef.DARK_JUGGLER, "ZombieDarkJugglerDefault"),
        Map.entry(ZombieDef.WIZARD, "ZombieWizardDefault"),
        Map.entry(ZombieDef.DARK_KING, "ZombieDarkKing"),
        Map.entry(ZombieDef.DARK_IMP_DRAGON, "ZombieDarkImpDefault"),
        Map.entry(ZombieDef.MODERN_ALL_STAR, "ZombieModernAllStarDefault"),
        Map.entry(ZombieDef.LOST_CITY_JANE, "ZombieLostCityJaneDefault"),
        Map.entry(ZombieDef.CRYSTAL_SKULL, "ZombieCrystalSkullDefault"),
        Map.entry(ZombieDef.PROSPECTOR, "ZombieProspectorDefault"),
        Map.entry(ZombieDef.PIANO, "ZombiePianoDefault"),
        Map.entry(ZombieDef.NEWSPAPER, "ZombieModernNewspaperDefault"),
        Map.entry(ZombieDef.ARCADE, "ZombieEightiesArcade")
    );

    private ZombieCollectionLocator() {
    }

    public static @Nullable String menuAlias(ZombieDef zombieDef) {
        return MENU_ALIASES.get(zombieDef);
    }
}
