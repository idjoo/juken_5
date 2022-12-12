package juken.android.com.juken_5;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WifiListAdapter extends Activity {
    String ITEM_KEY = "key";
    SimpleAdapter adapter;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    ListView lv;
    ProgressDialog progressDialog;
    List<ScanResult> results;
    Boolean scanSekali = true;
    SharedPreferences sharedPreferences;
    int size = 0;
    WifiManager wifi;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_list_adapter);
        RunDialog();
        this.lv = (ListView) findViewById(R.id.list);
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 87);
        }
        this.wifi = (WifiManager) getApplicationContext().getSystemService("wifi");
        if (!this.wifi.isWifiEnabled()) {
            Toast.makeText(this, "Enabled Wifi", 0).show();
            this.wifi.setWifiEnabled(true);
        }
        this.adapter = new SimpleAdapter(this, this.arrayList, R.layout.row, new String[]{this.ITEM_KEY}, new int[]{R.id.list_value});
        this.lv.setAdapter(this.adapter);
        this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(WifiListAdapter.this);
                alert1.setTitle((CharSequence) "Set Password");
                alert1.setMessage((CharSequence) "Enter Your Password Here");
                final EditText input = new EditText(WifiListAdapter.this);
                input.setImeOptions(268435456);
                alert1.setView((View) input);
                alert1.setPositiveButton((CharSequence) "SAVE", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String dPASSWORD1 = input.getEditableText().toString();
                        WifiListAdapter.this.SavePreferences("dSSID", ((TextView) view.findViewById(R.id.list_value)).getText().toString());
                        WifiListAdapter.this.SavePreferences("dPASSWORD", dPASSWORD1);
                        WifiListAdapter.this.finish();
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
        this.arrayList.clear();
        this.wifi.startScan();
        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (WifiListAdapter.this.scanSekali.booleanValue()) {
                    WifiListAdapter.this.scanSekali = false;
                    WifiListAdapter.this.results = WifiListAdapter.this.wifi.getScanResults();
                    WifiListAdapter.this.size = WifiListAdapter.this.results.size();
                    if (WifiListAdapter.this.size == 0) {
                        Toast.makeText(context, "No WiFi detected", 0).show();
                    }
                    try {
                        WifiListAdapter.this.size--;
                        while (WifiListAdapter.this.size >= 0) {
                            HashMap<String, String> item = new HashMap<>();
                            item.put(WifiListAdapter.this.ITEM_KEY, WifiListAdapter.this.results.get(WifiListAdapter.this.size).SSID);
                            WifiListAdapter.this.arrayList.add(item);
                            WifiListAdapter wifiListAdapter = WifiListAdapter.this;
                            wifiListAdapter.size--;
                            WifiListAdapter.this.adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Catch " + e.toString(), 0).show();
                    }
                    if (WifiListAdapter.this.progressDialog.isShowing()) {
                        WifiListAdapter.this.progressDialog.dismiss();
                    }
                }
            }
        }, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
    }

    private void RunDialog() {
        runOnUiThread(new Runnable() {
            public void run() {
                WifiListAdapter.this.progressDialog = new ProgressDialog(WifiListAdapter.this);
                WifiListAdapter.this.progressDialog.setMessage("Scanning...");
                WifiListAdapter.this.progressDialog.setTitle("");
                WifiListAdapter.this.progressDialog.setProgressStyle(0);
                WifiListAdapter.this.progressDialog.show();
                WifiListAdapter.this.progressDialog.setCancelable(true);
            }
        });
    }

    /* access modifiers changed from: private */
    public void SavePreferences(String key, String value) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
