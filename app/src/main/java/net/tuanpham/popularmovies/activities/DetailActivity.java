package net.tuanpham.popularmovies.activities;

/*
    @author: Tuan Pham
    @since: 2018-05-28 12:25:27
 */

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import net.tuanpham.popularmovies.R;
import net.tuanpham.popularmovies.adapters.VideoAdapter;
import net.tuanpham.popularmovies.data.MovieContract;
import net.tuanpham.popularmovies.models.Movie;
import net.tuanpham.popularmovies.utils.JsonUtils;
import net.tuanpham.popularmovies.utils.TMDbUtils;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Movie> {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    private long mMovieId;
    private Movie mMovie;

    private ProgressBar mLoadingIndicator;

    private static final int MOVIE_DETAIL_LOADER = 101;
    private static final int MOVIE_FAVORITE_HANDLER = 102;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_detail);

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(MovieContract.MovieEntry.COLUMN_MOVIE_ID)) {
                mMovieId = intent.getLongExtra(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 0);
                Log.d(LOG_TAG, "mMovieId: " + String.valueOf(mMovieId));

                // load the movie details
                loadMovieDetail();

                // set up favorite toggle button
                setupFavoriteToggleButton();
            }
        }
    }

    private void loadMovieDetail() {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Movie> movieDataLoader = loaderManager.getLoader(MOVIE_DETAIL_LOADER);
        if (movieDataLoader == null) {
            loaderManager.initLoader(MOVIE_DETAIL_LOADER, null, this);
        } else {
            loaderManager.restartLoader(MOVIE_DETAIL_LOADER, null, this);
        }
    }

    private void handleMovieFavorite() {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Movie> movieDataLoader = loaderManager.getLoader(MOVIE_FAVORITE_HANDLER);
        if (movieDataLoader == null) {
            loaderManager.initLoader(MOVIE_FAVORITE_HANDLER, null, this);
        } else {
            loaderManager.restartLoader(MOVIE_FAVORITE_HANDLER, null, this);
        }
    }

    // set up favorite toggle button
    private void setupFavoriteToggleButton() {
        final ToggleButton tbFavorite = (ToggleButton) findViewById(R.id.tb_favorite);

        // attach an OnClickListener
        tbFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleMovieFavorite();
            }
        });

    }

    private void populateDetailUI() {

        ImageView ivPoster = findViewById(R.id.iv_poster);

        Picasso.with(this)
                .load(TMDbUtils.TMDB_MOVIES_IMAGE_BASE_W154_URL + mMovie.getPosterPath())
                .into(ivPoster);

        setTitle(mMovie.getOriginalTitle());

//        TextView tvOriginalTitle  = findViewById(R.id.tv_original_title);
//        tvOriginalTitle.setText(mMovie.getOriginalTitle());

        TextView tvVoteAverage  = findViewById(R.id.tv_vote_average);
        tvVoteAverage.setText(String.valueOf(mMovie.getVoteAverage()) + " out of 10");

//        SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy", Locale.US);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy", Locale.US);

        TextView tvReleaseDate  = findViewById(R.id.tv_release_date);
        tvReleaseDate.setText(formatter.format(mMovie.getReleaseDate()));

        TextView tvOverview  = findViewById(R.id.tv_overview);
        tvOverview.setText(mMovie.getOverview());

        if(mMovie.isFavorite()) {
            ToggleButton tbFavorite = (ToggleButton) findViewById(R.id.tb_favorite);
            tbFavorite.setChecked(true);
        }

        TextView tvReviews  = findViewById(R.id.tv_reviews);
        tvReviews.setText(mMovie.getReviews());

        final VideoAdapter videoAdapter = new VideoAdapter(this);

        if (mMovie.getVideos() != null) {
            // Get a reference to the ListView, and attach this adapter to it.
            ListView videoListView = (ListView) findViewById(R.id.lv_videos);

            videoListView.setAdapter(videoAdapter);
            videoAdapter.setVideoList(mMovie.getVideos());

            videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // Reference: https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
                    Movie.Video video = videoAdapter.getItem(i);
                    Context context = getApplicationContext();
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.getKey()));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=" + video.getKey()));
                    try {
                        context.startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        context.startActivity(webIntent);
                    }
                }
            });
        }
    }

    @Override
    public Loader<Movie> onCreateLoader(int loaderId, final Bundle args) {

        Log.d(LOG_TAG, "onCreateLoader");
        mLoadingIndicator.setVisibility(View.VISIBLE);

        switch (loaderId) {
            case MOVIE_DETAIL_LOADER:

                class MovieDetailAsyncTaskLoader extends AsyncTaskLoader<Movie> {

                    private Movie cachedData;
                    public MovieDetailAsyncTaskLoader(Context context) {
                        super(context);
                    }

                    @Override
                    protected void onStartLoading() {
                        Log.d(LOG_TAG, "onStartLoading");

                        if (cachedData == null) {
                            forceLoad();
                        }
                    }

                    @Override
                    public Movie loadInBackground() {

                        URL requestUrl = TMDbUtils.buildDetailUrl(String.valueOf(mMovieId));
                        try {
                            // get movie detail JSON from the API
                            String jsonResponse = TMDbUtils.getResponseFromHttpUrl(requestUrl);

                            // parse the JSON into a movie object
                            Movie movie = JsonUtils.parseMovieJsonToMovie(jsonResponse);

                            Context context = this.getContext();
                            ContentResolver movieContentResolver = context.getContentResolver();
                            Uri queryUri = ContentUris.withAppendedId(MovieContract.CONTENT_URI_FAVORITE, mMovieId);

                            // retrieve the favorite status from the content provider
                            Cursor favMovieCursor =  movieContentResolver.query(queryUri,
                                    null, null,
                                    null, null);

                            if (favMovieCursor != null && favMovieCursor.getCount() == 1) {
                                movie.setFavorite(true);
                            } else {
                                movie.setFavorite(false);
                            }

                            return movie;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public void deliverResult(Movie data) {
                        cachedData = data;
                        super.deliverResult(data);
                    }
                }

                return new MovieDetailAsyncTaskLoader(this);

            case MOVIE_FAVORITE_HANDLER:

                class MovieFavoriteAsyncTaskLoader extends AsyncTaskLoader<Movie> {

                    private Movie cachedData;

                    public MovieFavoriteAsyncTaskLoader(Context context) {
                        super(context);
                    }

                    @Override
                    protected void onStartLoading() {
                        Log.d(LOG_TAG, "onStartLoading");

                        if (cachedData == null) {
                            forceLoad();
                        }
                    }

                    @Override
                    public Movie loadInBackground() {

                        Context context = this.getContext();

                        /* Get a handle on the ContentResolver to delete and insert data */
                        ContentResolver movieContentResolver = context.getContentResolver();

                        // delete the favorite record first in case it exists
                        Uri deleteUri = ContentUris.withAppendedId(MovieContract.CONTENT_URI_FAVORITE, mMovieId);
                        movieContentResolver.delete(
                                deleteUri,
                                null,
                                null);

                        boolean isFavorite = mMovie.isFavorite();

                        if(!isFavorite) {
                            ContentValues values = new ContentValues();
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE, mMovie.getOriginalTitle());
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, mMovie.getPosterPath());
                            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_LAST_MOD_DT, new Date().getTime());

                            movieContentResolver.insert(
                                    MovieContract.CONTENT_URI_FAVORITE,
                                    values);

                        }

                        mMovie.setFavorite(!isFavorite);

                        return null;
                    }

                    @Override
                    public void deliverResult(Movie data) {
                        cachedData = data;
                        super.deliverResult(data);
                    }
                }

                return new MovieFavoriteAsyncTaskLoader(this);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }

        
        
    }   // end public Loader

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        Log.d(LOG_TAG, "onLoadFinished");
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (data != null) {
            mMovie = data;
            populateDetailUI();
        }
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
        return;
    }
}