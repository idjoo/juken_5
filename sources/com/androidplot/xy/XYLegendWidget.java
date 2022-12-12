package com.androidplot.xy;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import com.androidplot.ui.LayoutManager;
import com.androidplot.ui.SeriesBundle;
import com.androidplot.ui.Size;
import com.androidplot.ui.TableModel;
import com.androidplot.ui.widget.LegendWidget;
import com.androidplot.xy.XYLegendItem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class XYLegendWidget extends LegendWidget<XYLegendItem> {
    private XYPlot plot;

    public XYLegendWidget(LayoutManager layoutManager, XYPlot plot2, Size widgetSize, TableModel tableModel, Size iconSize) {
        super(tableModel, layoutManager, widgetSize, iconSize);
        this.plot = plot2;
        setLegendItemComparator(new Comparator<XYLegendItem>() {
            public int compare(XYLegendItem o1, XYLegendItem o2) {
                if (o1.type == o2.type) {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
                return o1.type.compareTo(o2.type);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void drawRegionLegendIcon(Canvas canvas, RectF rect, XYRegionFormatter formatter) {
        canvas.drawRect(rect, formatter.getPaint());
    }

    /* access modifiers changed from: protected */
    public void drawIcon(@NonNull Canvas canvas, @NonNull RectF iconRect, @NonNull XYLegendItem XYLegendItem) {
        switch (XYLegendItem.type) {
            case REGION:
                drawRegionLegendIcon(canvas, iconRect, (XYRegionFormatter) XYLegendItem.item);
                return;
            case SERIES:
                XYSeriesFormatter formatter = (XYSeriesFormatter) XYLegendItem.item;
                ((XYSeriesRenderer) this.plot.getRenderer(formatter.getRendererClass())).drawSeriesLegendIcon(canvas, iconRect, formatter);
                return;
            default:
                throw new UnsupportedOperationException("Unexpected item type: " + XYLegendItem.type);
        }
    }

    /* access modifiers changed from: protected */
    public List<XYLegendItem> getLegendItems() {
        ArrayList<XYLegendItem> items = new ArrayList<>();
        for (SeriesBundle<XYSeries, XYSeriesFormatter> sfPair : ((XYSeriesRegistry) this.plot.getRegistry()).getLegendEnabledItems()) {
            items.add(new XYLegendItem(XYLegendItem.Type.SERIES, sfPair.getFormatter(), sfPair.getSeries().getTitle()));
        }
        for (XYSeriesRenderer renderer : this.plot.getRendererList()) {
            for (Map.Entry<XYRegionFormatter, String> entry : renderer.getUniqueRegionFormatters().entrySet()) {
                items.add(new XYLegendItem(XYLegendItem.Type.REGION, entry.getKey(), entry.getValue()));
            }
        }
        return items;
    }
}
