package com.androidplot.xy;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.InputDeviceCompat;
import com.androidplot.R;
import com.androidplot.Region;
import com.androidplot.exception.PlotRenderException;
import com.androidplot.ui.HorizontalPosition;
import com.androidplot.ui.Insets;
import com.androidplot.ui.LayoutManager;
import com.androidplot.ui.RenderStack;
import com.androidplot.ui.Size;
import com.androidplot.ui.VerticalPosition;
import com.androidplot.ui.widget.Widget;
import com.androidplot.util.AttrUtils;
import com.androidplot.util.FontUtils;
import com.androidplot.util.PixelUtils;
import com.androidplot.util.RectFUtils;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;

public class XYGraphWidget extends Widget {
    /* access modifiers changed from: private */
    public static final float DEFAULT_LINE_LABEL_TEXT_SIZE_PX = PixelUtils.spToPix(15.0f);
    private static final float FUDGE = 1.0E-5f;
    private static final int MARKER_LABEL_SPACING = 2;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int ZERO = 0;
    private CursorLabelFormatter cursorLabelFormatter;
    private Paint domainCursorPaint;
    private Float domainCursorPosition;
    private Paint domainGridLinePaint;
    private Paint domainOriginLinePaint;
    private Paint domainSubGridLinePaint;
    private boolean drawGridOnTop;
    private boolean drawMarkersEnabled = true;
    private Paint gridBackgroundPaint = new Paint();
    private Insets gridInsets = new Insets();
    private RectF gridRect;
    private boolean isGridClippingEnabled = true;
    private RectF labelRect;
    private float lineExtensionBottom;
    private float lineExtensionLeft;
    private float lineExtensionRight;
    private float lineExtensionTop;
    private EnumSet<Edge> lineLabelEdges = EnumSet.noneOf(Edge.class);
    private Insets lineLabelInsets = new Insets();
    private Map<Edge, LineLabelRenderer> lineLabelRenderers = getDefaultLineLabelRenderers();
    private Map<Edge, LineLabelStyle> lineLabelStyles = getDefaultLineLabelStyles();
    private int linesPerDomainLabel = 1;
    private int linesPerRangeLabel = 1;
    private XYPlot plot;
    private Paint rangeCursorPaint;
    private Float rangeCursorPosition;
    private Paint rangeGridLinePaint;
    private Paint rangeOriginLinePaint;
    private Paint rangeSubGridLinePaint;
    private RenderStack<? extends XYSeries, ? extends XYSeriesFormatter> renderStack;

    public interface CursorLabelFormatter {
        Paint getBackgroundPaint();

        String getLabelText(Number number, Number number2);

        Paint getTextPaint();
    }

    public static class LineLabelRenderer {
        public void drawLabel(Canvas canvas, LineLabelStyle style, Number val, float x, float y, boolean isOrigin) {
            int canvasState = canvas.save();
            try {
                String txt = style.format.format(val);
                canvas.rotate(style.getRotation(), x, y);
                drawLabel(canvas, txt, style.getPaint(), x, y, isOrigin);
            } finally {
                canvas.restoreToCount(canvasState);
            }
        }

        /* access modifiers changed from: protected */
        public void drawLabel(Canvas canvas, String text, Paint paint, float x, float y, boolean isOrigin) {
            canvas.drawText(text, x, y, paint);
        }
    }

    public static class LineLabelStyle {
        /* access modifiers changed from: private */
        public Format format = new DecimalFormat("0.0");
        private Paint paint = new Paint();
        private float rotation = 0.0f;

        public LineLabelStyle() {
            this.paint.setColor(-3355444);
            this.paint.setAntiAlias(true);
            this.paint.setTextAlign(Paint.Align.CENTER);
            this.paint.setTextSize(XYGraphWidget.DEFAULT_LINE_LABEL_TEXT_SIZE_PX);
        }

        public Format getFormat() {
            return this.format;
        }

        public void setFormat(Format format2) {
            this.format = format2;
        }

        public float getRotation() {
            return this.rotation;
        }

        public void setRotation(float rotation2) {
            this.rotation = rotation2;
        }

        public Paint getPaint() {
            return this.paint;
        }

        public void setPaint(Paint paint2) {
            this.paint = paint2;
        }
    }

    public enum Edge {
        NONE(0),
        LEFT(1),
        RIGHT(2),
        TOP(4),
        BOTTOM(8);
        
        /* access modifiers changed from: private */
        public final int value;

        private Edge(int value2) {
            this.value = value2;
        }

        public int getValue() {
            return this.value;
        }
    }

