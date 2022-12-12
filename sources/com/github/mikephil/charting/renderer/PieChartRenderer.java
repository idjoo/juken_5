package com.github.mikephil.charting.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.lang.ref.WeakReference;
import java.util.List;

public class PieChartRenderer extends DataRenderer {
    protected Canvas mBitmapCanvas;
    private RectF mCenterTextLastBounds = new RectF();
    private CharSequence mCenterTextLastValue;
    private StaticLayout mCenterTextLayout;
    private TextPaint mCenterTextPaint;
    protected PieChart mChart;
    protected WeakReference<Bitmap> mDrawBitmap;
    private Path mHoleCirclePath = new Path();
    protected Paint mHolePaint;
    private RectF mInnerRectBuffer = new RectF();
    private Path mPathBuffer = new Path();
    private RectF[] mRectBuffer = {new RectF(), new RectF(), new RectF()};
    protected Paint mTransparentCirclePaint;

    public PieChartRenderer(PieChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;
        this.mHolePaint = new Paint(1);
        this.mHolePaint.setColor(-1);
        this.mHolePaint.setStyle(Paint.Style.FILL);
        this.mTransparentCirclePaint = new Paint(1);
        this.mTransparentCirclePaint.setColor(-1);
        this.mTransparentCirclePaint.setStyle(Paint.Style.FILL);
        this.mTransparentCirclePaint.setAlpha(105);
        this.mCenterTextPaint = new TextPaint(1);
        this.mCenterTextPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mCenterTextPaint.setTextSize(Utils.convertDpToPixel(12.0f));
        this.mValuePaint.setTextSize(Utils.convertDpToPixel(13.0f));
        this.mValuePaint.setColor(-1);
        this.mValuePaint.setTextAlign(Paint.Align.CENTER);
    }

    public Paint getPaintHole() {
        return this.mHolePaint;
    }

    public Paint getPaintTransparentCircle() {
        return this.mTransparentCirclePaint;
    }

    public TextPaint getPaintCenterText() {
        return this.mCenterTextPaint;
    }

    public void initBuffers() {
    }

