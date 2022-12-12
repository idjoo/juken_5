package com.github.mikephil.charting.data.realm.implementation;

import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.realm.base.RealmUtils;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.List;

public class RealmRadarData extends RadarData {
    public RealmRadarData(RealmResults<? extends RealmObject> result, String xValuesField, List<IRadarDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