    public XYGraphWidget(LayoutManager layoutManager, XYPlot plot2, Size size) {
        super(layoutManager, size);
        this.gridBackgroundPaint.setColor(Color.rgb(140, 140, 140));
        this.gridBackgroundPaint.setStyle(Paint.Style.FILL);
        Paint defaultLinePaint = new Paint();
        defaultLinePaint.setColor(Color.rgb(180, 180, 180));
        defaultLinePaint.setAntiAlias(true);
        defaultLinePaint.setStyle(Paint.Style.STROKE);
        this.rangeGridLinePaint = new Paint(defaultLinePaint);
        this.domainGridLinePaint = new Paint(defaultLinePaint);
        this.domainSubGridLinePaint = new Paint(defaultLinePaint);
        this.rangeSubGridLinePaint = new Paint(defaultLinePaint);
        this.domainOriginLinePaint = new Paint(defaultLinePaint);
        this.rangeOriginLinePaint = new Paint(defaultLinePaint);
        this.domainCursorPaint = new Paint();
        this.domainCursorPaint.setColor(InputDeviceCompat.SOURCE_ANY);
        this.rangeCursorPaint = new Paint();
        this.rangeCursorPaint.setColor(InputDeviceCompat.SOURCE_ANY);
        setMarginTop(7.0f);
        setMarginRight(4.0f);
        setMarginBottom(4.0f);
        setClippingEnabled(true);
        this.plot = plot2;
        this.renderStack = new RenderStack<>(plot2);
    }

