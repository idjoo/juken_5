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
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class auto_correction extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            auto_correction.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    Button exit;
    private InputStream inputStream = null;
    int keadaan = 0;
    MyBroadCastReceiver myBroadCastReceiver;
    EditText nilai_ki;
    EditText nilai_ti;
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
        setContentView(R.layout.auto_correction);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        TextView tutorial = (TextView) findViewById(R.id.tutorial);
        tutorial.setText(tutorial.getText().toString().replace("%%", "%"));
        this.nilai_ti = (EditText) findViewById(R.id.ti);
        this.nilai_ti.setFilters(new InputFilter[]{new range_int1("0", "120")});
        this.nilai_ki = (EditText) findViewById(R.id.ki);
        this.nilai_ki.setFilters(new InputFilter[]{new range_float2(2)});
        this.nilai_ki.setKeyListener(SignedDecimalKeyListener1.getInstance());
        this.nilai_ki.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    auto_correction.this.jumlah_nol((int) (100.0f * Float.parseFloat(auto_correction.this.nilai_ki.getText().toString())));
                } catch (NumberFormatException e) {
                    auto_correction.this.nilai_ki.setText("0.00");
                } catch (NullPointerException e2) {
                    auto_correction.this.nilai_ki.setText("0.00");
                }
            }
        });
        this.save = (Button) findViewById(R.id.save_auto_correction);
        this.save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                auto_correction.this.jumlah_nol((int) (100.0f * Float.parseFloat(auto_correction.this.nilai_ki.getText().toString())));
                auto_correction.this.keadaan = 0;
                auto_correction.this.kirim();
            }
        });
        this.exit = (Button) findViewById(R.id.exit_auto_correction);
        this.exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                auto_correction.this.finish();
            }
        });
    }

    /* access modifiers changed from: private */
    public void jumlah_nol(int value) {
        if (value <= 120 && value >= -120) {
            String userInput = String.valueOf(((double) value) / 100.0d);
            int dotPos = -1;
            for (int i = 0; i < userInput.length(); i++) {
                if (userInput.charAt(i) == '.') {
                    dotPos = i;
                }
            }
            if (dotPos == -1) {
                this.nilai_ki.setText(userInput + ".00");
            } else if (userInput.length() - dotPos == 1) {
                this.nilai_ki.setText(userInput + "00");
            } else if (userInput.length() - dotPos == 2) {
                this.nilai_ki.setText(userInput + "0");
            }
        } else if (value > 120) {
            this.nilai_ki.setText("1.20");
        } else if (value < -120) {
            this.nilai_ki.setText("-1.20");
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LoadPreferences();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        Log.d("Auto Cor", "On Destroy");
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            unregisterReceiver(this.myBroadCastReceiver);
        } else if (StaticClass.TipeKoneksi.equals("bt")) {
            try {
                if (this.nyambung == 1 && this.bluetooth.isEnabled()) {
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
        String nilai_ti1 = sharedPreferences.getString("ti", "ti1");
        String nilai_ki1 = sharedPreferences.getString("ki", "ki1");
        this.nilai_ti = (EditText) findViewById(R.id.ti);
        this.nilai_ti.setText(nilai_ti1);
        this.nilai_ki = (EditText) findViewById(R.id.ki);
        this.nilai_ki.setText(String.valueOf(Float.parseFloat(nilai_ki1) / 100.0f));
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    auto_correction.this.connect(perangkat);
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
        String code = this.nilai_ti.getText().toString();
        String yang_dikirim = String.valueOf((int) (100.0d * Double.parseDouble(this.nilai_ki.getText().toString())));
        String code4 = "3604" + ";" + code + ";" + yang_dikirim + "\r\n";
        byte[] msgBuffer = code4.getBytes();
        SavePreferences("ki", yang_dikirim);
        SavePreferences("ti", code);
        if (StaticClass.TipeKoneksi.equals("bt")) {
            if (this.nyambung == 1) {
                try {
                    this.outputStream = this.socket.getOutputStream();
                    this.outputStream.write(msgBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = code4;
            this.service.putExtra("send", "GlobalData");
            startService(this.service);
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
                                        Toast.makeText(auto_correction.this.getApplicationContext(), "data saved", 0).show();
                                    } else if (auto_correction.this.keadaan == 0) {
                                        Toast.makeText(auto_correction.this.getApplicationContext(), "failed to send", 0).show();
                                        auto_correction.this.keadaan = 1;
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
                juken.android.com.juken_5.auto_correction r2 = juken.android.com.juken_5.auto_correction.this     // Catch:{ Exception -> 0x0032 }
                r2.finish()     // Catch:{ Exception -> 0x0032 }
                goto L_0x0013
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.auto_correction.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
