package com.androidplot.xy;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import com.androidplot.Plot;
import com.androidplot.R;
import com.androidplot.ui.Anchor;
import com.androidplot.ui.DynamicTableModel;
import com.androidplot.ui.HorizontalPositioning;
import com.androidplot.ui.Size;
import com.androidplot.ui.SizeMode;
import com.androidplot.ui.TextOrientation;
import com.androidplot.ui.VerticalPositioning;
import com.androidplot.ui.widget.TextLabelWidget;
import com.androidplot.util.AttrUtils;
import com.androidplot.util.PixelUtils;
import com.androidplot.util.SeriesUtils;
import com.androidplot.xy.CandlestickSeries;
import com.androidplot.xy.SimpleXYSeries;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XYPlot extends Plot<XYSeries, XYSeriesFormatter, XYSeriesRenderer, XYSeriesBundle, XYSeriesRegistry> {
    private static final int DEFAULT_DOMAIN_LABEL_WIDGET_H_DP = 10;
    private static final int DEFAULT_DOMAIN_LABEL_WIDGET_W_DP = 80;
    private static final int DEFAULT_DOMAIN_LABEL_WIDGET_X_OFFSET_DP = 20;
    private static final int DEFAULT_DOMAIN_LABEL_WIDGET_Y_OFFSET_DP = 0;
    private static final int DEFAULT_GRAPH_WIDGET_H_DP = 18;
    private static final int DEFAULT_GRAPH_WIDGET_W_DP = 10;
    private static final int DEFAULT_GRAPH_WIDGET_X_OFFSET_DP = 0;
    private static final int DEFAULT_GRAPH_WIDGET_Y_OFFSET_DP = 0;
    private static final int DEFAULT_LEGEND_WIDGET_H_DP = 10;
    private static final int DEFAULT_LEGEND_WIDGET_ICON_SIZE_DP = 7;
    private static final int DEFAULT_LEGEND_WIDGET_X_OFFSET_DP = 40;
    private static final int DEFAULT_LEGEND_WIDGET_Y_OFFSET_DP = 0;
    private static final int DEFAULT_PLOT_BOTTOM_MARGIN_DP = 1;
    private static final int DEFAULT_PLOT_LEFT_MARGIN_DP = 1;
    private static final int DEFAULT_PLOT_RIGHT_MARGIN_DP = 1;
    private static final int DEFAULT_PLOT_TOP_MARGIN_DP = 1;
    private static final int DEFAULT_RANGE_LABEL_WIDGET_H_DP = 50;
    private static final int DEFAULT_RANGE_LABEL_WIDGET_W_DP = 10;
    private static final int DEFAULT_RANGE_LABEL_WIDGET_X_OFFSET_DP = 0;
    private static final int DEFAULT_RANGE_LABEL_WIDGET_Y_OFFSET_DP = 0;
    private RectRegion bounds = RectRegion.withDefaults(new RectRegion(-1, 1, -1, 1));
    private XYCoords calculatedOrigin = new XYCoords();
    private XYConstraints constraints = new XYConstraints();
    private BoundaryMode domainOriginBoundaryMode;
    private Number domainOriginExtent = null;
    private StepModel domainStepModel;
    private TextLabelWidget domainTitle;
    private XYGraphWidget graph;
    private final RectRegion innerLimits = new RectRegion();
    private XYLegendWidget legend;
    private final RectRegion outerLimits = new RectRegion();
    private Number prevMaxX;
    private Number prevMaxY;
    private Number prevMinX;
    private Number prevMinY;
    private PreviewMode previewMode;
    private BoundaryMode rangeOriginBoundaryMode;
    private Number rangeOriginExtent = null;
    private StepModel rangeStepModel;
    private TextLabelWidget rangeTitle;
    private Number userDomainOrigin;
    private Number userRangeOrigin;
    private ArrayList<XValueMarker> xValueMarkers;
    private ArrayList<YValueMarker> yValueMarkers;

    public enum PreviewMode {
        LineAndPoint,
        Candlestick,
        Bar
    }

    public XYPlot(Context context, String title) {
        super(context, title);
    }

    public XYPlot(Context context, String title, Plot.RenderMode mode) {
        super(context, title, mode);
    }

    public XYPlot(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public XYPlot(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* access modifiers changed from: protected */
    public void onPreInit() {
        this.legend = new XYLegendWidget(getLayoutManager(), this, new Size(PixelUtils.dpToPix(10.0f), SizeMode.ABSOLUTE, 0.5f, SizeMode.RELATIVE), new DynamicTableModel(0, 1), new Size(PixelUtils.dpToPix(7.0f), SizeMode.ABSOLUTE, PixelUtils.dpToPix(7.0f), SizeMode.ABSOLUTE));
        this.graph = new XYGraphWidget(getLayoutManager(), this, new Size(PixelUtils.dpToPix(18.0f), SizeMode.FILL, PixelUtils.dpToPix(10.0f), SizeMode.FILL));
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(-12303292);
        backgroundPaint.setStyle(Paint.Style.FILL);
        this.graph.setBackgroundPaint(backgroundPaint);
        this.domainTitle = new TextLabelWidget(getLayoutManager(), new Size(PixelUtils.dpToPix(10.0f), SizeMode.ABSOLUTE, PixelUtils.dpToPix(80.0f), SizeMode.ABSOLUTE), TextOrientation.HORIZONTAL);
        this.rangeTitle = new TextLabelWidget(getLayoutManager(), new Size(PixelUtils.dpToPix(50.0f), SizeMode.ABSOLUTE, PixelUtils.dpToPix(10.0f), SizeMode.ABSOLUTE), TextOrientation.VERTICAL_ASCENDING);
        this.legend.position(PixelUtils.dpToPix(40.0f), HorizontalPositioning.ABSOLUTE_FROM_RIGHT, PixelUtils.dpToPix(0.0f), VerticalPositioning.ABSOLUTE_FROM_BOTTOM, Anchor.RIGHT_BOTTOM);
        this.graph.position(PixelUtils.dpToPix(0.0f), HorizontalPositioning.ABSOLUTE_FROM_RIGHT, PixelUtils.dpToPix(0.0f), VerticalPositioning.ABSOLUTE_FROM_CENTER, Anchor.RIGHT_MIDDLE);
        this.domainTitle.position(PixelUtils.dpToPix(20.0f), HorizontalPositioning.ABSOLUTE_FROM_LEFT, PixelUtils.dpToPix(0.0f), VerticalPositioning.ABSOLUTE_FROM_BOTTOM, Anchor.LEFT_BOTTOM);
        this.rangeTitle.position(PixelUtils.dpToPix(0.0f), HorizontalPositioning.ABSOLUTE_FROM_LEFT, PixelUtils.dpToPix(0.0f), VerticalPositioning.ABSOLUTE_FROM_CENTER, Anchor.LEFT_MIDDLE);
        getLayoutManager().moveToTop(getTitle());
        getLayoutManager().moveToTop(getLegend());
        getDomainTitle().pack();
        getRangeTitle().pack();
        setPlotMarginLeft(PixelUtils.dpToPix(1.0f));
        setPlotMarginRight(PixelUtils.dpToPix(1.0f));
        setPlotMarginTop(PixelUtils.dpToPix(1.0f));
        setPlotMarginBottom(PixelUtils.dpToPix(1.0f));
        this.xValueMarkers = new ArrayList<>();
        this.yValueMarkers = new ArrayList<>();
        this.domainStepModel = new StepModel(StepMode.SUBDIVIDE, 10.0d);
        this.rangeStepModel = new StepModel(StepMode.SUBDIVIDE, 10.0d);
    }

    /* access modifiers changed from: protected */
    public void onAfterConfig() {
        if (isInEditMode()) {
            switch (this.previewMode) {
                case LineAndPoint:
                    addSeries(new SimpleXYSeries((List<? extends Number>) Arrays.asList(new Integer[]{1, 2, 3, 3, 4}), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Red"), new LineAndPointFormatter(Integer.valueOf(SupportMenu.CATEGORY_MASK), (Integer) null, (Integer) null, (PointLabelFormatter) null));
                    addSeries(new SimpleXYSeries((List<? extends Number>) Arrays.asList(new Integer[]{2, 1, 4, 2, 5}), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Green"), new LineAndPointFormatter(-16711936, (Integer) null, (Integer) null, (PointLabelFormatter) null));
                    addSeries(new SimpleXYSeries((List<? extends Number>) Arrays.asList(new Integer[]{3, 3, 2, 3, 3}), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Blue"), new LineAndPointFormatter(-16776961, (Integer) null, (Integer) null, (PointLabelFormatter) null));
                    return;
                case Candlestick:
                    CandlestickMaker.make(this, new CandlestickFormatter(), new CandlestickSeries(new CandlestickSeries.Item(1.0d, 10.0d, 2.0d, 9.0d), new CandlestickSeries.Item(4.0d, 18.0d, 6.0d, 5.0d), new CandlestickSeries.Item(3.0d, 11.0d, 5.0d, 10.0d), new CandlestickSeries.Item(2.0d, 17.0d, 2.0d, 15.0d), new CandlestickSeries.Item(6.0d, 11.0d, 11.0d, 7.0d), new CandlestickSeries.Item(8.0d, 16.0d, 10.0d, 15.0d)));
                    return;
                case Bar:
                    throw new UnsupportedOperationException("Not yet implemented.");
                default:
                    throw new UnsupportedOperationException("Unexpected preview mode: " + this.previewMode);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void processAttrs(TypedArray attrs) {
        this.previewMode = PreviewMode.values()[attrs.getInt(R.styleable.xy_XYPlot_previewMode, PreviewMode.LineAndPoint.ordinal())];
        String domainLabelAttr = attrs.getString(R.styleable.xy_XYPlot_domainTitle);
        if (domainLabelAttr != null) {
            getDomainTitle().setText(domainLabelAttr);
        }
        String rangeLabelAttr = attrs.getString(R.styleable.xy_XYPlot_rangeTitle);
        if (rangeLabelAttr != null) {
            getRangeTitle().setText(rangeLabelAttr);
        }
        AttrUtils.configureStep(attrs, getDomainStepModel(), R.styleable.xy_XYPlot_domainStepMode, R.styleable.xy_XYPlot_domainStep);
        AttrUtils.configureStep(attrs, getRangeStepModel(), R.styleable.xy_XYPlot_rangeStepMode, R.styleable.xy_XYPlot_rangeStep);
        AttrUtils.configureTextPaint(attrs, getDomainTitle().getLabelPaint(), R.styleable.xy_XYPlot_domainTitleTextColor, R.styleable.xy_XYPlot_domainTitleTextSize);
        AttrUtils.configureTextPaint(attrs, getRangeTitle().getLabelPaint(), R.styleable.xy_XYPlot_rangeTitleTextColor, R.styleable.xy_XYPlot_rangeTitleTextSize);
        AttrUtils.configureTextPaint(attrs, getLegend().getTextPaint(), R.styleable.xy_XYPlot_legendTextColor, R.styleable.xy_XYPlot_legendTextSize);
        AttrUtils.configureSize(attrs, getLegend().getIconSize(), R.styleable.xy_XYPlot_legendIconHeightMode, R.styleable.xy_XYPlot_legendIconHeight, R.styleable.xy_XYPlot_legendIconWidthMode, R.styleable.xy_XYPlot_legendIconWidth);
        AttrUtils.configureWidget(attrs, getLegend(), R.styleable.xy_XYPlot_legendHeightMode, R.styleable.xy_XYPlot_legendHeight, R.styleable.xy_XYPlot_legendWidthMode, R.styleable.xy_XYPlot_legendWidth, R.styleable.xy_XYPlot_legendHorizontalPositioning, R.styleable.xy_XYPlot_legendHorizontalPosition, R.styleable.xy_XYPlot_legendVerticalPositioning, R.styleable.xy_XYPlot_legendVerticalPosition, R.styleable.xy_XYPlot_legendAnchor, R.styleable.xy_XYPlot_legendVisible);
        getGraph().processAttrs(attrs);
    }

    /* access modifiers changed from: protected */
    public void notifyListenersBeforeDraw(Canvas canvas) {
        super.notifyListenersBeforeDraw(canvas);
        calculateMinMaxVals();
        ((XYSeriesRegistry) getRegistry()).estimate(this);
    }

    public boolean containsPoint(float x, float y) {
        return getGraph().containsPoint(x, y);
    }

    public boolean containsPoint(PointF point) {
        return containsPoint(point.x, point.y);
    }

    public void setCursorPosition(PointF point) {
        getGraph().setCursorPosition(point);
    }

    public void setCursorPosition(float x, float y) {
        getGraph().setCursorPosition(Float.valueOf(x), Float.valueOf(y));
    }

    @Deprecated
    public Number getXVal(float xPix) {
        return getGraph().screenToSeriesX(xPix);
    }

    public Number getYVal(float yPix) {
        return getGraph().screenToSeriesY(yPix);
    }

    @Deprecated
    public Number getYVal(PointF point) {
        return getGraph().screenToSeriesY(point);
    }

    @Deprecated
    public Number getXVal(PointF point) {
        return getGraph().screenToSeriesX(point);
    }

    public Number screenToSeriesX(float x) {
        return getGraph().screenToSeriesX(x);
    }

    public Number screenToSeriesY(float y) {
        return getGraph().screenToSeriesY(y);
    }

    public float seriesToScreenX(Number x) {
        return getGraph().seriesToScreenX(x);
    }

    public float seriesToScreenY(Number y) {
        return getGraph().seriesToScreenY(y);
    }

    public PointF seriesToScreen(XYCoords xy) {
        return getGraph().seriesToScreen(xy);
    }

    public XYCoords screentoSeries(PointF point) {
        return getGraph().screenToSeries(point);
    }

    public void calculateMinMaxVals() {
        Number number;
        Number number2;
        Number minX;
        Number minY;
        Number number3 = null;
        this.prevMinX = this.bounds.isMinXSet() ? this.bounds.getMinX() : null;
        if (this.bounds.isMaxXSet()) {
            number = this.bounds.getMaxX();
        } else {
            number = null;
        }
        this.prevMaxX = number;
        if (this.bounds.isMinYSet()) {
            number2 = this.bounds.getMinY();
        } else {
            number2 = null;
        }
        this.prevMinY = number2;
        if (this.bounds.isMaxYSet()) {
            number3 = this.bounds.getMaxY();
        }
        this.prevMaxY = number3;
        this.bounds.setMinX(this.constraints.getMinX());
        this.bounds.setMaxX(this.constraints.getMaxX());
        this.bounds.setMinY(this.constraints.getMinY());
        this.bounds.setMaxY(this.constraints.getMaxY());
        if (!this.bounds.isFullyDefined()) {
            RectRegion b = SeriesUtils.minMax(this.constraints, (List<XYSeries>) ((XYSeriesRegistry) getRegistry()).getSeriesList());
            if (!this.bounds.isMinXSet()) {
                this.bounds.setMinX(b.getMinX());
            }
            if (!this.bounds.isMaxXSet()) {
                this.bounds.setMaxX(b.getMaxX());
            }
            if (!this.bounds.isMinYSet()) {
                this.bounds.setMinY(b.getMinY());
            }
            if (!this.bounds.isMaxYSet()) {
                this.bounds.setMaxY(b.getMaxY());
            }
        }
        switch (this.constraints.getDomainFramingModel()) {
            case ORIGIN:
                updateDomainMinMaxForOriginModel();
                break;
            case EDGE:
                this.bounds.setMaxX(applyUserMinMax(getCalculatedUpperBoundary(this.constraints.getDomainUpperBoundaryMode(), this.prevMaxX, this.bounds.getMaxX()), this.innerLimits.getMaxX(), this.outerLimits.getMaxX()));
                this.bounds.setMinX(applyUserMinMax(getCalculatedLowerBoundary(this.constraints.getDomainLowerBoundaryMode(), this.prevMinX, this.bounds.getMinX()), this.outerLimits.getMinX(), this.innerLimits.getMinX()));
                break;
            default:
                throw new UnsupportedOperationException("Domain Framing Model not yet supported: " + this.constraints.getDomainFramingModel());
        }
        switch (this.constraints.getRangeFramingModel()) {
            case ORIGIN:
                updateRangeMinMaxForOriginModel();
                break;
            case EDGE:
                if (((XYSeriesRegistry) getRegistry()).size() > 0) {
                    this.bounds.setMaxY(applyUserMinMax(getCalculatedUpperBoundary(this.constraints.getRangeUpperBoundaryMode(), this.prevMaxY, this.bounds.getMaxY()), this.innerLimits.getMaxY(), this.outerLimits.getMaxY()));
                    this.bounds.setMinY(applyUserMinMax(getCalculatedLowerBoundary(this.constraints.getRangeLowerBoundaryMode(), this.prevMinY, this.bounds.getMinY()), this.outerLimits.getMinY(), this.innerLimits.getMinY()));
                    break;
                }
                break;
            default:
                throw new UnsupportedOperationException("Range Framing Model not yet supported: " + this.constraints.getRangeFramingModel());
        }
        XYCoords xYCoords = this.calculatedOrigin;
        if (this.userDomainOrigin != null) {
            minX = this.userDomainOrigin;
        } else {
            minX = this.bounds.getMinX();
        }
        xYCoords.x = minX;
        XYCoords xYCoords2 = this.calculatedOrigin;
        if (this.userRangeOrigin != null) {
            minY = this.userRangeOrigin;
        } else {
            minY = this.bounds.getMinY();
        }
        xYCoords2.y = minY;
    }

    /* access modifiers changed from: protected */
    public Number getCalculatedUpperBoundary(BoundaryMode mode, Number previousMax, Number calculatedMax) {
        switch (mode) {
            case FIXED:
            case AUTO:
                return calculatedMax;
            case GROW:
                if (previousMax == null || calculatedMax.doubleValue() > previousMax.doubleValue()) {
                    return calculatedMax;
                }
                return previousMax;
            case SHRINK:
                if (previousMax == null || calculatedMax.doubleValue() < previousMax.doubleValue()) {
                    return calculatedMax;
                }
                return previousMax;
            default:
                throw new UnsupportedOperationException("BoundaryMode not supported: " + mode);
        }
    }

    /* access modifiers changed from: protected */
    public Number getCalculatedLowerBoundary(BoundaryMode mode, Number previousMin, Number calculatedMin) {
        switch (mode) {
            case FIXED:
            case AUTO:
                break;
            case GROW:
                if (previousMin != null && calculatedMin.doubleValue() >= previousMin.doubleValue()) {
                    return previousMin;
                }
            case SHRINK:
                if (previousMin != null && calculatedMin.doubleValue() <= previousMin.doubleValue()) {
                    return previousMin;
                }
            default:
                throw new UnsupportedOperationException("BoundaryMode not supported: " + mode);
        }
        return calculatedMin;
    }

    private static Number applyUserMinMax(Number value, Number min, Number max) {
        if (!(min == null || value == null || value.doubleValue() > min.doubleValue())) {
            value = min;
        }
        if (max == null || value == null || value.doubleValue() < max.doubleValue()) {
            return value;
        }
        return max;
    }

    public void centerOnDomainOrigin(@NonNull Number origin) {
        centerOnDomainOrigin(origin, (Number) null, BoundaryMode.AUTO);
    }

    public void centerOnDomainOrigin(@NonNull Number origin, Number extent, BoundaryMode mode) {
        if (origin == null) {
            throw new IllegalArgumentException("Origin param cannot be null.");
        }
        this.constraints.setDomainFramingModel(XYFramingModel.ORIGIN);
        setUserDomainOrigin(origin);
        this.domainOriginExtent = extent;
        this.domainOriginBoundaryMode = mode;
        Number[] minMax = getOriginMinMax(this.domainOriginBoundaryMode, this.userDomainOrigin, this.domainOriginExtent);
        this.constraints.setMinX(minMax[0]);
        this.constraints.setMaxX(minMax[1]);
    }

    public void centerOnRangeOrigin(@NonNull Number origin) {
        centerOnRangeOrigin(origin, (Number) null, BoundaryMode.AUTO);
    }

    public void centerOnRangeOrigin(@NonNull Number origin, Number extent, BoundaryMode mode) {
        if (origin == null) {
            throw new IllegalArgumentException("Origin param cannot be null.");
        }
        this.constraints.setRangeFramingModel(XYFramingModel.ORIGIN);
        setUserRangeOrigin(origin);
        this.rangeOriginExtent = extent;
        this.rangeOriginBoundaryMode = mode;
        Number[] minMax = getOriginMinMax(this.rangeOriginBoundaryMode, this.userRangeOrigin, this.rangeOriginExtent);
        this.constraints.setMinY(minMax[0]);
        this.constraints.setMaxY(minMax[1]);
    }

    /* access modifiers changed from: protected */
    public Number[] getOriginMinMax(BoundaryMode mode, Number origin, Number extent) {
        if (mode == BoundaryMode.FIXED) {
            double o = origin.doubleValue();
            double e = extent.doubleValue();
            return new Number[]{Double.valueOf(o - e), Double.valueOf(o + e)};
        }
        return new Number[]{null, null};
    }

    private static double distance(double x, double y) {
        if (x > y) {
            return x - y;
        }
        return y - x;
    }

    public void updateDomainMinMaxForOriginModel() {
        double delta;
        double origin = this.userDomainOrigin.doubleValue();
        double maxDelta = distance(this.bounds.getMaxX().doubleValue(), origin);
        double minDelta = distance(this.bounds.getMinX().doubleValue(), origin);
        if (maxDelta > minDelta) {
            delta = maxDelta;
        } else {
            delta = minDelta;
        }
        double lowerBoundary = origin - delta;
        double upperBoundary = origin + delta;
        switch (this.domainOriginBoundaryMode) {
            case FIXED:
                return;
            case AUTO:
                this.bounds.setMinX(Double.valueOf(lowerBoundary));
                this.bounds.setMaxX(Double.valueOf(upperBoundary));
                return;
            case GROW:
                if (this.prevMinX == null || lowerBoundary < this.prevMinX.doubleValue()) {
                    this.bounds.setMinX(Double.valueOf(lowerBoundary));
                } else {
                    this.bounds.setMinX(this.prevMinX);
                }
                if (this.prevMaxX == null || upperBoundary > this.prevMaxX.doubleValue()) {
                    this.bounds.setMaxX(Double.valueOf(upperBoundary));
                    return;
                } else {
                    this.bounds.setMaxX(this.prevMaxX);
                    return;
                }
            case SHRINK:
                if (this.prevMinX == null || lowerBoundary > this.prevMinX.doubleValue()) {
                    this.bounds.setMinX(Double.valueOf(lowerBoundary));
                } else {
                    this.bounds.setMinX(this.prevMinX);
                }
                if (this.prevMaxX == null || upperBoundary < this.prevMaxX.doubleValue()) {
                    this.bounds.setMaxX(Double.valueOf(upperBoundary));
                    return;
                } else {
                    this.bounds.setMaxX(this.prevMaxX);
                    return;
                }
            default:
                throw new UnsupportedOperationException("Domain Origin Boundary Mode not yet supported: " + this.domainOriginBoundaryMode);
        }
    }

    public void updateRangeMinMaxForOriginModel() {
        switch (this.rangeOriginBoundaryMode) {
            case AUTO:
                double origin = this.userRangeOrigin.doubleValue();
                double maxDelta = distance(this.bounds.getMaxY().doubleValue(), origin);
                double minDelta = distance(this.bounds.getMinY().doubleValue(), origin);
                if (maxDelta > minDelta) {
                    this.bounds.setMinY(Double.valueOf(origin - maxDelta));
                    this.bounds.setMaxY(Double.valueOf(origin + maxDelta));
                    return;
                }
                this.bounds.setMinY(Double.valueOf(origin - minDelta));
                this.bounds.setMaxY(Double.valueOf(origin + minDelta));
                return;
            default:
                throw new UnsupportedOperationException("Range Origin Boundary Mode not yet supported: " + this.rangeOriginBoundaryMode);
        }
    }

    public int getLinesPerRangeLabel() {
        return this.graph.getLinesPerRangeLabel();
    }

    public void setLinesPerRangeLabel(int linesPerLabel) {
        this.graph.setLinesPerRangeLabel(linesPerLabel);
    }

    public int getLinesPerDomainLabel() {
        return this.graph.getLinesPerDomainLabel();
    }

    public void setLinesPerDomainLabel(int linesPerDomainLabel) {
        this.graph.setLinesPerDomainLabel(linesPerDomainLabel);
    }

    public StepMode getDomainStepMode() {
        return this.domainStepModel.getMode();
    }

    public void setDomainStepMode(StepMode domainStepMode) {
        this.domainStepModel.setMode(domainStepMode);
    }

    public double getDomainStepValue() {
        return this.domainStepModel.getValue();
    }

    public void setDomainStepValue(double domainStepValue) {
        this.domainStepModel.setValue(domainStepValue);
    }

    public void setDomainStep(StepMode mode, double value) {
        setDomainStepMode(mode);
        setDomainStepValue(value);
    }

    public StepMode getRangeStepMode() {
        return this.rangeStepModel.getMode();
    }

    public void setRangeStepMode(StepMode rangeStepMode) {
        this.rangeStepModel.setMode(rangeStepMode);
    }

    public double getRangeStepValue() {
        return this.rangeStepModel.getValue();
    }

    public void setRangeStepValue(double rangeStepValue) {
        this.rangeStepModel.setValue(rangeStepValue);
    }

    public void setRangeStep(StepMode mode, double value) {
        setRangeStepMode(mode);
        setRangeStepValue(value);
    }

    public XYLegendWidget getLegend() {
        return this.legend;
    }

    public void setLegend(XYLegendWidget legend2) {
        this.legend = legend2;
    }

    public XYGraphWidget getGraph() {
        return this.graph;
    }

    public void setGraph(XYGraphWidget graph2) {
        this.graph = graph2;
    }

    public TextLabelWidget getDomainTitle() {
        return this.domainTitle;
    }

    public void setDomainTitle(TextLabelWidget domainTitle2) {
        this.domainTitle = domainTitle2;
    }

    public void setDomainLabel(String domainLabel) {
        getDomainTitle().setText(domainLabel);
    }

    public TextLabelWidget getRangeTitle() {
        return this.rangeTitle;
    }

    public void setRangeTitle(TextLabelWidget rangeTitle2) {
        this.rangeTitle = rangeTitle2;
    }

    public void setRangeLabel(String rangeLabel) {
        getRangeTitle().setText(rangeLabel);
    }

    public synchronized void setDomainBoundaries(Number lowerBoundary, Number upperBoundary, BoundaryMode mode) {
        setDomainBoundaries(lowerBoundary, mode, upperBoundary, mode);
    }

    public synchronized void setDomainBoundaries(Number lowerBoundary, BoundaryMode lowerBoundaryMode, Number upperBoundary, BoundaryMode upperBoundaryMode) {
        setDomainLowerBoundary(lowerBoundary, lowerBoundaryMode);
        setDomainUpperBoundary(upperBoundary, upperBoundaryMode);
    }

    public synchronized void setRangeBoundaries(Number lowerBoundary, Number upperBoundary, BoundaryMode mode) {
        setRangeBoundaries(lowerBoundary, mode, upperBoundary, mode);
    }

    public synchronized void setRangeBoundaries(Number lowerBoundary, BoundaryMode lowerBoundaryMode, Number upperBoundary, BoundaryMode upperBoundaryMode) {
        setRangeLowerBoundary(lowerBoundary, lowerBoundaryMode);
        setRangeUpperBoundary(upperBoundary, upperBoundaryMode);
    }

    /* access modifiers changed from: protected */
    public synchronized void setDomainUpperBoundaryMode(BoundaryMode mode) {
        this.constraints.setDomainUpperBoundaryMode(mode);
    }

    /* access modifiers changed from: protected */
    public synchronized void setUserMaxX(Number maxX) {
        this.constraints.setMaxX(maxX);
    }

    public synchronized void setDomainUpperBoundary(Number boundary, BoundaryMode mode) {
        if (mode != BoundaryMode.FIXED) {
            boundary = null;
        }
        setUserMaxX(boundary);
        setDomainUpperBoundaryMode(mode);
        setDomainFramingModel(XYFramingModel.EDGE);
    }

    /* access modifiers changed from: protected */
    public synchronized void setDomainLowerBoundaryMode(BoundaryMode mode) {
        this.constraints.setDomainLowerBoundaryMode(mode);
    }

    /* access modifiers changed from: protected */
    public synchronized void setUserMinX(Number minX) {
        this.constraints.setMinX(minX);
    }

    public synchronized void setDomainLowerBoundary(Number boundary, BoundaryMode mode) {
        if (mode != BoundaryMode.FIXED) {
            boundary = null;
        }
        setUserMinX(boundary);
        setDomainLowerBoundaryMode(mode);
        setDomainFramingModel(XYFramingModel.EDGE);
    }

    /* access modifiers changed from: protected */
    public synchronized void setRangeUpperBoundaryMode(BoundaryMode mode) {
        this.constraints.setRangeUpperBoundaryMode(mode);
    }

    /* access modifiers changed from: protected */
    public synchronized void setUserMaxY(Number maxY) {
        this.constraints.setMaxY(maxY);
    }

    public synchronized void setRangeUpperBoundary(Number boundary, BoundaryMode mode) {
        if (mode != BoundaryMode.FIXED) {
            boundary = null;
        }
        setUserMaxY(boundary);
        setRangeUpperBoundaryMode(mode);
        setRangeFramingModel(XYFramingModel.EDGE);
    }

    /* access modifiers changed from: protected */
    public synchronized void setRangeLowerBoundaryMode(BoundaryMode mode) {
        this.constraints.setRangeLowerBoundaryMode(mode);
    }

    /* access modifiers changed from: protected */
    public synchronized void setUserMinY(Number minY) {
        this.constraints.setMinY(minY);
    }

    public synchronized void setRangeLowerBoundary(Number boundary, BoundaryMode mode) {
        if (mode != BoundaryMode.FIXED) {
            boundary = null;
        }
        setUserMinY(boundary);
        setRangeLowerBoundaryMode(mode);
        setRangeFramingModel(XYFramingModel.EDGE);
    }

    public XYCoords getOrigin() {
        return this.calculatedOrigin;
    }

    public Number getDomainOrigin() {
        return this.calculatedOrigin.x;
    }

    public Number getRangeOrigin() {
        return this.calculatedOrigin.y;
    }

    public synchronized void setUserDomainOrigin(Number origin) {
        if (origin == null) {
            throw new NullPointerException("Origin value cannot be null.");
        }
        this.userDomainOrigin = origin;
    }

    public synchronized void setUserRangeOrigin(Number origin) {
        if (origin == null) {
            throw new NullPointerException("Origin value cannot be null.");
        }
        this.userRangeOrigin = origin;
    }

    /* access modifiers changed from: protected */
    public void setDomainFramingModel(@NonNull XYFramingModel model) {
        this.constraints.setDomainFramingModel(model);
    }

    /* access modifiers changed from: protected */
    public void setRangeFramingModel(@NonNull XYFramingModel model) {
        this.constraints.setRangeFramingModel(model);
    }

    public RectRegion getBounds() {
        return this.bounds;
    }

    public boolean addMarker(YValueMarker marker) {
        if (this.yValueMarkers.contains(marker)) {
            return false;
        }
        return this.yValueMarkers.add(marker);
    }

    public YValueMarker removeMarker(YValueMarker marker) {
        int markerIndex = this.yValueMarkers.indexOf(marker);
        if (markerIndex == -1) {
            return null;
        }
        return this.yValueMarkers.remove(markerIndex);
    }

    public int removeMarkers() {
        return removeXMarkers() + removeYMarkers();
    }

    public int removeYMarkers() {
        int numMarkersRemoved = this.yValueMarkers.size();
        this.yValueMarkers.clear();
        return numMarkersRemoved;
    }

    public boolean addMarker(XValueMarker marker) {
        return !this.xValueMarkers.contains(marker) && this.xValueMarkers.add(marker);
    }

    public XValueMarker removeMarker(XValueMarker marker) {
        int markerIndex = this.xValueMarkers.indexOf(marker);
        if (markerIndex == -1) {
            return null;
        }
        return this.xValueMarkers.remove(markerIndex);
    }

    public int removeXMarkers() {
        int numMarkersRemoved = this.xValueMarkers.size();
        this.xValueMarkers.clear();
        return numMarkersRemoved;
    }

    /* access modifiers changed from: protected */
    public List<YValueMarker> getYValueMarkers() {
        return this.yValueMarkers;
    }

    /* access modifiers changed from: protected */
    public List<XValueMarker> getXValueMarkers() {
        return this.xValueMarkers;
    }

    public RectRegion getInnerLimits() {
        return this.innerLimits;
    }

    public RectRegion getOuterLimits() {
        return this.outerLimits;
    }

    public StepModel getDomainStepModel() {
        return this.domainStepModel;
    }

    public void setDomainStepModel(StepModel domainStepModel2) {
        this.domainStepModel = domainStepModel2;
    }

    public StepModel getRangeStepModel() {
        return this.rangeStepModel;
    }

    public void setRangeStepModel(StepModel rangeStepModel2) {
        this.rangeStepModel = rangeStepModel2;
    }

    /* access modifiers changed from: protected */
    public XYSeriesRegistry getRegistryInstance() {
        return new XYSeriesRegistry();
    }
}
