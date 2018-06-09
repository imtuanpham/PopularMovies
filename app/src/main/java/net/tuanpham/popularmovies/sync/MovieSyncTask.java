/*
    @author: Tuan Pham
    @since: 2018-06-03 09:15:08

    Reference: Sunshine Project
 */

package net.tuanpham.popularmovies.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import net.tuanpham.popularmovies.data.MovieContract;
import net.tuanpham.popularmovies.utils.JsonUtils;
import net.tuanpham.popularmovies.utils.TMDbUtils;

import java.net.URL;

public class MovieSyncTask {

    private final static String LOG_TAG = MovieSyncTask.class.getSimpleName();

    synchronized public static void syncPopularMovieData(Context context) {

        Log.d(LOG_TAG, "Syncing Popular Movies...");
        URL requestUrl = TMDbUtils.buildListUrl(TMDbUtils.TMDB_MOVIES_LIST_POPULAR, 1);
        try {
            String jsonResponse = TMDbUtils
                    .getResponseFromHttpUrl(requestUrl);

            /* Parse the JSON into a arry of movie values */
            ContentValues[] movieValues = JsonUtils.parseMoviesJsonToContentValuesArray(jsonResponse);

            if (movieValues != null && movieValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver movieContentResolver = context.getContentResolver();

                movieContentResolver.delete(
                        MovieContract.CONTENT_URI_POPULAR,
                        null,
                        null);

                movieContentResolver.bulkInsert(
                        MovieContract.CONTENT_URI_POPULAR,
                        movieValues);
            }

            /* If the code reaches this point, we have successfully performed our sync */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public static void syncTopRatedMovieData(Context context) {

        Log.d(LOG_TAG, "Syncing Top Rated Movies...");
        URL requestUrl = TMDbUtils.buildListUrl(TMDbUtils.TMDB_MOVIES_ORDER_TOP_RATED, 1);
        try {
            String jsonResponse = TMDbUtils
                    .getResponseFromHttpUrl(requestUrl);

            /* Parse the JSON into a arry of movie values */
            ContentValues[] movieValues = JsonUtils.parseMoviesJsonToContentValuesArray(jsonResponse);

            if (movieValues != null && movieValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver movieContentResolver = context.getContentResolver();

                movieContentResolver.delete(
                        MovieContract.CONTENT_URI_TOP_RATED,
                        null,
                        null);

                movieContentResolver.bulkInsert(
                        MovieContract.CONTENT_URI_TOP_RATED,
                        movieValues);
            }

            /* If the code reaches this point, we have successfully performed our sync */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}