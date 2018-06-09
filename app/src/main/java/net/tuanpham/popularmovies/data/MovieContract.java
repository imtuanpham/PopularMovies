/*
    @author: Tuan Pham
    @since: 2018-05-22 21:50:10

    Reference: Sunshine Project
 */
package net.tuanpham.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the movie database.
 */
public class MovieContract {

    /*
     * Name for the entire content provider. Should be unique on the
     * Play Store.
     */
    public static final String CONTENT_AUTHORITY = "net.tuanpham.popularmovies";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String SEGMENT_MOVIE_POPULAR = "popular";
    public static final String SEGMENT_MOVIE_TOP_RATED = "top_rated";
    public static final String SEGMENT_MOVIE_FAVORITE = "favorite";

    public static final Uri CONTENT_URI_POPULAR = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_MOVIE)
            .appendPath(SEGMENT_MOVIE_POPULAR)
            .build();

    public static final Uri CONTENT_URI_TOP_RATED = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_MOVIE)
            .appendPath(SEGMENT_MOVIE_TOP_RATED)
            .build();

    public static final Uri CONTENT_URI_FAVORITE = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_MOVIE)
            .appendPath(SEGMENT_MOVIE_FAVORITE)
            .build();

    public static String pathSegmentToTableName(String pathSegment) {
        switch (pathSegment) {

            case SEGMENT_MOVIE_POPULAR:
                return MovieEntry.TABLE_NAME_MOVIE_POPULAR;

            case SEGMENT_MOVIE_TOP_RATED:
                return MovieEntry.TABLE_NAME_MOVIE_TOP_RATED;

            case SEGMENT_MOVIE_FAVORITE:
                return MovieEntry.TABLE_NAME_MOVIE_FAVORITE;

            default:
                return null;
        }
    }

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME_MOVIE_POPULAR = "moviePopular";
        public static final String TABLE_NAME_MOVIE_TOP_RATED = "movieTopRated";
        public static final String TABLE_NAME_MOVIE_FAVORITE = "movieFavorite";

        /* Movie ID as returned by API */
        public static final String COLUMN_MOVIE_ID = "_id";

        public static final String COLUMN_MOVIE_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_MOVIE_POSTER_PATH = "posterPath";
        public static final String COLUMN_MOVIE_LAST_MOD_DT = "lastModDt";

        public static final String[] DEFAULT_PROJECTION = {
                COLUMN_MOVIE_ID,
                COLUMN_MOVIE_ORIGINAL_TITLE,
                COLUMN_MOVIE_POSTER_PATH,
                COLUMN_MOVIE_LAST_MOD_DT
        };
    }
}