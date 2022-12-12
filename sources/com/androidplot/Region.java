package com.androidplot;

import com.androidplot.util.FastNumber;

public class Region {
    private FastNumber cachedLength;
    private Region defaults = this;
    private FastNumber max;
    private FastNumber min;

    public Region() {
    }

    public static Region withDefaults(Region defaults2) {
        if (defaults2 == null || !defaults2.isDefined()) {
            throw new IllegalArgumentException("When specifying default min and max must both be non-null values");
        }
        Region r = new Region();
        r.defaults = defaults2;
        return r;
    }

    public Region(Number v1, Number v2) {
        if (v1 == null || v2 == null || v1.doubleValue() >= v2.doubleValue()) {
            setMin(v2);
            setMax(v1);
            return;
        }
        setMin(v1);
        setMax(v2);
    }

    public void setMinMax(Region region) {
        setMin(region.getMin());
        setMax(region.getMax());
    }

    public static Number measure(Number v1, Number v2) {
        return new Region(v1, v2).length();
    }

    public Number length() {
        Number l;
        if (this.cachedLength == null) {
            if (getMax() == null || getMin() == null) {
                l = null;
            } else {
                l = Double.valueOf(getMax().doubleValue() - getMin().doubleValue());
            }
            if (l != null) {
                this.cachedLength = FastNumber.orNull(l);
            }
        }
        return this.cachedLength;
    }

    public boolean contains(Number value) {
        return value.doubleValue() >= getMin().doubleValue() && value.doubleValue() <= getMax().doubleValue();
    }

    public boolean intersects(Region region) {
        return intersects(region.getMin(), region.getMax());
    }

    public Number center() {
        return Double.valueOf(getMax().doubleValue() - (length().doubleValue() / 2.0d));
    }

    public Number transform(double value, Region region2) {
        return transform(value, region2, false);
    }

    public Number transform(double value, Region region2, boolean flip) {
        return Double.valueOf(transform(value, region2.getMin().doubleValue(), region2.getMax().doubleValue(), flip));
    }

    public double transform(double value, double min2, double max2, boolean flip) {
        double scale = (max2 - min2) / length().doubleValue();
        if (!flip) {
            return ((value - getMin().doubleValue()) * scale) + min2;
        }
        return max2 - ((value - getMin().doubleValue()) * scale);
    }

    public Number ratio(Region r2) {
        return Double.valueOf(ratio(r2.getMin().doubleValue(), r2.getMax().doubleValue()));
    }

    public double ratio(double min2, double max2) {
        return length().doubleValue() / (max2 - min2);
    }

    public void union(Number value) {
        if (value != null) {
            double val = value.doubleValue();
            if (getMin() == null || val < getMin().doubleValue()) {
                setMin(value);
            }
            if (getMax() == null || val > getMax().doubleValue()) {
                setMax(value);
            }
        }
    }

    public void union(Region input) {
        union(input.getMin());
        union(input.getMax());
    }

    public void intersect(Region input) {
        if (getMin().doubleValue() < input.getMin().doubleValue()) {
            setMin(input.getMin());
        }
        if (getMax().doubleValue() > input.getMax().doubleValue()) {
            setMax(input.getMax());
        }
    }

    public boolean intersects(Number line2Min, Number line2Max) {
        if ((line2Min.doubleValue() > getMin().doubleValue() || line2Max.doubleValue() < getMax().doubleValue()) && !contains(line2Min) && !contains(line2Max)) {
            return false;
        }
        return true;
    }

    public boolean isMinSet() {
        return this.min != null;
    }

    public Number getMin() {
        return isMinSet() ? this.min : this.defaults.min;
    }

    public void setMin(Number min2) {
        this.cachedLength = null;
        if (min2 == null) {
            if (this.defaults == null) {
                throw new NullPointerException("Region values cannot be null unless defaults have been set.");
            }
            this.min = null;
        } else if (this.min == null || !this.min.equals(min2)) {
            this.min = FastNumber.orNull(min2);
        }
    }

    public boolean isMaxSet() {
        return this.max != null;
    }

    public Number getMax() {
        return isMaxSet() ? this.max : this.defaults.max;
    }

    public void setMax(Number max2) {
        this.cachedLength = null;
        if (max2 == null) {
            if (this.defaults == null) {
                throw new NullPointerException("Region values can never be null unless defaults have been set.");
            }
            this.max = null;
        } else if (this.max == null || !this.max.equals(max2)) {
            this.max = FastNumber.orNull(max2);
        }
    }

    public boolean isDefined() {
        return (this.min == null || this.max == null) ? false : true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Region{");
        sb.append("min=").append(this.min);
        sb.append(", max=").append(this.max);
        sb.append(", cachedLength=").append(this.cachedLength);
        sb.append(", defaults=");
        if (this.defaults != this) {
            sb.append(this.defaults);
        } else {
            sb.append("this");
        }
        sb.append('}');
        return sb.toString();
    }
}
