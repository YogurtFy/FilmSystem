package com.cqu.filmsystem.pojo;

import java.util.Date;


public class Movie implements Comparable<Movie>{
    private  int id;
    private String title;
    private String director;
    private String category;
    private Date releaseDate;
    private int runtime;
    private String language;
    private String description;
    private String averageRating;
    private String picture;
    private String video;
    private int regionId;
    private int pageView;
    private int commentTime;
    private int favoritesTime;
    private int sum;


    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    //用于推荐的字段
    private double rating;
    private double ViewTime;

    public int getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(int commentTime) {
        this.commentTime = commentTime;
    }

    public int getFavoritesTime() {
        return favoritesTime;
    }

    public void setFavoritesTime(int favoritesTime) {
        this.favoritesTime = favoritesTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getPageView() {
        return pageView;
    }

    public void setPageView(int pageView) {
        this.pageView = pageView;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getViewTime() {
        return ViewTime;
    }

    public void setViewTime(double viewTime) {
        ViewTime = viewTime;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", category='" + category + '\'' +
                ", releaseDate=" + releaseDate +
                ", runtime=" + runtime +
                ", language='" + language + '\'' +
                ", description='" + description + '\'' +
                ", averageRating='" + averageRating + '\'' +
                ", picture='" + picture + '\'' +
                ", video='" + video + '\'' +
                ", regionId=" + regionId +
                ", pageView=" + pageView +
                ", commentTime=" + commentTime +
                ", favoritesTime=" + favoritesTime +
                ", sum=" + sum +
                ", rating=" + rating +
                ", ViewTime=" + ViewTime +
                '}';
    }

    @Override
    public int compareTo(Movie o) {
        return rating > o.rating ? -1 : 1;
    }


}

