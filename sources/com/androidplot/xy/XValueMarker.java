package com.androidplot.xy;

import android.graphics.Paint;
import com.androidplot.ui.VerticalPosition;
import com.androidplot.ui.VerticalPositioning;

public class XValueMarker extends ValueMarker<VerticalPosition> {
    public XValueMarker(Number value, String text) {
        super(value, text, new VerticalPosition(3.0f, VerticalPositioning.ABSOLUTE_FROM_TOP));
    }

    public XValueMarker(Number value, String text, VerticalPosition textPosition, Paint linePaint, Paint textPaint) {
        super(value, text, textPosition, linePaint, textPaint);
    }

    public XValueMarker(Number value, String text, VerticalPosition textPosition, int linePaint, int textPaint) {
        super(value, text, textPosition, linePaint, textPaint);
    }
}
