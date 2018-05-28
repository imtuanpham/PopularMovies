/*
 * @author Tuan Pham
 * @since 2018-05-19 16:53:50
 */
package net.tuanpham.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class TMDbUtils {

    private static final String LOG_TAG = TMDbUtils.class.getSimpleName();

    private static final String TMDB_API_KEY = "TO_BE_POPULATED";

    private static final String TMDB_MOVIES_BASE_URL =
            "https://api.themoviedb.org/3/movie/";

    public static final String TMDB_MOVIES_ORDER = "order";
    public static final String TMDB_MOVIES_ORDER_POPULAR = "popular";
    public static final String TMDB_MOVIES_ORDER_TOP_RATED = "top_rated";

    public static final String TMDB_MOVIES_IMAGE_BASE_URL =
            "http://image.tmdb.org/t/p/w185/";

    final static String PARAM_PAGE = "page";
    final static String PARAM_API_KEY = "api_key";

    /**
     * Builds the URL used to retrieve movies from TMDb
     *
     * @order order Popular or Top Rated.
     * @param page Page.
     * @return The URL to use to query the TMDb server.
     */
    public static URL buildUrl(String order, int page) {
        Uri builtUri = Uri.parse(TMDB_MOVIES_BASE_URL + order).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, TMDB_API_KEY)
                .appendQueryParameter(PARAM_PAGE, String.valueOf(page))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(LOG_TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     * Reference: The Sunshine Project
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}