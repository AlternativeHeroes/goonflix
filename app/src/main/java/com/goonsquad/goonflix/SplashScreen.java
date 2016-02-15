package com.goonsquad.goonflix;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.goonsquad.goonflix.user.UserInfo;

public class SplashScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // load previous login if available (might go directly to user homepage)
        UserInfo.init(this);
    }
}
