package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.buffer.HorizontalBarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class HorizontalBarChartRenderer extends BarChartRenderer {
    public HorizontalBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
        this.mValuePaint.setTextAlign(Paint.Align.LEFT);
    }

    public void initBuffers() {
        BarData barData = this.mChart.getBarData();
        this.mBarBuffers = new HorizontalBarBuffer[barData.getDataSetCount()];
        for (int i = 0; i < this.mBarBuffers.length; i++) {
            IBarDataSet set = (IBarDataSet) barData.getDataSetByIndex(i);
            this.mBarBuffers[i] = new HorizontalBarBuffer((set.isStacked() ? set.getStackSize() : 1) * set.getEntryCount() * 4, barData.getGroupSpace(), barData.getDataSetCount(), set.isStacked());
        }
    }

    /* access modifiers changed from: protected */
    public void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {
        Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        this.mShadowPaint.setColor(dataSet.getBarShadowColor());
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        BarBuffer buffer = this.mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setBarSpace(dataSet.getBarSpace());
        buffer.setDataSet(index);
        buffer.setInverted(this.mChart.isInverted(dataSet.getAxisDependency()));
        buffer.feed(dataSet);
        trans.pointValuesToPixel(buffer.buffer);
        int j = 0;
        while (j < buffer.size() && this.mViewPortHandler.isInBoundsTop(buffer.buffer[j + 3])) {
            if (this.mViewPortHandler.isInBoundsBottom(buffer.buffer[j + 1])) {
                if (this.mChart.isDrawBarShadowEnabled()) {
                    c.drawRect(this.mViewPortHandler.contentLeft(), buffer.buffer[j + 1], this.mViewPortHandler.contentRight(), buffer.buffer[j + 3], this.mShadowPaint);
                }
                this.mRenderPaint.setColor(dataSet.getColor(j / 4));
                c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2], buffer.buffer[j + 3], this.mRenderPaint);
            }
            j += 4;
        }
    }

    public void drawValues(Canvas c) {
        float negOffset;
        float f;
        float y;
        float negOffset2;
        float f2;
        float negOffset3;
        float f3;
        if (passesCheck()) {
            List<IBarDataSet> dataSets = this.mChart.getBarData().getDataSets();
            float valueOffsetPlus = Utils.convertDpToPixel(5.0f);
            boolean drawValueAboveBar = this.mChart.isDrawValueAboveBarEnabled();
            for (int i = 0; i < this.mChart.getBarData().getDataSetCount(); i++) {
                IBarDataSet dataSet = dataSets.get(i);
                if (dataSet.isDrawValuesEnabled() && dataSet.getEntryCount() != 0) {
                    boolean isInverted = this.mChart.isInverted(dataSet.getAxisDependency());
                    applyValueTextStyle(dataSet);
                    float halfTextHeight = ((float) Utils.calcTextHeight(this.mValuePaint, "10")) / 2.0f;
                    ValueFormatter formatter = dataSet.getValueFormatter();
                    Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                    float[] valuePoints = getTransformedValues(trans, dataSet, i);
                    if (!dataSet.isStacked()) {
                        int j = 0;
                        while (((float) j) < ((float) valuePoints.length) * this.mAnimator.getPhaseX() && this.mViewPortHandler.isInBoundsTop(valuePoints[j + 1])) {
                            if (this.mViewPortHandler.isInBoundsX(valuePoints[j]) && this.mViewPortHandler.isInBoundsBottom(valuePoints[j + 1])) {
                                BarEntry e = (BarEntry) dataSet.getEntryForIndex(j / 2);
                                float val = e.getVal();
                                String formattedValue = formatter.getFormattedValue(val, e, i, this.mViewPortHandler);
                                float valueTextWidth = (float) Utils.calcTextWidth(this.mValuePaint, formattedValue);
                                float posOffset = drawValueAboveBar ? valueOffsetPlus : -(valueTextWidth + valueOffsetPlus);
                                if (drawValueAboveBar) {
                                    negOffset3 = -(valueTextWidth + valueOffsetPlus);
                                } else {
                                    negOffset3 = valueOffsetPlus;
                                }
                                if (isInverted) {
                                    posOffset = (-posOffset) - valueTextWidth;
                                    negOffset3 = (-negOffset3) - valueTextWidth;
                                }
                                float f4 = valuePoints[j];
                                if (val >= 0.0f) {
                                    f3 = posOffset;
                                } else {
                                    f3 = negOffset3;
                                }
                                drawValue(c, formattedValue, f4 + f3, valuePoints[j + 1] + halfTextHeight, dataSet.getValueTextColor(j / 2));
                            }
                            j += 2;
                        }
                    } else {
                        for (int j2 = 0; ((float) j2) < ((float) (valuePoints.length - 1)) * this.mAnimator.getPhaseX(); j2 += 2) {
                            BarEntry e2 = (BarEntry) dataSet.getEntryForIndex(j2 / 2);
                            float[] vals = e2.getVals();
                            if (vals == null) {
                                if (!this.mViewPortHandler.isInBoundsTop(valuePoints[j2 + 1])) {
                                    break;
                                } else if (this.mViewPortHandler.isInBoundsX(valuePoints[j2]) && this.mViewPortHandler.isInBoundsBottom(valuePoints[j2 + 1])) {
                                    String formattedValue2 = formatter.getFormattedValue(e2.getVal(), e2, i, this.mViewPortHandler);
                                    float valueTextWidth2 = (float) Utils.calcTextWidth(this.mValuePaint, formattedValue2);
                                    float posOffset2 = drawValueAboveBar ? valueOffsetPlus : -(valueTextWidth2 + valueOffsetPlus);
                                    if (drawValueAboveBar) {
                                        negOffset2 = -(valueTextWidth2 + valueOffsetPlus);
                                    } else {
                                        negOffset2 = valueOffsetPlus;
                                    }
                                    if (isInverted) {
                                        posOffset2 = (-posOffset2) - valueTextWidth2;
                                        negOffset2 = (-negOffset2) - valueTextWidth2;
                                    }
                                    float f5 = valuePoints[j2];
                                    if (e2.getVal() >= 0.0f) {
                                        f2 = posOffset2;
                                    } else {
                                        f2 = negOffset2;
                                    }
                                    drawValue(c, formattedValue2, f5 + f2, valuePoints[j2 + 1] + halfTextHeight, dataSet.getValueTextColor(j2 / 2));
                                }
                            } else {
                                float[] transformed = new float[(vals.length * 2)];
                                float posY = 0.0f;
                                float negY = -e2.getNegativeSum();
                                int k = 0;
                                int idx = 0;
                                while (k < transformed.length) {
                                    float value = vals[idx];
                                    if (value >= 0.0f) {
                                        posY += value;
                                        y = posY;
                                    } else {
                                        y = negY;
                                        negY -= value;
                                    }
                                    transformed[k] = this.mAnimator.getPhaseY() * y;
                                    k += 2;
                                    idx++;
                                }
                                trans.pointValuesToPixel(transformed);
                                for (int k2 = 0; k2 < transformed.length; k2 += 2) {
                                    float val2 = vals[k2 / 2];
                                    String formattedValue3 = formatter.getFormattedValue(val2, e2, i, this.mViewPortHandler);
                                    float valueTextWidth3 = (float) Utils.calcTextWidth(this.mValuePaint, formattedValue3);
                                    float posOffset3 = drawValueAboveBar ? valueOffsetPlus : -(valueTextWidth3 + valueOffsetPlus);
                                    if (drawValueAboveBar) {
                                        negOffset = -(valueTextWidth3 + valueOffsetPlus);
                                    } else {
                                        negOffset = valueOffsetPlus;
                                    }
                                    if (isInverted) {
                                        posOffset3 = (-posOffset3) - valueTextWidth3;
                                        negOffset = (-negOffset) - valueTextWidth3;
                                    }
                                    float f6 = transformed[k2];
                                    if (val2 >= 0.0f) {
                                        f = posOffset3;
                                    } else {
                                        f = negOffset;
                                    }
                                    float x = f6 + f;
                                    float y2 = valuePoints[j2 + 1];
                                    if (!this.mViewPortHandler.isInBoundsTop(y2)) {
                                        break;
                                    }
                                    if (this.mViewPortHandler.isInBoundsX(x) && this.mViewPortHandler.isInBoundsBottom(y2)) {
                                        drawValue(c, formattedValue3, x, y2 + halfTextHeight, dataSet.getValueTextColor(j2 / 2));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        this.mValuePaint.setColor(color);
        c.drawText(valueText, x, y, this.mValuePaint);
    }

    /* access modifiers changed from: protected */
    public void prepareBarHighlight(float x, float y1, float y2, float barspaceHalf, Transformer trans) {
        RectF rectF = this.mBarRect;
        rectF.set(y1, (x - 0.5f) + barspaceHalf, y2, (x + 0.5f) - barspaceHalf);
        trans.rectValueToPixelHorizontal(this.mBarRect, this.mAnimator.getPhaseY());
    }

    public float[] getTransformedValues(Transformer trans, IBarDataSet data, int dataSetIndex) {
        return trans.generateTransformedValuesHorizontalBarChart(data, dataSetIndex, this.mChart.getBarData(), this.mAnimator.getPhaseY());
    }

    /* access modifiers changed from: protected */
    public boolean passesCheck() {
        return ((float) this.mChart.getBarData().getYValCount()) < ((float) this.mChart.getMaxVisibleCount()) * this.mViewPortHandler.getScaleY();
    }
}
