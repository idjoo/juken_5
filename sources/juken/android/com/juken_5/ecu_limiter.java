package juken.android.com.juken_5;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.github.anastr.speedviewlib.ProgressiveGauge;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import juken.android.com.juken_5.live.StaticClassExecute;
import juken.android.com.juken_5.live.live_awal;

public class ecu_limiter extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private static final String[] array1 = {"5000", "5100", "5200", "5300", "5400", "5500", "5600", "5700", "5800", "5900", "6000", "6100", "6200", "6300", "6400", "6500", "6600", "6700", "6800", "6900", "7000", "7100", "7200", "7300", "7400", "7500", "7600", "7700", "7800", "7900", "8000", "8100", "8200", "8300", "8400", "8500", "8600", "8700", "8800", "8900", "9000", "9100", "9200", "9300", "9400", "9500", "9600", "9700", "9800", "9900", "10000", "10100", "10200", "10300", "10400", "10500", "10600", "10700", "10800", "10900", "11000", "11100", "11200", "11300", "11400", "11500", "11600", "11700", "11800", "11900", "12000", "12100", "12200", "12300", "12400", "12500", "12600", "12700", "12800", "12900", "13000", "13100", "13200", "13300", "13400", "13500", "13600", "13700", "13800", "13900", "14000", "14100", "14200", "14300", "14400", "14500", "14600", "14700", "14800", "14900", "15000", "15100", "15200", "15300", "15400", "15500", "15600", "15700", "15800", "15900", "16000"};
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            ecu_limiter.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    private InputStream inputStream = null;
    int keadaan = 0;
    public ProgressiveGauge mGaugeView1;
    MyBroadCastReceiver myBroadCastReceiver;
    int nyambung = 1;
    private OutputStream outputStream = null;
    byte[] readBuffer;
    int readBufferPosition;
    Spinner rpm_ecu_limiter;
    Intent service;
    SharedPreferences sharedPreferences;
    private BluetoothSocket socket = null;
    volatile boolean stopWorker;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecu_limiter);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        this.mGaugeView1 = (ProgressiveGauge) findViewById(R.id.gauge_view1);
        this.mGaugeView1.setHovered(false);
        ((Button) findViewById(R.id.save_ecu_limiter)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ecu_limiter.this.keadaan = 0;
                if (ecu_limiter.this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
                    StaticClassExecute.bisa_execute = true;
                }
                ecu_limiter.this.kirim();
            }
        });
        ((Button) findViewById(R.id.exit_ecu_limiter)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    try {
                        if (ecu_limiter.this.nyambung == 1) {
                            ecu_limiter.this.resetConnection();
                            Thread.sleep(700);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (StaticClass.TipeKoneksi.equals("wifi")) {
                    ecu_limiter.this.unregisterReceiver(ecu_limiter.this.myBroadCastReceiver);
                }
                ecu_limiter.this.finish();
                if (ecu_limiter.this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    ecu_limiter.this.startActivityForResult(new Intent(ecu_limiter.this.getApplicationContext(), live_awal.class), 0);
                }
            }
        });
        this.rpm_ecu_limiter = (Spinner) findViewById(R.id.et_ecu_limiter);
        this.rpm_ecu_limiter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ecu_limiter.this.mGaugeView1.speedTo(Float.parseFloat(ecu_limiter.this.rpm_ecu_limiter.getSelectedItem().toString()), 1000);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LoadPreferences();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) <= 5 || keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.bolehPing = true;
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
        finish();
        if (this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
            startActivityForResult(new Intent(getApplicationContext(), live_awal.class), 0);
        }
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

    private void LoadPreferences() {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int taruh = (Integer.parseInt(this.sharedPreferences.getString("limiter", "limiter1")) / 100) - 50;
        this.rpm_ecu_limiter = (Spinner) findViewById(R.id.et_ecu_limiter);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 17367048, array1);
        adapter.setDropDownViewResource(17367049);
        this.rpm_ecu_limiter.setAdapter(adapter);
        this.rpm_ecu_limiter.setSelection(taruh);
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(this.sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    ecu_limiter.this.connect(perangkat);
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
        String code = this.rpm_ecu_limiter.getSelectedItem().toString();
        String code1 = "3605" + ";" + code + "\r\n";
        byte[] msgBuffer = code1.getBytes();
        SavePreferences("limiter", code);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = code1;
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
                                        Toast.makeText(ecu_limiter.this.getApplicationContext(), "data saved", 0).show();
                                    } else if (ecu_limiter.this.keadaan == 0) {
                                        Toast.makeText(ecu_limiter.this.getApplicationContext(), "failed to send", 0).show();
                                        ecu_limiter.this.keadaan = 1;
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
        public void onReceive(android.content.Context r6, android.content.Intent r7) {
            /*
                r5 = this;
                r2 = 0
                java.lang.String r3 = "key"
                java.lang.String r0 = r7.getStringExtra(r3)     // Catch:{ Exception -> 0x0032 }
                r3 = -1
                int r4 = r0.hashCode()     // Catch:{ Exception -> 0x0032 }
                switch(r4) {
                    case -375011075: goto L_0x0014;
                    case 108388975: goto L_0x001d;
                    default: goto L_0x000f;
                }     // Catch:{ Exception -> 0x0032 }
            L_0x000f:
                r2 = r3
            L_0x0010:
                switch(r2) {
                    case 0: goto L_0x0027;
                    case 1: goto L_0x0037;
                    default: goto L_0x0013;
                }     // Catch:{ Exception -> 0x0032 }
            L_0x0013:
                return
            L_0x0014:
                java.lang.String r4 = "dataSaved"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x0032 }
                if (r4 == 0) goto L_0x000f
                goto L_0x0010
            L_0x001d:
                java.lang.String r2 = "recon"
                boolean r2 = r0.equals(r2)     // Catch:{ Exception -> 0x0032 }
                if (r2 == 0) goto L_0x000f
                r2 = 1
                goto L_0x0010
            L_0x0027:
                java.lang.String r2 = "Saved"
                r3 = 0
                android.widget.Toast r2 = android.widget.Toast.makeText(r6, r2, r3)     // Catch:{ Exception -> 0x0032 }
                r2.show()     // Catch:{ Exception -> 0x0032 }
                goto L_0x0013
            L_0x0032:
                r1 = move-exception
                r1.printStackTrace()
                goto L_0x0013
            L_0x0037:
                juken.android.com.juken_5.ecu_limiter r2 = juken.android.com.juken_5.ecu_limiter.this     // Catch:{ Exception -> 0x0032 }
                juken.android.com.juken_5.ecu_limiter r3 = juken.android.com.juken_5.ecu_limiter.this     // Catch:{ Exception -> 0x0032 }
                juken.android.com.juken_5.ecu_limiter$MyBroadCastReceiver r3 = r3.myBroadCastReceiver     // Catch:{ Exception -> 0x0032 }
                r2.unregisterReceiver(r3)     // Catch:{ Exception -> 0x0032 }
                juken.android.com.juken_5.ecu_limiter r2 = juken.android.com.juken_5.ecu_limiter.this     // Catch:{ Exception -> 0x0032 }
                r2.finish()     // Catch:{ Exception -> 0x0032 }
                goto L_0x0013
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.ecu_limiter.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
