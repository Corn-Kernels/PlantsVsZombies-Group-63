package com.cornkernels.game.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cornkernels.game.entities.types.plants.PlantDef;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

public class UIEntityTextureFinder {

    private static final Map<PlantDef, String> REGION_NAME_OVERRIDES = Map.of(
        PlantDef.MEGA_GATLING_PEA1, "megagatling",
        PlantDef.MELON_PULT1, "Melonpult",
        PlantDef.CHERRY_BOMB1, "cherry_bomb",
        PlantDef.GOO_PEASHOOTER1, "poisonpeashooter"
    );

    private static final String FALLBACK_REGION = "empty_packet";

    private final TextureAtlas seedPacketsAtlas;
    private final Map<PlantDef, TextureRegion> cache = new HashMap<>();

    public UIEntityTextureFinder(TextureAtlas seedPacketsAtlas) {
        this.seedPacketsAtlas = seedPacketsAtlas;
    }

    private static @NonNull String slugify(@NonNull String plantName) {
        StringBuilder builder = new StringBuilder(plantName.length());
        for (char c : plantName.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                builder.append(Character.toLowerCase(c));
            }
        }
        return builder.toString();
    }

    public TextureRegion getPlantUITextureOf(PlantDef plantDef) {
        return cache.computeIfAbsent(plantDef, this::resolve);
    }

    @Contract("_ -> new")
    private @NonNull TextureRegion resolve(PlantDef plantDef) {
        String regionName = REGION_NAME_OVERRIDES.getOrDefault(plantDef, slugify(plantDef.getPlantName()));
        TextureAtlas.AtlasRegion found = seedPacketsAtlas.findRegion(regionName);

        if (found == null) {
            Gdx.app.error("UIEntityTextureFinder",
                "No seed packet texture for " + plantDef + " (tried atlas region '" + regionName +
                    "'). Add an entry to UIEntityTextureFinder.REGION_NAME_OVERRIDES once you know the " +
                    "correct name — falling back to '" + FALLBACK_REGION + "' for now.");
            found = seedPacketsAtlas.findRegion(FALLBACK_REGION);
            if (found == null) {
                throw new IllegalStateException(
                    "No seed packet texture for " + plantDef + ", and fallback region '" + FALLBACK_REGION +
                        "' is missing too — the seed packets atlas itself is broken.");
            }
        }
        return new TextureRegion(found);
    }
}
