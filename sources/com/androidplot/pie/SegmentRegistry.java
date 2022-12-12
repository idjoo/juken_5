package com.androidplot.pie;

import com.androidplot.SeriesRegistry;

public class SegmentRegistry extends SeriesRegistry<SegmentBundle, Segment, SegmentFormatter> {
    /* access modifiers changed from: protected */
    public SegmentBundle newSeriesBundle(Segment series, SegmentFormatter formatter) {
        return new SegmentBundle(series, formatter);
    }
}
