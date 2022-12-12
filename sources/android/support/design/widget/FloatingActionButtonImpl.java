package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.R;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;

@RequiresApi(14)
class FloatingActionButtonImpl {
    static final Interpolator ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
    static final int ANIM_STATE_HIDING = 1;
    static final int ANIM_STATE_NONE = 0;
    static final int ANIM_STATE_SHOWING = 2;
    static final int[] EMPTY_STATE_SET = new int[0];
    static final int[] ENABLED_STATE_SET = {16842910};
    static final int[] FOCUSED_ENABLED_STATE_SET = {16842908, 16842910};
    static final long PRESSED_ANIM_DELAY = 100;
    static final long PRESSED_ANIM_DURATION = 100;
    static final int[] PRESSED_ENABLED_STATE_SET = {16842919, 16842910};
    static final int SHOW_HIDE_ANIM_DURATION = 200;
    int mAnimState = 0;
    CircularBorderDrawable mBorderDrawable;
    Drawable mContentBackground;
    float mElevation;
    private ViewTreeObserver.OnPreDrawListener mPreDrawListener;
    float mPressedTranslationZ;
    Drawable mRippleDrawable;
    private float mRotation;
    ShadowDrawableWrapper mShadowDrawable;
    final ShadowViewDelegate mShadowViewDelegate;
    Drawable mShapeDrawable;
    private final StateListAnimator mStateListAnimator;
    private final Rect mTmpRect = new Rect();
    final VisibilityAwareImageButton mView;

    interface InternalVisibilityChangedListener {
        void onHidden();

        void onShown();
    }

