package com.cqu.filmsystem.pojo;

import java.util.List;
import java.util.Objects;



public class UserRecommend {

    private int id;

    private String username;

    private Movie movie;

    private List<Movie> movieList;

    public UserRecommend(String username) {
        this.username=username;
    }

    public UserRecommend()
    {

    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }


    @Override
    public String toString() {
        return "UserRecommend{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", movie=" + movie +
                ", movieList=" + movieList +
                '}';
    }

    public Movie find(String movieName) {
        for (Movie movie : movieList) {
            // 使用 Objects.equals() 可以安全地避免 NullPointerException
            if (Objects.equals(movie.getTitle(), movieName)) {
                return movie;
            }
        }
        return null;
    }

}
