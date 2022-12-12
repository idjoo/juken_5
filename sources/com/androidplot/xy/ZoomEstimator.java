package com.androidplot.xy;

public class ZoomEstimator extends Estimator {
    public void run(XYPlot plot, XYSeriesBundle sf) {
        if (sf.getSeries() instanceof SampledXYSeries) {
            SampledXYSeries oxy = (SampledXYSeries) sf.getSeries();
            oxy.setZoomFactor(calculateZoom(oxy, plot.getBounds()));
        }
    }

    /* access modifiers changed from: protected */
    public double calculateZoom(SampledXYSeries series, RectRegion visibleBounds) {
        double factor = (double) Math.abs(Math.round(series.getMaxZoomFactor() / series.getBounds().getxRegion().ratio(visibleBounds.getxRegion()).doubleValue()));
        if (factor > 0.0d) {
            return factor;
        }
        return 1.0d;
    }
}
