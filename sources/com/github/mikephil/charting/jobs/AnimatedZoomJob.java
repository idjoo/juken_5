package com.github.mikephil.charting.jobs;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.View;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

@SuppressLint({"NewApi"})
public class AnimatedZoomJob extends AnimatedViewPortJob implements Animator.AnimatorListener {
    protected float xValCount;
    protected YAxis yAxis;
    protected float zoomCenterX;
    protected float zoomCenterY;
    protected float zoomOriginX;
    protected float zoomOriginY;

    @SuppressLint({"NewApi"})
    public AnimatedZoomJob(ViewPortHandler viewPortHandler, View v, Transformer trans, YAxis axis, float xValCount2, float scaleX, float scaleY, float xOrigin, float yOrigin, float zoomCenterX2, float zoomCenterY2, float zoomOriginX2, float zoomOriginY2, long duration) {
        super(viewPortHandler, scaleX, scaleY, trans, v, xOrigin, yOrigin, duration);
        this.zoomCenterX = zoomCenterX2;
        this.zoomCenterY = zoomCenterY2;
        this.zoomOriginX = zoomOriginX2;
        this.zoomOriginY = zoomOriginY2;
        this.animator.addListener(this);
        this.yAxis = axis;
        this.xValCount = xValCount2;
    }

    public void onAnimationUpdate(ValueAnimator animation) {
        this.mViewPortHandler.refresh(this.mViewPortHandler.setZoom(this.xOrigin + ((this.xValue - this.xOrigin) * this.phase), this.yOrigin + ((this.yValue - this.yOrigin) * this.phase)), this.view, false);
        float valsInView = this.yAxis.mAxisRange / this.mViewPortHandler.getScaleY();
        this.pts[0] = this.zoomOriginX + (((this.zoomCenterX - ((this.xValCount / this.mViewPortHandler.getScaleX()) / 2.0f)) - this.zoomOriginX) * this.phase);
        this.pts[1] = this.zoomOriginY + (((this.zoomCenterY + (valsInView / 2.0f)) - this.zoomOriginY) * this.phase);
        this.mTrans.pointValuesToPixel(this.pts);
        this.mViewPortHandler.refresh(this.mViewPortHandler.translate(this.pts), this.view, true);
    }

    public void onAnimationEnd(Animator animation) {
        ((BarLineChartBase) this.view).calculateOffsets();
        this.view.postInvalidate();
    }

    public void onAnimationCancel(Animator animation) {
    }

    public void onAnimationRepeat(Animator animation) {
    }

    public void onAnimationStart(Animator animation) {
    }
}
