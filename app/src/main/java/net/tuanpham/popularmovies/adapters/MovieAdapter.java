package net.tuanpham.popularmovies.adapters;

/*
    @author: Tuan Pham
    @since: 2018-05-23 20:54:06
    Based on https://github.com/udacity/android-custom-arrayadapter
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import net.tuanpham.popularmovies.R;
import net.tuanpham.popularmovies.models.Movie;
import net.tuanpham.popularmovies.utils.TMDbUtils;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    /**
     *
     * @param context The current context used to inflate the layout file.
     */
    public MovieAdapter(Activity context) {
        super(context, 0);
    }

    /**
     *
     * @param position    The AdapterView position
     * @param convertView The recycled view to populate
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Movie object from the ArrayAdapter at the appropriate position
        Movie movie = getItem(position);

        Context context = getContext();

        if (convertView == null) {
            convertView = LayoutInflater
                                .from(context)
                                .inflate(R.layout.movie_item, parent, false);
        }

        ImageView movieImageView = (ImageView) convertView.findViewById(R.id.iv_movie);

        Picasso.with(getContext())
                .load(TMDbUtils.TMDB_MOVIES_IMAGE_BASE_URL + movie.getPosterPath())
                .into(movieImageView);

        return convertView;
    }

    public void setMovieList(List<Movie> movieList) {
        this.clear();
        this.addAll(movieList);
        notifyDataSetChanged();
    }
}
