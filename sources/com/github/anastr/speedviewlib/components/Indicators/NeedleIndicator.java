package com.github.anastr.speedviewlib.components.Indicators;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class NeedleIndicator extends Indicator {
    private float bottomY;
    private Paint circlePaint = new Paint(1);
    private Path circlePath = new Path();
    private Path indicatorPath = new Path();

    public NeedleIndicator(Context context) {
        super(context);
        this.circlePaint.setStyle(Paint.Style.STROKE);
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
        canvas.drawPath(this.circlePath, this.circlePaint);
        canvas.restore();
    }

    /* access modifiers changed from: protected */
    public void updateIndicator() {
        this.indicatorPath.reset();
        this.circlePath.reset();
        this.indicatorPath.moveTo(getCenterX(), (float) getPadding());
        this.bottomY = ((float) (((double) getIndicatorWidth()) * Math.sin(Math.toRadians(260.0d)))) + (getViewSize() * 0.5f) + ((float) getPadding());
        this.indicatorPath.lineTo(((float) (((double) getIndicatorWidth()) * Math.cos(Math.toRadians(260.0d)))) + (getViewSize() * 0.5f) + ((float) getPadding()), this.bottomY);
        this.indicatorPath.arcTo(new RectF(getCenterX() - getIndicatorWidth(), getCenterY() - getIndicatorWidth(), getCenterX() + getIndicatorWidth(), getCenterY() + getIndicatorWidth()), 260.0f, 20.0f);
        float circleWidth = getIndicatorWidth() * 0.25f;
        this.circlePath.addCircle(getCenterX(), getCenterY(), (getIndicatorWidth() - (circleWidth * 0.5f)) + 0.6f, Path.Direction.CW);
        this.indicatorPaint.setColor(getIndicatorColor());
        this.circlePaint.setColor(getIndicatorColor());
        this.circlePaint.setStrokeWidth(circleWidth);
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
