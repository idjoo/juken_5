package com.github.mikephil.charting.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.List;

public abstract class Utils {
    public static final double DEG2RAD = 0.017453292519943295d;
    public static final float FDEG2RAD = 0.017453292f;
    private static final int[] POW_10 = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
    private static Rect mDrawTextRectBuffer = new Rect();
    private static Paint.FontMetrics mFontMetricsBuffer = new Paint.FontMetrics();
    private static int mMaximumFlingVelocity = 8000;
    private static DisplayMetrics mMetrics;
    private static int mMinimumFlingVelocity = 50;

    public static void init(Context context) {
        if (context == null) {
            mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
            mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
            Log.e("MPChartLib-Utils", "Utils.init(...) PROVIDED CONTEXT OBJECT IS NULL");
            return;
        }
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mMetrics = context.getResources().getDisplayMetrics();
    }

    @Deprecated
    public static void init(Resources res) {
        mMetrics = res.getDisplayMetrics();
        mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
        mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
    }

    public static float convertDpToPixel(float dp) {
        if (mMetrics != null) {
            return dp * (((float) mMetrics.densityDpi) / 160.0f);
        }
        Log.e("MPChartLib-Utils", "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before calling Utils.convertDpToPixel(...). Otherwise conversion does not take place.");
        return dp;
    }

    public static float convertPixelsToDp(float px) {
        if (mMetrics != null) {
            return px / (((float) mMetrics.densityDpi) / 160.0f);
        }
        Log.e("MPChartLib-Utils", "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before calling Utils.convertPixelsToDp(...). Otherwise conversion does not take place.");
        return px;
    }

    public static int calcTextWidth(Paint paint, String demoText) {
        return (int) paint.measureText(demoText);
    }

    public static int calcTextHeight(Paint paint, String demoText) {
        Rect r = new Rect();
        paint.getTextBounds(demoText, 0, demoText.length(), r);
        return r.height();
    }

    public static float getLineHeight(Paint paint) {
        Paint.FontMetrics metrics = paint.getFontMetrics();
        return metrics.descent - metrics.ascent;
    }

    public static float getLineSpacing(Paint paint) {
        Paint.FontMetrics metrics = paint.getFontMetrics();
        return (metrics.ascent - metrics.top) + metrics.bottom;
    }

    public static FSize calcTextSize(Paint paint, String demoText) {
        Rect r = new Rect();
        paint.getTextBounds(demoText, 0, demoText.length(), r);
        return new FSize((float) r.width(), (float) r.height());
    }

    public static String formatNumber(float number, int digitCount, boolean separateThousands) {
        return formatNumber(number, digitCount, separateThousands, '.');
    }

    public static String formatNumber(float number, int digitCount, boolean separateThousands, char separateChar) {
        int ind;
        char[] out = new char[35];
        boolean neg = false;
        if (number == 0.0f) {
            return "0";
        }
        boolean zero = false;
        if (number < 1.0f && number > -1.0f) {
            zero = true;
        }
        if (number < 0.0f) {
            neg = true;
            number = -number;
        }
        if (digitCount > POW_10.length) {
            digitCount = POW_10.length - 1;
        }
        long lval = (long) Math.round(number * ((float) POW_10[digitCount]));
        int ind2 = out.length - 1;
        int charCount = 0;
        boolean decimalPointAdded = false;
        while (true) {
            ind = ind2;
            if (lval == 0 && charCount >= digitCount + 1) {
                break;
            }
            lval /= 10;
            ind2 = ind - 1;
            out[ind] = (char) (((int) (lval % 10)) + 48);
            charCount++;
            if (charCount == digitCount) {
                out[ind2] = ',';
                charCount++;
                decimalPointAdded = true;
                ind2--;
            } else if (separateThousands && lval != 0 && charCount > digitCount) {
                if (decimalPointAdded) {
                    if ((charCount - digitCount) % 4 == 0) {
                        out[ind2] = separateChar;
                        charCount++;
                        ind2--;
                    }
                } else if ((charCount - digitCount) % 4 == 3) {
                    out[ind2] = separateChar;
                    charCount++;
                    ind2--;
                }
            }
        }
        if (zero) {
            out[ind] = '0';
            charCount++;
            ind--;
        }
        if (neg) {
            int i = ind - 1;
            out[ind] = '-';
            charCount++;
        }
        int start = out.length - charCount;
        return String.valueOf(out, start, out.length - start);
    }

