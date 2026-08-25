package com.cornkernels.game.menus.model;

public class User {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String gender;
    private boolean isLoggedIn;
    private boolean stayLoggedIn;
    private PlayerProgress progress;
    private int securityQuestionIndex;
    private String securityAnswer;


    public User(String username, String password, String nickname, String email, String gender) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
        this.isLoggedIn = false;
        this.stayLoggedIn = false;
        this.progress = new PlayerProgress();
        this.securityQuestionIndex = 0;
        this.securityAnswer = "";
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getGender() { return gender; }
    public boolean isLoggedIn() { return isLoggedIn; }
    public void setLoggedIn(boolean loggedIn) { isLoggedIn = loggedIn; }
    public boolean isStayLoggedIn() { return stayLoggedIn; }
    public void setStayLoggedIn(boolean stayLoggedIn) { this.stayLoggedIn = stayLoggedIn; }
    public PlayerProgress getProgress() { return progress; }

    public int getSecurityQuestionIndex() { return securityQuestionIndex; }
    public void setSecurityQuestionIndex(int index) { this.securityQuestionIndex = index; }

    public String getSecurityAnswer() { return securityAnswer; }
    public void setSecurityAnswer(String answer) { this.securityAnswer = answer;}

    public void logout() {
        this.isLoggedIn = false;
        this.stayLoggedIn = false;
    }

}
