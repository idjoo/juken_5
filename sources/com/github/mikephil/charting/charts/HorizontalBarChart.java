package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.HorizontalBarHighlighter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer;
import com.github.mikephil.charting.renderer.XAxisRendererHorizontalBarChart;
import com.github.mikephil.charting.renderer.YAxisRendererHorizontalBarChart;
import com.github.mikephil.charting.utils.TransformerHorizontalBarChart;
import com.github.mikephil.charting.utils.Utils;

public class HorizontalBarChart extends BarChart {
    public HorizontalBarChart(Context context) {
        super(context);
    }

    public HorizontalBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* access modifiers changed from: protected */
    public void init() {
        super.init();
        this.mLeftAxisTransformer = new TransformerHorizontalBarChart(this.mViewPortHandler);
        this.mRightAxisTransformer = new TransformerHorizontalBarChart(this.mViewPortHandler);
        this.mRenderer = new HorizontalBarChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        setHighlighter(new HorizontalBarHighlighter(this));
        this.mAxisRendererLeft = new YAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mAxisLeft, this.mLeftAxisTransformer);
        this.mAxisRendererRight = new YAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mAxisRight, this.mRightAxisTransformer);
        this.mXAxisRenderer = new XAxisRendererHorizontalBarChart(this.mViewPortHandler, this.mXAxis, this.mLeftAxisTransformer, this);
    }

    public void calculateOffsets() {
        float offsetLeft = 0.0f;
        float offsetRight = 0.0f;
        float offsetTop = 0.0f;
        float offsetBottom = 0.0f;
        if (this.mLegend != null && this.mLegend.isEnabled()) {
            if (this.mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART || this.mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART_CENTER) {
                offsetRight = 0.0f + Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + (this.mLegend.getXOffset() * 2.0f);
            } else if (this.mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART || this.mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART_CENTER) {
                offsetLeft = 0.0f + Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + (this.mLegend.getXOffset() * 2.0f);
            } else if (this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_LEFT || this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_RIGHT || this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_CENTER) {
                offsetBottom = 0.0f + Math.min(this.mLegend.mNeededHeight + this.mLegend.mTextHeightMax, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
            } else if (this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_LEFT || this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_RIGHT || this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_CENTER) {
                offsetTop = 0.0f + Math.min(this.mLegend.mNeededHeight + (this.mLegend.mTextHeightMax * 2.0f), this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
            }
        }
        if (this.mAxisLeft.needsOffset()) {
            offsetTop += this.mAxisLeft.getRequiredHeightSpace(this.mAxisRendererLeft.getPaintAxisLabels());
        }
        if (this.mAxisRight.needsOffset()) {
            offsetBottom += this.mAxisRight.getRequiredHeightSpace(this.mAxisRendererRight.getPaintAxisLabels());
        }
        float xlabelwidth = (float) this.mXAxis.mLabelRotatedWidth;
        if (this.mXAxis.isEnabled()) {
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
                offsetLeft += xlabelwidth;
            } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
                offsetRight += xlabelwidth;
            } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                offsetLeft += xlabelwidth;
                offsetRight += xlabelwidth;
            }
        }
        float offsetTop2 = offsetTop + getExtraTopOffset();
        float offsetRight2 = offsetRight + getExtraRightOffset();
        float offsetBottom2 = offsetBottom + getExtraBottomOffset();
        float offsetLeft2 = offsetLeft + getExtraLeftOffset();
        float minOffset = Utils.convertDpToPixel(this.mMinOffset);
        this.mViewPortHandler.restrainViewPort(Math.max(minOffset, offsetLeft2), Math.max(minOffset, offsetTop2), Math.max(minOffset, offsetRight2), Math.max(minOffset, offsetBottom2));
        if (this.mLogEnabled) {
            Log.i(Chart.LOG_TAG, "offsetLeft: " + offsetLeft2 + ", offsetTop: " + offsetTop2 + ", offsetRight: " + offsetRight2 + ", offsetBottom: " + offsetBottom2);
            Log.i(Chart.LOG_TAG, "Content: " + this.mViewPortHandler.getContentRect().toString());
        }
        prepareOffsetMatrix();
        prepareValuePxMatrix();
    }

    /* access modifiers changed from: protected */
    public void prepareValuePxMatrix() {
        this.mRightAxisTransformer.prepareMatrixValuePx(this.mAxisRight.mAxisMinimum, this.mAxisRight.mAxisRange, this.mXAxis.mAxisRange, this.mXAxis.mAxisMinimum);
        this.mLeftAxisTransformer.prepareMatrixValuePx(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisRange, this.mXAxis.mAxisRange, this.mXAxis.mAxisMinimum);
    }

    /* access modifiers changed from: protected */
    public void calcModulus() {
        float[] values = new float[9];
        this.mViewPortHandler.getMatrixTouch().getValues(values);
        this.mXAxis.mAxisLabelModulus = (int) Math.ceil((double) (((float) (((BarData) this.mData).getXValCount() * this.mXAxis.mLabelRotatedHeight)) / (this.mViewPortHandler.contentHeight() * values[4])));
        if (this.mXAxis.mAxisLabelModulus < 1) {
            this.mXAxis.mAxisLabelModulus = 1;
        }
    }

    public RectF getBarBounds(BarEntry e) {
        float left;
        float right = 0.0f;
        IBarDataSet set = (IBarDataSet) ((BarData) this.mData).getDataSetForEntry(e);
        if (set == null) {
            return null;
        }
        float barspace = set.getBarSpace();
        float y = e.getVal();
        float x = (float) e.getXIndex();
        float spaceHalf = barspace / 2.0f;
        float top = (x - 0.5f) + spaceHalf;
        float bottom = (x + 0.5f) - spaceHalf;
        if (y >= 0.0f) {
            left = y;
        } else {
            left = 0.0f;
        }
        if (y <= 0.0f) {
            right = y;
        }
        RectF bounds = new RectF(left, top, right, bottom);
        getTransformer(set.getAxisDependency()).rectValueToPixel(bounds);
        return bounds;
    }

    public PointF getPosition(Entry e, YAxis.AxisDependency axis) {
        if (e == null) {
            return null;
        }
        float[] vals = {e.getVal(), (float) e.getXIndex()};
        getTransformer(axis).pointValuesToPixel(vals);
        return new PointF(vals[0], vals[1]);
    }

    public Highlight getHighlightByTouchPoint(float x, float y) {
        if (this.mData != null) {
            return getHighlighter().getHighlight(y, x);
        }
        Log.e(Chart.LOG_TAG, "Can't select by touch. No data set.");
        return null;
    }

    public int getLowestVisibleXIndex() {
        float f;
        float step = (float) ((BarData) this.mData).getDataSetCount();
        float div = step <= 1.0f ? 1.0f : step + ((BarData) this.mData).getGroupSpace();
        float[] pts = {this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom()};
        getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        if (pts[1] <= 0.0f) {
            f = 0.0f;
        } else {
            f = pts[1] / div;
        }
        return (int) (f + 1.0f);
    }

    public int getHighestVisibleXIndex() {
        float f;
        float div = 1.0f;
        float step = (float) ((BarData) this.mData).getDataSetCount();
        if (step > 1.0f) {
            div = step + ((BarData) this.mData).getGroupSpace();
        }
        float[] pts = {this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop()};
        getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        if (pts[1] >= getXChartMax()) {
            f = getXChartMax() / div;
        } else {
            f = pts[1] / div;
        }
        return (int) f;
    }
}
