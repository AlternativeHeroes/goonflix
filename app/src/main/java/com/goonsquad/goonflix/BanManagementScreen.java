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
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_management_screen);
        Firebase.setAndroidContext(this);

        final Firebase fbUsers = new Firebase("https://goonflix.firebaseio.com/").child("users");
        final UserBanListAdapter userList = new UserBanListAdapter(fbUsers);

        ListView usersList = (ListView) findViewById(R.id.banman_list);

        usersList.setAdapter(userList);
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = userList.getUser(position);
                Firebase isBanned = fbUsers.child(user.getUuid()).child("banned");

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

                if (user.getBanned()) {
                    isBanned.removeValue(callback);
                } else {
                    isBanned.setValue(true, callback);
                }
            }
        });
    }
}
