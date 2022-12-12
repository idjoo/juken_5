package com.androidplot.xy;

import com.androidplot.ui.SeriesRenderer;

public class StepFormatter extends LineAndPointFormatter {
    public StepFormatter() {
    }

    public StepFormatter(Integer lineColor, Integer fillColor) {
        initLinePaint(lineColor);
        initFillPaint(fillColor);
    }

    public Class<? extends SeriesRenderer> getRendererClass() {
        return StepRenderer.class;
    }

    public SeriesRenderer doGetRendererInstance(XYPlot plot) {
        return new StepRenderer(plot);
    }
}