    public void processAttrs(TypedArray attrs) {
        setDrawGridOnTop(attrs.getBoolean(R.styleable.xy_XYPlot_drawGridOnTop, isDrawGridOnTop()));
        int tlp = attrs.getInt(R.styleable.xy_XYPlot_lineLabels, 0);
        if (tlp != 0) {
            setLineLabelEdges(tlp);
        }
        setGridClippingEnabled(attrs.getBoolean(R.styleable.xy_XYPlot_gridClippingEnabled, isGridClippingEnabled()));
        LineLabelStyle lineLabelStyleTop = getLineLabelStyle(Edge.TOP);
        LineLabelStyle lineLabelStyleBottom = getLineLabelStyle(Edge.BOTTOM);
        LineLabelStyle lineLabelStyleLeft = getLineLabelStyle(Edge.LEFT);
        LineLabelStyle lineLabelStyleRight = getLineLabelStyle(Edge.RIGHT);
        lineLabelStyleTop.setRotation(attrs.getFloat(R.styleable.xy_XYPlot_lineLabelRotationTop, lineLabelStyleTop.getRotation()));
        lineLabelStyleBottom.setRotation(attrs.getFloat(R.styleable.xy_XYPlot_lineLabelRotationBottom, lineLabelStyleBottom.getRotation()));
        lineLabelStyleLeft.setRotation(attrs.getFloat(R.styleable.xy_XYPlot_lineLabelRotationLeft, lineLabelStyleLeft.getRotation()));
        lineLabelStyleRight.setRotation(attrs.getFloat(R.styleable.xy_XYPlot_lineLabelRotationRight, lineLabelStyleRight.getRotation()));
        setLineExtensionTop(attrs.getDimension(R.styleable.xy_XYPlot_lineExtensionTop, getLineExtensionTop()));
        setLineExtensionBottom(attrs.getDimension(R.styleable.xy_XYPlot_lineExtensionBottom, getLineExtensionBottom()));
        setLineExtensionLeft(attrs.getDimension(R.styleable.xy_XYPlot_lineExtensionLeft, getLineExtensionLeft()));
        setLineExtensionRight(attrs.getDimension(R.styleable.xy_XYPlot_lineExtensionRight, getLineExtensionRight()));
        AttrUtils.configureTextPaint(attrs, lineLabelStyleTop.getPaint(), R.styleable.xy_XYPlot_lineLabelTextColorTop, R.styleable.xy_XYPlot_lineLabelTextSizeTop, Integer.valueOf(R.styleable.xy_XYPlot_lineLabelAlignTop));
        AttrUtils.configureTextPaint(attrs, lineLabelStyleBottom.getPaint(), R.styleable.xy_XYPlot_lineLabelTextColorBottom, R.styleable.xy_XYPlot_lineLabelTextSizeBottom, Integer.valueOf(R.styleable.xy_XYPlot_lineLabelAlignBottom));
        AttrUtils.configureTextPaint(attrs, lineLabelStyleLeft.getPaint(), R.styleable.xy_XYPlot_lineLabelTextColorLeft, R.styleable.xy_XYPlot_lineLabelTextSizeLeft, Integer.valueOf(R.styleable.xy_XYPlot_lineLabelAlignLeft));
        AttrUtils.configureTextPaint(attrs, lineLabelStyleRight.getPaint(), R.styleable.xy_XYPlot_lineLabelTextColorRight, R.styleable.xy_XYPlot_lineLabelTextSizeRight, Integer.valueOf(R.styleable.xy_XYPlot_lineLabelAlignRight));
        AttrUtils.configureInsets(attrs, getGridInsets(), R.styleable.xy_XYPlot_gridInsetTop, R.styleable.xy_XYPlot_gridInsetBottom, R.styleable.xy_XYPlot_gridInsetLeft, R.styleable.xy_XYPlot_gridInsetRight);
        AttrUtils.configureInsets(attrs, getLineLabelInsets(), R.styleable.xy_XYPlot_lineLabelInsetTop, R.styleable.xy_XYPlot_lineLabelInsetBottom, R.styleable.xy_XYPlot_lineLabelInsetLeft, R.styleable.xy_XYPlot_lineLabelInsetRight);
        AttrUtils.configureWidget(attrs, this, R.styleable.xy_XYPlot_graphHeightMode, R.styleable.xy_XYPlot_graphHeight, R.styleable.xy_XYPlot_graphWidthMode, R.styleable.xy_XYPlot_graphWidth, R.styleable.xy_XYPlot_graphHorizontalPositioning, R.styleable.xy_XYPlot_graphHorizontalPosition, R.styleable.xy_XYPlot_graphVerticalPositioning, R.styleable.xy_XYPlot_graphVerticalPosition, R.styleable.xy_XYPlot_graphAnchor, R.styleable.xy_XYPlot_graphVisible);
        AttrUtils.configureWidget(attrs, this, R.styleable.xy_XYPlot_domainTitleHeightMode, R.styleable.xy_XYPlot_domainTitleHeight, R.styleable.xy_XYPlot_domainTitleWidthMode, R.styleable.xy_XYPlot_domainTitleWidth, R.styleable.xy_XYPlot_domainTitleHorizontalPositioning, R.styleable.xy_XYPlot_domainTitleHorizontalPosition, R.styleable.xy_XYPlot_domainTitleVerticalPositioning, R.styleable.xy_XYPlot_domainTitleVerticalPosition, R.styleable.xy_XYPlot_domainTitleAnchor, R.styleable.xy_XYPlot_domainTitleVisible);
        AttrUtils.configureWidget(attrs, this, R.styleable.xy_XYPlot_rangeTitleHeightMode, R.styleable.xy_XYPlot_rangeTitleHeight, R.styleable.xy_XYPlot_rangeTitleWidthMode, R.styleable.xy_XYPlot_rangeTitleWidth, R.styleable.xy_XYPlot_rangeTitleHorizontalPositioning, R.styleable.xy_XYPlot_rangeTitleHorizontalPosition, R.styleable.xy_XYPlot_rangeTitleVerticalPositioning, R.styleable.xy_XYPlot_rangeTitleVerticalPosition, R.styleable.xy_XYPlot_rangeTitleAnchor, R.styleable.xy_XYPlot_rangeTitleVisible);
        AttrUtils.configureWidgetRotation(attrs, this, R.styleable.xy_XYPlot_graphRotation);
        AttrUtils.configureBoxModelable(attrs, this, R.styleable.xy_XYPlot_graphMarginTop, R.styleable.xy_XYPlot_graphMarginBottom, R.styleable.xy_XYPlot_graphMarginLeft, R.styleable.xy_XYPlot_graphMarginRight, R.styleable.xy_XYPlot_graphPaddingTop, R.styleable.xy_XYPlot_graphPaddingBottom, R.styleable.xy_XYPlot_graphPaddingLeft, R.styleable.xy_XYPlot_graphPaddingRight);
        AttrUtils.configureLinePaint(attrs, getDomainOriginLinePaint(), R.styleable.xy_XYPlot_domainOriginLineColor, R.styleable.xy_XYPlot_domainOriginLineThickness);
        AttrUtils.configureLinePaint(attrs, getRangeOriginLinePaint(), R.styleable.xy_XYPlot_rangeOriginLineColor, R.styleable.xy_XYPlot_rangeOriginLineThickness);
        AttrUtils.configureLinePaint(attrs, getDomainGridLinePaint(), R.styleable.xy_XYPlot_domainLineColor, R.styleable.xy_XYPlot_domainLineThickness);
        AttrUtils.configureLinePaint(attrs, getRangeGridLinePaint(), R.styleable.xy_XYPlot_rangeLineColor, R.styleable.xy_XYPlot_rangeLineThickness);
        AttrUtils.setColor(attrs, getBackgroundPaint(), R.styleable.xy_XYPlot_graphBackgroundColor);
        AttrUtils.setColor(attrs, getGridBackgroundPaint(), R.styleable.xy_XYPlot_gridBackgroundColor);
    }

