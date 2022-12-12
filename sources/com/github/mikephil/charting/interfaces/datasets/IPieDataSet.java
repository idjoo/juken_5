package com.github.mikephil.charting.interfaces.datasets;

import com.github.mikephil.charting.data.Entry;

public interface IPieDataSet extends IDataSet<Entry> {
    float getSelectionShift();

    float getSliceSpace();
}
