package com.androidplot.xy;

import android.content.Context;
import android.graphics.Paint;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.util.PixelUtils;

public class BubbleFormatter extends XYSeriesFormatter<XYRegionFormatter> {
    private static final int DEFAULT_FILL_COLOR = -256;
    private static final int DEFAULT_STROKE_COLOR = -16777216;
    private static final float DEFAULT_STROKE_PIX = 1.0f;
    private Paint fillPaint;
    private Paint strokePaint;

    public BubbleFormatter() {
        this.strokePaint = new Paint();
        this.strokePaint.setAntiAlias(true);
        this.strokePaint.setStrokeWidth(PixelUtils.dpToPix(1.0f));
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setColor(-16777216);
        this.fillPaint = new Paint();
        this.fillPaint.setAntiAlias(true);
        this.fillPaint.setColor(-256);
        setPointLabeler(new PointLabeler<BubbleSeries>() {
            public String getLabel(BubbleSeries series, int index) {
                return String.valueOf(series.getZ(index));
            }
        });
    }

    public BubbleFormatter(Context context, int xmlCfgId) {
        this();
        configure(context, xmlCfgId);
    }

    public BubbleFormatter(int fillColor, int strokeColor) {
        this.strokePaint = new Paint();
        this.strokePaint.setAntiAlias(true);
        this.strokePaint.setStrokeWidth(PixelUtils.dpToPix(1.0f));
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setColor(-16777216);
        this.fillPaint = new Paint();
        this.fillPaint.setAntiAlias(true);
        this.fillPaint.setColor(-256);
        setPointLabeler(new PointLabeler<BubbleSeries>() {
            public String getLabel(BubbleSeries series, int index) {
                return String.valueOf(series.getZ(index));
            }
        });
        this.fillPaint.setColor(fillColor);
        this.strokePaint.setColor(strokeColor);
    }

    public Class<? extends SeriesRenderer> getRendererClass() {
        return BubbleRenderer.class;
    }

    public BubbleRenderer doGetRendererInstance(XYPlot plot) {
        return new BubbleRenderer(plot);
    }

    public Paint getStrokePaint() {
        return this.strokePaint;
    }

    public void setStrokePaint(Paint strokePaint2) {
        this.strokePaint = strokePaint2;
    }

    public Paint getFillPaint() {
        return this.fillPaint;
    }

    public void setFillPaint(Paint fillPaint2) {
        this.fillPaint = fillPaint2;
    }
}
