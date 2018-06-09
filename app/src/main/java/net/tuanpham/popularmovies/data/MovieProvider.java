/*
    @author: Tuan Pham
    @since: 2018-06-03 09:15:08

    Reference: Sunshine Project
 */


package net.tuanpham.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

public class MovieProvider extends ContentProvider {

    private final static String LOG_TAG = MovieProvider.class.getSimpleName();

    public static final int CODE_MOVIE = 101;
    public static final int CODE_MOVIE_WITH_ID = 1001;

    public static final String MOVIE_LIST = "list";
    public static final String MOVIE_LIST_POPULAR = "popular";
    public static final String MOVIE_LIST_TOP_RATED = "top_rated";
    public static final String MOVIE_LIST_FAVORITE = "favorite";

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mDbHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", CODE_MOVIE);
        matcher.addURI(authority,MovieContract.PATH_MOVIE + "/*/#", CODE_MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    private int bulkInsertMovies(@NonNull Uri uri, String tblName, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.beginTransaction();
        int rowsInserted = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(tblName, null, value);
                if (_id != -1) {
                    rowsInserted++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsInserted;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        Log.d(LOG_TAG, "bulkInsert " + uri.toString());

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE:
                String movieSegment = uri.getLastPathSegment();
                String tblName = MovieContract.pathSegmentToTableName(movieSegment);
                return bulkInsertMovies(uri, tblName, values);

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Log.d(LOG_TAG, "query " + uri.toString());

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE: {
                String movieSegment = uri.getLastPathSegment();
                String tblName = MovieContract.pathSegmentToTableName(movieSegment);

                cursor = mDbHelper.getReadableDatabase().query(
                        tblName,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_MOVIE_WITH_ID: {

                String id = uri.getLastPathSegment();
                String movieSegment = uri.getPathSegments().get(1);
                String tblName = MovieContract.pathSegmentToTableName(movieSegment);


                String[] selectionArguments = new String[]{id};

                cursor = mDbHelper.getReadableDatabase().query(
                        tblName,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete " + uri.toString());

        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE: {
                String movieSegment = uri.getLastPathSegment();
                String tblName = MovieContract.pathSegmentToTableName(movieSegment);

                /*
                 * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
                 * deleted. However, if we do pass null and delete all of the rows in the table, we won't
                 * know how many rows were deleted. According to the documentation for SQLiteDatabase,
                 * passing "1" for the selection will delete all rows and return the number of rows
                 * deleted, which is what the caller of this method expects.
                 */
                if (null == selection) selection = "1";

                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        tblName,
                        selection,
                        selectionArgs);

                break;
            }
            case CODE_MOVIE_WITH_ID: {

                String id = uri.getLastPathSegment();
                String movieSegment = uri.getPathSegments().get(1);
                String tblName = MovieContract.pathSegmentToTableName(movieSegment);

                String[] selectionArguments = new String[]{id};

                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        tblName,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert " + uri.toString());

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE:
                String movieSegment = uri.getLastPathSegment();
                String tblName = MovieContract.pathSegmentToTableName(movieSegment);
                final SQLiteDatabase db = mDbHelper.getWritableDatabase();

                db.beginTransaction();
                long _id = db.insert(tblName, null, values);
                db.setTransactionSuccessful();
                db.endTransaction();

                if (_id > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return null;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("We are not implementing update");
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mDbHelper.close();
        super.shutdown();
    }
}