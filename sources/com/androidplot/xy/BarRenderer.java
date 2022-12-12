package com.androidplot.xy;

import android.graphics.Canvas;
import android.graphics.RectF;
import com.androidplot.ui.RenderStack;
import com.androidplot.ui.SeriesBundle;
import com.androidplot.util.PixelUtils;
import com.androidplot.util.RectFUtils;
import com.androidplot.xy.BarFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class BarRenderer<FormatterType extends BarFormatter> extends GroupRenderer<FormatterType> {
    private BarGroupWidthMode barGroupWidthMode = BarGroupWidthMode.FIXED_WIDTH;
    private BarOrientation barOrientation = BarOrientation.OVERLAID;
    private float width = PixelUtils.dpToPix(3.0f);

    public enum BarGroupWidthMode {
        FIXED_WIDTH,
        FIXED_GAP
    }

    public enum BarOrientation {
        OVERLAID,
        STACKED,
        SIDE_BY_SIDE
    }

    public BarRenderer(XYPlot plot) {
        super(plot);
    }

    public void setBarOrientation(BarOrientation renderBarOrientation) {
        this.barOrientation = renderBarOrientation;
    }

    public BarOrientation getBarOrientation() {
        return this.barOrientation;
    }

    public BarGroupWidthMode getBarGroupWidthMode() {
        return this.barGroupWidthMode;
    }

    public float getBarGroupWidth() {
        return this.width;
    }

    public void setBarGroupWidth(BarGroupWidthMode mode, float width2) {
        this.barGroupWidthMode = mode;
        this.width = width2;
    }

    /* access modifiers changed from: protected */
    public BarComparator getBarComparator(float rangeOriginPx) {
        return new BarComparator(getBarOrientation(), rangeOriginPx);
    }

    public void doDrawLegendIcon(Canvas canvas, RectF rect, BarFormatter formatter) {
        if (formatter.hasFillPaint()) {
            canvas.drawRect(rect, formatter.getFillPaint());
        }
        canvas.drawRect(rect, formatter.getBorderPaint());
    }

    public FormatterType getFormatter(int index, XYSeries series) {
        return null;
    }

    public void onRender(Canvas canvas, RectF plotArea, List<SeriesBundle<XYSeries, ? extends FormatterType>> sfList, int seriesSize, RenderStack stack) {
        ArrayList<BarGroup> arrayList = new ArrayList<>();
        for (int i = 0; i < seriesSize; i++) {
            BarGroup barGroup = new BarGroup(i, 0.0f, plotArea);
            int seriesOrder = 0;
            for (SeriesBundle<XYSeries, ? extends FormatterType> bundle : sfList) {
                if (bundle.getSeries().getX(i) != null) {
                    Bar bar = new Bar((XYPlot) getPlot(), bundle.getSeries(), (BarFormatter) bundle.getFormatter(), seriesOrder, i, plotArea);
                    barGroup.addBar(bar);
                    barGroup.centerPix = bar.xPix;
                }
                seriesOrder++;
            }
            arrayList.add(barGroup);
        }
        int groupCount = arrayList.size();
        for (BarGroup barGroup2 : arrayList) {
            switch (this.barGroupWidthMode) {
                case FIXED_WIDTH:
                    barGroup2.leftPix = barGroup2.centerPix - (this.width / 2.0f);
                    barGroup2.rightPix = barGroup2.leftPix + this.width;
                    break;
                case FIXED_GAP:
                    float barWidth = plotArea.width();
                    if (groupCount > 1) {
                        barWidth = (((BarGroup) arrayList.get(1)).centerPix - ((BarGroup) arrayList.get(0)).centerPix) - this.width;
                    }
                    float halfWidth = barWidth / 2.0f;
                    barGroup2.leftPix = barGroup2.centerPix - halfWidth;
                    barGroup2.rightPix = barGroup2.centerPix + halfWidth;
                    break;
            }
            float rangeOriginPx = (float) ((XYPlot) getPlot()).getBounds().yRegion.transform(((XYPlot) getPlot()).getRangeOrigin().doubleValue(), (double) plotArea.top, (double) plotArea.bottom, true);
            BarComparator comparator = getBarComparator(rangeOriginPx);
            switch (this.barOrientation) {
                case OVERLAID:
                    Collections.sort(barGroup2.bars, comparator);
                    Iterator<Bar> it = barGroup2.bars.iterator();
                    while (it.hasNext()) {
                        Bar bar2 = it.next();
                        Canvas canvas2 = canvas;
                        drawBar(canvas2, bar2, createBarRect(bar2.barGroup.leftPix, bar2.yPix, bar2.barGroup.rightPix, rangeOriginPx, bar2.formatter));
                    }
                    break;
                case SIDE_BY_SIDE:
                    float width2 = barGroup2.getWidth() / ((float) barGroup2.bars.size());
                    float leftX = barGroup2.leftPix;
                    Collections.sort(barGroup2.bars, comparator);
                    Iterator<Bar> it2 = barGroup2.bars.iterator();
                    while (it2.hasNext()) {
                        Bar bar3 = it2.next();
                        Canvas canvas3 = canvas;
                        drawBar(canvas3, bar3, createBarRect(leftX, bar3.yPix, leftX + width2, rangeOriginPx, bar3.formatter));
                        leftX += width2;
                    }
                    break;
                case STACKED:
                    float bottom = (float) ((int) barGroup2.plotArea.bottom);
                    Collections.sort(barGroup2.bars, comparator);
                    Iterator<Bar> it3 = barGroup2.bars.iterator();
                    while (it3.hasNext()) {
                        Bar bar4 = it3.next();
                        float top = bottom - (((float) ((int) bar4.barGroup.plotArea.bottom)) - bar4.yPix);
                        Canvas canvas4 = canvas;
                        drawBar(canvas4, bar4, createBarRect(bar4.barGroup.leftPix, top, bar4.barGroup.rightPix, bottom, bar4.formatter));
                        bottom = top;
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Unexpected BarOrientation: " + this.barOrientation);
            }
        }
    }

    /* access modifiers changed from: protected */
    public RectF createBarRect(float w1, float h1, float w2, float h2, BarFormatter formatter) {
        RectF result = RectFUtils.createFromEdges(w1, h1, w2, h2);
        result.left += formatter.getMarginLeft();
        result.right -= formatter.getMarginRight();
        result.top += formatter.getMarginTop();
        result.bottom -= formatter.getMarginBottom();
        return result;
    }

    /* access modifiers changed from: protected */
    public void drawBar(Canvas canvas, Bar<FormatterType> bar, RectF rect) {
        PointLabelFormatter plf;
        PointLabeler pointLabeler = null;
        if (bar.getY() != null) {
            BarFormatter formatter = getFormatter(bar.i, bar.series);
            if (formatter == null) {
                formatter = bar.formatter;
            }
            if (rect.height() > 0.0f && rect.width() > 0.0f) {
                if (formatter.hasFillPaint()) {
                    canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, formatter.getFillPaint());
                }
                if (formatter.hasLinePaint()) {
                    canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, formatter.getBorderPaint());
                }
            }
            if (formatter.hasPointLabelFormatter()) {
                plf = formatter.getPointLabelFormatter();
            } else {
                plf = null;
            }
            if (formatter != null) {
                pointLabeler = formatter.getPointLabeler();
            }
            if (plf != null && plf.hasTextPaint() && pointLabeler != null) {
                canvas.drawText(pointLabeler.getLabel(bar.series, bar.i), rect.centerX() + plf.hOffset, bar.yPix + plf.vOffset, plf.getTextPaint());
            }
        }
    }

    public static class Bar<FormatterType extends BarFormatter> {
        protected BarGroup barGroup;
        public final FormatterType formatter;
        public final int i;
        public final XYSeries series;
        public final int seriesOrder;
        public final float xPix;
        public final float yPix;

        public Bar(XYPlot plot, XYSeries series2, FormatterType formatter2, int seriesOrder2, int i2, RectF plotArea) {
            this.series = series2;
            this.formatter = formatter2;
            this.i = i2;
            this.seriesOrder = seriesOrder2;
            this.xPix = (float) plot.getBounds().getxRegion().transform(series2.getX(i2).doubleValue(), (double) plotArea.left, (double) plotArea.right, false);
            if (series2.getY(i2) != null) {
                this.yPix = (float) plot.getBounds().yRegion.transform(series2.getY(i2).doubleValue(), (double) plotArea.top, (double) plotArea.bottom, true);
                return;
            }
            this.yPix = 0.0f;
        }

        public Number getY() {
            return this.series.getY(this.i);
        }
    }

    private static class BarGroup {
        public ArrayList<Bar> bars = new ArrayList<>();
        public float centerPix;
        public int i;
        public float leftPix;
        public RectF plotArea;
        public float rightPix;

        public BarGroup(int i2, float centerPix2, RectF plotArea2) {
            this.centerPix = centerPix2;
            this.plotArea = plotArea2;
            this.i = i2;
        }

        public void addBar(Bar bar) {
            bar.barGroup = this;
            this.bars.add(bar);
        }

        /* access modifiers changed from: protected */
        public float getWidth() {
            return this.rightPix - this.leftPix;
        }
    }

    public static class BarComparator implements Comparator<Bar> {
        private final BarOrientation barOrientation;
        private final float rangeOriginPx;

        public BarComparator(BarOrientation barOrientation2, float rangeOriginPx2) {
            this.rangeOriginPx = rangeOriginPx2;
            this.barOrientation = barOrientation2;
        }

        public int compare(Bar bar1, Bar bar2) {
            switch (this.barOrientation) {
                case OVERLAID:
                    if (bar1.yPix <= this.rangeOriginPx || bar2.yPix <= this.rangeOriginPx) {
                        return Float.valueOf(bar1.yPix).compareTo(Float.valueOf(bar2.yPix));
                    }
                    return Float.valueOf(bar2.yPix).compareTo(Float.valueOf(bar1.yPix));
                default:
                    return Integer.valueOf(bar1.seriesOrder).compareTo(Integer.valueOf(bar2.seriesOrder));
            }
        }
    }
}
