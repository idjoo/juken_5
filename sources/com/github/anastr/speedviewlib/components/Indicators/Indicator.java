package com.github.anastr.speedviewlib.components.Indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.github.anastr.speedviewlib.Speedometer;
import com.github.anastr.speedviewlib.components.Indicators.Indicator;

public abstract class Indicator<I extends Indicator> {
    private float density;
    private boolean inEditMode;
    private int indicatorColor = -14575885;
    protected Paint indicatorPaint = new Paint(1);
    private float indicatorWidth;
    private int padding;
    private float speedometerWidth;
    private float viewSize;

    public enum Indicators {
        NoIndicator,
        NormalIndicator,
        NormalSmallIndicator,
        TriangleIndicator,
        SpindleIndicator,
        LineIndicator,
        HalfLineIndicator,
        QuarterLineIndicator,
        KiteIndicator,
        NeedleIndicator
    }

    public abstract void draw(Canvas canvas, float f);

    /* access modifiers changed from: protected */
    public abstract float getDefaultIndicatorWidth();

    /* access modifiers changed from: protected */
    public abstract void setWithEffects(boolean z);

    /* access modifiers changed from: protected */
    public abstract void updateIndicator();

    protected Indicator(Context context) {
        this.density = context.getResources().getDisplayMetrics().density;
        init();
    }

    private void init() {
        this.indicatorPaint.setColor(this.indicatorColor);
        this.indicatorWidth = getDefaultIndicatorWidth();
    }

    public float getTop() {
        return (float) getPadding();
    }

    public float getBottom() {
        return getCenterY();
    }

    public float getLightBottom() {
        return getCenterY() > getBottom() ? getBottom() : getCenterY();
    }

    public void onSizeChange(Speedometer speedometer) {
        setTargetSpeedometer(speedometer);
    }

    public void setTargetSpeedometer(Speedometer speedometer) {
        updateData(speedometer);
        updateIndicator();
    }

    private void updateData(Speedometer speedometer) {
        this.viewSize = (float) speedometer.getSize();
        this.speedometerWidth = speedometer.getSpeedometerWidth();
        this.padding = speedometer.getPadding();
        this.inEditMode = speedometer.isInEditMode();
    }

    public float dpTOpx(float dp) {
        return this.density * dp;
    }

    public float getIndicatorWidth() {
        return this.indicatorWidth;
    }

    public I setIndicatorWidth(float indicatorWidth2) {
        this.indicatorWidth = indicatorWidth2;
        return this;
    }

    public float getViewSize() {
        return this.viewSize - (((float) this.padding) * 2.0f);
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public I setIndicatorColor(int indicatorColor2) {
        this.indicatorColor = indicatorColor2;
        return this;
    }

    public float getCenterX() {
        return this.viewSize / 2.0f;
    }

    public float getCenterY() {
        return this.viewSize / 2.0f;
    }

    public int getPadding() {
        return this.padding;
    }

    public float getSpeedometerWidth() {
        return this.speedometerWidth;
    }

    public void noticeIndicatorWidthChange(float indicatorWidth2) {
        this.indicatorWidth = indicatorWidth2;
        updateIndicator();
    }

    public void noticeIndicatorColorChange(int indicatorColor2) {
        this.indicatorColor = indicatorColor2;
        updateIndicator();
    }

    public void noticeSpeedometerWidthChange(float speedometerWidth2) {
        this.speedometerWidth = speedometerWidth2;
        updateIndicator();
    }

    public void noticePaddingChange(int newPadding) {
        this.padding = newPadding;
        updateIndicator();
    }

    public void withEffects(boolean withEffects) {
        setWithEffects(withEffects);
        updateIndicator();
    }

    public boolean isInEditMode() {
        return this.inEditMode;
    }

    public static Indicator createIndicator(Context context, Indicators indicator) {
        switch (indicator) {
            case NoIndicator:
                return new NoIndicator(context);
            case NormalIndicator:
                return new NormalIndicator(context);
            case NormalSmallIndicator:
                return new NormalSmallIndicator(context);
            case TriangleIndicator:
                return new TriangleIndicator(context);
            case SpindleIndicator:
                return new SpindleIndicator(context);
            case LineIndicator:
                return new LineIndicator(context, 1.0f);
            case HalfLineIndicator:
                return new LineIndicator(context, 0.5f);
            case QuarterLineIndicator:
                return new LineIndicator(context, 0.25f);
            case KiteIndicator:
                return new KiteIndicator(context);
            case NeedleIndicator:
                return new NeedleIndicator(context);
            default:
                return new NormalIndicator(context);
        }
    }
}
