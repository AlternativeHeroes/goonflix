package com.goonsquad.goonflix.util;
import org.junit.Test;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
/**
 * Created by Nick on 4/4/2016.
 */
public class UnitTesting {
    @Test
    public final void UserRegexTester(){
        assertEquals(UserUtilMethods.validateUserName(null), false);
        assertEquals(UserUtilMethods.validateUserName("12"), false);
        assertEquals(UserUtilMethods.validateUserName(" "), false);
        assertEquals(UserUtilMethods.validateUserName("michael123"), true);
        assertEquals(UserUtilMethods.validateUserName("nick78"), true);
        assertEquals(UserUtilMethods.validateUserName("-wqe"), false);

    }

}
