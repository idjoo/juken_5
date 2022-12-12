package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;

public class RadarDataSet extends LineRadarDataSet<Entry> implements IRadarDataSet {
    protected boolean mDrawHighlightCircleEnabled = false;
    protected int mHighlightCircleFillColor = -1;
    protected float mHighlightCircleInnerRadius = 3.0f;
    protected float mHighlightCircleOuterRadius = 4.0f;
    protected int mHighlightCircleStrokeAlpha = 76;
    protected int mHighlightCircleStrokeColor = ColorTemplate.COLOR_NONE;
    protected float mHighlightCircleStrokeWidth = 2.0f;

    public RadarDataSet(List<Entry> yVals, String label) {
        super(yVals, label);
    }

    public boolean isDrawHighlightCircleEnabled() {
        return this.mDrawHighlightCircleEnabled;
    }

    public void setDrawHighlightCircleEnabled(boolean enabled) {
        this.mDrawHighlightCircleEnabled = enabled;
    }

    public int getHighlightCircleFillColor() {
        return this.mHighlightCircleFillColor;
    }

    public void setHighlightCircleFillColor(int color) {
        this.mHighlightCircleFillColor = color;
    }

    public int getHighlightCircleStrokeColor() {
        return this.mHighlightCircleStrokeColor;
    }

    public void setHighlightCircleStrokeColor(int color) {
        this.mHighlightCircleStrokeColor = color;
    }

    public int getHighlightCircleStrokeAlpha() {
        return this.mHighlightCircleStrokeAlpha;
    }

    public void setHighlightCircleStrokeAlpha(int alpha) {
        this.mHighlightCircleStrokeAlpha = alpha;
    }

    public float getHighlightCircleInnerRadius() {
        return this.mHighlightCircleInnerRadius;
    }

    public void setHighlightCircleInnerRadius(float radius) {
        this.mHighlightCircleInnerRadius = radius;
    }

    public float getHighlightCircleOuterRadius() {
        return this.mHighlightCircleOuterRadius;
    }

    public void setHighlightCircleOuterRadius(float radius) {
        this.mHighlightCircleOuterRadius = radius;
    }

    public float getHighlightCircleStrokeWidth() {
        return this.mHighlightCircleStrokeWidth;
    }

    public void setHighlightCircleStrokeWidth(float strokeWidth) {
        this.mHighlightCircleStrokeWidth = strokeWidth;
    }

    public DataSet<Entry> copy() {
        List<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < this.mYVals.size(); i++) {
            yVals.add(((Entry) this.mYVals.get(i)).copy());
        }
        RadarDataSet copied = new RadarDataSet(yVals, getLabel());
        copied.mColors = this.mColors;
        copied.mHighLightColor = this.mHighLightColor;
        return copied;
    }
}
