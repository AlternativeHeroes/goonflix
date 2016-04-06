package com.goonsquad.goonflix;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Shows when the user is not logged in, has options to
 * register as a new user or log into another user
 */
public class WelcomeScreen extends ActionBarActivity {

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
