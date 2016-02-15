package com.goonsquad.goonflix.user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.goonsquad.goonflix.UserHomepage;
import com.goonsquad.goonflix.WelcomeScreen;

/**
 * "Singleton" "instance" information holder for uid of logged in user
 */
public class UserInfo {

    private static final String USER_INFO_PREFERENCES = "goonflix_user_info";
    private static String uid;

    public static void init(Activity source) {
        uid = source.getSharedPreferences("goonflix_user_info", 0).getString("uid", null);
        if (isLoggedIn()) {
            gotoUserHomepage(source);
        } else {
            gotoWelcomeScreen(source);
        }
    }

    public static boolean isLoggedIn() { return uid != null; }

    /**
     * Login the user, remembers the uid, moves to user homepage and prevents backtracking
     * @param uid
     * @param source
     */
    public static void login(String uid, Activity source) {
        if (isLoggedIn()) {
            throw new AlreadyLoggedInException();
        }
        // set the in-memory uid
        UserInfo.uid = uid;
        // save the uid to app's storage
        SharedPreferences.Editor editor = source.getSharedPreferences("goonflix_user_info", 0).edit();
        editor.putString("uid", uid);
        editor.commit();
        // move to user homepage
        gotoUserHomepage(source);
    }

    private static void gotoUserHomepage(Activity source) {
        // move to user homescreen
        Intent homescreen_intent = new Intent(source, UserHomepage.class);
        homescreen_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        source.startActivity(homescreen_intent);
    }

    /**
     * Logout the user, remove the uid, moves to welcome screen and prevents backtracking
     * @param source
     */
    public static void logout(Activity source) {
        // clear the in-memory uid
        UserInfo.uid = null;
        // save the uid to app's storage
        SharedPreferences.Editor editor = source.getSharedPreferences("goonflix_user_info", 0).edit();
        editor.remove("uid");
        editor.commit();
        // move to welcome screen
        gotoWelcomeScreen(source);
    }

    private static void gotoWelcomeScreen(Activity source) {
        // move to welcome screen
        Intent welcome_screen_intent = new Intent(source, WelcomeScreen.class);
        welcome_screen_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        source.startActivity(welcome_screen_intent);
    }
}
