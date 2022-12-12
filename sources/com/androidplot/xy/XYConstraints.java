package com.androidplot.xy;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class XYConstraints {
    private XYFramingModel domainFramingModel;
    private BoundaryMode domainLowerBoundaryMode;
    private BoundaryMode domainUpperBoundaryMode;
    private Number maxX;
    private Number maxY;
    private Number minX;
    private Number minY;
    private XYFramingModel rangeFramingModel;
    private BoundaryMode rangeLowerBoundaryMode;
    private BoundaryMode rangeUpperBoundaryMode;

    public XYConstraints() {
        this((Number) null, (Number) null, (Number) null, (Number) null);
    }

    public XYConstraints(@Nullable Number minX2, @Nullable Number maxX2, @Nullable Number minY2, @Nullable Number maxY2) {
        this.domainFramingModel = XYFramingModel.EDGE;
        this.rangeFramingModel = XYFramingModel.EDGE;
        this.domainUpperBoundaryMode = BoundaryMode.AUTO;
        this.domainLowerBoundaryMode = BoundaryMode.AUTO;
        this.rangeUpperBoundaryMode = BoundaryMode.AUTO;
        this.rangeLowerBoundaryMode = BoundaryMode.AUTO;
        this.minX = minX2;
        this.minY = minY2;
        this.maxX = maxX2;
        this.maxY = maxY2;
    }

    public boolean contains(@NonNull RectRegion rectRegion) {
        return contains(rectRegion.getMinY(), rectRegion.getMinY()) && contains(rectRegion.getMaxX(), rectRegion.getMaxY());
    }

    public boolean contains(Number x, Number y) {
        if (x == null || y == null) {
            return false;
        }
        if (this.minX == null && this.maxX == null && this.minY == null && this.maxY == null) {
            return true;
        }
        double dx = x.doubleValue();
        if (this.minX != null && dx < this.minX.doubleValue()) {
            return false;
        }
        if (this.maxX != null && dx > this.maxX.doubleValue()) {
            return false;
        }
        double dy = y.doubleValue();
        if (this.minY != null && dy < this.minY.doubleValue()) {
            return false;
        }
        if (this.maxY == null || dy <= this.maxY.doubleValue()) {
            return true;
        }
        return false;
    }

    @Nullable
    public Number getMinX() {
        return this.minX;
    }

    @Nullable
    public Number getMaxX() {
        return this.maxX;
    }

    @Nullable
    public Number getMinY() {
        return this.minY;
    }

    @Nullable
    public Number getMaxY() {
        return this.maxY;
    }

    public void setMinX(@Nullable Number minX2) {
        this.minX = minX2;
    }

    public void setMaxX(@Nullable Number maxX2) {
        this.maxX = maxX2;
    }

    public void setMinY(@Nullable Number minY2) {
        this.minY = minY2;
    }

    public void setMaxY(@Nullable Number maxY2) {
        this.maxY = maxY2;
    }

    @NonNull
    public XYFramingModel getDomainFramingModel() {
        return this.domainFramingModel;
    }

    public void setDomainFramingModel(@NonNull XYFramingModel domainFramingModel2) {
        this.domainFramingModel = domainFramingModel2;
    }

    @NonNull
    public XYFramingModel getRangeFramingModel() {
        return this.rangeFramingModel;
    }

    public void setRangeFramingModel(@NonNull XYFramingModel rangeFramingModel2) {
        this.rangeFramingModel = rangeFramingModel2;
    }

    @NonNull
    public BoundaryMode getDomainUpperBoundaryMode() {
        return this.domainUpperBoundaryMode;
    }

    public void setDomainUpperBoundaryMode(@NonNull BoundaryMode domainUpperBoundaryMode2) {
        this.domainUpperBoundaryMode = domainUpperBoundaryMode2;
    }

    @NonNull
    public BoundaryMode getDomainLowerBoundaryMode() {
        return this.domainLowerBoundaryMode;
    }

    public void setDomainLowerBoundaryMode(@NonNull BoundaryMode domainLowerBoundaryMode2) {
        this.domainLowerBoundaryMode = domainLowerBoundaryMode2;
    }

    @NonNull
    public BoundaryMode getRangeUpperBoundaryMode() {
        return this.rangeUpperBoundaryMode;
    }

    public void setRangeUpperBoundaryMode(@NonNull BoundaryMode rangeUpperBoundaryMode2) {
        this.rangeUpperBoundaryMode = rangeUpperBoundaryMode2;
    }

    @NonNull
    public BoundaryMode getRangeLowerBoundaryMode() {
        return this.rangeLowerBoundaryMode;
    }

    public void setRangeLowerBoundaryMode(@NonNull BoundaryMode rangeLowerBoundaryMode2) {
        this.rangeLowerBoundaryMode = rangeLowerBoundaryMode2;
    }

    public String toString() {
        return "XYConstraints{domainFramingModel=" + this.domainFramingModel + ", rangeFramingModel=" + this.rangeFramingModel + ", domainUpperBoundaryMode=" + this.domainUpperBoundaryMode + ", domainLowerBoundaryMode=" + this.domainLowerBoundaryMode + ", rangeUpperBoundaryMode=" + this.rangeUpperBoundaryMode + ", rangeLowerBoundaryMode=" + this.rangeLowerBoundaryMode + ", minX=" + this.minX + ", maxX=" + this.maxX + ", minY=" + this.minY + ", maxY=" + this.maxY + '}';
    }
}
