package com.androidplot.util;

import android.os.Build;
import android.os.Trace;

public abstract class APTrace {
    public static void begin(String sectionName) {
        if (Build.VERSION.SDK_INT >= 18) {
            Trace.beginSection(sectionName);
        }
    }

    public static void end() {
        if (Build.VERSION.SDK_INT >= 18) {
            Trace.endSection();
        }
    }
}
