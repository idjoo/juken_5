package com.androidplot.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import com.androidplot.exception.PlotRenderException;
import com.androidplot.ui.widget.Widget;
import com.androidplot.util.DisplayDimensions;
import com.androidplot.util.LinkedLayerList;

public class LayoutManager extends LinkedLayerList<Widget> implements View.OnTouchListener, Resizable {
    private Paint anchorPaint = new Paint();
    private DisplayDimensions displayDims = new DisplayDimensions();
    private boolean drawAnchorsEnabled = false;
    private boolean drawMarginsEnabled = false;
    private boolean drawOutlineShadowsEnabled = false;
    private boolean drawOutlinesEnabled = false;
    private boolean drawPaddingEnabled = false;
    private Paint marginPaint;
    private Paint outlinePaint;
    private Paint outlineShadowPaint;
    private Paint paddingPaint;

    public synchronized void onPostInit() {
        for (Widget w : elements()) {
            w.onPostInit();
        }
    }

    public LayoutManager() {
        this.anchorPaint.setStyle(Paint.Style.FILL);
        this.anchorPaint.setColor(-16711936);
        this.outlinePaint = new Paint();
        this.outlinePaint.setColor(-16711936);
        this.outlinePaint.setStyle(Paint.Style.STROKE);
        this.outlinePaint.setAntiAlias(true);
        this.outlinePaint.setStrokeWidth(2.0f);
        this.marginPaint = new Paint();
        this.marginPaint.setColor(InputDeviceCompat.SOURCE_ANY);
        this.marginPaint.setStyle(Paint.Style.FILL);
        this.marginPaint.setAlpha(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.paddingPaint = new Paint();
        this.paddingPaint.setColor(-16776961);
        this.paddingPaint.setStyle(Paint.Style.FILL);
        this.paddingPaint.setAlpha(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
    }

    public void setMarkupEnabled(boolean enabled) {
        setDrawOutlinesEnabled(enabled);
        setDrawAnchorsEnabled(enabled);
        setDrawMarginsEnabled(enabled);
        setDrawPaddingEnabled(enabled);
        setDrawOutlineShadowsEnabled(enabled);
    }

    public void draw(Canvas canvas) throws PlotRenderException {
        if (isDrawMarginsEnabled()) {
            drawSpacing(canvas, this.displayDims.canvasRect, this.displayDims.marginatedRect, this.marginPaint);
        }
        if (isDrawPaddingEnabled()) {
            drawSpacing(canvas, this.displayDims.marginatedRect, this.displayDims.paddedRect, this.paddingPaint);
        }
        for (Widget widget : elements()) {
            try {
                canvas.save();
                PositionMetrics metrics = widget.getPositionMetrics();
                float elementWidth = widget.getWidthPix(this.displayDims.paddedRect.width());
                float elementHeight = widget.getHeightPix(this.displayDims.paddedRect.height());
                PointF coords = Widget.calculateCoordinates(elementHeight, elementWidth, this.displayDims.paddedRect, metrics);
                DisplayDimensions dims = widget.getWidgetDimensions();
                if (this.drawOutlineShadowsEnabled) {
                    canvas.drawRect(dims.canvasRect, this.outlineShadowPaint);
                }
                if (widget.isClippingEnabled()) {
                    canvas.clipRect(dims.canvasRect, Region.Op.INTERSECT);
                }
                widget.draw(canvas);
                if (this.drawMarginsEnabled) {
                    drawSpacing(canvas, dims.canvasRect, dims.marginatedRect, getMarginPaint());
                }
                if (this.drawPaddingEnabled) {
                    drawSpacing(canvas, dims.marginatedRect, dims.paddedRect, getPaddingPaint());
                }
                if (this.drawAnchorsEnabled) {
                    drawAnchor(canvas, Widget.getAnchorCoordinates(coords.x, coords.y, elementWidth, elementHeight, metrics.getAnchor()));
                }
                if (this.drawOutlinesEnabled) {
                    canvas.drawRect(dims.canvasRect, this.outlinePaint);
                }
            } finally {
                canvas.restore();
            }
        }
    }

    private static void drawSpacing(Canvas canvas, RectF outer, RectF inner, Paint paint) {
        try {
            canvas.save();
            canvas.clipRect(inner, Region.Op.DIFFERENCE);
            canvas.drawRect(outer, paint);
        } finally {
            canvas.restore();
        }
    }

    /* access modifiers changed from: protected */
    public void drawAnchor(Canvas canvas, PointF coords) {
        canvas.drawRect(coords.x - 4.0f, coords.y - 4.0f, coords.x + 4.0f, coords.y + 4.0f, this.anchorPaint);
    }

    public boolean isDrawOutlinesEnabled() {
        return this.drawOutlinesEnabled;
    }

    public void setDrawOutlinesEnabled(boolean drawOutlinesEnabled2) {
        this.drawOutlinesEnabled = drawOutlinesEnabled2;
    }

    public Paint getOutlinePaint() {
        return this.outlinePaint;
    }

    public void setOutlinePaint(Paint outlinePaint2) {
        this.outlinePaint = outlinePaint2;
    }

    public boolean isDrawAnchorsEnabled() {
        return this.drawAnchorsEnabled;
    }

    public void setDrawAnchorsEnabled(boolean drawAnchorsEnabled2) {
        this.drawAnchorsEnabled = drawAnchorsEnabled2;
    }

    public boolean isDrawMarginsEnabled() {
        return this.drawMarginsEnabled;
    }

    public void setDrawMarginsEnabled(boolean drawMarginsEnabled2) {
        this.drawMarginsEnabled = drawMarginsEnabled2;
    }

    public Paint getMarginPaint() {
        return this.marginPaint;
    }

    public void setMarginPaint(Paint marginPaint2) {
        this.marginPaint = marginPaint2;
    }

    public boolean isDrawPaddingEnabled() {
        return this.drawPaddingEnabled;
    }

    public void setDrawPaddingEnabled(boolean drawPaddingEnabled2) {
        this.drawPaddingEnabled = drawPaddingEnabled2;
    }

    public Paint getPaddingPaint() {
        return this.paddingPaint;
    }

    public void setPaddingPaint(Paint paddingPaint2) {
        this.paddingPaint = paddingPaint2;
    }

    public boolean isDrawOutlineShadowsEnabled() {
        return this.drawOutlineShadowsEnabled;
    }

    public void setDrawOutlineShadowsEnabled(boolean drawOutlineShadowsEnabled2) {
        this.drawOutlineShadowsEnabled = drawOutlineShadowsEnabled2;
        if (drawOutlineShadowsEnabled2 && this.outlineShadowPaint == null) {
            this.outlineShadowPaint = new Paint();
            this.outlineShadowPaint.setColor(-12303292);
            this.outlineShadowPaint.setStyle(Paint.Style.FILL);
            this.outlineShadowPaint.setShadowLayer(3.0f, 5.0f, 5.0f, ViewCompat.MEASURED_STATE_MASK);
        }
    }

    public Paint getOutlineShadowPaint() {
        return this.outlineShadowPaint;
    }

    public void setOutlineShadowPaint(Paint outlineShadowPaint2) {
        this.outlineShadowPaint = outlineShadowPaint2;
    }

    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public void refreshLayout() {
        for (Widget widget : elements()) {
            widget.layout(this.displayDims);
        }
    }

    public void layout(DisplayDimensions dims) {
        this.displayDims = dims;
        refreshLayout();
    }
}
