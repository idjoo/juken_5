package com.androidplot.xy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CandlestickSeries {
    private SimpleXYSeries closeSeries;
    private SimpleXYSeries highSeries;
    private SimpleXYSeries lowSeries;
    private SimpleXYSeries openSeries;

    protected static List<Number> generateRange(int start, int end) {
        List<Number> range = new ArrayList<>(end - start);
        for (int i = start; i < end; i++) {
            range.add(Integer.valueOf(i));
        }
        return range;
    }

    public CandlestickSeries(Item... items) {
        this((List<Item>) Arrays.asList(items));
    }

    public CandlestickSeries(List<Item> items) {
        this(generateRange(0, items.size()), items);
    }

    public CandlestickSeries(List<Number> xVals, List<Item> items) {
        this.highSeries = new SimpleXYSeries((String) null);
        this.lowSeries = new SimpleXYSeries((String) null);
        this.openSeries = new SimpleXYSeries((String) null);
        this.closeSeries = new SimpleXYSeries((String) null);
        if (xVals.size() != items.size()) {
            throw new IllegalArgumentException("xVals and yVals length must be identical.");
        }
        for (int i = 0; i < xVals.size(); i++) {
            Number x = xVals.get(i);
            this.highSeries.addLast(x, Double.valueOf(items.get(i).getHigh()));
            this.lowSeries.addLast(x, Double.valueOf(items.get(i).getLow()));
            this.openSeries.addLast(x, Double.valueOf(items.get(i).getOpen()));
            this.closeSeries.addLast(x, Double.valueOf(items.get(i).getClose()));
        }
    }

    public SimpleXYSeries getHighSeries() {
        return this.highSeries;
    }

    public void setHighSeries(SimpleXYSeries highSeries2) {
        this.highSeries = highSeries2;
    }

    public SimpleXYSeries getLowSeries() {
        return this.lowSeries;
    }

    public void setLowSeries(SimpleXYSeries lowSeries2) {
        this.lowSeries = lowSeries2;
    }

    public SimpleXYSeries getOpenSeries() {
        return this.openSeries;
    }

    public void setOpenSeries(SimpleXYSeries openSeries2) {
        this.openSeries = openSeries2;
    }

    public SimpleXYSeries getCloseSeries() {
        return this.closeSeries;
    }

    public void setCloseSeries(SimpleXYSeries closeSeries2) {
        this.closeSeries = closeSeries2;
    }

    public static class Item {
        private double close;
        private double high;
        private double low;
        private double open;

        public Item(double low2, double high2, double open2, double close2) {
            this.low = low2;
            this.high = high2;
            this.open = open2;
            this.close = close2;
        }

        public double getLow() {
            return this.low;
        }

        public void setLow(double low2) {
            this.low = low2;
        }

        public double getHigh() {
            return this.high;
        }

        public void setHigh(double high2) {
            this.high = high2;
        }

        public double getOpen() {
            return this.open;
        }

        public void setOpen(double open2) {
            this.open = open2;
        }

        public double getClose() {
            return this.close;
        }

        public void setClose(double close2) {
            this.close = close2;
        }
    }
}
