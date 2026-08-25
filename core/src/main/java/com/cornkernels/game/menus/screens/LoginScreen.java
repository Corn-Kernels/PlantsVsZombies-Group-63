package com.cornkernels.game.menus.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cornkernels.GameManager;
import com.cornkernels.game.menus.model.User;

public class LoginScreen extends BaseScreen {

    private TextField usernameField;
    private TextField passwordField;
    private CheckBox stayLoggedInCheckBox;

    public LoginScreen(GameManager game) {
        super(game);
        buildUI();
    }
    private void buildUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label(" Login", skin);
        table.add(titleLabel).padBottom(20).row();

        table.add(new Label("Username:", skin)).left().padBottom(5).row();
        usernameField = new TextField("", skin);
        table.add(usernameField).width(250).height(40).padBottom(10).row();

        table.add(new Label("Password:", skin)).left().padBottom(5).row();
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        table.add(passwordField).width(250).height(40).padBottom(10).row();

        stayLoggedInCheckBox = new CheckBox(" Stay Logged In", skin);
        table.add(stayLoggedInCheckBox).left().padBottom(10).row();

        TextButton loginBtn = new TextButton("Login", skin, "green");
        TextButton registerBtn = new TextButton("Go to Register", skin, "default");
        TextButton forgetPassBtn = new TextButton("Forget Password", skin, "default");  // ✅ اضافه شد

        table.add(loginBtn).width(200).height(50).padBottom(10).row();
        table.add(forgetPassBtn).width(200).height(50).padBottom(10).row();  // ✅
        table.add(registerBtn).width(200).height(50).row();

        loginBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleLogin();
            }
        });
        registerBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new RegisterScreen(game));
            }
        });
        forgetPassBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ForgetPasswordScreen(game));
            }
        });
    }
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        boolean stay = stayLoggedInCheckBox.isChecked();

        if (username.isEmpty() || password.isEmpty()) {
            showToast(" Enter username and password!", 2f, true);
            return;
        }
        User user = game.getStorageService().getUser(username);
        if (user == null) {
            showToast(" User does not exist!", 2f, true);
            return;
        }
        if (!user.getPassword().equals(password)) {
            showToast(" Incorrect password!", 2f, true);
            return;
        }
        user.setLoggedIn(true);
        user.setStayLoggedIn(stay);
        game.setCurrentUser(user);
        game.getStorageService().saveUsers();

        updateCurrencyDisplay();

        showToast(" Welcome " + user.getNickname() + "!", 2f, false);
        game.setScreen(new MainMenuScreen(game, user));
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
}
