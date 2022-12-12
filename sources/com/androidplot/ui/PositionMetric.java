package com.androidplot.ui;

import java.lang.Enum;

public abstract class PositionMetric<LayoutType extends Enum> extends LayoutMetric<LayoutType> {

    protected enum LayoutMode {
        ABSOLUTE,
        RELATIVE
    }

    protected enum Origin {
        FROM_BEGINING,
        FROM_CENTER,
        FROM_END
    }

    public /* bridge */ /* synthetic */ Enum getLayoutType() {
        return super.getLayoutType();
    }

    public /* bridge */ /* synthetic */ float getValue() {
        return super.getValue();
    }

    public /* bridge */ /* synthetic */ void set(float f, Enum enumR) {
        super.set(f, enumR);
    }

    public /* bridge */ /* synthetic */ void setLayoutType(Enum enumR) {
        super.setLayoutType(enumR);
    }

    public /* bridge */ /* synthetic */ void setValue(float f) {
        super.setValue(f);
    }

    public PositionMetric(float value, LayoutType layoutType) {
        super(value, layoutType);
    }

    protected static void validateValue(float value, LayoutMode layoutMode) throws IllegalArgumentException {
        switch (layoutMode) {
            case ABSOLUTE:
                return;
            case RELATIVE:
                if (value < -1.0f || value > 1.0f) {
                    throw new IllegalArgumentException("Relative layout values must be within the range of -1 to 1.");
                }
                return;
            default:
                throw new IllegalArgumentException("Unknown LayoutMode: " + layoutMode);
        }
    }

    /* access modifiers changed from: protected */
    public float getAbsolutePosition(float size, Origin origin) {
        switch (origin) {
            case FROM_BEGINING:
                return getValue();
            case FROM_CENTER:
                return (size / 2.0f) + getValue();
            case FROM_END:
                return size - getValue();
            default:
                throw new IllegalArgumentException("Unsupported Origin: " + origin);
        }
    }

    /* access modifiers changed from: protected */
    public float getRelativePosition(float size, Origin origin) {
        switch (origin) {
            case FROM_BEGINING:
                return getValue() * size;
            case FROM_CENTER:
                return (size / 2.0f) + ((size / 2.0f) * getValue());
            case FROM_END:
                return (getValue() * size) + size;
            default:
                throw new IllegalArgumentException("Unsupported Origin: " + origin);
        }
    }
}
