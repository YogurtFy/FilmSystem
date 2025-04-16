package com.cqu.filmsystem.pojo;

import java.util.Date;

public class MovieTag {

    private long userId;
    private int movieId;
    private double rating;
    private Date ratingDate;


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Date getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Date ratingDate) {
        this.ratingDate = ratingDate;
    }

    @Override
    public String toString() {
        return "MovieTag{" +
                "userId=" + userId +
                ", movieId=" + movieId +
                ", rating=" + rating +
                ", ratingDate=" + ratingDate +
                '}';
    }
}
