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

    public static final String TMDB_MOVIES_LIST = "list";
    public static final String TMDB_MOVIES_LIST_POPULAR = "popular";
    public static final String TMDB_MOVIES_ORDER_TOP_RATED = "top_rated";

    public static final String TMDB_MOVIE_ID = "id";

    public static final String TMDB_MOVIES_IMAGE_BASE_W185_URL =
            "http://image.tmdb.org/t/p/w185/";

    public static final String TMDB_MOVIES_IMAGE_BASE_W154_URL =
            "http://image.tmdb.org/t/p/w154/";

    final static String PARAM_PAGE = "page";
    final static String PARAM_API_KEY = "api_key";

    final static String PARAM_APPEND_TO_RESPOND = "append_to_response";
    final static String PARAM_APPEND_TO_RESPOND_VALUES = "videos,reviews";

    /**
     * Builds the URL used to retrieve details of a movie from TMDb
     * Eg: https://api.themoviedb.org/3/movie/299536?api_key=02948ab118431a160d48bd89e38bc240&append_to_response=videos,reviews
     * @param movieIdStr Movie ID.
     * @return The URL to use to query the TMDb server.
     */
    public static URL buildDetailUrl(String movieIdStr) {
        Uri builtUri = Uri.parse(TMDB_MOVIES_BASE_URL + movieIdStr).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, TMDB_API_KEY)
                .appendQueryParameter(PARAM_APPEND_TO_RESPOND, PARAM_APPEND_TO_RESPOND_VALUES)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "Built Detail URL " + url);

        return url;
    }
    /**
     * Builds the URL used to retrieve list of movies from TMDb
     *
     * @param order order Popular or Top Rated.
     * @param page Page.
     * @return The URL to use to query the TMDb server.
     */
    public static URL buildListUrl(String order, int page) {
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

        Log.d(LOG_TAG, "Built List URL " + url);

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