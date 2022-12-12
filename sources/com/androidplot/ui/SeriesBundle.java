package com.androidplot.ui;

import com.androidplot.Series;
import com.androidplot.ui.Formatter;

public class SeriesBundle<SeriesType extends Series, FormatterType extends Formatter> {
    private final FormatterType formatter;
    private final SeriesType series;

    public SeriesBundle(SeriesType series2, FormatterType formatter2) {
        this.series = series2;
        this.formatter = formatter2;
    }

    public SeriesType getSeries() {
        return this.series;
    }

    public FormatterType getFormatter() {
        return this.formatter;
    }

    public boolean rendersWith(SeriesRenderer renderer) {
        return getFormatter().getRendererClass() == renderer.getClass();
    }
}
