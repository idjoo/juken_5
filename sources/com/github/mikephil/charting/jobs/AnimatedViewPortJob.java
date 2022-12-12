package com.github.mikephil.charting.jobs;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.View;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

@SuppressLint({"NewApi"})
public abstract class AnimatedViewPortJob extends ViewPortJob implements ValueAnimator.AnimatorUpdateListener {
    protected ObjectAnimator animator = ObjectAnimator.ofFloat(this, "phase", new float[]{0.0f, 1.0f});
    protected float phase;
    protected float xOrigin;
    protected float yOrigin;

    public AnimatedViewPortJob(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer trans, View v, float xOrigin2, float yOrigin2, long duration) {
        super(viewPortHandler, xValue, yValue, trans, v);
        this.xOrigin = xOrigin2;
        this.yOrigin = yOrigin2;
        this.animator.setDuration(duration);
        this.animator.addUpdateListener(this);
    }

    @SuppressLint({"NewApi"})
    public void run() {
        this.animator.start();
    }

    public float getPhase() {
        return this.phase;
    }

    public void setPhase(float phase2) {
        this.phase = phase2;
    }

    public float getXOrigin() {
        return this.xOrigin;
    }

    public float getYOrigin() {
        return this.yOrigin;
    }
}
