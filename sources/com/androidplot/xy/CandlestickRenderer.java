package com.androidplot.xy;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.androidplot.ui.RenderStack;
import com.androidplot.ui.SeriesBundle;
import com.androidplot.xy.CandlestickFormatter;
import java.util.List;

public class CandlestickRenderer<FormatterType extends CandlestickFormatter> extends GroupRenderer<FormatterType> {
    protected static final int CLOSE_INDEX = 3;
    protected static final int HIGH_INDEX = 0;
    protected static final int LOW_INDEX = 1;
    protected static final int OPEN_INDEX = 2;

    public CandlestickRenderer(XYPlot plot) {
        super(plot);
    }

    public void onRender(Canvas canvas, RectF plotArea, List<SeriesBundle<XYSeries, ? extends FormatterType>> sfList, int seriesSize, RenderStack stack) {
        FormatterType formatter = (CandlestickFormatter) sfList.get(0).getFormatter();
        for (int i = 0; i < seriesSize; i++) {
            XYSeries highSeries = (XYSeries) sfList.get(0).getSeries();
            XYSeries lowSeries = (XYSeries) sfList.get(1).getSeries();
            XYSeries openSeries = (XYSeries) sfList.get(2).getSeries();
            XYSeries closeSeries = (XYSeries) sfList.get(3).getSeries();
            Number x = highSeries.getX(i);
            Number high = highSeries.getY(i);
            Number low = lowSeries.getY(i);
            Number open = openSeries.getY(i);
            Number close = closeSeries.getY(i);
            PointF highPix = ((XYPlot) getPlot()).getBounds().transformScreen(x, high, plotArea);
            PointF lowPix = ((XYPlot) getPlot()).getBounds().transformScreen(x, low, plotArea);
            PointF openPix = ((XYPlot) getPlot()).getBounds().transformScreen(x, open, plotArea);
            PointF closePix = ((XYPlot) getPlot()).getBounds().transformScreen(x, close, plotArea);
            drawWick(canvas, highPix, lowPix, formatter);
            drawBody(canvas, openPix, closePix, formatter);
            drawUpperCap(canvas, highPix, formatter);
            drawLowerCap(canvas, lowPix, formatter);
            PointLabelFormatter plf = formatter.hasPointLabelFormatter() ? formatter.getPointLabelFormatter() : null;
            PointLabeler pointLabeler = formatter.getPointLabeler();
            if (!(plf == null || pointLabeler == null)) {
                drawTextLabel(canvas, highPix, pointLabeler.getLabel(highSeries, i), plf);
                drawTextLabel(canvas, lowPix, pointLabeler.getLabel(lowSeries, i), plf);
                drawTextLabel(canvas, openPix, pointLabeler.getLabel(openSeries, i), plf);
                drawTextLabel(canvas, closePix, pointLabeler.getLabel(closeSeries, i), plf);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawTextLabel(Canvas canvas, PointF coords, String text, PointLabelFormatter plf) {
        if (text != null) {
            canvas.drawText(text, coords.x + plf.hOffset, coords.y + plf.vOffset, plf.getTextPaint());
        }
    }

    /* access modifiers changed from: protected */
    public void drawWick(Canvas canvas, PointF min, PointF max, FormatterType formatter) {
        canvas.drawLine(min.x, min.y, max.x, max.y, formatter.getWickPaint());
    }

    /* access modifiers changed from: protected */
    public void drawBody(Canvas canvas, PointF open, PointF close, FormatterType formatter) {
        float halfWidth = formatter.getBodyWidth() / 2.0f;
        RectF rect = new RectF(open.x - halfWidth, open.y, close.x + halfWidth, close.y);
        Paint bodyFillPaint = open.y >= close.y ? formatter.getRisingBodyFillPaint() : formatter.getFallingBodyFillPaint();
        Paint bodyStrokePaint = open.y >= close.y ? formatter.getRisingBodyStrokePaint() : formatter.getFallingBodyStrokePaint();
        switch (formatter.getBodyStyle()) {
            case SQUARE:
                canvas.drawRect(rect, bodyFillPaint);
                canvas.drawRect(rect, bodyStrokePaint);
                return;
            case TRIANGULAR:
                drawTriangle(canvas, rect, bodyFillPaint, bodyStrokePaint);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void drawUpperCap(Canvas canvas, PointF val, FormatterType formatter) {
        float halfWidth = formatter.getUpperCapWidth();
        canvas.drawLine(val.x - halfWidth, val.y, val.x + halfWidth, val.y, formatter.getUpperCapPaint());
    }

    /* access modifiers changed from: protected */
    public void drawLowerCap(Canvas canvas, PointF val, FormatterType formatter) {
        float halfWidth = formatter.getLowerCapWidth();
        canvas.drawLine(val.x - halfWidth, val.y, val.x + halfWidth, val.y, formatter.getLowerCapPaint());
    }

    /* access modifiers changed from: protected */
    public void doDrawLegendIcon(Canvas canvas, RectF rect, FormatterType formattertype) {
    }

    /* access modifiers changed from: protected */
    public void drawTriangle(Canvas canvas, RectF rect, Paint fillPaint, Paint strokePaint) {
        Path path = new Path();
        path.moveTo(rect.centerX(), rect.bottom);
        path.lineTo(rect.left, rect.top);
        path.lineTo(rect.right, rect.top);
        path.close();
        canvas.drawPath(path, fillPaint);
        canvas.drawPath(path, strokePaint);
    }
}
