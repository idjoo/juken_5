package com.github.mikephil.charting.charts;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.animation.EasingFunction;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.renderer.LegendRenderer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressLint({"NewApi"})
public abstract class Chart<T extends ChartData<? extends IDataSet<? extends Entry>>> extends ViewGroup implements ChartInterface {
    public static final String LOG_TAG = "MPAndroidChart";
    public static final int PAINT_CENTER_TEXT = 14;
    public static final int PAINT_DESCRIPTION = 11;
    public static final int PAINT_GRID_BACKGROUND = 4;
    public static final int PAINT_HOLE = 13;
    public static final int PAINT_INFO = 7;
    public static final int PAINT_LEGEND_LABEL = 18;
    protected ChartAnimator mAnimator;
    protected ChartTouchListener mChartTouchListener;
    protected T mData = null;
    protected ValueFormatter mDefaultFormatter;
    protected Paint mDescPaint;
    protected String mDescription = "Description";
    private PointF mDescriptionPosition;
    private boolean mDragDecelerationEnabled = true;
    private float mDragDecelerationFrictionCoef = 0.9f;
    protected boolean mDrawMarkerViews = true;
    protected Paint mDrawPaint;
    private float mExtraBottomOffset = 0.0f;
    private float mExtraLeftOffset = 0.0f;
    private float mExtraRightOffset = 0.0f;
    private float mExtraTopOffset = 0.0f;
    private OnChartGestureListener mGestureListener;
    protected boolean mHighLightPerTapEnabled = true;
    protected ChartHighlighter mHighlighter;
    protected Highlight[] mIndicesToHighlight;
    protected Paint mInfoPaint;
    protected ArrayList<Runnable> mJobs = new ArrayList<>();
    protected Legend mLegend;
    protected LegendRenderer mLegendRenderer;
    protected boolean mLogEnabled = false;
    protected MarkerView mMarkerView;
    private String mNoDataText = "No chart data available.";
    private String mNoDataTextDescription;
    private boolean mOffsetsCalculated = false;
    protected DataRenderer mRenderer;
    protected OnChartValueSelectedListener mSelectionListener;
    protected boolean mTouchEnabled = true;
    private boolean mUnbind = false;
    protected ViewPortHandler mViewPortHandler;
    protected XAxis mXAxis;

    /* access modifiers changed from: protected */
    public abstract void calcMinMax();

    /* access modifiers changed from: protected */
    public abstract void calculateOffsets();

    /* access modifiers changed from: protected */
    public abstract float[] getMarkerPosition(Entry entry, Highlight highlight);

    public abstract void notifyDataSetChanged();

