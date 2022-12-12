package com.github.mikephil.charting.data.realm.implementation;

import android.graphics.Paint;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.realm.base.RealmLineScatterCandleRadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import io.realm.DynamicRealmObject;
import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.Iterator;

public class RealmCandleDataSet<T extends RealmObject> extends RealmLineScatterCandleRadarDataSet<T, CandleEntry> implements ICandleDataSet {
    private float mBarSpace = 0.1f;
    private String mCloseField;
    protected int mDecreasingColor = ColorTemplate.COLOR_NONE;
    protected Paint.Style mDecreasingPaintStyle = Paint.Style.FILL;
    private String mHighField;
    protected int mIncreasingColor = ColorTemplate.COLOR_NONE;
    protected Paint.Style mIncreasingPaintStyle = Paint.Style.STROKE;
    private String mLowField;
    protected int mNeutralColor = ColorTemplate.COLOR_NONE;
    private String mOpenField;
    protected int mShadowColor = ColorTemplate.COLOR_NONE;
    private boolean mShadowColorSameAsCandle = false;
    private float mShadowWidth = 3.0f;
    private boolean mShowCandleBar = true;

    public RealmCandleDataSet(RealmResults<T> result, String highField, String lowField, String openField, String closeField) {
        super(result, (String) null);
        this.mHighField = highField;
        this.mLowField = lowField;
        this.mOpenField = openField;
        this.mCloseField = closeField;
        build(this.results);
        calcMinMax(0, this.results.size());
    }

    public RealmCandleDataSet(RealmResults<T> result, String highField, String lowField, String openField, String closeField, String xIndexField) {
        super(result, (String) null, xIndexField);
        this.mHighField = highField;
        this.mLowField = lowField;
        this.mOpenField = openField;
        this.mCloseField = closeField;
        build(this.results);
        calcMinMax(0, this.results.size());
    }

    public void build(RealmResults<T> results) {
        if (this.mIndexField == null) {
            int xIndex = 0;
            Iterator i$ = results.iterator();
            while (i$.hasNext()) {
                DynamicRealmObject dynamicObject = new DynamicRealmObject((RealmObject) i$.next());
                this.mValues.add(new CandleEntry(xIndex, dynamicObject.getFloat(this.mHighField), dynamicObject.getFloat(this.mLowField), dynamicObject.getFloat(this.mOpenField), dynamicObject.getFloat(this.mCloseField)));
                xIndex++;
            }
            return;
        }
        Iterator i$2 = results.iterator();
        while (i$2.hasNext()) {
            DynamicRealmObject dynamicObject2 = new DynamicRealmObject((RealmObject) i$2.next());
            this.mValues.add(new CandleEntry(dynamicObject2.getInt(this.mIndexField), dynamicObject2.getFloat(this.mHighField), dynamicObject2.getFloat(this.mLowField), dynamicObject2.getFloat(this.mOpenField), dynamicObject2.getFloat(this.mCloseField)));
        }
    }

    public void calcMinMax(int start, int end) {
        int endValue;
        if (this.mValues != null && this.mValues.size() != 0) {
            if (end == 0 || end >= this.mValues.size()) {
                endValue = this.mValues.size() - 1;
            } else {
                endValue = end;
            }
            this.mYMin = Float.MAX_VALUE;
            this.mYMax = -3.4028235E38f;
            for (int i = start; i <= endValue; i++) {
                CandleEntry e = (CandleEntry) this.mValues.get(i);
                if (e.getLow() < this.mYMin) {
                    this.mYMin = e.getLow();
                }
                if (e.getHigh() > this.mYMax) {
                    this.mYMax = e.getHigh();
                }
            }
        }
    }

    public void setBarSpace(float space) {
        if (space < 0.0f) {
            space = 0.0f;
        }
        if (space > 0.45f) {
            space = 0.45f;
        }
        this.mBarSpace = space;
    }

    public float getBarSpace() {
        return this.mBarSpace;
    }

    public void setShadowWidth(float width) {
        this.mShadowWidth = Utils.convertDpToPixel(width);
    }

    public float getShadowWidth() {
        return this.mShadowWidth;
    }

    public void setShowCandleBar(boolean showCandleBar) {
        this.mShowCandleBar = showCandleBar;
    }

    public boolean getShowCandleBar() {
        return this.mShowCandleBar;
    }

    public void setNeutralColor(int color) {
        this.mNeutralColor = color;
    }

    public int getNeutralColor() {
        return this.mNeutralColor;
    }

    public void setIncreasingColor(int color) {
        this.mIncreasingColor = color;
    }

    public int getIncreasingColor() {
        return this.mIncreasingColor;
    }

    public void setDecreasingColor(int color) {
        this.mDecreasingColor = color;
    }

    public int getDecreasingColor() {
        return this.mDecreasingColor;
    }

    public Paint.Style getIncreasingPaintStyle() {
        return this.mIncreasingPaintStyle;
    }

    public void setIncreasingPaintStyle(Paint.Style paintStyle) {
        this.mIncreasingPaintStyle = paintStyle;
    }

    public Paint.Style getDecreasingPaintStyle() {
        return this.mDecreasingPaintStyle;
    }

    public void setDecreasingPaintStyle(Paint.Style decreasingPaintStyle) {
        this.mDecreasingPaintStyle = decreasingPaintStyle;
    }

    public int getShadowColor() {
        return this.mShadowColor;
    }

    public void setShadowColor(int shadowColor) {
        this.mShadowColor = shadowColor;
    }

    public boolean getShadowColorSameAsCandle() {
        return this.mShadowColorSameAsCandle;
    }

    public void setShadowColorSameAsCandle(boolean shadowColorSameAsCandle) {
        this.mShadowColorSameAsCandle = shadowColorSameAsCandle;
    }
}
