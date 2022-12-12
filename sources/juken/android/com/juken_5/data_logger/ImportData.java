package juken.android.com.juken_5.data_logger;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import juken.android.com.juken_5.MappingHandle;
import juken.android.com.juken_5.R;
import juken.android.com.juken_5.folder_fuel.FileArrayAdapter;
import juken.android.com.juken_5.folder_fuel.Option;

public class ImportData extends ListActivity {
    private FileArrayAdapter adapter;
    private File currentDir;
    Boolean pertama100 = true;
    int posisiAkhir = -1;
    int posisiAwal = -1;
    int posisiDataLogger = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.currentDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/JUKEN");
        if (!this.currentDir.exists()) {
            this.currentDir.getParentFile().mkdir();
        }
        fill(this.currentDir);
    }

    private void fill(File f) {
        File[] dirs = f.listFiles();
        setTitle("Directory: " + f.getName());
        List<Option> dir = new ArrayList<>();
        List<Option> fls = new ArrayList<>();
        try {
            for (File ff : dirs) {
                if (ff.isDirectory()) {
                    dir.add(new Option(ff.getName(), "Folder", ff.getAbsolutePath()));
                } else if (ff.getName().substring(ff.getName().length() - 3).equals("txt") || ff.getName().substring(ff.getName().length() - 3).equals("TXT")) {
                    fls.add(new Option(ff.getName(), "File Size: " + ff.length() + " bytes", ff.getAbsolutePath()));
                }
            }
        } catch (Exception e) {
        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if (!f.getName().equalsIgnoreCase("sdcard")) {
            dir.add(0, new Option("", "Parent Directory", f.getParent()));
        }
        this.adapter = new FileArrayAdapter(this, R.layout.file_view, dir);
        setListAdapter(this.adapter);
    }

    /* access modifiers changed from: protected */
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Option o = this.adapter.getItem(position);
        if (o.getData().equalsIgnoreCase("folder") || o.getData().equalsIgnoreCase("parent directory")) {
            this.currentDir = new File(o.getPath());
            fill(this.currentDir);
            return;
        }
        onFileClick(o);
    }

    private void onFileClick(Option o) {
        String temp = o.getName().substring(o.getName().length() - 3);
        String name = o.getName();
        try {
            if (temp.equals("txt")) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(o.getPath(), ""))));
                StringBuilder simpan = new StringBuilder();
                while (true) {
                    String text = bufferedReader.readLine();
                    if (text == null) {
                        break;
                    }
                    simpan.append(text).append(";");
                }
                String[] pisah = String.valueOf(simpan).split(";");
                int hitung = 0;
                int tps_data_logger2 = 0;
                int rpm_data_logger2 = 0;
                int rpm_data_logger_ig2 = 0;
                this.posisiDataLogger = 0;
                MappingHandle.data_data_logger.clear();
                MappingHandle.data_logger_posisi100.clear();
                int length = pisah.length;
                for (int i = 0; i < length; i++) {
                    String s = pisah[i];
                    switch (hitung) {
                        case 0:
                            MappingHandle.data_data_logger.add(s);
                            tps_data_logger2 = Integer.valueOf(s).intValue();
                            if (tps_data_logger2 == 20) {
                                if (this.pertama100.booleanValue()) {
                                    this.posisiAwal = this.posisiDataLogger;
                                    this.pertama100 = null;
                                } else {
                                    this.posisiAkhir = this.posisiDataLogger;
                                }
                            } else if (this.posisiAwal != -1) {
                                MappingHandle.data_logger_posisi100.add(String.valueOf(this.posisiAwal) + ";" + String.valueOf(this.posisiAkhir));
                                this.posisiAkhir = -1;
                                this.posisiAwal = -1;
                                this.pertama100 = 1;
                            }
                            this.posisiDataLogger += 10;
                            hitung = 1;
                            break;
                        case 1:
                            MappingHandle.data_data_logger.add(s);
                            rpm_data_logger2 = Integer.valueOf(s).intValue();
                            if (rpm_data_logger2 % 2 == 0) {
                                rpm_data_logger_ig2 = Integer.valueOf(s).intValue() / 2;
                            } else {
                                rpm_data_logger_ig2 = (Integer.valueOf(s).intValue() + 1) / 2;
                            }
                            hitung = 2;
                            break;
                        case 2:
                            MappingHandle.data_data_logger.add(s);
                            hitung = 3;
                            break;
                        case 3:
                            MappingHandle.data_data_logger.add(s);
                            hitung = 4;
                            break;
                        case 4:
                            MappingHandle.data_data_logger.add(s);
                            hitung = 5;
                            break;
                        case 5:
                            MappingHandle.data_data_logger.add(s);
                            MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger2 * 61) + rpm_data_logger2 + 1));
                            MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger2 * 61) + rpm_data_logger2 + 1));
                            MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger2 * 61) + rpm_data_logger2 + 1));
                            MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger2 * 31) + rpm_data_logger_ig2 + 1));
                            hitung = 0;
                            break;
                    }
                }
                Toast.makeText(this, "File Imported : " + o.getName(), 0).show();
            } else if (temp.equals("TXT")) {
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(new File(o.getPath(), ""))));
                StringBuilder simpan2 = new StringBuilder();
                while (true) {
                    String text2 = bufferedReader2.readLine();
                    if (text2 == null) {
                        break;
                    }
                    simpan2.append(text2).append(";");
                }
                String[] pisah2 = String.valueOf(simpan2).split(";");
                int hitung2 = 0;
                int tps_data_logger22 = 0;
                int rpm_data_logger22 = 0;
                int rpm_data_logger_ig22 = 0;
                String logger_rpm = "";
                String logger_tps = "";
                String logger_afr = "";
                String logger_eot = "";
                MappingHandle.data_data_logger.clear();
                int length2 = pisah2.length;
                for (int i2 = 0; i2 < length2; i2++) {
                    String s2 = pisah2[i2];
                    switch (hitung2) {
                        case 0:
                            rpm_data_logger22 = (((Math.round((4.0f * Float.valueOf(s2).floatValue()) / 1000.0f) * 1000) / 4) + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED) / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
                            if (rpm_data_logger22 % 2 == 0) {
                                rpm_data_logger_ig22 = rpm_data_logger22 / 2;
                            } else {
                                rpm_data_logger_ig22 = (rpm_data_logger22 + 1) / 2;
                            }
                            logger_rpm = String.valueOf(rpm_data_logger22);
                            hitung2 = 1;
                            break;
                        case 1:
                            logger_afr = String.format("%.1f", new Object[]{Double.valueOf(Double.valueOf(s2).doubleValue() / 10.0d)}).replace(",", ".");
                            hitung2 = 2;
                            break;
                        case 2:
                            float tps_baru = Float.valueOf(s2).floatValue();
                            tps_data_logger22 = 0;
                            if (tps_baru >= 1.0f && tps_baru <= 2.0f) {
                                tps_data_logger22 = 1;
                            } else if (tps_baru >= 3.0f && tps_baru <= 90.0f) {
                                tps_data_logger22 = (((Math.round((2.0f * tps_baru) / 10.0f) * 10) / 2) + 5) / 5;
                            } else if (tps_baru >= 91.0f) {
                                tps_data_logger22 = 20;
                            }
                            logger_tps = String.valueOf(tps_data_logger22);
                            hitung2 = 3;
                            break;
                        case 3:
                            logger_eot = String.format("%.1f", new Object[]{Double.valueOf(Double.valueOf(s2).doubleValue() / 10.0d)}).replace(",", ".");
                            hitung2 = 4;
                            break;
                        case 4:
                            String logger_bat = String.format("%.2f", new Object[]{Double.valueOf(Double.valueOf(s2).doubleValue() / 100.0d)}).replace(",", ".");
                            MappingHandle.data_data_logger.add(logger_tps);
                            MappingHandle.data_data_logger.add(logger_rpm);
                            MappingHandle.data_data_logger.add(logger_afr);
                            MappingHandle.data_data_logger.add(logger_eot);
                            MappingHandle.data_data_logger.add(logger_bat);
                            MappingHandle.data_data_logger.add("0");
                            MappingHandle.data_data_logger.add(MappingHandle.list_fuel.get((tps_data_logger22 * 61) + rpm_data_logger22 + 1));
                            MappingHandle.data_data_logger.add(MappingHandle.list_base_map.get((tps_data_logger22 * 61) + rpm_data_logger22 + 1));
                            MappingHandle.data_data_logger.add(MappingHandle.list_injector.get((tps_data_logger22 * 61) + rpm_data_logger22 + 1));
                            MappingHandle.data_data_logger.add(MappingHandle.list_ignition.get((tps_data_logger22 * 31) + rpm_data_logger_ig22 + 1));
                            hitung2 = 0;
                            break;
                        case 5:
                            hitung2 = 0;
                            break;
                    }
                }
                Toast.makeText(this, "File Imported : " + o.getName(), 0).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), 1).show();
        }
        finish();
        startActivityForResult(new Intent(this, data_logger_list.class), 100);
    }

    public static String Decrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        cipher.init(2, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(keyBytes));
        byte[] results = new byte[text.length()];
        try {
            results = cipher.doFinal(Base64.decode(text, 2));
        } catch (Exception e) {
            Log.i("Erron in Decryption", e.toString());
        }
        Log.i("Data", new String(results, "UTF-8"));
        return new String(results, "UTF-8");
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }
}
