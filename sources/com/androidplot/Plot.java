package com.androidplot;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.androidplot.R;
import com.androidplot.Series;
import com.androidplot.SeriesRegistry;
import com.androidplot.exception.PlotRenderException;
import com.androidplot.ui.Anchor;
import com.androidplot.ui.BoxModel;
import com.androidplot.ui.Formatter;
import com.androidplot.ui.HorizontalPositioning;
import com.androidplot.ui.LayoutManager;
import com.androidplot.ui.Resizable;
import com.androidplot.ui.SeriesBundle;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.ui.Size;
import com.androidplot.ui.SizeMode;
import com.androidplot.ui.TextOrientation;
import com.androidplot.ui.VerticalPositioning;
import com.androidplot.ui.widget.TextLabelWidget;
import com.androidplot.util.AttrUtils;
import com.androidplot.util.DisplayDimensions;
import com.androidplot.util.PixelUtils;
import com.halfhp.fig.Fig;
import com.halfhp.fig.FigException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class Plot<SeriesType extends Series, FormatterType extends Formatter, RendererType extends SeriesRenderer, BundleType extends SeriesBundle<SeriesType, FormatterType>, RegistryType extends SeriesRegistry<BundleType, SeriesType, FormatterType>> extends View implements Resizable {
    private static final String BASE_PACKAGE = "com.androidplot.";
    private static final int DEFAULT_TITLE_WIDGET_TEXT_SIZE_SP = 10;
    private static final String TAG = Plot.class.getName();
    private static final String XML_ATTR_PREFIX = "androidplot";
    private Paint backgroundPaint;
    private Paint borderPaint;
    private float borderRadiusX;
    private float borderRadiusY;
    private BorderStyle borderStyle;
    private BoxModel boxModel;
    private DisplayDimensions displayDims;
    /* access modifiers changed from: private */
    public boolean isIdle;
    /* access modifiers changed from: private */
    public boolean keepRunning;
    private LayoutManager layoutManager;
    private final ArrayList<PlotListener> listeners;
    /* access modifiers changed from: private */
    public final BufferedCanvas pingPong;
    private RegistryType registry;
    private RenderMode renderMode;
    /* access modifiers changed from: private */
    public final Object renderSync;
    private Thread renderThread;
    private HashMap<Class<? extends RendererType>, RendererType> renderers;
    private TextLabelWidget title;

    public enum BorderStyle {
        ROUNDED,
        SQUARE,
        NONE
    }

    public enum RenderMode {
        USE_BACKGROUND_THREAD,
        USE_MAIN_THREAD
    }

    /* access modifiers changed from: protected */
    public abstract RegistryType getRegistryInstance();

    /* access modifiers changed from: protected */
    public abstract void processAttrs(TypedArray typedArray);

    public DisplayDimensions getDisplayDimensions() {
        return this.displayDims;
    }

    public HashMap<Class<? extends RendererType>, RendererType> getRenderers() {
        return this.renderers;
    }

    public RegistryType getRegistry() {
        return this.registry;
    }

    public void setRegistry(RegistryType registry2) {
        this.registry = registry2;
        for (BundleType bundle : registry2.getSeriesAndFormatterList()) {
            attachSeries(bundle.getSeries(), bundle.getFormatter());
        }
    }

    public TextLabelWidget getTitle() {
        return this.title;
    }

    public void setTitle(TextLabelWidget title2) {
        this.title = title2;
    }

    public void setTitle(String title2) {
        getTitle().setText(title2);
    }

    private static class BufferedCanvas {
        private volatile Bitmap bgBuffer;
        private Canvas canvas;
        private volatile Bitmap fgBuffer;

        private BufferedCanvas() {
            this.canvas = new Canvas();
        }

        public synchronized void swap() {
            Bitmap tmp = this.bgBuffer;
            this.bgBuffer = this.fgBuffer;
            this.fgBuffer = tmp;
        }

        public synchronized void resize(int h, int w) {
            if (w <= 0 || h <= 0) {
                this.bgBuffer = null;
                this.fgBuffer = null;
            } else {
                this.bgBuffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
                this.fgBuffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
            }
        }

        public void recycle() {
            this.bgBuffer.recycle();
            this.bgBuffer = null;
            this.fgBuffer.recycle();
            this.fgBuffer = null;
            System.gc();
        }

        public synchronized Canvas getCanvas() {
            Canvas canvas2;
            if (this.bgBuffer != null) {
                this.canvas.setBitmap(this.bgBuffer);
                canvas2 = this.canvas;
            } else {
                canvas2 = null;
            }
            return canvas2;
        }

        public Bitmap getBitmap() {
            return this.fgBuffer;
        }
    }

    public Plot(Context context, String title2) {
        this(context, title2, RenderMode.USE_MAIN_THREAD);
    }

    public Plot(Context context, String title2, RenderMode mode) {
        super(context);
        this.boxModel = new BoxModel();
        this.borderStyle = BorderStyle.NONE;
        this.borderRadiusX = 15.0f;
        this.borderRadiusY = 15.0f;
        this.displayDims = new DisplayDimensions();
        this.renderMode = RenderMode.USE_MAIN_THREAD;
        this.pingPong = new BufferedCanvas();
        this.renderSync = new Object();
        this.keepRunning = false;
        this.isIdle = true;
        this.listeners = new ArrayList<>();
        this.registry = getRegistryInstance();
        this.renderers = new HashMap<>();
        this.borderPaint = new Paint();
        this.borderPaint.setColor(Color.rgb(150, 150, 150));
        this.borderPaint.setStyle(Paint.Style.STROKE);
        this.borderPaint.setStrokeWidth(1.0f);
        this.borderPaint.setAntiAlias(true);
        this.backgroundPaint = new Paint();
        this.backgroundPaint.setColor(-12303292);
        this.backgroundPaint.setStyle(Paint.Style.FILL);
        this.renderMode = mode;
        init(context, (AttributeSet) null, 0);
        getTitle().setText(title2);
    }

    public Plot(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.boxModel = new BoxModel();
        this.borderStyle = BorderStyle.NONE;
        this.borderRadiusX = 15.0f;
        this.borderRadiusY = 15.0f;
        this.displayDims = new DisplayDimensions();
        this.renderMode = RenderMode.USE_MAIN_THREAD;
        this.pingPong = new BufferedCanvas();
        this.renderSync = new Object();
        this.keepRunning = false;
        this.isIdle = true;
        this.listeners = new ArrayList<>();
        this.registry = getRegistryInstance();
        this.renderers = new HashMap<>();
        this.borderPaint = new Paint();
        this.borderPaint.setColor(Color.rgb(150, 150, 150));
        this.borderPaint.setStyle(Paint.Style.STROKE);
        this.borderPaint.setStrokeWidth(1.0f);
        this.borderPaint.setAntiAlias(true);
        this.backgroundPaint = new Paint();
        this.backgroundPaint.setColor(-12303292);
        this.backgroundPaint.setStyle(Paint.Style.FILL);
        init(context, attrs, 0);
    }

    public Plot(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.boxModel = new BoxModel();
        this.borderStyle = BorderStyle.NONE;
        this.borderRadiusX = 15.0f;
        this.borderRadiusY = 15.0f;
        this.displayDims = new DisplayDimensions();
        this.renderMode = RenderMode.USE_MAIN_THREAD;
        this.pingPong = new BufferedCanvas();
        this.renderSync = new Object();
        this.keepRunning = false;
        this.isIdle = true;
        this.listeners = new ArrayList<>();
        this.registry = getRegistryInstance();
        this.renderers = new HashMap<>();
        this.borderPaint = new Paint();
        this.borderPaint.setColor(Color.rgb(150, 150, 150));
        this.borderPaint.setStyle(Paint.Style.STROKE);
        this.borderPaint.setStrokeWidth(1.0f);
        this.borderPaint.setAntiAlias(true);
        this.backgroundPaint = new Paint();
        this.backgroundPaint.setColor(-12303292);
        this.backgroundPaint.setStyle(Paint.Style.FILL);
        init(context, attrs, defStyle);
    }

    /* access modifiers changed from: protected */
    public boolean isHwAccelerationSupported() {
        return false;
    }

    public void setRenderMode(RenderMode mode) {
        this.renderMode = mode;
    }

    /* access modifiers changed from: protected */
    public void onPreInit() {
    }

    /* access modifiers changed from: protected */
    public void onAfterConfig() {
    }

    /* access modifiers changed from: protected */
    public final void init(Context context, AttributeSet attrs, int defStyle) {
        PixelUtils.init(context);
        this.layoutManager = new LayoutManager();
        this.title = new TextLabelWidget(this.layoutManager, new Size(25.0f, SizeMode.ABSOLUTE, 100.0f, SizeMode.ABSOLUTE), TextOrientation.HORIZONTAL);
        this.title.position(0.0f, HorizontalPositioning.RELATIVE_TO_CENTER, 0.0f, VerticalPositioning.ABSOLUTE_FROM_TOP, Anchor.TOP_MIDDLE);
        this.title.getLabelPaint().setTextSize(PixelUtils.spToPix(10.0f));
        onPreInit();
        this.layoutManager.moveToTop(this.title);
        if (!(context == null || attrs == null)) {
            loadAttrs(attrs, defStyle);
        }
        onAfterConfig();
        this.layoutManager.onPostInit();
        if (this.renderMode == RenderMode.USE_BACKGROUND_THREAD) {
            this.renderThread = new Thread(new Runnable() {
                public void run() {
                    boolean unused = Plot.this.keepRunning = true;
                    while (Plot.this.keepRunning) {
                        boolean unused2 = Plot.this.isIdle = false;
                        synchronized (Plot.this.pingPong) {
                            Plot.this.renderOnCanvas(Plot.this.pingPong.getCanvas());
                            Plot.this.pingPong.swap();
                        }
                        synchronized (Plot.this.renderSync) {
                            Plot.this.postInvalidate();
                            if (Plot.this.keepRunning) {
                                try {
                                    Plot.this.renderSync.wait();
                                } catch (InterruptedException e) {
                                    boolean unused3 = Plot.this.keepRunning = false;
                                }
                            }
                        }
                    }
                    Plot.this.pingPong.recycle();
                    return;
                }
            }, "Androidplot renderThread");
        }
    }

    private void processBaseAttrs(TypedArray attrs) {
        setMarkupEnabled(attrs.getBoolean(R.styleable.Plot_markupEnabled, false));
        RenderMode renderMode2 = RenderMode.values()[attrs.getInt(R.styleable.Plot_renderMode, getRenderMode().ordinal())];
        if (renderMode2 != getRenderMode()) {
            setRenderMode(renderMode2);
        }
        AttrUtils.configureBoxModelable(attrs, this.boxModel, R.styleable.Plot_marginTop, R.styleable.Plot_marginBottom, R.styleable.Plot_marginLeft, R.styleable.Plot_marginRight, R.styleable.Plot_paddingTop, R.styleable.Plot_paddingBottom, R.styleable.Plot_paddingLeft, R.styleable.Plot_paddingRight);
        getTitle().setText(attrs.getString(R.styleable.Plot_title));
        getTitle().getLabelPaint().setTextSize(attrs.getDimension(R.styleable.Plot_titleTextSize, PixelUtils.spToPix(10.0f)));
        getTitle().getLabelPaint().setColor(attrs.getColor(R.styleable.Plot_titleTextColor, getTitle().getLabelPaint().getColor()));
        getBackgroundPaint().setColor(attrs.getColor(R.styleable.Plot_backgroundColor, getBackgroundPaint().getColor()));
        AttrUtils.configureLinePaint(attrs, getBorderPaint(), R.styleable.Plot_borderColor, R.styleable.Plot_borderThickness);
    }

    private void loadAttrs(AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            Field styleableFieldInR = null;
            TypedArray typedAttrs = null;
            Class styleableClass = R.styleable.class;
            String styleableName = getClass().getName().substring(BASE_PACKAGE.length()).replace('.', '_');
            try {
                styleableFieldInR = styleableClass.getField(styleableName);
            } catch (NoSuchFieldException e) {
                Log.d(TAG, "Styleable definition not found for: " + styleableName);
            }
            if (styleableFieldInR != null) {
                try {
                    typedAttrs = getContext().obtainStyledAttributes(attrs, (int[]) styleableFieldInR.get((Object) null), defStyle, 0);
                    if (typedAttrs != null) {
                        processAttrs(typedAttrs);
                        typedAttrs.recycle();
                    }
                } catch (IllegalAccessException e2) {
                    if (typedAttrs != null) {
                        processAttrs(typedAttrs);
                        typedAttrs.recycle();
                    }
                } catch (Throwable th) {
                    if (typedAttrs != null) {
                        processAttrs(typedAttrs);
                        typedAttrs.recycle();
                    }
                    throw th;
                }
            }
            try {
                Field styleableFieldInR2 = styleableClass.getField(Plot.class.getSimpleName());
                if (styleableFieldInR2 != null) {
                    typedAttrs = getContext().obtainStyledAttributes(attrs, (int[]) styleableFieldInR2.get((Object) null), defStyle, 0);
                }
                if (typedAttrs != null) {
                    processBaseAttrs(typedAttrs);
                    typedAttrs.recycle();
                }
            } catch (IllegalAccessException e3) {
                if (typedAttrs != null) {
                    processBaseAttrs(typedAttrs);
                    typedAttrs.recycle();
                }
            } catch (NoSuchFieldException e4) {
                Log.d(TAG, "Styleable definition not found for: " + Plot.class.getSimpleName());
                if (typedAttrs != null) {
                    processBaseAttrs(typedAttrs);
                    typedAttrs.recycle();
                }
            } catch (Throwable th2) {
                if (typedAttrs != null) {
                    processBaseAttrs(typedAttrs);
                    typedAttrs.recycle();
                }
                throw th2;
            }
            HashMap<String, String> attrHash = new HashMap<>();
            for (int i = 0; i < attrs.getAttributeCount(); i++) {
                String attrName = attrs.getAttributeName(i);
                if (attrName != null && attrName.toUpperCase().startsWith(XML_ATTR_PREFIX.toUpperCase())) {
                    attrHash.put(attrName.substring(XML_ATTR_PREFIX.length() + 1), attrs.getAttributeValue(i));
                }
            }
            try {
                Fig.configure(getContext(), (Object) this, attrHash);
            } catch (FigException e5) {
                throw new RuntimeException(e5);
            }
        }
    }

    public RenderMode getRenderMode() {
        return this.renderMode;
    }

    public synchronized boolean addListener(PlotListener listener) {
        return !this.listeners.contains(listener) && this.listeners.add(listener);
    }

    public synchronized boolean removeListener(PlotListener listener) {
        return this.listeners.remove(listener);
    }

    /* access modifiers changed from: protected */
    public ArrayList<PlotListener> getListeners() {
        return this.listeners;
    }

    /* access modifiers changed from: protected */
    public void notifyListenersBeforeDraw(Canvas canvas) {
        Iterator<PlotListener> it = this.listeners.iterator();
        while (it.hasNext()) {
            it.next().onBeforeDraw(this, canvas);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyListenersAfterDraw(Canvas canvas) {
        Iterator<PlotListener> it = this.listeners.iterator();
        while (it.hasNext()) {
            it.next().onAfterDraw(this, canvas);
        }
    }

    public synchronized boolean addSeries(FormatterType formatter, SeriesType... series) {
        boolean z = false;
        synchronized (this) {
            int length = series.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    z = true;
                    break;
                } else if (!addSeries(series[i], formatter)) {
                    break;
                } else {
                    i++;
                }
            }
        }
        return z;
    }

    public synchronized boolean addSeries(SeriesType series, FormatterType formatter) {
        boolean result;
        result = getRegistry().add(series, formatter);
        attachSeries(series, formatter);
        return result;
    }

    /* access modifiers changed from: protected */
    public void attachSeries(SeriesType series, FormatterType formatter) {
        Class rendererClass = formatter.getRendererClass();
        if (!getRenderers().containsKey(rendererClass)) {
            getRenderers().put(rendererClass, formatter.getRendererInstance(this));
        }
        if (series instanceof PlotListener) {
            addListener((PlotListener) series);
        }
    }

    /* access modifiers changed from: protected */
    public SeriesBundle<SeriesType, FormatterType> getSeries(SeriesType series, Class<? extends RendererType> rendererClass) {
        for (SeriesBundle<SeriesType, FormatterType> thisPair : getSeries(series)) {
            if (thisPair.getFormatter().getRendererClass() == rendererClass) {
                return thisPair;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public List<SeriesBundle<SeriesType, FormatterType>> getSeries(SeriesType series) {
        return getRegistry().get(series);
    }

    public synchronized boolean removeSeries(SeriesType series, Class<? extends RendererType> rendererClass) {
        boolean z = true;
        synchronized (this) {
            if (getRegistry().remove(series, rendererClass).size() != 1 || !(series instanceof PlotListener)) {
                z = false;
            } else {
                removeListener((PlotListener) series);
            }
        }
        return z;
    }

    public synchronized void removeSeries(SeriesType series) {
        if (series instanceof PlotListener) {
            removeListener((PlotListener) series);
        }
        getRegistry().remove(series);
    }

    public void clear() {
        for (SeriesType series : getRegistry().getSeriesList()) {
            if (series instanceof PlotListener) {
                removeListener((PlotListener) series);
            }
        }
        getRegistry().clear();
    }

    public boolean isEmpty() {
        return getRegistry().isEmpty();
    }

    public FormatterType getFormatter(SeriesType series, Class<? extends RendererType> rendererClass) {
        return getSeries(series, rendererClass).getFormatter();
    }

    public <T extends RendererType> T getRenderer(Class<T> rendererClass) {
        return (SeriesRenderer) getRenderers().get(rendererClass);
    }

    public List<RendererType> getRendererList() {
        return new ArrayList(getRenderers().values());
    }

    public void setMarkupEnabled(boolean enabled) {
        this.layoutManager.setMarkupEnabled(enabled);
    }

    public void redraw() {
        if (this.renderMode == RenderMode.USE_BACKGROUND_THREAD) {
            if (this.isIdle) {
                synchronized (this.renderSync) {
                    this.renderSync.notify();
                }
            }
        } else if (this.renderMode != RenderMode.USE_MAIN_THREAD) {
            throw new IllegalArgumentException("Unsupported Render Mode: " + this.renderMode);
        } else if (Looper.myLooper() == Looper.getMainLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public synchronized void layout(DisplayDimensions dims) {
        this.displayDims = dims;
        this.layoutManager.layout(this.displayDims);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        synchronized (this.renderSync) {
            this.keepRunning = false;
            this.renderSync.notify();
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void onSizeChanged(int w, int h, int oldw, int oldh) {
        PixelUtils.init(getContext());
        if (Build.VERSION.SDK_INT >= 11 && !isHwAccelerationSupported() && isHardwareAccelerated()) {
            setLayerType(1, (Paint) null);
        }
        if (this.renderMode == RenderMode.USE_BACKGROUND_THREAD) {
            this.pingPong.resize(h, w);
        }
        RectF cRect = new RectF(0.0f, 0.0f, (float) w, (float) h);
        RectF mRect = this.boxModel.getMarginatedRect(cRect);
        layout(new DisplayDimensions(cRect, mRect, this.boxModel.getPaddedRect(mRect)));
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.renderThread != null && !this.renderThread.isAlive()) {
            this.renderThread.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.renderMode == RenderMode.USE_BACKGROUND_THREAD) {
            synchronized (this.pingPong) {
                Bitmap bmp = this.pingPong.getBitmap();
                if (bmp != null) {
                    canvas.drawBitmap(bmp, 0.0f, 0.0f, (Paint) null);
                }
            }
        } else if (this.renderMode == RenderMode.USE_MAIN_THREAD) {
            renderOnCanvas(canvas);
        } else {
            throw new IllegalArgumentException("Unsupported Render Mode: " + this.renderMode);
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void renderOnCanvas(@Nullable Canvas canvas) {
        if (canvas != null) {
            notifyListenersBeforeDraw(canvas);
            try {
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                if (this.backgroundPaint != null) {
                    drawBackground(canvas, this.displayDims.marginatedRect);
                }
                this.layoutManager.draw(canvas);
                if (getBorderPaint() != null) {
                    drawBorder(canvas, this.displayDims.marginatedRect);
                }
            } catch (PlotRenderException e) {
                Log.e(TAG, "Exception while rendering Plot.", e);
            } catch (Exception e2) {
                Log.e(TAG, "Exception while rendering Plot.", e2);
            }
            this.isIdle = true;
            notifyListenersAfterDraw(canvas);
        }
    }

    public void setBorderStyle(BorderStyle style, Float radiusX, Float radiusY) {
        if (style == BorderStyle.ROUNDED) {
            if (radiusX == null || radiusY == null) {
                throw new IllegalArgumentException("radiusX and radiusY cannot be null when using BorderStyle.ROUNDED");
            }
            this.borderRadiusX = radiusX.floatValue();
            this.borderRadiusY = radiusY.floatValue();
        }
        this.borderStyle = style;
    }

    /* access modifiers changed from: protected */
    public void drawBorder(Canvas canvas, RectF dims) {
        drawRect(canvas, dims, this.borderPaint);
    }

    /* access modifiers changed from: protected */
    public void drawBackground(Canvas canvas, RectF dims) {
        drawRect(canvas, dims, this.backgroundPaint);
    }

    /* access modifiers changed from: protected */
    public void drawRect(Canvas canvas, RectF dims, Paint paint) {
        switch (this.borderStyle) {
            case ROUNDED:
                canvas.drawRoundRect(dims, this.borderRadiusX, this.borderRadiusY, paint);
                return;
            default:
                canvas.drawRect(dims, paint);
                return;
        }
    }

    public LayoutManager getLayoutManager() {
        return this.layoutManager;
    }

    public void setLayoutManager(LayoutManager layoutManager2) {
        this.layoutManager = layoutManager2;
    }

    public Paint getBackgroundPaint() {
        return this.backgroundPaint;
    }

    public void setBackgroundPaint(Paint backgroundPaint2) {
        this.backgroundPaint = backgroundPaint2;
    }

    public void setPlotMargins(float left, float top, float right, float bottom) {
        setPlotMarginLeft(left);
        setPlotMarginTop(top);
        setPlotMarginRight(right);
        setPlotMarginBottom(bottom);
    }

    public void setPlotPadding(float left, float top, float right, float bottom) {
        setPlotPaddingLeft(left);
        setPlotPaddingTop(top);
        setPlotPaddingRight(right);
        setPlotPaddingBottom(bottom);
    }

    public float getPlotMarginTop() {
        return this.boxModel.getMarginTop();
    }

    public void setPlotMarginTop(float plotMarginTop) {
        this.boxModel.setMarginTop(plotMarginTop);
    }

    public float getPlotMarginBottom() {
        return this.boxModel.getMarginBottom();
    }

    public void setPlotMarginBottom(float plotMarginBottom) {
        this.boxModel.setMarginBottom(plotMarginBottom);
    }

    public float getPlotMarginLeft() {
        return this.boxModel.getMarginLeft();
    }

    public void setPlotMarginLeft(float plotMarginLeft) {
        this.boxModel.setMarginLeft(plotMarginLeft);
    }

    public float getPlotMarginRight() {
        return this.boxModel.getMarginRight();
    }

    public void setPlotMarginRight(float plotMarginRight) {
        this.boxModel.setMarginRight(plotMarginRight);
    }

    public float getPlotPaddingTop() {
        return this.boxModel.getPaddingTop();
    }

    public void setPlotPaddingTop(float plotPaddingTop) {
        this.boxModel.setPaddingTop(plotPaddingTop);
    }

    public float getPlotPaddingBottom() {
        return this.boxModel.getPaddingBottom();
    }

    public void setPlotPaddingBottom(float plotPaddingBottom) {
        this.boxModel.setPaddingBottom(plotPaddingBottom);
    }

    public float getPlotPaddingLeft() {
        return this.boxModel.getPaddingLeft();
    }

    public void setPlotPaddingLeft(float plotPaddingLeft) {
        this.boxModel.setPaddingLeft(plotPaddingLeft);
    }

    public float getPlotPaddingRight() {
        return this.boxModel.getPaddingRight();
    }

    public void setPlotPaddingRight(float plotPaddingRight) {
        this.boxModel.setPaddingRight(plotPaddingRight);
    }

    public Paint getBorderPaint() {
        return this.borderPaint;
    }

    public void setBorderPaint(Paint borderPaint2) {
        if (borderPaint2 == null) {
            this.borderPaint = null;
            return;
        }
        this.borderPaint = new Paint(borderPaint2);
        this.borderPaint.setStyle(Paint.Style.STROKE);
    }
}
