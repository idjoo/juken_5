package com.androidplot.ui.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.androidplot.exception.PlotRenderException;
import com.androidplot.ui.Anchor;
import com.androidplot.ui.BoxModel;
import com.androidplot.ui.BoxModelable;
import com.androidplot.ui.HorizontalPositioning;
import com.androidplot.ui.LayoutManager;
import com.androidplot.ui.PositionMetrics;
import com.androidplot.ui.Resizable;
import com.androidplot.ui.Size;
import com.androidplot.ui.SizeMetric;
import com.androidplot.ui.SizeMode;
import com.androidplot.ui.VerticalPositioning;
import com.androidplot.util.DisplayDimensions;
import com.androidplot.util.PixelUtils;

public abstract class Widget implements BoxModelable, Resizable {
    private Paint backgroundPaint;
    private Paint borderPaint;
    private BoxModel boxModel;
    private boolean clippingEnabled;
    private boolean isVisible;
    private RectF lastWidgetRect;
    private LayoutManager layoutManager;
    private DisplayDimensions plotDimensions;
    private PositionMetrics positionMetrics;
    private Rotation rotation;
    private Size size;
    private DisplayDimensions widgetDimensions;

    public enum Rotation {
        NINETY_DEGREES,
        NEGATIVE_NINETY_DEGREES,
        ONE_HUNDRED_EIGHTY_DEGREES,
        NONE
    }

    /* access modifiers changed from: protected */
    public abstract void doOnDraw(Canvas canvas, RectF rectF) throws PlotRenderException;

    public Widget(LayoutManager layoutManager2, SizeMetric heightMetric, SizeMetric widthMetric) {
        this(layoutManager2, new Size(heightMetric, widthMetric));
    }

    public Widget(LayoutManager layoutManager2, Size size2) {
        this.clippingEnabled = false;
        this.boxModel = new BoxModel();
        this.plotDimensions = new DisplayDimensions();
        this.widgetDimensions = new DisplayDimensions();
        this.isVisible = true;
        this.rotation = Rotation.NONE;
        this.lastWidgetRect = null;
        this.layoutManager = layoutManager2;
        Size oldSize = this.size;
        setSize(size2);
        onMetricsChanged(oldSize, size2);
    }

    public DisplayDimensions getWidgetDimensions() {
        return this.widgetDimensions;
    }

    public Anchor getAnchor() {
        return getPositionMetrics().getAnchor();
    }

    public void setAnchor(Anchor anchor) {
        getPositionMetrics().setAnchor(anchor);
    }

    public void position(float x, HorizontalPositioning horizontalPositioning, float y, VerticalPositioning verticalPositioning) {
        position(x, horizontalPositioning, y, verticalPositioning, Anchor.LEFT_TOP);
    }

    public void position(float x, HorizontalPositioning horizontalPositioning, float y, VerticalPositioning verticalPositioning, Anchor anchor) {
        setPositionMetrics(new PositionMetrics(x, horizontalPositioning, y, verticalPositioning, anchor));
        this.layoutManager.addToTop(this);
    }

    /* access modifiers changed from: protected */
    public void onMetricsChanged(Size oldSize, Size newSize) {
    }

    public void onPostInit() {
    }

    public boolean containsPoint(PointF point) {
        return this.widgetDimensions.canvasRect.contains(point.x, point.y);
    }

    public void setSize(Size size2) {
        this.size = size2;
    }

    public Size getSize() {
        return this.size;
    }

    public void setWidth(float width) {
        this.size.getWidth().setValue(width);
    }

    public void setWidth(float width, SizeMode layoutType) {
        this.size.getWidth().set(width, layoutType);
    }

    public void setHeight(float height) {
        this.size.getHeight().setValue(height);
    }

    public void setHeight(float height, SizeMode layoutType) {
        this.size.getHeight().set(height, layoutType);
    }

    public SizeMetric getWidthMetric() {
        return this.size.getWidth();
    }

    public SizeMetric getHeightMetric() {
        return this.size.getHeight();
    }

    public float getWidthPix(float size2) {
        return this.size.getWidth().getPixelValue(size2);
    }

