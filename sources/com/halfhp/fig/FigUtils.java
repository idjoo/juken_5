package com.halfhp.fig;

import android.content.Context;
import android.util.TypedValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class FigUtils {
    protected static final Pattern DIMENSION_PATTERN = Pattern.compile(DIMENSION_REGEX);
    protected static final String DIMENSION_REGEX = "^\\-?\\s*(\\d+(\\.\\d+)*)\\s*([a-zA-Z]+)\\s*$";
    public static final Map<String, Integer> dimensionConstantLookup = initDimensionConstantLookup();

    FigUtils() {
    }

    private static Map<String, Integer> initDimensionConstantLookup() {
        Map<String, Integer> m = new HashMap<>();
        m.put("px", 0);
        m.put("dip", 1);
        m.put("dp", 1);
        m.put("sp", 2);
        m.put("pt", 3);
        m.put("in", 4);
        m.put("mm", 5);
        return Collections.unmodifiableMap(m);
    }

    private static class InternalDimension {
        int unit;
        float value;

        public InternalDimension(float value2, int unit2) {
            this.value = value2;
            this.unit = unit2;
        }
    }

    public static float stringToDimension(Context context, String dimension) {
        InternalDimension internalDimension = stringToInternalDimension(dimension);
        return TypedValue.applyDimension(internalDimension.unit, internalDimension.value, context.getResources().getDisplayMetrics());
    }

    private static InternalDimension stringToInternalDimension(String dimension) {
        Matcher matcher = DIMENSION_PATTERN.matcher(dimension);
        if (matcher.matches()) {
            float value = Float.valueOf(matcher.group(1)).floatValue();
            Integer dimensionUnit = dimensionConstantLookup.get(matcher.group(3).toLowerCase());
            if (dimensionUnit != null) {
                return new InternalDimension(value, dimensionUnit.intValue());
            }
            throw new NumberFormatException();
        }
        throw new NumberFormatException();
    }
}
