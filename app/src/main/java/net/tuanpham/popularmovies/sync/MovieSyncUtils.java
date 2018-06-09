/*
    @author: Tuan Pham
    @since: 2018-06-03 09:15:08

    Reference: Sunshine Project
 */
package net.tuanpham.popularmovies.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

public class MovieSyncUtils {

    private static boolean sInitialized;

    synchronized public static void initialize(@NonNull final Context context) {
        if (sInitialized) return;

        MovieSyncAsyncTask task = new MovieSyncAsyncTask();
        task.execute(context);

        sInitialized = true;
    }


    private static class MovieSyncAsyncTask extends AsyncTask<Context, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Context... contexts) {
            Context context = contexts[0];
            startImmediateSync(context);
            return null;
        }

        @Override
        protected void onPostExecute(Cursor s) {
            super.onPostExecute(s);
            // close the cursor to avoid the memory leak
            if(s != null) s.close();
        }
    }

    /**
     * Helper method to perform a sync immediately using an IntentService for asynchronous
     * execution.
     *
     * @param context The Context used to start the IntentService for the sync.
     */
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, MovieSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}