    public float getHeightPix(float size2) {
        return this.size.getHeight().getPixelValue(size2);
    }

    public RectF getMarginatedRect(RectF widgetRect) {
        return this.boxModel.getMarginatedRect(widgetRect);
    }

    public RectF getPaddedRect(RectF widgetMarginRect) {
        return this.boxModel.getPaddedRect(widgetMarginRect);
    }

    public void setMarginRight(float marginRight) {
        this.boxModel.setMarginRight(marginRight);
    }

    public void setMargins(float left, float top, float right, float bottom) {
        this.boxModel.setMargins(left, top, right, bottom);
    }

    public void setPadding(float left, float top, float right, float bottom) {
        this.boxModel.setPadding(left, top, right, bottom);
    }

    public float getMarginTop() {
        return this.boxModel.getMarginTop();
    }

    public void setMarginTop(float marginTop) {
        this.boxModel.setMarginTop(marginTop);
    }

    public float getMarginBottom() {
        return this.boxModel.getMarginBottom();
    }

    public float getPaddingLeft() {
        return this.boxModel.getPaddingLeft();
    }

    public void setPaddingLeft(float paddingLeft) {
        this.boxModel.setPaddingLeft(paddingLeft);
    }

    public float getPaddingTop() {
        return this.boxModel.getPaddingTop();
    }

    public void setPaddingTop(float paddingTop) {
        this.boxModel.setPaddingTop(paddingTop);
    }

    public float getPaddingRight() {
        return this.boxModel.getPaddingRight();
    }

    public void setPaddingRight(float paddingRight) {
        this.boxModel.setPaddingRight(paddingRight);
    }

    public float getPaddingBottom() {
        return this.boxModel.getPaddingBottom();
    }

    public void setPaddingBottom(float paddingBottom) {
        this.boxModel.setPaddingBottom(paddingBottom);
    }

    public void setMarginBottom(float marginBottom) {
        this.boxModel.setMarginBottom(marginBottom);
    }

    public float getMarginLeft() {
        return this.boxModel.getMarginLeft();
    }

    public void setMarginLeft(float marginLeft) {
        this.boxModel.setMarginLeft(marginLeft);
    }

    public float getMarginRight() {
        return this.boxModel.getMarginRight();
    }

    public synchronized void refreshLayout() {
        if (this.positionMetrics != null) {
            float elementWidth = getWidthPix(this.plotDimensions.paddedRect.width());
            float elementHeight = getHeightPix(this.plotDimensions.paddedRect.height());
            PointF coords = calculateCoordinates(elementHeight, elementWidth, this.plotDimensions.paddedRect, this.positionMetrics);
            RectF widgetRect = new RectF(coords.x, coords.y, coords.x + elementWidth, coords.y + elementHeight);
            RectF marginatedWidgetRect = getMarginatedRect(widgetRect);
            this.widgetDimensions = new DisplayDimensions(widgetRect, marginatedWidgetRect, getPaddedRect(marginatedWidgetRect));
        }
    }

    public synchronized void layout(DisplayDimensions plotDimensions2) {
        this.plotDimensions = plotDimensions2;
        refreshLayout();
    }

    public static PointF calculateCoordinates(float height, float width, RectF viewRect, PositionMetrics metrics) {
        return PixelUtils.sub(new PointF(metrics.getXPositionMetric().getPixelValue(viewRect.width()) + viewRect.left, metrics.getYPositionMetric().getPixelValue(viewRect.height()) + viewRect.top), getAnchorOffset(width, height, metrics.getAnchor()));
    }

    public static PointF getAnchorOffset(float width, float height, Anchor anchor) {
        PointF point = new PointF();
        switch (anchor) {
            case LEFT_TOP:
                break;
            case LEFT_MIDDLE:
                point.set(0.0f, height / 2.0f);
                break;
            case LEFT_BOTTOM:
                point.set(0.0f, height);
                break;
            case RIGHT_TOP:
                point.set(width, 0.0f);
                break;
            case RIGHT_BOTTOM:
                point.set(width, height);
                break;
            case RIGHT_MIDDLE:
                point.set(width, height / 2.0f);
                break;
            case TOP_MIDDLE:
                point.set(width / 2.0f, 0.0f);
                break;
            case BOTTOM_MIDDLE:
                point.set(width / 2.0f, height);
                break;
            case CENTER:
                point.set(width / 2.0f, height / 2.0f);
                break;
            default:
                throw new IllegalArgumentException("Unsupported anchor location: " + anchor);
        }
        return point;
    }

