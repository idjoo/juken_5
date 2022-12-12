package com.androidplot.xy;

import android.graphics.Canvas;
import com.androidplot.Plot;
import com.androidplot.PlotListener;
import com.androidplot.xy.OrderedXYSeries;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SimpleXYSeries implements EditableXYSeries, OrderedXYSeries, PlotListener {
    private ReentrantReadWriteLock lock;
    private volatile String title;
    private OrderedXYSeries.XOrder xOrder;
    private volatile LinkedList<Number> xVals;
    private volatile LinkedList<Number> yVals;

    public enum ArrayFormat {
        Y_VALS_ONLY,
        XY_VALS_INTERLEAVED
    }

    public SimpleXYSeries(String title2) {
        this.xVals = new LinkedList<>();
        this.yVals = new LinkedList<>();
        this.title = null;
        this.lock = new ReentrantReadWriteLock(true);
        this.xOrder = OrderedXYSeries.XOrder.NONE;
        this.title = title2;
    }

    public SimpleXYSeries(ArrayFormat format, String title2, Number... model) {
        this((List<? extends Number>) asNumberList(model), format, title2);
    }

    public OrderedXYSeries.XOrder getXOrder() {
        return this.xOrder;
    }

    public void setXOrder(OrderedXYSeries.XOrder xOrder2) {
        this.xOrder = xOrder2;
    }

    public void onBeforeDraw(Plot source, Canvas canvas) {
        this.lock.readLock().lock();
    }

    public void onAfterDraw(Plot source, Canvas canvas) {
        this.lock.readLock().unlock();
    }

    protected static List<Number> asNumberList(Number... model) {
        List<Number> numbers = new ArrayList<>(model.length);
        Collections.addAll(numbers, model);
        return numbers;
    }

    public SimpleXYSeries(List<? extends Number> model, ArrayFormat format, String title2) {
        this(title2);
        setModel(model, format);
    }

    public SimpleXYSeries(List<? extends Number> xVals2, List<? extends Number> yVals2, String title2) {
        this(title2);
        if (xVals2 == null || yVals2 == null) {
            throw new IllegalArgumentException("Neither the xVals nor the yVals parameters may be null.");
        } else if (xVals2.size() != yVals2.size()) {
            throw new IllegalArgumentException("xVals and yVals List parameters must be of the same size.");
        } else {
            this.xVals.addAll(xVals2);
            this.yVals.addAll(yVals2);
        }
    }

    public void useImplicitXVals() {
        this.lock.writeLock().lock();
        try {
            this.xVals = null;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void setModel(List<? extends Number> model, ArrayFormat format) {
        this.lock.writeLock().lock();
        try {
            this.xVals.clear();
            this.yVals.clear();
            if (model != null && model.size() != 0) {
                switch (format) {
                    case Y_VALS_ONLY:
                        this.yVals.addAll(model);
                        for (int i = 0; i < this.yVals.size(); i++) {
                            this.xVals.add(Integer.valueOf(i));
                        }
                        break;
                    case XY_VALS_INTERLEAVED:
                        if (this.xVals == null) {
                            this.xVals = new LinkedList<>();
                        }
                        if (model.size() % 2 == 0) {
                            int sz = model.size() / 2;
                            int i2 = 0;
                            int j = 0;
                            while (i2 < sz) {
                                this.xVals.add(model.get(j));
                                this.yVals.add(model.get(j + 1));
                                i2++;
                                j += 2;
                            }
                            break;
                        } else {
                            throw new IndexOutOfBoundsException("Cannot auto-generate series from odd-sized xy List.");
                        }
                    default:
                        throw new IllegalArgumentException("Unexpected enum value: " + format);
                }
                this.lock.writeLock().unlock();
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void setX(Number value, int index) {
        this.lock.writeLock().lock();
        try {
            this.xVals.set(index, value);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void setY(Number value, int index) {
        this.lock.writeLock().lock();
        try {
            this.yVals.set(index, value);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void resize(int size) {
        try {
            this.lock.writeLock().lock();
            if (this.xVals.size() < size) {
                for (int i = this.xVals.size(); i < size; i++) {
                    this.xVals.add((Object) null);
                    this.yVals.add((Object) null);
                }
            } else if (this.xVals.size() > size) {
                for (int i2 = this.xVals.size(); i2 > size; i2--) {
                    this.xVals.removeLast();
                    this.yVals.removeLast();
                }
            }
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void setXY(Number xVal, Number yVal, int index) {
        this.lock.writeLock().lock();
        try {
            this.yVals.set(index, yVal);
            this.xVals.set(index, xVal);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void addFirst(Number x, Number y) {
        this.lock.writeLock().lock();
        try {
            if (this.xVals != null) {
                this.xVals.addFirst(x);
            }
            this.yVals.addFirst(y);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public XYCoords removeFirst() {
        this.lock.writeLock().lock();
        try {
            if (size() <= 0) {
                throw new NoSuchElementException();
            }
            return new XYCoords(this.xVals != null ? this.xVals.removeFirst() : 0, this.yVals.removeFirst());
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void addLast(Number x, Number y) {
        this.lock.writeLock().lock();
        try {
            if (this.xVals != null) {
                this.xVals.addLast(x);
            }
            this.yVals.addLast(y);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public XYCoords removeLast() {
        this.lock.writeLock().lock();
        try {
            if (size() <= 0) {
                throw new NoSuchElementException();
            }
            return new XYCoords(this.xVals != null ? this.xVals.removeLast() : Integer.valueOf(this.yVals.size() - 1), this.yVals.removeLast());
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.lock.writeLock().lock();
        try {
            this.title = title2;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public int size() {
        if (this.yVals != null) {
            return this.yVals.size();
        }
        return 0;
    }

    public Number getX(int index) {
        return this.xVals != null ? this.xVals.get(index) : Integer.valueOf(index);
    }

    public Number getY(int index) {
        return this.yVals.get(index);
    }

    public LinkedList<Number> getxVals() {
        return this.xVals;
    }

    public LinkedList<Number> getyVals() {
        return this.yVals;
    }

    public void clear() {
        this.lock.writeLock().lock();
        try {
            if (this.xVals != null) {
                this.xVals.clear();
            }
            this.yVals.clear();
        } finally {
            this.lock.writeLock().unlock();
        }
    }
}
