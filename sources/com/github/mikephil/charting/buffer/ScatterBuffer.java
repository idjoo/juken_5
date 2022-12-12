package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

public class ScatterBuffer extends AbstractBuffer<IScatterDataSet> {
    public ScatterBuffer(int size) {
        super(size);
    }

    /* access modifiers changed from: protected */
    public void addForm(float x, float y) {
        float[] fArr = this.buffer;
        int i = this.index;
        this.index = i + 1;
        fArr[i] = x;
        float[] fArr2 = this.buffer;
        int i2 = this.index;
        this.index = i2 + 1;
        fArr2[i2] = y;
    }

    public void feed(IScatterDataSet data) {
        float size = ((float) data.getEntryCount()) * this.phaseX;
        for (int i = 0; ((float) i) < size; i++) {
            Entry e = data.getEntryForIndex(i);
            addForm((float) e.getXIndex(), e.getVal() * this.phaseY);
        }
        reset();
    }
}
