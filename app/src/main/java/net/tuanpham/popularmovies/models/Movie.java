package net.tuanpham.popularmovies.models;

/*
    @author: Tuan Pham
    @since: 2018-05-22 21:50:10
 */

import java.util.ArrayList;
import java.util.Date;

public class Movie {
    private int id;
    private String originalTitle;
    private String posterPath;
    private String overview;
    private double voteAverage;
    private Date releaseDate;
    private boolean isFavorite;

    private ArrayList<Review> reviews;
    private ArrayList<Video> videos;

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

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public String getReviews() {
        if(reviews == null) return "";

        String str = "";
        for (Review review: reviews) {
            str = str + review.author + "\n" + review.content + "\n\n";
        }
        return str;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public Movie(int vId, String vOriginalTitle, String vPosterPath, String vOverview, double vVoteAverage, Date vReleaseDate) {
        this.id = vId;
        this.originalTitle = vOriginalTitle;
        this.posterPath = vPosterPath;
        this.overview = vOverview;
        this.voteAverage = vVoteAverage;
        this.releaseDate = vReleaseDate;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String toString() {
        String str = String.valueOf(this.id)
                + "--" + this.originalTitle
                + "--" + this.posterPath
                + "--" + this.overview
                + "--" + String.valueOf(this.voteAverage)
                + "--" + this.releaseDate.toString()
                + "--" + String.valueOf(this.isFavorite);

        if(this.reviews != null) {
            str = str + this.reviews.toString();
        }

        return str;
    }

    public static class Review {
        private String author;
        private String content;

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }


        public Review(String vAuthor, String vContent) {
            this.author = vAuthor;
            this.content = vContent;
        }

        public String toString() {
            return this.author
                    + "--" + this.content;
        }
    }

    /*

    "key":"6ZfuNTqbHE8",
    "name":"Official Trailer",
    "site":"YouTube",
    "size":1080,
    "type":"Trailer"
     */

    public static class Video {

        private String key;
        private String name;
        private String site;
        private int size;
        private String type;

        public Video(String key, String name, String site, int size, String type) {
            this.key = key;
            this.name = name;
            this.site = site;
            this.size = size;
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public String getSite() {
            return site;
        }

        public int getSize() {
            return size;
        }

        public String getType() {
            return type;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String toString() {
            return this.key
                    + "--" + this.name
                    + "--" + this.site
                    + "--" + this.size
                    + "--" + this.type;
        }
    }
}