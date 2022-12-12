package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class BarHighlighter extends ChartHighlighter<BarDataProvider> {
    public BarHighlighter(BarDataProvider chart) {
        super(chart);
    }

    public Highlight getHighlight(float x, float y) {
        Highlight h = super.getHighlight(x, y);
        if (h == null) {
            return h;
        }
        IBarDataSet set = (IBarDataSet) ((BarDataProvider) this.mChart).getBarData().getDataSetByIndex(h.getDataSetIndex());
        if (!set.isStacked()) {
            return h;
        }
        float[] pts = new float[2];
        pts[1] = y;
        ((BarDataProvider) this.mChart).getTransformer(set.getAxisDependency()).pixelsToValue(pts);
        return getStackedHighlight(h, set, h.getXIndex(), h.getDataSetIndex(), (double) pts[1]);
    }

    /* access modifiers changed from: protected */
    public int getXIndex(float x) {
        if (!((BarDataProvider) this.mChart).getBarData().isGrouped()) {
            return super.getXIndex(x);
        }
        float baseNoSpace = getBase(x);
        int xIndex = ((int) baseNoSpace) / ((BarDataProvider) this.mChart).getBarData().getDataSetCount();
        int valCount = ((BarDataProvider) this.mChart).getData().getXValCount();
        if (xIndex < 0) {
            return 0;
        }
        if (xIndex >= valCount) {
            return valCount - 1;
        }
        return xIndex;
    }

    /* access modifiers changed from: protected */
    public int getDataSetIndex(int xIndex, float x, float y) {
        if (!((BarDataProvider) this.mChart).getBarData().isGrouped()) {
            return 0;
        }
        float baseNoSpace = getBase(x);
        int setCount = ((BarDataProvider) this.mChart).getBarData().getDataSetCount();
        int dataSetIndex = ((int) baseNoSpace) % setCount;
        if (dataSetIndex < 0) {
            return 0;
        }
        if (dataSetIndex >= setCount) {
            return setCount - 1;
        }
        return dataSetIndex;
    }

    /* access modifiers changed from: protected */
    public Highlight getStackedHighlight(Highlight old, IBarDataSet set, int xIndex, int dataSetIndex, double yValue) {
        BarEntry entry = (BarEntry) set.getEntryForXIndex(xIndex);
        if (entry == null || entry.getVals() == null) {
            return old;
        }
        Range[] ranges = getRanges(entry);
        int stackIndex = getClosestStackIndex(ranges, (float) yValue);
        if (ranges.length > 0) {
            return new Highlight(xIndex, dataSetIndex, stackIndex, ranges[stackIndex]);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public int getClosestStackIndex(Range[] ranges, float value) {
        if (ranges == null || ranges.length == 0) {
            return 0;
        }
        int stackIndex = 0;
        for (Range range : ranges) {
            if (range.contains(value)) {
                return stackIndex;
            }
            stackIndex++;
        }
        int length = Math.max(ranges.length - 1, 0);
        if (value <= ranges[length].to) {
            length = 0;
        }
        return length;
    }

    /* access modifiers changed from: protected */
    public float getBase(float x) {
        float[] pts = new float[2];
        pts[0] = x;
        ((BarDataProvider) this.mChart).getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        float xVal = pts[0];
        return xVal - (((BarDataProvider) this.mChart).getBarData().getGroupSpace() * ((float) ((int) (xVal / (((BarDataProvider) this.mChart).getBarData().getGroupSpace() + ((float) ((BarDataProvider) this.mChart).getBarData().getDataSetCount()))))));
    }

    /* access modifiers changed from: protected */
    public Range[] getRanges(BarEntry entry) {
        float[] values = entry.getVals();
        if (values == null || values.length == 0) {
            return new Range[0];
        }
        Range[] ranges = new Range[values.length];
        float negRemain = -entry.getNegativeSum();
        float posRemain = 0.0f;
        for (int i = 0; i < ranges.length; i++) {
            float value = values[i];
            if (value < 0.0f) {
                ranges[i] = new Range(negRemain, Math.abs(value) + negRemain);
                negRemain += Math.abs(value);
            } else {
                ranges[i] = new Range(posRemain, posRemain + value);
                posRemain += value;
            }
        }
        return ranges;
    }
}
