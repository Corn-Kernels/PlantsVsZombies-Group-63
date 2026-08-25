package com.cornkernels.game.menus.utils;

import com.cornkernels.game.menus.model.Plant;
import com.cornkernels.game.menus.model.Zombie;

import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    // ===== تبدیل اسم گیاه به اسم فایل (همه گیاهان) =====
    private static String getPlantImagePath(String plantName) {
        // همه گیاهان با اسم فایل‌هایی که در پوشه داری
        switch (plantName) {
            // Sun Producers
            case "Sunflower": return "sunflower.png";
            case "Twin Sunflower": return "twin_sunflower.png";
            case "Sun-shroom": return "sun_shroom.png";
            case "Primal Sunflower": return "primal_sunflower.png";
            case "Gold Bloom": return "gold_bloom.png";

            // Shooters
            case "Peashooter": return "peashooter.png";
            case "Repeater": return "repeater.png";
            case "Threepeater": return "threepeater.png";
            case "Snow Pea": return "snow_pea.png";
            case "Rotobaga": return "rotobaga.png";
            case "Pea Pod": return "pea_pod.png";
            case "Split Pea": return "split_pea.png";
            case "Citron": return "citron.png";
            case "Caulipower": return "caulipower.png";

            // Homing
            case "Electric Blueberry": return "electric_blueberry.png";
            case "Bowling Bulb": return "bowling_bulb.png";

            // Strike-through & Special
            case "Cactus": return "cactus.png";
            case "Fire Peashooter": return "fire_peashooter.png";
            case "Starfruit": return "starfruit.png";
            case "Goo Peashooter": return "goo_peashooter.png";
            case "Mega Gatling Pea": return "mega_gatling_pea.png";

            // Shroom Shooters
            case "Sea-shroom": return "sea_shroom.png";
            case "Puff-shroom": return "puff_shroom.png";
            case "Fume-shroom": return "fume_shroom.png";

            // Lobbers
            case "Cabbage-pult": return "cabbage_pult.png";
            case "Kernel-pult": return "kernel_pult.png";
            case "Melon-pult": return "melon_pult.png";
            case "Winter Melon": return "winter_melon.png";
            case "Pepper-pult": return "pepper_pult.png";

            // Explosives & Traps
            case "Potato Mine": return "potato_mine.png";
            case "Primal Potato Mine": return "primal_potato_mine.png";
            case "Cherry Bomb": return "cherry_bomb.png";
            case "Squash": return "squash.png";
            case "Grapeshot": return "grapeshot.png";
            case "Jalapeno": return "jalapeno.png";
            case "Doom-shroom": return "doom_shroom.png";
            case "Tangle Kelp": return "tangle_kelp.png";
            case "Iceberg Lettuce": return "iceberg_lettuce.png";

            // Melee
            case "Bonk Choy": return "bonk_choy.png";
            case "Phat Beet": return "phet_beet.png";
            case "Chomper": return "chomper.png";
            case "Wasabi Whip": return "wasabi_whip.png";
            case "Kiwibeast": return "kiwibeast.png";

            // Wall-nuts & Defensive
            case "Wall-nut": return "wall_nut.png";
            case "Tall-nut": return "tall_nut.png";
            case "Endurian": return "endurian.png";
            case "Garlic": return "garlic.png";
            case "Sweet Potato": return "sweet_potato.png";
            case "Explode-o-nut": return "explode_o_nut.png";
            case "Pumpkin": return "pumpkin.png";
            case "Sun Bean": return "sun_bean.png";

            // Modifiers & Support
            case "Torchwood": return "torchwood.png";
            case "Magnet-shroom": return "magnet_shroom.png";
            case "Hypno-shroom": return "hypno_shroom.png";
            case "Cat-tail": return "cat_tail.png";

            // Special Modifiers
            case "Imitater": return "imitater.png";
            case "Ice-shroom": return "ice_shroom.png";
            case "Lily Pad": return "lily_pad.png";

            // Utility
            case "Hot Potato": return "hot_potato.png";
            case "Grave Buster": return "grave_buster.png";
            case "Enlighten-mint": return "enlighten_mint.png";

            // Mint Family
            case "Appease-mint": return "appease_mint.png";
            case "Arma-mint": return "armamint.png";
            case "Bombard-mint": return "bombard_mint.png";
            case "Enforce-mint": return "enforce_mint.png";
            case "Reinforce-mint": return "reinforce_mint.png";
            case "Enchant-mint": return "enchant_mint.png";
            case "Pierce-mint": return "pierce_mint.png";
            case "CatTail-mint": return "cattail_mint.png";

            default: return plantName.toLowerCase().replace(" ", "_").replace("-", "_") + ".png";
        }
    }

    // ===== تبدیل alias زامبی به اسم فایل =====
    private static String getZombieImagePath(String alias) {
        return alias + ".png";
    }

    // ===== LOAD ALL PLANTS (69 plants) =====
    public static List<Plant> loadAllPlants() {
        List<Plant> plants = new ArrayList<>();

        // ===== Sun Producers (1-5) =====
        plants.add(new Plant(1, "Sunflower", "Sun Producer", new String[]{"Day"}, 50, 300, 0,
            "Produces 50 sun every 24 seconds.", "Instantly produces 150 sun.",
            0, 150, 0, 24, 5, getPlantImagePath("Sunflower")));
        plants.add(new Plant(2, "Twin Sunflower", "Sun Producer", new String[]{"Day"}, 125, 300, 0,
            "Produces 100 sun per cycle.", "Instantly produces 250 sun.",
            0, 150, -25, 24, 15, getPlantImagePath("Twin Sunflower")));
        plants.add(new Plant(3, "Sun-shroom", "Sun Producer", new String[]{"Shroom", "wramp-up", "Night"}, 25, 300, 0,
            "Grows in 3 stages, producing more sun (25/50/75).", "Instantly grows to max size and produces 225 sun.",
            0, 150, 0, 24, 5, getPlantImagePath("Sun-shroom")));
        plants.add(new Plant(4, "Primal Sunflower", "Sun Producer", new String[]{}, 75, 300, 0,
            "Produces 75 sun from the start.", "Instantly produces 225 sun.",
            0, 150, -25, 24, 5, getPlantImagePath("Primal Sunflower")));
        plants.add(new Plant(5, "Gold Bloom", "Sun Producer", new String[]{}, 0, 0, 0,
            "Instantly produces 375 sun then disappears.", "None (instant use).",
            -5, 50, -25, 0, 75, getPlantImagePath("Gold Bloom")));

        // ===== Shooters (6-14) =====
        plants.add(new Plant(6, "Peashooter", "Shooter", new String[]{"Pea"}, 100, 300, 20,
            "Shoots a straight pea at zombies.", "Fires a rapid barrage for several seconds.",
            10, 150, -25, 1.5f, 5, getPlantImagePath("Peashooter")));
        plants.add(new Plant(7, "Repeater", "Shooter", new String[]{"Pea"}, 200, 300, 20,
            "Shoots 2 peas in quick succession.", "Heavy barrage + 1 giant pea (20x damage).",
            10, 200, -25, 1.5f, 5, getPlantImagePath("Repeater")));
        plants.add(new Plant(8, "Threepeater", "Shooter", new String[]{"Pea"}, 300, 300, 20,
            "Shoots peas in 3 parallel lanes simultaneously.", "Fan barrage in all lanes.",
            -25, 10, 200, 1.5f, 5, getPlantImagePath("Threepeater")));
        plants.add(new Plant(9, "Snow Pea", "Shooter", new String[]{"Ice", "Pea"}, 150, 300, 20,
            "Shoots a chilling pea that slows zombies.", "Freezes the lane and fires icy barrage.",
            10, 0, -25, 1.5f, 5, getPlantImagePath("Snow Pea")));
        plants.add(new Plant(10, "Rotobaga", "Shooter", new String[]{}, 150, 300, 10,
            "Shoots in 4 diagonal directions.", "Barrage in all 4 diagonal directions.",
            10, 150, -25, 1.5f, 5, getPlantImagePath("Rotobaga")));
        plants.add(new Plant(11, "Pea Pod", "Shooter", new String[]{"Pea", "stack"}, 125, 300, 20,
            "Shoots 1 pea per head planted (up to 5 heads).", "Shoots 1 giant pea per head (20x damage each).",
            10, 200, -25, 1.5f, 5, getPlantImagePath("Pea Pod")));
        plants.add(new Plant(12, "Split Pea", "Shooter", new String[]{"Pea"}, 125, 300, 20,
            "Shoots 1 pea forward and 2 peas backward.", "Rapid barrage from both directions.",
            10, 200, -25, 1.5f, 5, getPlantImagePath("Split Pea")));
        plants.add(new Plant(13, "Citron", "Shooter", new String[]{"charge"}, 350, 300, 800,
            "Shoots a heavy plasma ball (requires charge time).", "Fires a plasma ball that clears the entire lane.",
            -1, 150, -50, 9, 5, getPlantImagePath("Citron")));
        plants.add(new Plant(14, "Caulipower", "Homing", new String[]{"Magic", "charge"}, 250, 300, 0,
            "Shoots magic that hypnotizes zombies (ignores obstacles).", "Hypnotizes multiple random zombies on the lawn.",
            -2, 150, -50, 12, 15, getPlantImagePath("Caulipower")));

        // ===== Homing (15-16) =====
        plants.add(new Plant(15, "Electric Blueberry", "Homing", new String[]{"charge"}, 150, 300, 5000,
            "Shoots lightning that instantly destroys one zombie.", "Instantly destroys 3 random zombies.",
            -2, 0, -25, 12, 15, getPlantImagePath("Electric Blueberry")));
        plants.add(new Plant(16, "Bowling Bulb", "Shooter", new String[]{"charge"}, 200, 300, 40,
            "Shoots 3 types of bulbs that bounce between lanes.", "Shoots 3 explosive bouncing bulbs.",
            -1, 15, -25, 2, 5, getPlantImagePath("Bowling Bulb")));

        // ===== Strike-through & Special (17-21) =====
        plants.add(new Plant(17, "Cactus", "Strike-through", new String[]{}, 175, 300, 30,
            "Shoots spikes that pierce through 3 zombies.", "Electric spikes with high damage and infinite pierce.",
            1, 10, -25, 1.5f, 5, getPlantImagePath("Cactus")));
        plants.add(new Plant(18, "Fire Peashooter", "Shooter", new String[]{"Fire", "Pea"}, 175, 300, 40,
            "Shoots a fire pea (2x damage).", "Rapid fire barrage across the entire lane.",
            10, 200, -25, 1.5f, 5, getPlantImagePath("Fire Peashooter")));
        plants.add(new Plant(19, "Starfruit", "Shooter", new String[]{}, 150, 300, 20,
            "Shoots in 5 star directions including backward.", "Rapid star barrage in all directions.",
            0, 10, -25, 1.5f, 5, getPlantImagePath("Starfruit")));
        plants.add(new Plant(20, "Goo Peashooter", "Shooter", new String[]{"Poison"}, 125, 300, 20,
            "Shoots goo that ignores armor and deals poison damage over time.",
            "Rapid poison barrage that poisons all zombies on the lawn.",
            5, 150, -25, 1.5f, 5, getPlantImagePath("Goo Peashooter")));
        plants.add(new Plant(21, "Mega Gatling Pea", "Shooter", new String[]{"Pea"}, 400, 300, 20,
            "Shoots 4 peas rapidly with high speed.", "Massive rapid barrage with 4 giant peas.",
            10, 0, -50, 1.5f, 5, getPlantImagePath("Mega Gatling Pea")));

        // ===== Shroom Shooters (22-24) =====
        plants.add(new Plant(22, "Sea-shroom", "Shooter", new String[]{"Shroom", "Water"}, 0, 300, 20,
            "Shoots short-range peas. Has 60 second lifespan.", "Rapid barrage and resets lifespan of all Sea-shrooms.",
            0, 5, 10, 1.5f, 15, getPlantImagePath("Sea-shroom")));
        plants.add(new Plant(23, "Puff-shroom", "Shooter", new String[]{"Shroom"}, 0, 300, 20,
            "Shoots short-range peas. Has 60 second lifespan.", "Rapid barrage and resets lifespan of all Puff-shrooms.",
            10, 10, 0, 1.5f, 5, getPlantImagePath("Puff-shroom")));
        plants.add(new Plant(24, "Fume-shroom", "Strike-through", new String[]{"Shroom"}, 125, 300, 20,
            "Shoots fumes that pass through zombies (medium range).", "Throws a giant fume cloud that pushes zombies back.",
            0, 10, -25, 1.5f, 5, getPlantImagePath("Fume-shroom")));

        // ===== Lobbers (25-29) =====
        plants.add(new Plant(25, "Cabbage-pult", "Lobber", new String[]{}, 100, 300, 40,
            "Lobs cabbages over obstacles.", "Throws cabbages at random zombies.",
            10, 15, 150, 2.9f, 5, getPlantImagePath("Cabbage-pult")));
        plants.add(new Plant(26, "Kernel-pult", "Lobber", new String[]{}, 100, 300, 20,
            "Shoots corn (low damage) or butter (stops zombie).", "Throws butter at all zombies on the lawn.",
            5, 10, 150, 2.9f, 5, getPlantImagePath("Kernel-pult")));
        plants.add(new Plant(27, "Melon-pult", "Lobber", new String[]{"AoE"}, 325, 300, 80,
            "Lobs heavy melons with area damage.", "Throws giant melons at random zombies.",
            -25, 15, 30, 2.9f, 5, getPlantImagePath("Melon-pult")));
        plants.add(new Plant(28, "Winter Melon", "Lobber", new String[]{"Ice", "AoE"}, 500, 300, 80,
            "Lobs ice melons that chill zombies.", "Throws ice melons at random zombies.",
            -50, 15, -25, 2.9f, 5, getPlantImagePath("Winter Melon")));
        plants.add(new Plant(29, "Pepper-pult", "Lobber", new String[]{"Fire", "AoE"}, 200, 300, 50,
            "Lobs peppers with area damage (warms surroundings).", "Throws giant peppers at 3 random zombies.",
            15, 0, -25, 2.9f, 5, getPlantImagePath("Pepper-pult")));

        // ===== Explosives & Traps (30-38) =====
        plants.add(new Plant(30, "Potato Mine", "Explosive", new String[]{"Trap", "charge"}, 25, 300, 1800,
            "Arms after 15 seconds; explodes on contact.", "Instantly arms and spawns 2 clone mines.",
            -3, -5, 600, 0, 25, getPlantImagePath("Potato Mine")));
        plants.add(new Plant(31, "Primal Potato Mine", "Explosive", new String[]{"Trap", "charge"}, 50, 300, 2400,
            "Arms faster (5 seconds) with 3x3 area damage.", "Instantly arms and spawns 2 mines elsewhere.",
            -1, -3, 400, 0, 5, getPlantImagePath("Primal Potato Mine")));
        plants.add(new Plant(32, "Cherry Bomb", "Explosive", new String[]{}, 150, 0, 1800,
            "Instantly explodes in a 3x3 area.", "None (instant use).",
            -5, 600, -25, 0, 35, getPlantImagePath("Cherry Bomb")));
        plants.add(new Plant(33, "Squash", "Explosive", new String[]{"Trap"}, 50, 300, 1800,
            "Squashes the first adjacent zombie.", "Squashes 2 random zombies on the lawn.",
            -3, 600, 0, 0, 20, getPlantImagePath("Squash")));
        plants.add(new Plant(34, "Grapeshot", "Explosive", new String[]{}, 150, 0, 1800,
            "3x3 explosion + bouncing grapes that last 5 seconds.", "None (instant use).",
            600, 0, -25, 0, 35, getPlantImagePath("Grapeshot")));
        plants.add(new Plant(35, "Jalapeno", "Explosive", new String[]{"Fire"}, 125, 0, 1800,
            "Instantly burns all zombies in one lane (melts ice).", "None (instant use).",
            -5, 600, -25, 0, 35, getPlantImagePath("Jalapeno")));
        plants.add(new Plant(36, "Doom-shroom", "Explosive", new String[]{"Shroom"}, 125, 0, 1800,
            "Massive explosion across the lawn + creates a crater.", "None (instant use).",
            -5, 800, -50, 0, 15, getPlantImagePath("Doom-shroom")));
        plants.add(new Plant(37, "Tangle Kelp", "Explosive", new String[]{"Trap", "Water"}, 25, 300, 0,
            "Pulls the first water zombie underwater (instant kill).", "Pulls multiple random water zombies underwater.",
            -5, 0, -25, 0, 15, getPlantImagePath("Tangle Kelp")));
        plants.add(new Plant(38, "Iceberg Lettuce", "Explosive", new String[]{"Trap", "Ice"}, 0, 300, 0,
            "Freezes the first zombie that steps on it.", "Freezes all zombies on the lawn.",
            -2, 0, 0, 0, 20, getPlantImagePath("Iceberg Lettuce")));

        // ===== Melee (39-43) =====
        plants.add(new Plant(39, "Bonk Choy", "Melee", new String[]{}, 150, 300, 15,
            "Punches zombies in front and behind.", "Rapid punches in a 3x3 radius.",
            5, 10, 200, 0.25f, 5, getPlantImagePath("Bonk Choy")));
        plants.add(new Plant(40, "Phat Beet", "Melee", new String[]{"AoE"}, 150, 300, 15,
            "Sound wave attack in a 3x3 area.", "Powerful sound wave to all nearby zombies.",
            10, 10, 200, 2, 5, getPlantImagePath("Phat Beet")));
        plants.add(new Plant(41, "Chomper", "Melee", new String[]{}, 150, 300, 0,
            "Instantly swallows one zombie then digests for 40 seconds.", "Swallows 3 zombies from a distance.",
            -2, 200, -3, 40, 5, getPlantImagePath("Chomper")));
        plants.add(new Plant(42, "Wasabi Whip", "Melee", new String[]{"Fire"}, 150, 300, 40,
            "Whips zombies in front and behind (warms surroundings).", "Spinning whip attack in a 3x3 area.",
            10, 0, 200, 2, 5, getPlantImagePath("Wasabi Whip")));
        plants.add(new Plant(43, "Kiwibeast", "Melee", new String[]{"AoE", "wramp-up"}, 175, 300, 15,
            "Area sound wave; grows larger over time dealing more damage.", "Jumps and slams the ground with area damage.",
            200, 15, 0, 2, 5, getPlantImagePath("Kiwibeast")));

        // ===== Wall-nuts & Defensive (44-51) =====
        plants.add(new Plant(44, "Wall-nut", "Wall-nut", new String[]{}, 50, 4000, 0,
            "A sturdy defensive wall to stop zombies.", "Gains permanent 4000 HP armor.",
            1000, -5, 1500, 0, 20, getPlantImagePath("Wall-nut")));
        plants.add(new Plant(45, "Tall-nut", "Wall-nut", new String[]{}, 125, 8000, 0,
            "A tall wall that blocks jumping zombies.", "Gains permanent 8000 HP armor.",
            2000, -5, 3000, 0, 20, getPlantImagePath("Tall-nut")));
        plants.add(new Plant(46, "Endurian", "Wall-nut", new String[]{}, 100, 3000, 20,
            "Defensive wall that reflects damage to attackers.", "Gains metal armor and increased reflect damage.",
            5, 1000, -25, 0, 15, getPlantImagePath("Endurian")));
        plants.add(new Plant(47, "Garlic", "Wall-nut", new String[]{"moveZombies"}, 50, 300, 0,
            "Forces zombies to move to adjacent lanes when eaten.", "Forces all zombies in the lane to move to other lanes.",
            150, -3, 250, 0, 20, getPlantImagePath("Garlic")));
        plants.add(new Plant(48, "Sweet Potato", "Wall-nut", new String[]{"moveZombies"}, 150, 3000, 0,
            "Attracts zombies from adjacent lanes to this lane.", "Attracts all nearby zombies and fully heals.",
            1000, -5, 1500, 0, 20, getPlantImagePath("Sweet Potato")));
        plants.add(new Plant(49, "Explode-o-nut", "Wall-nut", new String[]{"Explosive"}, 50, 4000, 1800,
            "Defensive wall that explodes when destroyed.", "Gains metal armor (explodes when armor is destroyed too).",
            1000, 200, -25, 0, 20, getPlantImagePath("Explode-o-nut")));
        plants.add(new Plant(50, "Pumpkin", "Wall-nut", new String[]{"stack"}, 150, 4000, 0,
            "Protective wall that covers other plants.", "Gains powerful metal armor.",
            1000, -5, 1500, 0, 20, getPlantImagePath("Pumpkin")));
        plants.add(new Plant(51, "Sun Bean", "Wall-nut", new String[]{"Sun"}, 50, 1000, 0,
            "Defensive wall that produces 5 sun per hit.", "Gains powerful metal armor.",
            5, 150, -25, 0, 20, getPlantImagePath("Sun Bean")));

        // ===== Modifiers & Support (52-55) =====
        plants.add(new Plant(52, "Torchwood", "Modifier", new String[]{"Fire"}, 175, 300, 0,
            "Converts passing peas into fire peas (2x damage, melts ice).", "Creates blue flame (3x damage for all peas).",
            300, 0, -25, 0, 5, getPlantImagePath("Torchwood")));
        plants.add(new Plant(53, "Magnet-shroom", "Homing", new String[]{"Shroom", "Magic"}, 100, 300, 0,
            "Disarms zombies by pulling metal objects from their heads.", "Pulls multiple metals simultaneously.",
            0, -5, 200, 10, 15, getPlantImagePath("Magnet-shroom")));
        plants.add(new Plant(54, "Hypno-shroom", "Modifier", new String[]{"Shroom", "Magic"}, 125, 300, 0,
            "When eaten, hypnotizes the zombie to fight for you.", "Turns the eating zombie into a friendly Gargantuar.",
            -25, 0, 0, 0, 20, getPlantImagePath("Hypno-shroom")));
        plants.add(new Plant(55, "Cat-tail", "Homing", new String[]{}, 175, 300, 15,
            "Shoots homing projectiles at the nearest zombie.", "Rapid barrage of homing projectiles.",
            10, 200, -25, 1.5f, 20, getPlantImagePath("Cat-tail")));

        // ===== Special Modifiers (56-58) =====
        plants.add(new Plant(56, "Imitater", "Modifier", new String[]{}, 0, 0, 0,
            "Copies another plant (allows using two of the same card).", "Depends on the copied plant.",
            -2, -25, 0, 0, 0, getPlantImagePath("Imitater")));
        plants.add(new Plant(57, "Ice-shroom", "Explosive", new String[]{"Shroom", "Ice"}, 75, 0, 0,
            "Freezes and temporarily stops all zombies on the map.", "None (instant use).",
            2, -5, 50, 0, 50, getPlantImagePath("Ice-shroom")));
        plants.add(new Plant(58, "Lily Pad", "Modifier", new String[]{"Water", "stack"}, 25, 300, 0,
            "Allows planting on water (essential for Beach).", "Creates copies on empty water tiles.",
            -25, 200, -2, 0, 5, getPlantImagePath("Lily Pad")));

        // ===== Utility (59-61) =====
        plants.add(new Plant(59, "Hot Potato", "Explosive", new String[]{"Fire"}, 0, 0, 0,
            "Placed on ice to instantly melt it (essential for Frostbite Caves).", "None (instant use).",
            -2, 0, 0, 0, 5, getPlantImagePath("Hot Potato")));
        plants.add(new Plant(60, "Grave Buster", "Explosive", new String[]{}, 0, 0, 0,
            "Destroys graves (essential for Egypt and Dark Ages).", "None (instant use).",
            -1, -2, 0, 0, 10, getPlantImagePath("Grave Buster")));
        plants.add(new Plant(61, "Enlighten-mint", "Sun Producer", new String[]{}, 0, 0, 0,
            "Temporarily applies Plant Food to all plants in its family.", "None (instant use).",
            1, -5, 0, 0, 85, getPlantImagePath("Enlighten-mint")));

        // ===== Mint Family (62-69) =====
        plants.add(new Plant(62, "Appease-mint", "Shooter", new String[]{}, 0, 0, 0,
            "Temporarily applies Plant Food to all plants in its family.", "None (instant use).",
            1, -5, 0, 0, 85, getPlantImagePath("Appease-mint")));
        plants.add(new Plant(63, "Arma-mint", "Lobber", new String[]{}, 0, 0, 0,
            "Temporarily applies Plant Food to all plants in its family.", "None (instant use).",
            1, -5, 0, 0, 85, getPlantImagePath("Arma-mint")));
        plants.add(new Plant(64, "Bombard-mint", "Explosive", new String[]{}, 0, 0, 0,
            "Temporarily applies Plant Food to all plants in its family.", "None (instant use).",
            1, -5, 0, 0, 85, getPlantImagePath("Bombard-mint")));
        plants.add(new Plant(65, "Enforce-mint", "Melee", new String[]{}, 0, 0, 0,
            "Temporarily applies Plant Food to all plants in its family.", "None (instant use).",
            1, -5, 0, 0, 85, getPlantImagePath("Enforce-mint")));
        plants.add(new Plant(66, "Reinforce-mint", "Wall-nut", new String[]{}, 0, 0, 0,
            "Temporarily applies Plant Food to all plants in its family.", "None (instant use).",
            1, -5, 0, 0, 85, getPlantImagePath("Reinforce-mint")));
        plants.add(new Plant(67, "Enchant-mint", "Modifier", new String[]{}, 0, 0, 0,
            "Temporarily applies Plant Food to all plants in its family.", "None (instant use).",
            1, -5, 0, 0, 85, getPlantImagePath("Enchant-mint")));
        plants.add(new Plant(68, "Pierce-mint", "Strike-through", new String[]{}, 0, 0, 0,
            "Temporarily applies Plant Food to all plants in its family.", "None (instant use).",
            1, -5, 0, 0, 85, getPlantImagePath("Pierce-mint")));
        plants.add(new Plant(69, "CatTail-mint", "Homing", new String[]{}, 0, 0, 0,
            "Temporarily applies Plant Food to all plants in its family.", "None (instant use).",
            1, -5, 0, 0, 85, getPlantImagePath("CatTail-mint")));

        return plants;
    }

    // ===== LOAD ALL ZOMBIES (59 zombies) =====
    public static List<Zombie> loadAllZombies() {
        List<Zombie> zombies = new ArrayList<>();

        // ===== All Chapters (Core) - 7 zombies =====
        zombies.add(new Zombie("ZombieTutorialDefault", "Basic Zombie",
            190, 0.185f, 100, new String[]{},
            "Basic zombie; moves and eats plants.",
            getZombieImagePath("ZombieTutorialDefault"), "All Chapters"));
        zombies.add(new Zombie("ZombieTutorialArmor1Default", "Conehead Zombie",
            190, 0.185f, 100, new String[]{"ConeDefault"},
            "Conehead zombie; cone armor absorbs damage before health is affected.",
            getZombieImagePath("ZombieTutorialArmor1Default"), "All Chapters"));
        zombies.add(new Zombie("ZombieTutorialArmor2Default", "Buckethead Zombie",
            190, 0.185f, 100, new String[]{"BucketDefault"},
            "Buckethead zombie; stronger armor than Conehead.",
            getZombieImagePath("ZombieTutorialArmor2Default"), "All Chapters"));
        zombies.add(new Zombie("ZombieTutorialArmor4Default", "Brickhead Zombie",
            190, 0.185f, 100, new String[]{"BrickDefault"},
            "Brickhead zombie; even tougher armor.",
            getZombieImagePath("ZombieTutorialArmor4Default"), "All Chapters"));
        zombies.add(new Zombie("ZombieGargantuarBasic", "Gargantuar",
            3600, 0.24f, 0, new String[]{},
            "Huge zombie; smashes plants instantly and throws an Imp when damaged or near the house.",
            getZombieImagePath("ZombieGargantuarBasic"), "All Chapters"));
        zombies.add(new Zombie("ZombieTutorialImpDefault", "Imp",
            190, 0.22f, 100, new String[]{},
            "Small, fast zombie thrown by Gargantuar; runs after landing.",
            getZombieImagePath("ZombieTutorialImpDefault"), "All Chapters"));
        zombies.add(new Zombie("ZombieTutorialFlagDefault", "Flag Zombie",
            190, 0.185f, 100, new String[]{},
            "Flag zombie; same as basic but used to mark wave starts.",
            getZombieImagePath("ZombieTutorialFlagDefault"), "All Chapters"));

        // ===== Ancient Egypt - 11 zombies (8-18) =====
        zombies.add(new Zombie("ZombieMummyDefault", "Mummy Zombie",
            190, 0.185f, 100, new String[]{},
            "Basic Egyptian zombie.",
            getZombieImagePath("ZombieMummyDefault"), "Ancient Egypt"));
        zombies.add(new Zombie("ZombieMummyArmor1Default", "Conehead Mummy",
            190, 0.185f, 100, new String[]{"ConeDefault"},
            "Conehead Egyptian zombie.",
            getZombieImagePath("ZombieMummyArmor1Default"), "Ancient Egypt"));
        zombies.add(new Zombie("ZombieMummyArmor2Default", "Buckethead Mummy",
            190, 0.185f, 100, new String[]{"BucketDefault"},
            "Buckethead Egyptian zombie.",
            getZombieImagePath("ZombieMummyArmor2Default"), "Ancient Egypt"));
        zombies.add(new Zombie("ZombieMummyArmor4Default", "Brickhead Mummy",
            190, 0.185f, 100, new String[]{"BrickDefault"},
            "Brickhead Egyptian zombie.",
            getZombieImagePath("ZombieMummyArmor4Default"), "Ancient Egypt"));
        zombies.add(new Zombie("ZombiePharaohDefault", "Pharaoh Zombie",
            250, 0.15f, 100, new String[]{},
            "Walks slowly inside a sarcophagus; after it breaks, the zombie runs faster.",
            getZombieImagePath("ZombiePharaohDefault"), "Ancient Egypt"));
        zombies.add(new Zombie("ZombieRaDefault", "Ra Zombie",
            190, 0.2f, 100, new String[]{},
            "Steals sun from the player's reserve.",
            getZombieImagePath("ZombieRaDefault"), "Ancient Egypt"));
        zombies.add(new Zombie("ZombieExplorerDefault", "Explorer Zombie",
            250, 0.25f, 100, new String[]{},
            "Carries a torch that instantly destroys specific plants.",
            getZombieImagePath("ZombieExplorerDefault"), "Ancient Egypt"));
        zombies.add(new Zombie("ZombieTombRaiserDefault", "Tomb Raiser Zombie",
            380, 0.185f, 100, new String[]{},
            "Summons graves (tombs) on the lawn.",
            getZombieImagePath("ZombieTombRaiserDefault"), "Ancient Egypt"));
        zombies.add(new Zombie("ZombieCamelDefault", "Camel Zombie",
            300, 0.185f, 100, new String[]{},
            "Three-segment camel zombie; only the front segment eats.",
            getZombieImagePath("ZombieCamelDefault"), "Ancient Egypt"));
        zombies.add(new Zombie("ZombieEgyptGargantuar", "Egypt Gargantuar",
            3600, 0.24f, 0, new String[]{},
            "Egyptian Gargantuar; smashes plants and throws an Egyptian Imp.",
            getZombieImagePath("ZombieEgyptGargantuar"), "Ancient Egypt"));
        zombies.add(new Zombie("ZombieEgyptImpDefault", "Egypt Imp",
            190, 0.22f, 100, new String[]{},
            "Imp thrown by Egyptian Gargantuar.",
            getZombieImagePath("ZombieEgyptImpDefault"), "Ancient Egypt"));

        // ===== Frostbite Caves - 11 zombies (19-29) =====
        zombies.add(new Zombie("ZombieIceageDefault", "Ice Age Zombie",
            190, 0.185f, 100, new String[]{},
            "Basic ice-age zombie.",
            getZombieImagePath("ZombieIceageDefault"), "Frostbite Caves"));
        zombies.add(new Zombie("ZombieIceageArmor1Default", "Conehead Ice Age",
            190, 0.185f, 100, new String[]{"ConeDefault"},
            "Conehead ice-age zombie.",
            getZombieImagePath("ZombieIceageArmor1Default"), "Frostbite Caves"));
        zombies.add(new Zombie("ZombieIceageArmor2Default", "Buckethead Ice Age",
            190, 0.185f, 100, new String[]{"BucketDefault"},
            "Buckethead ice-age zombie.",
            getZombieImagePath("ZombieIceageArmor2Default"), "Frostbite Caves"));
        zombies.add(new Zombie("ZombieIceageArmor3Default", "Ice Block Zombie",
            190, 0.185f, 100, new String[]{"IceBlock"},
            "Wears an ice block as armor; the block also chills plants that attack it.",
            getZombieImagePath("ZombieIceageArmor3Default"), "Frostbite Caves"));
        zombies.add(new Zombie("ZombieIceAgeHunter", "Hunter Zombie",
            700, 0.12f, 100, new String[]{},
            "Ranged zombie; throws snowballs that chill or freeze plants.",
            getZombieImagePath("ZombieIceAgeHunter"), "Frostbite Caves"));
        zombies.add(new Zombie("ZombieIceAgeTroglobite", "Troglobite",
            470, 0.185f, 100, new String[]{},
            "Pushes ice blocks down the lane; blocks crush plants and absorb damage.",
            getZombieImagePath("ZombieIceAgeTroglobite"), "Frostbite Caves"));
        zombies.add(new Zombie("ZombieIceAgeDodo", "Dodo Zombie",
            490, 0.3f, 100, new String[]{},
            "Flies over certain plants and obstacles.",
            getZombieImagePath("ZombieIceAgeDodo"), "Frostbite Caves"));
        zombies.add(new Zombie("ZombieWeaselHoarderDefault", "Weasel Hoarder",
            300, 0.185f, 100, new String[]{},
            "When damaged, releases fast, weak weasels.",
            getZombieImagePath("ZombieWeaselHoarderDefault"), "Frostbite Caves"));
        zombies.add(new Zombie("ZombieWeaselDefault", "Weasel",
            100, 0.35f, 50, new String[]{},
            "The small, fast weasel unit released by Weasel Hoarder.",
            getZombieImagePath("ZombieWeaselDefault"), "Frostbite Caves"));
        zombies.add(new Zombie("ZombieIceAgeGargantuar", "Ice Age Gargantuar",
            3600, 0.24f, 0, new String[]{},
            "Gargantuar of Frostbite Caves; throws an ice-age Imp.",
            getZombieImagePath("ZombieIceAgeGargantuar"), "Frostbite Caves"));
        zombies.add(new Zombie("ZombieIceageImpDefault", "Ice Age Imp",
            190, 0.22f, 100, new String[]{},
            "Imp thrown by ice-age Gargantuar.",
            getZombieImagePath("ZombieIceageImpDefault"), "Frostbite Caves"));

        // ===== Big Wave Beach - 10 zombies (30-39) =====
        zombies.add(new Zombie("ZombieBeachDefault", "Beach Zombie",
            190, 0.185f, 100, new String[]{},
            "Basic beach zombie.",
            getZombieImagePath("ZombieBeachDefault"), "Big Wave Beach"));
        zombies.add(new Zombie("ZombieBeachArmor1Default", "Conehead Beach",
            190, 0.185f, 100, new String[]{"ConeDefault"},
            "Conehead beach zombie.",
            getZombieImagePath("ZombieBeachArmor1Default"), "Big Wave Beach"));
        zombies.add(new Zombie("ZombieBeachArmor2Default", "Buckethead Beach",
            190, 0.185f, 100, new String[]{"BucketDefault"},
            "Buckethead beach zombie.",
            getZombieImagePath("ZombieBeachArmor2Default"), "Big Wave Beach"));
        zombies.add(new Zombie("ZombieBeachSnorkel", "Snorkel Zombie",
            350, 0.185f, 100, new String[]{},
            "Walks underwater; invulnerable to most plants except when surfacing to eat.",
            getZombieImagePath("ZombieBeachSnorkel"), "Big Wave Beach"));
        zombies.add(new Zombie("ZombieBeachSurfer", "Surfer Zombie",
            400, 0.25f, 100, new String[]{},
            "Rides a surfboard; moves very fast and crushes plants.",
            getZombieImagePath("ZombieBeachSurfer"), "Big Wave Beach"));
        zombies.add(new Zombie("ZombieBeachFisherman", "Fisherman Zombie",
            1000, 0.185f, 100, new String[]{},
            "Casts a fishing line to hook plants and pull them toward himself.",
            getZombieImagePath("ZombieBeachFisherman"), "Big Wave Beach"));
        zombies.add(new Zombie("ZombieBeachOctopus", "Octopus Zombie",
            910, 0.12f, 100, new String[]{},
            "Throws octopi that disable plants.",
            getZombieImagePath("ZombieBeachOctopus"), "Big Wave Beach"));
        zombies.add(new Zombie("ZombieBeachGargantuar", "Beach Gargantuar",
            3600, 0.24f, 0, new String[]{},
            "Beach Gargantuar; throws a beach Imp.",
            getZombieImagePath("ZombieBeachGargantuar"), "Big Wave Beach"));
        zombies.add(new Zombie("ZombieBeachImpDefault", "Beach Imp",
            190, 0.22f, 100, new String[]{},
            "Imp thrown by beach Gargantuar.",
            getZombieImagePath("ZombieBeachImpDefault"), "Big Wave Beach"));
        zombies.add(new Zombie("ZombieBeachFastSwimmer", "Fast Swimmer",
            250, 0.3f, 80, new String[]{},
            "Swims quickly in water lanes but is slow on land.",
            getZombieImagePath("ZombieBeachFastSwimmer"), "Big Wave Beach"));

        // ===== Dark Ages - 10 zombies (40-49) =====
        zombies.add(new Zombie("ZombieDarkDefault", "Dark Ages Zombie",
            190, 0.185f, 100, new String[]{},
            "Basic Dark Ages zombie.",
            getZombieImagePath("ZombieDarkDefault"), "Dark Ages"));
        zombies.add(new Zombie("ZombieDarkArmor1Default", "Conehead Dark",
            190, 0.185f, 100, new String[]{"ConeDefault"},
            "Conehead Dark Ages zombie.",
            getZombieImagePath("ZombieDarkArmor1Default"), "Dark Ages"));
        zombies.add(new Zombie("ZombieDarkArmor2Default", "Buckethead Dark",
            190, 0.185f, 100, new String[]{"BucketDefault"},
            "Buckethead Dark Ages zombie.",
            getZombieImagePath("ZombieDarkArmor2Default"), "Dark Ages"));
        zombies.add(new Zombie("ZombieDarkArmor3Default", "Armored Dark Zombie",
            190, 0.185f, 100, new String[]{"ShoulderArmorDefault", "CrownDefault"},
            "Wears shoulder armor and a crown; higher durability.",
            getZombieImagePath("ZombieDarkArmor3Default"), "Dark Ages"));
        zombies.add(new Zombie("ZombieDarkArmor4Default", "Brickhead Dark",
            190, 0.185f, 100, new String[]{"BrickDefault"},
            "Brickhead Dark Ages zombie.",
            getZombieImagePath("ZombieDarkArmor4Default"), "Dark Ages"));
        zombies.add(new Zombie("ZombieWizardDefault", "Wizard Zombie",
            490, 0.12f, 100, new String[]{},
            "Casts a spell to transform plants into harmless sheep.",
            getZombieImagePath("ZombieWizardDefault"), "Dark Ages"));
        zombies.add(new Zombie("ZombieDarkJugglerDefault", "Juggler Zombie",
            420, 0.2f, 100, new String[]{},
            "Catches and reflects many projectiles back at plants.",
            getZombieImagePath("ZombieDarkJugglerDefault"), "Dark Ages"));
        zombies.add(new Zombie("ZombieDarkKing", "Dark King",
            1000, 0.185f, 100, new String[]{},
            "Buffs nearby Dark Ages zombies (e.g., increases speed or damage).",
            getZombieImagePath("ZombieDarkKing"), "Dark Ages"));
        zombies.add(new Zombie("ZombieDarkGargantuar", "Dark Gargantuar",
            3600, 0.24f, 0, new String[]{},
            "Dark Ages Gargantuar; throws a Dark Ages Imp.",
            getZombieImagePath("ZombieDarkGargantuar"), "Dark Ages"));
        zombies.add(new Zombie("ZombieDarkImpDefault", "Dark Imp",
            190, 0.22f, 100, new String[]{},
            "Imp thrown by Dark Ages Gargantuar.",
            getZombieImagePath("ZombieDarkImpDefault"), "Dark Ages"));

        // ===== Zombosses - 4 zombies (50-53) =====
        zombies.add(new Zombie("ZombieZombossMechEgypt", "Zomboss Egypt",
            4000, 0.1f, 0, new String[]{},
            "Zomboss of Ancient Egypt chapter. Fires rockets and spawns graves.",
            getZombieImagePath("ZombieZombossMechEgypt"), "Ancient Egypt"));
        zombies.add(new Zombie("ZombieZombossMechPirate", "Zomboss Pirate",
            5500, 0.1f, 0, new String[]{},
            "Zomboss of Pirate Seas chapter. Uses imp cannons and rushing attacks.",
            getZombieImagePath("ZombieZombossMechPirate"), "Pirate Seas"));
        zombies.add(new Zombie("ZombieZombossMechCowboy", "Zomboss Cowboy",
            6500, 0.1f, 0, new String[]{},
            "Zomboss of Wild West chapter. Uses fire attacks and spawns cowboys.",
            getZombieImagePath("ZombieZombossMechCowboy"), "Wild West"));
        zombies.add(new Zombie("ZombieZombossMechDark", "Zomboss Dark Ages",
            7000, 0.1f, 0, new String[]{},
            "Zomboss of Dark Ages chapter. Uses fire breath and fireballs.",
            getZombieImagePath("ZombieZombossMechDark"), "Dark Ages"));

        // ===== Extra Zombies - 8 zombies (54-61) =====
        zombies.add(new Zombie("ZombiePetDefault", "Pet Zombie",
            300, 0.1f, 100, new String[]{},
            "A smaller zombie pet that follows other zombies.",
            getZombieImagePath("ZombiePetDefault"), "All Chapters"));
        zombies.add(new Zombie("ZombieModernAllStarDefault", "All-Star Zombie",
            1100, 0.16f, 100, new String[]{},
            "Modern All-Star zombie with high health and damage.",
            getZombieImagePath("ZombieModernAllStarDefault"), "Modern Day"));
        zombies.add(new Zombie("ZombieModernNewspaperDefault", "Newspaper Zombie",
            460, 0.22f, 200, new String[]{"NewspaperDefault"},
            "Carries a newspaper; becomes enraged when it breaks.",
            getZombieImagePath("ZombieModernNewspaperDefault"), "Modern Day"));
        zombies.add(new Zombie("ZombieEightiesArcade", "Arcade Zombie",
            490, 0.19f, 100, new String[]{},
            "Pushes an arcade cabinet that spawns 8-bit zombies.",
            getZombieImagePath("ZombieEightiesArcade"), "Far Future"));
        zombies.add(new Zombie("ZombieLostCityJaneDefault", "Jane Zombie",
            350, 0.25f, 100, new String[]{},
            "Bounces projectiles back at plants.",
            getZombieImagePath("ZombieLostCityJaneDefault"), "Lost City"));
        zombies.add(new Zombie("ZombieCrystalSkullDefault", "Crystal Skull Zombie",
            250, 0.185f, 100, new String[]{},
            "Charges up a laser beam that deals massive damage.",
            getZombieImagePath("ZombieCrystalSkullDefault"), "Lost City"));
        zombies.add(new Zombie("ZombieProspectorDefault", "Prospector Zombie",
            190, 0.16f, 100, new String[]{},
            "Mines for treasure; stuns when hit.",
            getZombieImagePath("ZombieProspectorDefault"), "Wild West"));
        zombies.add(new Zombie("ZombiePianoDefault", "Piano Zombie",
            840, 0.12f, 4000, new String[]{},
            "Pushes a piano that crushes plants; very high damage.",
            getZombieImagePath("ZombiePianoDefault"), "All Chapters"));

        return zombies;
    }

    // ===== HELPER METHODS =====
    public static Plant getPlantById(List<Plant> plants, int id) {
        for (Plant p : plants) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public static Zombie getZombieByAlias(List<Zombie> zombies, String alias) {
        for (Zombie z : zombies) {
            if (z.getAlias().equals(alias)) return z;
        }
        return null;
    }

    public static List<Zombie> getZombiesByChapter(List<Zombie> zombies, String chapter) {
        List<Zombie> result = new ArrayList<>();
        for (Zombie z : zombies) {
            if (z.getChapter().equals(chapter)) {
                result.add(z);
            }
        }
        return result;
    }
}
