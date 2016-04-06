package com.goonsquad.goonflix.user;

import com.firebase.client.DataSnapshot;

/**
 * Created by michael on 3/14/16.
 * User is an information holder to hold info about a user (for admin use)
 */
public final class User {
    private final String uuid;
    private final String name;
    private final boolean banned;

    private User(String p_uuid, String p_name, boolean p_banned) {
        this.uuid = p_uuid;
        this.name = p_name;
        this.banned = p_banned;
    }

    public static User parse(DataSnapshot data) {
        boolean is_banned = data.hasChild("banned");
        String name = data.child("name").getValue("".getClass());
        String uuid = data.getKey();
        return new User(uuid, name, is_banned);
    }

    public String getName() {
        return name;
    }

    public boolean getBanned() {
        return banned;
    }

    public String getUuid() {
        return uuid;
    }
}
