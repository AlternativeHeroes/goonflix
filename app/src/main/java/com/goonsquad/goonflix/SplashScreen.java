package com.goonsquad.goonflix;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.goonsquad.goonflix.user.UserInfo;

/**
 * This class is a splash screen.
 * It will change to either the welcome screen or the user homepage
 * depending on if a user is logged in.
 */
public class SplashScreen extends ActionBarActivity {

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // load previous login if available (might go directly to user homepage)
        UserInfo.init(this);
    }
}
