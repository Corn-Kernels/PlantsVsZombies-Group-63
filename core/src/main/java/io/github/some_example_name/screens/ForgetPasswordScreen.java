package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.Main;
import io.github.some_example_name.model.User;

public class ForgetPasswordScreen extends BaseScreen {

    private Main game;
    private TextField usernameField;
    private TextField emailField;
    private TextField answerField;
    private TextField newPasswordField;
    private TextField confirmPasswordField;
    private Label statusLabel;

    private User targetUser;
    private boolean step1Complete = false;

    private static final String[] SECURITY_QUESTIONS = {
        "What is your sister's name?",
        "What was your first pet?",
        "Where was your hometown?",
        "What was your best friend's name?"
    };

    public ForgetPasswordScreen(Main game) {
        super(game);
        this.game = game;
        buildUI();
    }

    private void buildUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabel = new Label(" Forget Password", skin);
        mainTable.add(titleLabel).padBottom(20).row();

        Table step1Table = new Table();

        step1Table.add(new Label("Username:", skin)).left().padBottom(5).row();
        usernameField = new TextField("", skin);
        step1Table.add(usernameField).width(250).height(40).padBottom(10).row();

        step1Table.add(new Label("Email:", skin)).left().padBottom(5).row();
        emailField = new TextField("", skin);
        step1Table.add(emailField).width(250).height(40).padBottom(10).row();

        TextButton verifyBtn = new TextButton("Verify", skin, "default");
        step1Table.add(verifyBtn).width(150).height(40).padBottom(20).row();

        mainTable.add(step1Table).row();

        Table step2Table = new Table();
        step2Table.setVisible(false);

        Label questionLabel = new Label("", skin);
        step2Table.add(questionLabel).padBottom(10).row();

        step2Table.add(new Label("Answer:", skin)).left().padBottom(5).row();
        answerField = new TextField("", skin);
        step2Table.add(answerField).width(250).height(40).padBottom(10).row();

        step2Table.add(new Label("New Password:", skin)).left().padBottom(5).row();
        newPasswordField = new TextField("", skin);
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');
        step2Table.add(newPasswordField).width(250).height(40).padBottom(10).row();

        step2Table.add(new Label("Confirm Password:", skin)).left().padBottom(5).row();
        confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');
        step2Table.add(confirmPasswordField).width(250).height(40).padBottom(10).row();

        TextButton resetBtn = new TextButton("Reset Password", skin, "green");
        step2Table.add(resetBtn).width(150).height(40).padBottom(20).row();

        mainTable.add(step2Table).row();

        statusLabel = new Label("", skin);
        statusLabel.setColor(1, 1, 1, 1);
        mainTable.add(statusLabel).padBottom(10).row();

        TextButton backBtn = new TextButton(" Back to Login", skin, "default");
        mainTable.add(backBtn).width(150).height(50).row();


        verifyBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleVerify(step2Table, questionLabel);
            }
        });
        resetBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleReset();
            }
        });
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoginScreen(game));
            }
        });
    }
    private void handleVerify(Table step2Table, Label questionLabel) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || email.isEmpty()) {
            statusLabel.setText(" Enter username and email!");
            statusLabel.setColor(1, 0, 0, 1);
            return;
        }

        targetUser = game.getStorageService().getUser(username);
        if (targetUser == null) {
            statusLabel.setText(" User not found!");
            statusLabel.setColor(1, 0, 0, 1);
            return;
        }

        if (!targetUser.getEmail().equals(email)) {
            statusLabel.setText(" Email does not match!");
            statusLabel.setColor(1, 0, 0, 1);
            return;
        }

        // ===== نمایش سوال امنیتی =====
        int qIndex = targetUser.getSecurityQuestionIndex();
        questionLabel.setText("🔐 " + SECURITY_QUESTIONS[qIndex]);
        step2Table.setVisible(true);
        statusLabel.setText(" User verified! Answer the security question.");
        statusLabel.setColor(0, 1, 0, 1);
        step1Complete = true;
    }

    private void handleReset() {
        if (!step1Complete || targetUser == null) {
            statusLabel.setText(" Please verify your identity first!");
            statusLabel.setColor(1, 0, 0, 1);
            return;
        }
        String answer = answerField.getText().trim();
        String newPass = newPasswordField.getText().trim();
        String confirmPass = confirmPasswordField.getText().trim();

        if (answer.isEmpty()) {
            statusLabel.setText(" Please answer the security question!");
            statusLabel.setColor(1, 0, 0, 1);
            return;
        }
        if (!answer.equalsIgnoreCase(targetUser.getSecurityAnswer())) {
            statusLabel.setText(" Wrong answer!");
            statusLabel.setColor(1, 0, 0, 1);
            return;
        }

        if (newPass.length() < 3) {
            statusLabel.setText(" Password must be at least 3 characters!");
            statusLabel.setColor(1, 0, 0, 1);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            statusLabel.setText(" Passwords do not match!");
            statusLabel.setColor(1, 0, 0, 1);
            return;
        }

        // ===== تغییر رمز =====
        targetUser.setPassword(newPass);
        game.getStorageService().saveUsers();

        statusLabel.setText(" Password changed successfully!");
        statusLabel.setColor(0, 1, 0, 1);

        usernameField.setText("");
        emailField.setText("");
        answerField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");

        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new LoginScreen(game));
            }
        }, 2);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
}
