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
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import com.github.anastr.speedviewlib.components.Indicators.Indicator;

public class RaySpeedometer extends Speedometer {
    private Paint activeMarkPaint;
    private int degreeBetweenMark;
    private Paint markPaint;
    private Path markPath;
    private Path ray1Path;
    private Path ray2Path;
    private Paint rayPaint;
    private Paint speedBackgroundPaint;
    private boolean withEffects;

    public RaySpeedometer(Context context) {
        this(context, (AttributeSet) null);
    }

    public RaySpeedometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RaySpeedometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.markPath = new Path();
        this.ray1Path = new Path();
        this.ray2Path = new Path();
        this.markPaint = new Paint(1);
        this.activeMarkPaint = new Paint(1);
        this.speedBackgroundPaint = new Paint(1);
        this.rayPaint = new Paint(1);
        this.withEffects = true;
        this.degreeBetweenMark = 5;
        init();
        initAttributeSet(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void defaultGaugeValues() {
        super.setTextColor(-1);
    }

    /* access modifiers changed from: protected */
    public void defaultSpeedometerValues() {
        super.setBackgroundCircleColor(-14606047);
        super.setMarkColor(ViewCompat.MEASURED_STATE_MASK);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RaySpeedometer, 0, 0);
            this.rayPaint.setColor(a.getColor(R.styleable.RaySpeedometer_sv_rayColor, this.rayPaint.getColor()));
            int degreeBetweenMark2 = a.getInt(R.styleable.RaySpeedometer_sv_degreeBetweenMark, this.degreeBetweenMark);
            float markWidth = a.getDimension(R.styleable.RaySpeedometer_sv_markWidth, this.markPaint.getStrokeWidth());
            this.markPaint.setStrokeWidth(markWidth);
            this.activeMarkPaint.setStrokeWidth(markWidth);
            this.speedBackgroundPaint.setColor(a.getColor(R.styleable.RaySpeedometer_sv_speedBackgroundColor, this.speedBackgroundPaint.getColor()));
            this.withEffects = a.getBoolean(R.styleable.RaySpeedometer_sv_withEffects, this.withEffects);
            a.recycle();
            setWithEffects(this.withEffects);
            if (degreeBetweenMark2 > 0 && degreeBetweenMark2 <= 20) {
                this.degreeBetweenMark = degreeBetweenMark2;
            }
        }
    }

    private void init() {
        this.markPaint.setStyle(Paint.Style.STROKE);
        this.markPaint.setStrokeWidth(dpTOpx(3.0f));
        this.activeMarkPaint.setStyle(Paint.Style.STROKE);
        this.activeMarkPaint.setStrokeWidth(dpTOpx(3.0f));
        this.rayPaint.setStyle(Paint.Style.STROKE);
        this.rayPaint.setStrokeWidth(dpTOpx(1.8f));
        this.rayPaint.setColor(-1);
        this.speedBackgroundPaint.setColor(-1);
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(1, (Paint) null);
        }
        setWithEffects(this.withEffects);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        updateMarkPath();
        updateBackgroundBitmap();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.rotate(((float) getStartDegree()) + 90.0f, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
        int i = getStartDegree();
        while (i < getEndDegree()) {
            if (getDegree() <= ((float) i)) {
                this.markPaint.setColor(getMarkColor());
                canvas.drawPath(this.markPath, this.markPaint);
                canvas.rotate((float) this.degreeBetweenMark, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
            } else {
                if (((float) i) > (((float) (getEndDegree() - getStartDegree())) * getMediumSpeedOffset()) + ((float) getStartDegree())) {
                    this.activeMarkPaint.setColor(getHighSpeedColor());
                } else if (((float) i) > (((float) (getEndDegree() - getStartDegree())) * getLowSpeedOffset()) + ((float) getStartDegree())) {
                    this.activeMarkPaint.setColor(getMediumSpeedColor());
                } else {
                    this.activeMarkPaint.setColor(getLowSpeedColor());
                }
                canvas.drawPath(this.markPath, this.activeMarkPaint);
                canvas.rotate((float) this.degreeBetweenMark, ((float) getSize()) * 0.5f, ((float) getSize()) / 2.0f);
            }
            i += this.degreeBetweenMark;
        }
        canvas.restore();
        RectF speedBackgroundRect = getSpeedUnitTextBounds();
        speedBackgroundRect.left -= 2.0f;
        speedBackgroundRect.right += 2.0f;
        speedBackgroundRect.bottom += 2.0f;
        canvas.drawRect(speedBackgroundRect, this.speedBackgroundPaint);
        drawSpeedUnitText(canvas);
        drawIndicator(canvas);
        drawNotes(canvas);
    }

    /* access modifiers changed from: protected */
    public void updateBackgroundBitmap() {
        Canvas c = createBackgroundBitmapCanvas();
        updateMarkPath();
        this.ray1Path.reset();
        this.ray1Path.moveTo(((float) getSize()) / 2.0f, ((float) getSize()) / 2.0f);
        this.ray1Path.lineTo(((float) getSize()) / 2.0f, (((float) getSizePa()) / 3.2f) + ((float) getPadding()));
        this.ray1Path.moveTo(((float) getSize()) / 2.0f, (((float) getSizePa()) / 3.2f) + ((float) getPadding()));
        this.ray1Path.lineTo(((float) getSize()) / 2.2f, (((float) getSizePa()) / 3.0f) + ((float) getPadding()));
        this.ray1Path.moveTo(((float) getSize()) / 2.2f, (((float) getSizePa()) / 3.0f) + ((float) getPadding()));
        this.ray1Path.lineTo(((float) getSize()) / 2.1f, (((float) getSizePa()) / 4.5f) + ((float) getPadding()));
        this.ray2Path.reset();
        this.ray2Path.moveTo(((float) getSize()) / 2.0f, ((float) getSize()) / 2.0f);
        this.ray2Path.lineTo(((float) getSize()) / 2.0f, (((float) getSizePa()) / 3.2f) + ((float) getPadding()));
        this.ray2Path.moveTo(((float) getSize()) / 2.0f, (((float) getSizePa()) / 3.2f) + ((float) getPadding()));
        this.ray2Path.lineTo(((float) getSize()) / 2.2f, (((float) getSizePa()) / 3.8f) + ((float) getPadding()));
        this.ray2Path.moveTo(((float) getSize()) / 2.0f, (((float) getSizePa()) / 3.2f) + ((float) getPadding()));
        this.ray2Path.lineTo(((float) getSize()) / 1.8f, (((float) getSizePa()) / 3.8f) + ((float) getPadding()));
        c.save();
        for (int i = 0; i < 6; i++) {
            c.rotate(58.0f, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
            if (i % 2 == 0) {
                c.drawPath(this.ray1Path, this.rayPaint);
            } else {
                c.drawPath(this.ray2Path, this.rayPaint);
            }
        }
        c.restore();
        if (getTickNumber() > 0) {
            drawTicks(c);
        } else {
            drawDefMinMaxSpeedPosition(c);
        }
    }

    private void updateMarkPath() {
        this.markPath.reset();
        this.markPath.moveTo(((float) getSize()) * 0.5f, (float) getPadding());
        this.markPath.lineTo(((float) getSize()) * 0.5f, getSpeedometerWidth() + ((float) getPadding()));
    }

    public boolean isWithEffects() {
        return this.withEffects;
    }

    public void setWithEffects(boolean withEffects2) {
        this.withEffects = withEffects2;
        if (!isInEditMode()) {
            indicatorEffects(withEffects2);
            if (withEffects2) {
                this.rayPaint.setMaskFilter(new BlurMaskFilter(3.0f, BlurMaskFilter.Blur.SOLID));
                this.activeMarkPaint.setMaskFilter(new BlurMaskFilter(5.0f, BlurMaskFilter.Blur.SOLID));
                this.speedBackgroundPaint.setMaskFilter(new BlurMaskFilter(8.0f, BlurMaskFilter.Blur.SOLID));
            } else {
                this.rayPaint.setMaskFilter((MaskFilter) null);
                this.activeMarkPaint.setMaskFilter((MaskFilter) null);
                this.speedBackgroundPaint.setMaskFilter((MaskFilter) null);
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

    public int getDegreeBetweenMark() {
        return this.degreeBetweenMark;
    }

    public void setDegreeBetweenMark(int degreeBetweenMark2) {
        if (degreeBetweenMark2 > 0 && degreeBetweenMark2 <= 20) {
            this.degreeBetweenMark = degreeBetweenMark2;
            invalidate();
        }
    }

    public float getMarkWidth() {
        return this.markPaint.getStrokeWidth();
    }

    public void setMarkWidth(float markWidth) {
        this.markPaint.setStrokeWidth(markWidth);
        this.activeMarkPaint.setStrokeWidth(markWidth);
        invalidate();
    }

    public int getRayColor() {
        return this.rayPaint.getColor();
    }

    public void setRayColor(int rayColor) {
        this.rayPaint.setColor(rayColor);
        updateBackgroundBitmap();
        invalidate();
    }

    @Deprecated
    public int getIndicatorColor() {
        return 0;
    }

    @Deprecated
    public void setIndicatorColor(int indicatorColor) {
    }
}
