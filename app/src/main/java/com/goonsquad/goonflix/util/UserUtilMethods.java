package com.goonsquad.goonflix.util;

/**
 * Created by Nick on 4/4/2016.
 */
public class UserUtilMethods {
    private UserUtilMethods(){}
    public static boolean validateUserName(String user) {
        if (user == null) {
            return false;
        }
        String regex = "[a-zA-Z0-9\\._\\-]{3,}";
        return user.matches(regex);
    }
}
