package com.androidplot.xy;

import com.androidplot.xy.XYSeries;

public interface PointLabeler<SeriesType extends XYSeries> {
    String getLabel(SeriesType seriestype, int i);
}
