package com.github.mikephil.charting.data;

public class BubbleEntry extends Entry {
    private float mSize = 0.0f;

    public BubbleEntry(int xIndex, float val, float size) {
        super(val, xIndex);
        this.mSize = size;
    }

    public BubbleEntry(int xIndex, float val, float size, Object data) {
        super(val, xIndex, data);
        this.mSize = size;
    }

    public BubbleEntry copy() {
        return new BubbleEntry(getXIndex(), getVal(), this.mSize, getData());
    }

    public float getSize() {
        return this.mSize;
    }

    public void setSize(float size) {
        this.mSize = size;
    }
}
