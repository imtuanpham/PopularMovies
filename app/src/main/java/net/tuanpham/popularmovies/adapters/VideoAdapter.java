package net.tuanpham.popularmovies.adapters;

/*
    @author: Tuan Pham
    @since: 2018-06-07 21:34:45
    Based on https://github.com/udacity/android-custom-arrayadapter
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.tuanpham.popularmovies.R;
import net.tuanpham.popularmovies.models.Movie;

import java.util.List;

public class VideoAdapter extends ArrayAdapter<Movie.Video> {
    private static final String LOG_TAG = VideoAdapter.class.getSimpleName();

    /**
     *
     * @param context The current context used to inflate the layout file.
     */
    public VideoAdapter(Activity context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Video object from the ArrayAdapter at the appropriate position
        Movie.Video video = getItem(position);

        Context context = getContext();

        if (convertView == null) {
            convertView = LayoutInflater
                    .from(context)
                    .inflate(R.layout.video_item, parent, false);
        }

        TextView videoTextView = (TextView) convertView.findViewById(R.id.tv_video);

        videoTextView.setText(video.getName());

        return convertView;
    }

    public void setVideoList(List<Movie.Video> videoList) {
        this.clear();
        this.addAll(videoList);
        notifyDataSetChanged();
    }
}