package com.androidplot.xy;

import com.androidplot.Region;
import java.util.Arrays;

public class StepModelFit extends StepModel {
    private Region scale;
    private double[] steps;

    public StepModelFit(Region axisRegion, double[] increments, double numLines) {
        super(StepMode.INCREMENT_BY_FIT, numLines);
        setSteps(increments);
        setScale(axisRegion);
    }

    public double[] getSteps() {
        return this.steps;
    }

    public void setSteps(double[] steps2) {
        if (steps2 != null && steps2.length != 0) {
            int length = steps2.length;
            int i = 0;
            while (i < length) {
                if (steps2[i] > 0.0d) {
                    i++;
                } else {
                    return;
                }
            }
            this.steps = steps2;
        }
    }

    public Region getScale() {
        return this.scale;
    }

    public void setScale(Region scale2) {
        this.scale = scale2;
    }

    public double getValue() {
        if (this.steps == null || this.scale == null || !this.scale.isDefined()) {
            return super.getValue();
        }
        double curStep = this.steps[0];
        double oldDistance = Math.abs((this.scale.length().doubleValue() / curStep) - super.getValue());
        for (double step : this.steps) {
            double newDistance = Math.abs((this.scale.length().doubleValue() / step) - super.getValue());
            if (newDistance < oldDistance) {
                curStep = step;
                oldDistance = newDistance;
            }
        }
        return curStep;
    }

    public String toString() {
        return "StepModelFit{steps=" + Arrays.toString(this.steps) + ", scale=" + this.scale + ", current stepping=" + getValue() + '}';
    }
}
