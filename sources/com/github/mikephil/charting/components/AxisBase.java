package com.github.mikephil.charting.components;

import android.graphics.DashPathEffect;
import android.util.Log;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public abstract class AxisBase extends ComponentBase {
    private int mAxisLineColor = -7829368;
    private float mAxisLineWidth = 1.0f;
    public float mAxisMaximum = 0.0f;
    public float mAxisMinimum = 0.0f;
    public float mAxisRange = 0.0f;
    protected boolean mCustomAxisMax = false;
    protected boolean mCustomAxisMin = false;
    protected boolean mDrawAxisLine = true;
    protected boolean mDrawGridLines = true;
    protected boolean mDrawLabels = true;
    protected boolean mDrawLimitLineBehindData = false;
    private int mGridColor = -7829368;
    private DashPathEffect mGridDashPathEffect = null;
    private float mGridLineWidth = 1.0f;
    protected List<LimitLine> mLimitLines;

    public abstract String getLongestLabel();

    public AxisBase() {
        this.mTextSize = Utils.convertDpToPixel(10.0f);
        this.mXOffset = Utils.convertDpToPixel(5.0f);
        this.mYOffset = Utils.convertDpToPixel(5.0f);
        this.mLimitLines = new ArrayList();
    }

    public void setDrawGridLines(boolean enabled) {
        this.mDrawGridLines = enabled;
    }

    public boolean isDrawGridLinesEnabled() {
        return this.mDrawGridLines;
    }

    public void setDrawAxisLine(boolean enabled) {
        this.mDrawAxisLine = enabled;
    }

    public boolean isDrawAxisLineEnabled() {
        return this.mDrawAxisLine;
    }

    public void setGridColor(int color) {
        this.mGridColor = color;
    }

    public int getGridColor() {
        return this.mGridColor;
    }

    public void setAxisLineWidth(float width) {
        this.mAxisLineWidth = Utils.convertDpToPixel(width);
    }

    public float getAxisLineWidth() {
        return this.mAxisLineWidth;
    }

    public void setGridLineWidth(float width) {
        this.mGridLineWidth = Utils.convertDpToPixel(width);
    }

    public float getGridLineWidth() {
        return this.mGridLineWidth;
    }

    public void setAxisLineColor(int color) {
        this.mAxisLineColor = color;
    }

    public int getAxisLineColor() {
        return this.mAxisLineColor;
    }

    public void setDrawLabels(boolean enabled) {
        this.mDrawLabels = enabled;
    }

    public boolean isDrawLabelsEnabled() {
        return this.mDrawLabels;
    }

    public void addLimitLine(LimitLine l) {
        this.mLimitLines.add(l);
        if (this.mLimitLines.size() > 6) {
            Log.e("MPAndroiChart", "Warning! You have more than 6 LimitLines on your axis, do you really want that?");
        }
    }

    public void removeLimitLine(LimitLine l) {
        this.mLimitLines.remove(l);
    }

    public void removeAllLimitLines() {
        this.mLimitLines.clear();
    }

    public List<LimitLine> getLimitLines() {
        return this.mLimitLines;
    }

    public void setDrawLimitLinesBehindData(boolean enabled) {
        this.mDrawLimitLineBehindData = enabled;
    }

    public boolean isDrawLimitLinesBehindDataEnabled() {
        return this.mDrawLimitLineBehindData;
    }

    public void enableGridDashedLine(float lineLength, float spaceLength, float phase) {
        this.mGridDashPathEffect = new DashPathEffect(new float[]{lineLength, spaceLength}, phase);
    }

    public void disableGridDashedLine() {
        this.mGridDashPathEffect = null;
    }

    public boolean isGridDashedLineEnabled() {
        return this.mGridDashPathEffect != null;
    }

    public DashPathEffect getGridDashPathEffect() {
        return this.mGridDashPathEffect;
    }

    public float getAxisMaximum() {
        return this.mAxisMaximum;
    }

    public float getAxisMinimum() {
        return this.mAxisMinimum;
    }

    public void resetAxisMaxValue() {
        this.mCustomAxisMax = false;
    }

    public boolean isAxisMaxCustom() {
        return this.mCustomAxisMax;
    }

    public void resetAxisMinValue() {
        this.mCustomAxisMin = false;
    }

    public boolean isAxisMinCustom() {
        return this.mCustomAxisMin;
    }

    public void setAxisMinValue(float min) {
        this.mCustomAxisMin = true;
        this.mAxisMinimum = min;
    }

    public void setAxisMaxValue(float max) {
        this.mCustomAxisMax = true;
        this.mAxisMaximum = max;
    }
}
