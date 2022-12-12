package juken.android.com.juken_5.folder_ignition;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

public class ImportData extends ListActivity {
  private FileArrayAdapter adapter;
  private File currentDir;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.currentDir = new File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/JUKEN");
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
        } else if (ff.getName().substring(ff.getName().length() - 4).equals("Prjx")) {
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
    String temp = o.getName().substring(o.getName().length() - 4);
    String name = o.getName();
    try {
      if (temp.equals("Prjx")) {
        String[] result = Decrypt(
            new BufferedReader(new InputStreamReader(new FileInputStream(new File(o.getPath(), "")))).readLine(),
            "Felix").split("\r\n");
        ArrayList<String> FuelSementara = new ArrayList<>();
        ArrayList<String> IgSementara = new ArrayList<>();
        ArrayList<String> ItSementara = new ArrayList<>();
        ArrayList<String> BmSementara = new ArrayList<>();
        ArrayList<String> HistorySementara = new ArrayList<>();
        MappingHandle.list_fuel.clear();
        MappingHandle.list_base_map.clear();
        MappingHandle.list_injector.clear();
        MappingHandle.list_ignition.clear();
        MappingHandle.list_history.clear();
        int mulai = 1;
        int length = result.length;
        for (int i = 0; i < length; i++) {
          String s = result[i];
          if (mulai >= 1 && mulai <= 1281) {
            FuelSementara.add(s.replace("\r", "").replace("\n", ""));
          } else if (mulai > 1281 && mulai <= 2562) {
            BmSementara.add(s.replace(",", ".").replace("\r", "").replace("\n", ""));
          } else if (mulai > 2562 && mulai <= 3843) {
            ItSementara.add(s.replace("\r", "").replace("\n", ""));
          } else if (mulai > 3843 && mulai <= 4494) {
            IgSementara.add(s.replace(",", ".").replace("\r", "").replace("\n", ""));
          } else if ((mulai <= 4494 || mulai > 5593)
              && ((mulai <= 5593 || mulai > 6874) && mulai > 6874 && mulai <= 8155)) {
            HistorySementara.add(s.replace(",", ".").replace("\r", "").replace("\n", ""));
          }
          mulai++;
        }
        int posisi = 0;
        for (int i2 = 0; i2 < FuelSementara.size(); i2++) {
          MappingHandle.list_fuel.add(FuelSementara.get(posisi));
          MappingHandle.list_base_map.add(BmSementara.get(posisi));
          MappingHandle.list_injector.add(ItSementara.get(posisi));
          MappingHandle.list_history.add(HistorySementara.get(posisi));
          if (posisi > 1259) {
            posisi -= 1259;
            if (posisi == 21) {
              posisi = 0;
            }
          } else {
            posisi += 21;
          }
        }
        int posisi2 = 0;
        for (int i3 = 0; i3 < IgSementara.size(); i3++) {
          MappingHandle.list_ignition.add(IgSementara.get(posisi2));
          if (posisi2 > 629) {
            posisi2 -= 629;
            if (posisi2 == 21) {
              posisi2 = 0;
            }
          } else {
            posisi2 += 21;
          }
        }
        MappingHandle.list_pattern_current_file.clear();
        ArrayList<String> bmTotSementara = new ArrayList<>();
        for (int i4 = 0; i4 < MappingHandle.list_fuel.size(); i4++) {
          Double Fuel = Double.valueOf(MappingHandle.list_fuel.get(i4));
          Double Bm = Double.valueOf(MappingHandle.list_base_map.get(i4));
          Double BmTot = Double.valueOf(Bm.doubleValue() + ((Bm.doubleValue() * Fuel.doubleValue()) / 100.0d));
          if (BmTot.doubleValue() > 20.0d) {
            BmTot = Double.valueOf(20.0d);
          }
          if (BmTot.doubleValue() < 0.0d) {
            BmTot = Double.valueOf(0.0d);
          }
          bmTotSementara.add(String.format("%.2f", new Object[] { BmTot }).replace(",", "."));
        }
        for (int i5 = 19; i5 > 0; i5--) {
          for (int j = 0; j < 61; j++) {
            int posisi1 = j + 1220;
            double seratus = Double.valueOf(bmTotSementara.get(posisi1)).doubleValue();
            MappingHandle.list_pattern_current_file.add(String.format("%.2f",
                new Object[] { Double.valueOf(
                    ((Double.valueOf(bmTotSementara.get(posisi1 - (i5 * 61))).doubleValue() - seratus) * 100.0d)
                        / seratus) })
                .replace(",", "."));
          }
        }
        MappingHandle.NamaFileFuel = "Fuel Correction - " + o.getName();
        MappingHandle.NamaFileBM = "Base Map - " + o.getName();
        MappingHandle.NamaFileIG = "Ignition Timing - " + o.getName();
        MappingHandle.NamaFileIT = "Injector Timing - " + o.getName();
        Toast.makeText(this, "File Imported : " + o.getName(), 0).show();
      }
    } catch (Exception e) {
    }
    finish();
    startActivityForResult(new Intent(this, ignition_timing.class), 100);
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
}
