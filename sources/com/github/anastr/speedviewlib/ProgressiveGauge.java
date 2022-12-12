package com.github.anastr.speedviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import com.github.anastr.speedviewlib.Gauge;
import com.github.anastr.speedviewlib.LinearGauge;

public class ProgressiveGauge extends LinearGauge {
    private Paint backPaint;
    private Paint frontPaint;
    private Path path;

    public ProgressiveGauge(Context context) {
        this(context, (AttributeSet) null);
    }

    public ProgressiveGauge(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressiveGauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.path = new Path();
        this.frontPaint = new Paint(1);
        this.backPaint = new Paint(1);
        init();
        initAttributeSet(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void defaultGaugeValues() {
        super.setSpeedTextPosition(Gauge.Position.CENTER);
        super.setUnitUnderSpeedText(true);
    }

    private void init() {
        this.frontPaint.setColor(-16711681);
        this.backPaint.setColor(-2697257);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LinearGauge, 0, 0);
            this.frontPaint.setColor(a.getColor(R.styleable.LinearGauge_sv_speedometerColor, this.frontPaint.getColor()));
            this.backPaint.setColor(a.getColor(R.styleable.LinearGauge_sv_speedometerBackColor, this.backPaint.getColor()));
            a.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        if (getOrientation() == LinearGauge.Orientation.HORIZONTAL) {
            if (h > w / 2) {
                setMeasuredDimension(w, w / 2);
            } else {
                setMeasuredDimension(h * 2, h);
            }
        } else if (w > h / 2) {
            setMeasuredDimension(h / 2, h);
        } else {
            setMeasuredDimension(w, w * 2);
        }
    }

    /* access modifiers changed from: protected */
    public void updateFrontAndBackBitmaps() {
        updateOrientation();
        Canvas canvasBack = createBackgroundBitmapCanvas();
        Canvas canvasFront = createForegroundBitmapCanvas();
        canvasBack.translate((float) getPadding(), (float) getPadding());
        canvasBack.drawPath(this.path, this.backPaint);
        canvasFront.drawPath(this.path, this.frontPaint);
    }

    private void updateOrientation() {
        if (getOrientation() == LinearGauge.Orientation.HORIZONTAL) {
            updateHorizontalPath();
        } else {
            updateVerticalPath();
        }
    }

    /* access modifiers changed from: protected */
    public void updateHorizontalPath() {
        this.path.reset();
        this.path.moveTo(0.0f, (float) getHeightPa());
        this.path.lineTo(0.0f, ((float) getHeightPa()) - (((float) getHeightPa()) * 0.1f));
        this.path.quadTo(((float) getWidthPa()) * 0.75f, ((float) getHeightPa()) * 0.75f, (float) getWidthPa(), 0.0f);
        this.path.lineTo((float) getWidthPa(), (float) getHeightPa());
        this.path.lineTo(0.0f, (float) getHeightPa());
    }

    /* access modifiers changed from: protected */
    public void updateVerticalPath() {
        this.path.reset();
        this.path.moveTo(0.0f, (float) getHeightPa());
        this.path.lineTo(((float) getWidthPa()) * 0.1f, (float) getHeightPa());
        this.path.quadTo(((float) getWidthPa()) * 0.25f, ((float) getHeightPa()) * 0.25f, (float) getWidthPa(), 0.0f);
        this.path.lineTo(0.0f, 0.0f);
        this.path.lineTo(0.0f, (float) getHeightPa());
    }

    public int getSpeedometerColor() {
        return this.frontPaint.getColor();
    }

    public void setSpeedometerColor(int speedometerColor) {
        this.frontPaint.setColor(speedometerColor);
        if (isAttachedToWindow()) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public int getSpeedometerBackColor() {
        return this.backPaint.getColor();
    }

    public void setSpeedometerBackColor(int speedometerBackColor) {
        this.backPaint.setColor(speedometerBackColor);
        if (isAttachedToWindow()) {
            updateBackgroundBitmap();
            invalidate();
        }
    }
}
