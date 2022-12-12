package com.androidplot.xy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.internal.view.SupportMenu;
import com.androidplot.exception.PlotRenderException;
import com.androidplot.ui.RenderStack;
import com.androidplot.ui.SeriesRenderer;

public class AdvancedLineAndPointRenderer extends XYSeriesRenderer<XYSeries, Formatter> {
    private int latestIndex;

    public AdvancedLineAndPointRenderer(XYPlot plot) {
        super(plot);
    }

    /* access modifiers changed from: protected */
    public void onRender(Canvas canvas, RectF plotArea, XYSeries series, Formatter formatter, RenderStack stack) throws PlotRenderException {
        PointF thisPoint;
        PointF lastPoint = null;
        for (int i = 0; i < series.size(); i++) {
            Number y = series.getY(i);
            Number x = series.getX(i);
            if (y == null || x == null) {
                thisPoint = null;
            } else {
                thisPoint = ((XYPlot) getPlot()).getBounds().transformScreen(x, y, plotArea);
            }
            if (!(formatter.getLinePaint() == null || thisPoint == null || lastPoint == null)) {
                canvas.drawLine(lastPoint.x, lastPoint.y, thisPoint.x, thisPoint.y, formatter.getLinePaint(i, this.latestIndex, series.size()));
            }
            lastPoint = thisPoint;
        }
    }

    /* access modifiers changed from: protected */
    public void doDrawLegendIcon(Canvas canvas, RectF rect, Formatter formatter) {
        if (formatter.getLinePaint() != null) {
            canvas.drawLine(rect.left, rect.bottom, rect.right, rect.top, formatter.getLinePaint());
        }
    }

    public void setLatestIndex(int latestIndex2) {
        this.latestIndex = latestIndex2;
    }

    public static class Formatter extends XYSeriesFormatter<XYRegionFormatter> {
        private static final int DEFAULT_STROKE_WIDTH = 3;
        private Paint linePaint;

        public Formatter() {
            this.linePaint = new Paint();
            this.linePaint.setStrokeWidth(3.0f);
            this.linePaint.setColor(SupportMenu.CATEGORY_MASK);
        }

        public Formatter(Context context, int xmlConfigId) {
            this();
            configure(context, xmlConfigId);
        }

        public Class<? extends SeriesRenderer> getRendererClass() {
            return AdvancedLineAndPointRenderer.class;
        }

        public AdvancedLineAndPointRenderer doGetRendererInstance(XYPlot plot) {
            return new AdvancedLineAndPointRenderer(plot);
        }

        public Paint getLinePaint() {
            return this.linePaint;
        }

        public Paint getLinePaint(int thisIndex, int latestIndex, int seriesSize) {
            return getLinePaint();
        }

        public void setLinePaint(Paint linePaint2) {
            this.linePaint = linePaint2;
        }
    }
}
