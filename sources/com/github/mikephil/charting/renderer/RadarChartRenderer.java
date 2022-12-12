package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class RadarChartRenderer extends LineRadarRenderer {
    protected RadarChart mChart;
    protected Paint mHighlightCirclePaint;
    protected Paint mWebPaint = new Paint(1);

    public RadarChartRenderer(RadarChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;
        this.mHighlightPaint = new Paint(1);
        this.mHighlightPaint.setStyle(Paint.Style.STROKE);
        this.mHighlightPaint.setStrokeWidth(2.0f);
        this.mHighlightPaint.setColor(Color.rgb(255, 187, 115));
        this.mWebPaint.setStyle(Paint.Style.STROKE);
        this.mHighlightCirclePaint = new Paint(1);
    }

    public Paint getWebPaint() {
        return this.mWebPaint;
    }

    public void initBuffers() {
    }

    public void drawData(Canvas c) {
        RadarData radarData = (RadarData) this.mChart.getData();
        int mostEntries = 0;
        for (IRadarDataSet set : radarData.getDataSets()) {
            if (set.getEntryCount() > mostEntries) {
                mostEntries = set.getEntryCount();
            }
        }
        for (IRadarDataSet set2 : radarData.getDataSets()) {
            if (set2.isVisible() && set2.getEntryCount() > 0) {
                drawDataSet(c, set2, mostEntries);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawDataSet(Canvas c, IRadarDataSet dataSet, int mostEntries) {
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float sliceangle = this.mChart.getSliceAngle();
        float factor = this.mChart.getFactor();
        PointF center = this.mChart.getCenterOffsets();
        Path surface = new Path();
        boolean hasMovedToPoint = false;
        for (int j = 0; j < dataSet.getEntryCount(); j++) {
            this.mRenderPaint.setColor(dataSet.getColor(j));
            PointF p = Utils.getPosition(center, (dataSet.getEntryForIndex(j).getVal() - this.mChart.getYChartMin()) * factor * phaseY, (((float) j) * sliceangle * phaseX) + this.mChart.getRotationAngle());
            if (!Float.isNaN(p.x)) {
                if (!hasMovedToPoint) {
                    surface.moveTo(p.x, p.y);
                    hasMovedToPoint = true;
                } else {
                    surface.lineTo(p.x, p.y);
                }
            }
        }
        if (dataSet.getEntryCount() > mostEntries) {
            surface.lineTo(center.x, center.y);
        }
        surface.close();
        if (dataSet.isDrawFilledEnabled()) {
            Drawable drawable = dataSet.getFillDrawable();
            if (drawable != null) {
                drawFilledPath(c, surface, drawable);
            } else {
                drawFilledPath(c, surface, dataSet.getFillColor(), dataSet.getFillAlpha());
            }
        }
        this.mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
        this.mRenderPaint.setStyle(Paint.Style.STROKE);
        if (!dataSet.isDrawFilledEnabled() || dataSet.getFillAlpha() < 255) {
            c.drawPath(surface, this.mRenderPaint);
        }
    }

    public void drawValues(Canvas c) {
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float sliceangle = this.mChart.getSliceAngle();
        float factor = this.mChart.getFactor();
        PointF center = this.mChart.getCenterOffsets();
        float yoffset = Utils.convertDpToPixel(5.0f);
        for (int i = 0; i < ((RadarData) this.mChart.getData()).getDataSetCount(); i++) {
            IRadarDataSet dataSet = (IRadarDataSet) ((RadarData) this.mChart.getData()).getDataSetByIndex(i);
            if (dataSet.isDrawValuesEnabled() && dataSet.getEntryCount() != 0) {
                applyValueTextStyle(dataSet);
                for (int j = 0; j < dataSet.getEntryCount(); j++) {
                    Entry entry = dataSet.getEntryForIndex(j);
                    PointF p = Utils.getPosition(center, (entry.getVal() - this.mChart.getYChartMin()) * factor * phaseY, (((float) j) * sliceangle * phaseX) + this.mChart.getRotationAngle());
                    drawValue(c, dataSet.getValueFormatter(), entry.getVal(), entry, i, p.x, p.y - yoffset, dataSet.getValueTextColor(j));
                }
            }
        }
    }

    public void drawExtras(Canvas c) {
        drawWeb(c);
    }

    /* access modifiers changed from: protected */
    public void drawWeb(Canvas c) {
        float sliceangle = this.mChart.getSliceAngle();
        float factor = this.mChart.getFactor();
        float rotationangle = this.mChart.getRotationAngle();
        PointF center = this.mChart.getCenterOffsets();
        this.mWebPaint.setStrokeWidth(this.mChart.getWebLineWidth());
        this.mWebPaint.setColor(this.mChart.getWebColor());
        this.mWebPaint.setAlpha(this.mChart.getWebAlpha());
        int xIncrements = this.mChart.getSkipWebLineCount() + 1;
        for (int i = 0; i < ((RadarData) this.mChart.getData()).getXValCount(); i += xIncrements) {
            PointF p = Utils.getPosition(center, this.mChart.getYRange() * factor, (((float) i) * sliceangle) + rotationangle);
            c.drawLine(center.x, center.y, p.x, p.y, this.mWebPaint);
        }
        this.mWebPaint.setStrokeWidth(this.mChart.getWebLineWidthInner());
        this.mWebPaint.setColor(this.mChart.getWebColorInner());
        this.mWebPaint.setAlpha(this.mChart.getWebAlpha());
        int labelCount = this.mChart.getYAxis().mEntryCount;
        for (int j = 0; j < labelCount; j++) {
            for (int i2 = 0; i2 < ((RadarData) this.mChart.getData()).getXValCount(); i2++) {
                float r = (this.mChart.getYAxis().mEntries[j] - this.mChart.getYChartMin()) * factor;
                PointF p1 = Utils.getPosition(center, r, (((float) i2) * sliceangle) + rotationangle);
                PointF p2 = Utils.getPosition(center, r, (((float) (i2 + 1)) * sliceangle) + rotationangle);
                c.drawLine(p1.x, p1.y, p2.x, p2.y, this.mWebPaint);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x004f, code lost:
        r22 = r26[r15].getXIndex();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void drawHighlighted(android.graphics.Canvas r25, com.github.mikephil.charting.highlight.Highlight[] r26) {
        /*
            r24 = this;
            r0 = r24
            com.github.mikephil.charting.animation.ChartAnimator r4 = r0.mAnimator
            float r17 = r4.getPhaseX()
            r0 = r24
            com.github.mikephil.charting.animation.ChartAnimator r4 = r0.mAnimator
            float r18 = r4.getPhaseY()
            r0 = r24
            com.github.mikephil.charting.charts.RadarChart r4 = r0.mChart
            float r21 = r4.getSliceAngle()
            r0 = r24
            com.github.mikephil.charting.charts.RadarChart r4 = r0.mChart
            float r14 = r4.getFactor()
            r0 = r24
            com.github.mikephil.charting.charts.RadarChart r4 = r0.mChart
            android.graphics.PointF r12 = r4.getCenterOffsets()
            r15 = 0
        L_0x0029:
            r0 = r26
            int r4 = r0.length
            if (r15 >= r4) goto L_0x0104
            r0 = r24
            com.github.mikephil.charting.charts.RadarChart r4 = r0.mChart
            com.github.mikephil.charting.data.ChartData r4 = r4.getData()
            com.github.mikephil.charting.data.RadarData r4 = (com.github.mikephil.charting.data.RadarData) r4
            r5 = r26[r15]
            int r5 = r5.getDataSetIndex()
            com.github.mikephil.charting.interfaces.datasets.IDataSet r20 = r4.getDataSetByIndex(r5)
            com.github.mikephil.charting.interfaces.datasets.IRadarDataSet r20 = (com.github.mikephil.charting.interfaces.datasets.IRadarDataSet) r20
            if (r20 == 0) goto L_0x004c
            boolean r4 = r20.isHighlightEnabled()
            if (r4 != 0) goto L_0x004f
        L_0x004c:
            int r15 = r15 + 1
            goto L_0x0029
        L_0x004f:
            r4 = r26[r15]
            int r22 = r4.getXIndex()
            r0 = r20
            r1 = r22
            com.github.mikephil.charting.data.Entry r13 = r0.getEntryForXIndex(r1)
            if (r13 == 0) goto L_0x004c
            int r4 = r13.getXIndex()
            r0 = r22
            if (r4 != r0) goto L_0x004c
            r0 = r20
            int r16 = r0.getEntryIndex(r13)
            float r4 = r13.getVal()
            r0 = r24
            com.github.mikephil.charting.charts.RadarChart r5 = r0.mChart
            float r5 = r5.getYChartMin()
            float r23 = r4 - r5
            boolean r4 = java.lang.Float.isNaN(r23)
            if (r4 != 0) goto L_0x004c
            float r4 = r23 * r14
            float r4 = r4 * r18
            r0 = r16
            float r5 = (float) r0
            float r5 = r5 * r21
            float r5 = r5 * r17
            r0 = r24
            com.github.mikephil.charting.charts.RadarChart r7 = r0.mChart
            float r7 = r7.getRotationAngle()
            float r5 = r5 + r7
            android.graphics.PointF r6 = com.github.mikephil.charting.utils.Utils.getPosition(r12, r4, r5)
            r4 = 2
            float[] r0 = new float[r4]
            r19 = r0
            r4 = 0
            float r5 = r6.x
            r19[r4] = r5
            r4 = 1
            float r5 = r6.y
            r19[r4] = r5
            r0 = r24
            r1 = r25
            r2 = r19
            r3 = r20
            r0.drawHighlightLines(r1, r2, r3)
            boolean r4 = r20.isDrawHighlightCircleEnabled()
            if (r4 == 0) goto L_0x004c
            r4 = 0
            r4 = r19[r4]
            boolean r4 = java.lang.Float.isNaN(r4)
            if (r4 != 0) goto L_0x004c
            r4 = 1
            r4 = r19[r4]
            boolean r4 = java.lang.Float.isNaN(r4)
            if (r4 != 0) goto L_0x004c
            int r10 = r20.getHighlightCircleStrokeColor()
            r4 = 1122867(0x112233, float:1.573472E-39)
            if (r10 != r4) goto L_0x00db
            r4 = 0
            r0 = r20
            int r10 = r0.getColor(r4)
        L_0x00db:
            int r4 = r20.getHighlightCircleStrokeAlpha()
            r5 = 255(0xff, float:3.57E-43)
            if (r4 >= r5) goto L_0x00eb
            int r4 = r20.getHighlightCircleStrokeAlpha()
            int r10 = com.github.mikephil.charting.utils.ColorTemplate.getColorWithAlphaComponent(r10, r4)
        L_0x00eb:
            float r7 = r20.getHighlightCircleInnerRadius()
            float r8 = r20.getHighlightCircleOuterRadius()
            int r9 = r20.getHighlightCircleFillColor()
            float r11 = r20.getHighlightCircleStrokeWidth()
            r4 = r24
            r5 = r25
            r4.drawHighlightCircle(r5, r6, r7, r8, r9, r10, r11)
            goto L_0x004c
        L_0x0104:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.mikephil.charting.renderer.RadarChartRenderer.drawHighlighted(android.graphics.Canvas, com.github.mikephil.charting.highlight.Highlight[]):void");
    }

    public void drawHighlightCircle(Canvas c, PointF point, float innerRadius, float outerRadius, int fillColor, int strokeColor, float strokeWidth) {
        c.save();
        float outerRadius2 = Utils.convertDpToPixel(outerRadius);
        float innerRadius2 = Utils.convertDpToPixel(innerRadius);
        if (fillColor != 1122867) {
            Path p = new Path();
            p.addCircle(point.x, point.y, outerRadius2, Path.Direction.CW);
            if (innerRadius2 > 0.0f) {
                p.addCircle(point.x, point.y, innerRadius2, Path.Direction.CCW);
            }
            this.mHighlightCirclePaint.setColor(fillColor);
            this.mHighlightCirclePaint.setStyle(Paint.Style.FILL);
            c.drawPath(p, this.mHighlightCirclePaint);
        }
        if (strokeColor != 1122867) {
            this.mHighlightCirclePaint.setColor(strokeColor);
            this.mHighlightCirclePaint.setStyle(Paint.Style.STROKE);
            this.mHighlightCirclePaint.setStrokeWidth(Utils.convertDpToPixel(strokeWidth));
            c.drawCircle(point.x, point.y, outerRadius2, this.mHighlightCirclePaint);
        }
        c.restore();
    }
}
