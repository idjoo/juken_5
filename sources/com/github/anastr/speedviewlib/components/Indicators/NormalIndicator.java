package com.github.anastr.speedviewlib.components.Indicators;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Path;
import android.graphics.RectF;

public class NormalIndicator extends Indicator<NormalIndicator> {
    private float bottomY;
    private Path indicatorPath = new Path();

    public NormalIndicator(Context context) {
        super(context);
        updateIndicator();
    }

    /* access modifiers changed from: protected */
    public float getDefaultIndicatorWidth() {
        return dpTOpx(12.0f);
    }

    public float getBottom() {
        return this.bottomY;
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
        this.bottomY = ((getViewSize() * 2.0f) / 3.0f) + ((float) getPadding());
        this.indicatorPath.lineTo(getCenterX() - getIndicatorWidth(), this.bottomY);
        this.indicatorPath.lineTo(getCenterX() + getIndicatorWidth(), this.bottomY);
        this.indicatorPath.addArc(new RectF(getCenterX() - getIndicatorWidth(), this.bottomY - getIndicatorWidth(), getCenterX() + getIndicatorWidth(), this.bottomY + getIndicatorWidth()), 0.0f, 180.0f);
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
