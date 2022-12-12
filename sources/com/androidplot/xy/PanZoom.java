package com.androidplot.xy;

import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import com.androidplot.Region;
import com.androidplot.util.RectFUtils;
import java.io.Serializable;
import java.util.EnumSet;

public class PanZoom implements View.OnTouchListener {
    protected static final int FIRST_FINGER = 0;
    protected static final float MIN_DIST_2_FING = 5.0f;
    protected static final int SECOND_FINGER = 1;
    private View.OnTouchListener delegate;
    private DragState dragState = DragState.NONE;
    protected RectF fingersRect;
    private PointF firstFingerPos;
    private boolean isEnabled = true;
    private Pan pan;
    private XYPlot plot;
    private State state = new State();
    private Zoom zoom;
    private ZoomLimit zoomLimit;

    protected enum DragState {
        NONE,
        ONE_FINGER,
        TWO_FINGERS
    }

    public enum Pan {
        NONE,
        HORIZONTAL,
        VERTICAL,
        BOTH
    }

    public enum Zoom {
        NONE,
        STRETCH_HORIZONTAL,
        STRETCH_VERTICAL,
        STRETCH_BOTH,
        SCALE
    }

    public enum ZoomLimit {
        OUTER,
        MIN_TICKS
    }

    public static class State implements Serializable {
        private BoundaryMode domainBoundaryMode;
        private Number domainLowerBoundary;
        private Number domainUpperBoundary;
        private BoundaryMode rangeBoundaryMode;
        private Number rangeLowerBoundary;
        private Number rangeUpperBoundary;

        public void setDomainBoundaries(Number lowerBoundary, Number upperBoundary, BoundaryMode mode) {
            this.domainLowerBoundary = lowerBoundary;
            this.domainUpperBoundary = upperBoundary;
            this.domainBoundaryMode = mode;
        }

        public void setRangeBoundaries(Number lowerBoundary, Number upperBoundary, BoundaryMode mode) {
            this.rangeLowerBoundary = lowerBoundary;
            this.rangeUpperBoundary = upperBoundary;
            this.rangeBoundaryMode = mode;
        }

        public void applyDomainBoundaries(@NonNull XYPlot plot) {
            plot.setDomainBoundaries(this.domainLowerBoundary, this.domainUpperBoundary, this.domainBoundaryMode);
        }

        public void applyRangeBoundaries(@NonNull XYPlot plot) {
            plot.setRangeBoundaries(this.rangeLowerBoundary, this.rangeUpperBoundary, this.rangeBoundaryMode);
        }

        public void apply(@NonNull XYPlot plot) {
            applyDomainBoundaries(plot);
            applyRangeBoundaries(plot);
        }
    }

    protected PanZoom(@NonNull XYPlot plot2, Pan pan2, Zoom zoom2) {
        this.plot = plot2;
        this.pan = pan2;
        this.zoom = zoom2;
        this.zoomLimit = ZoomLimit.OUTER;
    }

    protected PanZoom(@NonNull XYPlot plot2, Pan pan2, Zoom zoom2, ZoomLimit limit) {
        this.plot = plot2;
        this.pan = pan2;
        this.zoom = zoom2;
        this.zoomLimit = limit;
    }

    public State getState() {
        return this.state;
    }

    public void setState(@NonNull State state2) {
        this.state = state2;
        state2.apply(this.plot);
    }

    /* access modifiers changed from: protected */
    public void adjustRangeBoundary(Number lower, Number upper, BoundaryMode mode) {
        this.state.setRangeBoundaries(lower, upper, mode);
        this.state.applyRangeBoundaries(this.plot);
    }

    /* access modifiers changed from: protected */
    public void adjustDomainBoundary(Number lower, Number upper, BoundaryMode mode) {
        this.state.setDomainBoundaries(lower, upper, mode);
        this.state.applyDomainBoundaries(this.plot);
    }

    public static PanZoom attach(@NonNull XYPlot plot2) {
        return attach(plot2, Pan.BOTH, Zoom.SCALE);
    }

    public static PanZoom attach(@NonNull XYPlot plot2, @NonNull Pan pan2, @NonNull Zoom zoom2) {
        return attach(plot2, pan2, zoom2, ZoomLimit.OUTER);
    }

