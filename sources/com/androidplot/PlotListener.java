package com.androidplot;

import android.graphics.Canvas;

public interface PlotListener {
    void onAfterDraw(Plot plot, Canvas canvas);

    void onBeforeDraw(Plot plot, Canvas canvas);
}
