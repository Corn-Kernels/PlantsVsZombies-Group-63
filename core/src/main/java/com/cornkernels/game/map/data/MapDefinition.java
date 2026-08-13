package com.cornkernels.game.map.data;

public class MapDefinition {
    private final int levelId;
    private final String layoutTmxPath;
    private final MapSkin skin;

    public MapDefinition(int levelId, String layoutTmxPath, MapSkin skin) {
        this.levelId = levelId;
        this.layoutTmxPath = layoutTmxPath;
        this.skin = skin;
    }

    public String getLayoutTmxPath() {
        return layoutTmxPath;
    }

    public MapSkin getSkin() {
        return skin;
    }
}
