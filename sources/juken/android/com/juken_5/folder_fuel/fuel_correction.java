package juken.android.com.juken_5.folder_fuel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import juken.android.com.juken_5.MappingHandle;
import juken.android.com.juken_5.R;
import juken.android.com.juken_5.StaticClass;
import juken.android.com.juken_5.WifiService;
import juken.android.com.juken_5.folder_afr.afr_display;
import juken.android.com.juken_5.main_menu;
import juken.android.com.juken_5.range_int_min;

public class fuel_correction extends AppCompatActivity {
    private static String[] PERMISSION_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    Boolean Koneksi = false;
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            fuel_correction.this.listen();
        }
    };
    int alamat = 1;
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    int cek = 0;
    String compare = "";
    int core_dipilih = 0;
    String data = "";
    int hitung_tps = 0;
    private InputStream inputStream = null;
    final CharSequence[] items = {"F Core 1", "F Core 2"};
    int jumlah = 0;
    int keadaan = 0;
    Boolean kondisi_history = true;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            Toast.makeText(fuel_correction.this.getApplicationContext(), (String) message.obj, 1).show();
        }
    };
    int maksimum = 0;
    MyBroadCastReceiver myBroadCastReceiver;
    int nyambung = 1;
    private OutputStream outputStream = null;
    int pengingat = 1;
    String posisi_memory = "1";
    ProgressDialog progressDialog;
    byte[] readBuffer;
    int readBufferPosition;
    Boolean sendMapFuel = false;
    Intent service;
    SharedPreferences sharedPreferences;
    private BluetoothSocket socket = null;
    String srt = "";
    int status_flag = 0;
    volatile boolean stopWorker;
    /* access modifiers changed from: private */
    public Toolbar toolbar;
    int tps_terima = 0;
    int value = 1;
    Thread workerThread;

    public static void verify(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, PERMISSION_STORAGE, 1);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuel_correction);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
            this.Koneksi = StaticClass.koneksi;
        } else if (this.bluetooth.isEnabled()) {
            this.Koneksi = 1;
        } else {
            this.Koneksi = null;
        }
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        TextView text = (TextView) findViewById(1680);
        final TextView textView = text;
        text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text1 = (TextView) findViewById(1681);
        final TextView textView2 = text1;
        text1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView2.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text2 = (TextView) findViewById(1682);
        final TextView textView3 = text2;
        text2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView3.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text3 = (TextView) findViewById(1683);
        final TextView textView4 = text3;
        text3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView4.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text4 = (TextView) findViewById(1684);
        final TextView textView5 = text4;
        text4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView5.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text5 = (TextView) findViewById(1685);
        final TextView textView6 = text5;
        text5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView6.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text6 = (TextView) findViewById(1686);
        final TextView textView7 = text6;
        text6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView7.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text7 = (TextView) findViewById(1687);
        final TextView textView8 = text7;
        text7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView8.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text8 = (TextView) findViewById(1688);
        final TextView textView9 = text8;
        text8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView9.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text9 = (TextView) findViewById(1689);
        final TextView textView10 = text9;
        text9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView10.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text10 = (TextView) findViewById(1690);
        final TextView textView11 = text10;
        text10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView11.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text11 = (TextView) findViewById(1691);
        final TextView textView12 = text11;
        text11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView12.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text12 = (TextView) findViewById(1692);
        final TextView textView13 = text12;
        text12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView13.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text13 = (TextView) findViewById(1693);
        final TextView textView14 = text13;
        text13.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView14.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text14 = (TextView) findViewById(1694);
        final TextView textView15 = text14;
        text14.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView15.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text15 = (TextView) findViewById(1695);
        final TextView textView16 = text15;
        text15.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView16.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text16 = (TextView) findViewById(1696);
        final TextView textView17 = text16;
        text16.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView17.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text17 = (TextView) findViewById(1697);
        final TextView textView18 = text17;
        text17.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView18.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text18 = (TextView) findViewById(1698);
        final TextView textView19 = text18;
        text18.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView19.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text19 = (TextView) findViewById(1699);
        final TextView textView20 = text19;
        text19.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView20.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        TextView text20 = (TextView) findViewById(1700);
        final TextView textView21 = text20;
        text20.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_fuel.class);
                i1.putExtra("posisi", textView21.getText().toString());
                fuel_correction.this.startActivityForResult(i1, 0);
            }
        });
        getNilai();
        this.value = 1;
        while (this.value <= 1281) {
            final EditText berhasil = (EditText) findViewById(this.value);
            berhasil.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    fuel_correction.this.compare = berhasil.getText().toString();
                }

                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                public void afterTextChanged(Editable editable) {
                    if (!fuel_correction.this.compare.equals(editable.toString())) {
                        StaticClass.saveFuel = true;
                    }
                    if (fuel_correction.this.value >= 1220) {
                        fuel_correction.this.SavePreferences(main_menu.calculate_pertama, "0");
                    }
                }
            });
            this.value++;
        }
        ((ImageButton) findViewById(R.id.send_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fuel_correction.this.core_dipilih = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(fuel_correction.this);
                builder.setTitle((CharSequence) "Send Map");
                builder.setSingleChoiceItems(fuel_correction.this.items, 0, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        fuel_correction.this.core_dipilih = item;
                    }
                });
                builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(fuel_correction.this);
                        alert.setTitle((CharSequence) "Alert");
                        alert.setMessage((CharSequence) fuel_correction.this.getString(R.string.TanyaSendAllMap));
                        alert.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                fuel_correction.this.sendMapFuel = true;
                                if (fuel_correction.this.Koneksi.booleanValue()) {
                                    fuel_correction.this.keadaan = 0;
                                    if (fuel_correction.this.hitung_tps > 0) {
                                        fuel_correction.this.hitung_tps = 0;
                                    }
                                    if (fuel_correction.this.core_dipilih == 0) {
                                        MappingHandle.NamaFileFuel = "Fuel Correction - ECU Core 1";
                                        fuel_correction.this.toolbar.setTitle((CharSequence) MappingHandle.NamaFileFuel);
                                        fuel_correction.this.posisi_memory = fuel_correction.this.sharedPreferences.getString("f_c1", "f_c11");
                                    } else {
                                        MappingHandle.NamaFileFuel = "Fuel Correction - ECU Core 2";
                                        fuel_correction.this.toolbar.setTitle((CharSequence) MappingHandle.NamaFileFuel);
                                        fuel_correction.this.posisi_memory = fuel_correction.this.sharedPreferences.getString("f_c2", "f_c21");
                                    }
                                    fuel_correction.this.kirim(fuel_correction.this.posisi_memory);
                                }
                            }
                        });
                        alert.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fuel_correction.this.sendMapFuel = false;
                                if (fuel_correction.this.Koneksi.booleanValue()) {
                                    fuel_correction.this.keadaan = 0;
                                    if (fuel_correction.this.hitung_tps > 0) {
                                        fuel_correction.this.hitung_tps = 0;
                                    }
                                    if (fuel_correction.this.core_dipilih == 0) {
                                        MappingHandle.NamaFileFuel = "Fuel Correction - ECU Core 1";
                                        fuel_correction.this.toolbar.setTitle((CharSequence) MappingHandle.NamaFileFuel);
                                        fuel_correction.this.posisi_memory = fuel_correction.this.sharedPreferences.getString("f_c1", "f_c11");
                                    } else {
                                        MappingHandle.NamaFileFuel = "Fuel Correction - ECU Core 2";
                                        fuel_correction.this.toolbar.setTitle((CharSequence) MappingHandle.NamaFileFuel);
                                        fuel_correction.this.posisi_memory = fuel_correction.this.sharedPreferences.getString("f_c2", "f_c21");
                                    }
                                    fuel_correction.this.kirim(fuel_correction.this.posisi_memory);
                                }
                            }
                        });
                        alert.create().show();
                    }
                });
                builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
        ((ImageButton) findViewById(R.id.get_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fuel_correction.this.core_dipilih = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(fuel_correction.this);
                builder.setTitle((CharSequence) "Get Map");
                builder.setSingleChoiceItems(fuel_correction.this.items, 0, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        fuel_correction.this.core_dipilih = item;
                    }
                });
                builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(fuel_correction.this);
                        alert.setTitle((CharSequence) "Alert");
                        alert.setMessage((CharSequence) fuel_correction.this.getString(R.string.TanyaAllMap));
                        alert.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                StaticClass.fromMenu = true;
                                fuel_correction.this.sendMapFuel = true;
                                if (fuel_correction.this.Koneksi.booleanValue()) {
                                    fuel_correction.this.keadaan = 0;
                                    MappingHandle.list_fuel.clear();
                                    if (fuel_correction.this.tps_terima > 0) {
                                        fuel_correction.this.tps_terima = 0;
                                    }
                                    if (fuel_correction.this.core_dipilih == 0) {
                                        MappingHandle.NamaFileFuel = "Fuel Correction - ECU Core 1";
                                        fuel_correction.this.toolbar.setTitle((CharSequence) MappingHandle.NamaFileFuel);
                                        fuel_correction.this.posisi_memory = fuel_correction.this.sharedPreferences.getString("f_c1", "f_c11");
                                    } else {
                                        MappingHandle.NamaFileFuel = "Fuel Correction - ECU Core 2";
                                        fuel_correction.this.toolbar.setTitle((CharSequence) MappingHandle.NamaFileFuel);
                                        fuel_correction.this.posisi_memory = fuel_correction.this.sharedPreferences.getString("f_c2", "f_c21");
                                    }
                                    fuel_correction.this.terima(fuel_correction.this.posisi_memory);
                                }
                            }
                        });
                        alert.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                StaticClass.fromMenu = true;
                                fuel_correction.this.sendMapFuel = false;
                                if (fuel_correction.this.Koneksi.booleanValue()) {
                                    fuel_correction.this.keadaan = 0;
                                    MappingHandle.list_fuel.clear();
                                    if (fuel_correction.this.tps_terima > 0) {
                                        fuel_correction.this.tps_terima = 0;
                                    }
                                    if (fuel_correction.this.core_dipilih == 0) {
                                        MappingHandle.NamaFileFuel = "Fuel Correction - ECU Core 1";
                                        fuel_correction.this.toolbar.setTitle((CharSequence) MappingHandle.NamaFileFuel);
                                        fuel_correction.this.posisi_memory = fuel_correction.this.sharedPreferences.getString("f_c1", "f_c11");
                                    } else {
                                        MappingHandle.NamaFileFuel = "Fuel Correction - ECU Core 2";
                                        fuel_correction.this.toolbar.setTitle((CharSequence) MappingHandle.NamaFileFuel);
                                        fuel_correction.this.posisi_memory = fuel_correction.this.sharedPreferences.getString("f_c2", "f_c21");
                                    }
                                    fuel_correction.this.terima(fuel_correction.this.posisi_memory);
                                }
                            }
                        });
                        alert.create().show();
                    }
                });
                builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
        ((ImageButton) findViewById(R.id.open_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (StaticClass.TipeKoneksi.equals("bt")) {
                        fuel_correction.this.resetConnection();
                    }
                    fuel_correction.this.finish();
                    fuel_correction.this.startActivityForResult(new Intent(fuel_correction.this, ImportData.class), 100);
                } catch (Exception x) {
                    System.out.println(x.getMessage());
                }
            }
        });
        ((ImageButton) findViewById(R.id.save_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(fuel_correction.this);
                alert1.setTitle((CharSequence) "Save File");
                alert1.setMessage((CharSequence) "Enter Your File Name Here");
                final EditText input = new EditText(fuel_correction.this);
                input.setImeOptions(268435456);
                alert1.setView((View) input);
                alert1.setPositiveButton((CharSequence) "SAVE", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        fuel_correction.this.save_data_ke_list();
                        fuel_correction.this.srt = input.getEditableText().toString();
                        fuel_correction.this.writeToFile(fuel_correction.this.srt);
                    }
                });
                alert1.setNegativeButton((CharSequence) "CANCEL", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                alert1.create().show();
            }
        });
        ((ImageButton) findViewById(R.id.set_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fuel_correction.this.set_value();
            }
        });
        ((ImageButton) findViewById(R.id.add_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fuel_correction.this.add_value();
            }
        });
        ((ImageButton) findViewById(R.id.copy_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fuel_correction.this.Copy();
            }
        });
        ((ImageButton) findViewById(R.id.paste_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fuel_correction.this.Paste();
            }
        });
        ((ImageButton) findViewById(R.id.select_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fuel_correction.this.select();
            }
        });
        ((ImageButton) findViewById(R.id.plus_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fuel_correction.this.plus_minus("plus");
            }
        });
        ((ImageButton) findViewById(R.id.minus_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fuel_correction.this.plus_minus("minus");
            }
        });
        ((ImageButton) findViewById(R.id.save_as_bm)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int i = 1;
                while (i <= 1281) {
                    try {
                        EditText view = (EditText) fuel_correction.this.findViewById(i);
                        double pengali = Double.valueOf(view.getText().toString()).doubleValue();
                        double nilai_bm = Double.valueOf(MappingHandle.list_base_map.get(i - 1)).doubleValue();
                        double hasil_belum_pembulatan = nilai_bm + ((pengali * nilai_bm) / 100.0d);
                        if (hasil_belum_pembulatan > 20.0d) {
                            hasil_belum_pembulatan = 20.0d;
                        } else if (hasil_belum_pembulatan < 0.0d) {
                            hasil_belum_pembulatan = 0.0d;
                        }
                        MappingHandle.list_base_map.set(i - 1, String.format("%.2f", new Object[]{Double.valueOf(hasil_belum_pembulatan)}).replace(",", "."));
                        view.setText("0");
                        i++;
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        });
        ((ImageButton) findViewById(R.id.afr)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fuel_correction.this.SavePreferences("back_pressed_mapping", "0");
                fuel_correction.this.save_data_ke_list();
                if (StaticClass.TipeKoneksi.equals("bt")) {
                    fuel_correction.this.resetConnection();
                }
                fuel_correction.this.finish();
                fuel_correction.this.startActivityForResult(new Intent(v.getContext(), afr_display.class), 0);
            }
        });
        final ImageButton his_afr = (ImageButton) findViewById(R.id.his_afr);
        his_afr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fuel_correction.this.kondisi_history.booleanValue()) {
                    fuel_correction.this.kondisi_history = false;
                    if (Build.VERSION.SDK_INT >= 21) {
                        his_afr.setImageDrawable(fuel_correction.this.getResources().getDrawable(R.drawable.his_afr, fuel_correction.this.getApplicationContext().getTheme()));
                    } else {
                        his_afr.setImageDrawable(fuel_correction.this.getResources().getDrawable(R.drawable.his_afr));
                    }
                    int i = 1;
                    while (i <= 1281) {
                        EditText edit = (EditText) fuel_correction.this.findViewById(i);
                        try {
                            double value = Double.valueOf(MappingHandle.list_history.get(i - 1)).doubleValue() * 10.0d;
                            if (value > 0.0d && value < 120.0d) {
                                edit.setBackgroundColor(Color.argb(255, 15, 11, 239));
                                i++;
                            } else if (value >= 120.0d && value < 130.0d) {
                                edit.setBackgroundColor(Color.argb(255, 95, 239, 11));
                                i++;
                            } else if (value < 130.0d || value >= 140.0d) {
                                if (value >= 140.0d) {
                                    edit.setBackgroundColor(Color.argb(255, 239, 11, 22));
                                }
                                i++;
                            } else {
                                edit.setBackgroundColor(Color.argb(255, 239, 235, 11));
                                i++;
                            }
                        } catch (Exception e) {
                        }
                    }
                    return;
                }
                fuel_correction.this.kondisi_history = true;
                if (Build.VERSION.SDK_INT >= 21) {
                    his_afr.setImageDrawable(fuel_correction.this.getResources().getDrawable(R.drawable.his_history, fuel_correction.this.getApplicationContext().getTheme()));
                } else {
                    his_afr.setImageDrawable(fuel_correction.this.getResources().getDrawable(R.drawable.his_history));
                }
                for (int i2 = 1; i2 <= 1281; i2++) {
                    EditText edit2 = (EditText) fuel_correction.this.findViewById(i2);
                    try {
                        if (Double.valueOf(MappingHandle.list_history.get(i2 - 1)).doubleValue() * 10.0d != 0.0d) {
                            edit2.setBackgroundColor(Color.argb(255, 255, 136, 215));
                        }
                    } catch (Exception e2) {
                    }
                }
            }
        });
        ((ImageButton) findViewById(R.id.interpolation)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fuel_correction.this.interpolasi();
            }
        });
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.toolbar.setTitle((CharSequence) MappingHandle.NamaFileFuel);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LoadPreferences();
    }

    private void getNilai() {
        cek_nilai();
        if (MappingHandle.list_fuel.size() < 2) {
            for (int i = 1; i <= 1281; i++) {
                EditText v = (EditText) findViewById(i);
                v.setText("0");
                try {
                    if (Double.valueOf(MappingHandle.list_history.get(i - 1)).doubleValue() * 10.0d != 0.0d) {
                        v.setBackgroundColor(Color.argb(255, 255, 136, 215));
                    }
                } catch (Exception e) {
                }
            }
        } else {
            int posisi = 1;
            for (int i2 = 1; i2 <= 1281; i2++) {
                EditText v2 = (EditText) findViewById(posisi);
                posisi++;
                try {
                    v2.setText(String.valueOf(MappingHandle.list_fuel.get(i2 - 1)));
                } catch (IndexOutOfBoundsException e2) {
                    v2.setText("0");
                }
                try {
                    if (Double.valueOf(MappingHandle.list_history.get(i2 - 1)).doubleValue() * 10.0d != 0.0d) {
                        v2.setBackgroundColor(Color.argb(255, 255, 136, 215));
                    }
                } catch (Exception e3) {
                }
            }
        }
        for (int i3 = 1; i3 <= MappingHandle.list_fuel_ganti.size(); i3++) {
            EditText views = (EditText) findViewById(i3);
            if (MappingHandle.list_fuel_ganti.get(i3 - 1).equals("1")) {
                views.setTextColor(Color.argb(255, 240, 81, 0));
            }
        }
    }

    private void cek_nilai() {
        if (MappingHandle.list_fuel.size() < 2) {
            for (int i = 0; i < 1281; i++) {
                if (i / 61 == 0) {
                    MappingHandle.list_fuel.add("68");
                } else {
                    MappingHandle.list_fuel.add("5");
                }
            }
        }
        if (MappingHandle.list_history.size() < 2) {
            for (int i2 = 0; i2 < 1281; i2++) {
                MappingHandle.list_history.add("255");
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            resetConnection();
        } else if (StaticClass.TipeKoneksi.equals("wifi")) {
            try {
                unregisterReceiver(this.myBroadCastReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        StaticClass.position.clear();
        save_data_ke_list();
    }

    /* access modifiers changed from: private */
    public void save_data_ke_list() {
        MappingHandle.list_fuel.clear();
        for (int i = 1; i <= 1281; i++) {
            MappingHandle.list_fuel.add(((EditText) findViewById(i)).getText().toString());
        }
    }

    private void LoadPreferences() {
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            String data2 = this.sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame");
            this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(data2);
            new Thread() {
                public void run() {
                    fuel_correction.this.connect(perangkat);
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

    /* access modifiers changed from: protected */
    public void listen() {
        this.stopWorker = false;
        this.readBufferPosition = 0;
        this.readBuffer = new byte[1024];
        while (!this._serverWorker.isInterrupted() && !this.stopWorker && this.nyambung == 1) {
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
                                this.data = new String(encodedBytes, "US-ASCII");
                                this.data = this.data.replaceAll("\n", "");
                                this.data = this.data.replaceAll("\r", "");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (fuel_correction.this.data.equals("1A00")) {
                                            if (fuel_correction.this.hitung_tps < 21) {
                                                fuel_correction.this.kirim(fuel_correction.this.posisi_memory);
                                            } else if (fuel_correction.this.sendMapFuel.booleanValue()) {
                                                Message message23 = Message.obtain();
                                                message23.obj = "Send Base Map";
                                                message23.setTarget(fuel_correction.this.mHandler);
                                                message23.sendToTarget();
                                                if (fuel_correction.this.core_dipilih == 0) {
                                                    MappingHandle.NamaFileBM = "Base Map - ECU Core 1";
                                                } else {
                                                    MappingHandle.NamaFileBM = "Base Map - ECU Core 2";
                                                }
                                                fuel_correction.this.hitung_tps = 0;
                                                fuel_correction.this.kirimBaseMap(fuel_correction.this.posisi_memory);
                                            } else {
                                                StaticClass.saveFuel = false;
                                                Message message232 = Message.obtain();
                                                message232.obj = "Send Done";
                                                message232.setTarget(fuel_correction.this.mHandler);
                                                message232.sendToTarget();
                                                fuel_correction.this.progressDialog.dismiss();
                                            }
                                        } else if (fuel_correction.this.keadaan < 2) {
                                            Message message233 = Message.obtain();
                                            message233.obj = "failed to send";
                                            message233.setTarget(fuel_correction.this.mHandler);
                                            message233.sendToTarget();
                                            fuel_correction.this.keadaan++;
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
                                final String data2 = new String(encodedBytes2, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (!data2.equals("9602") && !data2.equals("\r")) {
                                            EditText edit = (EditText) fuel_correction.this.findViewById(fuel_correction.this.alamat);
                                            edit.setText(data2.trim());
                                            MappingHandle.list_fuel.add(data2.trim());
                                            if (fuel_correction.this.mod(fuel_correction.this.alamat, 61) == 0) {
                                                edit.setText(data2.replaceAll("\\r", ""));
                                                if (fuel_correction.this.tps_terima < 21) {
                                                    fuel_correction.this.terima(fuel_correction.this.posisi_memory);
                                                } else if (fuel_correction.this.sendMapFuel.booleanValue()) {
                                                    if (fuel_correction.this.core_dipilih == 0) {
                                                        MappingHandle.NamaFileBM = "Base Map - ECU Core 1";
                                                    } else {
                                                        MappingHandle.NamaFileBM = "Base Map - ECU Core 2";
                                                    }
                                                    Message message23 = Message.obtain();
                                                    message23.obj = "Get Base Map";
                                                    message23.setTarget(fuel_correction.this.mHandler);
                                                    message23.sendToTarget();
                                                    fuel_correction.this.tps_terima = 0;
                                                    fuel_correction.this.alamat = 0;
                                                    MappingHandle.list_base_map.clear();
                                                    fuel_correction.this.terimaBaseMap(fuel_correction.this.posisi_memory);
                                                } else {
                                                    fuel_correction.this.CurrentPatternFile();
                                                    StaticClass.saveFuel = false;
                                                    Message message232 = Message.obtain();
                                                    message232.obj = "Get Done";
                                                    message232.setTarget(fuel_correction.this.mHandler);
                                                    message232.sendToTarget();
                                                    fuel_correction.this.progressDialog.dismiss();
                                                }
                                            }
                                            fuel_correction.this.alamat++;
                                            if (fuel_correction.this.alamat > 1281) {
                                                fuel_correction.this.alamat = 1;
                                            }
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
                            if (b == 10 || b == 59) {
                                byte[] encodedBytes3 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes3, 0, encodedBytes3.length);
                                this.data = new String(encodedBytes3, "US-ASCII");
                                this.data = this.data.replaceAll("\n", "");
                                this.data = this.data.replaceAll("\r", "");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (fuel_correction.this.data.equals("1A00")) {
                                            if (fuel_correction.this.hitung_tps < 21) {
                                                fuel_correction.this.kirimBaseMap(fuel_correction.this.posisi_memory);
                                                return;
                                            }
                                            Message message23 = Message.obtain();
                                            message23.obj = "Send Injector Timing";
                                            message23.setTarget(fuel_correction.this.mHandler);
                                            message23.sendToTarget();
                                            if (fuel_correction.this.core_dipilih == 0) {
                                                MappingHandle.NamaFileIT = "Injector Timing - ECU Core 1";
                                            } else {
                                                MappingHandle.NamaFileIT = "Injector Timing - ECU Core 2";
                                            }
                                            fuel_correction.this.hitung_tps = 0;
                                            fuel_correction.this.kirimInjectorTiming(fuel_correction.this.posisi_memory);
                                        } else if (fuel_correction.this.keadaan < 2) {
                                            Message message232 = Message.obtain();
                                            message232.obj = "failed to send";
                                            message232.setTarget(fuel_correction.this.mHandler);
                                            message232.sendToTarget();
                                            fuel_correction.this.keadaan++;
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr3 = this.readBuffer;
                                int i4 = this.readBufferPosition;
                                this.readBufferPosition = i4 + 1;
                                bArr3[i4] = b;
                            }
                        } else if (this.cek == 3) {
                            if (b == 10 || b == 59) {
                                byte[] encodedBytes4 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes4, 0, encodedBytes4.length);
                                this.data = new String(encodedBytes4, "US-ASCII");
                                this.data = this.data.replaceAll("\n", "");
                                this.data = this.data.replaceAll("\r", "");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (fuel_correction.this.data.equals("1A00")) {
                                            if (fuel_correction.this.hitung_tps < 21) {
                                                fuel_correction.this.kirimInjectorTiming(fuel_correction.this.posisi_memory);
                                                return;
                                            }
                                            Message message23 = Message.obtain();
                                            message23.obj = "Send Ignition Timing";
                                            message23.setTarget(fuel_correction.this.mHandler);
                                            message23.sendToTarget();
                                            if (fuel_correction.this.core_dipilih == 0) {
                                                MappingHandle.NamaFileIG = "Ignition Timing - ECU Core 1";
                                            } else {
                                                MappingHandle.NamaFileIG = "Ignition Timing - ECU Core 2";
                                            }
                                            fuel_correction.this.hitung_tps = 0;
                                            fuel_correction.this.kirimIgnitionTiming(fuel_correction.this.posisi_memory);
                                        } else if (fuel_correction.this.keadaan < 2) {
                                            Message message232 = Message.obtain();
                                            message232.obj = "failed to send";
                                            message232.setTarget(fuel_correction.this.mHandler);
                                            message232.sendToTarget();
                                            fuel_correction.this.keadaan++;
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr4 = this.readBuffer;
                                int i5 = this.readBufferPosition;
                                this.readBufferPosition = i5 + 1;
                                bArr4[i5] = b;
                            }
                        } else if (this.cek == 4) {
                            if (b == 10 || b == 59) {
                                byte[] encodedBytes5 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes5, 0, encodedBytes5.length);
                                this.data = new String(encodedBytes5, "US-ASCII");
                                this.data = this.data.replaceAll("\n", "");
                                this.data = this.data.replaceAll("\r", "");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (fuel_correction.this.data.equals("1A00")) {
                                            if (fuel_correction.this.hitung_tps < 21) {
                                                fuel_correction.this.kirimIgnitionTiming(fuel_correction.this.posisi_memory);
                                                return;
                                            }
                                            StaticClass.saveFuel = false;
                                            StaticClass.saveBM = false;
                                            StaticClass.saveIT = false;
                                            StaticClass.saveIG = false;
                                            Message message23 = Message.obtain();
                                            message23.obj = "Send Done";
                                            message23.setTarget(fuel_correction.this.mHandler);
                                            message23.sendToTarget();
                                            fuel_correction.this.progressDialog.dismiss();
                                        } else if (fuel_correction.this.keadaan < 2) {
                                            Message message232 = Message.obtain();
                                            message232.obj = "failed to send";
                                            message232.setTarget(fuel_correction.this.mHandler);
                                            message232.sendToTarget();
                                            fuel_correction.this.keadaan++;
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr5 = this.readBuffer;
                                int i6 = this.readBufferPosition;
                                this.readBufferPosition = i6 + 1;
                                bArr5[i6] = b;
                            }
                        } else if (this.cek == 5) {
                            if (b == 59 || b == 10) {
                                byte[] encodedBytes6 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes6, 0, encodedBytes6.length);
                                final String data3 = new String(encodedBytes6, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (!data3.equals("9601") && !data3.equals("\r")) {
                                            MappingHandle.list_base_map.add(data3.trim());
                                            if (fuel_correction.this.mod(fuel_correction.this.alamat, 61) == 0) {
                                                if (fuel_correction.this.tps_terima < 21) {
                                                    fuel_correction.this.terimaBaseMap(fuel_correction.this.posisi_memory);
                                                } else {
                                                    fuel_correction.this.CurrentPatternFile();
                                                    if (fuel_correction.this.core_dipilih == 0) {
                                                        MappingHandle.NamaFileIT = "Injector Timing - ECU Core 1";
                                                    } else {
                                                        MappingHandle.NamaFileIT = "Injector Timing - ECU Core 2";
                                                    }
                                                    Message message23 = Message.obtain();
                                                    message23.obj = "Get Injector Timing";
                                                    message23.setTarget(fuel_correction.this.mHandler);
                                                    message23.sendToTarget();
                                                    fuel_correction.this.tps_terima = 0;
                                                    fuel_correction.this.alamat = 0;
                                                    fuel_correction.this.terimaInjectorTiming(fuel_correction.this.posisi_memory);
                                                }
                                            }
                                            fuel_correction.this.alamat++;
                                            if (fuel_correction.this.alamat > 1281) {
                                                fuel_correction.this.alamat = 1;
                                            }
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr6 = this.readBuffer;
                                int i7 = this.readBufferPosition;
                                this.readBufferPosition = i7 + 1;
                                bArr6[i7] = b;
                            }
                        } else if (this.cek == 6) {
                            if (b == 59 || b == 10) {
                                byte[] encodedBytes7 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes7, 0, encodedBytes7.length);
                                final String data4 = new String(encodedBytes7, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (!data4.equals("9603") && !data4.equals("\r")) {
                                            MappingHandle.list_injector.add(data4.trim());
                                            if (fuel_correction.this.mod(fuel_correction.this.alamat, 61) == 0) {
                                                if (fuel_correction.this.tps_terima < 21) {
                                                    fuel_correction.this.terimaInjectorTiming(fuel_correction.this.posisi_memory);
                                                } else {
                                                    if (fuel_correction.this.core_dipilih == 0) {
                                                        MappingHandle.NamaFileIG = "Ignition Timing - ECU Core 1";
                                                    } else {
                                                        MappingHandle.NamaFileIG = "Ignition Timing - ECU Core 2";
                                                    }
                                                    Message message23 = Message.obtain();
                                                    message23.obj = "Get Ignition Timing";
                                                    message23.setTarget(fuel_correction.this.mHandler);
                                                    message23.sendToTarget();
                                                    fuel_correction.this.tps_terima = 0;
                                                    fuel_correction.this.alamat = 0;
                                                    MappingHandle.list_ignition.clear();
                                                    fuel_correction.this.terimaIgnitionTiming(fuel_correction.this.posisi_memory);
                                                }
                                            }
                                            fuel_correction.this.alamat++;
                                            if (fuel_correction.this.alamat > 1281) {
                                                fuel_correction.this.alamat = 1;
                                            }
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr7 = this.readBuffer;
                                int i8 = this.readBufferPosition;
                                this.readBufferPosition = i8 + 1;
                                bArr7[i8] = b;
                            }
                        } else if (this.cek == 7) {
                            if (b == 59 || b == 10) {
                                byte[] encodedBytes8 = new byte[this.readBufferPosition];
                                System.arraycopy(this.readBuffer, 0, encodedBytes8, 0, encodedBytes8.length);
                                final String data5 = new String(encodedBytes8, "US-ASCII");
                                this.readBufferPosition = 0;
                                this._handler.post(new Runnable() {
                                    public void run() {
                                        if (!data5.equals("9605") && !data5.equals("\r")) {
                                            MappingHandle.list_ignition.add(data5.trim());
                                            if (fuel_correction.this.mod(fuel_correction.this.alamat, 31) == 0) {
                                                if (fuel_correction.this.tps_terima < 21) {
                                                    fuel_correction.this.terimaIgnitionTiming(fuel_correction.this.posisi_memory);
                                                } else {
                                                    StaticClass.saveFuel = false;
                                                    StaticClass.saveBM = false;
                                                    StaticClass.saveIT = false;
                                                    StaticClass.saveIG = false;
                                                    Message message23 = Message.obtain();
                                                    message23.obj = "Get Done";
                                                    message23.setTarget(fuel_correction.this.mHandler);
                                                    message23.sendToTarget();
                                                    fuel_correction.this.progressDialog.dismiss();
                                                }
                                            }
                                            fuel_correction.this.alamat++;
                                            if (fuel_correction.this.alamat > 651) {
                                                fuel_correction.this.alamat = 1;
                                            }
                                        }
                                    }
                                });
                            } else {
                                byte[] bArr8 = this.readBuffer;
                                int i9 = this.readBufferPosition;
                                this.readBufferPosition = i9 + 1;
                                bArr8[i9] = b;
                            }
                        } else if (this.keadaan < 2) {
                            Message message23 = Message.obtain();
                            message23.obj = "failed to send";
                            message23.setTarget(this.mHandler);
                            message23.sendToTarget();
                            this.keadaan++;
                        }
                    }
                }
            } catch (IOException e) {
                this.stopWorker = true;
            }
        }
    }

    /* access modifiers changed from: private */
    public void CurrentPatternFile() {
        MappingHandle.list_pattern_current_file.clear();
        ArrayList<String> bmTotSementara = new ArrayList<>();
        for (int i = 0; i < MappingHandle.list_fuel.size(); i++) {
            Double Fuel = Double.valueOf(MappingHandle.list_fuel.get(i));
            Double Bm = Double.valueOf(MappingHandle.list_base_map.get(i));
            Double BmTot = Double.valueOf(Bm.doubleValue() + ((Bm.doubleValue() * Fuel.doubleValue()) / 100.0d));
            if (BmTot.doubleValue() > 20.0d) {
                BmTot = Double.valueOf(20.0d);
            }
            if (BmTot.doubleValue() < 0.0d) {
                BmTot = Double.valueOf(0.0d);
            }
            bmTotSementara.add(String.format("%.2f", new Object[]{BmTot}).replace(",", "."));
        }
        for (int i2 = 19; i2 > 0; i2--) {
            for (int j = 0; j < 61; j++) {
                int posisi1 = j + 1220;
                double seratus = Double.valueOf(bmTotSementara.get(posisi1)).doubleValue();
                MappingHandle.list_pattern_current_file.add(String.format("%.2f", new Object[]{Double.valueOf(((Double.valueOf(bmTotSementara.get(posisi1 - (i2 * 61))).doubleValue() - seratus) * 100.0d) / seratus)}).replace(",", "."));
            }
        }
    }

    public void terima(String posisi) {
        this.cek = 1;
        String ack = "1602;" + posisi + ";" + this.tps_terima + "\r\n";
        byte[] msgAck = ack.getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = ack;
            this.service.putExtra("send", "GlobalData");
            startService(this.service);
        } else if (StaticClass.TipeKoneksi.equals("bt") && this.nyambung == 1) {
            try {
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(msgAck);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.tps_terima == 0) {
            runOnUiThread(new Runnable() {
                public void run() {
                    fuel_correction.this.progressDialog = new ProgressDialog(fuel_correction.this);
                    fuel_correction.this.progressDialog.setMessage("Loading...");
                    fuel_correction.this.progressDialog.setTitle("Get Map");
                    fuel_correction.this.progressDialog.setProgressStyle(0);
                    fuel_correction.this.progressDialog.show();
                    fuel_correction.this.progressDialog.setCancelable(true);
                }
            });
            if (this.sendMapFuel.booleanValue()) {
                Toast.makeText(getApplicationContext(), "Get Fuel Correction", 0).show();
            } else {
                Toast.makeText(getApplicationContext(), "Receiving data", 0).show();
            }
        }
        this.tps_terima++;
    }

    public void terimaBaseMap(String posisi) {
        this.cek = 5;
        String ack = "1601;" + posisi + ";" + this.tps_terima + "\r\n";
        byte[] msgAck = ack.getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = ack;
            this.service.putExtra("send", "GlobalData");
            startService(this.service);
        } else if (StaticClass.TipeKoneksi.equals("bt") && this.nyambung == 1) {
            try {
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(msgAck);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.tps_terima++;
    }

    public void terimaInjectorTiming(String posisi) {
        this.cek = 6;
        String ack = "1603;" + posisi + ";" + this.tps_terima + "\r\n";
        byte[] msgAck = ack.getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = ack;
            this.service.putExtra("send", "GlobalData");
            startService(this.service);
        } else if (StaticClass.TipeKoneksi.equals("bt") && this.nyambung == 1) {
            try {
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(msgAck);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.tps_terima++;
    }

    public void terimaIgnitionTiming(String posisi) {
        this.cek = 7;
        String ack = "1605;" + posisi + ";" + this.tps_terima + "\r\n";
        byte[] msgAck = ack.getBytes();
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            StaticClass.GlobalData = ack;
            this.service.putExtra("send", "GlobalData");
            startService(this.service);
        } else if (StaticClass.TipeKoneksi.equals("bt") && this.nyambung == 1) {
            try {
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(msgAck);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.tps_terima++;
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
                    fuel_correction.this.progressDialog = new ProgressDialog(fuel_correction.this);
                    fuel_correction.this.progressDialog.setMessage("Loading...");
                    fuel_correction.this.progressDialog.setTitle("Send Map");
                    fuel_correction.this.progressDialog.setProgressStyle(0);
                    fuel_correction.this.progressDialog.show();
                    fuel_correction.this.progressDialog.setCancelable(true);
                }
            });
            if (this.sendMapFuel.booleanValue()) {
                Toast.makeText(getApplicationContext(), "Send Fuel Correction", 0).show();
            } else {
                Toast.makeText(getApplicationContext(), "Send data", 0).show();
            }
        }
        this.hitung_tps++;
    }

    /* access modifiers changed from: private */
    public void kirimBaseMap(String posisi) {
        this.cek = 2;
        String ack = "2601;" + posisi + ";" + String.valueOf(this.hitung_tps) + ";";
        String data2 = "";
        for (int i = 0; i < 61; i++) {
            String teks = MappingHandle.list_base_map.get((this.hitung_tps * 61) + i).toString();
            if (i == 60) {
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
        this.hitung_tps++;
    }

    /* access modifiers changed from: private */
    public void kirimInjectorTiming(String posisi) {
        this.cek = 3;
        String ack = "2603;" + posisi + ";" + String.valueOf(this.hitung_tps) + ";";
        String data2 = "";
        for (int i = 0; i < 61; i++) {
            String teks = MappingHandle.list_injector.get((this.hitung_tps * 61) + i).toString();
            if (i == 60) {
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
        this.hitung_tps++;
    }

    /* access modifiers changed from: private */
    public void kirimIgnitionTiming(String posisi) {
        this.cek = 4;
        String ack = "2605;" + posisi + ";" + String.valueOf(this.hitung_tps) + ";";
        String data2 = "";
        for (int i = 0; i < 31; i++) {
            String teks = MappingHandle.list_ignition.get((this.hitung_tps * 31) + i).toString();
            if (i == 30) {
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
        this.hitung_tps++;
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

    /* access modifiers changed from: private */
    public void interpolasi() {
        for (int i = 1; i <= 61; i++) {
            int awal = i + 1220;
            int akhir = i;
            int j = i;
            while (j < i + 1220) {
                try {
                    if (((EditText) findViewById(j)).getTag().equals("Warna CYAN")) {
                        if (j <= awal) {
                            awal = j;
                        }
                        if (j > akhir) {
                            akhir = j;
                        }
                    }
                    j += 61;
                } catch (Exception e) {
                    return;
                }
            }
            if (!(awal == i + 1220 || akhir == i)) {
                EditText edit1 = (EditText) findViewById(awal);
                Double hasil = Double.valueOf(Double.valueOf(Double.valueOf(((EditText) findViewById(akhir)).getText().toString()).doubleValue() - Double.valueOf(edit1.getText().toString()).doubleValue()).doubleValue() / Double.valueOf(((double) (akhir - awal)) / 61.0d).doubleValue());
                Double pengali = Double.valueOf(1.0d);
                Double nilai_awal = Double.valueOf(edit1.getText().toString());
                for (int k = awal + 61; k < akhir; k += 61) {
                    pengali = Double.valueOf(pengali.doubleValue() + 1.0d);
                    ((EditText) findViewById(k)).setText(String.valueOf((int) (nilai_awal.doubleValue() + (pengali.doubleValue() * hasil.doubleValue()))));
                }
            }
        }
        for (int i2 = 1; i2 <= 1281; i2 += 61) {
            int awal2 = i2 + 61;
            int akhir2 = i2;
            for (int j2 = i2; j2 < i2 + 61; j2++) {
                if (((EditText) findViewById(j2)).getTag().equals("Warna CYAN")) {
                    if (j2 <= awal2) {
                        awal2 = j2;
                    }
                    if (j2 > akhir2) {
                        akhir2 = j2;
                    }
                }
            }
            if (!(awal2 == i2 + 61 || akhir2 == i2)) {
                EditText edit12 = (EditText) findViewById(awal2);
                Double hasil2 = Double.valueOf(Double.valueOf(Double.valueOf(((EditText) findViewById(akhir2)).getText().toString()).doubleValue() - Double.valueOf(edit12.getText().toString()).doubleValue()).doubleValue() / Double.valueOf((double) (akhir2 - awal2)).doubleValue());
                Double pengali2 = Double.valueOf(1.0d);
                Double nilai_awal2 = Double.valueOf(edit12.getText().toString());
                for (int k2 = awal2 + 1; k2 < akhir2; k2++) {
                    pengali2 = Double.valueOf(pengali2.doubleValue() + 1.0d);
                    ((EditText) findViewById(k2)).setText(String.valueOf((int) (nilai_awal2.doubleValue() + (pengali2.doubleValue() * hasil2.doubleValue()))));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void add_value() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle((CharSequence) "Add Value");
        alert.setMessage((CharSequence) "Enter Your Value Here");
        final EditText inputNilaiAdd = new EditText(this);
        inputNilaiAdd.setKeyListener(DigitsKeyListener.getInstance("-0123456789"));
        inputNilaiAdd.setFilters(new InputFilter[]{new range_int_min("-100", "100")});
        inputNilaiAdd.setImeOptions(268435456);
        alert.setView((View) inputNilaiAdd);
        alert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                fuel_correction.this.AddValue(inputNilaiAdd.getEditableText().toString());
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
        inputNilai.setKeyListener(DigitsKeyListener.getInstance("-0123456789"));
        inputNilai.setFilters(new InputFilter[]{new range_int_min("-100", "100")});
        inputNilai.setImeOptions(268435456);
        alert_set.setView((View) inputNilai);
        alert_set.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                fuel_correction.this.SetValue(inputNilai.getEditableText().toString());
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
        for (int i = 1; i <= 1281; i++) {
            EditText v = (EditText) findViewById(i);
            String str = v.getText().toString();
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                if (str.length() != 0) {
                    nilai = Double.parseDouble(v.getText().toString());
                } else {
                    nilai = Double.parseDouble("0");
                }
                double nilai2 = nilai + Double.parseDouble(Value);
                if (nilai2 > 100.0d) {
                    nilai2 = 100.0d;
                } else if (nilai2 < -100.0d) {
                    nilai2 = -100.0d;
                }
                v.setText(String.valueOf(Math.round(nilai2)));
            }
        }
    }

    public void SetValue(String Value) {
        for (int i = 1; i <= 1281; i++) {
            EditText v = (EditText) findViewById(i);
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                v.setText(Value);
            }
        }
    }

    /* access modifiers changed from: private */
    public void plus_minus(String pilihan) {
        double nilai;
        for (int i = 1; i <= 1281; i++) {
            EditText v = (EditText) findViewById(i);
            String str = v.getText().toString();
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                if (str.length() != 0) {
                    nilai = Double.parseDouble(v.getText().toString());
                } else {
                    nilai = Double.parseDouble("0");
                }
                if (pilihan.equals("plus")) {
                    nilai += 1.0d;
                } else if (pilihan.equals("minus")) {
                    nilai -= 1.0d;
                }
                if (nilai > 100.0d) {
                    nilai = 100.0d;
                } else if (nilai < -100.0d) {
                    nilai = -100.0d;
                }
                v.setText(String.valueOf(Math.round(nilai)));
            }
        }
    }

    /* access modifiers changed from: private */
    public void select() {
        StaticClass.selectionStatus = false;
        StaticClass.position.clear();
        for (int i = 1; i <= 1281; i++) {
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
        for (int i = 1; i <= 1281; i++) {
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
                for (int i = 1; i <= 1281; i++) {
                    EditText v = (EditText) findViewById(i);
                    if (v.getTag() != null && v.getTag().equals("Warna BLUE")) {
                        for (int k = 0; k < this.maksimum; k++) {
                            String data_ditulis = StaticClass.data.get(k);
                            int letakan = i + (StaticClass.letak.get(k).intValue() - nilai_acuan);
                            this.pengingat = letakan;
                            if (letakan <= 1281) {
                                ((EditText) findViewById(letakan)).setText(data_ditulis);
                            }
                        }
                        StaticClass.position.add(Integer.valueOf(i));
                    }
                }
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

    private void save_file() {
        AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
        alert1.setTitle((CharSequence) "Save File");
        alert1.setMessage((CharSequence) "Enter Your File Name Here");
        final EditText input = new EditText(this);
        alert1.setView((View) input);
        alert1.setPositiveButton((CharSequence) "SAVE", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                fuel_correction.this.srt = input.getEditableText().toString();
                new ExportData(fuel_correction.this).writeToFile(fuel_correction.this.srt);
            }
        });
        alert1.setNegativeButton((CharSequence) "CANCEL", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert1.create().show();
    }

    public void writeToFile(String nama_file) {
        verify(this);
        File file = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/JUKEN") + "/" + (nama_file.replaceAll(".Prjx", "").replaceAll(".prjx", "") + ".Prjx"));
        if (!file.exists()) {
            file.getParentFile().mkdir();
        }
        try {
            file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);
            StringBuilder data2 = new StringBuilder();
            int posisi = 0;
            for (int i = 0; i < MappingHandle.list_fuel.size(); i++) {
                data2.append(String.valueOf(MappingHandle.list_fuel.get(posisi).replace("\r", "").replace("\n", ""))).append("\r\n");
                if (posisi > 1219) {
                    posisi -= 1219;
                    if (posisi == 62) {
                        posisi = 0;
                    }
                } else {
                    posisi += 61;
                }
            }
            int posisi2 = 0;
            for (int i2 = 0; i2 < MappingHandle.list_base_map.size(); i2++) {
                data2.append(String.valueOf(MappingHandle.list_base_map.get(posisi2)).replace(",", ".").replace("\r", "").replace("\n", "")).append("\r\n");
                if (posisi2 > 1219) {
                    posisi2 -= 1219;
                    if (posisi2 == 62) {
                        posisi2 = 0;
                    }
                } else {
                    posisi2 += 61;
                }
            }
            int posisi3 = 0;
            for (int i3 = 0; i3 < MappingHandle.list_injector.size(); i3++) {
                data2.append(String.valueOf(MappingHandle.list_injector.get(posisi3)).replace("\r", "").replace("\n", "")).append("\r\n");
                if (posisi3 > 1219) {
                    posisi3 -= 1219;
                    if (posisi3 == 62) {
                        posisi3 = 0;
                    }
                } else {
                    posisi3 += 61;
                }
            }
            int posisi4 = 0;
            for (int i4 = 0; i4 < MappingHandle.list_ignition.size(); i4++) {
                data2.append(String.valueOf(MappingHandle.list_ignition.get(posisi4)).replace(",", ".").replace("\r", "").replace("\n", "")).append("\r\n");
                if (posisi4 > 619) {
                    posisi4 -= 619;
                    if (posisi4 == 32) {
                        posisi4 = 0;
                    }
                } else {
                    posisi4 += 31;
                }
            }
            for (int i5 = 0; i5 < 1098; i5++) {
                data2.append("0\r\n");
            }
            data2.append("0\r\n");
            int posisi5 = 0;
            for (int i6 = 0; i6 < MappingHandle.list_history.size(); i6++) {
                data2.append(String.valueOf(MappingHandle.list_history.get(posisi5)).replace(",", "").replace(".", "").replace("\r", "").replace("\n", "")).append("\r\n");
                if (posisi5 > 1219) {
                    posisi5 -= 1219;
                    if (posisi5 == 62) {
                        posisi5 = 0;
                    }
                } else {
                    posisi5 += 61;
                }
            }
            int posisi6 = 0;
            for (int i7 = 0; i7 < MappingHandle.list_history.size(); i7++) {
                data2.append(String.valueOf(MappingHandle.list_history.get(posisi6)).replace(",", ".").replace("\r", "").replace("\n", "")).append("\r\n");
                if (posisi6 > 1219) {
                    posisi6 -= 1219;
                    if (posisi6 == 62) {
                        posisi6 = 0;
                    }
                } else {
                    posisi6 += 61;
                }
            }
            data2.append("50\r\n50\r\n");
            stream.write(Encrypt(String.valueOf(data2), "Felix").getBytes());
            stream.flush();
            stream.close();
            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, (String[]) null, (MediaScannerConnection.OnScanCompletedListener) null);
            Toast.makeText(getApplicationContext(), "file saved", 1).show();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            Toast.makeText(getApplicationContext(), "1 : " + e.toString(), 1).show();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(getApplicationContext(), "2 : " + e2.toString(), 1).show();
        }
    }

    public static String Encrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        cipher.init(1, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(keyBytes));
        return Base64.encodeToString(cipher.doFinal(text.getBytes("UTF-8")), 2);
    }

    public class SaveDataExt extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        public SaveDataExt() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.progressDialog = ProgressDialog.show(fuel_correction.this, "Program loading", "Please wait..", true);
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voids) {
            new ExportData(fuel_correction.this).writeToFile("srt");
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void result) {
            super.onPostExecute(result);
            this.progressDialog.dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void SavePreferences(String key, String value2) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value2);
        editor.commit();
    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        MyBroadCastReceiver() {
        }

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r13, android.content.Intent r14) {
            /*
                r12 = this;
                r11 = 1281(0x501, float:1.795E-42)
                r10 = 21
                r8 = 1
                r6 = 0
                java.lang.String r7 = "key"
                java.lang.String r0 = r14.getStringExtra(r7)     // Catch:{ Exception -> 0x006a }
                r7 = -1
                int r9 = r0.hashCode()     // Catch:{ Exception -> 0x006a }
                switch(r9) {
                    case -1094760710: goto L_0x002c;
                    case -1094760499: goto L_0x0040;
                    case -1094760486: goto L_0x0036;
                    case -375011075: goto L_0x0019;
                    case 108388975: goto L_0x004a;
                    case 202106053: goto L_0x0022;
                    default: goto L_0x0014;
                }     // Catch:{ Exception -> 0x006a }
            L_0x0014:
                r6 = r7
            L_0x0015:
                switch(r6) {
                    case 0: goto L_0x0054;
                    case 1: goto L_0x01a9;
                    case 2: goto L_0x026c;
                    case 3: goto L_0x02e1;
                    case 4: goto L_0x0356;
                    case 5: goto L_0x03bc;
                    default: goto L_0x0018;
                }     // Catch:{ Exception -> 0x006a }
            L_0x0018:
                return
            L_0x0019:
                java.lang.String r8 = "dataSaved"
                boolean r8 = r0.equals(r8)     // Catch:{ Exception -> 0x006a }
                if (r8 == 0) goto L_0x0014
                goto L_0x0015
            L_0x0022:
                java.lang.String r6 = "processFuel"
                boolean r6 = r0.equals(r6)     // Catch:{ Exception -> 0x006a }
                if (r6 == 0) goto L_0x0014
                r6 = r8
                goto L_0x0015
            L_0x002c:
                java.lang.String r6 = "processBM"
                boolean r6 = r0.equals(r6)     // Catch:{ Exception -> 0x006a }
                if (r6 == 0) goto L_0x0014
                r6 = 2
                goto L_0x0015
            L_0x0036:
                java.lang.String r6 = "processIT"
                boolean r6 = r0.equals(r6)     // Catch:{ Exception -> 0x006a }
                if (r6 == 0) goto L_0x0014
                r6 = 3
                goto L_0x0015
            L_0x0040:
                java.lang.String r6 = "processIG"
                boolean r6 = r0.equals(r6)     // Catch:{ Exception -> 0x006a }
                if (r6 == 0) goto L_0x0014
                r6 = 4
                goto L_0x0015
            L_0x004a:
                java.lang.String r6 = "recon"
                boolean r6 = r0.equals(r6)     // Catch:{ Exception -> 0x006a }
                if (r6 == 0) goto L_0x0014
                r6 = 5
                goto L_0x0015
            L_0x0054:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.cek     // Catch:{ Exception -> 0x006a }
                if (r6 != 0) goto L_0x00cf
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.hitung_tps     // Catch:{ Exception -> 0x006a }
                if (r6 >= r10) goto L_0x006f
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = r7.posisi_memory     // Catch:{ Exception -> 0x006a }
                r6.kirim(r7)     // Catch:{ Exception -> 0x006a }
                goto L_0x0018
            L_0x006a:
                r3 = move-exception
                r3.printStackTrace()
                goto L_0x0018
            L_0x006f:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.Boolean r6 = r6.sendMapFuel     // Catch:{ Exception -> 0x006a }
                boolean r6 = r6.booleanValue()     // Catch:{ Exception -> 0x006a }
                if (r6 == 0) goto L_0x00ac
                android.os.Message r4 = android.os.Message.obtain()     // Catch:{ Exception -> 0x006a }
                java.lang.String r6 = "Send Base Map"
                r4.obj = r6     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                android.os.Handler r6 = r6.mHandler     // Catch:{ Exception -> 0x006a }
                r4.setTarget(r6)     // Catch:{ Exception -> 0x006a }
                r4.sendToTarget()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.core_dipilih     // Catch:{ Exception -> 0x006a }
                if (r6 != 0) goto L_0x00a7
                java.lang.String r6 = "Base Map - ECU Core 1"
                juken.android.com.juken_5.MappingHandle.NamaFileBM = r6     // Catch:{ Exception -> 0x006a }
            L_0x0097:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r7 = 0
                r6.hitung_tps = r7     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = r7.posisi_memory     // Catch:{ Exception -> 0x006a }
                r6.kirimBaseMap(r7)     // Catch:{ Exception -> 0x006a }
                goto L_0x0018
            L_0x00a7:
                java.lang.String r6 = "Base Map - ECU Core 2"
                juken.android.com.juken_5.MappingHandle.NamaFileBM = r6     // Catch:{ Exception -> 0x006a }
                goto L_0x0097
            L_0x00ac:
                r6 = 0
                juken.android.com.juken_5.StaticClass.saveFuel = r6     // Catch:{ Exception -> 0x006a }
                android.os.Message r4 = android.os.Message.obtain()     // Catch:{ Exception -> 0x006a }
                java.lang.String r6 = "Send Done"
                r4.obj = r6     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                android.os.Handler r6 = r6.mHandler     // Catch:{ Exception -> 0x006a }
                r4.setTarget(r6)     // Catch:{ Exception -> 0x006a }
                r4.sendToTarget()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                android.app.ProgressDialog r6 = r6.progressDialog     // Catch:{ Exception -> 0x006a }
                r6.dismiss()     // Catch:{ Exception -> 0x006a }
                r6 = 1
                juken.android.com.juken_5.StaticClass.bolehPing = r6     // Catch:{ Exception -> 0x006a }
                goto L_0x0018
            L_0x00cf:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.cek     // Catch:{ Exception -> 0x006a }
                r7 = 2
                if (r6 != r7) goto L_0x011a
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.hitung_tps     // Catch:{ Exception -> 0x006a }
                if (r6 >= r10) goto L_0x00e7
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = r7.posisi_memory     // Catch:{ Exception -> 0x006a }
                r6.kirimBaseMap(r7)     // Catch:{ Exception -> 0x006a }
                goto L_0x0018
            L_0x00e7:
                android.os.Message r4 = android.os.Message.obtain()     // Catch:{ Exception -> 0x006a }
                java.lang.String r6 = "Send Injector Timing"
                r4.obj = r6     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                android.os.Handler r6 = r6.mHandler     // Catch:{ Exception -> 0x006a }
                r4.setTarget(r6)     // Catch:{ Exception -> 0x006a }
                r4.sendToTarget()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.core_dipilih     // Catch:{ Exception -> 0x006a }
                if (r6 != 0) goto L_0x0115
                java.lang.String r6 = "Injector Timing - ECU Core 1"
                juken.android.com.juken_5.MappingHandle.NamaFileIT = r6     // Catch:{ Exception -> 0x006a }
            L_0x0105:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r7 = 0
                r6.hitung_tps = r7     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = r7.posisi_memory     // Catch:{ Exception -> 0x006a }
                r6.kirimInjectorTiming(r7)     // Catch:{ Exception -> 0x006a }
                goto L_0x0018
            L_0x0115:
                java.lang.String r6 = "Injector Timing - ECU Core 2"
                juken.android.com.juken_5.MappingHandle.NamaFileIT = r6     // Catch:{ Exception -> 0x006a }
                goto L_0x0105
            L_0x011a:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.cek     // Catch:{ Exception -> 0x006a }
                r7 = 3
                if (r6 != r7) goto L_0x0165
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.hitung_tps     // Catch:{ Exception -> 0x006a }
                if (r6 >= r10) goto L_0x0132
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = r7.posisi_memory     // Catch:{ Exception -> 0x006a }
                r6.kirimInjectorTiming(r7)     // Catch:{ Exception -> 0x006a }
                goto L_0x0018
            L_0x0132:
                android.os.Message r4 = android.os.Message.obtain()     // Catch:{ Exception -> 0x006a }
                java.lang.String r6 = "Send Ignition Timing"
                r4.obj = r6     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                android.os.Handler r6 = r6.mHandler     // Catch:{ Exception -> 0x006a }
                r4.setTarget(r6)     // Catch:{ Exception -> 0x006a }
                r4.sendToTarget()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.core_dipilih     // Catch:{ Exception -> 0x006a }
                if (r6 != 0) goto L_0x0160
                java.lang.String r6 = "Ignition Timing - ECU Core 1"
                juken.android.com.juken_5.MappingHandle.NamaFileIG = r6     // Catch:{ Exception -> 0x006a }
            L_0x0150:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r7 = 0
                r6.hitung_tps = r7     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = r7.posisi_memory     // Catch:{ Exception -> 0x006a }
                r6.kirimIgnitionTiming(r7)     // Catch:{ Exception -> 0x006a }
                goto L_0x0018
            L_0x0160:
                java.lang.String r6 = "Ignition Timing - ECU Core 2"
                juken.android.com.juken_5.MappingHandle.NamaFileIG = r6     // Catch:{ Exception -> 0x006a }
                goto L_0x0150
            L_0x0165:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.cek     // Catch:{ Exception -> 0x006a }
                r7 = 4
                if (r6 != r7) goto L_0x0018
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.hitung_tps     // Catch:{ Exception -> 0x006a }
                if (r6 >= r10) goto L_0x017d
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = r7.posisi_memory     // Catch:{ Exception -> 0x006a }
                r6.kirimIgnitionTiming(r7)     // Catch:{ Exception -> 0x006a }
                goto L_0x0018
            L_0x017d:
                r6 = 0
                juken.android.com.juken_5.StaticClass.saveFuel = r6     // Catch:{ Exception -> 0x006a }
                r6 = 0
                juken.android.com.juken_5.StaticClass.saveBM = r6     // Catch:{ Exception -> 0x006a }
                r6 = 0
                juken.android.com.juken_5.StaticClass.saveIT = r6     // Catch:{ Exception -> 0x006a }
                r6 = 0
                juken.android.com.juken_5.StaticClass.saveIG = r6     // Catch:{ Exception -> 0x006a }
                android.os.Message r4 = android.os.Message.obtain()     // Catch:{ Exception -> 0x006a }
                java.lang.String r6 = "Send Done"
                r4.obj = r6     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                android.os.Handler r6 = r6.mHandler     // Catch:{ Exception -> 0x006a }
                r4.setTarget(r6)     // Catch:{ Exception -> 0x006a }
                r4.sendToTarget()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                android.app.ProgressDialog r6 = r6.progressDialog     // Catch:{ Exception -> 0x006a }
                r6.dismiss()     // Catch:{ Exception -> 0x006a }
                r6 = 1
                juken.android.com.juken_5.StaticClass.bolehPing = r6     // Catch:{ Exception -> 0x006a }
                goto L_0x0018
            L_0x01a9:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r7 = r7.alamat     // Catch:{ Exception -> 0x006a }
                android.view.View r2 = r6.findViewById(r7)     // Catch:{ Exception -> 0x006a }
                android.widget.EditText r2 = (android.widget.EditText) r2     // Catch:{ Exception -> 0x006a }
                java.lang.String r6 = juken.android.com.juken_5.StaticClass.dataGetMap     // Catch:{ Exception -> 0x006a }
                r2.setText(r6)     // Catch:{ Exception -> 0x006a }
                java.util.ArrayList<java.lang.String> r6 = juken.android.com.juken_5.MappingHandle.list_fuel     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = juken.android.com.juken_5.StaticClass.dataGetMap     // Catch:{ Exception -> 0x006a }
                r6.add(r7)     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r7 = r7.alamat     // Catch:{ Exception -> 0x006a }
                r8 = 61
                int r6 = r6.mod(r7, r8)     // Catch:{ Exception -> 0x006a }
                if (r6 != 0) goto L_0x01e9
                java.lang.String r6 = "\\r"
                java.lang.String r7 = ""
                java.lang.String r5 = r0.replaceAll(r6, r7)     // Catch:{ Exception -> 0x006a }
                r2.setText(r5)     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.tps_terima     // Catch:{ Exception -> 0x006a }
                if (r6 >= r10) goto L_0x01fe
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = r7.posisi_memory     // Catch:{ Exception -> 0x006a }
                r6.terima(r7)     // Catch:{ Exception -> 0x006a }
            L_0x01e9:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r7 = r6.alamat     // Catch:{ Exception -> 0x006a }
                int r7 = r7 + 1
                r6.alamat = r7     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.alamat     // Catch:{ Exception -> 0x006a }
                if (r6 <= r11) goto L_0x0018
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r7 = 1
                r6.alamat = r7     // Catch:{ Exception -> 0x006a }
                goto L_0x0018
            L_0x01fe:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.Boolean r6 = r6.sendMapFuel     // Catch:{ Exception -> 0x006a }
                boolean r6 = r6.booleanValue()     // Catch:{ Exception -> 0x006a }
                if (r6 == 0) goto L_0x0244
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.core_dipilih     // Catch:{ Exception -> 0x006a }
                if (r6 != 0) goto L_0x023f
                java.lang.String r6 = "Base Map - ECU Core 1"
                juken.android.com.juken_5.MappingHandle.NamaFileBM = r6     // Catch:{ Exception -> 0x006a }
            L_0x0212:
                android.os.Message r4 = android.os.Message.obtain()     // Catch:{ Exception -> 0x006a }
                java.lang.String r6 = "Get Base Map"
                r4.obj = r6     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                android.os.Handler r6 = r6.mHandler     // Catch:{ Exception -> 0x006a }
                r4.setTarget(r6)     // Catch:{ Exception -> 0x006a }
                r4.sendToTarget()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r7 = 0
                r6.tps_terima = r7     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r7 = 0
                r6.alamat = r7     // Catch:{ Exception -> 0x006a }
                java.util.ArrayList<java.lang.String> r6 = juken.android.com.juken_5.MappingHandle.list_base_map     // Catch:{ Exception -> 0x006a }
                r6.clear()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = r7.posisi_memory     // Catch:{ Exception -> 0x006a }
                r6.terimaBaseMap(r7)     // Catch:{ Exception -> 0x006a }
                goto L_0x01e9
            L_0x023f:
                java.lang.String r6 = "Base Map - ECU Core 2"
                juken.android.com.juken_5.MappingHandle.NamaFileBM = r6     // Catch:{ Exception -> 0x006a }
                goto L_0x0212
            L_0x0244:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r6.CurrentPatternFile()     // Catch:{ Exception -> 0x006a }
                r6 = 0
                juken.android.com.juken_5.StaticClass.saveFuel = r6     // Catch:{ Exception -> 0x006a }
                android.os.Message r4 = android.os.Message.obtain()     // Catch:{ Exception -> 0x006a }
                java.lang.String r6 = "Get Done"
                r4.obj = r6     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                android.os.Handler r6 = r6.mHandler     // Catch:{ Exception -> 0x006a }
                r4.setTarget(r6)     // Catch:{ Exception -> 0x006a }
                r4.sendToTarget()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                android.app.ProgressDialog r6 = r6.progressDialog     // Catch:{ Exception -> 0x006a }
                r6.dismiss()     // Catch:{ Exception -> 0x006a }
                r6 = 1
                juken.android.com.juken_5.StaticClass.bolehPing = r6     // Catch:{ Exception -> 0x006a }
                goto L_0x01e9
            L_0x026c:
                java.util.ArrayList<java.lang.String> r6 = juken.android.com.juken_5.MappingHandle.list_base_map     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = juken.android.com.juken_5.StaticClass.dataGetMap     // Catch:{ Exception -> 0x006a }
                r6.add(r7)     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r7 = r7.alamat     // Catch:{ Exception -> 0x006a }
                r8 = 61
                int r6 = r6.mod(r7, r8)     // Catch:{ Exception -> 0x006a }
                if (r6 != 0) goto L_0x0290
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.tps_terima     // Catch:{ Exception -> 0x006a }
                if (r6 >= r10) goto L_0x02a5
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = r7.posisi_memory     // Catch:{ Exception -> 0x006a }
                r6.terimaBaseMap(r7)     // Catch:{ Exception -> 0x006a }
            L_0x0290:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r7 = r6.alamat     // Catch:{ Exception -> 0x006a }
                int r7 = r7 + 1
                r6.alamat = r7     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.alamat     // Catch:{ Exception -> 0x006a }
                if (r6 <= r11) goto L_0x0018
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r7 = 1
                r6.alamat = r7     // Catch:{ Exception -> 0x006a }
                goto L_0x0018
            L_0x02a5:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r6.CurrentPatternFile()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.core_dipilih     // Catch:{ Exception -> 0x006a }
                if (r6 != 0) goto L_0x02dc
                java.lang.String r6 = "Injector Timing - ECU Core 1"
                juken.android.com.juken_5.MappingHandle.NamaFileIT = r6     // Catch:{ Exception -> 0x006a }
            L_0x02b4:
                android.os.Message r4 = android.os.Message.obtain()     // Catch:{ Exception -> 0x006a }
                java.lang.String r6 = "Get Injector Timing"
                r4.obj = r6     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                android.os.Handler r6 = r6.mHandler     // Catch:{ Exception -> 0x006a }
                r4.setTarget(r6)     // Catch:{ Exception -> 0x006a }
                r4.sendToTarget()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r7 = 0
                r6.tps_terima = r7     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r7 = 0
                r6.alamat = r7     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = r7.posisi_memory     // Catch:{ Exception -> 0x006a }
                r6.terimaInjectorTiming(r7)     // Catch:{ Exception -> 0x006a }
                goto L_0x0290
            L_0x02dc:
                java.lang.String r6 = "Injector Timing - ECU Core 2"
                juken.android.com.juken_5.MappingHandle.NamaFileIT = r6     // Catch:{ Exception -> 0x006a }
                goto L_0x02b4
            L_0x02e1:
                java.util.ArrayList<java.lang.String> r6 = juken.android.com.juken_5.MappingHandle.list_injector     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = juken.android.com.juken_5.StaticClass.dataGetMap     // Catch:{ Exception -> 0x006a }
                r6.add(r7)     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r7 = r7.alamat     // Catch:{ Exception -> 0x006a }
                r8 = 61
                int r6 = r6.mod(r7, r8)     // Catch:{ Exception -> 0x006a }
                if (r6 != 0) goto L_0x0305
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.tps_terima     // Catch:{ Exception -> 0x006a }
                if (r6 >= r10) goto L_0x031a
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = r7.posisi_memory     // Catch:{ Exception -> 0x006a }
                r6.terimaInjectorTiming(r7)     // Catch:{ Exception -> 0x006a }
            L_0x0305:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r7 = r6.alamat     // Catch:{ Exception -> 0x006a }
                int r7 = r7 + 1
                r6.alamat = r7     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.alamat     // Catch:{ Exception -> 0x006a }
                if (r6 <= r11) goto L_0x0018
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r7 = 1
                r6.alamat = r7     // Catch:{ Exception -> 0x006a }
                goto L_0x0018
            L_0x031a:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.core_dipilih     // Catch:{ Exception -> 0x006a }
                if (r6 != 0) goto L_0x0351
                java.lang.String r6 = "Ignition Timing - ECU Core 1"
                juken.android.com.juken_5.MappingHandle.NamaFileIG = r6     // Catch:{ Exception -> 0x006a }
            L_0x0324:
                android.os.Message r4 = android.os.Message.obtain()     // Catch:{ Exception -> 0x006a }
                java.lang.String r6 = "Get Ignition Timing"
                r4.obj = r6     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                android.os.Handler r6 = r6.mHandler     // Catch:{ Exception -> 0x006a }
                r4.setTarget(r6)     // Catch:{ Exception -> 0x006a }
                r4.sendToTarget()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r7 = 0
                r6.tps_terima = r7     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r7 = 0
                r6.alamat = r7     // Catch:{ Exception -> 0x006a }
                java.util.ArrayList<java.lang.String> r6 = juken.android.com.juken_5.MappingHandle.list_ignition     // Catch:{ Exception -> 0x006a }
                r6.clear()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = r7.posisi_memory     // Catch:{ Exception -> 0x006a }
                r6.terimaIgnitionTiming(r7)     // Catch:{ Exception -> 0x006a }
                goto L_0x0305
            L_0x0351:
                java.lang.String r6 = "Ignition Timing - ECU Core 2"
                juken.android.com.juken_5.MappingHandle.NamaFileIG = r6     // Catch:{ Exception -> 0x006a }
                goto L_0x0324
            L_0x0356:
                java.util.ArrayList<java.lang.String> r6 = juken.android.com.juken_5.MappingHandle.list_ignition     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = juken.android.com.juken_5.StaticClass.dataGetMap     // Catch:{ Exception -> 0x006a }
                r6.add(r7)     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r7 = r7.alamat     // Catch:{ Exception -> 0x006a }
                r8 = 31
                int r6 = r6.mod(r7, r8)     // Catch:{ Exception -> 0x006a }
                if (r6 != 0) goto L_0x037a
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.tps_terima     // Catch:{ Exception -> 0x006a }
                if (r6 >= r10) goto L_0x0391
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                java.lang.String r7 = r7.posisi_memory     // Catch:{ Exception -> 0x006a }
                r6.terimaIgnitionTiming(r7)     // Catch:{ Exception -> 0x006a }
            L_0x037a:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r7 = r6.alamat     // Catch:{ Exception -> 0x006a }
                int r7 = r7 + 1
                r6.alamat = r7     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                int r6 = r6.alamat     // Catch:{ Exception -> 0x006a }
                r7 = 651(0x28b, float:9.12E-43)
                if (r6 <= r7) goto L_0x0018
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r7 = 1
                r6.alamat = r7     // Catch:{ Exception -> 0x006a }
                goto L_0x0018
            L_0x0391:
                r6 = 0
                juken.android.com.juken_5.StaticClass.saveFuel = r6     // Catch:{ Exception -> 0x006a }
                r6 = 0
                juken.android.com.juken_5.StaticClass.saveBM = r6     // Catch:{ Exception -> 0x006a }
                r6 = 0
                juken.android.com.juken_5.StaticClass.saveIT = r6     // Catch:{ Exception -> 0x006a }
                r6 = 0
                juken.android.com.juken_5.StaticClass.saveIG = r6     // Catch:{ Exception -> 0x006a }
                android.os.Message r4 = android.os.Message.obtain()     // Catch:{ Exception -> 0x006a }
                java.lang.String r6 = "Get Done"
                r4.obj = r6     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                android.os.Handler r6 = r6.mHandler     // Catch:{ Exception -> 0x006a }
                r4.setTarget(r6)     // Catch:{ Exception -> 0x006a }
                r4.sendToTarget()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                android.app.ProgressDialog r6 = r6.progressDialog     // Catch:{ Exception -> 0x006a }
                r6.dismiss()     // Catch:{ Exception -> 0x006a }
                r6 = 1
                juken.android.com.juken_5.StaticClass.bolehPing = r6     // Catch:{ Exception -> 0x006a }
                goto L_0x037a
            L_0x03bc:
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ IllegalArgumentException -> 0x03d6 }
                juken.android.com.juken_5.folder_fuel.fuel_correction r7 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ IllegalArgumentException -> 0x03d6 }
                juken.android.com.juken_5.folder_fuel.fuel_correction$MyBroadCastReceiver r7 = r7.myBroadCastReceiver     // Catch:{ IllegalArgumentException -> 0x03d6 }
                r6.unregisterReceiver(r7)     // Catch:{ IllegalArgumentException -> 0x03d6 }
            L_0x03c5:
                java.util.ArrayList<java.lang.Integer> r6 = juken.android.com.juken_5.folder_fuel.StaticClass.position     // Catch:{ Exception -> 0x006a }
                r6.clear()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r6.save_data_ke_list()     // Catch:{ Exception -> 0x006a }
                juken.android.com.juken_5.folder_fuel.fuel_correction r6 = juken.android.com.juken_5.folder_fuel.fuel_correction.this     // Catch:{ Exception -> 0x006a }
                r6.finish()     // Catch:{ Exception -> 0x006a }
                goto L_0x0018
            L_0x03d6:
                r1 = move-exception
                r1.printStackTrace()     // Catch:{ Exception -> 0x006a }
                goto L_0x03c5
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.folder_fuel.fuel_correction.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
