package com.androidplot.xy;

import android.graphics.Paint;
import android.support.v4.internal.view.SupportMenu;
import com.androidplot.ui.PositionMetric;
import com.androidplot.ui.TextOrientation;

public abstract class ValueMarker<PositionMetricType extends PositionMetric> {
    private Paint linePaint;
    private String text;
    private int textMargin;
    private TextOrientation textOrientation;
    private Paint textPaint;
    private PositionMetricType textPosition;
    private Number value;

    public String getText() {
        return this.text;
    }

    public void setText(String text2) {
        this.text = text2;
    }

    public ValueMarker(Number value2, String text2, PositionMetricType textPosition2) {
        this.textMargin = 2;
        this.linePaint = new Paint();
        this.linePaint.setColor(SupportMenu.CATEGORY_MASK);
        this.linePaint.setAntiAlias(true);
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.textPaint = new Paint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.value = value2;
        this.textPosition = textPosition2;
        this.text = text2;
    }

    public ValueMarker(Number value2, String text2, PositionMetricType textPosition2, Paint linePaint2, Paint textPaint2) {
        this(value2, text2, textPosition2);
        this.linePaint = linePaint2;
        this.textPaint = textPaint2;
    }

    public ValueMarker(Number value2, String text2, PositionMetricType textPosition2, int linePaint2, int textPaint2) {
        this(value2, text2, textPosition2);
        this.linePaint.setColor(linePaint2);
        this.textPaint.setColor(textPaint2);
    }

    public Number getValue() {
        return this.value;
    }

    public void setValue(Number value2) {
        this.value = value2;
    }

    public Paint getLinePaint() {
        return this.linePaint;
    }

    public void setLinePaint(Paint linePaint2) {
        this.linePaint = linePaint2;
    }

    public Paint getTextPaint() {
        return this.textPaint;
    }

    public void setTextPaint(Paint textPaint2) {
        this.textPaint = textPaint2;
    }

    public TextOrientation getTextOrientation() {
        return this.textOrientation;
    }

    public void setTextOrientation(TextOrientation textOrientation2) {
        this.textOrientation = textOrientation2;
    }

    public int getTextMargin() {
        return this.textMargin;
    }

    public void setTextMargin(int textMargin2) {
        this.textMargin = textMargin2;
    }

    public PositionMetricType getTextPosition() {
        return this.textPosition;
    }

    public void setTextPosition(PositionMetricType textPosition2) {
        this.textPosition = textPosition2;
    }
}
