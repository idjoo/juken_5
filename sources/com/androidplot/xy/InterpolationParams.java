package com.androidplot.xy;

import com.androidplot.xy.Interpolator;

public interface InterpolationParams<InterpolatorType extends Interpolator> {
    Class<InterpolatorType> getInterpolatorClass();
}
