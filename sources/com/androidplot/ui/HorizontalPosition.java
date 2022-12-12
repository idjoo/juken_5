package com.androidplot.ui;

import com.androidplot.ui.PositionMetric;

public class HorizontalPosition extends PositionMetric<HorizontalPositioning> {
    public HorizontalPosition(float value, HorizontalPositioning layoutStyle) {
        super(value, layoutStyle);
        validatePair(value, layoutStyle);
    }

    /* access modifiers changed from: protected */
    public void validatePair(float value, HorizontalPositioning layoutStyle) {
        switch (layoutStyle) {
            case ABSOLUTE_FROM_LEFT:
            case ABSOLUTE_FROM_RIGHT:
            case ABSOLUTE_FROM_CENTER:
                validateValue(value, PositionMetric.LayoutMode.ABSOLUTE);
                return;
            case RELATIVE_TO_LEFT:
            case RELATIVE_TO_RIGHT:
            case RELATIVE_TO_CENTER:
                validateValue(value, PositionMetric.LayoutMode.RELATIVE);
                return;
            default:
                return;
        }
    }

    public float getPixelValue(float size) {
        switch ((HorizontalPositioning) getLayoutType()) {
            case ABSOLUTE_FROM_LEFT:
                return getAbsolutePosition(size, PositionMetric.Origin.FROM_BEGINING);
            case ABSOLUTE_FROM_RIGHT:
                return getAbsolutePosition(size, PositionMetric.Origin.FROM_END);
            case ABSOLUTE_FROM_CENTER:
                return getAbsolutePosition(size, PositionMetric.Origin.FROM_CENTER);
            case RELATIVE_TO_LEFT:
                return getRelativePosition(size, PositionMetric.Origin.FROM_BEGINING);
            case RELATIVE_TO_RIGHT:
                return getRelativePosition(size, PositionMetric.Origin.FROM_END);
            case RELATIVE_TO_CENTER:
                return getRelativePosition(size, PositionMetric.Origin.FROM_CENTER);
            default:
                throw new IllegalArgumentException("Unsupported LayoutType: " + getLayoutType());
        }
    }

    public void setLayoutType(HorizontalPositioning layoutType) {
        super.setLayoutType(layoutType);
    }
}
