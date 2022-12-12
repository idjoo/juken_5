package com.androidplot.pie;

import android.support.annotation.NonNull;
import com.androidplot.ui.widget.LegendItem;

public class PieLegendItem implements LegendItem {
    public SegmentFormatter formatter;
    public Segment segment;

    public PieLegendItem(@NonNull Segment segment2, @NonNull SegmentFormatter formatter2) {
        this.segment = segment2;
        this.formatter = formatter2;
    }

    public String getTitle() {
        return this.segment.getTitle();
    }
}
