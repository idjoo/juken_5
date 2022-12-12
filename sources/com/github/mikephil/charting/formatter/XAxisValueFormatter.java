package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.utils.ViewPortHandler;

public interface XAxisValueFormatter {
    String getXValue(String str, int i, ViewPortHandler viewPortHandler);
}
