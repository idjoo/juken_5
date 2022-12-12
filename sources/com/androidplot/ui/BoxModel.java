package com.androidplot.ui;

import android.graphics.RectF;

public class BoxModel implements BoxModelable {
    private float marginBottom;
    private float marginLeft;
    private float marginRight;
    private float marginTop;
    private float paddingBottom;
    private float paddingLeft;
    private float paddingRight;
    private float paddingTop;

    public BoxModel() {
    }

    public BoxModel(float marginLeft2, float marginTop2, float marginRight2, float marginBottom2, float paddingLeft2, float paddingTop2, float paddingRight2, float paddingBottom2) {
        this.marginLeft = marginLeft2;
        this.marginTop = marginTop2;
        this.marginRight = marginRight2;
        this.marginBottom = marginBottom2;
        this.paddingLeft = paddingLeft2;
        this.paddingTop = paddingTop2;
        this.paddingRight = paddingRight2;
        this.paddingBottom = paddingBottom2;
    }

    public RectF getMarginatedRect(RectF boundsRect) {
        return new RectF(boundsRect.left + getMarginLeft(), boundsRect.top + getMarginTop(), boundsRect.right - getMarginRight(), boundsRect.bottom - getMarginBottom());
    }

    public RectF getPaddedRect(RectF marginRect) {
        return new RectF(marginRect.left + getPaddingLeft(), marginRect.top + getPaddingTop(), marginRect.right - getPaddingRight(), marginRect.bottom - getPaddingBottom());
    }

    public void setMargins(float left, float top, float right, float bottom) {
        setMarginLeft(left);
        setMarginTop(top);
        setMarginRight(right);
        setMarginBottom(bottom);
    }

    public void setPadding(float left, float top, float right, float bottom) {
        setPaddingLeft(left);
        setPaddingTop(top);
        setPaddingRight(right);
        setPaddingBottom(bottom);
    }

    public float getMarginLeft() {
        return this.marginLeft;
    }

    public void setMarginLeft(float marginLeft2) {
        this.marginLeft = marginLeft2;
    }

    public float getMarginTop() {
        return this.marginTop;
    }

    public void setMarginTop(float marginTop2) {
        this.marginTop = marginTop2;
    }

    public float getMarginRight() {
        return this.marginRight;
    }

    public void setMarginRight(float marginRight2) {
        this.marginRight = marginRight2;
    }

    public float getMarginBottom() {
        return this.marginBottom;
    }

    public void setMarginBottom(float marginBottom2) {
        this.marginBottom = marginBottom2;
    }

    public float getPaddingLeft() {
        return this.paddingLeft;
    }

    public void setPaddingLeft(float paddingLeft2) {
        this.paddingLeft = paddingLeft2;
    }

    public float getPaddingTop() {
        return this.paddingTop;
    }

    public void setPaddingTop(float paddingTop2) {
        this.paddingTop = paddingTop2;
    }

    public float getPaddingRight() {
        return this.paddingRight;
    }

    public void setPaddingRight(float paddingRight2) {
        this.paddingRight = paddingRight2;
    }

    public float getPaddingBottom() {
        return this.paddingBottom;
    }

    public void setPaddingBottom(float paddingBottom2) {
        this.paddingBottom = paddingBottom2;
    }
}
