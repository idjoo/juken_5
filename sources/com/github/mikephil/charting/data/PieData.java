package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import java.util.ArrayList;
import java.util.List;

public class PieData extends ChartData<IPieDataSet> {
    public PieData() {
    }

    public PieData(List<String> xVals) {
        super(xVals);
    }

    public PieData(String[] xVals) {
        super(xVals);
    }

    public PieData(List<String> xVals, IPieDataSet dataSet) {
        super(xVals, toList(dataSet));
    }

    public PieData(String[] xVals, IPieDataSet dataSet) {
        super(xVals, toList(dataSet));
    }

    private static List<IPieDataSet> toList(IPieDataSet dataSet) {
        List<IPieDataSet> sets = new ArrayList<>();
        sets.add(dataSet);
        return sets;
    }

    public void setDataSet(IPieDataSet dataSet) {
        this.mDataSets.clear();
        this.mDataSets.add(dataSet);
        init();
    }

    public IPieDataSet getDataSet() {
        return (IPieDataSet) this.mDataSets.get(0);
    }

    public IPieDataSet getDataSetByIndex(int index) {
        if (index == 0) {
            return getDataSet();
        }
        return null;
    }

    public IPieDataSet getDataSetByLabel(String label, boolean ignorecase) {
        if (ignorecase) {
            if (label.equalsIgnoreCase(((IPieDataSet) this.mDataSets.get(0)).getLabel())) {
                return (IPieDataSet) this.mDataSets.get(0);
            }
            return null;
        } else if (label.equals(((IPieDataSet) this.mDataSets.get(0)).getLabel())) {
            return (IPieDataSet) this.mDataSets.get(0);
        } else {
            return null;
        }
    }

    public float getYValueSum() {
        float sum = 0.0f;
        for (int i = 0; i < getDataSet().getEntryCount(); i++) {
            sum += getDataSet().getEntryForIndex(i).getVal();
        }
        return sum;
    }
}
