package juken.android.com.juken_5;

import android.text.InputFilter;
import android.text.Spanned;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class range_float3 implements InputFilter {
    private static final int MIN_SIG_FIG = 1;
    private BigDecimal max;
    private BigDecimal min;

    public range_float3(BigDecimal min2, BigDecimal max2) {
        this.min = min2;
        this.max = max2;
    }

    public range_float3(String min2, String max2) {
        this.min = new BigDecimal(min2);
        this.max = new BigDecimal(max2);
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            if (isInRange(this.min, this.max, formatStringToBigDecimal(dest.toString() + source.toString()))) {
                return null;
            }
            return "";
        } catch (NumberFormatException e) {
        }
    }

    private boolean isInRange(BigDecimal a, BigDecimal b, BigDecimal c) {
        if (b.compareTo(a) > 0) {
            return c.compareTo(a) >= 0 && c.compareTo(b) <= 0;
        }
        if (c.compareTo(b) < 0 || c.compareTo(a) > 0) {
            return false;
        }
        return true;
    }

    public static BigDecimal formatStringToBigDecimal(String n) {
        try {
            return new BigDecimal(getDefaultNumberFormat().parse(n.replaceAll("[^\\d]", "")).doubleValue()).divide(new BigDecimal(100), 2, 7);
        } catch (ParseException e) {
            return new BigDecimal(0);
        }
    }

    private static NumberFormat getDefaultNumberFormat() {
        NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
        nf.setMinimumFractionDigits(1);
        return nf;
    }
}
