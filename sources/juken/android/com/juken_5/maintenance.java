package juken.android.com.juken_5;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class maintenance extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private static final String[] array1 = {"2000", "2250", "2500", "2750", "3000", "3250", "3500", "3750", "4000", "4250", "4500", "4750", "5000", "5250", "5500", "5750", "6000", "6250", "6500", "6750", "7000", "7250", "7500", "7750", "8000", "8250", "8500", "8750", "9000", "9250", "9500", "9750", "10000", "10250", "10500", "10750", "11000", "11250", "11500", "11750", "12000", "12250", "12500", "12750", "13000", "13250", "13500", "13750", "14000", "14250", "14500", "14750", "15000"};
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            maintenance.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    int cek = 0;
    Boolean fromRecon = false;
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    private InputStream inputStream = null;
    MyBroadCastReceiver myBroadCastReceiver;
    EditText nilai_duration;
    EditText nilai_duty;
    int nyambung = 1;
    private OutputStream outputStream = null;
    int posisi = 0;
    byte[] readBuffer;
    int readBufferPosition;
    /* access modifiers changed from: private */
    public final Runnable sendData = new Runnable() {
        public void run() {
            try {
                maintenance.this.kirim_awal();
                maintenance.this.handler.postDelayed(this, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Intent service;
    private BluetoothSocket socket = null;
    Spinner speed;
    Button start;
    Button stop;
    volatile boolean stopWorker;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintenance);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        this.nilai_duration = (EditText) findViewById(R.id.et_duration);
        this.nilai_duration.setFilters(new InputFilter[]{new range_int1("1", "500")});
        this.nilai_duty = (EditText) findViewById(R.id.et_on_duty);
        this.nilai_duty.setFilters(new InputFilter[]{new range_int1("1", "100")});
        this.speed = (Spinner) findViewById(R.id.et_speed);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 17367048, array1);
        adapter.setDropDownViewResource(17367049);
        this.speed.setAdapter(adapter);
        this.speed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.start = (Button) findViewById(R.id.start);
        this.start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                maintenance.this.handler.post(maintenance.this.sendData);
            }
        });
        this.stop = (Button) findViewById(R.id.stop);
        this.stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                maintenance.this.handler.removeCallbacks(maintenance.this.sendData);
                maintenance.this.cek = 0;
                if (StaticClass.TipeKoneksi.equals("wifi")) {
                    StaticClass.bolehPing = true;
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
        this.handler.removeCallbacks(this.sendData);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            if (!this.fromRecon.booleanValue()) {
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
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    maintenance.this.connect(perangkat);
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
        }
    }

    public void kirim_awal() {
        if (this.cek == 0) {
            this.cek = 1;
            String code2 = "3800" + ";" + this.nilai_duration.getText().toString() + ";" + this.nilai_duty.getText().toString() + ";" + this.speed.getSelectedItem().toString() + "\r\n";
            byte[] msgBuffer = code2.getBytes();
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
        } else {
            String code22 = "3800" + "\r\n";
            byte[] msgBuffer2 = code22.getBytes();
            if (StaticClass.TipeKoneksi.equals("wifi")) {
                StaticClass.GlobalData = code22;
                this.service.putExtra("send", "GlobalData");
                startService(this.service);
            } else if (StaticClass.TipeKoneksi.equals("bt") && this.nyambung == 1) {
                try {
                    this.outputStream = this.socket.getOutputStream();
                    this.outputStream.write(msgBuffer2);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
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
                        if (b == 10 || b == 59) {
                            byte[] encodedBytes = new byte[this.readBufferPosition];
                            System.arraycopy(this.readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                            final String data = new String(encodedBytes, "US-ASCII");
                            this.readBufferPosition = 0;
                            this._handler.post(new Runnable() {
                                public void run() {
                                    switch (maintenance.this.posisi) {
                                        case 0:
                                            if (data.equals("b800") || data.equals("B800")) {
                                                maintenance.this.posisi = 1;
                                                return;
                                            }
                                            return;
                                        case 1:
                                            TextView tampil_durasi = (TextView) maintenance.this.findViewById(R.id.et_time);
                                            int waktu = Integer.parseInt(data);
                                            if (waktu >= 3600) {
                                                int menit = 0;
                                                int detik = 0;
                                                int jam = waktu / 3600;
                                                int cek = waktu - (jam * 3600);
                                                if (cek >= 60) {
                                                    menit = cek / 60;
                                                    detik = cek - (menit * 60);
                                                } else if (cek < 60) {
                                                    menit = 0;
                                                    detik = cek;
                                                }
                                                String pake_jam = String.valueOf(jam);
                                                String pake_menit = String.valueOf(menit);
                                                tampil_durasi.setText(pake_jam + ":" + pake_menit + ":" + String.valueOf(detik));
                                            }
                                            if (waktu >= 60 && waktu < 3600) {
                                                int menit2 = waktu / 60;
                                                String pake_menit2 = String.valueOf(menit2);
                                                tampil_durasi.setText("0" + ":" + pake_menit2 + ":" + String.valueOf(waktu - (menit2 * 60)));
                                            }
                                            if (waktu < 60) {
                                                int detik2 = waktu;
                                                tampil_durasi.setText("0" + ":" + "0" + ":" + String.valueOf(detik2));
                                                if (detik2 == 0) {
                                                    maintenance.this.handler.removeCallbacks(maintenance.this.sendData);
                                                    maintenance maintenance = maintenance.this;
                                                    maintenance.this.posisi = 0;
                                                    maintenance.cek = 0;
                                                }
                                            }
                                            maintenance.this.posisi = 2;
                                            return;
                                        case 2:
                                            TextView batere = (TextView) maintenance.this.findViewById(R.id.et_bat);
                                            String dataPake = data;
                                            if (!dataPake.contains(".") || !dataPake.contains(",")) {
                                                try {
                                                    dataPake = String.valueOf(Double.valueOf(Double.valueOf(data).doubleValue() / 100.0d)).replace(",", ".");
                                                } catch (Exception e) {
                                                }
                                            }
                                            batere.setText(dataPake + " V");
                                            maintenance.this.posisi = 3;
                                            return;
                                        case 3:
                                            maintenance.this.posisi = 4;
                                            return;
                                        case 4:
                                            maintenance.this.posisi = 5;
                                            ((TextView) maintenance.this.findViewById(R.id.et_counter)).setText(data);
                                            return;
                                        case 5:
                                            maintenance.this.posisi = 6;
                                            return;
                                        case 6:
                                            maintenance.this.posisi = 0;
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

        public void onReceive(Context context, Intent intent) {
            try {
                String data = intent.getStringExtra("key");
                char c = 65535;
                switch (data.hashCode()) {
                    case 108388975:
                        if (data.equals("recon")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 694532060:
                        if (data.equals("maintenanceProcess")) {
                            c = 0;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        TextView tampil_durasi = (TextView) maintenance.this.findViewById(R.id.et_time);
                        int waktu = StaticClass.waktu.intValue();
                        if (waktu >= 3600) {
                            int menit = 0;
                            int detik = 0;
                            int jam = waktu / 3600;
                            int cek = waktu - (jam * 3600);
                            if (cek >= 60) {
                                menit = cek / 60;
                                detik = cek - (menit * 60);
                            } else if (cek < 60) {
                                menit = 0;
                                detik = cek;
                            }
                            String pake_jam = String.valueOf(jam);
                            String pake_menit = String.valueOf(menit);
                            tampil_durasi.setText(pake_jam + ":" + pake_menit + ":" + String.valueOf(detik));
                        }
                        if (waktu >= 60 && waktu < 3600) {
                            int menit2 = waktu / 60;
                            String pake_menit2 = String.valueOf(menit2);
                            tampil_durasi.setText("0" + ":" + pake_menit2 + ":" + String.valueOf(waktu - (menit2 * 60)));
                        }
                        if (waktu < 60) {
                            int detik2 = waktu;
                            tampil_durasi.setText("0" + ":" + "0" + ":" + String.valueOf(detik2));
                            if (detik2 == 0) {
                                maintenance.this.handler.removeCallbacks(maintenance.this.sendData);
                                maintenance.this.cek = 0;
                                StaticClass.bolehPing = true;
                            }
                        }
                        TextView batere = (TextView) maintenance.this.findViewById(R.id.et_bat);
                        String dataPake = StaticClass.batere;
                        if (!dataPake.contains(".") || !dataPake.contains(",")) {
                            try {
                                dataPake = String.valueOf(Double.valueOf(Double.valueOf(StaticClass.batere).doubleValue() / 100.0d)).replace(",", ".");
                            } catch (Exception e) {
                            }
                        }
                        batere.setText(dataPake + " V");
                        ((TextView) maintenance.this.findViewById(R.id.et_counter)).setText(StaticClass.counter);
                        return;
                    case 1:
                        maintenance.this.fromRecon = 1;
                        maintenance.this.finish();
                        return;
                    default:
                        return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            ex.printStackTrace();
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
