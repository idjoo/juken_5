package com.github.anastr.speedviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

public class ImageSpeedometer extends Speedometer {
    private Drawable imageSpeedometer;

    public ImageSpeedometer(Context context) {
        this(context, (AttributeSet) null);
    }

    public ImageSpeedometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageSpeedometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void defaultSpeedometerValues() {
        setBackgroundCircleColor(0);
    }

    /* access modifiers changed from: protected */
    public void defaultGaugeValues() {
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageSpeedometer, 0, 0);
            this.imageSpeedometer = a.getDrawable(R.styleable.ImageSpeedometer_sv_image);
            a.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        updateBackgroundBitmap();
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
        if (this.imageSpeedometer != null) {
            this.imageSpeedometer.setBounds(((int) getViewLeft()) + getPadding(), ((int) getViewTop()) + getPadding(), ((int) getViewRight()) - getPadding(), ((int) getViewBottom()) - getPadding());
            this.imageSpeedometer.draw(c);
        }
        drawTicks(c);
    }

    public Drawable getImageSpeedometer() {
        return this.imageSpeedometer;
    }

    public void setImageSpeedometer(int imageResource) {
        if (Build.VERSION.SDK_INT >= 21) {
            setImageSpeedometer(getContext().getDrawable(imageResource));
        } else {
            setImageSpeedometer(getContext().getResources().getDrawable(imageResource));
        }
    }

    public void setImageSpeedometer(Drawable imageSpeedometer2) {
        this.imageSpeedometer = imageSpeedometer2;
        updateBackgroundBitmap();
    }

    public void setImageSpeedometer(Bitmap bitmapImage) {
        setImageSpeedometer((Drawable) new BitmapDrawable(getContext().getResources(), bitmapImage));
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

    @Deprecated
    public void setTextTypeface(Typeface typeface) {
    }

    @Deprecated
    public float getTextSize() {
        return 0.0f;
    }

    @Deprecated
    public void setTextSize(float textSize) {
    }

    @Deprecated
    public int getTextColor() {
        return 0;
    }

    @Deprecated
    public void setTextColor(int textColor) {
    }
}
