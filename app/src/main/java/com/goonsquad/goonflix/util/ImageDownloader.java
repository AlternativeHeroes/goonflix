package com.goonsquad.goonflix.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by michael on 2/24/16.
 * Simple class to download images in the background
 */
public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {


    // Keep the callback to use when finished
    private Callback callback;

    /**
     * Make a new service to download images
     * @param p_callback the action to take when a download completes
     */
    public ImageDownloader(Callback p_callback) {
        this.callback = p_callback;
    }

    protected final Bitmap doInBackground(String... urls) {
        try {
            InputStream in = new java.net.URL(urls[0]).openStream();
            return BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.d("image_downloader_error", Log.getStackTraceString(e));
            return null;
        }
    }

    protected final void onPostExecute(Bitmap result) {
        callback.finished(result);
    }
    /**
     * A simple callback to notify the caller of a downloaded image
     */
    public interface Callback {
        void finished(Bitmap result);
    }

}

