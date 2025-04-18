package com.cqu.filmsystem.pojo;

import java.util.Objects;

public class Type {
    private int categoryId;
    private String categoryName;
    private int movieCount;


    public Type(){
    }


    public Type(Integer tagId) {
        this.categoryId =tagId;
    }


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", movieCount=" + movieCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return categoryId == type.categoryId && movieCount == type.movieCount && Objects.equals(categoryName, type.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, categoryName, movieCount);
    }
}
