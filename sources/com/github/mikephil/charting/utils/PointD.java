package com.github.mikephil.charting.utils;

public class PointD {
    public double x;
    public double y;

    public PointD(double x2, double y2) {
        this.x = x2;
        this.y = y2;
    }

    public String toString() {
        return "PointD, x: " + this.x + ", y: " + this.y;
    }
}
