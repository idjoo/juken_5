package juken.android.com.juken_5;

import android.text.InputFilter;
import android.text.Spanned;

public class range_int_min implements InputFilter {
    private int max;
    private int min;

    public range_int_min(int min2, int max2) {
        this.min = min2;
        this.max = max2;
    }

    public range_int_min(String min2, String max2) {
        this.min = Integer.parseInt(min2);
        this.max = Integer.parseInt(max2);
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int value;
        try {
            String stringInput = dest.toString() + source.toString();
            if (stringInput.length() == 1 && stringInput.charAt(0) == '-') {
                value = -1;
            } else {
                value = Integer.parseInt(stringInput);
            }
            if (isInRange(this.min, this.max, value)) {
                return null;
            }
            return "";
        } catch (NumberFormatException e) {
        }
    }

    private boolean isInRange(int min2, int max2, int value) {
        return max2 > min2 ? value >= min2 && value <= max2 : value >= max2 && value <= min2;
    }
}
