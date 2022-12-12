package com.androidplot.ui;

import android.content.Context;
import com.androidplot.Plot;
import com.halfhp.fig.Fig;
import com.halfhp.fig.FigException;

public abstract class Formatter<PlotType extends Plot> {
    private boolean isLegendIconEnabled = true;

    /* access modifiers changed from: protected */
    public abstract SeriesRenderer doGetRendererInstance(PlotType plottype);

    public abstract Class<? extends SeriesRenderer> getRendererClass();

    public Formatter() {
    }

    public Formatter(Context ctx, int xmlCfgId) {
        configure(ctx, xmlCfgId);
    }

    public void configure(Context ctx, int xmlCfgId) {
        try {
            Fig.configure(ctx, (Object) this, xmlCfgId);
        } catch (FigException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends SeriesRenderer> T getRendererInstance(PlotType plot) {
        return doGetRendererInstance(plot);
    }

    public boolean isLegendIconEnabled() {
        return this.isLegendIconEnabled;
    }

    public void setLegendIconEnabled(boolean legendIconEnabled) {
        this.isLegendIconEnabled = legendIconEnabled;
    }
}
