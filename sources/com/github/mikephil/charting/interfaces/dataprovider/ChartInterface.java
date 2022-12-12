package com.github.mikephil.charting.interfaces.dataprovider;

import android.graphics.PointF;
import android.graphics.RectF;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.ValueFormatter;

public interface ChartInterface {
    PointF getCenterOfView();

    PointF getCenterOffsets();

    RectF getContentRect();

    ChartData getData();

    ValueFormatter getDefaultValueFormatter();

    int getHeight();

    int getWidth();

    float getXChartMax();

    float getXChartMin();

    int getXValCount();

    float getYChartMax();

    float getYChartMin();
}
