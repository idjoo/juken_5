package com.androidplot.ui;

import android.graphics.RectF;
import java.util.Iterator;

public abstract class TableModel {
    private TableOrder order;

    public enum Axis {
        ROW,
        COLUMN
    }

    public enum CellSizingMethod {
        FIXED,
        FILL
    }

    public abstract Iterator<RectF> getIterator(RectF rectF, int i);

    protected TableModel(TableOrder order2) {
        setOrder(order2);
    }

    public TableOrder getOrder() {
        return this.order;
    }

    public void setOrder(TableOrder order2) {
        this.order = order2;
    }
}
