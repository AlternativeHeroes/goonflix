package com.goonsquad.goonflix.user;

import com.firebase.client.DataSnapshot;

/**
 * Created by michael on 3/14/16.
 * User as an information holder to hold info about a user (for admin use)
 */
public class User {
    public final String uuid;
    public final String name;
    public final boolean banned;

    private User(String uuid, String name, boolean banned) {
        this.uuid = uuid;
        this.name = name;
        this.banned = banned;
    }

    public static User parse(DataSnapshot data) {
        boolean is_banned = data.hasChild("banned");
        String name = data.child("name").getValue("".getClass());
        String uuid = data.getKey();
        return new User(uuid, name, is_banned);
    }
}
