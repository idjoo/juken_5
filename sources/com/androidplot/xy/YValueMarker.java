package com.androidplot.xy;

import android.graphics.Paint;
import com.androidplot.ui.HorizontalPosition;
import com.androidplot.ui.HorizontalPositioning;

public class YValueMarker extends ValueMarker<HorizontalPosition> {
    public YValueMarker(Number value, String text) {
        super(value, text, new HorizontalPosition(3.0f, HorizontalPositioning.ABSOLUTE_FROM_LEFT));
    }

    public YValueMarker(Number value, String text, HorizontalPosition textPosition, Paint linePaint, Paint textPaint) {
        super(value, text, textPosition, linePaint, textPaint);
    }

    public YValueMarker(Number value, String text, HorizontalPosition textPosition, int linePaint, int textPaint) {
        super(value, text, textPosition, linePaint, textPaint);
    }
}
