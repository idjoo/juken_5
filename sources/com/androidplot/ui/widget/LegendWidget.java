package com.androidplot.ui.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import com.androidplot.exception.PlotRenderException;
import com.androidplot.ui.LayoutManager;
import com.androidplot.ui.Size;
import com.androidplot.ui.TableModel;
import com.androidplot.ui.widget.LegendItem;
import com.androidplot.util.FontUtils;
import com.androidplot.util.PixelUtils;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public abstract class LegendWidget<ItemT extends LegendItem> extends Widget {
    private static final float DEFAULT_TEXT_SIZE_DP = 20.0f;
    private boolean drawIconBackgroundEnabled = true;
    private boolean drawIconBorderEnabled = true;
    private Paint iconBackgroundPaint;
    private Paint iconBorderPaint;
    private Size iconSize;
    private Comparator<ItemT> legendItemComparator;
    private TableModel tableModel;
    private Paint textPaint = new Paint();

    /* access modifiers changed from: protected */
    public abstract void drawIcon(@NonNull Canvas canvas, @NonNull RectF rectF, @NonNull ItemT itemt);

    /* access modifiers changed from: protected */
    public abstract List<ItemT> getLegendItems();

    public LegendWidget(@NonNull TableModel tableModel2, @NonNull LayoutManager layoutManager, @NonNull Size size, @NonNull Size iconSize2) {
        super(layoutManager, size);
        this.textPaint.setColor(-3355444);
        this.textPaint.setTextSize(PixelUtils.spToPix(DEFAULT_TEXT_SIZE_DP));
        this.textPaint.setAntiAlias(true);
        this.iconBackgroundPaint = new Paint();
        this.iconBackgroundPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.iconBorderPaint = new Paint();
        this.iconBorderPaint.setColor(0);
        this.iconBorderPaint.setStyle(Paint.Style.STROKE);
        setTableModel(tableModel2);
        this.iconSize = iconSize2;
    }

    /* access modifiers changed from: protected */
    public void doOnDraw(Canvas canvas, RectF widgetRect) throws PlotRenderException {
        List<ItemT> items = getLegendItems();
        if (this.legendItemComparator != null) {
            Collections.sort(items, this.legendItemComparator);
        }
        Iterator<RectF> cellRectIterator = this.tableModel.getIterator(widgetRect, items.size());
        for (ItemT item : items) {
            RectF cellRect = cellRectIterator.next();
            RectF iconRect = getIconRect(cellRect);
            beginDrawingCell(canvas, iconRect);
            drawItem(canvas, iconRect, item);
            finishDrawingCell(canvas, cellRect, iconRect, item);
        }
    }

    /* access modifiers changed from: protected */
    public void drawItem(@NonNull Canvas canvas, @NonNull RectF iconRect, @NonNull ItemT item) {
        drawIcon(canvas, iconRect, item);
    }

    private RectF getIconRect(RectF cellRect) {
        float cellRectCenterY = cellRect.top + (cellRect.height() / 2.0f);
        RectF iconRect = this.iconSize.getRectF(cellRect);
        iconRect.offsetTo(cellRect.left + 1.0f, cellRectCenterY - (iconRect.height() / 2.0f));
        return iconRect;
    }

    /* access modifiers changed from: protected */
    public void beginDrawingCell(Canvas canvas, RectF iconRect) {
        if (this.drawIconBackgroundEnabled && this.iconBackgroundPaint != null) {
            canvas.drawRect(iconRect, this.iconBackgroundPaint);
        }
    }

    /* access modifiers changed from: protected */
    public void finishDrawingCell(Canvas canvas, RectF cellRect, RectF iconRect, LegendItem legendItem) {
        if (this.drawIconBorderEnabled && this.iconBorderPaint != null) {
            canvas.drawRect(iconRect, this.iconBorderPaint);
        }
        float centeredTextOriginY = getRectCenterY(cellRect) + (FontUtils.getFontHeight(this.textPaint) / 2.0f);
        if (this.textPaint.getTextAlign().equals(Paint.Align.RIGHT)) {
            canvas.drawText(legendItem.getTitle(), iconRect.left - 2.0f, centeredTextOriginY, this.textPaint);
        } else {
            canvas.drawText(legendItem.getTitle(), iconRect.right + 2.0f, centeredTextOriginY, this.textPaint);
        }
    }

    protected static float getRectCenterY(RectF cellRect) {
        return cellRect.top + (cellRect.height() / 2.0f);
    }

    public synchronized void setTableModel(TableModel tableModel2) {
        this.tableModel = tableModel2;
    }

    public Paint getTextPaint() {
        return this.textPaint;
    }

    public void setTextPaint(Paint textPaint2) {
        this.textPaint = textPaint2;
    }

    public boolean isDrawIconBackgroundEnabled() {
        return this.drawIconBackgroundEnabled;
    }

    public void setDrawIconBackgroundEnabled(boolean drawIconBackgroundEnabled2) {
        this.drawIconBackgroundEnabled = drawIconBackgroundEnabled2;
    }

    public boolean isDrawIconBorderEnabled() {
        return this.drawIconBorderEnabled;
    }

    public void setDrawIconBorderEnabled(boolean drawIconBorderEnabled2) {
        this.drawIconBorderEnabled = drawIconBorderEnabled2;
    }

    public Size getIconSize() {
        return this.iconSize;
    }

    public void setIconSize(Size iconSize2) {
        this.iconSize = iconSize2;
    }

    public Comparator<ItemT> getLegendItemComparator() {
        return this.legendItemComparator;
    }

    public void setLegendItemComparator(Comparator<ItemT> legendItemComparator2) {
        this.legendItemComparator = legendItemComparator2;
    }
}
