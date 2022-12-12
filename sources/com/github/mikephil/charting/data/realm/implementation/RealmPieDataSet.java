package com.github.mikephil.charting.data.realm.implementation;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.realm.base.RealmBaseDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.Utils;
import io.realm.DynamicRealmObject;
import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.Iterator;

public class RealmPieDataSet<T extends RealmObject> extends RealmBaseDataSet<T, Entry> implements IPieDataSet {
    private float mShift = 18.0f;
    private float mSliceSpace = 0.0f;

    public RealmPieDataSet(RealmResults<T> result, String yValuesField) {
        super(result, yValuesField);
        build(this.results);
        calcMinMax(0, this.results.size());
    }

    public RealmPieDataSet(RealmResults<T> result, String yValuesField, String xIndexField) {
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

    public void setSliceSpace(float spaceDp) {
        if (spaceDp > 20.0f) {
            spaceDp = 20.0f;
        }
        if (spaceDp < 0.0f) {
            spaceDp = 0.0f;
        }
        this.mSliceSpace = Utils.convertDpToPixel(spaceDp);
    }

    public float getSliceSpace() {
        return this.mSliceSpace;
    }

    public void setSelectionShift(float shift) {
        this.mShift = Utils.convertDpToPixel(shift);
    }

    public float getSelectionShift() {
        return this.mShift;
    }
}
