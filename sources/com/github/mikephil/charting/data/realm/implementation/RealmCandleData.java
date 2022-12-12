package com.github.mikephil.charting.data.realm.implementation;

import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.realm.base.RealmUtils;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.List;

public class RealmCandleData extends CandleData {
    public RealmCandleData(RealmResults<? extends RealmObject> result, String xValuesField, List<ICandleDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
