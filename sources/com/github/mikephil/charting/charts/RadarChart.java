package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.renderer.RadarChartRenderer;
import com.github.mikephil.charting.renderer.XAxisRendererRadarChart;
import com.github.mikephil.charting.renderer.YAxisRendererRadarChart;
import com.github.mikephil.charting.utils.Utils;

public class RadarChart extends PieRadarChartBase<RadarData> {
    private boolean mDrawWeb = true;
    private float mInnerWebLineWidth = 1.5f;
    private int mSkipWebLineCount = 0;
    private int mWebAlpha = 150;
    private int mWebColor = Color.rgb(122, 122, 122);
    private int mWebColorInner = Color.rgb(122, 122, 122);
    private float mWebLineWidth = 2.5f;
    protected XAxisRendererRadarChart mXAxisRenderer;
    private YAxis mYAxis;
    protected YAxisRendererRadarChart mYAxisRenderer;

    public RadarChart(Context context) {
        super(context);
    }

    public RadarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* access modifiers changed from: protected */
    public void init() {
        super.init();
        this.mYAxis = new YAxis(YAxis.AxisDependency.LEFT);
        this.mXAxis.setSpaceBetweenLabels(0);
        this.mWebLineWidth = Utils.convertDpToPixel(1.5f);
        this.mInnerWebLineWidth = Utils.convertDpToPixel(0.75f);
        this.mRenderer = new RadarChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.mYAxisRenderer = new YAxisRendererRadarChart(this.mViewPortHandler, this.mYAxis, this);
        this.mXAxisRenderer = new XAxisRendererRadarChart(this.mViewPortHandler, this.mXAxis, this);
    }

    /* access modifiers changed from: protected */
    public void calcMinMax() {
        super.calcMinMax();
        this.mXAxis.mAxisMaximum = (float) (((RadarData) this.mData).getXVals().size() - 1);
        this.mXAxis.mAxisRange = Math.abs(this.mXAxis.mAxisMaximum - this.mXAxis.mAxisMinimum);
        this.mYAxis.calcMinMax(((RadarData) this.mData).getYMin(YAxis.AxisDependency.LEFT), ((RadarData) this.mData).getYMax(YAxis.AxisDependency.LEFT));
    }

    /* access modifiers changed from: protected */
    public float[] getMarkerPosition(Entry e, Highlight highlight) {
        float angle = (getSliceAngle() * ((float) e.getXIndex())) + getRotationAngle();
        float val = e.getVal() * getFactor();
        PointF c = getCenterOffsets();
        PointF p = new PointF((float) (((double) c.x) + (((double) val) * Math.cos(Math.toRadians((double) angle)))), (float) (((double) c.y) + (((double) val) * Math.sin(Math.toRadians((double) angle)))));
        return new float[]{p.x, p.y};
    }

    public void notifyDataSetChanged() {
        if (this.mData != null) {
            calcMinMax();
            this.mYAxisRenderer.computeAxis(this.mYAxis.mAxisMinimum, this.mYAxis.mAxisMaximum);
            this.mXAxisRenderer.computeAxis(((RadarData) this.mData).getXValMaximumLength(), ((RadarData) this.mData).getXVals());
            if (this.mLegend != null && !this.mLegend.isLegendCustom()) {
                this.mLegendRenderer.computeLegend(this.mData);
            }
            calculateOffsets();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mData != null) {
            this.mXAxisRenderer.renderAxisLabels(canvas);
            if (this.mDrawWeb) {
                this.mRenderer.drawExtras(canvas);
            }
            this.mYAxisRenderer.renderLimitLines(canvas);
            this.mRenderer.drawData(canvas);
            if (valuesToHighlight()) {
                this.mRenderer.drawHighlighted(canvas, this.mIndicesToHighlight);
            }
            this.mYAxisRenderer.renderAxisLabels(canvas);
            this.mRenderer.drawValues(canvas);
            this.mLegendRenderer.renderLegend(canvas);
            drawDescription(canvas);
            drawMarkers(canvas);
        }
    }

    public float getFactor() {
        RectF content = this.mViewPortHandler.getContentRect();
        return Math.min(content.width() / 2.0f, content.height() / 2.0f) / this.mYAxis.mAxisRange;
    }

    public float getSliceAngle() {
        return 360.0f / ((float) ((RadarData) this.mData).getXValCount());
    }

    public int getIndexForAngle(float angle) {
        float a = Utils.getNormalizedAngle(angle - getRotationAngle());
        float sliceangle = getSliceAngle();
        for (int i = 0; i < ((RadarData) this.mData).getXValCount(); i++) {
            if ((((float) (i + 1)) * sliceangle) - (sliceangle / 2.0f) > a) {
                return i;
            }
        }
        return 0;
    }

    public YAxis getYAxis() {
        return this.mYAxis;
    }

    public void setWebLineWidth(float width) {
        this.mWebLineWidth = Utils.convertDpToPixel(width);
    }

    public float getWebLineWidth() {
        return this.mWebLineWidth;
    }

    public void setWebLineWidthInner(float width) {
        this.mInnerWebLineWidth = Utils.convertDpToPixel(width);
    }

    public float getWebLineWidthInner() {
        return this.mInnerWebLineWidth;
    }

    public void setWebAlpha(int alpha) {
        this.mWebAlpha = alpha;
    }

    public int getWebAlpha() {
        return this.mWebAlpha;
    }

    public void setWebColor(int color) {
        this.mWebColor = color;
    }

    public int getWebColor() {
        return this.mWebColor;
    }

    public void setWebColorInner(int color) {
        this.mWebColorInner = color;
    }

    public int getWebColorInner() {
        return this.mWebColorInner;
    }

    public void setDrawWeb(boolean enabled) {
        this.mDrawWeb = enabled;
    }

    public void setSkipWebLineCount(int count) {
        this.mSkipWebLineCount = Math.max(0, count);
    }

    public int getSkipWebLineCount() {
        return this.mSkipWebLineCount;
    }

    /* access modifiers changed from: protected */
    public float getRequiredLegendOffset() {
        return this.mLegendRenderer.getLabelPaint().getTextSize() * 4.0f;
    }

    /* access modifiers changed from: protected */
    public float getRequiredBaseOffset() {
        return (!this.mXAxis.isEnabled() || !this.mXAxis.isDrawLabelsEnabled()) ? Utils.convertDpToPixel(10.0f) : (float) this.mXAxis.mLabelRotatedWidth;
    }

    public float getRadius() {
        RectF content = this.mViewPortHandler.getContentRect();
        return Math.min(content.width() / 2.0f, content.height() / 2.0f);
    }

    public float getYChartMax() {
        return this.mYAxis.mAxisMaximum;
    }

    public float getYChartMin() {
        return this.mYAxis.mAxisMinimum;
    }

    public float getYRange() {
        return this.mYAxis.mAxisRange;
    }
}
