package juken.android.com.juken_5.folder_fuel;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.util.ArrayList;
import java.util.List;
import juken.android.com.juken_5.MappingHandle;
import juken.android.com.juken_5.R;
import juken.android.com.juken_5.main_menu;
import juken.android.com.juken_5.range_int_min;

public class imp_fuel extends AppCompatActivity {
    private static final String[] array = {"0", "2", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80", "85", "90", "100"};
    String[] arr1_fuel_file = new String[7];
    Boolean boleh_geser1_fuel_file = false;
    CheckBox delapanlima;
    CheckBox delapanpuluh;
    CheckBox dua;
    CheckBox dualima;
    CheckBox duapuluh;
    Spinner edit_tps;
    CheckBox empatlima;
    CheckBox empatpuluh;
    CheckBox enamlima;
    CheckBox enampuluh;
    int hitung1_fuel_file = 0;
    int jumlah = 0;
    CheckBox lima;
    CheckBox limabelas;
    CheckBox limalima;
    CheckBox limapuluh;
    ArrayList list1 = new ArrayList();
    private LineChart mChart;
    int maksimum = 0;
    CheckBox nol;
    int pengingat = 1;
    int pilih1_fuel_file = 0;
    CheckBox sembilanpuluh;
    CheckBox sepuluh;
    CheckBox seratus;
    LineDataSet set1;
    LineDataSet set10;
    LineDataSet set11;
    LineDataSet set12;
    LineDataSet set13;
    LineDataSet set14;
    LineDataSet set15;
    LineDataSet set16;
    LineDataSet set17;
    LineDataSet set18;
    LineDataSet set19;
    LineDataSet set2;
    LineDataSet set20;
    LineDataSet set21;
    LineDataSet set3;
    LineDataSet set4;
    LineDataSet set5;
    LineDataSet set6;
    LineDataSet set7;
    LineDataSet set8;
    LineDataSet set9;
    int status_flag = 0;
    CheckBox tigalima;
    CheckBox tigapuluh;
    CheckBox tujuhlima;
    CheckBox tujuhpuluh;
    ArrayList<Entry> yVals1 = new ArrayList<>();
    ArrayList<Entry> yVals10 = new ArrayList<>();
    ArrayList<Entry> yVals11 = new ArrayList<>();
    ArrayList<Entry> yVals12 = new ArrayList<>();
    ArrayList<Entry> yVals13 = new ArrayList<>();
    ArrayList<Entry> yVals14 = new ArrayList<>();
    ArrayList<Entry> yVals15 = new ArrayList<>();
    ArrayList<Entry> yVals16 = new ArrayList<>();
    ArrayList<Entry> yVals17 = new ArrayList<>();
    ArrayList<Entry> yVals18 = new ArrayList<>();
    ArrayList<Entry> yVals19 = new ArrayList<>();
    ArrayList<Entry> yVals2 = new ArrayList<>();
    ArrayList<Entry> yVals20 = new ArrayList<>();
    ArrayList<Entry> yVals21 = new ArrayList<>();
    ArrayList<Entry> yVals3 = new ArrayList<>();
    ArrayList<Entry> yVals4 = new ArrayList<>();
    ArrayList<Entry> yVals5 = new ArrayList<>();
    ArrayList<Entry> yVals6 = new ArrayList<>();
    ArrayList<Entry> yVals7 = new ArrayList<>();
    ArrayList<Entry> yVals8 = new ArrayList<>();
    ArrayList<Entry> yVals9 = new ArrayList<>();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        String newString;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imp_fuel);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newString = null;
            } else {
                newString = extras.getString("posisi");
            }
        } else {
            newString = (String) savedInstanceState.getSerializable("posisi");
        }
        this.nol = (CheckBox) findViewById(R.id.cb_nol);
        this.nol.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.nol, 0, "0");
            }
        });
        this.dua = (CheckBox) findViewById(R.id.cb_dua);
        this.dua.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.dua, 1, "2");
            }
        });
        this.lima = (CheckBox) findViewById(R.id.cb_lima);
        this.lima.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.lima, 2, "5");
            }
        });
        this.sepuluh = (CheckBox) findViewById(R.id.cb_sepuluh);
        this.sepuluh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.sepuluh, 3, "10");
            }
        });
        this.limabelas = (CheckBox) findViewById(R.id.cb_limabelas);
        this.limabelas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.limabelas, 4, "15");
            }
        });
        this.duapuluh = (CheckBox) findViewById(R.id.cb_duapuluh);
        this.duapuluh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.duapuluh, 5, "20");
            }
        });
        this.dualima = (CheckBox) findViewById(R.id.cb_dualima);
        this.dualima.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.dualima, 6, "25");
            }
        });
        this.tigapuluh = (CheckBox) findViewById(R.id.cb_tigapuluh);
        this.tigapuluh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.tigapuluh, 7, "30");
            }
        });
        this.tigalima = (CheckBox) findViewById(R.id.cb_tigalima);
        this.tigalima.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.tigalima, 8, "35");
            }
        });
        this.empatpuluh = (CheckBox) findViewById(R.id.cb_empatpuluh);
        this.empatpuluh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.empatpuluh, 9, "40");
            }
        });
        this.empatlima = (CheckBox) findViewById(R.id.cb_empatlima);
        this.empatlima.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.empatlima, 10, "45");
            }
        });
        this.limapuluh = (CheckBox) findViewById(R.id.cb_limapuluh);
        this.limapuluh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.limapuluh, 11, "50");
            }
        });
        this.limalima = (CheckBox) findViewById(R.id.cb_limalima);
        this.limalima.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.limalima, 12, "55");
            }
        });
        this.enampuluh = (CheckBox) findViewById(R.id.cb_enampuluh);
        this.enampuluh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.enampuluh, 13, "60");
            }
        });
        this.enamlima = (CheckBox) findViewById(R.id.cb_enamlima);
        this.enamlima.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.enamlima, 14, "65");
            }
        });
        this.tujuhpuluh = (CheckBox) findViewById(R.id.cb_tujuhpuluh);
        this.tujuhpuluh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.tujuhpuluh, 15, "70");
            }
        });
        this.tujuhlima = (CheckBox) findViewById(R.id.cb_tujuhlima);
        this.tujuhlima.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.tujuhlima, 16, "75");
            }
        });
        this.delapanpuluh = (CheckBox) findViewById(R.id.cb_delapanpuluh);
        this.delapanpuluh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.delapanpuluh, 17, "80");
            }
        });
        this.delapanlima = (CheckBox) findViewById(R.id.cb_delapanlima);
        this.delapanlima.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.delapanlima, 18, "85");
            }
        });
        this.sembilanpuluh = (CheckBox) findViewById(R.id.cb_sembilanpuluh);
        this.sembilanpuluh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.sembilanpuluh, 19, "90");
            }
        });
        this.seratus = (CheckBox) findViewById(R.id.cb_seratus);
        this.seratus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imp_fuel.this.checkedChanged(imp_fuel.this.seratus, 20, "100");
            }
        });
        ((EditText) findViewById(1)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 0, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(2)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 1, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(3)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 2, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(4)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 3, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(5)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 4, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(6)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 5, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(7)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 6, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(8)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 7, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(9)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 8, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(10)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 9, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(11)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 10, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(12)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 11, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(13)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 12, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(14)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 13, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(15)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 14, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(16)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 15, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(17)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 16, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(18)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 17, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(19)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 18, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(20)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 19, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(21)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 20, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(22)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 21, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(23)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 22, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(24)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 23, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(25)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 24, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(26)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 25, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(27)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 26, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(28)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 27, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(29)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 28, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(30)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 29, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(31)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 30, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(32)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 31, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(33)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 32, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(34)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 33, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(35)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 34, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(36)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 35, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(37)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 36, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(38)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 37, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(39)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 38, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(40)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 39, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(41)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 40, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(42)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 41, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(43)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 42, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(44)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 43, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(45)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 44, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(46)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 45, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(47)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 46, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(48)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 47, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(49)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 48, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(50)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 49, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(51)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 50, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(52)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 51, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(53)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 52, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(54)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 53, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(55)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 54, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(56)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 55, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(57)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 56, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(58)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 57, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(59)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 58, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(60)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 59, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((EditText) findViewById(61)).addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                switch (imp_fuel.this.pilih1_fuel_file) {
                    case 0:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set1);
                        return;
                    case 1:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set2);
                        return;
                    case 2:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set3);
                        return;
                    case 3:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set4);
                        return;
                    case 4:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set5);
                        return;
                    case 5:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set6);
                        return;
                    case 6:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set7);
                        return;
                    case 7:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set8);
                        return;
                    case 8:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set9);
                        return;
                    case 9:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set10);
                        return;
                    case 10:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set11);
                        return;
                    case 11:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set12);
                        return;
                    case 12:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set13);
                        return;
                    case 13:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set14);
                        return;
                    case 14:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set15);
                        return;
                    case 15:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set16);
                        return;
                    case 16:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set17);
                        return;
                    case 17:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set18);
                        return;
                    case 18:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set19);
                        return;
                    case 19:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set20);
                        return;
                    case 20:
                        imp_fuel.this.ValueChangedListener(editable, 60, imp_fuel.this.set21);
                        return;
                    default:
                        return;
                }
            }
        });
        ((ImageButton) findViewById(R.id.set_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imp_fuel.this.set_value();
            }
        });
        ((ImageButton) findViewById(R.id.add_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imp_fuel.this.add_value();
            }
        });
        ((ImageButton) findViewById(R.id.copy_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imp_fuel.this.Copy();
            }
        });
        ((ImageButton) findViewById(R.id.paste_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imp_fuel.this.Paste();
            }
        });
        ((ImageButton) findViewById(R.id.select_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imp_fuel.this.select();
            }
        });
        ((ImageButton) findViewById(R.id.plus_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imp_fuel.this.plus_minus("plus");
            }
        });
        ((ImageButton) findViewById(R.id.minus_map_f)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imp_fuel.this.plus_minus("minus");
            }
        });
        ((ImageButton) findViewById(R.id.interpolation)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imp_fuel.this.interpolasi();
            }
        });
        ((Button) findViewById(R.id.button_save)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int posisi = 0;
                for (int i = 1220; i <= 1281; i++) {
                    if (!String.format("%.2f", new Object[]{Float.valueOf(imp_fuel.this.set21.getYValForXIndex(posisi))}).replace(",", ".").equals("NaN")) {
                        if (!MappingHandle.list_base_map.get(i - 1).toString().equals(String.format("%.2f", new Object[]{Float.valueOf(imp_fuel.this.set21.getYValForXIndex(posisi))}).replace(",", "."))) {
                            imp_fuel.this.SavePreferences(main_menu.calculate_pertama, "0");
                        }
                    }
                    posisi++;
                }
                MappingHandle.list_fuel.clear();
                int awal = 0;
                for (int i2 = 0; i2 < 61; i2++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set1.getYValForXIndex(i2)));
                    awal++;
                }
                for (int i3 = 0; i3 < 61; i3++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set2.getYValForXIndex(i3)));
                    awal++;
                }
                for (int i4 = 0; i4 < 61; i4++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set3.getYValForXIndex(i4)));
                    awal++;
                }
                for (int i5 = 0; i5 < 61; i5++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set4.getYValForXIndex(i5)));
                    awal++;
                }
                for (int i6 = 0; i6 < 61; i6++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set5.getYValForXIndex(i6)));
                    awal++;
                }
                for (int i7 = 0; i7 < 61; i7++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set6.getYValForXIndex(i7)));
                    awal++;
                }
                for (int i8 = 0; i8 < 61; i8++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set7.getYValForXIndex(i8)));
                    awal++;
                }
                for (int i9 = 0; i9 < 61; i9++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set8.getYValForXIndex(i9)));
                    awal++;
                }
                for (int i10 = 0; i10 < 61; i10++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set9.getYValForXIndex(i10)));
                    awal++;
                }
                for (int i11 = 0; i11 < 61; i11++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set10.getYValForXIndex(i11)));
                    awal++;
                }
                for (int i12 = 0; i12 < 61; i12++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set11.getYValForXIndex(i12)));
                    awal++;
                }
                for (int i13 = 0; i13 < 61; i13++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set12.getYValForXIndex(i13)));
                    awal++;
                }
                for (int i14 = 0; i14 < 61; i14++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set13.getYValForXIndex(i14)));
                    awal++;
                }
                for (int i15 = 0; i15 < 61; i15++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set14.getYValForXIndex(i15)));
                    awal++;
                }
                for (int i16 = 0; i16 < 61; i16++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set15.getYValForXIndex(i16)));
                    awal++;
                }
                for (int i17 = 0; i17 < 61; i17++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set16.getYValForXIndex(i17)));
                    awal++;
                }
                for (int i18 = 0; i18 < 61; i18++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set17.getYValForXIndex(i18)));
                    awal++;
                }
                for (int i19 = 0; i19 < 61; i19++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set18.getYValForXIndex(i19)));
                    awal++;
                }
                for (int i20 = 0; i20 < 61; i20++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set19.getYValForXIndex(i20)));
                    awal++;
                }
                for (int i21 = 0; i21 < 61; i21++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set20.getYValForXIndex(i21)));
                    awal++;
                }
                for (int i22 = 0; i22 < 61; i22++) {
                    MappingHandle.list_fuel.add(awal, String.valueOf((int) imp_fuel.this.set21.getYValForXIndex(i22)));
                    awal++;
                }
                Toast.makeText(imp_fuel.this.getApplicationContext(), "Saved", 0).show();
            }
        });
        this.mChart = (LineChart) findViewById(R.id.linechart);
        this.mChart.setDrawGridBackground(false);
        this.mChart.getLegend().setEnabled(false);
        this.mChart.setDescription("");
        this.mChart.setNoDataTextDescription("You need to provide data for the chart.");
        this.mChart.setTouchEnabled(true);
        this.mChart.setDragEnabled(true);
        this.mChart.setScaleEnabled(true);
        addLimit();
        this.mChart.animateX(100, Easing.EasingOption.EaseInOutBounce);
        this.mChart.invalidate();
        this.edit_tps = (Spinner) findViewById(R.id.edit_tps);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, 17367048, array);
        adapter1.setDropDownViewResource(17367049);
        this.edit_tps.setAdapter(adapter1);
        this.edit_tps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        imp_fuel.this.pilih1_fuel_file = 0;
                        break;
                    case 1:
                        imp_fuel.this.pilih1_fuel_file = 1;
                        break;
                    case 2:
                        imp_fuel.this.pilih1_fuel_file = 2;
                        break;
                    case 3:
                        imp_fuel.this.pilih1_fuel_file = 3;
                        break;
                    case 4:
                        imp_fuel.this.pilih1_fuel_file = 4;
                        break;
                    case 5:
                        imp_fuel.this.pilih1_fuel_file = 5;
                        break;
                    case 6:
                        imp_fuel.this.pilih1_fuel_file = 6;
                        break;
                    case 7:
                        imp_fuel.this.pilih1_fuel_file = 7;
                        break;
                    case 8:
                        imp_fuel.this.pilih1_fuel_file = 8;
                        break;
                    case 9:
                        imp_fuel.this.pilih1_fuel_file = 9;
                        break;
                    case 10:
                        imp_fuel.this.pilih1_fuel_file = 10;
                        break;
                    case 11:
                        imp_fuel.this.pilih1_fuel_file = 11;
                        break;
                    case 12:
                        imp_fuel.this.pilih1_fuel_file = 12;
                        break;
                    case 13:
                        imp_fuel.this.pilih1_fuel_file = 13;
                        break;
                    case 14:
                        imp_fuel.this.pilih1_fuel_file = 14;
                        break;
                    case 15:
                        imp_fuel.this.pilih1_fuel_file = 15;
                        break;
                    case 16:
                        imp_fuel.this.pilih1_fuel_file = 16;
                        break;
                    case 17:
                        imp_fuel.this.pilih1_fuel_file = 17;
                        break;
                    case 18:
                        imp_fuel.this.pilih1_fuel_file = 18;
                        break;
                    case 19:
                        imp_fuel.this.pilih1_fuel_file = 19;
                        break;
                    case 20:
                        imp_fuel.this.pilih1_fuel_file = 20;
                        break;
                }
                imp_fuel.this.isi_tabel_fuel1_file();
                for (int i = 0; i < 7; i++) {
                    if (imp_fuel.this.edit_tps.getSelectedItem().toString().replace(" ", "").equals(imp_fuel.this.arr1_fuel_file[i])) {
                        for (int j = 1; j <= 61; j++) {
                            ((EditText) imp_fuel.this.findViewById(j)).setEnabled(true);
                        }
                        imp_fuel.this.boleh_geser1_fuel_file = true;
                        return;
                    }
                    for (int j2 = 1; j2 <= 61; j2++) {
                        ((EditText) imp_fuel.this.findViewById(j2)).setEnabled(false);
                    }
                    imp_fuel.this.boleh_geser1_fuel_file = false;
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        for (int i = 0; i <= 6; i++) {
            this.arr1_fuel_file[i] = "";
        }
        setData();
        String cocok = newString.replace("%", "").replace(" ", "");
        char c = 65535;
        switch (cocok.hashCode()) {
            case 48:
                if (cocok.equals("0")) {
                    c = 0;
                    break;
                }
                break;
            case 50:
                if (cocok.equals("2")) {
                    c = 1;
                    break;
                }
                break;
            case 53:
                if (cocok.equals("5")) {
                    c = 2;
                    break;
                }
                break;
            case 1567:
                if (cocok.equals("10")) {
                    c = 3;
                    break;
                }
                break;
            case 1572:
                if (cocok.equals("15")) {
                    c = 4;
                    break;
                }
                break;
            case 1598:
                if (cocok.equals("20")) {
                    c = 5;
                    break;
                }
                break;
            case 1603:
                if (cocok.equals("25")) {
                    c = 6;
                    break;
                }
                break;
            case 1629:
                if (cocok.equals("30")) {
                    c = 7;
                    break;
                }
                break;
            case 1634:
                if (cocok.equals("35")) {
                    c = 8;
                    break;
                }
                break;
            case 1660:
                if (cocok.equals("40")) {
                    c = 9;
                    break;
                }
                break;
            case 1665:
                if (cocok.equals("45")) {
                    c = 10;
                    break;
                }
                break;
            case 1691:
                if (cocok.equals("50")) {
                    c = 11;
                    break;
                }
                break;
            case 1696:
                if (cocok.equals("55")) {
                    c = 12;
                    break;
                }
                break;
            case 1722:
                if (cocok.equals("60")) {
                    c = 13;
                    break;
                }
                break;
            case 1727:
                if (cocok.equals("65")) {
                    c = 14;
                    break;
                }
                break;
            case 1753:
                if (cocok.equals("70")) {
                    c = 15;
                    break;
                }
                break;
            case 1758:
                if (cocok.equals("75")) {
                    c = 16;
                    break;
                }
                break;
            case 1784:
                if (cocok.equals("80")) {
                    c = 17;
                    break;
                }
                break;
            case 1789:
                if (cocok.equals("85")) {
                    c = 18;
                    break;
                }
                break;
            case 1815:
                if (cocok.equals("90")) {
                    c = 19;
                    break;
                }
                break;
            case 48625:
                if (cocok.equals("100")) {
                    c = 20;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                this.arr1_fuel_file[0] = "0";
                this.edit_tps.setSelection(0);
                this.pilih1_fuel_file = 0;
                this.nol.setChecked(true);
                this.set1.setVisible(true);
                return;
            case 1:
                this.arr1_fuel_file[0] = "2";
                this.edit_tps.setSelection(1);
                this.pilih1_fuel_file = 1;
                this.dua.setChecked(true);
                this.set2.setVisible(true);
                return;
            case 2:
                this.arr1_fuel_file[0] = "5";
                this.edit_tps.setSelection(2);
                this.pilih1_fuel_file = 2;
                this.lima.setChecked(true);
                this.set3.setVisible(true);
                return;
            case 3:
                this.arr1_fuel_file[0] = "10";
                this.edit_tps.setSelection(3);
                this.pilih1_fuel_file = 3;
                this.sepuluh.setChecked(true);
                this.set4.setVisible(true);
                return;
            case 4:
                this.arr1_fuel_file[0] = "15";
                this.edit_tps.setSelection(4);
                this.pilih1_fuel_file = 4;
                this.limabelas.setChecked(true);
                this.set5.setVisible(true);
                return;
            case 5:
                this.arr1_fuel_file[0] = "20";
                this.edit_tps.setSelection(5);
                this.pilih1_fuel_file = 5;
                this.duapuluh.setChecked(true);
                this.set6.setVisible(true);
                return;
            case 6:
                this.arr1_fuel_file[0] = "25";
                this.edit_tps.setSelection(6);
                this.pilih1_fuel_file = 6;
                this.dualima.setChecked(true);
                this.set7.setVisible(true);
                return;
            case 7:
                this.arr1_fuel_file[0] = "30";
                this.edit_tps.setSelection(7);
                this.pilih1_fuel_file = 7;
                this.tigapuluh.setChecked(true);
                this.set8.setVisible(true);
                return;
            case 8:
                this.arr1_fuel_file[0] = "35";
                this.edit_tps.setSelection(8);
                this.pilih1_fuel_file = 8;
                this.tigalima.setChecked(true);
                this.set9.setVisible(true);
                return;
            case 9:
                this.arr1_fuel_file[0] = "40";
                this.edit_tps.setSelection(9);
                this.pilih1_fuel_file = 9;
                this.empatpuluh.setChecked(true);
                this.set10.setVisible(true);
                return;
            case 10:
                this.arr1_fuel_file[0] = "45";
                this.edit_tps.setSelection(10);
                this.pilih1_fuel_file = 10;
                this.empatlima.setChecked(true);
                this.set11.setVisible(true);
                return;
            case 11:
                this.arr1_fuel_file[0] = "50";
                this.edit_tps.setSelection(11);
                this.pilih1_fuel_file = 11;
                this.limapuluh.setChecked(true);
                this.set12.setVisible(true);
                return;
            case 12:
                this.arr1_fuel_file[0] = "55";
                this.edit_tps.setSelection(12);
                this.pilih1_fuel_file = 12;
                this.limalima.setChecked(true);
                this.set13.setVisible(true);
                return;
            case 13:
                this.arr1_fuel_file[0] = "60";
                this.edit_tps.setSelection(13);
                this.pilih1_fuel_file = 13;
                this.enampuluh.setChecked(true);
                this.set14.setVisible(true);
                return;
            case 14:
                this.arr1_fuel_file[0] = "65";
                this.edit_tps.setSelection(14);
                this.pilih1_fuel_file = 14;
                this.enamlima.setChecked(true);
                this.set15.setVisible(true);
                return;
            case 15:
                this.arr1_fuel_file[0] = "70";
                this.edit_tps.setSelection(15);
                this.pilih1_fuel_file = 15;
                this.tujuhpuluh.setChecked(true);
                this.set16.setVisible(true);
                return;
            case 16:
                this.arr1_fuel_file[0] = "75";
                this.edit_tps.setSelection(16);
                this.pilih1_fuel_file = 16;
                this.tujuhlima.setChecked(true);
                this.set17.setVisible(true);
                return;
            case 17:
                this.arr1_fuel_file[0] = "80";
                this.edit_tps.setSelection(17);
                this.pilih1_fuel_file = 17;
                this.delapanpuluh.setChecked(true);
                this.set18.setVisible(true);
                return;
            case 18:
                this.arr1_fuel_file[0] = "85";
                this.edit_tps.setSelection(18);
                this.pilih1_fuel_file = 18;
                this.delapanlima.setChecked(true);
                this.set19.setVisible(true);
                return;
            case 19:
                this.arr1_fuel_file[0] = "90";
                this.edit_tps.setSelection(19);
                this.pilih1_fuel_file = 19;
                this.sembilanpuluh.setChecked(true);
                this.set20.setVisible(true);
                return;
            case 20:
                this.arr1_fuel_file[0] = "100";
                this.edit_tps.setSelection(20);
                this.pilih1_fuel_file = 20;
                this.seratus.setChecked(true);
                this.set21.setVisible(true);
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivityForResult(new Intent(getApplicationContext(), fuel_correction.class), 0);
    }

    /* access modifiers changed from: private */
    public void ValueChangedListener(Editable editable, int pos, LineDataSet set) {
        try {
            int cek = Integer.valueOf(editable.toString()).intValue();
            if (cek <= 100 && cek >= -100) {
                this.list1.clear();
                for (int i = 0; i < 61; i++) {
                    if (i == pos) {
                        this.list1.add(i, Integer.valueOf(cek));
                    } else {
                        this.list1.add(i, Integer.valueOf((int) set.getYValForXIndex(i)));
                    }
                }
                set.clear();
                for (int i2 = 0; i2 < 61; i2++) {
                    set.addEntry(new Entry((float) Integer.valueOf(this.list1.get(i2).toString()).intValue(), i2));
                }
                set.notifyDataSetChanged();
                this.mChart.notifyDataSetChanged();
                this.mChart.invalidate();
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public void checkedChanged(CheckBox cek, int value, String position) {
        if (cek.isChecked()) {
            if (this.hitung1_fuel_file < 6) {
                this.hitung1_fuel_file++;
            } else if (this.hitung1_fuel_file == 6) {
                uncheck_fuel1_file();
            }
            this.arr1_fuel_file[this.hitung1_fuel_file] = position;
            implisit_fuel1_click_file(value);
            this.pilih1_fuel_file = value;
            this.edit_tps.setSelection(value);
            isi_tabel_fuel1_file();
            this.mChart.invalidate();
            return;
        }
        chart_false(value);
        Boolean benar = false;
        for (int j = 0; j < this.hitung1_fuel_file; j++) {
            if (this.arr1_fuel_file[j].equals(position)) {
                this.arr1_fuel_file[j] = this.arr1_fuel_file[j + 1];
                benar = true;
            }
            if (benar.booleanValue()) {
                if (j < this.hitung1_fuel_file) {
                    this.arr1_fuel_file[j] = this.arr1_fuel_file[j + 1];
                } else if (j == this.hitung1_fuel_file) {
                    this.arr1_fuel_file[j] = "";
                }
            }
        }
        for (int k = this.hitung1_fuel_file; k <= 6; k++) {
            this.arr1_fuel_file[k] = "";
        }
        this.hitung1_fuel_file--;
        String str = this.arr1_fuel_file[0];
        char c = 65535;
        switch (str.hashCode()) {
            case 48:
                if (str.equals("0")) {
                    c = 0;
                    break;
                }
                break;
            case 50:
                if (str.equals("2")) {
                    c = 1;
                    break;
                }
                break;
            case 53:
                if (str.equals("5")) {
                    c = 2;
                    break;
                }
                break;
            case 1567:
                if (str.equals("10")) {
                    c = 3;
                    break;
                }
                break;
            case 1572:
                if (str.equals("15")) {
                    c = 4;
                    break;
                }
                break;
            case 1598:
                if (str.equals("20")) {
                    c = 5;
                    break;
                }
                break;
            case 1603:
                if (str.equals("25")) {
                    c = 6;
                    break;
                }
                break;
            case 1629:
                if (str.equals("30")) {
                    c = 7;
                    break;
                }
                break;
            case 1634:
                if (str.equals("35")) {
                    c = 8;
                    break;
                }
                break;
            case 1660:
                if (str.equals("40")) {
                    c = 9;
                    break;
                }
                break;
            case 1665:
                if (str.equals("45")) {
                    c = 10;
                    break;
                }
                break;
            case 1691:
                if (str.equals("50")) {
                    c = 11;
                    break;
                }
                break;
            case 1696:
                if (str.equals("55")) {
                    c = 12;
                    break;
                }
                break;
            case 1722:
                if (str.equals("60")) {
                    c = 13;
                    break;
                }
                break;
            case 1727:
                if (str.equals("65")) {
                    c = 14;
                    break;
                }
                break;
            case 1753:
                if (str.equals("70")) {
                    c = 15;
                    break;
                }
                break;
            case 1758:
                if (str.equals("75")) {
                    c = 16;
                    break;
                }
                break;
            case 1784:
                if (str.equals("80")) {
                    c = 17;
                    break;
                }
                break;
            case 1789:
                if (str.equals("85")) {
                    c = 18;
                    break;
                }
                break;
            case 1815:
                if (str.equals("90")) {
                    c = 19;
                    break;
                }
                break;
            case 48625:
                if (str.equals("100")) {
                    c = 20;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                this.edit_tps.setSelection(0);
                break;
            case 1:
                this.edit_tps.setSelection(1);
                break;
            case 2:
                this.edit_tps.setSelection(2);
                break;
            case 3:
                this.edit_tps.setSelection(3);
                break;
            case 4:
                this.edit_tps.setSelection(4);
                break;
            case 5:
                this.edit_tps.setSelection(5);
                break;
            case 6:
                this.edit_tps.setSelection(6);
                break;
            case 7:
                this.edit_tps.setSelection(7);
                break;
            case 8:
                this.edit_tps.setSelection(8);
                break;
            case 9:
                this.edit_tps.setSelection(9);
                break;
            case 10:
                this.edit_tps.setSelection(10);
                break;
            case 11:
                this.edit_tps.setSelection(11);
                break;
            case 12:
                this.edit_tps.setSelection(12);
                break;
            case 13:
                this.edit_tps.setSelection(13);
                break;
            case 14:
                this.edit_tps.setSelection(14);
                break;
            case 15:
                this.edit_tps.setSelection(15);
                break;
            case 16:
                this.edit_tps.setSelection(16);
                break;
            case 17:
                this.edit_tps.setSelection(17);
                break;
            case 18:
                this.edit_tps.setSelection(18);
                break;
            case 19:
                this.edit_tps.setSelection(19);
                break;
            case 20:
                this.edit_tps.setSelection(20);
                break;
        }
        this.mChart.invalidate();
    }

    private void implisit_fuel1_click_file(int cocok) {
        switch (cocok) {
            case 0:
                this.set1.setVisible(true);
                return;
            case 1:
                this.set2.setVisible(true);
                return;
            case 2:
                this.set3.setVisible(true);
                return;
            case 3:
                this.set4.setVisible(true);
                return;
            case 4:
                this.set5.setVisible(true);
                return;
            case 5:
                this.set6.setVisible(true);
                return;
            case 6:
                this.set7.setVisible(true);
                return;
            case 7:
                this.set8.setVisible(true);
                return;
            case 8:
                this.set9.setVisible(true);
                return;
            case 9:
                this.set10.setVisible(true);
                return;
            case 10:
                this.set11.setVisible(true);
                return;
            case 11:
                this.set12.setVisible(true);
                return;
            case 12:
                this.set13.setVisible(true);
                return;
            case 13:
                this.set14.setVisible(true);
                return;
            case 14:
                this.set15.setVisible(true);
                return;
            case 15:
                this.set16.setVisible(true);
                return;
            case 16:
                this.set17.setVisible(true);
                return;
            case 17:
                this.set18.setVisible(true);
                return;
            case 18:
                this.set19.setVisible(true);
                return;
            case 19:
                this.set20.setVisible(true);
                return;
            case 20:
                this.set21.setVisible(true);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void isi_tabel_fuel1_file() {
        switch (this.pilih1_fuel_file) {
            case 0:
                for (int j = 0; j < 61; j++) {
                    ((EditText) findViewById(j + 1)).setText(String.valueOf((int) this.set1.getYValForXIndex(j)));
                }
                return;
            case 1:
                for (int j2 = 0; j2 < 61; j2++) {
                    ((EditText) findViewById(j2 + 1)).setText(String.valueOf((int) this.set2.getYValForXIndex(j2)));
                }
                return;
            case 2:
                for (int j3 = 0; j3 < 61; j3++) {
                    ((EditText) findViewById(j3 + 1)).setText(String.valueOf((int) this.set3.getYValForXIndex(j3)));
                }
                return;
            case 3:
                for (int j4 = 0; j4 < 61; j4++) {
                    ((EditText) findViewById(j4 + 1)).setText(String.valueOf((int) this.set4.getYValForXIndex(j4)));
                }
                return;
            case 4:
                for (int j5 = 0; j5 < 61; j5++) {
                    ((EditText) findViewById(j5 + 1)).setText(String.valueOf((int) this.set5.getYValForXIndex(j5)));
                }
                return;
            case 5:
                for (int j6 = 0; j6 < 61; j6++) {
                    ((EditText) findViewById(j6 + 1)).setText(String.valueOf((int) this.set6.getYValForXIndex(j6)));
                }
                return;
            case 6:
                for (int j7 = 0; j7 < 61; j7++) {
                    ((EditText) findViewById(j7 + 1)).setText(String.valueOf((int) this.set7.getYValForXIndex(j7)));
                }
                return;
            case 7:
                for (int j8 = 0; j8 < 61; j8++) {
                    ((EditText) findViewById(j8 + 1)).setText(String.valueOf((int) this.set8.getYValForXIndex(j8)));
                }
                return;
            case 8:
                for (int j9 = 0; j9 < 61; j9++) {
                    ((EditText) findViewById(j9 + 1)).setText(String.valueOf((int) this.set9.getYValForXIndex(j9)));
                }
                return;
            case 9:
                for (int j10 = 0; j10 < 61; j10++) {
                    ((EditText) findViewById(j10 + 1)).setText(String.valueOf((int) this.set10.getYValForXIndex(j10)));
                }
                return;
            case 10:
                for (int j11 = 0; j11 < 61; j11++) {
                    ((EditText) findViewById(j11 + 1)).setText(String.valueOf((int) this.set11.getYValForXIndex(j11)));
                }
                return;
            case 11:
                for (int j12 = 0; j12 < 61; j12++) {
                    ((EditText) findViewById(j12 + 1)).setText(String.valueOf((int) this.set12.getYValForXIndex(j12)));
                }
                return;
            case 12:
                for (int j13 = 0; j13 < 61; j13++) {
                    ((EditText) findViewById(j13 + 1)).setText(String.valueOf((int) this.set13.getYValForXIndex(j13)));
                }
                return;
            case 13:
                for (int j14 = 0; j14 < 61; j14++) {
                    ((EditText) findViewById(j14 + 1)).setText(String.valueOf((int) this.set14.getYValForXIndex(j14)));
                }
                return;
            case 14:
                for (int j15 = 0; j15 < 61; j15++) {
                    ((EditText) findViewById(j15 + 1)).setText(String.valueOf((int) this.set15.getYValForXIndex(j15)));
                }
                return;
            case 15:
                for (int j16 = 0; j16 < 61; j16++) {
                    ((EditText) findViewById(j16 + 1)).setText(String.valueOf((int) this.set16.getYValForXIndex(j16)));
                }
                return;
            case 16:
                for (int j17 = 0; j17 < 61; j17++) {
                    ((EditText) findViewById(j17 + 1)).setText(String.valueOf((int) this.set17.getYValForXIndex(j17)));
                }
                return;
            case 17:
                for (int j18 = 0; j18 < 61; j18++) {
                    ((EditText) findViewById(j18 + 1)).setText(String.valueOf((int) this.set18.getYValForXIndex(j18)));
                }
                return;
            case 18:
                for (int j19 = 0; j19 < 61; j19++) {
                    ((EditText) findViewById(j19 + 1)).setText(String.valueOf((int) this.set19.getYValForXIndex(j19)));
                }
                return;
            case 19:
                for (int j20 = 0; j20 < 61; j20++) {
                    ((EditText) findViewById(j20 + 1)).setText(String.valueOf((int) this.set20.getYValForXIndex(j20)));
                }
                return;
            case 20:
                for (int j21 = 0; j21 < 61; j21++) {
                    ((EditText) findViewById(j21 + 1)).setText(String.valueOf((int) this.set21.getYValForXIndex(j21)));
                }
                return;
            default:
                return;
        }
    }

    private void chart_false(int posisi) {
        switch (posisi) {
            case 0:
                this.set1.setVisible(false);
                return;
            case 1:
                this.set2.setVisible(false);
                return;
            case 2:
                this.set3.setVisible(false);
                return;
            case 3:
                this.set4.setVisible(false);
                return;
            case 4:
                this.set5.setVisible(false);
                return;
            case 5:
                this.set6.setVisible(false);
                return;
            case 6:
                this.set7.setVisible(false);
                return;
            case 7:
                this.set8.setVisible(false);
                return;
            case 8:
                this.set9.setVisible(false);
                return;
            case 9:
                this.set10.setVisible(false);
                return;
            case 10:
                this.set11.setVisible(false);
                return;
            case 11:
                this.set12.setVisible(false);
                return;
            case 12:
                this.set13.setVisible(false);
                return;
            case 13:
                this.set14.setVisible(false);
                return;
            case 14:
                this.set15.setVisible(false);
                return;
            case 15:
                this.set16.setVisible(false);
                return;
            case 16:
                this.set17.setVisible(false);
                return;
            case 17:
                this.set18.setVisible(false);
                return;
            case 18:
                this.set19.setVisible(false);
                return;
            case 19:
                this.set20.setVisible(false);
                return;
            case 20:
                this.set21.setVisible(false);
                return;
            default:
                return;
        }
    }

    private void uncheck_fuel1_file() {
        int val = 0;
        int satu = Integer.valueOf(this.arr1_fuel_file[0]).intValue();
        if (satu == 2) {
            val = 1;
        } else if (satu >= 5 && satu <= 90) {
            val = ((satu * 17) / 85) + 1;
        } else if (satu == 100) {
            val = 20;
        }
        chart_false(val);
        switch (val) {
            case 0:
                this.nol.setChecked(false);
                break;
            case 1:
                this.dua.setChecked(false);
                break;
            case 2:
                this.lima.setChecked(false);
                break;
            case 3:
                this.sepuluh.setChecked(false);
                break;
            case 4:
                this.limabelas.setChecked(false);
                break;
            case 5:
                this.duapuluh.setChecked(false);
                break;
            case 6:
                this.dualima.setChecked(false);
                break;
            case 7:
                this.tigapuluh.setChecked(false);
                break;
            case 8:
                this.tigalima.setChecked(false);
                break;
            case 9:
                this.empatpuluh.setChecked(false);
                break;
            case 10:
                this.empatlima.setChecked(false);
                break;
            case 11:
                this.limapuluh.setChecked(false);
                break;
            case 12:
                this.limalima.setChecked(false);
                break;
            case 13:
                this.enampuluh.setChecked(false);
                break;
            case 14:
                this.enamlima.setChecked(false);
                break;
            case 15:
                this.tujuhpuluh.setChecked(false);
                break;
            case 16:
                this.tujuhlima.setChecked(false);
                break;
            case 17:
                this.delapanpuluh.setChecked(false);
                break;
            case 18:
                this.delapanlima.setChecked(false);
                break;
            case 19:
                this.sembilanpuluh.setChecked(false);
                break;
            case 20:
                this.seratus.setChecked(false);
                break;
        }
        for (int j = 0; j <= 5; j++) {
            this.arr1_fuel_file[j] = this.arr1_fuel_file[j + 1];
        }
    }

    private void setData() {
        ArrayList<String> xVals = setXAxisValues();
        int posisi_awal_y = 0;
        for (int i = 0; i < 61; i++) {
            this.yVals1.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i));
            posisi_awal_y++;
        }
        for (int i2 = 0; i2 < 61; i2++) {
            this.yVals2.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i2));
            posisi_awal_y++;
        }
        for (int i3 = 0; i3 < 61; i3++) {
            this.yVals3.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i3));
            posisi_awal_y++;
        }
        for (int i4 = 0; i4 < 61; i4++) {
            this.yVals4.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i4));
            posisi_awal_y++;
        }
        for (int i5 = 0; i5 < 61; i5++) {
            this.yVals5.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i5));
            posisi_awal_y++;
        }
        for (int i6 = 0; i6 < 61; i6++) {
            this.yVals6.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i6));
            posisi_awal_y++;
        }
        for (int i7 = 0; i7 < 61; i7++) {
            this.yVals7.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i7));
            posisi_awal_y++;
        }
        for (int i8 = 0; i8 < 61; i8++) {
            this.yVals8.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i8));
            posisi_awal_y++;
        }
        for (int i9 = 0; i9 < 61; i9++) {
            this.yVals9.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i9));
            posisi_awal_y++;
        }
        for (int i10 = 0; i10 < 61; i10++) {
            this.yVals10.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i10));
            posisi_awal_y++;
        }
        for (int i11 = 0; i11 < 61; i11++) {
            this.yVals11.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i11));
            posisi_awal_y++;
        }
        for (int i12 = 0; i12 < 61; i12++) {
            this.yVals12.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i12));
            posisi_awal_y++;
        }
        for (int i13 = 0; i13 < 61; i13++) {
            this.yVals13.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i13));
            posisi_awal_y++;
        }
        for (int i14 = 0; i14 < 61; i14++) {
            this.yVals14.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i14));
            posisi_awal_y++;
        }
        for (int i15 = 0; i15 < 61; i15++) {
            this.yVals15.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i15));
            posisi_awal_y++;
        }
        for (int i16 = 0; i16 < 61; i16++) {
            this.yVals16.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i16));
            posisi_awal_y++;
        }
        for (int i17 = 0; i17 < 61; i17++) {
            this.yVals17.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i17));
            posisi_awal_y++;
        }
        for (int i18 = 0; i18 < 61; i18++) {
            this.yVals18.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i18));
            posisi_awal_y++;
        }
        for (int i19 = 0; i19 < 61; i19++) {
            this.yVals19.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i19));
            posisi_awal_y++;
        }
        for (int i20 = 0; i20 < 61; i20++) {
            this.yVals20.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i20));
            posisi_awal_y++;
        }
        for (int i21 = 0; i21 < 61; i21++) {
            this.yVals21.add(new Entry(Float.valueOf(MappingHandle.list_fuel.get(posisi_awal_y)).floatValue(), i21));
            posisi_awal_y++;
        }
        this.set1 = new LineDataSet(this.yVals1, "");
        this.set1.setFillAlpha(110);
        this.set1.setColor(Color.rgb(255, 0, 0));
        this.set1.setCircleColor(Color.rgb(255, 0, 0));
        this.set1.setLineWidth(1.0f);
        this.set1.setCircleRadius(2.0f);
        this.set1.setDrawCircleHole(false);
        this.set1.setValueTextSize(9.0f);
        this.set2 = new LineDataSet(this.yVals2, "");
        this.set2.setFillAlpha(110);
        this.set2.setColor(Color.rgb(255, 162, 0));
        this.set2.setCircleColor(Color.rgb(255, 162, 0));
        this.set2.setLineWidth(1.0f);
        this.set2.setCircleRadius(2.0f);
        this.set2.setDrawCircleHole(false);
        this.set2.setValueTextSize(9.0f);
        this.set3 = new LineDataSet(this.yVals3, "");
        this.set3.setFillAlpha(110);
        this.set3.setColor(Color.rgb(255, 255, 0));
        this.set3.setCircleColor(Color.rgb(255, 255, 0));
        this.set3.setLineWidth(1.0f);
        this.set3.setCircleRadius(2.0f);
        this.set3.setDrawCircleHole(false);
        this.set3.setValueTextSize(9.0f);
        this.set4 = new LineDataSet(this.yVals4, "");
        this.set4.setFillAlpha(110);
        this.set4.setColor(Color.rgb(0, 255, 255));
        this.set4.setCircleColor(Color.rgb(0, 255, 255));
        this.set4.setLineWidth(1.0f);
        this.set4.setCircleRadius(2.0f);
        this.set4.setDrawCircleHole(false);
        this.set4.setValueTextSize(9.0f);
        this.set5 = new LineDataSet(this.yVals5, "");
        this.set5.setFillAlpha(110);
        this.set5.setColor(Color.rgb(255, 0, 255));
        this.set5.setCircleColor(Color.rgb(255, 0, 255));
        this.set5.setLineWidth(1.0f);
        this.set5.setCircleRadius(2.0f);
        this.set5.setDrawCircleHole(false);
        this.set5.setValueTextSize(9.0f);
        this.set6 = new LineDataSet(this.yVals6, "");
        this.set6.setFillAlpha(110);
        this.set6.setColor(Color.rgb(0, 0, 0));
        this.set6.setCircleColor(Color.rgb(0, 0, 0));
        this.set6.setLineWidth(1.0f);
        this.set6.setCircleRadius(2.0f);
        this.set6.setDrawCircleHole(false);
        this.set6.setValueTextSize(9.0f);
        this.set7 = new LineDataSet(this.yVals7, "");
        this.set7.setFillAlpha(110);
        this.set7.setColor(Color.rgb(0, 0, 255));
        this.set7.setCircleColor(Color.rgb(0, 0, 255));
        this.set7.setLineWidth(1.0f);
        this.set7.setCircleRadius(2.0f);
        this.set7.setDrawCircleHole(false);
        this.set7.setValueTextSize(9.0f);
        this.set8 = new LineDataSet(this.yVals8, "");
        this.set8.setFillAlpha(110);
        this.set8.setColor(Color.rgb(0, 255, 0));
        this.set8.setCircleColor(Color.rgb(0, 255, 0));
        this.set8.setLineWidth(1.0f);
        this.set8.setCircleRadius(2.0f);
        this.set8.setDrawCircleHole(false);
        this.set8.setValueTextSize(9.0f);
        this.set9 = new LineDataSet(this.yVals9, "");
        this.set9.setFillAlpha(110);
        this.set9.setColor(Color.rgb(105, 184, 146));
        this.set9.setCircleColor(Color.rgb(105, 184, 146));
        this.set9.setLineWidth(1.0f);
        this.set9.setCircleRadius(2.0f);
        this.set9.setDrawCircleHole(false);
        this.set9.setValueTextSize(9.0f);
        this.set10 = new LineDataSet(this.yVals10, "");
        this.set10.setFillAlpha(110);
        this.set10.setColor(Color.rgb(107, 116, 118));
        this.set10.setCircleColor(Color.rgb(107, 116, 118));
        this.set10.setLineWidth(1.0f);
        this.set10.setCircleRadius(2.0f);
        this.set10.setDrawCircleHole(false);
        this.set10.setValueTextSize(9.0f);
        this.set11 = new LineDataSet(this.yVals11, "");
        this.set11.setFillAlpha(110);
        this.set11.setColor(Color.rgb(128, 0, 0));
        this.set11.setCircleColor(Color.rgb(128, 0, 0));
        this.set11.setLineWidth(1.0f);
        this.set11.setCircleRadius(2.0f);
        this.set11.setDrawCircleHole(false);
        this.set11.setValueTextSize(9.0f);
        this.set12 = new LineDataSet(this.yVals12, "");
        this.set12.setFillAlpha(110);
        this.set12.setColor(Color.rgb(204, 153, 0));
        this.set12.setCircleColor(Color.rgb(204, 153, 0));
        this.set12.setLineWidth(1.0f);
        this.set12.setCircleRadius(2.0f);
        this.set12.setDrawCircleHole(false);
        this.set12.setValueTextSize(9.0f);
        this.set13 = new LineDataSet(this.yVals13, "");
        this.set13.setFillAlpha(110);
        this.set13.setColor(Color.rgb(128, 192, 255));
        this.set13.setCircleColor(Color.rgb(128, 192, 255));
        this.set13.setLineWidth(1.0f);
        this.set13.setCircleRadius(2.0f);
        this.set13.setDrawCircleHole(false);
        this.set13.setValueTextSize(9.0f);
        this.set14 = new LineDataSet(this.yVals14, "");
        this.set14.setFillAlpha(110);
        this.set14.setColor(Color.rgb(153, 0, 255));
        this.set14.setCircleColor(Color.rgb(153, 0, 255));
        this.set14.setLineWidth(1.0f);
        this.set14.setCircleRadius(2.0f);
        this.set14.setDrawCircleHole(false);
        this.set14.setValueTextSize(9.0f);
        this.set15 = new LineDataSet(this.yVals15, "");
        this.set15.setFillAlpha(110);
        this.set15.setColor(Color.rgb(255, 102, 51));
        this.set15.setCircleColor(Color.rgb(255, 102, 51));
        this.set15.setLineWidth(1.0f);
        this.set15.setCircleRadius(2.0f);
        this.set15.setDrawCircleHole(false);
        this.set15.setValueTextSize(9.0f);
        this.set16 = new LineDataSet(this.yVals16, "");
        this.set16.setFillAlpha(110);
        this.set16.setColor(Color.rgb(81, 81, 81));
        this.set16.setCircleColor(Color.rgb(81, 81, 81));
        this.set16.setLineWidth(1.0f);
        this.set16.setCircleRadius(2.0f);
        this.set16.setDrawCircleHole(false);
        this.set16.setValueTextSize(9.0f);
        this.set17 = new LineDataSet(this.yVals17, "");
        this.set17.setFillAlpha(110);
        this.set17.setColor(Color.rgb(192, 128, 0));
        this.set17.setCircleColor(Color.rgb(192, 128, 0));
        this.set17.setLineWidth(1.0f);
        this.set17.setCircleRadius(2.0f);
        this.set17.setDrawCircleHole(false);
        this.set17.setValueTextSize(9.0f);
        this.set18 = new LineDataSet(this.yVals18, "");
        this.set18.setFillAlpha(110);
        this.set18.setColor(Color.rgb(255, 128, 0));
        this.set18.setCircleColor(Color.rgb(255, 128, 0));
        this.set18.setLineWidth(1.0f);
        this.set18.setCircleRadius(2.0f);
        this.set18.setDrawCircleHole(false);
        this.set18.setValueTextSize(9.0f);
        this.set19 = new LineDataSet(this.yVals19, "");
        this.set19.setFillAlpha(110);
        this.set19.setColor(Color.rgb(102, 102, 0));
        this.set19.setCircleColor(Color.rgb(102, 102, 0));
        this.set19.setLineWidth(1.0f);
        this.set19.setCircleRadius(2.0f);
        this.set19.setDrawCircleHole(false);
        this.set19.setValueTextSize(9.0f);
        this.set20 = new LineDataSet(this.yVals20, "");
        this.set20.setFillAlpha(110);
        this.set20.setColor(Color.rgb(0, 102, 102));
        this.set20.setCircleColor(Color.rgb(0, 102, 102));
        this.set20.setLineWidth(1.0f);
        this.set20.setCircleRadius(2.0f);
        this.set20.setDrawCircleHole(false);
        this.set20.setValueTextSize(9.0f);
        this.set21 = new LineDataSet(this.yVals21, "");
        this.set21.setFillAlpha(110);
        this.set21.setColor(Color.rgb(128, 255, 0));
        this.set21.setCircleColor(Color.rgb(128, 255, 0));
        this.set21.setLineWidth(1.0f);
        this.set21.setCircleRadius(2.0f);
        this.set21.setDrawCircleHole(false);
        this.set21.setValueTextSize(9.0f);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(this.set1);
        dataSets.add(this.set2);
        dataSets.add(this.set3);
        dataSets.add(this.set4);
        dataSets.add(this.set5);
        dataSets.add(this.set6);
        dataSets.add(this.set7);
        dataSets.add(this.set8);
        dataSets.add(this.set9);
        dataSets.add(this.set10);
        dataSets.add(this.set11);
        dataSets.add(this.set12);
        dataSets.add(this.set13);
        dataSets.add(this.set14);
        dataSets.add(this.set15);
        dataSets.add(this.set16);
        dataSets.add(this.set17);
        dataSets.add(this.set18);
        dataSets.add(this.set19);
        dataSets.add(this.set20);
        dataSets.add(this.set21);
        this.set1.setVisible(false);
        this.set2.setVisible(false);
        this.set3.setVisible(false);
        this.set4.setVisible(false);
        this.set5.setVisible(false);
        this.set6.setVisible(false);
        this.set7.setVisible(false);
        this.set8.setVisible(false);
        this.set9.setVisible(false);
        this.set10.setVisible(false);
        this.set11.setVisible(false);
        this.set12.setVisible(false);
        this.set13.setVisible(false);
        this.set14.setVisible(false);
        this.set15.setVisible(false);
        this.set16.setVisible(false);
        this.set17.setVisible(false);
        this.set18.setVisible(false);
        this.set19.setVisible(false);
        this.set20.setVisible(false);
        this.set21.setVisible(false);
        this.mChart.setData(new LineData((List<String>) xVals, (List<ILineDataSet>) dataSets));
    }

    private ArrayList<String> setXAxisValues() {
        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 1000; i <= 16000; i += ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) {
            xVals.add(String.valueOf(i));
        }
        return xVals;
    }

    private ArrayList<Entry> setYAxisValues() {
        ArrayList<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < 61; i++) {
            yVals.add(new Entry(-5.0f, i));
        }
        return yVals;
    }

    private void addLimit() {
        YAxis leftAxis = this.mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaxValue(100.0f);
        leftAxis.setAxisMinValue(-100.0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.mDecimals = 0;
        leftAxis.setDrawLimitLinesBehindData(true);
        this.mChart.getAxisRight().setEnabled(false);
    }

    /* access modifiers changed from: private */
    public void interpolasi() {
        int awal = 61;
        int akhir = 0;
        for (int j = 1; j <= 61; j++) {
            if (((EditText) findViewById(j)).getTag().equals("Warna CYAN")) {
                if (j <= awal) {
                    awal = j;
                }
                if (j > akhir) {
                    akhir = j;
                }
            }
        }
        if (awal != 61 && akhir != 0) {
            EditText edit1 = (EditText) findViewById(awal);
            Double hasil = Double.valueOf(Double.valueOf(Double.valueOf(((EditText) findViewById(akhir)).getText().toString()).doubleValue() - Double.valueOf(edit1.getText().toString()).doubleValue()).doubleValue() / Double.valueOf((double) (akhir - awal)).doubleValue());
            Double pengali = Double.valueOf(1.0d);
            Double nilai_awal = Double.valueOf(edit1.getText().toString());
            for (int k = awal + 1; k < akhir; k++) {
                pengali = Double.valueOf(pengali.doubleValue() + 1.0d);
                ((EditText) findViewById(k)).setText(String.valueOf((int) (nilai_awal.doubleValue() + (pengali.doubleValue() * hasil.doubleValue()))));
            }
        }
    }

    /* access modifiers changed from: private */
    public void add_value() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle((CharSequence) "Add Value");
        alert.setMessage((CharSequence) "Enter Your Value Here");
        final EditText inputNilaiAdd = new EditText(this);
        inputNilaiAdd.setKeyListener(DigitsKeyListener.getInstance("-0123456789"));
        inputNilaiAdd.setFilters(new InputFilter[]{new range_int_min("-100", "100")});
        inputNilaiAdd.setImeOptions(268435456);
        alert.setView((View) inputNilaiAdd);
        alert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                imp_fuel.this.AddValue(inputNilaiAdd.getEditableText().toString());
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
    public void set_value() {
        new AlertDialog.Builder(this);
        AlertDialog.Builder alert_set = new AlertDialog.Builder(this);
        alert_set.setTitle((CharSequence) "Set Value");
        alert_set.setMessage((CharSequence) "Enter Your Value Here");
        final EditText inputNilai = new EditText(this);
        inputNilai.setKeyListener(DigitsKeyListener.getInstance("-0123456789"));
        inputNilai.setFilters(new InputFilter[]{new range_int_min("-100", "100")});
        inputNilai.setImeOptions(268435456);
        alert_set.setView((View) inputNilai);
        alert_set.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                imp_fuel.this.SetValue(inputNilai.getEditableText().toString());
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

    public void AddValue(String Value) {
        double nilai;
        for (int i = 1; i <= 61; i++) {
            EditText v = (EditText) findViewById(i);
            String str = v.getText().toString();
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                if (str.length() != 0) {
                    nilai = Double.parseDouble(v.getText().toString());
                } else {
                    nilai = Double.parseDouble("0");
                }
                double nilai2 = nilai + Double.parseDouble(Value);
                if (nilai2 > 100.0d) {
                    nilai2 = 100.0d;
                } else if (nilai2 < -100.0d) {
                    nilai2 = -100.0d;
                }
                v.setText(String.valueOf(Math.round(nilai2)));
            }
        }
    }

    public void SetValue(String Value) {
        for (int i = 1; i <= 61; i++) {
            EditText v = (EditText) findViewById(i);
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                v.setText(Value);
            }
        }
    }

    /* access modifiers changed from: private */
    public void plus_minus(String pilihan) {
        double nilai;
        for (int i = 1; i <= 61; i++) {
            EditText v = (EditText) findViewById(i);
            String str = v.getText().toString();
            if (v.getTag() != null && v.getTag().equals("Warna CYAN")) {
                if (str.length() != 0) {
                    nilai = Double.parseDouble(v.getText().toString());
                } else {
                    nilai = Double.parseDouble("0");
                }
                if (pilihan.equals("plus")) {
                    nilai += 1.0d;
                } else if (pilihan.equals("minus")) {
                    nilai -= 1.0d;
                }
                if (nilai > 100.0d) {
                    nilai = 100.0d;
                } else if (nilai < -100.0d) {
                    nilai = -100.0d;
                }
                v.setText(String.valueOf(Math.round(nilai)));
            }
        }
    }

    /* access modifiers changed from: private */
    public void select() {
        StaticClass.selectionStatus = false;
        StaticClass.position.clear();
        for (int i = 1; i <= 61; i++) {
            StaticClass.position.add(Integer.valueOf(i));
            EditText x = (EditText) findViewById(i);
            x.setBackgroundColor(-16711681);
            x.setTag("Warna CYAN");
        }
    }

    public void Copy() {
        this.status_flag = 1;
        if (StaticClass.letak.size() != 0) {
            StaticClass.data.clear();
            StaticClass.letak.clear();
        }
        for (int i = 1; i <= 61; i++) {
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
                for (int i = 1; i <= 61; i++) {
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
    public void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }
}
