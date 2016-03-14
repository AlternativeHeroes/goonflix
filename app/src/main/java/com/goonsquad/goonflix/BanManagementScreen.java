package com.goonsquad.goonflix;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.goonsquad.goonflix.user.User;
import com.goonsquad.goonflix.user.UserBanListAdapter;

public class BanManagementScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_management_screen);
        Firebase.setAndroidContext(this);

        final Firebase fb_users = new Firebase("https://goonflix.firebaseio.com/").child("users");
        final UserBanListAdapter user_list = new UserBanListAdapter(fb_users);

        ListView users_list = (ListView) findViewById(R.id.banman_list);

        users_list.setAdapter(user_list);
        users_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = user_list.getUser(position);
                Firebase is_banned = fb_users.child(user.uuid).child("banned");

                final ProgressDialog spinner = ProgressDialog.show(
                        BanManagementScreen.this,
                        "Loading...",
                        "Please wait");
                Firebase.CompletionListener callback = new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        spinner.dismiss();
                    }
                };

                if (user.banned) {
                    is_banned.removeValue(callback);
                } else {
                    is_banned.setValue(true, callback);
                }
            }
        });
    }
}
