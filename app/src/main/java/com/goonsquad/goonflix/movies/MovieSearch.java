package com.goonsquad.goonflix.movies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.VolleyError;
import com.goonsquad.goonflix.R;
import com.goonsquad.goonflix.movies.rottentomatoes.RTMovie;
import com.goonsquad.goonflix.movies.rottentomatoes.RottenApi;

import java.util.List;

/**
 * Simple activity to search for movies and display a list
 * of them using the rotten tomatoes API
 */
public class MovieSearch extends ActionBarActivity {

    private MovieResultAdapter result_adapter;
    private RottenApi rotten_api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);

        result_adapter = new MovieResultAdapter(this);
        rotten_api = new RottenApi(this, "yedukp76ffytfuy24zsqk7f5");

        ListView results_list = (ListView) findViewById(R.id.movie_results);
        results_list.setAdapter(result_adapter);

        SearchView search_bar = (SearchView) findViewById(R.id.movie_search_input);
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final ProgressDialog spinner = ProgressDialog.show(MovieSearch.this, "Loading...", "Please wait");
                MovieSearch.this.rotten_api.search_movies(query, new RottenApi.Callback<List<RTMovie>>() {
                    @Override
                    public void success(List<RTMovie> data) {
                        MovieSearch.this.result_adapter.updateMovieList(data);
                        spinner.dismiss();
                    }

                    @Override
                    public void failure(VolleyError error) {
                        new AlertDialog.Builder(MovieSearch.this)
                            .setTitle("Error accessing Rotten Tomatoes")
                            .setMessage(error.getMessage())
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                        spinner.dismiss();
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // We don't care if your typing
                return false;
            }
        });

        results_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RTMovie movie = (RTMovie) parent.getAdapter().getItem(position);
                Intent view_movie = new Intent(MovieSearch.this, MovieView.class);
                view_movie.putExtra("movie", movie);
                startActivity(view_movie);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        rotten_api.destroy();
    }
}
