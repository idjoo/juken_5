package com.androidplot.ui;

import com.androidplot.Series;
import com.androidplot.ui.RenderBundle;
import com.androidplot.xy.XYSeriesFormatter;

public abstract class RenderBundle<RenderBundleType extends RenderBundle, SeriesType extends Series, SeriesFormatterType extends XYSeriesFormatter> {
    private SeriesFormatterType formatter;
    private Series series;

    public RenderBundle(SeriesType series2, SeriesFormatterType formatter2) {
        this.formatter = formatter2;
        this.series = series2;
    }

    public Series getSeries() {
        return this.series;
    }

    public void setSeries(Series series2) {
        this.series = series2;
    }

    public SeriesFormatterType getFormatter() {
        return this.formatter;
    }

    public void setFormatter(SeriesFormatterType formatter2) {
        this.formatter = formatter2;
    }
}
