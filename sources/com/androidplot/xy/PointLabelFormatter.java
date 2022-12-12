package com.androidplot.xy;

import android.graphics.Paint;
import com.androidplot.util.PixelUtils;

public class PointLabelFormatter {
    private static final float DEFAULT_H_OFFSET_DP = 0.0f;
    private static final float DEFAULT_TEXT_SIZE_SP = 12.0f;
    private static final float DEFAULT_V_OFFSET_DP = -4.0f;
    public float hOffset;
    private Paint textPaint;
    public float vOffset;

    public PointLabelFormatter() {
        this(-1);
    }

    public PointLabelFormatter(int textColor) {
        this(textColor, PixelUtils.dpToPix(0.0f), PixelUtils.dpToPix(DEFAULT_V_OFFSET_DP));
    }

    public PointLabelFormatter(int textColor, float hOffset2, float vOffset2) {
        initTextPaint(Integer.valueOf(textColor));
        this.hOffset = hOffset2;
        this.vOffset = vOffset2;
    }

    public boolean hasTextPaint() {
        return this.textPaint != null;
    }

    public Paint getTextPaint() {
        if (this.textPaint == null) {
            initTextPaint(0);
        }
        return this.textPaint;
    }

    public void setTextPaint(Paint textPaint2) {
        this.textPaint = textPaint2;
    }

    /* access modifiers changed from: protected */
    public void initTextPaint(Integer textColor) {
        if (textColor == null) {
            setTextPaint((Paint) null);
            return;
        }
        setTextPaint(new Paint());
        getTextPaint().setAntiAlias(true);
        getTextPaint().setColor(textColor.intValue());
        getTextPaint().setTextAlign(Paint.Align.CENTER);
        getTextPaint().setTextSize(PixelUtils.spToPix(DEFAULT_TEXT_SIZE_SP));
    }
}
