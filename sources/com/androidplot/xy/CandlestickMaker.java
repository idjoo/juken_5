package com.androidplot.xy;

public abstract class CandlestickMaker {
    static final /* synthetic */ boolean $assertionsDisabled = (!CandlestickMaker.class.desiredAssertionStatus());

    public static void make(XYPlot plot, CandlestickFormatter formatter, XYSeries openVals, XYSeries closeVals, XYSeries highVals, XYSeries lowVals) {
        plot.addSeries(formatter, (SeriesType[]) new XYSeries[]{highVals, lowVals, openVals, closeVals});
    }

    public static void make(XYPlot plot, CandlestickFormatter formatter, CandlestickSeries series) {
        make(plot, formatter, series.getOpenSeries(), series.getCloseSeries(), series.getHighSeries(), series.getLowSeries());
    }

    public static void check(CandlestickSeries series) {
        check(series.getOpenSeries(), series.getCloseSeries(), series.getHighSeries(), series.getLowSeries());
    }

    public static void check(XYSeries openVals, XYSeries closeVals, XYSeries highVals, XYSeries lowVals) {
        int size = openVals.size();
        if (!$assertionsDisabled && closeVals.size() != size) {
            throw new AssertionError("closeVals has irregular size.");
        } else if (!$assertionsDisabled && highVals.size() != size) {
            throw new AssertionError("highVals has irregular size.");
        } else if ($assertionsDisabled || lowVals.size() == size) {
            int i = 0;
            while (i < size) {
                double highVal = highVals.getY(i).doubleValue();
                double lowVal = lowVals.getY(i).doubleValue();
                double openVal = openVals.getY(i).doubleValue();
                double closeVal = closeVals.getY(i).doubleValue();
                if (!$assertionsDisabled && openVal > highVal) {
                    throw new AssertionError("Detected openVal > highVal at index " + i);
                } else if (!$assertionsDisabled && openVal < lowVal) {
                    throw new AssertionError("Detected openVal < lowVal at index " + i);
                } else if (!$assertionsDisabled && closeVal > highVal) {
                    throw new AssertionError("Detected closeVal > highVal at index " + i);
                } else if (!$assertionsDisabled && closeVal < lowVal) {
                    throw new AssertionError("Detected closeVal < lowVal at index " + i);
                } else if ($assertionsDisabled || lowVal <= highVal) {
                    i++;
                } else {
                    throw new AssertionError("Detected lowVal > highVal at index " + i);
                }
            }
        } else {
            throw new AssertionError("lowVals has irregular size.");
        }
    }
}
