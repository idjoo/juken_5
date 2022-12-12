package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.DashPathEffect;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.FillFormatter;

public interface ILineDataSet extends ILineRadarDataSet<Entry> {
    int getCircleColor(int i);

    int getCircleHoleColor();

    float getCircleRadius();

    float getCubicIntensity();

    DashPathEffect getDashPathEffect();

    FillFormatter getFillFormatter();

    boolean isDashedLineEnabled();

    boolean isDrawCircleHoleEnabled();

    boolean isDrawCirclesEnabled();

    boolean isDrawCubicEnabled();

    boolean isDrawSteppedEnabled();
}
