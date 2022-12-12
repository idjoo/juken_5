package com.github.mikephil.charting.jobs;

import android.animation.ValueAnimator;
import android.view.View;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class AnimatedMoveViewJob extends AnimatedViewPortJob {
    public AnimatedMoveViewJob(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer trans, View v, float xOrigin, float yOrigin, long duration) {
        super(viewPortHandler, xValue, yValue, trans, v, xOrigin, yOrigin, duration);
    }

    public void onAnimationUpdate(ValueAnimator animation) {
        this.pts[0] = this.xOrigin + ((this.xValue - this.xOrigin) * this.phase);
        this.pts[1] = this.yOrigin + ((this.yValue - this.yOrigin) * this.phase);
        this.mTrans.pointValuesToPixel(this.pts);
        this.mViewPortHandler.centerViewPort(this.pts, this.view);
    }
}
