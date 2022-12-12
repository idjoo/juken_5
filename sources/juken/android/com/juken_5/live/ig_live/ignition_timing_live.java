package juken.android.com.juken_5.live.ig_live;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.UUID;
import juken.android.com.juken_5.MappingHandle;
import juken.android.com.juken_5.R;
import juken.android.com.juken_5.StaticClass;
import juken.android.com.juken_5.WifiService;
import juken.android.com.juken_5.live.StaticClassExecute;
import juken.android.com.juken_5.live.live_awal;
import juken.android.com.juken_5.range_float2;

public class ignition_timing_live extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            ignition_timing_live.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    int cek = 0;
    int core_dipilih = 0;
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    int hitung = 1;
    int hitung_tps = 0;
    private InputStream inputStream = null;
    int jumlah = 0;
    int keadaan = 0;
    Boolean koneksi = false;
    Boolean lock_cursor = false;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            Toast.makeText(ignition_timing_live.this.getApplicationContext(), (String) message.obj, 1).show();
        }
    };
    int maksimum = 0;
    MyBroadCastReceiver myBroadCastReceiver;
    double nilai_afr = 0.0d;
    int nyambung = 1;
    private OutputStream outputStream = null;
    int pengingat = 1;
    int posisi = 0;
    String posisi_memory = "1";
    int posisi_rpm = 0;
    int posisi_sebelumnya = 1;
    int posisi_tps = 0;
    ProgressDialog progressDialog;
    byte[] readBuffer;
    int readBufferPosition;
    int rpm_sebelumnya = 0;
    /* access modifiers changed from: private */
    public final Runnable sendData = new Runnable() {
        public void run() {
            try {
                ignition_timing_live.this.kirim_update(ignition_timing_live.this.posisi_memory);
                ignition_timing_live.this.handler.postDelayed(this, 200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Intent service;
    SharedPreferences sharedPreferences;
    private BluetoothSocket socket = null;
    int status_flag = 0;
    volatile boolean stopWorker;
    int tps_sebelumnya = 0;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ignition_timing_live);
        StaticClass.Live = true;
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
            this.koneksi = StaticClass.koneksi;
        } else if (this.bluetooth.isEnabled()) {
            this.koneksi = true;
        } else {
            this.koneksi = false;
        }
        ((ImageButton) findViewById(R.id.set_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ignition_timing_live.this.set_value();
            }
        });
        ((ImageButton) findViewById(R.id.add_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ignition_timing_live.this.add_value();
            }
        });
        ((ImageButton) findViewById(R.id.copy_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ignition_timing_live.this.Copy();
            }
        });
        ((ImageButton) findViewById(R.id.paste_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ignition_timing_live.this.Paste();
            }
        });
        ((ImageButton) findViewById(R.id.select_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ignition_timing_live.this.select();
            }
        });
        ((ImageButton) findViewById(R.id.plus_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ignition_timing_live.this.plus_minus("plus");
            }
        });
        ((ImageButton) findViewById(R.id.minus_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ignition_timing_live.this.plus_minus("minus");
            }
        });
        ((ImageButton) findViewById(R.id.execute)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClassExecute.bisa_execute) {
                    StaticClassExecute.bisa_execute = false;
                    MappingHandle.list_fuel_ganti.clear();
                    MappingHandle.list_fuel_live_ganti.clear();
                    MappingHandle.list_ignition_ganti.clear();
                    MappingHandle.list_ignition_live_ganti.clear();
                    ignition_timing_live.this.execute();
                    return;
                }
                Toast.makeText(ignition_timing_live.this.getApplicationContext(), "Update data first", 0).show();
            }
        });
        ((ImageButton) findViewById(R.id.update)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ignition_timing_live.this.koneksi.booleanValue()) {
                    StaticClassExecute.bisa_execute = true;
                    ignition_timing_live.this.save_data_ke_list();
                    MappingHandle.list_ignition.clear();
                    MappingHandle.list_ignition.addAll(MappingHandle.list_ignition_live);
                    MappingHandle.list_ignition_ganti.clear();
                    MappingHandle.list_ignition_ganti.addAll(MappingHandle.list_ignition_live_ganti);
                    ignition_timing_live.this.keadaan = 0;
                    if (ignition_timing_live.this.hitung_tps > 0) {
                        ignition_timing_live.this.hitung_tps = 0;
                    }
                    ignition_timing_live.this.core_dipilih = Integer.valueOf(ignition_timing_live.this.sharedPreferences.getString("tss", "tss1")).intValue();
                    if (ignition_timing_live.this.core_dipilih == 0) {
                        ignition_timing_live.this.posisi_memory = ignition_timing_live.this.sharedPreferences.getString("ig_c1", "ig_c11");
                    } else {
                        ignition_timing_live.this.posisi_memory = ignition_timing_live.this.sharedPreferences.getString("ig_c2", "ig_c21");
                    }
                    ignition_timing_live.this.progressOn();
                    ignition_timing_live.this.handler.post(ignition_timing_live.this.sendData);
                }
            }
        });
        final ImageButton lock = (ImageButton) findViewById(R.id.lock_cursor);
        lock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!ignition_timing_live.this.lock_cursor.booleanValue()) {
                    ignition_timing_live.this.lock_cursor = true;
                    if (Build.VERSION.SDK_INT >= 21) {
                        lock.setImageDrawable(ignition_timing_live.this.getResources().getDrawable(R.drawable.lock_cursor_tutup, ignition_timing_live.this.getApplicationContext().getTheme()));
                    } else {
                        lock.setImageDrawable(ignition_timing_live.this.getResources().getDrawable(R.drawable.lock_cursor_tutup));
                    }
                } else {
                    ignition_timing_live.this.lock_cursor = false;
                    if (Build.VERSION.SDK_INT >= 21) {
                        lock.setImageDrawable(ignition_timing_live.this.getResources().getDrawable(R.drawable.lock_cursor_buka, ignition_timing_live.this.getApplicationContext().getTheme()));
                    } else {
                        lock.setImageDrawable(ignition_timing_live.this.getResources().getDrawable(R.drawable.lock_cursor_buka));
                    }
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        StaticClass.Live = true;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (StaticClass.TipeKoneksi.equals("bt")) {
            LoadPreferences();
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
        if (StaticClass.TipeKoneksi.equals("bt")) {
            try {
                if (this.nyambung == 1) {
                    resetConnection();
                    Thread.sleep(500);
                }
            } catch (Exception e) {
            }
        } else if (StaticClass.TipeKoneksi.equals("wifi")) {
            try {
                unregisterReceiver(this.myBroadCastReceiver);
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            }
        }
        save_data_ke_list();
        StaticClass.Live = false;
        StaticClass.position.clear();
        if (this.koneksi.booleanValue()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ignition_timing_live.this.finish();
                    ignition_timing_live.this.startActivity(new Intent(ignition_timing_live.this.getApplicationContext(), live_awal.class));
                }
            }, 200);
        } else {
            finish();
        }
    }

    /* access modifiers changed from: private */
    public void save_data_ke_list() {
        MappingHandle.list_ignition_live_ganti.clear();
        for (int i = 1; i <= 651; i++) {
            if (((EditText) findViewById(i)).getCurrentTextColor() == -1027840) {
                MappingHandle.list_ignition_live_ganti.add("1");
            } else {
                MappingHandle.list_ignition_live_ganti.add("0");
            }
        }
        MappingHandle.list_ignition_live.clear();
        for (int i2 = 1; i2 <= 651; i2++) {
            MappingHandle.list_ignition_live.add(((EditText) findViewById(i2)).getText().toString());
        }
    }

    private void LoadPreferences() {
        if (this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(this.sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    ignition_timing_live.this.connect(perangkat);
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
                        if (this.cek == 0) {
                            if (b == 10 || b == 59) {
                                byte[] encodedBytes = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                final String data = new String(encodedBytes, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        switch (ignition_timing_live.this.posisi) {
                                            case 0:
                                                if (data.equals("A603") || data.equals("a603")) {
                                                    ignition_timing_live.this.posisi = 1;
                                                    return;
                                                } else {
                                                    ignition_timing_live.this.posisi = 0;
                                                    return;
                                                }
                                            case 1:
                                                ignition_timing_live.this.posisi = 2;
                                                try {
                                                    ignition_timing_live.this.posisi_tps = Integer.parseInt(data);
                                                    return;
                                                } catch (Exception e) {
                                                    return;
                                                }
                                            case 2:
                                                ignition_timing_live.this.posisi = 3;
                                                return;
                                            case 3:
                                                ignition_timing_live.this.posisi = 4;
                                                return;
                                            case 4:
                                                ignition_timing_live.this.posisi = 5;
                                                try {
                                                    float rpm_baru = Float.parseFloat(data);
                                                    if (rpm_baru < 1000.0f) {
                                                        rpm_baru = 1000.0f;
                                                    } else if (rpm_baru > 16000.0f) {
                                                        rpm_baru = 16000.0f;
                                                    }
                                                    int pembulatan_rpm = (Math.round((4.0f * rpm_baru) / 1000.0f) * 1000) / 4;
                                                    ignition_timing_live.this.posisi_rpm = (pembulatan_rpm + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED) / 500;
                                                    EditText clear = (EditText) ignition_timing_live.this.findViewById(ignition_timing_live.this.posisi_sebelumnya);
                                                    try {
                                                        Drawable drawable = clear.getBackground();
                                                        if (drawable instanceof ColorDrawable) {
                                                            ColorDrawable color = (ColorDrawable) drawable;
                                                            if (Build.VERSION.SDK_INT < 11) {
                                                                try {
                                                                    Field field = color.getClass().getDeclaredField("mState");
                                                                    field.setAccessible(true);
                                                                    Object object = field.get(color);
                                                                    Field field2 = object.getClass().getDeclaredField("mUseColor");
                                                                    field2.setAccessible(true);
                                                                    if (field2.getInt(object) != -16711681) {
                                                                        if (ignition_timing_live.this.nilai_afr > 0.0d && ignition_timing_live.this.nilai_afr < 120.0d) {
                                                                            clear.setBackgroundColor(Color.argb(255, 15, 11, 239));
                                                                        } else if (ignition_timing_live.this.nilai_afr >= 120.0d && ignition_timing_live.this.nilai_afr < 130.0d) {
                                                                            clear.setBackgroundColor(Color.argb(255, 95, 239, 11));
                                                                        } else if (ignition_timing_live.this.nilai_afr >= 130.0d && ignition_timing_live.this.nilai_afr < 140.0d) {
                                                                            clear.setBackgroundColor(Color.argb(255, 239, 235, 11));
                                                                        } else if (ignition_timing_live.this.nilai_afr >= 140.0d) {
                                                                            clear.setBackgroundColor(Color.argb(255, 239, 11, 22));
                                                                        } else {
                                                                            clear.setBackgroundColor(-1);
                                                                        }
                                                                    }
                                                                } catch (NoSuchFieldException e2) {
                                                                } catch (IllegalAccessException e3) {
                                                                }
                                                            } else if (color.getColor() != -16711681) {
                                                                if (ignition_timing_live.this.nilai_afr > 0.0d && ignition_timing_live.this.nilai_afr < 120.0d) {
                                                                    clear.setBackgroundColor(Color.argb(255, 15, 11, 239));
                                                                } else if (ignition_timing_live.this.nilai_afr >= 120.0d && ignition_timing_live.this.nilai_afr < 130.0d) {
                                                                    clear.setBackgroundColor(Color.argb(255, 95, 239, 11));
                                                                } else if (ignition_timing_live.this.nilai_afr >= 130.0d && ignition_timing_live.this.nilai_afr < 140.0d) {
                                                                    clear.setBackgroundColor(Color.argb(255, 239, 235, 11));
                                                                } else if (ignition_timing_live.this.nilai_afr >= 140.0d) {
                                                                    clear.setBackgroundColor(Color.argb(255, 239, 11, 22));
                                                                } else {
                                                                    clear.setBackgroundColor(-1);
                                                                }
                                                            }
                                                        }
                                                    } catch (Exception e4) {
                                                    }
                                                    int posisi_akhir = (ignition_timing_live.this.posisi_tps * 31) + ignition_timing_live.this.posisi_rpm + 1;
                                                    EditText edit = (EditText) ignition_timing_live.this.findViewById(posisi_akhir);
                                                    try {
                                                        if (!ignition_timing_live.this.lock_cursor.booleanValue()) {
                                                            edit.setBackgroundColor(Color.argb(255, 255, 153, 36));
                                                            edit.requestFocus();
                                                        }
                                                    } catch (Exception e5) {
                                                    }
                                                    ignition_timing_live.this.posisi_sebelumnya = posisi_akhir;
                                                    ignition_timing_live.this.rpm_sebelumnya = ignition_timing_live.this.posisi_rpm;
                                                    ignition_timing_live.this.tps_sebelumnya = ignition_timing_live.this.posisi_tps;
                                                    return;
                                                } catch (Exception e6) {
                                                    return;
                                                }
                                            case 5:
                                                ignition_timing_live.this.posisi = 6;
                                                return;
                                            case 6:
                                                ignition_timing_live.this.posisi = 7;
                                                return;
                                            case 7:
                                                ignition_timing_live.this.posisi = 8;
                                                try {
                                                    ignition_timing_live.this.nilai_afr = Double.valueOf(data).doubleValue() * 10.0d;
                                                    int posisi_list_afr = (ignition_timing_live.this.posisi_tps * 31) + ignition_timing_live.this.posisi_rpm;
                                                    MappingHandle.list_afr_live_ig.remove(posisi_list_afr);
                                                    MappingHandle.list_afr_live_ig.add(posisi_list_afr, data);
                                                    return;
                                                } catch (Exception e7) {
                                                    return;
                                                }
                                            case 8:
                                                ignition_timing_live.this.posisi = 9;
                                                return;
                                            case 9:
                                                ignition_timing_live.this.posisi = 10;
                                                return;
                                            case 10:
                                                ignition_timing_live.this.posisi = 11;
                                                return;
                                            case 11:
                                                ignition_timing_live.this.posisi = 0;
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
                        } else if (this.cek == 1) {
                            if (b == 59 || b == 10) {
                                byte[] encodedBytes2 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes2, 0, encodedBytes2.length);
                                final String data2 = new String(encodedBytes2, "US-ASCII").replaceAll("\n", "").replaceAll("\r", "");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (data2.equals("1A00") || data2.contains("1A00")) {
                                            if (ignition_timing_live.this.hitung_tps == 0) {
                                                ignition_timing_live.this.handler.removeCallbacks(ignition_timing_live.this.sendData);
                                                ignition_timing_live.this.hitung_tps++;
                                            }
                                            if (ignition_timing_live.this.hitung_tps < 21) {
                                                ignition_timing_live.this.kirim(ignition_timing_live.this.posisi_memory);
                                                return;
                                            }
                                            ignition_timing_live.this.cek = 0;
                                            Message message23 = Message.obtain();
                                            message23.obj = "Send Done";
                                            message23.setTarget(ignition_timing_live.this.mHandler);
                                            message23.sendToTarget();
                                            if (ignition_timing_live.this.progressDialog.isShowing()) {
                                                ignition_timing_live.this.progressDialog.dismiss();
                                            }
                                            ignition_timing_live.this.hitung_tps = 0;
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr2 = this.readBuffer;
                                int i3 = this.readBufferPosition;
                                this.readBufferPosition = i3 + 1;
                                bArr2[i3] = b;
                            }
                        } else if (this.cek == 2) {
                            if (b == 59 || b == 10) {
                                byte[] encodedBytes3 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes3, 0, encodedBytes3.length);
                                final String data3 = new String(encodedBytes3, "US-ASCII").replaceAll("\n", "").replaceAll("\r", "");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (data3.equals("1A00")) {
                                            ignition_timing_live.this.cek = 0;
                                            Message message23 = Message.obtain();
                                            message23.obj = "Send Done";
                                            message23.setTarget(ignition_timing_live.this.mHandler);
                                            message23.sendToTarget();
                                            ignition_timing_live.this.hitung_tps = 0;
                                            ignition_timing_live.this.resetConnection();
                                            ignition_timing_live.this.finish();
                                        } else if (data3.contains("1A00")) {
                                            ignition_timing_live.this.cek = 0;
                                            Message message232 = Message.obtain();
                                            message232.obj = "Send Done";
                                            message232.setTarget(ignition_timing_live.this.mHandler);
                                            message232.sendToTarget();
                                            ignition_timing_live.this.hitung_tps = 0;
                                            ignition_timing_live.this.resetConnection();
                                            ignition_timing_live.this.finish();
                                        } else {
                                            ignition_timing_live.this.execute();
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr3 = this.readBuffer;
                                int i4 = this.readBufferPosition;
                                this.readBufferPosition = i4 + 1;
                                bArr3[i4] = b;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                this.stopWorker = true;
            }
        }
    }

    public void kirim(String posisi2) {
        this.cek = 1;
        String ack = "2605;" + posisi2 + ";" + String.valueOf(this.hitung_tps) + ";";
        String data = "";
        for (int i = 1; i <= 31; i++) {
            String teks = String.valueOf(((EditText) findViewById((this.hitung_tps * 31) + i)).getText());
            if (i == 31) {
                data = data + teks;
            } else {
                data = data + teks + ";";
            }
        }
        String kirim_final = ack + (data + "\r\n");
        byte[] msgBuffer = kirim_final.getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = kirim_final;
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
        this.hitung_tps++;
    }

    /* access modifiers changed from: private */
    public void progressOn() {
        runOnUiThread(new Runnable() {
            public void run() {
                ignition_timing_live.this.progressDialog = new ProgressDialog(ignition_timing_live.this);
                ignition_timing_live.this.progressDialog.setMessage("Loading...");
                ignition_timing_live.this.progressDialog.setTitle("Send Map");
                ignition_timing_live.this.progressDialog.setProgressStyle(0);
                ignition_timing_live.this.progressDialog.show();
                ignition_timing_live.this.progressDialog.setCancelable(true);
            }
        });
        Toast.makeText(getApplicationContext(), "Send data", 0).show();
    }

    /* access modifiers changed from: private */
    public void kirim_update(String posisi2) {
        this.cek = 1;
        String ack = "2605;" + posisi2 + ";" + String.valueOf(this.hitung_tps) + ";";
        String data = "";
        for (int i = 1; i <= 31; i++) {
            String teks = String.valueOf(((EditText) findViewById((this.hitung_tps * 31) + i)).getText());
            if (i == 31) {
                data = data + teks;
            } else {
                data = data + teks + ";";
            }
        }
        String kirim_final = ack + (data + "\r\n");
        byte[] msgBuffer = kirim_final.getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = kirim_final;
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

    public void kirim_awal(String posisi2) {
        this.cek = 1;
        this.hitung_tps = 0;
        String ack = "2605;" + posisi2 + ";" + String.valueOf(this.hitung_tps) + ";";
        String data = "";
        this.hitung = this.hitung_tps + 1;
        for (int i = 1; i <= 31; i++) {
            String teks = String.valueOf(((EditText) findViewById(this.hitung)).getText());
            if (i == 31) {
                data = data + teks;
            } else {
                data = data + teks + ";";
            }
            this.hitung += 21;
        }
        String kirim_final = ack + (data + "\r\n");
        byte[] msgBuffer = kirim_final.getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = kirim_final;
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
        this.hitung_tps = 1;
    }

    public void execute() {
        this.cek = 2;
        byte[] msgBuffer = "5009\r\n".getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = "5009\r\n";
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
        if (this.hitung_tps == 0) {
            Toast.makeText(getApplicationContext(), "Send execute", 0).show();
        }
        this.hitung_tps++;
    }

    /* access modifiers changed from: private */
    public void add_value() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle((CharSequence) "Add Value");
        alert.setMessage((CharSequence) "Enter Your Value Here");
        final EditText inputNilaiAdd = new EditText(this);
        inputNilaiAdd.setKeyListener(SignedDecimalKeyListener2.getInstance());
        inputNilaiAdd.setFilters(new InputFilter[]{new range_float2(1)});
        inputNilaiAdd.setImeOptions(268435456);
        alert.setView((View) inputNilaiAdd);
        alert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Double val = Double.valueOf(0.0d);
                try {
                    val = Double.valueOf(Double.parseDouble(inputNilaiAdd.getText().toString()));
                } catch (NullPointerException | NumberFormatException e) {
                }
                int value = (int) (val.doubleValue() * 10.0d);
                int hasil = ignition_timing_live.this.mod(value, 5);
                if (hasil == 0 && val.doubleValue() <= 90.0d && val.doubleValue() >= 0.0d) {
                    ignition_timing_live.this.AddValue(String.valueOf(val));
                } else if (val.doubleValue() > 90.0d) {
                    ignition_timing_live.this.AddValue("90.0");
                } else if (val.doubleValue() < 0.0d) {
                    ignition_timing_live.this.AddValue("0.0");
                } else if (hasil != 0) {
                    ignition_timing_live.this.AddValue(String.valueOf(((double) (value - hasil)) / 10.0d));
                }
            }
        });
        alert.setNegativeButton((CharSequence) "CANCEL", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.create().show();
    }

    /* access modifiers changed from: private */
    public void set_value() {
        new AlertDialog.Builder(this);
        AlertDialog.Builder alert_set = new AlertDialog.Builder(this);
        alert_set.setTitle((CharSequence) "Set Value");
        alert_set.setMessage((CharSequence) "Enter Your Value Here");
        final EditText inputNilai = new EditText(this);
        inputNilai.setKeyListener(SignedDecimalKeyListener2.getInstance());
        inputNilai.setFilters(new InputFilter[]{new range_float2(1)});
        inputNilai.setImeOptions(268435456);
        alert_set.setView((View) inputNilai);
        alert_set.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Double val = Double.valueOf(0.0d);
                try {
                    val = Double.valueOf(Double.parseDouble(inputNilai.getText().toString()));
                } catch (NullPointerException | NumberFormatException e) {
                }
                int value = (int) (val.doubleValue() * 10.0d);
                int hasil = ignition_timing_live.this.mod(value, 5);
                if (hasil == 0 && val.doubleValue() <= 90.0d && val.doubleValue() >= 0.0d) {
                    ignition_timing_live.this.SetValue(String.valueOf(val));
                } else if (val.doubleValue() > 90.0d) {
                    ignition_timing_live.this.SetValue("90.0");
                } else if (val.doubleValue() < 0.0d) {
                    ignition_timing_live.this.SetValue("0.0");
                } else if (hasil != 0) {
                    ignition_timing_live.this.SetValue(String.valueOf(((double) (value - hasil)) / 10.0d));
                }
            }
        });
        alert_set.setNegativeButton((CharSequence) "CANCEL", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog create = alert_set.create();
        alert_set.create().show();
    }

    public void AddValue(String Value) {
        double nilai;
        for (int i = 1; i <= 651; i++) {
            EditText v = (EditText) findViewById(i);
            String str = v.getText().toString();
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                if (str.length() != 0) {
                    nilai = Double.parseDouble(v.getText().toString());
                } else {
                    nilai = Double.parseDouble("0");
                }
                double nilai2 = nilai + Double.parseDouble(Value);
                if (nilai2 > 90.0d) {
                    nilai2 = 90.0d;
                } else if (nilai2 < 0.0d) {
                    nilai2 = 0.0d;
                }
                v.setText(String.valueOf(nilai2));
                v.setTextColor(Color.argb(255, 240, 81, 0));
            }
        }
    }

    public void SetValue(String Value) {
        for (int i = 1; i <= 651; i++) {
            EditText v = (EditText) findViewById(i);
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                v.setText(Value);
                v.setTextColor(Color.argb(255, 240, 81, 0));
            }
        }
    }

    /* access modifiers changed from: private */
    public void plus_minus(String pilihan) {
        double nilai;
        for (int i = 1; i <= 651; i++) {
            EditText v = (EditText) findViewById(i);
            String str = v.getText().toString();
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                if (str.length() != 0) {
                    nilai = Double.parseDouble(v.getText().toString());
                } else {
                    nilai = Double.parseDouble("0");
                }
                if (pilihan.equals("plus")) {
                    nilai += 0.5d;
                } else if (pilihan.equals("minus")) {
                    nilai -= 0.5d;
                }
                if (nilai > 90.0d) {
                    nilai = 90.0d;
                } else if (nilai < 0.0d) {
                    nilai = 0.0d;
                }
                v.setText(String.valueOf(nilai));
                v.setTextColor(Color.argb(255, 240, 81, 0));
            }
        }
    }

    /* access modifiers changed from: private */
    public void select() {
        StaticClass.selectionStatus = false;
        StaticClass.position.clear();
        for (int i = 1; i <= 651; i++) {
            StaticClass.position.add(Integer.valueOf(i));
            EditText x = (EditText) findViewById(i);
            x.setBackgroundColor(-16711681);
            x.setTag("Warna CYAN");
        }
    }

    public void Copy() {
        this.status_flag = 1;
        if (StaticClass.letak.size() != 0) {
            StaticClass.data.clear();
            StaticClass.letak.clear();
        }
        for (int i = 1; i <= 651; i++) {
            EditText v = (EditText) findViewById(i);
            String str = v.getText().toString();
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                StaticClass.data.add(str);
                StaticClass.letak.add(Integer.valueOf(i));
                this.jumlah++;
            }
        }
        this.maksimum = this.jumlah;
        this.jumlah = 0;
        Toast.makeText(getApplicationContext(), "copied", 0).show();
    }

    public void Paste() {
        try {
            if (this.status_flag == 1) {
                this.status_flag = 1;
                int nilai_acuan = StaticClass.letak.get(0).intValue();
                for (int i = 1; i <= 651; i++) {
                    EditText v = (EditText) findViewById(i);
                    if (v.getTag() != null && v.getTag().equals("Warna BLUE")) {
                        for (int k = 0; k < this.maksimum; k++) {
                            String data_ditulis = StaticClass.data.get(k);
                            int letakan = i + (StaticClass.letak.get(k).intValue() - nilai_acuan);
                            this.pengingat = letakan;
                            if (letakan <= 651) {
                                EditText pake = (EditText) findViewById(letakan);
                                pake.setText(data_ditulis);
                                pake.setTextColor(Color.argb(255, 240, 81, 0));
                            }
                        }
                        StaticClass.position.add(Integer.valueOf(i));
                    }
                }
                StaticClass.selectionStatus = false;
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public int mod(int x, int y) {
        int result = x % y;
        if (result < 0) {
            return result + y;
        }
        return result;
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
                    case 1481843414:
                        if (data.equals("LiveData")) {
                            c = 0;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        String mentah = StaticClass.DataLive;
                        if (ignition_timing_live.this.cek == 0) {
                            String[] dataEnter = mentah.replace("\r", "").split("\n");
                            for (String split : dataEnter) {
                                String[] dataBener = split.split(";");
                                if (dataBener[0].equals("A603") || dataBener[0].equals("a603")) {
                                    try {
                                        ignition_timing_live.this.posisi_tps = Integer.parseInt(dataBener[1]);
                                    } catch (Exception e) {
                                    }
                                    try {
                                        float rpm_baru = Float.parseFloat(dataBener[4]);
                                        if (rpm_baru < 1000.0f) {
                                            rpm_baru = 1000.0f;
                                        } else if (rpm_baru > 16000.0f) {
                                            rpm_baru = 16000.0f;
                                        }
                                        ignition_timing_live.this.posisi_rpm = (((Math.round((4.0f * rpm_baru) / 1000.0f) * 1000) / 4) + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED) / 500;
                                        EditText clear = (EditText) ignition_timing_live.this.findViewById(ignition_timing_live.this.posisi_sebelumnya);
                                        try {
                                            Drawable drawable = clear.getBackground();
                                            if (drawable instanceof ColorDrawable) {
                                                ColorDrawable color = (ColorDrawable) drawable;
                                                if (Build.VERSION.SDK_INT < 11) {
                                                    try {
                                                        Field field = color.getClass().getDeclaredField("mState");
                                                        field.setAccessible(true);
                                                        Object object = field.get(color);
                                                        Field field2 = object.getClass().getDeclaredField("mUseColor");
                                                        field2.setAccessible(true);
                                                        if (field2.getInt(object) != -16711681) {
                                                            if (ignition_timing_live.this.nilai_afr > 0.0d && ignition_timing_live.this.nilai_afr < 120.0d) {
                                                                clear.setBackgroundColor(Color.argb(255, 15, 11, 239));
                                                            } else if (ignition_timing_live.this.nilai_afr >= 120.0d && ignition_timing_live.this.nilai_afr < 130.0d) {
                                                                clear.setBackgroundColor(Color.argb(255, 95, 239, 11));
                                                            } else if (ignition_timing_live.this.nilai_afr >= 130.0d && ignition_timing_live.this.nilai_afr < 140.0d) {
                                                                clear.setBackgroundColor(Color.argb(255, 239, 235, 11));
                                                            } else if (ignition_timing_live.this.nilai_afr >= 140.0d) {
                                                                clear.setBackgroundColor(Color.argb(255, 239, 11, 22));
                                                            } else {
                                                                clear.setBackgroundColor(-1);
                                                            }
                                                        }
                                                    } catch (NoSuchFieldException e2) {
                                                    } catch (IllegalAccessException e3) {
                                                    }
                                                } else if (color.getColor() != -16711681) {
                                                    if (ignition_timing_live.this.nilai_afr > 0.0d && ignition_timing_live.this.nilai_afr < 120.0d) {
                                                        clear.setBackgroundColor(Color.argb(255, 15, 11, 239));
                                                    } else if (ignition_timing_live.this.nilai_afr >= 120.0d && ignition_timing_live.this.nilai_afr < 130.0d) {
                                                        clear.setBackgroundColor(Color.argb(255, 95, 239, 11));
                                                    } else if (ignition_timing_live.this.nilai_afr >= 130.0d && ignition_timing_live.this.nilai_afr < 140.0d) {
                                                        clear.setBackgroundColor(Color.argb(255, 239, 235, 11));
                                                    } else if (ignition_timing_live.this.nilai_afr >= 140.0d) {
                                                        clear.setBackgroundColor(Color.argb(255, 239, 11, 22));
                                                    } else {
                                                        clear.setBackgroundColor(-1);
                                                    }
                                                }
                                            }
                                        } catch (Exception e4) {
                                        }
                                        int posisi_akhir = (ignition_timing_live.this.posisi_tps * 31) + ignition_timing_live.this.posisi_rpm + 1;
                                        EditText edit = (EditText) ignition_timing_live.this.findViewById(posisi_akhir);
                                        try {
                                            if (!ignition_timing_live.this.lock_cursor.booleanValue()) {
                                                edit.setBackgroundColor(Color.argb(255, 255, 153, 36));
                                                edit.requestFocus();
                                            }
                                        } catch (Exception e5) {
                                        }
                                        ignition_timing_live.this.posisi_sebelumnya = posisi_akhir;
                                        ignition_timing_live.this.rpm_sebelumnya = ignition_timing_live.this.posisi_rpm;
                                        ignition_timing_live.this.tps_sebelumnya = ignition_timing_live.this.posisi_tps;
                                    } catch (Exception e6) {
                                    }
                                    try {
                                        ignition_timing_live.this.nilai_afr = Double.valueOf(dataBener[7]).doubleValue() * 10.0d;
                                        int posisi_list_afr = (ignition_timing_live.this.posisi_tps * 31) + ignition_timing_live.this.posisi_rpm;
                                        MappingHandle.list_afr_live_ig.remove(posisi_list_afr);
                                        MappingHandle.list_afr_live_ig.add(posisi_list_afr, dataBener[7]);
                                    } catch (Exception e7) {
                                    }
                                }
                            }
                            return;
                        } else if (ignition_timing_live.this.cek == 1) {
                            if (mentah.contains("1A00")) {
                                if (ignition_timing_live.this.hitung_tps == 0) {
                                    ignition_timing_live.this.handler.removeCallbacks(ignition_timing_live.this.sendData);
                                    ignition_timing_live.this.hitung_tps++;
                                }
                                if (ignition_timing_live.this.hitung_tps < 21) {
                                    ignition_timing_live.this.kirim(ignition_timing_live.this.posisi_memory);
                                    return;
                                }
                                ignition_timing_live.this.cek = 0;
                                Message message23 = Message.obtain();
                                message23.obj = "Send Done";
                                message23.setTarget(ignition_timing_live.this.mHandler);
                                message23.sendToTarget();
                                if (ignition_timing_live.this.progressDialog.isShowing()) {
                                    ignition_timing_live.this.progressDialog.dismiss();
                                    return;
                                }
                                return;
                            }
                            return;
                        } else if (ignition_timing_live.this.cek != 2) {
                            return;
                        } else {
                            if (data.contains("1A00")) {
                                ignition_timing_live.this.cek = 0;
                                Message message232 = Message.obtain();
                                message232.obj = "Send Done";
                                message232.setTarget(ignition_timing_live.this.mHandler);
                                message232.sendToTarget();
                                ignition_timing_live.this.hitung_tps = 0;
                                ignition_timing_live.this.finish();
                                return;
                            }
                            ignition_timing_live.this.execute();
                            return;
                        }
                    case 1:
                        StaticClass.Live = false;
                        ignition_timing_live.this.finish();
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
