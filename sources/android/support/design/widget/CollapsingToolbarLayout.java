package android.support.design.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.design.R;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.math.MathUtils;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CollapsingToolbarLayout extends FrameLayout {
    private static final int DEFAULT_SCRIM_ANIMATION_DURATION = 600;
    final CollapsingTextHelper mCollapsingTextHelper;
    private boolean mCollapsingTitleEnabled;
    private Drawable mContentScrim;
    int mCurrentOffset;
    private boolean mDrawCollapsingTitle;
    private View mDummyView;
    private int mExpandedMarginBottom;
    private int mExpandedMarginEnd;
    private int mExpandedMarginStart;
    private int mExpandedMarginTop;
    WindowInsetsCompat mLastInsets;
    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
    private boolean mRefreshToolbar;
    private int mScrimAlpha;
    private long mScrimAnimationDuration;
    private ValueAnimator mScrimAnimator;
    private int mScrimVisibleHeightTrigger;
    private boolean mScrimsAreShown;
    Drawable mStatusBarScrim;
    private final Rect mTmpRect;
    private Toolbar mToolbar;
    private View mToolbarDirectChild;
    private int mToolbarId;

    public CollapsingToolbarLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public CollapsingToolbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mRefreshToolbar = true;
        this.mTmpRect = new Rect();
        this.mScrimVisibleHeightTrigger = -1;
        ThemeUtils.checkAppCompatTheme(context);
        this.mCollapsingTextHelper = new CollapsingTextHelper(this);
        this.mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CollapsingToolbarLayout, defStyleAttr, R.style.Widget_Design_CollapsingToolbar);
        this.mCollapsingTextHelper.setExpandedTextGravity(a.getInt(R.styleable.CollapsingToolbarLayout_expandedTitleGravity, 8388691));
        this.mCollapsingTextHelper.setCollapsedTextGravity(a.getInt(R.styleable.CollapsingToolbarLayout_collapsedTitleGravity, 8388627));
        int dimensionPixelSize = a.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMargin, 0);
        this.mExpandedMarginBottom = dimensionPixelSize;
        this.mExpandedMarginEnd = dimensionPixelSize;
        this.mExpandedMarginTop = dimensionPixelSize;
        this.mExpandedMarginStart = dimensionPixelSize;
        if (a.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart)) {
            this.mExpandedMarginStart = a.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart, 0);
        }
        if (a.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd)) {
            this.mExpandedMarginEnd = a.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd, 0);
        }
        if (a.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop)) {
            this.mExpandedMarginTop = a.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop, 0);
        }
        if (a.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom)) {
            this.mExpandedMarginBottom = a.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom, 0);
        }
        this.mCollapsingTitleEnabled = a.getBoolean(R.styleable.CollapsingToolbarLayout_titleEnabled, true);
        setTitle(a.getText(R.styleable.CollapsingToolbarLayout_title));
        this.mCollapsingTextHelper.setExpandedTextAppearance(R.style.TextAppearance_Design_CollapsingToolbar_Expanded);
        this.mCollapsingTextHelper.setCollapsedTextAppearance(android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Widget_ActionBar_Title);
        if (a.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance)) {
            this.mCollapsingTextHelper.setExpandedTextAppearance(a.getResourceId(R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance, 0));
        }
        if (a.hasValue(R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance)) {
            this.mCollapsingTextHelper.setCollapsedTextAppearance(a.getResourceId(R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance, 0));
        }
        this.mScrimVisibleHeightTrigger = a.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_scrimVisibleHeightTrigger, -1);
        this.mScrimAnimationDuration = (long) a.getInt(R.styleable.CollapsingToolbarLayout_scrimAnimationDuration, DEFAULT_SCRIM_ANIMATION_DURATION);
        setContentScrim(a.getDrawable(R.styleable.CollapsingToolbarLayout_contentScrim));
        setStatusBarScrim(a.getDrawable(R.styleable.CollapsingToolbarLayout_statusBarScrim));
        this.mToolbarId = a.getResourceId(R.styleable.CollapsingToolbarLayout_toolbarId, -1);
        a.recycle();
        setWillNotDraw(false);
        ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                return CollapsingToolbarLayout.this.onWindowInsetChanged(insets);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows((View) parent));
            if (this.mOnOffsetChangedListener == null) {
                this.mOnOffsetChangedListener = new OffsetUpdateListener();
            }
            ((AppBarLayout) parent).addOnOffsetChangedListener(this.mOnOffsetChangedListener);
            ViewCompat.requestApplyInsets(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        ViewParent parent = getParent();
        if (this.mOnOffsetChangedListener != null && (parent instanceof AppBarLayout)) {
            ((AppBarLayout) parent).removeOnOffsetChangedListener(this.mOnOffsetChangedListener);
        }
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: package-private */
    public WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat insets) {
        WindowInsetsCompat newInsets = null;
        if (ViewCompat.getFitsSystemWindows(this)) {
            newInsets = insets;
        }
        if (!ObjectsCompat.equals(this.mLastInsets, newInsets)) {
            this.mLastInsets = newInsets;
            requestLayout();
        }
        return insets.consumeSystemWindowInsets();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        ensureToolbar();
        if (this.mToolbar == null && this.mContentScrim != null && this.mScrimAlpha > 0) {
            this.mContentScrim.mutate().setAlpha(this.mScrimAlpha);
            this.mContentScrim.draw(canvas);
        }
        if (this.mCollapsingTitleEnabled && this.mDrawCollapsingTitle) {
            this.mCollapsingTextHelper.draw(canvas);
        }
        if (this.mStatusBarScrim != null && this.mScrimAlpha > 0) {
            int topInset = this.mLastInsets != null ? this.mLastInsets.getSystemWindowInsetTop() : 0;
            if (topInset > 0) {
                this.mStatusBarScrim.setBounds(0, -this.mCurrentOffset, getWidth(), topInset - this.mCurrentOffset);
                this.mStatusBarScrim.mutate().setAlpha(this.mScrimAlpha);
                this.mStatusBarScrim.draw(canvas);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean invalidated = false;
        if (this.mContentScrim != null && this.mScrimAlpha > 0 && isToolbarChild(child)) {
            this.mContentScrim.mutate().setAlpha(this.mScrimAlpha);
            this.mContentScrim.draw(canvas);
            invalidated = true;
        }
        return super.drawChild(canvas, child, drawingTime) || invalidated;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.mContentScrim != null) {
            this.mContentScrim.setBounds(0, 0, w, h);
        }
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [android.view.View] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void ensureToolbar() {
        /*
            r6 = this;
            r5 = 0
            boolean r4 = r6.mRefreshToolbar
            if (r4 != 0) goto L_0x0006
        L_0x0005:
            return
        L_0x0006:
            r6.mToolbar = r5
            r6.mToolbarDirectChild = r5
            int r4 = r6.mToolbarId
            r5 = -1
            if (r4 == r5) goto L_0x0025
            int r4 = r6.mToolbarId
            android.view.View r4 = r6.findViewById(r4)
            android.support.v7.widget.Toolbar r4 = (android.support.v7.widget.Toolbar) r4
            r6.mToolbar = r4
            android.support.v7.widget.Toolbar r4 = r6.mToolbar
            if (r4 == 0) goto L_0x0025
            android.support.v7.widget.Toolbar r4 = r6.mToolbar
            android.view.View r4 = r6.findDirectChild(r4)
            r6.mToolbarDirectChild = r4
        L_0x0025:
            android.support.v7.widget.Toolbar r4 = r6.mToolbar
            if (r4 != 0) goto L_0x003e
            r3 = 0
            r2 = 0
            int r1 = r6.getChildCount()
        L_0x002f:
            if (r2 >= r1) goto L_0x003c
            android.view.View r0 = r6.getChildAt(r2)
            boolean r4 = r0 instanceof android.support.v7.widget.Toolbar
            if (r4 == 0) goto L_0x0045
            r3 = r0
            android.support.v7.widget.Toolbar r3 = (android.support.v7.widget.Toolbar) r3
        L_0x003c:
            r6.mToolbar = r3
        L_0x003e:
            r6.updateDummyView()
            r4 = 0
            r6.mRefreshToolbar = r4
            goto L_0x0005
        L_0x0045:
            int r2 = r2 + 1
            goto L_0x002f
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.design.widget.CollapsingToolbarLayout.ensureToolbar():void");
    }

    private boolean isToolbarChild(View child) {
        return (this.mToolbarDirectChild == null || this.mToolbarDirectChild == this) ? child == this.mToolbar : child == this.mToolbarDirectChild;
    }

    private View findDirectChild(View descendant) {
        View directChild = descendant;
        ViewParent p = descendant.getParent();
        while (p != this && p != null) {
            if (p instanceof View) {
                directChild = (View) p;
            }
            p = p.getParent();
        }
        return directChild;
    }

    private void updateDummyView() {
        if (!this.mCollapsingTitleEnabled && this.mDummyView != null) {
            ViewParent parent = this.mDummyView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.mDummyView);
            }
        }
        if (this.mCollapsingTitleEnabled && this.mToolbar != null) {
            if (this.mDummyView == null) {
                this.mDummyView = new View(getContext());
            }
            if (this.mDummyView.getParent() == null) {
                this.mToolbar.addView(this.mDummyView, -1, -1);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ensureToolbar();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = View.MeasureSpec.getMode(heightMeasureSpec);
        int topInset = this.mLastInsets != null ? this.mLastInsets.getSystemWindowInsetTop() : 0;
        if (mode == 0 && topInset > 0) {
            super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() + topInset, 1073741824));
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int titleMarginStart;
        int titleMarginEnd;
        int i;
        super.onLayout(changed, left, top, right, bottom);
        if (this.mLastInsets != null) {
            int insetTop = this.mLastInsets.getSystemWindowInsetTop();
            int z = getChildCount();
            for (int i2 = 0; i2 < z; i2++) {
                View child = getChildAt(i2);
                if (!ViewCompat.getFitsSystemWindows(child) && child.getTop() < insetTop) {
                    ViewCompat.offsetTopAndBottom(child, insetTop);
                }
            }
        }
        if (this.mCollapsingTitleEnabled && this.mDummyView != null) {
            this.mDrawCollapsingTitle = ViewCompat.isAttachedToWindow(this.mDummyView) && this.mDummyView.getVisibility() == 0;
            if (this.mDrawCollapsingTitle) {
                boolean isRtl = ViewCompat.getLayoutDirection(this) == 1;
                int maxOffset = getMaxOffsetForPinChild(this.mToolbarDirectChild != null ? this.mToolbarDirectChild : this.mToolbar);
                ViewGroupUtils.getDescendantRect(this, this.mDummyView, this.mTmpRect);
                CollapsingTextHelper collapsingTextHelper = this.mCollapsingTextHelper;
                int i3 = this.mTmpRect.left;
                if (isRtl) {
                    titleMarginStart = this.mToolbar.getTitleMarginEnd();
                } else {
                    titleMarginStart = this.mToolbar.getTitleMarginStart();
                }
                int i4 = i3 + titleMarginStart;
                int titleMarginTop = this.mToolbar.getTitleMarginTop() + this.mTmpRect.top + maxOffset;
                int i5 = this.mTmpRect.right;
                if (isRtl) {
                    titleMarginEnd = this.mToolbar.getTitleMarginStart();
                } else {
                    titleMarginEnd = this.mToolbar.getTitleMarginEnd();
                }
                collapsingTextHelper.setCollapsedBounds(i4, titleMarginTop, titleMarginEnd + i5, (this.mTmpRect.bottom + maxOffset) - this.mToolbar.getTitleMarginBottom());
                CollapsingTextHelper collapsingTextHelper2 = this.mCollapsingTextHelper;
                int i6 = isRtl ? this.mExpandedMarginEnd : this.mExpandedMarginStart;
                int i7 = this.mExpandedMarginTop + this.mTmpRect.top;
                int i8 = right - left;
                if (isRtl) {
                    i = this.mExpandedMarginStart;
                } else {
                    i = this.mExpandedMarginEnd;
                }
                collapsingTextHelper2.setExpandedBounds(i6, i7, i8 - i, (bottom - top) - this.mExpandedMarginBottom);
                this.mCollapsingTextHelper.recalculate();
            }
        }
        int z2 = getChildCount();
        for (int i9 = 0; i9 < z2; i9++) {
            getViewOffsetHelper(getChildAt(i9)).onViewLayout();
        }
        if (this.mToolbar != null) {
            if (this.mCollapsingTitleEnabled && TextUtils.isEmpty(this.mCollapsingTextHelper.getText())) {
                this.mCollapsingTextHelper.setText(this.mToolbar.getTitle());
            }
            if (this.mToolbarDirectChild == null || this.mToolbarDirectChild == this) {
                setMinimumHeight(getHeightWithMargins(this.mToolbar));
            } else {
                setMinimumHeight(getHeightWithMargins(this.mToolbarDirectChild));
            }
        }
        updateScrimVisibility();
    }

    private static int getHeightWithMargins(@NonNull View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (!(lp instanceof ViewGroup.MarginLayoutParams)) {
            return view.getHeight();
        }
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lp;
        return view.getHeight() + mlp.topMargin + mlp.bottomMargin;
    }

    static ViewOffsetHelper getViewOffsetHelper(View view) {
        ViewOffsetHelper offsetHelper = (ViewOffsetHelper) view.getTag(R.id.view_offset_helper);
        if (offsetHelper != null) {
            return offsetHelper;
        }
        ViewOffsetHelper offsetHelper2 = new ViewOffsetHelper(view);
        view.setTag(R.id.view_offset_helper, offsetHelper2);
        return offsetHelper2;
    }

    public void setTitle(@Nullable CharSequence title) {
        this.mCollapsingTextHelper.setText(title);
    }

    @Nullable
    public CharSequence getTitle() {
        if (this.mCollapsingTitleEnabled) {
            return this.mCollapsingTextHelper.getText();
        }
        return null;
    }

    public void setTitleEnabled(boolean enabled) {
        if (enabled != this.mCollapsingTitleEnabled) {
            this.mCollapsingTitleEnabled = enabled;
            updateDummyView();
            requestLayout();
        }
    }

    public boolean isTitleEnabled() {
        return this.mCollapsingTitleEnabled;
    }

    public void setScrimsShown(boolean shown) {
        setScrimsShown(shown, ViewCompat.isLaidOut(this) && !isInEditMode());
    }

    public void setScrimsShown(boolean shown, boolean animate) {
        int i = 255;
        if (this.mScrimsAreShown != shown) {
            if (animate) {
                if (!shown) {
                    i = 0;
                }
                animateScrim(i);
            } else {
                if (!shown) {
                    i = 0;
                }
                setScrimAlpha(i);
            }
            this.mScrimsAreShown = shown;
        }
    }

    private void animateScrim(int targetAlpha) {
        ensureToolbar();
        if (this.mScrimAnimator == null) {
            this.mScrimAnimator = new ValueAnimator();
            this.mScrimAnimator.setDuration(this.mScrimAnimationDuration);
            this.mScrimAnimator.setInterpolator(targetAlpha > this.mScrimAlpha ? AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR : AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
            this.mScrimAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    CollapsingToolbarLayout.this.setScrimAlpha(((Integer) animator.getAnimatedValue()).intValue());
                }
            });
        } else if (this.mScrimAnimator.isRunning()) {
            this.mScrimAnimator.cancel();
        }
        this.mScrimAnimator.setIntValues(new int[]{this.mScrimAlpha, targetAlpha});
        this.mScrimAnimator.start();
    }

    /* access modifiers changed from: package-private */
    public void setScrimAlpha(int alpha) {
        if (alpha != this.mScrimAlpha) {
            if (!(this.mContentScrim == null || this.mToolbar == null)) {
                ViewCompat.postInvalidateOnAnimation(this.mToolbar);
            }
            this.mScrimAlpha = alpha;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /* access modifiers changed from: package-private */
    public int getScrimAlpha() {
        return this.mScrimAlpha;
    }

    public void setContentScrim(@Nullable Drawable drawable) {
        Drawable drawable2 = null;
        if (this.mContentScrim != drawable) {
            if (this.mContentScrim != null) {
                this.mContentScrim.setCallback((Drawable.Callback) null);
            }
            if (drawable != null) {
                drawable2 = drawable.mutate();
            }
            this.mContentScrim = drawable2;
            if (this.mContentScrim != null) {
                this.mContentScrim.setBounds(0, 0, getWidth(), getHeight());
                this.mContentScrim.setCallback(this);
                this.mContentScrim.setAlpha(this.mScrimAlpha);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setContentScrimColor(@ColorInt int color) {
        setContentScrim(new ColorDrawable(color));
    }

    public void setContentScrimResource(@DrawableRes int resId) {
        setContentScrim(ContextCompat.getDrawable(getContext(), resId));
    }

    @Nullable
    public Drawable getContentScrim() {
        return this.mContentScrim;
    }

    public void setStatusBarScrim(@Nullable Drawable drawable) {
        Drawable drawable2 = null;
        if (this.mStatusBarScrim != drawable) {
            if (this.mStatusBarScrim != null) {
                this.mStatusBarScrim.setCallback((Drawable.Callback) null);
            }
            if (drawable != null) {
                drawable2 = drawable.mutate();
            }
            this.mStatusBarScrim = drawable2;
            if (this.mStatusBarScrim != null) {
                if (this.mStatusBarScrim.isStateful()) {
                    this.mStatusBarScrim.setState(getDrawableState());
                }
                DrawableCompat.setLayoutDirection(this.mStatusBarScrim, ViewCompat.getLayoutDirection(this));
                this.mStatusBarScrim.setVisible(getVisibility() == 0, false);
                this.mStatusBarScrim.setCallback(this);
                this.mStatusBarScrim.setAlpha(this.mScrimAlpha);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int[] state = getDrawableState();
        boolean changed = false;
        Drawable d = this.mStatusBarScrim;
        if (d != null && d.isStateful()) {
            changed = false | d.setState(state);
        }
        Drawable d2 = this.mContentScrim;
        if (d2 != null && d2.isStateful()) {
            changed |= d2.setState(state);
        }
        if (this.mCollapsingTextHelper != null) {
            changed |= this.mCollapsingTextHelper.setState(state);
        }
        if (changed) {
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mContentScrim || who == this.mStatusBarScrim;
    }

    public void setVisibility(int visibility) {
        boolean visible;
        super.setVisibility(visibility);
        if (visibility == 0) {
            visible = true;
        } else {
            visible = false;
        }
        if (!(this.mStatusBarScrim == null || this.mStatusBarScrim.isVisible() == visible)) {
            this.mStatusBarScrim.setVisible(visible, false);
        }
        if (this.mContentScrim != null && this.mContentScrim.isVisible() != visible) {
            this.mContentScrim.setVisible(visible, false);
        }
    }

    public void setStatusBarScrimColor(@ColorInt int color) {
        setStatusBarScrim(new ColorDrawable(color));
    }

    public void setStatusBarScrimResource(@DrawableRes int resId) {
        setStatusBarScrim(ContextCompat.getDrawable(getContext(), resId));
    }

    @Nullable
    public Drawable getStatusBarScrim() {
        return this.mStatusBarScrim;
    }

    public void setCollapsedTitleTextAppearance(@StyleRes int resId) {
        this.mCollapsingTextHelper.setCollapsedTextAppearance(resId);
    }

    public void setCollapsedTitleTextColor(@ColorInt int color) {
        setCollapsedTitleTextColor(ColorStateList.valueOf(color));
    }

    public void setCollapsedTitleTextColor(@NonNull ColorStateList colors) {
        this.mCollapsingTextHelper.setCollapsedTextColor(colors);
    }

    public void setCollapsedTitleGravity(int gravity) {
        this.mCollapsingTextHelper.setCollapsedTextGravity(gravity);
    }

    public int getCollapsedTitleGravity() {
        return this.mCollapsingTextHelper.getCollapsedTextGravity();
    }

    public void setExpandedTitleTextAppearance(@StyleRes int resId) {
        this.mCollapsingTextHelper.setExpandedTextAppearance(resId);
    }

    public void setExpandedTitleColor(@ColorInt int color) {
        setExpandedTitleTextColor(ColorStateList.valueOf(color));
    }

    public void setExpandedTitleTextColor(@NonNull ColorStateList colors) {
        this.mCollapsingTextHelper.setExpandedTextColor(colors);
    }

    public void setExpandedTitleGravity(int gravity) {
        this.mCollapsingTextHelper.setExpandedTextGravity(gravity);
    }

    public int getExpandedTitleGravity() {
        return this.mCollapsingTextHelper.getExpandedTextGravity();
    }

    public void setCollapsedTitleTypeface(@Nullable Typeface typeface) {
        this.mCollapsingTextHelper.setCollapsedTypeface(typeface);
    }

    @NonNull
    public Typeface getCollapsedTitleTypeface() {
        return this.mCollapsingTextHelper.getCollapsedTypeface();
    }

    public void setExpandedTitleTypeface(@Nullable Typeface typeface) {
        this.mCollapsingTextHelper.setExpandedTypeface(typeface);
    }

    @NonNull
    public Typeface getExpandedTitleTypeface() {
        return this.mCollapsingTextHelper.getExpandedTypeface();
    }

    public void setExpandedTitleMargin(int start, int top, int end, int bottom) {
        this.mExpandedMarginStart = start;
        this.mExpandedMarginTop = top;
        this.mExpandedMarginEnd = end;
        this.mExpandedMarginBottom = bottom;
        requestLayout();
    }

    public int getExpandedTitleMarginStart() {
        return this.mExpandedMarginStart;
    }

    public void setExpandedTitleMarginStart(int margin) {
        this.mExpandedMarginStart = margin;
        requestLayout();
    }

    public int getExpandedTitleMarginTop() {
        return this.mExpandedMarginTop;
    }

    public void setExpandedTitleMarginTop(int margin) {
        this.mExpandedMarginTop = margin;
        requestLayout();
    }

    public int getExpandedTitleMarginEnd() {
        return this.mExpandedMarginEnd;
    }

    public void setExpandedTitleMarginEnd(int margin) {
        this.mExpandedMarginEnd = margin;
        requestLayout();
    }

    public int getExpandedTitleMarginBottom() {
        return this.mExpandedMarginBottom;
    }

    public void setExpandedTitleMarginBottom(int margin) {
        this.mExpandedMarginBottom = margin;
        requestLayout();
    }

    public void setScrimVisibleHeightTrigger(@IntRange(from = 0) int height) {
        if (this.mScrimVisibleHeightTrigger != height) {
            this.mScrimVisibleHeightTrigger = height;
            updateScrimVisibility();
        }
    }

    public int getScrimVisibleHeightTrigger() {
        if (this.mScrimVisibleHeightTrigger >= 0) {
            return this.mScrimVisibleHeightTrigger;
        }
        int insetTop = this.mLastInsets != null ? this.mLastInsets.getSystemWindowInsetTop() : 0;
        int minHeight = ViewCompat.getMinimumHeight(this);
        if (minHeight > 0) {
            return Math.min((minHeight * 2) + insetTop, getHeight());
        }
        return getHeight() / 3;
    }

    public void setScrimAnimationDuration(@IntRange(from = 0) long duration) {
        this.mScrimAnimationDuration = duration;
    }

    public long getScrimAnimationDuration() {
        return this.mScrimAnimationDuration;
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public FrameLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {
        public static final int COLLAPSE_MODE_OFF = 0;
        public static final int COLLAPSE_MODE_PARALLAX = 2;
        public static final int COLLAPSE_MODE_PIN = 1;
        private static final float DEFAULT_PARALLAX_MULTIPLIER = 0.5f;
        int mCollapseMode = 0;
        float mParallaxMult = 0.5f;

        @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
        @Retention(RetentionPolicy.SOURCE)
        @interface CollapseMode {
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CollapsingToolbarLayout_Layout);
            this.mCollapseMode = a.getInt(R.styleable.CollapsingToolbarLayout_Layout_layout_collapseMode, 0);
            setParallaxMultiplier(a.getFloat(R.styleable.CollapsingToolbarLayout_Layout_layout_collapseParallaxMultiplier, 0.5f));
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        @RequiresApi(19)
        public LayoutParams(FrameLayout.LayoutParams source) {
            super(source);
        }

        public void setCollapseMode(int collapseMode) {
            this.mCollapseMode = collapseMode;
        }

        public int getCollapseMode() {
            return this.mCollapseMode;
        }

        public void setParallaxMultiplier(float multiplier) {
            this.mParallaxMult = multiplier;
        }

        public float getParallaxMultiplier() {
            return this.mParallaxMult;
        }
    }

    /* access modifiers changed from: package-private */
    public final void updateScrimVisibility() {
        if (this.mContentScrim != null || this.mStatusBarScrim != null) {
            setScrimsShown(getHeight() + this.mCurrentOffset < getScrimVisibleHeightTrigger());
        }
    }

    /* access modifiers changed from: package-private */
    public final int getMaxOffsetForPinChild(View child) {
        return ((getHeight() - getViewOffsetHelper(child).getLayoutTop()) - child.getHeight()) - ((LayoutParams) child.getLayoutParams()).bottomMargin;
    }

    private class OffsetUpdateListener implements AppBarLayout.OnOffsetChangedListener {
        OffsetUpdateListener() {
        }

        public void onOffsetChanged(AppBarLayout layout, int verticalOffset) {
            int insetTop;
            CollapsingToolbarLayout.this.mCurrentOffset = verticalOffset;
            if (CollapsingToolbarLayout.this.mLastInsets != null) {
                insetTop = CollapsingToolbarLayout.this.mLastInsets.getSystemWindowInsetTop();
            } else {
                insetTop = 0;
            }
            int z = CollapsingToolbarLayout.this.getChildCount();
            for (int i = 0; i < z; i++) {
                View child = CollapsingToolbarLayout.this.getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                ViewOffsetHelper offsetHelper = CollapsingToolbarLayout.getViewOffsetHelper(child);
                switch (lp.mCollapseMode) {
                    case 1:
                        offsetHelper.setTopAndBottomOffset(MathUtils.clamp(-verticalOffset, 0, CollapsingToolbarLayout.this.getMaxOffsetForPinChild(child)));
                        break;
                    case 2:
                        offsetHelper.setTopAndBottomOffset(Math.round(((float) (-verticalOffset)) * lp.mParallaxMult));
                        break;
                }
            }
            CollapsingToolbarLayout.this.updateScrimVisibility();
            if (CollapsingToolbarLayout.this.mStatusBarScrim != null && insetTop > 0) {
                ViewCompat.postInvalidateOnAnimation(CollapsingToolbarLayout.this);
            }
            CollapsingToolbarLayout.this.mCollapsingTextHelper.setExpansionFraction(((float) Math.abs(verticalOffset)) / ((float) ((CollapsingToolbarLayout.this.getHeight() - ViewCompat.getMinimumHeight(CollapsingToolbarLayout.this)) - insetTop)));
        }
    }
}
