package com.github.mikephil.charting.charts;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.jobs.AnimatedMoveViewJob;
import com.github.mikephil.charting.jobs.AnimatedZoomJob;
import com.github.mikephil.charting.jobs.MoveViewJob;
import com.github.mikephil.charting.jobs.ZoomJob;
import com.github.mikephil.charting.listener.BarLineChartTouchListener;
import com.github.mikephil.charting.listener.OnDrawListener;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.PointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;

@SuppressLint({"RtlHardcoded"})
public abstract class BarLineChartBase<T extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>> extends Chart<T> implements BarLineScatterCandleBubbleDataProvider {
    private long drawCycles = 0;
    private Integer mAutoScaleLastHighestVisibleXIndex = null;
    private Integer mAutoScaleLastLowestVisibleXIndex = null;
    private boolean mAutoScaleMinMaxEnabled = false;
    protected YAxis mAxisLeft;
    protected YAxisRenderer mAxisRendererLeft;
    protected YAxisRenderer mAxisRendererRight;
    protected YAxis mAxisRight;
    protected Paint mBorderPaint;
    private boolean mCustomViewPortEnabled = false;
    protected boolean mDoubleTapToZoomEnabled = true;
    private boolean mDragEnabled = true;
    protected boolean mDrawBorders = false;
    protected boolean mDrawGridBackground = false;
    protected OnDrawListener mDrawListener;
    protected Paint mGridBackgroundPaint;
    protected boolean mHighlightPerDragEnabled = true;
    protected boolean mKeepPositionOnRotation = false;
    protected Transformer mLeftAxisTransformer;
    protected int mMaxVisibleCount = 100;
    protected float mMinOffset = 15.0f;
    protected boolean mPinchZoomEnabled = false;
    protected Transformer mRightAxisTransformer;
    private boolean mScaleXEnabled = true;
    private boolean mScaleYEnabled = true;
    protected XAxisRenderer mXAxisRenderer;
    private long totalTime = 0;

    public /* bridge */ /* synthetic */ BarLineScatterCandleBubbleData getData() {
        return (BarLineScatterCandleBubbleData) super.getData();
    }

