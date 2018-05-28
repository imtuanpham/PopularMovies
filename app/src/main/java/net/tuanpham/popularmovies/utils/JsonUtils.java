package net.tuanpham.popularmovies.utils;
/*
    @author: Tuan Pham
    @since: 2018-05-22 21:50:10
 */
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

    public static ArrayList<Movie> parseMoviesJson(String json) throws JSONException {

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
}
