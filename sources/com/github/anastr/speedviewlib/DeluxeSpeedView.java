package com.github.anastr.speedviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import com.github.anastr.speedviewlib.components.Indicators.Indicator;
import com.github.anastr.speedviewlib.components.Indicators.NormalSmallIndicator;

public class DeluxeSpeedView extends Speedometer {
    private Paint circlePaint;
    private Paint markPaint;
    private Path markPath;
    private Paint smallMarkPaint;
    private Path smallMarkPath;
    private Paint speedBackgroundPaint;
    private Paint speedometerPaint;
    private RectF speedometerRect;
    private boolean withEffects;

    public DeluxeSpeedView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DeluxeSpeedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeluxeSpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.markPath = new Path();
        this.smallMarkPath = new Path();
        this.circlePaint = new Paint(1);
        this.speedometerPaint = new Paint(1);
        this.markPaint = new Paint(1);
        this.smallMarkPaint = new Paint(1);
        this.speedBackgroundPaint = new Paint(1);
        this.speedometerRect = new RectF();
        this.withEffects = true;
        init();
        initAttributeSet(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void defaultGaugeValues() {
        super.setTextColor(-1);
    }

    /* access modifiers changed from: protected */
    public void defaultSpeedometerValues() {
        super.setIndicator(new NormalSmallIndicator(getContext()).setIndicatorColor(-16711700));
        super.setBackgroundCircleColor(-14606047);
        super.setLowSpeedColor(-13138129);
        super.setMediumSpeedColor(-6061516);
        super.setHighSpeedColor(-6610912);
    }

    private void init() {
        this.speedometerPaint.setStyle(Paint.Style.STROKE);
        this.markPaint.setStyle(Paint.Style.STROKE);
        this.smallMarkPaint.setStyle(Paint.Style.STROKE);
        this.speedBackgroundPaint.setColor(-1);
        this.circlePaint.setColor(-2039584);
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(1, (Paint) null);
        }
        setWithEffects(this.withEffects);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs == null) {
            initAttributeValue();
            return;
        }
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DeluxeSpeedView, 0, 0);
        this.speedBackgroundPaint.setColor(a.getColor(R.styleable.DeluxeSpeedView_sv_speedBackgroundColor, this.speedBackgroundPaint.getColor()));
        this.withEffects = a.getBoolean(R.styleable.DeluxeSpeedView_sv_withEffects, this.withEffects);
        this.circlePaint.setColor(a.getColor(R.styleable.DeluxeSpeedView_sv_centerCircleColor, this.circlePaint.getColor()));
        a.recycle();
        setWithEffects(this.withEffects);
        initAttributeValue();
    }

    private void initAttributeValue() {
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        updateBackgroundBitmap();
    }

    private void initDraw() {
        this.speedometerPaint.setStrokeWidth(getSpeedometerWidth());
        this.markPaint.setColor(getMarkColor());
        this.smallMarkPaint.setColor(getMarkColor());
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF speedBackgroundRect = getSpeedUnitTextBounds();
        speedBackgroundRect.left -= 2.0f;
        speedBackgroundRect.right += 2.0f;
        speedBackgroundRect.bottom += 2.0f;
        canvas.drawRect(speedBackgroundRect, this.speedBackgroundPaint);
        drawSpeedUnitText(canvas);
        drawIndicator(canvas);
        canvas.drawCircle(((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f, ((float) getWidthPa()) / 12.0f, this.circlePaint);
        drawNotes(canvas);
    }

    /* access modifiers changed from: protected */
    public void updateBackgroundBitmap() {
        Canvas c = createBackgroundBitmapCanvas();
        initDraw();
        float smallMarkH = ((float) getViewSizePa()) / 20.0f;
        this.smallMarkPath.reset();
        this.smallMarkPath.moveTo(((float) getSize()) * 0.5f, getSpeedometerWidth() + ((float) getPadding()));
        this.smallMarkPath.lineTo(((float) getSize()) * 0.5f, getSpeedometerWidth() + ((float) getPadding()) + smallMarkH);
        this.smallMarkPaint.setStrokeWidth(3.0f);
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
        c.rotate(((float) getStartDegree()) + 90.0f, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
        float everyDegree = ((float) (getEndDegree() - getStartDegree())) * 0.111f;
        for (float i = (float) getStartDegree(); i < ((float) getEndDegree()) - (2.0f * everyDegree); i += everyDegree) {
            c.rotate(everyDegree, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
            c.drawPath(this.markPath, this.markPaint);
        }
        c.restore();
        c.save();
        c.rotate(((float) getStartDegree()) + 90.0f, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
        for (float i2 = (float) getStartDegree(); i2 < ((float) getEndDegree()) - 10.0f; i2 += 10.0f) {
            c.rotate(10.0f, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
            c.drawPath(this.smallMarkPath, this.smallMarkPaint);
        }
        c.restore();
        if (getTickNumber() > 0) {
            drawTicks(c);
        } else {
            drawDefMinMaxSpeedPosition(c);
        }
    }

    public boolean isWithEffects() {
        return this.withEffects;
    }

    public void setWithEffects(boolean withEffects2) {
        this.withEffects = withEffects2;
        if (!isInEditMode()) {
            indicatorEffects(withEffects2);
            if (withEffects2) {
                this.markPaint.setMaskFilter(new BlurMaskFilter(5.0f, BlurMaskFilter.Blur.SOLID));
                this.speedBackgroundPaint.setMaskFilter(new BlurMaskFilter(8.0f, BlurMaskFilter.Blur.SOLID));
                this.circlePaint.setMaskFilter(new BlurMaskFilter(10.0f, BlurMaskFilter.Blur.SOLID));
            } else {
                this.markPaint.setMaskFilter((MaskFilter) null);
                this.speedBackgroundPaint.setMaskFilter((MaskFilter) null);
                this.circlePaint.setMaskFilter((MaskFilter) null);
            }
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public void setIndicator(Indicator.Indicators indicator) {
        super.setIndicator(indicator);
        indicatorEffects(this.withEffects);
    }

    public int getSpeedBackgroundColor() {
        return this.speedBackgroundPaint.getColor();
    }

    public void setSpeedBackgroundColor(int speedBackgroundColor) {
        this.speedBackgroundPaint.setColor(speedBackgroundColor);
        updateBackgroundBitmap();
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
}
