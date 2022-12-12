package com.androidplot.ui;

public class SizeMetric extends LayoutMetric<SizeMode> {
    public /* bridge */ /* synthetic */ float getValue() {
        return super.getValue();
    }

    public /* bridge */ /* synthetic */ void setValue(float f) {
        super.setValue(f);
    }

    public SizeMetric(float value, SizeMode layoutType) {
        super(value, layoutType);
    }

    /* access modifiers changed from: protected */
    public void validatePair(float value, SizeMode layoutType) {
        switch (layoutType) {
            case RELATIVE:
                if (value < 0.0f || value > 1.0f) {
                    throw new IllegalArgumentException("SizeMetric Relative and Hybrid layout values must be within the range of 0 to 1.");
                }
                return;
            default:
                return;
        }
    }

    public float getPixelValue(float size) {
        switch ((SizeMode) getLayoutType()) {
            case RELATIVE:
                return getValue() * size;
            case ABSOLUTE:
                return getValue();
            case FILL:
                return size - getValue();
            default:
                throw new IllegalArgumentException("Unsupported LayoutType: " + getLayoutType());
        }
    }

    public void setLayoutType(SizeMode layoutType) {
        super.setLayoutType(layoutType);
    }
}
