package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import java.util.ArrayList;
import java.util.List;

public class BubbleData extends BarLineScatterCandleBubbleData<IBubbleDataSet> {
    public BubbleData() {
    }

    public BubbleData(List<String> xVals) {
        super(xVals);
    }

    public BubbleData(String[] xVals) {
        super(xVals);
    }

    public BubbleData(List<String> xVals, List<IBubbleDataSet> dataSets) {
        super(xVals, dataSets);
    }

    public BubbleData(String[] xVals, List<IBubbleDataSet> dataSets) {
        super(xVals, dataSets);
    }

    public BubbleData(List<String> xVals, IBubbleDataSet dataSet) {
        super(xVals, toList(dataSet));
    }

    public BubbleData(String[] xVals, IBubbleDataSet dataSet) {
        super(xVals, toList(dataSet));
    }

    private static List<IBubbleDataSet> toList(IBubbleDataSet dataSet) {
        List<IBubbleDataSet> sets = new ArrayList<>();
        sets.add(dataSet);
        return sets;
    }

    public void setHighlightCircleWidth(float width) {
        for (IBubbleDataSet set : this.mDataSets) {
            set.setHighlightCircleWidth(width);
        }
    }
}
