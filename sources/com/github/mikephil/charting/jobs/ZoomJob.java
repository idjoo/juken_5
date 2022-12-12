package com.github.mikephil.charting.jobs;

import android.view.View;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class ZoomJob extends ViewPortJob {
    protected YAxis.AxisDependency axisDependency;
    protected float scaleX;
    protected float scaleY;

    public ZoomJob(ViewPortHandler viewPortHandler, float scaleX2, float scaleY2, float xValue, float yValue, Transformer trans, YAxis.AxisDependency axis, View v) {
        super(viewPortHandler, xValue, yValue, trans, v);
        this.scaleX = scaleX2;
        this.scaleY = scaleY2;
        this.axisDependency = axis;
    }

    public void run() {
        this.mViewPortHandler.refresh(this.mViewPortHandler.zoom(this.scaleX, this.scaleY), this.view, false);
        float valsInView = ((BarLineChartBase) this.view).getDeltaY(this.axisDependency) / this.mViewPortHandler.getScaleY();
        this.pts[0] = this.xValue - ((((float) ((BarLineChartBase) this.view).getXAxis().getValues().size()) / this.mViewPortHandler.getScaleX()) / 2.0f);
        this.pts[1] = this.yValue + (valsInView / 2.0f);
        this.mTrans.pointValuesToPixel(this.pts);
        this.mViewPortHandler.refresh(this.mViewPortHandler.translate(this.pts), this.view, false);
        ((BarLineChartBase) this.view).calculateOffsets();
        this.view.postInvalidate();
    }
}
