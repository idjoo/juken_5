package com.github.mikephil.charting.data.realm.implementation;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.realm.base.RealmUtils;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.List;

public class RealmBarData extends BarData {
    public RealmBarData(RealmResults<? extends RealmObject> result, String xValuesField, List<IBarDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
