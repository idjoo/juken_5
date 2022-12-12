package com.androidplot.xy;

import android.graphics.PointF;
import android.graphics.RectF;
import com.androidplot.Region;
import java.util.ArrayList;
import java.util.List;

public class RectRegion {
    private String label;
    Region xRegion;
    Region yRegion;

    public RectRegion() {
        this.xRegion = new Region();
        this.yRegion = new Region();
    }

    public static RectRegion withDefaults(RectRegion defaults) {
        if (defaults == null || !defaults.isFullyDefined()) {
            throw new IllegalArgumentException("When specifying defaults, RectRegion param must contain no null values.");
        }
        RectRegion r = new RectRegion();
        r.xRegion = Region.withDefaults(defaults.getxRegion());
        r.yRegion = Region.withDefaults(defaults.getyRegion());
        return r;
    }

    public RectRegion(XYCoords min, XYCoords max) {
        this(min.x, max.x, min.y, max.y);
    }

    public RectRegion(Number minX, Number maxX, Number minY, Number maxY, String label2) {
        this.xRegion = new Region(minX, maxX);
        this.yRegion = new Region(minY, maxY);
        setLabel(label2);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public RectRegion(RectF rect) {
        this(Float.valueOf(rect.left < rect.right ? rect.left : rect.right), Float.valueOf(rect.right > rect.left ? rect.right : rect.left), Float.valueOf(rect.bottom < rect.top ? rect.bottom : rect.top), Float.valueOf(rect.top > rect.bottom ? rect.top : rect.bottom));
    }

    public RectRegion(Number minX, Number maxX, Number minY, Number maxY) {
        this(minX, maxX, minY, maxY, (String) null);
    }

    public XYCoords transform(Number x, Number y, RectRegion region2, boolean flipX, boolean flipY) {
        return new XYCoords(this.xRegion.transform(x.doubleValue(), region2.xRegion, flipX), this.yRegion.transform(y.doubleValue(), region2.yRegion, flipY));
    }

    public XYCoords transform(Number x, Number y, RectRegion region2) {
        return transform(x, y, region2, false, false);
    }

    public XYCoords transform(XYCoords value, RectRegion region2) {
        return transform(value.x, value.y, region2);
    }

    public RectRegion transform(RectRegion r, RectRegion r2, boolean flipX, boolean flipY) {
        return new RectRegion(transform(r.getMinX(), r.getMinY(), r2, flipX, flipY), transform(r.getMaxX(), r.getMaxY(), r2, flipX, flipY));
    }

    public PointF transformScreen(Number x, Number y, RectF region2) {
        return transform(x, y, region2, false, true);
    }

    public void transformScreen(PointF result, Number x, Number y, RectF region2) {
        transform(result, x, y, region2, false, true);
    }

    public void transform(PointF result, Number x, Number y, RectF region2, boolean flipX, boolean flipY) {
        result.x = (float) this.xRegion.transform(x.doubleValue(), (double) region2.left, (double) region2.right, flipX);
        result.y = (float) this.yRegion.transform(y.doubleValue(), (double) region2.top, (double) region2.bottom, flipY);
    }

    public PointF transform(Number x, Number y, RectF region2, boolean flipX, boolean flipY) {
        PointF result = new PointF();
        transform(result, x, y, region2, flipX, flipY);
        return result;
    }

    public PointF transformScreen(XYCoords value, RectF region2) {
        return transform(value, region2, false, true);
    }

    public PointF transform(XYCoords value, RectF region2, boolean flipX, boolean flipY) {
        return transform(value.x, value.y, region2, flipX, flipY);
    }

    public void union(Number x, Number y) {
        this.xRegion.union(x);
        this.yRegion.union(y);
    }

    public void union(RectRegion input) {
        this.xRegion.union(input.xRegion);
        this.yRegion.union(input.yRegion);
    }

    public boolean intersects(RectRegion region) {
        return intersects(region.getMinX(), region.getMaxX(), region.getMinY(), region.getMaxY());
    }

    public boolean intersects(Number minX, Number maxX, Number minY, Number maxY) {
        return this.xRegion.intersects(minX, maxX) && this.yRegion.intersects(minY, maxY);
    }

    public RectF asRectF() {
        return new RectF(getMinX().floatValue(), getMinY().floatValue(), getMaxX().floatValue(), getMaxY().floatValue());
    }

    public void intersect(RectRegion clippingBounds) {
        if (intersects(clippingBounds)) {
            this.xRegion.intersect(clippingBounds.xRegion);
            this.yRegion.intersect(clippingBounds.yRegion);
            return;
        }
        setMinY((Number) null);
        setMaxY((Number) null);
        setMinX((Number) null);
        setMaxX((Number) null);
    }

    public List<RectRegion> intersects(List<RectRegion> regions) {
        ArrayList<RectRegion> intersectingRegions = new ArrayList<>();
        for (RectRegion r : regions) {
            if (r.intersects(getMinX(), getMaxX(), getMinY(), getMaxY())) {
                intersectingRegions.add(r);
            }
        }
        return intersectingRegions;
    }

    public Number getWidth() {
        return distanceBetween(getMinX(), getMaxX());
    }

    public Number getHeight() {
        return distanceBetween(getMinY(), getMaxY());
    }

    private static Number distanceBetween(Number x, Number y) {
        return Double.valueOf(Math.abs(x.doubleValue() - y.doubleValue()));
    }

    public void set(Number minX, Number maxX, Number minY, Number maxY) {
        setMinX(minX);
        setMaxX(maxX);
        setMinY(minY);
        setMaxY(maxY);
    }

    public boolean isMinXSet() {
        return this.xRegion.isMinSet();
    }

    public Number getMinX() {
        return this.xRegion.getMin();
    }

    public void setMinX(Number minX) {
        this.xRegion.setMin(minX);
    }

    public boolean isMaxXSet() {
        return this.xRegion.isMaxSet();
    }

    public Number getMaxX() {
        return this.xRegion.getMax();
    }

    public void setMaxX(Number maxX) {
        this.xRegion.setMax(maxX);
    }

    public boolean isMinYSet() {
        return this.yRegion.isMinSet();
    }

    public Number getMinY() {
        return this.yRegion.getMin();
    }

    public void setMinY(Number minY) {
        this.yRegion.setMin(minY);
    }

    public boolean isMaxYSet() {
        return this.yRegion.isMaxSet();
    }

    public Number getMaxY() {
        return this.yRegion.getMax();
    }

    public void setMaxY(Number maxY) {
        this.yRegion.setMax(maxY);
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label2) {
        this.label = label2;
    }

    public Region getxRegion() {
        return this.xRegion;
    }

    public void setxRegion(Region xRegion2) {
        this.xRegion = xRegion2;
    }

    public Region getyRegion() {
        return this.yRegion;
    }

    public void setyRegion(Region yRegion2) {
        this.yRegion = yRegion2;
    }

    public boolean isFullyDefined() {
        return this.xRegion.isDefined() && this.yRegion.isDefined();
    }

    public boolean contains(Number x, Number y) {
        return getxRegion().contains(x) && getyRegion().contains(y);
    }

    public String toString() {
        return "RectRegion{xRegion=" + this.xRegion + ", yRegion=" + this.yRegion + ", label='" + this.label + '\'' + '}';
    }
}