    public static float roundToNextSignificant(double number) {
        double d;
        if (number < 0.0d) {
            d = -number;
        } else {
            d = number;
        }
        float magnitude = (float) Math.pow(10.0d, (double) (1 - ((int) ((float) Math.ceil((double) ((float) Math.log10(d)))))));
        return ((float) Math.round(((double) magnitude) * number)) / magnitude;
    }

    public static int getDecimals(float number) {
        return ((int) Math.ceil(-Math.log10((double) roundToNextSignificant((double) number)))) + 2;
    }

    public static int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }

    public static String[] convertStrings(List<String> strings) {
        String[] ret = new String[strings.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = strings.get(i);
        }
        return ret;
    }

    public static double nextUp(double d) {
        if (d == Double.POSITIVE_INFINITY) {
            return d;
        }
        double d2 = d + 0.0d;
        return Double.longBitsToDouble((d2 >= 0.0d ? 1 : -1) + Double.doubleToRawLongBits(d2));
    }

    public static int getClosestDataSetIndex(List<SelectionDetail> valsAtIndex, float val, YAxis.AxisDependency axis) {
        int index = -2147483647;
        float distance = Float.MAX_VALUE;
        for (int i = 0; i < valsAtIndex.size(); i++) {
            SelectionDetail sel = valsAtIndex.get(i);
            if (axis == null || sel.dataSet.getAxisDependency() == axis) {
                float cdistance = Math.abs(sel.val - val);
                if (cdistance < distance) {
                    index = valsAtIndex.get(i).dataSetIndex;
                    distance = cdistance;
                }
            }
        }
        return index;
    }

    public static float getMinimumDistance(List<SelectionDetail> valsAtIndex, float val, YAxis.AxisDependency axis) {
        float distance = Float.MAX_VALUE;
        for (int i = 0; i < valsAtIndex.size(); i++) {
            SelectionDetail sel = valsAtIndex.get(i);
            if (sel.dataSet.getAxisDependency() == axis) {
                float cdistance = Math.abs(sel.val - val);
                if (cdistance < distance) {
                    distance = cdistance;
                }
            }
        }
        return distance;
    }

    public static boolean needsDefaultFormatter(ValueFormatter formatter) {
        if (formatter != null && !(formatter instanceof DefaultValueFormatter)) {
            return false;
        }
        return true;
    }

    public static PointF getPosition(PointF center, float dist, float angle) {
        return new PointF((float) (((double) center.x) + (((double) dist) * Math.cos(Math.toRadians((double) angle)))), (float) (((double) center.y) + (((double) dist) * Math.sin(Math.toRadians((double) angle)))));
    }

    public static void velocityTrackerPointerUpCleanUpIfNecessary(MotionEvent ev, VelocityTracker tracker) {
        tracker.computeCurrentVelocity(1000, (float) mMaximumFlingVelocity);
        int upIndex = ev.getActionIndex();
        int id1 = ev.getPointerId(upIndex);
        float x1 = tracker.getXVelocity(id1);
        float y1 = tracker.getYVelocity(id1);
        int count = ev.getPointerCount();
        for (int i = 0; i < count; i++) {
            if (i != upIndex) {
                int id2 = ev.getPointerId(i);
                if ((x1 * tracker.getXVelocity(id2)) + (y1 * tracker.getYVelocity(id2)) < 0.0f) {
                    tracker.clear();
                    return;
                }
            }
        }
    }

    @SuppressLint({"NewApi"})
    public static void postInvalidateOnAnimation(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.postInvalidateOnAnimation();
        } else {
            view.postInvalidateDelayed(10);
        }
    }

    public static int getMinimumFlingVelocity() {
        return mMinimumFlingVelocity;
    }

    public static int getMaximumFlingVelocity() {
        return mMaximumFlingVelocity;
    }

    public static float getNormalizedAngle(float angle) {
        while (angle < 0.0f) {
            angle += 360.0f;
        }
        return angle % 360.0f;
    }

    public static void drawText(Canvas c, String text, float x, float y, Paint paint, PointF anchor, float angleDegrees) {
        paint.getTextBounds(text, 0, text.length(), mDrawTextRectBuffer);
        float lineHeight = (float) mDrawTextRectBuffer.height();
        float drawOffsetX = 0.0f - ((float) mDrawTextRectBuffer.left);
        float drawOffsetY = 0.0f + lineHeight;
        Paint.Align originalTextAlign = paint.getTextAlign();
        paint.setTextAlign(Paint.Align.LEFT);
        if (angleDegrees != 0.0f) {
            float drawOffsetX2 = drawOffsetX - (((float) mDrawTextRectBuffer.width()) * 0.5f);
            float drawOffsetY2 = drawOffsetY - (0.5f * lineHeight);
            float translateX = x;
            float translateY = y;
            if (!(anchor.x == 0.5f && anchor.y == 0.5f)) {
                FSize rotatedSize = getSizeOfRotatedRectangleByDegrees((float) mDrawTextRectBuffer.width(), lineHeight, angleDegrees);
                translateX -= rotatedSize.width * (anchor.x - 0.5f);
                translateY -= rotatedSize.height * (anchor.y - 0.5f);
            }
            c.save();
            c.translate(translateX, translateY);
            c.rotate(angleDegrees);
            c.drawText(text, drawOffsetX2, drawOffsetY2, paint);
            c.restore();
        } else {
            if (!(anchor.x == 0.0f && anchor.y == 0.0f)) {
                drawOffsetX -= ((float) mDrawTextRectBuffer.width()) * anchor.x;
                drawOffsetY -= anchor.y * lineHeight;
            }
            c.drawText(text, drawOffsetX + x, drawOffsetY + y, paint);
        }
        paint.setTextAlign(originalTextAlign);
    }

    public static void drawMultilineText(Canvas c, StaticLayout textLayout, float x, float y, TextPaint paint, PointF anchor, float angleDegrees) {
        float lineHeight = paint.getFontMetrics(mFontMetricsBuffer);
        float drawWidth = (float) textLayout.getWidth();
        float drawHeight = ((float) textLayout.getLineCount()) * lineHeight;
        float drawOffsetX = 0.0f - ((float) mDrawTextRectBuffer.left);
        float drawOffsetY = 0.0f + drawHeight;
        Paint.Align originalTextAlign = paint.getTextAlign();
        paint.setTextAlign(Paint.Align.LEFT);
        if (angleDegrees != 0.0f) {
            float drawOffsetX2 = drawOffsetX - (0.5f * drawWidth);
            float drawOffsetY2 = drawOffsetY - (0.5f * drawHeight);
            float translateX = x;
            float translateY = y;
            if (!(anchor.x == 0.5f && anchor.y == 0.5f)) {
                FSize rotatedSize = getSizeOfRotatedRectangleByDegrees(drawWidth, drawHeight, angleDegrees);
                translateX -= rotatedSize.width * (anchor.x - 0.5f);
                translateY -= rotatedSize.height * (anchor.y - 0.5f);
            }
            c.save();
            c.translate(translateX, translateY);
            c.rotate(angleDegrees);
            c.translate(drawOffsetX2, drawOffsetY2);
            textLayout.draw(c);
            c.restore();
        } else {
            if (!(anchor.x == 0.0f && anchor.y == 0.0f)) {
                drawOffsetX -= anchor.x * drawWidth;
                drawOffsetY -= anchor.y * drawHeight;
            }
            c.save();
            c.translate(drawOffsetX + x, drawOffsetY + y);
            textLayout.draw(c);
            c.restore();
        }
        paint.setTextAlign(originalTextAlign);
    }

    public static void drawMultilineText(Canvas c, String text, float x, float y, TextPaint paint, FSize constrainedToSize, PointF anchor, float angleDegrees) {
        drawMultilineText(c, new StaticLayout(text, 0, text.length(), paint, (int) Math.max(Math.ceil((double) constrainedToSize.width), 1.0d), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false), x, y, paint, anchor, angleDegrees);
    }

    public static FSize getSizeOfRotatedRectangleByDegrees(FSize rectangleSize, float degrees) {
        return getSizeOfRotatedRectangleByRadians(rectangleSize.width, rectangleSize.height, degrees * 0.017453292f);
    }

    public static FSize getSizeOfRotatedRectangleByRadians(FSize rectangleSize, float radians) {
        return getSizeOfRotatedRectangleByRadians(rectangleSize.width, rectangleSize.height, radians);
    }

    public static FSize getSizeOfRotatedRectangleByDegrees(float rectangleWidth, float rectangleHeight, float degrees) {
        return getSizeOfRotatedRectangleByRadians(rectangleWidth, rectangleHeight, degrees * 0.017453292f);
    }

    public static FSize getSizeOfRotatedRectangleByRadians(float rectangleWidth, float rectangleHeight, float radians) {
        return new FSize(Math.abs(((float) Math.cos((double) radians)) * rectangleWidth) + Math.abs(((float) Math.sin((double) radians)) * rectangleHeight), Math.abs(((float) Math.sin((double) radians)) * rectangleWidth) + Math.abs(((float) Math.cos((double) radians)) * rectangleHeight));
    }

    public static int getSDKInt() {
        return Build.VERSION.SDK_INT;
    }
}
