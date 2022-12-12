package com.github.anastr.speedviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.github.anastr.speedviewlib.Gauge;

public class ImageLinearGauge extends LinearGauge {
    private int backColor;
    private Drawable image;

    public ImageLinearGauge(Context context) {
        this(context, (AttributeSet) null);
    }

    public ImageLinearGauge(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageLinearGauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.backColor = -2697257;
        initAttributeSet(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void defaultGaugeValues() {
        super.setSpeedTextPosition(Gauge.Position.CENTER);
        super.setUnitUnderSpeedText(true);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageLinearGauge, 0, 0);
            this.backColor = a.getColor(R.styleable.ImageLinearGauge_sv_speedometerBackColor, this.backColor);
            this.image = a.getDrawable(R.styleable.ImageLinearGauge_sv_image);
            a.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.image != null && this.image.getIntrinsicWidth() != -1 && this.image.getIntrinsicHeight() != -1) {
            int w = getMeasuredWidth();
            int h = getMeasuredHeight();
            float imageW = (float) this.image.getIntrinsicWidth();
            float imageH = (float) this.image.getIntrinsicHeight();
            if (imageW / imageH > ((float) (w / h))) {
                setMeasuredDimension(w, (int) ((((float) w) * imageH) / imageW));
            } else {
                setMeasuredDimension((int) ((((float) h) * imageW) / imageH), h);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void updateFrontAndBackBitmaps() {
        Canvas canvasBack = createBackgroundBitmapCanvas();
        Canvas canvasFront = createForegroundBitmapCanvas();
        if (this.image != null) {
            this.image.setBounds(getPadding(), getPadding(), getWidth() - getPadding(), getHeight() - getPadding());
            this.image.setColorFilter(this.backColor, PorterDuff.Mode.SRC_IN);
            this.image.draw(canvasBack);
            this.image.setColorFilter((ColorFilter) null);
            this.image.draw(canvasFront);
        }
    }
}
