package com.cqu.filmsystem.pojo;

import java.util.Objects;

public class Type {
    private int genreId;
    private String genreName;
    private int movieCount;


    public Type(){
    }


    public Type(Integer tagId) {
        this.genreId=tagId;
    }


    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public int getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
    }

    @Override
    public String toString() {
        return "Type{" +
                "genreId=" + genreId +
                ", genreName='" + genreName + '\'' +
                ", movieCount=" + movieCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return genreId == type.genreId && movieCount == type.movieCount && Objects.equals(genreName, type.genreName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId, genreName, movieCount);
    }
}
