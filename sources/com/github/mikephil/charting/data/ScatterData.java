package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import java.util.ArrayList;
import java.util.List;

public class ScatterData extends BarLineScatterCandleBubbleData<IScatterDataSet> {
    public ScatterData() {
    }

    public ScatterData(List<String> xVals) {
        super(xVals);
    }

    public ScatterData(String[] xVals) {
        super(xVals);
    }

    public ScatterData(List<String> xVals, List<IScatterDataSet> dataSets) {
        super(xVals, dataSets);
    }

    public ScatterData(String[] xVals, List<IScatterDataSet> dataSets) {
        super(xVals, dataSets);
    }

    public ScatterData(List<String> xVals, IScatterDataSet dataSet) {
        super(xVals, toList(dataSet));
    }

    public ScatterData(String[] xVals, IScatterDataSet dataSet) {
        super(xVals, toList(dataSet));
    }

    private static List<IScatterDataSet> toList(IScatterDataSet dataSet) {
        List<IScatterDataSet> sets = new ArrayList<>();
        sets.add(dataSet);
        return sets;
    }

    public float getGreatestShapeSize() {
        float max = 0.0f;
        for (IScatterDataSet set : this.mDataSets) {
            float size = set.getScatterShapeSize();
            if (size > max) {
                max = size;
            }
        }
        return max;
    }
}
