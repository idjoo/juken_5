package com.androidplot.xy;

public class ScalingXYSeries implements XYSeries {
    private Mode mode;
    private double scale;
    private XYSeries series;

    public enum Mode {
        X_ONLY,
        Y_ONLY,
        X_AND_Y
    }

    public ScalingXYSeries(XYSeries series2, double scale2, Mode mode2) {
        this.series = series2;
        this.scale = scale2;
        this.mode = mode2;
    }

    public String getTitle() {
        return this.series.getTitle();
    }

    public int size() {
        return this.series.size();
    }

    public Number getX(int index) {
        Number x = this.series.getX(index);
        if (this.mode != Mode.X_ONLY && this.mode != Mode.X_AND_Y) {
            return x;
        }
        return x == null ? null : Double.valueOf(x.doubleValue() * this.scale);
    }

    public Number getY(int index) {
        Number y = this.series.getY(index);
        if (this.mode != Mode.Y_ONLY && this.mode != Mode.X_AND_Y) {
            return y;
        }
        return y == null ? null : Double.valueOf(y.doubleValue() * this.scale);
    }

    public double getScale() {
        return this.scale;
    }

    public void setScale(double scale2) {
        this.scale = scale2;
    }
}
