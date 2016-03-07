package com.goonsquad.goonflix.movies.rottentomatoes;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.goonsquad.goonflix.util.ImageDownloader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michael on 2/24/16.
 * Represents a movie from Rotten Tomatoes
 */
public class RTMovie implements Parcelable{

    /**
     * The title of the movie
     */
    private final String title;
    /**
     * the rating of the movie (G, PG, PG-13, R, etc.)
     */
    private final String mpaa_rating;
    /**
     * The rotten tomatoes id of the movie
     */
    private final Long id;
    /**
     * the year the movie was released
     */
    private final Long year;

    private Bitmap thumbnail;

    protected RTMovie(Parcel in) {
        title = in.readString();
        mpaa_rating = in.readString();
        id = in.readLong();
        year = in.readLong();
        thumbnail = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<RTMovie> CREATOR = new Creator<RTMovie>() {
        @Override
        public RTMovie createFromParcel(Parcel in) {
            return new RTMovie(in);
        }

        @Override
        public RTMovie[] newArray(int size) {
            return new RTMovie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(mpaa_rating);
        dest.writeLong(id);
        dest.writeLong(year);
        dest.writeParcelable(thumbnail, PARCELABLE_WRITE_RETURN_VALUE);
    }

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
        String t_year = movie.getString("year");
        if (t_year.equals("")) {
            year = null;
        } else {
            year = Long.parseLong(movie.getString("year"));
        }
        mpaa_rating = movie.getString("mpaa_rating");
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

    public String getTitle() { return title; }
    public String getMPAARating() { return mpaa_rating; }
    public String getYearString() { return year == null ? "-" : year.toString(); }
    public Long getYear() { return year; }
    public Long getId() { return id; }

    /**
     * Get the thumbnail of this movie
     * @return the thumbnail as a Bitmap
     */
    public Bitmap getThumbnail() {
        return thumbnail;
    }
}
