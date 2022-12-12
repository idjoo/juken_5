package com.androidplot.xy;

import android.graphics.RectF;
import com.androidplot.Region;

public class XYStepCalculator {
    public static Step getStep(XYPlot plot, Axis axisType, RectF pixRect) {
        switch (axisType) {
            case DOMAIN:
                return getStep(plot.getDomainStepMode(), plot.getDomainStepValue(), plot.getBounds().getxRegion(), new Region(Float.valueOf(pixRect.left), Float.valueOf(pixRect.right)));
            case RANGE:
                return getStep(plot.getRangeStepMode(), plot.getRangeStepValue(), plot.getBounds().getyRegion(), new Region(Float.valueOf(pixRect.top), Float.valueOf(pixRect.bottom)));
            default:
                return null;
        }
    }

    public static Step getStep(StepMode typeXY, double stepValue, Region realBounds, Region pixelBounds) {
        double stepVal = 0.0d;
        double stepPix = 0.0d;
        double stepCount = 0.0d;
        switch (typeXY) {
            case INCREMENT_BY_VAL:
            case INCREMENT_BY_FIT:
                stepVal = stepValue;
                stepPix = stepValue / realBounds.ratio(pixelBounds).doubleValue();
                stepCount = pixelBounds.length().doubleValue() / stepPix;
                break;
            case INCREMENT_BY_PIXELS:
                stepPix = stepValue;
                stepVal = realBounds.ratio(pixelBounds).doubleValue() * stepPix;
                stepCount = pixelBounds.length().doubleValue() / stepPix;
                break;
            case SUBDIVIDE:
                stepCount = stepValue;
                stepPix = pixelBounds.length().doubleValue() / (stepCount - 1.0d);
                stepVal = realBounds.ratio(pixelBounds).doubleValue() * stepPix;
                break;
        }
        return new Step(stepCount, stepPix, stepVal);
    }
}
