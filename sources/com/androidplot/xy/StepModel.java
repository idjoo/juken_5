package com.androidplot.xy;

public class StepModel {
    private StepMode mode;
    private double value;

    public StepModel(StepMode mode2, double value2) {
        setMode(mode2);
        setValue(value2);
    }

    public StepMode getMode() {
        return this.mode;
    }

    public void setMode(StepMode mode2) {
        this.mode = mode2;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value2) {
        this.value = value2;
    }
}
