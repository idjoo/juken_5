package com.androidplot.xy;

import android.content.Context;
import com.androidplot.ui.Formatter;
import com.androidplot.util.LayerHash;
import com.androidplot.util.Layerable;
import com.androidplot.xy.XYRegionFormatter;

public abstract class XYSeriesFormatter<XYRegionFormatterType extends XYRegionFormatter> extends Formatter<XYPlot> {
    private PointLabelFormatter pointLabelFormatter;
    private PointLabeler pointLabeler = new PointLabeler() {
        public String getLabel(XYSeries series, int index) {
            return String.valueOf(series.getY(index));
        }
    };
    LayerHash<RectRegion, XYRegionFormatterType> regions = new LayerHash<>();

    public XYSeriesFormatter() {
    }

    public XYSeriesFormatter(Context context, int xmlCfgId) {
        super(context, xmlCfgId);
    }

    public void addRegion(RectRegion region, XYRegionFormatterType regionFormatter) {
        this.regions.addToBottom(region, regionFormatter);
    }

    public void removeRegion(RectRegion region) {
        this.regions.remove(region);
    }

    public Layerable<RectRegion> getRegions() {
        return this.regions;
    }

    public XYRegionFormatterType getRegionFormatter(RectRegion region) {
        return (XYRegionFormatter) this.regions.get(region);
    }

    public PointLabeler getPointLabeler() {
        return this.pointLabeler;
    }

    public void setPointLabeler(PointLabeler pointLabeler2) {
        this.pointLabeler = pointLabeler2;
    }

    public boolean hasPointLabelFormatter() {
        return this.pointLabelFormatter != null;
    }

    public PointLabelFormatter getPointLabelFormatter() {
        if (this.pointLabelFormatter == null) {
            this.pointLabelFormatter = new PointLabelFormatter();
        }
        return this.pointLabelFormatter;
    }

    public void setPointLabelFormatter(PointLabelFormatter pointLabelFormatter2) {
        this.pointLabelFormatter = pointLabelFormatter2;
    }
}
