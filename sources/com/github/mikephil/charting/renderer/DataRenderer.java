package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class DataRenderer extends Renderer {
    protected ChartAnimator mAnimator;
    protected Paint mDrawPaint;
    protected Paint mHighlightPaint;
    protected Paint mRenderPaint = new Paint(1);
    protected Paint mValuePaint;

    public abstract void drawData(Canvas canvas);

    public abstract void drawExtras(Canvas canvas);

    public abstract void drawHighlighted(Canvas canvas, Highlight[] highlightArr);

    public abstract void drawValues(Canvas canvas);

    public abstract void initBuffers();

    public DataRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(viewPortHandler);
        this.mAnimator = animator;
        this.mRenderPaint.setStyle(Paint.Style.FILL);
        this.mDrawPaint = new Paint(4);
        this.mValuePaint = new Paint(1);
        this.mValuePaint.setColor(Color.rgb(63, 63, 63));
        this.mValuePaint.setTextAlign(Paint.Align.CENTER);
        this.mValuePaint.setTextSize(Utils.convertDpToPixel(9.0f));
        this.mHighlightPaint = new Paint(1);
        this.mHighlightPaint.setStyle(Paint.Style.STROKE);
        this.mHighlightPaint.setStrokeWidth(2.0f);
        this.mHighlightPaint.setColor(Color.rgb(255, 187, 115));
    }

    public Paint getPaintValues() {
        return this.mValuePaint;
    }

    public Paint getPaintHighlight() {
        return this.mHighlightPaint;
    }

    public Paint getPaintRender() {
        return this.mRenderPaint;
    }

    /* access modifiers changed from: protected */
    public void applyValueTextStyle(IDataSet set) {
        this.mValuePaint.setTypeface(set.getValueTypeface());
        this.mValuePaint.setTextSize(set.getValueTextSize());
    }

    public void drawValue(Canvas c, ValueFormatter formatter, float value, Entry entry, int dataSetIndex, float x, float y, int color) {
        this.mValuePaint.setColor(color);
        c.drawText(formatter.getFormattedValue(value, entry, dataSetIndex, this.mViewPortHandler), x, y, this.mValuePaint);
    }
}
