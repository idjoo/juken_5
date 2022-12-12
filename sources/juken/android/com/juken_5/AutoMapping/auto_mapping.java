package juken.android.com.juken_5.AutoMapping;

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
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
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
import juken.android.com.juken_5.folder_base_map.base_map;
import juken.android.com.juken_5.main_menu;

public class auto_mapping extends Activity {
    private static String[] PERMISSION_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    Boolean Koneksi = false;
    Button LoadEcu;
    Button OpenFile;
    RadioButton RCurrent;
    RadioButton Recu;
    RadioButton Rfile;
    Button Run;
    Button SaveEcu;
    TextView TextEcu;
    TextView TextFile;
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            auto_mapping.this.listen();
        }
    };
    int baris_pattern = 1;
    /* access modifiers changed from: private */
    public BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    private InputStream inputStream = null;
    int keadaan = 0;
    int kolom_pattern = 0;
    ArrayList<String> list_penampung = new ArrayList<>();
    MyBroadCastReceiver myBroadCastReceiver;
    int nyambung = 1;
    private OutputStream outputStream = null;
    ProgressDialog progressDialog;
    byte[] readBuffer;
    int readBufferPosition;
    Intent service;
    SharedPreferences sharedPreferences;
    private BluetoothSocket socket = null;
    volatile boolean stopWorker;
    Thread workerThread;

    public static void verify(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, PERMISSION_STORAGE, 1);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_mapping);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        this.Recu = (RadioButton) findViewById(R.id.Recu);
        this.Rfile = (RadioButton) findViewById(R.id.RFile);
        this.RCurrent = (RadioButton) findViewById(R.id.RCurrent);
        this.TextEcu = (TextView) findViewById(R.id.textEcu);
        this.TextFile = (TextView) findViewById(R.id.textFile);
        this.SaveEcu = (Button) findViewById(R.id.save_ecu_pattern);
        this.LoadEcu = (Button) findViewById(R.id.load_ecu_pattern);
        this.OpenFile = (Button) findViewById(R.id.load_file_pattern);
        this.Run = (Button) findViewById(R.id.run_auto_mapping);
        this.Recu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!auto_mapping.this.Recu.isChecked()) {
                    return;
                }
                if (auto_mapping.this.Koneksi.booleanValue()) {
                    auto_mapping.this.Rfile.setChecked(false);
                    auto_mapping.this.TextFile.setEnabled(false);
                    auto_mapping.this.TextFile.setText("");
                    auto_mapping.this.OpenFile.setEnabled(false);
                    auto_mapping.this.RCurrent.setChecked(false);
                    auto_mapping.this.TextEcu.setEnabled(true);
                    auto_mapping.this.LoadEcu.setEnabled(true);
                    if (!auto_mapping.this.TextEcu.getText().toString().equals("")) {
                        auto_mapping.this.SaveEcu.setEnabled(true);
                    } else {
                        auto_mapping.this.SaveEcu.setEnabled(false);
                    }
                } else {
                    auto_mapping.this.Recu.setChecked(false);
                    auto_mapping.this.SaveEcu.setEnabled(false);
                    auto_mapping.this.TextEcu.setEnabled(false);
                    auto_mapping.this.TextEcu.setText("");
                    auto_mapping.this.LoadEcu.setEnabled(false);
                    auto_mapping.this.Rfile.setChecked(true);
                    auto_mapping.this.TextFile.setEnabled(true);
                    auto_mapping.this.OpenFile.setEnabled(true);
                    auto_mapping.this.RCurrent.setChecked(false);
                }
            }
        });
        this.Rfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (auto_mapping.this.Rfile.isChecked()) {
                    auto_mapping.this.Recu.setChecked(false);
                    auto_mapping.this.TextEcu.setEnabled(false);
                    auto_mapping.this.TextEcu.setText("");
                    auto_mapping.this.LoadEcu.setEnabled(false);
                    auto_mapping.this.SaveEcu.setEnabled(false);
                    auto_mapping.this.RCurrent.setChecked(false);
                    auto_mapping.this.TextFile.setEnabled(true);
                    auto_mapping.this.OpenFile.setEnabled(true);
                }
            }
        });
        this.RCurrent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                auto_mapping.this.Recu.setChecked(false);
                auto_mapping.this.Rfile.setChecked(false);
                auto_mapping.this.TextFile.setEnabled(false);
                auto_mapping.this.TextFile.setText("");
                auto_mapping.this.OpenFile.setEnabled(false);
                auto_mapping.this.TextEcu.setEnabled(false);
                auto_mapping.this.TextEcu.setText("");
                auto_mapping.this.LoadEcu.setEnabled(false);
            }
        });
        this.Recu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            }
        });
        this.Rfile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            }
        });
        this.RCurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            }
        });
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String namaFile = this.sharedPreferences.getString("NamaPatternFile", "Nama");
        if (!namaFile.equals("kosong")) {
            this.Recu.setChecked(false);
            this.Rfile.setChecked(true);
            this.TextEcu.setEnabled(false);
            this.SaveEcu.setEnabled(false);
            this.LoadEcu.setEnabled(false);
            this.TextFile.setText(namaFile);
        } else if (this.Koneksi.booleanValue()) {
            this.Recu.setChecked(true);
            this.Rfile.setChecked(false);
            this.TextFile.setEnabled(false);
            this.OpenFile.setEnabled(false);
        } else {
            this.Recu.setChecked(false);
            this.Rfile.setChecked(true);
            this.TextEcu.setEnabled(false);
            this.SaveEcu.setEnabled(false);
            this.LoadEcu.setEnabled(false);
        }
        this.OpenFile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    auto_mapping.this.finish();
                    auto_mapping.this.startActivityForResult(new Intent(auto_mapping.this, ImportData.class), 100);
                } catch (Exception x) {
                    System.out.println(x.getMessage());
                }
            }
        });
        this.LoadEcu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!auto_mapping.this.Koneksi.booleanValue()) {
                    return;
                }
                if (StaticClass.TipeKoneksi.equals("wifi")) {
                    auto_mapping.this.service.putExtra("send", "AutoMappingAwal");
                    auto_mapping.this.startService(auto_mapping.this.service);
                    auto_mapping.this.runOnUiThread(new Runnable() {
                        public void run() {
                            auto_mapping.this.progressDialog = new ProgressDialog(auto_mapping.this);
                            auto_mapping.this.progressDialog.setMessage("Loading...");
                            auto_mapping.this.progressDialog.setTitle("Get Pattern");
                            auto_mapping.this.progressDialog.setProgressStyle(0);
                            auto_mapping.this.progressDialog.show();
                            auto_mapping.this.progressDialog.setCancelable(true);
                        }
                    });
                    Toast.makeText(auto_mapping.this.getApplicationContext(), "Get data", 0).show();
                } else if (StaticClass.TipeKoneksi.equals("bt") && auto_mapping.this.bluetooth.isEnabled()) {
                    auto_mapping.this.baris_pattern = 1;
                    MappingHandle.list_pattern.clear();
                    auto_mapping.this.kirim();
                }
            }
        });
        this.SaveEcu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(auto_mapping.this);
                alert1.setTitle((CharSequence) "Save File");
                alert1.setMessage((CharSequence) "Enter Your File Name Here");
                final EditText input = new EditText(auto_mapping.this);
                input.setText(auto_mapping.this.TextEcu.getText().toString());
                input.setImeOptions(268435456);
                alert1.setView((View) input);
                alert1.setPositiveButton((CharSequence) "SAVE", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!auto_mapping.this.TextEcu.getText().toString().equals("")) {
                            auto_mapping.this.writeToFile(input.getEditableText().toString());
                        }
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
        this.Run.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!PreferenceManager.getDefaultSharedPreferences(auto_mapping.this.getApplicationContext()).getString(main_menu.calculate_pertama, "calculate_pertama1").equals("0")) {
                    Toast.makeText(auto_mapping.this.getApplicationContext(), auto_mapping.this.getString(R.string.AutoMappingBM), 1).show();
                } else if (auto_mapping.this.Recu.isChecked()) {
                    if (!auto_mapping.this.TextEcu.getText().toString().equals("")) {
                        auto_mapping.this.AutoMap(MappingHandle.list_pattern);
                    } else {
                        Toast.makeText(auto_mapping.this.getApplicationContext(), auto_mapping.this.getString(R.string.AutoMappingEcu), 1).show();
                    }
                } else if (auto_mapping.this.Rfile.isChecked()) {
                    if (!auto_mapping.this.TextFile.getText().toString().equals("")) {
                        auto_mapping.this.AutoMap(MappingHandle.list_pattern);
                    } else {
                        Toast.makeText(auto_mapping.this.getApplicationContext(), auto_mapping.this.getString(R.string.AutoMappingFIle), 1).show();
                    }
                } else if (!auto_mapping.this.RCurrent.isChecked()) {
                } else {
                    if (!MappingHandle.list_pattern_current_file.isEmpty() || MappingHandle.list_pattern_current_file.size() != 0) {
                        auto_mapping.this.AutoMap(MappingHandle.list_pattern_current_file);
                    } else {
                        Toast.makeText(auto_mapping.this.getApplicationContext(), auto_mapping.this.getString(R.string.AutoMappingFull), 1).show();
                    }
                }
            }
        });
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.service.putExtra("send", "cekConnection");
            startService(this.service);
        } else if (!StaticClass.TipeKoneksi.equals("bt")) {
        } else {
            if (this.bluetooth.isEnabled()) {
                this.Koneksi = true;
            } else {
                this.Koneksi = false;
            }
        }
    }

    /* access modifiers changed from: private */
    public void AutoMap(ArrayList<String> list) {
        try {
            if (list.size() >= 1) {
                MappingHandle.list_auto.clear();
                for (int i = 0; i < MappingHandle.list_fuel.size(); i++) {
                    Double pengali = Double.valueOf(MappingHandle.list_fuel.get(i));
                    Double nilai_bm = Double.valueOf(MappingHandle.list_base_map.get(i));
                    Double hasil_bm = Double.valueOf(nilai_bm.doubleValue() + ((nilai_bm.doubleValue() * pengali.doubleValue()) / 100.0d));
                    if (hasil_bm.doubleValue() > 20.0d) {
                        hasil_bm = Double.valueOf(20.0d);
                    }
                    MappingHandle.list_auto.add(String.format("%.2f", new Object[]{hasil_bm}).replace(",", "."));
                }
                this.list_penampung.clear();
                for (int i2 = 0; i2 < 61; i2++) {
                    this.list_penampung.add(MappingHandle.list_auto.get(i2));
                }
                int posisi_bm = 1220;
                int i3 = 0;
                while (true) {
                    if (i3 >= list.size()) {
                        break;
                    }
                    Double pengurang = Double.valueOf(list.get(i3));
                    Double nilai_bm2 = Double.valueOf(MappingHandle.list_auto.get(posisi_bm));
                    posisi_bm++;
                    if (posisi_bm > 1280) {
                        posisi_bm = 1220;
                    }
                    Double hasil_bm2 = Double.valueOf(nilai_bm2.doubleValue() + ((nilai_bm2.doubleValue() * pengurang.doubleValue()) / 100.0d));
                    if (hasil_bm2.doubleValue() == 0.0d) {
                        this.keadaan = 1;
                        break;
                    }
                    if (hasil_bm2.doubleValue() < 2.0d) {
                        this.keadaan = 2;
                    }
                    this.list_penampung.add(String.format("%.2f", new Object[]{hasil_bm2}).replace(",", "."));
                    i3++;
                }
                for (int i4 = 1220; i4 < 1281; i4++) {
                    this.list_penampung.add(MappingHandle.list_auto.get(i4));
                }
                if (this.keadaan == 2) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case -1:
                                    MappingHandle.posisi_perubahan_pattern.clear();
                                    for (int i = 0; i < 1281; i++) {
                                        if (!MappingHandle.list_base_map.get(i).equals(auto_mapping.this.list_penampung.get(i))) {
                                            MappingHandle.posisi_perubahan_pattern.add(String.valueOf(i + 1));
                                        }
                                    }
                                    MappingHandle.list_fuel.clear();
                                    MappingHandle.list_base_map.clear();
                                    for (int i2 = 0; i2 < 1281; i2++) {
                                        MappingHandle.list_fuel.add("0");
                                        MappingHandle.list_base_map.add(auto_mapping.this.list_penampung.get(i2));
                                    }
                                    auto_mapping.this.SavePreferences(main_menu.calculate_pertama, "1");
                                    Toast.makeText(auto_mapping.this.getApplicationContext(), "Auto Mapping Success", 0).show();
                                    auto_mapping.this.finish();
                                    auto_mapping.this.startActivityForResult(new Intent(auto_mapping.this, base_map.class), 100);
                                    return;
                                default:
                                    return;
                            }
                        }
                    };
                    new AlertDialog.Builder(this).setMessage((CharSequence) getString(R.string.AutoMappingProcess)).setPositiveButton((CharSequence) "Yes", dialogClickListener).setNegativeButton((CharSequence) "No", dialogClickListener).show();
                }
                if (this.keadaan == 0) {
                    MappingHandle.list_fuel.clear();
                    MappingHandle.list_base_map.clear();
                    for (int i5 = 0; i5 < 1281; i5++) {
                        MappingHandle.list_fuel.add("0");
                        MappingHandle.list_base_map.add(this.list_penampung.get(i5));
                    }
                    SavePreferences(main_menu.calculate_pertama, "1");
                    Toast.makeText(getApplicationContext(), "Auto Mapping Success", 0).show();
                    finish();
                    startActivityForResult(new Intent(this, base_map.class), 100);
                } else if (this.keadaan == 1) {
                    Toast.makeText(getApplicationContext(), "Base Map Value = 0.00 mS", 0).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), 0).show();
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
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(this.sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    auto_mapping.this.connect(perangkat);
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

    public void kirim() {
        byte[] msgBuffer = ("1614;1;" + String.valueOf(this.baris_pattern) + "\r\n").getBytes();
        try {
            this.outputStream = this.socket.getOutputStream();
            this.outputStream.write(msgBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.baris_pattern == 1) {
            runOnUiThread(new Runnable() {
                public void run() {
                    auto_mapping.this.progressDialog = new ProgressDialog(auto_mapping.this);
                    auto_mapping.this.progressDialog.setMessage("Loading...");
                    auto_mapping.this.progressDialog.setTitle("Get Pattern");
                    auto_mapping.this.progressDialog.setProgressStyle(0);
                    auto_mapping.this.progressDialog.show();
                    auto_mapping.this.progressDialog.setCancelable(true);
                }
            });
            Toast.makeText(getApplicationContext(), "Get data", 0).show();
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
                        if (b == 59 || b == 10) {
                            byte[] encodedBytes = new byte[this.readBufferPosition];
                            System.arraycopy(this.readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                            final String data = new String(encodedBytes, "US-ASCII");
                            this.readBufferPosition = 0;
                            this._handler.post(new Runnable() {
                                public void run() {
                                    String s2 = data.replaceAll("\\n", "").replaceAll("\\r", "");
                                    if (!s2.equals("9614")) {
                                        if (Double.valueOf(s2).doubleValue() > 0.0d) {
                                            s2 = "0.00";
                                        }
                                        MappingHandle.list_pattern.add(s2);
                                        auto_mapping.this.kolom_pattern++;
                                        if (auto_mapping.this.kolom_pattern == 61) {
                                            auto_mapping.this.kolom_pattern = 0;
                                            if (auto_mapping.this.baris_pattern == 19) {
                                                auto_mapping.this.baris_pattern = 1;
                                                auto_mapping.this.progressDialog.dismiss();
                                                Toast.makeText(auto_mapping.this.getApplicationContext(), "get done", 0).show();
                                                try {
                                                    auto_mapping.this.TextEcu.setText(auto_mapping.this.sharedPreferences.getString("NamaEcu", "NamaEcu1").replace("\r", "").replace("\n", ""));
                                                    auto_mapping.this.SaveEcu.setEnabled(true);
                                                } catch (Exception e) {
                                                }
                                            } else {
                                                auto_mapping.this.baris_pattern++;
                                                auto_mapping.this.kirim();
                                            }
                                        }
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

    public void writeToFile(String nama_file) {
        verify(this);
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/JUKEN");
        File file = new File(path + "/" + (nama_file.replaceAll(".Ptn", "") + ".Ptn"));
        if (!file.exists()) {
            file.getParentFile().mkdir();
        }
        try {
            file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);
            StringBuilder data = new StringBuilder();
            for (int i = 0; i < MappingHandle.list_pattern.size(); i++) {
                data.append(MappingHandle.list_pattern.get(i).replace("%", "")).append("\r\n");
            }
            stream.write(Encrypt(String.valueOf(data), "Felix").getBytes());
            stream.flush();
            stream.close();
            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, (String[]) null, (MediaScannerConnection.OnScanCompletedListener) null);
            Toast.makeText(getApplicationContext(), "file saved", 1).show();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            Toast.makeText(getApplicationContext(), e.toString(), 1).show();
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(getApplicationContext(), e2.toString(), 1).show();
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

    /* access modifiers changed from: private */
    public void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        MyBroadCastReceiver() {
        }

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r8, android.content.Intent r9) {
            /*
                r7 = this;
                r5 = 1
                r3 = 0
                java.lang.String r4 = "key"
                java.lang.String r1 = r9.getStringExtra(r4)     // Catch:{ Exception -> 0x0047 }
                r4 = -1
                int r6 = r1.hashCode()     // Catch:{ Exception -> 0x0047 }
                switch(r6) {
                    case -2020022020: goto L_0x0028;
                    case -1854273503: goto L_0x001e;
                    case -375011075: goto L_0x0015;
                    case 108388975: goto L_0x0032;
                    default: goto L_0x0010;
                }     // Catch:{ Exception -> 0x0047 }
            L_0x0010:
                r3 = r4
            L_0x0011:
                switch(r3) {
                    case 0: goto L_0x003c;
                    case 1: goto L_0x004c;
                    case 2: goto L_0x009d;
                    case 3: goto L_0x00a5;
                    default: goto L_0x0014;
                }     // Catch:{ Exception -> 0x0047 }
            L_0x0014:
                return
            L_0x0015:
                java.lang.String r5 = "dataSaved"
                boolean r5 = r1.equals(r5)     // Catch:{ Exception -> 0x0047 }
                if (r5 == 0) goto L_0x0010
                goto L_0x0011
            L_0x001e:
                java.lang.String r3 = "AutoMappingDone"
                boolean r3 = r1.equals(r3)     // Catch:{ Exception -> 0x0047 }
                if (r3 == 0) goto L_0x0010
                r3 = r5
                goto L_0x0011
            L_0x0028:
                java.lang.String r3 = "dataKoneksi"
                boolean r3 = r1.equals(r3)     // Catch:{ Exception -> 0x0047 }
                if (r3 == 0) goto L_0x0010
                r3 = 2
                goto L_0x0011
            L_0x0032:
                java.lang.String r3 = "recon"
                boolean r3 = r1.equals(r3)     // Catch:{ Exception -> 0x0047 }
                if (r3 == 0) goto L_0x0010
                r3 = 3
                goto L_0x0011
            L_0x003c:
                java.lang.String r3 = "Saved"
                r4 = 0
                android.widget.Toast r3 = android.widget.Toast.makeText(r8, r3, r4)     // Catch:{ Exception -> 0x0047 }
                r3.show()     // Catch:{ Exception -> 0x0047 }
                goto L_0x0014
            L_0x0047:
                r2 = move-exception
                r2.printStackTrace()
                goto L_0x0014
            L_0x004c:
                juken.android.com.juken_5.AutoMapping.auto_mapping r3 = juken.android.com.juken_5.AutoMapping.auto_mapping.this     // Catch:{ Exception -> 0x0047 }
                android.app.ProgressDialog r3 = r3.progressDialog     // Catch:{ Exception -> 0x0047 }
                boolean r3 = r3.isShowing()     // Catch:{ Exception -> 0x0047 }
                if (r3 == 0) goto L_0x005d
                juken.android.com.juken_5.AutoMapping.auto_mapping r3 = juken.android.com.juken_5.AutoMapping.auto_mapping.this     // Catch:{ Exception -> 0x0047 }
                android.app.ProgressDialog r3 = r3.progressDialog     // Catch:{ Exception -> 0x0047 }
                r3.dismiss()     // Catch:{ Exception -> 0x0047 }
            L_0x005d:
                juken.android.com.juken_5.AutoMapping.auto_mapping r3 = juken.android.com.juken_5.AutoMapping.auto_mapping.this     // Catch:{ Exception -> 0x0047 }
                android.content.Context r3 = r3.getApplicationContext()     // Catch:{ Exception -> 0x0047 }
                java.lang.String r4 = "get done"
                r5 = 0
                android.widget.Toast r3 = android.widget.Toast.makeText(r3, r4, r5)     // Catch:{ Exception -> 0x0047 }
                r3.show()     // Catch:{ Exception -> 0x0047 }
                juken.android.com.juken_5.AutoMapping.auto_mapping r3 = juken.android.com.juken_5.AutoMapping.auto_mapping.this     // Catch:{ Exception -> 0x009a }
                android.content.SharedPreferences r3 = r3.sharedPreferences     // Catch:{ Exception -> 0x009a }
                java.lang.String r4 = "NamaEcu"
                java.lang.String r5 = "NamaEcu1"
                java.lang.String r0 = r3.getString(r4, r5)     // Catch:{ Exception -> 0x009a }
                java.lang.String r3 = "\r"
                java.lang.String r4 = ""
                java.lang.String r3 = r0.replace(r3, r4)     // Catch:{ Exception -> 0x009a }
                java.lang.String r4 = "\n"
                java.lang.String r5 = ""
                java.lang.String r0 = r3.replace(r4, r5)     // Catch:{ Exception -> 0x009a }
                juken.android.com.juken_5.AutoMapping.auto_mapping r3 = juken.android.com.juken_5.AutoMapping.auto_mapping.this     // Catch:{ Exception -> 0x009a }
                android.widget.TextView r3 = r3.TextEcu     // Catch:{ Exception -> 0x009a }
                r3.setText(r0)     // Catch:{ Exception -> 0x009a }
                juken.android.com.juken_5.AutoMapping.auto_mapping r3 = juken.android.com.juken_5.AutoMapping.auto_mapping.this     // Catch:{ Exception -> 0x009a }
                android.widget.Button r3 = r3.SaveEcu     // Catch:{ Exception -> 0x009a }
                r4 = 1
                r3.setEnabled(r4)     // Catch:{ Exception -> 0x009a }
                goto L_0x0014
            L_0x009a:
                r3 = move-exception
                goto L_0x0014
            L_0x009d:
                juken.android.com.juken_5.AutoMapping.auto_mapping r3 = juken.android.com.juken_5.AutoMapping.auto_mapping.this     // Catch:{ Exception -> 0x0047 }
                java.lang.Boolean r4 = juken.android.com.juken_5.StaticClass.koneksi     // Catch:{ Exception -> 0x0047 }
                r3.Koneksi = r4     // Catch:{ Exception -> 0x0047 }
                goto L_0x0014
            L_0x00a5:
                juken.android.com.juken_5.AutoMapping.auto_mapping r3 = juken.android.com.juken_5.AutoMapping.auto_mapping.this     // Catch:{ Exception -> 0x0047 }
                r3.finish()     // Catch:{ Exception -> 0x0047 }
                goto L_0x0014
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.AutoMapping.auto_mapping.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
