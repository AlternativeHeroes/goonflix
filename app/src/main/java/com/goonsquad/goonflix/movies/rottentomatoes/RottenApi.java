package com.goonsquad.goonflix.movies.rottentomatoes;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by michael on 2/24/16.
 * Rotten Tomatoes API using Android's Volley
 */
public class RottenApi {
    public String key;
    public RequestQueue request_queue;

    public static final String TAG = "GOONFLIX_ROTTEN_API";
    public static final String MOVIE_SEARCH = "http://api.rottentomatoes.com/api/public/v1.0/movies.json";
    public static final String NEW_RELEASES = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json";
    public static final String NEW_DVDS = "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/new_releases.json";
    public static final String GET_MOVIE = "http://api.rottentomatoes.com/api/public/v1.0/movies/";

    /**
     * A callback to notify the user when an API call has finished.
     * @param <D> The expected result of the API call.
     */
    public interface Callback<D> {
        void success(D data);
        void failure(VolleyError error);
    }

    /**
     * Create an instance of the RottenTomatoes API
     * @param context the context from which this will run from
     * @param key the API key for rotten tomatoes
     */
    public RottenApi(Context context, String key) {
        this.key = key;
        this.request_queue = Volley.newRequestQueue(context);
    }

    /**
     * Destruct this instance when the activity closes
     */
    public void destroy() {
        this.request_queue.cancelAll(TAG);
    }

    /**
     * Get a list of new DVDs
     * @param callback called when a list is available or an error occurred.
     */
    public void new_dvds(Callback<List<RTMovie>> callback) {
        new_dvds(16, 1, callback);
    }

    /**
     * Get a list of new DVDs
     * @param number number of elements to return in this request
     * @param page page from which to retrieve the elements
     * @param callback called when a list is available or an error occurred.
     */
    public void new_dvds(Integer number, Integer page, final Callback<List<RTMovie>> callback) {
        List<NameValuePair> params = new ArrayList<>(3);
        params.add(new BasicNameValuePair("country", "us"));
        params.add(new BasicNameValuePair("page_limit", number.toString()));
        params.add(new BasicNameValuePair("page", page.toString()));

        rest_request(NEW_DVDS, params, new Callback<JSONObject>() {
            @Override
            public void success(JSONObject data) {
                try {
                    JSONArray json_movies = data.getJSONArray("movies");
                    parse_movies(json_movies, callback);
                } catch (JSONException err) {
                    callback.failure(new VolleyError("Received incorrect JSON"));
                }
            }

            @Override
            public void failure(VolleyError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Get a list of new releases
     * @param callback called when a list is available or an error occurred.
     */
    public void new_releases(Callback<List<RTMovie>> callback) {
        new_releases(16, 1, callback);
    }

    /**
     * Get a list of new releases
     * @param number number of new releases to retrieve
     * @param page page from which to retrieve it from
     * @param callback called when a list is available or an error occurred.
     */
    public void new_releases(Integer number, Integer page, final Callback<List<RTMovie>> callback) {
        List<NameValuePair> params = new ArrayList<>(3);
        params.add(new BasicNameValuePair("country", "us"));
        params.add(new BasicNameValuePair("page_limit", number.toString()));
        params.add(new BasicNameValuePair("page", page.toString()));

        rest_request(NEW_RELEASES, params, new Callback<JSONObject>() {
            @Override
            public void success(JSONObject data) {
                try {
                    JSONArray json_movies = data.getJSONArray("movies");
                    parse_movies(json_movies, callback);
                } catch (JSONException err) {
                    callback.failure(new VolleyError("Received incorrect JSON"));
                }
            }

            @Override
            public void failure(VolleyError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Search movies using the specified query
     * @param query search for a movie using the query
     * @param callback called when a list of results is available or an error has occurred.
     */
    public void search_movies(String query, Callback<List<RTMovie>> callback) {
        search_movies(query, 30, 1, callback);
    }

    /**
     * Search movies using the specified query
     * @param query the query to use in the search
     * @param number the number of results to return
     * @param page the page from which to get the results
     * @param callback called when a list of results is ready or an error has occurred.
     */
    public void search_movies(String query, Integer number, Integer page, final Callback<List<RTMovie>> callback) {
        List<NameValuePair> params = new ArrayList<>(3);
        params.add(new BasicNameValuePair("q", query));
        params.add(new BasicNameValuePair("page_limit", number.toString()));
        params.add(new BasicNameValuePair("page", page.toString()));

        rest_request(MOVIE_SEARCH, params, new Callback<JSONObject>() {
            @Override
            public void success(final JSONObject data) {
                try {
                    JSONArray json_movies = data.getJSONArray("movies");
                    parse_movies(json_movies, callback);
                } catch (JSONException err) {
                    callback.failure(new VolleyError("Received incorrect JSON"));
                }
            }

            @Override
            public void failure(VolleyError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Fetch some movies from rotten tomatoes based on their IDs
     * @param ids the ids of the movies to fetch
     * @return a list of parsed RTMovie objects
     */
    public void fetch_movies(final List<Long> ids, final Callback<List<RTMovie>> callback) {

        if (ids.size() == 0) {
            callback.success(new ArrayList<RTMovie>(0));
            return;
        }

        final JSONArray json_movies = new JSONArray();

        Callback<JSONObject> collector = new Callback<JSONObject>() {
            @Override
            public void success(JSONObject data) {
                json_movies.put(data);
                if (json_movies.length() == ids.size()) {
                    try {
                        Log.i(TAG, json_movies.toString());
                        parse_movies(json_movies, callback);
                    } catch (JSONException err) {
                        callback.failure(new VolleyError("JSON parsing exception"));
                    }
                }
            }

            @Override
            public void failure(VolleyError error) {
                callback.failure(error);
            }
        };

        for (long id : ids) {
            Uri.Builder uri_builder = Uri.parse(GET_MOVIE).buildUpon();
            uri_builder.appendPath(id + ".json");
            rest_request(uri_builder.build().toString(), null, collector);
        }
    }

    private void parse_movies(JSONArray json_movies, final Callback<List<RTMovie>> callback) throws JSONException {
        final List<RTMovie> movies = new ArrayList<RTMovie>(json_movies.length());
        final int total = json_movies.length();
        final AtomicInteger current = new AtomicInteger(0);

        for (int i = 0; i < json_movies.length(); ++i) {
            movies.add(new RTMovie(json_movies.getJSONObject(i), new RTMovie.Callback() {
                @Override
                public void finished() {
                    int finished = current.addAndGet(1);
                    if (finished == total) {
                        callback.success(movies);
                    }
                }
            }));
        }
    }

    private void rest_request(String base_url, List<NameValuePair> params, final Callback<JSONObject> callback) {
        Uri.Builder uri_builder = Uri.parse(base_url).buildUpon();
        if (params != null) {
            for (NameValuePair param : params) {
                uri_builder.appendQueryParameter(param.getName(), param.getValue());
            }
        }
        uri_builder.appendQueryParameter("apikey", key);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, uri_builder.build().toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "request success: " + response);
                callback.success(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "request failure: " + error);
                callback.failure(error);
            }
        });
        request.setTag(TAG);
        request_queue.add(request);
    }
}
