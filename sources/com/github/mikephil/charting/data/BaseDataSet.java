package com.github.mikephil.charting.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDataSet<T extends Entry> implements IDataSet<T> {
    protected YAxis.AxisDependency mAxisDependency;
    protected List<Integer> mColors;
    protected boolean mDrawValues;
    protected boolean mHighlightEnabled;
    private String mLabel;
    protected List<Integer> mValueColors;
    protected transient ValueFormatter mValueFormatter;
    protected float mValueTextSize;
    protected Typeface mValueTypeface;
    protected boolean mVisible;

    public BaseDataSet() {
        this.mColors = null;
        this.mValueColors = null;
        this.mLabel = "DataSet";
        this.mAxisDependency = YAxis.AxisDependency.LEFT;
        this.mHighlightEnabled = true;
        this.mDrawValues = true;
        this.mValueTextSize = 17.0f;
        this.mVisible = true;
        this.mColors = new ArrayList();
        this.mValueColors = new ArrayList();
        this.mColors.add(Integer.valueOf(Color.rgb(140, 234, 255)));
        this.mValueColors.add(Integer.valueOf(ViewCompat.MEASURED_STATE_MASK));
    }

    public BaseDataSet(String label) {
        this();
        this.mLabel = label;
    }

    public void notifyDataSetChanged() {
        calcMinMax(0, getEntryCount() - 1);
    }

    public List<Integer> getColors() {
        return this.mColors;
    }

    public List<Integer> getValueColors() {
        return this.mValueColors;
    }

    public int getColor() {
        return this.mColors.get(0).intValue();
    }

    public int getColor(int index) {
        return this.mColors.get(index % this.mColors.size()).intValue();
    }

    public void setColors(List<Integer> colors) {
        this.mColors = colors;
    }

    public void setColors(int[] colors) {
        this.mColors = ColorTemplate.createColors(colors);
    }

    public void setColors(int[] colors, Context c) {
        List<Integer> clrs = new ArrayList<>();
        for (int color : colors) {
            clrs.add(Integer.valueOf(c.getResources().getColor(color)));
        }
        this.mColors = clrs;
    }

    public void addColor(int color) {
        if (this.mColors == null) {
            this.mColors = new ArrayList();
        }
        this.mColors.add(Integer.valueOf(color));
    }

    public void setColor(int color) {
        resetColors();
        this.mColors.add(Integer.valueOf(color));
    }

    public void setColor(int color, int alpha) {
        setColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
    }

    public void setColors(int[] colors, int alpha) {
        resetColors();
        for (int color : colors) {
            addColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
        }
    }

    public void resetColors() {
        this.mColors = new ArrayList();
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }

    public String getLabel() {
        return this.mLabel;
    }

    public void setHighlightEnabled(boolean enabled) {
        this.mHighlightEnabled = enabled;
    }

    public boolean isHighlightEnabled() {
        return this.mHighlightEnabled;
    }

    public void setValueFormatter(ValueFormatter f) {
        if (f != null) {
            this.mValueFormatter = f;
        }
    }

    public ValueFormatter getValueFormatter() {
        if (this.mValueFormatter == null) {
            return new DefaultValueFormatter(1);
        }
        return this.mValueFormatter;
    }

    public void setValueTextColor(int color) {
        this.mValueColors.clear();
        this.mValueColors.add(Integer.valueOf(color));
    }

    public void setValueTextColors(List<Integer> colors) {
        this.mValueColors = colors;
    }

    public void setValueTypeface(Typeface tf) {
        this.mValueTypeface = tf;
    }

    public void setValueTextSize(float size) {
        this.mValueTextSize = Utils.convertDpToPixel(size);
    }

    public int getValueTextColor() {
        return this.mValueColors.get(0).intValue();
    }

    public int getValueTextColor(int index) {
        return this.mValueColors.get(index % this.mValueColors.size()).intValue();
    }

    public Typeface getValueTypeface() {
        return this.mValueTypeface;
    }

    public float getValueTextSize() {
        return this.mValueTextSize;
    }

    public void setDrawValues(boolean enabled) {
        this.mDrawValues = enabled;
    }

    public boolean isDrawValuesEnabled() {
        return this.mDrawValues;
    }

    public void setVisible(boolean visible) {
        this.mVisible = visible;
    }

    public boolean isVisible() {
        return this.mVisible;
    }

    public YAxis.AxisDependency getAxisDependency() {
        return this.mAxisDependency;
    }

    public void setAxisDependency(YAxis.AxisDependency dependency) {
        this.mAxisDependency = dependency;
    }

    public int getIndexInEntries(int xIndex) {
        for (int i = 0; i < getEntryCount(); i++) {
            if (xIndex == getEntryForIndex(i).getXIndex()) {
                return i;
            }
        }
        return -1;
    }

    public boolean removeFirst() {
        return removeEntry(getEntryForIndex(0));
    }

    public boolean removeLast() {
        return removeEntry(getEntryForIndex(getEntryCount() - 1));
    }

    public boolean removeEntry(int xIndex) {
        return removeEntry(getEntryForXIndex(xIndex));
    }

    public boolean contains(T e) {
        for (int i = 0; i < getEntryCount(); i++) {
            if (getEntryForIndex(i).equals(e)) {
                return true;
            }
        }
        return false;
    }
}
