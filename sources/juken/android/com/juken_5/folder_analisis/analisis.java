package juken.android.com.juken_5.folder_analisis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import java.util.ArrayList;
import juken.android.com.juken_5.MappingHandle;
import juken.android.com.juken_5.R;
import juken.android.com.juken_5.folder_analisis.helper.BottomNavigationBehavior;

public class analisis extends AppCompatActivity {
    Fragment fragment_image;
    Fragment fragment_matrix;
    Fragment fragment_param;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_param:
                    if (analisis.this.fragment_param == null) {
                        analisis.this.fragment_param = new AnalisisParameterFragment();
                    }
                    analisis.this.loadFragment(analisis.this.fragment_param);
                    return true;
                case R.id.navigation_matrix:
                    if (analisis.this.fragment_matrix == null) {
                        analisis.this.fragment_matrix = new AnalisisMatrixFragment();
                    }
                    analisis.this.loadFragment(analisis.this.fragment_matrix);
                    return true;
                case R.id.navigation_image:
                    if (analisis.this.fragment_image == null) {
                        analisis.this.fragment_image = new AnalisisImageFragment();
                    }
                    analisis.this.loadFragment(analisis.this.fragment_image);
                    return true;
                default:
                    return false;
            }
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.analisis);
        for (int i = 0; i < 1281; i++) {
            AnalisisPassData.list_analisis_asli.add(MappingHandle.list_injector.get(i));
            AnalisisPassData.list_analisis_backup.add(MappingHandle.list_injector.get(i));
            Double fuel = Double.valueOf(MappingHandle.list_fuel.get(i));
            Double bm = Double.valueOf(MappingHandle.list_base_map.get(i));
            Double bm_tot = Double.valueOf(bm.doubleValue() + ((bm.doubleValue() * fuel.doubleValue()) / 100.0d));
            AnalisisPassData.list_analisis_asli.add(String.format("%.2f", new Object[]{bm_tot}).replace(",", "."));
            AnalisisPassData.list_analisis_backup.add(String.format("%.2f", new Object[]{bm_tot}).replace(",", "."));
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this.mOnNavigationItemSelectedListener);
        ((CoordinatorLayout.LayoutParams) navigation.getLayoutParams()).setBehavior(new BottomNavigationBehavior());
        this.fragment_param = new AnalisisParameterFragment();
        loadFragment(this.fragment_param);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        AnalisisPassData.AnaArray = new ArrayList<>();
        AnalisisPassData.cek_spinner = "20";
        AnalisisPassData.masuk_awal = true;
        AnalisisPassData.nilai_actual_tps = "100%";
        if (MappingHandle.list_injector_ganti.size() < 100) {
            for (int i = 0; i < 1281; i++) {
                MappingHandle.list_injector_ganti.add("0");
            }
        }
        for (int i2 = 0; i2 < MappingHandle.list_injector.size(); i2++) {
            if (!MappingHandle.list_injector.get(i2).equals(AnalisisPassData.list_analisis_backup.get(i2 * 2))) {
                MappingHandle.list_injector_ganti.remove(i2);
                MappingHandle.list_injector_ganti.add(i2, "1");
            }
        }
        MappingHandle.list_injector.clear();
        for (int i3 = 0; i3 < AnalisisPassData.list_analisis_backup.size(); i3 += 2) {
            MappingHandle.list_injector.add(AnalisisPassData.list_analisis_backup.get(i3));
        }
        AnalisisPassData.list_analisis_asli = new ArrayList<>();
        AnalisisPassData.list_analisis_backup = new ArrayList<>();
    }

    /* access modifiers changed from: private */
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
}
