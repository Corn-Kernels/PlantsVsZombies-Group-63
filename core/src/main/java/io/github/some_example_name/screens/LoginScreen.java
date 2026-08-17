package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.User;

public class LoginScreen extends BaseScreen{
    private TextField usernameField;
    private TextField passwordField;
    private CheckBox stayLoggedInCheckBox;
    private Label errorLabel;

    public LoginScreen(Main game){
        super(game);
        buildUI();
    }
    private void buildUI(){
        Table table=new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel=new Label(" Login", skin);
        table.add(titleLabel).padBottom(20).row();
        table.add(new Label("Username:", skin)).left().padBottom(5).row();
        usernameField=new TextField("",skin);
        table.add(usernameField).width(250).height(40).padBottom(10).row();

        table.add(new Label("Password:", skin)).left().padBottom(5).row();
        passwordField=new TextField("",skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        table.add(passwordField).width(250).height(40).padBottom(10).row();

        stayLoggedInCheckBox = new CheckBox(" Stay Logged In", skin);
        table.add(stayLoggedInCheckBox).left().padBottom(10).row();

        errorLabel=new Label("",skin);
        errorLabel.setColor(1,0,0,1);
        table.add(errorLabel).padBottom(10).row();

        TextButton loginBtn=new TextButton("Login",skin);
        TextButton registerBtn=new TextButton("Go to Register", skin);

        table.add(loginBtn).width(250).height(50).padBottom(10).row();
        table.add(registerBtn).width(250).height(50).row();

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
    }
    private void handleLogin(){
        String username=usernameField.getText().trim();
        String password=passwordField.getText().trim();
        boolean stay = stayLoggedInCheckBox.isChecked();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText(" Enter username and password!");
            return;
        }
        User user=game.getStorageService().getUser(username);
        if(user==null){
            errorLabel.setText(" User does not exist!");
            return;
        }
        if(!user.getPassword().equals(password)){
            errorLabel.setText(" Incorrect password!");
            return;
        }
        user.setLoggedIn(true);
        game.setCurrentUser(user);
        game.getStorageService().saveUsers();
        game.setScreen(new MainMenuScreen(game, user));
    }
    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0.2f,0.5f,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

}
