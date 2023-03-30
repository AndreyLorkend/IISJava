package com.example.iisjava;

public class Point {
    private double _x;
    private double _y;

    Point(double x, double y) {
        this._x = x;
        this._y = y;
    }

    public double getX() {
        return this._x;
    }

    public double getY() {
        return this._y;
    }

    public void setX(double x) {
        this._x = x;
    }

    public void setY(double y) {
        this._y = y;
    }
}
