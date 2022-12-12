package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class HorizontalBarHighlighter extends BarHighlighter {
    public HorizontalBarHighlighter(BarDataProvider chart) {
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
        pts[0] = y;
        ((BarDataProvider) this.mChart).getTransformer(set.getAxisDependency()).pixelsToValue(pts);
        return getStackedHighlight(h, set, h.getXIndex(), h.getDataSetIndex(), (double) pts[0]);
    }

    /* access modifiers changed from: protected */
    public int getXIndex(float x) {
        if (!((BarDataProvider) this.mChart).getBarData().isGrouped()) {
            float[] pts = new float[2];
            pts[1] = x;
            ((BarDataProvider) this.mChart).getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
            return Math.round(pts[1]);
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
    public float getBase(float y) {
        float[] pts = new float[2];
        pts[1] = y;
        ((BarDataProvider) this.mChart).getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        float yVal = pts[1];
        return yVal - (((BarDataProvider) this.mChart).getBarData().getGroupSpace() * ((float) ((int) (yVal / (((BarDataProvider) this.mChart).getBarData().getGroupSpace() + ((float) ((BarDataProvider) this.mChart).getBarData().getDataSetCount()))))));
    }
}
