package com.goonsquad.goonflix.user;

/**
 * Exception thrown when handling the UserInfo singleton as if no user
 * was logged in, when actually a user is logged in.
 */
public class AlreadyLoggedInException extends IllegalStateException {
    public AlreadyLoggedInException() {
        super("User is already logged in");
    }
}
