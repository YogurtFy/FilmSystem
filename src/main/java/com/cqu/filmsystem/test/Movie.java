package com.cqu.filmsystem.test;

/**
 * @author junfeng.lin
 * @date 2021/3/18 13:37
 */
public class Movie implements Comparable<Movie> {
    public String movieName;
    public int score;

    public Movie(String movieName, int score) {
        this.movieName = movieName;
        this.score = score;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieName='" + movieName + '\'' +
                ", score=" + score +
                '}';
    }
    @Override
    public int compareTo(Movie o) {
        return score > o.score ? -1 : 1;
    }

}

