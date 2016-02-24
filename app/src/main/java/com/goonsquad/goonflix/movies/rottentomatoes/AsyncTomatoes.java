package com.goonsquad.goonflix.movies.rottentomatoes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 2/23/16.
 */
public class AsyncTomatoes<T> extends AsyncTask<AsyncTomatoes.Task<T>, Void, AsyncTomatoes.PartialProgress<T>> {

    private Callback<T> callback;

    private ProgressDialog spinner;
    private Context context;

    private boolean working = false;

    public static class PartialProgress<D> {
        private Exception error;
        private List<D> results;

        public PartialProgress(List<D> results, Exception error) {
            this.error = error;
            this.results = results;
        }

        public PartialProgress(List<D> results) {
            this(results, null);
        }

        public boolean has_error() {
            return error != null;
        }

        public List<D> results() {
            return results;
        }

        public Exception error() {
            return error;
        }
    }

    public interface Callback<D> {
        void onFinish(D data);
    }

    public interface Task<D> {
        D run();
    }

    public AsyncTomatoes(Context context, Callback<T> callback) {
        this.callback = callback;
        this.context = context;
    }

    public void run(Task<T> task) {
        spinner = ProgressDialog.show(context, "Loading", "please wait...");
        working = true;
        this.execute(task);
    }

    @Override
    protected PartialProgress<T> doInBackground(Task<T>... tasks) {
        ArrayList<T> results = new ArrayList<>(tasks.length);
        for (Task<T> task : tasks) {
            try {
                results.add(task.run());
            } catch (Exception err) {
                return new PartialProgress<>(results, err);
            }
        }
        return new PartialProgress<>(results);
    }

    @Override
    protected void onPostExecute(PartialProgress<T> results) {
        if (results.has_error()) {
            new AlertDialog.Builder(context)
                    .setTitle("Could not connect to the Rotten Tomatoes API")
                    .setMessage(results.error().getMessage())
                    .setPositiveButton("Ok", null)
                    .create()
                    .show();
        }
        for (T result : results.results()) {
            this.callback.onFinish(result);
        }
        spinner.dismiss();
        spinner = null;
        working = false;
    }

    public boolean is_working() {
        return working;
    }
}

