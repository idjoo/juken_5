package com.github.mikephil.charting.data.filter;

import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;
import java.util.List;

public class Approximator {
    private boolean[] keep;
    private float mDeltaRatio;
    private float mScaleRatio;
    private double mTolerance;
    private ApproximatorType mType;

    public enum ApproximatorType {
        NONE,
        DOUGLAS_PEUCKER
    }

    public Approximator() {
        this.mType = ApproximatorType.DOUGLAS_PEUCKER;
        this.mTolerance = 0.0d;
        this.mScaleRatio = 1.0f;
        this.mDeltaRatio = 1.0f;
        this.mType = ApproximatorType.NONE;
    }

    public Approximator(ApproximatorType type, double tolerance) {
        this.mType = ApproximatorType.DOUGLAS_PEUCKER;
        this.mTolerance = 0.0d;
        this.mScaleRatio = 1.0f;
        this.mDeltaRatio = 1.0f;
        setup(type, tolerance);
    }

    public void setup(ApproximatorType type, double tolerance) {
        this.mType = type;
        this.mTolerance = tolerance;
    }

    public void setTolerance(double tolerance) {
        this.mTolerance = tolerance;
    }

    public void setType(ApproximatorType type) {
        this.mType = type;
    }

    public void setRatios(float deltaRatio, float scaleRatio) {
        this.mDeltaRatio = deltaRatio;
        this.mScaleRatio = scaleRatio;
    }

    public List<Entry> filter(List<Entry> points) {
        return filter(points, this.mTolerance);
    }

    public List<Entry> filter(List<Entry> points, double tolerance) {
        if (tolerance <= 0.0d) {
            return points;
        }
        this.keep = new boolean[points.size()];
        switch (this.mType) {
            case DOUGLAS_PEUCKER:
                return reduceWithDouglasPeuker(points, tolerance);
            default:
                return points;
        }
    }

    private List<Entry> reduceWithDouglasPeuker(List<Entry> entries, double epsilon) {
        if (epsilon <= 0.0d || entries.size() < 3) {
            return entries;
        }
        this.keep[0] = true;
        this.keep[entries.size() - 1] = true;
        algorithmDouglasPeucker(entries, epsilon, 0, entries.size() - 1);
        List<Entry> reducedEntries = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            if (this.keep[i]) {
                Entry curEntry = entries.get(i);
                reducedEntries.add(new Entry(curEntry.getVal(), curEntry.getXIndex()));
            }
        }
        return reducedEntries;
    }

    private void algorithmDouglasPeucker(List<Entry> entries, double epsilon, int start, int end) {
        if (end > start + 1) {
            int maxDistIndex = 0;
            double distMax = 0.0d;
            Entry firstEntry = entries.get(start);
            Entry lastEntry = entries.get(end);
            for (int i = start + 1; i < end; i++) {
                double dist = calcAngleBetweenLines(firstEntry, lastEntry, firstEntry, entries.get(i));
                if (dist > distMax) {
                    distMax = dist;
                    maxDistIndex = i;
                }
            }
            if (distMax > epsilon) {
                this.keep[maxDistIndex] = true;
                algorithmDouglasPeucker(entries, epsilon, start, maxDistIndex);
                algorithmDouglasPeucker(entries, epsilon, maxDistIndex, end);
            }
        }
    }

    public double calcPointToLineDistance(Entry startEntry, Entry endEntry, Entry entryPoint) {
        float xDiffEndStart = ((float) endEntry.getXIndex()) - ((float) startEntry.getXIndex());
        float xDiffEntryStart = ((float) entryPoint.getXIndex()) - ((float) startEntry.getXIndex());
        return ((double) Math.abs(((endEntry.getVal() - startEntry.getVal()) * xDiffEntryStart) - ((entryPoint.getVal() - startEntry.getVal()) * xDiffEndStart))) / Math.sqrt((double) ((xDiffEndStart * xDiffEndStart) + ((endEntry.getVal() - startEntry.getVal()) * (endEntry.getVal() - startEntry.getVal()))));
    }

    public double calcAngleBetweenLines(Entry start1, Entry end1, Entry start2, Entry end2) {
        return Math.abs(calcAngleWithRatios(start1, end1) - calcAngleWithRatios(start2, end2));
    }

    public double calcAngleWithRatios(Entry p1, Entry p2) {
        return (Math.atan2((double) ((p2.getVal() * this.mScaleRatio) - (p1.getVal() * this.mScaleRatio)), (double) ((((float) p2.getXIndex()) * this.mDeltaRatio) - (((float) p1.getXIndex()) * this.mDeltaRatio))) * 180.0d) / 3.141592653589793d;
    }

    public double calcAngle(Entry p1, Entry p2) {
        return (Math.atan2((double) (p2.getVal() - p1.getVal()), (double) ((float) (p2.getXIndex() - p1.getXIndex()))) * 180.0d) / 3.141592653589793d;
    }
}
