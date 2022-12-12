package com.github.mikephil.charting.charts;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.PieRadarChartTouchListener;
import com.github.mikephil.charting.utils.SelectionDetail;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public abstract class PieRadarChartBase<T extends ChartData<? extends IDataSet<? extends Entry>>> extends Chart<T> {
    protected float mMinOffset = 0.0f;
    private float mRawRotationAngle = 270.0f;
    protected boolean mRotateEnabled = true;
    private float mRotationAngle = 270.0f;

    public abstract int getIndexForAngle(float f);

    public abstract float getRadius();

    /* access modifiers changed from: protected */
    public abstract float getRequiredBaseOffset();

    /* access modifiers changed from: protected */
    public abstract float getRequiredLegendOffset();

    public PieRadarChartBase(Context context) {
        super(context);
    }

    public PieRadarChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieRadarChartBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* access modifiers changed from: protected */
    public void init() {
        super.init();
        this.mChartTouchListener = new PieRadarChartTouchListener(this);
    }

    /* access modifiers changed from: protected */
    public void calcMinMax() {
        this.mXAxis.mAxisRange = (float) (this.mData.getXVals().size() - 1);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mTouchEnabled || this.mChartTouchListener == null) {
            return super.onTouchEvent(event);
        }
        return this.mChartTouchListener.onTouch(this, event);
    }

    public void computeScroll() {
        if (this.mChartTouchListener instanceof PieRadarChartTouchListener) {
            ((PieRadarChartTouchListener) this.mChartTouchListener).computeScroll();
        }
    }

    public void notifyDataSetChanged() {
        if (this.mData != null) {
            calcMinMax();
            if (this.mLegend != null) {
                this.mLegendRenderer.computeLegend(this.mData);
            }
            calculateOffsets();
        }
    }

    public void calculateOffsets() {
        float legendLeft = 0.0f;
        float legendRight = 0.0f;
        float legendBottom = 0.0f;
        float legendTop = 0.0f;
        if (this.mLegend != null && this.mLegend.isEnabled()) {
            float fullLegendWidth = Math.min(this.mLegend.mNeededWidth, this.mViewPortHandler.getChartWidth() * this.mLegend.getMaxSizePercent()) + this.mLegend.getFormSize() + this.mLegend.getFormToTextSpace();
            if (this.mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART_CENTER) {
                legendRight = fullLegendWidth + Utils.convertDpToPixel(13.0f);
            } else {
                if (this.mLegend.getPosition() == Legend.LegendPosition.RIGHT_OF_CHART) {
                    float legendWidth = fullLegendWidth + Utils.convertDpToPixel(8.0f);
                    float legendHeight = this.mLegend.mNeededHeight + this.mLegend.mTextHeightMax;
                    PointF c = getCenter();
                    PointF bottomRight = new PointF((((float) getWidth()) - legendWidth) + 15.0f, 15.0f + legendHeight);
                    float distLegend = distanceToCenter(bottomRight.x, bottomRight.y);
                    PointF reference = getPosition(c, getRadius(), getAngleForPoint(bottomRight.x, bottomRight.y));
                    float distReference = distanceToCenter(reference.x, reference.y);
                    float min = Utils.convertDpToPixel(5.0f);
                    if (distLegend < distReference) {
                        legendRight = min + (distReference - distLegend);
                    }
                    if (bottomRight.y >= c.y && ((float) getHeight()) - legendWidth > ((float) getWidth())) {
                        legendRight = legendWidth;
                    }
                } else {
                    if (this.mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART_CENTER) {
                        legendLeft = fullLegendWidth + Utils.convertDpToPixel(13.0f);
                    } else {
                        if (this.mLegend.getPosition() == Legend.LegendPosition.LEFT_OF_CHART) {
                            float legendWidth2 = fullLegendWidth + Utils.convertDpToPixel(8.0f);
                            float legendHeight2 = this.mLegend.mNeededHeight + this.mLegend.mTextHeightMax;
                            PointF c2 = getCenter();
                            PointF bottomLeft = new PointF(legendWidth2 - 15.0f, 15.0f + legendHeight2);
                            float distLegend2 = distanceToCenter(bottomLeft.x, bottomLeft.y);
                            PointF reference2 = getPosition(c2, getRadius(), getAngleForPoint(bottomLeft.x, bottomLeft.y));
                            float distReference2 = distanceToCenter(reference2.x, reference2.y);
                            float min2 = Utils.convertDpToPixel(5.0f);
                            if (distLegend2 < distReference2) {
                                legendLeft = min2 + (distReference2 - distLegend2);
                            }
                            if (bottomLeft.y >= c2.y && ((float) getHeight()) - legendWidth2 > ((float) getWidth())) {
                                legendLeft = legendWidth2;
                            }
                        } else {
                            if (this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_LEFT || this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_RIGHT || this.mLegend.getPosition() == Legend.LegendPosition.BELOW_CHART_CENTER) {
                                legendBottom = Math.min(this.mLegend.mNeededHeight + getRequiredLegendOffset(), this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
                            } else {
                                if (this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_LEFT || this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_RIGHT || this.mLegend.getPosition() == Legend.LegendPosition.ABOVE_CHART_CENTER) {
                                    legendTop = Math.min(this.mLegend.mNeededHeight + getRequiredLegendOffset(), this.mViewPortHandler.getChartHeight() * this.mLegend.getMaxSizePercent());
                                }
                            }
                        }
                    }
                }
            }
            legendLeft += getRequiredBaseOffset();
            legendRight += getRequiredBaseOffset();
            legendTop += getRequiredBaseOffset();
        }
        float minOffset = Utils.convertDpToPixel(this.mMinOffset);
        if (this instanceof RadarChart) {
            XAxis x = ((RadarChart) this).getXAxis();
            if (x.isEnabled() && x.isDrawLabelsEnabled()) {
                minOffset = Math.max(minOffset, (float) x.mLabelRotatedWidth);
            }
        }
        float legendTop2 = legendTop + getExtraTopOffset();
        float legendRight2 = legendRight + getExtraRightOffset();
        float legendBottom2 = legendBottom + getExtraBottomOffset();
        float offsetLeft = Math.max(minOffset, legendLeft + getExtraLeftOffset());
        float offsetTop = Math.max(minOffset, legendTop2);
        float offsetRight = Math.max(minOffset, legendRight2);
        float offsetBottom = Math.max(minOffset, Math.max(getRequiredBaseOffset(), legendBottom2));
        this.mViewPortHandler.restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom);
        if (this.mLogEnabled) {
            Log.i(Chart.LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom);
        }
    }

    public float getAngleForPoint(float x, float y) {
        PointF c = getCenterOffsets();
        double tx = (double) (x - c.x);
        double ty = (double) (y - c.y);
        float angle = (float) Math.toDegrees(Math.acos(ty / Math.sqrt((tx * tx) + (ty * ty))));
        if (x > c.x) {
            angle = 360.0f - angle;
        }
        float angle2 = angle + 90.0f;
        if (angle2 > 360.0f) {
            return angle2 - 360.0f;
        }
        return angle2;
    }

    /* access modifiers changed from: protected */
    public PointF getPosition(PointF center, float dist, float angle) {
        return new PointF((float) (((double) center.x) + (((double) dist) * Math.cos(Math.toRadians((double) angle)))), (float) (((double) center.y) + (((double) dist) * Math.sin(Math.toRadians((double) angle)))));
    }

    public float distanceToCenter(float x, float y) {
        float xDist;
        float yDist;
        PointF c = getCenterOffsets();
        if (x > c.x) {
            xDist = x - c.x;
        } else {
            xDist = c.x - x;
        }
        if (y > c.y) {
            yDist = y - c.y;
        } else {
            yDist = c.y - y;
        }
        return (float) Math.sqrt(Math.pow((double) xDist, 2.0d) + Math.pow((double) yDist, 2.0d));
    }

    public void setRotationAngle(float angle) {
        this.mRawRotationAngle = angle;
        this.mRotationAngle = Utils.getNormalizedAngle(this.mRawRotationAngle);
    }

    public float getRawRotationAngle() {
        return this.mRawRotationAngle;
    }

    public float getRotationAngle() {
        return this.mRotationAngle;
    }

    public void setRotationEnabled(boolean enabled) {
        this.mRotateEnabled = enabled;
    }

    public boolean isRotationEnabled() {
        return this.mRotateEnabled;
    }

    public float getMinOffset() {
        return this.mMinOffset;
    }

    public void setMinOffset(float minOffset) {
        this.mMinOffset = minOffset;
    }

    public float getDiameter() {
        RectF content = this.mViewPortHandler.getContentRect();
        return Math.min(content.width(), content.height());
    }

    public float getYChartMax() {
        return 0.0f;
    }

    public float getYChartMin() {
        return 0.0f;
    }

    public List<SelectionDetail> getSelectionDetailsAtIndex(int xIndex) {
        List<SelectionDetail> vals = new ArrayList<>();
        for (int i = 0; i < this.mData.getDataSetCount(); i++) {
            IDataSet<?> dataSet = this.mData.getDataSetByIndex(i);
            float yVal = dataSet.getYValForXIndex(xIndex);
            if (yVal != Float.NaN) {
                vals.add(new SelectionDetail(yVal, i, dataSet));
            }
        }
        return vals;
    }

    @SuppressLint({"NewApi"})
    public void spin(int durationmillis, float fromangle, float toangle, Easing.EasingOption easing) {
        if (Build.VERSION.SDK_INT >= 11) {
            setRotationAngle(fromangle);
            ObjectAnimator spinAnimator = ObjectAnimator.ofFloat(this, "rotationAngle", new float[]{fromangle, toangle});
            spinAnimator.setDuration((long) durationmillis);
            spinAnimator.setInterpolator(Easing.getEasingFunctionFromOption(easing));
            spinAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    PieRadarChartBase.this.postInvalidate();
                }
            });
            spinAnimator.start();
        }
    }
}
