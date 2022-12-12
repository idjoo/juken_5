package com.github.anastr.speedviewlib.components.Indicators;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.MaskFilter;
import android.graphics.Path;
import android.graphics.Shader;

public class TriangleIndicator extends Indicator<TriangleIndicator> {
    private Path indicatorPath = new Path();
    private float indicatorTop = 0.0f;

    public TriangleIndicator(Context context) {
        super(context);
        updateIndicator();
    }

    /* access modifiers changed from: protected */
    public float getDefaultIndicatorWidth() {
        return dpTOpx(25.0f);
    }

    public float getTop() {
        return this.indicatorTop;
    }

    public float getBottom() {
        return this.indicatorTop + getIndicatorWidth();
    }

    public void draw(Canvas canvas, float degree) {
        canvas.save();
        canvas.rotate(90.0f + degree, getCenterX(), getCenterY());
        canvas.drawPath(this.indicatorPath, this.indicatorPaint);
        canvas.restore();
    }

    /* access modifiers changed from: protected */
    public void updateIndicator() {
        this.indicatorPath = new Path();
        this.indicatorTop = ((float) getPadding()) + getSpeedometerWidth() + dpTOpx(5.0f);
        this.indicatorPath.moveTo(getCenterX(), this.indicatorTop);
        this.indicatorPath.lineTo(getCenterX() - getIndicatorWidth(), this.indicatorTop + getIndicatorWidth());
        this.indicatorPath.lineTo(getCenterX() + getIndicatorWidth(), this.indicatorTop + getIndicatorWidth());
        this.indicatorPath.moveTo(0.0f, 0.0f);
        this.indicatorPaint.setShader(new LinearGradient(getCenterX(), this.indicatorTop, getCenterX(), this.indicatorTop + getIndicatorWidth(), getIndicatorColor(), Color.argb(0, Color.red(getIndicatorColor()), Color.green(getIndicatorColor()), Color.blue(getIndicatorColor())), Shader.TileMode.CLAMP));
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
