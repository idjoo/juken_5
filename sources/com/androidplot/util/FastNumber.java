package com.androidplot.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FastNumber extends Number {
    private double doublePrimitive;
    private float floatPrimitive;
    private boolean hasDoublePrimitive;
    private boolean hasFloatPrimitive;
    private boolean hasIntPrimitive;
    private int intPrimitive;
    @NonNull
    private final Number number;

    public static FastNumber orNull(@NonNull Number number2) {
        if (number2 == null) {
            return null;
        }
        return new FastNumber(number2);
    }

    private FastNumber(@NonNull Number number2) {
        if (number2 == null) {
            throw new IllegalArgumentException("number parameter cannot be null");
        } else if (number2 instanceof FastNumber) {
            FastNumber rhs = (FastNumber) number2;
            this.number = rhs.number;
            this.hasDoublePrimitive = rhs.hasDoublePrimitive;
            this.hasFloatPrimitive = rhs.hasFloatPrimitive;
            this.hasIntPrimitive = rhs.hasIntPrimitive;
            this.doublePrimitive = rhs.doublePrimitive;
            this.floatPrimitive = rhs.floatPrimitive;
            this.intPrimitive = rhs.intPrimitive;
        } else {
            this.number = number2;
        }
    }

    public int intValue() {
        if (!this.hasIntPrimitive) {
            this.intPrimitive = this.number.intValue();
            this.hasIntPrimitive = true;
        }
        return this.intPrimitive;
    }

    public long longValue() {
        return this.number.longValue();
    }

    public float floatValue() {
        if (!this.hasFloatPrimitive) {
            this.floatPrimitive = this.number.floatValue();
            this.hasFloatPrimitive = true;
        }
        return this.floatPrimitive;
    }

    public double doubleValue() {
        if (!this.hasDoublePrimitive) {
            this.doublePrimitive = this.number.doubleValue();
            this.hasDoublePrimitive = true;
        }
        return this.doublePrimitive;
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.number.equals(((FastNumber) o).number);
    }

    public int hashCode() {
        return this.number.hashCode();
    }

    @NonNull
    public String toString() {
        return String.valueOf(doubleValue());
    }
}
