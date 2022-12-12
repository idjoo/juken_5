package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class HorizontalBarBuffer extends BarBuffer {
    public HorizontalBarBuffer(int size, float groupspace, int dataSetCount, boolean containsStacks) {
        super(size, groupspace, dataSetCount, containsStacks);
    }

    public void feed(IBarDataSet data) {
        float right;
        float left;
        float y;
        float yStart;
        float right2;
        float left2;
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
                float bottom = (x - 0.5f) + barSpaceHalf;
                float top = (x + 0.5f) - barSpaceHalf;
                if (this.mInverted) {
                    left = y2 >= 0.0f ? y2 : 0.0f;
                    right = y2 <= 0.0f ? y2 : 0.0f;
                } else {
                    right = y2 >= 0.0f ? y2 : 0.0f;
                    left = y2 <= 0.0f ? y2 : 0.0f;
                }
                if (right > 0.0f) {
                    right *= this.phaseY;
                } else {
                    left *= this.phaseY;
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
                    float bottom2 = (x - 0.5f) + barSpaceHalf;
                    float top2 = (x + 0.5f) - barSpaceHalf;
                    if (this.mInverted) {
                        if (y >= yStart) {
                            left2 = y;
                        } else {
                            left2 = yStart;
                        }
                        if (y <= yStart) {
                            right2 = y;
                        } else {
                            right2 = yStart;
                        }
                    } else {
                        if (y >= yStart) {
                            right2 = y;
                        } else {
                            right2 = yStart;
                        }
                        if (y <= yStart) {
                            left2 = y;
                        } else {
                            left2 = yStart;
                        }
                    }
                    addBar(left2 * this.phaseY, top2, right2 * this.phaseY, bottom2);
                }
            }
        }
        reset();
    }
}
