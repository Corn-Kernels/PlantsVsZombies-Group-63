package com.cornkernels.game.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.cornkernels.game.systems.controller.PauseController;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class PauseMenu extends WidgetGroup {

    private static final float PANEL_WIDTH = 420f;
    private static final float TOPPER_WIDTH = 360f;
    private static final float BUTTON_WIDTH = 240f;
    private static final float BUTTON_HEIGHT = 64f;
    private static final float BUTTON_GAP = 18f;
    private static final float TOP_INSET = 70f;
    private static final float BOTTOM_INSET = 30f;
    private static final float TITLE_GAP = 14f;
    private static final float TITLE_HEIGHT = 50f;

    private final Image background;
    private final Image windowTopper;
    private final Label titleLabel;
    private final List<TextButton> buttons;
    private final PauseController pauseController;

    private final float topperHeight;
    private final float panelHeight;

    public PauseMenu(Image background, @NonNull Image windowTopper, Label titleLabel,
                     @NonNull List<TextButton> buttons, PauseController pauseController) {
        this.background = background;
        this.windowTopper = windowTopper;
        this.titleLabel = titleLabel;
        this.buttons = buttons;
        this.pauseController = pauseController;
        this.topperHeight = TOPPER_WIDTH * (windowTopper.getPrefHeight() / windowTopper.getPrefWidth());
        this.panelHeight = TOP_INSET + buttons.size() * BUTTON_HEIGHT
            + Math.max(buttons.size() - 1, 0) * BUTTON_GAP + BOTTOM_INSET;

        addActor(background);
        addActor(windowTopper);
        for (TextButton button : buttons) {
            addActor(button);
        }
        addActor(titleLabel);

        setSize(PANEL_WIDTH, panelHeight + topperHeight / 2f + TITLE_GAP + TITLE_HEIGHT);
        setVisible(false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setVisible(pauseController.isPaused());
    }

    @Override
    public void layout() {
        background.setBounds(0f, 0f, PANEL_WIDTH, panelHeight);
        windowTopper.setBounds((PANEL_WIDTH - TOPPER_WIDTH) / 2f, panelHeight - topperHeight / 2f,
            TOPPER_WIDTH, topperHeight);
        titleLabel.setBounds(0f, panelHeight + topperHeight / 2f + TITLE_GAP, PANEL_WIDTH, TITLE_HEIGHT);

        float top = panelHeight - TOP_INSET;
        for (int i = 0; i < buttons.size(); i++) {
            float y = top - (i + 1) * BUTTON_HEIGHT - i * BUTTON_GAP;
            buttons.get(i).setBounds((PANEL_WIDTH - BUTTON_WIDTH) / 2f, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        }
        super.layout();
    }
}
