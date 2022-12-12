package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.utils.ViewPortHandler;

public class DefaultXAxisValueFormatter implements XAxisValueFormatter {
    public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
        return original;
    }
}
