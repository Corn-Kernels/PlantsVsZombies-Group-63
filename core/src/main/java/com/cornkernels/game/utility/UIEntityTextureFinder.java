package com.cornkernels.game.utility;

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
        PlantDef.CHERRY_BOMB1, "cherry_bomb"
    );

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
            throw new IllegalStateException(
                "No seed packet texture for " + plantDef + " (tried atlas region '" + regionName +
                    "'). Add an entry to UIEntityTextureFinder.REGION_NAME_OVERRIDES once you know the correct name."
            );
        }
        return new TextureRegion(found);
    }
}
