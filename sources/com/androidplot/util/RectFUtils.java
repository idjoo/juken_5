package com.androidplot.util;

import android.graphics.RectF;
import com.androidplot.ui.Insets;

public abstract class RectFUtils {
    public static boolean areIdentical(RectF r1, RectF r2) {
        return r1.left == r2.left && r1.top == r2.top && r1.right == r2.right && r1.bottom == r2.bottom;
    }

    public static RectF applyInsets(RectF rect, Insets insets) {
        if (insets != null) {
            return new RectF(rect.left + insets.getLeft(), rect.top + insets.getTop(), rect.right - insets.getRight(), rect.bottom - insets.getBottom());
        }
        return rect;
    }

    public static RectF createFromEdges(float w1, float h1, float w2, float h2) {
        boolean w1IsLeft;
        float f;
        boolean h1IsTop = true;
        if (w1 <= w2) {
            w1IsLeft = true;
        } else {
            w1IsLeft = false;
        }
        if (h1 > h2) {
            h1IsTop = false;
        }
        if (w1IsLeft) {
            f = w1;
        } else {
            f = w2;
        }
        float f2 = h1IsTop ? h1 : h2;
        if (!w1IsLeft) {
            w2 = w1;
        }
        if (!h1IsTop) {
            h2 = h1;
        }
        return new RectF(f, f2, w2, h2);
    }
}
