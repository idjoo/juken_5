package android.support.transition;

import android.os.Build;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

class ViewGroupUtils {
    private static final ViewGroupUtilsImpl IMPL;

    ViewGroupUtils() {
    }

    static {
        if (Build.VERSION.SDK_INT >= 18) {
            IMPL = new ViewGroupUtilsApi18();
        } else {
            IMPL = new ViewGroupUtilsApi14();
        }
    }

    static ViewGroupOverlayImpl getOverlay(@NonNull ViewGroup group) {
        return IMPL.getOverlay(group);
    }

    static void suppressLayout(@NonNull ViewGroup group, boolean suppress) {
        IMPL.suppressLayout(group, suppress);
    }
}
