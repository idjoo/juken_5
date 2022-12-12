package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Matrix;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

@RequiresApi(14)
class ImageViewUtilsApi14 implements ImageViewUtilsImpl {
    ImageViewUtilsApi14() {
    }

    public void startAnimateTransform(ImageView view) {
        ImageView.ScaleType scaleType = view.getScaleType();
        view.setTag(R.id.save_scale_type, scaleType);
        if (scaleType == ImageView.ScaleType.MATRIX) {
            view.setTag(R.id.save_image_matrix, view.getImageMatrix());
        } else {
            view.setScaleType(ImageView.ScaleType.MATRIX);
        }
        view.setImageMatrix(MatrixUtils.IDENTITY_MATRIX);
    }

    public void animateTransform(ImageView view, Matrix matrix) {
        view.setImageMatrix(matrix);
    }

    public void reserveEndAnimateTransform(final ImageView view, Animator animator) {
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                ImageView.ScaleType scaleType = (ImageView.ScaleType) view.getTag(R.id.save_scale_type);
                view.setScaleType(scaleType);
                view.setTag(R.id.save_scale_type, (Object) null);
                if (scaleType == ImageView.ScaleType.MATRIX) {
                    view.setImageMatrix((Matrix) view.getTag(R.id.save_image_matrix));
                    view.setTag(R.id.save_image_matrix, (Object) null);
                }
                animation.removeListener(this);
            }
        });
    }
}
