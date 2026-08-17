package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.User;
import io.github.some_example_name.utils.InputValidator;

public class EditProfileScreen extends BaseScreen {

    private User user;
    private TextField usernameField;
    private TextField nicknameField;
    private TextField emailField;
    private TextField oldPasswordField;
    private TextField newPasswordField;
    private TextField confirmPasswordField;
    private Label statusLabel;

    public EditProfileScreen(Main game, User user) {
        super(game);
        this.user = user;
        buildUI();
        loadCurrentData();
    }

    private void loadCurrentData() {
        usernameField.setText(user.getUsername());
        nicknameField.setText(user.getNickname());
        emailField.setText(user.getEmail());
    }

    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabel = new Label(" Edit Profile", skin);
        mainTable.add(titleLabel).padBottom(20).row();

        mainTable.add(new Label("Username:", skin)).left().padBottom(5).row();
        usernameField = new TextField("", skin);
        mainTable.add(usernameField).width(250).height(40).padBottom(10).row();

        mainTable.add(new Label("Nickname:", skin)).left().padBottom(5).row();
        nicknameField = new TextField("", skin);
        mainTable.add(nicknameField).width(250).height(40).padBottom(10).row();

        mainTable.add(new Label("Email:", skin)).left().padBottom(5).row();
        emailField = new TextField("", skin);
        mainTable.add(emailField).width(250).height(40).padBottom(10).row();

        mainTable.add(new Label("--- Change Password ---", skin)).padBottom(10).row();

        mainTable.add(new Label("Old Password:", skin)).left().padBottom(5).row();
        oldPasswordField = new TextField("", skin);
        oldPasswordField.setPasswordMode(true);
        oldPasswordField.setPasswordCharacter('*');
        mainTable.add(oldPasswordField).width(250).height(40).padBottom(10).row();

        mainTable.add(new Label("New Password:", skin)).left().padBottom(5).row();
        newPasswordField = new TextField("", skin);
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');
        mainTable.add(newPasswordField).width(250).height(40).padBottom(10).row();

        mainTable.add(new Label("Confirm New Password:", skin)).left().padBottom(5).row();
        confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');
        mainTable.add(confirmPasswordField).width(250).height(40).padBottom(10).row();

        statusLabel = new Label("", skin);
        statusLabel.setColor(1, 1, 1, 1);
        mainTable.add(statusLabel).padBottom(10).row();
        TextButton saveBtn = new TextButton(" Save Changes", skin, "green");
        TextButton backBtn = new TextButton(" Cancel", skin, "default");

        Table buttonTable = new Table();
        buttonTable.add(saveBtn).width(150).height(50).padRight(10);
        buttonTable.add(backBtn).width(150).height(50);
        mainTable.add(buttonTable).row();

        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveChanges();
            }
        });
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ProfileScreen(game, user));
            }
        });
    }
    private void saveChanges() {
        String newUsername = usernameField.getText().trim();
        String newNickname = nicknameField.getText().trim();
        String newEmail = emailField.getText().trim();
        String oldPass = oldPasswordField.getText().trim();
        String newPass = newPasswordField.getText().trim();
        String confirmPass = confirmPasswordField.getText().trim();

        if (!newUsername.isEmpty() && !newUsername.equals(user.getUsername())) {
            if (!InputValidator.isValidUsername(newUsername)) {
                statusLabel.setText(" Invalid username!");
                statusLabel.setColor(1, 0, 0, 1);
                return;
            }
            if (game.getStorageService().userExists(newUsername)) {
                statusLabel.setText(" Username already exists!");
                statusLabel.setColor(1, 0, 0, 1);
                return;
            }
            game.getStorageService().getAllUsers().remove(user.getUsername());
            user.setUsername(newUsername);
            game.getStorageService().addUser(user);
        }
        if (!newNickname.isEmpty() && !newNickname.equals(user.getNickname())) {
            if (!InputValidator.isValidNickname(newNickname)) {
                statusLabel.setText(" Nickname must be 3-30 characters!");
                statusLabel.setColor(1, 0, 0, 1);
                return;
            }
            user.setNickname(newNickname);
        }
        if (!newEmail.isEmpty() && !newEmail.equals(user.getEmail())) {
            if (!InputValidator.isValidEmail(newEmail)) {
                statusLabel.setText(" Invalid email!");
                statusLabel.setColor(1, 0, 0, 1);
                return;
            }
            user.setEmail(newEmail);
        }
        if (!oldPass.isEmpty() || !newPass.isEmpty() || !confirmPass.isEmpty()) {
            if (!user.getPassword().equals(oldPass)) {
                statusLabel.setText(" Old password is incorrect!");
                statusLabel.setColor(1, 0, 0, 1);
                return;
            }
            if (newPass.length() < 3) {
                statusLabel.setText(" New password must be at least 3 characters!");
                statusLabel.setColor(1, 0, 0, 1);
                return;
            }
            if (!newPass.equals(confirmPass)) {
                statusLabel.setText(" Passwords do not match!");
                statusLabel.setColor(1, 0, 0, 1);
                return;
            }
            if (newPass.equals(oldPass)) {
                statusLabel.setText(" New password is same as old password!");
                statusLabel.setColor(1, 0, 0, 1);
                return;
            }
            user.setPassword(newPass);
        }
        game.getStorageService().saveUsers();
        statusLabel.setText(" Profile updated successfully!");
        statusLabel.setColor(0, 1, 0, 1);

        oldPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
        game.setScreen(new ProfileScreen(game, user));
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
}
