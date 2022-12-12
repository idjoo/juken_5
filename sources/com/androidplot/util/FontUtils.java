package com.androidplot.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class FontUtils {
    private static final int ZERO = 0;

    public static float getFontHeight(Paint paint) {
        Paint.FontMetrics metrics = paint.getFontMetrics();
        return (-metrics.ascent) + metrics.descent;
    }

    public static Rect getPackedStringDimensions(String text, Paint paint) {
        Rect size = new Rect();
        paint.getTextBounds(text, 0, text.length(), size);
        return size;
    }

    public static Rect getStringDimensions(String text, Paint paint) {
        Rect size = new Rect();
        if (text == null || text.length() == 0) {
            return null;
        }
        paint.getTextBounds(text, 0, text.length(), size);
        size.bottom = size.top + ((int) getFontHeight(paint));
        return size;
    }

    public static void drawTextVerticallyCentered(Canvas canvas, String text, float cx, float cy, Paint paint) {
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx, cy - textBounds.exactCenterY(), paint);
    }
}
