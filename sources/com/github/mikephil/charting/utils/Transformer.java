package com.github.mikephil.charting.utils;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import java.util.List;

public class Transformer {
    private Matrix mMBuffer1 = new Matrix();
    private Matrix mMBuffer2 = new Matrix();
    protected Matrix mMatrixOffset = new Matrix();
    protected Matrix mMatrixValueToPx = new Matrix();
    protected ViewPortHandler mViewPortHandler;

    public Transformer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }

    public void prepareMatrixValuePx(float xChartMin, float deltaX, float deltaY, float yChartMin) {
        float scaleX = this.mViewPortHandler.contentWidth() / deltaX;
        float scaleY = this.mViewPortHandler.contentHeight() / deltaY;
        if (Float.isInfinite(scaleX)) {
            scaleX = 0.0f;
        }
        if (Float.isInfinite(scaleY)) {
            scaleY = 0.0f;
        }
        this.mMatrixValueToPx.reset();
        this.mMatrixValueToPx.postTranslate(-xChartMin, -yChartMin);
        this.mMatrixValueToPx.postScale(scaleX, -scaleY);
    }

    public void prepareMatrixOffset(boolean inverted) {
        this.mMatrixOffset.reset();
        if (!inverted) {
            this.mMatrixOffset.postTranslate(this.mViewPortHandler.offsetLeft(), this.mViewPortHandler.getChartHeight() - this.mViewPortHandler.offsetBottom());
            return;
        }
        this.mMatrixOffset.setTranslate(this.mViewPortHandler.offsetLeft(), -this.mViewPortHandler.offsetTop());
        this.mMatrixOffset.postScale(1.0f, -1.0f);
    }

    public float[] generateTransformedValuesScatter(IScatterDataSet data, float phaseY) {
        float[] valuePoints = new float[(data.getEntryCount() * 2)];
        for (int j = 0; j < valuePoints.length; j += 2) {
            Entry e = data.getEntryForIndex(j / 2);
            if (e != null) {
                valuePoints[j] = (float) e.getXIndex();
                valuePoints[j + 1] = e.getVal() * phaseY;
            }
        }
        getValueToPixelMatrix().mapPoints(valuePoints);
        return valuePoints;
    }

    public float[] generateTransformedValuesBubble(IBubbleDataSet data, float phaseX, float phaseY, int from, int to) {
        int count = ((int) Math.ceil((double) (to - from))) * 2;
        float[] valuePoints = new float[count];
        for (int j = 0; j < count; j += 2) {
            Entry e = data.getEntryForIndex((j / 2) + from);
            if (e != null) {
                valuePoints[j] = (((float) (e.getXIndex() - from)) * phaseX) + ((float) from);
                valuePoints[j + 1] = e.getVal() * phaseY;
            }
        }
        getValueToPixelMatrix().mapPoints(valuePoints);
        return valuePoints;
    }

    public float[] generateTransformedValuesLine(ILineDataSet data, float phaseX, float phaseY, int from, int to) {
        int count = ((int) Math.ceil((double) (((float) (to - from)) * phaseX))) * 2;
        float[] valuePoints = new float[count];
        for (int j = 0; j < count; j += 2) {
            Entry e = data.getEntryForIndex((j / 2) + from);
            if (e != null) {
                valuePoints[j] = (float) e.getXIndex();
                valuePoints[j + 1] = e.getVal() * phaseY;
            }
        }
        getValueToPixelMatrix().mapPoints(valuePoints);
        return valuePoints;
    }

    public float[] generateTransformedValuesCandle(ICandleDataSet data, float phaseX, float phaseY, int from, int to) {
        int count = ((int) Math.ceil((double) (((float) (to - from)) * phaseX))) * 2;
        float[] valuePoints = new float[count];
        for (int j = 0; j < count; j += 2) {
            CandleEntry e = (CandleEntry) data.getEntryForIndex((j / 2) + from);
            if (e != null) {
                valuePoints[j] = (float) e.getXIndex();
                valuePoints[j + 1] = e.getHigh() * phaseY;
            }
        }
        getValueToPixelMatrix().mapPoints(valuePoints);
        return valuePoints;
    }

    public float[] generateTransformedValuesBarChart(IBarDataSet data, int dataSetIndex, BarData bd, float phaseY) {
        float[] valuePoints = new float[(data.getEntryCount() * 2)];
        int setCount = bd.getDataSetCount();
        float space = bd.getGroupSpace();
        for (int j = 0; j < valuePoints.length; j += 2) {
            Entry e = data.getEntryForIndex(j / 2);
            int i = e.getXIndex();
            float y = e.getVal();
            valuePoints[j] = ((float) (e.getXIndex() + ((setCount - 1) * i) + dataSetIndex)) + (((float) i) * space) + (space / 2.0f);
            valuePoints[j + 1] = y * phaseY;
        }
        getValueToPixelMatrix().mapPoints(valuePoints);
        return valuePoints;
    }

    public float[] generateTransformedValuesHorizontalBarChart(IBarDataSet data, int dataSet, BarData bd, float phaseY) {
        float[] valuePoints = new float[(data.getEntryCount() * 2)];
        int setCount = bd.getDataSetCount();
        float space = bd.getGroupSpace();
        for (int j = 0; j < valuePoints.length; j += 2) {
            Entry e = data.getEntryForIndex(j / 2);
            int i = e.getXIndex();
            valuePoints[j] = e.getVal() * phaseY;
            valuePoints[j + 1] = ((float) (((setCount - 1) * i) + i + dataSet)) + (((float) i) * space) + (space / 2.0f);
        }
        getValueToPixelMatrix().mapPoints(valuePoints);
        return valuePoints;
    }

    public void pathValueToPixel(Path path) {
        path.transform(this.mMatrixValueToPx);
        path.transform(this.mViewPortHandler.getMatrixTouch());
        path.transform(this.mMatrixOffset);
    }

    public void pathValuesToPixel(List<Path> paths) {
        for (int i = 0; i < paths.size(); i++) {
            pathValueToPixel(paths.get(i));
        }
    }

    public void pointValuesToPixel(float[] pts) {
        this.mMatrixValueToPx.mapPoints(pts);
        this.mViewPortHandler.getMatrixTouch().mapPoints(pts);
        this.mMatrixOffset.mapPoints(pts);
    }

    public void rectValueToPixel(RectF r) {
        this.mMatrixValueToPx.mapRect(r);
        this.mViewPortHandler.getMatrixTouch().mapRect(r);
        this.mMatrixOffset.mapRect(r);
    }

    public void rectValueToPixel(RectF r, float phaseY) {
        r.top *= phaseY;
        r.bottom *= phaseY;
        this.mMatrixValueToPx.mapRect(r);
        this.mViewPortHandler.getMatrixTouch().mapRect(r);
        this.mMatrixOffset.mapRect(r);
    }

    public void rectValueToPixelHorizontal(RectF r) {
        this.mMatrixValueToPx.mapRect(r);
        this.mViewPortHandler.getMatrixTouch().mapRect(r);
        this.mMatrixOffset.mapRect(r);
    }

    public void rectValueToPixelHorizontal(RectF r, float phaseY) {
        r.left *= phaseY;
        r.right *= phaseY;
        this.mMatrixValueToPx.mapRect(r);
        this.mViewPortHandler.getMatrixTouch().mapRect(r);
        this.mMatrixOffset.mapRect(r);
    }

    public void rectValuesToPixel(List<RectF> rects) {
        Matrix m = getValueToPixelMatrix();
        for (int i = 0; i < rects.size(); i++) {
            m.mapRect(rects.get(i));
        }
    }

    public void pixelsToValue(float[] pixels) {
        Matrix tmp = new Matrix();
        this.mMatrixOffset.invert(tmp);
        tmp.mapPoints(pixels);
        this.mViewPortHandler.getMatrixTouch().invert(tmp);
        tmp.mapPoints(pixels);
        this.mMatrixValueToPx.invert(tmp);
        tmp.mapPoints(pixels);
    }

    public PointD getValuesByTouchPoint(float x, float y) {
        float[] pts = {x, y};
        pixelsToValue(pts);
        return new PointD((double) pts[0], (double) pts[1]);
    }

    public Matrix getValueMatrix() {
        return this.mMatrixValueToPx;
    }

    public Matrix getOffsetMatrix() {
        return this.mMatrixOffset;
    }

    public Matrix getValueToPixelMatrix() {
        this.mMBuffer1.set(this.mMatrixValueToPx);
        this.mMBuffer1.postConcat(this.mViewPortHandler.mMatrixTouch);
        this.mMBuffer1.postConcat(this.mMatrixOffset);
        return this.mMBuffer1;
    }

    public Matrix getPixelToValueMatrix() {
        getValueToPixelMatrix().invert(this.mMBuffer2);
        return this.mMBuffer2;
    }
}
