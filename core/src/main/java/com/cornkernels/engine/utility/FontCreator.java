package com.cornkernels.engine.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

public class FontCreator {

    private static final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("")); // TODO: Add font name when available

    private static final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    private static final Map<Integer, BitmapFont> fonts = new HashMap<>();

    public static BitmapFont createFont(int fontSize) {

        if (fonts.containsKey(fontSize)) {
            return fonts.get(fontSize);
        }

        parameter.size = fontSize;
        parameter.color = Color.WHITE;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        BitmapFont font = generator.generateFont(parameter);
        fonts.put(fontSize, font);
        return font;
    }
}
