package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class XAxisRendererHorizontalBarChart extends XAxisRendererBarChart {
    public XAxisRendererHorizontalBarChart(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans, BarChart chart) {
        super(viewPortHandler, xAxis, trans, chart);
    }

    public void computeAxis(float xValAverageLength, List<String> xValues) {
        this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
        this.mXAxis.setValues(xValues);
        FSize labelSize = Utils.calcTextSize(this.mAxisLabelPaint, this.mXAxis.getLongestLabel());
        float labelWidth = (float) ((int) (labelSize.width + (this.mXAxis.getXOffset() * 3.5f)));
        float labelHeight = labelSize.height;
        FSize labelRotatedSize = Utils.getSizeOfRotatedRectangleByDegrees(labelSize.width, labelHeight, this.mXAxis.getLabelRotationAngle());
        this.mXAxis.mLabelWidth = Math.round(labelWidth);
        this.mXAxis.mLabelHeight = Math.round(labelHeight);
        this.mXAxis.mLabelRotatedWidth = (int) (labelRotatedSize.width + (this.mXAxis.getXOffset() * 3.5f));
        this.mXAxis.mLabelRotatedHeight = Math.round(labelRotatedSize.height);
    }

    public void renderAxisLabels(Canvas c) {
        if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
            float xoffset = this.mXAxis.getXOffset();
            this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mXAxis.getTextColor());
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
                drawLabels(c, this.mViewPortHandler.contentRight() + xoffset, new PointF(0.0f, 0.5f));
            } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE) {
                drawLabels(c, this.mViewPortHandler.contentRight() - xoffset, new PointF(1.0f, 0.5f));
            } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
                drawLabels(c, this.mViewPortHandler.contentLeft() - xoffset, new PointF(1.0f, 0.5f));
            } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE) {
                drawLabels(c, this.mViewPortHandler.contentLeft() + xoffset, new PointF(0.0f, 0.5f));
            } else {
                drawLabels(c, this.mViewPortHandler.contentRight() + xoffset, new PointF(0.0f, 0.5f));
                drawLabels(c, this.mViewPortHandler.contentLeft() - xoffset, new PointF(1.0f, 0.5f));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawLabels(Canvas c, float pos, PointF anchor) {
        float labelRotationAngleDegrees = this.mXAxis.getLabelRotationAngle();
        float[] position = {0.0f, 0.0f};
        BarData bd = (BarData) this.mChart.getData();
        int step = bd.getDataSetCount();
        int i = this.mMinX;
        while (i <= this.mMaxX) {
            position[1] = ((float) (i * step)) + (((float) i) * bd.getGroupSpace()) + (bd.getGroupSpace() / 2.0f);
            if (step > 1) {
                position[1] = position[1] + ((((float) step) - 1.0f) / 2.0f);
            }
            this.mTrans.pointValuesToPixel(position);
            if (this.mViewPortHandler.isInBoundsY(position[1])) {
                drawLabel(c, this.mXAxis.getValues().get(i), i, pos, position[1], anchor, labelRotationAngleDegrees);
            }
            i += this.mXAxis.mAxisLabelModulus;
        }
    }

    public void renderGridLines(Canvas c) {
        if (this.mXAxis.isDrawGridLinesEnabled() && this.mXAxis.isEnabled()) {
            float[] position = {0.0f, 0.0f};
            this.mGridPaint.setColor(this.mXAxis.getGridColor());
            this.mGridPaint.setStrokeWidth(this.mXAxis.getGridLineWidth());
            BarData bd = (BarData) this.mChart.getData();
            int step = bd.getDataSetCount();
            int i = this.mMinX;
            while (i <= this.mMaxX) {
                position[1] = (((float) (i * step)) + (((float) i) * bd.getGroupSpace())) - 0.5f;
                this.mTrans.pointValuesToPixel(position);
                if (this.mViewPortHandler.isInBoundsY(position[1])) {
                    c.drawLine(this.mViewPortHandler.contentLeft(), position[1], this.mViewPortHandler.contentRight(), position[1], this.mGridPaint);
                }
                i += this.mXAxis.mAxisLabelModulus;
            }
        }
    }

    public void renderAxisLine(Canvas c) {
        if (this.mXAxis.isDrawAxisLineEnabled() && this.mXAxis.isEnabled()) {
            this.mAxisLinePaint.setColor(this.mXAxis.getAxisLineColor());
            this.mAxisLinePaint.setStrokeWidth(this.mXAxis.getAxisLineWidth());
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP || this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                c.drawLine(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
            }
            if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE || this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
            }
        }
    }

    public void renderLimitLines(Canvas c) {
        List<LimitLine> limitLines = this.mXAxis.getLimitLines();
        if (limitLines != null && limitLines.size() > 0) {
            float[] pts = new float[2];
            Path limitLinePath = new Path();
            for (int i = 0; i < limitLines.size(); i++) {
                LimitLine l = limitLines.get(i);
                if (l.isEnabled()) {
                    this.mLimitLinePaint.setStyle(Paint.Style.STROKE);
                    this.mLimitLinePaint.setColor(l.getLineColor());
                    this.mLimitLinePaint.setStrokeWidth(l.getLineWidth());
                    this.mLimitLinePaint.setPathEffect(l.getDashPathEffect());
                    pts[1] = l.getLimit();
                    this.mTrans.pointValuesToPixel(pts);
                    limitLinePath.moveTo(this.mViewPortHandler.contentLeft(), pts[1]);
                    limitLinePath.lineTo(this.mViewPortHandler.contentRight(), pts[1]);
                    c.drawPath(limitLinePath, this.mLimitLinePaint);
                    limitLinePath.reset();
                    String label = l.getLabel();
                    if (label != null && !label.equals("")) {
                        this.mLimitLinePaint.setStyle(l.getTextStyle());
                        this.mLimitLinePaint.setPathEffect((PathEffect) null);
                        this.mLimitLinePaint.setColor(l.getTextColor());
                        this.mLimitLinePaint.setStrokeWidth(0.5f);
                        this.mLimitLinePaint.setTextSize(l.getTextSize());
                        float labelLineHeight = (float) Utils.calcTextHeight(this.mLimitLinePaint, label);
                        float xOffset = Utils.convertDpToPixel(4.0f) + l.getXOffset();
                        float yOffset = l.getLineWidth() + labelLineHeight + l.getYOffset();
                        LimitLine.LimitLabelPosition position = l.getLabelPosition();
                        if (position == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                            this.mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                            c.drawText(label, this.mViewPortHandler.contentRight() - xOffset, (pts[1] - yOffset) + labelLineHeight, this.mLimitLinePaint);
                        } else if (position == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                            this.mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                            c.drawText(label, this.mViewPortHandler.contentRight() - xOffset, pts[1] + yOffset, this.mLimitLinePaint);
                        } else if (position == LimitLine.LimitLabelPosition.LEFT_TOP) {
                            this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                            c.drawText(label, this.mViewPortHandler.contentLeft() + xOffset, (pts[1] - yOffset) + labelLineHeight, this.mLimitLinePaint);
                        } else {
                            this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                            c.drawText(label, this.mViewPortHandler.offsetLeft() + xOffset, pts[1] + yOffset, this.mLimitLinePaint);
                        }
                    }
                }
            }
        }
    }
}
