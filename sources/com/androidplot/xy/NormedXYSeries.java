package com.androidplot.xy;

import com.androidplot.Region;
import com.androidplot.util.SeriesUtils;

public class NormedXYSeries implements XYSeries {
    private Region minMaxX;
    private Region minMaxY;
    private XYSeries rawData;
    private Region transformX;
    private Region transformY;

    public static class Norm {
        final Region minMax;
        final double offset;
        final boolean useOffsetCompression;

        public Norm(Region minMax2) {
            this(minMax2, 0.0d, false);
        }

        public Norm(Region minMax2, double offset2, boolean useOffsetCompression2) {
            this.minMax = minMax2;
            this.offset = offset2;
            this.useOffsetCompression = useOffsetCompression2;
            if (!useOffsetCompression2) {
                return;
            }
            if (offset2 <= -1.0d || offset2 >= 1.0d) {
                throw new IllegalArgumentException("When useOffsetCompression is true, offset must be > -1 and < 1.");
            }
        }
    }

    public NormedXYSeries(XYSeries rawData2) {
        this(rawData2, (Norm) null, new Norm((Region) null, 0.0d, false));
    }

    public NormedXYSeries(XYSeries rawData2, Norm x, Norm y) {
        this.rawData = rawData2;
        normalize(x, y);
    }

    /* access modifiers changed from: protected */
    public void normalize(Norm x, Norm y) {
        Region minMaxY2;
        Region minMaxX2;
        if (x != null) {
            if (x.minMax != null) {
                minMaxX2 = x.minMax;
            } else {
                minMaxX2 = SeriesUtils.minMaxX(this.rawData);
            }
            this.minMaxX = minMaxX2;
            this.transformX = calculateTransform(x);
        }
        if (y != null) {
            if (y.minMax != null) {
                minMaxY2 = y.minMax;
            } else {
                minMaxY2 = SeriesUtils.minMaxY(this.rawData);
            }
            this.minMaxY = minMaxY2;
            this.transformY = calculateTransform(y);
        }
    }

    /* access modifiers changed from: protected */
    public Region calculateTransform(Norm norm) {
        double d;
        double d2;
        if (!norm.useOffsetCompression) {
            return new Region(Double.valueOf(0.0d + norm.offset), Double.valueOf(norm.offset + 1.0d));
        }
        if (norm.offset > 0.0d) {
            d = norm.offset;
        } else {
            d = 0.0d;
        }
        Double valueOf = Double.valueOf(d);
        if (norm.offset < 0.0d) {
            d2 = norm.offset + 1.0d;
        } else {
            d2 = 1.0d;
        }
        return new Region(valueOf, Double.valueOf(d2));
    }

    public String getTitle() {
        return this.rawData.getTitle();
    }

    public int size() {
        return this.rawData.size();
    }

    public Number denormalizeXVal(Number xVal) {
        if (xVal != null) {
            return this.transformX.transform(xVal.doubleValue(), this.minMaxX);
        }
        return null;
    }

    public Number denormalizeYVal(Number yVal) {
        if (yVal != null) {
            return this.transformY.transform(yVal.doubleValue(), this.minMaxY);
        }
        return null;
    }

    public Number getX(int index) {
        Number xVal = this.rawData.getX(index);
        if (xVal == null || this.transformX == null) {
            return xVal;
        }
        return this.minMaxX.transform(xVal.doubleValue(), this.transformX);
    }

    public Number getY(int index) {
        Number yVal = this.rawData.getY(index);
        if (yVal == null || this.transformY == null) {
            return yVal;
        }
        return this.minMaxY.transform(yVal.doubleValue(), this.transformY);
    }
}
