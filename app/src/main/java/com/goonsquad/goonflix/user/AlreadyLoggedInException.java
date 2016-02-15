package com.goonsquad.goonflix.user;

public class AlreadyLoggedInException extends IllegalStateException {
    public AlreadyLoggedInException() {
        super("User is already logged in");
    }
}
