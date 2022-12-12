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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class vvtc extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private static final String[] array1 = {"1000", "1250", "1500", "1750", "2000", "2250", "2500", "2750", "3000", "3250", "3500", "3750", "4000", "4250", "4500", "4750", "5000", "5250", "5500", "5750", "6000", "6250", "6500", "6750", "7000", "7250", "7500", "7750", "8000", "8250", "8500", "8750", "9000", "9250", "9500", "9750", "10000", "10250", "10500", "10750", "11000", "11250", "11500", "11750", "12000", "12250", "12500", "12750", "13000", "13250", "13500", "13750", "14000", "14250", "14500", "14750", "15000", "15250", "15500", "15750", "16000"};
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            vvtc.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    Button exit;
    private InputStream inputStream = null;
    int keadaan = 0;
    MyBroadCastReceiver myBroadCastReceiver;
    Spinner nilai_vvtc;
    int nyambung = 1;
    private OutputStream outputStream = null;
    byte[] readBuffer;
    int readBufferPosition;
    Button save;
    Intent service;
    private BluetoothSocket socket = null;
    volatile boolean stopWorker;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vvtc);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        this.save = (Button) findViewById(R.id.save_vvtc);
        this.save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vvtc.this.kirim();
            }
        });
        this.exit = (Button) findViewById(R.id.exit_vvtc);
        this.exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vvtc.this.finish();
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
        String nilai = sharedPreferences.getString("VVTC", "VVTC1");
        if (nilai.equals("0")) {
            this.save = (Button) findViewById(R.id.save_vvtc);
            this.save.setEnabled(false);
        }
        int hitung = (Integer.parseInt(nilai) / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) - 4;
        this.nilai_vvtc = (Spinner) findViewById(R.id.et_vvtc);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 17367048, array1);
        adapter.setDropDownViewResource(17367049);
        this.nilai_vvtc.setAdapter(adapter);
        this.nilai_vvtc.setSelection(hitung);
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    vvtc.this.connect(perangkat);
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
        String code = this.nilai_vvtc.getSelectedItem().toString();
        String code1 = "3617" + ";" + code + "\r\n";
        byte[] msgBuffer = code1.getBytes();
        SavePreferences("VVTC", code);
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
                                        Toast.makeText(vvtc.this.getApplicationContext(), "data saved", 0).show();
                                    } else {
                                        Toast.makeText(vvtc.this.getApplicationContext(), "failed to send", 0).show();
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
                juken.android.com.juken_5.vvtc r2 = juken.android.com.juken_5.vvtc.this     // Catch:{ Exception -> 0x0032 }
                r2.finish()     // Catch:{ Exception -> 0x0032 }
                goto L_0x0013
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.vvtc.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
