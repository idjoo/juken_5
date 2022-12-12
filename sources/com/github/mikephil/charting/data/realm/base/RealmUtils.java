package com.github.mikephil.charting.data.realm.base;

import io.realm.DynamicRealmObject;
import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RealmUtils {
    public static List<String> toXVals(RealmResults<? extends RealmObject> result, String xValuesField) {
        List<String> xVals = new ArrayList<>();
        Iterator i$ = result.iterator();
        while (i$.hasNext()) {
            xVals.add(new DynamicRealmObject((RealmObject) i$.next()).getString(xValuesField));
        }
        return xVals;
    }
}
