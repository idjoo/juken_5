package com.github.mikephil.charting.interfaces.datasets;

import android.graphics.Typeface;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.List;

public interface IDataSet<T extends Entry> {
    boolean addEntry(T t);

    void addEntryOrdered(T t);

    void calcMinMax(int i, int i2);

    void clear();

    boolean contains(T t);

    YAxis.AxisDependency getAxisDependency();

    int getColor();

    int getColor(int i);

    List<Integer> getColors();

    int getEntryCount();

    T getEntryForIndex(int i);

    T getEntryForXIndex(int i);

    T getEntryForXIndex(int i, DataSet.Rounding rounding);

    int getEntryIndex(int i, DataSet.Rounding rounding);

    int getEntryIndex(T t);

    int getIndexInEntries(int i);

    String getLabel();

    ValueFormatter getValueFormatter();

    int getValueTextColor();

    int getValueTextColor(int i);

    float getValueTextSize();

    Typeface getValueTypeface();

    float getYMax();

    float getYMin();

    float getYValForXIndex(int i);

    boolean isDrawValuesEnabled();

    boolean isHighlightEnabled();

    boolean isVisible();

    boolean removeEntry(int i);

    boolean removeEntry(T t);

    boolean removeFirst();

    boolean removeLast();

    void setAxisDependency(YAxis.AxisDependency axisDependency);

    void setDrawValues(boolean z);

    void setHighlightEnabled(boolean z);

    void setLabel(String str);

    void setValueFormatter(ValueFormatter valueFormatter);

    void setValueTextColor(int i);

    void setValueTextColors(List<Integer> list);

    void setValueTextSize(float f);

    void setValueTypeface(Typeface typeface);

    void setVisible(boolean z);
}
