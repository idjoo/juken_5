package com.github.mikephil.charting.utils;

import android.os.Environment;
import android.util.Log;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String LOG = "MPChart-FileUtils";

    public static List<Entry> loadEntriesFromFile(String path) {
        File file = new File(Environment.getExternalStorageDirectory(), path);
        List<Entry> entries = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                String[] split = line.split("#");
                if (split.length <= 2) {
                    entries.add(new Entry(Float.parseFloat(split[0]), Integer.parseInt(split[1])));
                } else {
                    float[] vals = new float[(split.length - 1)];
                    for (int i = 0; i < vals.length; i++) {
                        vals[i] = Float.parseFloat(split[i]);
                    }
                    entries.add(new BarEntry(vals, Integer.parseInt(split[split.length - 1])));
                }
            }
        } catch (IOException e) {
            Log.e(LOG, e.toString());
        }
        return entries;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0074 A[SYNTHETIC, Splitter:B:21:0x0074] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0099 A[SYNTHETIC, Splitter:B:34:0x0099] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<com.github.mikephil.charting.data.Entry> loadEntriesFromAssets(android.content.res.AssetManager r11, java.lang.String r12) {
        /*
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r4 = 0
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ IOException -> 0x00ab }
            java.io.InputStreamReader r8 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x00ab }
            java.io.InputStream r9 = r11.open(r12)     // Catch:{ IOException -> 0x00ab }
            java.lang.String r10 = "UTF-8"
            r8.<init>(r9, r10)     // Catch:{ IOException -> 0x00ab }
            r5.<init>(r8)     // Catch:{ IOException -> 0x00ab }
            java.lang.String r3 = r5.readLine()     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
        L_0x001a:
            if (r3 == 0) goto L_0x0078
            java.lang.String r8 = "#"
            java.lang.String[] r6 = r3.split(r8)     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            int r8 = r6.length     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            r9 = 2
            if (r8 > r9) goto L_0x0041
            com.github.mikephil.charting.data.Entry r8 = new com.github.mikephil.charting.data.Entry     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            r9 = 0
            r9 = r6[r9]     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            float r9 = java.lang.Float.parseFloat(r9)     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            r10 = 1
            r10 = r6[r10]     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            int r10 = java.lang.Integer.parseInt(r10)     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            r8.<init>(r9, r10)     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            r1.add(r8)     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
        L_0x003c:
            java.lang.String r3 = r5.readLine()     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            goto L_0x001a
        L_0x0041:
            int r8 = r6.length     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            int r8 = r8 + -1
            float[] r7 = new float[r8]     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            r2 = 0
        L_0x0047:
            int r8 = r7.length     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            if (r2 >= r8) goto L_0x0055
            r8 = r6[r2]     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            float r8 = java.lang.Float.parseFloat(r8)     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            r7[r2] = r8     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            int r2 = r2 + 1
            goto L_0x0047
        L_0x0055:
            com.github.mikephil.charting.data.BarEntry r8 = new com.github.mikephil.charting.data.BarEntry     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            int r9 = r6.length     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            int r9 = r9 + -1
            r9 = r6[r9]     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            int r9 = java.lang.Integer.parseInt(r9)     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            r8.<init>((float[]) r7, (int) r9)     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            r1.add(r8)     // Catch:{ IOException -> 0x0067, all -> 0x00a8 }
            goto L_0x003c
        L_0x0067:
            r0 = move-exception
            r4 = r5
        L_0x0069:
            java.lang.String r8 = "MPChart-FileUtils"
            java.lang.String r9 = r0.toString()     // Catch:{ all -> 0x0096 }
            android.util.Log.e(r8, r9)     // Catch:{ all -> 0x0096 }
            if (r4 == 0) goto L_0x0077
            r4.close()     // Catch:{ IOException -> 0x008b }
        L_0x0077:
            return r1
        L_0x0078:
            if (r5 == 0) goto L_0x00ad
            r5.close()     // Catch:{ IOException -> 0x007f }
            r4 = r5
            goto L_0x0077
        L_0x007f:
            r0 = move-exception
            java.lang.String r8 = "MPChart-FileUtils"
            java.lang.String r9 = r0.toString()
            android.util.Log.e(r8, r9)
            r4 = r5
            goto L_0x0077
        L_0x008b:
            r0 = move-exception
            java.lang.String r8 = "MPChart-FileUtils"
            java.lang.String r9 = r0.toString()
            android.util.Log.e(r8, r9)
            goto L_0x0077
        L_0x0096:
            r8 = move-exception
        L_0x0097:
            if (r4 == 0) goto L_0x009c
            r4.close()     // Catch:{ IOException -> 0x009d }
        L_0x009c:
            throw r8
        L_0x009d:
            r0 = move-exception
            java.lang.String r9 = "MPChart-FileUtils"
            java.lang.String r10 = r0.toString()
            android.util.Log.e(r9, r10)
            goto L_0x009c
        L_0x00a8:
            r8 = move-exception
            r4 = r5
            goto L_0x0097
        L_0x00ab:
            r0 = move-exception
            goto L_0x0069
        L_0x00ad:
            r4 = r5
            goto L_0x0077
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.mikephil.charting.utils.FileUtils.loadEntriesFromAssets(android.content.res.AssetManager, java.lang.String):java.util.List");
    }

    public static void saveToSdCard(List<Entry> entries, String path) {
        File saved = new File(Environment.getExternalStorageDirectory(), path);
        if (!saved.exists()) {
            try {
                saved.createNewFile();
            } catch (IOException e) {
                Log.e(LOG, e.toString());
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(saved, true));
            for (Entry e2 : entries) {
                buf.append(e2.getVal() + "#" + e2.getXIndex());
                buf.newLine();
            }
            buf.close();
        } catch (IOException e3) {
            Log.e(LOG, e3.toString());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x005c A[SYNTHETIC, Splitter:B:19:0x005c] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x006e A[SYNTHETIC, Splitter:B:25:0x006e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<com.github.mikephil.charting.data.BarEntry> loadBarEntriesFromAssets(android.content.res.AssetManager r9, java.lang.String r10) {
        /*
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r3 = 0
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0050 }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0050 }
            java.io.InputStream r7 = r9.open(r10)     // Catch:{ IOException -> 0x0050 }
            java.lang.String r8 = "UTF-8"
            r6.<init>(r7, r8)     // Catch:{ IOException -> 0x0050 }
            r4.<init>(r6)     // Catch:{ IOException -> 0x0050 }
            java.lang.String r2 = r4.readLine()     // Catch:{ IOException -> 0x0080, all -> 0x007d }
        L_0x001a:
            if (r2 == 0) goto L_0x003d
            java.lang.String r6 = "#"
            java.lang.String[] r5 = r2.split(r6)     // Catch:{ IOException -> 0x0080, all -> 0x007d }
            com.github.mikephil.charting.data.BarEntry r6 = new com.github.mikephil.charting.data.BarEntry     // Catch:{ IOException -> 0x0080, all -> 0x007d }
            r7 = 0
            r7 = r5[r7]     // Catch:{ IOException -> 0x0080, all -> 0x007d }
            float r7 = java.lang.Float.parseFloat(r7)     // Catch:{ IOException -> 0x0080, all -> 0x007d }
            r8 = 1
            r8 = r5[r8]     // Catch:{ IOException -> 0x0080, all -> 0x007d }
            int r8 = java.lang.Integer.parseInt(r8)     // Catch:{ IOException -> 0x0080, all -> 0x007d }
            r6.<init>((float) r7, (int) r8)     // Catch:{ IOException -> 0x0080, all -> 0x007d }
            r1.add(r6)     // Catch:{ IOException -> 0x0080, all -> 0x007d }
            java.lang.String r2 = r4.readLine()     // Catch:{ IOException -> 0x0080, all -> 0x007d }
            goto L_0x001a
        L_0x003d:
            if (r4 == 0) goto L_0x0083
            r4.close()     // Catch:{ IOException -> 0x0044 }
            r3 = r4
        L_0x0043:
            return r1
        L_0x0044:
            r0 = move-exception
            java.lang.String r6 = "MPChart-FileUtils"
            java.lang.String r7 = r0.toString()
            android.util.Log.e(r6, r7)
            r3 = r4
            goto L_0x0043
        L_0x0050:
            r0 = move-exception
        L_0x0051:
            java.lang.String r6 = "MPChart-FileUtils"
            java.lang.String r7 = r0.toString()     // Catch:{ all -> 0x006b }
            android.util.Log.e(r6, r7)     // Catch:{ all -> 0x006b }
            if (r3 == 0) goto L_0x0043
            r3.close()     // Catch:{ IOException -> 0x0060 }
            goto L_0x0043
        L_0x0060:
            r0 = move-exception
            java.lang.String r6 = "MPChart-FileUtils"
            java.lang.String r7 = r0.toString()
            android.util.Log.e(r6, r7)
            goto L_0x0043
        L_0x006b:
            r6 = move-exception
        L_0x006c:
            if (r3 == 0) goto L_0x0071
            r3.close()     // Catch:{ IOException -> 0x0072 }
        L_0x0071:
            throw r6
        L_0x0072:
            r0 = move-exception
            java.lang.String r7 = "MPChart-FileUtils"
            java.lang.String r8 = r0.toString()
            android.util.Log.e(r7, r8)
            goto L_0x0071
        L_0x007d:
            r6 = move-exception
            r3 = r4
            goto L_0x006c
        L_0x0080:
            r0 = move-exception
            r3 = r4
            goto L_0x0051
        L_0x0083:
            r3 = r4
            goto L_0x0043
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.mikephil.charting.utils.FileUtils.loadBarEntriesFromAssets(android.content.res.AssetManager, java.lang.String):java.util.List");
    }
}
