package juken.android.com.juken_5;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import juken.android.com.juken_5.live.StaticClassExecute;
import juken.android.com.juken_5.live.live_awal;

public class deceleration_fuel extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            deceleration_fuel.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    Button exit;
    private InputStream inputStream = null;
    int keadaan = 0;
    MyBroadCastReceiver myBroadCastReceiver;
    EditText nilai_deselerasi;
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
        setContentView(R.layout.deceleration_fuel);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        this.nilai_deselerasi = (EditText) findViewById(R.id.et_deceleration_fuel);
        this.nilai_deselerasi.setFilters(new InputFilter[]{new range_int_min("-100", "100")});
        this.exit = (Button) findViewById(R.id.exit_deceleration_fuel);
        this.exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    deceleration_fuel.this.resetConnection();
                } else if (StaticClass.TipeKoneksi.equals("wifi")) {
                    deceleration_fuel.this.unregisterReceiver(deceleration_fuel.this.myBroadCastReceiver);
                }
                deceleration_fuel.this.finish();
                if (deceleration_fuel.this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    deceleration_fuel.this.startActivityForResult(new Intent(deceleration_fuel.this.getApplicationContext(), live_awal.class), 0);
                }
            }
        });
        this.save = (Button) findViewById(R.id.save_deceleration_fuel);
        this.save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deceleration_fuel.this.keadaan = 0;
                if (MappingHandle.NamaFileFuel.equals("Fuel Correction - ECU Core 1") || MappingHandle.NamaFileFuel.equals("Fuel Correction - ECU Core 2")) {
                    for (int j = 0; j < 29; j++) {
                        MappingHandle.list_fuel.remove(j);
                        MappingHandle.list_fuel.add(j, deceleration_fuel.this.nilai_deselerasi.getText().toString());
                    }
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(deceleration_fuel.this);
                    alert.setTitle((CharSequence) "Alert!");
                    alert.setMessage((CharSequence) deceleration_fuel.this.getString(R.string.AlertDeselerasi));
                    alert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.create().show();
                }
                if (deceleration_fuel.this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
                    StaticClassExecute.bisa_execute = true;
                    for (int j2 = 0; j2 < 29; j2++) {
                        MappingHandle.list_fuel_live.remove(j2);
                        MappingHandle.list_fuel_live.add(j2, deceleration_fuel.this.nilai_deselerasi.getText().toString());
                    }
                }
                deceleration_fuel.this.kirim();
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
            finish();
            if (this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
                startActivityForResult(new Intent(getApplicationContext(), live_awal.class), 0);
            }
        } else if (StaticClass.TipeKoneksi.equals("bt")) {
            try {
                if (this.nyambung == 1) {
                    resetConnection();
                    Thread.sleep(500);
                    finish();
                    if (this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
                        startActivityForResult(new Intent(getApplicationContext(), live_awal.class), 0);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            finish();
        }
    }

    private void LoadPreferences() {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String nilai = this.sharedPreferences.getString("dec_fuel", "dec_fuel1");
        this.nilai_deselerasi = (EditText) findViewById(R.id.et_deceleration_fuel);
        this.nilai_deselerasi.setText(nilai);
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(this.sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    deceleration_fuel.this.connect(perangkat);
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

    public void kirim() {
        String code = this.nilai_deselerasi.getText().toString();
        String code2 = "3611" + ";" + code + "\r\n";
        byte[] msgBuffer = code2.getBytes();
        SavePreferences("dec_fuel", code);
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
                                        Toast.makeText(deceleration_fuel.this.getApplicationContext(), "data saved", 0).show();
                                    } else if (deceleration_fuel.this.keadaan == 0) {
                                        Toast.makeText(deceleration_fuel.this.getApplicationContext(), "failed to send", 0).show();
                                        deceleration_fuel.this.keadaan = 1;
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
                juken.android.com.juken_5.deceleration_fuel r2 = juken.android.com.juken_5.deceleration_fuel.this     // Catch:{ Exception -> 0x0032 }
                juken.android.com.juken_5.deceleration_fuel r3 = juken.android.com.juken_5.deceleration_fuel.this     // Catch:{ Exception -> 0x0032 }
                juken.android.com.juken_5.deceleration_fuel$MyBroadCastReceiver r3 = r3.myBroadCastReceiver     // Catch:{ Exception -> 0x0032 }
                r2.unregisterReceiver(r3)     // Catch:{ Exception -> 0x0032 }
                juken.android.com.juken_5.deceleration_fuel r2 = juken.android.com.juken_5.deceleration_fuel.this     // Catch:{ Exception -> 0x0032 }
                r2.finish()     // Catch:{ Exception -> 0x0032 }
                goto L_0x0013
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.deceleration_fuel.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
