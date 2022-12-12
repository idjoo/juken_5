package com.github.mikephil.charting.data.realm.implementation;

import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.realm.base.RealmBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.utils.Utils;
import io.realm.DynamicRealmObject;
import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.Iterator;

public class RealmBubbleDataSet<T extends RealmObject> extends RealmBarLineScatterCandleBubbleDataSet<T, BubbleEntry> implements IBubbleDataSet {
    private float mHighlightCircleWidth = 2.5f;
    protected float mMaxSize;
    private String mSizeField;
    protected float mXMax;
    protected float mXMin;

    public RealmBubbleDataSet(RealmResults<T> result, String yValuesField, String sizeField) {
        super(result, yValuesField);
        this.mSizeField = sizeField;
        build(this.results);
        calcMinMax(0, this.results.size());
    }

    public RealmBubbleDataSet(RealmResults<T> result, String yValuesField, String xIndexField, String sizeField) {
        super(result, yValuesField, xIndexField);
        this.mSizeField = sizeField;
        build(this.results);
        calcMinMax(0, this.results.size());
    }

    public void build(RealmResults<T> results) {
        if (this.mIndexField == null) {
            int xIndex = 0;
            Iterator i$ = results.iterator();
            while (i$.hasNext()) {
                DynamicRealmObject dynamicObject = new DynamicRealmObject((RealmObject) i$.next());
                this.mValues.add(new BubbleEntry(xIndex, dynamicObject.getFloat(this.mValuesField), dynamicObject.getFloat(this.mSizeField)));
                xIndex++;
            }
            return;
        }
        Iterator i$2 = results.iterator();
        while (i$2.hasNext()) {
            DynamicRealmObject dynamicObject2 = new DynamicRealmObject((RealmObject) i$2.next());
            this.mValues.add(new BubbleEntry(dynamicObject2.getInt(this.mIndexField), dynamicObject2.getFloat(this.mValuesField), dynamicObject2.getFloat(this.mSizeField)));
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
            this.mYMin = yMin((BubbleEntry) this.mValues.get(start));
            this.mYMax = yMax((BubbleEntry) this.mValues.get(start));
            for (int i = start; i < endValue; i++) {
                BubbleEntry entry = (BubbleEntry) this.mValues.get(i);
                float ymin = yMin(entry);
                float ymax = yMax(entry);
                if (ymin < this.mYMin) {
                    this.mYMin = ymin;
                }
                if (ymax > this.mYMax) {
                    this.mYMax = ymax;
                }
                float xmin = xMin(entry);
                float xmax = xMax(entry);
                if (xmin < this.mXMin) {
                    this.mXMin = xmin;
                }
                if (xmax > this.mXMax) {
                    this.mXMax = xmax;
                }
                float size = largestSize(entry);
                if (size > this.mMaxSize) {
                    this.mMaxSize = size;
                }
            }
        }
    }

    public float getXMax() {
        return this.mXMax;
    }

    public float getXMin() {
        return this.mXMin;
    }

    public float getMaxSize() {
        return this.mMaxSize;
    }

    private float yMin(BubbleEntry entry) {
        return entry.getVal();
    }

    private float yMax(BubbleEntry entry) {
        return entry.getVal();
    }

    private float xMin(BubbleEntry entry) {
        return (float) entry.getXIndex();
    }

    private float xMax(BubbleEntry entry) {
        return (float) entry.getXIndex();
    }

    private float largestSize(BubbleEntry entry) {
        return entry.getSize();
    }

    public void setHighlightCircleWidth(float width) {
        this.mHighlightCircleWidth = Utils.convertDpToPixel(width);
    }

    public float getHighlightCircleWidth() {
        return this.mHighlightCircleWidth;
    }

    public void setSizeField(String sizeField) {
        this.mSizeField = sizeField;
    }

    public String getSizeField() {
        return this.mSizeField;
    }
}
