package com.github.mikephil.charting.utils;

import com.github.mikephil.charting.interfaces.datasets.IDataSet;

public class SelectionDetail {
    public IDataSet dataSet;
    public int dataSetIndex;
    public float val;

    public SelectionDetail(float val2, int dataSetIndex2, IDataSet set) {
        this.val = val2;
        this.dataSetIndex = dataSetIndex2;
        this.dataSet = set;
    }
}
