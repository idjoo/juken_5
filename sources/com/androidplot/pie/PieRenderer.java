package com.androidplot.pie;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import com.androidplot.exception.PlotRenderException;
import com.androidplot.ui.RenderStack;
import com.androidplot.ui.SeriesBundle;
import com.androidplot.ui.SeriesRenderer;
import java.util.List;

public class PieRenderer extends SeriesRenderer<PieChart, Segment, SegmentFormatter> {
    private static final float FULL_PIE_DEGS = 360.0f;
    private static final float HALF_PIE_DEGS = 180.0f;
    private DonutMode donutMode = DonutMode.PERCENT;
    private float donutSize = 0.5f;
    private float extentDegs = FULL_PIE_DEGS;
    private float startDegs = 0.0f;

    public enum DonutMode {
        PERCENT,
        PIXELS
    }

    public PieRenderer(PieChart plot) {
        super(plot);
    }

    public float getRadius(RectF rect) {
        return rect.width() < rect.height() ? rect.width() / 2.0f : rect.height() / 2.0f;
    }

    public void onRender(Canvas canvas, RectF plotArea, Segment series, SegmentFormatter formatter, RenderStack stack) throws PlotRenderException {
        stack.disable(getClass());
        float radius = getRadius(plotArea);
        PointF origin = new PointF(plotArea.centerX(), plotArea.centerY());
        double[] values = getValues();
        double scale = calculateScale(values);
        float offset = degsToScreenDegs(this.startDegs);
        RectF rec = new RectF(origin.x - radius, origin.y - radius, origin.x + radius, origin.y + radius);
        int i = 0;
        for (SeriesBundle<Segment, ? extends SegmentFormatter> sfPair : getSeriesAndFormatterList()) {
            float lastOffset = offset;
            float sweep = (float) (values[i] * scale * ((double) this.extentDegs));
            offset += sweep;
            drawSegment(canvas, rec, sfPair.getSeries(), (SegmentFormatter) sfPair.getFormatter(), radius, lastOffset, sweep);
            i++;
        }
    }

    /* access modifiers changed from: protected */
    public void drawSegment(Canvas canvas, RectF bounds, Segment seg, SegmentFormatter f, float rad, float startAngle, float sweep) {
        float donutSizePx;
        float innerRad;
        canvas.save();
        float startAngle2 = startAngle + f.getRadialInset();
        float sweep2 = sweep - (f.getRadialInset() * 2.0f);
        float halfSweepEndAngle = startAngle2 + (sweep2 / 2.0f);
        PointF translated = calculateLineEnd(bounds.centerX(), bounds.centerY(), f.getOffset(), halfSweepEndAngle);
        float cx = translated.x;
        float cy = translated.y;
        switch (this.donutMode) {
            case PERCENT:
                donutSizePx = this.donutSize * rad;
                break;
            case PIXELS:
                if (this.donutSize <= 0.0f) {
                    donutSizePx = rad + this.donutSize;
                    break;
                } else {
                    donutSizePx = this.donutSize;
                    break;
                }
            default:
                throw new UnsupportedOperationException("Unsupported DonutMde: " + this.donutMode);
        }
        float outerRad = rad - f.getOuterInset();
        if (donutSizePx == 0.0f) {
            innerRad = 0.0f;
        } else {
            innerRad = donutSizePx + f.getInnerInset();
        }
        if (Math.abs(sweep2 - this.extentDegs) > Float.MIN_VALUE) {
            PointF r1Outer = calculateLineEnd(cx, cy, outerRad, startAngle2);
            PointF r1Inner = calculateLineEnd(cx, cy, innerRad, startAngle2);
            PointF r2Outer = calculateLineEnd(cx, cy, outerRad, startAngle2 + sweep2);
            PointF r2Inner = calculateLineEnd(cx, cy, innerRad, startAngle2 + sweep2);
            Path clip = new Path();
            clip.arcTo(new RectF(bounds.left - outerRad, bounds.top - outerRad, bounds.right + outerRad, bounds.bottom + outerRad), startAngle2, sweep2);
            clip.lineTo(cx, cy);
            clip.close();
            canvas.clipPath(clip);
            Path p = new Path();
            p.arcTo(new RectF(cx - outerRad, cy - outerRad, cx + outerRad, cy + outerRad), startAngle2, sweep2);
            p.lineTo(r2Inner.x, r2Inner.y);
            Path path = p;
            path.arcTo(new RectF(cx - innerRad, cy - innerRad, cx + innerRad, cy + innerRad), startAngle2 + sweep2, -sweep2);
            p.close();
            canvas.drawPath(p, f.getFillPaint());
            canvas.drawLine(r1Inner.x, r1Inner.y, r1Outer.x, r1Outer.y, f.getRadialEdgePaint());
            canvas.drawLine(r2Inner.x, r2Inner.y, r2Outer.x, r2Outer.y, f.getRadialEdgePaint());
        } else {
            canvas.save();
            Path chart = new Path();
            chart.addCircle(cx, cy, outerRad, Path.Direction.CW);
            Path inside = new Path();
            inside.addCircle(cx, cy, innerRad, Path.Direction.CW);
            canvas.clipPath(inside, Region.Op.DIFFERENCE);
            canvas.drawPath(chart, f.getFillPaint());
            canvas.restore();
        }
        canvas.drawCircle(cx, cy, innerRad, f.getInnerEdgePaint());
        canvas.drawCircle(cx, cy, outerRad, f.getOuterEdgePaint());
        canvas.restore();
        PointF labelOrigin = calculateLineEnd(cx, cy, outerRad - ((outerRad - innerRad) / 2.0f), halfSweepEndAngle);
        if (f.getLabelPaint() != null) {
            drawSegmentLabel(canvas, labelOrigin, seg, f);
        }
    }