    /* access modifiers changed from: protected */
    public XYCoords screenToSeries(PointF point) {
        if (!this.plot.getBounds().isFullyDefined()) {
            return null;
        }
        return new RectRegion(this.gridRect).transform((Number) Float.valueOf(point.x), (Number) Float.valueOf(point.y), this.plot.getBounds(), false, true);
    }

    /* access modifiers changed from: protected */
    public Number screenToSeriesX(PointF point) {
        return screenToSeriesX(point.x);
    }

    /* access modifiers changed from: protected */
    public Number screenToSeriesY(PointF point) {
        return screenToSeriesY(point.y);
    }

    /* access modifiers changed from: protected */
    public Number screenToSeriesX(float xPix) {
        if (!this.plot.getBounds().xRegion.isDefined()) {
            return null;
        }
        return new Region(Float.valueOf(this.gridRect.left), Float.valueOf(this.gridRect.right)).transform((double) xPix, this.plot.getBounds().getxRegion());
    }

    /* access modifiers changed from: protected */
    public Number screenToSeriesY(float yPix) {
        if (!this.plot.getBounds().getyRegion().isDefined()) {
            return null;
        }
        return new Region(Float.valueOf(this.gridRect.top), Float.valueOf(this.gridRect.bottom)).transform((double) yPix, this.plot.getBounds().getyRegion(), true);
    }

    /* access modifiers changed from: protected */
    public PointF seriesToScreen(XYCoords xy) {
        if (!this.plot.getBounds().isFullyDefined()) {
            return null;
        }
        return this.plot.getBounds().transform(xy, this.gridRect, false, true);
    }

    /* access modifiers changed from: protected */
    public float seriesToScreenX(Number x) {
        return (float) this.plot.getBounds().getxRegion().transform(x.doubleValue(), (double) this.gridRect.left, (double) this.gridRect.right, false);
    }

    /* access modifiers changed from: protected */
    public float seriesToScreenY(Number y) {
        return (float) this.plot.getBounds().getyRegion().transform(y.doubleValue(), (double) this.gridRect.bottom, (double) this.gridRect.top, true);
    }

    /* access modifiers changed from: protected */
    public void onResize(@Nullable RectF oldRect, @NonNull RectF newRect) {
        recalculateSizes(newRect);
    }

    /* access modifiers changed from: protected */
    public void recalculateSizes(@Nullable RectF rect) {
        if (rect == null) {
            rect = getWidgetDimensions().paddedRect;
        }
        this.gridRect = RectFUtils.applyInsets(rect, this.gridInsets);
        this.labelRect = RectFUtils.applyInsets(rect, this.lineLabelInsets);
    }

