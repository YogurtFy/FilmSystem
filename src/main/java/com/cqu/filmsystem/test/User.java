package com.cqu.filmsystem.test;
import java.util.ArrayList;
import java.util.List;

/**
 * @author junfeng.lin
 * @date 2021/3/18 13:39
 */
public class User {
    public String username;
    public List<Movie> movieList = new ArrayList<>();



    public User() {}

    public User(String username) {
        this.username = username;
    }

    public User set(String movieName, int score) {
        this.movieList.add(new Movie(movieName, score));
        return this;
    }

    public Movie find(String movieName) {
        for (Movie movie : movieList) {
            if (movie.movieName.equals(username)) {
                return movie;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}

