package com.androidplot.xy;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.androidplot.util.FastNumber;
import java.util.ArrayList;
import java.util.List;

public class FixedSizeEditableXYSeries implements EditableXYSeries {
    private String title;
    @NonNull
    private List<FastNumber> xVals = new ArrayList();
    @NonNull
    private List<FastNumber> yVals = new ArrayList();

    public FixedSizeEditableXYSeries(String title2, int size) {
        setTitle(title2);
        resize(size);
    }

    public void setX(@Nullable Number x, int index) {
        this.xVals.set(index, FastNumber.orNull(x));
    }

    public void setY(@Nullable Number y, int index) {
        this.yVals.set(index, FastNumber.orNull(y));
    }

    public void resize(int size) {
        resize(this.xVals, size);
        resize(this.yVals, size);
    }

    /* access modifiers changed from: protected */
    public void resize(@NonNull List list, int size) {
        if (size > list.size()) {
            while (list.size() < size) {
                list.add((Object) null);
            }
        } else if (size < list.size()) {
            while (list.size() > size) {
                list.remove(list.size() - 1);
            }
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public int size() {
        return this.xVals.size();
    }

    public Number getX(int index) {
        return this.xVals.get(index);
    }

    public Number getY(int index) {
        return this.yVals.get(index);
    }
}
