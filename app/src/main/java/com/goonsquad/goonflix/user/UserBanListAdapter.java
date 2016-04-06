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
import java.util.List;

/**
 * Created by michael on 3/14/16.
 * An adapter to list users and if they are banned or not.
 */
public class UserBanListAdapter extends BaseAdapter {


    private final List users;
    public static final int COLOR1 = 0xffff0000;
    public static final int COLOR2 = 0xff000000;
    public static final int FIFTEEN = 15;
    public static final int THIRTY = 30;
    public UserBanListAdapter(Firebase fb_users) {

        this.users = new ArrayList<>();

        fb_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    if (!user.hasChild("admin")) {
                        users.add(User.parse(user));
                    }
                }
                UserBanListAdapter.this.notifyDataSetInvalidated();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public final User getUser(int position) {
        return (User) users.get(position);
    }

    @Override
    public final int getCount() {
        return users.size();
    }

    @Override
    public final Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public final long getItemId(int position) {
        return users.get(position).hashCode();
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        TextView view;
        if (convertView == null) {
            view = new TextView(parent.getContext());
        } else {
            view  = (TextView) convertView;
        }
        User user = getUser(position);
        view.setText(user.getName());
        if (user.getBanned()) {
            view.setBackgroundColor(COLOR1);
            view.setTextColor(0xffffffff);
        } else {
            view.setBackgroundColor(0x000000000);
            view.setTextColor(COLOR2);
        }
        view.setPadding(FIFTEEN, THIRTY, FIFTEEN, THIRTY);
        view.setTextSize(THIRTY);

        return view;
    }
}
