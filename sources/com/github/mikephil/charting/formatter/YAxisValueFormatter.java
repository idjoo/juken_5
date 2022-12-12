package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.YAxis;

public interface YAxisValueFormatter {
    String getFormattedValue(float f, YAxis yAxis);
}
