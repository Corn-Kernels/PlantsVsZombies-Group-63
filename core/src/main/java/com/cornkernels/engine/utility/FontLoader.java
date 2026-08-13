package com.cornkernels.engine.utility;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public final class FontLoader {

    private FontLoader() {
    }

    public static BitmapFont generate(FileHandle ttfFile, int sizePx, Color color) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(ttfFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = sizePx;
        parameter.color = color;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }
}
