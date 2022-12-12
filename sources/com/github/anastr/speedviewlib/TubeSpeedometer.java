package com.github.anastr.speedviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;

public class TubeSpeedometer extends Speedometer {
    private RectF speedometerRect;
    private Paint tubeBacPaint;
    private Paint tubePaint;
    private boolean withEffects3D;

    public TubeSpeedometer(Context context) {
        this(context, (AttributeSet) null);
    }

    public TubeSpeedometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TubeSpeedometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.tubePaint = new Paint(1);
        this.tubeBacPaint = new Paint(1);
        this.speedometerRect = new RectF();
        this.withEffects3D = true;
        init();
        initAttributeSet(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void defaultGaugeValues() {
    }

    /* access modifiers changed from: protected */
    public void defaultSpeedometerValues() {
        super.setBackgroundCircleColor(0);
        super.setLowSpeedColor(-16728876);
        super.setMediumSpeedColor(-16121);
        super.setHighSpeedColor(-769226);
        super.setSpeedometerWidth(dpTOpx(40.0f));
    }

    private void init() {
        this.tubePaint.setStyle(Paint.Style.STROKE);
        this.tubeBacPaint.setStyle(Paint.Style.STROKE);
        this.tubeBacPaint.setColor(-9079435);
        this.tubePaint.setColor(getLowSpeedColor());
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(1, (Paint) null);
        }
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TubeSpeedometer, 0, 0);
            this.tubeBacPaint.setColor(a.getColor(R.styleable.TubeSpeedometer_sv_speedometerBackColor, this.tubeBacPaint.getColor()));
            this.withEffects3D = a.getBoolean(R.styleable.TubeSpeedometer_sv_withEffects3D, this.withEffects3D);
            a.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        updateEmboss();
        updateBackgroundBitmap();
    }

    private void updateEmboss() {
        if (!isInEditMode()) {
            if (!this.withEffects3D) {
                this.tubePaint.setMaskFilter((MaskFilter) null);
                this.tubeBacPaint.setMaskFilter((MaskFilter) null);
                return;
            }
            this.tubePaint.setMaskFilter(new EmbossMaskFilter(new float[]{0.5f, 1.0f, 1.0f}, 0.6f, 3.0f, pxTOdp(getSpeedometerWidth()) * 0.35f));
            this.tubeBacPaint.setMaskFilter(new EmbossMaskFilter(new float[]{-0.5f, -1.0f, 0.0f}, 0.6f, 1.0f, pxTOdp(getSpeedometerWidth()) * 0.35f));
        }
    }

    private void initDraw() {
        this.tubePaint.setStrokeWidth(getSpeedometerWidth());
        byte section = getSection();
        if (section == 1) {
            this.tubePaint.setColor(getLowSpeedColor());
        } else if (section == 2) {
            this.tubePaint.setColor(getMediumSpeedColor());
        } else {
            this.tubePaint.setColor(getHighSpeedColor());
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initDraw();
        Canvas canvas2 = canvas;
        canvas2.drawArc(this.speedometerRect, (float) getStartDegree(), ((float) (getEndDegree() - getStartDegree())) * getOffsetSpeed(), false, this.tubePaint);
        drawSpeedUnitText(canvas);
        drawIndicator(canvas);
        drawNotes(canvas);
    }

    /* access modifiers changed from: protected */
    public void updateBackgroundBitmap() {
        Canvas c = createBackgroundBitmapCanvas();
        this.tubeBacPaint.setStrokeWidth(getSpeedometerWidth());
        float risk = (getSpeedometerWidth() * 0.5f) + ((float) getPadding());
        this.speedometerRect.set(risk, risk, ((float) getSize()) - risk, ((float) getSize()) - risk);
        c.drawArc(this.speedometerRect, (float) getStartDegree(), (float) (getEndDegree() - getStartDegree()), false, this.tubeBacPaint);
        if (getTickNumber() > 0) {
            drawTicks(c);
        } else {
            drawDefMinMaxSpeedPosition(c);
        }
    }

    public int getSpeedometerBackColor() {
        return this.tubeBacPaint.getColor();
    }

    public void setSpeedometerBackColor(int speedometerBackColor) {
        this.tubeBacPaint.setColor(speedometerBackColor);
        updateBackgroundBitmap();
        invalidate();
    }

    public void setLowSpeedColor(int lowSpeedColor) {
        super.setLowSpeedColor(lowSpeedColor);
    }

    public boolean isWithEffects3D() {
        return this.withEffects3D;
    }

    public void setWithEffects3D(boolean withEffects3D2) {
        this.withEffects3D = withEffects3D2;
        updateEmboss();
        updateBackgroundBitmap();
        invalidate();
    }
}
