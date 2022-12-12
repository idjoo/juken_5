package com.github.mikephil.charting.highlight;

public class Highlight {
    private int mDataSetIndex;
    private Range mRange;
    private int mStackIndex;
    private int mXIndex;

    public Highlight(int x, int dataSet) {
        this.mStackIndex = -1;
        this.mXIndex = x;
        this.mDataSetIndex = dataSet;
    }

    public Highlight(int x, int dataSet, int stackIndex) {
        this(x, dataSet);
        this.mStackIndex = stackIndex;
    }

    public Highlight(int x, int dataSet, int stackIndex, Range range) {
        this(x, dataSet, stackIndex);
        this.mRange = range;
    }

    public int getDataSetIndex() {
        return this.mDataSetIndex;
    }

    public int getXIndex() {
        return this.mXIndex;
    }

    public int getStackIndex() {
        return this.mStackIndex;
    }

    public Range getRange() {
        return this.mRange;
    }

    public boolean equalTo(Highlight h) {
        if (h != null && this.mDataSetIndex == h.mDataSetIndex && this.mXIndex == h.mXIndex && this.mStackIndex == h.mStackIndex) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "Highlight, xIndex: " + this.mXIndex + ", dataSetIndex: " + this.mDataSetIndex + ", stackIndex (only stacked barentry): " + this.mStackIndex;
    }
}
