package com.github.mikephil.charting.formatter;

import java.text.DecimalFormat;

public class StackedValueFormatter implements ValueFormatter {
    private String mAppendix;
    private boolean mDrawWholeStack;
    private DecimalFormat mFormat;

    public StackedValueFormatter(boolean drawWholeStack, String appendix, int decimals) {
        this.mDrawWholeStack = drawWholeStack;
        this.mAppendix = appendix;
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < decimals; i++) {
            if (i == 0) {
                b.append(".");
            }
            b.append("0");
        }
        this.mFormat = new DecimalFormat("###,###,###,##0" + b.toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0008, code lost:
        r0 = (com.github.mikephil.charting.data.BarEntry) r8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getFormattedValue(float r7, com.github.mikephil.charting.data.Entry r8, int r9, com.github.mikephil.charting.utils.ViewPortHandler r10) {
        /*
            r6 = this;
            boolean r2 = r6.mDrawWholeStack
            if (r2 != 0) goto L_0x003c
            boolean r2 = r8 instanceof com.github.mikephil.charting.data.BarEntry
            if (r2 == 0) goto L_0x003c
            r0 = r8
            com.github.mikephil.charting.data.BarEntry r0 = (com.github.mikephil.charting.data.BarEntry) r0
            float[] r1 = r0.getVals()
            if (r1 == 0) goto L_0x003c
            int r2 = r1.length
            int r2 = r2 + -1
            r2 = r1[r2]
            int r2 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1))
            if (r2 != 0) goto L_0x0039
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.text.DecimalFormat r3 = r6.mFormat
            float r4 = r0.getVal()
            double r4 = (double) r4
            java.lang.String r3 = r3.format(r4)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = r6.mAppendix
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
        L_0x0038:
            return r2
        L_0x0039:
            java.lang.String r2 = ""
            goto L_0x0038
        L_0x003c:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.text.DecimalFormat r3 = r6.mFormat
            double r4 = (double) r7
            java.lang.String r3 = r3.format(r4)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = r6.mAppendix
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            goto L_0x0038
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.mikephil.charting.formatter.StackedValueFormatter.getFormattedValue(float, com.github.mikephil.charting.data.Entry, int, com.github.mikephil.charting.utils.ViewPortHandler):java.lang.String");
    }
}
