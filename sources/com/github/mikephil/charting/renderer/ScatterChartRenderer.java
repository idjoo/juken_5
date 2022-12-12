package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.ScatterBuffer;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class ScatterChartRenderer extends LineScatterCandleRadarRenderer {
    protected ScatterDataProvider mChart;
    protected ScatterBuffer[] mScatterBuffers;

    public ScatterChartRenderer(ScatterDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;
    }

    public void initBuffers() {
        ScatterData scatterData = this.mChart.getScatterData();
        this.mScatterBuffers = new ScatterBuffer[scatterData.getDataSetCount()];
        for (int i = 0; i < this.mScatterBuffers.length; i++) {
            this.mScatterBuffers[i] = new ScatterBuffer(((IScatterDataSet) scatterData.getDataSetByIndex(i)).getEntryCount() * 2);
        }
    }

    public void drawData(Canvas c) {
        for (IScatterDataSet set : this.mChart.getScatterData().getDataSets()) {
            if (set.isVisible()) {
                drawDataSet(c, set);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawDataSet(Canvas c, IScatterDataSet dataSet) {
        Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float shapeSize = Utils.convertDpToPixel(dataSet.getScatterShapeSize());
        float shapeHalf = shapeSize / 2.0f;
        float shapeHoleSizeHalf = Utils.convertDpToPixel(dataSet.getScatterShapeHoleRadius());
        float shapeHoleSize = shapeHoleSizeHalf * 2.0f;
        int shapeHoleColor = dataSet.getScatterShapeHoleColor();
        float shapeStrokeSize = (shapeSize - shapeHoleSize) / 2.0f;
        float shapeStrokeSizeHalf = shapeStrokeSize / 2.0f;
        ScatterChart.ScatterShape shape = dataSet.getScatterShape();
        ScatterBuffer buffer = this.mScatterBuffers[this.mChart.getScatterData().getIndexOfDataSet(dataSet)];
        buffer.setPhases(phaseX, phaseY);
        buffer.feed(dataSet);
        trans.pointValuesToPixel(buffer.buffer);
        switch (shape) {
            case SQUARE:
                int i = 0;
                while (i < buffer.size() && this.mViewPortHandler.isInBoundsRight(buffer.buffer[i])) {
                    if (this.mViewPortHandler.isInBoundsLeft(buffer.buffer[i]) && this.mViewPortHandler.isInBoundsY(buffer.buffer[i + 1])) {
                        this.mRenderPaint.setColor(dataSet.getColor(i / 2));
                        if (((double) shapeHoleSize) > 0.0d) {
                            this.mRenderPaint.setStyle(Paint.Style.STROKE);
                            this.mRenderPaint.setStrokeWidth(shapeStrokeSize);
                            c.drawRect((buffer.buffer[i] - shapeHoleSizeHalf) - shapeStrokeSizeHalf, (buffer.buffer[i + 1] - shapeHoleSizeHalf) - shapeStrokeSizeHalf, buffer.buffer[i] + shapeHoleSizeHalf + shapeStrokeSizeHalf, buffer.buffer[i + 1] + shapeHoleSizeHalf + shapeStrokeSizeHalf, this.mRenderPaint);
                            if (shapeHoleColor != 1122867) {
                                this.mRenderPaint.setStyle(Paint.Style.FILL);
                                this.mRenderPaint.setColor(shapeHoleColor);
                                c.drawRect(buffer.buffer[i] - shapeHoleSizeHalf, buffer.buffer[i + 1] - shapeHoleSizeHalf, buffer.buffer[i] + shapeHoleSizeHalf, buffer.buffer[i + 1] + shapeHoleSizeHalf, this.mRenderPaint);
                            }
                        } else {
                            this.mRenderPaint.setStyle(Paint.Style.FILL);
                            c.drawRect(buffer.buffer[i] - shapeHalf, buffer.buffer[i + 1] - shapeHalf, buffer.buffer[i] + shapeHalf, buffer.buffer[i + 1] + shapeHalf, this.mRenderPaint);
                        }
                    }
                    i += 2;
                }
                return;
            case CIRCLE:
                int i2 = 0;
                while (i2 < buffer.size() && this.mViewPortHandler.isInBoundsRight(buffer.buffer[i2])) {
                    if (this.mViewPortHandler.isInBoundsLeft(buffer.buffer[i2]) && this.mViewPortHandler.isInBoundsY(buffer.buffer[i2 + 1])) {
                        this.mRenderPaint.setColor(dataSet.getColor(i2 / 2));
                        if (((double) shapeHoleSize) > 0.0d) {
                            this.mRenderPaint.setStyle(Paint.Style.STROKE);
                            this.mRenderPaint.setStrokeWidth(shapeStrokeSize);
                            Canvas canvas = c;
                            canvas.drawCircle(buffer.buffer[i2], buffer.buffer[i2 + 1], shapeHoleSizeHalf + shapeStrokeSizeHalf, this.mRenderPaint);
                            if (shapeHoleColor != 1122867) {
                                this.mRenderPaint.setStyle(Paint.Style.FILL);
                                this.mRenderPaint.setColor(shapeHoleColor);
                                c.drawCircle(buffer.buffer[i2], buffer.buffer[i2 + 1], shapeHoleSizeHalf, this.mRenderPaint);
                            }
                        } else {
                            this.mRenderPaint.setStyle(Paint.Style.FILL);
                            c.drawCircle(buffer.buffer[i2], buffer.buffer[i2 + 1], shapeHalf, this.mRenderPaint);
                        }
                    }
                    i2 += 2;
                }
                return;
            case TRIANGLE:
                this.mRenderPaint.setStyle(Paint.Style.FILL);
                Path tri = new Path();
                int i3 = 0;
                while (i3 < buffer.size() && this.mViewPortHandler.isInBoundsRight(buffer.buffer[i3])) {
                    if (this.mViewPortHandler.isInBoundsLeft(buffer.buffer[i3]) && this.mViewPortHandler.isInBoundsY(buffer.buffer[i3 + 1])) {
                        this.mRenderPaint.setColor(dataSet.getColor(i3 / 2));
                        tri.moveTo(buffer.buffer[i3], buffer.buffer[i3 + 1] - shapeHalf);
                        tri.lineTo(buffer.buffer[i3] + shapeHalf, buffer.buffer[i3 + 1] + shapeHalf);
                        tri.lineTo(buffer.buffer[i3] - shapeHalf, buffer.buffer[i3 + 1] + shapeHalf);
                        if (((double) shapeHoleSize) > 0.0d) {
                            tri.lineTo(buffer.buffer[i3], buffer.buffer[i3 + 1] - shapeHalf);
                            tri.moveTo((buffer.buffer[i3] - shapeHalf) + shapeStrokeSize, (buffer.buffer[i3 + 1] + shapeHalf) - shapeStrokeSize);
                            tri.lineTo((buffer.buffer[i3] + shapeHalf) - shapeStrokeSize, (buffer.buffer[i3 + 1] + shapeHalf) - shapeStrokeSize);
                            tri.lineTo(buffer.buffer[i3], (buffer.buffer[i3 + 1] - shapeHalf) + shapeStrokeSize);
                            tri.lineTo((buffer.buffer[i3] - shapeHalf) + shapeStrokeSize, (buffer.buffer[i3 + 1] + shapeHalf) - shapeStrokeSize);
                        }
                        tri.close();
                        c.drawPath(tri, this.mRenderPaint);
                        tri.reset();
                        if (((double) shapeHoleSize) > 0.0d && shapeHoleColor != 1122867) {
                            this.mRenderPaint.setColor(shapeHoleColor);
                            tri.moveTo(buffer.buffer[i3], (buffer.buffer[i3 + 1] - shapeHalf) + shapeStrokeSize);
                            tri.lineTo((buffer.buffer[i3] + shapeHalf) - shapeStrokeSize, (buffer.buffer[i3 + 1] + shapeHalf) - shapeStrokeSize);
                            tri.lineTo((buffer.buffer[i3] - shapeHalf) + shapeStrokeSize, (buffer.buffer[i3 + 1] + shapeHalf) - shapeStrokeSize);
                            tri.close();
                            c.drawPath(tri, this.mRenderPaint);
                            tri.reset();
                        }
                    }
                    i3 += 2;
                }
                return;
            case CROSS:
                this.mRenderPaint.setStyle(Paint.Style.STROKE);
                this.mRenderPaint.setStrokeWidth(Utils.convertDpToPixel(1.0f));
                int i4 = 0;
                while (i4 < buffer.size() && this.mViewPortHandler.isInBoundsRight(buffer.buffer[i4])) {
                    if (this.mViewPortHandler.isInBoundsLeft(buffer.buffer[i4]) && this.mViewPortHandler.isInBoundsY(buffer.buffer[i4 + 1])) {
                        this.mRenderPaint.setColor(dataSet.getColor(i4 / 2));
                        c.drawLine(buffer.buffer[i4] - shapeHalf, buffer.buffer[i4 + 1], buffer.buffer[i4] + shapeHalf, buffer.buffer[i4 + 1], this.mRenderPaint);
                        c.drawLine(buffer.buffer[i4], buffer.buffer[i4 + 1] - shapeHalf, buffer.buffer[i4], buffer.buffer[i4 + 1] + shapeHalf, this.mRenderPaint);
                    }
                    i4 += 2;
                }
                return;
            case X:
                this.mRenderPaint.setStyle(Paint.Style.STROKE);
                this.mRenderPaint.setStrokeWidth(Utils.convertDpToPixel(1.0f));
                int i5 = 0;
                while (i5 < buffer.size() && this.mViewPortHandler.isInBoundsRight(buffer.buffer[i5])) {
                    if (this.mViewPortHandler.isInBoundsLeft(buffer.buffer[i5]) && this.mViewPortHandler.isInBoundsY(buffer.buffer[i5 + 1])) {
                        this.mRenderPaint.setColor(dataSet.getColor(i5 / 2));
                        c.drawLine(buffer.buffer[i5] - shapeHalf, buffer.buffer[i5 + 1] - shapeHalf, buffer.buffer[i5] + shapeHalf, buffer.buffer[i5 + 1] + shapeHalf, this.mRenderPaint);
                        c.drawLine(buffer.buffer[i5] + shapeHalf, buffer.buffer[i5 + 1] - shapeHalf, buffer.buffer[i5] - shapeHalf, buffer.buffer[i5 + 1] + shapeHalf, this.mRenderPaint);
                    }
                    i5 += 2;
                }
                return;
            default:
                return;
        }
    }

    public void drawValues(Canvas c) {
        if (((float) this.mChart.getScatterData().getYValCount()) < ((float) this.mChart.getMaxVisibleCount()) * this.mViewPortHandler.getScaleX()) {
            List<IScatterDataSet> dataSets = this.mChart.getScatterData().getDataSets();
            for (int i = 0; i < this.mChart.getScatterData().getDataSetCount(); i++) {
                IScatterDataSet dataSet = dataSets.get(i);
                if (dataSet.isDrawValuesEnabled() && dataSet.getEntryCount() != 0) {
                    applyValueTextStyle(dataSet);
                    float[] positions = this.mChart.getTransformer(dataSet.getAxisDependency()).generateTransformedValuesScatter(dataSet, this.mAnimator.getPhaseY());
                    float shapeSize = Utils.convertDpToPixel(dataSet.getScatterShapeSize());
                    int j = 0;
                    while (((float) j) < ((float) positions.length) * this.mAnimator.getPhaseX() && this.mViewPortHandler.isInBoundsRight(positions[j])) {
                        if (this.mViewPortHandler.isInBoundsLeft(positions[j]) && this.mViewPortHandler.isInBoundsY(positions[j + 1])) {
                            Entry entry = dataSet.getEntryForIndex(j / 2);
                            drawValue(c, dataSet.getValueFormatter(), entry.getVal(), entry, i, positions[j], positions[j + 1] - shapeSize, dataSet.getValueTextColor(j / 2));
                        }
                        j += 2;
                    }
                }
            }
        }
    }

    public void drawExtras(Canvas c) {
    }

    public void drawHighlighted(Canvas c, Highlight[] indices) {
        for (int i = 0; i < indices.length; i++) {
            IScatterDataSet set = (IScatterDataSet) this.mChart.getScatterData().getDataSetByIndex(indices[i].getDataSetIndex());
            if (set != null && set.isHighlightEnabled()) {
                int xIndex = indices[i].getXIndex();
                if (((float) xIndex) <= this.mChart.getXChartMax() * this.mAnimator.getPhaseX()) {
                    float yVal = set.getYValForXIndex(xIndex);
                    if (yVal != Float.NaN) {
                        float[] pts = {(float) xIndex, yVal * this.mAnimator.getPhaseY()};
                        this.mChart.getTransformer(set.getAxisDependency()).pointValuesToPixel(pts);
                        drawHighlightLines(c, pts, set);
                    }
                }
            }
        }
    }
}
