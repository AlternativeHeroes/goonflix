package com.goonsquad.goonflix;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.goonsquad.goonflix.user.UserInfo;

import java.util.HashMap;
import java.util.Map;

public class EditUserProfile extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        // TODO move to utility method
        Firebase.setAndroidContext(this);
        final Firebase fb = new Firebase("https://goonflix.firebaseio.com/").child("users").child(UserInfo.getUid());

        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                populate(data.getValue(new GenericTypeIndicator<Map<String, String>>() {
                }));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        Button save = (Button) findViewById(R.id.editprofile_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> info = getData();
                // TODO spinner
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
        });
    }

    private void populate (Map<String, String> data) {
        System.out.println(data);
        ((TextView) findViewById(R.id.editprofile_name)).setText(data.get("name"));
        ((TextView) findViewById(R.id.editprofile_email)).setText(data.get("email"));
        ((TextView) findViewById(R.id.editprofile_major)).setText(data.get("major"));
        ((TextView) findViewById(R.id.editprofile_interests)).setText(data.get("interests"));

    }

    private Map<String, String> getData() {
        Map<String, String> info = new HashMap<String, String>();
        info.put("name", ((TextView) findViewById(R.id.editprofile_name)).getText().toString());
        info.put("email", ((TextView) findViewById(R.id.editprofile_email)).getText().toString());
        info.put("major", ((TextView) findViewById(R.id.editprofile_major)).getText().toString());
        info.put("interests", ((TextView) findViewById(R.id.editprofile_interests)).getText().toString());
        return info;
    }
}
