package juken.android.com.juken_5.folder_base_map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
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
import juken.android.com.juken_5.main_menu;

public class ImportDataPattern extends ListActivity {
    private FileArrayAdapter adapter;
    private File currentDir;
    int keadaan = 0;
    ArrayList<String> list_penampung = new ArrayList<>();
    String[] separated;

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
                } else {
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
        if (o.getName().substring(o.getName().length() - 3).equals("Ptn")) {
            try {
                this.keadaan = 0;
                BufferedReader myReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(o.getPath(), ""))));
                StringBuilder simpan = new StringBuilder();
                while (true) {
                    String text = myReader.readLine();
                    if (text == null) {
                        break;
                    }
                    simpan.append(text);
                }
                String[] pisah = Decrypt(simpan.toString(), "Felix").split("\r\n");
                MappingHandle.list_pattern.clear();
                for (String add : pisah) {
                    MappingHandle.list_pattern.add(add);
                }
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
                    if (i3 >= MappingHandle.list_pattern.size()) {
                        break;
                    }
                    Double pengurang = Double.valueOf(MappingHandle.list_pattern.get(i3));
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
                                case -2:
                                    ImportDataPattern.this.finish();
                                    ImportDataPattern.this.startActivityForResult(new Intent(ImportDataPattern.this, base_map.class), 100);
                                    return;
                                case -1:
                                    MappingHandle.list_fuel.clear();
                                    MappingHandle.list_base_map.clear();
                                    for (int i = 0; i < 1281; i++) {
                                        MappingHandle.list_fuel.add("0");
                                        MappingHandle.list_base_map.add(ImportDataPattern.this.list_penampung.get(i));
                                    }
                                    ImportDataPattern.this.SavePreferences(main_menu.calculate_pertama, "1");
                                    Toast.makeText(ImportDataPattern.this.getApplicationContext(), "Auto Calculating Success", 0).show();
                                    ImportDataPattern.this.finish();
                                    ImportDataPattern.this.startActivityForResult(new Intent(ImportDataPattern.this, base_map.class), 100);
                                    return;
                                default:
                                    return;
                            }
                        }
                    };
                    new AlertDialog.Builder(this).setMessage("Ada nilai Base Map < 2mS, lanjutkan proses?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                }
                if (this.keadaan == 0) {
                    MappingHandle.list_fuel.clear();
                    MappingHandle.list_base_map.clear();
                    for (int i5 = 0; i5 < 1281; i5++) {
                        MappingHandle.list_fuel.add("0");
                        MappingHandle.list_base_map.add(this.list_penampung.get(i5));
                    }
                    SavePreferences(main_menu.calculate_pertama, "1");
                    Toast.makeText(this, "Auto Calculating Success", 0).show();
                    finish();
                    startActivityForResult(new Intent(this, base_map.class), 100);
                } else if (this.keadaan == 1) {
                    Toast.makeText(this, "Base Map Value = 0.00 mS", 0).show();
                    finish();
                    startActivityForResult(new Intent(this, base_map.class), 100);
                }
            } catch (Exception e) {
                Toast.makeText(this, String.valueOf(e), 1).show();
            }
        } else {
            Toast.makeText(this, "wrong file format", 1).show();
        }
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

    /* access modifiers changed from: private */
    public void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }
}
