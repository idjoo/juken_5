package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.v4.view.ViewCompat;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.PointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

public class YAxisRenderer extends AxisRenderer {
    protected YAxis mYAxis;
    protected Paint mZeroLinePaint = new Paint(1);

    public YAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, trans);
        this.mYAxis = yAxis;
        this.mAxisLabelPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10.0f));
        this.mZeroLinePaint.setColor(-7829368);
        this.mZeroLinePaint.setStrokeWidth(1.0f);
        this.mZeroLinePaint.setStyle(Paint.Style.STROKE);
    }

    public void computeAxis(float yMin, float yMax) {
        if (this.mViewPortHandler.contentWidth() > 10.0f && !this.mViewPortHandler.isFullyZoomedOutY()) {
            PointD p1 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop());
            PointD p2 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom());
            if (!this.mYAxis.isInverted()) {
                yMin = (float) p2.y;
                yMax = (float) p1.y;
            } else {
                yMin = (float) p1.y;
                yMax = (float) p2.y;
            }
        }
        computeAxisValues(yMin, yMax);
    }

    /* access modifiers changed from: protected */
    public void computeAxisValues(float min, float max) {
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
        if (this.mYAxis.isGranularityEnabled() && interval < ((double) this.mYAxis.getGranularity())) {
            interval = (double) this.mYAxis.getGranularity();
        }
        double intervalMagnitude = (double) Utils.roundToNextSignificant(Math.pow(10.0d, (double) ((int) Math.log10(interval))));
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
            double first = Math.ceil(((double) yMin) / interval) * interval;
            int n = 0;
            for (double f = first; f <= Utils.nextUp(Math.floor(((double) yMax) / interval) * interval); f += interval) {
                n++;
            }
            this.mYAxis.mEntryCount = n;
            if (this.mYAxis.mEntries.length < n) {
                this.mYAxis.mEntries = new float[n];
            }
            double f2 = first;
            for (int i2 = 0; i2 < n; i2++) {
                if (f2 == 0.0d) {
                    f2 = 0.0d;
                }
                this.mYAxis.mEntries[i2] = (float) f2;
                f2 += interval;
            }
        }
        if (interval < 1.0d) {
            this.mYAxis.mDecimals = (int) Math.ceil(-Math.log10(interval));
            return;
        }
        this.mYAxis.mDecimals = 0;
    }

    public void renderAxisLabels(Canvas c) {
        float xPos;
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLabelsEnabled()) {
            float[] positions = new float[(this.mYAxis.mEntryCount * 2)];
            for (int i = 0; i < positions.length; i += 2) {
                positions[i + 1] = this.mYAxis.mEntries[i / 2];
            }
            this.mTrans.pointValuesToPixel(positions);
            this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
            float xoffset = this.mYAxis.getXOffset();
            float yoffset = (((float) Utils.calcTextHeight(this.mAxisLabelPaint, "A")) / 2.5f) + this.mYAxis.getYOffset();
            YAxis.AxisDependency dependency = this.mYAxis.getAxisDependency();
            YAxis.YAxisLabelPosition labelPosition = this.mYAxis.getLabelPosition();
            if (dependency == YAxis.AxisDependency.LEFT) {
                if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                    this.mAxisLabelPaint.setTextAlign(Paint.Align.RIGHT);
                    xPos = this.mViewPortHandler.offsetLeft() - xoffset;
                } else {
                    this.mAxisLabelPaint.setTextAlign(Paint.Align.LEFT);
                    xPos = this.mViewPortHandler.offsetLeft() + xoffset;
                }
            } else if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                this.mAxisLabelPaint.setTextAlign(Paint.Align.LEFT);
                xPos = this.mViewPortHandler.contentRight() + xoffset;
            } else {
                this.mAxisLabelPaint.setTextAlign(Paint.Align.RIGHT);
                xPos = this.mViewPortHandler.contentRight() - xoffset;
            }
            drawYLabels(c, xPos, positions, yoffset);
        }
    }

    public void renderAxisLine(Canvas c) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawAxisLineEnabled()) {
            this.mAxisLinePaint.setColor(this.mYAxis.getAxisLineColor());
            this.mAxisLinePaint.setStrokeWidth(this.mYAxis.getAxisLineWidth());
            if (this.mYAxis.getAxisDependency() == YAxis.AxisDependency.LEFT) {
                c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
                return;
            }
            c.drawLine(this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
        }
    }

    /* access modifiers changed from: protected */
    public void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        int i = 0;
        while (i < this.mYAxis.mEntryCount) {
            String text = this.mYAxis.getFormattedLabel(i);
            if (this.mYAxis.isDrawTopYLabelEntryEnabled() || i < this.mYAxis.mEntryCount - 1) {
                c.drawText(text, fixedPosition, positions[(i * 2) + 1] + offset, this.mAxisLabelPaint);
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
                this.mGridPaint.setPathEffect(this.mYAxis.getGridDashPathEffect());
                Path gridLinePath = new Path();
                for (int i = 0; i < this.mYAxis.mEntryCount; i++) {
                    position[1] = this.mYAxis.mEntries[i];
                    this.mTrans.pointValuesToPixel(position);
                    gridLinePath.moveTo(this.mViewPortHandler.offsetLeft(), position[1]);
                    gridLinePath.lineTo(this.mViewPortHandler.contentRight(), position[1]);
                    c.drawPath(gridLinePath, this.mGridPaint);
                    gridLinePath.reset();
                }
            }
            if (this.mYAxis.isDrawZeroLineEnabled()) {
                position[1] = 0.0f;
                this.mTrans.pointValuesToPixel(position);
                drawZeroLine(c, this.mViewPortHandler.offsetLeft(), this.mViewPortHandler.contentRight(), position[1] - 1.0f, position[1] - 1.0f);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawZeroLine(Canvas c, float x1, float x2, float y1, float y2) {
        this.mZeroLinePaint.setColor(this.mYAxis.getZeroLineColor());
        this.mZeroLinePaint.setStrokeWidth(this.mYAxis.getZeroLineWidth());
        Path zeroLinePath = new Path();
        zeroLinePath.moveTo(x1, y1);
        zeroLinePath.lineTo(x2, y2);
        c.drawPath(zeroLinePath, this.mZeroLinePaint);
    }

    public void renderLimitLines(Canvas c) {
        List<LimitLine> limitLines = this.mYAxis.getLimitLines();
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
                        this.mLimitLinePaint.setTypeface(l.getTypeface());
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
