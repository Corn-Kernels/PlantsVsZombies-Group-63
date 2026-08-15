package com.cornkernels.game.hud.seeds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.cornkernels.game.systems.controller.plants.SeedSlot;
import org.jspecify.annotations.NonNull;

import java.util.function.BooleanSupplier;

public class SeedPacket extends WidgetGroup {

    public static final float ASPECT = 119f / 75f;

    private static final Color SELECTED_TINT = new Color(1f, 1f, 0.55f, 1f);
    private static final Color UNAVAILABLE_TINT = new Color(0.55f, 0.55f, 0.55f, 1f);

    private final SeedSlot seedSlot;
    private final Image background;
    private final Image plant;
    private final Image priceTab;
    private final Label priceLabel;
    private final CooldownOverlay cooldownOverlay;
    private final BitmapFont priceFont;

    private BooleanSupplier availableWhen = () -> true;
    private boolean selected;

    public SeedPacket(@NonNull SeedSlot seedSlot,
                      Drawable backgroundDrawable,
                      TextureRegion plantRegion,
                      Drawable priceTabDrawable,
                      BitmapFont priceFont,
                      Texture whitePixel) {
        this.seedSlot = seedSlot;
        this.priceFont = priceFont;

        this.background = new Image(backgroundDrawable);
        this.background.setScaling(Scaling.stretch);

        this.plant = new Image(new TextureRegionDrawable(plantRegion));
        this.plant.setScaling(Scaling.fit);
        this.plant.setAlign(Align.center);

        this.cooldownOverlay = new CooldownOverlay(whitePixel);

        this.priceTab = new Image(priceTabDrawable);
        this.priceTab.setScaling(Scaling.stretch);

        this.priceLabel = new Label(String.valueOf(seedSlot.getPlantDef().getCost()),
            new Label.LabelStyle(priceFont, Color.WHITE));
        this.priceLabel.setAlignment(Align.center);

        addActor(background);
        addActor(plant);
        addActor(cooldownOverlay);
        addActor(priceTab);
        addActor(priceLabel);
    }

    public void setAvailableWhen(BooleanSupplier availableWhen) {
        this.availableWhen = availableWhen;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public SeedSlot getSeedSlot() {
        return seedSlot;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        cooldownOverlay.setCooldownRatio(1f - seedSlot.getRechargeProgress());

        boolean available = seedSlot.isReady() && availableWhen.getAsBoolean();
        if (selected) {
            setColor(SELECTED_TINT);
        } else if (available) {
            setColor(Color.WHITE);
        } else {
            setColor(UNAVAILABLE_TINT);
        }
    }

    @Override
    public void layout() {
        float w = getWidth();
        float h = getHeight();

        background.setBounds(0f, 0f, w, h);

        float plantX = w * 0.08f;
        float plantY = h * 0.14f;
        float plantW = w * 0.84f;
        float plantH = h * 0.78f;
        plant.setBounds(plantX, plantY, plantW, plantH);
        cooldownOverlay.setBounds(plantX, plantY, plantW, plantH);

        float tabW = w * 0.44f;
        float tabH = h * 0.32f;
        float tabX = w - tabW;
        float tabY = 0f;
        priceTab.setBounds(tabX, tabY, tabW, tabH);

        float targetTextHeight = tabH * 0.85f;
        float lineHeight = priceFont.getLineHeight();
        priceLabel.setFontScale(lineHeight > 0f ? targetTextHeight / lineHeight : 1f);
        priceLabel.setBounds(tabX, tabY, tabW, tabH);

        super.layout();
    }

    @Override
    public float getPrefWidth() {
        return getWidth();
    }

    @Override
    public float getPrefHeight() {
        return getHeight();
    }
}
