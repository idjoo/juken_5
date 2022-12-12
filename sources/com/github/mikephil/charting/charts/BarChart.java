package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.highlight.BarHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.renderer.XAxisRendererBarChart;

public class BarChart extends BarLineChartBase<BarData> implements BarDataProvider {
    private boolean mDrawBarShadow = false;
    private boolean mDrawHighlightArrow = false;
    private boolean mDrawValueAboveBar = true;

    public BarChart(Context context) {
        super(context);
    }

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* access modifiers changed from: protected */
    public void init() {
        super.init();
        this.mRenderer = new BarChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.mXAxisRenderer = new XAxisRendererBarChart(this.mViewPortHandler, this.mXAxis, this.mLeftAxisTransformer, this);
        setHighlighter(new BarHighlighter(this));
        this.mXAxis.mAxisMinimum = -0.5f;
    }

    /* access modifiers changed from: protected */
    public void calcMinMax() {
        super.calcMinMax();
        this.mXAxis.mAxisRange += 0.5f;
        XAxis xAxis = this.mXAxis;
        xAxis.mAxisRange = ((float) ((BarData) this.mData).getDataSetCount()) * xAxis.mAxisRange;
        float groupSpace = ((BarData) this.mData).getGroupSpace();
        XAxis xAxis2 = this.mXAxis;
        xAxis2.mAxisRange = (((float) ((BarData) this.mData).getXValCount()) * groupSpace) + xAxis2.mAxisRange;
        this.mXAxis.mAxisMaximum = this.mXAxis.mAxisRange - this.mXAxis.mAxisMinimum;
    }

    public Highlight getHighlightByTouchPoint(float x, float y) {
        if (this.mData != null) {
            return getHighlighter().getHighlight(x, y);
        }
        Log.e(Chart.LOG_TAG, "Can't select by touch. No data set.");
        return null;
    }

    public RectF getBarBounds(BarEntry e) {
        float top;
        float bottom = 0.0f;
        IBarDataSet set = (IBarDataSet) ((BarData) this.mData).getDataSetForEntry(e);
        if (set == null) {
            return null;
        }
        float barspace = set.getBarSpace();
        float y = e.getVal();
        float x = (float) e.getXIndex();
        float spaceHalf = barspace / 2.0f;
        float left = (x - 0.5f) + spaceHalf;
        float right = (x + 0.5f) - spaceHalf;
        if (y >= 0.0f) {
            top = y;
        } else {
            top = 0.0f;
        }
        if (y <= 0.0f) {
            bottom = y;
        }
        RectF bounds = new RectF(left, top, right, bottom);
        getTransformer(set.getAxisDependency()).rectValueToPixel(bounds);
        return bounds;
    }

    public void setDrawHighlightArrow(boolean enabled) {
        this.mDrawHighlightArrow = enabled;
    }

    public boolean isDrawHighlightArrowEnabled() {
        return this.mDrawHighlightArrow;
    }

    public void setDrawValueAboveBar(boolean enabled) {
        this.mDrawValueAboveBar = enabled;
    }

    public boolean isDrawValueAboveBarEnabled() {
        return this.mDrawValueAboveBar;
    }

    public void setDrawBarShadow(boolean enabled) {
        this.mDrawBarShadow = enabled;
    }

    public boolean isDrawBarShadowEnabled() {
        return this.mDrawBarShadow;
    }

    public BarData getBarData() {
        return (BarData) this.mData;
    }

    public int getLowestVisibleXIndex() {
        float f;
        float step = (float) ((BarData) this.mData).getDataSetCount();
        float div = step <= 1.0f ? 1.0f : step + ((BarData) this.mData).getGroupSpace();
        float[] pts = {this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom()};
        getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        if (pts[0] <= getXChartMin()) {
            f = 0.0f;
        } else {
            f = (pts[0] / div) + 1.0f;
        }
        return (int) f;
    }

    public int getHighestVisibleXIndex() {
        float f;
        float div = 1.0f;
        float step = (float) ((BarData) this.mData).getDataSetCount();
        if (step > 1.0f) {
            div = step + ((BarData) this.mData).getGroupSpace();
        }
        float[] pts = {this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom()};
        getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        if (pts[0] >= getXChartMax()) {
            f = getXChartMax() / div;
        } else {
            f = pts[0] / div;
        }
        return (int) f;
    }
}
