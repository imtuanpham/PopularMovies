/*
    @author: Tuan Pham
    @since: 2018-06-03 09:15:08

    Reference: Sunshine Project
 */
package net.tuanpham.popularmovies.sync;

import android.app.IntentService;
import android.content.Intent;

public class MovieSyncIntentService extends IntentService {

    public MovieSyncIntentService() {
        super("MovieSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MovieSyncTask.syncPopularMovieData(this);
        MovieSyncTask.syncTopRatedMovieData(this);
    }
}