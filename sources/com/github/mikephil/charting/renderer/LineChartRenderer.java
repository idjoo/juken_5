package com.github.mikephil.charting.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.lang.ref.WeakReference;
import java.util.List;

public class LineChartRenderer extends LineRadarRenderer {
    protected Path cubicFillPath = new Path();
    protected Path cubicPath = new Path();
    protected Canvas mBitmapCanvas;
    protected Bitmap.Config mBitmapConfig = Bitmap.Config.ARGB_8888;
    protected LineDataProvider mChart;
    protected Paint mCirclePaintInner;
    protected WeakReference<Bitmap> mDrawBitmap;
    private float[] mLineBuffer = new float[4];

    public LineChartRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;
        this.mCirclePaintInner = new Paint(1);
        this.mCirclePaintInner.setStyle(Paint.Style.FILL);
        this.mCirclePaintInner.setColor(-1);
    }

    public void initBuffers() {
    }

    public void drawData(Canvas c) {
        int width = (int) this.mViewPortHandler.getChartWidth();
        int height = (int) this.mViewPortHandler.getChartHeight();
        if (!(this.mDrawBitmap != null && ((Bitmap) this.mDrawBitmap.get()).getWidth() == width && ((Bitmap) this.mDrawBitmap.get()).getHeight() == height)) {
            if (width > 0 && height > 0) {
                this.mDrawBitmap = new WeakReference<>(Bitmap.createBitmap(width, height, this.mBitmapConfig));
                this.mBitmapCanvas = new Canvas((Bitmap) this.mDrawBitmap.get());
            } else {
                return;
            }
        }
        ((Bitmap) this.mDrawBitmap.get()).eraseColor(0);
        for (ILineDataSet set : this.mChart.getLineData().getDataSets()) {
            if (set.isVisible() && set.getEntryCount() > 0) {
                drawDataSet(c, set);
            }
        }
        c.drawBitmap((Bitmap) this.mDrawBitmap.get(), 0.0f, 0.0f, this.mRenderPaint);
    }

    /* access modifiers changed from: protected */
    public void drawDataSet(Canvas c, ILineDataSet dataSet) {
        if (dataSet.getEntryCount() >= 1) {
            this.mRenderPaint.setStrokeWidth(dataSet.getLineWidth());
            this.mRenderPaint.setPathEffect(dataSet.getDashPathEffect());
            if (dataSet.isDrawCubicEnabled()) {
                drawCubic(c, dataSet);
            } else {
                drawLinear(c, dataSet);
            }
            this.mRenderPaint.setPathEffect((PathEffect) null);
        }
    }

    /* access modifiers changed from: protected */
    public void drawCubic(Canvas c, ILineDataSet dataSet) {
        Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        int entryCount = dataSet.getEntryCount();
        Entry entryFrom = dataSet.getEntryForXIndex(this.mMinX < 0 ? 0 : this.mMinX, DataSet.Rounding.DOWN);
        Entry entryTo = dataSet.getEntryForXIndex(this.mMaxX, DataSet.Rounding.UP);
        int minx = Math.max(dataSet.getEntryIndex(entryFrom) - (entryFrom == entryTo ? 1 : 0), 0);
        int maxx = Math.min(Math.max(minx + 2, dataSet.getEntryIndex(entryTo) + 1), entryCount);
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float intensity = dataSet.getCubicIntensity();
        this.cubicPath.reset();
        int size = (int) Math.ceil((double) ((((float) (maxx - minx)) * phaseX) + ((float) minx)));
        if (size - minx >= 2) {
            Entry cur = dataSet.getEntryForIndex(minx);
            Entry entryForIndex = dataSet.getEntryForIndex(minx + 1);
            this.cubicPath.moveTo((float) cur.getXIndex(), cur.getVal() * phaseY);
            int j = minx + 1;
            int count = Math.min(size, entryCount - 1);
            while (j < count) {
                Entry prevPrev = dataSet.getEntryForIndex(j == 1 ? 0 : j - 2);
                Entry prev = dataSet.getEntryForIndex(j - 1);
                Entry cur2 = dataSet.getEntryForIndex(j);
                Entry next = dataSet.getEntryForIndex(j + 1);
                this.cubicPath.cubicTo(((float) prev.getXIndex()) + (((float) (cur2.getXIndex() - prevPrev.getXIndex())) * intensity), (prev.getVal() + ((cur2.getVal() - prevPrev.getVal()) * intensity)) * phaseY, ((float) cur2.getXIndex()) - (((float) (next.getXIndex() - prev.getXIndex())) * intensity), (cur2.getVal() - ((next.getVal() - prev.getVal()) * intensity)) * phaseY, (float) cur2.getXIndex(), cur2.getVal() * phaseY);
                j++;
            }
            if (size > entryCount - 1) {
                Entry prevPrev2 = dataSet.getEntryForIndex(entryCount >= 3 ? entryCount - 3 : entryCount - 2);
                Entry prev2 = dataSet.getEntryForIndex(entryCount - 2);
                Entry cur3 = dataSet.getEntryForIndex(entryCount - 1);
                Entry next2 = cur3;
                this.cubicPath.cubicTo(((float) prev2.getXIndex()) + (((float) (cur3.getXIndex() - prevPrev2.getXIndex())) * intensity), (prev2.getVal() + ((cur3.getVal() - prevPrev2.getVal()) * intensity)) * phaseY, ((float) cur3.getXIndex()) - (((float) (next2.getXIndex() - prev2.getXIndex())) * intensity), (cur3.getVal() - ((next2.getVal() - prev2.getVal()) * intensity)) * phaseY, (float) cur3.getXIndex(), cur3.getVal() * phaseY);
            }
        }
        if (dataSet.isDrawFilledEnabled()) {
            this.cubicFillPath.reset();
            this.cubicFillPath.addPath(this.cubicPath);
            drawCubicFill(this.mBitmapCanvas, dataSet, this.cubicFillPath, trans, minx, size);
        }
        this.mRenderPaint.setColor(dataSet.getColor());
        this.mRenderPaint.setStyle(Paint.Style.STROKE);
        trans.pathValueToPixel(this.cubicPath);
        this.mBitmapCanvas.drawPath(this.cubicPath, this.mRenderPaint);
        this.mRenderPaint.setPathEffect((PathEffect) null);
    }

    /* access modifiers changed from: protected */
    public void drawCubicFill(Canvas c, ILineDataSet dataSet, Path spline, Transformer trans, int from, int to) {
        float xFrom = 0.0f;
        if (to - from > 1) {
            float fillMin = dataSet.getFillFormatter().getFillLinePosition(dataSet, this.mChart);
            Entry toEntry = dataSet.getEntryForIndex(to - 1);
            Entry fromEntry = dataSet.getEntryForIndex(from);
            float xTo = toEntry == null ? 0.0f : (float) toEntry.getXIndex();
            if (fromEntry != null) {
                xFrom = (float) fromEntry.getXIndex();
            }
            spline.lineTo(xTo, fillMin);
            spline.lineTo(xFrom, fillMin);
            spline.close();
            trans.pathValueToPixel(spline);
            Drawable drawable = dataSet.getFillDrawable();
            if (drawable != null) {
                drawFilledPath(c, spline, drawable);
            } else {
                drawFilledPath(c, spline, dataSet.getFillColor(), dataSet.getFillAlpha());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawLinear(Canvas c, ILineDataSet dataSet) {
        Canvas canvas;
        int entryCount = dataSet.getEntryCount();
        boolean isDrawSteppedEnabled = dataSet.isDrawSteppedEnabled();
        int pointsPerEntryPair = isDrawSteppedEnabled ? 4 : 2;
        Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        this.mRenderPaint.setStyle(Paint.Style.STROKE);
        if (dataSet.isDashedLineEnabled()) {
            canvas = this.mBitmapCanvas;
        } else {
            canvas = c;
        }
        Entry entryFrom = dataSet.getEntryForXIndex(this.mMinX < 0 ? 0 : this.mMinX, DataSet.Rounding.DOWN);
        Entry entryTo = dataSet.getEntryForXIndex(this.mMaxX, DataSet.Rounding.UP);
        int minx = Math.max(dataSet.getEntryIndex(entryFrom) - (entryFrom == entryTo ? 1 : 0), 0);
        int maxx = Math.min(Math.max(minx + 2, dataSet.getEntryIndex(entryTo) + 1), entryCount);
        int count = (int) Math.ceil((double) ((((float) (maxx - minx)) * phaseX) + ((float) minx)));
        if (dataSet.getColors().size() > 1) {
            if (this.mLineBuffer.length != pointsPerEntryPair * 2) {
                this.mLineBuffer = new float[(pointsPerEntryPair * 2)];
            }
            int j = minx;
            while (j < count && (count <= 1 || j != count - 1)) {
                Entry e = dataSet.getEntryForIndex(j);
                if (e != null) {
                    this.mLineBuffer[0] = (float) e.getXIndex();
                    this.mLineBuffer[1] = e.getVal() * phaseY;
                    if (j + 1 < count) {
                        Entry e2 = dataSet.getEntryForIndex(j + 1);
                        if (e2 == null) {
                            break;
                        } else if (isDrawSteppedEnabled) {
                            this.mLineBuffer[2] = (float) e2.getXIndex();
                            this.mLineBuffer[3] = this.mLineBuffer[1];
                            this.mLineBuffer[4] = this.mLineBuffer[2];
                            this.mLineBuffer[5] = this.mLineBuffer[3];
                            this.mLineBuffer[6] = (float) e2.getXIndex();
                            this.mLineBuffer[7] = e2.getVal() * phaseY;
                        } else {
                            this.mLineBuffer[2] = (float) e2.getXIndex();
                            this.mLineBuffer[3] = e2.getVal() * phaseY;
                        }
                    } else {
                        this.mLineBuffer[2] = this.mLineBuffer[0];
                        this.mLineBuffer[3] = this.mLineBuffer[1];
                    }
                    trans.pointValuesToPixel(this.mLineBuffer);
                    if (!this.mViewPortHandler.isInBoundsRight(this.mLineBuffer[0])) {
                        break;
                    } else if (this.mViewPortHandler.isInBoundsLeft(this.mLineBuffer[2]) && ((this.mViewPortHandler.isInBoundsTop(this.mLineBuffer[1]) || this.mViewPortHandler.isInBoundsBottom(this.mLineBuffer[3])) && (this.mViewPortHandler.isInBoundsTop(this.mLineBuffer[1]) || this.mViewPortHandler.isInBoundsBottom(this.mLineBuffer[3])))) {
                        this.mRenderPaint.setColor(dataSet.getColor(j));
                        canvas.drawLines(this.mLineBuffer, 0, pointsPerEntryPair * 2, this.mRenderPaint);
                    }
                }
                j++;
            }
        } else {
            if (this.mLineBuffer.length != Math.max((entryCount - 1) * pointsPerEntryPair, pointsPerEntryPair) * 2) {
                this.mLineBuffer = new float[(Math.max((entryCount - 1) * pointsPerEntryPair, pointsPerEntryPair) * 2)];
            }
            if (dataSet.getEntryForIndex(minx) != null) {
                int x = count > 1 ? minx + 1 : minx;
                int j2 = 0;
                while (true) {
                    int j3 = j2;
                    if (x >= count) {
                        break;
                    }
                    Entry e1 = dataSet.getEntryForIndex(x == 0 ? 0 : x - 1);
                    Entry e22 = dataSet.getEntryForIndex(x);
                    if (e1 == null) {
                        j2 = j3;
                    } else if (e22 == null) {
                        j2 = j3;
                    } else {
                        int j4 = j3 + 1;
                        this.mLineBuffer[j3] = (float) e1.getXIndex();
                        int j5 = j4 + 1;
                        this.mLineBuffer[j4] = e1.getVal() * phaseY;
                        if (isDrawSteppedEnabled) {
                            int j6 = j5 + 1;
                            this.mLineBuffer[j5] = (float) e22.getXIndex();
                            int j7 = j6 + 1;
                            this.mLineBuffer[j6] = e1.getVal() * phaseY;
                            int j8 = j7 + 1;
                            this.mLineBuffer[j7] = (float) e22.getXIndex();
                            j5 = j8 + 1;
                            this.mLineBuffer[j8] = e1.getVal() * phaseY;
                        }
                        int j9 = j5;
                        int j10 = j9 + 1;
                        this.mLineBuffer[j9] = (float) e22.getXIndex();
                        j2 = j10 + 1;
                        this.mLineBuffer[j10] = e22.getVal() * phaseY;
                    }
                    x++;
                }
                trans.pointValuesToPixel(this.mLineBuffer);
                int size = Math.max(((count - minx) - 1) * pointsPerEntryPair, pointsPerEntryPair) * 2;
                this.mRenderPaint.setColor(dataSet.getColor());
                canvas.drawLines(this.mLineBuffer, 0, size, this.mRenderPaint);
            }
        }
        this.mRenderPaint.setPathEffect((PathEffect) null);
        if (dataSet.isDrawFilledEnabled() && entryCount > 0) {
            drawLinearFill(c, dataSet, minx, maxx, trans);
        }
    }

    /* access modifiers changed from: protected */
    public void drawLinearFill(Canvas c, ILineDataSet dataSet, int minx, int maxx, Transformer trans) {
        Path filled = generateFilledPath(dataSet, minx, maxx);
        trans.pathValueToPixel(filled);
        Drawable drawable = dataSet.getFillDrawable();
        if (drawable != null) {
            drawFilledPath(c, filled, drawable);
        } else {
            drawFilledPath(c, filled, dataSet.getFillColor(), dataSet.getFillAlpha());
        }
    }

    private Path generateFilledPath(ILineDataSet dataSet, int from, int to) {
        float fillMin = dataSet.getFillFormatter().getFillLinePosition(dataSet, this.mChart);
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        boolean isDrawSteppedEnabled = dataSet.isDrawSteppedEnabled();
        Path filled = new Path();
        Entry entry = dataSet.getEntryForIndex(from);
        filled.moveTo((float) entry.getXIndex(), fillMin);
        filled.lineTo((float) entry.getXIndex(), entry.getVal() * phaseY);
        int count = (int) Math.ceil((double) ((((float) (to - from)) * phaseX) + ((float) from)));
        for (int x = from + 1; x < count; x++) {
            Entry e = dataSet.getEntryForIndex(x);
            if (isDrawSteppedEnabled) {
                Entry ePrev = dataSet.getEntryForIndex(x - 1);
                if (ePrev == null) {
                } else {
                    filled.lineTo((float) e.getXIndex(), ePrev.getVal() * phaseY);
                }
            }
            filled.lineTo((float) e.getXIndex(), e.getVal() * phaseY);
        }
        filled.lineTo((float) dataSet.getEntryForIndex(Math.max(Math.min(((int) Math.ceil((double) ((((float) (to - from)) * phaseX) + ((float) from)))) - 1, dataSet.getEntryCount() - 1), 0)).getXIndex(), fillMin);
        filled.close();
        return filled;
    }

    public void drawValues(Canvas c) {
        if (((float) this.mChart.getLineData().getYValCount()) < ((float) this.mChart.getMaxVisibleCount()) * this.mViewPortHandler.getScaleX()) {
            List<ILineDataSet> dataSets = this.mChart.getLineData().getDataSets();
            for (int i = 0; i < dataSets.size(); i++) {
                ILineDataSet dataSet = dataSets.get(i);
                if (dataSet.isDrawValuesEnabled() && dataSet.getEntryCount() != 0) {
                    applyValueTextStyle(dataSet);
                    Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                    int valOffset = (int) (dataSet.getCircleRadius() * 1.75f);
                    if (!dataSet.isDrawCirclesEnabled()) {
                        valOffset /= 2;
                    }
                    int entryCount = dataSet.getEntryCount();
                    Entry entryFrom = dataSet.getEntryForXIndex(this.mMinX < 0 ? 0 : this.mMinX, DataSet.Rounding.DOWN);
                    Entry entryTo = dataSet.getEntryForXIndex(this.mMaxX, DataSet.Rounding.UP);
                    int minx = Math.max(dataSet.getEntryIndex(entryFrom) - (entryFrom == entryTo ? 1 : 0), 0);
                    float[] positions = trans.generateTransformedValuesLine(dataSet, this.mAnimator.getPhaseX(), this.mAnimator.getPhaseY(), minx, Math.min(Math.max(minx + 2, dataSet.getEntryIndex(entryTo) + 1), entryCount));
                    for (int j = 0; j < positions.length; j += 2) {
                        float x = positions[j];
                        float y = positions[j + 1];
                        if (!this.mViewPortHandler.isInBoundsRight(x)) {
                            break;
                        }
                        if (this.mViewPortHandler.isInBoundsLeft(x) && this.mViewPortHandler.isInBoundsY(y)) {
                            Entry entry = dataSet.getEntryForIndex((j / 2) + minx);
                            drawValue(c, dataSet.getValueFormatter(), entry.getVal(), entry, i, x, y - ((float) valOffset), dataSet.getValueTextColor(j / 2));
                        }
                    }
                }
            }
        }
    }

    public void drawExtras(Canvas c) {
        drawCircles(c);
    }

    /* access modifiers changed from: protected */
    public void drawCircles(Canvas c) {
        Entry e;
        this.mRenderPaint.setStyle(Paint.Style.FILL);
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float[] circlesBuffer = new float[2];
        List<ILineDataSet> dataSets = this.mChart.getLineData().getDataSets();
        for (int i = 0; i < dataSets.size(); i++) {
            ILineDataSet dataSet = dataSets.get(i);
            if (dataSet.isVisible() && dataSet.isDrawCirclesEnabled() && dataSet.getEntryCount() != 0) {
                this.mCirclePaintInner.setColor(dataSet.getCircleHoleColor());
                Transformer trans = this.mChart.getTransformer(dataSet.getAxisDependency());
                int entryCount = dataSet.getEntryCount();
                Entry entryFrom = dataSet.getEntryForXIndex(this.mMinX < 0 ? 0 : this.mMinX, DataSet.Rounding.DOWN);
                Entry entryTo = dataSet.getEntryForXIndex(this.mMaxX, DataSet.Rounding.UP);
                int minx = Math.max(dataSet.getEntryIndex(entryFrom) - (entryFrom == entryTo ? 1 : 0), 0);
                int maxx = Math.min(Math.max(minx + 2, dataSet.getEntryIndex(entryTo) + 1), entryCount);
                float halfsize = dataSet.getCircleRadius() / 2.0f;
                int j = minx;
                int count = (int) Math.ceil((double) ((((float) (maxx - minx)) * phaseX) + ((float) minx)));
                while (j < count && (e = dataSet.getEntryForIndex(j)) != null) {
                    circlesBuffer[0] = (float) e.getXIndex();
                    circlesBuffer[1] = e.getVal() * phaseY;
                    trans.pointValuesToPixel(circlesBuffer);
                    if (!this.mViewPortHandler.isInBoundsRight(circlesBuffer[0])) {
                        break;
                    }
                    if (this.mViewPortHandler.isInBoundsLeft(circlesBuffer[0]) && this.mViewPortHandler.isInBoundsY(circlesBuffer[1])) {
                        int circleColor = dataSet.getCircleColor(j);
                        this.mRenderPaint.setColor(circleColor);
                        c.drawCircle(circlesBuffer[0], circlesBuffer[1], dataSet.getCircleRadius(), this.mRenderPaint);
                        if (dataSet.isDrawCircleHoleEnabled() && circleColor != this.mCirclePaintInner.getColor()) {
                            c.drawCircle(circlesBuffer[0], circlesBuffer[1], halfsize, this.mCirclePaintInner);
                        }
                    }
                    j++;
                }
            }
        }
    }

    public void drawHighlighted(Canvas c, Highlight[] indices) {
        for (int i = 0; i < indices.length; i++) {
            ILineDataSet set = (ILineDataSet) this.mChart.getLineData().getDataSetByIndex(indices[i].getDataSetIndex());
            if (set != null && set.isHighlightEnabled()) {
                int xIndex = indices[i].getXIndex();
                if (((float) xIndex) <= this.mChart.getXChartMax() * this.mAnimator.getPhaseX()) {
                    float yVal = set.getYValForXIndex(xIndex);
                    if (yVal != Float.NaN) {
                        float[] pts = {(float) xIndex, yVal * this.mAnimator.getPhaseY()};
                        this.mChart.getTransformer(set.getAxisDependency()).pointValuesToPixel(pts);
                        drawHighlightLines(c, pts, set);
                    }
                }
            }
        }
    }

    public void setBitmapConfig(Bitmap.Config config) {
        this.mBitmapConfig = config;
        releaseBitmap();
    }

    public Bitmap.Config getBitmapConfig() {
        return this.mBitmapConfig;
    }

    public void releaseBitmap() {
        if (this.mDrawBitmap != null) {
            ((Bitmap) this.mDrawBitmap.get()).recycle();
            this.mDrawBitmap.clear();
            this.mDrawBitmap = null;
        }
    }
}
