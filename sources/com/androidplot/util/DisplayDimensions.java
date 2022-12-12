package com.androidplot.util;

import android.graphics.RectF;

public class DisplayDimensions {
    private static final int ONE = 1;
    private static final RectF initRect = new RectF(1.0f, 1.0f, 1.0f, 1.0f);
    public final RectF canvasRect;
    public final RectF marginatedRect;
    public final RectF paddedRect;

    public DisplayDimensions() {
        this(initRect, initRect, initRect);
    }

    public DisplayDimensions(RectF canvasRect2, RectF marginatedRect2, RectF paddedRect2) {
        this.canvasRect = canvasRect2;
        this.marginatedRect = marginatedRect2;
        this.paddedRect = paddedRect2;
    }
}
