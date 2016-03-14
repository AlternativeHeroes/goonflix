package com.goonsquad.goonflix.movies;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.goonsquad.goonflix.R;
import com.goonsquad.goonflix.movies.rottentomatoes.RTMovie;
import com.goonsquad.goonflix.user.UserInfo;

public class MovieView extends ActionBarActivity {

    RTMovie movie;

    Firebase fb, fb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);

        movie = (RTMovie) getIntent().getExtras().get("movie");

        Firebase.setAndroidContext(this);
        fb = new Firebase("https://goonflix.firebaseio.com/")
                .child("ratings")
                .child(movie.getId().toString())
                .child(UserInfo.getUid());
        fb2 = new Firebase("https://goonflix.firebaseio.com/") // IM SO SORRY
                .child("users")
                .child(UserInfo.getUid())
                .child("ratings")
                .child(movie.getId().toString());


        ((TextView) findViewById(R.id.movieview_title)).setText(movie.getTitle());
        ((TextView) findViewById(R.id.movieview_year)).setText(movie.getYearString());
        ((TextView) findViewById(R.id.movieview_mpaa_rating)).setText(movie.getMPAARating()); // this is not a bug
        ((ImageView) findViewById(R.id.movieview_image)).setImageBitmap(movie.getThumbnail());

        final RatingBar rating_bar = (RatingBar) findViewById(R.id.movieview_user_rating);

        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String val = (String) dataSnapshot.getValue();
                if (val != null) {
                    float user_rating = Float.parseFloat(val);
                    rating_bar.setRating(user_rating);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    // NOTE: im so sorry
                    // TODO fix this
                    fb.setValue(((Float) rating).toString());
                    fb2.setValue(((Float) rating).toString());
                }
            }
        });
    }
}
