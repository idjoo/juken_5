package juken.android.com.juken_5;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.github.anastr.speedviewlib.ProgressiveGauge;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class calibration extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            calibration.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    int cek = 0;
    int cek1 = 0;
    boolean fromRecon = false;
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    double hitung_tps = 0.0d;
    private InputStream inputStream = null;
    int keadaan = 0;
    public ProgressiveGauge mGaugeView1;
    TextView minimum;
    TextView minimum_open;
    MyBroadCastReceiver myBroadCastReceiver;
    int nyambung = 1;
    private OutputStream outputStream = null;
    int posisi = 0;
    byte[] readBuffer;
    int readBufferPosition;
    Button save_calibration_close;
    Button save_calibration_open;
    private final Runnable sendData = new Runnable() {
        public void run() {
            try {
                if (calibration.this.yang_disave == 0) {
                    calibration.this.kirim_awal();
                } else if (calibration.this.yang_disave == 1) {
                    calibration.this.yang_disave = 0;
                    calibration.this.kirim_initial();
                } else if (calibration.this.yang_disave == 2) {
                    calibration.this.yang_disave = 0;
                    calibration.this.kirim_wot();
                }
                calibration.this.handler.postDelayed(this, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Intent service;
    private BluetoothSocket socket = null;
    volatile boolean stopWorker;
    double tps_high1 = 0.0d;
    double tps_low1 = 0.0d;
    TextView value;
    TextView value_open;
    TextView voltage;
    TextView voltage_open;
    Thread workerThread;
    int yang_disave = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calibration);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        TextView tutorial = (TextView) findViewById(R.id.tutorial);
        tutorial.setText(tutorial.getText().toString().replace("%%", "%"));
        this.voltage = (TextView) findViewById(R.id.nilai_volt);
        this.voltage_open = (TextView) findViewById(R.id.nilai_volt_open);
        this.minimum = (TextView) findViewById(R.id.nilai_minimum);
        this.minimum_open = (TextView) findViewById(R.id.nilai_minimum_open);
        this.value = (TextView) findViewById(R.id.nilai_value);
        this.value_open = (TextView) findViewById(R.id.nilai_value_open);
        this.mGaugeView1 = (ProgressiveGauge) findViewById(R.id.gauge_view1);
        this.mGaugeView1.setHovered(false);
        this.save_calibration_close = (Button) findViewById(R.id.save_calibration_close);
        this.save_calibration_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calibration.this.keadaan = 0;
                calibration.this.yang_disave = 1;
            }
        });
        this.save_calibration_open = (Button) findViewById(R.id.save_calibration_open);
        this.save_calibration_open.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calibration.this.keadaan = 0;
                calibration.this.yang_disave = 2;
            }
        });
        ((Button) findViewById(R.id.exit_calibration)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calibration.this.finish();
            }
        });
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.handler.post(this.sendData);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            LoadPreferences();
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
        final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("android.bluetooth.device.extra.DEVICE", "rame"));
        new Thread() {
            public void run() {
                calibration.this.connect(perangkat);
            }
        }.start();
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

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.sendData);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            if (!this.fromRecon) {
                StaticClass.bolehPing = true;
            }
            unregisterReceiver(this.myBroadCastReceiver);
        } else if (StaticClass.TipeKoneksi.equals("bt")) {
            try {
                if (this.bluetooth.isEnabled() && this.nyambung == 1) {
                    resetConnection();
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void kirim_awal() {
        this.cek = 0;
        byte[] msgBuffer = "4601\r\n".getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = "4601\r\n";
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
                                        switch (calibration.this.posisi) {
                                            case 0:
                                                if (data.contains("\r")) {
                                                    calibration.this.posisi = 0;
                                                    return;
                                                } else {
                                                    calibration.this.posisi = 1;
                                                    return;
                                                }
                                            case 1:
                                                calibration.this.hitung_tps = Double.parseDouble(data);
                                                calibration.this.posisi = 2;
                                                return;
                                            case 2:
                                                calibration.this.tps_low1 = Double.parseDouble(data);
                                                calibration.this.posisi = 3;
                                                return;
                                            case 3:
                                                calibration.this.tps_high1 = Double.parseDouble(data);
                                                Double tegangan = Double.valueOf((calibration.this.hitung_tps * 5.0d) / 1023.0d);
                                                calibration.this.value.setText(String.valueOf(calibration.this.hitung_tps));
                                                calibration.this.value_open.setText(String.valueOf(calibration.this.hitung_tps));
                                                calibration.this.minimum.setText(String.valueOf(calibration.this.tps_low1));
                                                calibration.this.minimum_open.setText(String.valueOf(calibration.this.tps_high1));
                                                calibration.this.voltage.setText(String.format("%.2f", new Object[]{tegangan}) + " V");
                                                calibration.this.voltage_open.setText(String.format("%.2f", new Object[]{tegangan}) + " V");
                                                int buat_gauge = (int) (((calibration.this.hitung_tps - calibration.this.tps_low1) * 100.0d) / (calibration.this.tps_high1 - calibration.this.tps_low1));
                                                if (buat_gauge > 100) {
                                                    buat_gauge = 100;
                                                } else if (buat_gauge < 0) {
                                                    buat_gauge = 0;
                                                }
                                                calibration.this.mGaugeView1.speedTo((float) buat_gauge, 10);
                                                calibration.this.posisi = 0;
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
                            if (b == 10) {
                                byte[] encodedBytes2 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes2, 0, encodedBytes2.length);
                                final String data2 = new String(encodedBytes2, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (data2.replaceAll("\\r", "").equals("1A00")) {
                                            Toast.makeText(calibration.this.getApplicationContext(), "saved", 0).show();
                                        } else if (calibration.this.keadaan < 2) {
                                            Toast.makeText(calibration.this.getApplicationContext(), "failed to send", 0).show();
                                            calibration.this.keadaan++;
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr2 = this.readBuffer;
                                int i3 = this.readBufferPosition;
                                this.readBufferPosition = i3 + 1;
                                bArr2[i3] = b;
                            }
                        } else if (this.keadaan < 2) {
                            Toast.makeText(getApplicationContext(), "failed to send", 0).show();
                            this.keadaan++;
                        }
                    }
                }
            } catch (IOException e) {
                this.stopWorker = true;
            }
        }
    }

    public void kirim_initial() {
        this.cek = 1;
        byte[] msgBuffer = "3612\r\n".getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = "3612\r\n";
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

    public void kirim_wot() {
        this.cek = 1;
        byte[] msgBuffer = "3613\r\n".getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = "3613\r\n";
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

    class MyBroadCastReceiver extends BroadcastReceiver {
        MyBroadCastReceiver() {
        }

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r11, android.content.Intent r12) {
            /*
                r10 = this;
                r6 = 1
                r4 = 0
                java.lang.String r5 = "key"
                java.lang.String r1 = r12.getStringExtra(r5)     // Catch:{ Exception -> 0x003d }
                r5 = -1
                int r7 = r1.hashCode()     // Catch:{ Exception -> 0x003d }
                switch(r7) {
                    case -375011075: goto L_0x0015;
                    case 108388975: goto L_0x0028;
                    case 399704357: goto L_0x001e;
                    default: goto L_0x0010;
                }     // Catch:{ Exception -> 0x003d }
            L_0x0010:
                r4 = r5
            L_0x0011:
                switch(r4) {
                    case 0: goto L_0x0032;
                    case 1: goto L_0x0042;
                    case 2: goto L_0x010a;
                    default: goto L_0x0014;
                }     // Catch:{ Exception -> 0x003d }
            L_0x0014:
                return
            L_0x0015:
                java.lang.String r6 = "dataSaved"
                boolean r6 = r1.equals(r6)     // Catch:{ Exception -> 0x003d }
                if (r6 == 0) goto L_0x0010
                goto L_0x0011
            L_0x001e:
                java.lang.String r4 = "calibrationProcess"
                boolean r4 = r1.equals(r4)     // Catch:{ Exception -> 0x003d }
                if (r4 == 0) goto L_0x0010
                r4 = r6
                goto L_0x0011
            L_0x0028:
                java.lang.String r4 = "recon"
                boolean r4 = r1.equals(r4)     // Catch:{ Exception -> 0x003d }
                if (r4 == 0) goto L_0x0010
                r4 = 2
                goto L_0x0011
            L_0x0032:
                java.lang.String r4 = "Saved"
                r5 = 0
                android.widget.Toast r4 = android.widget.Toast.makeText(r11, r4, r5)     // Catch:{ Exception -> 0x003d }
                r4.show()     // Catch:{ Exception -> 0x003d }
                goto L_0x0014
            L_0x003d:
                r2 = move-exception
                r2.printStackTrace()
                goto L_0x0014
            L_0x0042:
                java.lang.Double r4 = juken.android.com.juken_5.StaticClass.hitung_tps     // Catch:{ Exception -> 0x003d }
                double r4 = r4.doubleValue()     // Catch:{ Exception -> 0x003d }
                r6 = 4617315517961601024(0x4014000000000000, double:5.0)
                double r4 = r4 * r6
                r6 = 4652209618980700160(0x408ff80000000000, double:1023.0)
                double r4 = r4 / r6
                java.lang.Double r3 = java.lang.Double.valueOf(r4)     // Catch:{ Exception -> 0x003d }
                juken.android.com.juken_5.calibration r4 = juken.android.com.juken_5.calibration.this     // Catch:{ Exception -> 0x003d }
                android.widget.TextView r4 = r4.value     // Catch:{ Exception -> 0x003d }
                java.lang.Double r5 = juken.android.com.juken_5.StaticClass.hitung_tps     // Catch:{ Exception -> 0x003d }
                java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x003d }
                r4.setText(r5)     // Catch:{ Exception -> 0x003d }
                juken.android.com.juken_5.calibration r4 = juken.android.com.juken_5.calibration.this     // Catch:{ Exception -> 0x003d }
                android.widget.TextView r4 = r4.value_open     // Catch:{ Exception -> 0x003d }
                java.lang.Double r5 = juken.android.com.juken_5.StaticClass.hitung_tps     // Catch:{ Exception -> 0x003d }
                java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x003d }
                r4.setText(r5)     // Catch:{ Exception -> 0x003d }
                juken.android.com.juken_5.calibration r4 = juken.android.com.juken_5.calibration.this     // Catch:{ Exception -> 0x003d }
                android.widget.TextView r4 = r4.minimum     // Catch:{ Exception -> 0x003d }
                java.lang.Double r5 = juken.android.com.juken_5.StaticClass.tps_low1     // Catch:{ Exception -> 0x003d }
                java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x003d }
                r4.setText(r5)     // Catch:{ Exception -> 0x003d }
                juken.android.com.juken_5.calibration r4 = juken.android.com.juken_5.calibration.this     // Catch:{ Exception -> 0x003d }
                android.widget.TextView r4 = r4.minimum_open     // Catch:{ Exception -> 0x003d }
                java.lang.Double r5 = juken.android.com.juken_5.StaticClass.tps_high1     // Catch:{ Exception -> 0x003d }
                java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x003d }
                r4.setText(r5)     // Catch:{ Exception -> 0x003d }
                juken.android.com.juken_5.calibration r4 = juken.android.com.juken_5.calibration.this     // Catch:{ Exception -> 0x003d }
                android.widget.TextView r4 = r4.voltage     // Catch:{ Exception -> 0x003d }
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x003d }
                r5.<init>()     // Catch:{ Exception -> 0x003d }
                java.lang.String r6 = "%.2f"
                r7 = 1
                java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x003d }
                r8 = 0
                r7[r8] = r3     // Catch:{ Exception -> 0x003d }
                java.lang.String r6 = java.lang.String.format(r6, r7)     // Catch:{ Exception -> 0x003d }
                java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x003d }
                java.lang.String r6 = " V"
                java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x003d }
                java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x003d }
                r4.setText(r5)     // Catch:{ Exception -> 0x003d }
                juken.android.com.juken_5.calibration r4 = juken.android.com.juken_5.calibration.this     // Catch:{ Exception -> 0x003d }
                android.widget.TextView r4 = r4.voltage_open     // Catch:{ Exception -> 0x003d }
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x003d }
                r5.<init>()     // Catch:{ Exception -> 0x003d }
                java.lang.String r6 = "%.2f"
                r7 = 1
                java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x003d }
                r8 = 0
                r7[r8] = r3     // Catch:{ Exception -> 0x003d }
                java.lang.String r6 = java.lang.String.format(r6, r7)     // Catch:{ Exception -> 0x003d }
                java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x003d }
                java.lang.String r6 = " V"
                java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x003d }
                java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x003d }
                r4.setText(r5)     // Catch:{ Exception -> 0x003d }
                java.lang.Double r4 = juken.android.com.juken_5.StaticClass.hitung_tps     // Catch:{ Exception -> 0x003d }
                double r4 = r4.doubleValue()     // Catch:{ Exception -> 0x003d }
                java.lang.Double r6 = juken.android.com.juken_5.StaticClass.tps_low1     // Catch:{ Exception -> 0x003d }
                double r6 = r6.doubleValue()     // Catch:{ Exception -> 0x003d }
                double r4 = r4 - r6
                r6 = 4636737291354636288(0x4059000000000000, double:100.0)
                double r4 = r4 * r6
                java.lang.Double r6 = juken.android.com.juken_5.StaticClass.tps_high1     // Catch:{ Exception -> 0x003d }
                double r6 = r6.doubleValue()     // Catch:{ Exception -> 0x003d }
                java.lang.Double r8 = juken.android.com.juken_5.StaticClass.tps_low1     // Catch:{ Exception -> 0x003d }
                double r8 = r8.doubleValue()     // Catch:{ Exception -> 0x003d }
                double r6 = r6 - r8
                double r4 = r4 / r6
                int r0 = (int) r4     // Catch:{ Exception -> 0x003d }
                r4 = 100
                if (r0 <= r4) goto L_0x0106
                r0 = 100
            L_0x00fa:
                juken.android.com.juken_5.calibration r4 = juken.android.com.juken_5.calibration.this     // Catch:{ Exception -> 0x003d }
                com.github.anastr.speedviewlib.ProgressiveGauge r4 = r4.mGaugeView1     // Catch:{ Exception -> 0x003d }
                float r5 = (float) r0     // Catch:{ Exception -> 0x003d }
                r6 = 10
                r4.speedTo(r5, r6)     // Catch:{ Exception -> 0x003d }
                goto L_0x0014
            L_0x0106:
                if (r0 >= 0) goto L_0x00fa
                r0 = 0
                goto L_0x00fa
            L_0x010a:
                juken.android.com.juken_5.calibration r4 = juken.android.com.juken_5.calibration.this     // Catch:{ Exception -> 0x003d }
                r5 = 1
                r4.fromRecon = r5     // Catch:{ Exception -> 0x003d }
                juken.android.com.juken_5.calibration r4 = juken.android.com.juken_5.calibration.this     // Catch:{ Exception -> 0x003d }
                r4.finish()     // Catch:{ Exception -> 0x003d }
                goto L_0x0014
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.calibration.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
