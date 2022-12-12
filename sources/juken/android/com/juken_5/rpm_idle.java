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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import juken.android.com.juken_5.live.StaticClassExecute;
import juken.android.com.juken_5.live.live_awal;

public class rpm_idle extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private static final String[] array = {"1200", "1250", "1300", "1350", "1400", "1450", "1500", "1550", "1600", "1650", "1700", "1750", "1800", "1850", "1900", "1950", "2000"};
    private static final String[] array1 = {"0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0", "4.5", "5.0", "5.5", "6.0", "6.5", "7.0", "7.5", "8.0", "8.5", "9.0", "9.5", "10.0", "10.5", "11.0", "11.5", "12.0", "12.5", "13.0", "13.5", "14.0", "14.5", "15.0", "15.5", "16.0", "16.5", "17.0", "17.5", "18.0", "18.5", "19.0", "19.5", "20.0", "20.5", "21.0", "21.5", "22.0", "22.5", "23.0", "23.5", "24.0", "24.5", "25.0", "25.5", "26.0", "26.5", "27.0", "27.5", "28.0", "28.5", "29.0", "29.5", "30.0", "30.5", "31.0", "31.5", "32.0", "32.5", "33.0", "33.5", "34.0", "34.5", "35.0", "35.5", "36.0", "36.5", "37.0", "37.5", "38.0", "38.5", "39.0", "39.5", "40.0", "40.5", "41.0", "41.5", "42.0", "42.5", "43.0", "43.5", "44.0", "44.5", "45.0", "45.5", "46.0", "46.5", "47.0", "47.5", "48.0", "48.5", "49.0", "49.5", "50.0"};
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            rpm_idle.this.listen();
        }
    };
    /* access modifiers changed from: private */
    public BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    Spinner deceleration_delay;
    EditText idle_fuel;
    private InputStream inputStream = null;
    int keadaan = 0;
    MyBroadCastReceiver myBroadCastReceiver;
    int nyambung = 1;
    private OutputStream outputStream = null;
    EditText position;
    byte[] readBuffer;
    int readBufferPosition;
    Intent service;
    SharedPreferences sharedPreferences;
    private BluetoothSocket socket = null;
    Spinner stasioner;
    volatile boolean stopWorker;
    int urutan_kirim = 0;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rpm_idle);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        ImageView view = (ImageView) findViewById(R.id.imageview1);
        if (LanguageHelper.getLanguage(this).toString().equals("en")) {
            view.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rpm_idle_panah_bawah_en));
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rpm_idle_panah_bawah));
        }
        this.position = (EditText) findViewById(R.id.et_position);
        this.position.setFilters(new InputFilter[]{new range_int1("0", "100")});
        this.idle_fuel = (EditText) findViewById(R.id.et_idle_fuel);
        this.idle_fuel.setFilters(new InputFilter[]{new range_int_min("-100", "100")});
        ((Button) findViewById(R.id.save_rpm_idle)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rpm_idle.this.keadaan = 0;
                rpm_idle.this.urutan_kirim = 0;
                if (MappingHandle.NamaFileFuel.equals("Fuel Correction - ECU Core 1") || MappingHandle.NamaFileFuel.equals("Fuel Correction - ECU Core 2")) {
                    for (int j = 0; j < 9; j++) {
                        MappingHandle.list_fuel.remove(j);
                        MappingHandle.list_fuel.add(j, rpm_idle.this.idle_fuel.getText().toString());
                    }
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(rpm_idle.this);
                    alert.setTitle((CharSequence) "Alert!");
                    alert.setMessage((CharSequence) "Fuel Correction yang tampil merupakan File Data,\nmaka perubahan nilai idle fuel tidak akan tampil di Fuel Correction");
                    alert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.create().show();
                }
                if (rpm_idle.this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
                    for (int j2 = 0; j2 < 9; j2++) {
                        MappingHandle.list_fuel_live.remove(j2);
                        MappingHandle.list_fuel_live.add(j2, rpm_idle.this.idle_fuel.getText().toString());
                    }
                    StaticClassExecute.bisa_execute = true;
                }
                rpm_idle.this.kirim_rpm_idle();
            }
        });
        ((Button) findViewById(R.id.exit_rpm_idle)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("wifi")) {
                    rpm_idle.this.unregisterReceiver(rpm_idle.this.myBroadCastReceiver);
                } else if (StaticClass.TipeKoneksi.equals("bt")) {
                    try {
                        if (rpm_idle.this.bluetooth.isEnabled() && rpm_idle.this.nyambung == 1) {
                            rpm_idle.this.resetConnection();
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                rpm_idle.this.finish();
                if (rpm_idle.this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    rpm_idle.this.startActivityForResult(new Intent(rpm_idle.this.getApplicationContext(), live_awal.class), 0);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LoadPreferences();
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) <= 5 || keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        finish();
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
        if (this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
            startActivityForResult(new Intent(getApplicationContext(), live_awal.class), 0);
        }
    }

    private void LoadPreferences() {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String nilai = this.sharedPreferences.getString("idle_fuel", "idle_fuel1");
        String position_rpm_idle = this.sharedPreferences.getString("position_rpm_idle", "position_rpm_idle1");
        String nilai_idle = this.sharedPreferences.getString("nilai_idle", "nilai_idle1");
        String deceleration_time = this.sharedPreferences.getString("dec_time", "dec_time1");
        this.idle_fuel = (EditText) findViewById(R.id.et_idle_fuel);
        this.idle_fuel.setText(nilai);
        int taruh = (Integer.parseInt(position_rpm_idle) / 50) - 24;
        this.stasioner = (Spinner) findViewById(R.id.et_idle);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, 17367048, array);
        adapter1.setDropDownViewResource(17367049);
        this.stasioner.setAdapter(adapter1);
        this.stasioner.setSelection(taruh);
        this.position = (EditText) findViewById(R.id.et_position);
        this.position.setText(nilai_idle);
        double hitung_dec = Double.parseDouble(deceleration_time) - 1.0d;
        this.deceleration_delay = (Spinner) findViewById(R.id.et_deceleration);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 17367048, array1);
        adapter.setDropDownViewResource(17367049);
        this.deceleration_delay.setAdapter(adapter);
        this.deceleration_delay.setSelection((int) hitung_dec);
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(this.sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    rpm_idle.this.connect(perangkat);
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
        String code = this.idle_fuel.getText().toString();
        String code2 = "3610" + ";" + code + "\r\n";
        byte[] msgBuffer = code2.getBytes();
        SavePreferences("idle_fuel", code);
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

    public void kirim_rpm_idle() {
        this.urutan_kirim = 0;
        String code = this.position.getText().toString();
        String code1 = this.stasioner.getSelectedItem().toString();
        String code2 = String.valueOf(this.deceleration_delay.getSelectedItemPosition() + 1);
        String code4 = "360F" + ";" + code1 + ";" + code + ";" + code2 + "\r\n";
        byte[] msgBuffer = code4.getBytes();
        SavePreferences("nilai_idle", code);
        SavePreferences("position_rpm_idle", code1);
        SavePreferences("dec_time", code2);
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
                                        if (rpm_idle.this.urutan_kirim == 0) {
                                            rpm_idle.this.urutan_kirim = 1;
                                            rpm_idle.this.kirim();
                                            return;
                                        }
                                        Toast.makeText(rpm_idle.this.getApplicationContext(), "data saved", 0).show();
                                    } else if (rpm_idle.this.keadaan == 0) {
                                        Toast.makeText(rpm_idle.this.getApplicationContext(), "failed to send", 0).show();
                                        rpm_idle.this.keadaan = 1;
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
                java.lang.String r0 = r8.getStringExtra(r3)     // Catch:{ Exception -> 0x0039 }
                r3 = -1
                int r5 = r0.hashCode()     // Catch:{ Exception -> 0x0039 }
                switch(r5) {
                    case -375011075: goto L_0x0015;
                    case 108388975: goto L_0x001e;
                    default: goto L_0x0010;
                }     // Catch:{ Exception -> 0x0039 }
            L_0x0010:
                r2 = r3
            L_0x0011:
                switch(r2) {
                    case 0: goto L_0x0028;
                    case 1: goto L_0x004f;
                    default: goto L_0x0014;
                }     // Catch:{ Exception -> 0x0039 }
            L_0x0014:
                return
            L_0x0015:
                java.lang.String r4 = "dataSaved"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x0039 }
                if (r4 == 0) goto L_0x0010
                goto L_0x0011
            L_0x001e:
                java.lang.String r2 = "recon"
                boolean r2 = r0.equals(r2)     // Catch:{ Exception -> 0x0039 }
                if (r2 == 0) goto L_0x0010
                r2 = r4
                goto L_0x0011
            L_0x0028:
                juken.android.com.juken_5.rpm_idle r2 = juken.android.com.juken_5.rpm_idle.this     // Catch:{ Exception -> 0x0039 }
                int r2 = r2.urutan_kirim     // Catch:{ Exception -> 0x0039 }
                if (r2 != 0) goto L_0x003e
                juken.android.com.juken_5.rpm_idle r2 = juken.android.com.juken_5.rpm_idle.this     // Catch:{ Exception -> 0x0039 }
                r3 = 1
                r2.urutan_kirim = r3     // Catch:{ Exception -> 0x0039 }
                juken.android.com.juken_5.rpm_idle r2 = juken.android.com.juken_5.rpm_idle.this     // Catch:{ Exception -> 0x0039 }
                r2.kirim()     // Catch:{ Exception -> 0x0039 }
                goto L_0x0014
            L_0x0039:
                r1 = move-exception
                r1.printStackTrace()
                goto L_0x0014
            L_0x003e:
                juken.android.com.juken_5.rpm_idle r2 = juken.android.com.juken_5.rpm_idle.this     // Catch:{ Exception -> 0x0039 }
                android.content.Context r2 = r2.getApplicationContext()     // Catch:{ Exception -> 0x0039 }
                java.lang.String r3 = "data saved"
                r4 = 0
                android.widget.Toast r2 = android.widget.Toast.makeText(r2, r3, r4)     // Catch:{ Exception -> 0x0039 }
                r2.show()     // Catch:{ Exception -> 0x0039 }
                goto L_0x0014
            L_0x004f:
                juken.android.com.juken_5.rpm_idle r2 = juken.android.com.juken_5.rpm_idle.this     // Catch:{ Exception -> 0x0039 }
                juken.android.com.juken_5.rpm_idle r3 = juken.android.com.juken_5.rpm_idle.this     // Catch:{ Exception -> 0x0039 }
                juken.android.com.juken_5.rpm_idle$MyBroadCastReceiver r3 = r3.myBroadCastReceiver     // Catch:{ Exception -> 0x0039 }
                r2.unregisterReceiver(r3)     // Catch:{ Exception -> 0x0039 }
                juken.android.com.juken_5.rpm_idle r2 = juken.android.com.juken_5.rpm_idle.this     // Catch:{ Exception -> 0x0039 }
                r2.finish()     // Catch:{ Exception -> 0x0039 }
                goto L_0x0014
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.rpm_idle.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
