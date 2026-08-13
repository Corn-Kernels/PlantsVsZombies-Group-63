package com.cornkernels.game.map.data;

public enum MapSkin {
    DELAY_LOAD_BACKGROUND_FRONTLAWN_BIGBRAINZ(
        "maps/atlases/delay_load_background_frontlawn_bigbrainz.atlas");

    private final String atlasPath;

    MapSkin(String atlasPath) {
        this.atlasPath = atlasPath;
    }

    public String getAtlasPath() {
        return atlasPath;
    }
}
