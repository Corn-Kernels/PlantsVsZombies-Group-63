package com.cornkernels.engine.utility.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import org.jspecify.annotations.NonNull;

public class TextRenderer {

    private final BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();
    private final Color color;

    public TextRenderer(BitmapFont font) {
        this(font, Color.WHITE);
    }

    public TextRenderer(@NonNull BitmapFont font, @NonNull Color color) {
        this.font = font;
        this.color = color;
    }

    public void draw(SpriteBatch batch, @NonNull Rectangle bounds, String text) {
        layout.setText(font, text, color, bounds.width, Align.center, false);
        float x = bounds.x;
        float y = bounds.y + bounds.height / 2f + layout.height / 2f;
        font.draw(batch, text, x, y);
    }
}
