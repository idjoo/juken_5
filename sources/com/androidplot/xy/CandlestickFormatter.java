package com.androidplot.xy;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.util.PixelUtils;

public class CandlestickFormatter extends XYSeriesFormatter<XYRegionFormatter> {
    private static final float DEFAULT_STROKE_PIX = PixelUtils.dpToPix(4.0f);
    private static final float DEFAULT_WIDTH_PIX = PixelUtils.dpToPix(10.0f);
    private BodyStyle bodyStyle;
    private float bodyWidth;
    private Paint fallingBodyFillPaint;
    private Paint fallingBodyStrokePaint;
    private Paint lowerCapPaint;
    private float lowerCapWidth;
    private Paint risingBodyFillPaint;
    private Paint risingBodyStrokePaint;
    private Paint upperCapPaint;
    private float upperCapWidth;
    private Paint wickPaint;

    public enum BodyStyle {
        SQUARE,
        TRIANGULAR
    }

    protected static Paint getDefaultFillPaint(int color) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(color);
        return p;
    }

    protected static Paint getDefaultStrokePaint(int color) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(DEFAULT_STROKE_PIX);
        p.setColor(color);
        p.setAntiAlias(true);
        return p;
    }

    public CandlestickFormatter(Context context, int xmlCfgId) {
        this();
        configure(context, xmlCfgId);
    }

    public CandlestickFormatter() {
        this(getDefaultStrokePaint(InputDeviceCompat.SOURCE_ANY), getDefaultFillPaint(-16711936), getDefaultFillPaint(SupportMenu.CATEGORY_MASK), getDefaultStrokePaint(-16711936), getDefaultStrokePaint(SupportMenu.CATEGORY_MASK), getDefaultStrokePaint(InputDeviceCompat.SOURCE_ANY), getDefaultStrokePaint(InputDeviceCompat.SOURCE_ANY), BodyStyle.SQUARE);
    }

    public CandlestickFormatter(Paint wickPaint2, Paint risingBodyFillPaint2, Paint fallingBodyFillPaint2, Paint risingBodyStrokePaint2, Paint fallingBodyStrokePaint2, Paint upperCapPaint2, Paint lowerCapPaint2, BodyStyle bodyStyle2) {
        this.bodyWidth = DEFAULT_WIDTH_PIX;
        this.upperCapWidth = DEFAULT_WIDTH_PIX;
        this.lowerCapWidth = DEFAULT_WIDTH_PIX;
        setWickPaint(wickPaint2);
        setRisingBodyFillPaint(risingBodyFillPaint2);
        setFallingBodyFillPaint(fallingBodyFillPaint2);
        setRisingBodyStrokePaint(risingBodyStrokePaint2);
        setFallingBodyStrokePaint(fallingBodyStrokePaint2);
        setUpperCapPaint(upperCapPaint2);
        setLowerCapPaint(lowerCapPaint2);
        setBodyStyle(bodyStyle2);
    }

    public Class<? extends SeriesRenderer> getRendererClass() {
        return CandlestickRenderer.class;
    }

    public SeriesRenderer doGetRendererInstance(XYPlot plot) {
        return new CandlestickRenderer(plot);
    }

    public Paint getWickPaint() {
        return this.wickPaint;
    }

    public void setWickPaint(Paint wickPaint2) {
        this.wickPaint = wickPaint2;
    }

    public Paint getRisingBodyFillPaint() {
        return this.risingBodyFillPaint;
    }

    public void setRisingBodyFillPaint(Paint risingBodyFillPaint2) {
        this.risingBodyFillPaint = risingBodyFillPaint2;
    }

    public Paint getRisingBodyStrokePaint() {
        return this.risingBodyStrokePaint;
    }

    public void setRisingBodyStrokePaint(Paint risingBodyStrokePaint2) {
        this.risingBodyStrokePaint = risingBodyStrokePaint2;
    }

    public Paint getUpperCapPaint() {
        return this.upperCapPaint;
    }

    public void setUpperCapPaint(Paint upperCapPaint2) {
        this.upperCapPaint = upperCapPaint2;
    }

    public Paint getLowerCapPaint() {
        return this.lowerCapPaint;
    }

    public void setLowerCapPaint(Paint lowerCapPaint2) {
        this.lowerCapPaint = lowerCapPaint2;
    }

    public float getBodyWidth() {
        return this.bodyWidth;
    }

    public void setBodyWidth(float bodyWidth2) {
        this.bodyWidth = bodyWidth2;
    }

    public float getLowerCapWidth() {
        return this.lowerCapWidth;
    }

    public void setLowerCapWidth(float lowerCapWidth2) {
        this.lowerCapWidth = lowerCapWidth2;
    }

    public float getUpperCapWidth() {
        return this.upperCapWidth;
    }

    public void setUpperCapWidth(float upperCapWidth2) {
        this.upperCapWidth = upperCapWidth2;
    }

    public Paint getFallingBodyFillPaint() {
        return this.fallingBodyFillPaint;
    }

    public void setFallingBodyFillPaint(Paint fallingBodyFillPaint2) {
        this.fallingBodyFillPaint = fallingBodyFillPaint2;
    }

    public Paint getFallingBodyStrokePaint() {
        return this.fallingBodyStrokePaint;
    }

    public void setFallingBodyStrokePaint(Paint fallingBodyStrokePaint2) {
        this.fallingBodyStrokePaint = fallingBodyStrokePaint2;
    }

    public BodyStyle getBodyStyle() {
        return this.bodyStyle;
    }

    public void setBodyStyle(BodyStyle bodyStyle2) {
        this.bodyStyle = bodyStyle2;
    }

    public void setCapAndWickPaint(Paint paint) {
        setUpperCapPaint(paint);
        setLowerCapPaint(paint);
        setWickPaint(paint);
    }
}
