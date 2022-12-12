package com.androidplot.pie;

import android.content.Context;
import android.graphics.Paint;
import com.androidplot.ui.Formatter;
import com.androidplot.ui.SeriesRenderer;

public class SegmentFormatter extends Formatter<PieChart> {
    private static final int DEFAULT_EDGE_COLOR = -16777216;
    private static final float DEFAULT_EDGE_THICKNESS = 3.0f;
    private static final int DEFAULT_FILL_COLOR = 0;
    private static final int DEFAULT_LABEL_COLOR = -1;
    private static final float DEFAULT_LABEL_FONT_SIZE = 18.0f;
    private static final float DEFAULT_LABEL_MARKER_THICKNESS = 3.0f;
    private Paint fillPaint;
    private Paint innerEdgePaint;
    private float innerInset;
    private Paint labelMarkerPaint;
    private Paint labelPaint;
    private float offset;
    private Paint outerEdgePaint;
    private float outerInset;
    private Paint radialEdgePaint;
    private float radialInset;

    public SegmentFormatter(Integer fillColor) {
        setFillPaint(new Paint());
        setOuterEdgePaint(new Paint());
        getOuterEdgePaint().setStyle(Paint.Style.STROKE);
        getOuterEdgePaint().setStrokeWidth(3.0f);
        getOuterEdgePaint().setAntiAlias(true);
        setInnerEdgePaint(new Paint());
        getInnerEdgePaint().setStyle(Paint.Style.STROKE);
        getInnerEdgePaint().setStrokeWidth(3.0f);
        getInnerEdgePaint().setAntiAlias(true);
        setRadialEdgePaint(new Paint());
        getRadialEdgePaint().setStyle(Paint.Style.STROKE);
        getRadialEdgePaint().setStrokeWidth(3.0f);
        getRadialEdgePaint().setAntiAlias(true);
        setLabelPaint(new Paint());
        getLabelPaint().setColor(-1);
        getLabelPaint().setTextSize(DEFAULT_LABEL_FONT_SIZE);
        getLabelPaint().setAntiAlias(true);
        getLabelPaint().setTextAlign(Paint.Align.CENTER);
        setLabelMarkerPaint(new Paint());
        getLabelMarkerPaint().setColor(-1);
        getLabelMarkerPaint().setStrokeWidth(3.0f);
        if (fillColor != null) {
            getFillPaint().setColor(fillColor.intValue());
        } else {
            getFillPaint().setColor(0);
        }
    }

    public SegmentFormatter(Context context, int xmlCfgId) {
        setFillPaint(new Paint());
        setOuterEdgePaint(new Paint());
        getOuterEdgePaint().setStyle(Paint.Style.STROKE);
        getOuterEdgePaint().setStrokeWidth(3.0f);
        getOuterEdgePaint().setAntiAlias(true);
        setInnerEdgePaint(new Paint());
        getInnerEdgePaint().setStyle(Paint.Style.STROKE);
        getInnerEdgePaint().setStrokeWidth(3.0f);
        getInnerEdgePaint().setAntiAlias(true);
        setRadialEdgePaint(new Paint());
        getRadialEdgePaint().setStyle(Paint.Style.STROKE);
        getRadialEdgePaint().setStrokeWidth(3.0f);
        getRadialEdgePaint().setAntiAlias(true);
        setLabelPaint(new Paint());
        getLabelPaint().setColor(-1);
        getLabelPaint().setTextSize(DEFAULT_LABEL_FONT_SIZE);
        getLabelPaint().setAntiAlias(true);
        getLabelPaint().setTextAlign(Paint.Align.CENTER);
        setLabelMarkerPaint(new Paint());
        getLabelMarkerPaint().setColor(-1);
        getLabelMarkerPaint().setStrokeWidth(3.0f);
        configure(context, xmlCfgId);
    }

    public SegmentFormatter(Integer fillColor, Integer borderColor) {
        this(fillColor);
        getInnerEdgePaint().setColor(borderColor.intValue());
        getOuterEdgePaint().setColor(borderColor.intValue());
        getRadialEdgePaint().setColor(borderColor.intValue());
    }

    public SegmentFormatter(Integer fillColor, Integer outerEdgeColor, Integer innerEdgeColor, Integer radialEdgeColor) {
        this(fillColor);
        if (getOuterEdgePaint() != null) {
            getOuterEdgePaint().setColor(outerEdgeColor.intValue());
        } else {
            this.outerEdgePaint = new Paint();
            getOuterEdgePaint().setColor(-16777216);
        }
        if (getInnerEdgePaint() != null) {
            getInnerEdgePaint().setColor(innerEdgeColor.intValue());
        } else {
            this.outerEdgePaint = new Paint();
            getInnerEdgePaint().setColor(-16777216);
        }
        if (getRadialEdgePaint() != null) {
            getRadialEdgePaint().setColor(radialEdgeColor.intValue());
            return;
        }
        this.radialEdgePaint = new Paint();
        getRadialEdgePaint().setColor(-16777216);
    }

    public Class<? extends SeriesRenderer> getRendererClass() {
        return PieRenderer.class;
    }

    public SeriesRenderer doGetRendererInstance(PieChart plot) {
        return new PieRenderer(plot);
    }

    public Paint getInnerEdgePaint() {
        return this.innerEdgePaint;
    }

    public void setInnerEdgePaint(Paint innerEdgePaint2) {
        this.innerEdgePaint = innerEdgePaint2;
    }

    public Paint getOuterEdgePaint() {
        return this.outerEdgePaint;
    }

    public void setOuterEdgePaint(Paint outerEdgePaint2) {
        this.outerEdgePaint = outerEdgePaint2;
    }

    public Paint getRadialEdgePaint() {
        return this.radialEdgePaint;
    }

    public void setRadialEdgePaint(Paint radialEdgePaint2) {
        this.radialEdgePaint = radialEdgePaint2;
    }

    public Paint getFillPaint() {
        return this.fillPaint;
    }

    public void setFillPaint(Paint fillPaint2) {
        this.fillPaint = fillPaint2;
    }

    public Paint getLabelPaint() {
        return this.labelPaint;
    }

    public void setLabelPaint(Paint labelPaint2) {
        this.labelPaint = labelPaint2;
    }

    public Paint getLabelMarkerPaint() {
        return this.labelMarkerPaint;
    }

    public void setLabelMarkerPaint(Paint labelMarkerPaint2) {
        this.labelMarkerPaint = labelMarkerPaint2;
    }

    public float getOffset() {
        return this.offset;
    }

    public void setOffset(float offset2) {
        this.offset = offset2;
    }

    public float getRadialInset() {
        return this.radialInset;
    }

    public void setRadialInset(float radialInset2) {
        this.radialInset = radialInset2;
    }

    public float getInnerInset() {
        return this.innerInset;
    }

    public void setInnerInset(float innerInset2) {
        this.innerInset = innerInset2;
    }

    public float getOuterInset() {
        return this.outerInset;
    }

    public void setOuterInset(float outerInset2) {
        this.outerInset = outerInset2;
    }
}
