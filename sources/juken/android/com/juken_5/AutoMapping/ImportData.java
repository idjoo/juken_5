package juken.android.com.juken_5.AutoMapping;

import android.app.ListActivity;
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
import juken.android.com.juken_5.folder_fuel.FileArrayAdapter;
import juken.android.com.juken_5.folder_fuel.Option;

public class ImportData extends ListActivity {
    private FileArrayAdapter adapter;
    private File currentDir;

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
                } else if (ff.getName().substring(ff.getName().length() - 3).equals("Ptn")) {
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
            if (temp.equals("Ptn")) {
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
                ArrayList<String> list_patternS = new ArrayList<>();
                for (String add : pisah) {
                    list_patternS.add(add);
                }
                MappingHandle.list_pattern.clear();
                for (int i = 0; i < 19; i++) {
                    int posisi = i;
                    for (int j = 0; j < 61; j++) {
                        MappingHandle.list_pattern.add(list_patternS.get(posisi));
                        posisi += 19;
                    }
                }
                SavePreferences("NamaPatternFile", o.getName().replace(".Ptn", ""));
                Toast.makeText(this, "File Imported : " + o.getName(), 0).show();
            }
        } catch (Exception e) {
        }
        finish();
        startActivityForResult(new Intent(this, auto_mapping.class), 100);
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
