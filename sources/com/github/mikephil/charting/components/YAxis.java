package com.github.mikephil.charting.components;

import android.graphics.Paint;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.DefaultYAxisValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;

public class YAxis extends AxisBase {
    private AxisDependency mAxisDependency;
    public int mDecimals;
    private boolean mDrawTopYLabelEntry;
    protected boolean mDrawZeroLine;
    public float[] mEntries;
    public int mEntryCount;
    protected boolean mForceLabels;
    protected float mGranularity;
    protected boolean mGranularityEnabled;
    protected boolean mInverted;
    private int mLabelCount;
    protected float mMaxWidth;
    protected float mMinWidth;
    private YAxisLabelPosition mPosition;
    protected boolean mShowOnlyMinMax;
    protected float mSpacePercentBottom;
    protected float mSpacePercentTop;
    protected YAxisValueFormatter mYAxisValueFormatter;
    protected int mZeroLineColor;
    protected float mZeroLineWidth;

    public enum AxisDependency {
        LEFT,
        RIGHT
    }

    public enum YAxisLabelPosition {
        OUTSIDE_CHART,
        INSIDE_CHART
    }

    public YAxis() {
        this.mEntries = new float[0];
        this.mLabelCount = 6;
        this.mDrawTopYLabelEntry = true;
        this.mShowOnlyMinMax = false;
        this.mInverted = false;
        this.mForceLabels = false;
        this.mDrawZeroLine = false;
        this.mZeroLineColor = -7829368;
        this.mZeroLineWidth = 1.0f;
        this.mSpacePercentTop = 10.0f;
        this.mSpacePercentBottom = 10.0f;
        this.mPosition = YAxisLabelPosition.OUTSIDE_CHART;
        this.mMinWidth = 0.0f;
        this.mMaxWidth = Float.POSITIVE_INFINITY;
        this.mGranularityEnabled = true;
        this.mGranularity = 1.0f;
        this.mAxisDependency = AxisDependency.LEFT;
        this.mYOffset = 0.0f;
    }

    public YAxis(AxisDependency position) {
        this.mEntries = new float[0];
        this.mLabelCount = 6;
        this.mDrawTopYLabelEntry = true;
        this.mShowOnlyMinMax = false;
        this.mInverted = false;
        this.mForceLabels = false;
        this.mDrawZeroLine = false;
        this.mZeroLineColor = -7829368;
        this.mZeroLineWidth = 1.0f;
        this.mSpacePercentTop = 10.0f;
        this.mSpacePercentBottom = 10.0f;
        this.mPosition = YAxisLabelPosition.OUTSIDE_CHART;
        this.mMinWidth = 0.0f;
        this.mMaxWidth = Float.POSITIVE_INFINITY;
        this.mGranularityEnabled = true;
        this.mGranularity = 1.0f;
        this.mAxisDependency = position;
        this.mYOffset = 0.0f;
    }

    public AxisDependency getAxisDependency() {
        return this.mAxisDependency;
    }

    public float getMinWidth() {
        return this.mMinWidth;
    }

    public void setMinWidth(float minWidth) {
        this.mMinWidth = minWidth;
    }

    public float getMaxWidth() {
        return this.mMaxWidth;
    }

    public void setMaxWidth(float maxWidth) {
        this.mMaxWidth = maxWidth;
    }

    public boolean isGranularityEnabled() {
        return this.mGranularityEnabled;
    }

    public void setGranularityEnabled(boolean enabled) {
        this.mGranularityEnabled = true;
    }

    public float getGranularity() {
        return this.mGranularity;
    }

    public void setGranularity(float granularity) {
        this.mGranularity = granularity;
    }

    public YAxisLabelPosition getLabelPosition() {
        return this.mPosition;
    }

    public void setPosition(YAxisLabelPosition pos) {
        this.mPosition = pos;
    }

    public boolean isDrawTopYLabelEntryEnabled() {
        return this.mDrawTopYLabelEntry;
    }

    public void setDrawTopYLabelEntry(boolean enabled) {
        this.mDrawTopYLabelEntry = enabled;
    }

    public void setLabelCount(int count, boolean force) {
        if (count > 25) {
            count = 25;
        }
        if (count < 2) {
            count = 2;
        }
        this.mLabelCount = count;
        this.mForceLabels = force;
    }

    public int getLabelCount() {
        return this.mLabelCount;
    }

    public boolean isForceLabelsEnabled() {
        return this.mForceLabels;
    }

    public void setShowOnlyMinMax(boolean enabled) {
        this.mShowOnlyMinMax = enabled;
    }

