package com.github.mikephil.charting.interfaces.dataprovider;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {
    BarLineScatterCandleBubbleData getData();

    int getHighestVisibleXIndex();

    int getLowestVisibleXIndex();

    int getMaxVisibleCount();

    Transformer getTransformer(YAxis.AxisDependency axisDependency);

    boolean isInverted(YAxis.AxisDependency axisDependency);
}