    /* access modifiers changed from: protected */
    public void drawSegmentLabel(Canvas canvas, PointF origin, Segment seg, SegmentFormatter f) {
        canvas.drawText(seg.getTitle(), origin.x, origin.y, f.getLabelPaint());
    }

    /* access modifiers changed from: protected */
    public void doDrawLegendIcon(Canvas canvas, RectF rect, SegmentFormatter formatter) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /* access modifiers changed from: protected */
    public double calculateScale(double[] values) {
        double total = 0.0d;
        for (double d : values) {
            total += d;
        }
        return 1.0d / total;
    }

    /* access modifiers changed from: protected */
    public double[] getValues() {
        List<SeriesBundle<Segment, ? extends SegmentFormatter>> seriesList = getSeriesAndFormatterList();
        double[] result = new double[seriesList.size()];
        int i = 0;
        for (SeriesBundle<Segment, ? extends SegmentFormatter> sfPair : seriesList) {
            result[i] = sfPair.getSeries().getValue().doubleValue();
            i++;
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public PointF calculateLineEnd(float x, float y, float rad, float deg) {
        return calculateLineEnd(new PointF(x, y), rad, deg);
    }

    /* access modifiers changed from: protected */
    public PointF calculateLineEnd(PointF origin, float rad, float deg) {
        double radians = (((double) deg) * 3.141592653589793d) / 180.0d;
        return new PointF(origin.x + ((float) (((double) rad) * Math.cos(radians))), origin.y + ((float) (((double) rad) * Math.sin(radians))));
    }

    public void setDonutSize(float size, DonutMode mode) {
        switch (mode) {
            case PERCENT:
                if (size < 0.0f || size > 1.0f) {
                    throw new IllegalArgumentException("Size parameter must be between 0 and 1 when operating in PERCENT mode.");
                }
            case PIXELS:
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented.");
        }
        this.donutMode = mode;
        this.donutSize = size;
    }

    public Segment getContainingSegment(PointF point) {
        RectF plotArea = ((PieChart) getPlot()).getPie().getWidgetDimensions().marginatedRect;
        PointF origin = new PointF(plotArea.centerX(), plotArea.centerY());
        double angle = Math.atan2((double) (point.y - origin.y), (double) (point.x - origin.x)) * 57.29577951308232d;
        if (angle < 0.0d) {
            angle += 360.0d;
        }
        List<SeriesBundle<Segment, ? extends SegmentFormatter>> seriesList = getSeriesAndFormatterList();
        int i = 0;
        double[] values = getValues();
        double scale = calculateScale(values);
        float offset = degsToScreenDegs(this.startDegs);
        for (SeriesBundle<Segment, ? extends SegmentFormatter> sfPair : seriesList) {
            float lastOffset = offset;
            offset = (offset + ((float) ((values[i] * scale) * ((double) this.extentDegs)))) % FULL_PIE_DEGS;
            double dist = signedDistance((double) offset, angle);
            double endDist = signedDistance((double) offset, (double) lastOffset);
            if (endDist < 0.0d) {
                endDist += 360.0d;
            }
            if (dist > 0.0d && dist <= endDist) {
                return sfPair.getSeries();
            }
            i++;
        }
        return null;
    }

    protected static float degsToScreenDegs(float degs) {
        float degs2 = degs % FULL_PIE_DEGS;
        if (degs2 > 0.0f) {
            return FULL_PIE_DEGS - degs2;
        }
        return degs2;
    }

    protected static double signedDistance(double angle1, double angle2) {
        double r;
        double d = Math.abs(angle1 - angle2) % 360.0d;
        if (d > 180.0d) {
            r = 360.0d - d;
        } else {
            r = d;
        }
        return r * ((double) (((angle1 - angle2 < 0.0d || angle1 - angle2 > 180.0d) && (angle1 - angle2 > -180.0d || angle1 - angle2 < -360.0d)) ? -1 : 1));
    }

    protected static void validateInputDegs(float degs) {
        if (degs < 0.0f || degs > FULL_PIE_DEGS) {
            throw new IllegalArgumentException("Degrees values must be between 0.0 and 360.");
        }
    }

    public void setStartDegs(float degs) {
        validateInputDegs(degs);
        this.startDegs = degs;
    }

    public float getStartDegs() {
        return this.startDegs;
    }

    public void setExtentDegs(float degs) {
        validateInputDegs(degs);
        this.extentDegs = degs;
    }

    public float getExtentDegs() {
        return this.extentDegs;
    }
}
