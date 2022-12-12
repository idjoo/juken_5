package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.PointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class YAxisRendererHorizontalBarChart extends YAxisRenderer {
    public YAxisRendererHorizontalBarChart(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
        this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
    }

    public void computeAxis(float yMin, float yMax) {
        if (this.mViewPortHandler.contentHeight() > 10.0f && !this.mViewPortHandler.isFullyZoomedOutX()) {
            PointD p1 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop());
            PointD p2 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop());
            if (!this.mYAxis.isInverted()) {
                yMin = (float) p1.x;
                yMax = (float) p2.x;
            } else {
                yMin = (float) p2.x;
                yMax = (float) p1.x;
            }
        }
        computeAxisValues(yMin, yMax);
    }

    public void renderAxisLabels(Canvas c) {
        float yPos;
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLabelsEnabled()) {
            float[] positions = new float[(this.mYAxis.mEntryCount * 2)];
            for (int i = 0; i < positions.length; i += 2) {
                positions[i] = this.mYAxis.mEntries[i / 2];
            }
            this.mTrans.pointValuesToPixel(positions);
            this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
            this.mAxisLabelPaint.setTextAlign(Paint.Align.CENTER);
            float baseYOffset = Utils.convertDpToPixel(2.5f);
            float textHeight = (float) Utils.calcTextHeight(this.mAxisLabelPaint, "Q");
            YAxis.AxisDependency dependency = this.mYAxis.getAxisDependency();
            YAxis.YAxisLabelPosition labelPosition = this.mYAxis.getLabelPosition();
            if (dependency == YAxis.AxisDependency.LEFT) {
                if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                    yPos = this.mViewPortHandler.contentTop() - baseYOffset;
                } else {
                    yPos = this.mViewPortHandler.contentTop() - baseYOffset;
                }
            } else if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                yPos = this.mViewPortHandler.contentBottom() + textHeight + baseYOffset;
            } else {
                yPos = this.mViewPortHandler.contentBottom() + textHeight + baseYOffset;
            }
            drawYLabels(c, yPos, positions, this.mYAxis.getYOffset());
        }
    }

    public void renderAxisLine(Canvas c) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawAxisLineEnabled()) {
            this.mAxisLinePaint.setColor(this.mYAxis.getAxisLineColor());
            this.mAxisLinePaint.setStrokeWidth(this.mYAxis.getAxisLineWidth());
            if (this.mYAxis.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mAxisLinePaint);
                return;
            }
            c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
        }
    }

    /* access modifiers changed from: protected */
    public void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
        this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
        this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
        int i = 0;
        while (i < this.mYAxis.mEntryCount) {
            String text = this.mYAxis.getFormattedLabel(i);
            if (this.mYAxis.isDrawTopYLabelEntryEnabled() || i < this.mYAxis.mEntryCount - 1) {
                c.drawText(text, positions[i * 2], fixedPosition - offset, this.mAxisLabelPaint);
                i++;
            } else {
                return;
            }
        }
    }

    public void renderGridLines(Canvas c) {
        if (this.mYAxis.isEnabled()) {
            float[] position = new float[2];
            if (this.mYAxis.isDrawGridLinesEnabled()) {
                this.mGridPaint.setColor(this.mYAxis.getGridColor());
                this.mGridPaint.setStrokeWidth(this.mYAxis.getGridLineWidth());
                for (int i = 0; i < this.mYAxis.mEntryCount; i++) {
                    position[0] = this.mYAxis.mEntries[i];
                    this.mTrans.pointValuesToPixel(position);
                    c.drawLine(position[0], this.mViewPortHandler.contentTop(), position[0], this.mViewPortHandler.contentBottom(), this.mGridPaint);
                }
            }
            if (this.mYAxis.isDrawZeroLineEnabled()) {
                position[0] = 0.0f;
                this.mTrans.pointValuesToPixel(position);
                drawZeroLine(c, position[0] + 1.0f, position[0] + 1.0f, this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentBottom());
            }
        }
    }

    public void renderLimitLines(Canvas c) {
        List<LimitLine> limitLines = this.mYAxis.getLimitLines();
        if (limitLines != null && limitLines.size() > 0) {
            float[] pts = new float[4];
            Path limitLinePath = new Path();
            for (int i = 0; i < limitLines.size(); i++) {
                LimitLine l = limitLines.get(i);
                if (l.isEnabled()) {
                    pts[0] = l.getLimit();
                    pts[2] = l.getLimit();
                    this.mTrans.pointValuesToPixel(pts);
                    pts[1] = this.mViewPortHandler.contentTop();
                    pts[3] = this.mViewPortHandler.contentBottom();
                    limitLinePath.moveTo(pts[0], pts[1]);
                    limitLinePath.lineTo(pts[2], pts[3]);
                    this.mLimitLinePaint.setStyle(Paint.Style.STROKE);
                    this.mLimitLinePaint.setColor(l.getLineColor());
                    this.mLimitLinePaint.setPathEffect(l.getDashPathEffect());
                    this.mLimitLinePaint.setStrokeWidth(l.getLineWidth());
                    c.drawPath(limitLinePath, this.mLimitLinePaint);
                    limitLinePath.reset();
                    String label = l.getLabel();
                    if (label != null && !label.equals("")) {
                        this.mLimitLinePaint.setStyle(l.getTextStyle());
                        this.mLimitLinePaint.setPathEffect((PathEffect) null);
                        this.mLimitLinePaint.setColor(l.getTextColor());
                        this.mLimitLinePaint.setTypeface(l.getTypeface());
                        this.mLimitLinePaint.setStrokeWidth(0.5f);
                        this.mLimitLinePaint.setTextSize(l.getTextSize());
                        float xOffset = l.getLineWidth() + l.getXOffset();
                        float yOffset = Utils.convertDpToPixel(2.0f) + l.getYOffset();
                        LimitLine.LimitLabelPosition position = l.getLabelPosition();
                        if (position == LimitLine.LimitLabelPosition.RIGHT_TOP) {
                            float labelLineHeight = (float) Utils.calcTextHeight(this.mLimitLinePaint, label);
                            this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                            c.drawText(label, pts[0] + xOffset, this.mViewPortHandler.contentTop() + yOffset + labelLineHeight, this.mLimitLinePaint);
                        } else if (position == LimitLine.LimitLabelPosition.RIGHT_BOTTOM) {
                            this.mLimitLinePaint.setTextAlign(Paint.Align.LEFT);
                            c.drawText(label, pts[0] + xOffset, this.mViewPortHandler.contentBottom() - yOffset, this.mLimitLinePaint);
                        } else if (position == LimitLine.LimitLabelPosition.LEFT_TOP) {
                            this.mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                            c.drawText(label, pts[0] - xOffset, this.mViewPortHandler.contentTop() + yOffset + ((float) Utils.calcTextHeight(this.mLimitLinePaint, label)), this.mLimitLinePaint);
                        } else {
                            this.mLimitLinePaint.setTextAlign(Paint.Align.RIGHT);
                            c.drawText(label, pts[0] - xOffset, this.mViewPortHandler.contentBottom() - yOffset, this.mLimitLinePaint);
                        }
                    }
                }
            }
        }
    }
}
