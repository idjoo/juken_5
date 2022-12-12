package juken.android.com.juken_5;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import juken.android.com.juken_5.live.StaticClassExecute;
import juken.android.com.juken_5.live.live_awal;

public class e_map extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private static final String[] array1 = {"1000", "1250", "1500", "1750", "2000", "2250", "2500", "2750", "3000", "3250", "3500", "3750", "4000", "4250", "4500", "4750", "5000", "5250", "5500", "5750", "6000", "6250", "6500", "6750", "7000", "7250", "7500", "7750", "8000", "8250", "8500", "8750", "9000", "9250", "9500", "9750", "10000", "10250", "10500", "10750", "11000", "11250", "11500", "11750", "12000", "12250", "12500", "12750", "13000", "13250", "13500", "13750", "14000", "14250", "14500", "14750", "15000", "15250", "15500", "15750", "16000"};
    /* access modifiers changed from: private */
    public static final String[] array_emap = {"1500", "1750", "2000", "2250", "2500", "2750", "3000", "3250", "3500", "3750", "4000", "4250", "4500", "4750", "5000", "5250", "5500", "5750", "6000", "6250", "6500", "6750", "7000", "7250", "7500", "7750", "8000", "8250", "8500", "8750", "9000", "9250", "9500", "9750", "10000", "10250", "10500", "10750", "11000", "11250", "11500", "11750", "12000", "12250", "12500", "12750", "13000", "13250", "13500", "13750", "14000", "14250", "14500", "14750", "15000", "15250", "15500"};
    private static final String[] array_tps_jss = {"2", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90", "100"};
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            e_map.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    int cek = 0;
    int core_dipilih = 0;
    String data = "";
    EditText emap_high;
    EditText emap_low;
    EditText emap_mid;
    TextView et_high_high;
    TextView et_high_low;
    TextView et_low_high;
    Spinner high_mid;
    int hitung = 1;
    int hitung_tps = 0;
    private InputStream inputStream = null;
    final CharSequence[] items = {"F Core 1", "F Core 2"};
    int jss_rpm_atas = 0;
    int jss_rpm_bawah = 0;
    Spinner jss_rpm_high;
    Spinner jss_rpm_low;
    int jss_tps_atas = 1;
    int jss_tps_bawah = 1;
    Spinner jss_tps_high;
    Spinner jss_tps_low;
    EditText jss_value;
    int keadaan = 0;
    Spinner low_mid;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            Toast.makeText(e_map.this.getApplicationContext(), (String) message.obj, 1).show();
        }
    };
    MyBroadCastReceiver myBroadCastReceiver;
    int nilai_mid_high = 8000;
    int nilai_mid_low = 4000;
    int nyambung = 1;
    private OutputStream outputStream = null;
    String posisi_memory = "1";
    ProgressDialog progressDialog;
    String range_hi1 = "31";
    String range_lo1 = "14";
    String range_mid1 = "15";
    byte[] readBuffer;
    int readBufferPosition;
    int rpm_atas = 0;
    int rpm_bawah = 0;
    Intent service;
    SharedPreferences sharedPreferences;
    private BluetoothSocket socket = null;
    volatile boolean stopWorker;
    int tps_atas = 0;
    int tps_bawah = 0;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_map);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        this.emap_low = (EditText) findViewById(R.id.et_value_min);
        this.emap_low.setFilters(new InputFilter[]{new range_int_min("-100", "100")});
        this.emap_low.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                e_map.this.ubah_data();
            }
        });
        this.emap_mid = (EditText) findViewById(R.id.et_value_mid);
        this.emap_mid.setFilters(new InputFilter[]{new range_int_min("-100", "100")});
        this.emap_mid.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                e_map.this.ubah_data();
            }
        });
        this.emap_high = (EditText) findViewById(R.id.et_value_high);
        this.emap_high.setFilters(new InputFilter[]{new range_int_min("-100", "100")});
        this.emap_high.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                e_map.this.ubah_data();
            }
        });
        this.jss_value = (EditText) findViewById(R.id.et_value_jss);
        this.jss_value.setFilters(new InputFilter[]{new range_int_min("-100", "100")});
        this.jss_value.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                e_map.this.ubah_data();
            }
        });
        this.low_mid = (Spinner) findViewById(R.id.et_low_mid);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 17367048, array_emap);
        adapter.setDropDownViewResource(17367049);
        this.low_mid.setAdapter(adapter);
        this.low_mid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                e_map.this.nilai_mid_low = Integer.parseInt(e_map.array_emap[position].toString());
                e_map.this.ubah_mid_low(view);
                e_map.this.ubah_data();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.high_mid = (Spinner) findViewById(R.id.et_high_mid);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, 17367048, array_emap);
        adapter1.setDropDownViewResource(17367049);
        this.high_mid.setAdapter(adapter1);
        this.high_mid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                e_map.this.nilai_mid_high = Integer.parseInt(e_map.array_emap[position].toString());
                e_map.this.ubah_mid_high(view);
                e_map.this.ubah_data();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.jss_tps_low = (Spinner) findViewById(R.id.et_tps_low);
        ArrayAdapter<String> adapter_tps_low = new ArrayAdapter<>(this, 17367048, array_tps_jss);
        adapter_tps_low.setDropDownViewResource(17367049);
        this.jss_tps_low.setAdapter(adapter_tps_low);
        this.jss_tps_low.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (Integer.valueOf(e_map.this.jss_tps_low.getSelectedItem().toString()).intValue() >= Integer.valueOf(e_map.this.jss_tps_high.getSelectedItem().toString()).intValue()) {
                    e_map.this.jss_tps_low.setSelection(e_map.this.jss_tps_high.getSelectedItemPosition());
                }
                e_map.this.ubah_data();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.jss_tps_high = (Spinner) findViewById(R.id.et_tps_high);
        ArrayAdapter<String> adapter_tps_high = new ArrayAdapter<>(this, 17367048, array_tps_jss);
        adapter_tps_high.setDropDownViewResource(17367049);
        this.jss_tps_high.setAdapter(adapter_tps_high);
        this.jss_tps_high.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (Integer.valueOf(e_map.this.jss_tps_high.getSelectedItem().toString()).intValue() <= Integer.valueOf(e_map.this.jss_tps_low.getSelectedItem().toString()).intValue()) {
                    e_map.this.jss_tps_high.setSelection(e_map.this.jss_tps_low.getSelectedItemPosition());
                }
                e_map.this.ubah_data();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.jss_rpm_low = (Spinner) findViewById(R.id.et_rpm_low);
        ArrayAdapter<String> adapter_rpm_low = new ArrayAdapter<>(this, 17367048, array1);
        adapter_rpm_low.setDropDownViewResource(17367049);
        this.jss_rpm_low.setAdapter(adapter_rpm_low);
        this.jss_rpm_low.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int rpm_lo = Integer.valueOf(e_map.this.jss_rpm_low.getSelectedItem().toString()).intValue();
                int rpm_hi = Integer.valueOf(e_map.this.jss_rpm_high.getSelectedItem().toString()).intValue();
                if (rpm_lo >= rpm_hi) {
                    e_map.this.jss_rpm_low.setSelection((rpm_hi + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED) / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
                }
                e_map.this.ubah_data();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.jss_rpm_high = (Spinner) findViewById(R.id.et_rpm_high);
        ArrayAdapter<String> adapter_rpm_high = new ArrayAdapter<>(this, 17367048, array1);
        adapter_rpm_high.setDropDownViewResource(17367049);
        this.jss_rpm_high.setAdapter(adapter_rpm_high);
        this.jss_rpm_high.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int rpm_lo = Integer.valueOf(e_map.this.jss_rpm_low.getSelectedItem().toString()).intValue();
                if (Integer.valueOf(e_map.this.jss_rpm_high.getSelectedItem().toString()).intValue() <= rpm_lo) {
                    e_map.this.jss_rpm_high.setSelection((rpm_lo + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED) / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
                }
                e_map.this.ubah_data();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ((Button) findViewById(R.id.save_emap)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                e_map.this.core_dipilih = 0;
                try {
                    e_map.this.core_dipilih = Integer.valueOf(e_map.this.sharedPreferences.getString("tss", "tss1")).intValue();
                } catch (Exception e) {
                }
                MappingHandle.list_fuel.clear();
                MappingHandle.list_fuel_live.clear();
                for (int i = 1; i <= 1281; i++) {
                    String data = ((EditText) e_map.this.findViewById(i)).getText().toString();
                    MappingHandle.list_fuel.add(data);
                    MappingHandle.list_fuel_live.add(data);
                }
                int tengah1 = Integer.valueOf(e_map.this.low_mid.getSelectedItem().toString()).intValue();
                int tengah2 = Integer.valueOf(e_map.this.high_mid.getSelectedItem().toString()).intValue();
                int range_lo5 = (tengah1 + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED) / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
                int range_mid5 = ((tengah2 - tengah1) + ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
                e_map.this.range_lo1 = String.valueOf(range_lo5);
                e_map.this.range_mid1 = String.valueOf(range_mid5);
                e_map e_map = e_map.this;
                e_map.range_hi1 = String.valueOf(61 - (range_lo5 + range_mid5));
                e_map.this.SavePreferences("range_lo", e_map.this.range_lo1);
                e_map.this.SavePreferences("range_mig", e_map.this.range_mid1);
                e_map.this.SavePreferences("range_hi", e_map.this.range_hi1);
                e_map.this.emap_low.setText("0");
                e_map.this.emap_mid.setText("0");
                e_map.this.emap_high.setText("0");
                e_map.this.jss_value.setText("0");
                e_map.this.jss_tps_bawah = e_map.this.jss_tps_low.getSelectedItemPosition() + 1;
                e_map.this.jss_tps_atas = e_map.this.jss_tps_high.getSelectedItemPosition() + 1;
                e_map.this.jss_rpm_bawah = e_map.this.jss_rpm_low.getSelectedItemPosition();
                e_map.this.jss_rpm_atas = e_map.this.jss_rpm_high.getSelectedItemPosition();
                String jjsore_tps1 = String.valueOf((e_map.this.jss_tps_atas << 8) + e_map.this.jss_tps_bawah);
                String jjsore_rpm1 = String.valueOf((e_map.this.jss_rpm_atas << 8) + e_map.this.jss_rpm_bawah);
                e_map.this.SavePreferences("jjsore_tps", jjsore_tps1);
                e_map.this.SavePreferences("jjsore_rpm", jjsore_rpm1);
                if (e_map.this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
                    StaticClassExecute.bisa_execute = true;
                }
                e_map.this.cek = 0;
                e_map.this.keadaan = 0;
                if (e_map.this.hitung_tps > 0) {
                    e_map.this.hitung_tps = 0;
                }
                if (e_map.this.core_dipilih == 0) {
                    e_map.this.posisi_memory = e_map.this.sharedPreferences.getString("f_c1", "f_c11");
                } else {
                    e_map.this.posisi_memory = e_map.this.sharedPreferences.getString("f_c2", "f_c21");
                }
                e_map.this.kirim(e_map.this.posisi_memory);
            }
        });
        ((Button) findViewById(R.id.exit_emap)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (StaticClass.TipeKoneksi.equals("wifi")) {
                    e_map.this.unregisterReceiver(e_map.this.myBroadCastReceiver);
                } else {
                    try {
                        if (e_map.this.nyambung == 1) {
                            e_map.this.resetConnection();
                            Thread.sleep(700);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                e_map.this.finish();
                if (e_map.this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    e_map.this.startActivityForResult(new Intent(e_map.this.getApplicationContext(), live_awal.class), 0);
                }
            }
        });
    }

    public void kirim(String posisi) {
        this.cek = 0;
        String ack = "2602;" + posisi + ";" + String.valueOf(this.hitung_tps) + ";";
        String data2 = "";
        for (int i = 1; i <= 61; i++) {
            String teks = String.valueOf(((EditText) findViewById((this.hitung_tps * 61) + i)).getText());
            if (i == 61) {
                data2 = data2 + teks;
            } else {
                data2 = data2 + teks + ";";
            }
        }
        String kirim_final = ack + (data2 + "\r\n");
        byte[] msgKirim = kirim_final.getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = kirim_final;
            this.service.putExtra("send", "GlobalData");
            startService(this.service);
        } else if (StaticClass.TipeKoneksi.equals("bt") && this.nyambung == 1) {
            try {
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(msgKirim);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.hitung_tps == 0) {
            runOnUiThread(new Runnable() {
                public void run() {
                    e_map.this.progressDialog = new ProgressDialog(e_map.this);
                    e_map.this.progressDialog.setMessage("Loading...");
                    e_map.this.progressDialog.setTitle("Send Map");
                    e_map.this.progressDialog.setProgressStyle(0);
                    e_map.this.progressDialog.show();
                    e_map.this.progressDialog.setCancelable(true);
                }
            });
            Toast.makeText(getApplicationContext(), "Send data", 0).show();
        }
        this.hitung_tps++;
    }

    public void kirim_emap_range() {
        String data2 = "3601;" + this.range_lo1 + ";" + this.range_mid1 + ";" + this.range_hi1 + "\r\n";
        byte[] msgKirim = data2.getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = data2;
            this.service.putExtra("send", "GlobalData");
            startService(this.service);
        } else if (StaticClass.TipeKoneksi.equals("bt") && this.nyambung == 1) {
            try {
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(msgKirim);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void kirim_jss() {
        String data2 = "3616;" + String.valueOf(this.jss_tps_bawah) + ";" + String.valueOf(this.jss_tps_atas) + ";" + String.valueOf(this.jss_rpm_bawah) + ";" + String.valueOf(this.jss_rpm_atas) + "\r\n";
        byte[] msgKirim = data2.getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = data2;
            this.service.putExtra("send", "GlobalData");
            startService(this.service);
        } else if (StaticClass.TipeKoneksi.equals("bt") && this.nyambung == 1) {
            try {
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(msgKirim);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public void ubah_data() {
        try {
            String nilai_low = this.emap_low.getText().toString();
            String nilai_mid = this.emap_mid.getText().toString();
            String nilai_high = this.emap_high.getText().toString();
            int batas_bawah = (Integer.valueOf(this.et_high_low.getText().toString()).intValue() / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) - 3;
            int batas_atas = (Integer.valueOf(this.et_low_high.getText().toString()).intValue() / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) - 3;
            int rpm_high = (Integer.valueOf(this.jss_rpm_high.getSelectedItem().toString()).intValue() / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) - 4;
            int rpm_low = (Integer.valueOf(this.jss_rpm_low.getSelectedItem().toString()).intValue() / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) - 4;
            int tps_low = this.jss_tps_low.getSelectedItemPosition() + 1;
            int tps_high = this.jss_tps_high.getSelectedItemPosition() + 1;
            int nilai_jss = Integer.valueOf(this.jss_value.getText().toString()).intValue();
            int posisi = 1;
            for (int i = 1; i <= batas_bawah * 21; i++) {
                if (!nilai_low.equals("")) {
                    int nilai_fuel = Integer.valueOf(MappingHandle.list_fuel.get(posisi - 1)).intValue();
                    if (posisi > 61) {
                        EditText v = (EditText) findViewById(posisi);
                        v.setBackgroundColor(Color.argb(255, 0, 255, 0));
                        int value = Integer.valueOf(nilai_low).intValue() + nilai_fuel;
                        if (value > 100) {
                            value = 100;
                        } else if (value < -100) {
                            value = -100;
                        }
                        v.setText(String.valueOf(value));
                    }
                    posisi += 61;
                    if (posisi > 1281) {
                        posisi -= 1280;
                    }
                }
            }
            int posisi2 = batas_bawah + 1;
            for (int i2 = 1; i2 <= ((batas_atas - batas_bawah) - 1) * 21; i2++) {
                if (!nilai_mid.equals("")) {
                    int nilai_fuel2 = Integer.valueOf(MappingHandle.list_fuel.get(posisi2 - 1)).intValue();
                    if (posisi2 > 61) {
                        EditText v2 = (EditText) findViewById(posisi2);
                        v2.setBackgroundColor(Color.argb(255, 255, 255, 0));
                        int value2 = Integer.valueOf(nilai_mid).intValue() + nilai_fuel2;
                        if (value2 > 100) {
                            value2 = 100;
                        } else if (value2 < -100) {
                            value2 = -100;
                        }
                        v2.setText(String.valueOf(value2));
                    }
                    posisi2 += 61;
                    if (posisi2 > 1281) {
                        posisi2 -= 1280;
                    }
                }
            }
            int posisi3 = batas_atas;
            for (int i3 = 1; i3 <= ((61 - batas_atas) + 1) * 21; i3++) {
                if (!nilai_high.equals("")) {
                    int nilai_fuel3 = Integer.valueOf(MappingHandle.list_fuel.get(posisi3 - 1)).intValue();
                    if (posisi3 > 61) {
                        EditText v3 = (EditText) findViewById(posisi3);
                        v3.setBackgroundColor(Color.argb(255, 255, 0, 0));
                        int value3 = Integer.valueOf(nilai_high).intValue() + nilai_fuel3;
                        if (value3 > 100) {
                            value3 = 100;
                        } else if (value3 < -100) {
                            value3 = -100;
                        }
                        v3.setText(String.valueOf(value3));
                    }
                    posisi3 += 61;
                    if (posisi3 > 1281) {
                        posisi3 -= 1280;
                    }
                }
            }
            for (int i4 = tps_low; i4 <= tps_high; i4++) {
                for (int j = rpm_low; j <= rpm_high; j++) {
                    EditText v4 = (EditText) findViewById((i4 * 61) + j + 1);
                    v4.setBackgroundColor(Color.argb(255, 255, 195, 14));
                    int value4 = Integer.valueOf(v4.getText().toString()).intValue() + nilai_jss;
                    if (value4 > 100) {
                        value4 = 100;
                    } else if (value4 < -100) {
                        value4 = -100;
                    }
                    v4.setText(String.valueOf(value4));
                }
            }
        } catch (Exception e) {
        }
    }

    private int mod(int x, int y) {
        int result = x % y;
        if (result < 0) {
            return result + y;
        }
        return result;
    }

    public void ubah_mid_low(View view) {
        if (this.nilai_mid_low < this.nilai_mid_high) {
            this.et_high_low = (TextView) findViewById(R.id.et_high_low);
            this.et_high_low.setText(String.valueOf((this.nilai_mid_low - 4500) + 4250));
            return;
        }
        this.et_high_low = (TextView) findViewById(R.id.et_high_low);
        this.et_high_low.setText(String.valueOf((this.nilai_mid_low - 4500) + 4250));
        this.high_mid.setSelection((this.nilai_mid_low / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) - 6);
        this.et_low_high = (TextView) findViewById(R.id.et_low_high);
        this.et_low_high.setText(String.valueOf((this.nilai_mid_low - 8250) + 8500));
    }

    public void ubah_mid_high(View view) {
        if (this.nilai_mid_high > this.nilai_mid_low) {
            this.et_low_high = (TextView) findViewById(R.id.et_low_high);
            this.et_low_high.setText(String.valueOf((this.nilai_mid_high - 8250) + 8500));
            return;
        }
        this.et_low_high = (TextView) findViewById(R.id.et_low_high);
        this.et_low_high.setText(String.valueOf((this.nilai_mid_high - 8250) + 8500));
        this.low_mid.setSelection((this.nilai_mid_high / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) - 6);
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
        finish();
        try {
            if (this.sharedPreferences.getString("dari_live", "dari_live1").equals("1")) {
                startActivityForResult(new Intent(getApplicationContext(), live_awal.class), 0);
            }
        } catch (Exception e2) {
        }
    }

    private void LoadPreferences() {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String range_lo = this.sharedPreferences.getString("range_lo", "range_lo1");
        String range_mid = this.sharedPreferences.getString("range_mid", "range_mid1");
        String range_high = this.sharedPreferences.getString("range_hi", "range_hi1");
        String jjs_rpm = this.sharedPreferences.getString("jjsore_rpm", "jjsore_rpm1");
        String jjs_tps = this.sharedPreferences.getString("jjsore_tps", "jjsore_tps1");
        int olahan_rpm = Integer.valueOf(jjs_rpm).intValue();
        this.rpm_atas = (byte) ((olahan_rpm >> 8) & 255);
        this.rpm_bawah = (byte) (olahan_rpm & 255);
        int olahan_tps = Integer.valueOf(jjs_tps).intValue();
        this.tps_atas = (byte) ((olahan_tps >> 8) & 255);
        this.tps_bawah = (byte) (olahan_tps & 255);
        this.jss_rpm_high.setSelection(this.rpm_atas);
        this.jss_rpm_low.setSelection(this.rpm_bawah);
        if (this.tps_atas == 0) {
            this.jss_tps_high.setSelection(0);
        } else {
            this.jss_tps_high.setSelection(this.tps_atas - 1);
        }
        if (this.tps_bawah == 0) {
            this.jss_tps_low.setSelection(0);
        } else {
            this.jss_tps_low.setSelection(this.tps_bawah - 1);
        }
        int range_bawah = Integer.parseInt(range_lo);
        int range_tengah = Integer.parseInt(range_mid);
        int range_atas = Integer.parseInt(range_high);
        int bawah_atas = ((range_bawah - 1) * ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) + 1000;
        int tengah_bawah = bawah_atas + ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
        int tengah_atas = tengah_bawah + ((range_tengah - 1) * ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
        int atas_bawah = tengah_atas + ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
        int atas_atas = atas_bawah + ((range_atas - 1) * ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
        this.nilai_mid_high = tengah_atas;
        this.nilai_mid_low = tengah_bawah;
        this.low_mid.setSelection((this.nilai_mid_low - 1500) / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
        this.high_mid.setSelection((this.nilai_mid_high - 1500) / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
        this.et_high_low = (TextView) findViewById(R.id.et_high_low);
        this.et_high_low.setText(String.valueOf(bawah_atas));
        this.et_low_high = (TextView) findViewById(R.id.et_low_high);
        this.et_low_high.setText(String.valueOf(atas_bawah));
        this.et_high_high = (TextView) findViewById(R.id.et_high_high);
        this.et_high_high.setText(String.valueOf(atas_atas));
        for (int i = 10; i <= 61; i++) {
            ((EditText) findViewById(i)).setBackgroundColor(Color.argb(255, 163, 163, 163));
        }
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(this.sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    e_map.this.connect(perangkat);
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
                        if (this.cek == 0) {
                            if (b == 59 || b == 10) {
                                byte[] encodedBytes = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                this.data = new String(encodedBytes, "US-ASCII");
                                this.data = this.data.replaceAll("\n", "");
                                this.data = this.data.replaceAll("\r", "");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (e_map.this.data.equals("1A00")) {
                                            if (e_map.this.hitung_tps < 21) {
                                                e_map.this.kirim(e_map.this.posisi_memory);
                                                return;
                                            }
                                            e_map.this.hitung_tps = 0;
                                            e_map.this.cek = 1;
                                            e_map.this.kirim_emap_range();
                                        } else if (e_map.this.keadaan < 2) {
                                            Message message23 = Message.obtain();
                                            message23.obj = "failed to send";
                                            message23.setTarget(e_map.this.mHandler);
                                            message23.sendToTarget();
                                            e_map.this.keadaan++;
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
                                this.data = new String(encodedBytes2, "US-ASCII");
                                this.data = this.data.replaceAll("\n", "");
                                this.data = this.data.replaceAll("\r", "");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (e_map.this.data.equals("1A00")) {
                                            e_map.this.cek = 2;
                                            e_map.this.kirim_jss();
                                        } else if (e_map.this.keadaan < 2) {
                                            Message message23 = Message.obtain();
                                            message23.obj = "failed to send";
                                            message23.setTarget(e_map.this.mHandler);
                                            message23.sendToTarget();
                                            e_map.this.keadaan++;
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
                                this.data = new String(encodedBytes3, "US-ASCII");
                                this.data = this.data.replaceAll("\n", "");
                                this.data = this.data.replaceAll("\r", "");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (e_map.this.data.equals("1A00")) {
                                            Message message23 = Message.obtain();
                                            message23.obj = "data saved";
                                            message23.setTarget(e_map.this.mHandler);
                                            message23.sendToTarget();
                                            e_map.this.progressDialog.dismiss();
                                        } else if (e_map.this.keadaan < 2) {
                                            Message message232 = Message.obtain();
                                            message232.obj = "failed to send";
                                            message232.setTarget(e_map.this.mHandler);
                                            message232.sendToTarget();
                                            e_map.this.keadaan++;
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

    class MyBroadCastReceiver extends BroadcastReceiver {
        MyBroadCastReceiver() {
        }

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r9, android.content.Intent r10) {
            /*
                r8 = this;
                r7 = 2
                r3 = 0
                r5 = 1
                java.lang.String r4 = "key"
                java.lang.String r0 = r10.getStringExtra(r4)     // Catch:{ Exception -> 0x0041 }
                r4 = -1
                int r6 = r0.hashCode()     // Catch:{ Exception -> 0x0041 }
                switch(r6) {
                    case -375011075: goto L_0x0016;
                    case 108388975: goto L_0x001f;
                    default: goto L_0x0011;
                }     // Catch:{ Exception -> 0x0041 }
            L_0x0011:
                r3 = r4
            L_0x0012:
                switch(r3) {
                    case 0: goto L_0x0029;
                    case 1: goto L_0x008c;
                    default: goto L_0x0015;
                }     // Catch:{ Exception -> 0x0041 }
            L_0x0015:
                return
            L_0x0016:
                java.lang.String r6 = "dataSaved"
                boolean r6 = r0.equals(r6)     // Catch:{ Exception -> 0x0041 }
                if (r6 == 0) goto L_0x0011
                goto L_0x0012
            L_0x001f:
                java.lang.String r3 = "recon"
                boolean r3 = r0.equals(r3)     // Catch:{ Exception -> 0x0041 }
                if (r3 == 0) goto L_0x0011
                r3 = r5
                goto L_0x0012
            L_0x0029:
                juken.android.com.juken_5.e_map r3 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                int r3 = r3.cek     // Catch:{ Exception -> 0x0041 }
                if (r3 != 0) goto L_0x0056
                juken.android.com.juken_5.e_map r3 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                int r3 = r3.hitung_tps     // Catch:{ Exception -> 0x0041 }
                r4 = 21
                if (r3 >= r4) goto L_0x0046
                juken.android.com.juken_5.e_map r3 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                juken.android.com.juken_5.e_map r4 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                java.lang.String r4 = r4.posisi_memory     // Catch:{ Exception -> 0x0041 }
                r3.kirim(r4)     // Catch:{ Exception -> 0x0041 }
                goto L_0x0015
            L_0x0041:
                r1 = move-exception
                r1.printStackTrace()
                goto L_0x0015
            L_0x0046:
                juken.android.com.juken_5.e_map r3 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                r4 = 0
                r3.hitung_tps = r4     // Catch:{ Exception -> 0x0041 }
                juken.android.com.juken_5.e_map r3 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                r4 = 1
                r3.cek = r4     // Catch:{ Exception -> 0x0041 }
                juken.android.com.juken_5.e_map r3 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                r3.kirim_emap_range()     // Catch:{ Exception -> 0x0041 }
                goto L_0x0015
            L_0x0056:
                juken.android.com.juken_5.e_map r3 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                int r3 = r3.cek     // Catch:{ Exception -> 0x0041 }
                if (r3 != r5) goto L_0x0067
                juken.android.com.juken_5.e_map r3 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                r4 = 2
                r3.cek = r4     // Catch:{ Exception -> 0x0041 }
                juken.android.com.juken_5.e_map r3 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                r3.kirim_jss()     // Catch:{ Exception -> 0x0041 }
                goto L_0x0015
            L_0x0067:
                juken.android.com.juken_5.e_map r3 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                int r3 = r3.cek     // Catch:{ Exception -> 0x0041 }
                if (r3 != r7) goto L_0x0015
                android.os.Message r2 = android.os.Message.obtain()     // Catch:{ Exception -> 0x0041 }
                java.lang.String r3 = "data saved"
                r2.obj = r3     // Catch:{ Exception -> 0x0041 }
                juken.android.com.juken_5.e_map r3 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                android.os.Handler r3 = r3.mHandler     // Catch:{ Exception -> 0x0041 }
                r2.setTarget(r3)     // Catch:{ Exception -> 0x0041 }
                r2.sendToTarget()     // Catch:{ Exception -> 0x0041 }
                juken.android.com.juken_5.e_map r3 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                android.app.ProgressDialog r3 = r3.progressDialog     // Catch:{ Exception -> 0x0041 }
                r3.dismiss()     // Catch:{ Exception -> 0x0041 }
                r3 = 1
                juken.android.com.juken_5.StaticClass.bolehPing = r3     // Catch:{ Exception -> 0x0041 }
                goto L_0x0015
            L_0x008c:
                juken.android.com.juken_5.e_map r3 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                juken.android.com.juken_5.e_map r4 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                juken.android.com.juken_5.e_map$MyBroadCastReceiver r4 = r4.myBroadCastReceiver     // Catch:{ Exception -> 0x0041 }
                r3.unregisterReceiver(r4)     // Catch:{ Exception -> 0x0041 }
                juken.android.com.juken_5.e_map r3 = juken.android.com.juken_5.e_map.this     // Catch:{ Exception -> 0x0041 }
                r3.finish()     // Catch:{ Exception -> 0x0041 }
                goto L_0x0015
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.e_map.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
