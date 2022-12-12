package com.github.anastr.speedviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

public abstract class LinearGauge extends Gauge {
    private Bitmap foregroundBitmap;
    private Orientation orientation;
    private Paint paint;
    private Rect rect;

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    /* access modifiers changed from: protected */
    public abstract void updateFrontAndBackBitmaps();

    public LinearGauge(Context context) {
        this(context, (AttributeSet) null);
    }

    public LinearGauge(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearGauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.paint = new Paint(1);
        this.rect = new Rect();
        this.orientation = Orientation.HORIZONTAL;
        initAttributeSet(context, attrs);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LinearGauge, 0, 0);
            int orientation2 = a.getInt(R.styleable.LinearGauge_sv_orientation, -1);
            if (orientation2 != -1) {
                setOrientation(Orientation.values()[orientation2]);
            }
            a.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        updateBackgroundBitmap();
    }

    /* access modifiers changed from: protected */
    public void updateBackgroundBitmap() {
        updateFrontAndBackBitmaps();
    }

    /* access modifiers changed from: protected */
    public final Canvas createForegroundBitmapCanvas() {
        if (getWidthPa() == 0 || getHeightPa() == 0) {
            return new Canvas();
        }
        this.foregroundBitmap = Bitmap.createBitmap(getWidthPa(), getHeightPa(), Bitmap.Config.ARGB_8888);
        return new Canvas(this.foregroundBitmap);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.orientation == Orientation.HORIZONTAL) {
            this.rect.set(0, 0, (int) (((float) getWidthPa()) * getOffsetSpeed()), getHeightPa());
        } else {
            this.rect.set(0, getHeightPa() - ((int) (((float) getHeightPa()) * getOffsetSpeed())), getWidthPa(), getHeightPa());
        }
        canvas.translate((float) getPadding(), (float) getPadding());
        canvas.drawBitmap(this.foregroundBitmap, this.rect, this.rect, this.paint);
        canvas.translate((float) (-getPadding()), (float) (-getPadding()));
        drawSpeedUnitText(canvas);
    }

    public Orientation getOrientation() {
        return this.orientation;
    }

    public void setOrientation(Orientation orientation2) {
        this.orientation = orientation2;
        if (isAttachedToWindow()) {
            requestLayout();
            updateBackgroundBitmap();
            invalidate();
        }
    }
}