    public static PointF getAnchorCoordinates(RectF widgetRect, Anchor anchor) {
        return PixelUtils.add(new PointF(widgetRect.left, widgetRect.top), getAnchorOffset(widgetRect.width(), widgetRect.height(), anchor));
    }

    public static PointF getAnchorCoordinates(float x, float y, float width, float height, Anchor anchor) {
        return getAnchorCoordinates(new RectF(x, y, x + width, y + height), anchor);
    }

    private void checkSize(@NonNull RectF widgetRect) {
        if (this.lastWidgetRect == null || !this.lastWidgetRect.equals(widgetRect)) {
            onResize(this.lastWidgetRect, widgetRect);
        }
        this.lastWidgetRect = widgetRect;
    }

    /* access modifiers changed from: protected */
    public void onResize(@Nullable RectF oldRect, @NonNull RectF newRect) {
    }

    public void draw(Canvas canvas) throws PlotRenderException {
        if (isVisible()) {
            if (this.backgroundPaint != null) {
                drawBackground(canvas, this.widgetDimensions.canvasRect);
            }
            canvas.save();
            RectF widgetRect = applyRotation(canvas, this.widgetDimensions.paddedRect);
            checkSize(widgetRect);
            doOnDraw(canvas, widgetRect);
            canvas.restore();
            if (this.borderPaint != null) {
                drawBorder(canvas, widgetRect);
            }
        }
    }

    /* access modifiers changed from: protected */
    public RectF applyRotation(Canvas canvas, RectF rect) {
        float rotationDegs = 0.0f;
        float cx = this.widgetDimensions.paddedRect.centerX();
        float cy = this.widgetDimensions.paddedRect.centerY();
        float halfHeight = this.widgetDimensions.paddedRect.height() / 2.0f;
        float halfWidth = this.widgetDimensions.paddedRect.width() / 2.0f;
        switch (this.rotation) {
            case NINETY_DEGREES:
                rotationDegs = 90.0f;
                rect = new RectF(cx - halfHeight, cy - halfWidth, cx + halfHeight, cy + halfWidth);
                break;
            case NEGATIVE_NINETY_DEGREES:
                rotationDegs = -90.0f;
                rect = new RectF(cx - halfHeight, cy - halfWidth, cx + halfHeight, cy + halfWidth);
                break;
            case ONE_HUNDRED_EIGHTY_DEGREES:
                rotationDegs = 180.0f;
                break;
            case NONE:
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented.");
        }
        if (this.rotation != Rotation.NONE) {
            canvas.rotate(rotationDegs, cx, cy);
        }
        return rect;
    }

    /* access modifiers changed from: protected */
    public void drawBorder(Canvas canvas, RectF paddedRect) {
        canvas.drawRect(paddedRect, this.borderPaint);
    }

    /* access modifiers changed from: protected */
    public void drawBackground(Canvas canvas, RectF widgetRect) {
        canvas.drawRect(widgetRect, this.backgroundPaint);
    }

    public Paint getBorderPaint() {
        return this.borderPaint;
    }

    public void setBorderPaint(Paint borderPaint2) {
        this.borderPaint = borderPaint2;
    }

    public Paint getBackgroundPaint() {
        return this.backgroundPaint;
    }

    public void setBackgroundPaint(Paint backgroundPaint2) {
        this.backgroundPaint = backgroundPaint2;
    }

    public boolean isClippingEnabled() {
        return this.clippingEnabled;
    }

    public void setClippingEnabled(boolean clippingEnabled2) {
        this.clippingEnabled = clippingEnabled2;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public PositionMetrics getPositionMetrics() {
        return this.positionMetrics;
    }

    public void setPositionMetrics(PositionMetrics positionMetrics2) {
        this.positionMetrics = positionMetrics2;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public void setRotation(Rotation rotation2) {
        this.rotation = rotation2;
    }
}
