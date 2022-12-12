package com.github.anastr.speedviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import com.github.anastr.speedviewlib.components.Indicators.SpindleIndicator;

public class PointerSpeedometer extends Speedometer {
    private Paint circlePaint;
    private Paint markPaint;
    private Path markPath;
    private Paint pointerBackPaint;
    private int pointerColor;
    private Paint pointerPaint;
    private int speedometerColor;
    private Paint speedometerPaint;
    private RectF speedometerRect;
    private boolean withPointer;

    public PointerSpeedometer(Context context) {
        this(context, (AttributeSet) null);
    }

    public PointerSpeedometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointerSpeedometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.markPath = new Path();
        this.speedometerPaint = new Paint(1);
        this.pointerPaint = new Paint(1);
        this.pointerBackPaint = new Paint(1);
        this.circlePaint = new Paint(1);
        this.markPaint = new Paint(1);
        this.speedometerRect = new RectF();
        this.speedometerColor = -1118482;
        this.pointerColor = -1;
        this.withPointer = true;
        init();
        initAttributeSet(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void defaultGaugeValues() {
        super.setTextColor(-1);
        super.setSpeedTextColor(-1);
        super.setUnitTextColor(-1);
        super.setSpeedTextSize(dpTOpx(24.0f));
        super.setUnitTextSize(dpTOpx(11.0f));
        super.setSpeedTextTypeface(Typeface.create(Typeface.DEFAULT, 1));
    }

    /* access modifiers changed from: protected */
    public void defaultSpeedometerValues() {
        super.setIndicator(((SpindleIndicator) new SpindleIndicator(getContext()).setIndicatorWidth(dpTOpx(16.0f))).setIndicatorColor(-1));
        super.setBackgroundCircleColor(-12006167);
        super.setSpeedometerWidth(dpTOpx(10.0f));
    }

    private void init() {
        this.speedometerPaint.setStyle(Paint.Style.STROKE);
        this.speedometerPaint.setStrokeCap(Paint.Cap.ROUND);
        this.markPaint.setStyle(Paint.Style.STROKE);
        this.markPaint.setStrokeCap(Paint.Cap.ROUND);
        this.markPaint.setStrokeWidth(dpTOpx(2.0f));
        this.circlePaint.setColor(-1);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs == null) {
            initAttributeValue();
            return;
        }
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PointerSpeedometer, 0, 0);
        this.speedometerColor = a.getColor(R.styleable.PointerSpeedometer_sv_speedometerColor, this.speedometerColor);
        this.pointerColor = a.getColor(R.styleable.PointerSpeedometer_sv_pointerColor, this.pointerColor);
        this.circlePaint.setColor(a.getColor(R.styleable.PointerSpeedometer_sv_centerCircleColor, this.circlePaint.getColor()));
        this.withPointer = a.getBoolean(R.styleable.PointerSpeedometer_sv_withPointer, this.withPointer);
        a.recycle();
        initAttributeValue();
    }

    private void initAttributeValue() {
        this.pointerPaint.setColor(this.pointerColor);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        float risk = (getSpeedometerWidth() * 0.5f) + dpTOpx(8.0f) + ((float) getPadding());
        this.speedometerRect.set(risk, risk, ((float) getSize()) - risk, ((float) getSize()) - risk);
        updateRadial();
        updateBackgroundBitmap();
    }

    private void initDraw() {
        this.speedometerPaint.setStrokeWidth(getSpeedometerWidth());
        this.speedometerPaint.setShader(updateSweep());
        this.markPaint.setColor(getMarkColor());
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initDraw();
        canvas.drawArc(this.speedometerRect, (float) getStartDegree(), (float) (getEndDegree() - getStartDegree()), false, this.speedometerPaint);
        if (this.withPointer) {
            canvas.save();
            canvas.rotate(90.0f + getDegree(), ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
            canvas.drawCircle(((float) getSize()) * 0.5f, (getSpeedometerWidth() * 0.5f) + dpTOpx(8.0f) + ((float) getPadding()), (getSpeedometerWidth() * 0.5f) + dpTOpx(8.0f), this.pointerBackPaint);
            canvas.drawCircle(((float) getSize()) * 0.5f, (getSpeedometerWidth() * 0.5f) + dpTOpx(8.0f) + ((float) getPadding()), (getSpeedometerWidth() * 0.5f) + dpTOpx(1.0f), this.pointerPaint);
            canvas.restore();
        }
        drawSpeedUnitText(canvas);
        drawIndicator(canvas);
        int c = getCenterCircleColor();
        this.circlePaint.setColor(Color.argb((int) (((float) Color.alpha(c)) * 0.5f), Color.red(c), Color.green(c), Color.blue(c)));
        canvas.drawCircle(((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f, ((float) getWidthPa()) / 14.0f, this.circlePaint);
        this.circlePaint.setColor(c);
        canvas.drawCircle(((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f, ((float) getWidthPa()) / 22.0f, this.circlePaint);
        drawNotes(canvas);
    }

    /* access modifiers changed from: protected */
    public void updateBackgroundBitmap() {
        Canvas c = createBackgroundBitmapCanvas();
        initDraw();
        this.markPath.reset();
        this.markPath.moveTo(((float) getSize()) * 0.5f, getSpeedometerWidth() + dpTOpx(8.0f) + dpTOpx(4.0f) + ((float) getPadding()));
        this.markPath.lineTo(((float) getSize()) * 0.5f, getSpeedometerWidth() + dpTOpx(8.0f) + dpTOpx(4.0f) + ((float) getPadding()) + ((float) (getSize() / 60)));
        c.save();
        c.rotate(90.0f + ((float) getStartDegree()), ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
        float everyDegree = ((float) (getEndDegree() - getStartDegree())) * 0.111f;
        for (float i = (float) getStartDegree(); i < ((float) getEndDegree()) - (2.0f * everyDegree); i += everyDegree) {
            c.rotate(everyDegree, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
            c.drawPath(this.markPath, this.markPaint);
        }
        c.restore();
        if (getTickNumber() > 0) {
            drawTicks(c);
        } else {
            drawDefMinMaxSpeedPosition(c);
        }
    }

    private SweepGradient updateSweep() {
        int startColor = Color.argb(150, Color.red(this.speedometerColor), Color.green(this.speedometerColor), Color.blue(this.speedometerColor));
        int color2 = Color.argb(220, Color.red(this.speedometerColor), Color.green(this.speedometerColor), Color.blue(this.speedometerColor));
        int color3 = Color.argb(70, Color.red(this.speedometerColor), Color.green(this.speedometerColor), Color.blue(this.speedometerColor));
        int endColor = Color.argb(15, Color.red(this.speedometerColor), Color.green(this.speedometerColor), Color.blue(this.speedometerColor));
        float position = (getOffsetSpeed() * ((float) (getEndDegree() - getStartDegree()))) / 360.0f;
        SweepGradient sweepGradient = new SweepGradient(((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f, new int[]{startColor, color2, this.speedometerColor, color3, endColor, startColor}, new float[]{0.0f, 0.5f * position, position, position, 0.99f, 1.0f});
        Matrix matrix = new Matrix();
        matrix.postRotate((float) getStartDegree(), ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
        sweepGradient.setLocalMatrix(matrix);
        return sweepGradient;
    }

    private void updateRadial() {
        int centerColor = Color.argb(160, Color.red(this.pointerColor), Color.green(this.pointerColor), Color.blue(this.pointerColor));
        int edgeColor = Color.argb(10, Color.red(this.pointerColor), Color.green(this.pointerColor), Color.blue(this.pointerColor));
        this.pointerBackPaint.setShader(new RadialGradient(((float) getSize()) * 0.5f, (getSpeedometerWidth() * 0.5f) + dpTOpx(8.0f) + ((float) getPadding()), (getSpeedometerWidth() * 0.5f) + dpTOpx(8.0f), new int[]{centerColor, edgeColor}, new float[]{0.4f, 1.0f}, Shader.TileMode.CLAMP));
    }

    public int getSpeedometerColor() {
        return this.speedometerColor;
    }

    public void setSpeedometerColor(int speedometerColor2) {
        this.speedometerColor = speedometerColor2;
        invalidate();
    }

    public int getPointerColor() {
        return this.pointerColor;
    }

    public void setPointerColor(int pointerColor2) {
        this.pointerColor = pointerColor2;
        this.pointerPaint.setColor(pointerColor2);
        updateRadial();
        invalidate();
    }

    public int getCenterCircleColor() {
        return this.circlePaint.getColor();
    }

    public void setCenterCircleColor(int centerCircleColor) {
        this.circlePaint.setColor(centerCircleColor);
        if (isAttachedToWindow()) {
            invalidate();
        }
    }

    public boolean isWithPointer() {
        return this.withPointer;
    }

    public void setWithPointer(boolean withPointer2) {
        this.withPointer = withPointer2;
        if (isAttachedToWindow()) {
            invalidate();
        }
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
