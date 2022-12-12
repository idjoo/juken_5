package com.github.anastr.speedviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.view.View;
import com.github.anastr.speedviewlib.components.Indicators.Indicator;
import com.github.anastr.speedviewlib.components.Indicators.NoIndicator;
import com.github.anastr.speedviewlib.components.note.Note;
import com.github.anastr.speedviewlib.util.OnPrintTickLabel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class Speedometer extends Gauge {
    private int backgroundCircleColor;
    private Paint circleBackPaint;
    private int cutPadding;
    private float degree;
    private int endDegree;
    private int highSpeedColor;
    private Indicator indicator;
    private int indicatorLightColor;
    private Paint indicatorLightPaint;
    private float initTickPadding;
    private float lastPercentSpeed;
    private int lowSpeedColor;
    private int markColor;
    private int mediumSpeedColor;
    /* access modifiers changed from: private */
    public ArrayList<Note> notes;
    private OnPrintTickLabel onPrintTickLabel;
    private Mode speedometerMode;
    private float speedometerWidth;
    private int startDegree;
    private int tickPadding;
    private boolean tickRotation;
    private List<Float> ticks;
    private boolean withIndicatorLight;

    /* access modifiers changed from: protected */
    public abstract void defaultSpeedometerValues();

    public Speedometer(Context context) {
        this(context, (AttributeSet) null);
    }

    public Speedometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Speedometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.withIndicatorLight = false;
        this.indicatorLightColor = -1140893918;
        this.circleBackPaint = new Paint(1);
        this.indicatorLightPaint = new Paint(1);
        this.speedometerWidth = dpTOpx(30.0f);
        this.markColor = -1;
        this.lowSpeedColor = -16711936;
        this.mediumSpeedColor = InputDeviceCompat.SOURCE_ANY;
        this.highSpeedColor = SupportMenu.CATEGORY_MASK;
        this.backgroundCircleColor = -1;
        this.startDegree = 135;
        this.endDegree = 405;
        this.degree = (float) this.startDegree;
        this.notes = new ArrayList<>();
        this.speedometerMode = Mode.NORMAL;
        this.cutPadding = 0;
        this.ticks = new ArrayList();
        this.tickRotation = true;
        this.initTickPadding = 0.0f;
        this.tickPadding = (int) (getSpeedometerWidth() + dpTOpx(3.0f));
        this.lastPercentSpeed = 0.0f;
        init();
        initAttributeSet(context, attrs);
        initAttributeValue();
    }

    private void init() {
        this.indicatorLightPaint.setStyle(Paint.Style.STROKE);
        this.indicator = new NoIndicator(getContext());
        defaultSpeedometerValues();
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Speedometer, 0, 0);
            int mode = a.getInt(R.styleable.Speedometer_sv_speedometerMode, -1);
            if (!(mode == -1 || mode == 0)) {
                setSpeedometerMode(Mode.values()[mode]);
            }
            int ind = a.getInt(R.styleable.Speedometer_sv_indicator, -1);
            if (ind != -1) {
                setIndicator(Indicator.Indicators.values()[ind]);
            }
            this.markColor = a.getColor(R.styleable.Speedometer_sv_markColor, this.markColor);
            this.lowSpeedColor = a.getColor(R.styleable.Speedometer_sv_lowSpeedColor, this.lowSpeedColor);
            this.mediumSpeedColor = a.getColor(R.styleable.Speedometer_sv_mediumSpeedColor, this.mediumSpeedColor);
            this.highSpeedColor = a.getColor(R.styleable.Speedometer_sv_highSpeedColor, this.highSpeedColor);
            this.backgroundCircleColor = a.getColor(R.styleable.Speedometer_sv_backgroundCircleColor, this.backgroundCircleColor);
            this.speedometerWidth = a.getDimension(R.styleable.Speedometer_sv_speedometerWidth, this.speedometerWidth);
            this.startDegree = a.getInt(R.styleable.Speedometer_sv_startDegree, this.startDegree);
            this.endDegree = a.getInt(R.styleable.Speedometer_sv_endDegree, this.endDegree);
            setIndicatorWidth(a.getDimension(R.styleable.Speedometer_sv_indicatorWidth, this.indicator.getIndicatorWidth()));
            this.cutPadding = (int) a.getDimension(R.styleable.Speedometer_sv_cutPadding, (float) this.cutPadding);
            setTickNumber(a.getInteger(R.styleable.Speedometer_sv_tickNumber, this.ticks.size()));
            this.tickRotation = a.getBoolean(R.styleable.Speedometer_sv_tickRotation, this.tickRotation);
            this.tickPadding = (int) a.getDimension(R.styleable.Speedometer_sv_tickPadding, (float) this.tickPadding);
            setIndicatorColor(a.getColor(R.styleable.Speedometer_sv_indicatorColor, this.indicator.getIndicatorColor()));
            this.withIndicatorLight = a.getBoolean(R.styleable.Speedometer_sv_withIndicatorLight, this.withIndicatorLight);
            this.indicatorLightColor = a.getColor(R.styleable.Speedometer_sv_indicatorLightColor, this.indicatorLightColor);
            this.degree = (float) this.startDegree;
            a.recycle();
            checkStartAndEndDegree();
        }
    }

    private void initAttributeValue() {
        this.circleBackPaint.setColor(this.backgroundCircleColor);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int defaultSize = (int) dpTOpx(250.0f);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == 1073741824) {
            size = getMeasuredWidth();
        } else if (heightMode == 1073741824) {
            size = getMeasuredHeight();
        } else if (widthMode == 0 && heightMode == 0) {
            size = defaultSize;
        } else if (widthMode == Integer.MIN_VALUE && heightMode == Integer.MIN_VALUE) {
            size = Math.min(defaultSize, Math.min(getMeasuredWidth(), getMeasuredHeight()));
        } else if (widthMode == Integer.MIN_VALUE) {
            size = Math.min(defaultSize, getMeasuredWidth());
        } else {
            size = Math.min(defaultSize, getMeasuredHeight());
        }
        int newW = size / this.speedometerMode.divWidth;
        int newH = size / this.speedometerMode.divHeight;
        if (this.speedometerMode.isHalf) {
            if (this.speedometerMode.divWidth == 2) {
                newW += this.cutPadding;
            } else {
                newH += this.cutPadding;
            }
        }
        setMeasuredDimension(newW, newH);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        this.indicator.onSizeChange(this);
        updateTranslated();
    }

    private void checkStartAndEndDegree() {
        if (this.startDegree < 0) {
            throw new IllegalArgumentException("StartDegree can't be Negative");
        } else if (this.endDegree < 0) {
            throw new IllegalArgumentException("EndDegree can't be Negative");
        } else if (this.startDegree >= this.endDegree) {
            throw new IllegalArgumentException("EndDegree must be bigger than StartDegree !");
        } else if (this.endDegree - this.startDegree > 360) {
            throw new IllegalArgumentException("(EndDegree - StartDegree) must be smaller than 360 !");
        } else if (this.startDegree < this.speedometerMode.minDegree) {
            throw new IllegalArgumentException("StartDegree must be bigger than " + this.speedometerMode.minDegree + " in " + this.speedometerMode + " Mode !");
        } else if (this.endDegree > this.speedometerMode.maxDegree) {
            throw new IllegalArgumentException("EndDegree must be smaller than " + this.speedometerMode.maxDegree + " in " + this.speedometerMode + " Mode !");
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.degree = getDegreeAtSpeed(getCurrentSpeed());
    }

    /* access modifiers changed from: protected */
    public void drawIndicator(Canvas canvas) {
        if (this.withIndicatorLight) {
            drawIndicatorLight(canvas);
        }
        this.indicator.draw(canvas, this.degree);
    }

    /* access modifiers changed from: protected */
    public void drawIndicatorLight(Canvas canvas) {
        float sweep = Math.abs(getPercentSpeed() - this.lastPercentSpeed) * 30.0f;
        this.lastPercentSpeed = getPercentSpeed();
        if (sweep > 30.0f) {
            sweep = 30.0f;
        }
        this.indicatorLightPaint.setShader(new SweepGradient(((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f, new int[]{this.indicatorLightColor, 16777215}, new float[]{0.0f, sweep / 360.0f}));
        this.indicatorLightPaint.setStrokeWidth(this.indicator.getLightBottom() - this.indicator.getTop());
        float risk = this.indicator.getTop() + (this.indicatorLightPaint.getStrokeWidth() * 0.5f);
        RectF speedometerRect = new RectF(risk, risk, ((float) getSize()) - risk, ((float) getSize()) - risk);
        canvas.save();
        canvas.rotate(this.degree, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
        if (isSpeedIncrease()) {
            canvas.scale(1.0f, -1.0f, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
        }
        canvas.drawArc(speedometerRect, 0.0f, sweep, false, this.indicatorLightPaint);
        canvas.restore();
    }

    /* access modifiers changed from: protected */
    public void drawNotes(Canvas canvas) {
        Iterator<Note> it = this.notes.iterator();
        while (it.hasNext()) {
            Note note = it.next();
            if (note.getPosition() == Note.Position.CenterSpeedometer) {
                note.draw(canvas, ((float) getWidth()) * 0.5f, ((float) getHeight()) * 0.5f);
            } else {
                float y = 0.0f;
                switch (note.getPosition()) {
                    case TopIndicator:
                        y = this.indicator.getTop();
                        break;
                    case CenterIndicator:
                        y = (this.indicator.getTop() + this.indicator.getBottom()) * 0.5f;
                        break;
                    case BottomIndicator:
                        y = this.indicator.getBottom();
                        break;
                    case TopSpeedometer:
                        y = (float) getPadding();
                        break;
                    case QuarterSpeedometer:
                        y = (((float) getHeightPa()) * 0.25f) + ((float) getPadding());
                        break;
                }
                canvas.save();
                canvas.rotate(getDegree() + 90.0f, ((float) getWidth()) * 0.5f, ((float) getHeight()) * 0.5f);
                canvas.rotate(-(getDegree() + 90.0f), ((float) getWidth()) * 0.5f, y);
                note.draw(canvas, ((float) getWidth()) * 0.5f, y);
                canvas.restore();
            }
        }
    }

    /* access modifiers changed from: protected */
    public final Canvas createBackgroundBitmapCanvas() {
        if (getWidth() == 0 || getHeight() == 0) {
            return new Canvas();
        }
        this.backgroundBitmap = Bitmap.createBitmap(getSize(), getSize(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(this.backgroundBitmap);
        canvas.drawCircle(((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f, (((float) getSize()) * 0.5f) - ((float) getPadding()), this.circleBackPaint);
        canvas.clipRect(0, 0, getSize(), getSize());
        return canvas;
    }

    /* access modifiers changed from: protected */
    public float getDegree() {
        return this.degree;
    }

    /* access modifiers changed from: protected */
    public float getDegreeAtSpeed(float speed) {
        return (((speed - getMinSpeed()) * ((float) (this.endDegree - this.startDegree))) / (getMaxSpeed() - getMinSpeed())) + ((float) this.startDegree);
    }

    /* access modifiers changed from: protected */
    public float getSpeedAtDegree(float degree2) {
        return (((degree2 - ((float) this.startDegree)) * (getMaxSpeed() - getMinSpeed())) / ((float) (this.endDegree - this.startDegree))) + getMinSpeed();
    }

    public int getIndicatorColor() {
        return this.indicator.getIndicatorColor();
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicator.noticeIndicatorColorChange(indicatorColor);
        if (isAttachedToWindow()) {
            invalidate();
        }
    }

    public int getMarkColor() {
        return this.markColor;
    }

    public void setMarkColor(int markColor2) {
        this.markColor = markColor2;
        if (isAttachedToWindow()) {
            invalidate();
        }
    }

    public int getLowSpeedColor() {
        return this.lowSpeedColor;
    }

    public void setLowSpeedColor(int lowSpeedColor2) {
        this.lowSpeedColor = lowSpeedColor2;
        if (isAttachedToWindow()) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public int getMediumSpeedColor() {
        return this.mediumSpeedColor;
    }

    public void setMediumSpeedColor(int mediumSpeedColor2) {
        this.mediumSpeedColor = mediumSpeedColor2;
        if (isAttachedToWindow()) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public int getHighSpeedColor() {
        return this.highSpeedColor;
    }

    public void setHighSpeedColor(int highSpeedColor2) {
        this.highSpeedColor = highSpeedColor2;
        if (isAttachedToWindow()) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public int getBackgroundCircleColor() {
        return this.backgroundCircleColor;
    }

    public void setBackgroundCircleColor(int backgroundCircleColor2) {
        this.backgroundCircleColor = backgroundCircleColor2;
        this.circleBackPaint.setColor(backgroundCircleColor2);
        if (isAttachedToWindow()) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public float getSpeedometerWidth() {
        return this.speedometerWidth;
    }

    public void setSpeedometerWidth(float speedometerWidth2) {
        this.speedometerWidth = speedometerWidth2;
        if (isAttachedToWindow()) {
            this.indicator.noticeSpeedometerWidthChange(speedometerWidth2);
            updateBackgroundBitmap();
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public int getStartDegree() {
        return this.startDegree;
    }

    public void setStartDegree(int startDegree2) {
        setStartEndDegree(startDegree2, this.endDegree);
    }

    /* access modifiers changed from: protected */
    public int getEndDegree() {
        return this.endDegree;
    }

    public void setEndDegree(int endDegree2) {
        setStartEndDegree(this.startDegree, endDegree2);
    }

    public void setStartEndDegree(int startDegree2, int endDegree2) {
        this.startDegree = startDegree2;
        this.endDegree = endDegree2;
        checkStartAndEndDegree();
        if (this.ticks.size() != 0) {
            setTickNumber(this.ticks.size());
        }
        cancelSpeedAnimator();
        this.degree = getDegreeAtSpeed(getSpeed());
        if (isAttachedToWindow()) {
            updateBackgroundBitmap();
            tremble();
            invalidate();
        }
    }

    public int getSize() {
        if (this.speedometerMode == Mode.NORMAL) {
            return getWidth();
        }
        if (this.speedometerMode.isHalf) {
            return Math.max(getWidth(), getHeight());
        }
        return (Math.max(getWidth(), getHeight()) * 2) - (this.cutPadding * 2);
    }

    public int getSizePa() {
        return getSize() - (getPadding() * 2);
    }

    public void addNote(Note note) {
        addNote(note, 3000);
    }

    public void addNote(final Note note, long showTimeMillisecond) {
        note.build(getWidth());
        this.notes.add(note);
        if (showTimeMillisecond != -1) {
            postDelayed(new Runnable() {
                public void run() {
                    if (Speedometer.this.isAttachedToWindow()) {
                        Speedometer.this.notes.remove(note);
                        Speedometer.this.postInvalidate();
                    }
                }
            }, showTimeMillisecond);
            invalidate();
        }
    }

    public void removeAllNotes() {
        this.notes.clear();
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void drawDefMinMaxSpeedPosition(Canvas c) {
        if (getStartDegree() % 360 <= 90) {
            this.textPaint.setTextAlign(Paint.Align.RIGHT);
        } else if (getStartDegree() % 360 <= 180) {
            this.textPaint.setTextAlign(Paint.Align.LEFT);
        } else if (getStartDegree() % 360 <= 270) {
            this.textPaint.setTextAlign(Paint.Align.CENTER);
        } else {
            this.textPaint.setTextAlign(Paint.Align.RIGHT);
        }
        c.save();
        c.rotate(((float) getStartDegree()) + 90.0f, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
        c.rotate(-(((float) getStartDegree()) + 90.0f), ((((float) getSizePa()) * 0.5f) - this.textPaint.getTextSize()) + ((float) getPadding()), this.textPaint.getTextSize() + ((float) getPadding()));
        c.drawText(getMinSpeedText(), ((((float) getSizePa()) * 0.5f) - this.textPaint.getTextSize()) + ((float) getPadding()), this.textPaint.getTextSize() + ((float) getPadding()), this.textPaint);
        c.restore();
        if (getEndDegree() % 360 <= 90) {
            this.textPaint.setTextAlign(Paint.Align.RIGHT);
        } else if (getEndDegree() % 360 <= 180) {
            this.textPaint.setTextAlign(Paint.Align.LEFT);
        } else if (getEndDegree() % 360 <= 270) {
            this.textPaint.setTextAlign(Paint.Align.CENTER);
        } else {
            this.textPaint.setTextAlign(Paint.Align.RIGHT);
        }
        c.save();
        c.rotate(((float) getEndDegree()) + 90.0f, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
        c.rotate(-(((float) getEndDegree()) + 90.0f), (((float) getSizePa()) * 0.5f) + this.textPaint.getTextSize() + ((float) getPadding()), this.textPaint.getTextSize() + ((float) getPadding()));
        c.drawText(getMaxSpeedText(), (((float) getSizePa()) * 0.5f) + this.textPaint.getTextSize() + ((float) getPadding()), this.textPaint.getTextSize() + ((float) getPadding()), this.textPaint);
        c.restore();
    }

    /* access modifiers changed from: protected */
    public void drawTicks(Canvas c) {
        if (this.ticks.size() != 0) {
            this.textPaint.setTextAlign(Paint.Align.LEFT);
            for (int i = 0; i < this.ticks.size(); i++) {
                float d = getDegreeAtSpeed(this.ticks.get(i).floatValue()) + 90.0f;
                c.save();
                c.rotate(d, ((float) getSize()) * 0.5f, ((float) getSize()) * 0.5f);
                if (!this.tickRotation) {
                    c.rotate(-d, ((float) getSize()) * 0.5f, this.initTickPadding + this.textPaint.getTextSize() + ((float) getPadding()) + ((float) this.tickPadding));
                }
                CharSequence tick = null;
                if (this.onPrintTickLabel != null) {
                    tick = this.onPrintTickLabel.getTickLabel(i, this.ticks.get(i).floatValue());
                }
                if (tick == null) {
                    if (getTickTextFormat() == 1) {
                        tick = String.format(getLocale(), "%.1f", new Object[]{this.ticks.get(i)});
                    } else {
                        tick = String.format(getLocale(), "%d", new Object[]{Integer.valueOf(this.ticks.get(i).intValue())});
                    }
                }
                c.translate(0.0f, this.initTickPadding + ((float) getPadding()) + ((float) this.tickPadding));
                new StaticLayout(tick, this.textPaint, getSize(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false).draw(c);
                c.restore();
            }
        }
    }

    public float getIndicatorWidth() {
        return this.indicator.getIndicatorWidth();
    }

    public void setIndicatorWidth(float indicatorWidth) {
        this.indicator.noticeIndicatorWidthChange(indicatorWidth);
        if (isAttachedToWindow()) {
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void indicatorEffects(boolean withEffects) {
        this.indicator.withEffects(withEffects);
    }

    public void setIndicator(Indicator.Indicators indicator2) {
        this.indicator = Indicator.createIndicator(getContext(), indicator2);
        if (isAttachedToWindow()) {
            this.indicator.setTargetSpeedometer(this);
            invalidate();
        }
    }

    public void setIndicator(Indicator indicator2) {
        this.indicator = indicator2;
        if (isAttachedToWindow()) {
            this.indicator.setTargetSpeedometer(this);
            invalidate();
        }
    }

    public boolean isWithIndicatorLight() {
        return this.withIndicatorLight;
    }

    public void setWithIndicatorLight(boolean withIndicatorLight2) {
        this.withIndicatorLight = withIndicatorLight2;
    }

    public int getIndicatorLightColor() {
        return this.indicatorLightColor;
    }

    public void setIndicatorLightColor(int indicatorLightColor2) {
        this.indicatorLightColor = indicatorLightColor2;
    }

    public int getTickNumber() {
        return this.ticks.size();
    }

    public void setTickNumber(int tickNumber) {
        if (tickNumber < 0) {
            throw new IllegalArgumentException("tickNumber mustn't be negative");
        }
        List<Float> ticks2 = new ArrayList<>();
        float tickEach = tickNumber != 1 ? ((float) (this.endDegree - this.startDegree)) / ((float) (tickNumber - 1)) : ((float) this.endDegree) + 1.0f;
        for (int i = 0; i < tickNumber; i++) {
            ticks2.add(Float.valueOf(getSpeedAtDegree((((float) i) * tickEach) + ((float) getStartDegree()))));
        }
        setTicks(ticks2);
    }

    public List<Float> getTicks() {
        return this.ticks;
    }

    public void setTicks(Float... ticks2) {
        setTicks((List<Float>) Arrays.asList(ticks2));
    }

    public void setTicks(List<Float> ticks2) {
        this.ticks.clear();
        this.ticks.addAll(ticks2);
        checkTicks();
        if (isAttachedToWindow()) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    private void checkTicks() {
        float lastTick = getMinSpeed() - 1.0f;
        for (Float floatValue : this.ticks) {
            float tick = floatValue.floatValue();
            if (lastTick == tick) {
                throw new IllegalArgumentException("you mustn't have double ticks");
            } else if (lastTick > tick) {
                throw new IllegalArgumentException("ticks must be ascending order");
            } else if (tick < getMinSpeed() || tick > getMaxSpeed()) {
                throw new IllegalArgumentException("ticks must be between [minSpeed, maxSpeed] !!");
            } else {
                lastTick = tick;
            }
        }
    }

    public boolean isTickRotation() {
        return this.tickRotation;
    }

    public void setTickRotation(boolean tickRotation2) {
        this.tickRotation = tickRotation2;
        if (isAttachedToWindow()) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public int getTickPadding() {
        return this.tickPadding;
    }

    public void setTickPadding(int tickPadding2) {
        this.tickPadding = tickPadding2;
        if (isAttachedToWindow()) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public float getInitTickPadding() {
        return this.initTickPadding;
    }

    /* access modifiers changed from: protected */
    public void setInitTickPadding(float initTickPadding2) {
        this.initTickPadding = initTickPadding2;
    }

    public void setOnPrintTickLabel(OnPrintTickLabel onPrintTickLabel2) {
        this.onPrintTickLabel = onPrintTickLabel2;
        if (isAttachedToWindow()) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public final float getViewCenterX() {
        switch (this.speedometerMode) {
            case LEFT:
            case TOP_LEFT:
            case BOTTOM_LEFT:
                return (((float) getSize()) * 0.5f) - (((float) getWidth()) * 0.5f);
            case RIGHT:
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
                return (((float) getSize()) * 0.5f) + (((float) getWidth()) * 0.5f);
            default:
                return ((float) getSize()) * 0.5f;
        }
    }

    /* access modifiers changed from: protected */
    public final float getViewCenterY() {
        switch (this.speedometerMode) {
            case TOP_LEFT:
            case TOP_RIGHT:
            case TOP:
                return (((float) getSize()) * 0.5f) - (((float) getHeight()) * 0.5f);
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
            case BOTTOM:
                return (((float) getSize()) * 0.5f) + (((float) getHeight()) * 0.5f);
            default:
                return ((float) getSize()) * 0.5f;
        }
    }

    /* access modifiers changed from: protected */
    public final float getViewLeft() {
        return getViewCenterX() - (((float) getWidth()) * 0.5f);
    }

    /* access modifiers changed from: protected */
    public final float getViewTop() {
        return getViewCenterY() - (((float) getHeight()) * 0.5f);
    }

    /* access modifiers changed from: protected */
    public final float getViewRight() {
        return getViewCenterX() + (((float) getWidth()) * 0.5f);
    }

    /* access modifiers changed from: protected */
    public final float getViewBottom() {
        return getViewCenterY() + (((float) getHeight()) * 0.5f);
    }

    public void setSpeedometerMode(Mode speedometerMode2) {
        this.speedometerMode = speedometerMode2;
        if (speedometerMode2 != Mode.NORMAL) {
            this.startDegree = speedometerMode2.minDegree;
            this.endDegree = speedometerMode2.maxDegree;
        }
        updateTranslated();
        cancelSpeedAnimator();
        this.degree = getDegreeAtSpeed(getSpeed());
        this.indicator.onSizeChange(this);
        if (isAttachedToWindow()) {
            requestLayout();
            updateBackgroundBitmap();
            tremble();
            invalidate();
        }
    }

    private void updateTranslated() {
        float f;
        float f2 = 0.0f;
        if (this.speedometerMode.isRight()) {
            f = (((float) (-getSize())) * 0.5f) + ((float) this.cutPadding);
        } else {
            f = 0.0f;
        }
        this.translatedDx = f;
        if (this.speedometerMode.isBottom()) {
            f2 = ((float) this.cutPadding) + (((float) (-getSize())) * 0.5f);
        }
        this.translatedDy = f2;
    }

    public Mode getSpeedometerMode() {
        return this.speedometerMode;
    }

    public enum Mode {
        NORMAL(0, 720, false, 1, 1),
        LEFT(90, 270, true, 2, 1),
        TOP(180, 360, true, 1, 2),
        RIGHT(270, 450, true, 2, 1),
        BOTTOM(0, 180, true, 1, 2),
        TOP_LEFT(180, 270, false, 1, 1),
        TOP_RIGHT(270, 360, false, 1, 1),
        BOTTOM_RIGHT(0, 90, false, 1, 1),
        BOTTOM_LEFT(90, 180, false, 1, 1);
        
        final int divHeight;
        final int divWidth;
        public final boolean isHalf;
        final int maxDegree;
        final int minDegree;

        private Mode(int minDegree2, int maxDegree2, boolean isHalf2, int divWidth2, int divHeight2) {
            this.minDegree = minDegree2;
            this.maxDegree = maxDegree2;
            this.isHalf = isHalf2;
            this.divWidth = divWidth2;
            this.divHeight = divHeight2;
        }

        public boolean isLeft() {
            return this == LEFT || this == TOP_LEFT || this == BOTTOM_LEFT;
        }

        public boolean isTop() {
            return this == TOP || this == TOP_LEFT || this == TOP_RIGHT;
        }

        public boolean isRight() {
            return this == RIGHT || this == TOP_RIGHT || this == BOTTOM_RIGHT;
        }

        public boolean isBottom() {
            return this == BOTTOM || this == BOTTOM_LEFT || this == BOTTOM_RIGHT;
        }

        public boolean isQuarter() {
            return !this.isHalf && this != NORMAL;
        }
    }
}