    public static PanZoom attach(@NonNull XYPlot plot2, @NonNull Pan pan2, @NonNull Zoom zoom2, @NonNull ZoomLimit limit) {
        PanZoom pz = new PanZoom(plot2, pan2, zoom2, limit);
        plot2.setOnTouchListener(pz);
        return pz;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean onTouch(View view, MotionEvent event) {
        boolean isConsumed = false;
        if (this.delegate != null) {
            isConsumed = this.delegate.onTouch(view, event);
        }
        if (!isEnabled() || isConsumed) {
            return true;
        }
        switch (event.getAction() & 255) {
            case 0:
                this.firstFingerPos = new PointF(event.getX(), event.getY());
                this.dragState = DragState.ONE_FINGER;
                return true;
            case 1:
                reset();
                return true;
            case 2:
                if (this.dragState == DragState.ONE_FINGER) {
                    pan(event);
                    return true;
                } else if (this.dragState != DragState.TWO_FINGERS) {
                    return true;
                } else {
                    zoom(event);
                    return true;
                }
            case 5:
                setFingersRect(fingerDistance(event));
                if (getFingersRect().width() <= MIN_DIST_2_FING && getFingersRect().width() >= -5.0f) {
                    return true;
                }
                this.dragState = DragState.TWO_FINGERS;
                return true;
            case 6:
                this.dragState = DragState.NONE;
                return true;
            default:
                return true;
        }
    }

    /* access modifiers changed from: protected */
    public RectF fingerDistance(float firstFingerX, float firstFingerY, float secondFingerX, float secondFingerY) {
        float left;
        float right;
        float top;
        float bottom;
        if (firstFingerX > secondFingerX) {
            left = secondFingerX;
        } else {
            left = firstFingerX;
        }
        if (firstFingerX > secondFingerX) {
            right = firstFingerX;
        } else {
            right = secondFingerX;
        }
        if (firstFingerY > secondFingerY) {
            top = secondFingerY;
        } else {
            top = firstFingerY;
        }
        if (firstFingerY > secondFingerY) {
            bottom = firstFingerY;
        } else {
            bottom = secondFingerY;
        }
        return new RectF(left, top, right, bottom);
    }

    /* access modifiers changed from: protected */
    public RectF fingerDistance(MotionEvent evt) {
        return fingerDistance(evt.getX(0), evt.getY(0), evt.getX(1), evt.getY(1));
    }

    /* access modifiers changed from: protected */
    public void pan(MotionEvent motionEvent) {
        if (this.pan != Pan.NONE) {
            PointF oldFirstFinger = this.firstFingerPos;
            this.firstFingerPos = new PointF(motionEvent.getX(), motionEvent.getY());
            if (EnumSet.of(Pan.HORIZONTAL, Pan.BOTH).contains(this.pan)) {
                Region newBounds = new Region();
                calculatePan(oldFirstFinger, newBounds, true);
                adjustDomainBoundary(newBounds.getMin(), newBounds.getMax(), BoundaryMode.FIXED);
            }
            if (EnumSet.of(Pan.VERTICAL, Pan.BOTH).contains(this.pan)) {
                Region newBounds2 = new Region();
                calculatePan(oldFirstFinger, newBounds2, false);
                adjustRangeBoundary(newBounds2.getMin(), newBounds2.getMax(), BoundaryMode.FIXED);
            }
            this.plot.redraw();
        }
    }

    /* access modifiers changed from: protected */
    public void calculatePan(PointF oldFirstFinger, Region bounds, boolean horizontal) {
        float offset;
        if (horizontal) {
            bounds.setMinMax(this.plot.getBounds().getxRegion());
            offset = (oldFirstFinger.x - this.firstFingerPos.x) * ((bounds.getMax().floatValue() - bounds.getMin().floatValue()) / ((float) this.plot.getWidth()));
        } else {
            bounds.setMinMax(this.plot.getBounds().getyRegion());
            offset = (-(oldFirstFinger.y - this.firstFingerPos.y)) * ((bounds.getMax().floatValue() - bounds.getMin().floatValue()) / ((float) this.plot.getHeight()));
        }
        bounds.setMin(Float.valueOf(bounds.getMin().floatValue() + offset));
        bounds.setMax(Float.valueOf(bounds.getMax().floatValue() + offset));
        float diff = bounds.length().floatValue();
        if (horizontal && this.plot.getOuterLimits().getxRegion().isDefined()) {
            if (bounds.getMin().floatValue() < this.plot.getOuterLimits().getMinX().floatValue()) {
                bounds.setMin(this.plot.getOuterLimits().getMinX());
                bounds.setMax(Float.valueOf(bounds.getMin().floatValue() + diff));
            }
            if (bounds.getMax().floatValue() > this.plot.getOuterLimits().getMaxX().floatValue()) {
                bounds.setMax(this.plot.getOuterLimits().getMaxX());
                bounds.setMin(Float.valueOf(bounds.getMax().floatValue() - diff));
            }
        } else if (this.plot.getOuterLimits().getyRegion().isDefined()) {
            if (bounds.getMin().floatValue() < this.plot.getOuterLimits().getMinY().floatValue()) {
                bounds.setMin(this.plot.getOuterLimits().getMinY());
                bounds.setMax(Float.valueOf(bounds.getMin().floatValue() + diff));
            }
            if (bounds.getMax().floatValue() > this.plot.getOuterLimits().getMaxY().floatValue()) {
                bounds.setMax(this.plot.getOuterLimits().getMaxY());
                bounds.setMin(Float.valueOf(bounds.getMax().floatValue() - diff));
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isValidScale(float scale) {
        return !Float.isInfinite(scale) && !Float.isNaN(scale) && (((double) scale) <= -0.001d || ((double) scale) >= 0.001d);
    }

    /* access modifiers changed from: protected */
    public void zoom(MotionEvent motionEvent) {
        if (this.zoom != Zoom.NONE) {
            RectF oldFingersRect = getFingersRect();
            RectF newFingersRect = fingerDistance(motionEvent);
            setFingersRect(newFingersRect);
            if (oldFingersRect != null && !RectFUtils.areIdentical(oldFingersRect, newFingersRect)) {
                RectF newRect = new RectF();
                float scaleX = 1.0f;
                float scaleY = 1.0f;
                switch (this.zoom) {
                    case STRETCH_HORIZONTAL:
                        scaleX = oldFingersRect.width() / getFingersRect().width();
                        if (!isValidScale(scaleX)) {
                            return;
                        }
                        break;
                    case STRETCH_VERTICAL:
                        scaleY = oldFingersRect.height() / getFingersRect().height();
                        if (!isValidScale(scaleY)) {
                            return;
                        }
                        break;
                    case STRETCH_BOTH:
                        scaleX = oldFingersRect.width() / getFingersRect().width();
                        scaleY = oldFingersRect.height() / getFingersRect().height();
                        if (!isValidScale(scaleX) || !isValidScale(scaleY)) {
                            return;
                        }
                    case SCALE:
                        float sc = ((float) Math.hypot((double) oldFingersRect.height(), (double) oldFingersRect.width())) / ((float) Math.hypot((double) getFingersRect().height(), (double) getFingersRect().width()));
                        scaleX = sc;
                        scaleY = sc;
                        if (!isValidScale(scaleX) || !isValidScale(scaleY)) {
                            return;
                        }
                }
                if (EnumSet.of(Zoom.STRETCH_HORIZONTAL, Zoom.STRETCH_BOTH, Zoom.SCALE).contains(this.zoom)) {
                    calculateZoom(newRect, scaleX, true);
                    adjustDomainBoundary(Float.valueOf(newRect.left), Float.valueOf(newRect.right), BoundaryMode.FIXED);
                }
                if (EnumSet.of(Zoom.STRETCH_VERTICAL, Zoom.STRETCH_BOTH, Zoom.SCALE).contains(this.zoom)) {
                    calculateZoom(newRect, scaleY, false);
                    adjustRangeBoundary(Float.valueOf(newRect.top), Float.valueOf(newRect.bottom), BoundaryMode.FIXED);
                }
                this.plot.redraw();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void calculateZoom(RectF newRect, float scale, boolean isHorizontal) {
        float calcMax;
        float span;
        RectRegion bounds = this.plot.getBounds();
        if (isHorizontal) {
            calcMax = bounds.getMaxX().floatValue();
            span = calcMax - bounds.getMinX().floatValue();
        } else {
            calcMax = bounds.getMaxY().floatValue();
            span = calcMax - bounds.getMinY().floatValue();
        }
        float midPoint = calcMax - (span / 2.0f);
        float offset = (span * scale) / 2.0f;
        RectRegion limits = this.plot.getOuterLimits();
        if (isHorizontal) {
            if (this.zoomLimit == ZoomLimit.MIN_TICKS && this.plot.getDomainStepValue() > ((double) (scale * span))) {
                offset = (float) (this.plot.getDomainStepValue() / 2.0d);
            }
            newRect.left = midPoint - offset;
            newRect.right = midPoint + offset;
            if (limits.isFullyDefined()) {
                if (newRect.left < limits.getMinX().floatValue()) {
                    newRect.left = limits.getMinX().floatValue();
                }
                if (newRect.right > limits.getMaxX().floatValue()) {
                    newRect.right = limits.getMaxX().floatValue();
                    return;
                }
                return;
            }
            return;
        }
        if (this.zoomLimit == ZoomLimit.MIN_TICKS && this.plot.getRangeStepValue() > ((double) (scale * span))) {
            offset = (float) (this.plot.getRangeStepValue() / 2.0d);
        }
        newRect.top = midPoint - offset;
        newRect.bottom = midPoint + offset;
        if (limits.isFullyDefined()) {
            if (newRect.top < limits.getMinY().floatValue()) {
                newRect.top = limits.getMinY().floatValue();
            }
            if (newRect.bottom > limits.getMaxY().floatValue()) {
                newRect.bottom = limits.getMaxY().floatValue();
            }
        }
    }

    public Pan getPan() {
        return this.pan;
    }

    public void setPan(Pan pan2) {
        this.pan = pan2;
    }

    public Zoom getZoom() {
        return this.zoom;
    }

    public void setZoom(Zoom zoom2) {
        this.zoom = zoom2;
    }

    public ZoomLimit getZoomLimit() {
        return this.zoomLimit;
    }

    public void setZoomLimit(ZoomLimit zoomLimit2) {
        this.zoomLimit = zoomLimit2;
    }

    public View.OnTouchListener getDelegate() {
        return this.delegate;
    }

    public void setDelegate(View.OnTouchListener delegate2) {
        this.delegate = delegate2;
    }

    public void reset() {
        this.firstFingerPos = null;
        setFingersRect((RectF) null);
        setFingersRect((RectF) null);
    }

    /* access modifiers changed from: protected */
    public RectF getFingersRect() {
        return this.fingersRect;
    }

    /* access modifiers changed from: protected */
    public void setFingersRect(RectF fingersRect2) {
        this.fingersRect = fingersRect2;
    }
}
