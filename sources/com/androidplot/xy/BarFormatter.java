package com.androidplot.xy;

import android.content.Context;
import android.graphics.Paint;
import com.androidplot.ui.SeriesRenderer;

public class BarFormatter extends LineAndPointFormatter {
    private Paint borderPaint;
    private Paint fillPaint;
    private float marginBottom;
    private float marginLeft;
    private float marginRight;
    private float marginTop;

    public Paint getFillPaint() {
        return this.fillPaint;
    }

    public void setFillPaint(Paint fillPaint2) {
        this.fillPaint = fillPaint2;
    }

    public Paint getBorderPaint() {
        return this.borderPaint;
    }

    public void setBorderPaint(Paint borderPaint2) {
        this.borderPaint = borderPaint2;
    }

    public BarFormatter() {
        this.fillPaint = new Paint();
        this.fillPaint.setStyle(Paint.Style.FILL);
        this.fillPaint.setAlpha(100);
        this.borderPaint = new Paint();
        this.borderPaint.setStyle(Paint.Style.STROKE);
        this.borderPaint.setAlpha(100);
    }

    public BarFormatter(int fillColor, int borderColor) {
        this();
        this.fillPaint.setColor(fillColor);
        this.borderPaint.setColor(borderColor);
    }

    public BarFormatter(Context context, int xmlCfgId) {
        this();
        configure(context, xmlCfgId);
    }

    public Class<? extends SeriesRenderer> getRendererClass() {
        return BarRenderer.class;
    }

    public SeriesRenderer doGetRendererInstance(XYPlot plot) {
        return new BarRenderer(plot);
    }

    public float getMarginTop() {
        return this.marginTop;
    }

    public void setMarginTop(float marginTop2) {
        this.marginTop = marginTop2;
    }

    public float getMarginBottom() {
        return this.marginBottom;
    }

    public void setMarginBottom(float marginBottom2) {
        this.marginBottom = marginBottom2;
    }

    public float getMarginLeft() {
        return this.marginLeft;
    }

    public void setMarginLeft(float marginLeft2) {
        this.marginLeft = marginLeft2;
    }

    public float getMarginRight() {
        return this.marginRight;
    }

    public void setMarginRight(float marginRight2) {
        this.marginRight = marginRight2;
    }
}
