package juken.android.com.juken_5.folder_analisis;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import juken.android.com.juken_5.R;
import juken.android.com.juken_5.range_int1;

public class AnalisisMatrixFragment extends Fragment {
    private static final String[] array = {"0", "2", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90", "100"};
    int jumlah = 0;
    int maksimum = 0;
    int pengingat = 1;
    int posisiSpinner = 20;
    Spinner spinnerTPS;
    int status_flag = 0;
    View view;

    public static AnalisisMatrixFragment newInstance(String param1, String param2) {
        AnalisisMatrixFragment fragment = new AnalisisMatrixFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onDestroy() {
        super.onDestroy();
        save_data_exit(this.spinnerTPS.getSelectedItemPosition() * 122);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_analisis_matrix, container, false);
        this.spinnerTPS = (Spinner) this.view.findViewById(R.id.spinnerTPS);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.view.getContext(), 17367048, array);
        adapter.setDropDownViewResource(17367049);
        this.spinnerTPS.setAdapter(adapter);
        this.spinnerTPS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View v, int pos, long id) {
                if (Integer.valueOf(AnalisisPassData.cek_spinner).intValue() != pos) {
                    AnalisisPassData.cek_spinner = String.valueOf(pos);
                    String item = AnalisisMatrixFragment.this.spinnerTPS.getSelectedItem().toString();
                    AnalisisPassData.nilai_actual_tps = item + "%";
                    try {
                        AnalisisMatrixFragment.this.save_data_exit(AnalisisMatrixFragment.this.posisiSpinner * 122);
                        AnalisisPassData.AnaArray.clear();
                        for (int i = 1000; i < 1061; i++) {
                            TextView text = (TextView) AnalisisMatrixFragment.this.view.findViewById(i);
                            text.setText(item + "%");
                            text.setBackgroundColor(-1);
                        }
                        for (int i2 = 2; i2 < 366; i2 += 6) {
                            EditText edit = (EditText) AnalisisMatrixFragment.this.view.findViewById(i2);
                            edit.setBackgroundColor(-1);
                            edit.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                            ((CheckBox) AnalisisMatrixFragment.this.view.findViewById(i2 + 2)).setChecked(false);
                        }
                        AnalisisMatrixFragment.this.posisiSpinner = AnalisisMatrixFragment.this.spinnerTPS.getSelectedItemPosition();
                        int posisi = AnalisisMatrixFragment.this.posisiSpinner * 122;
                        for (int i3 = 2; i3 <= 366; i3 += 6) {
                            EditText edit2 = (EditText) AnalisisMatrixFragment.this.view.findViewById(i3);
                            edit2.setText(AnalisisPassData.list_analisis_backup.get(posisi));
                            if (!AnalisisPassData.list_analisis_backup.get(posisi).equals(AnalisisPassData.list_analisis_asli.get(posisi))) {
                                edit2.setTextColor(SupportMenu.CATEGORY_MASK);
                            }
                            ((TextView) AnalisisMatrixFragment.this.view.findViewById(i3 + 1)).setText(AnalisisPassData.list_analisis_backup.get(posisi + 1));
                            posisi += 2;
                        }
                        AnalisisMatrixFragment.this.hitung_analisis_advance(AnalisisMatrixFragment.this.view);
                    } catch (Exception e) {
                        Toast.makeText(AnalisisMatrixFragment.this.getActivity(), e.toString(), 0).show();
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        if (AnalisisPassData.masuk_awal.booleanValue()) {
            this.spinnerTPS.setSelection(20);
            AnalisisPassData.masuk_awal = false;
        }
        if (AnalisisPassData.AnaArray.size() > 0) {
            String positionTps = "100";
            ArrayList positionRpm = new ArrayList();
            Iterator<AnalisisParam> it = AnalisisPassData.AnaArray.iterator();
            while (it.hasNext()) {
                String[] pecah = it.next().toString().split(";");
                positionTps = pecah[0];
                positionRpm.add(pecah[1]);
            }
            int tempatTps = Integer.valueOf(positionTps.replace("%", "")).intValue();
            int taroTps = 0;
            if (tempatTps == 0) {
                taroTps = 0;
            } else if (tempatTps == 2) {
                taroTps = 1;
            } else if (tempatTps == 100) {
                taroTps = 20;
            } else if (tempatTps > 2 && tempatTps < 100) {
                taroTps = (tempatTps / 5) + 1;
            }
            this.spinnerTPS.setSelection(taroTps);
            for (int i = 0; i < positionRpm.size(); i++) {
                ((TextView) this.view.findViewById(((Integer.valueOf(positionRpm.get(i).toString()).intValue() / ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) - 4) + 1000)).setBackgroundColor(InputDeviceCompat.SOURCE_ANY);
            }
        }
        for (int i2 = 1000; i2 < 1061; i2++) {
            ((TextView) this.view.findViewById(i2)).setText(AnalisisPassData.nilai_actual_tps);
        }
        int data = Integer.valueOf(AnalisisPassData.nilai_actual_tps.replace("%", "")).intValue();
        int data_masuk = 0;
        if (data == 0) {
            data_masuk = 0;
        } else if (data == 2) {
            data_masuk = 1;
        } else if (data == 100) {
            data_masuk = 20;
        } else if (data > 2 && data < 100) {
            data_masuk = (data / 5) + 1;
        }
        int posisi = data_masuk * 122;
        for (int i3 = 2; i3 <= 366; i3 += 6) {
            EditText edit = (EditText) this.view.findViewById(i3);
            edit.setText(AnalisisPassData.list_analisis_backup.get(posisi));
            ((TextView) this.view.findViewById(i3 + 1)).setText(AnalisisPassData.list_analisis_backup.get(posisi + 1));
            if (!AnalisisPassData.list_analisis_backup.get(posisi).equals(AnalisisPassData.list_analisis_asli.get(posisi))) {
                edit.setTextColor(SupportMenu.CATEGORY_MASK);
            }
            posisi += 2;
        }
        if (Integer.valueOf(AnalisisPassData.maxIT).intValue() < 540) {
            Toast.makeText(getActivity(), "Invalid Maximum IT Value (<540)", 1).show();
        } else {
            hitung_analisis_advance(this.view);
        }
        ((ImageButton) this.view.findViewById(R.id.analisis_add_value)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnalisisMatrixFragment.this.add_value(AnalisisMatrixFragment.this.view);
            }
        });
        ((ImageButton) this.view.findViewById(R.id.analisis_set_value)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnalisisMatrixFragment.this.set_value(AnalisisMatrixFragment.this.view);
            }
        });
        ((ImageButton) this.view.findViewById(R.id.analisis_copy)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnalisisMatrixFragment.this.Copy(AnalisisMatrixFragment.this.view);
            }
        });
        ((ImageButton) this.view.findViewById(R.id.analisis_paste)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnalisisMatrixFragment.this.Paste(AnalisisMatrixFragment.this.view);
            }
        });
        ((ImageButton) this.view.findViewById(R.id.analisis_tambah_5)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnalisisMatrixFragment.this.plus_minus("plus", AnalisisMatrixFragment.this.view);
            }
        });
        ((ImageButton) this.view.findViewById(R.id.analisis_kurang_5)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AnalisisMatrixFragment.this.plus_minus("minus", AnalisisMatrixFragment.this.view);
            }
        });
        ((ImageButton) this.view.findViewById(R.id.analisis_auto_arrange)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int i = 2;
                while (i < 366) {
                    try {
                        TextView edit = (TextView) AnalisisMatrixFragment.this.view.findViewById(i + 3);
                        if (!edit.getText().toString().equals("-")) {
                            EditText ganti = (EditText) AnalisisMatrixFragment.this.view.findViewById(i);
                            ganti.setText(edit.getText().toString());
                            ganti.setTextColor(SupportMenu.CATEGORY_MASK);
                        }
                        i += 6;
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        });
        return this.view;
    }

    /* access modifiers changed from: private */
    public void save_data_exit(int posisi) {
        for (int i = 2; i <= 366; i += 6) {
            AnalisisPassData.list_analisis_backup.remove(posisi);
            AnalisisPassData.list_analisis_backup.add(posisi, ((EditText) this.view.findViewById(i)).getText().toString());
            AnalisisPassData.list_analisis_backup.remove(posisi + 1);
            AnalisisPassData.list_analisis_backup.add(posisi + 1, ((TextView) this.view.findViewById(i + 1)).getText().toString());
            posisi += 2;
        }
    }

    /* access modifiers changed from: private */
    public void hitung_analisis_advance(View v) {
        double batas_akhir_total = Double.valueOf(AnalisisPassData.maxIT).doubleValue();
        int posisi = 1;
        for (int i = 0; i < 61; i++) {
            EditText editIT = (EditText) v.findViewById(posisi + 1);
            double value_bm = Double.valueOf(((TextView) v.findViewById(posisi + 2)).getText().toString()).doubleValue();
            double waktu = 30000.0d / (180.0d * Double.valueOf(((TextView) v.findViewById(posisi)).getText().toString()).doubleValue());
            double batas_akhir_maksimal = batas_akhir_total * waktu;
            double nilai_injector = 0.0d;
            int pembulatan = -1;
            if ((value_bm / waktu) + Double.valueOf(editIT.getText().toString()).doubleValue() > batas_akhir_total) {
                int it_int = (int) (batas_akhir_total - (value_bm / waktu));
                pembulatan = it_int - (it_int % 5);
                if (pembulatan <= 0) {
                    if (Integer.valueOf(editIT.getText().toString()).intValue() == 0) {
                        pembulatan = -1;
                    } else {
                        pembulatan = 0;
                    }
                    nilai_injector = (value_bm * Double.valueOf(AnalisisPassData.flow_inj).doubleValue()) / batas_akhir_maksimal;
                }
            }
            TextView hasil = (TextView) v.findViewById(posisi + 4);
            if (pembulatan == -1) {
                hasil.setText("-");
            } else {
                hasil.setText(String.valueOf(pembulatan));
            }
            TextView hasil1 = (TextView) v.findViewById(posisi + 5);
            if (nilai_injector == 0.0d) {
                hasil1.setText("-");
            } else {
                hasil1.setText("Replace injector-1 with min " + String.format("%.2f", new Object[]{Double.valueOf(nilai_injector)}).replace(",", ".") + " cc");
                AnalisisPassData.min_injector_1 = String.format("%.2f", new Object[]{Double.valueOf(nilai_injector)}).replace(",", ".");
            }
            posisi += 6;
        }
    }

    /* access modifiers changed from: private */
    public void add_value(final View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle((CharSequence) "Add Value");
        alert.setMessage((CharSequence) "Enter Your Value Here");
        final EditText inputNilaiAdd = new EditText(getContext());
        inputNilaiAdd.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        inputNilaiAdd.setFilters(new InputFilter[]{new range_int1("0", "720")});
        alert.setView((View) inputNilaiAdd);
        alert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AnalisisMatrixFragment.this.AddValue(inputNilaiAdd.getEditableText().toString(), v);
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
    public void set_value(final View v) {
        new AlertDialog.Builder(getContext());
        AlertDialog.Builder alert_set = new AlertDialog.Builder(getContext());
        alert_set.setTitle((CharSequence) "Set Value");
        alert_set.setMessage((CharSequence) "Enter Your Value Here");
        final EditText inputNilai = new EditText(getContext());
        inputNilai.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        inputNilai.setFilters(new InputFilter[]{new range_int1("0", "720")});
        alert_set.setView((View) inputNilai);
        alert_set.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AnalisisMatrixFragment.this.SetValue(inputNilai.getEditableText().toString(), v);
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

    public void AddValue(String Value, View view2) {
        double nilai;
        for (int i = 2; i < 366; i += 6) {
            EditText v = (EditText) view2.findViewById(i);
            String str = v.getText().toString();
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                if (str.length() != 0) {
                    nilai = Double.parseDouble(v.getText().toString());
                } else {
                    nilai = Double.parseDouble("0");
                }
                double nilai2 = nilai + Double.parseDouble(Value);
                if (nilai2 > 720.0d) {
                    nilai2 = 720.0d;
                } else if (nilai2 < 0.0d) {
                    nilai2 = 0.0d;
                }
                v.setText(String.valueOf(Math.round(nilai2)));
            }
        }
    }

    public void SetValue(String Value, View view2) {
        for (int i = 2; i < 366; i += 6) {
            EditText v = (EditText) view2.findViewById(i);
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                v.setText(Value);
            }
        }
    }

    /* access modifiers changed from: private */
    public void plus_minus(String pilihan, View view2) {
        double nilai;
        for (int i = 2; i <= 366; i += 6) {
            EditText v = (EditText) view2.findViewById(i);
            String str = v.getText().toString();
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                if (str.length() != 0) {
                    nilai = Double.parseDouble(v.getText().toString());
                } else {
                    nilai = Double.parseDouble("0");
                }
                if (pilihan.equals("plus")) {
                    nilai += 5.0d;
                } else if (pilihan.equals("minus")) {
                    nilai -= 5.0d;
                }
                if (nilai > 720.0d) {
                    nilai = 720.0d;
                } else if (nilai < 0.0d) {
                    nilai = 0.0d;
                }
                v.setText(String.valueOf(Math.round(nilai)));
            }
        }
    }

    public void Copy(View view2) {
        this.status_flag = 1;
        if (StaticClass.letak.size() != 0) {
            StaticClass.data.clear();
            StaticClass.letak.clear();
        }
        for (int i = 2; i <= 366; i += 6) {
            EditText v = (EditText) view2.findViewById(i);
            String str = v.getText().toString();
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                StaticClass.data.add(str);
                StaticClass.letak.add(Integer.valueOf(i));
                this.jumlah++;
            }
        }
        this.maksimum = this.jumlah;
        this.jumlah = 0;
        Toast.makeText(getActivity(), "copied", 0).show();
    }

    public void Paste(View view2) {
        if (this.status_flag == 1) {
            this.status_flag = 1;
            int nilai_acuan = StaticClass.letak.get(0).intValue();
            for (int i = 2; i <= 366; i += 6) {
                EditText v = (EditText) view2.findViewById(i);
                if (v.getTag() != null && v.getTag().equals("Warna BLUE")) {
                    for (int k = 0; k < this.maksimum; k++) {
                        String data_ditulis = StaticClass.data.get(k);
                        int letakan = i + (StaticClass.letak.get(k).intValue() - nilai_acuan);
                        int tanda = mod(this.pengingat, 21);
                        this.pengingat = letakan;
                        if (letakan <= 1281 && tanda != 0) {
                            ((EditText) view2.findViewById(letakan)).setText(data_ditulis);
                        }
                    }
                    StaticClass.position.add(Integer.valueOf(i));
                }
            }
        }
    }

    private int mod(int x, int y) {
        int result = x % y;
        if (result < 0) {
            return result + y;
        }
        return result;
    }
}
