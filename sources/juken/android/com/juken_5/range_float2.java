package juken.android.com.juken_5;

import android.text.InputFilter;
import android.text.Spanned;

public class range_float2 implements InputFilter {
    private final int decimalDigits;

    public range_float2(int decimalDigits2) {
        this.decimalDigits = decimalDigits2;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int dotPos = -1;
        int len = dest.length();
        int i = 0;
        while (true) {
            if (i >= len) {
                break;
            }
            char c = dest.charAt(i);
            if (c == '.' || c == ',') {
                dotPos = i;
            } else {
                i++;
            }
        }
        dotPos = i;
        if (dotPos < 0) {
            return null;
        }
        if (source.equals(".") || source.equals(",")) {
            return "";
        }
        if (dend <= dotPos || len - dotPos <= this.decimalDigits) {
            return null;
        }
        return "";
    }
}
