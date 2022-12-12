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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class turbo extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private static final String[] array = {"0", "2", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90", "95", "100"};
    private static final String[] array1 = {"1000", "1250", "1500", "1750", "2000", "2250", "2500", "2750", "3000", "3250", "3500", "3750", "4000", "4250", "4500", "4750", "5000", "5250", "5500", "5750", "6000", "6250", "6500", "6750", "7000", "7250", "7500", "7750", "8000", "8250", "8500", "8750", "9000", "9250", "9500", "9750", "10000", "10250", "10500", "10750", "11000", "11250", "11500", "11750", "12000", "12250", "12500", "12750", "13000", "13250", "13500", "13750", "14000", "14250", "14500", "14750", "15000"};
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            turbo.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    int cek_status = 0;
    String ecu_tipe = "";
    Button exit;
    private InputStream inputStream = null;
    int keadaan = 0;
    MyBroadCastReceiver myBroadCastReceiver;
    int nyambung = 1;
    private OutputStream outputStream = null;
    byte[] readBuffer;
    int readBufferPosition;
    Spinner rpm_start;
    Button save;
    Intent service;
    private BluetoothSocket socket = null;
    TextView status;
    volatile boolean stopWorker;
    Spinner tps_start;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.turbo);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        ((Button) findViewById(R.id.enable)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (turbo.this.ecu_tipe.equals("192") || turbo.this.ecu_tipe.equals("224")) {
                    turbo.this.keadaan = 0;
                    turbo.this.jadi();
                    return;
                }
                Toast.makeText(turbo.this.getApplicationContext(), "ecu model not supported", 0).show();
            }
        });
        ((Button) findViewById(R.id.disable)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (turbo.this.ecu_tipe.equals("192") || turbo.this.ecu_tipe.equals("224")) {
                    turbo.this.keadaan = 0;
                    turbo.this.batal();
                    return;
                }
                Toast.makeText(turbo.this.getApplicationContext(), "ecu model not supported", 0).show();
            }
        });
        this.rpm_start = (Spinner) findViewById(R.id.rpm_start);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 17367048, array1);
        adapter.setDropDownViewResource(17367049);
        this.rpm_start.setAdapter(adapter);
        this.rpm_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.tps_start = (Spinner) findViewById(R.id.tps_start);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, 17367048, array);
        adapter1.setDropDownViewResource(17367049);
        this.tps_start.setAdapter(adapter1);
        this.tps_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
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
        String turbo_tps = sharedPreferences.getString("turbo_tps", "turbo_tps1");
        String turbo_rpm = sharedPreferences.getString("turbo_rpm", "turbo_rpm1");
        this.ecu_tipe = sharedPreferences.getString("ecu_model", "ecu_model1").replace("\r", "");
        String turbo_status = sharedPreferences.getString("turbo_en", "turbo_en1");
        this.status = (TextView) findViewById(R.id.status);
        if (turbo_status.equals("0")) {
            this.status.setText("OFF");
            this.cek_status = 0;
        } else {
            this.status.setText("ON");
            this.cek_status = 1;
        }
        int turbo_tps1 = Integer.parseInt(turbo_tps);
        int turbo_rpm1 = Integer.parseInt(turbo_rpm);
        int pakai_tps = 0;
        if (turbo_tps1 == 0) {
            pakai_tps = 0;
        } else if (turbo_tps1 == 2) {
            pakai_tps = 1;
        } else if (turbo_tps1 == 100) {
            pakai_tps = 20;
        } else if (turbo_tps1 > 2 && turbo_tps1 < 100) {
            pakai_tps = (turbo_tps1 / 5) + 1;
        }
        int pakai_rpm = (turbo_rpm1 / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) - 4;
        this.rpm_start = (Spinner) findViewById(R.id.rpm_start);
        this.rpm_start.setSelection(pakai_rpm);
        this.tps_start = (Spinner) findViewById(R.id.tps_start);
        this.tps_start.setSelection(pakai_tps);
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    turbo.this.connect(perangkat);
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
        this.cek_status = 1;
        this.status = (TextView) findViewById(R.id.status);
        this.status.setText("ON");
        String code = this.tps_start.getSelectedItem().toString();
        String code1 = this.rpm_start.getSelectedItem().toString();
        String code2 = "3608" + ";1;" + code + ";" + code1 + "\r\n";
        byte[] msgBuffer = code2.getBytes();
        SavePreferences("turbo_en", "1");
        SavePreferences("turbo_tps", code);
        SavePreferences("turbo_rpm", code1);
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
        this.cek_status = 0;
        this.status = (TextView) findViewById(R.id.status);
        this.status.setText("OFF");
        String code2 = "3608" + ";0;" + this.tps_start.getSelectedItem().toString() + ";" + this.rpm_start.getSelectedItem().toString() + "\r\n";
        byte[] msgBuffer = code2.getBytes();
        SavePreferences("turbo_en", "0");
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
                                        Toast.makeText(turbo.this.getApplicationContext(), "data saved", 0).show();
                                    } else if (turbo.this.keadaan == 0) {
                                        Toast.makeText(turbo.this.getApplicationContext(), "failed to send", 0).show();
                                        turbo.this.keadaan = 1;
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
                juken.android.com.juken_5.turbo r2 = juken.android.com.juken_5.turbo.this     // Catch:{ Exception -> 0x0032 }
                r2.finish()     // Catch:{ Exception -> 0x0032 }
                goto L_0x0013
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.turbo.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
