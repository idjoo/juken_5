package juken.android.com.juken_5;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

public class SamplesUtils {
    public static void indeterminate(Context context, Handler handler, String message, Runnable runnable, DialogInterface.OnDismissListener dismissListener) {
        try {
            indeterminateInternal(context, handler, message, runnable, dismissListener, true);
        } catch (Exception e) {
        }
    }

    public static void indeterminate(Context context, Handler handler, String message, Runnable runnable, DialogInterface.OnDismissListener dismissListener, boolean cancelable) {
        try {
            indeterminateInternal(context, handler, message, runnable, dismissListener, cancelable);
        } catch (Exception e) {
        }
    }

    private static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setIndeterminate(false);
        dialog.setMessage(message);
        return dialog;
    }

    private static void indeterminateInternal(Context context, final Handler handler, String message, final Runnable runnable, DialogInterface.OnDismissListener dismissListener, boolean cancelable) {
        final ProgressDialog dialog = createProgressDialog(context, message);
        dialog.setCancelable(cancelable);
        if (dismissListener != null) {
            dialog.setOnDismissListener(dismissListener);
        }
        dialog.show();
        new Thread() {
            public void run() {
                runnable.run();
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        }.start();
    }
}
