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
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class smart_key extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            smart_key.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    RadioButton disable;
    String ecu_tipe = "";
    RadioButton enable;
    private InputStream inputStream = null;
    int keadaan = 0;
    MyBroadCastReceiver myBroadCastReceiver;
    int nyambung = 1;
    private OutputStream outputStream = null;
    byte[] readBuffer;
    int readBufferPosition;
    int register = 0;
    RadioButton registrasi;
    Button save;
    Intent service;
    private BluetoothSocket socket = null;
    String status = "";
    TextView status_smart_key;
    volatile boolean stopWorker;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_key);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        this.enable = (RadioButton) findViewById(R.id.smart_key_enable);
        this.enable.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (smart_key.this.enable.isChecked()) {
                    smart_key.this.disable.setChecked(false);
                    smart_key.this.registrasi.setChecked(false);
                }
            }
        });
        this.disable = (RadioButton) findViewById(R.id.smart_key_disable);
        this.disable.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (smart_key.this.disable.isChecked()) {
                    smart_key.this.enable.setChecked(false);
                    smart_key.this.registrasi.setChecked(false);
                }
            }
        });
        this.registrasi = (RadioButton) findViewById(R.id.smart_key_registration);
        this.registrasi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (smart_key.this.registrasi.isChecked()) {
                    smart_key.this.disable.setChecked(false);
                    smart_key.this.enable.setChecked(false);
                }
            }
        });
        this.status_smart_key = (TextView) findViewById(R.id.et_status);
        ((Button) findViewById(R.id.exit_smart_key)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                smart_key.this.finish();
            }
        });
        this.save = (Button) findViewById(R.id.save_smart_key);
        this.save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (smart_key.this.ecu_tipe.equals("00") || smart_key.this.ecu_tipe.equals("128") || smart_key.this.ecu_tipe.equals("192")) {
                    Toast.makeText(smart_key.this.getApplicationContext(), "ecu model not supported", 0).show();
                } else if (smart_key.this.enable.isChecked()) {
                    if (smart_key.this.status.equals("0")) {
                        smart_key.this.jadi();
                        smart_key.this.status_smart_key.setText("ON");
                        smart_key.this.enable.setEnabled(false);
                        smart_key.this.disable.setEnabled(true);
                        smart_key.this.registrasi.setEnabled(true);
                        smart_key.this.register = 1;
                    }
                } else if (smart_key.this.disable.isChecked()) {
                    if (smart_key.this.status.equals("1")) {
                        smart_key.this.batal();
                        smart_key.this.status_smart_key.setText("OFF");
                        smart_key.this.enable.setEnabled(true);
                        smart_key.this.disable.setEnabled(false);
                        smart_key.this.registrasi.setEnabled(false);
                        smart_key.this.register = 0;
                    }
                } else if (smart_key.this.registrasi.isChecked() && smart_key.this.status.equals("1") && smart_key.this.register == 1) {
                    smart_key.this.register();
                }
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
                if (this.bluetooth.isEnabled() && this.nyambung == 1) {
                    resetConnection();
                    Thread.sleep(500);
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
        this.status = sharedPreferences.getString("imob_en", "imob_en1").replace("\r", "");
        this.ecu_tipe = sharedPreferences.getString("ecu_model", "ecu_model1").replace("\r", "");
        this.enable = (RadioButton) findViewById(R.id.smart_key_enable);
        this.disable = (RadioButton) findViewById(R.id.smart_key_disable);
        this.registrasi = (RadioButton) findViewById(R.id.smart_key_registration);
        this.status_smart_key = (TextView) findViewById(R.id.et_status);
        if (this.status.equals("0")) {
            this.status_smart_key.setText("OFF");
            this.enable.setEnabled(true);
            this.disable.setEnabled(false);
            this.registrasi.setEnabled(false);
        } else {
            this.status_smart_key.setText("ON");
            this.enable.setEnabled(false);
            this.disable.setEnabled(true);
            this.registrasi.setEnabled(true);
        }
        if (this.ecu_tipe.equals("00") || this.ecu_tipe.equals("128") || this.ecu_tipe.equals("192")) {
            this.status_smart_key.setText("N/A");
            this.enable.setEnabled(false);
            this.disable.setEnabled(false);
            this.registrasi.setEnabled(false);
        }
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    smart_key.this.connect(perangkat);
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

    public void jadi() {
        String code2 = "3609" + ";" + "1" + "\r\n";
        byte[] msgBuffer = code2.getBytes();
        SavePreferences("imob_en", "1");
        this.status = "1";
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = code2;
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

    public void batal() {
        String code2 = "3609" + ";" + "0" + "\r\n";
        byte[] msgBuffer = code2.getBytes();
        SavePreferences("imob_en", "0");
        this.status = "0";
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = code2;
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

    public void register() {
        String code2 = "3609" + ";" + "2" + "\r\n";
        byte[] msgBuffer = code2.getBytes();
        SavePreferences("imob_en", "1");
        this.status = "1";
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = code2;
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
                                        Toast.makeText(smart_key.this.getApplicationContext(), "data saved", 0).show();
                                    } else if (smart_key.this.keadaan == 0) {
                                        Toast.makeText(smart_key.this.getApplicationContext(), "failed to send", 0).show();
                                        smart_key.this.keadaan = 1;
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
                juken.android.com.juken_5.smart_key r2 = juken.android.com.juken_5.smart_key.this     // Catch:{ Exception -> 0x0032 }
                r2.finish()     // Catch:{ Exception -> 0x0032 }
                goto L_0x0013
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.smart_key.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
