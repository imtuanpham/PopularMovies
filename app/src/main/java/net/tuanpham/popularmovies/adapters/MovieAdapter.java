package net.tuanpham.popularmovies.adapters;

/*
    @author: Tuan Pham
    @since: 2018-05-23 20:54:06
    Based on https://github.com/udacity/android-custom-arrayadapter

    @since: 2018-06-03 21:32:39
    Switched from ArrayAdapter to CursorAdapter
 */

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import net.tuanpham.popularmovies.R;
import net.tuanpham.popularmovies.data.MovieContract;
import net.tuanpham.popularmovies.utils.TMDbUtils;

public class MovieAdapter extends CursorAdapter {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private Cursor mCursor;

    public MovieAdapter(Activity context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater
                .from(context)
                .inflate(R.layout.movie_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView movieImageView = (ImageView) view.findViewById(R.id.iv_movie);

        String posterPath = cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH));

        Picasso.with(context)
                .load(TMDbUtils.TMDB_MOVIES_IMAGE_BASE_W185_URL + posterPath)
                .into(movieImageView);
    }
}
