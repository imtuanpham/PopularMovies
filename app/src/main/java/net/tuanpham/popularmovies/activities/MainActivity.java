package net.tuanpham.popularmovies.activities;
/*
    @author: Tuan Pham
    @since: 2018-06-03 21:09:33
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.tuanpham.popularmovies.R;
import net.tuanpham.popularmovies.adapters.MovieAdapter;
import net.tuanpham.popularmovies.data.MovieContract;
import net.tuanpham.popularmovies.data.MovieProvider;
import net.tuanpham.popularmovies.sync.MovieSyncUtils;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int MOVIE_DATA_LOADER = 222;

    private MovieAdapter mMovieAdapter;

    private String mMovieList;
    private Uri mMovieUri;

    private GridView mGridView;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_main);

        mMovieAdapter = new MovieAdapter(this, null);

        // Get a reference to the ListView, and attach this adapter to it.
        mGridView = (GridView) findViewById(R.id.gv_movies);
        mGridView.setAdapter(mMovieAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long movieId = mMovieAdapter.getItemId(i);

                Context context = getApplicationContext();
                Class destinationClass = DetailActivity.class;
                Intent intentToStartDetailActivity = new Intent(context, destinationClass);
                intentToStartDetailActivity.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
                startActivity(intentToStartDetailActivity);
            }
        });

        if(savedInstanceState != null && savedInstanceState.containsKey(MovieProvider.MOVIE_LIST)) {
            // retrieve movie list from saved state
            String movieList = savedInstanceState.getString(MovieProvider.MOVIE_LIST);
            loadMovies(movieList);
        } else {
            // default to the popular list
            loadMovies(MovieProvider.MOVIE_LIST_POPULAR);
        }

        // sync movie data if the app is first opened
        MovieSyncUtils.initialize(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence(MovieProvider.MOVIE_LIST, mMovieList);
        super.onSaveInstanceState(outState);
    }

    private void loadMovies(String list) {

        mMovieList = list;

        switch (mMovieList) {
            case MovieProvider.MOVIE_LIST_POPULAR: {
                mMovieUri = MovieContract.CONTENT_URI_POPULAR;
                this.setTitle(R.string.action_most_popular);
                break;
            }
            case MovieProvider.MOVIE_LIST_TOP_RATED: {
                mMovieUri = MovieContract.CONTENT_URI_TOP_RATED;
                this.setTitle(R.string.action_top_rated);
                break;
            }
            case MovieProvider.MOVIE_LIST_FAVORITE: {
                mMovieUri = MovieContract.CONTENT_URI_FAVORITE;
                this.setTitle(R.string.action_favorites);
                break;
            }

            default:
                throw new RuntimeException("Invalid Movie List: " + mMovieList);
        }

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> movieDataLoader = loaderManager.getLoader(MOVIE_DATA_LOADER);
        if (movieDataLoader == null) {
            loaderManager.initLoader(MOVIE_DATA_LOADER, null, this);
        } else {
            loaderManager.restartLoader(MOVIE_DATA_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, final Bundle args) {

        Log.d(LOG_TAG, "onCreateLoader");
        mLoadingIndicator.setVisibility(View.VISIBLE);

        switch (loaderId) {

            case MOVIE_DATA_LOADER:
                return new CursorLoader(this,
                        mMovieUri,
                        MovieContract.MovieEntry.DEFAULT_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }

    }   // end public Loader

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (data == null || data.getCount() == 0) {
            showErrorMessage();
        } else {
            showMoviesView();
            mMovieAdapter.changeCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
        mMovieAdapter.changeCursor(null);
        return;
    }

    private void showMoviesView() {
        /* First, make sure the error is invisible */
        mErrorMessage.setVisibility(View.INVISIBLE);
        /* Then, make sure the movies data is visible */
        mGridView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mGridView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        // return true so the menu can show up
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_most_popular) {
            loadMovies(MovieProvider.MOVIE_LIST_POPULAR);
            return true;
        }

        if (id == R.id.action_top_rated) {
            loadMovies(MovieProvider.MOVIE_LIST_TOP_RATED);
            return true;
        }

        if (id == R.id.action_favorites) {
            loadMovies(MovieProvider.MOVIE_LIST_FAVORITE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
