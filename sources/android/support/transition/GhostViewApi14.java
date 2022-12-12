package android.support.transition;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.GhostViewImpl;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

@SuppressLint({"ViewConstructor"})
@RequiresApi(14)
class GhostViewApi14 extends View implements GhostViewImpl {
    Matrix mCurrentMatrix;
    private int mDeltaX;
    private int mDeltaY;
    private final Matrix mMatrix = new Matrix();
    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        public boolean onPreDraw() {
            GhostViewApi14.this.mCurrentMatrix = GhostViewApi14.this.mView.getMatrix();
            ViewCompat.postInvalidateOnAnimation(GhostViewApi14.this);
            if (GhostViewApi14.this.mStartParent == null || GhostViewApi14.this.mStartView == null) {
                return true;
            }
            GhostViewApi14.this.mStartParent.endViewTransition(GhostViewApi14.this.mStartView);
            ViewCompat.postInvalidateOnAnimation(GhostViewApi14.this.mStartParent);
            GhostViewApi14.this.mStartParent = null;
            GhostViewApi14.this.mStartView = null;
            return true;
        }
    };
    int mReferences;
    ViewGroup mStartParent;
    View mStartView;
    final View mView;

    static class Creator implements GhostViewImpl.Creator {
        Creator() {
        }

        public GhostViewImpl addGhost(View view, ViewGroup viewGroup, Matrix matrix) {
            GhostViewApi14 ghostView = GhostViewApi14.getGhostView(view);
            if (ghostView == null) {
                FrameLayout frameLayout = findFrameLayout(viewGroup);
                if (frameLayout == null) {
                    return null;
                }
                ghostView = new GhostViewApi14(view);
                frameLayout.addView(ghostView);
            }
            ghostView.mReferences++;
            return ghostView;
        }

        public void removeGhost(View view) {
            GhostViewApi14 ghostView = GhostViewApi14.getGhostView(view);
            if (ghostView != null) {
                ghostView.mReferences--;
                if (ghostView.mReferences <= 0) {
                    ViewParent parent = ghostView.getParent();
                    if (parent instanceof ViewGroup) {
                        ViewGroup group = (ViewGroup) parent;
                        group.endViewTransition(ghostView);
                        group.removeView(ghostView);
                    }
                }
            }
        }

        /* JADX WARNING: type inference failed for: r0v0, types: [android.view.ViewParent] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static android.widget.FrameLayout findFrameLayout(android.view.ViewGroup r2) {
            /*
            L_0x0000:
                boolean r1 = r2 instanceof android.widget.FrameLayout
                if (r1 != 0) goto L_0x0012
                android.view.ViewParent r0 = r2.getParent()
                boolean r1 = r0 instanceof android.view.ViewGroup
                if (r1 != 0) goto L_0x000e
                r2 = 0
            L_0x000d:
                return r2
            L_0x000e:
                r2 = r0
                android.view.ViewGroup r2 = (android.view.ViewGroup) r2
                goto L_0x0000
            L_0x0012:
                android.widget.FrameLayout r2 = (android.widget.FrameLayout) r2
                goto L_0x000d
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.transition.GhostViewApi14.Creator.findFrameLayout(android.view.ViewGroup):android.widget.FrameLayout");
        }
    }

    GhostViewApi14(View view) {
        super(view.getContext());
        this.mView = view;
        setLayerType(2, (Paint) null);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setGhostView(this.mView, this);
        int[] location = new int[2];
        int[] viewLocation = new int[2];
        getLocationOnScreen(location);
        this.mView.getLocationOnScreen(viewLocation);
        viewLocation[0] = (int) (((float) viewLocation[0]) - this.mView.getTranslationX());
        viewLocation[1] = (int) (((float) viewLocation[1]) - this.mView.getTranslationY());
        this.mDeltaX = viewLocation[0] - location[0];
        this.mDeltaY = viewLocation[1] - location[1];
        this.mView.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
        this.mView.setVisibility(4);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mView.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
        this.mView.setVisibility(0);
        setGhostView(this.mView, (GhostViewApi14) null);
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        this.mMatrix.set(this.mCurrentMatrix);
        this.mMatrix.postTranslate((float) this.mDeltaX, (float) this.mDeltaY);
        canvas.setMatrix(this.mMatrix);
        this.mView.draw(canvas);
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        this.mView.setVisibility(visibility == 0 ? 4 : 0);
    }

    public void reserveEndViewTransition(ViewGroup viewGroup, View view) {
        this.mStartParent = viewGroup;
        this.mStartView = view;
    }

    private static void setGhostView(@NonNull View view, GhostViewApi14 ghostView) {
        view.setTag(R.id.ghost_view, ghostView);
    }

    static GhostViewApi14 getGhostView(@NonNull View view) {
        return (GhostViewApi14) view.getTag(R.id.ghost_view);
    }
}
