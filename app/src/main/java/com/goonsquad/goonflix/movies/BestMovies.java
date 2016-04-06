package com.goonsquad.goonflix.movies;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.goonsquad.goonflix.movies.rottentomatoes.RottenApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrew on 3/14/16.
 */
public class BestMovies {
    private BestMovies(){

    }
    /**
     * Fetch id's of highest rated movies by major
     * @param fb Firebase reference to the root
     * @param user_uuid user uuid
     * @return a list of Rotten Tomatoes movie ids
     */
    public static void highestRatedByMajor(final Firebase fb, final String user_uuid, final RottenApi.Callback<List<Long>> callback) {
        fb.child("users").child(user_uuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String major = (String) ((Map<String, Object>)dataSnapshot.getValue()).get("major");
                if (major == null) {
                    callback.success(new ArrayList<Long>(0));
                    fb.removeEventListener(this);
                    return;
                }
                final String ratingsString = "ratings";
                fb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Map<Long, List<Float>> all_ratings = new HashMap<>();
                        Map<String, Map<String, Object>> users = (Map<String, Map<String, Object>>) snapshot.child("users").getValue();

                        for (Map<String, Object> user : users.values()) {
                            Log.i("dank memes", user.toString());
                            if (user.containsKey("major") && user.get("major").equals(major) && user.containsKey(ratingsString)) {
                                Map<String, String> user_ratings = (Map<String, String>) user.get(ratingsString);
                                for (String movie_id : user_ratings.keySet()) {
                                    if (!all_ratings.containsKey(movie_id)) {
                                        all_ratings.put(Long.parseLong(movie_id), new ArrayList<Float>());
                                    }

                                    all_ratings.get(Long.parseLong(movie_id)).add(Float.parseFloat(user_ratings.get(movie_id)));
                                }
                            }
                        }

                        Map<Long, Float> avg_ratings = new HashMap<Long, Float>();
                        for (Long movie_id : all_ratings.keySet()) {
                            List<Float> ratings = all_ratings.get(movie_id);
                            float average = 0;
                            for (float rating : ratings) {
                                average += rating;
                            }
                            average /= ratings.size();
                            avg_ratings.put(movie_id, average);
                        }

                        Map.Entry<Long, Float>[] entries = new Map.Entry[avg_ratings.size()];
                        avg_ratings.entrySet().toArray(entries);
                        List<Map.Entry<Long, Float>> entry_list = new ArrayList();
                        for (Map.Entry<Long, Float> entry : entries) {
                            entry_list.add(entry);
                        }
                        Collections.sort(entry_list, new Comparator<Map.Entry<Long, Float>>() {
                            @Override
                            public int compare(Map.Entry<Long, Float> lhs, Map.Entry<Long, Float> rhs) {
                                return Float.compare(rhs.getValue(), lhs.getValue());
                            }
                        });

                        List<Long> movie_ids = new ArrayList();
                        for (Map.Entry<Long, Float> entry : entry_list) {
                            movie_ids.add(entry.getKey());
                        }

                        fb.removeEventListener(this);
                        callback.success(movie_ids);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * Fetch the ids of highest overall rated movies
     * @param fb Firebase reference
     * @param callback called with a List of rotten tomato movie id's
     */
    public static void highestRatedOverall(final Firebase fb, final RottenApi.Callback<List<Long>> callback) {
        final String ratingsString = "ratings";
        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<Long, List<Float>> all_ratings = new HashMap<>();
                Map<String, Map<String, Object>> users = (Map<String, Map<String, Object>>) snapshot.child("users").getValue();

                for (Map<String, Object> user : users.values()) {
                    Log.i("dank memes", user.toString());
                    if (user.containsKey(ratingsString)) {
                        Map<String, String> user_ratings = (Map<String, String>) user.get(ratingsString);
                        for (String movie_id : user_ratings.keySet()) {
                            if (!all_ratings.containsKey(movie_id)) {
                                all_ratings.put(Long.parseLong(movie_id), new ArrayList<Float>());
                            }

                            all_ratings.get(Long.parseLong(movie_id)).add(Float.parseFloat(user_ratings.get(movie_id)));
                        }
                    }
                }

                Map<Long, Float> avg_ratings = new HashMap<Long, Float>();
                for (Long movie_id : all_ratings.keySet()) {
                    List<Float> ratings = all_ratings.get(movie_id);
                    float average = 0;
                    for (float rating : ratings) {
                        average += rating;
                    }
                    average /= ratings.size();
                    avg_ratings.put(movie_id, average);
                }

                Map.Entry<Long, Float>[] entries = new Map.Entry[avg_ratings.size()];
                avg_ratings.entrySet().toArray(entries);
                List<Map.Entry<Long, Float>> entry_list = new ArrayList();
                for (Map.Entry<Long, Float> entry : entries) {
                    entry_list.add(entry);
                }
                Collections.sort(entry_list, new Comparator<Map.Entry<Long, Float>>() {
                    @Override
                    public int compare(Map.Entry<Long, Float> lhs, Map.Entry<Long, Float> rhs) {
                        return Float.compare(rhs.getValue(), lhs.getValue());
                    }
                });

                List<Long> movie_ids = new ArrayList();
                for (Map.Entry<Long, Float> entry : entry_list) {
                    movie_ids.add(entry.getKey());
                }

                fb.removeEventListener(this);
                callback.success(movie_ids);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
