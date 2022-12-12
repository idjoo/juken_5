package com.androidplot;

import com.androidplot.Series;
import com.androidplot.ui.Formatter;
import com.androidplot.ui.SeriesBundle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class SeriesRegistry<BundleType extends SeriesBundle<SeriesType, FormatterType>, SeriesType extends Series, FormatterType extends Formatter> implements Serializable {
    private ArrayList<BundleType> registry = new ArrayList<>();

    /* access modifiers changed from: protected */
    public abstract BundleType newSeriesBundle(SeriesType seriestype, FormatterType formattertype);

    public List<BundleType> getSeriesAndFormatterList() {
        return this.registry;
    }

    public List<SeriesType> getSeriesList() {
        List<SeriesType> result = new ArrayList<>(this.registry.size());
        Iterator<BundleType> it = this.registry.iterator();
        while (it.hasNext()) {
            result.add(it.next().getSeries());
        }
        return result;
    }

    public int size() {
        return this.registry.size();
    }

    public boolean isEmpty() {
        return this.registry.isEmpty();
    }

    public boolean add(SeriesType series, FormatterType formatter) {
        if (series != null && formatter != null) {
            return this.registry.add(newSeriesBundle(series, formatter));
        }
        throw new IllegalArgumentException("Neither series nor formatter param may be null.");
    }

    /* access modifiers changed from: protected */
    public List<SeriesBundle<SeriesType, FormatterType>> get(SeriesType series) {
        List<SeriesBundle<SeriesType, FormatterType>> results = new ArrayList<>();
        Iterator<BundleType> it = this.registry.iterator();
        while (it.hasNext()) {
            SeriesBundle<SeriesType, FormatterType> thisPair = it.next();
            if (thisPair.getSeries() == series) {
                results.add(thisPair);
            }
        }
        return results;
    }

    public synchronized List<BundleType> remove(SeriesType series, Class rendererClass) {
        ArrayList<BundleType> removedItems;
        removedItems = new ArrayList<>();
        Iterator<BundleType> it = this.registry.iterator();
        while (it.hasNext()) {
            BundleType b = (SeriesBundle) it.next();
            if (b.getSeries() == series && b.getFormatter().getRendererClass() == rendererClass) {
                it.remove();
                removedItems.add(b);
            }
        }
        return removedItems;
    }

    public synchronized boolean remove(SeriesType series) {
        boolean result;
        result = false;
        Iterator<BundleType> it = this.registry.iterator();
        while (it.hasNext()) {
            if (((SeriesBundle) it.next()).getSeries() == series) {
                it.remove();
                result = true;
            }
        }
        return result;
    }

    public void clear() {
        Iterator<BundleType> it = this.registry.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    public List<SeriesBundle<SeriesType, FormatterType>> getLegendEnabledItems() {
        List<SeriesBundle<SeriesType, FormatterType>> sfList = new ArrayList<>();
        Iterator<BundleType> it = this.registry.iterator();
        while (it.hasNext()) {
            SeriesBundle<SeriesType, FormatterType> sf = it.next();
            if (sf.getFormatter().isLegendIconEnabled()) {
                sfList.add(sf);
            }
        }
        return sfList;
    }

    public boolean contains(SeriesType series, Class<? extends FormatterType> formatterClass) {
        Iterator<BundleType> it = this.registry.iterator();
        while (it.hasNext()) {
            BundleType b = (SeriesBundle) it.next();
            if (b.getFormatter().getClass() == formatterClass && b.getSeries() == series) {
                return true;
            }
        }
        return false;
    }
}
