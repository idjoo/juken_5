package com.androidplot.ui;

import android.graphics.RectF;

public interface BoxModelable {
    float getMarginBottom();

    float getMarginLeft();

    float getMarginRight();

    float getMarginTop();

    RectF getMarginatedRect(RectF rectF);

    RectF getPaddedRect(RectF rectF);

    float getPaddingBottom();

    float getPaddingLeft();

    float getPaddingRight();

    float getPaddingTop();

    void setMarginBottom(float f);

    void setMarginLeft(float f);

    void setMarginRight(float f);

    void setMarginTop(float f);

    void setMargins(float f, float f2, float f3, float f4);

    void setPadding(float f, float f2, float f3, float f4);

    void setPaddingBottom(float f);

    void setPaddingLeft(float f);

    void setPaddingRight(float f);

    void setPaddingTop(float f);
}
