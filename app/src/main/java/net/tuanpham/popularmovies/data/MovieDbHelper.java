/*
    @author: Tuan Pham
    @since: 2018-06-03 09:15:08

    Reference: Sunshine Project
 */


package net.tuanpham.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import net.tuanpham.popularmovies.data.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "PopularMovies.db";

    private static final int DATABASE_VERSION = 2;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * A simple SQL statement that will create tables that will
         * cache the movie data.
         */
        final String SQL_CREATE_TABLE_MOVIE_POPULAR =

                "CREATE TABLE " + MovieEntry.TABLE_NAME_MOVIE_POPULAR + " (" +

                MovieEntry.COLUMN_MOVIE_ID              + " INTEGER PRIMARY KEY, "                 +
                MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE  + " TEXT NOT NULL,"                  +
                MovieEntry.COLUMN_MOVIE_POSTER_PATH     + " TEXT NOT NULL,"                  +
                MovieEntry.COLUMN_MOVIE_LAST_MOD_DT     + " INTEGER NOT NULL"                 +
                        ");";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOVIE_POPULAR);

        final String SQL_CREATE_TABLE_MOVIE_TOP_RATED =

                "CREATE TABLE " + MovieEntry.TABLE_NAME_MOVIE_TOP_RATED + " (" +

                        MovieEntry.COLUMN_MOVIE_ID              + " INTEGER PRIMARY KEY, "                 +
                        MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE  + " TEXT NOT NULL,"                  +
                        MovieEntry.COLUMN_MOVIE_POSTER_PATH     + " TEXT NOT NULL,"                  +
                        MovieEntry.COLUMN_MOVIE_LAST_MOD_DT     + " INTEGER NOT NULL"                 +
                        ");";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOVIE_TOP_RATED);

        final String SQL_CREATE_TABLE_MOVIE_FAVORITE =

                "CREATE TABLE " + MovieEntry.TABLE_NAME_MOVIE_FAVORITE + " (" +

                        MovieEntry.COLUMN_MOVIE_ID              + " INTEGER PRIMARY KEY, "                 +
                        MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE  + " TEXT NOT NULL,"                  +
                        MovieEntry.COLUMN_MOVIE_POSTER_PATH     + " TEXT NOT NULL,"                  +
                        MovieEntry.COLUMN_MOVIE_LAST_MOD_DT     + " INTEGER NOT NULL"                 +
                        ");";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOVIE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME_MOVIE_POPULAR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME_MOVIE_TOP_RATED);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME_MOVIE_FAVORITE);
        onCreate(sqLiteDatabase);
    }
}