package net.tuanpham.popularmovies.utils;
/*
    @author: Tuan Pham
    @since: 2018-05-22 21:50:10
 */
import android.content.ContentValues;

import net.tuanpham.popularmovies.data.MovieContract;
import net.tuanpham.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class JsonUtils {

    public static ArrayList<Movie> parseMoviesJsonToArrayList(String json) throws JSONException {

        JSONObject moviesJson = new JSONObject(json);
        JSONArray moviesJsonArray = moviesJson.getJSONArray("results");

        ArrayList<Movie> movieList = new ArrayList<>();

        for (int i = 0; i < moviesJsonArray.length(); i++) {
            JSONObject movieJson = moviesJsonArray.getJSONObject(i);
            int vId;
            String vOriginalTitle;
            String vPosterPath;
            String vOverview;
            double vVoteAverage;
            Date vReleaseDate;

            vId = movieJson.getInt("id");
            vOriginalTitle = movieJson.getString("original_title");
            vPosterPath = movieJson.getString("poster_path");
            vOverview = movieJson.getString("overview");
            vVoteAverage = movieJson.getDouble("vote_average");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
            try {
                vReleaseDate = formatter.parse(movieJson.getString("release_date"));
            } catch (ParseException e) {
                vReleaseDate = new Date();
                e.printStackTrace();
            }

            Movie movie = new Movie(vId, vOriginalTitle, vPosterPath, vOverview, vVoteAverage, vReleaseDate);
            movieList.add(movie);
        }

        return movieList;
    }

    public static Movie parseMovieJsonToMovie(String json) throws JSONException {

        JSONObject movieJson = new JSONObject(json);

        int vId;
        String vOriginalTitle;
        String vPosterPath;
        String vOverview;
        double vVoteAverage;
        Date vReleaseDate;

        vId = movieJson.getInt("id");
        vOriginalTitle = movieJson.getString("original_title");
        vPosterPath = movieJson.getString("poster_path");
        vOverview = movieJson.getString("overview");
        vVoteAverage = movieJson.getDouble("vote_average");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
        try {
            vReleaseDate = formatter.parse(movieJson.getString("release_date"));
        } catch (ParseException e) {
            vReleaseDate = new Date();
            e.printStackTrace();
        }

        Movie movie = new Movie(vId, vOriginalTitle, vPosterPath, vOverview, vVoteAverage, vReleaseDate);

        JSONArray reviewsJsonArray = movieJson.getJSONObject("reviews").getJSONArray("results");
        if (reviewsJsonArray != null && reviewsJsonArray.length() > 0) {
            ArrayList<Movie.Review> reviews = new ArrayList<>();

            for (int i = 0; i < reviewsJsonArray.length(); i++) {
                JSONObject reviewJson = reviewsJsonArray.getJSONObject(i);

                String vAuthor;
                String vContent;

                vAuthor = reviewJson.getString("author");
                vContent = reviewJson.getString("content");

                Movie.Review review = new Movie.Review(vAuthor, vContent);
                reviews.add(review);
            }
            movie.setReviews(reviews);
        }

        JSONArray videosJsonArray = movieJson.getJSONObject("videos").getJSONArray("results");
        if (videosJsonArray != null && videosJsonArray.length() > 0) {
            ArrayList<Movie.Video> videos = new ArrayList<>();

            for (int i = 0; i < videosJsonArray.length(); i++) {
                JSONObject videoJson = videosJsonArray.getJSONObject(i);

                String vKey, vName, vSite, vType;
                int vSize;

                vKey = videoJson.getString("key");
                vName = videoJson.getString("name");
                vSite = videoJson.getString("site");
                vSize = videoJson.getInt("size");
                vType = videoJson.getString("type");


                Movie.Video video = new Movie.Video(vKey, vName, vSite, vSize, vType);
                videos.add(video);
            }
            movie.setVideos(videos);
        }

        return movie;
    }

    public static ContentValues[] parseMoviesJsonToContentValuesArray(String json) throws JSONException {

        JSONObject moviesJson = new JSONObject(json);
        JSONArray moviesJsonArray = moviesJson.getJSONArray("results");

        ContentValues[] movieContentValues = new ContentValues[moviesJsonArray.length()];

        for (int i = 0; i < moviesJsonArray.length(); i++) {
            JSONObject movieJson = moviesJsonArray.getJSONObject(i);
            int vId;
            String vOriginalTitle;
            String vPosterPath;

            vId = movieJson.getInt("id");
            vOriginalTitle = movieJson.getString("original_title");
            vPosterPath = movieJson.getString("poster_path");

            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, vId);
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE, vOriginalTitle);
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, vPosterPath);

            // add last mod date for troubleshooting and auditing
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_LAST_MOD_DT, new Date().getTime());


            movieContentValues[i] = movieValues;
        }

        return movieContentValues;
    }
}
