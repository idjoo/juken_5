package com.github.mikephil.charting.highlight;

public final class Range {
    public float from;
    public float to;

    public Range(float from2, float to2) {
        this.from = from2;
        this.to = to2;
    }

    public boolean contains(float value) {
        if (value <= this.from || value > this.to) {
            return false;
        }
        return true;
    }

    public boolean isLarger(float value) {
        return value > this.to;
    }

    public boolean isSmaller(float value) {
        return value < this.from;
    }
}
