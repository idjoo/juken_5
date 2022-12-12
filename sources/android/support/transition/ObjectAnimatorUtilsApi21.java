package android.support.transition;

import android.animation.ObjectAnimator;
import android.animation.TypeConverter;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.RequiresApi;
import android.util.Property;

@RequiresApi(21)
class ObjectAnimatorUtilsApi21 implements ObjectAnimatorUtilsImpl {
    ObjectAnimatorUtilsApi21() {
    }

    public <T> ObjectAnimator ofPointF(T target, Property<T, PointF> property, Path path) {
        return ObjectAnimator.ofObject(target, property, (TypeConverter) null, path);
    }
}
