package com.androidplot.xy;

public interface OrderedXYSeries extends XYSeries {

    public enum XOrder {
        ASCENDING,
        DESCENDING,
        NONE
    }

    XOrder getXOrder();
}
