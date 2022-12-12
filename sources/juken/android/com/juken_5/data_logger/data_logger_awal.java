package juken.android.com.juken_5.data_logger;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import juken.android.com.juken_5.MappingHandle;
import juken.android.com.juken_5.R;
import juken.android.com.juken_5.StaticClass;
import juken.android.com.juken_5.WifiService;

public class data_logger_awal extends Activity {
    private static String[] PERMISSION_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    Button HapusDataLogger;
    Boolean Koneksi = false;
    Button OpenDataLogger;
    RadioButton Recu;
    RadioButton Rfile;
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            data_logger_awal.this.listen();
        }
    };
    CheckBox afr;
    CheckBox bat;
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    CheckBox eot;
    Boolean erase_data_logger = false;
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    int hitung = 0;
    private InputStream inputStream = null;
    int keadaan = 0;
    Button loadDataLogger;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            data_logger_awal.this.mentah.append((String) message.obj);
            data_logger_awal.this.textMentah.setText(data_logger_awal.this.mentah.toString());
            data_logger_awal.this.scroll.fullScroll(130);
        }
    };
    CheckBox map;
    StringBuilder mentah = new StringBuilder();
    MyBroadCastReceiver myBroadCastReceiver;
    int nyambung = 1;
    private OutputStream outputStream = null;
    String param = "";
    String penampung = "";
    ProgressDialog progressDialog;
    byte[] readBuffer;
    int readBufferPosition;
    ScrollView scroll;
    /* access modifiers changed from: private */
    public final Runnable sendData = new Runnable() {
        public void run() {
            try {
                if (data_logger_awal.this.hitung < 3) {
                    data_logger_awal.this.kirim(data_logger_awal.this.param);
                    data_logger_awal.this.hitung++;
                    data_logger_awal.this.handler.postDelayed(this, 500);
                    return;
                }
                data_logger_awal.this.hitung = 0;
                data_logger_awal.this.handler.removeCallbacks(data_logger_awal.this.sendData);
                Toast.makeText(data_logger_awal.this.getApplicationContext(), "Empty Data Logger", 0).show();
                try {
                    data_logger_awal.this.progressDialog.dismiss();
                } catch (Exception e) {
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    };
    Intent service;
    private BluetoothSocket socket = null;
    volatile boolean stopWorker;
    TextView textMentah;
    Thread workerThread;

    public static void verify(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, PERMISSION_STORAGE, 1);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_logger_awal);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
            this.Koneksi = StaticClass.koneksi;
        } else if (StaticClass.TipeKoneksi.equals("bt")) {
            if (this.bluetooth.isEnabled()) {
                this.Koneksi = true;
            } else {
                this.Koneksi = false;
            }
        }
        this.textMentah = (TextView) findViewById(R.id.textMentah);
        this.scroll = (ScrollView) findViewById(R.id.scroll);
        this.Recu = (RadioButton) findViewById(R.id.Recu);
        this.Recu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!data_logger_awal.this.Recu.isChecked()) {
                    data_logger_awal.this.Rfile.setChecked(true);
                    data_logger_awal.this.afr.setEnabled(false);
                    data_logger_awal.this.eot.setEnabled(false);
                    data_logger_awal.this.bat.setEnabled(false);
                    data_logger_awal.this.map.setEnabled(false);
                    data_logger_awal.this.loadDataLogger.setEnabled(false);
                    data_logger_awal.this.HapusDataLogger.setEnabled(false);
                } else if (data_logger_awal.this.Koneksi.booleanValue()) {
                    data_logger_awal.this.Rfile.setChecked(false);
                    data_logger_awal.this.afr.setEnabled(true);
                    data_logger_awal.this.eot.setEnabled(true);
                    data_logger_awal.this.bat.setEnabled(true);
                    data_logger_awal.this.map.setEnabled(true);
                    data_logger_awal.this.loadDataLogger.setEnabled(true);
                    data_logger_awal.this.HapusDataLogger.setEnabled(true);
                } else {
                    data_logger_awal.this.Recu.setChecked(false);
                    data_logger_awal.this.Rfile.setChecked(true);
                }
            }
        });
        this.Rfile = (RadioButton) findViewById(R.id.Rfile);
        this.Rfile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (data_logger_awal.this.Rfile.isChecked()) {
                    data_logger_awal.this.Recu.setChecked(false);
                    data_logger_awal.this.OpenDataLogger.setEnabled(true);
                    return;
                }
                data_logger_awal.this.Recu.setChecked(true);
                data_logger_awal.this.OpenDataLogger.setEnabled(false);
            }
        });
        this.afr = (CheckBox) findViewById(R.id.param_afr);
        this.map = (CheckBox) findViewById(R.id.param_map);
        this.eot = (CheckBox) findViewById(R.id.param_eot);
        this.bat = (CheckBox) findViewById(R.id.param_bat);
        this.loadDataLogger = (Button) findViewById(R.id.load_ecu_data_logger);
        this.OpenDataLogger = (Button) findViewById(R.id.open_file_data_logger);
        this.HapusDataLogger = (Button) findViewById(R.id.hapus_data_logger);
        this.OpenDataLogger.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    data_logger_awal.this.finish();
                    data_logger_awal.this.startActivityForResult(new Intent(data_logger_awal.this, ImportData.class), 100);
                } catch (Exception x) {
                    System.out.println(x.getMessage());
                }
            }
        });
        this.loadDataLogger.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                data_logger_awal.this.ackAwal();
            }
        });
        this.HapusDataLogger.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                data_logger_awal.this.kirim("1632");
            }
        });
        if (this.Koneksi.booleanValue()) {
            this.Recu.setChecked(true);
            this.afr.setEnabled(true);
            this.eot.setEnabled(true);
            this.bat.setEnabled(true);
            this.map.setEnabled(true);
            this.loadDataLogger.setEnabled(true);
            this.HapusDataLogger.setEnabled(true);
            this.Rfile.setChecked(false);
            this.OpenDataLogger.setEnabled(false);
            return;
        }
        this.Recu.setChecked(false);
        this.afr.setEnabled(false);
        this.eot.setEnabled(false);
        this.bat.setEnabled(false);
        this.map.setEnabled(false);
        this.loadDataLogger.setEnabled(false);
        this.HapusDataLogger.setEnabled(false);
        this.Rfile.setChecked(true);
        this.OpenDataLogger.setEnabled(true);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        StaticClass.dataLoggerCon = true;
        if (StaticClass.TipeKoneksi.equals("bt")) {
            LoadPreferences();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        StaticClass.dataLoggerCon = false;
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            try {
                unregisterReceiver(this.myBroadCastReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else if (StaticClass.TipeKoneksi.equals("bt")) {
            try {
                if (this.nyambung == 1) {
                    resetConnection();
                    Thread.sleep(200);
                }
            } catch (Exception e2) {
            }
        }
    }

    private void LoadPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    data_logger_awal.this.connect(perangkat);
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

    /* access modifiers changed from: private */
    public void ackAwal() {
        runOnUiThread(new Runnable() {
            public void run() {
                data_logger_awal.this.progressDialog = new ProgressDialog(data_logger_awal.this);
                data_logger_awal.this.progressDialog.setMessage("Loading...");
                data_logger_awal.this.progressDialog.setTitle("Load Data");
                data_logger_awal.this.progressDialog.setProgressStyle(0);
                data_logger_awal.this.progressDialog.show();
                data_logger_awal.this.progressDialog.setCancelable(true);
            }
        });
        if (!this.afr.isChecked() && !this.eot.isChecked() && !this.bat.isChecked() && !this.map.isChecked()) {
            Toast.makeText(getApplicationContext(), "Pilih Parameter", 0).show();
        }
        if (!this.afr.isChecked() && !this.eot.isChecked() && !this.bat.isChecked() && this.map.isChecked()) {
            tanya_erase();
            this.param = "1668";
            this.hitung = 0;
        }
        if (!this.afr.isChecked() && !this.eot.isChecked() && this.bat.isChecked() && !this.map.isChecked()) {
            tanya_erase();
            this.param = "1664";
            this.hitung = 0;
        }
        if (!this.afr.isChecked() && !this.eot.isChecked() && this.bat.isChecked() && this.map.isChecked()) {
            tanya_erase();
            this.param = "166C";
            this.hitung = 0;
        }
        if (!this.afr.isChecked() && this.eot.isChecked() && !this.bat.isChecked() && !this.map.isChecked()) {
            tanya_erase();
            this.param = "1662";
            this.hitung = 0;
        }
        if (!this.afr.isChecked() && this.eot.isChecked() && !this.bat.isChecked() && this.map.isChecked()) {
            tanya_erase();
            this.param = "166A";
            this.hitung = 0;
        }
        if (!this.afr.isChecked() && this.eot.isChecked() && this.bat.isChecked() && !this.map.isChecked()) {
            tanya_erase();
            this.param = "1666";
            this.hitung = 0;
        }
        if (!this.afr.isChecked() && this.eot.isChecked() && this.bat.isChecked() && this.map.isChecked()) {
            tanya_erase();
            this.param = "166E";
            this.hitung = 0;
        }
        if (this.afr.isChecked() && !this.eot.isChecked() && !this.bat.isChecked() && !this.map.isChecked()) {
            tanya_erase();
            this.param = "1661";
            this.hitung = 0;
        }
        if (this.afr.isChecked() && !this.eot.isChecked() && !this.bat.isChecked() && this.map.isChecked()) {
            tanya_erase();
            this.param = "1669";
            this.hitung = 0;
        }
        if (this.afr.isChecked() && !this.eot.isChecked() && this.bat.isChecked() && !this.map.isChecked()) {
            tanya_erase();
            this.param = "1665";
            this.hitung = 0;
        }
        if (this.afr.isChecked() && !this.eot.isChecked() && this.bat.isChecked() && this.map.isChecked()) {
            tanya_erase();
            this.param = "166D";
            this.hitung = 0;
        }
        if (this.afr.isChecked() && this.eot.isChecked() && !this.bat.isChecked() && !this.map.isChecked()) {
            tanya_erase();
            this.param = "1663";
            this.hitung = 0;
        }
        if (this.afr.isChecked() && this.eot.isChecked() && !this.bat.isChecked() && this.map.isChecked()) {
            tanya_erase();
            this.param = "166B";
            this.hitung = 0;
        }
        if (this.afr.isChecked() && this.eot.isChecked() && this.bat.isChecked() && !this.map.isChecked()) {
            tanya_erase();
            this.param = "1667";
            this.hitung = 0;
        }
        if (this.afr.isChecked() && this.eot.isChecked() && this.bat.isChecked() && this.map.isChecked()) {
            this.erase_data_logger = false;
            this.param = "166F";
            this.hitung = 0;
            this.handler.post(this.sendData);
        }
    }

    private void tanya_erase() {
        AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
        alert1.setTitle("Warning");
        alert1.setMessage("Hapus Data Logger Setelah Load Data?");
        alert1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                data_logger_awal.this.erase_data_logger = true;
                data_logger_awal.this.handler.post(data_logger_awal.this.sendData);
            }
        });
        alert1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                data_logger_awal.this.erase_data_logger = false;
                data_logger_awal.this.handler.post(data_logger_awal.this.sendData);
                dialog.cancel();
            }
        });
        alert1.create().show();
    }

    public void kirim(String ack) {
        String code4 = ack + ";\r\n";
        byte[] msgBuffer = code4.getBytes();
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

    private void SaveDataLogger() {
        AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
        alert1.setTitle("Save File");
        alert1.setMessage("Enter Your File Name Here");
        final EditText input = new EditText(this);
        input.setImeOptions(268435456);
        alert1.setView(input);
        alert1.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                data_logger_awal.this.writeToFile(input.getEditableText().toString());
                data_logger_awal.this.PindahKeList();
            }
        });
        alert1.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                data_logger_awal.this.PindahKeList();
            }
        });
        alert1.create().show();
    }

    /* access modifiers changed from: private */
    public void PindahKeList() {
        Boolean pertama100 = true;
        int posisiAwal = -1;
        int posisiAkhir = -1;
        MappingHandle.data_logger_posisi100.clear();
        for (int i = 0; i < MappingHandle.data_data_logger.size(); i += 10) {
            if (Integer.valueOf(MappingHandle.data_data_logger.get(i)).intValue() == 20) {
                if (pertama100.booleanValue()) {
                    posisiAwal = i;
                    pertama100 = false;
                } else {
                    posisiAkhir = i;
                }
            } else if (posisiAwal != -1) {
                MappingHandle.data_logger_posisi100.add(String.valueOf(posisiAwal) + ";" + String.valueOf(posisiAkhir));
                posisiAkhir = -1;
                posisiAwal = -1;
                pertama100 = true;
            }
        }
        finish();
        startActivityForResult(new Intent(this, data_logger_list.class), 100);
    }

    /* access modifiers changed from: private */
    public void writeToFile(String nama_file) {
        verify(this);
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/JUKEN");
        File file = new File(path + "/" + (nama_file.replaceAll(".txt", "").replaceAll(".TXT", "") + ".txt"));
        if (!file.exists()) {
            file.getParentFile().mkdir();
        }
        try {
            file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);
            StringBuilder builder = new StringBuilder();
            int posisi = 0;
            for (int i = 0; i < MappingHandle.data_data_logger.size() / 10; i++) {
                builder.append(MappingHandle.data_data_logger.get(posisi)).append(";").append(MappingHandle.data_data_logger.get(posisi + 1)).append(";").append(MappingHandle.data_data_logger.get(posisi + 2)).append(";").append(MappingHandle.data_data_logger.get(posisi + 3)).append(";").append(MappingHandle.data_data_logger.get(posisi + 4)).append(";").append(MappingHandle.data_data_logger.get(posisi + 5)).append("\r\n");
                posisi += 10;
            }
            stream.write(String.valueOf(builder).getBytes());
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

    /* access modifiers changed from: protected */
    public void listen() {
        this.stopWorker = false;
        this.readBufferPosition = 0;
        this.readBuffer = new byte[1024];
        final StringBuilder BufferText = new StringBuilder();
        while (!Thread.currentThread().isInterrupted() && !this.stopWorker && this.nyambung == 1) {
            try {
                this.inputStream = this.socket.getInputStream();
                int bytesAvailable = this.inputStream.available();
                if (bytesAvailable > 0) {
                    this.handler.removeCallbacks(this.sendData);
                    byte[] packetBytes = new byte[bytesAvailable];
                    this.inputStream.read(packetBytes);
                    for (int i = 0; i < bytesAvailable; i++) {
                        byte b = packetBytes[i];
                        BufferText.append(new String(new byte[]{b}, "US-ASCII"));
                        if (b == 10) {
                            this._handler.post(new Runnable() {
                                public void run() {
                                    String s2 = BufferText.toString().replaceAll("\n", "").replaceAll("\r", "");
                                    if (data_logger_awal.this.erase_data_logger.booleanValue()) {
                                        data_logger_awal.this.erase_data_logger = false;
                                        data_logger_awal.this.penampung = s2;
                                        data_logger_awal.this.kirim("1632");
                                    } else if (data_logger_awal.this.penampung.equals("")) {
                                        data_logger_awal.this.SetTextLogger(s2);
                                    } else {
                                        data_logger_awal.this.SetTextLogger(data_logger_awal.this.penampung);
                                    }
                                }
                            });
                        }
                    }
                }
            } catch (IOException e) {
                this.stopWorker = true;
            }
        }
    }

    /* access modifiers changed from: private */
    public void SetTextLogger(String text) {
        Boolean boleh_masuk;
        int rpm_data_logger_ig1;
        int rpm_data_logger_ig12;
        int rpm_data_logger_ig13;
        int rpm_data_logger_ig14;
        int rpm_data_logger_ig15;
        int rpm_data_logger_ig16;
        int rpm_data_logger_ig17;
        int rpm_data_logger_ig18;
        int rpm_data_logger_ig19;
        int rpm_data_logger_ig110;
        int rpm_data_logger_ig111;
        int rpm_data_logger_ig112;
        int rpm_data_logger_ig113;
        int rpm_data_logger_ig114;
        int rpm_data_logger_ig115;
        int rpm_data_logger_ig116;
        String[] newData = text.replace("\r\n", "").replace("\r", "").replace("\n", "").replace("9632", "").split(";");
        int jumlah_data = newData.length;
        String kode = newData[0];
        if (jumlah_data <= 3) {
            Toast.makeText(getApplicationContext(), "Empty Data Logger", 0).show();
            boleh_masuk = false;
        } else {
            boleh_masuk = true;
        }
        if (boleh_masuk.booleanValue()) {
            MappingHandle.data_data_logger.clear();
            char c = 65535;
            switch (kode.hashCode()) {
                case 1751610:
                    if (kode.equals("9630")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    if (!this.param.equals("1661")) {
                        if (!this.param.equals("1662")) {
                            if (!this.param.equals("1663")) {
                                if (!this.param.equals("1664")) {
                                    if (!this.param.equals("1665")) {
                                        if (!this.param.equals("1666")) {
                                            if (!this.param.equals("1667")) {
                                                if (!this.param.equals("1668")) {
                                                    if (!this.param.equals("1669")) {
                                                        if (!this.param.equals("166A")) {
                                                            if (!this.param.equals("166B")) {
                                                                if (!this.param.equals("166C")) {
                                                                    if (!this.param.equals("166D")) {
                                                                        if (!this.param.equals("166E")) {
                                                                            if (!this.param.equals("166F")) {
                                                                                int i = 0;
                                                                                while (i < newData.length / 6) {
                                                                                    try {
                                                                                        int tps_data_logger1 = Integer.valueOf(newData[(i * 6) + 1]).intValue();
                                                                                        int rpm_data_logger1 = Integer.valueOf(newData[(i * 6) + 2]).intValue();
                                                                                        if (rpm_data_logger1 % 2 == 0) {
                                                                                            rpm_data_logger_ig1 = Integer.valueOf(newData[(i * 6) + 2]).intValue() / 2;
                                                                                        } else {
                                                                                            rpm_data_logger_ig1 = (Integer.valueOf(newData[(i * 6) + 2]).intValue() + 1) / 2;
                                                                                        }
                                                                                        for (int k = 1; k <= 6; k++) {
                                                                                            MappingHandle.data_data_logger.add(newData[(i * 6) + k]);
                                                                                        }
                                                                                        MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger1 * 61) + rpm_data_logger1 + 1));
                                                                                        MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger1 * 61) + rpm_data_logger1 + 1));
                                                                                        MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger1 * 61) + rpm_data_logger1 + 1));
                                                                                        MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger1 * 31) + rpm_data_logger_ig1 + 1));
                                                                                        i++;
                                                                                    } catch (Exception e) {
                                                                                        SaveDataLogger();
                                                                                        break;
                                                                                    }
                                                                                }
                                                                                SaveDataLogger();
                                                                                break;
                                                                            } else {
                                                                                int i2 = 0;
                                                                                while (i2 < newData.length / 6) {
                                                                                    try {
                                                                                        int tps_data_logger12 = Integer.valueOf(newData[(i2 * 6) + 1]).intValue();
                                                                                        int rpm_data_logger12 = Integer.valueOf(newData[(i2 * 6) + 2]).intValue();
                                                                                        if (rpm_data_logger12 % 2 == 0) {
                                                                                            rpm_data_logger_ig12 = Integer.valueOf(newData[(i2 * 6) + 2]).intValue() / 2;
                                                                                        } else {
                                                                                            rpm_data_logger_ig12 = (Integer.valueOf(newData[(i2 * 6) + 2]).intValue() + 1) / 2;
                                                                                        }
                                                                                        for (int k2 = 1; k2 <= 6; k2++) {
                                                                                            MappingHandle.data_data_logger.add(newData[(i2 * 6) + k2]);
                                                                                        }
                                                                                        MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger12 * 61) + rpm_data_logger12 + 1));
                                                                                        MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger12 * 61) + rpm_data_logger12 + 1));
                                                                                        MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger12 * 61) + rpm_data_logger12 + 1));
                                                                                        MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger12 * 31) + rpm_data_logger_ig12 + 1));
                                                                                        i2++;
                                                                                    } catch (Exception e2) {
                                                                                        SaveDataLogger();
                                                                                        break;
                                                                                    }
                                                                                }
                                                                                SaveDataLogger();
                                                                                break;
                                                                            }
                                                                        } else {
                                                                            int i3 = 0;
                                                                            while (i3 < newData.length / 5) {
                                                                                try {
                                                                                    int tps_data_logger13 = Integer.valueOf(newData[(i3 * 5) + 1]).intValue();
                                                                                    int rpm_data_logger13 = Integer.valueOf(newData[(i3 * 5) + 2]).intValue();
                                                                                    if (rpm_data_logger13 % 2 == 0) {
                                                                                        rpm_data_logger_ig13 = Integer.valueOf(newData[(i3 * 5) + 2]).intValue() / 2;
                                                                                    } else {
                                                                                        rpm_data_logger_ig13 = (Integer.valueOf(newData[(i3 * 5) + 2]).intValue() + 1) / 2;
                                                                                    }
                                                                                    for (int k3 = 1; k3 <= 2; k3++) {
                                                                                        MappingHandle.data_data_logger.add(newData[(i3 * 5) + k3]);
                                                                                    }
                                                                                    MappingHandle.data_data_logger.add("0");
                                                                                    MappingHandle.data_data_logger.add(newData[(i3 * 5) + 3]);
                                                                                    MappingHandle.data_data_logger.add(newData[(i3 * 5) + 4]);
                                                                                    MappingHandle.data_data_logger.add(newData[(i3 * 5) + 5]);
                                                                                    MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger13 * 61) + rpm_data_logger13 + 1));
                                                                                    MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger13 * 61) + rpm_data_logger13 + 1));
                                                                                    MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger13 * 61) + rpm_data_logger13 + 1));
                                                                                    MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger13 * 31) + rpm_data_logger_ig13 + 1));
                                                                                    i3++;
                                                                                } catch (Exception e3) {
                                                                                    SaveDataLogger();
                                                                                    break;
                                                                                }
                                                                            }
                                                                            SaveDataLogger();
                                                                            break;
                                                                        }
                                                                    } else {
                                                                        int i4 = 0;
                                                                        while (i4 < newData.length / 5) {
                                                                            try {
                                                                                int tps_data_logger14 = Integer.valueOf(newData[(i4 * 5) + 1]).intValue();
                                                                                int rpm_data_logger14 = Integer.valueOf(newData[(i4 * 5) + 2]).intValue();
                                                                                if (rpm_data_logger14 % 2 == 0) {
                                                                                    rpm_data_logger_ig14 = Integer.valueOf(newData[(i4 * 5) + 2]).intValue() / 2;
                                                                                } else {
                                                                                    rpm_data_logger_ig14 = (Integer.valueOf(newData[(i4 * 5) + 2]).intValue() + 1) / 2;
                                                                                }
                                                                                for (int k4 = 1; k4 <= 3; k4++) {
                                                                                    MappingHandle.data_data_logger.add(newData[(i4 * 5) + k4]);
                                                                                }
                                                                                MappingHandle.data_data_logger.add("0");
                                                                                MappingHandle.data_data_logger.add(newData[(i4 * 5) + 4]);
                                                                                MappingHandle.data_data_logger.add(newData[(i4 * 5) + 5]);
                                                                                MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger14 * 61) + rpm_data_logger14 + 1));
                                                                                MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger14 * 61) + rpm_data_logger14 + 1));
                                                                                MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger14 * 61) + rpm_data_logger14 + 1));
                                                                                MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger14 * 31) + rpm_data_logger_ig14 + 1));
                                                                                i4++;
                                                                            } catch (Exception e4) {
                                                                                SaveDataLogger();
                                                                                break;
                                                                            }
                                                                        }
                                                                        SaveDataLogger();
                                                                        break;
                                                                    }
                                                                } else {
                                                                    int i5 = 0;
                                                                    while (i5 < newData.length / 4) {
                                                                        try {
                                                                            int tps_data_logger15 = Integer.valueOf(newData[(i5 * 4) + 1]).intValue();
                                                                            int rpm_data_logger15 = Integer.valueOf(newData[(i5 * 4) + 2]).intValue();
                                                                            if (rpm_data_logger15 % 2 == 0) {
                                                                                rpm_data_logger_ig15 = Integer.valueOf(newData[(i5 * 4) + 2]).intValue() / 2;
                                                                            } else {
                                                                                rpm_data_logger_ig15 = (Integer.valueOf(newData[(i5 * 4) + 2]).intValue() + 1) / 2;
                                                                            }
                                                                            for (int k5 = 1; k5 <= 2; k5++) {
                                                                                MappingHandle.data_data_logger.add(newData[(i5 * 4) + k5]);
                                                                            }
                                                                            MappingHandle.data_data_logger.add("0");
                                                                            MappingHandle.data_data_logger.add("0");
                                                                            MappingHandle.data_data_logger.add(newData[(i5 * 4) + 3]);
                                                                            MappingHandle.data_data_logger.add(newData[(i5 * 4) + 4]);
                                                                            MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger15 * 61) + rpm_data_logger15 + 1));
                                                                            MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger15 * 61) + rpm_data_logger15 + 1));
                                                                            MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger15 * 61) + rpm_data_logger15 + 1));
                                                                            MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger15 * 31) + rpm_data_logger_ig15 + 1));
                                                                            i5++;
                                                                        } catch (Exception e5) {
                                                                            SaveDataLogger();
                                                                            break;
                                                                        }
                                                                    }
                                                                    SaveDataLogger();
                                                                    break;
                                                                }
                                                            } else {
                                                                int i6 = 0;
                                                                while (i6 < newData.length / 5) {
                                                                    try {
                                                                        int tps_data_logger16 = Integer.valueOf(newData[(i6 * 5) + 1]).intValue();
                                                                        int rpm_data_logger16 = Integer.valueOf(newData[(i6 * 5) + 2]).intValue();
                                                                        if (rpm_data_logger16 % 2 == 0) {
                                                                            rpm_data_logger_ig16 = Integer.valueOf(newData[(i6 * 5) + 2]).intValue() / 2;
                                                                        } else {
                                                                            rpm_data_logger_ig16 = (Integer.valueOf(newData[(i6 * 5) + 2]).intValue() + 1) / 2;
                                                                        }
                                                                        for (int k6 = 1; k6 <= 4; k6++) {
                                                                            MappingHandle.data_data_logger.add(newData[(i6 * 5) + k6]);
                                                                        }
                                                                        MappingHandle.data_data_logger.add("0");
                                                                        MappingHandle.data_data_logger.add(newData[(i6 * 5) + 5]);
                                                                        MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger16 * 61) + rpm_data_logger16 + 1));
                                                                        MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger16 * 61) + rpm_data_logger16 + 1));
                                                                        MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger16 * 61) + rpm_data_logger16 + 1));
                                                                        MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger16 * 31) + rpm_data_logger_ig16 + 1));
                                                                        i6++;
                                                                    } catch (Exception e6) {
                                                                        SaveDataLogger();
                                                                        break;
                                                                    }
                                                                }
                                                                SaveDataLogger();
                                                                break;
                                                            }
                                                        } else {
                                                            int i7 = 0;
                                                            while (i7 < newData.length / 4) {
                                                                try {
                                                                    int tps_data_logger17 = Integer.valueOf(newData[(i7 * 4) + 1]).intValue();
                                                                    int rpm_data_logger17 = Integer.valueOf(newData[(i7 * 4) + 2]).intValue();
                                                                    if (rpm_data_logger17 % 2 == 0) {
                                                                        rpm_data_logger_ig17 = Integer.valueOf(newData[(i7 * 4) + 2]).intValue() / 2;
                                                                    } else {
                                                                        rpm_data_logger_ig17 = (Integer.valueOf(newData[(i7 * 4) + 2]).intValue() + 1) / 2;
                                                                    }
                                                                    for (int k7 = 1; k7 <= 2; k7++) {
                                                                        MappingHandle.data_data_logger.add(newData[(i7 * 4) + k7]);
                                                                    }
                                                                    MappingHandle.data_data_logger.add("0");
                                                                    MappingHandle.data_data_logger.add(newData[(i7 * 4) + 3]);
                                                                    MappingHandle.data_data_logger.add("0");
                                                                    MappingHandle.data_data_logger.add(newData[(i7 * 4) + 4]);
                                                                    MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger17 * 61) + rpm_data_logger17 + 1));
                                                                    MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger17 * 61) + rpm_data_logger17 + 1));
                                                                    MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger17 * 61) + rpm_data_logger17 + 1));
                                                                    MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger17 * 31) + rpm_data_logger_ig17 + 1));
                                                                    i7++;
                                                                } catch (Exception e7) {
                                                                    SaveDataLogger();
                                                                    break;
                                                                }
                                                            }
                                                            SaveDataLogger();
                                                            break;
                                                        }
                                                    } else {
                                                        int i8 = 0;
                                                        while (i8 < newData.length / 4) {
                                                            try {
                                                                int tps_data_logger18 = Integer.valueOf(newData[(i8 * 4) + 1]).intValue();
                                                                int rpm_data_logger18 = Integer.valueOf(newData[(i8 * 4) + 2]).intValue();
                                                                if (rpm_data_logger18 % 2 == 0) {
                                                                    rpm_data_logger_ig18 = Integer.valueOf(newData[(i8 * 4) + 2]).intValue() / 2;
                                                                } else {
                                                                    rpm_data_logger_ig18 = (Integer.valueOf(newData[(i8 * 4) + 2]).intValue() + 1) / 2;
                                                                }
                                                                for (int k8 = 1; k8 <= 3; k8++) {
                                                                    MappingHandle.data_data_logger.add(newData[(i8 * 4) + k8]);
                                                                }
                                                                MappingHandle.data_data_logger.add("0");
                                                                MappingHandle.data_data_logger.add("0");
                                                                MappingHandle.data_data_logger.add(newData[(i8 * 4) + 4]);
                                                                MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger18 * 61) + rpm_data_logger18 + 1));
                                                                MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger18 * 61) + rpm_data_logger18 + 1));
                                                                MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger18 * 61) + rpm_data_logger18 + 1));
                                                                MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger18 * 31) + rpm_data_logger_ig18 + 1));
                                                                i8++;
                                                            } catch (Exception e8) {
                                                                SaveDataLogger();
                                                                break;
                                                            }
                                                        }
                                                        SaveDataLogger();
                                                        break;
                                                    }
                                                } else {
                                                    int i9 = 0;
                                                    while (i9 < newData.length / 3) {
                                                        try {
                                                            int tps_data_logger19 = Integer.valueOf(newData[(i9 * 3) + 1]).intValue();
                                                            int rpm_data_logger19 = Integer.valueOf(newData[(i9 * 3) + 2]).intValue();
                                                            if (rpm_data_logger19 % 2 == 0) {
                                                                rpm_data_logger_ig19 = Integer.valueOf(newData[(i9 * 3) + 2]).intValue() / 2;
                                                            } else {
                                                                rpm_data_logger_ig19 = (Integer.valueOf(newData[(i9 * 3) + 2]).intValue() + 1) / 2;
                                                            }
                                                            for (int k9 = 1; k9 <= 2; k9++) {
                                                                MappingHandle.data_data_logger.add(newData[(i9 * 3) + k9]);
                                                            }
                                                            MappingHandle.data_data_logger.add("0");
                                                            MappingHandle.data_data_logger.add("0");
                                                            MappingHandle.data_data_logger.add("0");
                                                            MappingHandle.data_data_logger.add(newData[(i9 * 3) + 3]);
                                                            MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger19 * 61) + rpm_data_logger19 + 1));
                                                            MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger19 * 61) + rpm_data_logger19 + 1));
                                                            MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger19 * 61) + rpm_data_logger19 + 1));
                                                            MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger19 * 31) + rpm_data_logger_ig19 + 1));
                                                            i9++;
                                                        } catch (Exception e9) {
                                                            SaveDataLogger();
                                                            break;
                                                        }
                                                    }
                                                    SaveDataLogger();
                                                    break;
                                                }
                                            } else {
                                                int i10 = 0;
                                                while (i10 < newData.length / 5) {
                                                    try {
                                                        int tps_data_logger110 = Integer.valueOf(newData[(i10 * 5) + 1]).intValue();
                                                        int rpm_data_logger110 = Integer.valueOf(newData[(i10 * 5) + 2]).intValue();
                                                        if (rpm_data_logger110 % 2 == 0) {
                                                            rpm_data_logger_ig110 = Integer.valueOf(newData[(i10 * 5) + 2]).intValue() / 2;
                                                        } else {
                                                            rpm_data_logger_ig110 = (Integer.valueOf(newData[(i10 * 5) + 2]).intValue() + 1) / 2;
                                                        }
                                                        for (int k10 = 1; k10 <= 5; k10++) {
                                                            MappingHandle.data_data_logger.add(newData[(i10 * 5) + k10]);
                                                        }
                                                        MappingHandle.data_data_logger.add("0");
                                                        MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger110 * 61) + rpm_data_logger110 + 1));
                                                        MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger110 * 61) + rpm_data_logger110 + 1));
                                                        MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger110 * 61) + rpm_data_logger110 + 1));
                                                        MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger110 * 31) + rpm_data_logger_ig110 + 1));
                                                        i10++;
                                                    } catch (Exception e10) {
                                                        SaveDataLogger();
                                                        break;
                                                    }
                                                }
                                                SaveDataLogger();
                                                break;
                                            }
                                        } else {
                                            int i11 = 0;
                                            while (i11 < newData.length / 4) {
                                                try {
                                                    int tps_data_logger111 = Integer.valueOf(newData[(i11 * 4) + 1]).intValue();
                                                    int rpm_data_logger111 = Integer.valueOf(newData[(i11 * 4) + 2]).intValue();
                                                    if (rpm_data_logger111 % 2 == 0) {
                                                        rpm_data_logger_ig111 = Integer.valueOf(newData[(i11 * 4) + 2]).intValue() / 2;
                                                    } else {
                                                        rpm_data_logger_ig111 = (Integer.valueOf(newData[(i11 * 4) + 2]).intValue() + 1) / 2;
                                                    }
                                                    for (int k11 = 1; k11 <= 2; k11++) {
                                                        MappingHandle.data_data_logger.add(newData[(i11 * 4) + k11]);
                                                    }
                                                    MappingHandle.data_data_logger.add("0");
                                                    MappingHandle.data_data_logger.add(newData[(i11 * 4) + 3]);
                                                    MappingHandle.data_data_logger.add(newData[(i11 * 4) + 4]);
                                                    MappingHandle.data_data_logger.add("0");
                                                    MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger111 * 61) + rpm_data_logger111 + 1));
                                                    MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger111 * 61) + rpm_data_logger111 + 1));
                                                    MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger111 * 61) + rpm_data_logger111 + 1));
                                                    MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger111 * 31) + rpm_data_logger_ig111 + 1));
                                                    i11++;
                                                } catch (Exception e11) {
                                                    SaveDataLogger();
                                                    break;
                                                }
                                            }
                                            SaveDataLogger();
                                            break;
                                        }
                                    } else {
                                        int i12 = 0;
                                        while (i12 < newData.length / 4) {
                                            try {
                                                int tps_data_logger112 = Integer.valueOf(newData[(i12 * 4) + 1]).intValue();
                                                int rpm_data_logger112 = Integer.valueOf(newData[(i12 * 4) + 2]).intValue();
                                                if (rpm_data_logger112 % 2 == 0) {
                                                    rpm_data_logger_ig112 = Integer.valueOf(newData[(i12 * 4) + 2]).intValue() / 2;
                                                } else {
                                                    rpm_data_logger_ig112 = (Integer.valueOf(newData[(i12 * 4) + 2]).intValue() + 1) / 2;
                                                }
                                                for (int k12 = 1; k12 <= 3; k12++) {
                                                    MappingHandle.data_data_logger.add(newData[(i12 * 4) + k12]);
                                                }
                                                MappingHandle.data_data_logger.add("0");
                                                MappingHandle.data_data_logger.add(newData[(i12 * 4) + 4]);
                                                MappingHandle.data_data_logger.add("0");
                                                MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger112 * 61) + rpm_data_logger112 + 1));
                                                MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger112 * 61) + rpm_data_logger112 + 1));
                                                MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger112 * 61) + rpm_data_logger112 + 1));
                                                MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger112 * 31) + rpm_data_logger_ig112 + 1));
                                                i12++;
                                            } catch (Exception e12) {
                                                SaveDataLogger();
                                                break;
                                            }
                                        }
                                        SaveDataLogger();
                                        break;
                                    }
                                } else {
                                    int i13 = 0;
                                    while (i13 < newData.length / 3) {
                                        try {
                                            int tps_data_logger113 = Integer.valueOf(newData[(i13 * 3) + 1]).intValue();
                                            int rpm_data_logger113 = Integer.valueOf(newData[(i13 * 3) + 2]).intValue();
                                            if (rpm_data_logger113 % 2 == 0) {
                                                rpm_data_logger_ig113 = Integer.valueOf(newData[(i13 * 3) + 2]).intValue() / 2;
                                            } else {
                                                rpm_data_logger_ig113 = (Integer.valueOf(newData[(i13 * 3) + 2]).intValue() + 1) / 2;
                                            }
                                            for (int k13 = 1; k13 <= 2; k13++) {
                                                MappingHandle.data_data_logger.add(newData[(i13 * 3) + k13]);
                                            }
                                            MappingHandle.data_data_logger.add("0");
                                            MappingHandle.data_data_logger.add("0");
                                            MappingHandle.data_data_logger.add(newData[(i13 * 3) + 3]);
                                            MappingHandle.data_data_logger.add("0");
                                            MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger113 * 61) + rpm_data_logger113 + 1));
                                            MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger113 * 61) + rpm_data_logger113 + 1));
                                            MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger113 * 61) + rpm_data_logger113 + 1));
                                            MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger113 * 31) + rpm_data_logger_ig113 + 1));
                                            i13++;
                                        } catch (Exception e13) {
                                            SaveDataLogger();
                                            break;
                                        }
                                    }
                                    SaveDataLogger();
                                    break;
                                }
                            } else {
                                int i14 = 0;
                                while (i14 < newData.length / 4) {
                                    try {
                                        int tps_data_logger114 = Integer.valueOf(newData[(i14 * 4) + 1]).intValue();
                                        int rpm_data_logger114 = Integer.valueOf(newData[(i14 * 4) + 2]).intValue();
                                        if (rpm_data_logger114 % 2 == 0) {
                                            rpm_data_logger_ig114 = Integer.valueOf(newData[(i14 * 4) + 2]).intValue() / 2;
                                        } else {
                                            rpm_data_logger_ig114 = (Integer.valueOf(newData[(i14 * 4) + 2]).intValue() + 1) / 2;
                                        }
                                        for (int k14 = 1; k14 <= 4; k14++) {
                                            MappingHandle.data_data_logger.add(newData[(i14 * 4) + k14]);
                                        }
                                        MappingHandle.data_data_logger.add("0");
                                        MappingHandle.data_data_logger.add("0");
                                        MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger114 * 61) + rpm_data_logger114 + 1));
                                        MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger114 * 61) + rpm_data_logger114 + 1));
                                        MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger114 * 61) + rpm_data_logger114 + 1));
                                        MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger114 * 31) + rpm_data_logger_ig114 + 1));
                                        i14++;
                                    } catch (Exception e14) {
                                        SaveDataLogger();
                                        break;
                                    }
                                }
                                SaveDataLogger();
                                break;
                            }
                        } else {
                            int i15 = 0;
                            while (i15 < newData.length / 3) {
                                try {
                                    int tps_data_logger115 = Integer.valueOf(newData[(i15 * 3) + 1]).intValue();
                                    int rpm_data_logger115 = Integer.valueOf(newData[(i15 * 3) + 2]).intValue();
                                    if (rpm_data_logger115 % 2 == 0) {
                                        rpm_data_logger_ig115 = Integer.valueOf(newData[(i15 * 3) + 2]).intValue() / 2;
                                    } else {
                                        rpm_data_logger_ig115 = (Integer.valueOf(newData[(i15 * 3) + 2]).intValue() + 1) / 2;
                                    }
                                    for (int k15 = 1; k15 <= 2; k15++) {
                                        MappingHandle.data_data_logger.add(newData[(i15 * 3) + k15]);
                                    }
                                    MappingHandle.data_data_logger.add("0");
                                    MappingHandle.data_data_logger.add(newData[(i15 * 3) + 3]);
                                    MappingHandle.data_data_logger.add("0");
                                    MappingHandle.data_data_logger.add("0");
                                    MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger115 * 61) + rpm_data_logger115 + 1));
                                    MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger115 * 61) + rpm_data_logger115 + 1));
                                    MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger115 * 61) + rpm_data_logger115 + 1));
                                    MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger115 * 31) + rpm_data_logger_ig115 + 1));
                                    i15++;
                                } catch (Exception e15) {
                                    SaveDataLogger();
                                    break;
                                }
                            }
                            SaveDataLogger();
                            break;
                        }
                    } else {
                        int i16 = 0;
                        while (i16 < newData.length / 3) {
                            try {
                                int tps_data_logger116 = Integer.valueOf(newData[(i16 * 3) + 1]).intValue();
                                int rpm_data_logger116 = Integer.valueOf(newData[(i16 * 3) + 2]).intValue();
                                if (rpm_data_logger116 % 2 == 0) {
                                    rpm_data_logger_ig116 = Integer.valueOf(newData[(i16 * 3) + 2]).intValue() / 2;
                                } else {
                                    rpm_data_logger_ig116 = (Integer.valueOf(newData[(i16 * 3) + 2]).intValue() + 1) / 2;
                                }
                                for (int k16 = 1; k16 <= 3; k16++) {
                                    MappingHandle.data_data_logger.add(newData[(i16 * 3) + k16]);
                                }
                                MappingHandle.data_data_logger.add("0");
                                MappingHandle.data_data_logger.add("0");
                                MappingHandle.data_data_logger.add("0");
                                MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger116 * 61) + rpm_data_logger116 + 1));
                                MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger116 * 61) + rpm_data_logger116 + 1));
                                MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger116 * 61) + rpm_data_logger116 + 1));
                                MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger116 * 31) + rpm_data_logger_ig116 + 1));
                                i16++;
                            } catch (Exception e16) {
                                SaveDataLogger();
                                break;
                            }
                        }
                        SaveDataLogger();
                        break;
                    }
            }
        }
        try {
            this.progressDialog.dismiss();
        } catch (Exception e17) {
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
                java.lang.String r0 = r7.getStringExtra(r3)     // Catch:{ Exception -> 0x0056 }
                r3 = -1
                int r4 = r0.hashCode()     // Catch:{ Exception -> 0x0056 }
                switch(r4) {
                    case -375011075: goto L_0x0014;
                    case 108388975: goto L_0x0027;
                    case 2144230189: goto L_0x001d;
                    default: goto L_0x000f;
                }     // Catch:{ Exception -> 0x0056 }
            L_0x000f:
                r2 = r3
            L_0x0010:
                switch(r2) {
                    case 0: goto L_0x0031;
                    case 1: goto L_0x005b;
                    case 2: goto L_0x0063;
                    default: goto L_0x0013;
                }     // Catch:{ Exception -> 0x0056 }
            L_0x0013:
                return
            L_0x0014:
                java.lang.String r4 = "dataSaved"
                boolean r4 = r0.equals(r4)     // Catch:{ Exception -> 0x0056 }
                if (r4 == 0) goto L_0x000f
                goto L_0x0010
            L_0x001d:
                java.lang.String r2 = "DataLoggerFinish"
                boolean r2 = r0.equals(r2)     // Catch:{ Exception -> 0x0056 }
                if (r2 == 0) goto L_0x000f
                r2 = 1
                goto L_0x0010
            L_0x0027:
                java.lang.String r2 = "recon"
                boolean r2 = r0.equals(r2)     // Catch:{ Exception -> 0x0056 }
                if (r2 == 0) goto L_0x000f
                r2 = 2
                goto L_0x0010
            L_0x0031:
                java.lang.String r2 = "Data Clear"
                r3 = 0
                android.widget.Toast r2 = android.widget.Toast.makeText(r6, r2, r3)     // Catch:{ Exception -> 0x0056 }
                r2.show()     // Catch:{ Exception -> 0x0056 }
                juken.android.com.juken_5.data_logger.data_logger_awal r2 = juken.android.com.juken_5.data_logger.data_logger_awal.this     // Catch:{ Exception -> 0x0056 }
                java.lang.Boolean r2 = r2.erase_data_logger     // Catch:{ Exception -> 0x0056 }
                boolean r2 = r2.booleanValue()     // Catch:{ Exception -> 0x0056 }
                if (r2 == 0) goto L_0x0013
                juken.android.com.juken_5.data_logger.data_logger_awal r2 = juken.android.com.juken_5.data_logger.data_logger_awal.this     // Catch:{ Exception -> 0x0056 }
                r3 = 0
                java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)     // Catch:{ Exception -> 0x0056 }
                r2.erase_data_logger = r3     // Catch:{ Exception -> 0x0056 }
                juken.android.com.juken_5.data_logger.data_logger_awal r2 = juken.android.com.juken_5.data_logger.data_logger_awal.this     // Catch:{ Exception -> 0x0056 }
                java.lang.String r3 = "1632"
                r2.kirim(r3)     // Catch:{ Exception -> 0x0056 }
                goto L_0x0013
            L_0x0056:
                r1 = move-exception
                r1.printStackTrace()
                goto L_0x0013
            L_0x005b:
                juken.android.com.juken_5.data_logger.data_logger_awal r2 = juken.android.com.juken_5.data_logger.data_logger_awal.this     // Catch:{ Exception -> 0x0056 }
                java.lang.String r3 = juken.android.com.juken_5.StaticClass.DataLogger     // Catch:{ Exception -> 0x0056 }
                r2.SetTextLogger(r3)     // Catch:{ Exception -> 0x0056 }
                goto L_0x0013
            L_0x0063:
                juken.android.com.juken_5.data_logger.data_logger_awal r2 = juken.android.com.juken_5.data_logger.data_logger_awal.this     // Catch:{ Exception -> 0x0056 }
                r2.finish()     // Catch:{ Exception -> 0x0056 }
                goto L_0x0013
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.data_logger.data_logger_awal.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
