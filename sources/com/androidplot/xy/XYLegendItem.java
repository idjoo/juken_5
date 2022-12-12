package com.androidplot.xy;

import android.support.annotation.NonNull;
import com.androidplot.ui.widget.LegendItem;

public class XYLegendItem implements LegendItem {
    public final Object item;
    private final String text;
    public final Type type;

    public enum Type {
        SERIES,
        REGION
    }

    public XYLegendItem(@NonNull Type cellType, @NonNull Object item2, @NonNull String text2) {
        this.type = cellType;
        this.item = item2;
        this.text = text2;
    }

    public String getTitle() {
        return this.text;
    }
}
