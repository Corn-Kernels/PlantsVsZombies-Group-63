package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.User;


public class RegisterScreen extends BaseScreen {

    private TextField usernameField;
    private TextField passwordField;
    private TextField confirmPasswordField;
    private TextField nicknameField;
    private TextField emailField;
    private SelectBox<String> genderSelectBox;
    private SelectBox<String> questionSelectBox;
    private TextField answerField;
    private Label errorLabel;

    public RegisterScreen(Main game) {
        super(game);
        buildUI();
    }
    private void buildUI(){
        Table table=new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel=new Label(" Register", skin);
        table.add(titleLabel).padBottom(20).row();

        table.add(new Label("Username:", skin)).left().padBottom(5).row();
        usernameField=new TextField("",skin);
        table.add(usernameField).width(250).height(40).padBottom(10).row();

        table.add(new Label("Password:", skin)).left().padBottom(5).row();
        passwordField=new TextField("",skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        table.add(passwordField).width(250).height(40).padBottom(10).row();

        table.add(new Label("Confirm Password:", skin)).left().padBottom(5).row();
        confirmPasswordField=new TextField("",skin);
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');
        table.add(confirmPasswordField).width(250).height(40).padBottom(10).row();

        table.add(new Label("Nickname:", skin)).left().padBottom(5).row();
        nicknameField=new TextField("",skin);
        table.add(nicknameField).width(250).height(40).padBottom(10).row();

        table.add(new Label("Email:", skin)).left().padBottom(5).row();
        emailField=new TextField("",skin);
        table.add(emailField).width(250).height(40).padBottom(10).row();

        table.add(new Label("Gender:", skin)).left().padBottom(5).row();
        genderSelectBox = new SelectBox<>(skin);
        genderSelectBox.setItems("male", "female");
        table.add(genderSelectBox).width(250).height(40).padBottom(10).row();

        table.add(new Label("Security Question:", skin)).left().padBottom(5).row();
        questionSelectBox = new SelectBox<>(skin);
        questionSelectBox.setItems(
            "What is your sister's name?",
            "What was your first pet?",
            "Where was your hometown?",
            "What was your best friend's name?"
        );
        table.add(questionSelectBox).width(250).height(40).padBottom(10).row();
        table.add(new Label("Answer:", skin)).left().padBottom(5).row();
        answerField = new TextField("", skin);
        table.add(answerField).width(250).height(40).padBottom(10).row();

        errorLabel = new Label("", skin);
        errorLabel.setColor(1, 0, 0, 1);
        table.add(errorLabel).padBottom(10).row();

        TextButton registerBtn = new TextButton("Register", skin);
        TextButton loginBtn = new TextButton("Go to Login", skin);

        table.add(registerBtn).width(200).height(50).padBottom(10).row();
        table.add(loginBtn).width(200).height(50).row();
        registerBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                handleRegister();
            }
        });
        loginBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                game.setScreen(new LoginScreen(game));
            }
        });
    }
    private void handleRegister(){
        String username=usernameField.getText().trim();
        String password=passwordField.getText().trim();
        String confirmPass=confirmPasswordField.getText().trim();
        String nickname=nicknameField.getText().trim();
        String email=emailField.getText().trim();
        String gender = genderSelectBox.getSelected();
        String answer = answerField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || nickname.isEmpty() || email.isEmpty()) {
            errorLabel.setText(" All fields are required!");
            return;
        }
        if (username.length() < 3 || !username.matches("^[a-zA-Z0-9-]+$")) {
            errorLabel.setText(" Invalid username! Use letters, numbers, and dashes.");
            return;
        }
        if (game.getStorageService().userExists(username)) {
            errorLabel.setText(" Username already exists!");
            return;
        }
        if (password.length() < 3) {
            errorLabel.setText(" Password must be at least 3 characters!");
            return;
        }
        if (!password.equals(confirmPass)) {
            errorLabel.setText(" Passwords do not match!");
            return;
        }
        if (nickname.length() < 3 || nickname.length() > 30) {
            errorLabel.setText(" Nickname must be 3-30 characters!");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            errorLabel.setText(" Invalid email format!");
            return;
        }
        if (!gender.equals("male") && !gender.equals("female")) {
            errorLabel.setText(" Gender must be 'male' or 'female'!");
            return;
        }
        User newUser=new User(username,password,nickname,email,gender);
        game.getStorageService().addUser(newUser);
        game.getStorageService().saveUsers();

        errorLabel.setColor(0, 1, 0, 1);
        errorLabel.setText(" Registered! Go to Login.");

        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        nicknameField.setText("");
        emailField.setText("");
        answerField.setText("");
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
}
