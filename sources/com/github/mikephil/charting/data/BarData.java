package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import java.util.ArrayList;
import java.util.List;

public class BarData extends BarLineScatterCandleBubbleData<IBarDataSet> {
    private float mGroupSpace = 0.8f;

    public BarData() {
    }

    public BarData(List<String> xVals) {
        super(xVals);
    }

    public BarData(String[] xVals) {
        super(xVals);
    }

    public BarData(List<String> xVals, List<IBarDataSet> dataSets) {
        super(xVals, dataSets);
    }

    public BarData(String[] xVals, List<IBarDataSet> dataSets) {
        super(xVals, dataSets);
    }

    public BarData(List<String> xVals, IBarDataSet dataSet) {
        super(xVals, toList(dataSet));
    }

    public BarData(String[] xVals, IBarDataSet dataSet) {
        super(xVals, toList(dataSet));
    }

    private static List<IBarDataSet> toList(IBarDataSet dataSet) {
        List<IBarDataSet> sets = new ArrayList<>();
        sets.add(dataSet);
        return sets;
    }

    public float getGroupSpace() {
        if (this.mDataSets.size() <= 1) {
            return 0.0f;
        }
        return this.mGroupSpace;
    }

    public void setGroupSpace(float percent) {
        this.mGroupSpace = percent / 100.0f;
    }

    public boolean isGrouped() {
        return this.mDataSets.size() > 1;
    }
}
