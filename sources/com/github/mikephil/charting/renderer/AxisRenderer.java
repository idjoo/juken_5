package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class AxisRenderer extends Renderer {
    protected Paint mAxisLabelPaint = new Paint(1);
    protected Paint mAxisLinePaint;
    protected Paint mGridPaint = new Paint();
    protected Paint mLimitLinePaint;
    protected Transformer mTrans;

    public abstract void renderAxisLabels(Canvas canvas);

    public abstract void renderAxisLine(Canvas canvas);

    public abstract void renderGridLines(Canvas canvas);

    public abstract void renderLimitLines(Canvas canvas);

    public AxisRenderer(ViewPortHandler viewPortHandler, Transformer trans) {
        super(viewPortHandler);
        this.mTrans = trans;
        this.mGridPaint.setColor(-7829368);
        this.mGridPaint.setStrokeWidth(1.0f);
        this.mGridPaint.setStyle(Paint.Style.STROKE);
        this.mGridPaint.setAlpha(90);
        this.mAxisLinePaint = new Paint();
        this.mAxisLinePaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mAxisLinePaint.setStrokeWidth(1.0f);
        this.mAxisLinePaint.setStyle(Paint.Style.STROKE);
        this.mLimitLinePaint = new Paint(1);
        this.mLimitLinePaint.setStyle(Paint.Style.STROKE);
    }

    public Paint getPaintAxisLabels() {
        return this.mAxisLabelPaint;
    }

    public Paint getPaintGrid() {
        return this.mGridPaint;
    }

    public Paint getPaintAxisLine() {
        return this.mAxisLinePaint;
    }

    public Transformer getTransformer() {
        return this.mTrans;
    }
}
