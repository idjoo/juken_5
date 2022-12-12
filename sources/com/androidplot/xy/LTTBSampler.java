package com.androidplot.xy;

import android.util.Log;

public class LTTBSampler implements Sampler {
    public RectRegion run(XYSeries rawData, EditableXYSeries sampled) {
        RectRegion bounds = new RectRegion();
        int threshold = sampled.size();
        int dataLength = rawData.size();
        if (threshold >= dataLength || threshold == 0) {
            throw new RuntimeException("Shouldnt be here!");
        }
        double bucketSize = ((double) (dataLength - 2)) / ((double) (threshold - 2));
        int a = 0;
        int nextA = 0;
        setSample(rawData, sampled, 0, 0, bounds);
        int sampledIndex = 0 + 1;
        for (int i = 0; i < threshold - 2; i++) {
            double pointCX = 0.0d;
            double pointCY = 0.0d;
            int pointCStart = ((int) Math.floor(((double) (i + 1)) * bucketSize)) + 1;
            int pointCEnd = ((int) Math.floor(((double) (i + 2)) * bucketSize)) + 1;
            if (pointCEnd >= dataLength) {
                pointCEnd = dataLength;
            }
            int pointCSize = pointCEnd - pointCStart;
            while (pointCStart < pointCEnd) {
                if (rawData.getX(pointCStart + 0) != null) {
                    pointCX += rawData.getX(pointCStart + 0).doubleValue();
                }
                if (rawData.getY(pointCStart + 0) != null) {
                    pointCY += rawData.getY(pointCStart + 0).doubleValue();
                }
                pointCStart++;
            }
            double pointCX2 = pointCX / ((double) pointCSize);
            double pointCY2 = pointCY / ((double) pointCSize);
            double pointAX = rawData.getX(a + 0).doubleValue();
            double pointAY = rawData.getY(a + 0).doubleValue();
            int pointBEnd = ((int) Math.floor(((double) (i + 1)) * bucketSize)) + 1;
            double maxArea = -1.0d;
            XYCoords maxAreaPoint = null;
            for (int pointBStart = ((int) Math.floor(((double) (i + 0)) * bucketSize)) + 1; pointBStart < pointBEnd; pointBStart++) {
                double area = Math.abs(((pointAX - pointCX2) * (rawData.getY(pointBStart + 0).doubleValue() - pointAY)) - ((pointAX - rawData.getX(pointBStart + 0).doubleValue()) * (pointCY2 - pointAY))) * 0.5d;
                if (area > maxArea) {
                    if (rawData.getY(pointBStart + 0) == null) {
                        Log.i("LTTB", "Null value encountered in raw data at index: " + pointBStart);
                    }
                    maxArea = area;
                    maxAreaPoint = new XYCoords(rawData.getX(pointBStart + 0), rawData.getY(pointBStart + 0));
                    nextA = pointBStart;
                }
            }
            setSample(sampled, maxAreaPoint.x, maxAreaPoint.y, sampledIndex, bounds);
            sampledIndex++;
            a = nextA;
        }
        setSample(rawData, sampled, (dataLength + 0) - 1, sampledIndex, bounds);
        int sampledIndex2 = sampledIndex + 1;
        return bounds;
    }

    /* access modifiers changed from: protected */
    public void setSample(XYSeries raw, EditableXYSeries sampled, int rawIndex, int sampleIndex, RectRegion bounds) {
        setSample(sampled, raw.getX(rawIndex), raw.getY(rawIndex), sampleIndex, bounds);
    }

    /* access modifiers changed from: protected */
    public void setSample(EditableXYSeries sampled, Number x, Number y, int sampleIndex, RectRegion bounds) {
        bounds.union(x, y);
        sampled.setX(x, sampleIndex);
        sampled.setY(y, sampleIndex);
    }
}
