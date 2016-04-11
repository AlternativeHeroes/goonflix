package com.goonsquad.goonflix;

import com.firebase.client.Firebase;
import com.goonsquad.goonflix.movies.MovieList;
import com.goonsquad.goonflix.util.UserUtilMethods;

import org.apache.commons.validator.routines.EmailValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * This class will generate a predetermined number of emails, some being valid
 * others invalid, and will use them to test the functionality of the
 * UserUtilMethods.validateEmail method.
 *
 * @author koushik
 */
public class EmailValidatorTest {

    private List<String> email_vectors;
    private List<Boolean> validation_vectors;
    private static int NUM_VECTORS = 1000;

    @Before
    public void setUp() {
        email_vectors = new ArrayList<>();
        validation_vectors = new ArrayList<>();

        String[] users = {
                "user1",
                "user2",
                "user3",
                "user 4",
                "user 5 5",
                "asdfa3 345 13 4tq34t65",
                "brian williams"
        };

        String[] at_symbol = {
                "",
                "@"
        };

        String[] domains = {
                "gmail.com",
                "hotmail.com",
                "this is a bad domain",
                "kkf.fok.com//kouahik67^^76jkjq4",
                "~~~haiii~~~.com"
        };

        for (int i = 0; i < NUM_VECTORS; i++) {
            // get a random index from ea
            int user_index = (int) Math.random() * users.length;
            int symbol_index = (int) Math.random() * at_symbol.length;
            int domain_index = (int) Math.random() * domains.length;

            boolean vaild = user_index < 3 && symbol_index == 1 && domain_index < 2;

            String email = users[user_index] + at_symbol[symbol_index] + domains[domain_index];

            email_vectors.add(email);
            validation_vectors.add(vaild);
        }
    }

    @Test
    public void testEmails() {
        for (int i = 0; i < NUM_VECTORS; i++) {
            String email_to_validate = email_vectors.get(i);
            boolean is_this_email_valid = validation_vectors.get(i);
            assertEquals(UserUtilMethods.validateEmail(email_to_validate), is_this_email_valid);
        }
    }

}
