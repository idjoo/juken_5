package com.androidplot.ui;

public class Insets {
    private float bottom;
    private float left;
    private float right;
    private float top;

    public Insets() {
    }

    public Insets(float top2, float bottom2, float left2, float right2) {
        this.top = top2;
        this.bottom = bottom2;
        this.left = left2;
        this.right = right2;
    }

    public float getTop() {
        return this.top;
    }

    public void setTop(float top2) {
        this.top = top2;
    }

    public float getBottom() {
        return this.bottom;
    }

    public void setBottom(float bottom2) {
        this.bottom = bottom2;
    }

    public float getLeft() {
        return this.left;
    }

    public void setLeft(float left2) {
        this.left = left2;
    }

    public float getRight() {
        return this.right;
    }

    public void setRight(float right2) {
        this.right = right2;
    }
}
