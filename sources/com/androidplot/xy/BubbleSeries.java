package com.androidplot.xy;

import java.util.ArrayList;
import java.util.List;

public class BubbleSeries implements XYSeries {
    private String title;
    private List<Number> xVals;
    private List<Number> yVals;
    private List<Number> zVals;

    public BubbleSeries(Number... interleavedValues) {
        if (interleavedValues == null || interleavedValues.length % 3 > 0) {
            throw new RuntimeException("BubbleSeries interleave array length must be a non-zero multiple of 3.");
        }
        this.xVals = new ArrayList();
        this.yVals = new ArrayList();
        this.zVals = new ArrayList();
        for (int i = 0; i < interleavedValues.length; i += 3) {
            this.xVals.add(interleavedValues[i]);
            this.yVals.add(interleavedValues[i + 1]);
            this.zVals.add(interleavedValues[i + 2]);
        }
    }

    public BubbleSeries(List<Number> yVals2, List<Number> zVals2, String title2) {
        this.yVals = yVals2;
        this.zVals = zVals2;
        this.title = title2;
        this.xVals = new ArrayList(zVals2.size());
        for (int i = 0; i < zVals2.size(); i++) {
            this.xVals.add(Integer.valueOf(i));
        }
    }

    public BubbleSeries(List<Number> xVals2, List<Number> yVals2, List<Number> zVals2, String title2) {
        this.xVals = xVals2;
        this.yVals = yVals2;
        this.zVals = zVals2;
        this.title = title2;
    }

    public String getTitle() {
        return this.title;
    }

    public int size() {
        return this.xVals.size();
    }

    public Number getX(int index) {
        return this.xVals.get(index);
    }

    public Number getY(int index) {
        return this.yVals.get(index);
    }

    public Number getZ(int index) {
        return this.zVals.get(index);
    }

    public List<Number> getZVals() {
        return this.zVals;
    }
}
