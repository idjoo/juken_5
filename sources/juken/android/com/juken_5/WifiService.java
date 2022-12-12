package juken.android.com.juken_5;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class WifiService extends Service {
    public static final String BROADCAST_ACTION = "com.server.tcp.tcp_server";
    public static final String VVTC = "VVTC";
    public static final String ack_awal = "ack_awal";
    public static final String auto_ig = "auto_ig";
    public static final String auto_ig_temp = "auto_ig_temp";
    public static final String b_c1 = "b_c1";
    public static final String b_c2 = "b_c2";
    public static final String base_mem_max = "base_mem_max";
    public static final String dec_fuel = "dec_fuel";
    public static final String dec_time = "dec_time";
    public static final String dwell_time = "dwell_time";
    public static final String ecu_cyl = "ecu_cyl";
    public static final String ecu_digunakan = "ecu_digunakan";
    public static final String ecu_model = "ecu_model";
    public static final String f_c1 = "f_c1";
    public static final String f_c2 = "f_c2";
    public static final String fuel_mem_max = "fuel_mem_max";
    public static final String fuel_start = "fuel_start";
    public static final String idle_fuel = "idle_fuel";
    public static final String ig_c1 = "ig_c1";
    public static final String ig_c2 = "ig_c2";
    public static final String ig_mem_max = "ig_mem_max";
    public static final String imob_en = "imob_en";
    public static final String it_c1 = "it_c1";
    public static final String it_c2 = "it_c2";
    public static final String it_mem_max = "it_mem_max";
    public static final String jf1 = "jf1";
    public static final String jf2 = "jf2";
    public static final String jf3 = "jf3";
    public static final String jf4 = "jf4";
    public static final String jjsore_rpm = "jjsore_rpm";
    public static final String jjsore_tps = "jjsore_tps";
    public static final String key1_h = "key1_h";
    public static final String key1_l = "key1_l";
    public static final String key2_h = "key2_h";
    public static final String key2_l = "key2_l";
    public static final String key3_h = "key3_h";
    public static final String key3_l = "key3_l";
    public static final String key4_h = "key4_h";
    public static final String key4_l = "key4_l";
    public static final String key5_h = "key5_h";
    public static final String key5_l = "key5_l";
    public static final String ki = "ki";
    public static final String limiter = "limiter";
    public static final String nilai_idle = "nilai_idle";
    public static final String position_rpm_idle = "position_rpm_idle";
    public static final String protect_enable = "protect_enable";
    public static final String protect_limit = "protect_limit";
    public static final String protect_temp = "protect_temp";
    public static final String range_hi = "range_hi";
    public static final String range_lo = "range_lo";
    public static final String range_mid = "range_mid";
    public static final String secure = "secure";
    public static final String shift = "shift";
    public static final String suhu_fan = "suhu_fan";
    public static final String t_rate1 = "t_rate1";
    public static final String t_rate2 = "t_rate2";
    public static final String t_rate3 = "t_rate3";
    public static final String ti = "ti";
    public static final String tps_high = "tps_high";
    public static final String tps_low = "tps_low";
    public static final String tss = "tss";
    public static final String turbo_en = "turbo_en";
    public static final String turbo_rpm = "turbo_rpm";
    public static final String turbo_tps = "turbo_tps";
    public static final String warming_up = "warming_up";
    String SERVER_IP = TcpClient.SERVER_IP;
    int SERVER_PORT = 80;
    Thread Thread1 = null;
    String b_c11 = "";
    String b_c21 = "";
    int baris_pattern = 0;
    Boolean boleh_terima = false;
    int cek = 0;
    StringBuilder dataLog = new StringBuilder();
    String f_c11 = "";
    String f_c21 = "";
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    /* access modifiers changed from: private */
    public Handler handler_ = new Handler();
    String ig_c11 = "";
    String ig_c21 = "";
    /* access modifiers changed from: private */
    public BufferedReader input;
    String it_c11 = "";
    String it_c21 = "";
    int jumlahPing = 0;
    Boolean kirim_ecu_model = false;
    int kolom_pattern = 0;
    TcpClient mTcpClient;
    String model = "";
    int nilai_tss = 1;
    /* access modifiers changed from: private */
    public PrintWriter output;
    int posisi_base_map = 0;
    int posisi_fuel = 0;
    int posisi_history = 0;
    int posisi_ignition = 0;
    int posisi_injector = 0;
    Boolean raise = false;
    private final Runnable sendHistory = new Runnable() {
        public void run() {
            try {
                if (WifiService.this.raise.booleanValue()) {
                    WifiService.this.raise = false;
                    for (int i = 0; i < 1281; i++) {
                        MappingHandle.list_history.add("0.0");
                    }
                    WifiService.this.sendMyBroadCast("data9");
                    StaticClass.bolehPing = true;
                } else {
                    WifiService.this.raise = true;
                }
                WifiService.this.handler.postDelayed(this, 2000);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("HISTORY", e.toString());
            }
        }
    };
    private final Runnable sendPing = new Runnable() {
        public void run() {
            try {
                if (StaticClass.bolehPing) {
                    new Thread(new Thread3("1B00\r\n")).start();
                }
                WifiService.this.handler_.postDelayed(this, 5000);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Ping Trace", e.toString());
            }
        }
    };
    Socket socket;
    int tps_terima = 0;
    String tss1 = "";

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        Log.d("Wifi Service", "On Create");
        Connect();
        this.handler_.post(this.sendPing);
    }

    public void onDestroy() {
        Log.d("Wifi Service", "On Destroy");
        this.handler_.removeCallbacks(this.sendPing);
    }

    /* access modifiers changed from: private */
    public void sendMyBroadCast(String data) {
        try {
            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction(BROADCAST_ACTION);
            broadCastIntent.putExtra("key", data);
            sendBroadcast(broadCastIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0061, code lost:
        kirim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0067, code lost:
        Reconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006b, code lost:
        new java.lang.Thread(new juken.android.com.juken_5.WifiService.Thread3(r5, juken.android.com.juken_5.StaticClass.GlobalData)).start();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x007b, code lost:
        r5.baris_pattern = 1;
        r5.kolom_pattern = 0;
        juken.android.com.juken_5.MappingHandle.list_pattern.clear();
        kirimAutoMapping();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0090, code lost:
        if (r5.socket.isConnected() == false) goto L_0x00ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0092, code lost:
        juken.android.com.juken_5.StaticClass.koneksi = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0099, code lost:
        sendMyBroadCast("dataKoneksi");
        android.util.Log.d("Connection", java.lang.String.valueOf(juken.android.com.juken_5.StaticClass.koneksi));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00ab, code lost:
        juken.android.com.juken_5.StaticClass.koneksi = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00b3, code lost:
        stopClient();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00b8, code lost:
        juken.android.com.juken_5.StaticClass.bolehPing = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0014, code lost:
        switch(r1) {
            case 0: goto L_0x0061;
            case 1: goto L_0x0067;
            case 2: goto L_0x006b;
            case 3: goto L_0x007b;
            case 4: goto L_0x008a;
            case 5: goto L_0x00b3;
            case 6: goto L_0x00b8;
            default: goto L_0x0017;
        };
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int onStartCommand(android.content.Intent r6, int r7, int r8) {
        /*
            r5 = this;
            r3 = 1
            r1 = 0
            java.lang.String r2 = "send"
            java.lang.String r0 = r6.getStringExtra(r2)     // Catch:{ Exception -> 0x0065 }
            r2 = 0
            juken.android.com.juken_5.StaticClass.bolehPing = r2     // Catch:{ Exception -> 0x0065 }
            r2 = -1
            int r4 = r0.hashCode()     // Catch:{ Exception -> 0x0065 }
            switch(r4) {
                case -1854355584: goto L_0x0039;
                case -1300841673: goto L_0x0025;
                case -1253292934: goto L_0x001c;
                case -907095033: goto L_0x0043;
                case 2487698: goto L_0x0057;
                case 1092798570: goto L_0x004d;
                case 1134213645: goto L_0x002f;
                default: goto L_0x0013;
            }
        L_0x0013:
            r1 = r2
        L_0x0014:
            switch(r1) {
                case 0: goto L_0x0061;
                case 1: goto L_0x0067;
                case 2: goto L_0x006b;
                case 3: goto L_0x007b;
                case 4: goto L_0x008a;
                case 5: goto L_0x00b3;
                case 6: goto L_0x00b8;
                default: goto L_0x0017;
            }
        L_0x0017:
            int r1 = super.onStartCommand(r6, r7, r8)
            return r1
        L_0x001c:
            java.lang.String r3 = "Var Awal"
            boolean r3 = r0.equals(r3)     // Catch:{ Exception -> 0x0065 }
            if (r3 == 0) goto L_0x0013
            goto L_0x0014
        L_0x0025:
            java.lang.String r1 = "Reconnect"
            boolean r1 = r0.equals(r1)     // Catch:{ Exception -> 0x0065 }
            if (r1 == 0) goto L_0x0013
            r1 = r3
            goto L_0x0014
        L_0x002f:
            java.lang.String r1 = "GlobalData"
            boolean r1 = r0.equals(r1)     // Catch:{ Exception -> 0x0065 }
            if (r1 == 0) goto L_0x0013
            r1 = 2
            goto L_0x0014
        L_0x0039:
            java.lang.String r1 = "AutoMappingAwal"
            boolean r1 = r0.equals(r1)     // Catch:{ Exception -> 0x0065 }
            if (r1 == 0) goto L_0x0013
            r1 = 3
            goto L_0x0014
        L_0x0043:
            java.lang.String r1 = "cekConnection"
            boolean r1 = r0.equals(r1)     // Catch:{ Exception -> 0x0065 }
            if (r1 == 0) goto L_0x0013
            r1 = 4
            goto L_0x0014
        L_0x004d:
            java.lang.String r1 = "closeCon"
            boolean r1 = r0.equals(r1)     // Catch:{ Exception -> 0x0065 }
            if (r1 == 0) goto L_0x0013
            r1 = 5
            goto L_0x0014
        L_0x0057:
            java.lang.String r1 = "Ping"
            boolean r1 = r0.equals(r1)     // Catch:{ Exception -> 0x0065 }
            if (r1 == 0) goto L_0x0013
            r1 = 6
            goto L_0x0014
        L_0x0061:
            r5.kirim()     // Catch:{ Exception -> 0x0065 }
            goto L_0x0017
        L_0x0065:
            r1 = move-exception
            goto L_0x0017
        L_0x0067:
            r5.Reconnect()     // Catch:{ Exception -> 0x0065 }
            goto L_0x0017
        L_0x006b:
            java.lang.Thread r1 = new java.lang.Thread     // Catch:{ Exception -> 0x0065 }
            juken.android.com.juken_5.WifiService$Thread3 r2 = new juken.android.com.juken_5.WifiService$Thread3     // Catch:{ Exception -> 0x0065 }
            java.lang.String r3 = juken.android.com.juken_5.StaticClass.GlobalData     // Catch:{ Exception -> 0x0065 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x0065 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x0065 }
            r1.start()     // Catch:{ Exception -> 0x0065 }
            goto L_0x0017
        L_0x007b:
            r1 = 1
            r5.baris_pattern = r1     // Catch:{ Exception -> 0x0065 }
            r1 = 0
            r5.kolom_pattern = r1     // Catch:{ Exception -> 0x0065 }
            java.util.ArrayList<java.lang.String> r1 = juken.android.com.juken_5.MappingHandle.list_pattern     // Catch:{ Exception -> 0x0065 }
            r1.clear()     // Catch:{ Exception -> 0x0065 }
            r5.kirimAutoMapping()     // Catch:{ Exception -> 0x0065 }
            goto L_0x0017
        L_0x008a:
            java.net.Socket r1 = r5.socket     // Catch:{ Exception -> 0x0065 }
            boolean r1 = r1.isConnected()     // Catch:{ Exception -> 0x0065 }
            if (r1 == 0) goto L_0x00ab
            r1 = 1
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x0065 }
            juken.android.com.juken_5.StaticClass.koneksi = r1     // Catch:{ Exception -> 0x0065 }
        L_0x0099:
            java.lang.String r1 = "dataKoneksi"
            r5.sendMyBroadCast(r1)     // Catch:{ Exception -> 0x0065 }
            java.lang.String r1 = "Connection"
            java.lang.Boolean r2 = juken.android.com.juken_5.StaticClass.koneksi     // Catch:{ Exception -> 0x0065 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Exception -> 0x0065 }
            android.util.Log.d(r1, r2)     // Catch:{ Exception -> 0x0065 }
            goto L_0x0017
        L_0x00ab:
            r1 = 0
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x0065 }
            juken.android.com.juken_5.StaticClass.koneksi = r1     // Catch:{ Exception -> 0x0065 }
            goto L_0x0099
        L_0x00b3:
            r5.stopClient()     // Catch:{ Exception -> 0x0065 }
            goto L_0x0017
        L_0x00b8:
            r1 = 1
            juken.android.com.juken_5.StaticClass.bolehPing = r1     // Catch:{ Exception -> 0x0065 }
            goto L_0x0017
        */
        throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.WifiService.onStartCommand(android.content.Intent, int, int):int");
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }

    /* access modifiers changed from: private */
    public void olahData(String values) {
        String mentah = values;
        this.jumlahPing = 0;
        if (mentah.contains("1b01") || mentah.contains("1B01")) {
            mentah = mentah.replace("1b01\r\n", "").replace("1B01\r\n", "");
        }
        if (StaticClass.Live.booleanValue()) {
            Log.d("Data Live", mentah);
            StaticClass.bolehPing = true;
            StaticClass.DataLive = mentah;
            sendMyBroadCast("LiveData");
        } else if (this.boleh_terima.booleanValue()) {
            this.boleh_terima = null;
            String[] data = mentah.replace("\r", "").replace("\n", "").split(";");
            int posisi = 0;
            for (int i = 0; i < data.length; i++) {
                if (i != 0) {
                    switch (posisi) {
                        case 1:
                            this.kirim_ecu_model = 1;
                            String VVTC1 = data[i].trim();
                            if (isNumeric(VVTC1)) {
                                SavePreferences("VVTC", VVTC1);
                            } else {
                                SavePreferences("VVTC", "1000");
                                StaticClass.checkVar = false;
                            }
                            posisi = 2;
                            break;
                        case 2:
                            String tps_low1 = data[i].trim();
                            if (isNumeric(tps_low1)) {
                                SavePreferences("tps_low", tps_low1);
                            } else {
                                SavePreferences("tps_low", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 3;
                            break;
                        case 3:
                            String tps_high1 = data[i].trim();
                            if (isNumeric(tps_high1)) {
                                SavePreferences("tps_high", tps_high1);
                            } else {
                                SavePreferences("tps_high", "100");
                                StaticClass.checkVar = false;
                            }
                            posisi = 4;
                            break;
                        case 4:
                            String jjsore_tps1 = data[i].trim();
                            if (isNumeric(jjsore_tps1)) {
                                SavePreferences("jjsore_tps", jjsore_tps1);
                            } else {
                                SavePreferences("jjsore_tps", "257");
                                StaticClass.checkVar = false;
                            }
                            posisi = 5;
                            break;
                        case 5:
                            String fuel_start1 = data[i].trim();
                            if (isNumeric(fuel_start1)) {
                                SavePreferences("fuel_start", fuel_start1);
                            } else {
                                SavePreferences("fuel_start", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 6;
                            break;
                        case 6:
                            String position_rpm_idle1 = data[i].trim();
                            if (isNumeric(position_rpm_idle1)) {
                                SavePreferences("position_rpm_idle", position_rpm_idle1);
                            } else {
                                SavePreferences("position_rpm_idle", "1200");
                                StaticClass.checkVar = false;
                            }
                            posisi = 7;
                            break;
                        case 7:
                            String warming_up1 = data[i].trim();
                            if (isNumeric(warming_up1)) {
                                SavePreferences("warming_up", warming_up1);
                            } else {
                                SavePreferences("warming_up", "1000");
                                StaticClass.checkVar = false;
                            }
                            posisi = 8;
                            break;
                        case 8:
                            String jjsore_rpm1 = data[i].trim();
                            if (isNumeric(jjsore_rpm1)) {
                                SavePreferences("jjsore_rpm", jjsore_rpm1);
                            } else {
                                SavePreferences("jjsore_rpm", "60");
                                StaticClass.checkVar = false;
                            }
                            posisi = 9;
                            break;
                        case 9:
                            String range_lo1 = data[i].trim();
                            if (isNumeric(range_lo1)) {
                                SavePreferences("range_lo", range_lo1);
                            } else {
                                SavePreferences("range_lo", "14");
                                StaticClass.checkVar = false;
                            }
                            posisi = 10;
                            break;
                        case 10:
                            String range_mid1 = data[i].trim();
                            if (isNumeric(range_mid1)) {
                                SavePreferences("range_mid", range_mid1);
                            } else {
                                SavePreferences("range_mid", "16");
                                StaticClass.checkVar = false;
                            }
                            posisi = 11;
                            break;
                        case 11:
                            String range_hi1 = data[i].trim();
                            if (isNumeric(range_hi1)) {
                                SavePreferences("range_hi", range_hi1);
                            } else {
                                SavePreferences("range_hi", "31");
                                StaticClass.checkVar = false;
                            }
                            posisi = 12;
                            break;
                        case 12:
                            String ti1 = data[i].trim();
                            if (isNumeric(ti1)) {
                                SavePreferences("ti", ti1);
                            } else {
                                SavePreferences("ti", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 13;
                            break;
                        case 13:
                            String ki1 = data[i].trim();
                            if (isNumeric(ki1)) {
                                SavePreferences("ki", ki1);
                            } else {
                                SavePreferences("ki", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 14;
                            break;
                        case 14:
                            String shift1 = data[i].trim();
                            if (isNumeric(shift1)) {
                                SavePreferences("shift", shift1);
                            } else {
                                SavePreferences("shift", "80");
                                StaticClass.checkVar = false;
                            }
                            posisi = 15;
                            break;
                        case 15:
                            String dec_fuel1 = data[i].trim();
                            if (isNumeric(dec_fuel1)) {
                                SavePreferences("dec_fuel", dec_fuel1);
                            } else {
                                SavePreferences("dec_fuel", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 16;
                            break;
                        case 16:
                            String limiter1 = data[i].trim();
                            if (isNumeric(limiter1)) {
                                SavePreferences("limiter", limiter1);
                            } else {
                                SavePreferences("limiter", "16000");
                                StaticClass.checkVar = false;
                            }
                            posisi = 17;
                            break;
                        case 17:
                            String dwell_time1 = data[i].trim();
                            if (isNumeric(dwell_time1)) {
                                SavePreferences("dwell_time", dwell_time1);
                            } else {
                                SavePreferences("dwell_time", "10");
                                StaticClass.checkVar = false;
                            }
                            posisi = 18;
                            break;
                        case 18:
                            String auto_ig1 = data[i].trim();
                            if (isNumeric(auto_ig1)) {
                                SavePreferences("auto_ig", auto_ig1);
                            } else {
                                SavePreferences("auto_ig", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 19;
                            break;
                        case 19:
                            String auto_ig_temp1 = data[i].trim();
                            if (isNumeric(auto_ig_temp1)) {
                                SavePreferences("auto_ig_temp", auto_ig_temp1);
                            } else {
                                SavePreferences("auto_ig_temp", "85");
                                StaticClass.checkVar = false;
                            }
                            posisi = 20;
                            break;
                        case 20:
                            String fuel_mem_max1 = data[i].trim();
                            if (isNumeric(fuel_mem_max1)) {
                                SavePreferences("fuel_mem_max", fuel_mem_max1);
                            } else {
                                SavePreferences("fuel_mem_max", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 21;
                            break;
                        case 21:
                            String ig_mem_max1 = data[i].trim();
                            if (isNumeric(ig_mem_max1)) {
                                SavePreferences("ig_mem_max", ig_mem_max1);
                            } else {
                                SavePreferences("ig_mem_max", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 22;
                            break;
                        case 22:
                            String it_mem_max1 = data[i].trim();
                            if (isNumeric(it_mem_max1)) {
                                SavePreferences("it_mem_max", it_mem_max1);
                            } else {
                                SavePreferences("it_mem_max", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 23;
                            break;
                        case 23:
                            String base_mem_max1 = data[i].trim();
                            if (isNumeric(base_mem_max1)) {
                                SavePreferences("base_mem_max", base_mem_max1);
                            } else {
                                SavePreferences("base_mem_max", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 24;
                            break;
                        case 24:
                            if (data[i].trim().equals("0")) {
                                this.tss1 = "1";
                            } else {
                                this.tss1 = "2";
                            }
                            SavePreferences("tss", this.tss1);
                            Log.d("TSS", this.tss1);
                            posisi = 25;
                            break;
                        case 25:
                            this.b_c11 = data[i].trim();
                            if (isNumeric(this.b_c11)) {
                                SavePreferences("b_c1", this.b_c11);
                            } else {
                                this.b_c11 = "0";
                                SavePreferences("b_c1", this.b_c11);
                                StaticClass.checkVar = false;
                            }
                            posisi = 26;
                            break;
                        case 26:
                            this.f_c11 = data[i].trim();
                            if (isNumeric(this.f_c11)) {
                                SavePreferences("f_c1", this.f_c11);
                            } else {
                                this.f_c11 = "0";
                                SavePreferences("f_c1", this.f_c11);
                                StaticClass.checkVar = false;
                            }
                            posisi = 27;
                            break;
                        case 27:
                            this.it_c11 = data[i].trim();
                            if (isNumeric(this.it_c11)) {
                                SavePreferences("it_c1", this.it_c11);
                            } else {
                                this.it_c11 = "0";
                                SavePreferences("it_c1", this.it_c11);
                                StaticClass.checkVar = false;
                            }
                            posisi = 28;
                            break;
                        case 28:
                            this.ig_c11 = data[i].trim();
                            if (isNumeric(this.ig_c11)) {
                                SavePreferences("ig_c1", this.ig_c11);
                            } else {
                                this.ig_c11 = "0";
                                SavePreferences("ig_c1", this.ig_c11);
                                StaticClass.checkVar = false;
                            }
                            posisi = 29;
                            break;
                        case 29:
                            this.b_c21 = data[i].trim();
                            if (isNumeric(this.b_c21)) {
                                SavePreferences("b_c2", this.b_c21);
                            } else {
                                this.b_c21 = "0";
                                SavePreferences("b_c2", this.b_c21);
                                StaticClass.checkVar = false;
                            }
                            posisi = 30;
                            break;
                        case 30:
                            this.f_c21 = data[i].trim();
                            if (isNumeric(this.f_c21)) {
                                SavePreferences("f_c2", this.f_c21);
                            } else {
                                this.f_c21 = "0";
                                SavePreferences("f_c2", this.f_c21);
                                StaticClass.checkVar = false;
                            }
                            posisi = 31;
                            break;
                        case 31:
                            this.it_c21 = data[i].trim();
                            if (isNumeric(this.it_c21)) {
                                SavePreferences("it_c2", this.it_c21);
                            } else {
                                this.it_c21 = "0";
                                SavePreferences("it_c2", this.it_c21);
                                StaticClass.checkVar = false;
                            }
                            posisi = 32;
                            break;
                        case 32:
                            this.ig_c21 = data[i].trim();
                            if (isNumeric(this.ig_c21)) {
                                SavePreferences("ig_c2", this.ig_c21);
                            } else {
                                this.ig_c21 = "0";
                                SavePreferences("ig_c2", this.ig_c21);
                                StaticClass.checkVar = false;
                            }
                            posisi = 33;
                            break;
                        case 33:
                            String turbo_en1 = data[i].trim();
                            if (isNumeric(turbo_en1)) {
                                SavePreferences("turbo_en", turbo_en1);
                            } else {
                                SavePreferences("turbo_en", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 34;
                            break;
                        case 34:
                            String dec_time1 = data[i].trim();
                            if (isNumeric(dec_time1)) {
                                SavePreferences("dec_time", dec_time1);
                            } else {
                                SavePreferences("dec_time", "10");
                                StaticClass.checkVar = false;
                            }
                            posisi = 35;
                            break;
                        case 35:
                            String nilai_idle1 = data[i].trim();
                            if (isNumeric(nilai_idle1)) {
                                SavePreferences("nilai_idle", nilai_idle1);
                            } else {
                                SavePreferences("nilai_idle", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 36;
                            break;
                        case 36:
                            String idle_fuel1 = data[i].trim();
                            if (isNumeric(idle_fuel1)) {
                                SavePreferences("idle_fuel", idle_fuel1);
                            } else {
                                SavePreferences("idle_fuel", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 37;
                            break;
                        case 37:
                            String turbo_tps1 = data[i].trim();
                            if (isNumeric(turbo_tps1)) {
                                SavePreferences("turbo_tps", turbo_tps1);
                            } else {
                                SavePreferences("turbo_tps", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 38;
                            break;
                        case 38:
                            String turbo_rpm1 = data[i].trim();
                            if (isNumeric(turbo_rpm1)) {
                                SavePreferences("turbo_rpm", turbo_rpm1);
                            } else {
                                SavePreferences("turbo_rpm", "1000");
                                StaticClass.checkVar = false;
                            }
                            posisi = 39;
                            break;
                        case 39:
                            String imob_en1 = data[i].trim();
                            if (isNumeric(imob_en1)) {
                                SavePreferences("imob_en", imob_en1);
                            } else {
                                SavePreferences("imob_en", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 40;
                            break;
                        case 40:
                            SavePreferences("key1_h", data[i]);
                            posisi = 41;
                            break;
                        case 41:
                            SavePreferences("key1_l", data[i]);
                            posisi = 42;
                            break;
                        case 42:
                            SavePreferences("key2_h", data[i]);
                            posisi = 43;
                            break;
                        case 43:
                            SavePreferences("key2_l", data[i]);
                            posisi = 44;
                            break;
                        case 44:
                            SavePreferences("key3_h", data[i]);
                            posisi = 45;
                            break;
                        case 45:
                            SavePreferences("key3_l", data[i]);
                            posisi = 46;
                            break;
                        case 46:
                            SavePreferences("ecu_cyl", data[i]);
                            posisi = 47;
                            break;
                        case 47:
                            SavePreferences("secure", data[i]);
                            posisi = 48;
                            break;
                        case 48:
                            String jf11 = data[i].trim();
                            if (isNumeric(jf11)) {
                                SavePreferences("jf1", jf11);
                            } else {
                                SavePreferences("jf1", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 49;
                            break;
                        case 49:
                            String jf21 = data[i].trim();
                            if (isNumeric(jf21)) {
                                SavePreferences("jf2", jf21);
                            } else {
                                SavePreferences("jf2", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 50;
                            break;
                        case 50:
                            String jf31 = data[i].trim();
                            if (isNumeric(jf31)) {
                                SavePreferences("jf3", jf31);
                            } else {
                                SavePreferences("jf3", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 51;
                            break;
                        case 51:
                            String jf41 = data[i].trim();
                            if (isNumeric(jf41)) {
                                SavePreferences("jf4", jf41);
                            } else {
                                SavePreferences("jf4", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 52;
                            break;
                        case 52:
                            String t_rate11 = data[i].trim();
                            if (isNumeric(t_rate11)) {
                                SavePreferences("t_rate1", t_rate11);
                            } else {
                                SavePreferences("t_rate1", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 53;
                            break;
                        case 53:
                            String t_rate21 = data[i].trim();
                            if (isNumeric(t_rate21)) {
                                SavePreferences("t_rate2", t_rate21);
                            } else {
                                SavePreferences("t_rate2", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 54;
                            break;
                        case 54:
                            String t_rate31 = data[i].trim();
                            if (isNumeric(t_rate31)) {
                                SavePreferences("t_rate3", t_rate31);
                            } else {
                                SavePreferences("t_rate3", "0");
                                StaticClass.checkVar = false;
                            }
                            posisi = 55;
                            break;
                        case 55:
                            if (!data[i].equals("9616")) {
                                SavePreferences("ecu_model", data[i]);
                                posisi = 56;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 56:
                            if (!data[i].equals("9616")) {
                                SavePreferences("key4_h", data[i]);
                                posisi = 57;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 57:
                            if (!data[i].equals("9616")) {
                                SavePreferences("key4_l", data[i]);
                                posisi = 58;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 58:
                            if (!data[i].equals("9616")) {
                                SavePreferences("key5_h", data[i]);
                                posisi = 59;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 59:
                            if (!data[i].equals("9616")) {
                                SavePreferences("key5_l", data[i]);
                                posisi = 60;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 60:
                            if (!data[i].equals("9616")) {
                                posisi = 61;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 61:
                            if (!data[i].equals("9616")) {
                                posisi = 62;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 62:
                            if (!data[i].equals("9616")) {
                                posisi = 63;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 63:
                            if (!data[i].equals("9616")) {
                                posisi = 64;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 64:
                            if (!data[i].equals("9616")) {
                                posisi = 65;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 65:
                            if (!data[i].equals("9616")) {
                                posisi = 66;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 66:
                            if (!data[i].equals("9616")) {
                                posisi = 67;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 67:
                            if (!data[i].equals("9616")) {
                                posisi = 68;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 68:
                            if (!data[i].equals("9616")) {
                                String suhu_fan1 = data[i].trim();
                                if (isNumeric(suhu_fan1)) {
                                    SavePreferences("suhu_fan", suhu_fan1);
                                } else {
                                    SavePreferences("suhu_fan", "600");
                                    StaticClass.checkVar = false;
                                }
                                posisi = 69;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 69:
                            if (!data[i].equals("9616")) {
                                posisi = 70;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 70:
                            if (!data[i].equals("9616")) {
                                posisi = 71;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 71:
                            if (!data[i].equals("9616")) {
                                String protect_enable1 = data[i].trim();
                                if (isNumeric(protect_enable1)) {
                                    SavePreferences("protect_enable", protect_enable1);
                                } else {
                                    SavePreferences("protect_enable", "0");
                                    StaticClass.checkVar = false;
                                }
                                posisi = 72;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 72:
                            if (!data[i].equals("9616")) {
                                String protect_temp1 = data[i].trim();
                                if (isNumeric(protect_temp1)) {
                                    SavePreferences("protect_temp", protect_temp1);
                                } else {
                                    SavePreferences("protect_temp", "60.0");
                                    StaticClass.checkVar = false;
                                }
                                posisi = 73;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case 73:
                            if (!data[i].equals("9616")) {
                                String protect_limit1 = data[i].trim();
                                if (isNumeric(protect_limit1)) {
                                    SavePreferences("protect_limit", protect_limit1);
                                } else {
                                    SavePreferences("protect_limit", "5000");
                                    StaticClass.checkVar = false;
                                }
                                posisi = 0;
                                break;
                            } else {
                                this.model = "";
                                posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                break;
                            }
                        case ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION:
                            this.model = data[i] + "\r\n";
                            SavePreferences("NamaEcu", this.model);
                            posisi = 201;
                            break;
                        case 201:
                            this.model += data[i] + "\r\n";
                            posisi = 202;
                            break;
                        case 202:
                            this.model += data[i].replaceAll("\\r", "");
                            String ecu_digunakan1 = this.model;
                            SavePreferences("ecu_digunakan", ecu_digunakan1);
                            StaticClass.EcuDigunakan = ecu_digunakan1;
                            sendMyBroadCast("data1");
                            if (this.tss1.equals("1")) {
                                this.nilai_tss = 1;
                            } else if (this.tss1.equals("2")) {
                                this.nilai_tss = 2;
                            }
                            this.tps_terima = 0;
                            posisi = 0;
                            this.cek = 0;
                            MappingHandle.list_fuel.clear();
                            if (this.nilai_tss != 1) {
                                if (this.nilai_tss != 2) {
                                    break;
                                } else {
                                    terima_fuel(this.f_c21);
                                    break;
                                }
                            } else {
                                terima_fuel(this.f_c11);
                                break;
                            }
                        case ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION:
                            MappingHandle.list_fuel.add(data[i].replace("\r", "").replace("\n", ""));
                            this.posisi_fuel++;
                            if (this.posisi_fuel <= 60) {
                                break;
                            } else {
                                this.tps_terima++;
                                this.posisi_fuel = 0;
                                if (this.tps_terima < 21) {
                                    if (this.nilai_tss != 1) {
                                        if (this.nilai_tss != 2) {
                                            break;
                                        } else {
                                            terima_fuel(this.f_c21);
                                            break;
                                        }
                                    } else {
                                        terima_fuel(this.f_c11);
                                        break;
                                    }
                                } else {
                                    this.tps_terima = 0;
                                    this.cek = 2;
                                    MappingHandle.list_base_map.clear();
                                    if (this.nilai_tss != 1) {
                                        if (this.nilai_tss != 2) {
                                            break;
                                        } else {
                                            terima_base_map(this.b_c21);
                                            break;
                                        }
                                    } else {
                                        terima_base_map(this.b_c11);
                                        break;
                                    }
                                }
                            }
                        case 251:
                            MappingHandle.list_base_map.add(data[i].replace("\r", "").replace("\n", "").trim());
                            this.posisi_base_map++;
                            if (this.posisi_base_map <= 60) {
                                break;
                            } else {
                                this.tps_terima++;
                                this.posisi_base_map = 0;
                                if (this.tps_terima < 21) {
                                    if (this.nilai_tss != 1) {
                                        if (this.nilai_tss != 2) {
                                            break;
                                        } else {
                                            terima_base_map(this.b_c21);
                                            break;
                                        }
                                    } else {
                                        terima_base_map(this.b_c11);
                                        break;
                                    }
                                } else {
                                    try {
                                        MappingHandle.list_pattern_current_file.clear();
                                        ArrayList<String> bmTotSementara = new ArrayList<>();
                                        for (int k = 0; k < MappingHandle.list_fuel.size(); k++) {
                                            Double Fuel = Double.valueOf(MappingHandle.list_fuel.get(k));
                                            Double Bm = Double.valueOf(MappingHandle.list_base_map.get(k));
                                            Double BmTot = Double.valueOf(Bm.doubleValue() + ((Bm.doubleValue() * Fuel.doubleValue()) / 100.0d));
                                            if (BmTot.doubleValue() > 20.0d) {
                                                BmTot = Double.valueOf(20.0d);
                                            }
                                            if (BmTot.doubleValue() < 0.0d) {
                                                BmTot = Double.valueOf(0.0d);
                                            }
                                            bmTotSementara.add(String.format("%.2f", new Object[]{BmTot}).replace(",", "."));
                                        }
                                        for (int k2 = 19; k2 > 0; k2--) {
                                            for (int j = 0; j < 61; j++) {
                                                int posisi1 = j + 1220;
                                                double seratus = Double.valueOf(bmTotSementara.get(posisi1)).doubleValue();
                                                MappingHandle.list_pattern_current_file.add(String.format("%.2f", new Object[]{Double.valueOf(((Double.valueOf(bmTotSementara.get(posisi1 - (k2 * 61))).doubleValue() - seratus) * 100.0d) / seratus)}).replace(",", "."));
                                            }
                                        }
                                    } catch (Exception e) {
                                        Log.d("Current Pattern", e.toString());
                                    }
                                    this.tps_terima = 0;
                                    this.cek = 3;
                                    MappingHandle.list_injector.clear();
                                    if (this.nilai_tss != 1) {
                                        if (this.nilai_tss != 2) {
                                            break;
                                        } else {
                                            terima_injector(this.it_c21);
                                            break;
                                        }
                                    } else {
                                        terima_injector(this.it_c11);
                                        break;
                                    }
                                }
                            }
                        case 252:
                            MappingHandle.list_injector.add(data[i].replace("\r", "").replace("\n", ""));
                            this.posisi_injector++;
                            if (this.posisi_injector <= 60) {
                                break;
                            } else {
                                this.tps_terima++;
                                this.posisi_injector = 0;
                                if (this.tps_terima < 21) {
                                    if (this.nilai_tss != 1) {
                                        if (this.nilai_tss != 2) {
                                            break;
                                        } else {
                                            terima_injector(this.it_c21);
                                            break;
                                        }
                                    } else {
                                        terima_injector(this.it_c11);
                                        break;
                                    }
                                } else {
                                    this.tps_terima = 0;
                                    this.cek = 4;
                                    MappingHandle.list_ignition.clear();
                                    if (this.nilai_tss != 1) {
                                        if (this.nilai_tss != 2) {
                                            break;
                                        } else {
                                            terima_ignition(this.ig_c21);
                                            break;
                                        }
                                    } else {
                                        terima_ignition(this.ig_c11);
                                        break;
                                    }
                                }
                            }
                        case 253:
                            MappingHandle.list_ignition.add(data[i].replace("\r", "").replace("\n", "").trim());
                            this.posisi_ignition++;
                            if (this.posisi_ignition <= 30) {
                                break;
                            } else {
                                this.tps_terima++;
                                this.posisi_ignition = 0;
                                if (this.tps_terima < 21) {
                                    if (this.nilai_tss != 1) {
                                        if (this.nilai_tss != 2) {
                                            break;
                                        } else {
                                            terima_ignition(this.ig_c21);
                                            break;
                                        }
                                    } else {
                                        terima_ignition(this.ig_c11);
                                        break;
                                    }
                                } else {
                                    this.tps_terima = 0;
                                    this.cek = 5;
                                    if (this.nilai_tss == 1) {
                                        MappingHandle.NamaFileFuel = "Fuel Correction - ECU Core 1";
                                        MappingHandle.NamaFileBM = "Base Map - ECU Core 1";
                                        MappingHandle.NamaFileIG = "Ignition Timing - ECU Core 1";
                                        MappingHandle.NamaFileIT = "Injector Timing - ECU Core 1";
                                    } else {
                                        MappingHandle.NamaFileFuel = "Fuel Correction - ECU Core 2";
                                        MappingHandle.NamaFileBM = "Base Map - ECU Core 2";
                                        MappingHandle.NamaFileIG = "Ignition Timing - ECU Core 2";
                                        MappingHandle.NamaFileIT = "Injector Timing - ECU Core 2";
                                    }
                                    MappingHandle.list_history.clear();
                                    terima_history();
                                    this.handler.post(this.sendHistory);
                                    break;
                                }
                            }
                        case 254:
                            MappingHandle.list_history.add(String.valueOf(Double.valueOf(data[i].replace("\r", "").replace("\n", "")).doubleValue() / 10.0d));
                            this.posisi_history++;
                            if (this.posisi_history <= 60) {
                                break;
                            } else {
                                this.tps_terima++;
                                posisi = 0;
                                if (this.tps_terima >= 21) {
                                    posisi = 0;
                                    sendMyBroadCast("data2");
                                    StaticClass.bolehPing = true;
                                    break;
                                } else {
                                    this.handler.removeCallbacks(this.sendHistory);
                                    terima_history();
                                    break;
                                }
                            }
                        case 260:
                            StaticClass.hitung_tps = Double.valueOf(Double.parseDouble(data[i]));
                            posisi = 261;
                            break;
                        case 261:
                            StaticClass.tps_low1 = Double.valueOf(Double.parseDouble(data[i]));
                            posisi = 262;
                            break;
                        case 262:
                            StaticClass.tps_high1 = Double.valueOf(Double.parseDouble(data[i]));
                            sendMyBroadCast("calibrationProcess");
                            posisi = 0;
                            break;
                        case 265:
                            StaticClass.waktu = Integer.valueOf(Integer.parseInt(data[i]));
                            posisi = 266;
                            break;
                        case 266:
                            StaticClass.batere = data[i];
                            posisi = 267;
                            break;
                        case 267:
                            posisi = 268;
                            break;
                        case 268:
                            StaticClass.counter = data[i];
                            posisi = 269;
                            break;
                        case 269:
                            posisi = 270;
                            break;
                        case 270:
                            sendMyBroadCast("maintenanceProcess");
                            posisi = 0;
                            break;
                        case 275:
                            StaticClass.str2 = data[i];
                            posisi = 276;
                            break;
                        case 276:
                            StaticClass.str1 = data[i];
                            posisi = 277;
                            break;
                        case 277:
                            posisi = 278;
                            break;
                        case 278:
                            posisi = 279;
                            break;
                        case 279:
                            StaticClass.tpsValue = data[i];
                            posisi = 280;
                            break;
                        case 280:
                            StaticClass.vBat = data[i];
                            sendMyBroadCast("diagnosticProcess");
                            posisi = 0;
                            break;
                        case 285:
                            if (Double.valueOf(data[i]).doubleValue() > 0.0d) {
                            }
                            MappingHandle.list_pattern.add(data[i]);
                            this.kolom_pattern++;
                            if (this.kolom_pattern != 61) {
                                break;
                            } else {
                                this.kolom_pattern = 0;
                                posisi = 0;
                                if (this.baris_pattern != 19) {
                                    this.baris_pattern++;
                                    kirimAutoMapping();
                                    break;
                                } else {
                                    this.baris_pattern = 1;
                                    sendMyBroadCast("AutoMappingDone");
                                    break;
                                }
                            }
                        case 290:
                            StaticClass.dataGetMap = data[i];
                            sendMyBroadCast("processFuel");
                            break;
                        case 291:
                            StaticClass.dataGetMap = data[i].trim();
                            sendMyBroadCast("processBM");
                            break;
                        case 292:
                            StaticClass.dataGetMap = data[i];
                            sendMyBroadCast("processIT");
                            break;
                        case 293:
                            StaticClass.dataGetMap = data[i].trim();
                            sendMyBroadCast("processIG");
                            break;
                        case GaugeView.SIZE:
                            if (!data[i].contains("\n")) {
                                this.dataLog.append(data[i]);
                                break;
                            } else {
                                this.dataLog.append(data[i]);
                                StaticClass.DataLogger = this.dataLog.toString();
                                posisi = 0;
                                sendMyBroadCast("DataLoggerFinish");
                                break;
                            }
                    }
                } else {
                    String str = data[i];
                    char c = 65535;
                    switch (str.hashCode()) {
                        case 1523760:
                            if (str.equals("1A00")) {
                                c = 16;
                                break;
                            }
                            break;
                        case 1751518:
                            if (str.equals("9601")) {
                                c = 3;
                                break;
                            }
                            break;
                        case 1751519:
                            if (str.equals("9602")) {
                                c = 2;
                                break;
                            }
                            break;
                        case 1751520:
                            if (str.equals("9603")) {
                                c = 4;
                                break;
                            }
                            break;
                        case 1751522:
                            if (str.equals("9605")) {
                                c = 5;
                                break;
                            }
                            break;
                        case 1751523:
                            if (str.equals("9606")) {
                                c = 0;
                                break;
                            }
                            break;
                        case 1751548:
                            if (str.equals("9610")) {
                                c = 6;
                                break;
                            }
                            break;
                        case 1751552:
                            if (str.equals("9614")) {
                                c = 13;
                                break;
                            }
                            break;
                        case 1751554:
                            if (str.equals("9616")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 1751610:
                            if (str.equals("9630")) {
                                c = 15;
                                break;
                            }
                            break;
                        case 1751612:
                            if (str.equals("9632")) {
                                c = 14;
                                break;
                            }
                            break;
                        case 1989846:
                            if (str.equals("A601")) {
                                c = 7;
                                break;
                            }
                            break;
                        case 1989847:
                            if (str.equals("A602")) {
                                c = 11;
                                break;
                            }
                            break;
                        case 2021558:
                            if (str.equals("B800")) {
                                c = 9;
                                break;
                            }
                            break;
                        case 2943158:
                            if (str.equals("a601")) {
                                c = 8;
                                break;
                            }
                            break;
                        case 2943159:
                            if (str.equals("a602")) {
                                c = 12;
                                break;
                            }
                            break;
                        case 2974870:
                            if (str.equals("b800")) {
                                c = 10;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            StaticClass.checkVar = true;
                            posisi = 1;
                            break;
                        case 1:
                            posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                            break;
                        case 2:
                            if (!StaticClass.fromMenu) {
                                posisi = ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
                                break;
                            } else {
                                posisi = 290;
                                break;
                            }
                        case 3:
                            if (!StaticClass.fromMenu) {
                                posisi = 251;
                                break;
                            } else {
                                posisi = 291;
                                break;
                            }
                        case 4:
                            if (!StaticClass.fromMenu) {
                                posisi = 252;
                                break;
                            } else {
                                posisi = 292;
                                break;
                            }
                        case 5:
                            if (!StaticClass.fromMenu) {
                                posisi = 253;
                                break;
                            } else {
                                posisi = 293;
                                break;
                            }
                        case 6:
                            posisi = 254;
                            break;
                        case 7:
                            posisi = 260;
                            break;
                        case 8:
                            posisi = 260;
                            break;
                        case 9:
                            posisi = 265;
                            break;
                        case 10:
                            posisi = 265;
                            break;
                        case 11:
                            posisi = 275;
                            break;
                        case 12:
                            posisi = 275;
                            break;
                        case 13:
                            posisi = 285;
                            break;
                        case 14:
                            this.dataLog.setLength(0);
                            StaticClass.DataLogger = "";
                            posisi = 0;
                            sendMyBroadCast("DataLoggerFinish");
                            break;
                        case 15:
                            this.dataLog.setLength(0);
                            posisi = GaugeView.SIZE;
                            break;
                        case 16:
                            StaticClass.bolehPing = true;
                            sendMyBroadCast("dataSaved");
                            break;
                    }
                }
            }
            if (this.kirim_ecu_model.booleanValue()) {
                this.kirim_ecu_model = null;
                kirim_lagi();
            }
        }
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private void Reconnect() {
        Log.d("Rec", "Reconnect");
    }

    public void kirim_lagi() {
        StaticClass.bolehPing = false;
        new Thread(new Thread3("1617" + "\r\n")).start();
    }

    public void kirim() {
        StaticClass.bolehPing = false;
        this.cek = 0;
        MappingHandle.list_fuel.clear();
        MappingHandle.list_history.clear();
        MappingHandle.list_injector.clear();
        MappingHandle.list_ignition.clear();
        MappingHandle.list_base_map.clear();
        sendMyBroadCast("data3");
        new Thread(new Thread3("1607" + "\r\n")).start();
    }

    public void terima_fuel(String posisi) {
        StaticClass.bolehPing = false;
        this.cek = 1;
        new Thread(new Thread3("1602;" + posisi + ";" + this.tps_terima + "\r\n")).start();
        if (this.tps_terima == 0) {
            sendMyBroadCast("data4");
        }
    }

    public void terima_base_map(String posisi) {
        StaticClass.bolehPing = false;
        this.cek = 2;
        new Thread(new Thread3("1601;" + posisi + ";" + this.tps_terima + "\r\n")).start();
        if (this.tps_terima == 0) {
            sendMyBroadCast("data5");
        }
    }

    public void terima_injector(String posisi) {
        StaticClass.bolehPing = false;
        this.cek = 3;
        new Thread(new Thread3("1603;" + posisi + ";" + this.tps_terima + "\r\n")).start();
        if (this.tps_terima == 0) {
            sendMyBroadCast("data6");
        }
    }

    public void terima_ignition(String posisi) {
        StaticClass.bolehPing = false;
        this.cek = 4;
        new Thread(new Thread3("1605;" + posisi + ";" + this.tps_terima + "\r\n")).start();
        if (this.tps_terima == 0) {
            sendMyBroadCast("data7");
        }
    }

    public void terima_history() {
        StaticClass.bolehPing = false;
        this.cek = 5;
        new Thread(new Thread3("1610;" + this.tps_terima + "\r\n")).start();
        if (this.tps_terima == 0) {
            sendMyBroadCast("data8");
        }
    }

    public void kirimAutoMapping() {
        StaticClass.bolehPing = false;
        new Thread(new Thread3("1614;1;" + String.valueOf(this.baris_pattern) + "\r\n")).start();
    }

    private void Connect() {
        this.Thread1 = new Thread(new Thread1());
        this.Thread1.start();
    }

    class Thread1 implements Runnable {
        Thread1() {
        }

        public void run() {
            try {
                Log.d("Thread1", "Log in");
                WifiService.this.socket = new Socket(WifiService.this.SERVER_IP, WifiService.this.SERVER_PORT);
                PrintWriter unused = WifiService.this.output = new PrintWriter(WifiService.this.socket.getOutputStream());
                BufferedReader unused2 = WifiService.this.input = new BufferedReader(new InputStreamReader(WifiService.this.socket.getInputStream()));
                new Thread(new Thread2()).start();
                WifiService.this.sendMyBroadCast("doVarAwal");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    class Thread2 implements Runnable {
        Thread2() {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:13:0x003e, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x003f, code lost:
            r2.printStackTrace();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0017, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0018, code lost:
            r0.printStackTrace();
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Removed duplicated region for block: B:13:0x003e A[ExcHandler: NullPointerException (r2v0 'k' java.lang.NullPointerException A[CUSTOM_DECLARE]), Splitter:B:0:0x0000] */
        /* JADX WARNING: Removed duplicated region for block: B:8:0x0017 A[ExcHandler: IOException (r0v0 'e' java.io.IOException A[CUSTOM_DECLARE]), Splitter:B:0:0x0000] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r8 = this;
            L_0x0000:
                juken.android.com.juken_5.WifiService r4 = juken.android.com.juken_5.WifiService.this     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                java.io.BufferedReader r4 = r4.input     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                java.lang.String r3 = r4.readLine()     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                java.lang.String r4 = "Response Data"
                android.util.Log.d(r4, r3)     // Catch:{ Exception -> 0x0048, IOException -> 0x0017, NullPointerException -> 0x003e }
            L_0x000f:
                if (r3 == 0) goto L_0x001c
                juken.android.com.juken_5.WifiService r4 = juken.android.com.juken_5.WifiService.this     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                r4.olahData(r3)     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                goto L_0x0000
            L_0x0017:
                r0 = move-exception
                r0.printStackTrace()
                goto L_0x0000
            L_0x001c:
                r4 = 0
                juken.android.com.juken_5.StaticClass.bolehPing = r4     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                juken.android.com.juken_5.WifiService r4 = juken.android.com.juken_5.WifiService.this     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                java.lang.String r5 = "recon"
                r4.sendMyBroadCast(r5)     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                juken.android.com.juken_5.WifiService r4 = juken.android.com.juken_5.WifiService.this     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                java.lang.Thread r5 = new java.lang.Thread     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                juken.android.com.juken_5.WifiService$Thread1 r6 = new juken.android.com.juken_5.WifiService$Thread1     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                juken.android.com.juken_5.WifiService r7 = juken.android.com.juken_5.WifiService.this     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                r6.<init>()     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                r5.<init>(r6)     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                r4.Thread1 = r5     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                juken.android.com.juken_5.WifiService r4 = juken.android.com.juken_5.WifiService.this     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                java.lang.Thread r4 = r4.Thread1     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
                r4.start()     // Catch:{ IOException -> 0x0017, NullPointerException -> 0x003e, Exception -> 0x0043 }
            L_0x003d:
                return
            L_0x003e:
                r2 = move-exception
                r2.printStackTrace()
                goto L_0x003d
            L_0x0043:
                r1 = move-exception
                r1.printStackTrace()
                goto L_0x003d
            L_0x0048:
                r4 = move-exception
                goto L_0x000f
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.WifiService.Thread2.run():void");
        }
    }

    class Thread3 implements Runnable {
        private String message;

        Thread3(String message2) {
            this.message = message2;
        }

        public void run() {
            try {
                Log.d("Send Data", this.message);
                WifiService.this.boleh_terima = true;
                WifiService.this.output.write(this.message);
                WifiService.this.output.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopClient() {
        Log.d("Stop Client", "stop");
        try {
            if (this.output != null) {
                this.output.flush();
                this.output.close();
            }
            this.input = null;
            this.output = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
