package com.androidplot.ui;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Region;
import com.androidplot.Plot;
import com.androidplot.Series;
import com.androidplot.exception.PlotRenderException;
import com.androidplot.ui.Formatter;
import java.util.ArrayList;
import java.util.List;

public abstract class SeriesRenderer<PlotType extends Plot, SeriesType extends Series, SeriesFormatterType extends Formatter> {
    private PlotType plot;

    /* access modifiers changed from: protected */
    public abstract void doDrawLegendIcon(Canvas canvas, RectF rectF, SeriesFormatterType seriesformattertype);

    /* access modifiers changed from: protected */
    public abstract void onRender(Canvas canvas, RectF rectF, SeriesType seriestype, SeriesFormatterType seriesformattertype, RenderStack renderStack) throws PlotRenderException;

    public SeriesRenderer(PlotType plot2) {
        this.plot = plot2;
    }

    public PlotType getPlot() {
        return this.plot;
    }

    public void setPlot(PlotType plot2) {
        this.plot = plot2;
    }

    public SeriesFormatterType getFormatter(SeriesType series) {
        return this.plot.getFormatter(series, getClass());
    }

    public void render(Canvas canvas, RectF plotArea, SeriesBundle<SeriesType, SeriesFormatterType> sfPair, RenderStack stack) throws PlotRenderException {
        onRender(canvas, plotArea, sfPair.getSeries(), sfPair.getFormatter(), stack);
    }

    public void drawSeriesLegendIcon(Canvas canvas, RectF rect, SeriesFormatterType formatter) {
        try {
            canvas.save();
            canvas.clipRect(rect, Region.Op.INTERSECT);
            doDrawLegendIcon(canvas, rect, formatter);
        } finally {
            canvas.restore();
        }
    }

    public List<SeriesBundle<SeriesType, ? extends SeriesFormatterType>> getSeriesAndFormatterList() {
        List<SeriesBundle<SeriesType, ? extends SeriesFormatterType>> results = new ArrayList<>();
        for (SeriesBundle<SeriesType, ? extends SeriesFormatterType> thisPair : getPlot().getRegistry().getSeriesAndFormatterList()) {
            if (thisPair.rendersWith(this)) {
                results.add(thisPair);
            }
        }
        return results;
    }

    public List<SeriesType> getSeriesList() {
        List<SeriesType> results = new ArrayList<>();
        for (SeriesBundle<SeriesType, ? extends SeriesFormatterType> thisPair : getPlot().getRegistry().getSeriesAndFormatterList()) {
            if (thisPair.rendersWith(this)) {
                results.add(thisPair.getSeries());
            }
        }
        return results;
    }
}
