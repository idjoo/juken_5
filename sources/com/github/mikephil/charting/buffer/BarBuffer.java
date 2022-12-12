package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class BarBuffer extends AbstractBuffer<IBarDataSet> {
    protected float mBarSpace = 0.0f;
    protected boolean mContainsStacks = false;
    protected int mDataSetCount = 1;
    protected int mDataSetIndex = 0;
    protected float mGroupSpace = 0.0f;
    protected boolean mInverted = false;

    public BarBuffer(int size, float groupspace, int dataSetCount, boolean containsStacks) {
        super(size);
        this.mGroupSpace = groupspace;
        this.mDataSetCount = dataSetCount;
        this.mContainsStacks = containsStacks;
    }

    public void setBarSpace(float barspace) {
        this.mBarSpace = barspace;
    }

    public void setDataSet(int index) {
        this.mDataSetIndex = index;
    }

    public void setInverted(boolean inverted) {
        this.mInverted = inverted;
    }

    /* access modifiers changed from: protected */
    public void addBar(float left, float top, float right, float bottom) {
        float[] fArr = this.buffer;
        int i = this.index;
        this.index = i + 1;
        fArr[i] = left;
        float[] fArr2 = this.buffer;
        int i2 = this.index;
        this.index = i2 + 1;
        fArr2[i2] = top;
        float[] fArr3 = this.buffer;
        int i3 = this.index;
        this.index = i3 + 1;
        fArr3[i3] = right;
        float[] fArr4 = this.buffer;
        int i4 = this.index;
        this.index = i4 + 1;
        fArr4[i4] = bottom;
    }

    public void feed(IBarDataSet data) {
        float top;
        float bottom;
        float y;
        float yStart;
        float top2;
        float bottom2;
        float size = ((float) data.getEntryCount()) * this.phaseX;
        int dataSetOffset = this.mDataSetCount - 1;
        float barSpaceHalf = this.mBarSpace / 2.0f;
        float groupSpaceHalf = this.mGroupSpace / 2.0f;
        for (int i = 0; ((float) i) < size; i++) {
            BarEntry e = (BarEntry) data.getEntryForIndex(i);
            float x = ((float) (e.getXIndex() + (e.getXIndex() * dataSetOffset) + this.mDataSetIndex)) + (this.mGroupSpace * ((float) e.getXIndex())) + groupSpaceHalf;
            float y2 = e.getVal();
            float[] vals = e.getVals();
            if (!this.mContainsStacks || vals == null) {
                float left = (x - 0.5f) + barSpaceHalf;
                float right = (x + 0.5f) - barSpaceHalf;
                if (this.mInverted) {
                    bottom = y2 >= 0.0f ? y2 : 0.0f;
                    top = y2 <= 0.0f ? y2 : 0.0f;
                } else {
                    top = y2 >= 0.0f ? y2 : 0.0f;
                    bottom = y2 <= 0.0f ? y2 : 0.0f;
                }
                if (top > 0.0f) {
                    top *= this.phaseY;
                } else {
                    bottom *= this.phaseY;
                }
                addBar(left, top, right, bottom);
            } else {
                float posY = 0.0f;
                float negY = -e.getNegativeSum();
                for (float value : vals) {
                    if (value >= 0.0f) {
                        y = posY;
                        yStart = posY + value;
                        posY = yStart;
                    } else {
                        y = negY;
                        yStart = negY + Math.abs(value);
                        negY += Math.abs(value);
                    }
                    float left2 = (x - 0.5f) + barSpaceHalf;
                    float right2 = (x + 0.5f) - barSpaceHalf;
                    if (this.mInverted) {
                        if (y >= yStart) {
                            bottom2 = y;
                        } else {
                            bottom2 = yStart;
                        }
                        if (y <= yStart) {
                            top2 = y;
                        } else {
                            top2 = yStart;
                        }
                    } else {
                        if (y >= yStart) {
                            top2 = y;
                        } else {
                            top2 = yStart;
                        }
                        if (y <= yStart) {
                            bottom2 = y;
                        } else {
                            bottom2 = yStart;
                        }
                    }
                    addBar(left2, top2 * this.phaseY, right2, bottom2 * this.phaseY);
                }
            }
        }
        reset();
    }
}
