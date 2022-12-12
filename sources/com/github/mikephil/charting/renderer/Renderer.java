package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class Renderer {
    protected int mMaxX = 0;
    protected int mMinX = 0;
    protected ViewPortHandler mViewPortHandler;

    public Renderer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }

    /* access modifiers changed from: protected */
    public boolean fitsBounds(float val, float min, float max) {
        if (val < min || val > max) {
            return false;
        }
        return true;
    }

    public void calcXBounds(BarLineScatterCandleBubbleDataProvider dataProvider, int xAxisModulus) {
        int subLow;
        int low = dataProvider.getLowestVisibleXIndex();
        int high = dataProvider.getHighestVisibleXIndex();
        if (low % xAxisModulus == 0) {
            subLow = xAxisModulus;
        } else {
            subLow = 0;
        }
        this.mMinX = Math.max(((low / xAxisModulus) * xAxisModulus) - subLow, 0);
        this.mMaxX = Math.min(((high / xAxisModulus) * xAxisModulus) + xAxisModulus, (int) dataProvider.getXChartMax());
    }
}
