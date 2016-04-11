package com.goonsquad.goonflix;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.goonsquad.goonflix.user.UserInfo;

import java.util.HashMap;
import java.util.Map;


/**
 * Activity for editing the user profile. This screen can also change
 * passwords and emails.
 */
public class EditUserProfile extends ActionBarActivity {

    private String email;
    static final String emailString = "email";
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        // TODO move to utility method
        Firebase.setAndroidContext(this);
        final Firebase fb = new Firebase("https://goonflix.firebaseio.com/").child("users").child(UserInfo.getUid());

        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                populate((Map<String, String>) data.getValue());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        Button save = (Button) findViewById(R.id.editprofile_save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final ProgressDialog spinner = ProgressDialog.show(EditUserProfile.this, "Updating...", "please wait");
                final Map<String, String> info = getData();
                final CharSequence newPass = ((TextView) findViewById(R.id.editprofile_newpass)).getText();

                final boolean emailChanged = !email.equals(info.get(emailString));
                final boolean passChanged = newPass != null && newPass.length() > 0;

                if (emailChanged || passChanged) {
                    final EditText askPass = new EditText(EditUserProfile.this);
                    askPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    new AlertDialog.Builder(EditUserProfile.this)
                        .setTitle("Changing Email or Password requires Authentication")
                        .setView(askPass)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String pass = askPass.getText().toString();
                                if (passChanged) {
                                    fb.changePassword(email, pass, newPass.toString(), new Firebase.ResultHandler() {
                                        @Override
                                        public void onSuccess() {
                                            if (emailChanged) {
                                                fb.changeEmail(email, newPass.toString(), info.get(emailString), new Firebase.ResultHandler() {
                                                    @Override
                                                    public void onSuccess() {
                                                        // Finish email & pass
                                                        commitChanges(fb, info);
                                                        spinner.dismiss();
                                                    }

                                                    @Override
                                                    public void onError(FirebaseError firebaseError) {
                                                        // Finish pass, fail email
                                                        EditUserProfile.this.showError(firebaseError, "Password updated, email did not.");
                                                        spinner.dismiss();
                                                    }
                                                });
                                            } else {
                                                // Finish pass, no email
                                                commitChanges(fb, info);
                                                spinner.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onError(FirebaseError firebaseError) {
                                            // Fail pass, not updated new email
                                            EditUserProfile.this.showError(firebaseError, "Neither email nor password updated.");
                                            spinner.dismiss();
                                        }
                                    });
                                } else if (emailChanged) {
                                    fb.changeEmail(email, pass, info.get(emailString), new Firebase.ResultHandler() {
                                        @Override
                                        public void onSuccess() {
                                            // finish email, no new pass
                                            commitChanges(fb, info);
                                            spinner.dismiss();
                                        }

                                        @Override
                                        public void onError(FirebaseError firebaseError) {
                                            // Fail email, no new pass
                                            EditUserProfile.this.showError(firebaseError, "email was not updated.");
                                            spinner.dismiss();
                                        }
                                    });
                                }
                            }
                        })
                        .create()
                        .show();
                } else {
                    commitChanges(fb, info);
                }
            }
        });
    }

    // TODO: Make into a util method
    private void showError(FirebaseError fbError, String title) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(fbError.getMessage())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }

    private void commitChanges(Firebase fb, Map<String, String> info) {
        fb.setValue(info, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    new AlertDialog.Builder(EditUserProfile.this).setMessage(firebaseError.getMessage())
                            .setPositiveButton("Ok", null).create().show();
                } else {
                    EditUserProfile.this.finish();
                }
            }
        });
    }

    private void populate (Map<String, String> data) {
        //System.out.println(data);
        ((TextView) findViewById(R.id.editprofile_name)).setText(data.get("name"));
        ((TextView) findViewById(R.id.editprofile_email)).setText(data.get("email"));
        ((TextView) findViewById(R.id.editprofile_major)).setText(data.get("major"));
        ((TextView) findViewById(R.id.editprofile_interests)).setText(data.get("interests"));

        this.email = data.get(emailString);
    }

    private Map<String, String> getData() {
        Map<String, String> info = new HashMap<String, String>();
        info.put("name", ((TextView) findViewById(R.id.editprofile_name)).getText().toString());
        info.put("email", ((TextView) findViewById(R.id.editprofile_email)).getText().toString());
        info.put("major", ((TextView) findViewById(R.id.editprofile_major)).getText().toString());
        info.put("interests", ((TextView) findViewById(R.id.editprofile_interests)).getText().toString());
        return info;
    }

    public static boolean validateMajor(String major) {
        if (major == null) {
            return false;
        }
        String regex = "([A-Z][a-z]+(?=\\s[A-Z])(?:\\s[A-Z][a-z]+)+)";
        return major.matches(regex);
    }
}
