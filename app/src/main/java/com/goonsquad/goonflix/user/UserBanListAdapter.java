package com.goonsquad.goonflix.user;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by michael on 3/14/16.
 * An adapter to list users and if they are banned or not.
 */
public class UserBanListAdapter extends BaseAdapter {

    Firebase fb_users;
    final ArrayList<User> users;

    public UserBanListAdapter(Firebase fb_users) {
        this.fb_users = fb_users;
        this.users = new ArrayList<>();

        fb_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    users.add(User.parse(user));
                }
                UserBanListAdapter.this.notifyDataSetInvalidated();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public User getUser(int position) {
        return users.get(position);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view;
        if (convertView == null) {
            view = new TextView(parent.getContext());
        } else {
            view  = (TextView) convertView;
        }
        User user = users.get(position);
        view.setText(user.name);
        if (user.banned) {
            view.setBackgroundColor(0xffff0000);
            view.setTextColor(0xffffffff);
        } else {
            view.setBackgroundColor(0x000000000);
            view.setTextColor(0xff000000);
        }
        view.setPadding(15, 30, 15, 30);
        view.setTextSize(30);

        return view;
    }
}
