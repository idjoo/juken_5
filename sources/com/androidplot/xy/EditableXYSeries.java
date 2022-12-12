package com.androidplot.xy;

public interface EditableXYSeries extends XYSeries {
    void resize(int i);

    void setX(Number number, int i);

    void setY(Number number, int i);
}
