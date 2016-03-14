package com.goonsquad.goonflix;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.goonsquad.goonflix.user.UserInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton;
    private Firebase fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO move to utility method
        Firebase.setAndroidContext(this);
        fb = new Firebase("https://goonflix.firebaseio.com/");

        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);

        mPasswordView = (EditText) findViewById(R.id.login_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button forgot_pass_button = (Button) findViewById(R.id.login_recover_password);
        forgot_pass_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progress = ProgressDialog.show(LoginActivity.this, "Sending email", "please wait");
                fb.resetPassword(mEmailView.getText().toString(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Password Reset Successful")
                                .setMessage("An email with instructions has been sent to " + mEmailView.getText())
                                .create()
                                .show();
                        progress.dismiss();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Error resetting password")
                                .setMessage(firebaseError.getMessage())
                                .create()
                                .show();
                        progress.dismiss();
                    }
                });
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private boolean isAttemptingAuthentication = false;

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        if (isAttemptingAuthentication) {
            return;
        }
        isAttemptingAuthentication = true;
        mEmailSignInButton.setEnabled(false);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        final ProgressDialog login_spinner = ProgressDialog.show(LoginActivity.this, "Logging in", "Please wait");


        fb.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                final String uuid = authData.getUid();
                fb.child("users").child(uuid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        login_spinner.dismiss();
                        fb.removeEventListener(this);
                        boolean is_banned = dataSnapshot.hasChild("banned");
                        if (is_banned) {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setMessage("Sorry your account has been banned.")
                                    .setPositiveButton("Ok", null)
                                    .create()
                                    .show();
                            mEmailSignInButton.setEnabled(true);
                            isAttemptingAuthentication = false;
                        } else {
                            UserInfo.login(uuid, LoginActivity.this);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                login_spinner.dismiss();
                isAttemptingAuthentication = false;
                mEmailSignInButton.setEnabled(true);
                System.out.println(firebaseError);
                new AlertDialog.Builder(LoginActivity.this).setMessage(firebaseError.getMessage())
                        .setPositiveButton("Ok", null).create().show();
            }
        });
    }
}

