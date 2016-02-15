package com.goonsquad.goonflix;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.goonsquad.goonflix.user.UserInfo;

public class WelcomeScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load previous login if available (might go directly to user homepage)
        UserInfo.init(this);

        setContentView(R.layout.activity_welcome_screen);

        Button login_button = (Button) this.findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(WelcomeScreen.this, LoginActivity.class);
                startActivity(login_intent);
            }
        });

        Button register_button = (Button) this.findViewById(R.id.register_button);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register_intent = new Intent(WelcomeScreen.this, RegistrationScreen.class);
                startActivity(register_intent);
            }
        });
    }
}
