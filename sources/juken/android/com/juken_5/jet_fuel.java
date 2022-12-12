package juken.android.com.juken_5;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class jet_fuel extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            jet_fuel.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    private InputStream inputStream = null;
    EditText jet_fuel_fast;
    EditText jet_fuel_med_fast;
    EditText jet_fuel_slow;
    EditText jet_fuel_slow_med;
    int keadaan = 0;
    MyBroadCastReceiver myBroadCastReceiver;
    int nyambung = 1;
    private OutputStream outputStream = null;
    byte[] readBuffer;
    int readBufferPosition;
    Intent service;
    private BluetoothSocket socket = null;
    volatile boolean stopWorker;
    EditText tps_fast;
    EditText tps_slow;
    EditText tps_slow_med;
    int urutan_kirim = 0;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jet_fuel);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        ImageView tps = (ImageView) findViewById(R.id.imageview1);
        ImageView pump = (ImageView) findViewById(R.id.imageview2);
        if (LanguageHelper.getLanguage(this).toString().equals("en")) {
            tps.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.jet_fuel_tps_en));
            pump.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.judul_tps_fast_en));
        } else {
            tps.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.jet_fuel_tps));
            pump.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.judul_tps_fast));
        }
        TextView tutorial = (TextView) findViewById(R.id.tutorial);
        tutorial.setText(tutorial.getText().toString().replace("%%", "%"));
        this.tps_slow = (EditText) findViewById(R.id.et_tps_slow);
        this.tps_slow.setFilters(new InputFilter[]{new range_int1("0", "100")});
        this.tps_slow_med = (EditText) findViewById(R.id.et_tps_slow_med);
        this.tps_slow_med.setFilters(new InputFilter[]{new range_int1("0", "100")});
        this.tps_fast = (EditText) findViewById(R.id.et_tps_fast);
        this.tps_fast.setFilters(new InputFilter[]{new range_int1("0", "100")});
        this.jet_fuel_slow = (EditText) findViewById(R.id.et_jet_fuel_slow);
        this.jet_fuel_slow.setFilters(new InputFilter[]{new range_int1("0", "100")});
        this.jet_fuel_slow_med = (EditText) findViewById(R.id.et_jet_fuel_slow_med);
        this.jet_fuel_slow_med.setFilters(new InputFilter[]{new range_int1("0", "100")});
        this.jet_fuel_med_fast = (EditText) findViewById(R.id.et_jet_fuel_med_fast);
        this.jet_fuel_med_fast.setFilters(new InputFilter[]{new range_int1("0", "100")});
        this.jet_fuel_fast = (EditText) findViewById(R.id.et_jet_fuel_fast);
        this.jet_fuel_fast.setFilters(new InputFilter[]{new range_int1("0", "100")});
        ((Button) findViewById(R.id.save_jet_fuel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                jet_fuel.this.keadaan = 0;
                jet_fuel.this.urutan_kirim = 0;
                jet_fuel.this.kirim();
            }
        });
        ((Button) findViewById(R.id.exit_jet_fuel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                jet_fuel.this.finish();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LoadPreferences();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            unregisterReceiver(this.myBroadCastReceiver);
        } else if (StaticClass.TipeKoneksi.equals("bt")) {
            try {
                if (this.nyambung == 1) {
                    resetConnection();
                    Thread.sleep(700);
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
        String jf1 = sharedPreferences.getString("jf1", "jf11");
        String jf2 = sharedPreferences.getString("jf2", "jf21");
        String jf3 = sharedPreferences.getString("jf3", "jf31");
        String jf4 = sharedPreferences.getString("jf4", "jf41");
        String tr1 = sharedPreferences.getString("t_rate1", "t_rate11");
        String tr2 = sharedPreferences.getString("t_rate2", "t_rate21");
        String tr3 = sharedPreferences.getString("t_rate3", "t_rate31");
        this.tps_slow = (EditText) findViewById(R.id.et_tps_slow);
        this.tps_slow.setText(tr1);
        this.tps_slow_med = (EditText) findViewById(R.id.et_tps_slow_med);
        this.tps_slow_med.setText(tr2);
        this.tps_fast = (EditText) findViewById(R.id.et_tps_fast);
        this.tps_fast.setText(tr3);
        this.jet_fuel_slow = (EditText) findViewById(R.id.et_jet_fuel_slow);
        this.jet_fuel_slow.setText(jf1);
        this.jet_fuel_slow_med = (EditText) findViewById(R.id.et_jet_fuel_slow_med);
        this.jet_fuel_slow_med.setText(jf2);
        this.jet_fuel_med_fast = (EditText) findViewById(R.id.et_jet_fuel_med_fast);
        this.jet_fuel_med_fast.setText(jf3);
        this.jet_fuel_fast = (EditText) findViewById(R.id.et_jet_fuel_fast);
        this.jet_fuel_fast.setText(jf4);
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    jet_fuel.this.connect(perangkat);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void kirim() {
        this.urutan_kirim = 0;
        String code = this.jet_fuel_slow.getText().toString();
        String code1 = this.jet_fuel_slow_med.getText().toString();
        String code2 = this.jet_fuel_med_fast.getText().toString();
        String code3 = this.jet_fuel_fast.getText().toString();
        String code4 = "360B" + ";" + code + ";" + code1 + ";" + code2 + ";" + code3 + "\r\n";
        byte[] msgBuffer = code4.getBytes();
        SavePreferences("jf1", code);
        SavePreferences("jf2", code1);
        SavePreferences("jf3", code2);
        SavePreferences("jf4", code3);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = code4;
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

    public void kirim_tps() {
        String code = this.tps_slow.getText().toString();
        String code1 = this.tps_slow_med.getText().toString();
        String code2 = this.tps_fast.getText().toString();
        String code4 = "360C" + ";" + code + ";" + code1 + ";" + code2 + "\r\n";
        byte[] msgBuffer = code4.getBytes();
        SavePreferences("t_rate1", code);
        SavePreferences("t_rate2", code1);
        SavePreferences("t_rate3", code2);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = code4;
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

    private void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
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
                        if (b == 10) {
                            byte[] encodedBytes = new byte[this.readBufferPosition];
                            System.arraycopy(this.readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                            final String data = new String(encodedBytes, "US-ASCII");
                            this.readBufferPosition = 0;
                            this._handler.post(new Runnable() {
                                public void run() {
                                    if (data.replaceAll("\\n", "").replaceAll("\\r", "").equals("1A00")) {
                                        if (jet_fuel.this.urutan_kirim == 0) {
                                            jet_fuel.this.urutan_kirim = 1;
                                            jet_fuel.this.kirim_tps();
                                            return;
                                        }
                                        jet_fuel.this.urutan_kirim = 0;
                                        Toast.makeText(jet_fuel.this.getApplicationContext(), "data saved", 0).show();
                                    } else if (jet_fuel.this.keadaan == 0) {
                                        Toast.makeText(jet_fuel.this.getApplicationContext(), "failed to send", 0).show();
                                        jet_fuel.this.keadaan = 1;
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

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r7, android.content.Intent r8) {
            /*
                r6 = this;
                r4 = 1
                r2 = 0
                java.lang.String r3 = "key"
                java.lang.String r0 = r8.getStringExtra(r3)     // Catch:{ Exception -> 0x0039 }
                r3 = -1
                int r5 = r0.hashCode()     // Catch:{ Exception -> 0x0039 }
                switch(r5) {
                    case -375011075: goto L_0x0015;
                    case 108388975: goto L_0x001e;
                    default: goto L_0x0010;
                }     // Catch:{ Exception -> 0x0039 }
            L_0x0010:
                r2 = r3
            L_0x0011:
                switch(r2) {
                    case 0: goto L_0x0028;
                    case 1: goto L_0x004f;
                    default: goto L_0x0014;
                }     // Catch:{ Exception -> 0x0039 }
            L_0x0014:
                return
            L_0x0015:
                java.lang.String r4 = "dataSaved"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x0039 }
                if (r4 == 0) goto L_0x0010
                goto L_0x0011
            L_0x001e:
                java.lang.String r2 = "recon"
                boolean r2 = r0.equals(r2)     // Catch:{ Exception -> 0x0039 }
                if (r2 == 0) goto L_0x0010
                r2 = r4
                goto L_0x0011
            L_0x0028:
                juken.android.com.juken_5.jet_fuel r2 = juken.android.com.juken_5.jet_fuel.this     // Catch:{ Exception -> 0x0039 }
                int r2 = r2.urutan_kirim     // Catch:{ Exception -> 0x0039 }
                if (r2 != 0) goto L_0x003e
                juken.android.com.juken_5.jet_fuel r2 = juken.android.com.juken_5.jet_fuel.this     // Catch:{ Exception -> 0x0039 }
                r3 = 1
                r2.urutan_kirim = r3     // Catch:{ Exception -> 0x0039 }
                juken.android.com.juken_5.jet_fuel r2 = juken.android.com.juken_5.jet_fuel.this     // Catch:{ Exception -> 0x0039 }
                r2.kirim_tps()     // Catch:{ Exception -> 0x0039 }
                goto L_0x0014
            L_0x0039:
                r1 = move-exception
                r1.printStackTrace()
                goto L_0x0014
            L_0x003e:
                juken.android.com.juken_5.jet_fuel r2 = juken.android.com.juken_5.jet_fuel.this     // Catch:{ Exception -> 0x0039 }
                android.content.Context r2 = r2.getApplicationContext()     // Catch:{ Exception -> 0x0039 }
                java.lang.String r3 = "data saved"
                r4 = 0
                android.widget.Toast r2 = android.widget.Toast.makeText(r2, r3, r4)     // Catch:{ Exception -> 0x0039 }
                r2.show()     // Catch:{ Exception -> 0x0039 }
                goto L_0x0014
            L_0x004f:
                juken.android.com.juken_5.jet_fuel r2 = juken.android.com.juken_5.jet_fuel.this     // Catch:{ Exception -> 0x0039 }
                r2.finish()     // Catch:{ Exception -> 0x0039 }
                goto L_0x0014
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.jet_fuel.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
