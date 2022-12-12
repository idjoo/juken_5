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

public class jet_fuel_plus extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            jet_fuel_plus.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    private InputStream inputStream = null;
    int keadaan = 0;
    MyBroadCastReceiver myBroadCastReceiver;
    int nyambung = 1;
    private OutputStream outputStream = null;
    EditText position;
    EditText pulse_width;
    byte[] readBuffer;
    int readBufferPosition;
    Intent service;
    private BluetoothSocket socket = null;
    volatile boolean stopWorker;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jet_fuel_plus);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        ImageView pump = (ImageView) findViewById(R.id.imageview2);
        if (LanguageHelper.getLanguage(this).toString().equals("en")) {
            pump.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.judul_tps_fast_en));
        } else {
            pump.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.judul_tps_fast));
        }
        TextView tutorial = (TextView) findViewById(R.id.tutorial);
        tutorial.setText(tutorial.getText().toString().replace("%%", "%"));
        this.position = (EditText) findViewById(R.id.et_jet_fuel_slow);
        this.position.setFilters(new InputFilter[]{new range_int1("0", "720")});
        this.pulse_width = (EditText) findViewById(R.id.et_jet_fuel_slow_med);
        this.pulse_width.setFilters(new InputFilter[]{new range_float2(1)});
        this.pulse_width.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    float val = Float.parseFloat(jet_fuel_plus.this.pulse_width.getText().toString());
                    if (val <= 10.0f && val >= 0.0f) {
                        jet_fuel_plus.this.pulse_width.setText(String.valueOf(val));
                    } else if (val > 10.0f) {
                        jet_fuel_plus.this.pulse_width.setText("10.0");
                    } else if (val < 0.0f) {
                        jet_fuel_plus.this.pulse_width.setText("0.0");
                    }
                } catch (NumberFormatException e) {
                    jet_fuel_plus.this.pulse_width.setText("0.0");
                } catch (NullPointerException e2) {
                    jet_fuel_plus.this.pulse_width.setText("0.0");
                }
            }
        });
        ((Button) findViewById(R.id.save_jet_fuel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                jet_fuel_plus.this.keadaan = 0;
                jet_fuel_plus.this.kirim();
            }
        });
        ((Button) findViewById(R.id.exit_jet_fuel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                jet_fuel_plus.this.finish();
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
        String string = sharedPreferences.getString("jf1", "jf11");
        String jf2 = sharedPreferences.getString("jf2", "jf21");
        String string2 = sharedPreferences.getString("jf3", "jf31");
        String jf4 = sharedPreferences.getString("jf4", "jf41");
        String string3 = sharedPreferences.getString("t_rate1", "t_rate11");
        String string4 = sharedPreferences.getString("t_rate2", "t_rate21");
        String string5 = sharedPreferences.getString("t_rate3", "t_rate31");
        Double val = Double.valueOf(0.0d);
        try {
            val = Double.valueOf(((double) Integer.valueOf(jf4).intValue()) / 10.0d);
        } catch (Exception e) {
        }
        this.pulse_width = (EditText) findViewById(R.id.et_jet_fuel_slow_med);
        this.pulse_width.setText(String.valueOf(val).replace(",", "."));
        this.position = (EditText) findViewById(R.id.et_jet_fuel_slow);
        this.position.setText(jf2);
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    jet_fuel_plus.this.connect(perangkat);
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
        String code = this.pulse_width.getText().toString().replace(".", "").replace(",", "");
        String code1 = this.position.getText().toString();
        String code4 = "360B" + ";0;" + code1 + ";0;" + code + "\r\n";
        byte[] msgBuffer = code4.getBytes();
        SavePreferences("jf4", code);
        SavePreferences("jf2", code1);
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
                                        Toast.makeText(jet_fuel_plus.this.getApplicationContext(), "data saved", 0).show();
                                    } else if (jet_fuel_plus.this.keadaan == 0) {
                                        Toast.makeText(jet_fuel_plus.this.getApplicationContext(), "failed to send", 0).show();
                                        jet_fuel_plus.this.keadaan = 1;
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
                java.lang.String r0 = r7.getStringExtra(r3)     // Catch:{ Exception -> 0x0038 }
                r3 = -1
                int r4 = r0.hashCode()     // Catch:{ Exception -> 0x0038 }
                switch(r4) {
                    case -375011075: goto L_0x0014;
                    case 108388975: goto L_0x001d;
                    default: goto L_0x000f;
                }     // Catch:{ Exception -> 0x0038 }
            L_0x000f:
                r2 = r3
            L_0x0010:
                switch(r2) {
                    case 0: goto L_0x0027;
                    case 1: goto L_0x003d;
                    default: goto L_0x0013;
                }     // Catch:{ Exception -> 0x0038 }
            L_0x0013:
                return
            L_0x0014:
                java.lang.String r4 = "dataSaved"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x0038 }
                if (r4 == 0) goto L_0x000f
                goto L_0x0010
            L_0x001d:
                java.lang.String r2 = "recon"
                boolean r2 = r0.equals(r2)     // Catch:{ Exception -> 0x0038 }
                if (r2 == 0) goto L_0x000f
                r2 = 1
                goto L_0x0010
            L_0x0027:
                juken.android.com.juken_5.jet_fuel_plus r2 = juken.android.com.juken_5.jet_fuel_plus.this     // Catch:{ Exception -> 0x0038 }
                android.content.Context r2 = r2.getApplicationContext()     // Catch:{ Exception -> 0x0038 }
                java.lang.String r3 = "data saved"
                r4 = 0
                android.widget.Toast r2 = android.widget.Toast.makeText(r2, r3, r4)     // Catch:{ Exception -> 0x0038 }
                r2.show()     // Catch:{ Exception -> 0x0038 }
                goto L_0x0013
            L_0x0038:
                r1 = move-exception
                r1.printStackTrace()
                goto L_0x0013
            L_0x003d:
                juken.android.com.juken_5.jet_fuel_plus r2 = juken.android.com.juken_5.jet_fuel_plus.this     // Catch:{ Exception -> 0x0038 }
                r2.finish()     // Catch:{ Exception -> 0x0038 }
                goto L_0x0013
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.jet_fuel_plus.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
