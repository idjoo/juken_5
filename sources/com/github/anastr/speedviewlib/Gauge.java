package com.github.anastr.speedviewlib;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import com.github.anastr.speedviewlib.util.OnSectionChangeListener;
import com.github.anastr.speedviewlib.util.OnSpeedChangeListener;
import java.util.Locale;
import java.util.Random;

public abstract class Gauge extends View {
    public static final byte FLOAT_FORMAT = 1;
    public static final byte HIGH_SECTION = 3;
    public static final byte INTEGER_FORMAT = 0;
    public static final byte LOW_SECTION = 1;
    public static final byte MEDIUM_SECTION = 2;
    /* access modifiers changed from: private */
    public float accelerate;
    private Animator.AnimatorListener animatorListener;
    private boolean attachedToWindow;
    protected Bitmap backgroundBitmap;
    private Paint backgroundBitmapPaint;
    /* access modifiers changed from: private */
    public boolean canceled;
    private int currentIntSpeed;
    /* access modifiers changed from: private */
    public float currentSpeed;
    /* access modifiers changed from: private */
    public float decelerate;
    private int heightPa;
    /* access modifiers changed from: private */
    public boolean isSpeedIncrease;
    private Locale locale;
    private int lowSpeedPercent;
    private float maxSpeed;
    private int mediumSpeedPercent;
    private float minSpeed;
    private OnSectionChangeListener onSectionChangeListener;
    private OnSpeedChangeListener onSpeedChangeListener;
    private int padding;
    private ValueAnimator realSpeedAnimator;
    private byte section;
    private float speed;
    /* access modifiers changed from: private */
    public ValueAnimator speedAnimator;
    private int speedTextFormat;
    private float speedTextPadding;
    private TextPaint speedTextPaint;
    private Position speedTextPosition;
    private Bitmap speedUnitTextBitmap;
    private Paint speedUnitTextBitmapPaint;
    private Canvas speedUnitTextCanvas;
    private boolean speedometerTextRightToLeft;
    protected TextPaint textPaint;
    private int tickTextFormat;
    protected float translatedDx;
    protected float translatedDy;
    /* access modifiers changed from: private */
    public ValueAnimator trembleAnimator;
    private float trembleDegree;
    private int trembleDuration;
    private String unit;
    private float unitSpeedInterval;
    private TextPaint unitTextPaint;
    private boolean unitUnderSpeedText;
    private int widthPa;
    private boolean withTremble;

    /* access modifiers changed from: protected */
    public abstract void defaultGaugeValues();

    /* access modifiers changed from: protected */
    public abstract void updateBackgroundBitmap();

    public Gauge(Context context) {
        this(context, (AttributeSet) null);
    }

    public Gauge(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Gauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.speedUnitTextBitmapPaint = new Paint(1);
        this.textPaint = new TextPaint(1);
        this.speedTextPaint = new TextPaint(1);
        this.unitTextPaint = new TextPaint(1);
        this.unit = "Km/h";
        this.withTremble = true;
        this.maxSpeed = 100.0f;
        this.minSpeed = 0.0f;
        this.speed = this.minSpeed;
        this.currentIntSpeed = 0;
        this.currentSpeed = this.minSpeed;
        this.isSpeedIncrease = false;
        this.trembleDegree = 4.0f;
        this.trembleDuration = 1000;
        this.canceled = false;
        this.backgroundBitmapPaint = new Paint(1);
        this.padding = 0;
        this.widthPa = 0;
        this.heightPa = 0;
        this.lowSpeedPercent = 60;
        this.mediumSpeedPercent = 87;
        this.section = 1;
        this.speedometerTextRightToLeft = false;
        this.attachedToWindow = false;
        this.translatedDx = 0.0f;
        this.translatedDy = 0.0f;
        this.locale = Locale.getDefault();
        this.accelerate = 0.1f;
        this.decelerate = 0.1f;
        this.speedTextPosition = Position.BOTTOM_CENTER;
        this.unitSpeedInterval = dpTOpx(1.0f);
        this.speedTextPadding = dpTOpx(20.0f);
        this.unitUnderSpeedText = false;
        this.speedTextFormat = 1;
        this.tickTextFormat = 0;
        init();
        initAttributeSet(context, attrs);
        initAttributeValue();
    }

