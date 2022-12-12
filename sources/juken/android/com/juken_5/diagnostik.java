package juken.android.com.juken_5;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class diagnostik extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            diagnostik.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    CheckBox cb_fuel_pump;
    CheckBox cb_lampu;
    CheckBox cb_vva;
    String cek_dwell = "";
    String cek_imob = "";
    String cek_turbo = "";
    String cek_type = "";
    String cek_vva = "";
    TextView coil;
    TextView cooke;
    Boolean dari_fuel_pump = false;
    Boolean dari_lampu = false;
    Boolean dari_vva = false;
    TextView dwell;
    TextView ecu_name;
    TextView eot;
    Boolean fromRecon = false;
    TextView front_lamp;
    TextView fuel_pump;
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    int hitung = 0;
    TextView iat;
    TextView injector;
    private InputStream inputStream = null;
    TextView lambda;
    TextView map;
    MyBroadCastReceiver myBroadCastReceiver;
    Boolean nyala = true;
    int nyambung = 1;
    private OutputStream outputStream = null;
    int posisi = 0;
    TextView pulser;
    byte[] readBuffer;
    int readBufferPosition;
    /* access modifiers changed from: private */
    public final Runnable sendData = new Runnable() {
        public void run() {
            try {
                diagnostik.this.kirim_awal();
                diagnostik.this.handler.postDelayed(this, 600);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    /* access modifiers changed from: private */
    public final Runnable sendOffTest = new Runnable() {
        public void run() {
            try {
                diagnostik.this.kirim_test();
                diagnostik.this.handler.postDelayed(this, 1200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    /* access modifiers changed from: private */
    public final Runnable sendTest = new Runnable() {
        public void run() {
            try {
                if (diagnostik.this.hitung == 0) {
                    diagnostik.this.hitung = 1;
                    diagnostik.this.kirim_test();
                } else {
                    diagnostik.this.hitung = 0;
                    diagnostik.this.handler.removeCallbacks(diagnostik.this.sendTest);
                    diagnostik.this.on_cb();
                }
                diagnostik.this.handler.postDelayed(this, 1200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Intent service;
    TextView smart_key;
    private BluetoothSocket socket = null;
    TextView speed_sensor;
    volatile boolean stopWorker;
    TextView tps;
    TextView turbo;
    TextView vBat;
    TextView vva;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagnostik);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        this.cb_fuel_pump = (CheckBox) findViewById(R.id.checkbox_fuel_pump);
        this.cb_fuel_pump.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (diagnostik.this.cb_fuel_pump.isChecked()) {
                    diagnostik.this.handler.removeCallbacks(diagnostik.this.sendData);
                    diagnostik.this.cb_fuel_pump.setText("ON");
                    diagnostik.this.off_cb();
                    diagnostik.this.dari_fuel_pump = true;
                    diagnostik.this.dari_lampu = false;
                    diagnostik.this.dari_vva = false;
                    diagnostik.this.hitung = 0;
                    diagnostik.this.nyala = true;
                    diagnostik.this.handler.post(diagnostik.this.sendTest);
                }
            }
        });
        this.cb_vva = (CheckBox) findViewById(R.id.checkbox_vva);
        this.cb_vva.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (diagnostik.this.cek_vva.equals("0")) {
                    diagnostik.this.cb_vva.setChecked(false);
                } else if (diagnostik.this.cb_vva.isChecked()) {
                    diagnostik.this.handler.removeCallbacks(diagnostik.this.sendData);
                    diagnostik.this.cb_vva.setText("ON");
                    diagnostik.this.off_cb();
                    diagnostik.this.dari_fuel_pump = false;
                    diagnostik.this.dari_lampu = false;
                    diagnostik.this.dari_vva = true;
                    diagnostik.this.hitung = 0;
                    diagnostik.this.nyala = true;
                    diagnostik.this.handler.post(diagnostik.this.sendTest);
                }
            }
        });
        this.cb_lampu = (CheckBox) findViewById(R.id.checkbox_lampu);
        this.cb_lampu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (diagnostik.this.cb_lampu.isChecked()) {
                    diagnostik.this.handler.removeCallbacks(diagnostik.this.sendData);
                    diagnostik.this.cb_lampu.setText("ON");
                    diagnostik.this.off_cb();
                    diagnostik.this.dari_fuel_pump = false;
                    diagnostik.this.dari_lampu = true;
                    diagnostik.this.dari_vva = false;
                    diagnostik.this.hitung = 0;
                    diagnostik.this.nyala = true;
                    diagnostik.this.handler.post(diagnostik.this.sendTest);
                }
            }
        });
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.handler.post(this.sendData);
        }
    }

    /* access modifiers changed from: private */
    public void off_cb() {
        this.cb_fuel_pump.setEnabled(false);
        this.cb_vva.setEnabled(false);
        this.cb_lampu.setEnabled(false);
    }

    /* access modifiers changed from: private */
    public void on_cb() {
        this.cb_fuel_pump.setEnabled(true);
        this.cb_vva.setEnabled(true);
        this.cb_lampu.setEnabled(true);
        this.cb_fuel_pump.setText("OFF");
        this.cb_vva.setText("OFF");
        this.cb_lampu.setText("OFF");
        this.cb_fuel_pump.setChecked(false);
        this.cb_vva.setChecked(false);
        this.cb_lampu.setChecked(false);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LoadPreferences();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.sendData);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            if (!this.fromRecon.booleanValue()) {
                StaticClass.bolehPing = true;
            }
            unregisterReceiver(this.myBroadCastReceiver);
        } else if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            try {
                if (this.nyambung == 1) {
                    resetConnection();
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetConnection() {
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

    private void LoadPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String data = sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame");
        this.cek_turbo = sharedPreferences.getString("turbo_en", "turbo_en1");
        this.cek_imob = sharedPreferences.getString("imob_en", "imob_en1");
        this.cek_dwell = sharedPreferences.getString("dwell_time", "dwell_time1");
        this.cek_type = sharedPreferences.getString("ecu_digunakan", "ecu_digunakan1");
        this.cek_vva = sharedPreferences.getString("VVTC", "VVTC1");
        if (this.cek_type.equals("ecu_digunakan1")) {
            this.cek_type = "Juken 5";
        }
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(data);
            new Thread() {
                public void run() {
                    diagnostik.this.connect(perangkat);
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
            this.handler.post(this.sendData);
        } catch (IOException e) {
        }
    }

    /* access modifiers changed from: private */
    public void kirim_test() {
        String data = "";
        if (this.dari_vva.booleanValue()) {
            data = "5002\r\n";
        } else if (this.dari_lampu.booleanValue()) {
            data = "5004\r\n";
        } else if (this.dari_fuel_pump.booleanValue()) {
            data = "5001\r\n";
        }
        byte[] msgBuffer = data.getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = data;
            this.service.putExtra("send", "GlobalData");
            startService(this.service);
        } else if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled() && this.nyambung == 1) {
            try {
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(msgBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void kirim_awal() {
        byte[] msgBuffer = "4602\r\n".getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = "4602\r\n";
            this.service.putExtra("send", "GlobalData");
            startService(this.service);
        } else if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled() && this.nyambung == 1) {
            try {
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(msgBuffer);
            } catch (IOException e) {
                e.printStackTrace();
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
                        if (b == 10 || b == 59) {
                            final byte[] encodedBytes = new byte[this.readBufferPosition];
                            System.arraycopy(this.readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                            final String data = new String(encodedBytes, "US-ASCII");
                            byte[] pake_ini = new byte[this.readBufferPosition];
                            System.arraycopy(this.readBuffer, 0, pake_ini, 0, pake_ini.length);
                            this.readBufferPosition = 0;
                            this._handler.post(new Runnable() {
                                public void run() {
                                    switch (diagnostik.this.posisi) {
                                        case 0:
                                            String s2 = data.replaceAll("\\n", "").replaceAll("\\r", "");
                                            if (s2.equals("1A00")) {
                                                diagnostik.this.posisi = 100;
                                                return;
                                            } else if (s2.equals("A602") || s2.equals("a602")) {
                                                diagnostik.this.posisi = 1;
                                                return;
                                            } else {
                                                return;
                                            }
                                        case 1:
                                            diagnostik.this.posisi = 2;
                                            String str2 = null;
                                            try {
                                                str2 = new String(encodedBytes, "UTF-8");
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            byte b = (byte) (Integer.parseInt(str2) & 255);
                                            String map_ = String.format("%8s", new Object[]{Integer.toBinaryString(b & 3)}).replace(' ', '0');
                                            String tps_ = String.format("%8s", new Object[]{Integer.toBinaryString(b & 12)}).replace(' ', '0');
                                            String iat_ = String.format("%8s", new Object[]{Integer.toBinaryString(b & 48)}).replace(' ', '0');
                                            String eot_ = String.format("%8s", new Object[]{Integer.toBinaryString(b & 192)}).replace(' ', '0');
                                            diagnostik.this.map = (TextView) diagnostik.this.findViewById(R.id.et_map);
                                            diagnostik.this.tps = (TextView) diagnostik.this.findViewById(R.id.et_tps);
                                            diagnostik.this.iat = (TextView) diagnostik.this.findViewById(R.id.et_iat);
                                            diagnostik.this.eot = (TextView) diagnostik.this.findViewById(R.id.et_eot);
                                            if (map_.equals("00000000")) {
                                                diagnostik.this.map.setText("ok");
                                            } else if (map_.equals("00000001")) {
                                                diagnostik.this.map.setText("error");
                                            } else if (map_.equals("00000010")) {
                                                diagnostik.this.map.setText("n/a");
                                            } else if (map_.equals("00000011")) {
                                                diagnostik.this.map.setText("reserved");
                                            }
                                            if (tps_.equals("00000000")) {
                                                diagnostik.this.tps.setText("ok");
                                            } else if (tps_.equals("00000100")) {
                                                diagnostik.this.tps.setText("error");
                                            } else if (tps_.equals("00001000")) {
                                                diagnostik.this.tps.setText("n/a");
                                            } else if (tps_.equals("00001100")) {
                                                diagnostik.this.tps.setText("reserved");
                                            }
                                            if (iat_.equals("00000000")) {
                                                diagnostik.this.iat.setText("ok");
                                            } else if (iat_.equals("00010000")) {
                                                diagnostik.this.iat.setText("error");
                                            } else if (iat_.equals("00100000")) {
                                                diagnostik.this.iat.setText("n/a");
                                            } else if (iat_.equals("00110000")) {
                                                diagnostik.this.iat.setText("reserved");
                                            }
                                            if (eot_.equals("00000000")) {
                                                diagnostik.this.eot.setText("ok");
                                                return;
                                            } else if (eot_.equals("01000000")) {
                                                diagnostik.this.eot.setText("error");
                                                return;
                                            } else if (eot_.equals("10000000")) {
                                                diagnostik.this.eot.setText("na");
                                                return;
                                            } else if (eot_.equals("11000000")) {
                                                diagnostik.this.eot.setText("reserved");
                                                return;
                                            } else {
                                                return;
                                            }
                                        case 2:
                                            diagnostik.this.posisi = 3;
                                            String str1 = null;
                                            try {
                                                str1 = new String(encodedBytes, "UTF-8");
                                            } catch (UnsupportedEncodingException e2) {
                                                e2.printStackTrace();
                                            }
                                            byte bytes = (byte) (Integer.parseInt(str1) & 255);
                                            String pump = String.format("%8s", new Object[]{Integer.toBinaryString(bytes & 3)}).replace(' ', '0');
                                            String inj = String.format("%8s", new Object[]{Integer.toBinaryString(bytes & 12)}).replace(' ', '0');
                                            String coil_ = String.format("%8s", new Object[]{Integer.toBinaryString(bytes & 48)}).replace(' ', '0');
                                            String choke = String.format("%8s", new Object[]{Integer.toBinaryString(bytes & 192)}).replace(' ', '0');
                                            diagnostik.this.fuel_pump = (TextView) diagnostik.this.findViewById(R.id.et_fuel_pump);
                                            diagnostik.this.injector = (TextView) diagnostik.this.findViewById(R.id.et_injector);
                                            diagnostik.this.coil = (TextView) diagnostik.this.findViewById(R.id.et_coil);
                                            diagnostik.this.cooke = (TextView) diagnostik.this.findViewById(R.id.et_cooke);
                                            if (pump.equals("00000000")) {
                                                diagnostik.this.fuel_pump.setText("ok");
                                            } else if (pump.equals("00000001")) {
                                                diagnostik.this.fuel_pump.setText("error");
                                            } else if (pump.equals("00000010")) {
                                                diagnostik.this.fuel_pump.setText("n/a");
                                            } else if (pump.equals("00000011")) {
                                                diagnostik.this.fuel_pump.setText("reserved");
                                            }
                                            if (inj.equals("00000000")) {
                                                diagnostik.this.injector.setText("ok");
                                            } else if (inj.equals("00000100")) {
                                                diagnostik.this.injector.setText("error");
                                            } else if (inj.equals("00001000")) {
                                                diagnostik.this.injector.setText("n/a");
                                            } else if (inj.equals("00001100")) {
                                                diagnostik.this.injector.setText("reserved");
                                            }
                                            if (coil_.equals("00000000")) {
                                                diagnostik.this.coil.setText("ok");
                                            } else if (coil_.equals("00010000")) {
                                                diagnostik.this.coil.setText("error");
                                            } else if (coil_.equals("00100000")) {
                                                diagnostik.this.coil.setText("n/a");
                                            } else if (coil_.equals("00110000")) {
                                                diagnostik.this.coil.setText("reserved");
                                            }
                                            if (choke.equals("00000000")) {
                                                diagnostik.this.cooke.setText("ok");
                                                return;
                                            } else if (choke.equals("01000000")) {
                                                diagnostik.this.cooke.setText("error");
                                                return;
                                            } else if (choke.equals("10000000")) {
                                                diagnostik.this.cooke.setText("n/a");
                                                return;
                                            } else if (choke.equals("11000000")) {
                                                diagnostik.this.cooke.setText("reserved");
                                                return;
                                            } else {
                                                return;
                                            }
                                        case 3:
                                            diagnostik.this.posisi = 4;
                                            return;
                                        case 4:
                                            diagnostik.this.posisi = 5;
                                            return;
                                        case 5:
                                            diagnostik.this.posisi = 6;
                                            ((TextView) diagnostik.this.findViewById(R.id.et_tps_value)).setText(data);
                                            return;
                                        case 6:
                                            diagnostik.this.posisi = 0;
                                            diagnostik.this.vBat = (TextView) diagnostik.this.findViewById(R.id.et_vbat);
                                            diagnostik.this.vBat.setText(data.trim() + "V");
                                            diagnostik.this.turbo = (TextView) diagnostik.this.findViewById(R.id.et_turbo);
                                            if (diagnostik.this.cek_turbo.equals("1")) {
                                                diagnostik.this.turbo.setText("on");
                                            } else {
                                                diagnostik.this.turbo.setText("off");
                                            }
                                            diagnostik.this.smart_key = (TextView) diagnostik.this.findViewById(R.id.et_smart_key);
                                            if (diagnostik.this.cek_imob.equals("1")) {
                                                diagnostik.this.smart_key.setText("on");
                                            } else {
                                                diagnostik.this.smart_key.setText("off");
                                            }
                                            diagnostik.this.dwell = (TextView) diagnostik.this.findViewById(R.id.et_dwell);
                                            diagnostik.this.dwell.setText(String.valueOf(Double.parseDouble(diagnostik.this.cek_dwell.replaceAll(",", ".")) / 10.0d) + " mS");
                                            diagnostik.this.vva = (TextView) diagnostik.this.findViewById(R.id.et_vva);
                                            if (diagnostik.this.cek_vva.equals("0")) {
                                                diagnostik.this.vva.setText("n/a");
                                            } else {
                                                diagnostik.this.vva.setText(diagnostik.this.cek_vva);
                                            }
                                            diagnostik.this.ecu_name = (TextView) diagnostik.this.findViewById(R.id.et_ecu_name);
                                            diagnostik.this.ecu_name.setText(diagnostik.this.cek_type);
                                            return;
                                        case 100:
                                            diagnostik.this.posisi = 0;
                                            if (diagnostik.this.nyala.booleanValue()) {
                                                diagnostik.this.handler.removeCallbacks(diagnostik.this.sendTest);
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e3) {
                                                    e3.printStackTrace();
                                                }
                                                diagnostik.this.handler.post(diagnostik.this.sendOffTest);
                                                diagnostik.this.nyala = null;
                                                return;
                                            }
                                            diagnostik.this.handler.removeCallbacks(diagnostik.this.sendOffTest);
                                            diagnostik.this.on_cb();
                                            diagnostik.this.handler.post(diagnostik.this.sendData);
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
                    }
                }
            } catch (IOException e) {
                this.stopWorker = true;
            }
        }
    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        MyBroadCastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            try {
                String data = intent.getStringExtra("key");
                char c = 65535;
                switch (data.hashCode()) {
                    case -1496268600:
                        if (data.equals("diagnosticProcess")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -375011075:
                        if (data.equals("dataSaved")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 108388975:
                        if (data.equals("recon")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        if (diagnostik.this.nyala.booleanValue()) {
                            diagnostik.this.handler.removeCallbacks(diagnostik.this.sendTest);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            diagnostik.this.handler.post(diagnostik.this.sendOffTest);
                            diagnostik.this.nyala = null;
                            return;
                        }
                        diagnostik.this.handler.removeCallbacks(diagnostik.this.sendOffTest);
                        diagnostik.this.on_cb();
                        diagnostik.this.handler.post(diagnostik.this.sendData);
                        return;
                    case 1:
                        byte b = (byte) (Integer.parseInt(StaticClass.str2) & 255);
                        String map_ = String.format("%8s", new Object[]{Integer.toBinaryString(b & 3)}).replace(' ', '0');
                        String tps_ = String.format("%8s", new Object[]{Integer.toBinaryString(b & 12)}).replace(' ', '0');
                        String iat_ = String.format("%8s", new Object[]{Integer.toBinaryString(b & 48)}).replace(' ', '0');
                        String eot_ = String.format("%8s", new Object[]{Integer.toBinaryString(b & 192)}).replace(' ', '0');
                        diagnostik.this.map = (TextView) diagnostik.this.findViewById(R.id.et_map);
                        diagnostik.this.tps = (TextView) diagnostik.this.findViewById(R.id.et_tps);
                        diagnostik.this.iat = (TextView) diagnostik.this.findViewById(R.id.et_iat);
                        diagnostik.this.eot = (TextView) diagnostik.this.findViewById(R.id.et_eot);
                        if (map_.equals("00000000")) {
                            diagnostik.this.map.setText("ok");
                        } else if (map_.equals("00000001")) {
                            diagnostik.this.map.setText("error");
                        } else if (map_.equals("00000010")) {
                            diagnostik.this.map.setText("n/a");
                        } else if (map_.equals("00000011")) {
                            diagnostik.this.map.setText("reserved");
                        }
                        if (tps_.equals("00000000")) {
                            diagnostik.this.tps.setText("ok");
                        } else if (tps_.equals("00000100")) {
                            diagnostik.this.tps.setText("error");
                        } else if (tps_.equals("00001000")) {
                            diagnostik.this.tps.setText("n/a");
                        } else if (tps_.equals("00001100")) {
                            diagnostik.this.tps.setText("reserved");
                        }
                        if (iat_.equals("00000000")) {
                            diagnostik.this.iat.setText("ok");
                        } else if (iat_.equals("00010000")) {
                            diagnostik.this.iat.setText("error");
                        } else if (iat_.equals("00100000")) {
                            diagnostik.this.iat.setText("n/a");
                        } else if (iat_.equals("00110000")) {
                            diagnostik.this.iat.setText("reserved");
                        }
                        if (eot_.equals("00000000")) {
                            diagnostik.this.eot.setText("ok");
                        } else if (eot_.equals("01000000")) {
                            diagnostik.this.eot.setText("error");
                        } else if (eot_.equals("10000000")) {
                            diagnostik.this.eot.setText("na");
                        } else if (eot_.equals("11000000")) {
                            diagnostik.this.eot.setText("reserved");
                        }
                        byte bytes = (byte) (Integer.parseInt(StaticClass.str1) & 255);
                        String pump = String.format("%8s", new Object[]{Integer.toBinaryString(bytes & 3)}).replace(' ', '0');
                        String inj = String.format("%8s", new Object[]{Integer.toBinaryString(bytes & 12)}).replace(' ', '0');
                        String coil_ = String.format("%8s", new Object[]{Integer.toBinaryString(bytes & 48)}).replace(' ', '0');
                        String choke = String.format("%8s", new Object[]{Integer.toBinaryString(bytes & 192)}).replace(' ', '0');
                        diagnostik.this.fuel_pump = (TextView) diagnostik.this.findViewById(R.id.et_fuel_pump);
                        diagnostik.this.injector = (TextView) diagnostik.this.findViewById(R.id.et_injector);
                        diagnostik.this.coil = (TextView) diagnostik.this.findViewById(R.id.et_coil);
                        diagnostik.this.cooke = (TextView) diagnostik.this.findViewById(R.id.et_cooke);
                        if (pump.equals("00000000")) {
                            diagnostik.this.fuel_pump.setText("ok");
                        } else if (pump.equals("00000001")) {
                            diagnostik.this.fuel_pump.setText("error");
                        } else if (pump.equals("00000010")) {
                            diagnostik.this.fuel_pump.setText("n/a");
                        } else if (pump.equals("00000011")) {
                            diagnostik.this.fuel_pump.setText("reserved");
                        }
                        if (inj.equals("00000000")) {
                            diagnostik.this.injector.setText("ok");
                        } else if (inj.equals("00000100")) {
                            diagnostik.this.injector.setText("error");
                        } else if (inj.equals("00001000")) {
                            diagnostik.this.injector.setText("n/a");
                        } else if (inj.equals("00001100")) {
                            diagnostik.this.injector.setText("reserved");
                        }
                        if (coil_.equals("00000000")) {
                            diagnostik.this.coil.setText("ok");
                        } else if (coil_.equals("00010000")) {
                            diagnostik.this.coil.setText("error");
                        } else if (coil_.equals("00100000")) {
                            diagnostik.this.coil.setText("n/a");
                        } else if (coil_.equals("00110000")) {
                            diagnostik.this.coil.setText("reserved");
                        }
                        if (choke.equals("00000000")) {
                            diagnostik.this.cooke.setText("ok");
                        } else if (choke.equals("01000000")) {
                            diagnostik.this.cooke.setText("error");
                        } else if (choke.equals("10000000")) {
                            diagnostik.this.cooke.setText("n/a");
                        } else if (choke.equals("11000000")) {
                            diagnostik.this.cooke.setText("reserved");
                        }
                        ((TextView) diagnostik.this.findViewById(R.id.et_tps_value)).setText(StaticClass.tpsValue);
                        diagnostik.this.vBat = (TextView) diagnostik.this.findViewById(R.id.et_vbat);
                        diagnostik.this.vBat.setText(StaticClass.vBat + "V");
                        diagnostik.this.turbo = (TextView) diagnostik.this.findViewById(R.id.et_turbo);
                        if (diagnostik.this.cek_turbo.equals("1")) {
                            diagnostik.this.turbo.setText("on");
                        } else {
                            diagnostik.this.turbo.setText("off");
                        }
                        diagnostik.this.smart_key = (TextView) diagnostik.this.findViewById(R.id.et_smart_key);
                        if (diagnostik.this.cek_imob.equals("1")) {
                            diagnostik.this.smart_key.setText("on");
                        } else {
                            diagnostik.this.smart_key.setText("off");
                        }
                        diagnostik.this.dwell = (TextView) diagnostik.this.findViewById(R.id.et_dwell);
                        diagnostik.this.dwell.setText(String.valueOf(Double.parseDouble(diagnostik.this.cek_dwell.replaceAll(",", ".")) / 10.0d) + " mS");
                        diagnostik.this.vva = (TextView) diagnostik.this.findViewById(R.id.et_vva);
                        if (diagnostik.this.cek_vva.equals("0")) {
                            diagnostik.this.vva.setText("n/a");
                        } else {
                            diagnostik.this.vva.setText(diagnostik.this.cek_vva);
                        }
                        diagnostik.this.ecu_name = (TextView) diagnostik.this.findViewById(R.id.et_ecu_name);
                        diagnostik.this.ecu_name.setText(diagnostik.this.cek_type);
                        return;
                    case 2:
                        diagnostik.this.fromRecon = 1;
                        diagnostik.this.finish();
                        return;
                    default:
                        return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
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
