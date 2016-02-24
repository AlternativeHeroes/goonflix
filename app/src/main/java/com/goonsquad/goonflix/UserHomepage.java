package com.goonsquad.goonflix;

import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.goonsquad.goonflix.movies.MovieList;
import com.goonsquad.goonflix.movies.MovieSearch;
import com.goonsquad.goonflix.user.UserInfo;

/**
 * Homepage of the User, first thing shown when user is logged in
 */
public class UserHomepage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO create "AuthenticatedActivity" super class with this logic
        if (!UserInfo.isLoggedIn()) {
            System.out.println("User not logged in. Cannot access this Activity");
            throw new AssertionError("pepe inhaled too much jet fuel");
        }

        setContentView(R.layout.activity_user_homepage);

        Button logout_button = (Button) findViewById(R.id.homepage_logout);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.logout(UserHomepage.this);
            }
        });

        Button edit_profile_button = (Button) findViewById(R.id.homepage_edit_profile);
        edit_profile_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // go to user homescreen
                Intent edit_profile_intent = new Intent(UserHomepage.this, EditUserProfile.class);
                startActivity(edit_profile_intent);
            }
        });

        Button search_movies_button = (Button) findViewById(R.id.homepage_search);
        search_movies_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to search movies screen
                Intent search_movies_intent = new Intent(UserHomepage.this, MovieSearch.class);
                startActivity(search_movies_intent);
            }
        });

        Button new_releases_button = (Button) findViewById(R.id.homepage_newreleases);
        new_releases_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to new releases page
                Intent new_releases_intent = new Intent(UserHomepage.this, MovieList.class);
                new_releases_intent.putExtra(MovieList.DATA_SOURCE_TAG, MovieList.NEW_RELEASES);
                startActivity(new_releases_intent);
            }
        });

        Button new_dvds_button = (Button) findViewById(R.id.homepage_newdvds);
        new_dvds_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to new dvds button
                Intent new_dvds_intent = new Intent(UserHomepage.this, MovieList.class);
                new_dvds_intent.putExtra(MovieList.DATA_SOURCE_TAG, MovieList.NEW_DVDS);
                startActivity(new_dvds_intent);
            }
        });
    }
}

