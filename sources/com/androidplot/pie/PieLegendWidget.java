package com.androidplot.pie;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import com.androidplot.ui.LayoutManager;
import com.androidplot.ui.SeriesBundle;
import com.androidplot.ui.Size;
import com.androidplot.ui.TableModel;
import com.androidplot.ui.widget.LegendWidget;
import java.util.ArrayList;
import java.util.List;

public class PieLegendWidget extends LegendWidget<PieLegendItem> {
    private PieChart pieChart;

    public PieLegendWidget(LayoutManager layoutManager, PieChart pieChart2, Size widgetSize, TableModel tableModel, Size iconSize) {
        super(tableModel, layoutManager, widgetSize, iconSize);
        this.pieChart = pieChart2;
    }

    /* access modifiers changed from: protected */
    public void drawIcon(@NonNull Canvas canvas, @NonNull RectF iconRect, @NonNull PieLegendItem item) {
        canvas.drawRect(iconRect, item.formatter.getFillPaint());
    }

    /* access modifiers changed from: protected */
    public List<PieLegendItem> getLegendItems() {
        List<PieLegendItem> legendItems = new ArrayList<>();
        for (SeriesBundle<Segment, SegmentFormatter> item : ((SegmentRegistry) this.pieChart.getRegistry()).getLegendEnabledItems()) {
            legendItems.add(new PieLegendItem(item.getSeries(), item.getFormatter()));
        }
        return legendItems;
    }
}
