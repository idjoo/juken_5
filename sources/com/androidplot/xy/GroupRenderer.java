package com.androidplot.xy;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import com.androidplot.exception.PlotRenderException;
import com.androidplot.ui.RenderStack;
import com.androidplot.ui.SeriesBundle;
import com.androidplot.xy.XYSeriesFormatter;
import java.util.List;

public abstract class GroupRenderer<FormatterType extends XYSeriesFormatter<XYRegionFormatter>> extends XYSeriesRenderer<XYSeries, FormatterType> {
    private static final String TAG = GroupRenderer.class.getName();

    public abstract void onRender(Canvas canvas, RectF rectF, List<SeriesBundle<XYSeries, ? extends FormatterType>> list, int i, RenderStack renderStack);

    public GroupRenderer(XYPlot plot) {
        super(plot);
    }

    /* access modifiers changed from: protected */
    public void onRender(Canvas canvas, RectF plotArea, XYSeries series, FormatterType formattertype, RenderStack stack) throws PlotRenderException {
        List<SeriesBundle<XYSeries, ? extends FormatterType>> sfList = getSeriesAndFormatterList();
        if (sfList != null) {
            int seriesLength = ((XYSeries) sfList.get(0).getSeries()).size();
            for (int i = 1; i < sfList.size(); i++) {
                if (((XYSeries) sfList.get(i).getSeries()).size() != seriesLength) {
                    Log.w(TAG, getClass() + ": not all associated series are of same size.");
                    return;
                }
            }
            stack.disable(getClass());
            onRender(canvas, plotArea, sfList, seriesLength, stack);
        }
    }
}
