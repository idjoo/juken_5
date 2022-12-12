package com.github.mikephil.charting.jobs;

import android.view.View;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class ViewPortJob implements Runnable {
    protected Transformer mTrans;
    protected ViewPortHandler mViewPortHandler;
    protected float[] pts = new float[2];
    protected View view;
    protected float xValue = 0.0f;
    protected float yValue = 0.0f;

    public ViewPortJob(ViewPortHandler viewPortHandler, float xValue2, float yValue2, Transformer trans, View v) {
        this.mViewPortHandler = viewPortHandler;
        this.xValue = xValue2;
        this.yValue = yValue2;
        this.mTrans = trans;
        this.view = v;
    }

    public float getXValue() {
        return this.xValue;
    }

    public float getYValue() {
        return this.yValue;
    }
}
