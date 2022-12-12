package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.data.Entry;

public interface IRadarDataSet extends ILineRadarDataSet<Entry> {
    int getHighlightCircleFillColor();

    float getHighlightCircleInnerRadius();

    float getHighlightCircleOuterRadius();

    int getHighlightCircleStrokeAlpha();

    int getHighlightCircleStrokeColor();

    float getHighlightCircleStrokeWidth();

    boolean isDrawHighlightCircleEnabled();

    void setDrawHighlightCircleEnabled(boolean z);
}