    public boolean isShowOnlyMinMaxEnabled() {
        return this.mShowOnlyMinMax;
    }

    public void setInverted(boolean enabled) {
        this.mInverted = enabled;
    }

    public boolean isInverted() {
        return this.mInverted;
    }

    @Deprecated
    public void setStartAtZero(boolean startAtZero) {
        if (startAtZero) {
            setAxisMinValue(0.0f);
        } else {
            resetAxisMinValue();
        }
    }

    public void setSpaceTop(float percent) {
        this.mSpacePercentTop = percent;
    }

    public float getSpaceTop() {
        return this.mSpacePercentTop;
    }

    public void setSpaceBottom(float percent) {
        this.mSpacePercentBottom = percent;
    }

    public float getSpaceBottom() {
        return this.mSpacePercentBottom;
    }

    public boolean isDrawZeroLineEnabled() {
        return this.mDrawZeroLine;
    }

    public void setDrawZeroLine(boolean mDrawZeroLine2) {
        this.mDrawZeroLine = mDrawZeroLine2;
    }

    public int getZeroLineColor() {
        return this.mZeroLineColor;
    }

    public void setZeroLineColor(int color) {
        this.mZeroLineColor = color;
    }

    public float getZeroLineWidth() {
        return this.mZeroLineWidth;
    }

    public void setZeroLineWidth(float width) {
        this.mZeroLineWidth = Utils.convertDpToPixel(width);
    }

    public float getRequiredWidthSpace(Paint p) {
        p.setTextSize(this.mTextSize);
        float width = ((float) Utils.calcTextWidth(p, getLongestLabel())) + (getXOffset() * 2.0f);
        float minWidth = getMinWidth();
        float maxWidth = getMaxWidth();
        if (minWidth > 0.0f) {
            minWidth = Utils.convertDpToPixel(minWidth);
        }
        if (maxWidth > 0.0f && maxWidth != Float.POSITIVE_INFINITY) {
            maxWidth = Utils.convertDpToPixel(maxWidth);
        }
        if (((double) maxWidth) <= 0.0d) {
            maxWidth = width;
        }
        return Math.max(minWidth, Math.min(width, maxWidth));
    }

    public float getRequiredHeightSpace(Paint p) {
        p.setTextSize(this.mTextSize);
        return ((float) Utils.calcTextHeight(p, getLongestLabel())) + (getYOffset() * 2.0f);
    }

    public String getLongestLabel() {
        String longest = "";
        for (int i = 0; i < this.mEntries.length; i++) {
            String text = getFormattedLabel(i);
            if (longest.length() < text.length()) {
                longest = text;
            }
        }
        return longest;
    }

    public String getFormattedLabel(int index) {
        if (index < 0 || index >= this.mEntries.length) {
            return "";
        }
        return getValueFormatter().getFormattedValue(this.mEntries[index], this);
    }

    public void setValueFormatter(YAxisValueFormatter f) {
        if (f == null) {
            this.mYAxisValueFormatter = new DefaultYAxisValueFormatter(this.mDecimals);
        } else {
            this.mYAxisValueFormatter = f;
        }
    }

    public YAxisValueFormatter getValueFormatter() {
        if (this.mYAxisValueFormatter == null) {
            this.mYAxisValueFormatter = new DefaultYAxisValueFormatter(this.mDecimals);
        }
        return this.mYAxisValueFormatter;
    }

    public boolean needsDefaultFormatter() {
        if (this.mYAxisValueFormatter != null && !(this.mYAxisValueFormatter instanceof DefaultValueFormatter)) {
            return false;
        }
        return true;
    }

    public boolean needsOffset() {
        if (!isEnabled() || !isDrawLabelsEnabled() || getLabelPosition() != YAxisLabelPosition.OUTSIDE_CHART) {
            return false;
        }
        return true;
    }

    public void calcMinMax(float dataMin, float dataMax) {
        float min;
        float max;
        if (this.mCustomAxisMin) {
            min = this.mAxisMinimum;
        } else {
            min = dataMin;
        }
        if (this.mCustomAxisMax) {
            max = this.mAxisMaximum;
        } else {
            max = dataMax;
        }
        float range = Math.abs(max - min);
        if (range == 0.0f) {
            max += 1.0f;
            min -= 1.0f;
        }
        if (!this.mCustomAxisMin) {
            this.mAxisMinimum = min - ((range / 100.0f) * getSpaceBottom());
        }
        if (!this.mCustomAxisMax) {
            this.mAxisMaximum = max + ((range / 100.0f) * getSpaceTop());
        }
        this.mAxisRange = Math.abs(this.mAxisMaximum - this.mAxisMinimum);
    }
}
