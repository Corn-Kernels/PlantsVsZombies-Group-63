package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.User;
import io.github.some_example_name.model.PlayerProgress;

public class SettingsScreen extends BaseScreen {

    private User user;
    private SelectBox<String> difficultySelect;
    private Slider speedSlider;
    private Label speedLabel;
    private CheckBox gridCheck;
    private CheckBox debugCheck;
    private Label statusLabel;

    public SettingsScreen(Main game, User user) {
        super(game);
        this.user = user;
        buildUI();
        loadCurrentSettings();
    }
    private String getDifficultyLabel(int level) {
        switch(level) {
            case 1: return "Very Easy";
            case 2: return "Easy";
            case 3: return "Normal";
            case 4: return "Hard";
            case 5: return "Very Hard";
            default: return "Normal";
        }
    }
    private void loadCurrentSettings() {
        PlayerProgress progress = user.getProgress();
        int level = progress.getDifficultyLevel();

        String target = level + " - " + getDifficultyLabel(level);
        boolean found = false;
        for (String item : difficultySelect.getItems()) {
            if (item.equals(target)) {
                difficultySelect.setSelected(item);
                found = true;
                break;
            }
        }
        if (!found) {
            difficultySelect.setSelectedIndex(0);
        }
        speedSlider.setValue(progress.getGameSpeed());
        speedLabel.setText(String.format("%.1fx", progress.getGameSpeed()));
        gridCheck.setChecked(progress.isShowGrid());
        debugCheck.setChecked(progress.isDebugMode());
    }

    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabel = new Label(" SETTINGS", skin);
        titleLabel.setFontScale(1.5f);
        mainTable.add(titleLabel).padBottom(20).row();

        Table difficultyTable = new Table();
        Label diffLabel = new Label("Difficulty:", skin);
        diffLabel.setFontScale(1.1f);
        difficultyTable.add(diffLabel).left().padRight(20);

        difficultySelect = new SelectBox<>(skin);
        difficultySelect.setItems("1 - Very Easy", "2 - Easy", "3 - Normal", "4 - Hard", "5 - Very Hard");
        difficultySelect.setSelected("3 - Normal");
        difficultyTable.add(difficultySelect).width(150);
        mainTable.add(difficultyTable).padBottom(15).row();

        Table speedTable = new Table();
        Label speedLabelText = new Label("Game Speed:", skin);
        speedLabelText.setFontScale(1.1f);
        speedTable.add(speedLabelText).left().padRight(20);
        speedSlider = new Slider(1, 3, 0.5f, false, skin);
        speedSlider.setValue(1);
        speedTable.add(speedSlider).width(150);
        speedLabel = new Label("1.0x", skin);
        speedLabel.setFontScale(1.1f);
        speedTable.add(speedLabel).padLeft(10);
        mainTable.add(speedTable).padBottom(15).row();


        gridCheck = new CheckBox(" Show Grid", skin);
        debugCheck = new CheckBox(" Debug Mode", skin);
        gridCheck.getLabel().setFontScale(1.1f);
        debugCheck.getLabel().setFontScale(1.1f);
        mainTable.add(gridCheck).left().padBottom(5).row();
        mainTable.add(debugCheck).left().padBottom(20).row();

        statusLabel = new Label("", skin);
        statusLabel.setFontScale(1.1f);
        statusLabel.setColor(0, 1, 0, 1);
        mainTable.add(statusLabel).padBottom(10).row();

        TextButton backBtn = new TextButton(" Back", skin, "default");
        backBtn.getLabel().setFontScale(1.2f);
        mainTable.add(backBtn).width(150).height(50).row();

        difficultySelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selected = difficultySelect.getSelected();
                int level = Integer.parseInt(selected.substring(0, 1));
                statusLabel.setText(" Difficulty changed to " + level);
            }
        });

        speedSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float val = speedSlider.getValue();
                speedLabel.setText(String.format("%.1fx", val));
                speedLabel.setText(String.format("%.1fx", val));
                statusLabel.setText(" Speed changed to " + String.format("%.1fx", val));
                System.out.println("🔧 Speed Slider changed to: " + val);
            }
        });

        gridCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean checked = gridCheck.isChecked();
                statusLabel.setText(checked ? " Grid shown" : "Grid hidden");

            }
        });

        debugCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean checked = debugCheck.isChecked();
                statusLabel.setText(checked ? " Debug mode ON" : " Debug mode OFF");
            }
        });
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveSettings();
                game.setScreen(new MainMenuScreen(game, user));
            }
        });
    }

    private void saveSettings() {
        PlayerProgress progress = user.getProgress();
        String selected = difficultySelect.getSelected();
        int level = Integer.parseInt(difficultySelect.getSelected().substring(0, 1));
        float speed = speedSlider.getValue();
        boolean showGrid = gridCheck.isChecked();
        boolean debugMode = debugCheck.isChecked();

        progress.setDifficultyLevel(level);
        progress.setGameSpeed(speed);
        progress.setShowGrid(showGrid);
        progress.setDebugMode(debugMode);

        game.getStorageService().saveUsers();

        System.out.println(" Settings saved:");
        System.out.println("  Difficulty: " + level);
        System.out.println("  Speed: " + speed + "x");
        System.out.println("  Show Grid: " + showGrid);
        System.out.println("  Debug Mode: " + debugMode);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
}
