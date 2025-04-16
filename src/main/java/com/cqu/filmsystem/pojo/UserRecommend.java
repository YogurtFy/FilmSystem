package com.cqu.filmsystem.pojo;

import com.cqu.filmsystem.test.Movie;

import java.util.Date;
import java.util.List;
import java.util.Objects;



public class UserRecommend {

    private int id;

    private String username;

    private Movice movice;

    private List<Movice> moviceList;

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

    public List<Movice> getMoviceList() {
        return moviceList;
    }

    public void setMoviceList(List<Movice> moviceList) {
        this.moviceList = moviceList;
    }

    public Movice getMovice() {
        return movice;
    }

    public void setMovice(Movice movice) {
        this.movice = movice;
    }


    @Override
    public String toString() {
        return "UserRecommend{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", movice=" + movice +
                ", moviceList=" + moviceList +
                '}';
    }

    public Movice find(String movieName) {
        for (Movice movie : moviceList) {
            // 使用 Objects.equals() 可以安全地避免 NullPointerException
            if (Objects.equals(movie.getTitle(), movieName)) {
                return movie;
            }
        }
        return null;
    }

}
