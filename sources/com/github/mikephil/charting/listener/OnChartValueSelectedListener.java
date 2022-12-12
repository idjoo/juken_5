package com.github.mikephil.charting.listener;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

public interface OnChartValueSelectedListener {
    void onNothingSelected();

    void onValueSelected(Entry entry, int i, Highlight highlight);
}
