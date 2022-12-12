package com.github.mikephil.charting.animation;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;

@SuppressLint({"NewApi"})
public interface EasingFunction extends TimeInterpolator {
    float getInterpolation(float f);
}
