package juken.android.com.juken_5.folder_analisis;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import juken.android.com.juken_5.R;
import juken.android.com.juken_5.range_int1;
import juken.android.com.juken_5.range_int_min;

public class AnalisisParameterFragment extends Fragment {
    EditText ana_close_intake_nilai;
    EditText ana_close_nilai;
    TextView ana_duration_intake_nilai;
    TextView ana_duration_nilai;
    TextView ana_lca_intake_nilai;
    TextView ana_lca_nilai;
    EditText ana_open_intake_nilai;
    EditText ana_open_nilai;
    int maksimum = 540;
    EditText maxIT;

    public static AnalisisParameterFragment newInstance(String param1, String param2) {
        AnalisisParameterFragment fragment = new AnalisisParameterFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analisis_parameter, container, false);
        String nilai = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("turbo_en", "turbo_en1");
        TextView status_inj2 = (TextView) view.findViewById(R.id.status_injector_2);
        if (nilai.equals("1")) {
            status_inj2.setText("On");
        }
        ((TextView) view.findViewById(R.id.et_injector_1_min)).setText(AnalisisPassData.min_injector_1);
        ((TextView) view.findViewById(R.id.et_injector_2_min)).setText(AnalisisPassData.min_injector_2);
        this.ana_duration_nilai = (TextView) view.findViewById(R.id.et_duration_exhaust);
        this.ana_duration_intake_nilai = (TextView) view.findViewById(R.id.et_duration_intake);
        this.ana_lca_nilai = (TextView) view.findViewById(R.id.et_lca_exhaust);
        this.ana_lca_intake_nilai = (TextView) view.findViewById(R.id.et_lca_intake);
        this.ana_open_nilai = (EditText) view.findViewById(R.id.et_open_exhaust);
        this.ana_open_nilai.setFilters(new InputFilter[]{new range_int1(0, 80)});
        this.ana_open_nilai.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                try {
                    AnalisisPassData.open_exhaust = AnalisisParameterFragment.this.ana_open_nilai.getText().toString();
                    AnalisisParameterFragment.this.hitung_duration_exhaust();
                    AnalisisParameterFragment.this.hitung_lca_exhaust();
                } catch (Exception e) {
                }
            }
        });
        this.ana_open_intake_nilai = (EditText) view.findViewById(R.id.et_open_intake);
        this.ana_open_intake_nilai.setFilters(new InputFilter[]{new range_int_min(-10, 50)});
        this.ana_open_intake_nilai.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                try {
                    AnalisisPassData.open_intake = AnalisisParameterFragment.this.ana_open_intake_nilai.getText().toString();
                    AnalisisParameterFragment.this.hitung_duration_intake();
                    AnalisisParameterFragment.this.hitung_lca_intake();
                } catch (Exception e) {
                }
            }
        });
        this.ana_close_nilai = (EditText) view.findViewById(R.id.et_close_exhaust);
        this.ana_close_nilai.setFilters(new InputFilter[]{new range_int_min(-10, 50)});
        this.ana_close_nilai.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                try {
                    AnalisisPassData.close_exhaust = AnalisisParameterFragment.this.ana_close_nilai.getText().toString();
                    AnalisisParameterFragment.this.hitung_duration_exhaust();
                    AnalisisParameterFragment.this.hitung_lca_exhaust();
                } catch (Exception e) {
                }
            }
        });
        this.ana_close_intake_nilai = (EditText) view.findViewById(R.id.et_close_intake);
        this.ana_close_intake_nilai.setFilters(new InputFilter[]{new range_int1(0, 70)});
        this.ana_close_intake_nilai.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                try {
                    AnalisisPassData.close_intake = AnalisisParameterFragment.this.ana_close_intake_nilai.getText().toString();
                    AnalisisParameterFragment.this.hitung_duration_intake();
                    AnalisisParameterFragment.this.hitung_lca_intake();
                    AnalisisParameterFragment.this.maksimum = (Integer.valueOf(AnalisisParameterFragment.this.ana_close_intake_nilai.getText().toString()).intValue() + 540) - 20;
                    if (AnalisisParameterFragment.this.maksimum < 540) {
                        AnalisisParameterFragment.this.maksimum = 540;
                    }
                    AnalisisParameterFragment.this.maxIT.setFilters(new InputFilter[]{new range_int1(0, AnalisisParameterFragment.this.maksimum)});
                } catch (Exception e) {
                }
            }
        });
        this.maxIT = (EditText) view.findViewById(R.id.et_max_limit_it);
        this.maksimum = (Integer.valueOf(this.ana_close_intake_nilai.getText().toString()).intValue() + 540) - 20;
        if (this.maksimum < 540) {
            this.maksimum = 540;
        }
        AnalisisPassData.maxIT = this.maxIT.getText().toString();
        this.maxIT.setFilters(new InputFilter[]{new range_int1(0, this.maksimum)});
        this.maxIT.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                try {
                    int cek = Integer.valueOf(AnalisisParameterFragment.this.maxIT.getText().toString()).intValue();
                    if (cek <= 100) {
                        return;
                    }
                    if (cek <= AnalisisParameterFragment.this.maksimum && cek >= 540) {
                        AnalisisPassData.maxIT = AnalisisParameterFragment.this.maxIT.getText().toString();
                    } else if (cek > AnalisisParameterFragment.this.maksimum) {
                        AnalisisPassData.maxIT = String.valueOf(AnalisisParameterFragment.this.maksimum);
                        AnalisisParameterFragment.this.maxIT.setText(String.valueOf(AnalisisParameterFragment.this.maksimum));
                    } else if (cek < 540) {
                        AnalisisPassData.maxIT = "540";
                        AnalisisParameterFragment.this.maxIT.setText("540");
                    }
                } catch (Exception e) {
                }
            }
        });
        final EditText ana_flow_rate = (EditText) view.findViewById(R.id.et_injector_1);
        ana_flow_rate.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                AnalisisPassData.flow_inj = ana_flow_rate.getText().toString();
            }
        });
        AnalisisPassData.open_exhaust = this.ana_open_nilai.getText().toString();
        AnalisisPassData.open_intake = this.ana_open_intake_nilai.getText().toString();
        AnalisisPassData.close_exhaust = this.ana_close_nilai.getText().toString();
        AnalisisPassData.close_intake = this.ana_close_intake_nilai.getText().toString();
        return view;
    }

    /* access modifiers changed from: private */
    public void hitung_duration_exhaust() {
        this.ana_duration_nilai.setText(String.valueOf(Integer.valueOf(this.ana_open_nilai.getText().toString()).intValue() + Integer.valueOf(this.ana_close_nilai.getText().toString()).intValue() + 180));
    }

    /* access modifiers changed from: private */
    public void hitung_lca_exhaust() {
        double open = Double.valueOf(this.ana_open_nilai.getText().toString()).doubleValue();
        double close = Double.valueOf(this.ana_close_nilai.getText().toString()).doubleValue();
        this.ana_lca_nilai.setText(String.valueOf((((open + close) + 180.0d) / 2.0d) - close));
    }

    /* access modifiers changed from: private */
    public void hitung_duration_intake() {
        this.ana_duration_intake_nilai.setText(String.valueOf(Integer.valueOf(this.ana_open_intake_nilai.getText().toString()).intValue() + Integer.valueOf(this.ana_close_intake_nilai.getText().toString()).intValue() + 180));
    }

    /* access modifiers changed from: private */
    public void hitung_lca_intake() {
        double open = Double.valueOf(this.ana_open_intake_nilai.getText().toString()).doubleValue();
        double close = Double.valueOf(this.ana_close_intake_nilai.getText().toString()).doubleValue();
        this.ana_lca_intake_nilai.setText(String.valueOf((((open + close) + 180.0d) / 2.0d) - close));
    }
}
