package com.cornkernels.game.entities.types.plants;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public enum PlantDef {

    SUNFLOWER(1, "Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 50, 300, "0", 24.0f, 5f),
    TWIN_SUNFLOWER(2, "Twin Sunflower", PlantCategory.SUN_PRODUCER, tags(PlantTag.DAY), 125, 300, "0", 24.0f, 15f),
    SUN_SHROOM(3, "Sun-shroom", PlantCategory.SUN_PRODUCER, tags(PlantTag.SHROOM, PlantTag.WRAMP_UP, PlantTag.NIGHT), 25, 300, "0", 24.0f, 5f),
    PRIMAL_SUNFLOWER(4, "Primal Sunflower", PlantCategory.SUN_PRODUCER, tags(), 75, 300, "0", 24.0f, 5f),
    GOLD_BLOOM(5, "Gold Bloom", PlantCategory.SUN_PRODUCER, tags(), 0, 0, "0", 0.0f, 75f),

    PEASHOOTER(6, "Peashooter", PlantCategory.SHOOTER, tags(PlantTag.PEA), 100, 300, "20", 1.5f, 5f),
    //plant upgrades will be done as shown for the PeaShooter they will each have their own definition

    REPEATER(7, "Repeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 200, 300, "20x2", 1.5f, 5f),
    THREEPEATER(8, "Threepeater", PlantCategory.SHOOTER, tags(PlantTag.PEA), 300, 300, "20", 1.5f, 5f),
    SNOW_PEA(9, "Snow Pea", PlantCategory.SHOOTER, tags(PlantTag.ICE, PlantTag.PEA), 150, 300, "20", 1.5f, 5f),
    ROTOBAGA(10, "Rotobaga", PlantCategory.SHOOTER, tags(), 150, 300, "10x3", 1.5f, 5f),
    PEA_POD(11, "Pea Pod", PlantCategory.SHOOTER, tags(PlantTag.PEA, PlantTag.STACK), 125, 300, "20/40/60/80/100", 1.5f, 5f),
    SPLIT_PEA(12, "Split Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 125, 300, "20", 1.5f, 5f),
    CITRON(13, "Citron", PlantCategory.SHOOTER, tags(PlantTag.CHARGE), 350, 300, "800", 9.0f, 5f),
    CAULIPOWER(14, "Caulipower", PlantCategory.HOMING, tags(PlantTag.MAGIC, PlantTag.CHARGE), 250, 300, "Insta-kill", 12.0f, 15f),
    ELECTRIC_BLUEBERRY(15, "Electric Blueberry", PlantCategory.HOMING, tags(PlantTag.CHARGE), 150, 300, "5000", 12.0f, 15f),
    BOWLING_BULB(16, "Bowling Bulb", PlantCategory.SHOOTER, tags(PlantTag.CHARGE), 200, 300, "40/120/180", 2.0f, 5f),
    CACTUS(17, "Cactus", PlantCategory.STRIKE_THROUGH, tags(), 175, 300, "30", 1.5f, 5f),
    FIRE_PEASHOOTER(18, "Fire Peashooter", PlantCategory.SHOOTER, tags(PlantTag.FIRE, PlantTag.PEA), 175, 300, "40", 1.5f, 5f),
    STARFRUIT(19, "Starfruit", PlantCategory.SHOOTER, tags(), 150, 300, "20", 1.5f, 5f),
    GOO_PEASHOOTER(20, "Goo Peashooter", PlantCategory.SHOOTER, tags(PlantTag.POISON), 125, 300, "20", 1.5f, 5f),
    MEGA_GATLING_PEA(21, "Mega Gatling Pea", PlantCategory.SHOOTER, tags(PlantTag.PEA), 400, 300, "20x4", 1.5f, 5f),
    SEA_SHROOM(22, "Sea-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM, PlantTag.WATER), 0, 300, "20", 1.5f, 15f),
    PUFF_SHROOM(23, "Puff-shroom", PlantCategory.SHOOTER, tags(PlantTag.SHROOM), 0, 300, "20", 1.5f, 5f),
    FUME_SHROOM(24, "Fume-shroom", PlantCategory.STRIKE_THROUGH, tags(PlantTag.SHROOM), 125, 300, "20", 1.5f, 5f),

    CABBAGE_PULT(25, "Cabbage-pult", PlantCategory.LOBBER, tags(), 100, 300, "40", 2.9f, 5f),
    KERNEL_PULT(26, "Kernel-pult", PlantCategory.LOBBER, tags(), 100, 300, "20/40", 2.9f, 5f),
    MELON_PULT(27, "Melon-pult", PlantCategory.LOBBER, tags(PlantTag.AOE), 325, 300, "80", 2.9f, 5f),
    WINTER_MELON(28, "Winter Melon", PlantCategory.LOBBER, tags(PlantTag.ICE, PlantTag.AOE), 500, 300, "80", 2.9f, 5f),
    PEPPER_PULT(29, "Pepper-pult", PlantCategory.LOBBER, tags(PlantTag.FIRE, PlantTag.AOE), 200, 300, "50", 2.9f, 5f),

    POTATO_MINE(30, "Potato Mine", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.CHARGE), 25, 300, "1800", null, 25f),
    PRIMAL_POTATO_MINE(31, "Primal Potato Mine", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.CHARGE), 50, 300, "2400", null, 5f),
    CHERRY_BOMB(32, "Cherry Bomb", PlantCategory.EXPLOSIVE, tags(), 150, 0, "1800", null, 35f),
    SQUASH(33, "Squash", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP), 50, 300, "1800", null, 20f),
    GRAPESHOT(34, "Grapeshot", PlantCategory.EXPLOSIVE, tags(), 150, 0, "1800", null, 35f),
    JALAPENO(35, "Jalapeno", PlantCategory.EXPLOSIVE, tags(PlantTag.FIRE), 125, 0, "1800", null, 35f),
    DOOM_SHROOM(36, "Doom-shroom", PlantCategory.EXPLOSIVE, tags(PlantTag.SHROOM), 125, 0, "1800", null, 15f),
    TANGLE_KELP(37, "Tangle Kelp", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.WATER), 25, 300, "Insta-kill", null, 15f),
    ICEBERG_LETTUCE(38, "Iceberg Lettuce", PlantCategory.EXPLOSIVE, tags(PlantTag.TRAP, PlantTag.ICE), 0, 300, "0", null, 20f),

    BONK_CHOY(39, "Bonk Choy", PlantCategory.MELEE, tags(), 150, 300, "15", 0.25f, 5f),
    PHAT_BEET(40, "Phat Beet", PlantCategory.MELEE, tags(PlantTag.AOE), 150, 300, "15", 2.0f, 5f),
    CHOMPER(41, "Chomper", PlantCategory.MELEE, tags(), 150, 300, "Insta-kill", 40.0f, 5f),
    WASABI_WHIP(42, "Wasabi Whip", PlantCategory.MELEE, tags(PlantTag.FIRE), 150, 300, "40", 2.0f, 5f),
    KIWIBEAST(43, "Kiwibeast", PlantCategory.MELEE, tags(PlantTag.AOE, PlantTag.WRAMP_UP), 175, 300, "15/30/45", 2.0f, 5f),

    WALL_NUT(44, "Wall-nut", PlantCategory.WALL_NUT, tags(), 50, 4000, "0", null, 20f),
    TALL_NUT(45, "Tall-nut", PlantCategory.WALL_NUT, tags(), 125, 8000, "0", null, 20f),
    ENDURIAN(46, "Endurian", PlantCategory.WALL_NUT, tags(), 100, 3000, "20", null, 15f),
    GARLIC(47, "Garlic", PlantCategory.WALL_NUT, tags(PlantTag.MOVE_ZOMBIES), 50, 300, "0", null, 20f),
    SWEET_POTATO(48, "Sweet Potato", PlantCategory.WALL_NUT, tags(PlantTag.MOVE_ZOMBIES), 150, 3000, "0", null, 20f),
    EXPLODE_O_NUT(49, "Explode-o-nut", PlantCategory.WALL_NUT, tags(PlantTag.EXPLOSIVE), 50, 4000, "1800", null, 20f),
    PUMPKIN(50, "Pumpkin", PlantCategory.WALL_NUT, tags(PlantTag.STACK), 150, 4000, "0", null, 20f),
    SUN_BEAN(51, "Sun Bean", PlantCategory.WALL_NUT, tags(PlantTag.SUN), 50, 1000, "0", null, 20f),

    TORCHWOOD(52, "Torchwood", PlantCategory.MODIFIER, tags(PlantTag.FIRE), 175, 300, "0", null, 5f),
    MAGNET_SHROOM(53, "Magnet-shroom", PlantCategory.HOMING, tags(PlantTag.SHROOM, PlantTag.MAGIC), 100, 300, "0", 10.0f, 15f),
    HYPNO_SHROOM(54, "Hypno-shroom", PlantCategory.MODIFIER, tags(PlantTag.SHROOM, PlantTag.MAGIC), 125, 300, "0", null, 20f),
    CAT_TAIL(55, "Cat-tail", PlantCategory.HOMING, tags(), 175, 300, "15", 1.5f, 20f),
    IMITATER(56, "Imitater", PlantCategory.MODIFIER, tags(), 0, 0, "0", null, 0f),
    ICE_SHROOM(57, "Ice-shroom", PlantCategory.EXPLOSIVE, tags(PlantTag.SHROOM, PlantTag.ICE), 75, 0, "0", null, 50f),
    LILY_PAD(58, "Lily Pad", PlantCategory.MODIFIER, tags(PlantTag.WATER, PlantTag.STACK), 25, 300, "0", null, 5f),
    HOT_POTATO(59, "Hot Potato", PlantCategory.EXPLOSIVE, tags(PlantTag.FIRE), 0, 0, "0", null, 5f),
    GRAVE_BUSTER(60, "Grave Buster", PlantCategory.EXPLOSIVE, tags(), 0, 0, "Insta-kill", null, 10f),

    ENLIGHTEN_MINT(61, "Enlighten-mint", PlantCategory.SUN_PRODUCER, tags(), 0, 0, "0", null, 85f),
    APPEASE_MINT(62, "Appease-mint", PlantCategory.SHOOTER, tags(), 0, 0, "0", null, 85f),
    ARMA_MINT(63, "Arma-mint", PlantCategory.LOBBER, tags(), 0, 0, "0", null, 85f),
    BOMBARD_MINT(64, "Bombard-mint", PlantCategory.EXPLOSIVE, tags(), 0, 0, "0", null, 85f),
    ENFORCE_MINT(65, "Enforce-mint", PlantCategory.MELEE, tags(), 0, 0, "0", null, 85f),
    REINFORCE_MINT(66, "Reinforce-mint", PlantCategory.WALL_NUT, tags(), 0, 0, "0", null, 85f),
    ENCHANT_MINT(67, "Enchant-mint", PlantCategory.MODIFIER, tags(), 0, 0, "0", null, 85f),
    PIERCE_MINT(68, "Pierce-mint", PlantCategory.STRIKE_THROUGH, tags(), 0, 0, "0", null, 85f),
    CATTAIL_MINT(69, "catTail-mint", PlantCategory.HOMING, tags(), 0, 0, "0", null, 85f);

    private final int id;
    private final String plantName;
    private final PlantCategory category;
    private final PlantTag[] tags;
    private final int cost;
    private final int baseHp;
    private final String damage;
    private final Float actionInterval; // null when the plant has no repeating action interval
    private final float recharge;

    PlantDef(int id, String plantName, PlantCategory category, PlantTag[] tags,
             int cost, int baseHp, String damage, Float actionInterval, float recharge) {
        this.id = id;
        this.plantName = plantName;
        this.category = category;
        this.tags = tags;
        this.cost = cost;
        this.baseHp = baseHp;
        this.damage = damage;
        this.actionInterval = actionInterval;
        this.recharge = recharge;
    }

    private static PlantTag[] tags(PlantTag... tags) {
        return tags;
    }

    public static @Nullable PlantDef getPlantTypeOfName(String plantName) {
        for (PlantDef plantDef : PlantDef.values()) {
            if (plantDef.getPlantName().equals(plantName)) {
                return plantDef;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getPlantName() {
        return plantName;
    }

    public PlantCategory getCategory() {
        return category;
    }

    public PlantTag[] getTags() {
        return tags;
    }

    public int getCost() {
        return cost;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public String getDamage() {
        return damage;
    }

    /**
     * @return the action interval in seconds, or empty if this plant has none.
     */
    @Contract(pure = true)
    public @NotNull Optional<Float> getActionInterval() {
        return Optional.ofNullable(actionInterval);
    }

    public float getRecharge() {
        return recharge;
    }
}
