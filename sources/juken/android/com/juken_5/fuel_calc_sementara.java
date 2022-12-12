package juken.android.com.juken_5;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class fuel_calc_sementara extends Activity {
    Button cancel;
    Button save_calculator;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuel_calc_sementara);
        this.cancel = (Button) findViewById(R.id.exit_fuel_calc_sementara);
        this.cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_calc_sementara.this.finish();
            }
        });
        this.save_calculator = (Button) findViewById(R.id.save_fuel_calc_sementara);
        this.save_calculator.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fuel_calc_sementara.this.SavePreferences("sudah_pernah_ecu", "1");
                MappingHandle.list_fuel.clear();
                for (int i = 0; i < MappingHandle.list_fuel_calculator.size(); i++) {
                    MappingHandle.list_fuel.add(MappingHandle.list_fuel_calculator.get(i));
                }
                Toast.makeText(fuel_calc_sementara.this.getApplicationContext(), "saved", 0).show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }
}
