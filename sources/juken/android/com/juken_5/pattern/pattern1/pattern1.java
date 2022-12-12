package juken.android.com.juken_5.pattern.pattern1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import juken.android.com.juken_5.MappingHandle;
import juken.android.com.juken_5.R;
import juken.android.com.juken_5.pattern.pattern2.pattern2;

public class pattern1 extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern1);
        ((Button) findViewById(R.id.exit_pattern)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pattern1.this.finish();
            }
        });
        ((Button) findViewById(R.id.save_pattern)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MappingHandle.list_pattern.clear();
                for (int i = 19; i > 0; i--) {
                    for (int j = 0; j < 61; j++) {
                        int posisi1 = j + 1221;
                        double seratus = Double.valueOf(((EditText) pattern1.this.findViewById(posisi1)).getText().toString()).doubleValue();
                        MappingHandle.list_pattern.add(String.format("%.2f", new Object[]{Double.valueOf(((Double.valueOf(((EditText) pattern1.this.findViewById(posisi1 - (i * 61))).getText().toString()).doubleValue() - seratus) * 100.0d) / seratus)}).replace(",", "."));
                    }
                }
                pattern1.this.startActivityForResult(new Intent(view.getContext(), pattern2.class), 0);
            }
        });
    }
}
