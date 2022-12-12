package com.androidplot.xy;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.internal.view.SupportMenu;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.util.PixelUtils;

public class LineAndPointFormatter extends XYSeriesFormatter<XYRegionFormatter> {
    private static final float DEFAULT_LINE_STROKE_WIDTH_DP = 1.5f;
    private static final float DEFAULT_VERTEX_STROKE_WIDTH_DP = 4.5f;
    protected FillDirection fillDirection;
    protected Paint fillPaint;
    protected InterpolationParams interpolationParams;
    protected Paint linePaint;
    protected Paint vertexPaint;

    public FillDirection getFillDirection() {
        return this.fillDirection;
    }

    public void setFillDirection(FillDirection fillDirection2) {
        this.fillDirection = fillDirection2;
    }

    public LineAndPointFormatter(Context context, int xmlCfgId) {
        super(context, xmlCfgId);
        this.fillDirection = FillDirection.BOTTOM;
    }

    public LineAndPointFormatter() {
        this(Integer.valueOf(SupportMenu.CATEGORY_MASK), -16711936, -16776961, (PointLabelFormatter) null);
    }

    public LineAndPointFormatter(Integer lineColor, Integer vertexColor, Integer fillColor, PointLabelFormatter plf) {
        this(lineColor, vertexColor, fillColor, plf, FillDirection.BOTTOM);
    }

    public LineAndPointFormatter(Integer lineColor, Integer vertexColor, Integer fillColor, PointLabelFormatter plf, FillDirection fillDir) {
        this.fillDirection = FillDirection.BOTTOM;
        initLinePaint(lineColor);
        initVertexPaint(vertexColor);
        initFillPaint(fillColor);
        setFillDirection(fillDir);
        setPointLabelFormatter(plf);
    }

    public Class<? extends SeriesRenderer> getRendererClass() {
        return LineAndPointRenderer.class;
    }

    public SeriesRenderer doGetRendererInstance(XYPlot plot) {
        return new LineAndPointRenderer(plot);
    }

    /* access modifiers changed from: protected */
    public void initLinePaint(Integer lineColor) {
        if (lineColor == null) {
            this.linePaint = null;
            return;
        }
        this.linePaint = new Paint();
        this.linePaint.setAntiAlias(true);
        this.linePaint.setStrokeWidth(PixelUtils.dpToPix(DEFAULT_LINE_STROKE_WIDTH_DP));
        this.linePaint.setColor(lineColor.intValue());
        this.linePaint.setStyle(Paint.Style.STROKE);
    }

    /* access modifiers changed from: protected */
    public void initVertexPaint(Integer vertexColor) {
        if (vertexColor == null) {
            this.vertexPaint = null;
            return;
        }
        this.vertexPaint = new Paint();
        this.vertexPaint.setAntiAlias(true);
        this.vertexPaint.setStrokeWidth(PixelUtils.dpToPix(DEFAULT_VERTEX_STROKE_WIDTH_DP));
        this.vertexPaint.setColor(vertexColor.intValue());
        this.vertexPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /* access modifiers changed from: protected */
    public void initFillPaint(Integer fillColor) {
        if (fillColor == null) {
            this.fillPaint = null;
            return;
        }
        this.fillPaint = new Paint();
        this.fillPaint.setAntiAlias(true);
        this.fillPaint.setColor(fillColor.intValue());
    }

    public boolean hasLinePaint() {
        return this.linePaint != null;
    }

    public Paint getLinePaint() {
        if (this.linePaint == null) {
            initLinePaint(0);
        }
        return this.linePaint;
    }

    public void setLinePaint(Paint linePaint2) {
        this.linePaint = linePaint2;
    }

    public boolean hasVertexPaint() {
        return this.vertexPaint != null;
    }

    public Paint getVertexPaint() {
        if (this.vertexPaint == null) {
            initVertexPaint(0);
        }
        return this.vertexPaint;
    }

    public void setVertexPaint(Paint vertexPaint2) {
        this.vertexPaint = vertexPaint2;
    }

    public boolean hasFillPaint() {
        return this.fillPaint != null;
    }

    public Paint getFillPaint() {
        if (this.fillPaint == null) {
            initFillPaint(0);
        }
        return this.fillPaint;
    }

    public void setFillPaint(Paint fillPaint2) {
        this.fillPaint = fillPaint2;
    }

    public InterpolationParams getInterpolationParams() {
        return this.interpolationParams;
    }

    public void setInterpolationParams(InterpolationParams params) {
        this.interpolationParams = params;
    }
}
