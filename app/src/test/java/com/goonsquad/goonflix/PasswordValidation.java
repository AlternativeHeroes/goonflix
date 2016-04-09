package com.goonsquad.goonflix;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.goonsquad.goonflix.LoginActivity.PasswordError;

/**
 * Created by michael on 4/9/16.
 */
public class PasswordValidation {
    @Test
    public void nullPassword() {
        assertEquals(LoginActivity.validatePassword(null), PasswordError.IsNull);
    }

    @Test
    public void shortPassword() {
        assertEquals(LoginActivity.validatePassword("tiny"), PasswordError.TooShort);
    }

    @Test
    public void justLongEnough() {
        assertEquals(LoginActivity.validatePassword("janie"), PasswordError.None);
    }

    @Test
    public void containsSpecials() {
        assertEquals(LoginActivity.validatePassword("ฅ^•ﻌ•^ฅ"), PasswordError.ContainsSpecials);
    }

    @Test
    public void containsNumbers() {
        assertEquals(LoginActivity.validatePassword("e133t"), PasswordError.ContainsNumbers);
    }

    @Test
    public void withSpaces() {
        assertEquals(LoginActivity.validatePassword("small words"), PasswordError.None);
    }

    @Test
    public void withCapitalLetters() {
        assertEquals(LoginActivity.validatePassword("Fargo"), PasswordError.None);
    }

    @Test
    public void mostVariedCharsInPass() {
        assertEquals(LoginActivity.validatePassword("Better Call Saul"), PasswordError.None);
    }
}

