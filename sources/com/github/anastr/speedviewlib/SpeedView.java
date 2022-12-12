package com.github.anastr.speedviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.github.anastr.speedviewlib.components.Indicators.Indicator;
import com.github.anastr.speedviewlib.components.Indicators.NormalIndicator;

public class SpeedView extends Speedometer {
    private Paint circlePaint;
    private Paint markPaint;
    private Path markPath;
    private Paint speedometerPaint;
    private RectF speedometerRect;

    public SpeedView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SpeedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.markPath = new Path();
        this.circlePaint = new Paint(1);
        this.speedometerPaint = new Paint(1);
        this.markPaint = new Paint(1);
        this.speedometerRect = new RectF();
        init();
        initAttributeSet(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void defaultGaugeValues() {
    }

    /* access modifiers changed from: protected */
    public void defaultSpeedometerValues() {
        super.setIndicator((Indicator) new NormalIndicator(getContext()));
        super.setBackgroundCircleColor(0);
    }

    private void init() {
        this.speedometerPaint.setStyle(Paint.Style.STROKE);
        this.markPaint.setStyle(Paint.Style.STROKE);
        this.circlePaint.setColor(-12303292);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SpeedView, 0, 0);
            this.circlePaint.setColor(a.getColor(R.styleable.SpeedView_sv_centerCircleColor, this.circlePaint.getColor()));
            a.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        updateBackgroundBitmap();
    }

    private void initDraw() {
        this.speedometerPaint.setStrokeWidth(getSpeedometerWidth());
        this.markPaint.setColor(getMarkColor());
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSpeedUnitText(canvas);
        drawIndicator(canvas);
        canvas.drawCircle(((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f, ((float) getWidthPa()) / 12.0f, this.circlePaint);
        drawNotes(canvas);
    }

    /* access modifiers changed from: protected */
    public void updateBackgroundBitmap() {
        Canvas c = createBackgroundBitmapCanvas();
        initDraw();
        float markH = ((float) getViewSizePa()) / 28.0f;
        this.markPath.reset();
        this.markPath.moveTo(((float) getSize()) * 0.5f, (float) getPadding());
        this.markPath.lineTo(((float) getSize()) * 0.5f, ((float) getPadding()) + markH);
        this.markPaint.setStrokeWidth(markH / 3.0f);
        float risk = (getSpeedometerWidth() * 0.5f) + ((float) getPadding());
        this.speedometerRect.set(risk, risk, ((float) getSize()) - risk, ((float) getSize()) - risk);
        this.speedometerPaint.setColor(getHighSpeedColor());
        c.drawArc(this.speedometerRect, (float) getStartDegree(), (float) (getEndDegree() - getStartDegree()), false, this.speedometerPaint);
        this.speedometerPaint.setColor(getMediumSpeedColor());
        c.drawArc(this.speedometerRect, (float) getStartDegree(), ((float) (getEndDegree() - getStartDegree())) * getMediumSpeedOffset(), false, this.speedometerPaint);
        this.speedometerPaint.setColor(getLowSpeedColor());
        c.drawArc(this.speedometerRect, (float) getStartDegree(), ((float) (getEndDegree() - getStartDegree())) * getLowSpeedOffset(), false, this.speedometerPaint);
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

    public int getCenterCircleColor() {
        return this.circlePaint.getColor();
    }

    public void setCenterCircleColor(int centerCircleColor) {
        this.circlePaint.setColor(centerCircleColor);
        if (isAttachedToWindow()) {
            invalidate();
        }
    }
}
