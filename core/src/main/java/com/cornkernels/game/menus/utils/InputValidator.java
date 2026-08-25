package com.cornkernels.game.menus.utils;

import java.util.regex.Pattern;
public class InputValidator{
    public static boolean isValidUsername(String username){
        if(username==null||username.isEmpty())return false;
        return username.matches("^[a-zA-Z0-9-]+$");
    }
    public static boolean isValidPassword(String password){
        return password!=null&&password.length()>=3;
    }
    public static boolean isValidNickname(String nickname){
        return nickname!=null&&nickname.length()>=3&&nickname.length()<=30;
    }
    public static boolean isValidEmail(String email){
        if(email==null)return false;
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(emailRegex,email);
    }
    public static boolean isValidGender(String gender){
        return gender!=null&&(gender.equals("male")||gender.equals("female"));
    }
}
