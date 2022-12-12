package com.github.mikephil.charting.charts;

import android.content.Context;
import android.util.AttributeSet;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.highlight.CombinedHighlighter;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.ScatterDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;

public class CombinedChart extends BarLineChartBase<CombinedData> implements LineDataProvider, BarDataProvider, ScatterDataProvider, CandleDataProvider, BubbleDataProvider {
    private boolean mDrawBarShadow = false;
    private boolean mDrawHighlightArrow = false;
    protected DrawOrder[] mDrawOrder = {DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.LINE, DrawOrder.CANDLE, DrawOrder.SCATTER};
    private boolean mDrawValueAboveBar = true;

    public enum DrawOrder {
        BAR,
        BUBBLE,
        LINE,
        CANDLE,
        SCATTER
    }

    public CombinedChart(Context context) {
        super(context);
    }

    public CombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* access modifiers changed from: protected */
    public void init() {
        super.init();
        setHighlighter(new CombinedHighlighter(this));
    }

    /* access modifiers changed from: protected */
    public void calcMinMax() {
        super.calcMinMax();
        if (!(getBarData() == null && getCandleData() == null && getBubbleData() == null)) {
            this.mXAxis.mAxisMinimum = -0.5f;
            this.mXAxis.mAxisMaximum = ((float) ((CombinedData) this.mData).getXVals().size()) - 0.5f;
            if (getBubbleData() != null) {
                for (IBubbleDataSet set : getBubbleData().getDataSets()) {
                    float xmin = set.getXMin();
                    float xmax = set.getXMax();
                    if (xmin < this.mXAxis.mAxisMinimum) {
                        this.mXAxis.mAxisMinimum = xmin;
                    }
                    if (xmax > this.mXAxis.mAxisMaximum) {
                        this.mXAxis.mAxisMaximum = xmax;
                    }
                }
            }
        }
        this.mXAxis.mAxisRange = Math.abs(this.mXAxis.mAxisMaximum - this.mXAxis.mAxisMinimum);
        if (this.mXAxis.mAxisRange == 0.0f && getLineData() != null && getLineData().getYValCount() > 0) {
            this.mXAxis.mAxisRange = 1.0f;
        }
    }

    public void setData(CombinedData data) {
        this.mData = null;
        this.mRenderer = null;
        super.setData(data);
        this.mRenderer = new CombinedChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.mRenderer.initBuffers();
    }

    public LineData getLineData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData) this.mData).getLineData();
    }

    public BarData getBarData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData) this.mData).getBarData();
    }

    public ScatterData getScatterData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData) this.mData).getScatterData();
    }

    public CandleData getCandleData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData) this.mData).getCandleData();
    }

    public BubbleData getBubbleData() {
        if (this.mData == null) {
            return null;
        }
        return ((CombinedData) this.mData).getBubbleData();
    }

    public boolean isDrawBarShadowEnabled() {
        return this.mDrawBarShadow;
    }

    public boolean isDrawValueAboveBarEnabled() {
        return this.mDrawValueAboveBar;
    }

    public boolean isDrawHighlightArrowEnabled() {
        return this.mDrawHighlightArrow;
    }

    public void setDrawHighlightArrow(boolean enabled) {
        this.mDrawHighlightArrow = enabled;
    }

    public void setDrawValueAboveBar(boolean enabled) {
        this.mDrawValueAboveBar = enabled;
    }

    public void setDrawBarShadow(boolean enabled) {
        this.mDrawBarShadow = enabled;
    }

    public DrawOrder[] getDrawOrder() {
        return this.mDrawOrder;
    }

    public void setDrawOrder(DrawOrder[] order) {
        if (order != null && order.length > 0) {
            this.mDrawOrder = order;
        }
    }
}
