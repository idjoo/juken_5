package com.androidplot.xy;

import com.androidplot.SeriesRegistry;

public class XYSeriesRegistry extends SeriesRegistry<XYSeriesBundle, XYSeries, XYSeriesFormatter> {
    private Estimator estimator;

    public void estimate(XYPlot plot) {
        if (this.estimator != null) {
            for (XYSeriesBundle sf : getSeriesAndFormatterList()) {
                getEstimator().run(plot, sf);
            }
        }
    }

    /* access modifiers changed from: protected */
    public XYSeriesBundle newSeriesBundle(XYSeries series, XYSeriesFormatter formatter) {
        return new XYSeriesBundle(series, formatter);
    }

    public Estimator getEstimator() {
        return this.estimator;
    }

    public void setEstimator(Estimator estimator2) {
        this.estimator = estimator2;
    }
}
