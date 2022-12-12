package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.ArrayList;
import java.util.List;

public class CombinedChartRenderer extends DataRenderer {
    protected List<DataRenderer> mRenderers;

    public CombinedChartRenderer(CombinedChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        createRenderers(chart, animator, viewPortHandler);
    }

    /* access modifiers changed from: protected */
    public void createRenderers(CombinedChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        this.mRenderers = new ArrayList();
        for (CombinedChart.DrawOrder order : chart.getDrawOrder()) {
            switch (order) {
                case BAR:
                    if (chart.getBarData() == null) {
                        break;
                    } else {
                        this.mRenderers.add(new BarChartRenderer(chart, animator, viewPortHandler));
                        break;
                    }
                case BUBBLE:
                    if (chart.getBubbleData() == null) {
                        break;
                    } else {
                        this.mRenderers.add(new BubbleChartRenderer(chart, animator, viewPortHandler));
                        break;
                    }
                case LINE:
                    if (chart.getLineData() == null) {
                        break;
                    } else {
                        this.mRenderers.add(new LineChartRenderer(chart, animator, viewPortHandler));
                        break;
                    }
                case CANDLE:
                    if (chart.getCandleData() == null) {
                        break;
                    } else {
                        this.mRenderers.add(new CandleStickChartRenderer(chart, animator, viewPortHandler));
                        break;
                    }
                case SCATTER:
                    if (chart.getScatterData() == null) {
                        break;
                    } else {
                        this.mRenderers.add(new ScatterChartRenderer(chart, animator, viewPortHandler));
                        break;
                    }
            }
        }
    }

    public void initBuffers() {
        for (DataRenderer renderer : this.mRenderers) {
            renderer.initBuffers();
        }
    }

    public void drawData(Canvas c) {
        for (DataRenderer renderer : this.mRenderers) {
            renderer.drawData(c);
        }
    }

    public void drawValues(Canvas c) {
        for (DataRenderer renderer : this.mRenderers) {
            renderer.drawValues(c);
        }
    }

    public void drawExtras(Canvas c) {
        for (DataRenderer renderer : this.mRenderers) {
            renderer.drawExtras(c);
        }
    }

    public void drawHighlighted(Canvas c, Highlight[] indices) {
        for (DataRenderer renderer : this.mRenderers) {
            renderer.drawHighlighted(c, indices);
        }
    }

    public void calcXBounds(BarLineScatterCandleBubbleDataProvider chart, int xAxisModulus) {
        for (DataRenderer renderer : this.mRenderers) {
            renderer.calcXBounds(chart, xAxisModulus);
        }
    }

    public DataRenderer getSubRenderer(int index) {
        if (index >= this.mRenderers.size() || index < 0) {
            return null;
        }
        return this.mRenderers.get(index);
    }

    public List<DataRenderer> getSubRenderers() {
        return this.mRenderers;
    }

    public void setSubRenderers(List<DataRenderer> renderers) {
        this.mRenderers = renderers;
    }
}