    /* access modifiers changed from: protected */
    public void doOnDraw(Canvas canvas, RectF widgetRect) throws PlotRenderException {
        if (this.gridRect.height() > 0.0f && this.gridRect.width() > 0.0f) {
            RectRegion bounds = this.plot.getBounds();
            if (bounds.getMinX() != null && bounds.getMaxX() != null && bounds.getMinY() != null && bounds.getMaxY() != null) {
                if (this.drawGridOnTop) {
                    drawData(canvas);
                    drawGrid(canvas);
                } else {
                    drawGrid(canvas);
                    drawData(canvas);
                }
                drawCursors(canvas);
                if (isDrawMarkersEnabled()) {
                    drawMarkers(canvas);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawDomainLine(Canvas canvas, float xPix, Number xVal, Paint linePaint, boolean isOrigin) {
        if (linePaint != null) {
            canvas.drawLine(xPix, this.gridRect.top - this.lineExtensionTop, xPix, this.gridRect.bottom + this.lineExtensionBottom, linePaint);
        }
        drawLineLabel(canvas, Edge.TOP, xVal, xPix, this.labelRect.top, isOrigin);
        drawLineLabel(canvas, Edge.BOTTOM, xVal, xPix, this.labelRect.bottom, isOrigin);
    }

    /* access modifiers changed from: protected */
    public void drawRangeLine(Canvas canvas, float yPix, Number yVal, Paint linePaint, boolean isOrigin) {
        if (linePaint != null) {
            canvas.drawLine(this.gridRect.left - this.lineExtensionLeft, yPix, this.gridRect.right + this.lineExtensionRight, yPix, linePaint);
        }
        drawLineLabel(canvas, Edge.LEFT, yVal, this.labelRect.left, yPix, isOrigin);
        drawLineLabel(canvas, Edge.RIGHT, yVal, this.labelRect.right, yPix, isOrigin);
    }

    /* access modifiers changed from: protected */
    public void drawLineLabel(Canvas canvas, Edge edge, Number val, float x, float y, boolean isOrigin) {
        if (isLineLabelEnabled(edge)) {
            getLineLabelRenderer(edge).drawLabel(canvas, getLineLabelStyle(edge), val, x, y, isOrigin);
        }
    }

    /* access modifiers changed from: protected */
    public void drawGrid(Canvas canvas) {
        double domainOriginPix;
        double rangeOriginPix;
        Paint linePaint;
        Paint linePaint2;
        if (!this.drawGridOnTop) {
            drawGridBackground(canvas);
        }
        Number domainOrigin = this.plot.getDomainOrigin();
        if (domainOrigin != null) {
            domainOriginPix = this.plot.getBounds().getxRegion().transform(this.plot.getDomainOrigin().doubleValue(), (double) this.gridRect.left, (double) this.gridRect.right, false);
        } else {
            domainOriginPix = (double) this.gridRect.left;
            domainOrigin = this.plot.getBounds().getMinX();
        }
        Step domainStep = XYStepCalculator.getStep(this.plot, Axis.DOMAIN, this.gridRect);
        double domainStepPix = domainStep.getStepPix();
        double iMin = ((((double) this.gridRect.left) - domainOriginPix) - 9.999999747378752E-6d) / domainStepPix;
        double iMax = ((((double) this.gridRect.right) - domainOriginPix) + 9.999999747378752E-6d) / domainStepPix;
        int i = (int) Math.ceil(iMin);
        while (((double) i) <= iMax) {
            double xVal = domainOrigin.doubleValue() + (((double) i) * domainStep.getStepVal());
            double xPix = domainOriginPix + (((double) i) * domainStepPix);
            boolean isMajorTick = i % getLinesPerDomainLabel() == 0;
            boolean isOrigin = i == 0;
            if (isOrigin) {
                linePaint2 = this.domainOriginLinePaint;
            } else if (isMajorTick) {
                linePaint2 = this.domainGridLinePaint;
            } else {
                linePaint2 = this.domainSubGridLinePaint;
            }
            drawDomainLine(canvas, (float) xPix, Double.valueOf(xVal), linePaint2, isOrigin);
            i++;
        }
        Number rangeOrigin = this.plot.getRangeOrigin();
        if (rangeOrigin != null) {
            rangeOriginPix = this.plot.getBounds().getyRegion().transform(rangeOrigin.doubleValue(), (double) this.gridRect.top, (double) this.gridRect.bottom, true);
        } else {
            rangeOriginPix = (double) this.gridRect.bottom;
            rangeOrigin = this.plot.getBounds().getMinY();
        }
        Step rangeStep = XYStepCalculator.getStep(this.plot, Axis.RANGE, this.gridRect);
        double rangeStepPix = rangeStep.getStepPix();
        double kMin = ((((double) this.gridRect.top) - rangeOriginPix) - 9.999999747378752E-6d) / rangeStepPix;
        double kMax = ((((double) this.gridRect.bottom) - rangeOriginPix) + 9.999999747378752E-6d) / rangeStepPix;
        int k = (int) Math.ceil(kMin);
        while (((double) k) <= kMax) {
            double yVal = rangeOrigin.doubleValue() - (((double) k) * rangeStep.getStepVal());
            double yPix = rangeOriginPix + (((double) k) * rangeStepPix);
            boolean isMajorTick2 = k % getLinesPerRangeLabel() == 0;
            boolean isOrigin2 = k == 0;
            if (isOrigin2) {
                linePaint = this.rangeOriginLinePaint;
            } else if (isMajorTick2) {
                linePaint = this.rangeGridLinePaint;
            } else {
                linePaint = this.rangeSubGridLinePaint;
            }
            drawRangeLine(canvas, (float) yPix, Double.valueOf(yVal), linePaint, isOrigin2);
            k++;
        }
    }

    /* access modifiers changed from: protected */
    public void drawMarkerText(Canvas canvas, String text, ValueMarker marker, float x, float y) {
        if (marker.getText() != null) {
            RectF textRect = new RectF(FontUtils.getStringDimensions(text, marker.getTextPaint()));
            textRect.offsetTo(x + 2.0f, (y - 2.0f) - textRect.height());
            if (textRect.right > this.gridRect.right) {
                textRect.offset(-(textRect.right - this.gridRect.right), 0.0f);
            }
            if (textRect.top < this.gridRect.top) {
                textRect.offset(0.0f, this.gridRect.top - textRect.top);
            }
            canvas.drawText(text, textRect.left, textRect.bottom, marker.getTextPaint());
        }
    }

    /* access modifiers changed from: protected */
    public void drawMarkers(Canvas canvas) {
        if (this.plot.getYValueMarkers() != null && this.plot.getYValueMarkers().size() > 0) {
            for (YValueMarker marker : this.plot.getYValueMarkers()) {
                if (marker.getValue() != null) {
                    float yPix = (float) this.plot.getBounds().yRegion.transform(marker.getValue().doubleValue(), (double) this.gridRect.top, (double) this.gridRect.bottom, true);
                    canvas.drawLine(this.gridRect.left, yPix, this.gridRect.right, yPix, marker.getLinePaint());
                    Canvas canvas2 = canvas;
                    drawMarkerText(canvas2, marker.getText(), marker, ((HorizontalPosition) marker.getTextPosition()).getPixelValue(this.gridRect.width()) + this.gridRect.left, yPix);
                }
            }
        }
        if (this.plot.getXValueMarkers() != null && this.plot.getXValueMarkers().size() > 0) {
            for (XValueMarker marker2 : this.plot.getXValueMarkers()) {
                if (marker2.getValue() != null) {
                    float xPix = (float) this.plot.getBounds().xRegion.transform(marker2.getValue().doubleValue(), (double) this.gridRect.left, (double) this.gridRect.right, false);
                    canvas.drawLine(xPix, this.gridRect.top, xPix, this.gridRect.bottom, marker2.getLinePaint());
                    float yPix2 = ((VerticalPosition) marker2.getTextPosition()).getPixelValue(this.gridRect.height()) + this.gridRect.top;
                    if (marker2.getText() != null) {
                        drawMarkerText(canvas, marker2.getText(), marker2, xPix, yPix2);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawCursors(Canvas canvas) {
        boolean hasDomainCursor = false;
        if (this.domainCursorPaint != null && this.domainCursorPosition != null && this.domainCursorPosition.floatValue() <= this.gridRect.right && this.domainCursorPosition.floatValue() >= this.gridRect.left) {
            hasDomainCursor = true;
            canvas.drawLine(this.domainCursorPosition.floatValue(), this.gridRect.top, this.domainCursorPosition.floatValue(), this.gridRect.bottom, this.domainCursorPaint);
        }
        boolean hasRangeCursor = false;
        if (this.rangeCursorPaint != null && this.rangeCursorPosition != null && this.rangeCursorPosition.floatValue() >= this.gridRect.top && this.rangeCursorPosition.floatValue() <= this.gridRect.bottom) {
            hasRangeCursor = true;
            canvas.drawLine(this.gridRect.left, this.rangeCursorPosition.floatValue(), this.gridRect.right, this.rangeCursorPosition.floatValue(), this.rangeCursorPaint);
        }
        if (getCursorLabelFormatter() != null && hasRangeCursor && hasDomainCursor) {
            drawCursorLabel(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public void drawCursorLabel(Canvas canvas) {
        String label = getCursorLabelFormatter().getLabelText(getDomainCursorVal(), getRangeCursorVal());
        RectF cursorRect = new RectF(FontUtils.getPackedStringDimensions(label, getCursorLabelFormatter().getTextPaint()));
        cursorRect.offsetTo(this.domainCursorPosition.floatValue(), this.rangeCursorPosition.floatValue() - cursorRect.height());
        if (cursorRect.right >= this.gridRect.right) {
            cursorRect.offsetTo(this.domainCursorPosition.floatValue() - cursorRect.width(), cursorRect.top);
        }
        if (cursorRect.top <= this.gridRect.top) {
            cursorRect.offsetTo(cursorRect.left, this.rangeCursorPosition.floatValue());
        }
        if (getCursorLabelFormatter().getBackgroundPaint() != null) {
            canvas.drawRect(cursorRect, getCursorLabelFormatter().getBackgroundPaint());
        }
        canvas.drawText(label, cursorRect.left, cursorRect.bottom, getCursorLabelFormatter().getTextPaint());
    }

    /* access modifiers changed from: protected */
    public void drawGridBackground(Canvas canvas) {
        if (this.gridBackgroundPaint != null) {
            canvas.drawRect(this.gridRect, this.gridBackgroundPaint);
        }
    }

    /* access modifiers changed from: protected */
    public void drawData(Canvas canvas) throws PlotRenderException {
        if (this.drawGridOnTop) {
            drawGridBackground(canvas);
        }
        try {
            if (this.isGridClippingEnabled) {
                canvas.save();
                canvas.clipRect(this.gridRect, Region.Op.INTERSECT);
            }
            this.renderStack.sync();
            Iterator<RenderStack.StackElement<? extends XYSeries, ? extends XYSeriesFormatter>> it = this.renderStack.getElements().iterator();
            while (it.hasNext()) {
                RenderStack.StackElement thisElement = it.next();
                if (thisElement.isEnabled()) {
                    ((XYSeriesRenderer) this.plot.getRenderer(thisElement.get().getFormatter().getRendererClass())).render(canvas, this.gridRect, thisElement.get(), this.renderStack);
                }
            }
        } finally {
            if (this.isGridClippingEnabled) {
                canvas.restore();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawPoint(Canvas canvas, PointF point, Paint paint) {
        canvas.drawPoint(point.x, point.y, paint);
    }

    public Paint getGridBackgroundPaint() {
        return this.gridBackgroundPaint;
    }

    public void setGridBackgroundPaint(Paint gridBackgroundPaint2) {
        this.gridBackgroundPaint = gridBackgroundPaint2;
    }

    public Paint getDomainGridLinePaint() {
        return this.domainGridLinePaint;
    }

    public void setDomainGridLinePaint(Paint gridLinePaint) {
        this.domainGridLinePaint = gridLinePaint;
    }

    public Paint getRangeGridLinePaint() {
        return this.rangeGridLinePaint;
    }

    public Paint getDomainSubGridLinePaint() {
        return this.domainSubGridLinePaint;
    }

    public void setDomainSubGridLinePaint(Paint gridLinePaint) {
        this.domainSubGridLinePaint = gridLinePaint;
    }

    public void setRangeGridLinePaint(Paint gridLinePaint) {
        this.rangeGridLinePaint = gridLinePaint;
    }

    public Paint getRangeSubGridLinePaint() {
        return this.rangeSubGridLinePaint;
    }

    public void setRangeSubGridLinePaint(Paint gridLinePaint) {
        this.rangeSubGridLinePaint = gridLinePaint;
    }

    public int getLinesPerRangeLabel() {
        return this.linesPerRangeLabel;
    }

    public void setLinesPerRangeLabel(int linesPerRangeLabel2) {
        this.linesPerRangeLabel = linesPerRangeLabel2;
    }

    public int getLinesPerDomainLabel() {
        return this.linesPerDomainLabel;
    }

    public void setLinesPerDomainLabel(int linesPerDomainLabel2) {
        this.linesPerDomainLabel = linesPerDomainLabel2;
    }

    public Paint getDomainOriginLinePaint() {
        return this.domainOriginLinePaint;
    }

    public void setDomainOriginLinePaint(Paint domainOriginLinePaint2) {
        this.domainOriginLinePaint = domainOriginLinePaint2;
    }

    public Paint getRangeOriginLinePaint() {
        return this.rangeOriginLinePaint;
    }

    public void setRangeOriginLinePaint(Paint rangeOriginLinePaint2) {
        this.rangeOriginLinePaint = rangeOriginLinePaint2;
    }

    public void setCursorPosition(Float x, Float y) {
        setDomainCursorPosition(x);
        setRangeCursorPosition(y);
    }

    public void setCursorPosition(PointF point) {
        setCursorPosition(Float.valueOf(point.x), Float.valueOf(point.y));
    }

    public Float getDomainCursorPosition() {
        return this.domainCursorPosition;
    }

    public Number getDomainCursorVal() {
        return screenToSeriesX(getDomainCursorPosition().floatValue());
    }

    public void setDomainCursorPosition(Float domainCursorPosition2) {
        this.domainCursorPosition = domainCursorPosition2;
    }

    public Float getRangeCursorPosition() {
        return this.rangeCursorPosition;
    }

    public Number getRangeCursorVal() {
        return screenToSeriesY(getRangeCursorPosition().floatValue());
    }

    public void setRangeCursorPosition(Float rangeCursorPosition2) {
        this.rangeCursorPosition = rangeCursorPosition2;
    }

    public boolean isDrawGridOnTop() {
        return this.drawGridOnTop;
    }

    public void setDrawGridOnTop(boolean drawGridOnTop2) {
        this.drawGridOnTop = drawGridOnTop2;
    }

    public boolean isDrawMarkersEnabled() {
        return this.drawMarkersEnabled;
    }

    public void setDrawMarkersEnabled(boolean drawMarkersEnabled2) {
        this.drawMarkersEnabled = drawMarkersEnabled2;
    }

    public Paint getDomainCursorPaint() {
        return this.domainCursorPaint;
    }

    public void setDomainCursorPaint(Paint domainCursorPaint2) {
        this.domainCursorPaint = domainCursorPaint2;
    }

    public Paint getRangeCursorPaint() {
        return this.rangeCursorPaint;
    }

    public void setRangeCursorPaint(Paint rangeCursorPaint2) {
        this.rangeCursorPaint = rangeCursorPaint2;
    }

    public float getLineExtensionTop() {
        return this.lineExtensionTop;
    }

    public void setLineExtensionTop(float lineExtensionTop2) {
        this.lineExtensionTop = lineExtensionTop2;
    }

    public float getLineExtensionBottom() {
        return this.lineExtensionBottom;
    }

    public void setLineExtensionBottom(float lineExtensionBottom2) {
        this.lineExtensionBottom = lineExtensionBottom2;
    }

    public float getLineExtensionLeft() {
        return this.lineExtensionLeft;
    }

    public void setLineExtensionLeft(float lineExtensionLeft2) {
        this.lineExtensionLeft = lineExtensionLeft2;
    }

    public float getLineExtensionRight() {
        return this.lineExtensionRight;
    }

    public void setLineExtensionRight(float lineExtensionRight2) {
        this.lineExtensionRight = lineExtensionRight2;
    }

    /* access modifiers changed from: protected */
    public Map<Edge, LineLabelStyle> getDefaultLineLabelStyles() {
        EnumMap<Edge, LineLabelStyle> defaults = new EnumMap<>(Edge.class);
        defaults.put(Edge.TOP, new LineLabelStyle());
        defaults.put(Edge.BOTTOM, new LineLabelStyle());
        defaults.put(Edge.LEFT, new LineLabelStyle());
        defaults.put(Edge.RIGHT, new LineLabelStyle());
        return defaults;
    }

    /* access modifiers changed from: protected */
    public Map<Edge, LineLabelRenderer> getDefaultLineLabelRenderers() {
        EnumMap<Edge, LineLabelRenderer> defaults = new EnumMap<>(Edge.class);
        defaults.put(Edge.TOP, new LineLabelRenderer());
        defaults.put(Edge.BOTTOM, new LineLabelRenderer());
        defaults.put(Edge.LEFT, new LineLabelRenderer());
        defaults.put(Edge.RIGHT, new LineLabelRenderer());
        return defaults;
    }

    public LineLabelRenderer getLineLabelRenderer(Edge edge) {
        return this.lineLabelRenderers.get(edge);
    }

    public void setLineLabelRenderer(Edge edge, LineLabelRenderer renderer) {
        this.lineLabelRenderers.put(edge, renderer);
    }

    public LineLabelStyle getLineLabelStyle(Edge edge) {
        return this.lineLabelStyles.get(edge);
    }

    public void setLineLabelStyle(Edge edge, LineLabelStyle style) {
        this.lineLabelStyles.put(edge, style);
    }

    public CursorLabelFormatter getCursorLabelFormatter() {
        return this.cursorLabelFormatter;
    }

    public void setCursorLabelFormatter(CursorLabelFormatter cursorLabelFormatter2) {
        this.cursorLabelFormatter = cursorLabelFormatter2;
    }

    public Insets getGridInsets() {
        return this.gridInsets;
    }

    public void setGridInsets(Insets gridInsets2) {
        this.gridInsets = gridInsets2;
        recalculateSizes((RectF) null);
    }

    public Insets getLineLabelInsets() {
        return this.lineLabelInsets;
    }

    public void setLineLabelInsets(Insets lineLabelInsets2) {
        this.lineLabelInsets = lineLabelInsets2;
        recalculateSizes((RectF) null);
    }

    public RectF getGridRect() {
        return this.gridRect;
    }

    public void setGridRect(RectF gridRect2) {
        this.gridRect = gridRect2;
    }

    public RectF getLabelRect() {
        return this.labelRect;
    }

    public void setLabelRect(RectF labelRect2) {
        this.labelRect = labelRect2;
    }

    public boolean isGridClippingEnabled() {
        return this.isGridClippingEnabled;
    }

    public void setGridClippingEnabled(boolean gridClippingEnabled) {
        this.isGridClippingEnabled = gridClippingEnabled;
    }

    public boolean isLineLabelEnabled(Edge position) {
        return this.lineLabelEdges.contains(position);
    }

    public void setLineLabelEdges(Edge... positions) {
        EnumSet<Edge> positionSet = EnumSet.noneOf(Edge.class);
        if (positions != null) {
            Collections.addAll(positionSet, positions);
        }
        this.lineLabelEdges = positionSet;
    }

    public void setLineLabelEdges(Collection<Edge> positions) {
        this.lineLabelEdges = EnumSet.copyOf(positions);
    }

    /* access modifiers changed from: protected */
    public void setLineLabelEdges(int bitfield) {
        for (Edge tp : Edge.values()) {
            if ((tp.value & bitfield) == tp.value) {
                this.lineLabelEdges.add(tp);
            }
        }
    }

    public boolean containsPoint(float x, float y) {
        if (this.gridRect != null) {
            return this.gridRect.contains(x, y);
        }
        return false;
    }
}
