package com.goonsquad.goonflix;

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
            finish();
        }

        setContentView(R.layout.activity_user_homepage);

        Button logout_button = (Button) findViewById(R.id.logout_button);

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHomepage.this.finish();
            }
        });
    }
}