    public void drawData(Canvas c) {
        int width = (int) this.mViewPortHandler.getChartWidth();
        int height = (int) this.mViewPortHandler.getChartHeight();
        if (!(this.mDrawBitmap != null && ((Bitmap) this.mDrawBitmap.get()).getWidth() == width && ((Bitmap) this.mDrawBitmap.get()).getHeight() == height)) {
            if (width > 0 && height > 0) {
                this.mDrawBitmap = new WeakReference<>(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444));
                this.mBitmapCanvas = new Canvas((Bitmap) this.mDrawBitmap.get());
            } else {
                return;
            }
        }
        ((Bitmap) this.mDrawBitmap.get()).eraseColor(0);
        for (IPieDataSet set : ((PieData) this.mChart.getData()).getDataSets()) {
            if (set.isVisible() && set.getEntryCount() > 0) {
                drawDataSet(c, set);
            }
        }
    }

    /* access modifiers changed from: protected */
    public float calculateMinimumRadiusForSpacedSlice(PointF center, float radius, float angle, float arcStartPointX, float arcStartPointY, float startAngle, float sweepAngle) {
        float angleMiddle = startAngle + (sweepAngle / 2.0f);
        float arcEndPointX = center.x + (((float) Math.cos((double) ((startAngle + sweepAngle) * 0.017453292f))) * radius);
        float arcEndPointY = center.y + (((float) Math.sin((double) ((startAngle + sweepAngle) * 0.017453292f))) * radius);
        return (float) (((double) (radius - ((float) ((Math.sqrt(Math.pow((double) (arcEndPointX - arcStartPointX), 2.0d) + Math.pow((double) (arcEndPointY - arcStartPointY), 2.0d)) / 2.0d) * Math.tan(((180.0d - ((double) angle)) / 2.0d) * 0.017453292519943295d))))) - Math.sqrt(Math.pow((double) ((center.x + (((float) Math.cos((double) (0.017453292f * angleMiddle))) * radius)) - ((arcEndPointX + arcStartPointX) / 2.0f)), 2.0d) + Math.pow((double) ((center.y + (((float) Math.sin((double) (0.017453292f * angleMiddle))) * radius)) - ((arcEndPointY + arcStartPointY) / 2.0f)), 2.0d)));
    }

    /* access modifiers changed from: protected */
    public void drawDataSet(Canvas c, IPieDataSet dataSet) {
        float sliceSpaceAngleOuter;
        float sliceSpaceAngleInner;
        float angle = 0.0f;
        float rotationAngle = this.mChart.getRotationAngle();
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        RectF circleBox = this.mChart.getCircleBox();
        int entryCount = dataSet.getEntryCount();
        float[] drawAngles = this.mChart.getDrawAngles();
        PointF center = this.mChart.getCenterCircleBox();
        float radius = this.mChart.getRadius();
        boolean drawInnerArc = this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled();
        float userInnerRadius = drawInnerArc ? radius * (this.mChart.getHoleRadius() / 100.0f) : 0.0f;
        int visibleAngleCount = 0;
        for (int j = 0; j < entryCount; j++) {
            if (((double) Math.abs(dataSet.getEntryForIndex(j).getVal())) > 1.0E-6d) {
                visibleAngleCount++;
            }
        }
        float sliceSpace = visibleAngleCount <= 1 ? 0.0f : dataSet.getSliceSpace();
        for (int j2 = 0; j2 < entryCount; j2++) {
            float sliceAngle = drawAngles[j2];
            float innerRadius = userInnerRadius;
            Entry e = dataSet.getEntryForIndex(j2);
            if (((double) Math.abs(e.getVal())) > 1.0E-6d) {
                if (!this.mChart.needsHighlight(e.getXIndex(), ((PieData) this.mChart.getData()).getIndexOfDataSet(dataSet))) {
                    boolean accountForSliceSpacing = sliceSpace > 0.0f && sliceAngle <= 180.0f;
                    this.mRenderPaint.setColor(dataSet.getColor(j2));
                    if (visibleAngleCount == 1) {
                        sliceSpaceAngleOuter = 0.0f;
                    } else {
                        sliceSpaceAngleOuter = sliceSpace / (0.017453292f * radius);
                    }
                    float startAngleOuter = rotationAngle + (((sliceSpaceAngleOuter / 2.0f) + angle) * phaseY);
                    float sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * phaseY;
                    if (sweepAngleOuter < 0.0f) {
                        sweepAngleOuter = 0.0f;
                    }
                    this.mPathBuffer.reset();
                    float arcStartPointX = 0.0f;
                    float arcStartPointY = 0.0f;
                    if (sweepAngleOuter % 360.0f == 0.0f) {
                        this.mPathBuffer.addCircle(center.x, center.y, radius, Path.Direction.CW);
                    } else {
                        arcStartPointX = center.x + (((float) Math.cos((double) (0.017453292f * startAngleOuter))) * radius);
                        arcStartPointY = center.y + (((float) Math.sin((double) (0.017453292f * startAngleOuter))) * radius);
                        this.mPathBuffer.moveTo(arcStartPointX, arcStartPointY);
                        this.mPathBuffer.arcTo(circleBox, startAngleOuter, sweepAngleOuter);
                    }
                    this.mInnerRectBuffer.set(center.x - innerRadius, center.y - innerRadius, center.x + innerRadius, center.y + innerRadius);
                    if (drawInnerArc && (innerRadius > 0.0f || accountForSliceSpacing)) {
                        if (accountForSliceSpacing) {
                            float minSpacedRadius = calculateMinimumRadiusForSpacedSlice(center, radius, sliceAngle * phaseY, arcStartPointX, arcStartPointY, startAngleOuter, sweepAngleOuter);
                            if (minSpacedRadius < 0.0f) {
                                minSpacedRadius = -minSpacedRadius;
                            }
                            innerRadius = Math.max(innerRadius, minSpacedRadius);
                        }
                        if (visibleAngleCount == 1 || innerRadius == 0.0f) {
                            sliceSpaceAngleInner = 0.0f;
                        } else {
                            sliceSpaceAngleInner = sliceSpace / (0.017453292f * innerRadius);
                        }
                        float startAngleInner = rotationAngle + (((sliceSpaceAngleInner / 2.0f) + angle) * phaseY);
                        float sweepAngleInner = (sliceAngle - sliceSpaceAngleInner) * phaseY;
                        if (sweepAngleInner < 0.0f) {
                            sweepAngleInner = 0.0f;
                        }
                        float endAngleInner = startAngleInner + sweepAngleInner;
                        if (sweepAngleOuter % 360.0f == 0.0f) {
                            this.mPathBuffer.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW);
                        } else {
                            this.mPathBuffer.lineTo(center.x + (((float) Math.cos((double) (0.017453292f * endAngleInner))) * innerRadius), center.y + (((float) Math.sin((double) (0.017453292f * endAngleInner))) * innerRadius));
                            this.mPathBuffer.arcTo(this.mInnerRectBuffer, endAngleInner, -sweepAngleInner);
                        }
                    } else if (sweepAngleOuter % 360.0f != 0.0f) {
                        if (accountForSliceSpacing) {
                            float angleMiddle = startAngleOuter + (sweepAngleOuter / 2.0f);
                            float sliceSpaceOffset = calculateMinimumRadiusForSpacedSlice(center, radius, sliceAngle * phaseY, arcStartPointX, arcStartPointY, startAngleOuter, sweepAngleOuter);
                            this.mPathBuffer.lineTo(center.x + (((float) Math.cos((double) (0.017453292f * angleMiddle))) * sliceSpaceOffset), center.y + (((float) Math.sin((double) (0.017453292f * angleMiddle))) * sliceSpaceOffset));
                        } else {
                            this.mPathBuffer.lineTo(center.x, center.y);
                        }
                    }
                    this.mPathBuffer.close();
                    this.mBitmapCanvas.drawPath(this.mPathBuffer, this.mRenderPaint);
                }
            }
            angle += sliceAngle * phaseX;
        }
    }

    public void drawValues(Canvas c) {
        float angle;
        PointF center = this.mChart.getCenterCircleBox();
        float r = this.mChart.getRadius();
        float rotationAngle = this.mChart.getRotationAngle();
        float[] drawAngles = this.mChart.getDrawAngles();
        float[] absoluteAngles = this.mChart.getAbsoluteAngles();
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float off = (r / 10.0f) * 3.6f;
        if (this.mChart.isDrawHoleEnabled()) {
            off = (r - ((r / 100.0f) * this.mChart.getHoleRadius())) / 2.0f;
        }
        float r2 = r - off;
        PieData data = (PieData) this.mChart.getData();
        List<IPieDataSet> dataSets = data.getDataSets();
        float yValueSum = data.getYValueSum();
        boolean drawXVals = this.mChart.isDrawSliceTextEnabled();
        int xIndex = 0;
        for (int i = 0; i < dataSets.size(); i++) {
            IPieDataSet dataSet = dataSets.get(i);
            if (dataSet.isDrawValuesEnabled() || drawXVals) {
                applyValueTextStyle(dataSet);
                float lineHeight = ((float) Utils.calcTextHeight(this.mValuePaint, "Q")) + Utils.convertDpToPixel(4.0f);
                int entryCount = dataSet.getEntryCount();
                int maxEntry = Math.min((int) Math.ceil((double) (((float) entryCount) * phaseX)), entryCount);
                for (int j = 0; j < maxEntry; j++) {
                    Entry entry = dataSet.getEntryForIndex(j);
                    if (xIndex == 0) {
                        angle = 0.0f;
                    } else {
                        angle = absoluteAngles[xIndex - 1] * phaseX;
                    }
                    float angle2 = angle + ((drawAngles[xIndex] - ((dataSet.getSliceSpace() / (0.017453292f * r2)) / 2.0f)) / 2.0f);
                    float x = (float) ((((double) r2) * Math.cos(Math.toRadians((double) (rotationAngle + angle2)))) + ((double) center.x));
                    float y = (float) ((((double) r2) * Math.sin(Math.toRadians((double) (rotationAngle + angle2)))) + ((double) center.y));
                    float value = this.mChart.isUsePercentValuesEnabled() ? (entry.getVal() / yValueSum) * 100.0f : entry.getVal();
                    ValueFormatter formatter = dataSet.getValueFormatter();
                    boolean drawYVals = dataSet.isDrawValuesEnabled();
                    if (drawXVals && drawYVals) {
                        drawValue(c, formatter, value, entry, 0, x, y, dataSet.getValueTextColor(j));
                        if (j < data.getXValCount()) {
                            c.drawText(data.getXVals().get(j), x, y + lineHeight, this.mValuePaint);
                        }
                    } else if (drawXVals) {
                        if (j < data.getXValCount()) {
                            this.mValuePaint.setColor(dataSet.getValueTextColor(j));
                            c.drawText(data.getXVals().get(j), x, (lineHeight / 2.0f) + y, this.mValuePaint);
                        }
                    } else if (drawYVals) {
                        drawValue(c, formatter, value, entry, 0, x, y + (lineHeight / 2.0f), dataSet.getValueTextColor(j));
                    }
                    xIndex++;
                }
            }
        }
    }

    public void drawExtras(Canvas c) {
        drawHole(c);
        c.drawBitmap((Bitmap) this.mDrawBitmap.get(), 0.0f, 0.0f, (Paint) null);
        drawCenterText(c);
    }

    /* access modifiers changed from: protected */
    public void drawHole(Canvas c) {
        if (this.mChart.isDrawHoleEnabled()) {
            float radius = this.mChart.getRadius();
            float holeRadius = radius * (this.mChart.getHoleRadius() / 100.0f);
            PointF center = this.mChart.getCenterCircleBox();
            if (Color.alpha(this.mHolePaint.getColor()) > 0) {
                this.mBitmapCanvas.drawCircle(center.x, center.y, holeRadius, this.mHolePaint);
            }
            if (Color.alpha(this.mTransparentCirclePaint.getColor()) > 0 && this.mChart.getTransparentCircleRadius() > this.mChart.getHoleRadius()) {
                int alpha = this.mTransparentCirclePaint.getAlpha();
                float secondHoleRadius = radius * (this.mChart.getTransparentCircleRadius() / 100.0f);
                this.mTransparentCirclePaint.setAlpha((int) (((float) alpha) * this.mAnimator.getPhaseX() * this.mAnimator.getPhaseY()));
                this.mHoleCirclePath.reset();
                this.mHoleCirclePath.addCircle(center.x, center.y, secondHoleRadius, Path.Direction.CW);
                this.mHoleCirclePath.addCircle(center.x, center.y, holeRadius, Path.Direction.CCW);
                this.mBitmapCanvas.drawPath(this.mHoleCirclePath, this.mTransparentCirclePaint);
                this.mTransparentCirclePaint.setAlpha(alpha);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawCenterText(Canvas c) {
        CharSequence centerText = this.mChart.getCenterText();
        if (this.mChart.isDrawCenterTextEnabled() && centerText != null) {
            PointF center = this.mChart.getCenterCircleBox();
            float innerRadius = (!this.mChart.isDrawHoleEnabled() || this.mChart.isDrawSlicesUnderHoleEnabled()) ? this.mChart.getRadius() : this.mChart.getRadius() * (this.mChart.getHoleRadius() / 100.0f);
            RectF holeRect = this.mRectBuffer[0];
            holeRect.left = center.x - innerRadius;
            holeRect.top = center.y - innerRadius;
            holeRect.right = center.x + innerRadius;
            holeRect.bottom = center.y + innerRadius;
            RectF boundingRect = this.mRectBuffer[1];
            boundingRect.set(holeRect);
            float radiusPercent = this.mChart.getCenterTextRadiusPercent() / 100.0f;
            if (((double) radiusPercent) > 0.0d) {
                boundingRect.inset((boundingRect.width() - (boundingRect.width() * radiusPercent)) / 2.0f, (boundingRect.height() - (boundingRect.height() * radiusPercent)) / 2.0f);
            }
            if (!centerText.equals(this.mCenterTextLastValue) || !boundingRect.equals(this.mCenterTextLastBounds)) {
                this.mCenterTextLastBounds.set(boundingRect);
                this.mCenterTextLastValue = centerText;
                this.mCenterTextLayout = new StaticLayout(centerText, 0, centerText.length(), this.mCenterTextPaint, (int) Math.max(Math.ceil((double) this.mCenterTextLastBounds.width()), 1.0d), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            }
            float layoutHeight = (float) this.mCenterTextLayout.getHeight();
            c.save();
            if (Build.VERSION.SDK_INT >= 18) {
                Path path = new Path();
                path.addOval(holeRect, Path.Direction.CW);
                c.clipPath(path);
            }
            c.translate(boundingRect.left, boundingRect.top + ((boundingRect.height() - layoutHeight) / 2.0f));
            this.mCenterTextLayout.draw(c);
            c.restore();
        }
    }

    public void drawHighlighted(Canvas c, Highlight[] indices) {
        IPieDataSet set;
        float angle;
        float sliceSpaceAngleShifted;
        float sliceSpaceAngleInner;
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float rotationAngle = this.mChart.getRotationAngle();
        float[] drawAngles = this.mChart.getDrawAngles();
        float[] absoluteAngles = this.mChart.getAbsoluteAngles();
        PointF center = this.mChart.getCenterCircleBox();
        float radius = this.mChart.getRadius();
        boolean drawInnerArc = this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled();
        float userInnerRadius = drawInnerArc ? radius * (this.mChart.getHoleRadius() / 100.0f) : 0.0f;
        RectF highlightedCircleBox = new RectF();
        for (int i = 0; i < indices.length; i++) {
            int xIndex = indices[i].getXIndex();
            if (xIndex < drawAngles.length && (set = ((PieData) this.mChart.getData()).getDataSetByIndex(indices[i].getDataSetIndex())) != null && set.isHighlightEnabled()) {
                int entryCount = set.getEntryCount();
                int visibleAngleCount = 0;
                for (int j = 0; j < entryCount; j++) {
                    if (((double) Math.abs(set.getEntryForIndex(j).getVal())) > 1.0E-6d) {
                        visibleAngleCount++;
                    }
                }
                if (xIndex == 0) {
                    angle = 0.0f;
                } else {
                    angle = absoluteAngles[xIndex - 1] * phaseX;
                }
                float sliceSpace = visibleAngleCount <= 1 ? 0.0f : set.getSliceSpace();
                float sliceAngle = drawAngles[xIndex];
                float innerRadius = userInnerRadius;
                float shift = set.getSelectionShift();
                float highlightedRadius = radius + shift;
                highlightedCircleBox.set(this.mChart.getCircleBox());
                highlightedCircleBox.inset(-shift, -shift);
                boolean accountForSliceSpacing = sliceSpace > 0.0f && sliceAngle <= 180.0f;
                this.mRenderPaint.setColor(set.getColor(xIndex));
                float sliceSpaceAngleOuter = visibleAngleCount == 1 ? 0.0f : sliceSpace / (0.017453292f * radius);
                if (visibleAngleCount == 1) {
                    sliceSpaceAngleShifted = 0.0f;
                } else {
                    sliceSpaceAngleShifted = sliceSpace / (0.017453292f * highlightedRadius);
                }
                float startAngleOuter = rotationAngle + (((sliceSpaceAngleOuter / 2.0f) + angle) * phaseY);
                float sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * phaseY;
                if (sweepAngleOuter < 0.0f) {
                    sweepAngleOuter = 0.0f;
                }
                float startAngleShifted = rotationAngle + (((sliceSpaceAngleShifted / 2.0f) + angle) * phaseY);
                float sweepAngleShifted = (sliceAngle - sliceSpaceAngleShifted) * phaseY;
                if (sweepAngleShifted < 0.0f) {
                    sweepAngleShifted = 0.0f;
                }
                this.mPathBuffer.reset();
                if (sweepAngleOuter % 360.0f == 0.0f) {
                    this.mPathBuffer.addCircle(center.x, center.y, highlightedRadius, Path.Direction.CW);
                } else {
                    this.mPathBuffer.moveTo(center.x + (((float) Math.cos((double) (0.017453292f * startAngleShifted))) * highlightedRadius), center.y + (((float) Math.sin((double) (0.017453292f * startAngleShifted))) * highlightedRadius));
                    this.mPathBuffer.arcTo(highlightedCircleBox, startAngleShifted, sweepAngleShifted);
                }
                float sliceSpaceRadius = 0.0f;
                if (accountForSliceSpacing) {
                    sliceSpaceRadius = calculateMinimumRadiusForSpacedSlice(center, radius, sliceAngle * phaseY, (((float) Math.cos((double) (0.017453292f * startAngleOuter))) * radius) + center.x, (((float) Math.sin((double) (0.017453292f * startAngleOuter))) * radius) + center.y, startAngleOuter, sweepAngleOuter);
                }
                this.mInnerRectBuffer.set(center.x - innerRadius, center.y - innerRadius, center.x + innerRadius, center.y + innerRadius);
                if (drawInnerArc && (innerRadius > 0.0f || accountForSliceSpacing)) {
                    if (accountForSliceSpacing) {
                        float minSpacedRadius = sliceSpaceRadius;
                        if (minSpacedRadius < 0.0f) {
                            minSpacedRadius = -minSpacedRadius;
                        }
                        innerRadius = Math.max(innerRadius, minSpacedRadius);
                    }
                    if (visibleAngleCount == 1 || innerRadius == 0.0f) {
                        sliceSpaceAngleInner = 0.0f;
                    } else {
                        sliceSpaceAngleInner = sliceSpace / (0.017453292f * innerRadius);
                    }
                    float startAngleInner = rotationAngle + (((sliceSpaceAngleInner / 2.0f) + angle) * phaseY);
                    float sweepAngleInner = (sliceAngle - sliceSpaceAngleInner) * phaseY;
                    if (sweepAngleInner < 0.0f) {
                        sweepAngleInner = 0.0f;
                    }
                    float endAngleInner = startAngleInner + sweepAngleInner;
                    if (sweepAngleOuter % 360.0f == 0.0f) {
                        this.mPathBuffer.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW);
                    } else {
                        this.mPathBuffer.lineTo(center.x + (((float) Math.cos((double) (0.017453292f * endAngleInner))) * innerRadius), center.y + (((float) Math.sin((double) (0.017453292f * endAngleInner))) * innerRadius));
                        this.mPathBuffer.arcTo(this.mInnerRectBuffer, endAngleInner, -sweepAngleInner);
                    }
                } else if (sweepAngleOuter % 360.0f != 0.0f) {
                    if (accountForSliceSpacing) {
                        float angleMiddle = startAngleOuter + (sweepAngleOuter / 2.0f);
                        this.mPathBuffer.lineTo(center.x + (((float) Math.cos((double) (0.017453292f * angleMiddle))) * sliceSpaceRadius), center.y + (((float) Math.sin((double) (0.017453292f * angleMiddle))) * sliceSpaceRadius));
                    } else {
                        this.mPathBuffer.lineTo(center.x, center.y);
                    }
                }
                this.mPathBuffer.close();
                this.mBitmapCanvas.drawPath(this.mPathBuffer, this.mRenderPaint);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawRoundedSlices(Canvas c) {
        if (this.mChart.isDrawRoundedSlicesEnabled()) {
            IPieDataSet dataSet = ((PieData) this.mChart.getData()).getDataSet();
            if (dataSet.isVisible()) {
                float phaseX = this.mAnimator.getPhaseX();
                float phaseY = this.mAnimator.getPhaseY();
                PointF center = this.mChart.getCenterCircleBox();
                float r = this.mChart.getRadius();
                float circleRadius = (r - ((this.mChart.getHoleRadius() * r) / 100.0f)) / 2.0f;
                float[] drawAngles = this.mChart.getDrawAngles();
                float angle = this.mChart.getRotationAngle();
                for (int j = 0; j < dataSet.getEntryCount(); j++) {
                    float sliceAngle = drawAngles[j];
                    if (((double) Math.abs(dataSet.getEntryForIndex(j).getVal())) > 1.0E-6d) {
                        float x = (float) ((((double) (r - circleRadius)) * Math.cos(Math.toRadians((double) ((angle + sliceAngle) * phaseY)))) + ((double) center.x));
                        float y = (float) ((((double) (r - circleRadius)) * Math.sin(Math.toRadians((double) ((angle + sliceAngle) * phaseY)))) + ((double) center.y));
                        this.mRenderPaint.setColor(dataSet.getColor(j));
                        this.mBitmapCanvas.drawCircle(x, y, circleRadius, this.mRenderPaint);
                    }
                    angle += sliceAngle * phaseX;
                }
            }
        }
    }

    public void releaseBitmap() {
        if (this.mDrawBitmap != null) {
            ((Bitmap) this.mDrawBitmap.get()).recycle();
            this.mDrawBitmap.clear();
            this.mDrawBitmap = null;
        }
    }
}
