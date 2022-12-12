package juken.android.com.juken_5.pattern.pattern2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
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
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import juken.android.com.juken_5.MappingHandle;
import juken.android.com.juken_5.R;
import juken.android.com.juken_5.range_float2;

public class pattern2 extends Activity {
    private static String[] PERMISSION_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    int jumlah = 0;
    int maksimum = 0;
    int pengingat = 1;
    String srt = "";
    int status_flag = 0;

    public static void verify(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, PERMISSION_STORAGE, 1);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern2);
        final TextView text = (TextView) findViewById(1680);
        text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", text.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        final TextView text1 = (TextView) findViewById(1681);
        text1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", text1.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text2 = (TextView) findViewById(1682);
        final TextView textView = text2;
        text2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text3 = (TextView) findViewById(1683);
        final TextView textView2 = text3;
        text3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView2.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text4 = (TextView) findViewById(1684);
        final TextView textView3 = text4;
        text4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView3.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text5 = (TextView) findViewById(1685);
        final TextView textView4 = text5;
        text5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView4.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text6 = (TextView) findViewById(1686);
        final TextView textView5 = text6;
        text6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView5.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text7 = (TextView) findViewById(1687);
        final TextView textView6 = text7;
        text7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView6.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text8 = (TextView) findViewById(1688);
        final TextView textView7 = text8;
        text8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView7.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text9 = (TextView) findViewById(1689);
        final TextView textView8 = text9;
        text9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView8.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        final TextView text10 = (TextView) findViewById(1690);
        text10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", text10.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        final TextView text11 = (TextView) findViewById(1691);
        text11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", text11.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text12 = (TextView) findViewById(1692);
        final TextView textView9 = text12;
        text12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView9.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text13 = (TextView) findViewById(1693);
        final TextView textView10 = text13;
        text13.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView10.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text14 = (TextView) findViewById(1694);
        final TextView textView11 = text14;
        text14.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView11.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text15 = (TextView) findViewById(1695);
        final TextView textView12 = text15;
        text15.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView12.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text16 = (TextView) findViewById(1696);
        final TextView textView13 = text16;
        text16.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView13.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text17 = (TextView) findViewById(1697);
        final TextView textView14 = text17;
        text17.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView14.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        TextView text18 = (TextView) findViewById(1698);
        final TextView textView15 = text18;
        text18.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.save_data_ke_list();
                pattern2.this.finish();
                Intent i1 = new Intent(view.getContext(), imp_pattern.class);
                i1.putExtra("posisi", textView15.getText().toString().replace("100% -> ", "").replace("%", ""));
                pattern2.this.startActivityForResult(i1, 0);
            }
        });
        ((ImageButton) findViewById(R.id.open_pattern)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    pattern2.this.finish();
                    pattern2.this.startActivityForResult(new Intent(pattern2.this, ImportData.class), 100);
                } catch (Exception x) {
                    System.out.println(x.getMessage());
                }
            }
        });
        ((ImageButton) findViewById(R.id.save_pattern)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(pattern2.this);
                alert1.setTitle((CharSequence) "Save File");
                alert1.setMessage((CharSequence) "Enter Your File Name Here");
                final EditText input = new EditText(pattern2.this);
                input.setImeOptions(268435456);
                alert1.setView((View) input);
                alert1.setPositiveButton((CharSequence) "SAVE", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Boolean bisa_save = true;
                        int i = 1;
                        while (true) {
                            if (i > 1159) {
                                break;
                            } else if (Double.valueOf(((EditText) pattern2.this.findViewById(i)).getText().toString().replace("100% -> ", "").replace("%", "").replace("%", "")).doubleValue() > 0.0d) {
                                bisa_save = false;
                                break;
                            } else {
                                i++;
                            }
                        }
                        if (bisa_save.booleanValue()) {
                            pattern2.this.srt = input.getEditableText().toString();
                            pattern2.this.writeToFile(pattern2.this.srt);
                            return;
                        }
                        Toast.makeText(pattern2.this.getApplicationContext(), pattern2.this.getString(R.string.PatternNilaiBesar), 1).show();
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
        ((ImageButton) findViewById(R.id.set_pattern)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.set_value();
            }
        });
        ((ImageButton) findViewById(R.id.add_pattern)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.add_value();
            }
        });
        ((ImageButton) findViewById(R.id.plus_pattern)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.plus_minus("plus");
            }
        });
        ((ImageButton) findViewById(R.id.minus_pattern)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.plus_minus("minus");
            }
        });
        ((ImageButton) findViewById(R.id.copy_pattern)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.Copy();
            }
        });
        ((ImageButton) findViewById(R.id.paste_pattern)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.Paste();
            }
        });
        ((ImageButton) findViewById(R.id.select_pattern)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pattern2.this.select();
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        StaticClass.position.clear();
    }

    /* access modifiers changed from: private */
    public void save_data_ke_list() {
        MappingHandle.list_pattern.clear();
        for (int i = 1; i <= 1159; i++) {
            MappingHandle.list_pattern.add(((EditText) findViewById(i)).getText().toString().replace("%", ""));
        }
    }

    /* access modifiers changed from: private */
    public void plus_minus(String pilihan) {
        double nilai;
        for (int i = 1; i <= 1159; i++) {
            EditText v = (EditText) findViewById(i);
            String str = v.getText().toString().replace("100% -> ", "").replace("%", "").replace("%", "");
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                if (str.length() != 0) {
                    nilai = Double.parseDouble(v.getText().toString().replace("100% -> ", "").replace("%", "").replace("%", ""));
                } else {
                    nilai = Double.parseDouble("0");
                }
                if (pilihan.equals("plus")) {
                    nilai += 0.05d;
                } else if (pilihan.equals("minus")) {
                    nilai -= 0.05d;
                }
                if (nilai > 0.0d) {
                    nilai = 0.0d;
                } else if (nilai < -90.0d) {
                    nilai = -90.0d;
                }
                v.setText(belakang_nol(String.format("%.2f", new Object[]{Double.valueOf(nilai)}).replace(",", ".")) + "%");
            }
        }
    }

    /* access modifiers changed from: private */
    public String belakang_nol(String userInput) {
        int dotPos = -1;
        for (int i = 0; i < userInput.length(); i++) {
            if (userInput.charAt(i) == '.') {
                dotPos = i;
            }
        }
        if (dotPos == -1) {
            return userInput + ".00";
        }
        if (userInput.length() - dotPos == 1) {
            return userInput + "00";
        }
        if (userInput.length() - dotPos == 2) {
            return userInput + "0";
        }
        return userInput;
    }

    public void Copy() {
        this.status_flag = 1;
        if (StaticClass.letak.size() != 0) {
            StaticClass.data.clear();
            StaticClass.letak.clear();
        }
        for (int i = 1; i <= 1159; i++) {
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
                for (int i = 1; i <= 1159; i++) {
                    EditText v = (EditText) findViewById(i);
                    if (v.getTag() != null && v.getTag().equals("Warna BLUE")) {
                        for (int k = 0; k < this.maksimum; k++) {
                            String data_ditulis = StaticClass.data.get(k);
                            int letakan = i + (StaticClass.letak.get(k).intValue() - nilai_acuan);
                            int tanda = mod(this.pengingat, 21);
                            this.pengingat = letakan;
                            if (letakan <= 1281 && tanda != 0) {
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

    private int mod(int x, int y) {
        int result = x % y;
        if (result < 0) {
            return result + y;
        }
        return result;
    }

    /* access modifiers changed from: private */
    public void select() {
        StaticClass.selectionStatus = false;
        StaticClass.position.clear();
        for (int i = 1; i <= 1159; i++) {
            StaticClass.position.add(Integer.valueOf(i));
            EditText x = (EditText) findViewById(i);
            x.setBackgroundColor(-16711681);
            x.setTag("Warna CYAN");
        }
    }

    /* access modifiers changed from: private */
    public void set_value() {
        new AlertDialog.Builder(this);
        AlertDialog.Builder alert_set = new AlertDialog.Builder(this);
        alert_set.setTitle((CharSequence) "Set Value");
        alert_set.setMessage((CharSequence) "Enter Your Value Here");
        final EditText inputNilai = new EditText(this);
        inputNilai.setKeyListener(SignedDecimalKeyListener2.getInstance());
        inputNilai.setFilters(new InputFilter[]{new range_float2(2)});
        alert_set.setView((View) inputNilai);
        alert_set.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                double nilai = 0.0d;
                try {
                    nilai = Double.parseDouble(inputNilai.getEditableText().toString());
                } catch (NullPointerException | NumberFormatException e) {
                }
                if (nilai > 0.0d) {
                    nilai = 0.0d;
                } else if (nilai < -90.0d) {
                    nilai = -90.0d;
                }
                pattern2.this.SetValue(pattern2.this.belakang_nol(String.valueOf(nilai)));
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

    public void SetValue(String Value) {
        for (int i = 1; i <= 1159; i++) {
            EditText v = (EditText) findViewById(i);
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                v.setText(Value + "%");
            }
        }
    }

    /* access modifiers changed from: private */
    public void add_value() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle((CharSequence) "Add Value");
        alert.setMessage((CharSequence) "Enter Your Value Here");
        final EditText inputNilaiAdd = new EditText(this);
        inputNilaiAdd.setKeyListener(SignedDecimalKeyListener2.getInstance());
        inputNilaiAdd.setFilters(new InputFilter[]{new range_float2(2)});
        alert.setView((View) inputNilaiAdd);
        alert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                double nilai = 0.0d;
                try {
                    nilai = Double.parseDouble(inputNilaiAdd.getEditableText().toString());
                } catch (NullPointerException | NumberFormatException e) {
                }
                if (nilai > 0.0d) {
                    nilai = 0.0d;
                } else if (nilai < -90.0d) {
                    nilai = -90.0d;
                }
                pattern2.this.AddValue(String.valueOf(nilai));
            }
        });
        alert.setNegativeButton((CharSequence) "CANCEL", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.create().show();
    }

    public void AddValue(String Value) {
        double nilai;
        for (int i = 1; i <= 1159; i++) {
            EditText v = (EditText) findViewById(i);
            String str = v.getText().toString().replace("%", "");
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                if (str.length() != 0) {
                    nilai = Double.parseDouble(v.getText().toString().replace("%", ""));
                } else {
                    nilai = Double.parseDouble("0");
                }
                double nilai2 = nilai + Double.parseDouble(Value);
                if (nilai2 > 0.0d) {
                    nilai2 = 0.0d;
                } else if (nilai2 < -90.0d) {
                    nilai2 = -90.0d;
                }
                v.setText(belakang_nol(String.valueOf(nilai2)) + "%");
            }
        }
    }

    public void writeToFile(String nama_file) {
        verify(this);
        File file = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/JUKEN") + "/" + (nama_file.replaceAll(".Ptn", "") + ".Ptn"));
        if (!file.exists()) {
            file.getParentFile().mkdir();
        }
        try {
            file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);
            StringBuilder data = new StringBuilder();
            int posisi = 1;
            for (int i = 1; i <= 1159; i++) {
                data.append(((EditText) findViewById(posisi)).getText().toString().replace("%", "")).append("\r\n");
                if (posisi > 1098) {
                    posisi -= 1097;
                    if (posisi == 62) {
                        posisi = 1;
                    }
                } else {
                    posisi += 61;
                }
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
}