    FloatingActionButtonImpl(VisibilityAwareImageButton view, ShadowViewDelegate shadowViewDelegate) {
        this.mView = view;
        this.mShadowViewDelegate = shadowViewDelegate;
        this.mStateListAnimator = new StateListAnimator();
        this.mStateListAnimator.addState(PRESSED_ENABLED_STATE_SET, createAnimator(new ElevateToTranslationZAnimation()));
        this.mStateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, createAnimator(new ElevateToTranslationZAnimation()));
        this.mStateListAnimator.addState(ENABLED_STATE_SET, createAnimator(new ResetElevationAnimation()));
        this.mStateListAnimator.addState(EMPTY_STATE_SET, createAnimator(new DisabledElevationAnimation()));
        this.mRotation = this.mView.getRotation();
    }

    /* access modifiers changed from: package-private */
    public void setBackgroundDrawable(ColorStateList backgroundTint, PorterDuff.Mode backgroundTintMode, int rippleColor, int borderWidth) {
        Drawable[] layers;
        this.mShapeDrawable = DrawableCompat.wrap(createShapeDrawable());
        DrawableCompat.setTintList(this.mShapeDrawable, backgroundTint);
        if (backgroundTintMode != null) {
            DrawableCompat.setTintMode(this.mShapeDrawable, backgroundTintMode);
        }
        this.mRippleDrawable = DrawableCompat.wrap(createShapeDrawable());
        DrawableCompat.setTintList(this.mRippleDrawable, createColorStateList(rippleColor));
        if (borderWidth > 0) {
            this.mBorderDrawable = createBorderDrawable(borderWidth, backgroundTint);
            layers = new Drawable[]{this.mBorderDrawable, this.mShapeDrawable, this.mRippleDrawable};
        } else {
            this.mBorderDrawable = null;
            layers = new Drawable[]{this.mShapeDrawable, this.mRippleDrawable};
        }
        this.mContentBackground = new LayerDrawable(layers);
        this.mShadowDrawable = new ShadowDrawableWrapper(this.mView.getContext(), this.mContentBackground, this.mShadowViewDelegate.getRadius(), this.mElevation, this.mElevation + this.mPressedTranslationZ);
        this.mShadowDrawable.setAddPaddingForCorners(false);
        this.mShadowViewDelegate.setBackgroundDrawable(this.mShadowDrawable);
    }

    /* access modifiers changed from: package-private */
    public void setBackgroundTintList(ColorStateList tint) {
        if (this.mShapeDrawable != null) {
            DrawableCompat.setTintList(this.mShapeDrawable, tint);
        }
        if (this.mBorderDrawable != null) {
            this.mBorderDrawable.setBorderTint(tint);
        }
    }

    /* access modifiers changed from: package-private */
    public void setBackgroundTintMode(PorterDuff.Mode tintMode) {
        if (this.mShapeDrawable != null) {
            DrawableCompat.setTintMode(this.mShapeDrawable, tintMode);
        }
    }

    /* access modifiers changed from: package-private */
    public void setRippleColor(int rippleColor) {
        if (this.mRippleDrawable != null) {
            DrawableCompat.setTintList(this.mRippleDrawable, createColorStateList(rippleColor));
        }
    }

    /* access modifiers changed from: package-private */
    public final void setElevation(float elevation) {
        if (this.mElevation != elevation) {
            this.mElevation = elevation;
            onElevationsChanged(elevation, this.mPressedTranslationZ);
        }
    }

    /* access modifiers changed from: package-private */
    public float getElevation() {
        return this.mElevation;
    }

    /* access modifiers changed from: package-private */
    public final void setPressedTranslationZ(float translationZ) {
        if (this.mPressedTranslationZ != translationZ) {
            this.mPressedTranslationZ = translationZ;
            onElevationsChanged(this.mElevation, translationZ);
        }
    }

    /* access modifiers changed from: package-private */
    public void onElevationsChanged(float elevation, float pressedTranslationZ) {
        if (this.mShadowDrawable != null) {
            this.mShadowDrawable.setShadowSize(elevation, this.mPressedTranslationZ + elevation);
            updatePadding();
        }
    }

    /* access modifiers changed from: package-private */
    public void onDrawableStateChanged(int[] state) {
        this.mStateListAnimator.setState(state);
    }

    /* access modifiers changed from: package-private */
    public void jumpDrawableToCurrentState() {
        this.mStateListAnimator.jumpToCurrentState();
    }

    /* access modifiers changed from: package-private */
    public void hide(@Nullable final InternalVisibilityChangedListener listener, final boolean fromUser) {
        if (!isOrWillBeHidden()) {
            this.mView.animate().cancel();
            if (shouldAnimateVisibilityChange()) {
                this.mAnimState = 1;
                this.mView.animate().scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setDuration(200).setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
                    private boolean mCancelled;

                    public void onAnimationStart(Animator animation) {
                        FloatingActionButtonImpl.this.mView.internalSetVisibility(0, fromUser);
                        this.mCancelled = false;
                    }

                    public void onAnimationCancel(Animator animation) {
                        this.mCancelled = true;
                    }

                    public void onAnimationEnd(Animator animation) {
                        FloatingActionButtonImpl.this.mAnimState = 0;
                        if (!this.mCancelled) {
                            FloatingActionButtonImpl.this.mView.internalSetVisibility(fromUser ? 8 : 4, fromUser);
                            if (listener != null) {
                                listener.onHidden();
                            }
                        }
                    }
                });
                return;
            }
            this.mView.internalSetVisibility(fromUser ? 8 : 4, fromUser);
            if (listener != null) {
                listener.onHidden();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void show(@Nullable final InternalVisibilityChangedListener listener, final boolean fromUser) {
        if (!isOrWillBeShown()) {
            this.mView.animate().cancel();
            if (shouldAnimateVisibilityChange()) {
                this.mAnimState = 2;
                if (this.mView.getVisibility() != 0) {
                    this.mView.setAlpha(0.0f);
                    this.mView.setScaleY(0.0f);
                    this.mView.setScaleX(0.0f);
                }
                this.mView.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(200).setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animation) {
                        FloatingActionButtonImpl.this.mView.internalSetVisibility(0, fromUser);
                    }

                    public void onAnimationEnd(Animator animation) {
                        FloatingActionButtonImpl.this.mAnimState = 0;
                        if (listener != null) {
                            listener.onShown();
                        }
                    }
                });
                return;
            }
            this.mView.internalSetVisibility(0, fromUser);
            this.mView.setAlpha(1.0f);
            this.mView.setScaleY(1.0f);
            this.mView.setScaleX(1.0f);
            if (listener != null) {
                listener.onShown();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final Drawable getContentBackground() {
        return this.mContentBackground;
    }

    /* access modifiers changed from: package-private */
    public void onCompatShadowChanged() {
    }

    /* access modifiers changed from: package-private */
    public final void updatePadding() {
        Rect rect = this.mTmpRect;
        getPadding(rect);
        onPaddingUpdated(rect);
        this.mShadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    /* access modifiers changed from: package-private */
    public void getPadding(Rect rect) {
        this.mShadowDrawable.getPadding(rect);
    }

    /* access modifiers changed from: package-private */
    public void onPaddingUpdated(Rect padding) {
    }

    /* access modifiers changed from: package-private */
    public void onAttachedToWindow() {
        if (requirePreDrawListener()) {
            ensurePreDrawListener();
            this.mView.getViewTreeObserver().addOnPreDrawListener(this.mPreDrawListener);
        }
    }

    /* access modifiers changed from: package-private */
    public void onDetachedFromWindow() {
        if (this.mPreDrawListener != null) {
            this.mView.getViewTreeObserver().removeOnPreDrawListener(this.mPreDrawListener);
            this.mPreDrawListener = null;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean requirePreDrawListener() {
        return true;
    }

    /* access modifiers changed from: package-private */
    public CircularBorderDrawable createBorderDrawable(int borderWidth, ColorStateList backgroundTint) {
        Context context = this.mView.getContext();
        CircularBorderDrawable borderDrawable = newCircularDrawable();
        borderDrawable.setGradientColors(ContextCompat.getColor(context, R.color.design_fab_stroke_top_outer_color), ContextCompat.getColor(context, R.color.design_fab_stroke_top_inner_color), ContextCompat.getColor(context, R.color.design_fab_stroke_end_inner_color), ContextCompat.getColor(context, R.color.design_fab_stroke_end_outer_color));
        borderDrawable.setBorderWidth((float) borderWidth);
        borderDrawable.setBorderTint(backgroundTint);
        return borderDrawable;
    }

    /* access modifiers changed from: package-private */
    public CircularBorderDrawable newCircularDrawable() {
        return new CircularBorderDrawable();
    }

    /* access modifiers changed from: package-private */
    public void onPreDraw() {
        float rotation = this.mView.getRotation();
        if (this.mRotation != rotation) {
            this.mRotation = rotation;
            updateFromViewRotation();
        }
    }

    private void ensurePreDrawListener() {
        if (this.mPreDrawListener == null) {
            this.mPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    FloatingActionButtonImpl.this.onPreDraw();
                    return true;
                }
            };
        }
    }

    /* access modifiers changed from: package-private */
    public GradientDrawable createShapeDrawable() {
        GradientDrawable d = newGradientDrawableForShape();
        d.setShape(1);
        d.setColor(-1);
        return d;
    }

    /* access modifiers changed from: package-private */
    public GradientDrawable newGradientDrawableForShape() {
        return new GradientDrawable();
    }

    /* access modifiers changed from: package-private */
    public boolean isOrWillBeShown() {
        if (this.mView.getVisibility() != 0) {
            if (this.mAnimState == 2) {
                return true;
            }
            return false;
        } else if (this.mAnimState == 1) {
            return false;
        } else {
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isOrWillBeHidden() {
        if (this.mView.getVisibility() == 0) {
            if (this.mAnimState == 1) {
                return true;
            }
            return false;
        } else if (this.mAnimState == 2) {
            return false;
        } else {
            return true;
        }
    }

    private ValueAnimator createAnimator(@NonNull ShadowAnimatorImpl impl) {
        ValueAnimator animator = new ValueAnimator();
        animator.setInterpolator(ANIM_INTERPOLATOR);
        animator.setDuration(100);
        animator.addListener(impl);
        animator.addUpdateListener(impl);
        animator.setFloatValues(new float[]{0.0f, 1.0f});
        return animator;
    }

    private abstract class ShadowAnimatorImpl extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {
        private float mShadowSizeEnd;
        private float mShadowSizeStart;
        private boolean mValidValues;

        /* access modifiers changed from: protected */
        public abstract float getTargetShadowSize();

        private ShadowAnimatorImpl() {
        }

        public void onAnimationUpdate(ValueAnimator animator) {
            if (!this.mValidValues) {
                this.mShadowSizeStart = FloatingActionButtonImpl.this.mShadowDrawable.getShadowSize();
                this.mShadowSizeEnd = getTargetShadowSize();
                this.mValidValues = true;
            }
            FloatingActionButtonImpl.this.mShadowDrawable.setShadowSize(this.mShadowSizeStart + ((this.mShadowSizeEnd - this.mShadowSizeStart) * animator.getAnimatedFraction()));
        }

        public void onAnimationEnd(Animator animator) {
            FloatingActionButtonImpl.this.mShadowDrawable.setShadowSize(this.mShadowSizeEnd);
            this.mValidValues = false;
        }
    }

    private class ResetElevationAnimation extends ShadowAnimatorImpl {
        ResetElevationAnimation() {
            super();
        }

        /* access modifiers changed from: protected */
        public float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.mElevation;
        }
    }

    private class ElevateToTranslationZAnimation extends ShadowAnimatorImpl {
        ElevateToTranslationZAnimation() {
            super();
        }

        /* access modifiers changed from: protected */
        public float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.mElevation + FloatingActionButtonImpl.this.mPressedTranslationZ;
        }
    }

    private class DisabledElevationAnimation extends ShadowAnimatorImpl {
        DisabledElevationAnimation() {
            super();
        }

        /* access modifiers changed from: protected */
        public float getTargetShadowSize() {
            return 0.0f;
        }
    }

    private static ColorStateList createColorStateList(int selectedColor) {
        int[][] states = new int[3][];
        int[] colors = new int[3];
        states[0] = FOCUSED_ENABLED_STATE_SET;
        colors[0] = selectedColor;
        int i = 0 + 1;
        states[i] = PRESSED_ENABLED_STATE_SET;
        colors[i] = selectedColor;
        int i2 = i + 1;
        states[i2] = new int[0];
        colors[i2] = 0;
        int i3 = i2 + 1;
        return new ColorStateList(states, colors);
    }

    private boolean shouldAnimateVisibilityChange() {
        return ViewCompat.isLaidOut(this.mView) && !this.mView.isInEditMode();
    }

    private void updateFromViewRotation() {
        if (Build.VERSION.SDK_INT == 19) {
            if (this.mRotation % 90.0f != 0.0f) {
                if (this.mView.getLayerType() != 1) {
                    this.mView.setLayerType(1, (Paint) null);
                }
            } else if (this.mView.getLayerType() != 0) {
                this.mView.setLayerType(0, (Paint) null);
            }
        }
        if (this.mShadowDrawable != null) {
            this.mShadowDrawable.setRotation(-this.mRotation);
        }
        if (this.mBorderDrawable != null) {
            this.mBorderDrawable.setRotation(-this.mRotation);
        }
    }
}
