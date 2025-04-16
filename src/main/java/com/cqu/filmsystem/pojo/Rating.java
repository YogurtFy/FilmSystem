package com.cqu.filmsystem.pojo;

import java.math.BigInteger;
import java.util.Date;

public class Rating {
    private Long userId;
    private int movieId;
    private Double rating;
    private Date ratingDate;
    private  UserInfo userInfo;
    private Movice movice;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Date getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Date ratingDate) {
        this.ratingDate = ratingDate;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Movice getMovice() {
        return movice;
    }

    public void setMovice(Movice movice) {
        this.movice = movice;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "userId=" + userId +
                ", movieId=" + movieId +
                ", rating=" + rating +
                ", ratingDate=" + ratingDate +
                ", userInfo=" + userInfo +
                ", movice=" + movice +
                '}';
    }
}
