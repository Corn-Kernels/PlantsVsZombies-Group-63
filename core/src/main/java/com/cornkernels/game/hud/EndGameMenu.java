package com.cornkernels.game.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.BooleanSupplier;

public class EndGameMenu extends WidgetGroup {

    private static final float PANEL_WIDTH = 420f;
    private static final float BUTTON_WIDTH = 240f;
    private static final float BUTTON_HEIGHT = 64f;
    private static final float BUTTON_GAP = 18f;
    private static final float TOP_INSET = 40f;
    private static final float BOTTOM_INSET = 30f;
    private static final float TITLE_GAP = 14f;
    private static final float TITLE_HEIGHT = 50f;

    private final Image background;
    private final Label titleLabel;
    private final List<TextButton> buttons;
    private final BooleanSupplier visibleWhen;

    private final float panelHeight;

    public EndGameMenu(Image background, Label titleLabel, @NonNull List<TextButton> buttons,
                       BooleanSupplier visibleWhen) {
        this.background = background;
        this.titleLabel = titleLabel;
        this.buttons = buttons;
        this.visibleWhen = visibleWhen;
        this.panelHeight = TOP_INSET + buttons.size() * BUTTON_HEIGHT
            + Math.max(buttons.size() - 1, 0) * BUTTON_GAP + BOTTOM_INSET;

        addActor(background);
        for (TextButton button : buttons) {
            addActor(button);
        }
        addActor(titleLabel);

        setSize(PANEL_WIDTH, panelHeight + TITLE_GAP + TITLE_HEIGHT);
        setVisible(false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setVisible(visibleWhen.getAsBoolean());
    }

    @Override
    public void layout() {
        background.setBounds(0f, 0f, PANEL_WIDTH, panelHeight);
        titleLabel.setBounds(0f, panelHeight + TITLE_GAP, PANEL_WIDTH, TITLE_HEIGHT);

        float top = panelHeight - TOP_INSET;
        for (int i = 0; i < buttons.size(); i++) {
            float y = top - (i + 1) * BUTTON_HEIGHT - i * BUTTON_GAP;
            buttons.get(i).setBounds((PANEL_WIDTH - BUTTON_WIDTH) / 2f, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        }
        super.layout();
    }
}
