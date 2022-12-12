package juken.android.com.juken_5.data_logger;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import juken.android.com.juken_5.MappingHandle;
import juken.android.com.juken_5.R;

public class data_logger_list extends ListActivity {
    private Handler _handler = new Handler();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(4, 4);
        setContentView(R.layout.data_logger_list);
        showDevices();
    }

    /* access modifiers changed from: protected */
    public void showDevices() {
        List<String> list = new ArrayList<>();
        int size = MappingHandle.data_logger_posisi100.size();
        for (int i = 0; i < size; i++) {
            list.add("Data " + String.valueOf(i + 1));
        }
        if (MappingHandle.data_logger_posisi100.size() <= 0) {
            Toast.makeText(this, "No Data at TPS 100%", 1).show();
        }
        setListAdapter(new ArrayAdapter<>(this, 17367043, list));
    }

    /* access modifiers changed from: protected */
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent myIntent = new Intent(v.getContext(), data_logger_utama.class);
        myIntent.putExtra("Posisi", String.valueOf(position));
        startActivityForResult(myIntent, 0);
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }
}
