package com.github.mikephil.charting.data;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;

public class ScatterDataSet extends LineScatterCandleRadarDataSet<Entry> implements IScatterDataSet {
    private ScatterChart.ScatterShape mScatterShape = ScatterChart.ScatterShape.SQUARE;
    private int mScatterShapeHoleColor = ColorTemplate.COLOR_NONE;
    private float mScatterShapeHoleRadius = 0.0f;
    private float mShapeSize = 15.0f;

    public ScatterDataSet(List<Entry> yVals, String label) {
        super(yVals, label);
    }

    public DataSet<Entry> copy() {
        List<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < this.mYVals.size(); i++) {
            yVals.add(((Entry) this.mYVals.get(i)).copy());
        }
        ScatterDataSet copied = new ScatterDataSet(yVals, getLabel());
        copied.mColors = this.mColors;
        copied.mShapeSize = this.mShapeSize;
        copied.mScatterShape = this.mScatterShape;
        copied.mScatterShapeHoleRadius = this.mScatterShapeHoleRadius;
        copied.mScatterShapeHoleColor = this.mScatterShapeHoleColor;
        copied.mHighLightColor = this.mHighLightColor;
        return copied;
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
