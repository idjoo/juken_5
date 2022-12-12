package com.androidplot;

import android.graphics.Paint;
import com.androidplot.util.PixelUtils;

public class SimpleLineLabelFormatter implements LineLabelFormatter {
    private static final int DEFAULT_STROKE_SIZE_DP = 2;
    private static final int DEFAULT_TEXT_SIZE_SP = 12;
    private Paint paint;

    public SimpleLineLabelFormatter() {
        this(new Paint());
        getPaint().setColor(-1);
        getPaint().setTextSize(PixelUtils.spToPix(12.0f));
        getPaint().setStrokeWidth(PixelUtils.dpToPix(2.0f));
    }

    public SimpleLineLabelFormatter(int color) {
        this();
        getPaint().setColor(color);
    }

    public SimpleLineLabelFormatter(Paint paint2) {
        this.paint = paint2;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public void setPaint(Paint paint2) {
        this.paint = paint2;
    }

    public Paint getPaint(Number value) {
        return getPaint();
    }
}
