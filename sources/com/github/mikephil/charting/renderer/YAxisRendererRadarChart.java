package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class YAxisRendererRadarChart extends YAxisRenderer {
    private RadarChart mChart;

    public YAxisRendererRadarChart(ViewPortHandler viewPortHandler, YAxis yAxis, RadarChart chart) {
        super(viewPortHandler, yAxis, (Transformer) null);
        this.mChart = chart;
    }

    public void computeAxis(float yMin, float yMax) {
        computeAxisValues(yMin, yMax);
    }

    /* access modifiers changed from: protected */
    public void computeAxisValues(float min, float max) {
        double first;
        float yMin = min;
        float yMax = max;
        int labelCount = this.mYAxis.getLabelCount();
        double range = (double) Math.abs(yMax - yMin);
        if (labelCount == 0 || range <= 0.0d) {
            this.mYAxis.mEntries = new float[0];
            this.mYAxis.mEntryCount = 0;
            return;
        }
        double interval = (double) Utils.roundToNextSignificant(range / ((double) labelCount));
        double intervalMagnitude = Math.pow(10.0d, (double) ((int) Math.log10(interval)));
        if (((int) (interval / intervalMagnitude)) > 5) {
            interval = Math.floor(10.0d * intervalMagnitude);
        }
        if (this.mYAxis.isForceLabelsEnabled()) {
            float step = ((float) range) / ((float) (labelCount - 1));
            this.mYAxis.mEntryCount = labelCount;
            if (this.mYAxis.mEntries.length < labelCount) {
                this.mYAxis.mEntries = new float[labelCount];
            }
            float v = min;
            for (int i = 0; i < labelCount; i++) {
                this.mYAxis.mEntries[i] = v;
                v += step;
            }
        } else if (this.mYAxis.isShowOnlyMinMaxEnabled()) {
            this.mYAxis.mEntryCount = 2;
            this.mYAxis.mEntries = new float[2];
            this.mYAxis.mEntries[0] = yMin;
            this.mYAxis.mEntries[1] = yMax;
        } else {
            double rawCount = ((double) yMin) / interval;
            if (rawCount < 0.0d) {
                first = Math.floor(rawCount) * interval;
            } else {
                first = Math.ceil(rawCount) * interval;
            }
            if (first == 0.0d) {
                first = 0.0d;
            }
            int n = 0;
            for (double f = first; f <= Utils.nextUp(Math.floor(((double) yMax) / interval) * interval); f += interval) {
                n++;
            }
            if (!this.mYAxis.isAxisMaxCustom()) {
                n++;
            }
            this.mYAxis.mEntryCount = n;
            if (this.mYAxis.mEntries.length < n) {
                this.mYAxis.mEntries = new float[n];
            }
            double f2 = first;
            for (int i2 = 0; i2 < n; i2++) {
                this.mYAxis.mEntries[i2] = (float) f2;
                f2 += interval;
            }
        }
        if (interval < 1.0d) {
            this.mYAxis.mDecimals = (int) Math.ceil(-Math.log10(interval));
        } else {
            this.mYAxis.mDecimals = 0;
        }
        if (this.mYAxis.mEntries[0] < yMin) {
            this.mYAxis.mAxisMinimum = this.mYAxis.mEntries[0];
        }
        this.mYAxis.mAxisMaximum = this.mYAxis.mEntries[this.mYAxis.mEntryCount - 1];
        this.mYAxis.mAxisRange = Math.abs(this.mYAxis.mAxisMaximum - this.mYAxis.mAxisMinimum);
    }

    public void renderAxisLabels(Canvas c) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLabelsEnabled()) {
            this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
            PointF center = this.mChart.getCenterOffsets();
            float factor = this.mChart.getFactor();
            int labelCount = this.mYAxis.mEntryCount;
            int j = 0;
            while (j < labelCount) {
                if (j != labelCount - 1 || this.mYAxis.isDrawTopYLabelEntryEnabled()) {
                    PointF p = Utils.getPosition(center, (this.mYAxis.mEntries[j] - this.mYAxis.mAxisMinimum) * factor, this.mChart.getRotationAngle());
                    c.drawText(this.mYAxis.getFormattedLabel(j), p.x + 10.0f, p.y, this.mAxisLabelPaint);
                    j++;
                } else {
                    return;
                }
            }
        }
    }

    public void renderLimitLines(Canvas c) {
        List<LimitLine> limitLines = this.mYAxis.getLimitLines();
        if (limitLines != null) {
            float sliceangle = this.mChart.getSliceAngle();
            float factor = this.mChart.getFactor();
            PointF center = this.mChart.getCenterOffsets();
            for (int i = 0; i < limitLines.size(); i++) {
                LimitLine l = limitLines.get(i);
                if (l.isEnabled()) {
                    this.mLimitLinePaint.setColor(l.getLineColor());
                    this.mLimitLinePaint.setPathEffect(l.getDashPathEffect());
                    this.mLimitLinePaint.setStrokeWidth(l.getLineWidth());
                    float r = (l.getLimit() - this.mChart.getYChartMin()) * factor;
                    Path limitPath = new Path();
                    for (int j = 0; j < ((RadarData) this.mChart.getData()).getXValCount(); j++) {
                        PointF p = Utils.getPosition(center, r, (((float) j) * sliceangle) + this.mChart.getRotationAngle());
                        if (j == 0) {
                            limitPath.moveTo(p.x, p.y);
                        } else {
                            limitPath.lineTo(p.x, p.y);
                        }
                    }
                    limitPath.close();
                    c.drawPath(limitPath, this.mLimitLinePaint);
                }
            }
        }
    }
}
