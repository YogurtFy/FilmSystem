package com.cqu.filmsystem.pojo;
import java.io.Serializable;
import java.util.Date;


public class Syslog implements Serializable {

    private Long id;  //我用的全宇宙唯一的子串串、也是直接用的工具类

    private String username; //用户名

    private String operation; //操作

    private String method; //方法名

    private Date createDate; //操作时间，这里可以使用Date来实现。我写的有个工具类。用的String接收

    private Movice movice;  //观看的电影

    private double ViewTime; //持续时间


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    public Movice getMovice() {
        return movice;
    }

    public void setMovice(Movice movice) {
        this.movice = movice;
    }

    public double getViewTime() {
        return ViewTime;
    }

    public void setViewTime(double viewTime) {
        ViewTime = viewTime;
    }

    @Override
    public String toString() {
        return "Syslog{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", operation='" + operation + '\'' +
                ", method='" + method + '\'' +
                ", createDate=" + createDate +
                ", movice=" + movice +
                ", ViewTime=" + ViewTime +
                '}';
    }
}