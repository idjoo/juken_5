package com.androidplot.util;

import com.androidplot.Region;
import com.androidplot.xy.FastXYSeries;
import com.androidplot.xy.OrderedXYSeries;
import com.androidplot.xy.RectRegion;
import com.androidplot.xy.XYConstraints;
import com.androidplot.xy.XYSeries;
import java.util.List;

public class SeriesUtils {
    public static RectRegion minMax(List<XYSeries> seriesList) {
        return minMax((XYConstraints) null, seriesList);
    }

    public static RectRegion minMax(XYSeries... seriesList) {
        return minMax((XYConstraints) null, seriesList);
    }

    public static Region minMaxX(XYSeries... seriesList) {
        Region bounds = new Region();
        for (XYSeries series : seriesList) {
            for (int i = 0; i < series.size(); i++) {
                bounds.union(series.getX(i));
            }
        }
        return bounds;
    }

    public static Region minMaxY(XYSeries... seriesList) {
        Region bounds = new Region();
        for (XYSeries series : seriesList) {
            for (int i = 0; i < series.size(); i++) {
                bounds.union(series.getY(i));
            }
        }
        return bounds;
    }

    public static RectRegion minMax(XYConstraints constraints, List<XYSeries> seriesList) {
        return minMax(constraints, (XYSeries[]) seriesList.toArray(new XYSeries[seriesList.size()]));
    }

    public static RectRegion minMax(XYConstraints constraints, XYSeries... seriesArray) {
        RectRegion bounds = new RectRegion();
        if (seriesArray != null && seriesArray.length > 0) {
            for (FastXYSeries fastXYSeries : seriesArray) {
                if (fastXYSeries instanceof FastXYSeries) {
                    RectRegion b = fastXYSeries.minMax();
                    if (b != null) {
                        if (constraints == null || constraints.contains(b)) {
                            bounds.union(b);
                        }
                    }
                }
                for (int i = 0; i < fastXYSeries.size(); i++) {
                    Number xi = fastXYSeries.getX(i);
                    Number yi = fastXYSeries.getY(i);
                    if (constraints == null || constraints.contains(xi, yi)) {
                        bounds.union(xi, yi);
                    }
                }
            }
        }
        return bounds;
    }

    public static Region minMax(Region bounds, List<Number>... lists) {
        for (List<Number> list : lists) {
            for (Number i : list) {
                bounds.union(i);
            }
        }
        return bounds;
    }

    public static Region iBounds(XYSeries series, RectRegion visibleBounds) {
        float step = series.size() >= 200 ? 50.0f : 1.0f;
        return new Region(Integer.valueOf(iBoundsMin(series, visibleBounds.getMinX().doubleValue(), step)), Integer.valueOf(iBoundsMax(series, visibleBounds.getMaxX().doubleValue(), step)));
    }

    protected static int iBoundsMax(XYSeries series, double visibleMax, float step) {
        Number thisX;
        int max = series.size() - 1;
        int seriesSize = series.size();
        for (int stepIndex = (int) Math.ceil((double) (((float) seriesSize) / step)); stepIndex >= 0; stepIndex--) {
            int i = stepIndex * ((int) step);
            int ii = 0;
            while (true) {
                if (((float) ii) >= step) {
                    break;
                }
                int iii = i + ii;
                if (iii >= seriesSize || (thisX = series.getX(iii)) == null) {
                    ii++;
                } else {
                    double thisDouble = thisX.doubleValue();
                    if (thisDouble > visibleMax) {
                        max = iii;
                    } else if (thisDouble == visibleMax) {
                        return iii;
                    } else {
                        return max;
                    }
                }
            }
        }
        return max;
    }

    protected static int iBoundsMin(XYSeries series, double visibleMin, float step) {
        int iii;
        Number thisX;
        int min = 0;
        int steps = (int) Math.ceil((double) (((float) series.size()) / step));
        for (int stepIndex = 1; stepIndex <= steps; stepIndex++) {
            int i = stepIndex * ((int) step);
            int ii = 1;
            while (true) {
                if (((float) ii) > step || (iii = i - ii) < 0) {
                    break;
                } else if (iii >= series.size() || (thisX = series.getX(iii)) == null) {
                    ii++;
                } else if (thisX.doubleValue() < visibleMin) {
                    min = iii;
                } else if (thisX.doubleValue() == visibleMin) {
                    return iii;
                } else {
                    return min;
                }
            }
        }
        return min;
    }

    protected static Region getNullRegion(XYSeries series, int index) {
        Region region = new Region();
        if (series.getX(index) != null) {
            throw new IllegalArgumentException("Attempt to find null region for non null index: " + index);
        }
        int i = index - 1;
        while (true) {
            if (i < 0) {
                break;
            } else if (series.getX(i) != null) {
                region.setMin(Integer.valueOf(i));
                break;
            } else {
                i--;
            }
        }
        int i2 = index + 1;
        while (true) {
            if (i2 >= series.size()) {
                break;
            } else if (series.getX(i2) != null) {
                region.setMax(Integer.valueOf(i2));
                break;
            } else {
                i2++;
            }
        }
        return region;
    }

    public static Region minMax(List<Number>... lists) {
        return minMax(new Region(), lists);
    }

    public static OrderedXYSeries.XOrder getXYOrder(XYSeries series) {
        return series instanceof OrderedXYSeries ? ((OrderedXYSeries) series).getXOrder() : OrderedXYSeries.XOrder.NONE;
    }
}
