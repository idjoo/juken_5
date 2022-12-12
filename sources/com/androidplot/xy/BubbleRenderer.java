package com.androidplot.xy;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import com.androidplot.Region;
import com.androidplot.exception.PlotRenderException;
import com.androidplot.ui.RenderStack;
import com.androidplot.ui.SeriesBundle;
import com.androidplot.util.FontUtils;
import com.androidplot.util.PixelUtils;
import com.androidplot.util.SeriesUtils;
import com.androidplot.xy.BubbleFormatter;
import java.util.List;

public class BubbleRenderer<FormatterType extends BubbleFormatter> extends XYSeriesRenderer<BubbleSeries, FormatterType> {
    protected static final float MAX_BUBBLE_RADIUS_DEFAULT_DP = 25.0f;
    protected static final float MIN_BUBBLE_RADIUS_DEFAULT_DP = 9.0f;
    private Region bubbleBounds = new Region(Float.valueOf(PixelUtils.dpToPix(MIN_BUBBLE_RADIUS_DEFAULT_DP)), Float.valueOf(PixelUtils.dpToPix(MAX_BUBBLE_RADIUS_DEFAULT_DP)));
    private BubbleScaleMode bubbleScaleMode = BubbleScaleMode.SQUARE_ROOT;

    public enum BubbleScaleMode {
        LINEAR,
        SQUARE_ROOT
    }

    public BubbleRenderer(XYPlot plot) {
        super(plot);
    }

    /* access modifiers changed from: protected */
    public void onRender(Canvas canvas, RectF plotArea, BubbleSeries series, FormatterType formatter, RenderStack stack) throws PlotRenderException {
        double doubleValue;
        Region magnitudeBounds = calculateBounds();
        for (int i = 0; i < series.size(); i++) {
            if (series.getY(i) != null && series.getZ(i).doubleValue() > 0.0d) {
                PointF centerPoint = ((XYPlot) getPlot()).getBounds().transform(series.getX(i), series.getY(i), plotArea, false, true);
                if (this.bubbleScaleMode == BubbleScaleMode.SQUARE_ROOT) {
                    doubleValue = Math.sqrt(series.getZ(i).doubleValue());
                } else {
                    doubleValue = series.getZ(i).doubleValue();
                }
                drawBubble(canvas, formatter, series, i, centerPoint, magnitudeBounds.transform(doubleValue, this.bubbleBounds).floatValue());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawBubble(Canvas canvas, FormatterType formatter, BubbleSeries series, int index, PointF centerPoint, float radius) {
        canvas.drawCircle(centerPoint.x, centerPoint.y, radius, formatter.getFillPaint());
        canvas.drawCircle(centerPoint.x, centerPoint.y, radius, formatter.getStrokePaint());
        if (series != null && formatter.hasPointLabelFormatter() && formatter.getPointLabeler() != null) {
            FontUtils.drawTextVerticallyCentered(canvas, formatter.getPointLabeler().getLabel(series, index), centerPoint.x, centerPoint.y, formatter.getPointLabelFormatter().getTextPaint());
        }
    }

    /* access modifiers changed from: protected */
    public void doDrawLegendIcon(Canvas canvas, RectF rect, FormatterType formatter) {
        drawBubble(canvas, formatter, (BubbleSeries) null, 0, new PointF(rect.centerX(), rect.centerY()), rect.width() / 2.5f);
    }

    public float getMinBubbleRadius() {
        return this.bubbleBounds.getMin().floatValue();
    }

    public void setMinBubbleRadius(float minBubbleRadius) {
        this.bubbleBounds.setMin(Float.valueOf(minBubbleRadius));
    }

    public float getMaxBubbleRadius() {
        return this.bubbleBounds.getMax().floatValue();
    }

    public void setMaxBubbleRadius(float maxBubbleRadius) {
        this.bubbleBounds.setMax(Float.valueOf(maxBubbleRadius));
    }

    public BubbleScaleMode getBubbleScaleMode() {
        return this.bubbleScaleMode;
    }

    public void setBubbleScaleMode(BubbleScaleMode bubbleScaleMode2) {
        this.bubbleScaleMode = bubbleScaleMode2;
    }

    /* access modifiers changed from: protected */
    public Region calculateBounds() {
        Region bounds = new Region();
        for (SeriesBundle<BubbleSeries, ? extends FormatterType> f : getSeriesAndFormatterList()) {
            SeriesUtils.minMax(bounds, (List<Number>[]) new List[]{f.getSeries().getZVals()});
        }
        if (bounds.getMax() == null || bounds.getMax().doubleValue() <= 0.0d) {
            return null;
        }
        if (this.bubbleScaleMode == BubbleScaleMode.SQUARE_ROOT) {
            bounds.setMax(Double.valueOf(Math.sqrt(bounds.getMax().doubleValue())));
        }
        if (bounds.getMin().doubleValue() <= 0.0d) {
            bounds.setMax(0);
            return bounds;
        } else if (this.bubbleScaleMode != BubbleScaleMode.SQUARE_ROOT) {
            return bounds;
        } else {
            bounds.setMin(Double.valueOf(Math.sqrt(bounds.getMin().doubleValue())));
            return bounds;
        }
    }
}
