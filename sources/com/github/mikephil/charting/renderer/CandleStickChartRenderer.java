package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class CandleStickChartRenderer extends LineScatterCandleRadarRenderer {
    private float[] mBodyBuffers = new float[4];
    protected CandleDataProvider mChart;
    private float[] mCloseBuffers = new float[4];
    private float[] mOpenBuffers = new float[4];
    private float[] mRangeBuffers = new float[4];
    private float[] mShadowBuffers = new float[8];

    public CandleStickChartRenderer(CandleDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;
    }

    public void initBuffers() {
    }

    public void drawData(Canvas c) {
        for (ICandleDataSet set : this.mChart.getCandleData().getDataSets()) {
            if (set.isVisible() && set.getEntryCount() > 0) {
                drawDataSet(c, set);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawDataSet(Canvas c, ICandleDataSet dataSet) {
        int barColor;
        int decreasingColor;
        Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float barSpace = dataSet.getBarSpace();
        boolean showCandleBar = dataSet.getShowCandleBar();
        int minx = Math.max(this.mMinX, 0);
        int maxx = Math.min(this.mMaxX + 1, dataSet.getEntryCount());
        this.mRenderPaint.setStrokeWidth(dataSet.getShadowWidth());
        int count = (int) Math.ceil((double) ((((float) (maxx - minx)) * phaseX) + ((float) minx)));
        for (int j = minx; j < count; j++) {
            CandleEntry e = (CandleEntry) dataSet.getEntryForIndex(j);
            int xIndex = e.getXIndex();
            if (xIndex >= minx && xIndex < maxx) {
                float open = e.getOpen();
                float close = e.getClose();
                float high = e.getHigh();
                float low = e.getLow();
                if (showCandleBar) {
                    this.mShadowBuffers[0] = (float) xIndex;
                    this.mShadowBuffers[2] = (float) xIndex;
                    this.mShadowBuffers[4] = (float) xIndex;
                    this.mShadowBuffers[6] = (float) xIndex;
                    if (open > close) {
                        this.mShadowBuffers[1] = high * phaseY;
                        this.mShadowBuffers[3] = open * phaseY;
                        this.mShadowBuffers[5] = low * phaseY;
                        this.mShadowBuffers[7] = close * phaseY;
                    } else if (open < close) {
                        this.mShadowBuffers[1] = high * phaseY;
                        this.mShadowBuffers[3] = close * phaseY;
                        this.mShadowBuffers[5] = low * phaseY;
                        this.mShadowBuffers[7] = open * phaseY;
                    } else {
                        this.mShadowBuffers[1] = high * phaseY;
                        this.mShadowBuffers[3] = open * phaseY;
                        this.mShadowBuffers[5] = low * phaseY;
                        this.mShadowBuffers[7] = this.mShadowBuffers[3];
                    }
                    trans.pointValuesToPixel(this.mShadowBuffers);
                    if (!dataSet.getShadowColorSameAsCandle()) {
                        this.mRenderPaint.setColor(dataSet.getShadowColor() == 1122867 ? dataSet.getColor(j) : dataSet.getShadowColor());
                    } else if (open > close) {
                        Paint paint = this.mRenderPaint;
                        if (dataSet.getDecreasingColor() == 1122867) {
                            decreasingColor = dataSet.getColor(j);
                        } else {
                            decreasingColor = dataSet.getDecreasingColor();
                        }
                        paint.setColor(decreasingColor);
                    } else if (open < close) {
                        this.mRenderPaint.setColor(dataSet.getIncreasingColor() == 1122867 ? dataSet.getColor(j) : dataSet.getIncreasingColor());
                    } else {
                        this.mRenderPaint.setColor(dataSet.getNeutralColor() == 1122867 ? dataSet.getColor(j) : dataSet.getNeutralColor());
                    }
                    this.mRenderPaint.setStyle(Paint.Style.STROKE);
                    c.drawLines(this.mShadowBuffers, this.mRenderPaint);
                    this.mBodyBuffers[0] = (((float) xIndex) - 0.5f) + barSpace;
                    this.mBodyBuffers[1] = close * phaseY;
                    this.mBodyBuffers[2] = (((float) xIndex) + 0.5f) - barSpace;
                    this.mBodyBuffers[3] = open * phaseY;
                    trans.pointValuesToPixel(this.mBodyBuffers);
                    if (open > close) {
                        if (dataSet.getDecreasingColor() == 1122867) {
                            this.mRenderPaint.setColor(dataSet.getColor(j));
                        } else {
                            this.mRenderPaint.setColor(dataSet.getDecreasingColor());
                        }
                        this.mRenderPaint.setStyle(dataSet.getDecreasingPaintStyle());
                        c.drawRect(this.mBodyBuffers[0], this.mBodyBuffers[3], this.mBodyBuffers[2], this.mBodyBuffers[1], this.mRenderPaint);
                    } else if (open < close) {
                        if (dataSet.getIncreasingColor() == 1122867) {
                            this.mRenderPaint.setColor(dataSet.getColor(j));
                        } else {
                            this.mRenderPaint.setColor(dataSet.getIncreasingColor());
                        }
                        this.mRenderPaint.setStyle(dataSet.getIncreasingPaintStyle());
                        c.drawRect(this.mBodyBuffers[0], this.mBodyBuffers[1], this.mBodyBuffers[2], this.mBodyBuffers[3], this.mRenderPaint);
                    } else {
                        if (dataSet.getNeutralColor() == 1122867) {
                            this.mRenderPaint.setColor(dataSet.getColor(j));
                        } else {
                            this.mRenderPaint.setColor(dataSet.getNeutralColor());
                        }
                        c.drawLine(this.mBodyBuffers[0], this.mBodyBuffers[1], this.mBodyBuffers[2], this.mBodyBuffers[3], this.mRenderPaint);
                    }
                } else {
                    this.mRangeBuffers[0] = (float) xIndex;
                    this.mRangeBuffers[1] = high * phaseY;
                    this.mRangeBuffers[2] = (float) xIndex;
                    this.mRangeBuffers[3] = low * phaseY;
                    this.mOpenBuffers[0] = (((float) xIndex) - 0.5f) + barSpace;
                    this.mOpenBuffers[1] = open * phaseY;
                    this.mOpenBuffers[2] = (float) xIndex;
                    this.mOpenBuffers[3] = open * phaseY;
                    this.mCloseBuffers[0] = (((float) xIndex) + 0.5f) - barSpace;
                    this.mCloseBuffers[1] = close * phaseY;
                    this.mCloseBuffers[2] = (float) xIndex;
                    this.mCloseBuffers[3] = close * phaseY;
                    trans.pointValuesToPixel(this.mRangeBuffers);
                    trans.pointValuesToPixel(this.mOpenBuffers);
                    trans.pointValuesToPixel(this.mCloseBuffers);
                    if (open > close) {
                        barColor = dataSet.getDecreasingColor() == 1122867 ? dataSet.getColor(j) : dataSet.getDecreasingColor();
                    } else if (open < close) {
                        barColor = dataSet.getIncreasingColor() == 1122867 ? dataSet.getColor(j) : dataSet.getIncreasingColor();
                    } else {
                        barColor = dataSet.getNeutralColor() == 1122867 ? dataSet.getColor(j) : dataSet.getNeutralColor();
                    }
                    this.mRenderPaint.setColor(barColor);
                    c.drawLine(this.mRangeBuffers[0], this.mRangeBuffers[1], this.mRangeBuffers[2], this.mRangeBuffers[3], this.mRenderPaint);
                    c.drawLine(this.mOpenBuffers[0], this.mOpenBuffers[1], this.mOpenBuffers[2], this.mOpenBuffers[3], this.mRenderPaint);
                    c.drawLine(this.mCloseBuffers[0], this.mCloseBuffers[1], this.mCloseBuffers[2], this.mCloseBuffers[3], this.mRenderPaint);
                }
            }
        }
    }

    public void drawValues(Canvas c) {
        if (((float) this.mChart.getCandleData().getYValCount()) < ((float) this.mChart.getMaxVisibleCount()) * this.mViewPortHandler.getScaleX()) {
            List<ICandleDataSet> dataSets = this.mChart.getCandleData().getDataSets();
            for (int i = 0; i < dataSets.size(); i++) {
                ICandleDataSet dataSet = dataSets.get(i);
                if (dataSet.isDrawValuesEnabled() && dataSet.getEntryCount() != 0) {
                    applyValueTextStyle(dataSet);
                    Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                    int minx = Math.max(this.mMinX, 0);
                    float[] positions = trans.generateTransformedValuesCandle(dataSet, this.mAnimator.getPhaseX(), this.mAnimator.getPhaseY(), minx, Math.min(this.mMaxX + 1, dataSet.getEntryCount()));
                    float yOffset = Utils.convertDpToPixel(5.0f);
                    for (int j = 0; j < positions.length; j += 2) {
                        float x = positions[j];
                        float y = positions[j + 1];
                        if (!this.mViewPortHandler.isInBoundsRight(x)) {
                            break;
                        }
                        if (this.mViewPortHandler.isInBoundsLeft(x) && this.mViewPortHandler.isInBoundsY(y)) {
                            CandleEntry entry = (CandleEntry) dataSet.getEntryForIndex((j / 2) + minx);
                            drawValue(c, dataSet.getValueFormatter(), entry.getHigh(), entry, i, x, y - yOffset, dataSet.getValueTextColor(j / 2));
                        }
                    }
                }
            }
        }
    }

    public void drawExtras(Canvas c) {
    }

    public void drawHighlighted(Canvas c, Highlight[] indices) {
        CandleEntry e;
        for (int i = 0; i < indices.length; i++) {
            int xIndex = indices[i].getXIndex();
            ICandleDataSet set = (ICandleDataSet) this.mChart.getCandleData().getDataSetByIndex(indices[i].getDataSetIndex());
            if (set != null && set.isHighlightEnabled() && (e = (CandleEntry) set.getEntryForXIndex(xIndex)) != null && e.getXIndex() == xIndex) {
                float y = ((e.getLow() * this.mAnimator.getPhaseY()) + (e.getHigh() * this.mAnimator.getPhaseY())) / 2.0f;
                float yChartMin = this.mChart.getYChartMin();
                float yChartMax = this.mChart.getYChartMax();
                float[] pts = {(float) xIndex, y};
                this.mChart.getTransformer(set.getAxisDependency()).pointValuesToPixel(pts);
                drawHighlightLines(c, pts, set);
            }
        }
    }
}
