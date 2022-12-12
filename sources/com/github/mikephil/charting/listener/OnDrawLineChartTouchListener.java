package com.github.mikephil.charting.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnDrawLineChartTouchListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
