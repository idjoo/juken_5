package com.androidplot.ui;

import com.androidplot.Plot;
import com.androidplot.Series;
import com.androidplot.ui.Formatter;
import java.util.ArrayList;
import java.util.Iterator;

public class RenderStack<SeriesType extends Series, FormatterType extends Formatter> {
    private final ArrayList<RenderStack<SeriesType, FormatterType>.StackElement<SeriesType, FormatterType>> elements;
    private final Plot plot;

    public ArrayList<RenderStack<SeriesType, FormatterType>.StackElement<SeriesType, FormatterType>> getElements() {
        return this.elements;
    }

    public class StackElement<SeriesType extends Series, FormatterType extends Formatter> {
        private boolean isEnabled = true;
        private SeriesBundle<SeriesType, FormatterType> seriesBundle;

        public StackElement(SeriesBundle<SeriesType, FormatterType> seriesBundle2) {
            set(seriesBundle2);
        }

        public SeriesBundle<SeriesType, FormatterType> get() {
            return this.seriesBundle;
        }

        public void set(SeriesBundle<SeriesType, FormatterType> seriesBundle2) {
            this.seriesBundle = seriesBundle2;
        }

        public boolean isEnabled() {
            return this.isEnabled;
        }

        public void setEnabled(boolean isEnabled2) {
            this.isEnabled = isEnabled2;
        }
    }

    public RenderStack(Plot plot2) {
        this.plot = plot2;
        this.elements = new ArrayList<>(plot2.getRegistry().size());
    }

    public void sync() {
        getElements().clear();
        for (SeriesBundle<SeriesType, FormatterType> thisPair : this.plot.getRegistry().getSeriesAndFormatterList()) {
            getElements().add(new StackElement(thisPair));
        }
    }

    public void disable(Class<? extends SeriesRenderer> rendererClass) {
        Iterator it = getElements().iterator();
        while (it.hasNext()) {
            StackElement element = (StackElement) it.next();
            if (element.get().getFormatter().getRendererClass() == rendererClass) {
                element.setEnabled(false);
            }
        }
    }
}
