package com.github.mikephil.charting.data.realm.base;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.ILineRadarDataSet;
import com.github.mikephil.charting.utils.Utils;
import io.realm.DynamicRealmObject;
import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.Iterator;

public abstract class RealmLineRadarDataSet<T extends RealmObject> extends RealmLineScatterCandleRadarDataSet<T, Entry> implements ILineRadarDataSet<Entry> {
    private boolean mDrawFilled = false;
    private int mFillAlpha = 85;
    private int mFillColor = Color.rgb(140, 234, 255);
    protected Drawable mFillDrawable;
    private float mLineWidth = 2.5f;

    public RealmLineRadarDataSet(RealmResults<T> results, String yValuesField) {
        super(results, yValuesField);
    }

    public RealmLineRadarDataSet(RealmResults<T> results, String yValuesField, String xIndexField) {
        super(results, yValuesField, xIndexField);
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

    public int getFillColor() {
        return this.mFillColor;
    }

    public void setFillColor(int color) {
        this.mFillColor = color;
        this.mFillDrawable = null;
    }

    public Drawable getFillDrawable() {
        return this.mFillDrawable;
    }

    public void setFillDrawable(Drawable drawable) {
        this.mFillDrawable = drawable;
    }

    public int getFillAlpha() {
        return this.mFillAlpha;
    }

    public void setFillAlpha(int alpha) {
        this.mFillAlpha = alpha;
    }

    public void setLineWidth(float width) {
        if (width < 0.2f) {
            width = 0.2f;
        }
        if (width > 10.0f) {
            width = 10.0f;
        }
        this.mLineWidth = Utils.convertDpToPixel(width);
    }

    public float getLineWidth() {
        return this.mLineWidth;
    }

    public void setDrawFilled(boolean filled) {
        this.mDrawFilled = filled;
    }

    public boolean isDrawFilledEnabled() {
        return this.mDrawFilled;
    }
}
