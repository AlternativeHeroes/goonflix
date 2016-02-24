package com.goonsquad.goonflix.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;

/**
 * Created by michael on 2/24/16.
 * Simple class to download images in the background
 */
public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    /**
     * A simple callback to notify the caller of a downloaded image
     */
    public interface Callback {
        void finished(Bitmap result);
    }

    // Keep the callback to use when finished
    public Callback callback;

    /**
     * Make a new service to download images
     * @param callback the action to take when a download completes
     */
    public ImageDownloader(Callback callback) {
        this.callback = callback;
    }

    protected Bitmap doInBackground(String... urls) {
        try {
            InputStream in = new java.net.URL(urls[0]).openStream();
            return BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(Bitmap result) {
        callback.finished(result);
    }
}

