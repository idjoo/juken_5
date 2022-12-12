package com.androidplot.ui;

import android.graphics.RectF;

public class Size {
    public static Size FILL = new Size(0.0f, SizeMode.FILL, 0.0f, SizeMode.FILL);
    private SizeMetric height;
    private SizeMetric width;

    public Size(float height2, SizeMode heightLayoutType, float width2, SizeMode widthLayoutType) {
        this.height = new SizeMetric(height2, heightLayoutType);
        this.width = new SizeMetric(width2, widthLayoutType);
    }

    public Size(SizeMetric height2, SizeMetric width2) {
        this.height = height2;
        this.width = width2;
    }

    public SizeMetric getHeight() {
        return this.height;
    }

    public void setHeight(SizeMetric height2) {
        this.height = height2;
    }

    public SizeMetric getWidth() {
        return this.width;
    }

    public RectF getRectF(RectF canvasRect) {
        return new RectF(0.0f, 0.0f, this.width.getPixelValue(canvasRect.width()), this.height.getPixelValue(canvasRect.height()));
    }

    public void setWidth(SizeMetric width2) {
        this.width = width2;
    }
}
