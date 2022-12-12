package com.github.mikephil.charting.data.realm.base;

import android.graphics.Color;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import io.realm.RealmObject;
import io.realm.RealmResults;

public abstract class RealmBarLineScatterCandleBubbleDataSet<T extends RealmObject, S extends Entry> extends RealmBaseDataSet<T, S> implements IBarLineScatterCandleBubbleDataSet<S> {
    protected int mHighLightColor = Color.rgb(255, 187, 115);

    public RealmBarLineScatterCandleBubbleDataSet(RealmResults<T> results, String yValuesField) {
        super(results, yValuesField);
    }

    public RealmBarLineScatterCandleBubbleDataSet(RealmResults<T> results, String yValuesField, String xIndexField) {
        super(results, yValuesField, xIndexField);
    }

    public void setHighLightColor(int color) {
        this.mHighLightColor = color;
    }

    public int getHighLightColor() {
        return this.mHighLightColor;
    }
}
