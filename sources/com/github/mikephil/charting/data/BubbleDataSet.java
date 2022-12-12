package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class BubbleDataSet extends BarLineScatterCandleBubbleDataSet<BubbleEntry> implements IBubbleDataSet {
    private float mHighlightCircleWidth = 2.5f;
    protected float mMaxSize;
    protected float mXMax;
    protected float mXMin;

    public BubbleDataSet(List<BubbleEntry> yVals, String label) {
        super(yVals, label);
    }

    public void setHighlightCircleWidth(float width) {
        this.mHighlightCircleWidth = Utils.convertDpToPixel(width);
    }

    public float getHighlightCircleWidth() {
        return this.mHighlightCircleWidth;
    }

    public void calcMinMax(int start, int end) {
        int endValue;
        if (this.mYVals != null && this.mYVals.size() != 0) {
            if (end == 0 || end >= this.mYVals.size()) {
                endValue = this.mYVals.size() - 1;
            } else {
                endValue = end;
            }
            this.mYMin = yMin((BubbleEntry) this.mYVals.get(start));
            this.mYMax = yMax((BubbleEntry) this.mYVals.get(start));
            for (int i = start; i <= endValue; i++) {
                BubbleEntry entry = (BubbleEntry) this.mYVals.get(i);
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

    public DataSet<BubbleEntry> copy() {
        List<BubbleEntry> yVals = new ArrayList<>();
        for (int i = 0; i < this.mYVals.size(); i++) {
            yVals.add(((BubbleEntry) this.mYVals.get(i)).copy());
        }
        BubbleDataSet copied = new BubbleDataSet(yVals, getLabel());
        copied.mColors = this.mColors;
        copied.mHighLightColor = this.mHighLightColor;
        return copied;
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
}
