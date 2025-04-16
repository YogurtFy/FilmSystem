package com.cqu.filmsystem.pojo;

public class Parameter {
    private int id;
    private double B1;
    private double B2;
    private double B3;
    private double B4;
    private double u;
    private double c;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getB1() {
        return B1;
    }

    public void setB1(double b1) {
        B1 = b1;
    }

    public double getB2() {
        return B2;
    }

    public void setB2(double b2) {
        B2 = b2;
    }

    public double getB3() {
        return B3;
    }

    public void setB3(double b3) {
        B3 = b3;
    }

    public double getB4() {
        return B4;
    }

    public void setB4(double b4) {
        B4 = b4;
    }

    public double getU() {
        return u;
    }

    public void setU(double u) {
        this.u = u;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "parameter{" +
                "id=" + id +
                ", B1=" + B1 +
                ", B2=" + B2 +
                ", B3=" + B3 +
                ", B4=" + B4 +
                ", u=" + u +
                ", c=" + c +
                '}';
    }
}