    public BarLineChartBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BarLineChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarLineChartBase(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void init() {
        super.init();
        this.mAxisLeft = new YAxis(YAxis.AxisDependency.LEFT);
        this.mAxisRight = new YAxis(YAxis.AxisDependency.RIGHT);
        this.mLeftAxisTransformer = new Transformer(this.mViewPortHandler);
        this.mRightAxisTransformer = new Transformer(this.mViewPortHandler);
        this.mAxisRendererLeft = new YAxisRenderer(this.mViewPortHandler, this.mAxisLeft, this.mLeftAxisTransformer);
        this.mAxisRendererRight = new YAxisRenderer(this.mViewPortHandler, this.mAxisRight, this.mRightAxisTransformer);
        this.mXAxisRenderer = new XAxisRenderer(this.mViewPortHandler, this.mXAxis, this.mLeftAxisTransformer);
        setHighlighter(new ChartHighlighter(this));
        this.mChartTouchListener = new BarLineChartTouchListener(this, this.mViewPortHandler.getMatrixTouch());
        this.mGridBackgroundPaint = new Paint();
        this.mGridBackgroundPaint.setStyle(Paint.Style.FILL);
        this.mGridBackgroundPaint.setColor(Color.rgb(240, 240, 240));
        this.mBorderPaint = new Paint();
        this.mBorderPaint.setStyle(Paint.Style.STROKE);
        this.mBorderPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(1.0f));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mData != null) {
            long starttime = System.currentTimeMillis();
            calcModulus();
            this.mXAxisRenderer.calcXBounds(this, this.mXAxis.mAxisLabelModulus);
            this.mRenderer.calcXBounds(this, this.mXAxis.mAxisLabelModulus);
            drawGridBackground(canvas);
            if (this.mAxisLeft.isEnabled()) {
                this.mAxisRendererLeft.computeAxis(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisMaximum);
            }
            if (this.mAxisRight.isEnabled()) {
                this.mAxisRendererRight.computeAxis(this.mAxisRight.mAxisMinimum, this.mAxisRight.mAxisMaximum);
            }
            this.mXAxisRenderer.renderAxisLine(canvas);
            this.mAxisRendererLeft.renderAxisLine(canvas);
            this.mAxisRendererRight.renderAxisLine(canvas);
            if (this.mAutoScaleMinMaxEnabled) {
                int lowestVisibleXIndex = getLowestVisibleXIndex();
                int highestVisibleXIndex = getHighestVisibleXIndex();
                if (this.mAutoScaleLastLowestVisibleXIndex == null || this.mAutoScaleLastLowestVisibleXIndex.intValue() != lowestVisibleXIndex || this.mAutoScaleLastHighestVisibleXIndex == null || this.mAutoScaleLastHighestVisibleXIndex.intValue() != highestVisibleXIndex) {
                    calcMinMax();
                    calculateOffsets();
                    this.mAutoScaleLastLowestVisibleXIndex = Integer.valueOf(lowestVisibleXIndex);
                    this.mAutoScaleLastHighestVisibleXIndex = Integer.valueOf(highestVisibleXIndex);
                }
            }
            int clipRestoreCount = canvas.save();
            canvas.clipRect(this.mViewPortHandler.getContentRect());
            this.mXAxisRenderer.renderGridLines(canvas);
            this.mAxisRendererLeft.renderGridLines(canvas);
            this.mAxisRendererRight.renderGridLines(canvas);
            if (this.mXAxis.isDrawLimitLinesBehindDataEnabled()) {
                this.mXAxisRenderer.renderLimitLines(canvas);
            }
            if (this.mAxisLeft.isDrawLimitLinesBehindDataEnabled()) {
                this.mAxisRendererLeft.renderLimitLines(canvas);
            }
            if (this.mAxisRight.isDrawLimitLinesBehindDataEnabled()) {
                this.mAxisRendererRight.renderLimitLines(canvas);
            }
            this.mRenderer.drawData(canvas);
            if (valuesToHighlight()) {
                this.mRenderer.drawHighlighted(canvas, this.mIndicesToHighlight);
            }
            canvas.restoreToCount(clipRestoreCount);
            this.mRenderer.drawExtras(canvas);
            if (!this.mXAxis.isDrawLimitLinesBehindDataEnabled()) {
                this.mXAxisRenderer.renderLimitLines(canvas);
            }
            if (!this.mAxisLeft.isDrawLimitLinesBehindDataEnabled()) {
                this.mAxisRendererLeft.renderLimitLines(canvas);
            }
            if (!this.mAxisRight.isDrawLimitLinesBehindDataEnabled()) {
                this.mAxisRendererRight.renderLimitLines(canvas);
            }
            this.mXAxisRenderer.renderAxisLabels(canvas);
            this.mAxisRendererLeft.renderAxisLabels(canvas);
            this.mAxisRendererRight.renderAxisLabels(canvas);
            this.mRenderer.drawValues(canvas);
            this.mLegendRenderer.renderLegend(canvas);
            drawMarkers(canvas);
            drawDescription(canvas);
            if (this.mLogEnabled) {
                long drawtime = System.currentTimeMillis() - starttime;
                this.totalTime += drawtime;
                this.drawCycles++;
                Log.i(Chart.LOG_TAG, "Drawtime: " + drawtime + " ms, average: " + (this.totalTime / this.drawCycles) + " ms, cycles: " + this.drawCycles);
            }
        }
    }

    public void resetTracking() {
        this.totalTime = 0;
        this.drawCycles = 0;
    }

    /* access modifiers changed from: protected */
    public void prepareValuePxMatrix() {
        if (this.mLogEnabled) {
            Log.i(Chart.LOG_TAG, "Preparing Value-Px Matrix, xmin: " + this.mXAxis.mAxisMinimum + ", xmax: " + this.mXAxis.mAxisMaximum + ", xdelta: " + this.mXAxis.mAxisRange);
        }
        this.mRightAxisTransformer.prepareMatrixValuePx(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisRange, this.mAxisRight.mAxisRange, this.mAxisRight.mAxisMinimum);
        this.mLeftAxisTransformer.prepareMatrixValuePx(this.mXAxis.mAxisMinimum, this.mXAxis.mAxisRange, this.mAxisLeft.mAxisRange, this.mAxisLeft.mAxisMinimum);
    }

    /* access modifiers changed from: protected */
    public void prepareOffsetMatrix() {
        this.mRightAxisTransformer.prepareMatrixOffset(this.mAxisRight.isInverted());
        this.mLeftAxisTransformer.prepareMatrixOffset(this.mAxisLeft.isInverted());
    }

    public void notifyDataSetChanged() {
        if (this.mData != null) {
            if (this.mLogEnabled) {
                Log.i(Chart.LOG_TAG, "Preparing...");
            }
            if (this.mRenderer != null) {
                this.mRenderer.initBuffers();
            }
            calcMinMax();
            this.mAxisRendererLeft.computeAxis(this.mAxisLeft.mAxisMinimum, this.mAxisLeft.mAxisMaximum);
            this.mAxisRendererRight.computeAxis(this.mAxisRight.mAxisMinimum, this.mAxisRight.mAxisMaximum);
            this.mXAxisRenderer.computeAxis(((BarLineScatterCandleBubbleData) this.mData).getXValMaximumLength(), ((BarLineScatterCandleBubbleData) this.mData).getXVals());
            if (this.mLegend != null) {
                this.mLegendRenderer.computeLegend(this.mData);
            }
            calculateOffsets();
        } else if (this.mLogEnabled) {
            Log.i(Chart.LOG_TAG, "Preparing... DATA NOT SET.");
        }
    }

    /* access modifiers changed from: protected */
    public void calcMinMax() {
        if (this.mAutoScaleMinMaxEnabled) {
            ((BarLineScatterCandleBubbleData) this.mData).calcMinMax(getLowestVisibleXIndex(), getHighestVisibleXIndex());
        }
        this.mXAxis.mAxisMaximum = (float) (((BarLineScatterCandleBubbleData) this.mData).getXVals().size() - 1);
        this.mXAxis.mAxisRange = Math.abs(this.mXAxis.mAxisMaximum - this.mXAxis.mAxisMinimum);
        this.mAxisLeft.calcMinMax(((BarLineScatterCandleBubbleData) this.mData).getYMin(YAxis.AxisDependency.LEFT), ((BarLineScatterCandleBubbleData) this.mData).getYMax(YAxis.AxisDependency.LEFT));
        this.mAxisRight.calcMinMax(((BarLineScatterCandleBubbleData) this.mData).getYMin(YAxis.AxisDependency.RIGHT), ((BarLineScatterCandleBubbleData) this.mData).getYMax(YAxis.AxisDependency.RIGHT));
    }

    public void calculateOffsets() {
        if (!this.mCustomViewPortEnabled) {
            float offsetLeft = 0.0f;
            float offsetRight = 0.0f;
            float offsetTop = 0.0f;
            float offsetBottom = 0.0f;
            if (this.mLegend != null && this.mLegend.isEnabled()) {
                if (this.mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART || this.mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART_CENTER) {
                    offsetRight = 0.0f + Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + (this.mLegend.getXOffset() * 2.0f);
                } else if (this.mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART || this.mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART_CENTER) {
                    offsetLeft = 0.0f + Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + (this.mLegend.getXOffset() * 2.0f);
                } else if (this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_LEFT || this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_RIGHT || this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_CENTER) {
                    offsetBottom = 0.0f + Math.min(this.mLegend.mNeededHeight + this.mLegend.mTextHeightMax, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
                } else if (this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_LEFT || this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_RIGHT || this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_CENTER) {
                    offsetTop = 0.0f + Math.min(this.mLegend.mNeededHeight + this.mLegend.mTextHeightMax, this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
                }
            }
            if (this.mAxisLeft.needsOffset()) {
                offsetLeft += this.mAxisLeft.getRequiredWidthSpace(this.mAxisRendererLeft.getPaintAxisLabels());
            }
            if (this.mAxisRight.needsOffset()) {
                offsetRight += this.mAxisRight.getRequiredWidthSpace(this.mAxisRendererRight.getPaintAxisLabels());
            }
            if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
                float xlabelheight = ((float) this.mXAxis.mLabelRotatedHeight) + this.mXAxis.getYOffset();
                if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
                    offsetBottom += xlabelheight;
                } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
                    offsetTop += xlabelheight;
                } else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
                    offsetBottom += xlabelheight;
                    offsetTop += xlabelheight;
                }
            }
            float offsetTop2 = offsetTop + getExtraTopOffset();
            float offsetRight2 = offsetRight + getExtraRightOffset();
            float offsetBottom2 = offsetBottom + getExtraBottomOffset();
            float offsetLeft2 = offsetLeft + getExtraLeftOffset();
            float minOffset = Utils.convertDpToPixel(this.mMinOffset);
            this.mViewPortHandler.restrainViewPort(Math.max(minOffset, offsetLeft2), Math.max(minOffset, offsetTop2), Math.max(minOffset, offsetRight2), Math.max(minOffset, offsetBottom2));
            if (this.mLogEnabled) {
                Log.i(Chart.LOG_TAG, "offsetLeft: " + offsetLeft2 + ", offsetTop: " + offsetTop2 + ", offsetRight: " + offsetRight2 + ", offsetBottom: " + offsetBottom2);
                Log.i(Chart.LOG_TAG, "Content: " + this.mViewPortHandler.getContentRect().toString());
            }
        }
        prepareOffsetMatrix();
        prepareValuePxMatrix();
    }

    /* access modifiers changed from: protected */
    public void calcModulus() {
        if (this.mXAxis != null && this.mXAxis.isEnabled()) {
            if (!this.mXAxis.isAxisModulusCustom()) {
                float[] values = new float[9];
                this.mViewPortHandler.getMatrixTouch().getValues(values);
                this.mXAxis.mAxisLabelModulus = (int) Math.ceil((double) (((float) (((BarLineScatterCandleBubbleData) this.mData).getXValCount() * this.mXAxis.mLabelRotatedWidth)) / (this.mViewPortHandler.contentWidth() * values[0])));
            }
            if (this.mLogEnabled) {
                Log.i(Chart.LOG_TAG, "X-Axis modulus: " + this.mXAxis.mAxisLabelModulus + ", x-axis label width: " + this.mXAxis.mLabelWidth + ", x-axis label rotated width: " + this.mXAxis.mLabelRotatedWidth + ", content width: " + this.mViewPortHandler.contentWidth());
            }
            if (this.mXAxis.mAxisLabelModulus < 1) {
                this.mXAxis.mAxisLabelModulus = 1;
            }
        }
    }

    /* access modifiers changed from: protected */
    public float[] getMarkerPosition(Entry e, Highlight highlight) {
        float yPos;
        float yPos2;
        float xPos;
        int dataSetIndex = highlight.getDataSetIndex();
        float xPos2 = (float) e.getXIndex();
        float yPos3 = e.getVal();
        if (this instanceof BarChart) {
            float space = ((BarData) this.mData).getGroupSpace();
            int setCount = ((BarLineScatterCandleBubbleData) this.mData).getDataSetCount();
            int i = e.getXIndex();
            if (this instanceof HorizontalBarChart) {
                yPos = ((float) (((setCount - 1) * i) + i + dataSetIndex)) + (((float) i) * space) + (space / 2.0f);
                if (((BarEntry) e).getVals() != null) {
                    xPos = highlight.getRange().to;
                } else {
                    xPos = e.getVal();
                }
                xPos2 = xPos * this.mAnimator.getPhaseY();
            } else {
                xPos2 = ((float) (((setCount - 1) * i) + i + dataSetIndex)) + (((float) i) * space) + (space / 2.0f);
                if (((BarEntry) e).getVals() != null) {
                    yPos2 = highlight.getRange().to;
                } else {
                    yPos2 = e.getVal();
                }
                yPos = yPos2 * this.mAnimator.getPhaseY();
            }
        } else {
            yPos = yPos3 * this.mAnimator.getPhaseY();
        }
        float[] pts = {xPos2, yPos};
        getTransformer(((IBarLineScatterCandleBubbleDataSet) ((BarLineScatterCandleBubbleData) this.mData).getDataSetByIndex(dataSetIndex)).getAxisDependency()).pointValuesToPixel(pts);
        return pts;
    }

    /* access modifiers changed from: protected */
    public void drawGridBackground(Canvas c) {
        if (this.mDrawGridBackground) {
            c.drawRect(this.mViewPortHandler.getContentRect(), this.mGridBackgroundPaint);
        }
        if (this.mDrawBorders) {
            c.drawRect(this.mViewPortHandler.getContentRect(), this.mBorderPaint);
        }
    }

    public Transformer getTransformer(YAxis.AxisDependency which) {
        if (which == YAxis.AxisDependency.LEFT) {
            return this.mLeftAxisTransformer;
        }
        return this.mRightAxisTransformer;
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (this.mChartTouchListener == null || this.mData == null || !this.mTouchEnabled) {
            return false;
        }
        return this.mChartTouchListener.onTouch(this, event);
    }

    public void computeScroll() {
        if (this.mChartTouchListener instanceof BarLineChartTouchListener) {
            ((BarLineChartTouchListener) this.mChartTouchListener).computeScroll();
        }
    }

    public void zoomIn() {
        PointF center = this.mViewPortHandler.getContentCenter();
        this.mViewPortHandler.refresh(this.mViewPortHandler.zoomIn(center.x, -center.y), this, false);
        calculateOffsets();
        postInvalidate();
    }

    public void zoomOut() {
        PointF center = this.mViewPortHandler.getContentCenter();
        this.mViewPortHandler.refresh(this.mViewPortHandler.zoomOut(center.x, -center.y), this, false);
        calculateOffsets();
        postInvalidate();
    }

    public void zoom(float scaleX, float scaleY, float x, float y) {
        this.mViewPortHandler.refresh(this.mViewPortHandler.zoom(scaleX, scaleY, x, y), this, false);
        calculateOffsets();
        postInvalidate();
    }

    public void zoom(float scaleX, float scaleY, float xValue, float yValue, YAxis.AxisDependency axis) {
        addViewportJob(new ZoomJob(this.mViewPortHandler, scaleX, scaleY, xValue, yValue, getTransformer(axis), axis, this));
    }

    @TargetApi(11)
    public void zoomAndCenterAnimated(float scaleX, float scaleY, float xValue, float yValue, YAxis.AxisDependency axis, long duration) {
        if (Build.VERSION.SDK_INT >= 11) {
            PointD origin = getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), axis);
            addViewportJob(new AnimatedZoomJob(this.mViewPortHandler, this, getTransformer(axis), getAxis(axis), (float) this.mXAxis.getValues().size(), scaleX, scaleY, this.mViewPortHandler.getScaleX(), this.mViewPortHandler.getScaleY(), xValue, yValue, (float) origin.x, (float) origin.y, duration));
            return;
        }
        Log.e(Chart.LOG_TAG, "Unable to execute zoomAndCenterAnimated(...) on API level < 11");
    }

    public void fitScreen() {
        this.mViewPortHandler.refresh(this.mViewPortHandler.fitScreen(), this, false);
        calculateOffsets();
        postInvalidate();
    }

    public void setScaleMinima(float scaleX, float scaleY) {
        this.mViewPortHandler.setMinimumScaleX(scaleX);
        this.mViewPortHandler.setMinimumScaleY(scaleY);
    }

    public void setVisibleXRangeMaximum(float maxXRange) {
        this.mViewPortHandler.setMinimumScaleX(this.mXAxis.mAxisRange / maxXRange);
    }

    public void setVisibleXRangeMinimum(float minXRange) {
        this.mViewPortHandler.setMaximumScaleX(this.mXAxis.mAxisRange / minXRange);
    }

    public void setVisibleXRange(float minXRange, float maxXRange) {
        float maxScale = this.mXAxis.mAxisRange / minXRange;
        this.mViewPortHandler.setMinMaxScaleX(this.mXAxis.mAxisRange / maxXRange, maxScale);
    }

    public void setVisibleYRangeMaximum(float maxYRange, YAxis.AxisDependency axis) {
        this.mViewPortHandler.setMinimumScaleY(getDeltaY(axis) / maxYRange);
    }

    public void moveViewToX(float xIndex) {
        addViewportJob(new MoveViewJob(this.mViewPortHandler, xIndex, 0.0f, getTransformer(YAxis.AxisDependency.LEFT), this));
    }

    public void moveViewToY(float yValue, YAxis.AxisDependency axis) {
        addViewportJob(new MoveViewJob(this.mViewPortHandler, 0.0f, ((getDeltaY(axis) / this.mViewPortHandler.getScaleY()) / 2.0f) + yValue, getTransformer(axis), this));
    }

    public void moveViewTo(float xIndex, float yValue, YAxis.AxisDependency axis) {
        float f = xIndex;
        addViewportJob(new MoveViewJob(this.mViewPortHandler, f, yValue + ((getDeltaY(axis) / this.mViewPortHandler.getScaleY()) / 2.0f), getTransformer(axis), this));
    }

    @TargetApi(11)
    public void moveViewToAnimated(float xIndex, float yValue, YAxis.AxisDependency axis, long duration) {
        if (Build.VERSION.SDK_INT >= 11) {
            PointD bounds = getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), axis);
            float f = xIndex;
            addViewportJob(new AnimatedMoveViewJob(this.mViewPortHandler, f, yValue + ((getDeltaY(axis) / this.mViewPortHandler.getScaleY()) / 2.0f), getTransformer(axis), this, (float) bounds.x, (float) bounds.y, duration));
            return;
        }
        Log.e(Chart.LOG_TAG, "Unable to execute moveViewToAnimated(...) on API level < 11");
    }

    public void centerViewTo(float xIndex, float yValue, YAxis.AxisDependency axis) {
        float valsInView = getDeltaY(axis) / this.mViewPortHandler.getScaleY();
        addViewportJob(new MoveViewJob(this.mViewPortHandler, xIndex - ((((float) getXAxis().getValues().size()) / this.mViewPortHandler.getScaleX()) / 2.0f), (valsInView / 2.0f) + yValue, getTransformer(axis), this));
    }

    @TargetApi(11)
    public void centerViewToAnimated(float xIndex, float yValue, YAxis.AxisDependency axis, long duration) {
        if (Build.VERSION.SDK_INT >= 11) {
            PointD bounds = getValuesByTouchPoint(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(), axis);
            float valsInView = getDeltaY(axis) / this.mViewPortHandler.getScaleY();
            addViewportJob(new AnimatedMoveViewJob(this.mViewPortHandler, xIndex - ((((float) getXAxis().getValues().size()) / this.mViewPortHandler.getScaleX()) / 2.0f), (valsInView / 2.0f) + yValue, getTransformer(axis), this, (float) bounds.x, (float) bounds.y, duration));
            return;
        }
        Log.e(Chart.LOG_TAG, "Unable to execute centerViewToAnimated(...) on API level < 11");
    }

    public void setViewPortOffsets(float left, float top, float right, float bottom) {
        this.mCustomViewPortEnabled = true;
        final float f = left;
        final float f2 = top;
        final float f3 = right;
        final float f4 = bottom;
        post(new Runnable() {
            public void run() {
                BarLineChartBase.this.mViewPortHandler.restrainViewPort(f, f2, f3, f4);
                BarLineChartBase.this.prepareOffsetMatrix();
                BarLineChartBase.this.prepareValuePxMatrix();
            }
        });
    }

    public void resetViewPortOffsets() {
        this.mCustomViewPortEnabled = false;
        calculateOffsets();
    }

    public float getDeltaY(YAxis.AxisDependency axis) {
        if (axis == YAxis.AxisDependency.LEFT) {
            return this.mAxisLeft.mAxisRange;
        }
        return this.mAxisRight.mAxisRange;
    }

    public void setOnDrawListener(OnDrawListener drawListener) {
        this.mDrawListener = drawListener;
    }

    public OnDrawListener getDrawListener() {
        return this.mDrawListener;
    }

    public PointF getPosition(Entry e, YAxis.AxisDependency axis) {
        if (e == null) {
            return null;
        }
        float[] vals = {(float) e.getXIndex(), e.getVal()};
        getTransformer(axis).pointValuesToPixel(vals);
        return new PointF(vals[0], vals[1]);
    }

    public void setMaxVisibleValueCount(int count) {
        this.mMaxVisibleCount = count;
    }

    public int getMaxVisibleCount() {
        return this.mMaxVisibleCount;
    }

    public void setHighlightPerDragEnabled(boolean enabled) {
        this.mHighlightPerDragEnabled = enabled;
    }

    public boolean isHighlightPerDragEnabled() {
        return this.mHighlightPerDragEnabled;
    }

    public void setGridBackgroundColor(int color) {
        this.mGridBackgroundPaint.setColor(color);
    }

    public void setDragEnabled(boolean enabled) {
        this.mDragEnabled = enabled;
    }

    public boolean isDragEnabled() {
        return this.mDragEnabled;
    }

    public void setScaleEnabled(boolean enabled) {
        this.mScaleXEnabled = enabled;
        this.mScaleYEnabled = enabled;
    }

    public void setScaleXEnabled(boolean enabled) {
        this.mScaleXEnabled = enabled;
    }

    public void setScaleYEnabled(boolean enabled) {
        this.mScaleYEnabled = enabled;
    }

    public boolean isScaleXEnabled() {
        return this.mScaleXEnabled;
    }

    public boolean isScaleYEnabled() {
        return this.mScaleYEnabled;
    }

    public void setDoubleTapToZoomEnabled(boolean enabled) {
        this.mDoubleTapToZoomEnabled = enabled;
    }

    public boolean isDoubleTapToZoomEnabled() {
        return this.mDoubleTapToZoomEnabled;
    }

    public void setDrawGridBackground(boolean enabled) {
        this.mDrawGridBackground = enabled;
    }

    public void setDrawBorders(boolean enabled) {
        this.mDrawBorders = enabled;
    }

    public void setBorderWidth(float width) {
        this.mBorderPaint.setStrokeWidth(Utils.convertDpToPixel(width));
    }

    public void setBorderColor(int color) {
        this.mBorderPaint.setColor(color);
    }

    public float getMinOffset() {
        return this.mMinOffset;
    }

    public void setMinOffset(float minOffset) {
        this.mMinOffset = minOffset;
    }

    public boolean isKeepPositionOnRotation() {
        return this.mKeepPositionOnRotation;
    }

    public void setKeepPositionOnRotation(boolean keepPositionOnRotation) {
        this.mKeepPositionOnRotation = keepPositionOnRotation;
    }

    public Highlight getHighlightByTouchPoint(float x, float y) {
        if (this.mData != null) {
            return getHighlighter().getHighlight(x, y);
        }
        Log.e(Chart.LOG_TAG, "Can't select by touch. No data set.");
        return null;
    }

    public PointD getValuesByTouchPoint(float x, float y, YAxis.AxisDependency axis) {
        float[] pts = {x, y};
        getTransformer(axis).pixelsToValue(pts);
        return new PointD((double) pts[0], (double) pts[1]);
    }

    public PointD getPixelsForValues(float x, float y, YAxis.AxisDependency axis) {
        float[] pts = {x, y};
        getTransformer(axis).pointValuesToPixel(pts);
        return new PointD((double) pts[0], (double) pts[1]);
    }

    public float getYValueByTouchPoint(float x, float y, YAxis.AxisDependency axis) {
        return (float) getValuesByTouchPoint(x, y, axis).y;
    }

    public Entry getEntryByTouchPoint(float x, float y) {
        Highlight h = getHighlightByTouchPoint(x, y);
        if (h != null) {
            return ((BarLineScatterCandleBubbleData) this.mData).getEntryForHighlight(h);
        }
        return null;
    }

    public IBarLineScatterCandleBubbleDataSet getDataSetByTouchPoint(float x, float y) {
        Highlight h = getHighlightByTouchPoint(x, y);
        if (h != null) {
            return (IBarLineScatterCandleBubbleDataSet) ((BarLineScatterCandleBubbleData) this.mData).getDataSetByIndex(h.getDataSetIndex());
        }
        return null;
    }

    public int getLowestVisibleXIndex() {
        float[] pts = {this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom()};
        getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        if (pts[0] <= 0.0f) {
            return 0;
        }
        return (int) (pts[0] + 1.0f);
    }

    public int getHighestVisibleXIndex() {
        float[] pts = {this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom()};
        getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        return pts[0] >= ((float) ((BarLineScatterCandleBubbleData) this.mData).getXValCount()) ? ((BarLineScatterCandleBubbleData) this.mData).getXValCount() - 1 : (int) pts[0];
    }

    public float getScaleX() {
        if (this.mViewPortHandler == null) {
            return 1.0f;
        }
        return this.mViewPortHandler.getScaleX();
    }

    public float getScaleY() {
        if (this.mViewPortHandler == null) {
            return 1.0f;
        }
        return this.mViewPortHandler.getScaleY();
    }

    public boolean isFullyZoomedOut() {
        return this.mViewPortHandler.isFullyZoomedOut();
    }

    public YAxis getAxisLeft() {
        return this.mAxisLeft;
    }

    public YAxis getAxisRight() {
        return this.mAxisRight;
    }

    public YAxis getAxis(YAxis.AxisDependency axis) {
        if (axis == YAxis.AxisDependency.LEFT) {
            return this.mAxisLeft;
        }
        return this.mAxisRight;
    }

    public boolean isInverted(YAxis.AxisDependency axis) {
        return getAxis(axis).isInverted();
    }

    public void setPinchZoom(boolean enabled) {
        this.mPinchZoomEnabled = enabled;
    }

    public boolean isPinchZoomEnabled() {
        return this.mPinchZoomEnabled;
    }

    public void setDragOffsetX(float offset) {
        this.mViewPortHandler.setDragOffsetX(offset);
    }

    public void setDragOffsetY(float offset) {
        this.mViewPortHandler.setDragOffsetY(offset);
    }

    public boolean hasNoDragOffset() {
        return this.mViewPortHandler.hasNoDragOffset();
    }

    public XAxisRenderer getRendererXAxis() {
        return this.mXAxisRenderer;
    }

    public void setXAxisRenderer(XAxisRenderer xAxisRenderer) {
        this.mXAxisRenderer = xAxisRenderer;
    }

    public YAxisRenderer getRendererLeftYAxis() {
        return this.mAxisRendererLeft;
    }

    public void setRendererLeftYAxis(YAxisRenderer rendererLeftYAxis) {
        this.mAxisRendererLeft = rendererLeftYAxis;
    }

    public YAxisRenderer getRendererRightYAxis() {
        return this.mAxisRendererRight;
    }

    public void setRendererRightYAxis(YAxisRenderer rendererRightYAxis) {
        this.mAxisRendererRight = rendererRightYAxis;
    }

    public float getYChartMax() {
        return Math.max(this.mAxisLeft.mAxisMaximum, this.mAxisRight.mAxisMaximum);
    }

    public float getYChartMin() {
        return Math.min(this.mAxisLeft.mAxisMinimum, this.mAxisRight.mAxisMinimum);
    }

    public boolean isAnyAxisInverted() {
        if (!this.mAxisLeft.isInverted() && !this.mAxisRight.isInverted()) {
            return false;
        }
        return true;
    }

    public void setAutoScaleMinMaxEnabled(boolean enabled) {
        this.mAutoScaleMinMaxEnabled = enabled;
    }

    public boolean isAutoScaleMinMaxEnabled() {
        return this.mAutoScaleMinMaxEnabled;
    }

    public void setPaint(Paint p, int which) {
        super.setPaint(p, which);
        switch (which) {
            case 4:
                this.mGridBackgroundPaint = p;
                return;
            default:
                return;
        }
    }

    public Paint getPaint(int which) {
        Paint p = super.getPaint(which);
        if (p != null) {
            return p;
        }
        switch (which) {
            case 4:
                return this.mGridBackgroundPaint;
            default:
                return null;
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        float[] pts = new float[2];
        if (this.mKeepPositionOnRotation) {
            pts[0] = this.mViewPortHandler.contentLeft();
            pts[1] = this.mViewPortHandler.contentTop();
            getTransformer(YAxis.AxisDependency.LEFT).pixelsToValue(pts);
        }
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.mKeepPositionOnRotation) {
            getTransformer(YAxis.AxisDependency.LEFT).pointValuesToPixel(pts);
            this.mViewPortHandler.centerViewPort(pts, this);
            return;
        }
        this.mViewPortHandler.refresh(this.mViewPortHandler.getMatrixTouch(), this, true);
    }
}
