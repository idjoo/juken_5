package com.github.anastr.speedviewlib.components.Indicators;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;

public class LineIndicator extends Indicator<LineIndicator> {
    public static final float HALF_LINE = 0.5f;
    public static final float LINE = 1.0f;
    public static final float QUARTER_LINE = 0.25f;
    private Path indicatorPath = new Path();
    private float mode;

    public LineIndicator(Context context, float mode2) {
        super(context);
        this.mode = mode2;
        updateIndicator();
    }

    /* access modifiers changed from: protected */
    public float getDefaultIndicatorWidth() {
        return dpTOpx(8.0f);
    }

    public float getBottom() {
        return getCenterY() * this.mode;
    }

    public void draw(Canvas canvas, float degree) {
        canvas.save();
        canvas.rotate(90.0f + degree, getCenterX(), getCenterY());
        canvas.drawPath(this.indicatorPath, this.indicatorPaint);
        canvas.restore();
    }

    /* access modifiers changed from: protected */
    public void updateIndicator() {
        this.indicatorPath.reset();
        this.indicatorPath.moveTo(getCenterX(), (float) getPadding());
        this.indicatorPath.lineTo(getCenterX(), getCenterY() * this.mode);
        this.indicatorPaint.setStyle(Paint.Style.STROKE);
        this.indicatorPaint.setStrokeWidth(getIndicatorWidth());
        this.indicatorPaint.setColor(getIndicatorColor());
    }

    /* access modifiers changed from: protected */
    public void setWithEffects(boolean withEffects) {
        if (!withEffects || isInEditMode()) {
            this.indicatorPaint.setMaskFilter((MaskFilter) null);
        } else {
            this.indicatorPaint.setMaskFilter(new BlurMaskFilter(15.0f, BlurMaskFilter.Blur.SOLID));
        }
    }
}
