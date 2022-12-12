package com.github.mikephil.charting.data.realm.implementation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import com.github.mikephil.charting.data.realm.base.RealmLineRadarDataSet;
import com.github.mikephil.charting.formatter.DefaultFillFormatter;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

public class RealmLineDataSet<T extends RealmObject> extends RealmLineRadarDataSet<T> implements ILineDataSet {
    private int mCircleColorHole;
    private List<Integer> mCircleColors;
    private float mCircleSize;
    private float mCubicIntensity;
    private DashPathEffect mDashPathEffect;
    private boolean mDrawCircleHole;
    private boolean mDrawCircles;
    private boolean mDrawCubic;
    private boolean mDrawStepped;
    private FillFormatter mFillFormatter;

    public RealmLineDataSet(RealmResults<T> result, String yValuesField) {
        super(result, yValuesField);
        this.mCircleColors = null;
        this.mCircleColorHole = -1;
        this.mCircleSize = 8.0f;
        this.mCubicIntensity = 0.2f;
        this.mDashPathEffect = null;
        this.mFillFormatter = new DefaultFillFormatter();
        this.mDrawCircles = true;
        this.mDrawCubic = false;
        this.mDrawStepped = false;
        this.mDrawCircleHole = true;
        this.mCircleColors = new ArrayList();
        this.mCircleColors.add(Integer.valueOf(Color.rgb(140, 234, 255)));
        build(this.results);
        calcMinMax(0, this.results.size());
    }

    public RealmLineDataSet(RealmResults<T> result, String yValuesField, String xIndexField) {
        super(result, yValuesField, xIndexField);
        this.mCircleColors = null;
        this.mCircleColorHole = -1;
        this.mCircleSize = 8.0f;
        this.mCubicIntensity = 0.2f;
        this.mDashPathEffect = null;
        this.mFillFormatter = new DefaultFillFormatter();
        this.mDrawCircles = true;
        this.mDrawCubic = false;
        this.mDrawStepped = false;
        this.mDrawCircleHole = true;
        this.mCircleColors = new ArrayList();
        this.mCircleColors.add(Integer.valueOf(Color.rgb(140, 234, 255)));
        build(this.results);
        calcMinMax(0, this.results.size());
    }

    public void build(RealmResults<T> results) {
        super.build(results);
    }

    public void setCubicIntensity(float intensity) {
        if (intensity > 1.0f) {
            intensity = 1.0f;
        }
        if (intensity < 0.05f) {
            intensity = 0.05f;
        }
        this.mCubicIntensity = intensity;
    }

    public float getCubicIntensity() {
        return this.mCubicIntensity;
    }

    public void setCircleSize(float size) {
        this.mCircleSize = Utils.convertDpToPixel(size);
    }

    public float getCircleRadius() {
        return this.mCircleSize;
    }

    public void enableDashedLine(float lineLength, float spaceLength, float phase) {
        this.mDashPathEffect = new DashPathEffect(new float[]{lineLength, spaceLength}, phase);
    }

    public void disableDashedLine() {
        this.mDashPathEffect = null;
    }

    public boolean isDashedLineEnabled() {
        return this.mDashPathEffect != null;
    }

    public DashPathEffect getDashPathEffect() {
        return this.mDashPathEffect;
    }

    public void setDrawCircles(boolean enabled) {
        this.mDrawCircles = enabled;
    }

    public boolean isDrawCirclesEnabled() {
        return this.mDrawCircles;
    }

    public void setDrawCubic(boolean enabled) {
        this.mDrawCubic = enabled;
    }

    public boolean isDrawCubicEnabled() {
        return this.mDrawCubic;
    }

    public void setDrawStepped(boolean enabled) {
        this.mDrawStepped = enabled;
    }

    public boolean isDrawSteppedEnabled() {
        return this.mDrawStepped;
    }

    public List<Integer> getCircleColors() {
        return this.mCircleColors;
    }

    public int getCircleColor(int index) {
        return this.mCircleColors.get(index % this.mCircleColors.size()).intValue();
    }

    public void setCircleColors(List<Integer> colors) {
        this.mCircleColors = colors;
    }

    public void setCircleColors(int[] colors) {
        this.mCircleColors = ColorTemplate.createColors(colors);
    }

    public void setCircleColors(int[] colors, Context c) {
        List<Integer> clrs = new ArrayList<>();
        for (int color : colors) {
            clrs.add(Integer.valueOf(c.getResources().getColor(color)));
        }
        this.mCircleColors = clrs;
    }

    public void setCircleColor(int color) {
        resetCircleColors();
        this.mCircleColors.add(Integer.valueOf(color));
    }

    public void resetCircleColors() {
        this.mCircleColors = new ArrayList();
    }

    public void setCircleColorHole(int color) {
        this.mCircleColorHole = color;
    }

    public int getCircleHoleColor() {
        return this.mCircleColorHole;
    }

    public void setDrawCircleHole(boolean enabled) {
        this.mDrawCircleHole = enabled;
    }

    public boolean isDrawCircleHoleEnabled() {
        return this.mDrawCircleHole;
    }

    public void setFillFormatter(FillFormatter formatter) {
        if (formatter == null) {
            this.mFillFormatter = new DefaultFillFormatter();
        } else {
            this.mFillFormatter = formatter;
        }
    }

    public FillFormatter getFillFormatter() {
        return this.mFillFormatter;
    }
}
