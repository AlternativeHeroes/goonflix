package com.goonsquad.goonflix;

import com.goonsquad.goonflix.util.UserUtilMethods;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Nick on 4/9/2016.
 */
public class userNameRegexTest {
    @Test
    public void username_isCorrect() throws Exception {
        assertEquals(false, UserUtilMethods.validateUserName(" "));
        assertEquals(false, UserUtilMethods.validateUserName(""));
        assertEquals(false, UserUtilMethods.validateUserName(null));
        assertEquals(true, UserUtilMethods.validateUserName("--123-"));
        assertEquals(true, UserUtilMethods.validateUserName("nick"));
        assertEquals(true, UserUtilMethods.validateUserName("andrew"));
        assertEquals(false, UserUtilMethods.validateUserName("~michael"));

    }
}
