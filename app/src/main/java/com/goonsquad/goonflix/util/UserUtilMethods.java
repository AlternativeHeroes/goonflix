package com.goonsquad.goonflix.util;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * @author nick
 * @author koushik
 */
public class UserUtilMethods {

    public static boolean validateUserName(String user) {
        if (user == null) {
            return false;
        }
        String regex = "[a-zA-Z0-9\\._\\-]{3,}";
        return user.matches(regex);
    }


    public static boolean validateEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
}
