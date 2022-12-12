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
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class auto_timing extends Activity {
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    private static final String[] array = {"85", "90", "95", "100", "105", "110", "115", "120"};
    private Handler _handler = new Handler();
    private Thread _serverWorker = new Thread() {
        public void run() {
            auto_timing.this.listen();
        }
    };
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    Button exit;
    private InputStream inputStream = null;
    int keadaan = 0;
    private LineChart mChart;
    MyBroadCastReceiver myBroadCastReceiver;
    int nyambung = 1;
    private OutputStream outputStream = null;
    byte[] readBuffer;
    int readBufferPosition;
    EditText retard;
    Button save;
    Intent service;
    LineDataSet set1;
    private BluetoothSocket socket = null;
    volatile boolean stopWorker;
    Boolean sudah_masuk = false;
    Spinner temperatur;
    Thread workerThread;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_timing);
        if (StaticClass.TipeKoneksi.equals("wifi")) {
            this.myBroadCastReceiver = new MyBroadCastReceiver();
            registerMyReceiver();
            this.service = new Intent(this, WifiService.class);
        }
        this.retard = (EditText) findViewById(R.id.retard);
        this.retard.setFilters(new InputFilter[]{new range_int1("0", "5")});
        this.retard.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                if (auto_timing.this.sudah_masuk.booleanValue()) {
                    int position = auto_timing.this.temperatur.getSelectedItemPosition();
                    int nilai = 0;
                    try {
                        nilai = Integer.parseInt(auto_timing.this.retard.getText().toString()) * -1;
                    } catch (NumberFormatException e) {
                    }
                    auto_timing.this.ubah_chart(nilai, position);
                }
            }
        });
        this.temperatur = (Spinner) findViewById(R.id.temp);
        this.temperatur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (auto_timing.this.sudah_masuk.booleanValue()) {
                    int nilai = 0;
                    try {
                        nilai = Integer.parseInt(auto_timing.this.retard.getText().toString()) * -1;
                    } catch (NumberFormatException e) {
                    }
                    auto_timing.this.ubah_chart(nilai, position);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.save = (Button) findViewById(R.id.save_auto_timing);
        this.save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                auto_timing.this.kirim();
            }
        });
        this.exit = (Button) findViewById(R.id.exit_auto_timing);
        this.exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                auto_timing.this.finish();
            }
        });
        this.mChart = (LineChart) findViewById(R.id.linechart);
        this.mChart.setDrawGridBackground(false);
        setData();
        this.mChart.getLegend().setForm(Legend.LegendForm.LINE);
        this.mChart.setDescription("");
        this.mChart.setNoDataTextDescription("You need to provide data for the chart.");
        this.mChart.setTouchEnabled(true);
        this.mChart.setDragEnabled(true);
        this.mChart.setScaleEnabled(true);
        addLimit();
        this.mChart.animateX(100, Easing.EasingOption.EaseInOutBounce);
        this.mChart.invalidate();
    }

    /* access modifiers changed from: private */
    public void ubah_chart(int nilai, int posisi) {
        ArrayList<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < posisi; i++) {
            yVals.add(new Entry(0.0f, i));
        }
        for (int i2 = posisi; i2 <= 11; i2++) {
            yVals.add(new Entry((float) nilai, i2));
        }
        this.set1.clear();
        ArrayList<String> xVals = setXAxisValues();
        this.set1 = new LineDataSet(yVals, "");
        this.set1.setFillAlpha(110);
        this.set1.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.set1.setCircleColor(ViewCompat.MEASURED_STATE_MASK);
        this.set1.setLineWidth(1.0f);
        this.set1.setCircleRadius(3.0f);
        this.set1.setDrawCircleHole(false);
        this.set1.setValueTextSize(9.0f);
        this.set1.setDrawFilled(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(this.set1);
        this.mChart.setData(new LineData((List<String>) xVals, (List<ILineDataSet>) dataSets));
        this.mChart.invalidate();
    }

    private ArrayList<String> setXAxisValues() {
        ArrayList<String> xVals = new ArrayList<>();
        xVals.add("85");
        xVals.add("90");
        xVals.add("95");
        xVals.add("100");
        xVals.add("105");
        xVals.add("110");
        xVals.add("115");
        xVals.add("120");
        xVals.add("125");
        xVals.add("130");
        xVals.add("135");
        xVals.add("140");
        return xVals;
    }

    private ArrayList<Entry> setYAxisValues() {
        ArrayList<Entry> yVals = new ArrayList<>();
        yVals.add(new Entry(0.0f, 0));
        yVals.add(new Entry(0.0f, 1));
        yVals.add(new Entry(-5.0f, 2));
        yVals.add(new Entry(-5.0f, 3));
        yVals.add(new Entry(-5.0f, 4));
        yVals.add(new Entry(-5.0f, 5));
        yVals.add(new Entry(-5.0f, 6));
        yVals.add(new Entry(-5.0f, 7));
        yVals.add(new Entry(-5.0f, 8));
        yVals.add(new Entry(-5.0f, 9));
        yVals.add(new Entry(-5.0f, 10));
        yVals.add(new Entry(-5.0f, 11));
        return yVals;
    }

    private void setData() {
        ArrayList<String> xVals = setXAxisValues();
        this.set1 = new LineDataSet(setYAxisValues(), "Auto Timing");
        this.set1.setFillAlpha(110);
        this.set1.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.set1.setCircleColor(ViewCompat.MEASURED_STATE_MASK);
        this.set1.setLineWidth(1.0f);
        this.set1.setCircleRadius(3.0f);
        this.set1.setDrawCircleHole(false);
        this.set1.setValueTextSize(9.0f);
        this.set1.setDrawFilled(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(this.set1);
        this.mChart.setData(new LineData((List<String>) xVals, (List<ILineDataSet>) dataSets));
    }

    private void addLimit() {
        LimitLine lower_limit = new LimitLine(0.0f, "");
        lower_limit.setLineWidth(4.0f);
        lower_limit.enableDashedLine(10.0f, 10.0f, 0.0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(0.0f);
        YAxis leftAxis = this.mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaxValue(5.0f);
        leftAxis.setAxisMinValue(-5.0f);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.mDecimals = 0;
        leftAxis.enableGridDashedLine(10.0f, 10.0f, 0.0f);
        leftAxis.setDrawLimitLinesBehindData(true);
        this.mChart.getAxisRight().setEnabled(false);
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
        String nilai_retard = sharedPreferences.getString("auto_ig", "auto_ig1");
        String nilai_temp = sharedPreferences.getString("auto_ig_temp", "auto_ig_temp1");
        this.retard = (EditText) findViewById(R.id.retard);
        this.retard.setText(nilai_retard);
        int pakai = (Integer.parseInt(nilai_temp) / 5) - 17;
        this.temperatur = (Spinner) findViewById(R.id.temp);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 17367048, array);
        adapter.setDropDownViewResource(17367049);
        this.temperatur.setAdapter(adapter);
        this.temperatur.setSelection(pakai);
        this.sudah_masuk = true;
        if (StaticClass.TipeKoneksi.equals("bt") && this.bluetooth.isEnabled()) {
            final BluetoothDevice perangkat = this.bluetooth.getRemoteDevice(sharedPreferences.getString("android.bluetooth.device.extra.DEVICE", "rame"));
            new Thread() {
                public void run() {
                    auto_timing.this.connect(perangkat);
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
        String code = this.retard.getText().toString();
        String code1 = this.temperatur.getSelectedItem().toString();
        String code2 = "3606" + ";" + code1 + ";" + code + "\r\n";
        byte[] msgBuffer = code2.getBytes();
        SavePreferences("auto_ig", code);
        SavePreferences("auto_ig_temp", code1);
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
                                        Toast.makeText(auto_timing.this.getApplicationContext(), "data saved", 0).show();
                                    } else if (auto_timing.this.keadaan == 0) {
                                        Toast.makeText(auto_timing.this.getApplicationContext(), "failed to send", 0).show();
                                        auto_timing.this.keadaan = 1;
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
                juken.android.com.juken_5.auto_timing r2 = juken.android.com.juken_5.auto_timing.this     // Catch:{ Exception -> 0x0032 }
                r2.finish()     // Catch:{ Exception -> 0x0032 }
                goto L_0x0013
            */
            throw new UnsupportedOperationException("Method not decompiled: juken.android.com.juken_5.auto_timing.MyBroadCastReceiver.onReceive(android.content.Context, android.content.Intent):void");
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
