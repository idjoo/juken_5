package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.R;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.internal.BottomNavigationPresenter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class BottomNavigationView extends FrameLayout {
    private static final int[] CHECKED_STATE_SET = {16842912};
    private static final int[] DISABLED_STATE_SET = {-16842910};
    private static final int MENU_PRESENTER_ID = 1;
    private final MenuBuilder mMenu;
    private MenuInflater mMenuInflater;
    private final BottomNavigationMenuView mMenuView;
    private final BottomNavigationPresenter mPresenter;
    /* access modifiers changed from: private */
    public OnNavigationItemReselectedListener mReselectedListener;
    /* access modifiers changed from: private */
    public OnNavigationItemSelectedListener mSelectedListener;

    public interface OnNavigationItemReselectedListener {
        void onNavigationItemReselected(@NonNull MenuItem menuItem);
    }

    public interface OnNavigationItemSelectedListener {
        boolean onNavigationItemSelected(@NonNull MenuItem menuItem);
    }

    public BottomNavigationView(Context context) {
        this(context, (AttributeSet) null);
    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPresenter = new BottomNavigationPresenter();
        ThemeUtils.checkAppCompatTheme(context);
        this.mMenu = new BottomNavigationMenu(context);
        this.mMenuView = new BottomNavigationMenuView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
        params.gravity = 17;
        this.mMenuView.setLayoutParams(params);
        this.mPresenter.setBottomNavigationMenuView(this.mMenuView);
        this.mPresenter.setId(1);
        this.mMenuView.setPresenter(this.mPresenter);
        this.mMenu.addMenuPresenter(this.mPresenter);
        this.mPresenter.initForMenu(getContext(), this.mMenu);
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.BottomNavigationView, defStyleAttr, R.style.Widget_Design_BottomNavigationView);
        if (a.hasValue(R.styleable.BottomNavigationView_itemIconTint)) {
            this.mMenuView.setIconTintList(a.getColorStateList(R.styleable.BottomNavigationView_itemIconTint));
        } else {
            this.mMenuView.setIconTintList(createDefaultColorStateList(16842808));
        }
        if (a.hasValue(R.styleable.BottomNavigationView_itemTextColor)) {
            this.mMenuView.setItemTextColor(a.getColorStateList(R.styleable.BottomNavigationView_itemTextColor));
        } else {
            this.mMenuView.setItemTextColor(createDefaultColorStateList(16842808));
        }
        if (a.hasValue(R.styleable.BottomNavigationView_elevation)) {
            ViewCompat.setElevation(this, (float) a.getDimensionPixelSize(R.styleable.BottomNavigationView_elevation, 0));
        }
        this.mMenuView.setItemBackgroundRes(a.getResourceId(R.styleable.BottomNavigationView_itemBackground, 0));
        if (a.hasValue(R.styleable.BottomNavigationView_menu)) {
            inflateMenu(a.getResourceId(R.styleable.BottomNavigationView_menu, 0));
        }
        a.recycle();
        addView(this.mMenuView, params);
        if (Build.VERSION.SDK_INT < 21) {
            addCompatibilityTopDivider(context);
        }
        this.mMenu.setCallback(new MenuBuilder.Callback() {
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                if (BottomNavigationView.this.mReselectedListener != null && item.getItemId() == BottomNavigationView.this.getSelectedItemId()) {
                    BottomNavigationView.this.mReselectedListener.onNavigationItemReselected(item);
                    return true;
                } else if (BottomNavigationView.this.mSelectedListener == null || BottomNavigationView.this.mSelectedListener.onNavigationItemSelected(item)) {
                    return false;
                } else {
                    return true;
                }
            }

            public void onMenuModeChange(MenuBuilder menu) {
            }
        });
    }

    public void setOnNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener listener) {
        this.mSelectedListener = listener;
    }

    public void setOnNavigationItemReselectedListener(@Nullable OnNavigationItemReselectedListener listener) {
        this.mReselectedListener = listener;
    }

    @NonNull
    public Menu getMenu() {
        return this.mMenu;
    }

    public void inflateMenu(int resId) {
        this.mPresenter.setUpdateSuspended(true);
        getMenuInflater().inflate(resId, this.mMenu);
        this.mPresenter.setUpdateSuspended(false);
        this.mPresenter.updateMenuView(true);
    }

    public int getMaxItemCount() {
        return 5;
    }

    @Nullable
    public ColorStateList getItemIconTintList() {
        return this.mMenuView.getIconTintList();
    }

    public void setItemIconTintList(@Nullable ColorStateList tint) {
        this.mMenuView.setIconTintList(tint);
    }

    @Nullable
    public ColorStateList getItemTextColor() {
        return this.mMenuView.getItemTextColor();
    }

    public void setItemTextColor(@Nullable ColorStateList textColor) {
        this.mMenuView.setItemTextColor(textColor);
    }

    @DrawableRes
    public int getItemBackgroundResource() {
        return this.mMenuView.getItemBackgroundRes();
    }

    public void setItemBackgroundResource(@DrawableRes int resId) {
        this.mMenuView.setItemBackgroundRes(resId);
    }

    @IdRes
    public int getSelectedItemId() {
        return this.mMenuView.getSelectedItemId();
    }

    public void setSelectedItemId(@IdRes int itemId) {
        MenuItem item = this.mMenu.findItem(itemId);
        if (item != null && !this.mMenu.performItemAction(item, this.mPresenter, 0)) {
            item.setChecked(true);
        }
    }

    private void addCompatibilityTopDivider(Context context) {
        View divider = new View(context);
        divider.setBackgroundColor(ContextCompat.getColor(context, R.color.design_bottom_navigation_shadow_color));
        divider.setLayoutParams(new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(R.dimen.design_bottom_navigation_shadow_height)));
        addView(divider);
    }

    private MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.mMenuInflater = new SupportMenuInflater(getContext());
        }
        return this.mMenuInflater;
    }

    private ColorStateList createDefaultColorStateList(int baseColorThemeAttr) {
        TypedValue value = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(baseColorThemeAttr, value, true)) {
            return null;
        }
        ColorStateList baseColor = AppCompatResources.getColorStateList(getContext(), value.resourceId);
        if (!getContext().getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.colorPrimary, value, true)) {
            return null;
        }
        int colorPrimary = value.data;
        int defaultColor = baseColor.getDefaultColor();
        return new ColorStateList(new int[][]{DISABLED_STATE_SET, CHECKED_STATE_SET, EMPTY_STATE_SET}, new int[]{baseColor.getColorForState(DISABLED_STATE_SET, defaultColor), colorPrimary, defaultColor});
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuPresenterState = new Bundle();
        this.mMenu.savePresenterStates(savedState.menuPresenterState);
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mMenu.restorePresenterStates(savedState.menuPresenterState);
    }

    static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, (ClassLoader) null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        Bundle menuPresenterState;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            readFromParcel(source, loader);
        }

        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBundle(this.menuPresenterState);
        }

        private void readFromParcel(Parcel in, ClassLoader loader) {
            this.menuPresenterState = in.readBundle(loader);
        }
    }
}
