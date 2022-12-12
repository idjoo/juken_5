package com.androidplot.xy;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import com.androidplot.exception.PlotRenderException;
import com.androidplot.ui.RenderStack;
import com.androidplot.ui.SeriesRenderer;
import java.util.ArrayList;
import java.util.List;

public class FastLineAndPointRenderer extends XYSeriesRenderer<XYSeries, Formatter> {
    private static final int MINIMUM_NUMBER_OF_POINTS_TO_DEFINE_A_LINE = 4;
    private float[] points;
    List<Integer> segmentLengths = new ArrayList();
    List<Integer> segmentOffsets = new ArrayList();

    public FastLineAndPointRenderer(XYPlot plot) {
        super(plot);
    }

    /* access modifiers changed from: protected */
    public void onRender(Canvas canvas, RectF plotArea, XYSeries series, Formatter formatter, RenderStack stack) throws PlotRenderException {
        this.segmentOffsets.clear();
        this.segmentLengths.clear();
        int numPoints = series.size() * 2;
        if (this.points == null || this.points.length != numPoints) {
            this.points = new float[(series.size() * 2)];
        }
        int segmentLen = 0;
        boolean isLastPointNull = true;
        PointF resultPoint = new PointF();
        int i = 0;
        int j = 0;
        while (i < series.size()) {
            Number y = series.getY(i);
            Number x = series.getX(i);
            if (y != null && x != null) {
                if (isLastPointNull) {
                    this.segmentOffsets.add(Integer.valueOf(j));
                    segmentLen = 0;
                    isLastPointNull = false;
                }
                ((XYPlot) getPlot()).getBounds().transformScreen(resultPoint, x, y, plotArea);
                this.points[j] = resultPoint.x;
                this.points[j + 1] = resultPoint.y;
                segmentLen += 2;
                if (i == series.size() - 1) {
                    this.segmentLengths.add(Integer.valueOf(segmentLen));
                }
            } else if (!isLastPointNull) {
                this.segmentLengths.add(Integer.valueOf(segmentLen));
                isLastPointNull = true;
            }
            i++;
            j += 2;
        }
        if (formatter.linePaint != null || formatter.vertexPaint != null) {
            for (int i2 = 0; i2 < this.segmentOffsets.size(); i2++) {
                int len = this.segmentLengths.get(i2).intValue();
                Canvas canvas2 = canvas;
                drawSegment(canvas2, this.points, this.segmentOffsets.get(i2).intValue(), len, formatter);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawSegment(@NonNull Canvas canvas, @NonNull float[] points2, int offset, int len, Formatter formatter) {
        if (formatter.linePaint != null && len >= 4) {
            if ((len & 2) != 0) {
                canvas.drawLines(points2, offset, len - 2, formatter.linePaint);
                canvas.drawLines(points2, offset + 2, len - 2, formatter.linePaint);
            } else {
                canvas.drawLines(points2, offset, len, formatter.linePaint);
                canvas.drawLines(points2, offset + 2, len - 4, formatter.linePaint);
            }
        }
        if (formatter.vertexPaint != null) {
            canvas.drawPoints(points2, offset, len, formatter.vertexPaint);
        }
    }

    /* access modifiers changed from: protected */
    public void doDrawLegendIcon(@NonNull Canvas canvas, @NonNull RectF rect, @NonNull Formatter formatter) {
        if (formatter.hasLinePaint()) {
            canvas.drawLine(rect.left, rect.bottom, rect.right, rect.top, formatter.getLinePaint());
        }
        if (formatter.hasVertexPaint()) {
            canvas.drawPoint(rect.centerX(), rect.centerY(), formatter.getVertexPaint());
        }
    }

    public static class Formatter extends LineAndPointFormatter {
        public Formatter(Integer lineColor, Integer vertexColor, PointLabelFormatter plf) {
            super(lineColor, vertexColor, (Integer) null, plf);
        }

        /* access modifiers changed from: protected */
        public void initLinePaint(Integer lineColor) {
            super.initLinePaint(lineColor);
            getLinePaint().setAntiAlias(false);
        }

        public Class<? extends SeriesRenderer> getRendererClass() {
            return FastLineAndPointRenderer.class;
        }

        public SeriesRenderer doGetRendererInstance(XYPlot plot) {
            return new FastLineAndPointRenderer(plot);
        }
    }
}
