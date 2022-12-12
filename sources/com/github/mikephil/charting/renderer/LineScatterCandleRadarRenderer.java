package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Path;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class LineScatterCandleRadarRenderer extends DataRenderer {
    private Path mHighlightLinePath = new Path();

    public LineScatterCandleRadarRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    /* access modifiers changed from: protected */
    public void drawHighlightLines(Canvas c, float[] pts, ILineScatterCandleRadarDataSet set) {
        this.mHighlightPaint.setColor(set.getHighLightColor());
        this.mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());
        this.mHighlightPaint.setPathEffect(set.getDashPathEffectHighlight());
        if (set.isVerticalHighlightIndicatorEnabled()) {
            this.mHighlightLinePath.reset();
            this.mHighlightLinePath.moveTo(pts[0], this.mViewPortHandler.contentTop());
            this.mHighlightLinePath.lineTo(pts[0], this.mViewPortHandler.contentBottom());
            c.drawPath(this.mHighlightLinePath, this.mHighlightPaint);
        }
        if (set.isHorizontalHighlightIndicatorEnabled()) {
            this.mHighlightLinePath.reset();
            this.mHighlightLinePath.moveTo(this.mViewPortHandler.contentLeft(), pts[1]);
            this.mHighlightLinePath.lineTo(this.mViewPortHandler.contentRight(), pts[1]);
            c.drawPath(this.mHighlightLinePath, this.mHighlightPaint);
        }
    }
}
