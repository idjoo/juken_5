package juken.android.com.juken_5;

import android.text.InputFilter;
import android.text.Spanned;

public class range_int1 implements InputFilter {
    private int max;
    private int min;

    public range_int1(int min2, int max2) {
        this.min = min2;
        this.max = max2;
    }

    public range_int1(String min2, String max2) {
        this.min = Integer.parseInt(min2);
        this.max = Integer.parseInt(max2);
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String startString = dest.toString().substring(0, dstart);
            String insert = source.toString();
            if (isInRange(this.min, this.max, Integer.parseInt(startString + insert + dest.toString().substring(dend)))) {
                return null;
            }
            return "";
        } catch (NumberFormatException e) {
        }
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