    private void init() {
        this.textPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.textPaint.setTextSize(dpTOpx(10.0f));
        this.textPaint.setTextAlign(Paint.Align.CENTER);
        this.speedTextPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.speedTextPaint.setTextSize(dpTOpx(18.0f));
        this.unitTextPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.unitTextPaint.setTextSize(dpTOpx(15.0f));
        if (Build.VERSION.SDK_INT >= 11) {
            this.speedAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.trembleAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.realSpeedAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.animatorListener = new Animator.AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    if (!Gauge.this.canceled) {
                        Gauge.this.tremble();
                    }
                }

                public void onAnimationCancel(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }
            };
        }
        defaultGaugeValues();
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Gauge, 0, 0);
            this.maxSpeed = a.getFloat(R.styleable.Gauge_sv_maxSpeed, this.maxSpeed);
            this.minSpeed = a.getFloat(R.styleable.Gauge_sv_minSpeed, this.minSpeed);
            this.speed = this.minSpeed;
            this.currentSpeed = this.minSpeed;
            this.withTremble = a.getBoolean(R.styleable.Gauge_sv_withTremble, this.withTremble);
            this.textPaint.setColor(a.getColor(R.styleable.Gauge_sv_textColor, this.textPaint.getColor()));
            this.textPaint.setTextSize(a.getDimension(R.styleable.Gauge_sv_textSize, this.textPaint.getTextSize()));
            this.speedTextPaint.setColor(a.getColor(R.styleable.Gauge_sv_speedTextColor, this.speedTextPaint.getColor()));
            this.speedTextPaint.setTextSize(a.getDimension(R.styleable.Gauge_sv_speedTextSize, this.speedTextPaint.getTextSize()));
            this.unitTextPaint.setColor(a.getColor(R.styleable.Gauge_sv_unitTextColor, this.unitTextPaint.getColor()));
            this.unitTextPaint.setTextSize(a.getDimension(R.styleable.Gauge_sv_unitTextSize, this.unitTextPaint.getTextSize()));
            String unit2 = a.getString(R.styleable.Gauge_sv_unit);
            if (unit2 == null) {
                unit2 = this.unit;
            }
            this.unit = unit2;
            this.trembleDegree = a.getFloat(R.styleable.Gauge_sv_trembleDegree, this.trembleDegree);
            this.trembleDuration = a.getInt(R.styleable.Gauge_sv_trembleDuration, this.trembleDuration);
            this.lowSpeedPercent = a.getInt(R.styleable.Gauge_sv_lowSpeedPercent, this.lowSpeedPercent);
            this.mediumSpeedPercent = a.getInt(R.styleable.Gauge_sv_mediumSpeedPercent, this.mediumSpeedPercent);
            this.speedometerTextRightToLeft = a.getBoolean(R.styleable.Gauge_sv_textRightToLeft, this.speedometerTextRightToLeft);
            this.accelerate = a.getFloat(R.styleable.Gauge_sv_accelerate, this.accelerate);
            this.decelerate = a.getFloat(R.styleable.Gauge_sv_decelerate, this.decelerate);
            this.unitUnderSpeedText = a.getBoolean(R.styleable.Gauge_sv_unitUnderSpeedText, this.unitUnderSpeedText);
            this.unitSpeedInterval = a.getDimension(R.styleable.Gauge_sv_unitSpeedInterval, this.unitSpeedInterval);
            this.speedTextPadding = a.getDimension(R.styleable.Gauge_sv_speedTextPadding, this.speedTextPadding);
            String speedTypefacePath = a.getString(R.styleable.Gauge_sv_speedTextTypeface);
            if (speedTypefacePath != null) {
                setSpeedTextTypeface(Typeface.createFromAsset(getContext().getAssets(), speedTypefacePath));
            }
            String typefacePath = a.getString(R.styleable.Gauge_sv_textTypeface);
            if (typefacePath != null) {
                setTextTypeface(Typeface.createFromAsset(getContext().getAssets(), typefacePath));
            }
            int position = a.getInt(R.styleable.Gauge_sv_speedTextPosition, -1);
            if (position != -1) {
                setSpeedTextPosition(Position.values()[position]);
            }
            int speedFormat = a.getInt(R.styleable.Gauge_sv_speedTextFormat, -1);
            if (speedFormat != -1) {
                setSpeedTextFormat(speedFormat);
            }
            int tickFormat = a.getInt(R.styleable.Gauge_sv_tickTextFormat, -1);
            if (tickFormat != -1) {
                setTickTextFormat(tickFormat);
            }
            a.recycle();
            checkSpeedometerPercent();
            checkAccelerate();
            checkDecelerate();
            checkTrembleData();
        }
    }

    private void initAttributeValue() {
        if (this.unitUnderSpeedText) {
            this.speedTextPaint.setTextAlign(Paint.Align.CENTER);
            this.unitTextPaint.setTextAlign(Paint.Align.CENTER);
            return;
        }
        this.speedTextPaint.setTextAlign(Paint.Align.LEFT);
        this.unitTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        if (this.widthPa > 0 && this.heightPa > 0) {
            this.speedUnitTextBitmap = Bitmap.createBitmap(this.widthPa, this.heightPa, Bitmap.Config.ARGB_8888);
        }
        this.speedUnitTextCanvas = new Canvas(this.speedUnitTextBitmap);
    }

    private void checkSpeedometerPercent() {
        if (this.lowSpeedPercent > this.mediumSpeedPercent) {
            throw new IllegalArgumentException("lowSpeedPercent must be smaller than mediumSpeedPercent");
        } else if (this.lowSpeedPercent > 100 || this.lowSpeedPercent < 0) {
            throw new IllegalArgumentException("lowSpeedPercent must be between [0, 100]");
        } else if (this.mediumSpeedPercent > 100 || this.mediumSpeedPercent < 0) {
            throw new IllegalArgumentException("mediumSpeedPercent must be between [0, 100]");
        }
    }

    private void checkAccelerate() {
        if (this.accelerate > 1.0f || this.accelerate <= 0.0f) {
            throw new IllegalArgumentException("accelerate must be between (0, 1]");
        }
    }

    private void checkDecelerate() {
        if (this.decelerate > 1.0f || this.decelerate <= 0.0f) {
            throw new IllegalArgumentException("decelerate must be between (0, 1]");
        }
    }

    private void checkTrembleData() {
        if (this.trembleDegree < 0.0f) {
            throw new IllegalArgumentException("trembleDegree  can't be Negative");
        } else if (this.trembleDuration < 0) {
            throw new IllegalArgumentException("trembleDuration  can't be Negative");
        }
    }

    public float dpTOpx(float dp) {
        return getContext().getResources().getDisplayMetrics().density * dp;
    }

    public float pxTOdp(float px) {
        return px / getContext().getResources().getDisplayMetrics().density;
    }

    private void updatePadding(int left, int top, int right, int bottom) {
        this.padding = Math.max(Math.max(left, right), Math.max(top, bottom));
        this.widthPa = getWidth() - (this.padding * 2);
        this.heightPa = getHeight() - (this.padding * 2);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        boolean byTremble;
        boolean isSpeedUp;
        canvas.translate(this.translatedDx, this.translatedDy);
        if (this.backgroundBitmap != null) {
            canvas.drawBitmap(this.backgroundBitmap, 0.0f, 0.0f, this.backgroundBitmapPaint);
        }
        int newSpeed = (int) this.currentSpeed;
        if (!(newSpeed == this.currentIntSpeed || this.onSpeedChangeListener == null)) {
            if (Build.VERSION.SDK_INT < 11 || !this.trembleAnimator.isRunning()) {
                byTremble = false;
            } else {
                byTremble = true;
            }
            if (newSpeed > this.currentIntSpeed) {
                isSpeedUp = true;
            } else {
                isSpeedUp = false;
            }
            int update = isSpeedUp ? 1 : -1;
            while (this.currentIntSpeed != newSpeed) {
                this.currentIntSpeed += update;
                this.onSpeedChangeListener.onSpeedChange(this, isSpeedUp, byTremble);
            }
        }
        this.currentIntSpeed = newSpeed;
        byte newSection = getSection();
        if (this.section != newSection) {
            onSectionChangeEvent(this.section, newSection);
        }
        this.section = newSection;
    }

    /* access modifiers changed from: protected */
    public void drawSpeedUnitText(Canvas canvas) {
        RectF r = getSpeedUnitTextBounds();
        updateSpeedUnitTextBitmap(getSpeedText());
        canvas.drawBitmap(this.speedUnitTextBitmap, (r.left - (((float) this.speedUnitTextBitmap.getWidth()) * 0.5f)) + (r.width() * 0.5f), (r.top - (((float) this.speedUnitTextBitmap.getHeight()) * 0.5f)) + (r.height() * 0.5f), this.speedUnitTextBitmapPaint);
    }

    private void updateSpeedUnitTextBitmap(String speedText) {
        float speedX;
        float unitX;
        this.speedUnitTextBitmap.eraseColor(0);
        if (this.unitUnderSpeedText) {
            this.speedUnitTextCanvas.drawText(speedText, ((float) this.speedUnitTextBitmap.getWidth()) * 0.5f, (((float) this.speedUnitTextBitmap.getHeight()) * 0.5f) - (this.unitSpeedInterval * 0.5f), this.speedTextPaint);
            this.speedUnitTextCanvas.drawText(this.unit, ((float) this.speedUnitTextBitmap.getWidth()) * 0.5f, (((float) this.speedUnitTextBitmap.getHeight()) * 0.5f) + this.unitTextPaint.getTextSize() + (this.unitSpeedInterval * 0.5f), this.unitTextPaint);
            return;
        }
        if (isSpeedometerTextRightToLeft()) {
            unitX = (((float) this.speedUnitTextBitmap.getWidth()) * 0.5f) - (getSpeedUnitTextWidth() * 0.5f);
            speedX = this.unitTextPaint.measureText(this.unit) + unitX + this.unitSpeedInterval;
        } else {
            speedX = (((float) this.speedUnitTextBitmap.getWidth()) * 0.5f) - (getSpeedUnitTextWidth() * 0.5f);
            unitX = this.speedTextPaint.measureText(speedText) + speedX + this.unitSpeedInterval;
        }
        float h = (((float) this.speedUnitTextBitmap.getHeight()) * 0.5f) + (getSpeedUnitTextHeight() * 0.5f);
        this.speedUnitTextCanvas.drawText(speedText, speedX, h, this.speedTextPaint);
        this.speedUnitTextCanvas.drawText(this.unit, unitX, h, this.unitTextPaint);
    }

    /* access modifiers changed from: protected */
    public RectF getSpeedUnitTextBounds() {
        float left = ((((((float) getWidthPa()) * this.speedTextPosition.x) - this.translatedDx) + ((float) this.padding)) - (getSpeedUnitTextWidth() * this.speedTextPosition.width)) + (this.speedTextPadding * ((float) this.speedTextPosition.paddingH));
        float top = ((((((float) getHeightPa()) * this.speedTextPosition.y) - this.translatedDy) + ((float) this.padding)) - (getSpeedUnitTextHeight() * this.speedTextPosition.height)) + (this.speedTextPadding * ((float) this.speedTextPosition.paddingV));
        return new RectF(left, top, getSpeedUnitTextWidth() + left, getSpeedUnitTextHeight() + top);
    }

    private float getSpeedUnitTextWidth() {
        if (this.unitUnderSpeedText) {
            return Math.max(this.speedTextPaint.measureText(getSpeedText()), this.unitTextPaint.measureText(getUnit()));
        }
        return this.speedTextPaint.measureText(getSpeedText()) + this.unitTextPaint.measureText(getUnit()) + this.unitSpeedInterval;
    }

    private float getSpeedUnitTextHeight() {
        if (this.unitUnderSpeedText) {
            return this.speedTextPaint.getTextSize() + this.unitTextPaint.getTextSize() + this.unitSpeedInterval;
        }
        return Math.max(this.speedTextPaint.getTextSize(), this.unitTextPaint.getTextSize());
    }

    /* access modifiers changed from: protected */
    public Canvas createBackgroundBitmapCanvas() {
        if (getWidth() == 0 || getHeight() == 0) {
            return new Canvas();
        }
        this.backgroundBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        return new Canvas(this.backgroundBitmap);
    }

    /* access modifiers changed from: protected */
    public void onSectionChangeEvent(byte oldSection, byte newSection) {
        if (this.onSectionChangeListener != null) {
            this.onSectionChangeListener.onSectionChangeListener(oldSection, newSection);
        }
    }

    @TargetApi(11)
    public void stop() {
        if (Build.VERSION.SDK_INT >= 11) {
            if (this.speedAnimator.isRunning() || this.realSpeedAnimator.isRunning()) {
                this.speed = this.currentSpeed;
                cancelSpeedAnimator();
                tremble();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void cancelSpeedAnimator() {
        cancelSpeedMove();
        cancelTremble();
    }

    @TargetApi(11)
    private void cancelTremble() {
        if (Build.VERSION.SDK_INT >= 11) {
            this.canceled = true;
            this.trembleAnimator.cancel();
            this.canceled = false;
        }
    }

    @TargetApi(11)
    private void cancelSpeedMove() {
        if (Build.VERSION.SDK_INT >= 11) {
            this.canceled = true;
            this.speedAnimator.cancel();
            this.realSpeedAnimator.cancel();
            this.canceled = false;
        }
    }

    public void setSpeedAt(float speed2) {
        if (speed2 > this.maxSpeed) {
            speed2 = this.maxSpeed;
        } else if (speed2 < this.minSpeed) {
            speed2 = this.minSpeed;
        }
        this.isSpeedIncrease = speed2 > this.currentSpeed;
        this.speed = speed2;
        this.currentSpeed = speed2;
        cancelSpeedAnimator();
        invalidate();
        tremble();
    }

    public void speedPercentTo(int percent) {
        speedPercentTo(percent, 2000);
    }

    public void speedPercentTo(int percent, long moveDuration) {
        speedTo(getSpeedValue((float) percent), moveDuration);
    }

    public void speedTo(float speed2) {
        speedTo(speed2, 2000);
    }

    @TargetApi(11)
    public void speedTo(float speed2, long moveDuration) {
        if (speed2 > this.maxSpeed) {
            speed2 = this.maxSpeed;
        } else if (speed2 < this.minSpeed) {
            speed2 = this.minSpeed;
        }
        if (speed2 != this.speed) {
            this.speed = speed2;
            if (Build.VERSION.SDK_INT < 11) {
                setSpeedAt(speed2);
                return;
            }
            this.isSpeedIncrease = speed2 > this.currentSpeed;
            cancelSpeedAnimator();
            this.speedAnimator = ValueAnimator.ofFloat(new float[]{this.currentSpeed, speed2});
            this.speedAnimator.setInterpolator(new DecelerateInterpolator());
            this.speedAnimator.setDuration(moveDuration);
            this.speedAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float unused = Gauge.this.currentSpeed = ((Float) Gauge.this.speedAnimator.getAnimatedValue()).floatValue();
                    Gauge.this.postInvalidate();
                }
            });
            this.speedAnimator.addListener(this.animatorListener);
            this.speedAnimator.start();
        }
    }

    public void speedUp() {
        realSpeedTo(getMaxSpeed());
    }

    public void slowDown() {
        realSpeedTo(0.0f);
    }

    public void realSpeedPercentTo(float percent) {
        realSpeedTo(getSpeedValue(percent));
    }

    @TargetApi(11)
    public void realSpeedTo(float speed2) {
        boolean oldIsSpeedUp;
        boolean z;
        if (this.speed > this.currentSpeed) {
            oldIsSpeedUp = true;
        } else {
            oldIsSpeedUp = false;
        }
        if (speed2 > this.maxSpeed) {
            speed2 = this.maxSpeed;
        } else if (speed2 < this.minSpeed) {
            speed2 = this.minSpeed;
        }
        if (speed2 != this.speed) {
            this.speed = speed2;
            if (Build.VERSION.SDK_INT < 11) {
                setSpeedAt(speed2);
                return;
            }
            if (speed2 > this.currentSpeed) {
                z = true;
            } else {
                z = false;
            }
            this.isSpeedIncrease = z;
            if (!this.realSpeedAnimator.isRunning() || oldIsSpeedUp != this.isSpeedIncrease) {
                cancelSpeedAnimator();
                this.realSpeedAnimator = ValueAnimator.ofInt(new int[]{(int) this.currentSpeed, (int) speed2});
                this.realSpeedAnimator.setRepeatCount(-1);
                this.realSpeedAnimator.setInterpolator(new LinearInterpolator());
                this.realSpeedAnimator.setDuration(Math.abs((long) ((speed2 - this.currentSpeed) * 10.0f)));
                final float finalSpeed = speed2;
                this.realSpeedAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (Gauge.this.isSpeedIncrease) {
                            float unused = Gauge.this.currentSpeed = Gauge.this.currentSpeed + (Gauge.this.accelerate * 10.0f * (100.005f - Gauge.this.getPercentSpeed()) * 0.01f);
                            if (Gauge.this.currentSpeed > finalSpeed) {
                                float unused2 = Gauge.this.currentSpeed = finalSpeed;
                            }
                        } else {
                            float unused3 = Gauge.this.currentSpeed = Gauge.this.currentSpeed - ((((Gauge.this.decelerate * 10.0f) * (Gauge.this.getPercentSpeed() + 0.005f)) * 0.01f) + 0.1f);
                            if (Gauge.this.currentSpeed < finalSpeed) {
                                float unused4 = Gauge.this.currentSpeed = finalSpeed;
                            }
                        }
                        Gauge.this.postInvalidate();
                        if (finalSpeed == Gauge.this.currentSpeed) {
                            Gauge.this.stop();
                        }
                    }
                });
                this.realSpeedAnimator.addListener(this.animatorListener);
                this.realSpeedAnimator.start();
            }
        }
    }

    /* access modifiers changed from: protected */
    @TargetApi(11)
    public void tremble() {
        int i;
        cancelTremble();
        if (isWithTremble() && Build.VERSION.SDK_INT >= 11) {
            Random random = new Random();
            float nextFloat = random.nextFloat() * this.trembleDegree;
            if (random.nextBoolean()) {
                i = -1;
            } else {
                i = 1;
            }
            float mad = nextFloat * ((float) i);
            if (this.speed + mad > this.maxSpeed) {
                mad = this.maxSpeed - this.speed;
            } else if (this.speed + mad < this.minSpeed) {
                mad = this.minSpeed - this.speed;
            }
            this.trembleAnimator = ValueAnimator.ofFloat(new float[]{this.currentSpeed, this.speed + mad});
            this.trembleAnimator.setInterpolator(new DecelerateInterpolator());
            this.trembleAnimator.setDuration((long) this.trembleDuration);
            this.trembleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    boolean unused = Gauge.this.isSpeedIncrease = ((Float) Gauge.this.trembleAnimator.getAnimatedValue()).floatValue() > Gauge.this.currentSpeed;
                    float unused2 = Gauge.this.currentSpeed = ((Float) Gauge.this.trembleAnimator.getAnimatedValue()).floatValue();
                    Gauge.this.postInvalidate();
                }
            });
            this.trembleAnimator.addListener(this.animatorListener);
            this.trembleAnimator.start();
        }
    }

    private float getSpeedValue(float percentSpeed) {
        if (percentSpeed > 100.0f) {
            percentSpeed = 100.0f;
        } else if (percentSpeed < 0.0f) {
            percentSpeed = 0.0f;
        }
        return ((this.maxSpeed - this.minSpeed) * percentSpeed * 0.01f) + this.minSpeed;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attachedToWindow = true;
        updateBackgroundBitmap();
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelSpeedAnimator();
        this.attachedToWindow = false;
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putFloat("speed", this.speed);
        return bundle;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        this.speed = bundle.getFloat("speed");
        super.onRestoreInstanceState(bundle.getParcelable("superState"));
        setSpeedAt(this.speed);
    }

    public void setTrembleDegree(float trembleDegree2) {
        setTrembleData(trembleDegree2, this.trembleDuration);
    }

    public void setTrembleDuration(int trembleDuration2) {
        setTrembleData(this.trembleDegree, trembleDuration2);
    }

    public void setTrembleData(float trembleDegree2, int trembleDuration2) {
        this.trembleDegree = trembleDegree2;
        this.trembleDuration = trembleDuration2;
        checkTrembleData();
    }

    public int getSpeedTextFormat() {
        return this.speedTextFormat;
    }

    public void setSpeedTextFormat(int speedTextFormat2) {
        this.speedTextFormat = speedTextFormat2;
        if (this.attachedToWindow) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public int getTickTextFormat() {
        return this.tickTextFormat;
    }

    public void setTickTextFormat(int tickTextFormat2) {
        this.tickTextFormat = tickTextFormat2;
        if (this.attachedToWindow) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public String getSpeedText() {
        return String.format(this.locale, "%." + this.speedTextFormat + "f", new Object[]{Float.valueOf(this.currentSpeed)});
    }

    /* access modifiers changed from: protected */
    public String getMaxSpeedText() {
        return String.format(this.locale, "%." + this.tickTextFormat + "f", new Object[]{Float.valueOf(this.maxSpeed)});
    }

    /* access modifiers changed from: protected */
    public String getMinSpeedText() {
        return String.format(this.locale, "%." + this.tickTextFormat + "f", new Object[]{Float.valueOf(this.minSpeed)});
    }

    public void setWithTremble(boolean withTremble2) {
        this.withTremble = withTremble2;
        tremble();
    }

    public boolean isWithTremble() {
        return this.withTremble;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getCurrentSpeed() {
        return this.currentSpeed;
    }

    public boolean isSpeedIncrease() {
        return this.isSpeedIncrease;
    }

    public int getCurrentIntSpeed() {
        return this.currentIntSpeed;
    }

    public float getMaxSpeed() {
        return this.maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed2) {
        setMinMaxSpeed(this.minSpeed, maxSpeed2);
    }

    public float getMinSpeed() {
        return this.minSpeed;
    }

    public void setMinSpeed(float minSpeed2) {
        setMinMaxSpeed(minSpeed2, this.maxSpeed);
    }

    public void setMinMaxSpeed(float minSpeed2, float maxSpeed2) {
        if (minSpeed2 >= maxSpeed2) {
            throw new IllegalArgumentException("minSpeed must be smaller than maxSpeed !!");
        }
        cancelSpeedAnimator();
        this.minSpeed = minSpeed2;
        this.maxSpeed = maxSpeed2;
        if (this.attachedToWindow) {
            updateBackgroundBitmap();
            setSpeedAt(this.speed);
        }
    }

    public float getPercentSpeed() {
        return ((this.currentSpeed - this.minSpeed) * 100.0f) / (this.maxSpeed - this.minSpeed);
    }

    public float getOffsetSpeed() {
        return (this.currentSpeed - this.minSpeed) / (this.maxSpeed - this.minSpeed);
    }

    public int getTextColor() {
        return this.textPaint.getColor();
    }

    public void setTextColor(int textColor) {
        this.textPaint.setColor(textColor);
        if (this.attachedToWindow) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public int getSpeedTextColor() {
        return this.speedTextPaint.getColor();
    }

    public void setSpeedTextColor(int speedTextColor) {
        this.speedTextPaint.setColor(speedTextColor);
        if (this.attachedToWindow) {
            invalidate();
        }
    }

    public int getUnitTextColor() {
        return this.unitTextPaint.getColor();
    }

    public void setUnitTextColor(int unitTextColor) {
        this.unitTextPaint.setColor(unitTextColor);
        if (this.attachedToWindow) {
            invalidate();
        }
    }

    public float getTextSize() {
        return this.textPaint.getTextSize();
    }

    public void setTextSize(float textSize) {
        this.textPaint.setTextSize(textSize);
        if (this.attachedToWindow) {
            invalidate();
        }
    }

    public float getSpeedTextSize() {
        return this.speedTextPaint.getTextSize();
    }

    public void setSpeedTextSize(float speedTextSize) {
        this.speedTextPaint.setTextSize(speedTextSize);
        if (this.attachedToWindow) {
            invalidate();
        }
    }

    public float getUnitTextSize() {
        return this.unitTextPaint.getTextSize();
    }

    public void setUnitTextSize(float unitTextSize) {
        this.unitTextPaint.setTextSize(unitTextSize);
        if (this.attachedToWindow) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit2) {
        this.unit = unit2;
        if (this.attachedToWindow) {
            invalidate();
        }
    }

    public void setOnSpeedChangeListener(OnSpeedChangeListener onSpeedChangeListener2) {
        this.onSpeedChangeListener = onSpeedChangeListener2;
    }

    public void setOnSectionChangeListener(OnSectionChangeListener onSectionChangeListener2) {
        this.onSectionChangeListener = onSectionChangeListener2;
    }

    public int getLowSpeedPercent() {
        return this.lowSpeedPercent;
    }

    public float getLowSpeedOffset() {
        return ((float) this.lowSpeedPercent) * 0.01f;
    }

    public void setLowSpeedPercent(int lowSpeedPercent2) {
        this.lowSpeedPercent = lowSpeedPercent2;
        checkSpeedometerPercent();
        if (this.attachedToWindow) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public int getMediumSpeedPercent() {
        return this.mediumSpeedPercent;
    }

    public float getMediumSpeedOffset() {
        return ((float) this.mediumSpeedPercent) * 0.01f;
    }

    public void setMediumSpeedPercent(int mediumSpeedPercent2) {
        this.mediumSpeedPercent = mediumSpeedPercent2;
        checkSpeedometerPercent();
        if (this.attachedToWindow) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public boolean isSpeedometerTextRightToLeft() {
        return this.speedometerTextRightToLeft;
    }

    public void setSpeedometerTextRightToLeft(boolean speedometerTextRightToLeft2) {
        this.speedometerTextRightToLeft = speedometerTextRightToLeft2;
        if (this.attachedToWindow) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public int getWidthPa() {
        return this.widthPa;
    }

    public int getHeightPa() {
        return this.heightPa;
    }

    public int getViewSize() {
        return Math.max(getWidth(), getHeight());
    }

    public int getViewSizePa() {
        return Math.max(this.widthPa, this.heightPa);
    }

    public void setPadding(int left, int top, int right, int bottom) {
        updatePadding(left, top, right, bottom);
        super.setPadding(this.padding, this.padding, this.padding, this.padding);
    }

    public void setPaddingRelative(int start, int top, int end, int bottom) {
        updatePadding(start, top, end, bottom);
        super.setPaddingRelative(this.padding, this.padding, this.padding, this.padding);
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale2) {
        this.locale = locale2;
        if (this.attachedToWindow) {
            invalidate();
        }
    }

    public boolean isInLowSection() {
        return ((this.maxSpeed - this.minSpeed) * getLowSpeedOffset()) + this.minSpeed >= this.currentSpeed;
    }

    public boolean isInMediumSection() {
        return ((this.maxSpeed - this.minSpeed) * getMediumSpeedOffset()) + this.minSpeed >= this.currentSpeed && !isInLowSection();
    }

    public boolean isInHighSection() {
        return this.currentSpeed > ((this.maxSpeed - this.minSpeed) * getMediumSpeedOffset()) + this.minSpeed;
    }

    public byte getSection() {
        if (isInLowSection()) {
            return 1;
        }
        if (isInMediumSection()) {
            return 2;
        }
        return 3;
    }

    public int getPadding() {
        return this.padding;
    }

    public boolean isAttachedToWindow() {
        return this.attachedToWindow;
    }

    public Typeface getSpeedTextTypeface() {
        return this.speedTextPaint.getTypeface();
    }

    public void setSpeedTextTypeface(Typeface typeface) {
        this.speedTextPaint.setTypeface(typeface);
        this.unitTextPaint.setTypeface(typeface);
        if (this.attachedToWindow) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public Typeface getTextTypeface() {
        return this.textPaint.getTypeface();
    }

    public void setTextTypeface(Typeface typeface) {
        this.textPaint.setTypeface(typeface);
        if (this.attachedToWindow) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public float getAccelerate() {
        return this.accelerate;
    }

    public void setAccelerate(float accelerate2) {
        this.accelerate = accelerate2;
        checkAccelerate();
    }

    public float getDecelerate() {
        return this.decelerate;
    }

    public void setDecelerate(float decelerate2) {
        this.decelerate = decelerate2;
    }

    /* access modifiers changed from: protected */
    public final float getTranslatedDx() {
        return this.translatedDx;
    }

    /* access modifiers changed from: protected */
    public final float getTranslatedDy() {
        return this.translatedDy;
    }

    public float getUnitSpeedInterval() {
        return this.unitSpeedInterval;
    }

    public void setUnitSpeedInterval(float unitSpeedInterval2) {
        this.unitSpeedInterval = unitSpeedInterval2;
        if (this.attachedToWindow) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public float getSpeedTextPadding() {
        return this.speedTextPadding;
    }

    public void setSpeedTextPadding(float speedTextPadding2) {
        this.speedTextPadding = speedTextPadding2;
        if (this.attachedToWindow) {
            invalidate();
        }
    }

    public boolean isUnitUnderSpeedText() {
        return this.unitUnderSpeedText;
    }

    public void setUnitUnderSpeedText(boolean unitUnderSpeedText2) {
        this.unitUnderSpeedText = unitUnderSpeedText2;
        if (unitUnderSpeedText2) {
            this.speedTextPaint.setTextAlign(Paint.Align.CENTER);
            this.unitTextPaint.setTextAlign(Paint.Align.CENTER);
        } else {
            this.speedTextPaint.setTextAlign(Paint.Align.LEFT);
            this.unitTextPaint.setTextAlign(Paint.Align.LEFT);
        }
        if (this.attachedToWindow) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public void setSpeedTextPosition(Position position) {
        this.speedTextPosition = position;
        if (this.attachedToWindow) {
            updateBackgroundBitmap();
            invalidate();
        }
    }

    public enum Position {
        TOP_LEFT(0.0f, 0.0f, 0.0f, 0.0f, 1, 1),
        TOP_CENTER(0.5f, 0.0f, 0.5f, 0.0f, 0, 1),
        TOP_RIGHT(1.0f, 0.0f, 1.0f, 0.0f, -1, 1),
        LEFT(0.0f, 0.5f, 0.0f, 0.5f, 1, 0),
        CENTER(0.5f, 0.5f, 0.5f, 0.5f, 0, 0),
        RIGHT(1.0f, 0.5f, 1.0f, 0.5f, -1, 0),
        BOTTOM_LEFT(0.0f, 1.0f, 0.0f, 1.0f, 1, -1),
        BOTTOM_CENTER(0.5f, 1.0f, 0.5f, 1.0f, 0, -1),
        BOTTOM_RIGHT(1.0f, 1.0f, 1.0f, 1.0f, -1, -1);
        
        final float height;
        final int paddingH;
        final int paddingV;
        final float width;
        final float x;
        final float y;

        private Position(float x2, float y2, float width2, float height2, int paddingH2, int paddingV2) {
            this.x = x2;
            this.y = y2;
            this.width = width2;
            this.height = height2;
            this.paddingH = paddingH2;
            this.paddingV = paddingV2;
        }
    }
}
