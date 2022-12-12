package juken.android.com.juken_5.folder_afr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import juken.android.com.juken_5.R;
import juken.android.com.juken_5.folder_base_map.base_map;
import juken.android.com.juken_5.folder_fuel.fuel_correction;
import juken.android.com.juken_5.folder_ignition.ignition_timing;
import juken.android.com.juken_5.folder_injector.injector_timing;

public class afr_display extends Activity {
    SharedPreferences sharedPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.afr_display);
        ((TextView) findViewById(R.id.ket_rich)).setText("< 12 Rich");
        ((TextView) findViewById(R.id.ket_lean)).setText(">= 14 Lean");
        ((Button) findViewById(R.id.switch_history)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                for (int i = 1; i < 1281; i++) {
                    EditText v = (EditText) afr_display.this.findViewById(i);
                    if (Double.valueOf(v.getText().toString()).doubleValue() != 0.0d) {
                        v.setBackgroundColor(Color.argb(255, 255, 136, 215));
                    }
                }
            }
        });
        ((Button) findViewById(R.id.switch_afr)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                for (int i = 1; i < 1281; i++) {
                    EditText v = (EditText) afr_display.this.findViewById(i);
                    double value = Double.valueOf(v.getText().toString()).doubleValue() * 10.0d;
                    if (value > 0.0d && value < 120.0d) {
                        v.setBackgroundColor(Color.argb(255, 15, 11, 239));
                    } else if (value >= 120.0d && value < 130.0d) {
                        v.setBackgroundColor(Color.argb(255, 95, 239, 11));
                    } else if (value >= 130.0d && value < 140.0d) {
                        v.setBackgroundColor(Color.argb(255, 239, 235, 11));
                    } else if (value >= 140.0d) {
                        v.setBackgroundColor(Color.argb(255, 239, 11, 22));
                    }
                }
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) <= 5 || keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        Log.d("CDA", "onKeyDown Called");
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        finish();
        String data = this.sharedPreferences.getString("back_pressed_mapping", "back_pressed_mapping1");
        if (data.equals("0")) {
            startActivityForResult(new Intent(getApplicationContext(), fuel_correction.class), 0);
        } else if (data.equals("1")) {
            startActivityForResult(new Intent(getApplicationContext(), base_map.class), 0);
        } else if (data.equals("2")) {
            startActivityForResult(new Intent(getApplicationContext(), injector_timing.class), 0);
        } else if (data.equals("3")) {
            startActivityForResult(new Intent(getApplicationContext(), ignition_timing.class), 0);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }
}
