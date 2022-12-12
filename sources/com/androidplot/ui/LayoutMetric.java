package com.androidplot.ui;

import java.lang.Enum;

abstract class LayoutMetric<LayoutType extends Enum> {
    private LayoutType layoutType;
    private float value;

    public abstract float getPixelValue(float f);

    /* access modifiers changed from: protected */
    public abstract void validatePair(float f, LayoutType layouttype);

    public LayoutMetric(float value2, LayoutType layoutType2) {
        validatePair(value2, layoutType2);
        set(value2, layoutType2);
    }

    public void set(float value2, LayoutType layoutType2) {
        validatePair(value2, layoutType2);
        this.value = value2;
        this.layoutType = layoutType2;
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float value2) {
        validatePair(value2, this.layoutType);
        this.value = value2;
    }

    public LayoutType getLayoutType() {
        return this.layoutType;
    }

    public void setLayoutType(LayoutType layoutType2) {
        validatePair(this.value, layoutType2);
        this.layoutType = layoutType2;
    }
}
