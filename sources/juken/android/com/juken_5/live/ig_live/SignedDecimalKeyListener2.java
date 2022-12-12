package juken.android.com.juken_5.live.ig_live;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.NumberKeyListener;

class SignedDecimalKeyListener2 extends NumberKeyListener {
    private static final char[] CHARACTERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};
    private static SignedDecimalKeyListener2 sInstance;
    private char[] mAccepted = CHARACTERS;

    /* access modifiers changed from: protected */
    public char[] getAcceptedChars() {
        return this.mAccepted;
    }

    private SignedDecimalKeyListener2() {
    }

    public static SignedDecimalKeyListener2 getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        sInstance = new SignedDecimalKeyListener2();
        return sInstance;
    }

    public int getInputType() {
        return 8194;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        CharSequence out = super.filter(source, start, end, dest, dstart, dend);
        if (out != null) {
            source = out;
            start = 0;
            end = out.length();
        }
        if ((dstart > 0 && source.equals("-")) || (dstart == 0 && source.equals("."))) {
            SpannableStringBuilder stripped = new SpannableStringBuilder(source, start, end);
            stripped.delete(start, end);
            if (stripped != null) {
                return stripped;
            }
        } else if (source.equals(".")) {
            for (int lo = dend - 1; lo > 0; lo--) {
                if (source.equals(String.valueOf(dest.charAt(lo)))) {
                    SpannableStringBuilder stripped2 = new SpannableStringBuilder(source, start, end);
                    stripped2.delete(start, end);
                    if (stripped2 != null) {
                        return stripped2;
                    }
                }
            }
        }
        if (out != null) {
            return out;
        }
        return null;
    }
}
