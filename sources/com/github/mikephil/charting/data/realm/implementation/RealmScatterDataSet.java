package com.github.mikephil.charting.data.realm.implementation;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.realm.base.RealmLineScatterCandleRadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import io.realm.DynamicRealmObject;
import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.Iterator;

public class RealmScatterDataSet<T extends RealmObject> extends RealmLineScatterCandleRadarDataSet<T, Entry> implements IScatterDataSet {
    private ScatterChart.ScatterShape mScatterShape = ScatterChart.ScatterShape.SQUARE;
    private int mScatterShapeHoleColor = ColorTemplate.COLOR_NONE;
    private float mScatterShapeHoleRadius = 0.0f;
    private float mShapeSize = 10.0f;

    public RealmScatterDataSet(RealmResults<T> result, String yValuesField) {
        super(result, yValuesField);
        build(this.results);
        calcMinMax(0, this.results.size());
    }

    public RealmScatterDataSet(RealmResults<T> result, String yValuesField, String xIndexField) {
        super(result, yValuesField, xIndexField);
        build(this.results);
        calcMinMax(0, this.results.size());
    }

    public void build(RealmResults<T> results) {
        if (this.mIndexField == null) {
            int xIndex = 0;
            Iterator i$ = results.iterator();
            while (i$.hasNext()) {
                this.mValues.add(new Entry(new DynamicRealmObject((RealmObject) i$.next()).getFloat(this.mValuesField), xIndex));
                xIndex++;
            }
            return;
        }
        Iterator i$2 = results.iterator();
        while (i$2.hasNext()) {
            DynamicRealmObject dynamicObject = new DynamicRealmObject((RealmObject) i$2.next());
            this.mValues.add(new Entry(dynamicObject.getFloat(this.mValuesField), dynamicObject.getInt(this.mIndexField)));
        }
    }

    public void setScatterShapeSize(float size) {
        this.mShapeSize = size;
    }

    public float getScatterShapeSize() {
        return this.mShapeSize;
    }

    public void setScatterShape(ScatterChart.ScatterShape shape) {
        this.mScatterShape = shape;
    }

    public ScatterChart.ScatterShape getScatterShape() {
        return this.mScatterShape;
    }

    public void setScatterShapeHoleRadius(float holeRadius) {
        this.mScatterShapeHoleRadius = holeRadius;
    }

    public float getScatterShapeHoleRadius() {
        return this.mScatterShapeHoleRadius;
    }

    public void setScatterShapeHoleColor(int holeColor) {
        this.mScatterShapeHoleColor = holeColor;
    }

    public int getScatterShapeHoleColor() {
        return this.mScatterShapeHoleColor;
    }
}
