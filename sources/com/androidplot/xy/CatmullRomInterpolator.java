package com.androidplot.xy;

import java.util.ArrayList;
import java.util.List;

public class CatmullRomInterpolator implements Interpolator<Params> {

    public enum Type {
        Uniform,
        Centripetal
    }

    public static class Params implements InterpolationParams {
        private int pointPerSegment;
        private Type type;

        public Params(int pointPerSegment2, Type type2) {
            this.pointPerSegment = pointPerSegment2;
            this.type = type2;
        }

        public Class<CatmullRomInterpolator> getInterpolatorClass() {
            return CatmullRomInterpolator.class;
        }

        public int getPointPerSegment() {
            return this.pointPerSegment;
        }

        public void setPointPerSegment(int pointPerSegment2) {
            this.pointPerSegment = pointPerSegment2;
        }

        public Type getType() {
            return this.type;
        }

        public void setType(Type type2) {
            this.type = type2;
        }
    }

    static class ExtrapolatedXYSeries implements XYSeries {
        private final XYCoords first;
        private final XYCoords last;
        private final XYSeries series;

        public ExtrapolatedXYSeries(XYSeries series2, XYCoords first2, XYCoords last2) {
            this.series = series2;
            this.first = first2;
            this.last = last2;
        }

        public Number getX(int i) {
            if (i == 0) {
                return this.first.x;
            }
            if (i == this.series.size() + 1) {
                return this.last.x;
            }
            return this.series.getX(i - 1);
        }

        public Number getY(int i) {
            if (i == 0) {
                return this.first.y;
            }
            if (i == this.series.size() + 1) {
                return this.last.y;
            }
            return this.series.getY(i - 1);
        }

        public int size() {
            return this.series.size() + 2;
        }

        public String getTitle() {
            return this.series.getTitle();
        }
    }

    public List<XYCoords> interpolate(XYSeries series, Params params) {
        if (params.getPointPerSegment() < 2) {
            throw new IllegalArgumentException("pointsPerSegment must be greater than 2, since 2 points is just the linear segment.");
        } else if (series.size() < 3) {
            throw new IllegalArgumentException("Cannot interpolate a series with fewer than 3 vertices.");
        } else {
            XYCoords start = new XYCoords(Double.valueOf(series.getX(0).doubleValue() - (series.getX(1).doubleValue() - series.getX(0).doubleValue())), Double.valueOf(series.getY(0).doubleValue() - (series.getY(1).doubleValue() - series.getY(0).doubleValue())));
            int n = series.size() - 1;
            ExtrapolatedXYSeries extrapolatedXYSeries = new ExtrapolatedXYSeries(series, start, new XYCoords(Double.valueOf(series.getX(n).doubleValue() + (series.getX(n).doubleValue() - series.getX(n - 1).doubleValue())), Double.valueOf(series.getY(n).doubleValue() + (series.getY(n).doubleValue() - series.getY(n - 1).doubleValue()))));
            List<XYCoords> result = new ArrayList<>();
            for (int i = 0; i < extrapolatedXYSeries.size() - 3; i++) {
                List<XYCoords> points = interpolate((XYSeries) extrapolatedXYSeries, i, params);
                if (result.size() > 0) {
                    points.remove(0);
                }
                result.addAll(points);
            }
            return result;
        }
    }

    /* access modifiers changed from: protected */
    public List<XYCoords> interpolate(XYSeries series, int index, Params params) {
        double pow;
        List<XYCoords> result = new ArrayList<>();
        double[] x = new double[4];
        double[] y = new double[4];
        double[] time = new double[4];
        for (int i = 0; i < 4; i++) {
            x[i] = series.getX(index + i).doubleValue();
            y[i] = series.getY(index + i).doubleValue();
            time[i] = (double) i;
        }
        double tstart = 1.0d;
        double tend = 2.0d;
        if (params.getType() != Type.Uniform) {
            double total = 0.0d;
            for (int i2 = 1; i2 < 4; i2++) {
                double dx = x[i2] - x[i2 - 1];
                double dy = y[i2] - y[i2 - 1];
                if (params.getType() == Type.Centripetal) {
                    pow = Math.pow((dx * dx) + (dy * dy), 0.25d);
                } else {
                    pow = Math.pow((dx * dx) + (dy * dy), 0.5d);
                }
                total += pow;
                time[i2] = total;
            }
            tstart = time[1];
            tend = time[2];
        }
        int segments = params.getPointPerSegment() - 1;
        result.add(new XYCoords(series.getX(index + 1), series.getY(index + 1)));
        for (int i3 = 1; i3 < segments; i3++) {
            result.add(new XYCoords(Double.valueOf(interpolate(x, time, ((((double) i3) * (tend - tstart)) / ((double) segments)) + tstart)), Double.valueOf(interpolate(y, time, ((((double) i3) * (tend - tstart)) / ((double) segments)) + tstart))));
        }
        result.add(new XYCoords(series.getX(index + 2), series.getY(index + 2)));
        return result;
    }

    protected static double interpolate(double[] p, double[] time, double t) {
        double L01 = ((p[0] * (time[1] - t)) / (time[1] - time[0])) + ((p[1] * (t - time[0])) / (time[1] - time[0]));
        double L12 = ((p[1] * (time[2] - t)) / (time[2] - time[1])) + ((p[2] * (t - time[1])) / (time[2] - time[1]));
        double L23 = ((p[2] * (time[3] - t)) / (time[3] - time[2])) + ((p[3] * (t - time[2])) / (time[3] - time[2]));
        return (((time[2] - t) * ((((time[2] - t) * L01) / (time[2] - time[0])) + (((t - time[0]) * L12) / (time[2] - time[0])))) / (time[2] - time[1])) + (((t - time[1]) * ((((time[3] - t) * L12) / (time[3] - time[1])) + (((t - time[1]) * L23) / (time[3] - time[1])))) / (time[2] - time[1]));
    }
}
