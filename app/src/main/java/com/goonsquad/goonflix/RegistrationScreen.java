package com.goonsquad.goonflix;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.goonsquad.goonflix.user.UserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Screen to register a new user, log this user in, and save his credentials to the db
 */
public class RegistrationScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO create "UnauthenticatedActivity" super class with this logic
        if (UserInfo.isLoggedIn()) {
            System.out.println("User is logged in. Cannot access this Activity");
            throw new AssertionError("pepe ran out of jet fuel");
        }

        setContentView(R.layout.activity_registration_screen);

        Firebase.setAndroidContext(this);
        final Firebase fb = new Firebase("https://goonflix.firebaseio.com/");

        // add submit button listener
        Button submit = (Button) this.findViewById(R.id.reg_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog reg_spinner = ProgressDialog.show(RegistrationScreen.this, "Creating new user", "Please wait");

                final String name = ((TextView)RegistrationScreen.this.findViewById(R.id.reg_name)).getText().toString();
                final String email = ((TextView)RegistrationScreen.this.findViewById(R.id.reg_email)).getText().toString();
                final String password = ((TextView)RegistrationScreen.this.findViewById(R.id.reg_password)).getText().toString();

                fb.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        final String uid = (String) result.get("uid");
                        System.out.println("Successfully created user account with uid: " + uid);
                        Map<String, String> user_info = new HashMap<String, String>();
                        user_info.put("name", name);
                        user_info.put("email", email);

                        fb.child("users").child(uid).setValue(user_info, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError != null) {
                                    // TODO alert user
                                    System.out.println("ayyy failed on user info save");
                                    System.out.println(firebaseError);
                                } else {
                                    reg_spinner.dismiss();
                                    // if no error, remember login, jump to user homepage
                                    UserInfo.login(uid, RegistrationScreen.this);
                                }
                            }
                        });

                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        System.out.println(firebaseError);
                        reg_spinner.dismiss();
                        new AlertDialog.Builder(RegistrationScreen.this).setMessage(firebaseError.getMessage())
                                .setPositiveButton("Ok", null).create().show();
                    }
                });
            }
        });

    }
}
