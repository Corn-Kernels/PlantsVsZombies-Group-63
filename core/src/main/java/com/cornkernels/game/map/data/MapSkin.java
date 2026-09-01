package com.cornkernels.game.map.data;

import org.jetbrains.annotations.NotNull;

public enum MapSkin {
    DELAY_LOAD_BACKGROUND_FRONTLAWN_BIGBRAINZ(
        1, "maps/atlases/delay_load_background_frontlawn_bigbrainz.atlas"),
    EGYPT(2, "maps/atlases/egypt.atlas"),
    ICE_AGE(3, "maps/atlases/ice_age.atlas"),
    DARK_AGES(4, "maps/atlases/dark_ages.atlas");

    private final int chapter;
    private final String atlasPath;

    MapSkin(int chapter, String atlasPath) {
        this.chapter = chapter;
        this.atlasPath = atlasPath;
    }

    public static @NotNull MapSkin forChapter(int chapter) {
        for (MapSkin skin : values()) {
            if (skin.chapter == chapter) return skin;
        }
        return DELAY_LOAD_BACKGROUND_FRONTLAWN_BIGBRAINZ;
    }

    public String getAtlasPath() {
        return atlasPath;
    }
}
