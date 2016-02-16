package com.goonsquad.goonflix;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.goonsquad.goonflix.user.UserInfo;

public class UserHomepage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO create "AuthenticatedActivity" super class with this logic
        if (!UserInfo.isLoggedIn()) {
            System.out.println("User not logged in. Cannot access this Activity");
            throw new AssertionError("pepe inhaled too much jet fuel");
        }

        setContentView(R.layout.activity_user_homepage);

        Button logout_button = (Button) findViewById(R.id.homepage_logout);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.logout(UserHomepage.this);
            }
        });

        Button edit_profile_button = (Button) findViewById(R.id.homepage_edit_profile);
        edit_profile_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // go to user homescreen
                Intent edit_profile_intent = new Intent(UserHomepage.this, EditUserProfile.class);
                startActivity(edit_profile_intent);
            }
        });
    }
}
