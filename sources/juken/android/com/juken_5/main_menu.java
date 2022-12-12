package juken.android.com.juken_5;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import juken.android.com.juken_5.AutoMapping.auto_mapping;
import juken.android.com.juken_5.data_logger.data_logger_awal;
import juken.android.com.juken_5.folder_base_map.base_map;
import juken.android.com.juken_5.folder_fuel.fuel_correction;
import juken.android.com.juken_5.folder_ignition.ignition_timing;
import juken.android.com.juken_5.folder_injector.injector_timing;
import juken.android.com.juken_5.live.live_awal;

public class main_menu extends AppCompatActivity {
    public static final String VVTC = "VVTC";
    public static final String ack_awal = "ack_awal";
    public static final String auto_ig = "auto_ig";
    public static final String auto_ig_temp = "auto_ig_temp";
    public static final String b_c1 = "b_c1";
    public static final String b_c2 = "b_c2";
    public static final String base_mem_max = "base_mem_max";
    public static final String calculate_pertama = "calculate_pertama";
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
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            main_menu.this.listen();
        }
    };
    private Button auto_correction;
    String b_c11 = "";
    String b_c21 = "";
    private Button base_map;
    private Button base_map1;
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    int cek = 0;
    String cek_ack_awal;
    private Button deceleration_fuel;
    Boolean doubleBackPressed = false;
    /* access modifiers changed from: private */
    public DrawerLayout drawerLayout;
    private Button e_map_;
    ExpandableListView expandableList;
    String f_c11 = "";
    String f_c21 = "";
    private Button fuel_correction;
    private Button fuel_correction1;
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    private Button idle_fuel_;
    String ig_c11 = "";
    String ig_c21 = "";
    private Button ignition_timing;
    private Button ignition_timing1;
    private Button injector_timing;
    private Button injector_timing1;
    private InputStream inputStream = null;
    String it_c11 = "";
    String it_c21 = "";
    private Button jet_fuel;
    private Button jss_;
    Boolean kirim_ecu_model = false;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    List<ExpandedMenuModel> listDataHeader;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            Toast.makeText(main_menu.this.getApplicationContext(), (String) message.obj, 1).show();
        }
    };
    ExpandableListAdapter mMenuAdapter;
    String model = "";
    MyBroadCastReceiver1 myBroadCastReceiver;
    int nilai_tss = 1;
    int nyambung = 1;
    private OutputStream outputStream = null;
    int posisi = 0;
    int posisi_base_map = 0;
    int posisi_fuel = 0;
    int posisi_history = 0;
    int posisi_ignition = 0;
    int posisi_injector = 0;
    ProgressDialog progressDialog;
    Boolean raise = false;
    byte[] readBuffer;
    int readBufferPosition;
    private Button rpm_idle_;
    /* access modifiers changed from: private */
    public final Runnable sendHistory = new Runnable() {
        public void run() {
            try {
                if (main_menu.this.raise.booleanValue()) {
                    main_menu.this.raise = false;
                    for (int i = 0; i < 1281; i++) {
                        MappingHandle.list_history.add("0.0");
                    }
                    if (main_menu.this.progressDialog.isShowing()) {
                        main_menu.this.progressDialog.dismiss();
                    }
                    main_menu.this.resetConnection();
                } else {
                    main_menu.this.raise = true;
                }
                main_menu.this.handler.postDelayed(this, 2000);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(main_menu.this.getApplicationContext(), e.toString(), 1).show();
            }
        }
    };
    Intent service;
    SharedPreferences sharedpreferences;
    private BluetoothSocket socket = null;
    private Button start_fuel;
    int status = 0;
    volatile boolean stopWorker;
    private Toolbar toolbar;
    int tps_terima = 0;
    String tss1 = "";
    private Button warming_up_;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.main_menu);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.toolbar.setTitle((CharSequence) "");
        this.toolbar.setSubtitle((CharSequence) "");
        this.auto_correction = (Button) findViewById(R.id.auto_correction);
        this.auto_correction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.startActivityForResult(new Intent(v.getContext(), auto_correction.class), 0);
            }
        });
        this.warming_up_ = (Button) findViewById(R.id.warming_up);
        this.warming_up_.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.SavePreferences("dari_live", "0");
                main_menu.this.startActivityForResult(new Intent(v.getContext(), warming_up.class), 0);
            }
        });
        this.start_fuel = (Button) findViewById(R.id.start_fuel);
        this.start_fuel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.startActivityForResult(new Intent(v.getContext(), start_fuel.class), 0);
            }
        });
        this.deceleration_fuel = (Button) findViewById(R.id.deceleration_fuel);
        this.deceleration_fuel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.SavePreferences("dari_live", "0");
                main_menu.this.startActivityForResult(new Intent(v.getContext(), deceleration_fuel.class), 0);
            }
        });
        this.jet_fuel = (Button) findViewById(R.id.jet_fuel);
        this.jet_fuel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                String jf1 = PreferenceManager.getDefaultSharedPreferences(main_menu.this.getApplicationContext()).getString("ecu_digunakan", "ecu_digunakan1");
                if (jf1.contains("JUKEN6") || jf1.contains("JUKEN5+")) {
                    main_menu.this.startActivityForResult(new Intent(v.getContext(), jet_fuel_plus.class), 0);
                    return;
                }
                main_menu.this.startActivityForResult(new Intent(v.getContext(), jet_fuel.class), 0);
            }
        });
        this.rpm_idle_ = (Button) findViewById(R.id.rpm_idle);
        this.rpm_idle_.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.SavePreferences("dari_live", "0");
                main_menu.this.startActivityForResult(new Intent(v.getContext(), rpm_idle.class), 0);
            }
        });
        this.idle_fuel_ = (Button) findViewById(R.id.idle_fuel);
        this.idle_fuel_.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.SavePreferences("dari_live", "0");
                main_menu.this.startActivityForResult(new Intent(v.getContext(), rpm_idle.class), 0);
            }
        });
        this.fuel_correction = (Button) findViewById(R.id.fuel_correction);
        this.fuel_correction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.startActivityForResult(new Intent(v.getContext(), fuel_correction.class), 0);
            }
        });
        this.fuel_correction1 = (Button) findViewById(R.id.fuel_correction1);
        this.fuel_correction1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.startActivityForResult(new Intent(v.getContext(), fuel_correction.class), 0);
            }
        });
        this.injector_timing = (Button) findViewById(R.id.injector_timing);
        this.injector_timing.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.startActivityForResult(new Intent(v.getContext(), injector_timing.class), 0);
            }
        });
        this.injector_timing1 = (Button) findViewById(R.id.injector_timing1);
        this.injector_timing1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.startActivityForResult(new Intent(v.getContext(), injector_timing.class), 0);
            }
        });
        this.base_map = (Button) findViewById(R.id.base_map);
        this.base_map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.startActivityForResult(new Intent(v.getContext(), base_map.class), 0);
            }
        });
        this.base_map1 = (Button) findViewById(R.id.base_map1);
        this.base_map1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.startActivityForResult(new Intent(v.getContext(), base_map.class), 0);
            }
        });
        this.ignition_timing = (Button) findViewById(R.id.ignition_timing);
        this.ignition_timing.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.startActivityForResult(new Intent(v.getContext(), ignition_timing.class), 0);
            }
        });
        this.ignition_timing1 = (Button) findViewById(R.id.ignition_timing1);
        this.ignition_timing1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.startActivityForResult(new Intent(v.getContext(), ignition_timing.class), 0);
            }
        });
        this.e_map_ = (Button) findViewById(R.id.e_map);
        this.e_map_.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.SavePreferences("dari_live", "0");
                main_menu.this.startActivityForResult(new Intent(v.getContext(), e_map.class), 0);
            }
        });
        this.jss_ = (Button) findViewById(R.id.jss);
        this.jss_.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    main_menu.this.resetConnection();
                }
                main_menu.this.startActivityForResult(new Intent(v.getContext(), e_map.class), 0);
            }
        });
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        this.drawerLayout.setDescendantFocusability(393216);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, this.toolbar, R.string.openDrawer, R.string.closeDrawer) {
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        this.drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        this.expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
        prepareListData();
        this.mMenuAdapter = new ExpandableListAdapter(this, this.listDataHeader, this.listDataChild, this.expandableList);
        this.expandableList.setAdapter(this.mMenuAdapter);
        this.expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            /* JADX WARNING: Can't fix incorrect switch cases order */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public boolean onChildClick(android.widget.ExpandableListView r26, android.view.View r27, int r28, int r29, long r30) {
                /*
                    r25 = this;
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r0 = r22
                    java.util.HashMap<juken.android.com.juken_5.ExpandedMenuModel, java.util.List<java.lang.String>> r0 = r0.listDataChild
                    r22 = r0
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r23 = r0
                    r0 = r23
                    java.util.List<juken.android.com.juken_5.ExpandedMenuModel> r0 = r0.listDataHeader
                    r23 = r0
                    r0 = r23
                    r1 = r28
                    java.lang.Object r23 = r0.get(r1)
                    java.lang.Object r22 = r22.get(r23)
                    java.util.List r22 = (java.util.List) r22
                    r0 = r22
                    r1 = r29
                    java.lang.Object r22 = r0.get(r1)
                    java.lang.String r22 = (java.lang.String) r22
                    r23 = -1
                    int r24 = r22.hashCode()
                    switch(r24) {
                        case -1358661054: goto L_0x0159;
                        case -1250742817: goto L_0x0041;
                        case -1205780022: goto L_0x00b9;
                        case -959801604: goto L_0x00c9;
                        case -547349541: goto L_0x0050;
                        case -192206932: goto L_0x0109;
                        case 1045241: goto L_0x0119;
                        case 2647343: goto L_0x005f;
                        case 65139636: goto L_0x009b;
                        case 65491762: goto L_0x007d;
                        case 81174014: goto L_0x008c;
                        case 270555047: goto L_0x0129;
                        case 445560775: goto L_0x00aa;
                        case 558324552: goto L_0x006e;
                        case 873562992: goto L_0x00f9;
                        case 1216814430: goto L_0x0139;
                        case 1234250653: goto L_0x0149;
                        case 1487842620: goto L_0x00e9;
                        case 1985518323: goto L_0x00d9;
                        default: goto L_0x0039;
                    }
                L_0x0039:
                    r22 = r23
                L_0x003b:
                    switch(r22) {
                        case 0: goto L_0x0169;
                        case 1: goto L_0x01ab;
                        case 2: goto L_0x01ed;
                        case 3: goto L_0x022d;
                        case 4: goto L_0x0273;
                        case 5: goto L_0x02b9;
                        case 6: goto L_0x02ff;
                        case 7: goto L_0x0352;
                        case 8: goto L_0x0394;
                        case 9: goto L_0x03d6;
                        case 10: goto L_0x0418;
                        case 11: goto L_0x045a;
                        case 12: goto L_0x04a0;
                        case 13: goto L_0x04e6;
                        case 14: goto L_0x0528;
                        case 15: goto L_0x0598;
                        case 16: goto L_0x05da;
                        case 17: goto L_0x061c;
                        case 18: goto L_0x0633;
                        default: goto L_0x003e;
                    }
                L_0x003e:
                    r22 = 0
                    return r22
                L_0x0041:
                    java.lang.String r24 = "ECU Limiter"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 0
                    goto L_0x003b
                L_0x0050:
                    java.lang.String r24 = "Auto Timing"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 1
                    goto L_0x003b
                L_0x005f:
                    java.lang.String r24 = "VVTC"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 2
                    goto L_0x003b
                L_0x006e:
                    java.lang.String r24 = "Smart Key"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 3
                    goto L_0x003b
                L_0x007d:
                    java.lang.String r24 = "DWell"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 4
                    goto L_0x003b
                L_0x008c:
                    java.lang.String r24 = "Turbo"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 5
                    goto L_0x003b
                L_0x009b:
                    java.lang.String r24 = "E-MAP"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 6
                    goto L_0x003b
                L_0x00aa:
                    java.lang.String r24 = "Diagnostic"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 7
                    goto L_0x003b
                L_0x00b9:
                    java.lang.String r24 = "Calibration"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 8
                    goto L_0x003b
                L_0x00c9:
                    java.lang.String r24 = "Analysis"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 9
                    goto L_0x003b
                L_0x00d9:
                    java.lang.String r24 = "Maintenance"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 10
                    goto L_0x003b
                L_0x00e9:
                    java.lang.String r24 = "Quick Shifter"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 11
                    goto L_0x003b
                L_0x00f9:
                    java.lang.String r24 = "Pattern"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 12
                    goto L_0x003b
                L_0x0109:
                    java.lang.String r24 = "Fuel Calculator"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 13
                    goto L_0x003b
                L_0x0119:
                    java.lang.String r24 = "Reset Factory"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 14
                    goto L_0x003b
                L_0x0129:
                    java.lang.String r24 = "Fan Temperature"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 15
                    goto L_0x003b
                L_0x0139:
                    java.lang.String r24 = "Auto Protect"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 16
                    goto L_0x003b
                L_0x0149:
                    java.lang.String r24 = "ECU Data Logger"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 17
                    goto L_0x003b
                L_0x0159:
                    java.lang.String r24 = "File Data Logger"
                    r0 = r22
                    r1 = r24
                    boolean r22 = r0.equals(r1)
                    if (r22 == 0) goto L_0x0039
                    r22 = 18
                    goto L_0x003b
                L_0x0169:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x017c
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x017c:
                    android.content.Intent r7 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.ecu_limiter> r23 = juken.android.com.juken_5.ecu_limiter.class
                    r0 = r22
                    r1 = r23
                    r7.<init>(r0, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r23
                    r0.startActivityForResult(r7, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x01ab:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x01be
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x01be:
                    android.content.Intent r4 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.auto_timing> r23 = juken.android.com.juken_5.auto_timing.class
                    r0 = r22
                    r1 = r23
                    r4.<init>(r0, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r23
                    r0.startActivityForResult(r4, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x01ed:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x0200
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x0200:
                    android.content.Intent r21 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.vvtc> r23 = juken.android.com.juken_5.vvtc.class
                    r21.<init>(r22, r23)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r21
                    r2 = r23
                    r0.startActivityForResult(r1, r2)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x022d:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x0240
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x0240:
                    android.content.Intent r16 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.smart_key> r23 = juken.android.com.juken_5.smart_key.class
                    r0 = r16
                    r1 = r22
                    r2 = r23
                    r0.<init>(r1, r2)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r16
                    r2 = r23
                    r0.startActivityForResult(r1, r2)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x0273:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x0286
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x0286:
                    android.content.Intent r17 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.dwell> r23 = juken.android.com.juken_5.dwell.class
                    r0 = r17
                    r1 = r22
                    r2 = r23
                    r0.<init>(r1, r2)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r17
                    r2 = r23
                    r0.startActivityForResult(r1, r2)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x02b9:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x02cc
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x02cc:
                    android.content.Intent r20 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.turbo> r23 = juken.android.com.juken_5.turbo.class
                    r0 = r20
                    r1 = r22
                    r2 = r23
                    r0.<init>(r1, r2)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r20
                    r2 = r23
                    r0.startActivityForResult(r1, r2)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x02ff:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x0312
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x0312:
                    java.lang.String r5 = "0"
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    java.lang.String r23 = "dari_live"
                    r0 = r22
                    r1 = r23
                    r0.SavePreferences(r1, r5)
                    android.content.Intent r8 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.e_map> r23 = juken.android.com.juken_5.e_map.class
                    r0 = r22
                    r1 = r23
                    r8.<init>(r0, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r23
                    r0.startActivityForResult(r8, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x0352:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x0365
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x0365:
                    android.content.Intent r13 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.diagnostik> r23 = juken.android.com.juken_5.diagnostik.class
                    r0 = r22
                    r1 = r23
                    r13.<init>(r0, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r23
                    r0.startActivityForResult(r13, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x0394:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x03a7
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x03a7:
                    android.content.Intent r12 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.calibration> r23 = juken.android.com.juken_5.calibration.class
                    r0 = r22
                    r1 = r23
                    r12.<init>(r0, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r23
                    r0.startActivityForResult(r12, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x03d6:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x03e9
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x03e9:
                    android.content.Intent r10 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.folder_analisis.analisis> r23 = juken.android.com.juken_5.folder_analisis.analisis.class
                    r0 = r22
                    r1 = r23
                    r10.<init>(r0, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r23
                    r0.startActivityForResult(r10, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x0418:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x042b
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x042b:
                    android.content.Intent r14 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.maintenance> r23 = juken.android.com.juken_5.maintenance.class
                    r0 = r22
                    r1 = r23
                    r14.<init>(r0, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r23
                    r0.startActivityForResult(r14, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x045a:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x046d
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x046d:
                    android.content.Intent r19 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.shifter> r23 = juken.android.com.juken_5.shifter.class
                    r0 = r19
                    r1 = r22
                    r2 = r23
                    r0.<init>(r1, r2)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r19
                    r2 = r23
                    r0.startActivityForResult(r1, r2)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x04a0:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x04b3
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x04b3:
                    android.content.Intent r18 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.pattern.pattern1.pattern1> r23 = juken.android.com.juken_5.pattern.pattern1.pattern1.class
                    r0 = r18
                    r1 = r22
                    r2 = r23
                    r0.<init>(r1, r2)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r18
                    r2 = r23
                    r0.startActivityForResult(r1, r2)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x04e6:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x04f9
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x04f9:
                    android.content.Intent r11 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.fuel_calculator> r23 = juken.android.com.juken_5.fuel_calculator.class
                    r0 = r22
                    r1 = r23
                    r11.<init>(r0, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r23
                    r0.startActivityForResult(r11, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x0528:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "wifi"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x0584
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this     // Catch:{ IllegalArgumentException -> 0x057f }
                    r22 = r0
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this     // Catch:{ IllegalArgumentException -> 0x057f }
                    r23 = r0
                    r0 = r23
                    juken.android.com.juken_5.main_menu$MyBroadCastReceiver1 r0 = r0.myBroadCastReceiver     // Catch:{ IllegalArgumentException -> 0x057f }
                    r23 = r0
                    r22.unregisterReceiver(r23)     // Catch:{ IllegalArgumentException -> 0x057f }
                L_0x0547:
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.finish()
                    android.content.Intent r15 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.reset_factory> r23 = juken.android.com.juken_5.reset_factory.class
                    r0 = r22
                    r1 = r23
                    r15.<init>(r0, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r23
                    r0.startActivityForResult(r15, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x057f:
                    r6 = move-exception
                    r6.printStackTrace()
                    goto L_0x0547
                L_0x0584:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x0547
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                    goto L_0x0547
                L_0x0598:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x05ab
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x05ab:
                    android.content.Intent r9 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.fan_temp> r23 = juken.android.com.juken_5.fan_temp.class
                    r0 = r22
                    r1 = r23
                    r9.<init>(r0, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r23
                    r0.startActivityForResult(r9, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x05da:
                    java.lang.String r22 = juken.android.com.juken_5.StaticClass.TipeKoneksi
                    java.lang.String r23 = "bt"
                    boolean r22 = r22.equals(r23)
                    if (r22 == 0) goto L_0x05ed
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r22.resetConnection()
                L_0x05ed:
                    android.content.Intent r3 = new android.content.Intent
                    android.content.Context r22 = r27.getContext()
                    java.lang.Class<juken.android.com.juken_5.auto_protect> r23 = juken.android.com.juken_5.auto_protect.class
                    r0 = r22
                    r1 = r23
                    r3.<init>(r0, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    r23 = 0
                    r0 = r22
                    r1 = r23
                    r0.startActivityForResult(r3, r1)
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.support.v4.widget.DrawerLayout r22 = r22.drawerLayout
                    r23 = 3
                    r22.closeDrawer((int) r23)
                    goto L_0x003e
                L_0x061c:
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.content.Context r22 = r22.getApplicationContext()
                    java.lang.String r23 = "ECU Data Logger"
                    r24 = 0
                    android.widget.Toast r22 = android.widget.Toast.makeText(r22, r23, r24)
                    r22.show()
                    goto L_0x003e
                L_0x0633:
                    r0 = r25
                    juken.android.com.juken_5.main_menu r0 = juken.android.com.juken_5.main_menu.this
                    r22 = r0
                    android.content.Context r22 = r22.getApplicationContext()
                    java.lang.String r23 = "File Data Logger"
                    r24 = 0
                    android.widget.Toast r22 = android.widget.Toast.makeText(r22, r23, r24)
                    r22.show()
                    goto L_0x003e
                */
                throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.main_menu.AnonymousClass21.onChildClick(android.widget.ExpandableListView, android.view.View, int, int, long):boolean");
            }
        });
        this.expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        if (StaticClass.TipeKoneksi.equals("bt")) {
                            main_menu.this.resetConnection();
                        }
                        main_menu.this.drawerLayout.closeDrawer(3);
                        main_menu.this.startActivityForResult(new Intent(view.getContext(), data_logger_awal.class), 0);
                        return false;
                    case 2:
                        if ((!MappingHandle.NamaFileFuel.equals("Fuel Correction - ECU Core 1") && !MappingHandle.NamaFileFuel.equals("Fuel Correction - ECU Core 2")) || ((!MappingHandle.NamaFileBM.equals("Base Map - ECU Core 1") && !MappingHandle.NamaFileBM.equals("Base Map - ECU Core 2")) || ((!MappingHandle.NamaFileIG.equals("Ignition Timing - ECU Core 1") && !MappingHandle.NamaFileIG.equals("Ignition Timing - ECU Core 2")) || (!MappingHandle.NamaFileIT.equals("Injector Timing - ECU Core 1") && !MappingHandle.NamaFileIT.equals("Injector Timing - ECU Core 2"))))) {
                            Toast.makeText(main_menu.this, main_menu.this.getString(R.string.LiveSalahMapping), 0).show();
                            return false;
                        } else if (StaticClass.saveFuel) {
                            Toast.makeText(main_menu.this, main_menu.this.getString(R.string.LiveSaveFuel), 1).show();
                            return false;
                        } else if (StaticClass.saveBM) {
                            Toast.makeText(main_menu.this, main_menu.this.getString(R.string.LiveSaveBM), 1).show();
                            return false;
                        } else if (StaticClass.saveIT) {
                            Toast.makeText(main_menu.this, main_menu.this.getString(R.string.LiveSaveIT), 1).show();
                            return false;
                        } else if (StaticClass.saveIG) {
                            Toast.makeText(main_menu.this, main_menu.this.getString(R.string.LiveSaveIG), 1).show();
                            return false;
                        } else {
                            MappingHandle.list_afr_live.clear();
                            MappingHandle.list_afr_live_ig.clear();
                            MappingHandle.list_fuel_live_ganti.clear();
                            MappingHandle.list_fuel_live.clear();
                            MappingHandle.list_ignition_live_ganti.clear();
                            MappingHandle.list_ignition_live.clear();
                            for (int h = 0; h < 1281; h++) {
                                MappingHandle.list_afr_live.add("0.0");
                                MappingHandle.list_fuel_live.add(MappingHandle.list_fuel.get(h));
                            }
                            for (int h2 = 0; h2 < 651; h2++) {
                                MappingHandle.list_afr_live_ig.add("0.0");
                                MappingHandle.list_ignition_live.add(MappingHandle.list_ignition.get(h2));
                            }
                            if (MappingHandle.list_fuel_ganti.size() < 2) {
                                for (int h3 = 0; h3 < 1281; h3++) {
                                    MappingHandle.list_fuel_live_ganti.add("0");
                                }
                            } else {
                                MappingHandle.list_fuel_live_ganti.addAll(MappingHandle.list_fuel_ganti);
                            }
                            if (MappingHandle.list_ignition_ganti.size() < 2) {
                                for (int h4 = 0; h4 < 651; h4++) {
                                    MappingHandle.list_ignition_live_ganti.add("0");
                                }
                            } else {
                                MappingHandle.list_ignition_live_ganti.addAll(MappingHandle.list_ignition_ganti);
                            }
                            if (StaticClass.TipeKoneksi.equals("bt")) {
                                main_menu.this.resetConnection();
                            }
                            main_menu.this.startActivityForResult(new Intent(main_menu.this.getApplicationContext(), live_awal.class), 0);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            main_menu.this.drawerLayout.closeDrawer(3);
                            return false;
                        }
                    case 3:
                        if (StaticClass.TipeKoneksi.equals("bt")) {
                            main_menu.this.resetConnection();
                        }
                        main_menu.this.SavePreferences("NamaPatternFile", "kosong");
                        main_menu.this.startActivityForResult(new Intent(view.getContext(), auto_mapping.class), 0);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        main_menu.this.drawerLayout.closeDrawer(3);
                        return false;
                    case 5:
                        Toast.makeText(main_menu.this.getApplicationContext(), "Smart Tunning", 0).show();
                        return false;
                    default:
                        return false;
                }
            }
        });
    }

    private void prepareListData() {
        this.listDataHeader = new ArrayList();
        this.listDataChild = new HashMap<>();
        ExpandedMenuModel item1 = new ExpandedMenuModel();
        item1.setIconName("Menu");
        item1.setIconImg(R.drawable.ic_menu_black_24dp);
        this.listDataHeader.add(item1);
        ExpandedMenuModel item2 = new ExpandedMenuModel();
        item2.setIconName("Data Logger");
        item2.setIconImg(R.drawable.data_logger);
        this.listDataHeader.add(item2);
        ExpandedMenuModel item3 = new ExpandedMenuModel();
        item3.setIconName("Live");
        item3.setIconImg(R.drawable.live);
        this.listDataHeader.add(item3);
        ExpandedMenuModel item5 = new ExpandedMenuModel();
        item5.setIconName("Auto Mapping");
        item5.setIconImg(R.drawable.ic_auto_calculation_copy);
        this.listDataHeader.add(item5);
        ExpandedMenuModel item6 = new ExpandedMenuModel();
        item6.setIconName("Smart Tunning");
        item6.setIconImg(17301533);
        this.listDataHeader.add(item6);
        List<String> heading1 = new ArrayList<>();
        heading1.add("ECU Limiter");
        heading1.add("Auto Timing");
        heading1.add("VVTC");
        heading1.add("Smart Key");
        heading1.add("DWell");
        heading1.add("Turbo");
        heading1.add("E-MAP");
        heading1.add("Diagnostic");
        heading1.add("Calibration");
        heading1.add("Analysis");
        heading1.add("Maintenance");
        heading1.add("Quick Shifter");
        heading1.add("Pattern");
        heading1.add("Fuel Calculator");
        heading1.add("Reset Factory");
        heading1.add("Fan Temperature");
        heading1.add("Auto Protect");
        List<String> heading2 = new ArrayList<>();
        List<String> heading3 = new ArrayList<>();
        new ArrayList();
        List<String> heading5 = new ArrayList<>();
        List<String> heading6 = new ArrayList<>();
        this.listDataChild.put(this.listDataHeader.get(0), heading1);
        this.listDataChild.put(this.listDataHeader.get(1), heading2);
        this.listDataChild.put(this.listDataHeader.get(2), heading3);
        this.listDataChild.put(this.listDataHeader.get(3), heading5);
        this.listDataChild.put(this.listDataHeader.get(4), heading6);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                this.drawerLayout.openDrawer((int) GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        if (this.doubleBackPressed.booleanValue()) {
            if (StaticClass.TipeKoneksi.equals("wifi")) {
                if (this.service != null) {
                    this.service.putExtra("send", "closeCon");
                    startService(this.service);
                }
                stopService(new Intent(this, WifiService.class));
                try {
                    unregisterReceiver(this.myBroadCastReceiver);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            } else if (StaticClass.TipeKoneksi.equals("bt")) {
                try {
                    if (this.bluetooth.isEnabled()) {
                        if (this.nyambung == 1) {
                            resetConnection();
                            Thread.sleep(500);
                        }
                        this.bluetooth.disable();
                    }
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
            super.onBackPressed();
            return;
        }
        this.doubleBackPressed = true;
        Toast.makeText(this, "Please click BACK again to exit", 1).show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                main_menu.this.doubleBackPressed = false;
            }
        }, 2000);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LanguageHelper.onAttach1(getApplicationContext());
        this.cek_ack_awal = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("ack_awal", "ack_awal1");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.cek_ack_awal.equals("1")) {
            SavePreferences("ack_awal", "0");
            SavePreferences("sudah_pernah_ecu", "0");
            SavePreferences("nilai_ecu_sebelumnya", "0");
            SavePreferences(calculate_pertama, "0");
            SavePreferences("dari_live", "0");
            SavePreferences("back_pressed_mapping", "0");
            MappingHandle.list_fuel_live.clear();
            MappingHandle.list_fuel_live_ganti.clear();
            MappingHandle.list_fuel_ganti.clear();
            MappingHandle.list_ignition_live.clear();
            MappingHandle.list_ignition_live_ganti.clear();
            MappingHandle.list_ignition_ganti.clear();
            MappingHandle.list_fuel.clear();
            MappingHandle.list_injector.clear();
            MappingHandle.list_ignition.clear();
            MappingHandle.list_base_map.clear();
            MappingHandle.list_history.clear();
            StaticClass.fromMenu = false;
            if (StaticClass.TipeKoneksi.equals("wifi")) {
                this.myBroadCastReceiver = new MyBroadCastReceiver1();
                registerMyReceiver();
                this.service = new Intent(this, WifiService.class);
                this.service.putExtra("send", "cekConnection");
                startService(this.service);
                this.service.putExtra("send", "Var Awal");
                startService(this.service);
            } else if (StaticClass.TipeKoneksi.equals("bt")) {
                LoadPreferences();
            } else {
                set_param_awal();
            }
        }
    }

    private void set_param_awal() {
        if (MappingHandle.list_fuel.size() < 2) {
            for (int i = 0; i < 1281; i++) {
                MappingHandle.list_fuel.add("0");
                MappingHandle.list_base_map.add("0.00");
                MappingHandle.list_injector.add("0");
                if (i < 651) {
                    MappingHandle.list_ignition.add("0.0");
                }
            }
            for (int i2 = 0; i2 < 1281; i2++) {
                MappingHandle.list_history.add("0.0");
            }
        }
        SavePreferences("VVTC", "1000");
        SavePreferences("tps_low", "0");
        SavePreferences("tps_high", "100");
        SavePreferences("jjsore_tps", "257");
        SavePreferences("fuel_start", "0");
        SavePreferences("position_rpm_idle", "1200");
        SavePreferences("warming_up", "1000");
        SavePreferences("jjsore_rpm", "60");
        SavePreferences("range_lo", "14");
        SavePreferences("range_mid", "16");
        SavePreferences("range_hi", "31");
        SavePreferences("ti", "0");
        SavePreferences("ki", "0");
        SavePreferences("shift", "80");
        SavePreferences("dec_fuel", "0");
        SavePreferences("limiter", "16000");
        SavePreferences("dwell_time", "10");
        SavePreferences("auto_ig", "0");
        SavePreferences("auto_ig_temp", "85");
        SavePreferences("fuel_mem_max", "0");
        SavePreferences("ig_mem_max", "0");
        SavePreferences("it_mem_max", "0");
        SavePreferences("base_mem_max", "0");
        SavePreferences("tss", "0");
        SavePreferences("b_c1", "0");
        SavePreferences("f_c1", "0");
        SavePreferences("it_c1", "0");
        SavePreferences("ig_c1", "0");
        SavePreferences("b_c2", "0");
        SavePreferences("f_c2", "0");
        SavePreferences("it_c2", "0");
        SavePreferences("ig_c2", "0");
        SavePreferences("turbo_en", "0");
        SavePreferences("dec_time", "10");
        SavePreferences("nilai_idle", "0");
        SavePreferences("idle_fuel", "0");
        SavePreferences("turbo_tps", "0");
        SavePreferences("turbo_rpm", "1000");
        SavePreferences("imob_en", "0");
        SavePreferences("key1_h", "0");
        SavePreferences("key1_l", "0");
        SavePreferences("key2_h", "0");
        SavePreferences("key2_l", "0");
        SavePreferences("key3_h", "0");
        SavePreferences("key3_l", "0");
        SavePreferences("ecu_cyl", "0");
        SavePreferences("secure", "0");
        SavePreferences("jf1", "0");
        SavePreferences("jf2", "0");
        SavePreferences("jf3", "0");
        SavePreferences("jf4", "0");
        SavePreferences("t_rate1", "0");
        SavePreferences("t_rate2", "0");
        SavePreferences("t_rate3", "0");
        SavePreferences("ecu_model", "224");
        SavePreferences("key4_h", "0");
        SavePreferences("key4_l", "0");
        SavePreferences("key5_h", "0");
        SavePreferences("key5_l", "0");
        SavePreferences("suhu_fan", "600");
        SavePreferences("protect_enable", "0");
        SavePreferences("protect_limit", "0");
        SavePreferences("protect_temp", "60.0");
    }

    private void LoadPreferences() {
        final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("android.bluetooth.device.extra.DEVICE", "rame"));
        new Thread() {
            public void run() {
                main_menu.this.connect(perangkat);
            }
        }.start();
    }

    /* access modifiers changed from: protected */
    public void connect(BluetoothDevice device) {
        try {
            this.socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            this.socket.connect();
            this._serverWorker.start();
            kirim();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void listen() {
        this.stopWorker = false;
        this.readBufferPosition = 0;
        this.readBuffer = new byte[1024];
        while (!Thread.currentThread().isInterrupted() && !this.stopWorker && this.nyambung == 1) {
            try {
                this.inputStream = this.socket.getInputStream();
                int bytesAvailable = this.inputStream.available();
                if (bytesAvailable > 0) {
                    byte[] packetBytes = new byte[bytesAvailable];
                    this.inputStream.read(packetBytes);
                    for (int i = 0; i < bytesAvailable; i++) {
                        byte b = packetBytes[i];
                        if (this.cek == 0) {
                            if (b == 59 || b == 10) {
                                byte[] encodedBytes = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                final String data = new String(encodedBytes, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        switch (main_menu.this.posisi) {
                                            case 0:
                                                if (data.equals("9606")) {
                                                    main_menu.this.posisi = 1;
                                                    return;
                                                } else if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                } else {
                                                    if (main_menu.this.status == 0) {
                                                        main_menu.this.status = 1;
                                                    }
                                                    main_menu.this.posisi = 0;
                                                    return;
                                                }
                                            case 1:
                                                main_menu.this.kirim_ecu_model = 1;
                                                String VVTC1 = data.trim();
                                                if (main_menu.isNumeric(VVTC1)) {
                                                    main_menu.this.SavePreferences("VVTC", VVTC1);
                                                } else {
                                                    main_menu.this.SavePreferences("VVTC", "1000");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 2;
                                                return;
                                            case 2:
                                                String tps_low1 = data.trim();
                                                if (main_menu.isNumeric(tps_low1)) {
                                                    main_menu.this.SavePreferences("tps_low", tps_low1);
                                                } else {
                                                    main_menu.this.SavePreferences("tps_low", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 3;
                                                return;
                                            case 3:
                                                String tps_high1 = data.trim();
                                                if (main_menu.isNumeric(tps_high1)) {
                                                    main_menu.this.SavePreferences("tps_high", tps_high1);
                                                } else {
                                                    main_menu.this.SavePreferences("tps_high", "100");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 4;
                                                return;
                                            case 4:
                                                String jjsore_tps1 = data.trim();
                                                if (main_menu.isNumeric(jjsore_tps1)) {
                                                    main_menu.this.SavePreferences("jjsore_tps", jjsore_tps1);
                                                } else {
                                                    main_menu.this.SavePreferences("jjsore_tps", "257");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 5;
                                                return;
                                            case 5:
                                                String fuel_start1 = data.trim();
                                                if (main_menu.isNumeric(fuel_start1)) {
                                                    main_menu.this.SavePreferences("fuel_start", fuel_start1);
                                                } else {
                                                    main_menu.this.SavePreferences("fuel_start", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 6;
                                                return;
                                            case 6:
                                                String position_rpm_idle1 = data.trim();
                                                if (main_menu.isNumeric(position_rpm_idle1)) {
                                                    main_menu.this.SavePreferences("position_rpm_idle", position_rpm_idle1);
                                                } else {
                                                    main_menu.this.SavePreferences("position_rpm_idle", "1200");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 7;
                                                return;
                                            case 7:
                                                String warming_up1 = data.trim();
                                                if (main_menu.isNumeric(warming_up1)) {
                                                    main_menu.this.SavePreferences("warming_up", warming_up1);
                                                } else {
                                                    main_menu.this.SavePreferences("warming_up", "1000");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 8;
                                                return;
                                            case 8:
                                                String jjsore_rpm1 = data.trim();
                                                if (main_menu.isNumeric(jjsore_rpm1)) {
                                                    main_menu.this.SavePreferences("jjsore_rpm", jjsore_rpm1);
                                                } else {
                                                    main_menu.this.SavePreferences("jjsore_rpm", "60");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 9;
                                                return;
                                            case 9:
                                                String range_lo1 = data.trim();
                                                if (main_menu.isNumeric(range_lo1)) {
                                                    main_menu.this.SavePreferences("range_lo", range_lo1);
                                                } else {
                                                    main_menu.this.SavePreferences("range_lo", "14");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 10;
                                                return;
                                            case 10:
                                                String range_mid1 = data.trim();
                                                if (main_menu.isNumeric(range_mid1)) {
                                                    main_menu.this.SavePreferences("range_mid", range_mid1);
                                                } else {
                                                    main_menu.this.SavePreferences("range_mid", "16");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 11;
                                                return;
                                            case 11:
                                                String range_hi1 = data.trim();
                                                if (main_menu.isNumeric(range_hi1)) {
                                                    main_menu.this.SavePreferences("range_hi", range_hi1);
                                                } else {
                                                    main_menu.this.SavePreferences("range_hi", "31");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 12;
                                                return;
                                            case 12:
                                                String ti1 = data.trim();
                                                if (main_menu.isNumeric(ti1)) {
                                                    main_menu.this.SavePreferences("ti", ti1);
                                                } else {
                                                    main_menu.this.SavePreferences("ti", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 13;
                                                return;
                                            case 13:
                                                String ki1 = data.trim();
                                                if (main_menu.isNumeric(ki1)) {
                                                    main_menu.this.SavePreferences("ki", ki1);
                                                } else {
                                                    main_menu.this.SavePreferences("ki", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 14;
                                                return;
                                            case 14:
                                                String shift1 = data.trim();
                                                if (main_menu.isNumeric(shift1)) {
                                                    main_menu.this.SavePreferences("shift", shift1);
                                                } else {
                                                    main_menu.this.SavePreferences("shift", "80");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 15;
                                                return;
                                            case 15:
                                                String dec_fuel1 = data.trim();
                                                if (main_menu.isNumeric(dec_fuel1)) {
                                                    main_menu.this.SavePreferences("dec_fuel", dec_fuel1);
                                                } else {
                                                    main_menu.this.SavePreferences("dec_fuel", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 16;
                                                return;
                                            case 16:
                                                String limiter1 = data.trim();
                                                if (main_menu.isNumeric(limiter1)) {
                                                    main_menu.this.SavePreferences("limiter", limiter1);
                                                } else {
                                                    main_menu.this.SavePreferences("limiter", "16000");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 17;
                                                return;
                                            case 17:
                                                String dwell_time1 = data.trim();
                                                if (main_menu.isNumeric(dwell_time1)) {
                                                    main_menu.this.SavePreferences("dwell_time", dwell_time1);
                                                } else {
                                                    main_menu.this.SavePreferences("dwell_time", "10");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 18;
                                                return;
                                            case 18:
                                                String auto_ig1 = data.trim();
                                                if (main_menu.isNumeric(auto_ig1)) {
                                                    main_menu.this.SavePreferences("auto_ig", auto_ig1);
                                                } else {
                                                    main_menu.this.SavePreferences("auto_ig", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 19;
                                                return;
                                            case 19:
                                                String auto_ig_temp1 = data.trim();
                                                if (main_menu.isNumeric(auto_ig_temp1)) {
                                                    main_menu.this.SavePreferences("auto_ig_temp", auto_ig_temp1);
                                                } else {
                                                    main_menu.this.SavePreferences("auto_ig_temp", "85");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 20;
                                                return;
                                            case 20:
                                                String fuel_mem_max1 = data.trim();
                                                if (main_menu.isNumeric(fuel_mem_max1)) {
                                                    main_menu.this.SavePreferences("fuel_mem_max", fuel_mem_max1);
                                                } else {
                                                    main_menu.this.SavePreferences("fuel_mem_max", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 21;
                                                return;
                                            case 21:
                                                String ig_mem_max1 = data.trim();
                                                if (main_menu.isNumeric(ig_mem_max1)) {
                                                    main_menu.this.SavePreferences("ig_mem_max", ig_mem_max1);
                                                } else {
                                                    main_menu.this.SavePreferences("ig_mem_max", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 22;
                                                return;
                                            case 22:
                                                String it_mem_max1 = data.trim();
                                                if (main_menu.isNumeric(it_mem_max1)) {
                                                    main_menu.this.SavePreferences("it_mem_max", it_mem_max1);
                                                } else {
                                                    main_menu.this.SavePreferences("it_mem_max", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 23;
                                                return;
                                            case 23:
                                                String base_mem_max1 = data.trim();
                                                if (main_menu.isNumeric(base_mem_max1)) {
                                                    main_menu.this.SavePreferences("base_mem_max", base_mem_max1);
                                                } else {
                                                    main_menu.this.SavePreferences("base_mem_max", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 24;
                                                return;
                                            case 24:
                                                if (data.trim().equals("0")) {
                                                    main_menu.this.tss1 = "1";
                                                } else {
                                                    main_menu.this.tss1 = "2";
                                                }
                                                main_menu.this.SavePreferences("tss", main_menu.this.tss1);
                                                Log.d("TSS", main_menu.this.tss1);
                                                main_menu.this.posisi = 25;
                                                return;
                                            case 25:
                                                main_menu.this.b_c11 = data.trim();
                                                if (main_menu.isNumeric(main_menu.this.b_c11)) {
                                                    main_menu.this.SavePreferences("b_c1", main_menu.this.b_c11);
                                                } else {
                                                    main_menu.this.b_c11 = "0";
                                                    main_menu.this.SavePreferences("b_c1", main_menu.this.b_c11);
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 26;
                                                return;
                                            case 26:
                                                main_menu.this.f_c11 = data.trim();
                                                if (main_menu.isNumeric(main_menu.this.f_c11)) {
                                                    main_menu.this.SavePreferences("f_c1", main_menu.this.f_c11);
                                                } else {
                                                    main_menu.this.f_c11 = "0";
                                                    main_menu.this.SavePreferences("f_c1", main_menu.this.f_c11);
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 27;
                                                return;
                                            case 27:
                                                main_menu.this.it_c11 = data.trim();
                                                if (main_menu.isNumeric(main_menu.this.it_c11)) {
                                                    main_menu.this.SavePreferences("it_c1", main_menu.this.it_c11);
                                                } else {
                                                    main_menu.this.it_c11 = "0";
                                                    main_menu.this.SavePreferences("it_c1", main_menu.this.it_c11);
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 28;
                                                return;
                                            case 28:
                                                main_menu.this.ig_c11 = data.trim();
                                                if (main_menu.isNumeric(main_menu.this.ig_c11)) {
                                                    main_menu.this.SavePreferences("ig_c1", main_menu.this.ig_c11);
                                                } else {
                                                    main_menu.this.ig_c11 = "0";
                                                    main_menu.this.SavePreferences("ig_c1", main_menu.this.ig_c11);
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 29;
                                                return;
                                            case 29:
                                                main_menu.this.b_c21 = data.trim();
                                                if (main_menu.isNumeric(main_menu.this.b_c21)) {
                                                    main_menu.this.SavePreferences("b_c2", main_menu.this.b_c21);
                                                } else {
                                                    main_menu.this.b_c21 = "0";
                                                    main_menu.this.SavePreferences("b_c2", main_menu.this.b_c21);
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 30;
                                                return;
                                            case 30:
                                                main_menu.this.f_c21 = data.trim();
                                                if (main_menu.isNumeric(main_menu.this.f_c21)) {
                                                    main_menu.this.SavePreferences("f_c2", main_menu.this.f_c21);
                                                } else {
                                                    main_menu.this.f_c21 = "0";
                                                    main_menu.this.SavePreferences("f_c2", main_menu.this.f_c21);
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 31;
                                                return;
                                            case 31:
                                                main_menu.this.it_c21 = data.trim();
                                                if (main_menu.isNumeric(main_menu.this.it_c21)) {
                                                    main_menu.this.SavePreferences("it_c2", main_menu.this.it_c21);
                                                } else {
                                                    main_menu.this.it_c21 = "0";
                                                    main_menu.this.SavePreferences("it_c2", main_menu.this.it_c21);
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 32;
                                                return;
                                            case 32:
                                                main_menu.this.ig_c21 = data.trim();
                                                if (main_menu.isNumeric(main_menu.this.ig_c21)) {
                                                    main_menu.this.SavePreferences("ig_c2", main_menu.this.ig_c21);
                                                } else {
                                                    main_menu.this.ig_c21 = "0";
                                                    main_menu.this.SavePreferences("ig_c2", main_menu.this.ig_c21);
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 33;
                                                return;
                                            case 33:
                                                String turbo_en1 = data.trim();
                                                if (main_menu.isNumeric(turbo_en1)) {
                                                    main_menu.this.SavePreferences("turbo_en", turbo_en1);
                                                } else {
                                                    main_menu.this.SavePreferences("turbo_en", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 34;
                                                return;
                                            case 34:
                                                String dec_time1 = data.trim();
                                                if (main_menu.isNumeric(dec_time1)) {
                                                    main_menu.this.SavePreferences("dec_time", dec_time1);
                                                } else {
                                                    main_menu.this.SavePreferences("dec_time", "10");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 35;
                                                return;
                                            case 35:
                                                String nilai_idle1 = data.trim();
                                                if (main_menu.isNumeric(nilai_idle1)) {
                                                    main_menu.this.SavePreferences("nilai_idle", nilai_idle1);
                                                } else {
                                                    main_menu.this.SavePreferences("nilai_idle", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 36;
                                                return;
                                            case 36:
                                                String idle_fuel1 = data.trim();
                                                if (main_menu.isNumeric(idle_fuel1)) {
                                                    main_menu.this.SavePreferences("idle_fuel", idle_fuel1);
                                                } else {
                                                    main_menu.this.SavePreferences("idle_fuel", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 37;
                                                return;
                                            case 37:
                                                String turbo_tps1 = data.trim();
                                                if (main_menu.isNumeric(turbo_tps1)) {
                                                    main_menu.this.SavePreferences("turbo_tps", turbo_tps1);
                                                } else {
                                                    main_menu.this.SavePreferences("turbo_tps", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 38;
                                                return;
                                            case 38:
                                                String turbo_rpm1 = data.trim();
                                                if (main_menu.isNumeric(turbo_rpm1)) {
                                                    main_menu.this.SavePreferences("turbo_rpm", turbo_rpm1);
                                                } else {
                                                    main_menu.this.SavePreferences("turbo_rpm", "1000");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 39;
                                                return;
                                            case 39:
                                                String imob_en1 = data.trim();
                                                if (main_menu.isNumeric(imob_en1)) {
                                                    main_menu.this.SavePreferences("imob_en", imob_en1);
                                                } else {
                                                    main_menu.this.SavePreferences("imob_en", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 40;
                                                return;
                                            case 40:
                                                main_menu.this.SavePreferences("key1_h", data);
                                                main_menu.this.posisi = 41;
                                                return;
                                            case 41:
                                                main_menu.this.SavePreferences("key1_l", data);
                                                main_menu.this.posisi = 42;
                                                return;
                                            case 42:
                                                main_menu.this.SavePreferences("key2_h", data);
                                                main_menu.this.posisi = 43;
                                                return;
                                            case 43:
                                                main_menu.this.SavePreferences("key2_l", data);
                                                main_menu.this.posisi = 44;
                                                return;
                                            case 44:
                                                main_menu.this.SavePreferences("key3_h", data);
                                                main_menu.this.posisi = 45;
                                                return;
                                            case 45:
                                                main_menu.this.SavePreferences("key3_l", data);
                                                main_menu.this.posisi = 46;
                                                return;
                                            case 46:
                                                main_menu.this.SavePreferences("ecu_cyl", data);
                                                main_menu.this.posisi = 47;
                                                return;
                                            case 47:
                                                main_menu.this.SavePreferences("secure", data);
                                                main_menu.this.posisi = 48;
                                                return;
                                            case 48:
                                                String jf11 = data.trim();
                                                if (main_menu.isNumeric(jf11)) {
                                                    main_menu.this.SavePreferences("jf1", jf11);
                                                } else {
                                                    main_menu.this.SavePreferences("jf1", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 49;
                                                return;
                                            case 49:
                                                String jf21 = data.trim();
                                                if (main_menu.isNumeric(jf21)) {
                                                    main_menu.this.SavePreferences("jf2", jf21);
                                                } else {
                                                    main_menu.this.SavePreferences("jf2", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 50;
                                                return;
                                            case 50:
                                                String jf31 = data.trim();
                                                if (main_menu.isNumeric(jf31)) {
                                                    main_menu.this.SavePreferences("jf3", jf31);
                                                } else {
                                                    main_menu.this.SavePreferences("jf3", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 51;
                                                return;
                                            case 51:
                                                String jf41 = data.trim();
                                                if (main_menu.isNumeric(jf41)) {
                                                    main_menu.this.SavePreferences("jf4", jf41);
                                                } else {
                                                    main_menu.this.SavePreferences("jf4", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 52;
                                                return;
                                            case 52:
                                                String t_rate11 = data.trim();
                                                if (main_menu.isNumeric(t_rate11)) {
                                                    main_menu.this.SavePreferences("t_rate1", t_rate11);
                                                } else {
                                                    main_menu.this.SavePreferences("t_rate1", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 53;
                                                return;
                                            case 53:
                                                String t_rate21 = data.trim();
                                                if (main_menu.isNumeric(t_rate21)) {
                                                    main_menu.this.SavePreferences("t_rate2", t_rate21);
                                                } else {
                                                    main_menu.this.SavePreferences("t_rate2", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 54;
                                                return;
                                            case 54:
                                                String t_rate31 = data.trim();
                                                if (main_menu.isNumeric(t_rate31)) {
                                                    main_menu.this.SavePreferences("t_rate3", t_rate31);
                                                } else {
                                                    main_menu.this.SavePreferences("t_rate3", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 55;
                                                return;
                                            case 55:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.SavePreferences("ecu_model", data);
                                                main_menu.this.posisi = 56;
                                                return;
                                            case 56:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.SavePreferences("key4_h", data);
                                                main_menu.this.posisi = 57;
                                                return;
                                            case 57:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.SavePreferences("key4_l", data);
                                                main_menu.this.posisi = 58;
                                                return;
                                            case 58:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.SavePreferences("key5_h", data);
                                                main_menu.this.posisi = 59;
                                                return;
                                            case 59:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.SavePreferences("key5_l", data);
                                                main_menu.this.posisi = 60;
                                                return;
                                            case 60:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.posisi = 61;
                                                return;
                                            case 61:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.posisi = 62;
                                                return;
                                            case 62:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.posisi = 63;
                                                return;
                                            case 63:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.posisi = 64;
                                                return;
                                            case 64:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.posisi = 65;
                                                return;
                                            case 65:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.posisi = 66;
                                                return;
                                            case 66:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.posisi = 67;
                                                return;
                                            case 67:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.posisi = 68;
                                                return;
                                            case 68:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                String suhu_fan1 = data.trim();
                                                if (main_menu.isNumeric(suhu_fan1)) {
                                                    main_menu.this.SavePreferences("suhu_fan", suhu_fan1);
                                                } else {
                                                    main_menu.this.SavePreferences("suhu_fan", "600");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 69;
                                                return;
                                            case 69:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.posisi = 70;
                                                return;
                                            case 70:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                main_menu.this.posisi = 71;
                                                return;
                                            case 71:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                String protect_enable1 = data.trim();
                                                if (main_menu.isNumeric(protect_enable1)) {
                                                    main_menu.this.SavePreferences("protect_enable", protect_enable1);
                                                } else {
                                                    main_menu.this.SavePreferences("protect_enable", "0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 72;
                                                return;
                                            case 72:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                String protect_temp1 = data.trim();
                                                if (main_menu.isNumeric(protect_temp1)) {
                                                    main_menu.this.SavePreferences("protect_temp", protect_temp1);
                                                } else {
                                                    main_menu.this.SavePreferences("protect_temp", "60.0");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 73;
                                                return;
                                            case 73:
                                                if (data.equals("9616")) {
                                                    main_menu.this.model = "";
                                                    main_menu.this.posisi = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                                                    return;
                                                }
                                                String protect_limit1 = data.trim();
                                                if (main_menu.isNumeric(protect_limit1)) {
                                                    main_menu.this.SavePreferences("protect_limit", protect_limit1);
                                                } else {
                                                    main_menu.this.SavePreferences("protect_limit", "5000");
                                                    StaticClass.checkVar = false;
                                                }
                                                main_menu.this.posisi = 0;
                                                return;
                                            case ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION:
                                                main_menu.this.model = data + "\r\n";
                                                main_menu.this.SavePreferences("NamaEcu", main_menu.this.model);
                                                main_menu.this.posisi = 201;
                                                return;
                                            case 201:
                                                main_menu.this.model += data + "\r\n";
                                                main_menu.this.posisi = 202;
                                                return;
                                            case 202:
                                                main_menu.this.model += data.replaceAll("\\r", "");
                                                String ecu_digunakan1 = main_menu.this.model;
                                                main_menu.this.SavePreferences("ecu_digunakan", ecu_digunakan1);
                                                Message message23 = Message.obtain();
                                                message23.obj = ecu_digunakan1;
                                                message23.setTarget(main_menu.this.mHandler);
                                                message23.sendToTarget();
                                                if (main_menu.this.tss1.equals("1")) {
                                                    main_menu.this.nilai_tss = 1;
                                                } else if (main_menu.this.tss1.equals("2")) {
                                                    main_menu.this.nilai_tss = 2;
                                                }
                                                main_menu.this.tps_terima = 0;
                                                main_menu.this.posisi = 0;
                                                main_menu.this.cek = 0;
                                                MappingHandle.list_fuel.clear();
                                                if (main_menu.this.nilai_tss == 1) {
                                                    main_menu.this.terima_fuel(main_menu.this.f_c11);
                                                    return;
                                                } else if (main_menu.this.nilai_tss == 2) {
                                                    main_menu.this.terima_fuel(main_menu.this.f_c21);
                                                    return;
                                                } else {
                                                    return;
                                                }
                                            default:
                                                return;
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr = this.readBuffer;
                                int i2 = this.readBufferPosition;
                                this.readBufferPosition = i2 + 1;
                                bArr[i2] = b;
                            }
                        } else if (this.cek == 1) {
                            if (b == 59 || b == 10) {
                                byte[] encodedBytes2 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes2, 0, encodedBytes2.length);
                                final String data2 = new String(encodedBytes2, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (data2.equals("9602")) {
                                            main_menu.this.posisi_fuel = 0;
                                        } else if (!data2.equals("\r")) {
                                            MappingHandle.list_fuel.add(data2.replace("\r", "").replace("\n", "").trim());
                                            main_menu.this.posisi_fuel++;
                                            if (main_menu.this.posisi_fuel > 60) {
                                                main_menu.this.tps_terima++;
                                                if (main_menu.this.tps_terima >= 21) {
                                                    main_menu.this.tps_terima = 0;
                                                    main_menu.this.cek = 2;
                                                    MappingHandle.list_base_map.clear();
                                                    if (main_menu.this.nilai_tss == 1) {
                                                        main_menu.this.terima_base_map(main_menu.this.b_c11);
                                                    } else if (main_menu.this.nilai_tss == 2) {
                                                        main_menu.this.terima_base_map(main_menu.this.b_c21);
                                                    }
                                                } else if (main_menu.this.nilai_tss == 1) {
                                                    main_menu.this.terima_fuel(main_menu.this.f_c11);
                                                } else if (main_menu.this.nilai_tss == 2) {
                                                    main_menu.this.terima_fuel(main_menu.this.f_c21);
                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr2 = this.readBuffer;
                                int i3 = this.readBufferPosition;
                                this.readBufferPosition = i3 + 1;
                                bArr2[i3] = b;
                            }
                        } else if (this.cek == 2) {
                            if (b == 59 || b == 10) {
                                byte[] encodedBytes3 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes3, 0, encodedBytes3.length);
                                final String data3 = new String(encodedBytes3, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (data3.equals("9601")) {
                                            main_menu.this.posisi_base_map = 0;
                                        } else if (!data3.equals("\r")) {
                                            MappingHandle.list_base_map.add(data3.replace("\r", "").replace("\n", "").trim());
                                            main_menu.this.posisi_base_map++;
                                            if (main_menu.this.posisi_base_map > 60) {
                                                main_menu.this.tps_terima++;
                                                if (main_menu.this.tps_terima >= 21) {
                                                    try {
                                                        MappingHandle.list_pattern_current_file.clear();
                                                        ArrayList<String> bmTotSementara = new ArrayList<>();
                                                        for (int i = 0; i < MappingHandle.list_fuel.size(); i++) {
                                                            Double Fuel = Double.valueOf(MappingHandle.list_fuel.get(i));
                                                            Double Bm = Double.valueOf(MappingHandle.list_base_map.get(i));
                                                            Double BmTot = Double.valueOf(Bm.doubleValue() + ((Bm.doubleValue() * Fuel.doubleValue()) / 100.0d));
                                                            if (BmTot.doubleValue() > 20.0d) {
                                                                BmTot = Double.valueOf(20.0d);
                                                            }
                                                            if (BmTot.doubleValue() < 0.0d) {
                                                                BmTot = Double.valueOf(0.0d);
                                                            }
                                                            bmTotSementara.add(String.format("%.2f", new Object[]{BmTot}).replace(",", "."));
                                                        }
                                                        for (int i2 = 19; i2 > 0; i2--) {
                                                            for (int j = 0; j < 61; j++) {
                                                                int posisi1 = j + 1220;
                                                                double seratus = Double.valueOf(bmTotSementara.get(posisi1)).doubleValue();
                                                                MappingHandle.list_pattern_current_file.add(String.format("%.2f", new Object[]{Double.valueOf(((Double.valueOf(bmTotSementara.get(posisi1 - (i2 * 61))).doubleValue() - seratus) * 100.0d) / seratus)}).replace(",", "."));
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        Toast.makeText(main_menu.this, "Current Pattern \n" + e.toString(), 0).show();
                                                    }
                                                    main_menu.this.tps_terima = 0;
                                                    main_menu.this.cek = 3;
                                                    MappingHandle.list_injector.clear();
                                                    if (main_menu.this.nilai_tss == 1) {
                                                        main_menu.this.terima_injector(main_menu.this.it_c11);
                                                    } else if (main_menu.this.nilai_tss == 2) {
                                                        main_menu.this.terima_injector(main_menu.this.it_c21);
                                                    }
                                                } else if (main_menu.this.nilai_tss == 1) {
                                                    main_menu.this.terima_base_map(main_menu.this.b_c11);
                                                } else if (main_menu.this.nilai_tss == 2) {
                                                    main_menu.this.terima_base_map(main_menu.this.b_c21);
                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr3 = this.readBuffer;
                                int i4 = this.readBufferPosition;
                                this.readBufferPosition = i4 + 1;
                                bArr3[i4] = b;
                            }
                        } else if (this.cek == 3) {
                            if (b == 59 || b == 10) {
                                byte[] encodedBytes4 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes4, 0, encodedBytes4.length);
                                final String data4 = new String(encodedBytes4, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (data4.equals("9603")) {
                                            main_menu.this.posisi_injector = 0;
                                        } else if (!data4.equals("\r")) {
                                            MappingHandle.list_injector.add(data4.replace("\r", "").replace("\n", "").trim());
                                            main_menu.this.posisi_injector++;
                                            if (main_menu.this.posisi_injector > 60) {
                                                main_menu.this.tps_terima++;
                                                if (main_menu.this.tps_terima >= 21) {
                                                    main_menu.this.tps_terima = 0;
                                                    main_menu.this.cek = 4;
                                                    MappingHandle.list_ignition.clear();
                                                    if (main_menu.this.nilai_tss == 1) {
                                                        main_menu.this.terima_ignition(main_menu.this.ig_c11);
                                                    } else if (main_menu.this.nilai_tss == 2) {
                                                        main_menu.this.terima_ignition(main_menu.this.ig_c21);
                                                    }
                                                } else if (main_menu.this.nilai_tss == 1) {
                                                    main_menu.this.terima_injector(main_menu.this.it_c11);
                                                } else if (main_menu.this.nilai_tss == 2) {
                                                    main_menu.this.terima_injector(main_menu.this.it_c21);
                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr4 = this.readBuffer;
                                int i5 = this.readBufferPosition;
                                this.readBufferPosition = i5 + 1;
                                bArr4[i5] = b;
                            }
                        } else if (this.cek == 4) {
                            if (b == 59 || b == 10) {
                                byte[] encodedBytes5 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes5, 0, encodedBytes5.length);
                                final String data5 = new String(encodedBytes5, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (data5.equals("9605")) {
                                            main_menu.this.posisi_ignition = 0;
                                        } else if (!data5.equals("\r")) {
                                            MappingHandle.list_ignition.add(data5.replace("\r", "").replace("\n", "").trim());
                                            main_menu.this.posisi_ignition++;
                                            if (main_menu.this.posisi_ignition > 30) {
                                                main_menu.this.tps_terima++;
                                                if (main_menu.this.tps_terima >= 21) {
                                                    main_menu.this.tps_terima = 0;
                                                    main_menu.this.cek = 5;
                                                    if (main_menu.this.nilai_tss == 1) {
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
                                                    main_menu.this.terima_history();
                                                    main_menu.this.handler.post(main_menu.this.sendHistory);
                                                } else if (main_menu.this.nilai_tss == 1) {
                                                    main_menu.this.terima_ignition(main_menu.this.ig_c11);
                                                } else if (main_menu.this.nilai_tss == 2) {
                                                    main_menu.this.terima_ignition(main_menu.this.ig_c21);
                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr5 = this.readBuffer;
                                int i6 = this.readBufferPosition;
                                this.readBufferPosition = i6 + 1;
                                bArr5[i6] = b;
                            }
                        } else if (this.cek == 5) {
                            if (b == 59 || b == 10) {
                                byte[] encodedBytes6 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes6, 0, encodedBytes6.length);
                                final String data6 = new String(encodedBytes6, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (data6.equals("9610")) {
                                            main_menu.this.posisi_history = 0;
                                        } else if (!data6.equals("\r")) {
                                            MappingHandle.list_history.add(String.valueOf(Double.valueOf(data6.replace("\r", "").replace("\n", "").trim()).doubleValue() / 10.0d));
                                            main_menu.this.posisi_history++;
                                            if (main_menu.this.posisi_history > 60) {
                                                main_menu.this.tps_terima++;
                                                if (main_menu.this.tps_terima < 21) {
                                                    main_menu.this.handler.removeCallbacks(main_menu.this.sendHistory);
                                                    main_menu.this.terima_history();
                                                    return;
                                                }
                                                main_menu.this.resetConnection();
                                                if (main_menu.this.progressDialog.isShowing()) {
                                                    main_menu.this.progressDialog.dismiss();
                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr6 = this.readBuffer;
                                int i7 = this.readBufferPosition;
                                this.readBufferPosition = i7 + 1;
                                bArr6[i7] = b;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                this.stopWorker = true;
            }
            if (this.kirim_ecu_model.booleanValue()) {
                this.posisi = 0;
                this.kirim_ecu_model = false;
                kirim_lagi();
            }
        }
    }

    public void kirim_lagi() {
        if (this.nyambung == 1) {
            byte[] msgBuffer = ("1617" + "\r\n").getBytes();
            try {
                Thread.sleep(1000);
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(msgBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void kirim() {
        if (this.nyambung == 1) {
            this.cek = 0;
            byte[] msgBuffer = ("1607" + "\r\n").getBytes();
            MappingHandle.list_fuel.clear();
            MappingHandle.list_history.clear();
            MappingHandle.list_injector.clear();
            MappingHandle.list_ignition.clear();
            MappingHandle.list_base_map.clear();
            runOnUiThread(new Runnable() {
                public void run() {
                    main_menu.this.progressDialog = new ProgressDialog(main_menu.this);
                    main_menu.this.progressDialog.setMessage("Variabel...");
                    main_menu.this.progressDialog.setTitle("Get Map");
                    main_menu.this.progressDialog.setProgressStyle(0);
                    main_menu.this.progressDialog.show();
                    main_menu.this.progressDialog.setCancelable(true);
                }
            });
            try {
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(msgBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void terima_fuel(String posisi2) {
        this.cek = 1;
        byte[] msgAck = ("1602;" + posisi2 + ";" + this.tps_terima + "\r\n").getBytes();
        try {
            this.outputStream = this.socket.getOutputStream();
            this.outputStream.write(msgAck);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.tps_terima == 0) {
            this.progressDialog.setMessage("Fuel Correction..");
        }
    }

    public void terima_base_map(String posisi2) {
        this.cek = 2;
        byte[] msgAck = ("1601;" + posisi2 + ";" + this.tps_terima + "\r\n").getBytes();
        try {
            this.outputStream = this.socket.getOutputStream();
            this.outputStream.write(msgAck);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.tps_terima == 0) {
            this.progressDialog.setMessage("Base Map..");
        }
    }

    public void terima_injector(String posisi2) {
        this.cek = 3;
        byte[] msgAck = ("1603;" + posisi2 + ";" + this.tps_terima + "\r\n").getBytes();
        try {
            this.outputStream = this.socket.getOutputStream();
            this.outputStream.write(msgAck);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.tps_terima == 0) {
            this.progressDialog.setMessage("Injector Timing..");
        }
    }

    public void terima_ignition(String posisi2) {
        this.cek = 4;
        byte[] msgAck = ("1605;" + posisi2 + ";" + this.tps_terima + "\r\n").getBytes();
        try {
            this.outputStream = this.socket.getOutputStream();
            this.outputStream.write(msgAck);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.tps_terima == 0) {
            this.progressDialog.setMessage("Ignition Timing..");
        }
    }

    public void terima_history() {
        this.cek = 5;
        byte[] msgAck = ("1610;" + this.tps_terima + "\r\n").getBytes();
        try {
            this.outputStream = this.socket.getOutputStream();
            this.outputStream.write(msgAck);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.tps_terima == 0) {
            this.progressDialog.setMessage("History..");
        }
    }

    /* access modifiers changed from: private */
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /* access modifiers changed from: private */
    public void resetConnection() {
        if (this.nyambung == 1) {
            this.nyambung = 0;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            if (this.inputStream != null) {
                try {
                    this.inputStream.close();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), 1).show();
                }
                this.inputStream = null;
            }
            if (this.outputStream != null) {
                try {
                    this.outputStream.close();
                } catch (Exception e2) {
                    Toast.makeText(getApplicationContext(), e2.toString(), 1).show();
                }
                this.outputStream = null;
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (Exception e3) {
                    Toast.makeText(getApplicationContext(), e3.toString(), 1).show();
                }
                this.socket = null;
            }
        }
    }

    /* access modifiers changed from: private */
    public void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void onStart() {
        super.onStart();
    }

    public void onPause() {
        super.onPause();
    }

    class MyBroadCastReceiver1 extends BroadcastReceiver {
        MyBroadCastReceiver1() {
        }

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r9, android.content.Intent r10) {
            /*
                r8 = this;
                r6 = 1
                r4 = 0
                java.lang.String r5 = "key"
                java.lang.String r0 = r10.getStringExtra(r5)     // Catch:{ Exception -> 0x009c }
                r5 = -1
                int r7 = r0.hashCode()     // Catch:{ Exception -> 0x009c }
                switch(r7) {
                    case -1187145697: goto L_0x0015;
                    case 95356359: goto L_0x001e;
                    case 95356360: goto L_0x0028;
                    case 95356361: goto L_0x0032;
                    case 95356362: goto L_0x003c;
                    case 95356363: goto L_0x0046;
                    case 95356364: goto L_0x0050;
                    case 95356365: goto L_0x005a;
                    case 95356366: goto L_0x0064;
                    case 95356367: goto L_0x006f;
                    case 108388975: goto L_0x007a;
                    case 1721867165: goto L_0x0085;
                    default: goto L_0x0010;
                }     // Catch:{ Exception -> 0x009c }
            L_0x0010:
                r4 = r5
            L_0x0011:
                switch(r4) {
                    case 0: goto L_0x0090;
                    case 1: goto L_0x00a2;
                    case 2: goto L_0x00b8;
                    case 3: goto L_0x00cb;
                    case 4: goto L_0x00f2;
                    case 5: goto L_0x010b;
                    case 6: goto L_0x0116;
                    case 7: goto L_0x0121;
                    case 8: goto L_0x012c;
                    case 9: goto L_0x0137;
                    case 10: goto L_0x014a;
                    case 11: goto L_0x0160;
                    default: goto L_0x0014;
                }     // Catch:{ Exception -> 0x009c }
            L_0x0014:
                return
            L_0x0015:
                java.lang.String r6 = "DataCek"
                boolean r6 = r0.equals(r6)     // Catch:{ Exception -> 0x009c }
                if (r6 == 0) goto L_0x0010
                goto L_0x0011
            L_0x001e:
                java.lang.String r4 = "data1"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x0010
                r4 = r6
                goto L_0x0011
            L_0x0028:
                java.lang.String r4 = "data2"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x0010
                r4 = 2
                goto L_0x0011
            L_0x0032:
                java.lang.String r4 = "data3"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x0010
                r4 = 3
                goto L_0x0011
            L_0x003c:
                java.lang.String r4 = "data4"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x0010
                r4 = 4
                goto L_0x0011
            L_0x0046:
                java.lang.String r4 = "data5"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x0010
                r4 = 5
                goto L_0x0011
            L_0x0050:
                java.lang.String r4 = "data6"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x0010
                r4 = 6
                goto L_0x0011
            L_0x005a:
                java.lang.String r4 = "data7"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x0010
                r4 = 7
                goto L_0x0011
            L_0x0064:
                java.lang.String r4 = "data8"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x0010
                r4 = 8
                goto L_0x0011
            L_0x006f:
                java.lang.String r4 = "data9"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x0010
                r4 = 9
                goto L_0x0011
            L_0x007a:
                java.lang.String r4 = "recon"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x0010
                r4 = 10
                goto L_0x0011
            L_0x0085:
                java.lang.String r4 = "doVarAwal"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x0010
                r4 = 11
                goto L_0x0011
            L_0x0090:
                java.lang.String r4 = juken.android.com.juken_5.StaticClass.GlobalData     // Catch:{ Exception -> 0x009c }
                r5 = 0
                android.widget.Toast r4 = android.widget.Toast.makeText(r9, r4, r5)     // Catch:{ Exception -> 0x009c }
                r4.show()     // Catch:{ Exception -> 0x009c }
                goto L_0x0014
            L_0x009c:
                r1 = move-exception
                r1.printStackTrace()
                goto L_0x0014
            L_0x00a2:
                android.os.Message r2 = android.os.Message.obtain()     // Catch:{ Exception -> 0x009c }
                java.lang.String r4 = juken.android.com.juken_5.StaticClass.EcuDigunakan     // Catch:{ Exception -> 0x009c }
                r2.obj = r4     // Catch:{ Exception -> 0x009c }
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.os.Handler r4 = r4.mHandler     // Catch:{ Exception -> 0x009c }
                r2.setTarget(r4)     // Catch:{ Exception -> 0x009c }
                r2.sendToTarget()     // Catch:{ Exception -> 0x009c }
                goto L_0x0014
            L_0x00b8:
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.app.ProgressDialog r4 = r4.progressDialog     // Catch:{ Exception -> 0x009c }
                boolean r4 = r4.isShowing()     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x0014
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.app.ProgressDialog r4 = r4.progressDialog     // Catch:{ Exception -> 0x009c }
                r4.dismiss()     // Catch:{ Exception -> 0x009c }
                goto L_0x0014
            L_0x00cb:
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.app.ProgressDialog r4 = r4.progressDialog     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x00e6
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.app.ProgressDialog r4 = r4.progressDialog     // Catch:{ Exception -> 0x009c }
                boolean r4 = r4.isShowing()     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x00e6
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.app.ProgressDialog r4 = r4.progressDialog     // Catch:{ Exception -> 0x009c }
                java.lang.String r5 = "Variabel..."
                r4.setMessage(r5)     // Catch:{ Exception -> 0x009c }
                goto L_0x0014
            L_0x00e6:
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                juken.android.com.juken_5.main_menu$MyBroadCastReceiver1$1 r5 = new juken.android.com.juken_5.main_menu$MyBroadCastReceiver1$1     // Catch:{ Exception -> 0x009c }
                r5.<init>()     // Catch:{ Exception -> 0x009c }
                r4.runOnUiThread(r5)     // Catch:{ Exception -> 0x009c }
                goto L_0x0014
            L_0x00f2:
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.app.ProgressDialog r4 = r4.progressDialog     // Catch:{ Exception -> 0x009c }
                java.lang.String r5 = "Fuel Correction.."
                r4.setMessage(r5)     // Catch:{ Exception -> 0x009c }
                boolean r4 = juken.android.com.juken_5.StaticClass.checkVar     // Catch:{ Exception -> 0x009c }
                if (r4 != 0) goto L_0x0014
                java.lang.String r4 = "ERR : INVALID VARIABLE DATA"
                r5 = 1
                android.widget.Toast r4 = android.widget.Toast.makeText(r9, r4, r5)     // Catch:{ Exception -> 0x009c }
                r4.show()     // Catch:{ Exception -> 0x009c }
                goto L_0x0014
            L_0x010b:
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.app.ProgressDialog r4 = r4.progressDialog     // Catch:{ Exception -> 0x009c }
                java.lang.String r5 = "Base Map.."
                r4.setMessage(r5)     // Catch:{ Exception -> 0x009c }
                goto L_0x0014
            L_0x0116:
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.app.ProgressDialog r4 = r4.progressDialog     // Catch:{ Exception -> 0x009c }
                java.lang.String r5 = "Injector Timing.."
                r4.setMessage(r5)     // Catch:{ Exception -> 0x009c }
                goto L_0x0014
            L_0x0121:
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.app.ProgressDialog r4 = r4.progressDialog     // Catch:{ Exception -> 0x009c }
                java.lang.String r5 = "Ignition Timing.."
                r4.setMessage(r5)     // Catch:{ Exception -> 0x009c }
                goto L_0x0014
            L_0x012c:
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.app.ProgressDialog r4 = r4.progressDialog     // Catch:{ Exception -> 0x009c }
                java.lang.String r5 = "History.."
                r4.setMessage(r5)     // Catch:{ Exception -> 0x009c }
                goto L_0x0014
            L_0x0137:
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.app.ProgressDialog r4 = r4.progressDialog     // Catch:{ Exception -> 0x009c }
                boolean r4 = r4.isShowing()     // Catch:{ Exception -> 0x009c }
                if (r4 == 0) goto L_0x0014
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.app.ProgressDialog r4 = r4.progressDialog     // Catch:{ Exception -> 0x009c }
                r4.dismiss()     // Catch:{ Exception -> 0x009c }
                goto L_0x0014
            L_0x014a:
                android.os.Message r3 = android.os.Message.obtain()     // Catch:{ Exception -> 0x009c }
                java.lang.String r4 = "Connection Close, Reconnect in Progress"
                r3.obj = r4     // Catch:{ Exception -> 0x009c }
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.os.Handler r4 = r4.mHandler     // Catch:{ Exception -> 0x009c }
                r3.setTarget(r4)     // Catch:{ Exception -> 0x009c }
                r3.sendToTarget()     // Catch:{ Exception -> 0x009c }
                goto L_0x0014
            L_0x0160:
                java.lang.String r4 = "Do"
                java.lang.String r5 = "Var Awal"
                android.util.Log.d(r4, r5)     // Catch:{ Exception -> 0x009c }
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.content.Intent r4 = r4.service     // Catch:{ Exception -> 0x009c }
                java.lang.String r5 = "send"
                java.lang.String r6 = "Var Awal"
                r4.putExtra(r5, r6)     // Catch:{ Exception -> 0x009c }
                juken.android.com.juken_5.main_menu r4 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                juken.android.com.juken_5.main_menu r5 = juken.android.com.juken_5.main_menu.this     // Catch:{ Exception -> 0x009c }
                android.content.Intent r5 = r5.service     // Catch:{ Exception -> 0x009c }
                r4.startService(r5)     // Catch:{ Exception -> 0x009c }
                goto L_0x0014
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.main_menu.MyBroadCastReceiver1.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    private void registerMyReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiService.BROADCAST_ACTION);
            registerReceiver(this.myBroadCastReceiver, intentFilter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
