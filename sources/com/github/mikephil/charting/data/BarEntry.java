package com.github.mikephil.charting.data;

public class BarEntry extends Entry {
    private float mNegativeSum;
    private float mPositiveSum;
    private float[] mVals;

    public BarEntry(float[] vals, int xIndex) {
        super(calcSum(vals), xIndex);
        this.mVals = vals;
        calcPosNegSum();
    }

    public BarEntry(float val, int xIndex) {
        super(val, xIndex);
    }

    public BarEntry(float[] vals, int xIndex, String label) {
        super(calcSum(vals), xIndex, label);
        this.mVals = vals;
        calcPosNegSum();
    }

    public BarEntry(float val, int xIndex, Object data) {
        super(val, xIndex, data);
    }

    public BarEntry copy() {
        BarEntry copied = new BarEntry(getVal(), getXIndex(), getData());
        copied.setVals(this.mVals);
        return copied;
    }

    public float[] getVals() {
        return this.mVals;
    }

    public void setVals(float[] vals) {
        setVal(calcSum(vals));
        this.mVals = vals;
        calcPosNegSum();
    }

    public float getVal() {
        return super.getVal();
    }

    public boolean isStacked() {
        return this.mVals != null;
    }

    public float getBelowSum(int stackIndex) {
        if (this.mVals == null) {
            return 0.0f;
        }
        float remainder = 0.0f;
        int index = this.mVals.length - 1;
        while (index > stackIndex && index >= 0) {
            remainder += this.mVals[index];
            index--;
        }
        return remainder;
    }

    public float getPositiveSum() {
        return this.mPositiveSum;
    }

    public float getNegativeSum() {
        return this.mNegativeSum;
    }

    private void calcPosNegSum() {
        if (this.mVals == null) {
            this.mNegativeSum = 0.0f;
            this.mPositiveSum = 0.0f;
            return;
        }
        float sumNeg = 0.0f;
        float sumPos = 0.0f;
        for (float f : this.mVals) {
            if (f <= 0.0f) {
                sumNeg += Math.abs(f);
            } else {
                sumPos += f;
            }
        }
        this.mNegativeSum = sumNeg;
        this.mPositiveSum = sumPos;
    }

    private static float calcSum(float[] vals) {
        if (vals == null) {
            return 0.0f;
        }
        float sum = 0.0f;
        for (float f : vals) {
            sum += f;
        }
        return sum;
    }
}
