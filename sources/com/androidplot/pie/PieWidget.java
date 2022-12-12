package com.androidplot.pie;

import android.graphics.Canvas;
import android.graphics.RectF;
import com.androidplot.exception.PlotRenderException;
import com.androidplot.ui.LayoutManager;
import com.androidplot.ui.RenderStack;
import com.androidplot.ui.Size;
import com.androidplot.ui.widget.Widget;
import java.util.Iterator;

public class PieWidget extends Widget {
    private PieChart pieChart;
    private RenderStack<? extends Segment, ? extends SegmentFormatter> renderStack;

    public PieWidget(LayoutManager layoutManager, PieChart pieChart2, Size metrics) {
        super(layoutManager, metrics);
        this.pieChart = pieChart2;
        this.renderStack = new RenderStack<>(pieChart2);
    }

    /* access modifiers changed from: protected */
    public void doOnDraw(Canvas canvas, RectF widgetRect) throws PlotRenderException {
        this.renderStack.sync();
        Iterator<RenderStack.StackElement<? extends Segment, ? extends SegmentFormatter>> it = this.renderStack.getElements().iterator();
        while (it.hasNext()) {
            RenderStack.StackElement thisElement = it.next();
            if (thisElement.isEnabled()) {
                ((PieRenderer) this.pieChart.getRenderer(thisElement.get().getFormatter().getRendererClass())).render(canvas, widgetRect, thisElement.get(), this.renderStack);
            }
        }
    }
}
