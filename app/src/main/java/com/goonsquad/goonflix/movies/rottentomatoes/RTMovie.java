package com.goonsquad.goonflix.movies.rottentomatoes;

import android.graphics.Bitmap;

import com.goonsquad.goonflix.util.ImageDownloader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 2/24/16.
 * Represents a movie from Rotten Tomatoes
 */
public class RTMovie {

    /**
     * The title of the movie
     */
    public final String title;
    /**
     * the rating of the movie (G, PG, PG-13, R, etc.)
     */
    public final String rating;
    /**
     * The rotten tomatoes id of the movie
     */
    public final long id;
    /**
     * the year the movie was released
     */
    public final long year;

    private Bitmap thumbnail;

    /**
     * A simple callback to notify user when this movie has finished
     * downloading extra assets from rotten tomatoes
     */
    public interface Callback {
        void finished();
    }

    /**
     * Build a new RTMovie
     * @param movie the JSONObject to parse
     * @param callback what to do when extra assets have finished downloading
     * @throws JSONException thrown when the JSON is not the expected scheme.
     */
    RTMovie(JSONObject movie, final Callback callback) throws JSONException {
        title = movie.getString("title");
        id = Long.parseLong(movie.getString("id"));
        year = movie.getLong("year");
        rating = movie.getString("mpaa_rating");

        ImageDownloader downloader = new ImageDownloader(new ImageDownloader.Callback() {
            @Override
            public void finished(Bitmap result) {
                RTMovie.this.thumbnail = result;
                callback.finished();
            }
        });
        String thumb = movie.getJSONObject("posters").getString("thumbnail");
        downloader.execute(thumb);
    }

    /**
     * Get the thumbnail of this movie
     * @return the thumbnail as a Bitmap
     */
    public Bitmap getThumbnail() {
        return thumbnail;
    }
}
