package com.goonsquad.goonflix.movies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.firebase.client.Firebase;
import com.goonsquad.goonflix.R;
import com.goonsquad.goonflix.movies.rottentomatoes.RTMovie;
import com.goonsquad.goonflix.movies.rottentomatoes.RottenApi;
import com.goonsquad.goonflix.user.UserInfo;

import java.util.List;

/**
 * This is a simple activity to display a list of movies
 * from a variety of data sources. Namely either new DVDs or
 * new Releases.
 */
public class MovieList extends ActionBarActivity {

    private MovieResultAdapter result_adapter;
    private RottenApi rotten_api;
    private int type;

    public static final int NEW_DVDS = 0;
    public static final int NEW_RELEASES = 1;
    public static final int OVERALL_RATED = 2;
    public static final int MAJOR_RATED = 3;
    public static final String DATA_SOURCE_TAG = "data-source";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        result_adapter = new MovieResultAdapter(this);
        rotten_api = new RottenApi(this, "yedukp76ffytfuy24zsqk7f5");

        ListView results_list = (ListView) findViewById(R.id.movielist_list);
        results_list.setAdapter(result_adapter);

        // Figure out what we are going to show.
        type = getIntent().getIntExtra(DATA_SOURCE_TAG, 0);

        results_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RTMovie movie = (RTMovie) parent.getAdapter().getItem(position);
                Intent view_movie = new Intent(MovieList.this, MovieView.class);
                view_movie.putExtra("movie", movie);
                startActivity(view_movie);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        final ProgressDialog spinner = ProgressDialog.show(this, "Loading...", "Please wait");

        final RottenApi.Callback<List<RTMovie>> callback = new RottenApi.Callback<List<RTMovie>>() {
            @Override
            public void success(List<RTMovie> data) {
                spinner.dismiss();
                result_adapter.updateMovieList(data);
            }

            @Override
            public void failure(VolleyError error) {
                new AlertDialog.Builder(MovieList.this)
                        .setTitle("Error accessing Rotten Tomatoes")
                        .setMessage(error.getMessage())
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
                spinner.dismiss();
            }
        };

        Firebase.setAndroidContext(this);
        Firebase fb = new Firebase("https://goonflix.firebaseio.com");

        if (type == NEW_DVDS) {
            rotten_api.new_dvds(callback);
        } else if (type == NEW_RELEASES) {
            rotten_api.new_releases(callback);
        } else if (type == OVERALL_RATED) {
            // TODO: get ids from FireBase
        } else if (type == MAJOR_RATED) {
            BestMovies.highestRatedByMajor(fb, UserInfo.getUid(), new RottenApi.Callback<List<Long>>() {
                @Override
                public void success(List<Long> data) {
                    rotten_api.fetch_movies(data, callback);
                }

                @Override
                public void failure(VolleyError error) {
                    callback.failure(error);
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        rotten_api.destroy();
    }
}
