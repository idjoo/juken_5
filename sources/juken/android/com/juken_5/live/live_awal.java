package juken.android.com.juken_5.live;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.anastr.speedviewlib.Gauge;
import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.github.anastr.speedviewlib.ProgressiveGauge;
import com.github.anastr.speedviewlib.util.OnSpeedChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import juken.android.com.juken_5.ExpandableListAdapterLive;
import juken.android.com.juken_5.ExpandedMenuModel;
import juken.android.com.juken_5.MappingHandle;
import juken.android.com.juken_5.R;
import juken.android.com.juken_5.StaticClass;
import juken.android.com.juken_5.WifiService;
import juken.android.com.juken_5.deceleration_fuel;
import juken.android.com.juken_5.e_map;
import juken.android.com.juken_5.ecu_limiter;
import juken.android.com.juken_5.live.fuel_live.fuel_correction_live;
import juken.android.com.juken_5.live.ig_live.ignition_timing_live;
import juken.android.com.juken_5.rpm_idle;
import juken.android.com.juken_5.warming_up;

public class live_awal extends AppCompatActivity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private static final DecimalFormat oneDecimal = new DecimalFormat("#,##0.0");
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            live_awal.this.listen();
        }
    };
    TextView afr;
    TextView base;
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    int cek = 0;
    TextView core;
    private DrawerLayout drawerLayout;
    ExpandableListView expandableList;
    TextView fuel;
    ProgressiveGauge gauge_afr;
    PointerSpeedometer gauge_rpm;
    PointerSpeedometer gauge_tps;
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    int hitung_tps = 0;
    TextView iat;
    TextView ignition_t;
    TextView injector_t;
    private InputStream inputStream = null;
    Boolean koneksi = false;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    List<ExpandedMenuModel> listDataHeader;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            Toast.makeText(live_awal.this.getApplicationContext(), (String) message.obj, 1).show();
        }
    };
    ExpandableListAdapterLive mMenuAdapter;
    TextView map;
    String masuk_ke = "";
    MyBroadCastReceiver myBroadCastReceiver;
    int nyambung = 1;
    private OutputStream outputStream = null;
    Boolean pertama_cek = true;
    int posisi = 0;
    byte[] readBuffer;
    int readBufferPosition;
    int rpm_mentah = 1000;
    /* access modifiers changed from: private */
    public final Runnable sendData = new Runnable() {
        public void run() {
            try {
                if (live_awal.this.sendStop < 10) {
                    live_awal.this.kirim_stop();
                    live_awal.this.sendStop++;
                } else {
                    live_awal.this.sendStop = 0;
                    live_awal.this.handler.removeCallbacks(live_awal.this.sendData);
                }
                live_awal.this.handler.postDelayed(this, 200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    int sendStop = 0;
    Intent service;
    private BluetoothSocket socket = null;
    volatile boolean stopWorker;
    Boolean stop_destroy = false;
    TextView temp;
    TextView text_rpm;
    private Toolbar toolbar;
    int tps_mentah = 0;
    TextView vbat;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.live_awal);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.koneksi = StaticClass.koneksi;
        } else if (this.bluetooth.isEnabled()) {
            this.koneksi = true;
        } else {
            this.koneksi = false;
        }
        this.vbat = (TextView) findViewById(R.id.textBAT);
        this.core = (TextView) findViewById(R.id.textCore);
        this.temp = (TextView) findViewById(R.id.textEOT);
        this.afr = (TextView) findViewById(R.id.textAFR);
        this.base = (TextView) findViewById(R.id.textBM);
        this.injector_t = (TextView) findViewById(R.id.textIT);
        this.map = (TextView) findViewById(R.id.textMAP);
        this.iat = (TextView) findViewById(R.id.textIAT);
        this.fuel = (TextView) findViewById(R.id.textF);
        this.ignition_t = (TextView) findViewById(R.id.textIG);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        this.mMenuAdapter = new ExpandableListAdapterLive(this, this.listDataHeader, this.listDataChild, this.expandableList);
        this.expandableList.setAdapter(this.mMenuAdapter);
        this.expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        live_awal.this.masuk_ke = "ecu limiter";
                        live_awal.this.handler.post(live_awal.this.sendData);
                        return false;
                    case 1:
                        live_awal.this.masuk_ke = "warming up";
                        live_awal.this.handler.post(live_awal.this.sendData);
                        return false;
                    case 2:
                        live_awal.this.masuk_ke = "rpm_idle";
                        live_awal.this.handler.post(live_awal.this.sendData);
                        return false;
                    case 3:
                        live_awal.this.masuk_ke = "deceleration_fuel";
                        live_awal.this.handler.post(live_awal.this.sendData);
                        return false;
                    case 4:
                        live_awal.this.masuk_ke = "emap";
                        live_awal.this.handler.post(live_awal.this.sendData);
                        return false;
                    case 5:
                        live_awal.this.masuk_ke = "emap";
                        live_awal.this.handler.post(live_awal.this.sendData);
                        return false;
                    default:
                        return false;
                }
            }
        });
        ((Button) findViewById(R.id.button_execute)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClassExecute.bisa_execute) {
                    StaticClassExecute.bisa_execute = false;
                    MappingHandle.list_fuel_ganti.clear();
                    MappingHandle.list_fuel_live_ganti.clear();
                    MappingHandle.list_ignition_ganti.clear();
                    MappingHandle.list_ignition_live_ganti.clear();
                    live_awal.this.execute();
                    return;
                }
                Toast.makeText(live_awal.this.getApplicationContext(), "Update data first", 0).show();
            }
        });
        ((TextView) findViewById(R.id.textIG)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    live_awal.this.resetConnection();
                }
                live_awal.this.finish();
                StaticClass.Live = true;
                live_awal.this.startActivity(new Intent(view.getContext(), ignition_timing_live.class));
            }
        });
        ((TextView) findViewById(R.id.textIG1)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    live_awal.this.resetConnection();
                }
                live_awal.this.finish();
                StaticClass.Live = true;
                live_awal.this.startActivity(new Intent(view.getContext(), ignition_timing_live.class));
            }
        });
        this.fuel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    live_awal.this.resetConnection();
                }
                live_awal.this.finish();
                StaticClass.Live = true;
                live_awal.this.startActivity(new Intent(view.getContext(), fuel_correction_live.class));
            }
        });
        ((TextView) findViewById(R.id.textF1)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    live_awal.this.resetConnection();
                }
                live_awal.this.finish();
                StaticClass.Live = true;
                live_awal.this.startActivity(new Intent(view.getContext(), fuel_correction_live.class));
            }
        });
        this.text_rpm = (TextView) findViewById(R.id.text_rpm);
        this.gauge_rpm = (PointerSpeedometer) findViewById(R.id.gauge_rpm);
        this.gauge_rpm.setTicks(Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(2.0f), Float.valueOf(3.0f), Float.valueOf(4.0f), Float.valueOf(5.0f), Float.valueOf(6.0f), Float.valueOf(7.0f), Float.valueOf(8.0f), Float.valueOf(9.0f), Float.valueOf(10.0f), Float.valueOf(11.0f), Float.valueOf(12.0f), Float.valueOf(13.0f), Float.valueOf(14.0f), Float.valueOf(15.0f), Float.valueOf(16.0f));
        this.gauge_rpm.speedTo(0.0f);
        this.gauge_rpm.setOnSpeedChangeListener(new OnSpeedChangeListener() {
            public void onSpeedChange(Gauge gauge, boolean isSpeedUp, boolean isByTremble) {
                int speed = (int) gauge.getCurrentSpeed();
                if (speed < 8) {
                    live_awal.this.gauge_rpm.setSpeedometerColor(Color.argb(255, 247, 228, 18));
                } else if (speed >= 8 && speed < 12) {
                    live_awal.this.gauge_rpm.setSpeedometerColor(Color.argb(255, 247, 152, 18));
                } else if (speed >= 12) {
                    live_awal.this.gauge_rpm.setSpeedometerColor(Color.argb(255, 247, 48, 16));
                }
            }
        });
        final TextView text_tps = (TextView) findViewById(R.id.text_tps);
        this.gauge_tps = (PointerSpeedometer) findViewById(R.id.gauge_tps);
        this.gauge_tps.setTicks(Float.valueOf(0.0f), Float.valueOf(10.0f), Float.valueOf(20.0f), Float.valueOf(30.0f), Float.valueOf(40.0f), Float.valueOf(50.0f), Float.valueOf(60.0f), Float.valueOf(70.0f), Float.valueOf(80.0f), Float.valueOf(90.0f), Float.valueOf(100.0f));
        this.gauge_tps.speedTo(0.0f);
        this.gauge_tps.setOnSpeedChangeListener(new OnSpeedChangeListener() {
            public void onSpeedChange(Gauge gauge, boolean isSpeedUp, boolean isByTremble) {
                int speed = (int) gauge.getCurrentSpeed();
                text_tps.setText(String.valueOf(speed));
                if (speed < 65) {
                    live_awal.this.gauge_tps.setSpeedometerColor(Color.argb(255, 247, 228, 18));
                } else if (speed >= 65 && speed < 85) {
                    live_awal.this.gauge_tps.setSpeedometerColor(Color.argb(255, 247, 152, 18));
                } else if (speed >= 85) {
                    live_awal.this.gauge_tps.setSpeedometerColor(Color.argb(255, 247, 48, 16));
                }
            }
        });
        this.gauge_afr = (ProgressiveGauge) findViewById(R.id.gauge_afr);
        this.gauge_afr.speedTo(0.0f);
        this.gauge_afr.setOnSpeedChangeListener(new OnSpeedChangeListener() {
            public void onSpeedChange(Gauge gauge, boolean isSpeedUp, boolean isByTremble) {
                int speed = (int) (gauge.getCurrentSpeed() * 10.0f);
                if (speed < 120) {
                    live_awal.this.gauge_afr.setSpeedometerColor(-16776961);
                } else if (speed >= 120 && speed < 130) {
                    live_awal.this.gauge_afr.setSpeedometerColor(-16711936);
                } else if (speed >= 130 && speed < 140) {
                    live_awal.this.gauge_afr.setSpeedometerColor(InputDeviceCompat.SOURCE_ANY);
                } else if (speed > 140) {
                    live_awal.this.gauge_afr.setSpeedometerColor(SupportMenu.CATEGORY_MASK);
                }
            }
        });
        StaticClass.Live = true;
    }

    private void prepareListData() {
        this.listDataHeader = new ArrayList();
        this.listDataChild = new HashMap<>();
        ExpandedMenuModel item1 = new ExpandedMenuModel();
        item1.setIconName("ECU Limiter");
        item1.setIconImg(R.drawable.arrow_forward);
        this.listDataHeader.add(item1);
        ExpandedMenuModel item2 = new ExpandedMenuModel();
        item2.setIconName("Warming Up");
        item2.setIconImg(R.drawable.arrow_forward);
        this.listDataHeader.add(item2);
        ExpandedMenuModel item3 = new ExpandedMenuModel();
        item3.setIconName("Idle Fuel");
        item3.setIconImg(R.drawable.arrow_forward);
        this.listDataHeader.add(item3);
        ExpandedMenuModel item4 = new ExpandedMenuModel();
        item4.setIconName("Deceleration Fuel");
        item4.setIconImg(R.drawable.arrow_forward);
        this.listDataHeader.add(item4);
        ExpandedMenuModel item5 = new ExpandedMenuModel();
        item5.setIconName("E-map");
        item5.setIconImg(R.drawable.arrow_forward);
        this.listDataHeader.add(item5);
        ExpandedMenuModel item6 = new ExpandedMenuModel();
        item6.setIconName("J.S.S");
        item6.setIconImg(R.drawable.arrow_forward);
        this.listDataHeader.add(item6);
        List<String> heading1 = new ArrayList<>();
        List<String> heading2 = new ArrayList<>();
        List<String> heading3 = new ArrayList<>();
        List<String> heading4 = new ArrayList<>();
        List<String> heading5 = new ArrayList<>();
        List<String> heading6 = new ArrayList<>();
        this.listDataChild.put(this.listDataHeader.get(0), heading1);
        this.listDataChild.put(this.listDataHeader.get(1), heading2);
        this.listDataChild.put(this.listDataHeader.get(2), heading3);
        this.listDataChild.put(this.listDataHeader.get(3), heading4);
        this.listDataChild.put(this.listDataHeader.get(4), heading5);
        this.listDataChild.put(this.listDataHeader.get(5), heading6);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        StaticClass.Live = true;
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
            kirim_open_live();
        } else if (StaticClass.TipeKoneksi.equals("bt")) {
            LoadPreferences();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            try {
                unregisterReceiver(this.myBroadCastReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else if (StaticClass.TipeKoneksi.equals("bt")) {
            try {
                if (this.nyambung == 1) {
                    resetConnection();
                    Thread.sleep(500);
                }
            } catch (Exception e2) {
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) <= 5 || keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        if (this.koneksi.booleanValue()) {
            this.sendStop = 0;
            StaticClass.Live = false;
            this.handler.post(this.sendData);
            this.stop_destroy = true;
            return;
        }
        finish();
    }

    private void LoadPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    live_awal.this.connect(perangkat);
                }
            }.start();
        }
    }

    /* access modifiers changed from: protected */
    public void connect(BluetoothDevice device) {
        try {
            this.socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            this.socket.connect();
            this._serverWorker.start();
            kirim_open_live();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void kirim_open_live() {
        this.cek = 1;
        byte[] msgBuffer = "160A\r\n".getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = "160A\r\n";
            this.service.putExtra("send", "GlobalData");
            startService(this.service);
        } else if (StaticClass.TipeKoneksi.equals("bt") && this.nyambung == 1) {
            try {
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(msgBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void kirim_stop() {
        this.cek = 2;
        byte[] msgBuffer = "160B\r\n".getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = "160B\r\n";
            this.service.putExtra("send", "GlobalData");
            startService(this.service);
        } else if (StaticClass.TipeKoneksi.equals("bt") && this.nyambung == 1) {
            try {
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(msgBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute() {
        this.cek = 3;
        byte[] msgKirim = "5009\r\n".getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = "5009\r\n";
            this.service.putExtra("send", "GlobalData");
            startService(this.service);
        } else if (StaticClass.TipeKoneksi.equals("bt") && this.nyambung == 1) {
            try {
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(msgKirim);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.hitung_tps == 0) {
            Toast.makeText(getApplicationContext(), "Send execute", 0).show();
        }
        this.hitung_tps++;
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
                }
                this.inputStream = null;
            }
            if (this.outputStream != null) {
                try {
                    this.outputStream.close();
                } catch (Exception e2) {
                }
                this.outputStream = null;
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (Exception e3) {
                }
                this.socket = null;
            }
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
                            if (b == 10 || b == 59) {
                                byte[] encodedBytes = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                final String data = new String(encodedBytes, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        switch (live_awal.this.posisi) {
                                            case 0:
                                                if (data.equals("A603") || data.equals("a603")) {
                                                    live_awal.this.posisi = 1;
                                                    return;
                                                } else {
                                                    live_awal.this.posisi = 0;
                                                    return;
                                                }
                                            case 1:
                                                live_awal.this.posisi = 2;
                                                Float nilaiTps = Float.valueOf(0.0f);
                                                try {
                                                    nilaiTps = Float.valueOf(data);
                                                } catch (Exception e) {
                                                }
                                                if (nilaiTps.floatValue() > 20.0f) {
                                                    nilaiTps = Float.valueOf(20.0f);
                                                } else if (nilaiTps.floatValue() < 0.0f) {
                                                    nilaiTps = Float.valueOf(0.0f);
                                                }
                                                Float gauge = Float.valueOf(0.0f);
                                                if (nilaiTps.floatValue() == 1.0f) {
                                                    gauge = Float.valueOf(5.0f);
                                                } else if (nilaiTps.floatValue() > 1.0f && nilaiTps.floatValue() < 20.0f) {
                                                    gauge = Float.valueOf((nilaiTps.floatValue() * 5.0f) - 5.0f);
                                                } else if (nilaiTps.floatValue() == 20.0f) {
                                                    gauge = Float.valueOf(100.0f);
                                                }
                                                live_awal.this.gauge_tps.speedTo(gauge.floatValue());
                                                try {
                                                    live_awal.this.tps_mentah = Math.round(gauge.floatValue());
                                                    return;
                                                } catch (Exception e2) {
                                                    live_awal.this.tps_mentah = 0;
                                                    return;
                                                }
                                            case 2:
                                                live_awal.this.posisi = 3;
                                                live_awal.this.vbat.setText(data);
                                                return;
                                            case 3:
                                                live_awal.this.posisi = 4;
                                                live_awal.this.core.setText(data);
                                                if (data.equals("1")) {
                                                    if (!MappingHandle.NamaFileBM.equals("Base Map - ECU Core 1") || !MappingHandle.NamaFileFuel.equals("Fuel Correction - ECU Core 1") || !MappingHandle.NamaFileIG.equals("Ignition Timing - ECU Core 1") || !MappingHandle.NamaFileIT.equals("Injector Timing - ECU Core 1")) {
                                                        if (live_awal.this.pertama_cek.booleanValue()) {
                                                            live_awal.this.pertama_cek = null;
                                                            Message message23 = Message.obtain();
                                                            message23.obj = live_awal.this.getString(R.string.LiveSalahCore);
                                                            message23.setTarget(live_awal.this.mHandler);
                                                            message23.sendToTarget();
                                                        }
                                                        live_awal.this.sendStop = 0;
                                                        live_awal.this.handler.post(live_awal.this.sendData);
                                                        live_awal.this.stop_destroy = 1;
                                                        return;
                                                    }
                                                    return;
                                                } else if (!data.equals("2")) {
                                                    return;
                                                } else {
                                                    if (!MappingHandle.NamaFileBM.equals("Base Map - ECU Core 2") || !MappingHandle.NamaFileFuel.equals("Fuel Correction - ECU Core 2") || !MappingHandle.NamaFileIG.equals("Ignition Timing - ECU Core 2") || !MappingHandle.NamaFileIT.equals("Injector Timing - ECU Core 2")) {
                                                        if (live_awal.this.pertama_cek.booleanValue()) {
                                                            live_awal.this.pertama_cek = null;
                                                            Message message232 = Message.obtain();
                                                            message232.obj = live_awal.this.getString(R.string.LiveSalahCore);
                                                            message232.setTarget(live_awal.this.mHandler);
                                                            message232.sendToTarget();
                                                        }
                                                        live_awal.this.sendStop = 0;
                                                        live_awal.this.handler.post(live_awal.this.sendData);
                                                        live_awal.this.stop_destroy = 1;
                                                        return;
                                                    }
                                                    return;
                                                }
                                            case 4:
                                                live_awal.this.posisi = 5;
                                                live_awal.this.text_rpm.setText(data);
                                                Float nilaiRpm = Float.valueOf(0.0f);
                                                try {
                                                    nilaiRpm = Float.valueOf(Float.valueOf(data).floatValue() / 1000.0f);
                                                } catch (Exception e3) {
                                                }
                                                if (nilaiRpm.floatValue() > 16.0f) {
                                                    nilaiRpm = Float.valueOf(16.0f);
                                                } else if (nilaiRpm.floatValue() < 0.0f) {
                                                    nilaiRpm = Float.valueOf(0.0f);
                                                }
                                                live_awal.this.gauge_rpm.speedTo(nilaiRpm.floatValue());
                                                try {
                                                    live_awal.this.rpm_mentah = Integer.valueOf(data).intValue();
                                                    return;
                                                } catch (Exception e4) {
                                                    live_awal.this.rpm_mentah = 1000;
                                                    return;
                                                }
                                            case 5:
                                                live_awal.this.posisi = 6;
                                                String tampil = "0";
                                                try {
                                                    tampil = String.format("%.1f", new Object[]{Double.valueOf(Double.parseDouble(data) / 10.0d)}).replace(",", ".");
                                                } catch (Exception e5) {
                                                }
                                                live_awal.this.temp.setText(tampil);
                                                return;
                                            case 6:
                                                live_awal.this.posisi = 7;
                                                int hitung_tps = 0;
                                                if (live_awal.this.tps_mentah == 0) {
                                                    hitung_tps = 0;
                                                } else if (live_awal.this.tps_mentah == 2) {
                                                    hitung_tps = 1;
                                                } else if (live_awal.this.tps_mentah == 100) {
                                                    hitung_tps = 20;
                                                } else if (live_awal.this.tps_mentah > 2 && live_awal.this.tps_mentah < 100) {
                                                    hitung_tps = (live_awal.this.tps_mentah / 5) + 1;
                                                }
                                                int posisiF = (hitung_tps * 61) + (((live_awal.this.rpm_mentah - live_awal.this.mod(live_awal.this.rpm_mentah, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION)) / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) - 4);
                                                if (posisiF < 0) {
                                                    posisiF = 0;
                                                } else if (posisiF > 1280) {
                                                    posisiF = 1280;
                                                }
                                                try {
                                                    live_awal.this.fuel.setText(MappingHandle.list_fuel_live.get(posisiF));
                                                    return;
                                                } catch (Exception e6) {
                                                    live_awal.this.fuel.setText(data);
                                                    return;
                                                }
                                            case 7:
                                                live_awal.this.posisi = 8;
                                                live_awal.this.afr.setText(data);
                                                Float nilaiAfr = Float.valueOf(9.0f);
                                                try {
                                                    nilaiAfr = Float.valueOf(data);
                                                } catch (Exception e7) {
                                                }
                                                if (nilaiAfr.floatValue() > 20.0f) {
                                                    nilaiAfr = Float.valueOf(20.0f);
                                                } else if (nilaiAfr.floatValue() < 9.0f) {
                                                    nilaiAfr = Float.valueOf(9.0f);
                                                }
                                                live_awal.this.gauge_afr.speedTo(nilaiAfr.floatValue());
                                                return;
                                            case 8:
                                                live_awal.this.posisi = 9;
                                                live_awal.this.base.setText(data);
                                                return;
                                            case 9:
                                                live_awal.this.posisi = 10;
                                                live_awal.this.injector_t.setText(data);
                                                return;
                                            case 10:
                                                live_awal.this.posisi = 11;
                                                live_awal.this.ignition_t.setText(String.valueOf(Double.valueOf(data).doubleValue() / 10.0d).replace(",", "."));
                                                return;
                                            case 11:
                                                live_awal.this.posisi = 12;
                                                int hasil = 0;
                                                try {
                                                    hasil = (int) live_awal.this.mapping_map((Double.valueOf(data).doubleValue() * 5.0d) / 1023.0d);
                                                } catch (Exception e8) {
                                                }
                                                live_awal.this.map.setText(String.valueOf(hasil));
                                                return;
                                            case 12:
                                                live_awal.this.posisi = 0;
                                                double hasil_iat = 0.0d;
                                                try {
                                                    hasil_iat = Double.valueOf(data).doubleValue() / 10.0d;
                                                } catch (Exception e9) {
                                                }
                                                live_awal.this.iat.setText(String.valueOf(hasil_iat).replace(",", "."));
                                                return;
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
                            if (b == 10 || b == 59) {
                                byte[] encodedBytes2 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes2, 0, encodedBytes2.length);
                                final String data2 = new String(encodedBytes2, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (data2.equals("1A00")) {
                                            live_awal.this.cek = 0;
                                        } else {
                                            live_awal.this.cek = 0;
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
                            if (b == 10 || b == 59) {
                                byte[] encodedBytes3 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes3, 0, encodedBytes3.length);
                                final String data3 = new String(encodedBytes3, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (data3.equals("1A00")) {
                                            if (live_awal.this.stop_destroy.booleanValue()) {
                                                live_awal.this.handler.removeCallbacks(live_awal.this.sendData);
                                                try {
                                                    if (live_awal.this.nyambung == 1) {
                                                        live_awal.this.resetConnection();
                                                        Thread.sleep(200);
                                                    }
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                live_awal.this.pindah();
                                            }
                                        } else if (!data3.contains("1A00")) {
                                        } else {
                                            if (live_awal.this.stop_destroy.booleanValue()) {
                                                live_awal.this.handler.removeCallbacks(live_awal.this.sendData);
                                                try {
                                                    if (live_awal.this.nyambung == 1) {
                                                        live_awal.this.resetConnection();
                                                        Thread.sleep(200);
                                                        live_awal.this.finish();
                                                    }
                                                } catch (InterruptedException e2) {
                                                    e2.printStackTrace();
                                                }
                                            } else {
                                                live_awal.this.pindah();
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
                                final String data4 = new String(encodedBytes4, "US-ASCII").replaceAll("\n", "").replaceAll("\r", "");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (data4.equals("1A00")) {
                                            live_awal.this.cek = 0;
                                            Message message23 = Message.obtain();
                                            message23.obj = "Send Done";
                                            message23.setTarget(live_awal.this.mHandler);
                                            message23.sendToTarget();
                                            live_awal.this.hitung_tps = 0;
                                            live_awal.this.resetConnection();
                                            live_awal.this.finish();
                                        } else if (data4.contains("1A00")) {
                                            live_awal.this.cek = 0;
                                            Message message232 = Message.obtain();
                                            message232.obj = "Send Done";
                                            message232.setTarget(live_awal.this.mHandler);
                                            message232.sendToTarget();
                                            live_awal.this.hitung_tps = 0;
                                            live_awal.this.resetConnection();
                                            live_awal.this.finish();
                                        } else {
                                            live_awal.this.execute();
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr4 = this.readBuffer;
                                int i5 = this.readBufferPosition;
                                this.readBufferPosition = i5 + 1;
                                bArr4[i5] = b;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                this.stopWorker = true;
            }
        }
    }

    /* access modifiers changed from: private */
    public int mod(int x, int y) {
        int result = x % y;
        if (result < 0) {
            return result + y;
        }
        return result;
    }

    /* access modifiers changed from: private */
    public void pindah() {
        if (this.masuk_ke.equals("ecu limiter")) {
            if (StaticClass.TipeKoneksi.equals("bt")) {
                resetConnection();
            }
            finish();
            SavePreferences("dari_live", "1");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.drawerLayout.closeDrawer(3);
            startActivityForResult(new Intent(getApplicationContext(), ecu_limiter.class), 0);
        } else if (this.masuk_ke.equals("warming up")) {
            if (StaticClass.TipeKoneksi.equals("bt")) {
                resetConnection();
            }
            finish();
            SavePreferences("dari_live", "1");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            this.drawerLayout.closeDrawer(3);
            startActivityForResult(new Intent(getApplicationContext(), warming_up.class), 0);
        } else if (this.masuk_ke.equals("rpm_idle")) {
            if (StaticClass.TipeKoneksi.equals("bt")) {
                resetConnection();
            }
            finish();
            SavePreferences("dari_live", "1");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
            this.drawerLayout.closeDrawer(3);
            startActivityForResult(new Intent(getApplicationContext(), rpm_idle.class), 0);
        } else if (this.masuk_ke.equals("deceleration_fuel")) {
            if (StaticClass.TipeKoneksi.equals("bt")) {
                resetConnection();
            }
            finish();
            SavePreferences("dari_live", "1");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e4) {
                e4.printStackTrace();
            }
            this.drawerLayout.closeDrawer(3);
            startActivityForResult(new Intent(getApplicationContext(), deceleration_fuel.class), 0);
        } else if (this.masuk_ke.equals("emap")) {
            if (StaticClass.TipeKoneksi.equals("bt")) {
                resetConnection();
            }
            finish();
            SavePreferences("dari_live", "1");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e5) {
                e5.printStackTrace();
            }
            this.drawerLayout.closeDrawer(3);
            startActivityForResult(new Intent(getApplicationContext(), e_map.class), 0);
        }
    }

    public static String formatBmi(double bmi) {
        return oneDecimal.format(bmi);
    }

    /* access modifiers changed from: private */
    public double mapping_map(double volt) {
        if (volt < 0.3d) {
            return (100.0d * volt) / 3.0d;
        }
        if (volt >= 0.3d && volt < 0.6d) {
            return ((100.0d * (volt - 0.3d)) / 3.0d) + 10.0d;
        }
        if (volt >= 0.6d && volt < 1.1d) {
            return ((100.0d * (volt - 0.6d)) / 5.0d) + 20.0d;
        }
        if (volt >= 1.1d && volt < 1.7d) {
            return ((100.0d * (volt - 1.1d)) / 6.0d) + 30.0d;
        }
        if (volt >= 1.7d && volt < 2.2d) {
            return ((100.0d * (volt - 1.7d)) / 5.0d) + 40.0d;
        }
        if (volt >= 2.2d && volt < 2.7d) {
            return ((100.0d * (volt - 2.2d)) / 5.0d) + 50.0d;
        }
        if (volt >= 2.7d && volt < 3.3d) {
            return ((100.0d * (volt - 2.7d)) / 6.0d) + 60.0d;
        }
        if (volt >= 3.3d && volt < 3.8d) {
            return ((100.0d * (volt - 3.3d)) / 5.0d) + 70.0d;
        }
        if (volt >= 3.8d && volt < 4.4d) {
            return ((100.0d * (volt - 3.8d)) / 6.0d) + 80.0d;
        }
        if (volt >= 4.4d && volt < 4.9d) {
            return ((100.0d * (volt - 4.4d)) / 5.0d) + 90.0d;
        }
        if (volt >= 4.9d) {
            return 100.0d;
        }
        return 0.0d;
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        MyBroadCastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Float nilaiTps;
            Float nilaiRpm;
            Float nilaiAfr;
            String data = intent.getStringExtra("key");
            char c = 65535;
            switch (data.hashCode()) {
                case 108388975:
                    if (data.equals("recon")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1481843414:
                    if (data.equals("LiveData")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    String mentah = StaticClass.DataLive;
                    if (live_awal.this.cek == 0) {
                        String[] dataEnter = mentah.replace("\r", "").split("\n");
                        for (int i = 0; i < dataEnter.length; i++) {
                            String[] dataBener = dataEnter[i].split(";");
                            if (dataBener[0].equals("A603") || dataBener[0].equals("a603")) {
                                Float valueOf = Float.valueOf(0.0f);
                                try {
                                    nilaiTps = Float.valueOf(dataBener[1]);
                                } catch (Exception e) {
                                }
                                if (nilaiTps.floatValue() > 20.0f) {
                                    nilaiTps = Float.valueOf(20.0f);
                                } else if (nilaiTps.floatValue() < 0.0f) {
                                    nilaiTps = Float.valueOf(0.0f);
                                }
                                Float gauge = Float.valueOf(0.0f);
                                if (nilaiTps.floatValue() == 1.0f) {
                                    gauge = Float.valueOf(5.0f);
                                } else if (nilaiTps.floatValue() > 1.0f && nilaiTps.floatValue() < 20.0f) {
                                    gauge = Float.valueOf((nilaiTps.floatValue() * 5.0f) - 5.0f);
                                } else if (nilaiTps.floatValue() == 20.0f) {
                                    gauge = Float.valueOf(100.0f);
                                }
                                live_awal.this.gauge_tps.speedTo(gauge.floatValue());
                                try {
                                    live_awal.this.tps_mentah = Math.round(gauge.floatValue());
                                } catch (Exception e2) {
                                    live_awal.this.tps_mentah = 0;
                                }
                                live_awal.this.vbat.setText(dataBener[2]);
                                live_awal.this.core.setText(dataBener[3]);
                                if (dataBener[3].equals("1")) {
                                    if (!MappingHandle.NamaFileBM.equals("Base Map - ECU Core 1") || !MappingHandle.NamaFileFuel.equals("Fuel Correction - ECU Core 1") || !MappingHandle.NamaFileIG.equals("Ignition Timing - ECU Core 1") || !MappingHandle.NamaFileIT.equals("Injector Timing - ECU Core 1")) {
                                        if (live_awal.this.pertama_cek.booleanValue()) {
                                            live_awal.this.pertama_cek = null;
                                            Message message23 = Message.obtain();
                                            message23.obj = live_awal.this.getString(R.string.LiveSalahCore);
                                            message23.setTarget(live_awal.this.mHandler);
                                            message23.sendToTarget();
                                        }
                                        live_awal.this.sendStop = 0;
                                        live_awal.this.handler.post(live_awal.this.sendData);
                                        live_awal.this.stop_destroy = 1;
                                    }
                                } else if (dataBener[3].equals("2") && (!MappingHandle.NamaFileBM.equals("Base Map - ECU Core 2") || !MappingHandle.NamaFileFuel.equals("Fuel Correction - ECU Core 2") || !MappingHandle.NamaFileIG.equals("Ignition Timing - ECU Core 2") || !MappingHandle.NamaFileIT.equals("Injector Timing - ECU Core 2"))) {
                                    if (live_awal.this.pertama_cek.booleanValue()) {
                                        live_awal.this.pertama_cek = null;
                                        Message message232 = Message.obtain();
                                        message232.obj = live_awal.this.getString(R.string.LiveSalahCore);
                                        message232.setTarget(live_awal.this.mHandler);
                                        message232.sendToTarget();
                                    }
                                    live_awal.this.sendStop = 0;
                                    live_awal.this.handler.post(live_awal.this.sendData);
                                    live_awal.this.stop_destroy = 1;
                                }
                                live_awal.this.text_rpm.setText(dataBener[4]);
                                Float valueOf2 = Float.valueOf(0.0f);
                                try {
                                    nilaiRpm = Float.valueOf(Float.valueOf(dataBener[4]).floatValue() / 1000.0f);
                                } catch (Exception e3) {
                                }
                                try {
                                    if (nilaiRpm.floatValue() > 16.0f) {
                                        nilaiRpm = Float.valueOf(16.0f);
                                    } else if (nilaiRpm.floatValue() < 0.0f) {
                                        nilaiRpm = Float.valueOf(0.0f);
                                    }
                                    live_awal.this.gauge_rpm.speedTo(nilaiRpm.floatValue());
                                    try {
                                        live_awal.this.rpm_mentah = Integer.valueOf(dataBener[4]).intValue();
                                    } catch (Exception e4) {
                                        live_awal.this.rpm_mentah = 1000;
                                    }
                                    String tampil = "0";
                                    try {
                                        tampil = String.format("%.1f", new Object[]{Double.valueOf(Double.parseDouble(dataBener[5]) / 10.0d)}).replace(",", ".");
                                    } catch (Exception e5) {
                                    }
                                    live_awal.this.temp.setText(tampil);
                                    int hitung_tps = 0;
                                    if (live_awal.this.tps_mentah == 0) {
                                        hitung_tps = 0;
                                    } else if (live_awal.this.tps_mentah == 2) {
                                        hitung_tps = 1;
                                    } else if (live_awal.this.tps_mentah == 100) {
                                        hitung_tps = 20;
                                    } else if (live_awal.this.tps_mentah > 2 && live_awal.this.tps_mentah < 100) {
                                        hitung_tps = (live_awal.this.tps_mentah / 5) + 1;
                                    }
                                    int posisiF = (hitung_tps * 61) + (((live_awal.this.rpm_mentah - live_awal.this.mod(live_awal.this.rpm_mentah, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION)) / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) - 4);
                                    if (posisiF < 0) {
                                        posisiF = 0;
                                    } else if (posisiF > 1280) {
                                        posisiF = 1280;
                                    }
                                    try {
                                        live_awal.this.fuel.setText(MappingHandle.list_fuel_live.get(posisiF));
                                    } catch (Exception e6) {
                                        live_awal.this.fuel.setText(dataBener[6]);
                                    }
                                    live_awal.this.afr.setText(dataBener[7]);
                                    Float valueOf3 = Float.valueOf(9.0f);
                                    try {
                                        nilaiAfr = Float.valueOf(dataBener[7]);
                                    } catch (Exception e7) {
                                    }
                                    if (nilaiAfr.floatValue() > 20.0f) {
                                        nilaiAfr = Float.valueOf(20.0f);
                                    } else if (nilaiAfr.floatValue() < 9.0f) {
                                        nilaiAfr = Float.valueOf(9.0f);
                                    }
                                    live_awal.this.gauge_afr.speedTo(nilaiAfr.floatValue());
                                    live_awal.this.base.setText(dataBener[8]);
                                    live_awal.this.injector_t.setText(dataBener[9]);
                                    live_awal.this.ignition_t.setText(String.valueOf(Double.valueOf(dataBener[10]).doubleValue() / 10.0d).replace(",", "."));
                                    int hasil = 0;
                                    try {
                                        hasil = (int) live_awal.this.mapping_map((Double.valueOf(dataBener[11]).doubleValue() * 5.0d) / 1023.0d);
                                    } catch (Exception e8) {
                                    }
                                    live_awal.this.map.setText(String.valueOf(hasil));
                                    double hasil_iat = 0.0d;
                                    try {
                                        hasil_iat = Double.valueOf(dataBener[12]).doubleValue() / 10.0d;
                                    } catch (Exception e9) {
                                    }
                                    live_awal.this.iat.setText(String.valueOf(hasil_iat).replace(",", "."));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        return;
                    } else if (live_awal.this.cek == 1) {
                        if (mentah.contains("1A00")) {
                            live_awal.this.cek = 0;
                            return;
                        }
                        return;
                    } else if (live_awal.this.cek == 2) {
                        if (!mentah.contains("1A00")) {
                            return;
                        }
                        if (live_awal.this.stop_destroy.booleanValue()) {
                            live_awal.this.handler.removeCallbacks(live_awal.this.sendData);
                            live_awal.this.finish();
                            return;
                        }
                        live_awal.this.pindah();
                        return;
                    } else if (live_awal.this.cek != 3) {
                        return;
                    } else {
                        if (mentah.contains("1A00")) {
                            live_awal.this.cek = 0;
                            Message message233 = Message.obtain();
                            message233.obj = "Send Done";
                            message233.setTarget(live_awal.this.mHandler);
                            message233.sendToTarget();
                            live_awal.this.hitung_tps = 0;
                            live_awal.this.finish();
                            return;
                        }
                        live_awal.this.execute();
                        return;
                    }
                case 1:
                    StaticClass.Live = false;
                    live_awal.this.finish();
                    return;
                default:
                    return;
            }
            ex.printStackTrace();
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
