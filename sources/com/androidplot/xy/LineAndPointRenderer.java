package com.androidplot.xy;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.androidplot.Plot;
import com.androidplot.PlotListener;
import com.androidplot.Region;
import com.androidplot.exception.PlotRenderException;
import com.androidplot.ui.RenderStack;
import com.androidplot.util.SeriesUtils;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.OrderedXYSeries;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LineAndPointRenderer<FormatterType extends LineAndPointFormatter> extends XYSeriesRenderer<XYSeries, FormatterType> {
    protected static final int ONE = 1;
    protected static final int ZERO = 0;
    private final Path path = new Path();
    protected final ConcurrentHashMap<XYSeries, ArrayList<PointF>> pointsCaches = new ConcurrentHashMap<>(2, 0.75f, 2);

    public LineAndPointRenderer(XYPlot plot) {
        super(plot);
        plot.addListener(new PlotListener() {
            public void onBeforeDraw(Plot source, Canvas canvas) {
                LineAndPointRenderer.this.cullPointsCache();
            }

            public void onAfterDraw(Plot source, Canvas canvas) {
            }
        });
    }

    public void onRender(Canvas canvas, RectF plotArea, XYSeries series, FormatterType formatter, RenderStack stack) throws PlotRenderException {
        drawSeries(canvas, plotArea, series, formatter);
    }

    public void doDrawLegendIcon(Canvas canvas, RectF rect, LineAndPointFormatter formatter) {
        float centerY = rect.centerY();
        float centerX = rect.centerX();
        if (formatter.getFillPaint() != null) {
            canvas.drawRect(rect, formatter.getFillPaint());
        }
        if (formatter.hasLinePaint()) {
            canvas.drawLine(rect.left, rect.bottom, rect.right, rect.top, formatter.getLinePaint());
        }
        if (formatter.hasVertexPaint()) {
            canvas.drawPoint(centerX, centerY, formatter.getVertexPaint());
        }
    }

    /* access modifiers changed from: protected */
    public void appendToPath(Path path2, PointF thisPoint, PointF lastPoint) {
        path2.lineTo(thisPoint.x, thisPoint.y);
    }

    /* access modifiers changed from: protected */
    public ArrayList<PointF> getPointsCache(XYSeries series) {
        ArrayList<PointF> pointsCache = this.pointsCaches.get(series);
        int seriesSize = series.size();
        if (pointsCache == null) {
            pointsCache = new ArrayList<>(seriesSize);
            this.pointsCaches.put(series, pointsCache);
        }
        if (pointsCache.size() < seriesSize) {
            while (pointsCache.size() < seriesSize) {
                pointsCache.add((Object) null);
            }
        } else if (pointsCache.size() > seriesSize) {
            while (pointsCache.size() > seriesSize) {
                pointsCache.remove(0);
            }
        }
        return pointsCache;
    }

    /* access modifiers changed from: protected */
    public void cullPointsCache() {
        for (XYSeries series : this.pointsCaches.keySet()) {
            if (!((XYSeriesRegistry) ((XYPlot) getPlot()).getRegistry()).contains(series, LineAndPointFormatter.class)) {
                this.pointsCaches.remove(series);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawSeries(Canvas canvas, RectF plotArea, XYSeries series, LineAndPointFormatter formatter) {
        PointF thisPoint;
        PointF lastPoint = null;
        PointF firstPoint = null;
        this.path.reset();
        ArrayList<PointF> pointsCache = getPointsCache(series);
        int iStart = 0;
        int iEnd = series.size();
        if (SeriesUtils.getXYOrder(series) == OrderedXYSeries.XOrder.ASCENDING) {
            Region iBounds = SeriesUtils.iBounds(series, ((XYPlot) getPlot()).getBounds());
            iStart = iBounds.getMin().intValue();
            if (iStart > 0) {
                iStart--;
            }
            iEnd = iBounds.getMax().intValue() + 1;
            if (iEnd < series.size() - 1) {
                iEnd++;
            }
        }
        for (int i = iStart; i < iEnd; i++) {
            Number y = series.getY(i);
            Number x = series.getX(i);
            PointF iPoint = pointsCache.get(i);
            if (y == null || x == null) {
                thisPoint = null;
                pointsCache.set(i, (Object) null);
            } else {
                if (iPoint == null) {
                    iPoint = new PointF();
                    pointsCache.set(i, iPoint);
                }
                thisPoint = iPoint;
                ((XYPlot) getPlot()).getBounds().transformScreen(thisPoint, x, y, plotArea);
            }
            if (formatter.hasLinePaint() && formatter.getInterpolationParams() == null) {
                if (thisPoint != null) {
                    if (firstPoint == null) {
                        this.path.reset();
                        firstPoint = thisPoint;
                        this.path.moveTo(firstPoint.x, firstPoint.y);
                    }
                    if (lastPoint != null) {
                        appendToPath(this.path, thisPoint, lastPoint);
                    }
                    lastPoint = thisPoint;
                } else {
                    if (lastPoint != null) {
                        renderPath(canvas, plotArea, this.path, firstPoint, lastPoint, formatter);
                    }
                    firstPoint = null;
                    lastPoint = null;
                }
            }
        }
        if (formatter.hasLinePaint()) {
            if (formatter.getInterpolationParams() != null) {
                List<XYCoords> interpolatedPoints = getInterpolator(formatter.getInterpolationParams()).interpolate(series, formatter.getInterpolationParams());
                firstPoint = convertPoint(interpolatedPoints.get(0), plotArea);
                lastPoint = convertPoint(interpolatedPoints.get(interpolatedPoints.size() - 1), plotArea);
                this.path.reset();
                this.path.moveTo(firstPoint.x, firstPoint.y);
                for (int i2 = 1; i2 < interpolatedPoints.size(); i2++) {
                    PointF thisPoint2 = convertPoint(interpolatedPoints.get(i2), plotArea);
                    this.path.lineTo(thisPoint2.x, thisPoint2.y);
                }
            }
            if (firstPoint != null) {
                renderPath(canvas, plotArea, this.path, firstPoint, lastPoint, formatter);
            }
        }
        renderPoints(canvas, plotArea, series, iStart, iEnd, pointsCache, formatter);
    }

    /* access modifiers changed from: protected */
    public Interpolator getInterpolator(InterpolationParams params) {
        try {
            return (Interpolator) params.getInterpolatorClass().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2);
        }
    }

    /* access modifiers changed from: protected */
    public PointF convertPoint(XYCoords coord, RectF plotArea) {
        return ((XYPlot) getPlot()).getBounds().transformScreen(coord, plotArea);
    }

    /* access modifiers changed from: protected */
    public void renderPoints(Canvas canvas, RectF plotArea, XYSeries series, int iStart, int iEnd, List<PointF> points, LineAndPointFormatter formatter) {
        if (formatter.hasVertexPaint() || formatter.hasPointLabelFormatter()) {
            Paint vertexPaint = formatter.hasVertexPaint() ? formatter.getVertexPaint() : null;
            boolean hasPointLabelFormatter = formatter.hasPointLabelFormatter();
            PointLabelFormatter plf = hasPointLabelFormatter ? formatter.getPointLabelFormatter() : null;
            PointLabeler pointLabeler = hasPointLabelFormatter ? formatter.getPointLabeler() : null;
            for (int i = iStart; i < iEnd; i++) {
                PointF p = points.get(i);
                if (p != null) {
                    if (vertexPaint != null) {
                        canvas.drawPoint(p.x, p.y, vertexPaint);
                    }
                    if (pointLabeler != null) {
                        canvas.drawText(pointLabeler.getLabel(series, i), p.x + plf.hOffset, p.y + plf.vOffset, plf.getTextPaint());
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void renderPath(Canvas canvas, RectF plotArea, Path path2, PointF firstPoint, PointF lastPoint, LineAndPointFormatter formatter) {
        RectF thisRegionRectF;
        Path outlinePath = new Path(path2);
        switch (formatter.getFillDirection()) {
            case BOTTOM:
                path2.lineTo(lastPoint.x, plotArea.bottom);
                path2.lineTo(firstPoint.x, plotArea.bottom);
                path2.close();
                break;
            case TOP:
                path2.lineTo(lastPoint.x, plotArea.top);
                path2.lineTo(firstPoint.x, plotArea.top);
                path2.close();
                break;
            case RANGE_ORIGIN:
                float originPix = (float) ((XYPlot) getPlot()).getBounds().getxRegion().transform(((XYPlot) getPlot()).getRangeOrigin().doubleValue(), (double) plotArea.top, (double) plotArea.bottom, true);
                path2.lineTo(lastPoint.x, originPix);
                path2.lineTo(firstPoint.x, originPix);
                path2.close();
                break;
            default:
                throw new UnsupportedOperationException("Fill direction not yet implemented: " + formatter.getFillDirection());
        }
        if (formatter.getFillPaint() != null) {
            canvas.drawPath(path2, formatter.getFillPaint());
        }
        RectRegion bounds = ((XYPlot) getPlot()).getBounds();
        RectRegion plotRegion = new RectRegion(plotArea);
        for (RectRegion thisRegion : bounds.intersects(formatter.getRegions().elements())) {
            XYRegionFormatter regionFormatter = formatter.getRegionFormatter(thisRegion);
            RectRegion thisRegionTransformed = bounds.transform(thisRegion, plotRegion, false, true);
            thisRegionTransformed.intersect(plotRegion);
            if (thisRegion.isFullyDefined() && (thisRegionRectF = thisRegionTransformed.asRectF()) != null) {
                try {
                    canvas.save();
                    canvas.clipPath(path2);
                    canvas.drawRect(thisRegionRectF, regionFormatter.getPaint());
                } finally {
                    canvas.restore();
                }
            }
        }
        if (formatter.hasLinePaint()) {
            canvas.drawPath(outlinePath, formatter.getLinePaint());
        }
        path2.rewind();
    }
}
