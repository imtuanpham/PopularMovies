package net.tuanpham.popularmovies.models;

/*
    @author: Tuan Pham
    @since: 2018-05-22 21:50:10
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Movie implements Parcelable{
    private int id;
    private String originalTitle;
    private String posterPath;
    private String overview;
    private double voteAverage;
    private Date releaseDate;

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public Movie(int vId, String vOriginalTitle, String vPosterPath, String vOverview, double vVoteAverage, Date vReleaseDate) {
        this.id = vId;
        this.originalTitle = vOriginalTitle;
        this.posterPath = vPosterPath;
        this.overview = vOverview;
        this.voteAverage = vVoteAverage;
        this.releaseDate = vReleaseDate;
    }

    private Movie(Parcel in){
        super();
        this.readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return String.valueOf(this.id)
                + "--" + this.originalTitle
                + "--" + this.posterPath
                + "--" + this.overview
                + "--" + String.valueOf(this.voteAverage)
                + "--" + this.releaseDate.toString();
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readInt();
        this.originalTitle = in.readString();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.voteAverage = in.readDouble();
        // Read Long value and convert to date
        this.releaseDate = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(originalTitle);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeDouble(voteAverage);
        // Write long value of Date
        parcel.writeLong(releaseDate.getTime());
    }



    public final static Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}