package com.androidplot.util;

import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.Log;
import com.androidplot.ui.Anchor;
import com.androidplot.ui.BoxModelable;
import com.androidplot.ui.HorizontalPositioning;
import com.androidplot.ui.Insets;
import com.androidplot.ui.PositionMetrics;
import com.androidplot.ui.Size;
import com.androidplot.ui.SizeMetric;
import com.androidplot.ui.SizeMode;
import com.androidplot.ui.VerticalPositioning;
import com.androidplot.ui.widget.Widget;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.StepModel;

public class AttrUtils {
    private static final String TAG = AttrUtils.class.getName();

    public static void configureInsets(TypedArray attrs, Insets insets, int topAttr, int bottomAttr, int leftAttr, int rightAttr) {
        insets.setTop(attrs.getDimension(topAttr, insets.getTop()));
        insets.setBottom(attrs.getDimension(bottomAttr, insets.getBottom()));
        insets.setLeft(attrs.getDimension(leftAttr, insets.getLeft()));
        insets.setRight(attrs.getDimension(rightAttr, insets.getRight()));
    }

    public static void configureTextPaint(TypedArray attrs, Paint paint, int colorAttr, int textSizeAttr) {
        configureTextPaint(attrs, paint, colorAttr, textSizeAttr, (Integer) null);
    }

    public static void configureTextPaint(TypedArray attrs, Paint paint, int colorAttr, int textSizeAttr, Integer alignAttr) {
        if (attrs != null) {
            setColor(attrs, paint, colorAttr);
            setTextSize(attrs, paint, textSizeAttr);
            if (alignAttr != null && attrs.hasValue(alignAttr.intValue())) {
                configureTextAlign(attrs, paint, alignAttr.intValue());
            }
        }
    }

    public static void configureTextAlign(TypedArray attrs, Paint paint, int alignAttr) {
        if (attrs != null) {
            paint.setTextAlign(Paint.Align.values()[attrs.getInt(alignAttr, paint.getTextAlign().ordinal())]);
        }
    }

    public static void configureLinePaint(TypedArray attrs, Paint paint, int colorAttr, int strokeWidthAttr) {
        if (attrs != null) {
            setColor(attrs, paint, colorAttr);
            paint.setStrokeWidth(attrs.getDimension(strokeWidthAttr, paint.getStrokeWidth()));
        }
    }

    public static void setColor(TypedArray attrs, Paint paint, int attrId) {
        if (paint == null) {
            Log.w(TAG, "Attempt to configure null Paint property for attrId: " + attrId);
        } else {
            paint.setColor(attrs.getColor(attrId, paint.getColor()));
        }
    }

    public static void setTextSize(TypedArray attrs, Paint paint, int attrId) {
        paint.setTextSize(attrs.getDimension(attrId, paint.getTextSize()));
    }

    public static void configureBoxModelable(TypedArray attrs, BoxModelable model, int marginTop, int marginBottom, int marginLeft, int marginRight, int paddingTop, int paddingBottom, int paddingLeft, int paddingRight) {
        if (attrs != null) {
            model.setMargins(attrs.getDimension(marginLeft, model.getMarginLeft()), attrs.getDimension(marginTop, model.getMarginTop()), attrs.getDimension(marginRight, model.getMarginRight()), attrs.getDimension(marginBottom, model.getMarginBottom()));
            model.setPadding(attrs.getDimension(paddingLeft, model.getPaddingLeft()), attrs.getDimension(paddingTop, model.getPaddingTop()), attrs.getDimension(paddingRight, model.getPaddingRight()), attrs.getDimension(paddingBottom, model.getPaddingBottom()));
        }
    }

    public static void configureSize(TypedArray attrs, Size model, int heightSizeLayoutTypeAttr, int heightAttr, int widthSizeLayoutTypeAttr, int widthAttr) {
        if (attrs != null) {
            configureSizeMetric(attrs, model.getHeight(), heightSizeLayoutTypeAttr, heightAttr);
            configureSizeMetric(attrs, model.getWidth(), widthSizeLayoutTypeAttr, widthAttr);
        }
    }

    private static void configureSizeMetric(TypedArray attrs, SizeMetric model, int typeAttr, int valueAttr) {
        model.set(getIntFloatDimenValue(attrs, valueAttr, Float.valueOf(model.getValue())).floatValue(), getSizeLayoutType(attrs, typeAttr, (SizeMode) model.getLayoutType()));
    }

