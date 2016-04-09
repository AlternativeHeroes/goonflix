package com.goonsquad.goonflix;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by andrew on 4/9/16.
 */
public class MajorRegexTest {
    @Test
    public void major_isCorrect() throws Exception {
        assertEquals(false, EditUserProfile.validateMajor(null));
        assertEquals(false, EditUserProfile.validateMajor(" "));
        assertEquals(false, EditUserProfile.validateMajor(""));
        assertEquals(false, EditUserProfile.validateMajor("1ComputerScience"));
        assertEquals(false, EditUserProfile.validateMajor("~computerScience"));
        assertEquals(false, EditUserProfile.validateMajor("computer Science"));
        assertEquals(false, EditUserProfile.validateMajor("computer science"));
        assertEquals(true, EditUserProfile.validateMajor("Computer Science"));
        assertEquals(true, EditUserProfile.validateMajor("Aerospace Engineering"));
    }
}
