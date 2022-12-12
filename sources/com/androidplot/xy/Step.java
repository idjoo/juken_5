package com.androidplot.xy;

public class Step {
    private final double stepCount;
    private final double stepPix;
    private final double stepVal;

    public Step(double stepCount2, double stepPix2, double stepVal2) {
        this.stepCount = stepCount2;
        this.stepPix = stepPix2;
        this.stepVal = stepVal2;
    }

    public double getStepCount() {
        return this.stepCount;
    }

    public double getStepPix() {
        return this.stepPix;
    }

    public double getStepVal() {
        return this.stepVal;
    }
}
