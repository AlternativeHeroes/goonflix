package com.goonsquad.goonflix.user;

import android.app.Activity;
import android.content.Intent;

import com.goonsquad.goonflix.WelcomeScreen;

/**
 * "Singleton" "instance" information holder for uid of logged in user
 */
public class UserInfo {

    private static String uid;

    public static boolean isLoggedIn() { return uid != null; }

    public static void login(String uid) {
        if (isLoggedIn()) {
            throw new IllegalStateException("User is already logged in");
        }
        UserInfo.uid = uid;
    }

    public static void logout(Activity source) {
        uid = null;
        Intent welcome_screen_intent = new Intent(source, WelcomeScreen.class);
        source.startActivity(welcome_screen_intent);
    }
}
