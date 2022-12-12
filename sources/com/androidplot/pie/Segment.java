package com.androidplot.pie;

import com.androidplot.Series;

public class Segment implements Series {
    private String title;
    private Number value;

    public Segment(String title2, Number value2) {
        this.title = title2;
        setValue(value2);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public Number getValue() {
        return this.value;
    }

    public void setValue(Number value2) {
        this.value = value2;
    }
}
