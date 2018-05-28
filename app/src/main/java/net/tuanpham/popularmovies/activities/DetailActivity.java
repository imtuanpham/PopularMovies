package net.tuanpham.popularmovies.activities;

/*
    @author: Tuan Pham
    @since: 2018-05-28 12:25:27
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.tuanpham.popularmovies.R;
import net.tuanpham.popularmovies.models.Movie;
import net.tuanpham.popularmovies.utils.TMDbUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                mMovie = (Movie) intent.getParcelableExtra(Intent.EXTRA_TEXT);
//                Log.d(LOG_TAG, mMovie.toString());
                populateDetailUI();
            }
        }
    }

    private void populateDetailUI() {

        ImageView ivPosterPath = findViewById(R.id.iv_poster_path);

        Picasso.with(this)
                .load(TMDbUtils.TMDB_MOVIES_IMAGE_BASE_URL + mMovie.getPosterPath())
                .into(ivPosterPath);

        setTitle(mMovie.getOriginalTitle());

        TextView tvOriginalTitle  = findViewById(R.id.tv_original_title);
        tvOriginalTitle.setText(mMovie.getOriginalTitle());

        TextView tvVoteAverage  = findViewById(R.id.tv_vote_average);
        tvVoteAverage.setText(String.valueOf(mMovie.getVoteAverage()));

        SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy", Locale.US);

        TextView tvReleaseDate  = findViewById(R.id.tv_release_date);
        tvReleaseDate.setText(formatter.format(mMovie.getReleaseDate()));

        TextView tvOverview  = findViewById(R.id.tv_overview);
        tvOverview.setText(mMovie.getOverview());
    }

}