    public Chart(Context context) {
        super(context);
        init();
    }

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Chart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /* access modifiers changed from: protected */
    public void init() {
        setWillNotDraw(false);
        if (Build.VERSION.SDK_INT < 11) {
            this.mAnimator = new ChartAnimator();
        } else {
            this.mAnimator = new ChartAnimator(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Chart.this.postInvalidate();
                }
            });
        }
        Utils.init(getContext());
        this.mDefaultFormatter = new DefaultValueFormatter(1);
        this.mViewPortHandler = new ViewPortHandler();
        this.mLegend = new Legend();
        this.mLegendRenderer = new LegendRenderer(this.mViewPortHandler, this.mLegend);
        this.mXAxis = new XAxis();
        this.mDescPaint = new Paint(1);
        this.mDescPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mDescPaint.setTextAlign(Paint.Align.RIGHT);
        this.mDescPaint.setTextSize(Utils.convertDpToPixel(9.0f));
        this.mInfoPaint = new Paint(1);
        this.mInfoPaint.setColor(Color.rgb(247, 189, 51));
        this.mInfoPaint.setTextAlign(Paint.Align.CENTER);
        this.mInfoPaint.setTextSize(Utils.convertDpToPixel(12.0f));
        this.mDrawPaint = new Paint(4);
        if (this.mLogEnabled) {
            Log.i("", "Chart.init()");
        }
    }

    public void setData(T data) {
        if (data == null) {
            Log.e(LOG_TAG, "Cannot set data for chart. Provided data object is null.");
            return;
        }
        this.mOffsetsCalculated = false;
        this.mData = data;
        calculateFormatter(data.getYMin(), data.getYMax());
        for (IDataSet set : this.mData.getDataSets()) {
            if (Utils.needsDefaultFormatter(set.getValueFormatter())) {
                set.setValueFormatter(this.mDefaultFormatter);
            }
        }
        notifyDataSetChanged();
        if (this.mLogEnabled) {
            Log.i(LOG_TAG, "Data is set.");
        }
    }

    public void clear() {
        this.mData = null;
        this.mIndicesToHighlight = null;
        invalidate();
    }

    public void clearValues() {
        this.mData.clearValues();
        invalidate();
    }

    public boolean isEmpty() {
        if (this.mData != null && this.mData.getYValCount() > 0) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void calculateFormatter(float min, float max) {
        float reference;
        if (this.mData == null || this.mData.getXValCount() < 2) {
            reference = Math.max(Math.abs(min), Math.abs(max));
        } else {
            reference = Math.abs(max - min);
        }
        this.mDefaultFormatter = new DefaultValueFormatter(Utils.getDecimals(reference));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        boolean hasDescription;
        float line1height;
        float line2height;
        float lineSpacing = 0.0f;
        if (this.mData == null) {
            boolean hasText = !TextUtils.isEmpty(this.mNoDataText);
            if (!TextUtils.isEmpty(this.mNoDataTextDescription)) {
                hasDescription = true;
            } else {
                hasDescription = false;
            }
            if (hasText) {
                line1height = (float) Utils.calcTextHeight(this.mInfoPaint, this.mNoDataText);
            } else {
                line1height = 0.0f;
            }
            if (hasDescription) {
                line2height = (float) Utils.calcTextHeight(this.mInfoPaint, this.mNoDataTextDescription);
            } else {
                line2height = 0.0f;
            }
            if (hasText && hasDescription) {
                lineSpacing = this.mInfoPaint.getFontSpacing() - line1height;
            }
            float y = ((((float) getHeight()) - ((line1height + lineSpacing) + line2height)) / 2.0f) + line1height;
            if (hasText) {
                canvas.drawText(this.mNoDataText, (float) (getWidth() / 2), y, this.mInfoPaint);
                if (hasDescription) {
                    y = y + line1height + lineSpacing;
                }
            }
            if (hasDescription) {
                canvas.drawText(this.mNoDataTextDescription, (float) (getWidth() / 2), y, this.mInfoPaint);
            }
        } else if (!this.mOffsetsCalculated) {
            calculateOffsets();
            this.mOffsetsCalculated = true;
        }
    }

    /* access modifiers changed from: protected */
    public void drawDescription(Canvas c) {
        if (this.mDescription.equals("")) {
            return;
        }
        if (this.mDescriptionPosition == null) {
            c.drawText(this.mDescription, (((float) getWidth()) - this.mViewPortHandler.offsetRight()) - 10.0f, (((float) getHeight()) - this.mViewPortHandler.offsetBottom()) - 10.0f, this.mDescPaint);
        } else {
            c.drawText(this.mDescription, this.mDescriptionPosition.x, this.mDescriptionPosition.y, this.mDescPaint);
        }
    }

    public Highlight[] getHighlighted() {
        return this.mIndicesToHighlight;
    }

    public boolean isHighlightPerTapEnabled() {
        return this.mHighLightPerTapEnabled;
    }

    public void setHighlightPerTapEnabled(boolean enabled) {
        this.mHighLightPerTapEnabled = enabled;
    }

    public boolean valuesToHighlight() {
        return (this.mIndicesToHighlight == null || this.mIndicesToHighlight.length <= 0 || this.mIndicesToHighlight[0] == null) ? false : true;
    }

    public void highlightValues(Highlight[] highs) {
        this.mIndicesToHighlight = highs;
        if (highs == null || highs.length <= 0 || highs[0] == null) {
            this.mChartTouchListener.setLastHighlighted((Highlight) null);
        } else {
            this.mChartTouchListener.setLastHighlighted(highs[0]);
        }
        invalidate();
    }

    public void highlightValue(int xIndex, int dataSetIndex) {
        if (xIndex < 0 || dataSetIndex < 0 || xIndex >= this.mData.getXValCount() || dataSetIndex >= this.mData.getDataSetCount()) {
            highlightValues((Highlight[]) null);
            return;
        }
        highlightValues(new Highlight[]{new Highlight(xIndex, dataSetIndex)});
    }

    public void highlightValue(Highlight highlight) {
        highlightValue(highlight, false);
    }

    public void highlightValue(Highlight high, boolean callListener) {
        Entry e = null;
        if (high == null) {
            this.mIndicesToHighlight = null;
        } else {
            if (this.mLogEnabled) {
                Log.i(LOG_TAG, "Highlighted: " + high.toString());
            }
            e = this.mData.getEntryForHighlight(high);
            if (e == null || e.getXIndex() != high.getXIndex()) {
                this.mIndicesToHighlight = null;
                high = null;
            } else {
                this.mIndicesToHighlight = new Highlight[]{high};
            }
        }
        if (callListener && this.mSelectionListener != null) {
            if (!valuesToHighlight()) {
                this.mSelectionListener.onNothingSelected();
            } else {
                this.mSelectionListener.onValueSelected(e, high.getDataSetIndex(), high);
            }
        }
        invalidate();
    }

    @Deprecated
    public void highlightTouch(Highlight high) {
        highlightValue(high, true);
    }

    public void setOnTouchListener(ChartTouchListener l) {
        this.mChartTouchListener = l;
    }

    /* access modifiers changed from: protected */
    public void drawMarkers(Canvas canvas) {
        Entry e;
        if (this.mMarkerView != null && this.mDrawMarkerViews && valuesToHighlight()) {
            for (int i = 0; i < this.mIndicesToHighlight.length; i++) {
                Highlight highlight = this.mIndicesToHighlight[i];
                int xIndex = highlight.getXIndex();
                int dataSetIndex = highlight.getDataSetIndex();
                float deltaX = this.mXAxis.mAxisRange;
                if (((float) xIndex) <= deltaX && ((float) xIndex) <= this.mAnimator.getPhaseX() * deltaX && (e = this.mData.getEntryForHighlight(this.mIndicesToHighlight[i])) != null && e.getXIndex() == this.mIndicesToHighlight[i].getXIndex()) {
                    float[] pos = getMarkerPosition(e, highlight);
                    if (this.mViewPortHandler.isInBounds(pos[0], pos[1])) {
                        this.mMarkerView.refreshContent(e, highlight);
                        this.mMarkerView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
                        this.mMarkerView.layout(0, 0, this.mMarkerView.getMeasuredWidth(), this.mMarkerView.getMeasuredHeight());
                        if (pos[1] - ((float) this.mMarkerView.getHeight()) <= 0.0f) {
                            this.mMarkerView.draw(canvas, pos[0], pos[1] + (((float) this.mMarkerView.getHeight()) - pos[1]));
                        } else {
                            this.mMarkerView.draw(canvas, pos[0], pos[1]);
                        }
                    }
                }
            }
        }
    }

    public ChartAnimator getAnimator() {
        return this.mAnimator;
    }

    public boolean isDragDecelerationEnabled() {
        return this.mDragDecelerationEnabled;
    }

    public void setDragDecelerationEnabled(boolean enabled) {
        this.mDragDecelerationEnabled = enabled;
    }

    public float getDragDecelerationFrictionCoef() {
        return this.mDragDecelerationFrictionCoef;
    }

    public void setDragDecelerationFrictionCoef(float newValue) {
        if (newValue < 0.0f) {
            newValue = 0.0f;
        }
        if (newValue >= 1.0f) {
            newValue = 0.999f;
        }
        this.mDragDecelerationFrictionCoef = newValue;
    }

    public void animateXY(int durationMillisX, int durationMillisY, EasingFunction easingX, EasingFunction easingY) {
        this.mAnimator.animateXY(durationMillisX, durationMillisY, easingX, easingY);
    }

    public void animateX(int durationMillis, EasingFunction easing) {
        this.mAnimator.animateX(durationMillis, easing);
    }

    public void animateY(int durationMillis, EasingFunction easing) {
        this.mAnimator.animateY(durationMillis, easing);
    }

    public void animateXY(int durationMillisX, int durationMillisY, Easing.EasingOption easingX, Easing.EasingOption easingY) {
        this.mAnimator.animateXY(durationMillisX, durationMillisY, easingX, easingY);
    }

    public void animateX(int durationMillis, Easing.EasingOption easing) {
        this.mAnimator.animateX(durationMillis, easing);
    }

    public void animateY(int durationMillis, Easing.EasingOption easing) {
        this.mAnimator.animateY(durationMillis, easing);
    }

    public void animateX(int durationMillis) {
        this.mAnimator.animateX(durationMillis);
    }

    public void animateY(int durationMillis) {
        this.mAnimator.animateY(durationMillis);
    }

    public void animateXY(int durationMillisX, int durationMillisY) {
        this.mAnimator.animateXY(durationMillisX, durationMillisY);
    }

    public XAxis getXAxis() {
        return this.mXAxis;
    }

    public ValueFormatter getDefaultValueFormatter() {
        return this.mDefaultFormatter;
    }

    public void setOnChartValueSelectedListener(OnChartValueSelectedListener l) {
        this.mSelectionListener = l;
    }

    public void setOnChartGestureListener(OnChartGestureListener l) {
        this.mGestureListener = l;
    }

    public OnChartGestureListener getOnChartGestureListener() {
        return this.mGestureListener;
    }

    public float getYMax() {
        return this.mData.getYMax();
    }

    public float getYMin() {
        return this.mData.getYMin();
    }

    public float getXChartMax() {
        return this.mXAxis.mAxisMaximum;
    }

    public float getXChartMin() {
        return this.mXAxis.mAxisMinimum;
    }

    public int getXValCount() {
        return this.mData.getXValCount();
    }

    public int getValueCount() {
        return this.mData.getYValCount();
    }

    public PointF getCenter() {
        return new PointF(((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f);
    }

    public PointF getCenterOffsets() {
        return this.mViewPortHandler.getContentCenter();
    }

    public void setDescription(String desc) {
        if (desc == null) {
            desc = "";
        }
        this.mDescription = desc;
    }

    public void setDescriptionPosition(float x, float y) {
        this.mDescriptionPosition = new PointF(x, y);
    }

    public void setDescriptionTypeface(Typeface t) {
        this.mDescPaint.setTypeface(t);
    }

    public void setDescriptionTextSize(float size) {
        if (size > 16.0f) {
            size = 16.0f;
        }
        if (size < 6.0f) {
            size = 6.0f;
        }
        this.mDescPaint.setTextSize(Utils.convertDpToPixel(size));
    }

    public void setDescriptionColor(int color) {
        this.mDescPaint.setColor(color);
    }

    public void setExtraOffsets(float left, float top, float right, float bottom) {
        setExtraLeftOffset(left);
        setExtraTopOffset(top);
        setExtraRightOffset(right);
        setExtraBottomOffset(bottom);
    }

    public void setExtraTopOffset(float offset) {
        this.mExtraTopOffset = Utils.convertDpToPixel(offset);
    }

    public float getExtraTopOffset() {
        return this.mExtraTopOffset;
    }

    public void setExtraRightOffset(float offset) {
        this.mExtraRightOffset = Utils.convertDpToPixel(offset);
    }

    public float getExtraRightOffset() {
        return this.mExtraRightOffset;
    }

    public void setExtraBottomOffset(float offset) {
        this.mExtraBottomOffset = Utils.convertDpToPixel(offset);
    }

    public float getExtraBottomOffset() {
        return this.mExtraBottomOffset;
    }

    public void setExtraLeftOffset(float offset) {
        this.mExtraLeftOffset = Utils.convertDpToPixel(offset);
    }

    public float getExtraLeftOffset() {
        return this.mExtraLeftOffset;
    }

    public void setLogEnabled(boolean enabled) {
        this.mLogEnabled = enabled;
    }

    public boolean isLogEnabled() {
        return this.mLogEnabled;
    }

    public void setNoDataText(String text) {
        this.mNoDataText = text;
    }

    public void setNoDataTextDescription(String text) {
        this.mNoDataTextDescription = text;
    }

    public void setTouchEnabled(boolean enabled) {
        this.mTouchEnabled = enabled;
    }

    public void setMarkerView(MarkerView v) {
        this.mMarkerView = v;
    }

    public MarkerView getMarkerView() {
        return this.mMarkerView;
    }

    public Legend getLegend() {
        return this.mLegend;
    }

    public LegendRenderer getLegendRenderer() {
        return this.mLegendRenderer;
    }

    public RectF getContentRect() {
        return this.mViewPortHandler.getContentRect();
    }

    public void disableScroll() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    public void enableScroll() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(false);
        }
    }

    public void setPaint(Paint p, int which) {
        switch (which) {
            case 7:
                this.mInfoPaint = p;
                return;
            case 11:
                this.mDescPaint = p;
                return;
            default:
                return;
        }
    }

    public Paint getPaint(int which) {
        switch (which) {
            case 7:
                return this.mInfoPaint;
            case 11:
                return this.mDescPaint;
            default:
                return null;
        }
    }

    public boolean isDrawMarkerViewEnabled() {
        return this.mDrawMarkerViews;
    }

    public void setDrawMarkerViews(boolean enabled) {
        this.mDrawMarkerViews = enabled;
    }

    public String getXValue(int index) {
        if (this.mData == null || this.mData.getXValCount() <= index) {
            return null;
        }
        return this.mData.getXVals().get(index);
    }

    public List<Entry> getEntriesAtIndex(int xIndex) {
        List<Entry> vals = new ArrayList<>();
        for (int i = 0; i < this.mData.getDataSetCount(); i++) {
            Entry e = this.mData.getDataSetByIndex(i).getEntryForXIndex(xIndex);
            if (e != null) {
                vals.add(e);
            }
        }
        return vals;
    }

    public T getData() {
        return this.mData;
    }

    public ViewPortHandler getViewPortHandler() {
        return this.mViewPortHandler;
    }

    public DataRenderer getRenderer() {
        return this.mRenderer;
    }

    public void setRenderer(DataRenderer renderer) {
        if (renderer != null) {
            this.mRenderer = renderer;
        }
    }

    public ChartHighlighter getHighlighter() {
        return this.mHighlighter;
    }

    public void setHighlighter(ChartHighlighter highlighter) {
        this.mHighlighter = highlighter;
    }

    public PointF getCenterOfView() {
        return getCenter();
    }

    public Bitmap getChartBitmap() {
        Bitmap returnedBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(-1);
        }
        draw(canvas);
        return returnedBitmap;
    }

    public boolean saveToPath(String title, String pathOnSD) {
        Bitmap b = getChartBitmap();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + pathOnSD + "/" + title + ".png");
            try {
                b.compress(Bitmap.CompressFormat.PNG, 40, fileOutputStream);
                fileOutputStream.close();
                FileOutputStream fileOutputStream2 = fileOutputStream;
                return true;
            } catch (Exception e) {
                e = e;
                FileOutputStream fileOutputStream3 = fileOutputStream;
                e.printStackTrace();
                return false;
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveToGallery(String fileName, String subFolderPath, String fileDescription, Bitmap.CompressFormat format, int quality) {
        String mimeType;
        if (quality < 0 || quality > 100) {
            quality = 50;
        }
        long currentTime = System.currentTimeMillis();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/" + subFolderPath);
        if (!file.exists() && !file.mkdirs()) {
            return false;
        }
        switch (AnonymousClass2.$SwitchMap$android$graphics$Bitmap$CompressFormat[format.ordinal()]) {
            case 1:
                mimeType = "image/png";
                if (!fileName.endsWith(".png")) {
                    fileName = fileName + ".png";
                    break;
                }
                break;
            case 2:
                mimeType = "image/webp";
                if (!fileName.endsWith(".webp")) {
                    fileName = fileName + ".webp";
                    break;
                }
                break;
            default:
                mimeType = "image/jpeg";
                if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
                    fileName = fileName + ".jpg";
                    break;
                }
        }
        String filePath = file.getAbsolutePath() + "/" + fileName;
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            try {
                getChartBitmap().compress(format, quality, out);
                out.flush();
                out.close();
                long size = new File(filePath).length();
                ContentValues values = new ContentValues(8);
                values.put("title", fileName);
                values.put("_display_name", fileName);
                values.put("date_added", Long.valueOf(currentTime));
                values.put("mime_type", mimeType);
                values.put("description", fileDescription);
                values.put("orientation", 0);
                values.put("_data", filePath);
                values.put("_size", Long.valueOf(size));
                if (getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) != null) {
                    return true;
                }
                return false;
            } catch (IOException e) {
                e = e;
                FileOutputStream fileOutputStream = out;
                e.printStackTrace();
                return false;
            }
        } catch (IOException e2) {
            e = e2;
            e.printStackTrace();
            return false;
        }
    }

    /* renamed from: com.github.mikephil.charting.charts.Chart$2  reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$CompressFormat = new int[Bitmap.CompressFormat.values().length];

        static {
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.PNG.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.WEBP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.JPEG.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public boolean saveToGallery(String fileName, int quality) {
        return saveToGallery(fileName, "", "MPAndroidChart-Library Save", Bitmap.CompressFormat.JPEG, quality);
    }

    public void removeViewportJob(Runnable job) {
        this.mJobs.remove(job);
    }

    public void clearAllViewportJobs() {
        this.mJobs.clear();
    }

    public void addViewportJob(Runnable job) {
        if (this.mViewPortHandler.hasChartDimens()) {
            post(job);
        } else {
            this.mJobs.add(job);
        }
    }

    public ArrayList<Runnable> getJobs() {
        return this.mJobs;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(left, top, right, bottom);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = (int) Utils.convertDpToPixel(50.0f);
        setMeasuredDimension(Math.max(getSuggestedMinimumWidth(), resolveSize(size, widthMeasureSpec)), Math.max(getSuggestedMinimumHeight(), resolveSize(size, heightMeasureSpec)));
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (this.mLogEnabled) {
            Log.i(LOG_TAG, "OnSizeChanged()");
        }
        if (w > 0 && h > 0 && w < 10000 && h < 10000) {
            this.mViewPortHandler.setChartDimens((float) w, (float) h);
            if (this.mLogEnabled) {
                Log.i(LOG_TAG, "Setting chart dimens, width: " + w + ", height: " + h);
            }
            Iterator i$ = this.mJobs.iterator();
            while (i$.hasNext()) {
                post(i$.next());
            }
            this.mJobs.clear();
        }
        notifyDataSetChanged();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setHardwareAccelerationEnabled(boolean enabled) {
        if (Build.VERSION.SDK_INT < 11) {
            Log.e(LOG_TAG, "Cannot enable/disable hardware acceleration for devices below API level 11.");
        } else if (enabled) {
            setLayerType(2, (Paint) null);
        } else {
            setLayerType(1, (Paint) null);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mUnbind) {
            unbindDrawables(this);
        }
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback((Drawable.Callback) null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    public void setUnbindEnabled(boolean enabled) {
        this.mUnbind = enabled;
    }
}
