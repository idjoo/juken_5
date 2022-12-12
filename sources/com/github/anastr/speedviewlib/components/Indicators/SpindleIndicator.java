package com.github.anastr.speedviewlib.components.Indicators;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Path;

public class SpindleIndicator extends Indicator<SpindleIndicator> {
    private Path indicatorPath = new Path();

    public SpindleIndicator(Context context) {
        super(context);
        updateIndicator();
    }

    /* access modifiers changed from: protected */
    public float getDefaultIndicatorWidth() {
        return dpTOpx(16.0f);
    }

    public float getTop() {
        return (getViewSize() * 0.18f) + ((float) getPadding());
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
        this.indicatorPath.moveTo(getCenterX(), getCenterY());
        this.indicatorPath.quadTo(getCenterX() - getIndicatorWidth(), (getViewSize() * 0.34f) + ((float) getPadding()), getCenterX(), (getViewSize() * 0.18f) + ((float) getPadding()));
        this.indicatorPath.quadTo(getCenterX() + getIndicatorWidth(), (getViewSize() * 0.34f) + ((float) getPadding()), getCenterX(), getCenterY());
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
