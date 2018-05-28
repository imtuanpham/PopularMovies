package net.tuanpham.popularmovies.activities;
/*
    @author: Tuan Pham
    @since: 2018-05-28 12:25:12
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import net.tuanpham.popularmovies.models.Movie;
import net.tuanpham.popularmovies.utils.JsonUtils;
import net.tuanpham.popularmovies.utils.TMDbUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int TMDB_DATA_LOADER = 222;

    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieList;

    private String movieOrder = TMDbUtils.TMDB_MOVIES_ORDER_POPULAR;

    private GridView mGridView;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Log.d(LOG_TAG, "onCreate");

        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        movieAdapter = new MovieAdapter(this);

        // Get a reference to the ListView, and attach this adapter to it.
        mGridView = (GridView) findViewById(R.id.gv_movies);
        mGridView.setAdapter(movieAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie clickedMovie = movieAdapter.getItem(i);

                Context context = getApplicationContext();
                Class destinationClass = DetailActivity.class;
                Intent intentToStartDetailActivity = new Intent(context, destinationClass);
                intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, clickedMovie);
                startActivity(intentToStartDetailActivity);
            }
        });

        if(movieList != null) return;

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            // if cache doesn't exist, load movies from API
            Log.d(LOG_TAG, "Loading Movies from API...");
            loadMoviesFromApi(movieOrder);
        } else {
            Log.d(LOG_TAG, "Loading Movies from instance state...");
            loadMoviesFromInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "Saving Instance State...");
        outState.putParcelableArrayList("movies", movieList);
        super.onSaveInstanceState(outState);
    }

    private void loadMoviesFromInstanceState(Bundle savedInstanceState) {
        movieList = savedInstanceState.getParcelableArrayList("movies");
        if(movieList == null || movieList.isEmpty()) {
            showErrorMessage();
            Log.d(LOG_TAG, "Invalid movie list");
        } else {
            movieAdapter.setMovieList(movieList);
        }
    }

    private void loadMoviesFromApi(String order) {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(TMDbUtils.TMDB_MOVIES_ORDER, order);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ArrayList<Movie>> movieDataLoader = loaderManager.getLoader(TMDB_DATA_LOADER);
        if (movieDataLoader == null) {
            loaderManager.initLoader(TMDB_DATA_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(TMDB_DATA_LOADER, queryBundle, this);
        }
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {

        class FetchDataAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Movie>> {

            private ArrayList<Movie> cachedData;

            public FetchDataAsyncTaskLoader(Context context) {
                super(context);
            }

            @Override
            protected void onStartLoading() {

                if (args == null) {
                    return;
                }

                Log.d(LOG_TAG, "onStartLoading");

                if (cachedData == null) {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Movie> loadInBackground() {

                String order = args.getString(TMDbUtils.TMDB_MOVIES_ORDER);

                if (order == null || TextUtils.isEmpty(order)) {
                    return null;
                }

                URL requestUrl = TMDbUtils.buildUrl(order, 1);
                try {
                    String jsonResponse = TMDbUtils
                            .getResponseFromHttpUrl(requestUrl);

                    Log.d(LOG_TAG, jsonResponse);

                    ArrayList<Movie> movieList = JsonUtils.parseMoviesJson(jsonResponse);
                    return movieList;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(ArrayList<Movie> data) {
                cachedData = data;
                super.deliverResult(data);
            }
        }

        return new FetchDataAsyncTaskLoader(this);

    }   // end public Loader

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (data == null || data.isEmpty()) {
            showErrorMessage();
        } else {
            showMoviesView();
            movieList = data;
            movieAdapter.setMovieList(movieList);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
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
            loadMoviesFromApi(TMDbUtils.TMDB_MOVIES_ORDER_POPULAR);
            return true;
        }

        if (id == R.id.action_top_rated) {
            loadMoviesFromApi(TMDbUtils.TMDB_MOVIES_ORDER_TOP_RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
