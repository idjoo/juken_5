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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class reset_factory extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            reset_factory.this.listen();
        }
    };
    CheckBox aktif_reset;
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    private InputStream inputStream = null;
    int keadaan = 0;
    MyBroadCastReceiver myBroadCastReceiver;
    int nyambung = 1;
    private OutputStream outputStream = null;
    byte[] readBuffer;
    int readBufferPosition;
    Button save;
    Intent service;
    private BluetoothSocket socket = null;
    volatile boolean stopWorker;
    Boolean sudah_reset = false;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_factory);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        this.aktif_reset = (CheckBox) findViewById(R.id.checkbox_reset);
        this.aktif_reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    reset_factory.this.save.setEnabled(true);
                } else {
                    reset_factory.this.save.setEnabled(false);
                }
            }
        });
        this.save = (Button) findViewById(R.id.save_reset);
        this.save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset_factory.this.keadaan = 0;
                reset_factory.this.kirim();
            }
        });
        ((Button) findViewById(R.id.exit_reset)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (reset_factory.this.sudah_reset.booleanValue()) {
                    reset_factory.this.SavePreferences("ack_awal", "1");
                }
                if (StaticClass.TipeKoneksi.equals("wifi")) {
                    reset_factory.this.unregisterReceiver(reset_factory.this.myBroadCastReceiver);
                } else if (StaticClass.TipeKoneksi.equals("bt")) {
                    try {
                        if (reset_factory.this.nyambung == 1) {
                            reset_factory.this.resetConnection();
                            Thread.sleep(200);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                reset_factory.this.finish();
                reset_factory.this.startActivityForResult(new Intent(reset_factory.this.getApplicationContext(), main_menu.class), 0);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LoadPreferences();
    }

    private void LoadPreferences() {
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    reset_factory.this.connect(perangkat);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) <= 5 || keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        if (this.sudah_reset.booleanValue()) {
            SavePreferences("ack_awal", "1");
        }
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            unregisterReceiver(this.myBroadCastReceiver);
        } else if (StaticClass.TipeKoneksi.equals("bt")) {
            try {
                if (this.nyambung == 1) {
                    resetConnection();
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        finish();
        startActivityForResult(new Intent(getApplicationContext(), main_menu.class), 0);
    }

    public void kirim() {
        byte[] msgBuffer = "260C\r\n".getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = "260C\r\n";
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

    /* access modifiers changed from: private */
    public void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
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
                        if (b == 10) {
                            byte[] encodedBytes = new byte[this.readBufferPosition];
                            System.arraycopy(this.readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                            final String data = new String(encodedBytes, "US-ASCII");
                            this.readBufferPosition = 0;
                            this._handler.post(new Runnable() {
                                public void run() {
                                    if (data.replaceAll("\\n", "").replaceAll("\\r", "").equals("1A00")) {
                                        MappingHandle.list_fuel_ganti.clear();
                                        MappingHandle.list_fuel_live_ganti.clear();
                                        MappingHandle.list_ignition_ganti.clear();
                                        MappingHandle.list_ignition_live_ganti.clear();
                                        reset_factory.this.sudah_reset = true;
                                        Toast.makeText(reset_factory.this.getApplicationContext(), "reset success", 0).show();
                                    } else if (reset_factory.this.keadaan == 0) {
                                        Toast.makeText(reset_factory.this.getApplicationContext(), "failed to send", 0).show();
                                        reset_factory.this.keadaan = 1;
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
                java.lang.String r0 = r8.getStringExtra(r3)     // Catch:{ Exception -> 0x0056 }
                r3 = -1
                int r5 = r0.hashCode()     // Catch:{ Exception -> 0x0056 }
                switch(r5) {
                    case -375011075: goto L_0x0015;
                    case 108388975: goto L_0x001e;
                    default: goto L_0x0010;
                }     // Catch:{ Exception -> 0x0056 }
            L_0x0010:
                r2 = r3
            L_0x0011:
                switch(r2) {
                    case 0: goto L_0x0028;
                    case 1: goto L_0x005b;
                    default: goto L_0x0014;
                }     // Catch:{ Exception -> 0x0056 }
            L_0x0014:
                return
            L_0x0015:
                java.lang.String r4 = "dataSaved"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x0056 }
                if (r4 == 0) goto L_0x0010
                goto L_0x0011
            L_0x001e:
                java.lang.String r2 = "recon"
                boolean r2 = r0.equals(r2)     // Catch:{ Exception -> 0x0056 }
                if (r2 == 0) goto L_0x0010
                r2 = r4
                goto L_0x0011
            L_0x0028:
                java.util.ArrayList<java.lang.String> r2 = juken.android.com.juken_5.MappingHandle.list_fuel_ganti     // Catch:{ Exception -> 0x0056 }
                r2.clear()     // Catch:{ Exception -> 0x0056 }
                java.util.ArrayList<java.lang.String> r2 = juken.android.com.juken_5.MappingHandle.list_fuel_live_ganti     // Catch:{ Exception -> 0x0056 }
                r2.clear()     // Catch:{ Exception -> 0x0056 }
                java.util.ArrayList<java.lang.String> r2 = juken.android.com.juken_5.MappingHandle.list_ignition_ganti     // Catch:{ Exception -> 0x0056 }
                r2.clear()     // Catch:{ Exception -> 0x0056 }
                java.util.ArrayList<java.lang.String> r2 = juken.android.com.juken_5.MappingHandle.list_ignition_live_ganti     // Catch:{ Exception -> 0x0056 }
                r2.clear()     // Catch:{ Exception -> 0x0056 }
                juken.android.com.juken_5.reset_factory r2 = juken.android.com.juken_5.reset_factory.this     // Catch:{ Exception -> 0x0056 }
                r3 = 1
                java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)     // Catch:{ Exception -> 0x0056 }
                r2.sudah_reset = r3     // Catch:{ Exception -> 0x0056 }
                juken.android.com.juken_5.reset_factory r2 = juken.android.com.juken_5.reset_factory.this     // Catch:{ Exception -> 0x0056 }
                android.content.Context r2 = r2.getApplicationContext()     // Catch:{ Exception -> 0x0056 }
                java.lang.String r3 = "reset success"
                r4 = 0
                android.widget.Toast r2 = android.widget.Toast.makeText(r2, r3, r4)     // Catch:{ Exception -> 0x0056 }
                r2.show()     // Catch:{ Exception -> 0x0056 }
                goto L_0x0014
            L_0x0056:
                r1 = move-exception
                r1.printStackTrace()
                goto L_0x0014
            L_0x005b:
                juken.android.com.juken_5.reset_factory r2 = juken.android.com.juken_5.reset_factory.this     // Catch:{ Exception -> 0x0056 }
                juken.android.com.juken_5.reset_factory r3 = juken.android.com.juken_5.reset_factory.this     // Catch:{ Exception -> 0x0056 }
                juken.android.com.juken_5.reset_factory$MyBroadCastReceiver r3 = r3.myBroadCastReceiver     // Catch:{ Exception -> 0x0056 }
                r2.unregisterReceiver(r3)     // Catch:{ Exception -> 0x0056 }
                juken.android.com.juken_5.reset_factory r2 = juken.android.com.juken_5.reset_factory.this     // Catch:{ Exception -> 0x0056 }
                r2.finish()     // Catch:{ Exception -> 0x0056 }
                goto L_0x0014
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.reset_factory.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
