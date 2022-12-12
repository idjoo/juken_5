package juken.android.com.juken_5;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class fuel_calculator extends AppCompatActivity {
    EditText debit_pengganti;
    EditText debit_terpasang;
    TextView et_debit_injector_berkurang;
    TextView et_debit_injector_bertambah;
    EditText et_diameter_debit_awal;
    EditText et_diameter_debit_menjadi;
    TextView et_estimasi_koreksi_fuel;
    TextView et_kapasitas_mesin_awal;
    TextView et_kapasitas_mesin_menjadi;
    EditText et_langkah_mesin_awal;
    EditText et_langkah_mesin_menjadi;
    TextView et_perubahan_kapasitas_mesin;
    Button exit_calculator;
    CheckBox ganti_bore_up;
    CheckBox ganti_injector;
    Boolean lolos = true;
    int nilai_ecu_sebelumnya = 0;
    Button save_calculator;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.fuel_calculator);
        this.save_calculator = (Button) findViewById(R.id.save_fuel_calc);
        this.save_calculator.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String sudah_pernah_ecu = PreferenceManager.getDefaultSharedPreferences(fuel_calculator.this.getApplicationContext()).getString("sudah_pernah_ecu", "sudah_pernah_ecu1");
                fuel_calculator.this.lolos = true;
                if (sudah_pernah_ecu.equals("1")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(fuel_calculator.this);
                    alert.setTitle((CharSequence) "Alert!");
                    alert.setMessage((CharSequence) fuel_calculator.this.getString(R.string.FuelCalculatorSebelumnya));
                    alert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            int data_masukin = Integer.valueOf(fuel_calculator.this.et_estimasi_koreksi_fuel.getText().toString()).intValue();
                            int i = 0;
                            while (true) {
                                if (i >= MappingHandle.list_fuel.size()) {
                                    break;
                                } else if ((Integer.valueOf(MappingHandle.list_fuel.get(i)).intValue() - fuel_calculator.this.nilai_ecu_sebelumnya) + data_masukin < -90) {
                                    fuel_calculator.this.lolos = false;
                                    break;
                                } else {
                                    i++;
                                }
                            }
                            if (fuel_calculator.this.lolos.booleanValue()) {
                                MappingHandle.list_fuel_calculator.clear();
                                for (int i2 = 0; i2 < MappingHandle.list_fuel.size(); i2++) {
                                    int nilai_akhir = (Integer.valueOf(MappingHandle.list_fuel.get(i2)).intValue() - fuel_calculator.this.nilai_ecu_sebelumnya) + data_masukin;
                                    if (nilai_akhir > 100) {
                                        nilai_akhir = 100;
                                    }
                                    MappingHandle.list_fuel_calculator.add(String.valueOf(nilai_akhir));
                                }
                                fuel_calculator.this.SavePreferences("nilai_ecu_sebelumnya", String.valueOf(data_masukin));
                                fuel_calculator.this.SavePreferences("sudah_pernah_ecu", "0");
                                fuel_calculator.this.startActivityForResult(new Intent(fuel_calculator.this.getApplicationContext(), fuel_calc_sementara.class), 0);
                                return;
                            }
                            Toast.makeText(fuel_calculator.this.getApplicationContext(), fuel_calculator.this.getString(R.string.FuelCalculatorNilai), 0).show();
                        }
                    });
                    alert.setNegativeButton((CharSequence) "CANCEL", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            int data_masukin = Integer.valueOf(fuel_calculator.this.et_estimasi_koreksi_fuel.getText().toString()).intValue();
                            int i = 0;
                            while (true) {
                                if (i >= MappingHandle.list_fuel.size()) {
                                    break;
                                } else if (Integer.valueOf(MappingHandle.list_fuel.get(i)).intValue() + data_masukin < -90) {
                                    fuel_calculator.this.lolos = false;
                                    break;
                                } else {
                                    i++;
                                }
                            }
                            if (fuel_calculator.this.lolos.booleanValue()) {
                                MappingHandle.list_fuel_calculator.clear();
                                for (int i2 = 0; i2 < MappingHandle.list_fuel.size(); i2++) {
                                    int nilai_akhir = Integer.valueOf(MappingHandle.list_fuel.get(i2)).intValue() + data_masukin;
                                    if (nilai_akhir > 100) {
                                        nilai_akhir = 100;
                                    }
                                    MappingHandle.list_fuel_calculator.add(String.valueOf(nilai_akhir));
                                }
                                fuel_calculator.this.SavePreferences("nilai_ecu_sebelumnya", String.valueOf(data_masukin));
                                fuel_calculator.this.startActivityForResult(new Intent(fuel_calculator.this.getApplicationContext(), fuel_calc_sementara.class), 0);
                                return;
                            }
                            Toast.makeText(fuel_calculator.this.getApplicationContext(), fuel_calculator.this.getString(R.string.FuelCalculatorNilai), 0).show();
                        }
                    });
                    alert.create().show();
                    return;
                }
                int data_masukin = Integer.valueOf(fuel_calculator.this.et_estimasi_koreksi_fuel.getText().toString()).intValue();
                int i = 0;
                while (true) {
                    if (i >= MappingHandle.list_fuel.size()) {
                        break;
                    } else if (Integer.valueOf(MappingHandle.list_fuel.get(i)).intValue() + data_masukin < -90) {
                        fuel_calculator.this.lolos = false;
                        break;
                    } else {
                        i++;
                    }
                }
                if (fuel_calculator.this.lolos.booleanValue()) {
                    MappingHandle.list_fuel_calculator.clear();
                    for (int i2 = 0; i2 < MappingHandle.list_fuel.size(); i2++) {
                        int nilai_akhir = Integer.valueOf(MappingHandle.list_fuel.get(i2)).intValue() + data_masukin;
                        if (nilai_akhir > 100) {
                            nilai_akhir = 100;
                        }
                        MappingHandle.list_fuel_calculator.add(String.valueOf(nilai_akhir));
                    }
                    fuel_calculator.this.SavePreferences("nilai_ecu_sebelumnya", String.valueOf(data_masukin));
                    fuel_calculator.this.startActivityForResult(new Intent(fuel_calculator.this.getApplicationContext(), fuel_calc_sementara.class), 0);
                    return;
                }
                Toast.makeText(fuel_calculator.this.getApplicationContext(), fuel_calculator.this.getString(R.string.FuelCalculatorNilai), 0).show();
            }
        });
        this.exit_calculator = (Button) findViewById(R.id.exit_fuel_calc);
        this.exit_calculator.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_calculator.this.finish();
            }
        });
        this.et_kapasitas_mesin_awal = (TextView) findViewById(R.id.et_kapasitas_mesin_awal);
        this.et_kapasitas_mesin_menjadi = (TextView) findViewById(R.id.et_kapasitas_mesin_menjadi);
        this.et_debit_injector_bertambah = (TextView) findViewById(R.id.et_debit_injector_bertambah);
        this.et_debit_injector_bertambah.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                fuel_calculator.this.isi_estimasi_fuel();
            }
        });
        this.et_debit_injector_berkurang = (TextView) findViewById(R.id.et_debit_injector_berkurang);
        this.et_debit_injector_berkurang.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                fuel_calculator.this.isi_estimasi_fuel();
            }
        });
        this.et_estimasi_koreksi_fuel = (TextView) findViewById(R.id.et_estimasi_koreksi_fuel);
        this.et_perubahan_kapasitas_mesin = (TextView) findViewById(R.id.et_perubahan_kapasitas_mesin);
        this.et_perubahan_kapasitas_mesin.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                fuel_calculator.this.isi_estimasi_fuel();
            }
        });
        this.debit_terpasang = (EditText) findViewById(R.id.et_debit_terpasang);
        this.debit_terpasang.setFilters(new InputFilter[]{new range_int1("0", "300")});
        this.debit_terpasang.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fuel_calculator.this.hitung_debit_injector();
            }

            public void afterTextChanged(Editable s) {
            }
        });
        this.debit_pengganti = (EditText) findViewById(R.id.et_debit_pengganti);
        this.debit_pengganti.setFilters(new InputFilter[]{new range_int1("0", "300")});
        this.debit_pengganti.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fuel_calculator.this.hitung_debit_injector();
            }

            public void afterTextChanged(Editable s) {
            }
        });
        this.et_diameter_debit_awal = (EditText) findViewById(R.id.et_diameter_debit_awal);
        this.et_diameter_debit_awal.setFilters(new InputFilter[]{new range_float2(1)});
        this.et_diameter_debit_awal.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                fuel_calculator.this.hitung_displacemet_lama();
            }
        });
        this.et_diameter_debit_awal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    float val = Float.parseFloat(fuel_calculator.this.et_diameter_debit_awal.getText().toString());
                    if (val <= 100.0f && val >= 0.0f) {
                        fuel_calculator.this.et_diameter_debit_awal.setText(String.valueOf(val));
                    } else if (val > 100.0f) {
                        fuel_calculator.this.et_diameter_debit_awal.setText("100.0");
                    } else if (val < 0.0f) {
                        fuel_calculator.this.et_diameter_debit_awal.setText("0.0");
                    }
                } catch (NumberFormatException e) {
                    fuel_calculator.this.et_diameter_debit_awal.setText("0.0");
                } catch (NullPointerException e2) {
                    fuel_calculator.this.et_diameter_debit_awal.setText("0.0");
                }
            }
        });
        this.et_diameter_debit_menjadi = (EditText) findViewById(R.id.et_diameter_debit_menjadi);
        this.et_diameter_debit_menjadi.setFilters(new InputFilter[]{new range_float2(1)});
        this.et_diameter_debit_menjadi.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                fuel_calculator.this.hitung_displacemet_baru();
            }
        });
        this.et_diameter_debit_menjadi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    float val = Float.parseFloat(fuel_calculator.this.et_diameter_debit_menjadi.getText().toString());
                    if (val <= 100.0f && val >= 0.0f) {
                        fuel_calculator.this.et_diameter_debit_menjadi.setText(String.valueOf(val));
                    } else if (val > 100.0f) {
                        fuel_calculator.this.et_diameter_debit_menjadi.setText("100.0");
                    } else if (val < 0.0f) {
                        fuel_calculator.this.et_diameter_debit_menjadi.setText("0.0");
                    }
                } catch (NumberFormatException e) {
                    fuel_calculator.this.et_diameter_debit_menjadi.setText("0.0");
                } catch (NullPointerException e2) {
                    fuel_calculator.this.et_diameter_debit_menjadi.setText("0.0");
                }
            }
        });
        this.et_langkah_mesin_awal = (EditText) findViewById(R.id.et_langkah_mesin_awal);
        this.et_langkah_mesin_awal.setFilters(new InputFilter[]{new range_float2(1)});
        this.et_langkah_mesin_awal.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                fuel_calculator.this.hitung_displacemet_lama();
            }
        });
        this.et_langkah_mesin_awal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    float val = Float.parseFloat(fuel_calculator.this.et_langkah_mesin_awal.getText().toString());
                    if (val <= 100.0f && val >= 0.0f) {
                        fuel_calculator.this.et_langkah_mesin_awal.setText(String.valueOf(val));
                    } else if (val > 100.0f) {
                        fuel_calculator.this.et_langkah_mesin_awal.setText("100.0");
                    } else if (val < 0.0f) {
                        fuel_calculator.this.et_langkah_mesin_awal.setText("0.0");
                    }
                } catch (NumberFormatException e) {
                    fuel_calculator.this.et_langkah_mesin_awal.setText("0.0");
                } catch (NullPointerException e2) {
                    fuel_calculator.this.et_langkah_mesin_awal.setText("0.0");
                }
            }
        });
        this.et_langkah_mesin_menjadi = (EditText) findViewById(R.id.et_langkah_mesin_menjadi);
        this.et_langkah_mesin_menjadi.setFilters(new InputFilter[]{new range_float2(1)});
        this.et_langkah_mesin_menjadi.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                fuel_calculator.this.hitung_displacemet_baru();
            }
        });
        this.et_langkah_mesin_menjadi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    float val = Float.parseFloat(fuel_calculator.this.et_langkah_mesin_menjadi.getText().toString());
                    if (val <= 100.0f && val >= 0.0f) {
                        fuel_calculator.this.et_langkah_mesin_menjadi.setText(String.valueOf(val));
                    } else if (val > 100.0f) {
                        fuel_calculator.this.et_langkah_mesin_menjadi.setText("100.0");
                    } else if (val < 0.0f) {
                        fuel_calculator.this.et_langkah_mesin_menjadi.setText("0.0");
                    }
                } catch (NumberFormatException e) {
                    fuel_calculator.this.et_langkah_mesin_menjadi.setText("0.0");
                } catch (NullPointerException e2) {
                    fuel_calculator.this.et_langkah_mesin_menjadi.setText("0.0");
                }
            }
        });
        this.ganti_injector = (CheckBox) findViewById(R.id.checkbox_ganti_injector);
        this.ganti_injector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fuel_calculator.this.ganti_injector.isChecked()) {
                    fuel_calculator.this.ganti_injector.setText("ON");
                    fuel_calculator.this.debit_terpasang.setEnabled(true);
                    fuel_calculator.this.debit_pengganti.setEnabled(true);
                    return;
                }
                fuel_calculator.this.ganti_injector.setText("OFF");
                fuel_calculator.this.debit_terpasang.setEnabled(false);
                fuel_calculator.this.debit_pengganti.setEnabled(false);
            }
        });
        this.ganti_bore_up = (CheckBox) findViewById(R.id.checkbox_bore_up);
        this.ganti_bore_up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fuel_calculator.this.ganti_bore_up.isChecked()) {
                    fuel_calculator.this.ganti_bore_up.setText("ON");
                    fuel_calculator.this.et_diameter_debit_awal.setEnabled(true);
                    fuel_calculator.this.et_diameter_debit_menjadi.setEnabled(true);
                    fuel_calculator.this.et_langkah_mesin_awal.setEnabled(true);
                    fuel_calculator.this.et_langkah_mesin_menjadi.setEnabled(true);
                    fuel_calculator.this.et_kapasitas_mesin_awal.setEnabled(true);
                    fuel_calculator.this.et_kapasitas_mesin_menjadi.setEnabled(true);
                    return;
                }
                fuel_calculator.this.ganti_bore_up.setText("OFF");
                fuel_calculator.this.et_diameter_debit_awal.setEnabled(false);
                fuel_calculator.this.et_diameter_debit_menjadi.setEnabled(false);
                fuel_calculator.this.et_langkah_mesin_awal.setEnabled(false);
                fuel_calculator.this.et_langkah_mesin_menjadi.setEnabled(false);
                fuel_calculator.this.et_kapasitas_mesin_awal.setEnabled(false);
                fuel_calculator.this.et_kapasitas_mesin_menjadi.setEnabled(false);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.nilai_ecu_sebelumnya = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("nilai_ecu_sebelumnya", "nilai_ecu_sebelumnya1")).intValue();
    }

    /* access modifiers changed from: private */
    public void hitung_debit_injector() {
        try {
            int debit_awal = Integer.valueOf(this.debit_terpasang.getText().toString()).intValue();
            int penambah = ((Integer.valueOf(this.debit_pengganti.getText().toString()).intValue() - debit_awal) * 100) / debit_awal;
            if (penambah >= 0) {
                this.et_debit_injector_bertambah.setText(String.valueOf(penambah));
                this.et_debit_injector_berkurang.setText("0");
            } else if (penambah < 0) {
                this.et_debit_injector_berkurang.setText(String.valueOf(penambah));
                this.et_debit_injector_bertambah.setText("0");
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public void hitung_displacemet_lama() {
        try {
            double stroke = Double.valueOf(this.et_langkah_mesin_awal.getText().toString()).doubleValue();
            double diameter_piston = Double.valueOf(this.et_diameter_debit_awal.getText().toString()).doubleValue();
            this.et_kapasitas_mesin_awal.setText(String.format("%.2f", new Object[]{Double.valueOf((((22.0d * diameter_piston) * diameter_piston) * stroke) / 28000.0d)}).replace(",", "."));
            hitung_perubahan_kapasitas_mesin();
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public void hitung_displacemet_baru() {
        try {
            double stroke = Double.valueOf(this.et_langkah_mesin_menjadi.getText().toString()).doubleValue();
            double diameter_piston = Double.valueOf(this.et_diameter_debit_menjadi.getText().toString()).doubleValue();
            this.et_kapasitas_mesin_menjadi.setText(String.format("%.2f", new Object[]{Double.valueOf((((22.0d * diameter_piston) * diameter_piston) * stroke) / 28000.0d)}).replace(",", "."));
            hitung_perubahan_kapasitas_mesin();
        } catch (Exception e) {
        }
    }

    private void hitung_perubahan_kapasitas_mesin() {
        try {
            double nilai_baru = Double.valueOf(this.et_kapasitas_mesin_menjadi.getText().toString()).doubleValue();
            double nilai_lama = Double.valueOf(this.et_kapasitas_mesin_awal.getText().toString()).doubleValue();
            if (nilai_baru < 1.0d || nilai_lama < 1.0d) {
                this.et_perubahan_kapasitas_mesin.setText("0");
            } else {
                this.et_perubahan_kapasitas_mesin.setText(String.valueOf((int) (((nilai_baru - nilai_lama) * 100.0d) / nilai_lama)).replace(",", "."));
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public void isi_estimasi_fuel() {
        int hasil_akhir;
        try {
            int koreksi_tambah_fuel = Integer.valueOf(this.et_debit_injector_bertambah.getText().toString()).intValue();
            int koreksi_kurang_fuel = Integer.valueOf(this.et_debit_injector_berkurang.getText().toString()).intValue();
            int koreksi_mesin = Integer.valueOf(this.et_perubahan_kapasitas_mesin.getText().toString()).intValue();
            if (koreksi_tambah_fuel == 0) {
                hasil_akhir = (koreksi_kurang_fuel * -1) + koreksi_mesin;
            } else if (koreksi_kurang_fuel == 0) {
                hasil_akhir = (koreksi_tambah_fuel * -1) + koreksi_mesin;
            } else {
                hasil_akhir = koreksi_mesin;
            }
            this.et_estimasi_koreksi_fuel.setText(String.valueOf(hasil_akhir));
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }
}