    private static SizeMode getSizeLayoutType(TypedArray attrs, int attr, SizeMode defaultValue) {
        return SizeMode.values()[attrs.getInt(attr, defaultValue.ordinal())];
    }

    public static void configureWidget(TypedArray attrs, Widget widget, int heightSizeLayoutTypeAttr, int heightAttr, int widthSizeLayoutTypeAttr, int widthAttr, int xLayoutStyleAttr, int xLayoutValueAttr, int yLayoutStyleAttr, int yLayoutValueAttr, int anchorPositionAttr, int visibilityAttr) {
        if (attrs != null) {
            configureSize(attrs, widget.getSize(), heightSizeLayoutTypeAttr, heightAttr, widthSizeLayoutTypeAttr, widthAttr);
            configurePositionMetrics(attrs, widget.getPositionMetrics(), xLayoutStyleAttr, xLayoutValueAttr, yLayoutStyleAttr, yLayoutValueAttr, anchorPositionAttr);
            widget.setVisible(attrs.getBoolean(visibilityAttr, widget.isVisible()));
        }
    }

    public static void configureWidgetRotation(TypedArray attrs, Widget widget, int rotationAttr) {
        if (attrs != null) {
            widget.setRotation(getWidgetRotation(attrs, rotationAttr, Widget.Rotation.NONE));
        }
    }

    public static void configurePositionMetrics(TypedArray attrs, PositionMetrics metrics, int xLayoutStyleAttr, int xLayoutValueAttr, int yLayoutStyleAttr, int yLayoutValueAttr, int anchorPositionAttr) {
        if (attrs != null && metrics != null) {
            metrics.getXPositionMetric().set(getIntFloatDimenValue(attrs, xLayoutValueAttr, Float.valueOf(metrics.getXPositionMetric().getValue())).floatValue(), getXLayoutStyle(attrs, xLayoutStyleAttr, (HorizontalPositioning) metrics.getXPositionMetric().getLayoutType()));
            metrics.getYPositionMetric().set(getIntFloatDimenValue(attrs, yLayoutValueAttr, Float.valueOf(metrics.getYPositionMetric().getValue())).floatValue(), getYLayoutStyle(attrs, yLayoutStyleAttr, (VerticalPositioning) metrics.getYPositionMetric().getLayoutType()));
            metrics.setAnchor(getAnchorPosition(attrs, anchorPositionAttr, metrics.getAnchor()));
        }
    }

    private static Number getIntFloatDimenValue(TypedArray attrs, int valueAttr, Number defaultValue) {
        Number result = defaultValue;
        if (attrs == null || !attrs.hasValue(valueAttr)) {
            return result;
        }
        int valueType = attrs.peekValue(valueAttr).type;
        if (valueType == 5) {
            return Float.valueOf(attrs.getDimension(valueAttr, defaultValue.floatValue()));
        }
        if (valueType == 16) {
            return Integer.valueOf(attrs.getInt(valueAttr, defaultValue.intValue()));
        }
        if (valueType == 4) {
            return Float.valueOf(attrs.getFloat(valueAttr, defaultValue.floatValue()));
        }
        throw new IllegalArgumentException("Invalid value type - must be int, float or dimension.");
    }

    private static HorizontalPositioning getXLayoutStyle(TypedArray attrs, int attr, HorizontalPositioning defaultValue) {
        return HorizontalPositioning.values()[attrs.getInt(attr, defaultValue.ordinal())];
    }

    private static VerticalPositioning getYLayoutStyle(TypedArray attrs, int attr, VerticalPositioning defaultValue) {
        return VerticalPositioning.values()[attrs.getInt(attr, defaultValue.ordinal())];
    }

    private static Widget.Rotation getWidgetRotation(TypedArray attrs, int attr, Widget.Rotation defaultValue) {
        return Widget.Rotation.values()[attrs.getInt(attr, defaultValue.ordinal())];
    }

    private static Anchor getAnchorPosition(TypedArray attrs, int attr, Anchor defaultValue) {
        return Anchor.values()[attrs.getInt(attr, defaultValue.ordinal())];
    }

    public static void configureStep(TypedArray attrs, StepModel model, int stepModeAttr, int stepValueAttr) {
        if (attrs != null) {
            model.setMode(StepMode.values()[attrs.getInt(stepModeAttr, model.getMode().ordinal())]);
            model.setValue(getIntFloatDimenValue(attrs, stepValueAttr, Double.valueOf(model.getValue())).doubleValue());
        }
    }
}
