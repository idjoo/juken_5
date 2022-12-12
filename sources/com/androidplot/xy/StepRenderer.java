package com.androidplot.xy;

import android.graphics.Path;
import android.graphics.PointF;

public class StepRenderer extends LineAndPointRenderer<StepFormatter> {
    public StepRenderer(XYPlot plot) {
        super(plot);
    }

    /* access modifiers changed from: protected */
    public void appendToPath(Path path, PointF thisPoint, PointF lastPoint) {
        path.lineTo(thisPoint.x, lastPoint.y);
        path.lineTo(thisPoint.x, thisPoint.y);
    }
}
