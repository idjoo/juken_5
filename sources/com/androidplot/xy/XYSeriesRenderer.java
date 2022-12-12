package com.androidplot.xy;

import com.androidplot.ui.SeriesBundle;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYSeriesFormatter;
import java.util.Hashtable;

public abstract class XYSeriesRenderer<SeriesType extends XYSeries, XYFormatterType extends XYSeriesFormatter> extends SeriesRenderer<XYPlot, SeriesType, XYFormatterType> {
    public XYSeriesRenderer(XYPlot plot) {
        super(plot);
    }

    public Hashtable<XYRegionFormatter, String> getUniqueRegionFormatters() {
        Hashtable<XYRegionFormatter, String> found = new Hashtable<>();
        for (SeriesBundle<SeriesType, ? extends XYFormatterType> sfPair : getSeriesAndFormatterList()) {
            for (RectRegion region : ((XYSeriesFormatter) sfPair.getFormatter()).getRegions().elements()) {
                found.put(((XYSeriesFormatter) sfPair.getFormatter()).getRegionFormatter(region), region.getLabel());
            }
        }
        return found;
    }
}
