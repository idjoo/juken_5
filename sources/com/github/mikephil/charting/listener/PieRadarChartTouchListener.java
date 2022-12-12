package com.github.mikephil.charting.listener;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.PieRadarChartBase;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.utils.SelectionDetail;
import com.github.mikephil.charting.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class PieRadarChartTouchListener extends ChartTouchListener<PieRadarChartBase<?>> {
    private ArrayList<AngularVelocitySample> _velocitySamples = new ArrayList<>();
    private float mDecelerationAngularVelocity = 0.0f;
    private long mDecelerationLastTime = 0;
    private float mStartAngle = 0.0f;
    private PointF mTouchStartPoint = new PointF();

    public PieRadarChartTouchListener(PieRadarChartBase<?> chart) {
        super(chart);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(View v, MotionEvent event) {
        if (!this.mGestureDetector.onTouchEvent(event) && ((PieRadarChartBase) this.mChart).isRotationEnabled()) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case 0:
                    startAction(event);
                    stopDeceleration();
                    resetVelocity();
                    if (((PieRadarChartBase) this.mChart).isDragDecelerationEnabled()) {
                        sampleVelocity(x, y);
                    }
                    setGestureStartAngle(x, y);
                    this.mTouchStartPoint.x = x;
                    this.mTouchStartPoint.y = y;
                    break;
                case 1:
                    if (((PieRadarChartBase) this.mChart).isDragDecelerationEnabled()) {
                        stopDeceleration();
                        sampleVelocity(x, y);
                        this.mDecelerationAngularVelocity = calculateVelocity();
                        if (this.mDecelerationAngularVelocity != 0.0f) {
                            this.mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis();
                            Utils.postInvalidateOnAnimation(this.mChart);
                        }
                    }
                    ((PieRadarChartBase) this.mChart).enableScroll();
                    this.mTouchMode = 0;
                    endAction(event);
                    break;
                case 2:
                    if (((PieRadarChartBase) this.mChart).isDragDecelerationEnabled()) {
                        sampleVelocity(x, y);
                    }
                    if (this.mTouchMode == 0 && distance(x, this.mTouchStartPoint.x, y, this.mTouchStartPoint.y) > Utils.convertDpToPixel(8.0f)) {
                        this.mLastGesture = ChartTouchListener.ChartGesture.ROTATE;
                        this.mTouchMode = 6;
                        ((PieRadarChartBase) this.mChart).disableScroll();
                    } else if (this.mTouchMode == 6) {
                        updateGestureRotation(x, y);
                        ((PieRadarChartBase) this.mChart).invalidate();
                    }
                    endAction(event);
                    break;
            }
        }
        return true;
    }

    public void onLongPress(MotionEvent me) {
        this.mLastGesture = ChartTouchListener.ChartGesture.LONG_PRESS;
        OnChartGestureListener l = ((PieRadarChartBase) this.mChart).getOnChartGestureListener();
        if (l != null) {
            l.onChartLongPressed(me);
        }
    }

    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    public boolean onSingleTapUp(MotionEvent e) {
        this.mLastGesture = ChartTouchListener.ChartGesture.SINGLE_TAP;
        OnChartGestureListener l = ((PieRadarChartBase) this.mChart).getOnChartGestureListener();
        if (l != null) {
            l.onChartSingleTapped(e);
        }
        if (!((PieRadarChartBase) this.mChart).isHighlightPerTapEnabled()) {
            return false;
        }
        float distance = ((PieRadarChartBase) this.mChart).distanceToCenter(e.getX(), e.getY());
        if (distance > ((PieRadarChartBase) this.mChart).getRadius()) {
            if (this.mLastHighlighted == null) {
                ((PieRadarChartBase) this.mChart).highlightValues((Highlight[]) null);
            } else {
                ((PieRadarChartBase) this.mChart).highlightTouch((Highlight) null);
            }
            this.mLastHighlighted = null;
        } else {
            float angle = ((PieRadarChartBase) this.mChart).getAngleForPoint(e.getX(), e.getY());
            if (this.mChart instanceof PieChart) {
                angle /= ((PieRadarChartBase) this.mChart).getAnimator().getPhaseY();
            }
            int index = ((PieRadarChartBase) this.mChart).getIndexForAngle(angle);
            if (index < 0) {
                ((PieRadarChartBase) this.mChart).highlightValues((Highlight[]) null);
                this.mLastHighlighted = null;
            } else {
                List<SelectionDetail> valsAtIndex = ((PieRadarChartBase) this.mChart).getSelectionDetailsAtIndex(index);
                int dataSetIndex = 0;
                if (this.mChart instanceof RadarChart) {
                    dataSetIndex = Utils.getClosestDataSetIndex(valsAtIndex, distance / ((RadarChart) this.mChart).getFactor(), (YAxis.AxisDependency) null);
                }
                if (dataSetIndex < 0) {
                    ((PieRadarChartBase) this.mChart).highlightValues((Highlight[]) null);
                    this.mLastHighlighted = null;
                } else {
                    performHighlight(new Highlight(index, dataSetIndex), e);
                }
            }
        }
        return true;
    }

    private void resetVelocity() {
        this._velocitySamples.clear();
    }

    private void sampleVelocity(float touchLocationX, float touchLocationY) {
        long currentTime = AnimationUtils.currentAnimationTimeMillis();
        this._velocitySamples.add(new AngularVelocitySample(currentTime, ((PieRadarChartBase) this.mChart).getAngleForPoint(touchLocationX, touchLocationY)));
        int i = 0;
        int count = this._velocitySamples.size();
        while (count - 2 > 0 && currentTime - this._velocitySamples.get(i).time > 1000) {
            this._velocitySamples.remove(0);
            count--;
            i = (i - 1) + 1;
        }
    }

    private float calculateVelocity() {
        if (this._velocitySamples.isEmpty()) {
            return 0.0f;
        }
        AngularVelocitySample firstSample = this._velocitySamples.get(0);
        AngularVelocitySample lastSample = this._velocitySamples.get(this._velocitySamples.size() - 1);
        AngularVelocitySample beforeLastSample = firstSample;
        for (int i = this._velocitySamples.size() - 1; i >= 0; i--) {
            beforeLastSample = this._velocitySamples.get(i);
            if (beforeLastSample.angle != lastSample.angle) {
                break;
            }
        }
        float timeDelta = ((float) (lastSample.time - firstSample.time)) / 1000.0f;
        if (timeDelta == 0.0f) {
            timeDelta = 0.1f;
        }
        boolean clockwise = lastSample.angle >= beforeLastSample.angle;
        if (((double) Math.abs(lastSample.angle - beforeLastSample.angle)) > 270.0d) {
            clockwise = !clockwise;
        }
        if (((double) (lastSample.angle - firstSample.angle)) > 180.0d) {
            firstSample.angle = (float) (((double) firstSample.angle) + 360.0d);
        } else if (((double) (firstSample.angle - lastSample.angle)) > 180.0d) {
            lastSample.angle = (float) (((double) lastSample.angle) + 360.0d);
        }
        float velocity = Math.abs((lastSample.angle - firstSample.angle) / timeDelta);
        if (!clockwise) {
            return -velocity;
        }
        return velocity;
    }

    public void setGestureStartAngle(float x, float y) {
        this.mStartAngle = ((PieRadarChartBase) this.mChart).getAngleForPoint(x, y) - ((PieRadarChartBase) this.mChart).getRawRotationAngle();
    }

    public void updateGestureRotation(float x, float y) {
        ((PieRadarChartBase) this.mChart).setRotationAngle(((PieRadarChartBase) this.mChart).getAngleForPoint(x, y) - this.mStartAngle);
    }

    public void stopDeceleration() {
        this.mDecelerationAngularVelocity = 0.0f;
    }

    public void computeScroll() {
        if (this.mDecelerationAngularVelocity != 0.0f) {
            long currentTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDecelerationAngularVelocity = ((PieRadarChartBase) this.mChart).getDragDecelerationFrictionCoef() * this.mDecelerationAngularVelocity;
            ((PieRadarChartBase) this.mChart).setRotationAngle(((PieRadarChartBase) this.mChart).getRotationAngle() + (this.mDecelerationAngularVelocity * (((float) (currentTime - this.mDecelerationLastTime)) / 1000.0f)));
            this.mDecelerationLastTime = currentTime;
            if (((double) Math.abs(this.mDecelerationAngularVelocity)) >= 0.001d) {
                Utils.postInvalidateOnAnimation(this.mChart);
            } else {
                stopDeceleration();
            }
        }
    }

    private class AngularVelocitySample {
        public float angle;
        public long time;

        public AngularVelocitySample(long time2, float angle2) {
            this.time = time2;
            this.angle = angle2;
        }
    }
}
