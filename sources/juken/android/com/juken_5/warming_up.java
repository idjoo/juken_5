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
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import juken.android.com.juken_5.live.StaticClassExecute;
import juken.android.com.juken_5.live.live_awal;

public class warming_up extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            warming_up.this.listen();
        }
    };
    /* access modifiers changed from: private */
    public BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    EditText et_warming_up;
    private InputStream inputStream = null;
    int keadaan = 0;
    MyBroadCastReceiver myBroadCastReceiver;
    int nyambung = 1;
    private OutputStream outputStream = null;
    byte[] readBuffer;
    int readBufferPosition;
    Button save;
    Intent service;
    SharedPreferences sharedPreferences;
    private BluetoothSocket socket = null;
    volatile boolean stopWorker;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warming_up);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        TextView tutorial = (TextView) findViewById(R.id.tutorial);
        tutorial.setText(tutorial.getText().toString().replace("%%", "%"));
        this.et_warming_up = (EditText) findViewById(R.id.et_warm_up);
        this.et_warming_up.setFilters(new InputFilter[]{new range_int_min("-100", "100")});
        this.save = (Button) findViewById(R.id.save_warming_up);
        this.save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                warming_up.this.keadaan = 0;
                if (warming_up.this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
                    StaticClassExecute.bisa_execute = true;
                }
                warming_up.this.kirim();
            }
        });
        ((Button) findViewById(R.id.exit_warming_up)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("wifi")) {
                    warming_up.this.unregisterReceiver(warming_up.this.myBroadCastReceiver);
                } else if (StaticClass.TipeKoneksi.equals("bt")) {
                    try {
                        if (warming_up.this.bluetooth.isEnabled() && warming_up.this.nyambung == 1) {
                            warming_up.this.resetConnection();
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                warming_up.this.finish();
                if (warming_up.this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    warming_up.this.startActivityForResult(new Intent(warming_up.this.getApplicationContext(), live_awal.class), 0);
                }
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
                if (this.bluetooth.isEnabled() && this.nyambung == 1) {
                    resetConnection();
                    Thread.sleep(500);
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
        String nilai = this.sharedPreferences.getString("warming_up", "warming_up1");
        this.et_warming_up = (EditText) findViewById(R.id.et_warm_up);
        this.et_warming_up.setText(nilai);
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(this.sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    warming_up.this.connect(perangkat);
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
        String code = this.et_warming_up.getText().toString();
        String code1 = "360E" + ";" + code + "\r\n";
        byte[] msgBuffer = code1.getBytes();
        SavePreferences("warming_up", code);
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
                                        Toast.makeText(warming_up.this.getApplicationContext(), "data saved", 0).show();
                                    } else if (warming_up.this.keadaan == 0) {
                                        Toast.makeText(warming_up.this.getApplicationContext(), "failed to send", 0).show();
                                        warming_up.this.keadaan = 1;
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
                juken.android.com.juken_5.warming_up r2 = juken.android.com.juken_5.warming_up.this     // Catch:{ Exception -> 0x0032 }
                juken.android.com.juken_5.warming_up r3 = juken.android.com.juken_5.warming_up.this     // Catch:{ Exception -> 0x0032 }
                juken.android.com.juken_5.warming_up$MyBroadCastReceiver r3 = r3.myBroadCastReceiver     // Catch:{ Exception -> 0x0032 }
                r2.unregisterReceiver(r3)     // Catch:{ Exception -> 0x0032 }
                juken.android.com.juken_5.warming_up r2 = juken.android.com.juken_5.warming_up.this     // Catch:{ Exception -> 0x0032 }
                r2.finish()     // Catch:{ Exception -> 0x0032 }
                goto L_0x0013
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.warming_up.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
