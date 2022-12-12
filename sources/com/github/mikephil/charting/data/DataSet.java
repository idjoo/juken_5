package com.github.mikephil.charting.data;

import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;
import java.util.List;

public abstract class DataSet<T extends Entry> extends BaseDataSet<T> {
    protected float mYMax = 0.0f;
    protected float mYMin = 0.0f;
    protected List<T> mYVals = null;

    public enum Rounding {
        UP,
        DOWN,
        CLOSEST
    }

    public abstract DataSet<T> copy();

    public DataSet(List<T> yVals, String label) {
        super(label);
        this.mYVals = yVals;
        if (this.mYVals == null) {
            this.mYVals = new ArrayList();
        }
        calcMinMax(0, this.mYVals.size());
    }

    public void calcMinMax(int start, int end) {
        int yValCount;
        int endValue;
        if (this.mYVals != null && (yValCount = this.mYVals.size()) != 0) {
            if (end == 0 || end >= yValCount) {
                endValue = yValCount - 1;
            } else {
                endValue = end;
            }
            this.mYMin = Float.MAX_VALUE;
            this.mYMax = -3.4028235E38f;
            for (int i = start; i <= endValue; i++) {
                T e = (Entry) this.mYVals.get(i);
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

    public int getEntryCount() {
        return this.mYVals.size();
    }

    public List<T> getYVals() {
        return this.mYVals;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(toSimpleString());
        for (int i = 0; i < this.mYVals.size(); i++) {
            buffer.append(((Entry) this.mYVals.get(i)).toString() + " ");
        }
        return buffer.toString();
    }

    public String toSimpleString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("DataSet, label: " + (getLabel() == null ? "" : getLabel()) + ", entries: " + this.mYVals.size() + "\n");
        return buffer.toString();
    }

    public float getYMin() {
        return this.mYMin;
    }

    public float getYMax() {
        return this.mYMax;
    }

    public void addEntryOrdered(T e) {
        if (e != null) {
            float val = e.getVal();
            if (this.mYVals == null) {
                this.mYVals = new ArrayList();
            }
            if (this.mYVals.size() == 0) {
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
            if (this.mYVals.size() <= 0 || ((Entry) this.mYVals.get(this.mYVals.size() - 1)).getXIndex() <= e.getXIndex()) {
                this.mYVals.add(e);
                return;
            }
            this.mYVals.add(getEntryIndex(e.getXIndex(), Rounding.UP), e);
        }
    }

    public void clear() {
        this.mYVals.clear();
        notifyDataSetChanged();
    }

    public boolean addEntry(T e) {
        if (e == null) {
            return false;
        }
        float val = e.getVal();
        List<T> yVals = getYVals();
        if (yVals == null) {
            yVals = new ArrayList<>();
        }
        if (yVals.size() == 0) {
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
        yVals.add(e);
        return true;
    }

    public boolean removeEntry(T e) {
        if (e == null) {
            return false;
        }
        if (this.mYVals == null) {
            return false;
        }
        boolean removed = this.mYVals.remove(e);
        if (!removed) {
            return removed;
        }
        calcMinMax(0, this.mYVals.size());
        return removed;
    }

    public int getEntryIndex(Entry e) {
        return this.mYVals.indexOf(e);
    }

    public T getEntryForXIndex(int xIndex, Rounding rounding) {
        int index = getEntryIndex(xIndex, rounding);
        if (index > -1) {
            return (Entry) this.mYVals.get(index);
        }
        return null;
    }

    public T getEntryForXIndex(int xIndex) {
        return getEntryForXIndex(xIndex, Rounding.CLOSEST);
    }

    public T getEntryForIndex(int index) {
        return (Entry) this.mYVals.get(index);
    }

    public int getEntryIndex(int xIndex, Rounding rounding) {
        int low = 0;
        int high = this.mYVals.size() - 1;
        int closest = -1;
        while (low <= high) {
            int m = (high + low) / 2;
            if (xIndex == ((Entry) this.mYVals.get(m)).getXIndex()) {
                while (m > 0 && ((Entry) this.mYVals.get(m - 1)).getXIndex() == xIndex) {
                    m--;
                }
                return m;
            }
            if (xIndex > ((Entry) this.mYVals.get(m)).getXIndex()) {
                low = m + 1;
            } else {
                high = m - 1;
            }
            closest = m;
        }
        if (closest != -1) {
            int closestXIndex = ((Entry) this.mYVals.get(closest)).getXIndex();
            if (rounding == Rounding.UP) {
                if (closestXIndex < xIndex && closest < this.mYVals.size() - 1) {
                    closest++;
                }
            } else if (rounding == Rounding.DOWN && closestXIndex > xIndex && closest > 0) {
                closest--;
            }
        }
        return closest;
    }

    public float getYValForXIndex(int xIndex) {
        Entry e = getEntryForXIndex(xIndex);
        if (e == null || e.getXIndex() != xIndex) {
            return Float.NaN;
        }
        return e.getVal();
    }

    public List<T> getEntriesForXIndex(int xIndex) {
        List<T> entries = new ArrayList<>();
        int low = 0;
        int high = this.mYVals.size() - 1;
        while (low <= high) {
            int m = (high + low) / 2;
            T entry = (Entry) this.mYVals.get(m);
            if (xIndex == entry.getXIndex()) {
                while (m > 0 && ((Entry) this.mYVals.get(m - 1)).getXIndex() == xIndex) {
                    m--;
                }
                high = this.mYVals.size();
                while (m < high) {
                    entry = (Entry) this.mYVals.get(m);
                    if (entry.getXIndex() != xIndex) {
                        break;
                    }
                    entries.add(entry);
                    m++;
                }
            }
            if (xIndex > entry.getXIndex()) {
                low = m + 1;
            } else {
                high = m - 1;
            }
        }
        return entries;
    }
}
