package com.github.mikephil.charting.data.realm.base;

import com.github.mikephil.charting.data.BaseDataSet;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.ArrayList;
import java.util.List;

public abstract class RealmBaseDataSet<T extends RealmObject, S extends Entry> extends BaseDataSet<S> {
    protected String mIndexField;
    protected List<S> mValues;
    protected String mValuesField;
    protected float mYMax = 0.0f;
    protected float mYMin = 0.0f;
    protected RealmResults<T> results;

    public abstract void build(RealmResults<T> realmResults);

    public RealmBaseDataSet(RealmResults<T> results2, String yValuesField) {
        this.results = results2;
        this.mValuesField = yValuesField;
        this.mValues = new ArrayList();
        if (this.mIndexField != null) {
            this.results.sort(this.mIndexField, Sort.ASCENDING);
        }
    }

    public RealmBaseDataSet(RealmResults<T> results2, String yValuesField, String xIndexField) {
        this.results = results2;
        this.mValuesField = yValuesField;
        this.mIndexField = xIndexField;
        this.mValues = new ArrayList();
        if (this.mIndexField != null) {
            this.results.sort(this.mIndexField, Sort.ASCENDING);
        }
    }

    public float getYMin() {
        return this.mYMin;
    }

    public float getYMax() {
        return this.mYMax;
    }

    public int getEntryCount() {
        return this.mValues.size();
    }

    public void calcMinMax(int start, int end) {
        int yValCount;
        int endValue;
        if (this.mValues != null && (yValCount = this.mValues.size()) != 0) {
            if (end == 0 || end >= yValCount) {
                endValue = yValCount - 1;
            } else {
                endValue = end;
            }
            this.mYMin = Float.MAX_VALUE;
            this.mYMax = -3.4028235E38f;
            for (int i = start; i <= endValue; i++) {
                S e = (Entry) this.mValues.get(i);
                if (e != null && !Float.isNaN(e.getVal())) {
                    if (e.getVal() < this.mYMin) {
                        this.mYMin = e.getVal();
                    }
                    if (e.getVal() > this.mYMax) {
                        this.mYMax = e.getVal();
                    }
                }
            }
            if (this.mYMin == Float.MAX_VALUE) {
                this.mYMin = 0.0f;
                this.mYMax = 0.0f;
            }
        }
    }

    public S getEntryForXIndex(int xIndex) {
        return getEntryForXIndex(xIndex, DataSet.Rounding.CLOSEST);
    }

    public S getEntryForXIndex(int xIndex, DataSet.Rounding rounding) {
        int index = getEntryIndex(xIndex, rounding);
        if (index > -1) {
            return (Entry) this.mValues.get(index);
        }
        return null;
    }

    public S getEntryForIndex(int index) {
        return (Entry) this.mValues.get(index);
    }

    public int getEntryIndex(int x, DataSet.Rounding rounding) {
        int low = 0;
        int high = this.mValues.size() - 1;
        int closest = -1;
        while (low <= high) {
            int m = (high + low) / 2;
            if (x == ((Entry) this.mValues.get(m)).getXIndex()) {
                while (m > 0 && ((Entry) this.mValues.get(m - 1)).getXIndex() == x) {
                    m--;
                }
                return m;
            }
            if (x > ((Entry) this.mValues.get(m)).getXIndex()) {
                low = m + 1;
            } else {
                high = m - 1;
            }
            closest = m;
        }
        if (closest != -1) {
            int closestXIndex = ((Entry) this.mValues.get(closest)).getXIndex();
            if (rounding == DataSet.Rounding.UP) {
                if (closestXIndex < x && closest < this.mValues.size() - 1) {
                    closest++;
                }
            } else if (rounding == DataSet.Rounding.DOWN && closestXIndex > x && closest > 0) {
                closest--;
            }
        }
        return closest;
    }

    public int getEntryIndex(S e) {
        return this.mValues.indexOf(e);
    }

    public float getYValForXIndex(int xIndex) {
        Entry e = getEntryForXIndex(xIndex);
        if (e == null || e.getXIndex() != xIndex) {
            return Float.NaN;
        }
        return e.getVal();
    }

    public boolean addEntry(S e) {
        if (e == null) {
            return false;
        }
        float val = e.getVal();
        if (this.mValues == null) {
            this.mValues = new ArrayList();
        }
        if (this.mValues.size() == 0) {
            this.mYMax = val;
            this.mYMin = val;
        } else {
            if (this.mYMax < val) {
                this.mYMax = val;
            }
            if (this.mYMin > val) {
                this.mYMin = val;
            }
        }
        this.mValues.add(e);
        return true;
    }

    public boolean removeEntry(S e) {
        if (e == null) {
            return false;
        }
        if (this.mValues == null) {
            return false;
        }
        boolean removed = this.mValues.remove(e);
        if (!removed) {
            return removed;
        }
        calcMinMax(0, this.mValues.size());
        return removed;
    }

    public void addEntryOrdered(S e) {
        if (e != null) {
            float val = e.getVal();
            if (this.mValues == null) {
                this.mValues = new ArrayList();
            }
            if (this.mValues.size() == 0) {
                this.mYMax = val;
                this.mYMin = val;
            } else {
                if (this.mYMax < val) {
                    this.mYMax = val;
                }
                if (this.mYMin > val) {
                    this.mYMin = val;
                }
            }
            if (this.mValues.size() <= 0 || ((Entry) this.mValues.get(this.mValues.size() - 1)).getXIndex() <= e.getXIndex()) {
                this.mValues.add(e);
                return;
            }
            this.mValues.add(getEntryIndex(e.getXIndex(), DataSet.Rounding.UP), e);
        }
    }

    public List<S> getValues() {
        return this.mValues;
    }

    public void clear() {
        this.mValues.clear();
        notifyDataSetChanged();
    }

    public RealmResults<T> getResults() {
        return this.results;
    }

    public String getValuesField() {
        return this.mValuesField;
    }

    public void setValuesField(String yValuesField) {
        this.mValuesField = yValuesField;
    }

    public String getIndexField() {
        return this.mIndexField;
    }

    public void setIndexField(String xIndexField) {
        this.mIndexField = xIndexField;
    }
}
