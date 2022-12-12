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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class auto_protect extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private static final String[] array1 = {"5000", "5100", "5200", "5300", "5400", "5500", "5600", "5700", "5800", "5900", "6000", "6100", "6200", "6300", "6400", "6500", "6600", "6700", "6800", "6900", "7000", "7100", "7200", "7300", "7400", "7500", "7600", "7700", "7800", "7900", "8000", "8100", "8200", "8300", "8400", "8500", "8600", "8700", "8800", "8900", "9000", "9100", "9200", "9300", "9400", "9500", "9600", "9700", "9800", "9900", "10000", "10100", "10200", "10300", "10400", "10500", "10600", "10700", "10800", "10900", "11000", "11100", "11200", "11300", "11400", "11500", "11600", "11700", "11800", "11900", "12000", "12100", "12200", "12300", "12400", "12500", "12600", "12700", "12800", "12900", "13000", "13100", "13200", "13300", "13400", "13500", "13600", "13700", "13800", "13900", "14000", "14100", "14200", "14300", "14400", "14500", "14600", "14700", "14800", "14900", "15000", "15100", "15200", "15300", "15400", "15500", "15600", "15700", "15800", "15900", "16000"};
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            auto_protect.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    private InputStream inputStream = null;
    int keadaan = 0;
    Spinner limiter;
    MyBroadCastReceiver myBroadCastReceiver;
    EditText nilai_temp;
    int nyambung = 1;
    RadioButton off;
    RadioButton on;
    private OutputStream outputStream = null;
    byte[] readBuffer;
    int readBufferPosition;
    Intent service;
    private BluetoothSocket socket = null;
    volatile boolean stopWorker;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_protect);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        this.limiter = (Spinner) findViewById(R.id.et_limit);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 17367048, array1);
        adapter.setDropDownViewResource(17367049);
        this.limiter.setAdapter(adapter);
        this.limiter.setSelection(0);
        this.nilai_temp = (EditText) findViewById(R.id.et_temp);
        this.nilai_temp.setFilters(new InputFilter[]{new range_float2(1)});
        this.nilai_temp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    float val = Float.parseFloat(auto_protect.this.nilai_temp.getText().toString());
                    if (val <= 140.0f && val >= 60.0f) {
                        auto_protect.this.nilai_temp.setText(String.valueOf(val));
                    } else if (val > 140.0f) {
                        auto_protect.this.nilai_temp.setText("140.0");
                    } else if (val < 60.0f) {
                        auto_protect.this.nilai_temp.setText("60.0");
                    }
                } catch (NumberFormatException e) {
                    auto_protect.this.nilai_temp.setText("60.0");
                } catch (NullPointerException e2) {
                    auto_protect.this.nilai_temp.setText("60.0");
                }
            }
        });
        this.on = (RadioButton) findViewById(R.id.On);
        this.on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (auto_protect.this.on.isChecked()) {
                    auto_protect.this.off.setChecked(false);
                }
            }
        });
        this.off = (RadioButton) findViewById(R.id.Off);
        this.off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (auto_protect.this.off.isChecked()) {
                    auto_protect.this.on.setChecked(false);
                }
            }
        });
        ((Button) findViewById(R.id.ButtonOk)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                float cek = Float.valueOf(auto_protect.this.nilai_temp.getText().toString()).floatValue();
                if (cek > 140.0f) {
                    auto_protect.this.nilai_temp.setText("140.0");
                } else if (cek < 60.0f) {
                    auto_protect.this.nilai_temp.setText("60.0");
                }
                auto_protect.this.kirim();
            }
        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String enable = sharedPreferences.getString("protect_enable", "protect_enable1");
        String limit = sharedPreferences.getString("protect_limit", "protect_limit1");
        String temp = sharedPreferences.getString("protect_temp", "protect_temp1");
        if (enable.equals("1")) {
            this.on.setChecked(true);
        } else {
            this.off.setChecked(true);
        }
        this.nilai_temp.setText(Double.valueOf(Double.valueOf(temp).doubleValue() / 10.0d).toString().replace(",", "."));
        int posisi = (Integer.valueOf(limit).intValue() - 5000) / 100;
        if (posisi > 110) {
            posisi = 110;
        } else if (posisi < 0) {
            posisi = 0;
        }
        this.limiter.setSelection(posisi);
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

    private void LoadPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    auto_protect.this.connect(perangkat);
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
        String kirim = String.valueOf((int) (10.0f * Float.parseFloat(this.nilai_temp.getText().toString()))).replace(",", ".");
        String kirim1 = this.limiter.getSelectedItem().toString();
        String enable = "0";
        if (this.on.isChecked()) {
            enable = "1";
        }
        String code1 = "361E" + ";" + enable + ";" + kirim + ";" + kirim1 + "\r\n";
        byte[] msgBuffer = code1.getBytes();
        SavePreferences("protect_enable", enable);
        SavePreferences("protect_temp", kirim);
        SavePreferences("protect_limit", kirim1);
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
                                        Toast.makeText(auto_protect.this.getApplicationContext(), "data saved", 0).show();
                                    } else if (auto_protect.this.keadaan == 0) {
                                        Toast.makeText(auto_protect.this.getApplicationContext(), "failed to send", 0).show();
                                        auto_protect.this.keadaan = 1;
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
                juken.android.com.juken_5.auto_protect r2 = juken.android.com.juken_5.auto_protect.this     // Catch:{ Exception -> 0x0032 }
                r2.finish()     // Catch:{ Exception -> 0x0032 }
                goto L_0x0013
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.auto_protect.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
