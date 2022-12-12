package com.github.anastr.speedviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import com.github.anastr.speedviewlib.Gauge;
import com.github.anastr.speedviewlib.components.Indicators.TriangleIndicator;

public class AwesomeSpeedometer extends Speedometer {
    private Paint markPaint;
    private Path markPath;
    private Paint ringPaint;
    private int speedometerColor;
    private RectF speedometerRect;
    private Paint trianglesPaint;
    private Path trianglesPath;

    public AwesomeSpeedometer(Context context) {
        this(context, (AttributeSet) null);
    }

    public AwesomeSpeedometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AwesomeSpeedometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.markPath = new Path();
        this.trianglesPath = new Path();
        this.markPaint = new Paint(1);
        this.ringPaint = new Paint(1);
        this.trianglesPaint = new Paint(1);
        this.speedometerRect = new RectF();
        this.speedometerColor = -16718106;
        init();
        initAttributeSet(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void defaultGaugeValues() {
        super.setTextColor(-15776);
        super.setSpeedTextColor(-1);
        super.setUnitTextColor(-1);
        super.setTextTypeface(Typeface.create(Typeface.DEFAULT, 1));
        super.setSpeedTextPosition(Gauge.Position.CENTER);
        super.setUnitUnderSpeedText(true);
    }

    /* access modifiers changed from: protected */
    public void defaultSpeedometerValues() {
        super.setIndicator(((TriangleIndicator) new TriangleIndicator(getContext()).setIndicatorWidth(dpTOpx(25.0f))).setIndicatorColor(-16718106));
        super.setStartEndDegree(135, 455);
        super.setSpeedometerWidth(dpTOpx(60.0f));
        super.setBackgroundCircleColor(-14606047);
        super.setTickNumber(9);
        super.setTickPadding(0);
    }

    private void init() {
        this.markPaint.setStyle(Paint.Style.STROKE);
        this.textPaint.setTextAlign(Paint.Align.CENTER);
        this.ringPaint.setStyle(Paint.Style.STROKE);
        this.trianglesPaint.setColor(-13022805);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AwesomeSpeedometer, 0, 0);
            this.speedometerColor = a.getColor(R.styleable.AwesomeSpeedometer_sv_speedometerColor, this.speedometerColor);
            this.trianglesPaint.setColor(a.getColor(R.styleable.AwesomeSpeedometer_sv_trianglesColor, this.trianglesPaint.getColor()));
            a.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        updateGradient();
        updateBackgroundBitmap();
    }

    private void updateGradient() {
        float stop = ((((float) getSizePa()) * 0.5f) - getSpeedometerWidth()) / (((float) getSizePa()) * 0.5f);
        this.ringPaint.setShader(new RadialGradient(((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f, ((float) getSizePa()) * 0.5f, new int[]{getBackgroundCircleColor(), this.speedometerColor, getBackgroundCircleColor(), getBackgroundCircleColor(), this.speedometerColor, this.speedometerColor}, new float[]{stop, stop + ((1.0f - stop) * 0.1f), stop + ((1.0f - stop) * 0.36f), stop + ((1.0f - stop) * 0.64f), stop + ((1.0f - stop) * 0.9f), 1.0f}, Shader.TileMode.CLAMP));
    }

    private void initDraw() {
        this.ringPaint.setStrokeWidth(getSpeedometerWidth());
        this.markPaint.setColor(getMarkColor());
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSpeedUnitText(canvas);
        drawIndicator(canvas);
        drawNotes(canvas);
    }

    /* access modifiers changed from: protected */
    public void updateBackgroundBitmap() {
        Canvas c = createBackgroundBitmapCanvas();
        initDraw();
        float markH = ((float) getViewSizePa()) / 22.0f;
        this.markPath.reset();
        this.markPath.moveTo(((float) getSize()) * 0.5f, (float) getPadding());
        this.markPath.lineTo(((float) getSize()) * 0.5f, ((float) getPadding()) + markH);
        this.markPaint.setStrokeWidth(markH / 5.0f);
        setInitTickPadding(((float) getViewSizePa()) / 20.0f);
        this.trianglesPath.reset();
        this.trianglesPath.moveTo(((float) getSize()) * 0.5f, ((float) getPadding()) + (((float) getViewSizePa()) / 20.0f));
        float triangleWidth = ((float) getViewSize()) / 20.0f;
        this.trianglesPath.lineTo((((float) getSize()) * 0.5f) - (triangleWidth / 2.0f), (float) getPadding());
        this.trianglesPath.lineTo((((float) getSize()) * 0.5f) + (triangleWidth / 2.0f), (float) getPadding());
        float risk = (getSpeedometerWidth() * 0.5f) + ((float) getPadding());
        this.speedometerRect.set(risk, risk, ((float) getSize()) - risk, ((float) getSize()) - risk);
        c.drawArc(this.speedometerRect, 0.0f, 360.0f, false, this.ringPaint);
        drawMarks(c);
        drawTicks(c);
    }

    /* access modifiers changed from: protected */
    public void drawMarks(Canvas c) {
        for (int i = 0; i < getTickNumber(); i++) {
            float d = getDegreeAtSpeed(getTicks().get(i).floatValue()) + 90.0f;
            c.save();
            c.rotate(d, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
            c.drawPath(this.trianglesPath, this.trianglesPaint);
            if (i + 1 != getTickNumber()) {
                c.save();
                float eachDegree = (getDegreeAtSpeed(getTicks().get(i + 1).floatValue()) + 90.0f) - d;
                for (int j = 1; j < 10; j++) {
                    c.rotate(0.1f * eachDegree, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
                    if (j == 5) {
                        this.markPaint.setStrokeWidth((((float) getSize()) / 22.0f) / 5.0f);
                    } else {
                        this.markPaint.setStrokeWidth((((float) getSize()) / 22.0f) / 9.0f);
                    }
                    c.drawPath(this.markPath, this.markPaint);
                }
                c.restore();
            }
            c.restore();
        }
    }

    public void setSpeedometerWidth(float speedometerWidth) {
        super.setSpeedometerWidth(speedometerWidth);
        float risk = speedometerWidth * 0.5f;
        this.speedometerRect.set(risk, risk, ((float) getSize()) - risk, ((float) getSize()) - risk);
        updateGradient();
        updateBackgroundBitmap();
        invalidate();
    }

    public int getSpeedometerColor() {
        return this.speedometerColor;
    }

    public void setSpeedometerColor(int speedometerColor2) {
        this.speedometerColor = speedometerColor2;
        updateGradient();
        updateBackgroundBitmap();
        invalidate();
    }

    public int getTrianglesColor() {
        return this.trianglesPaint.getColor();
    }

    public void setTrianglesColor(int trianglesColor) {
        this.trianglesPaint.setColor(trianglesColor);
        updateBackgroundBitmap();
        invalidate();
    }

    @Deprecated
    public int getLowSpeedColor() {
        return 0;
    }

    @Deprecated
    public void setLowSpeedColor(int lowSpeedColor) {
    }

    @Deprecated
    public int getMediumSpeedColor() {
        return 0;
    }

    @Deprecated
    public void setMediumSpeedColor(int mediumSpeedColor) {
    }

    @Deprecated
    public int getHighSpeedColor() {
        return 0;
    }

    @Deprecated
    public void setHighSpeedColor(int highSpeedColor) {
    }
}
