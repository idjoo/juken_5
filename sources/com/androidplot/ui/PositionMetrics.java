package com.androidplot.ui;

import android.support.annotation.NonNull;

public class PositionMetrics implements Comparable<PositionMetrics> {
    private Anchor anchor;
    private HorizontalPosition horizontalPosition;
    private float layerDepth;
    private VerticalPosition verticalPosition;

    public PositionMetrics(float x, HorizontalPositioning horizontalPositioning, float y, VerticalPositioning verticalPositioning, Anchor anchor2) {
        setXPositionMetric(new HorizontalPosition(x, horizontalPositioning));
        setYPositionMetric(new VerticalPosition(y, verticalPositioning));
        setAnchor(anchor2);
    }

    public VerticalPosition getYPositionMetric() {
        return this.verticalPosition;
    }

    public void setYPositionMetric(VerticalPosition verticalPosition2) {
        this.verticalPosition = verticalPosition2;
    }

    public Anchor getAnchor() {
        return this.anchor;
    }

    public void setAnchor(Anchor anchor2) {
        this.anchor = anchor2;
    }

    public int compareTo(@NonNull PositionMetrics o) {
        if (this.layerDepth < o.layerDepth) {
            return -1;
        }
        if (this.layerDepth == o.layerDepth) {
            return 0;
        }
        return 1;
    }

    public HorizontalPosition getXPositionMetric() {
        return this.horizontalPosition;
    }

    public void setXPositionMetric(HorizontalPosition horizontalPosition2) {
        this.horizontalPosition = horizontalPosition2;
    }
}
