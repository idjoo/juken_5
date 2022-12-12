package com.androidplot.ui;

import com.androidplot.ui.PositionMetric;

public class VerticalPosition extends PositionMetric<VerticalPositioning> {
    public VerticalPosition(float value, VerticalPositioning layoutStyle) {
        super(value, layoutStyle);
    }

    /* access modifiers changed from: protected */
    public void validatePair(float value, VerticalPositioning layoutStyle) {
        switch (layoutStyle) {
            case ABSOLUTE_FROM_TOP:
            case ABSOLUTE_FROM_BOTTOM:
            case ABSOLUTE_FROM_CENTER:
                validateValue(value, PositionMetric.LayoutMode.ABSOLUTE);
                return;
            case RELATIVE_TO_TOP:
            case RELATIVE_TO_BOTTOM:
            case RELATIVE_TO_CENTER:
                validateValue(value, PositionMetric.LayoutMode.RELATIVE);
                return;
            default:
                return;
        }
    }

    public float getPixelValue(float size) {
        switch ((VerticalPositioning) getLayoutType()) {
            case ABSOLUTE_FROM_TOP:
                return getAbsolutePosition(size, PositionMetric.Origin.FROM_BEGINING);
            case ABSOLUTE_FROM_BOTTOM:
                return getAbsolutePosition(size, PositionMetric.Origin.FROM_END);
            case ABSOLUTE_FROM_CENTER:
                return getAbsolutePosition(size, PositionMetric.Origin.FROM_CENTER);
            case RELATIVE_TO_TOP:
                return getRelativePosition(size, PositionMetric.Origin.FROM_BEGINING);
            case RELATIVE_TO_BOTTOM:
                return getRelativePosition(size, PositionMetric.Origin.FROM_END);
            case RELATIVE_TO_CENTER:
                return getRelativePosition(size, PositionMetric.Origin.FROM_CENTER);
            default:
                throw new IllegalArgumentException("Unsupported LayoutType: " + getLayoutType());
        }
    }

    public void setLayoutType(VerticalPositioning layoutType) {
        super.setLayoutType(layoutType);
    }
}
