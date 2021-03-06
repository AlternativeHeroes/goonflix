package com.goonsquad.goonflix.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.goonsquad.goonflix.R;
import com.goonsquad.goonflix.movies.rottentomatoes.RTMovie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 2/22/16.
 * Simple adapter to take a list of movies and render them on the screen.
 */
public class MovieResultAdapter extends BaseAdapter {

    private List<RTMovie> movies;
    private Context context;

    /**
     * Create an adapter for a list of movies.
     * This will take a list of Rotten Tomatoes movies and Display them in the UI.
     * @param p_context The context from which this is being run from.
     */
    public MovieResultAdapter(Context p_context) {
        super();
        movies = new ArrayList<>();
        this.context = p_context;
    }

    public final void updateMovieList(List<RTMovie> p_movies) {
        this.movies = p_movies;
        this.notifyDataSetInvalidated();
    }

    /**
     * Get the movie at the specified index
     * @param position index of the list item
     * @return the movie represented as an RTMovie
     */
    public final RTMovie getMovie(int position) {
        return movies.get(position);
    }

    @Override
    public final int getCount() {
        return movies.size();
    }

    @Override
    public final Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public final long getItemId(int position) {
        return movies.get(position).getId();
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RTMovie movie = movies.get(position);

        View movie_result;
        if (convertView == null) {
            // There is no old view, we must create one.
            movie_result = inflater.inflate(R.layout.movie_result, parent, false);
        } else {
            // Let's use an existing view.
            movie_result = convertView;
        }

        TextView result_title = (TextView) movie_result.findViewById(R.id.movieresult_title);
        result_title.setText(movie.getTitle());

        TextView result_year = (TextView) movie_result.findViewById(R.id.movieresult_year);
        result_year.setText(movie.getYearString());

        if (movie.getThumbnail() != null) {
            ImageView result_thumb = (ImageView) movie_result.findViewById(R.id.movieresult_thumb);
            result_thumb.setImageBitmap(movie.getThumbnail());
            result_thumb.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        return movie_result;
    }

}
