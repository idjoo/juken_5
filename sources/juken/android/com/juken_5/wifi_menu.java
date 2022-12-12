package juken.android.com.juken_5;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;

public class wifi_menu extends Activity {
    WifiConfiguration conf = new WifiConfiguration();
    int counter = 0;
    int counter1 = 0;
    String dataPASSWORD = "";
    String dataSSID = "";
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    private Handler handler_ = new Handler();
    EditText password;
    ProgressDialog progressDialog;
    /* access modifiers changed from: private */
    public final Runnable sendData = new Runnable() {
        public void run() {
            try {
                if (wifi_menu.this.checkWifiCon()) {
                    wifi_menu.this.counter = 0;
                    wifi_menu.this.counter1++;
                    if (wifi_menu.this.counter1 == 2) {
                        wifi_menu.this.service = new Intent(wifi_menu.this, WifiService.class);
                        wifi_menu.this.startService(wifi_menu.this.service);
                        if (wifi_menu.this.progressDialog.isShowing()) {
                            wifi_menu.this.progressDialog.setMessage("Create Socket");
                        }
                        wifi_menu.this.handler.postDelayed(this, 1000);
                    } else if (wifi_menu.this.counter1 < 2 || (wifi_menu.this.counter1 > 2 && wifi_menu.this.counter1 < 5)) {
                        wifi_menu.this.handler.postDelayed(this, 1000);
                    } else {
                        if (wifi_menu.this.progressDialog.isShowing()) {
                            wifi_menu.this.progressDialog.dismiss();
                        }
                        wifi_menu.this.setResult(-1, new Intent());
                        wifi_menu.this.finish();
                        wifi_menu.this.startActivityForResult(new Intent(wifi_menu.this.getApplicationContext(), main_menu.class), 0);
                    }
                } else {
                    wifi_menu.this.counter++;
                    wifi_menu.this.handler.postDelayed(this, 1000);
                }
                if (wifi_menu.this.counter == 6) {
                    wifi_menu wifi_menu = wifi_menu.this;
                    wifi_menu.this.counter1 = 0;
                    wifi_menu.counter = 0;
                    wifi_menu.this.handler.removeCallbacks(wifi_menu.this.sendData);
                    Toast.makeText(wifi_menu.this, "WiFi Not Connected", 0).show();
                    if (wifi_menu.this.progressDialog.isShowing()) {
                        wifi_menu.this.progressDialog.dismiss();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    /* access modifiers changed from: private */
    public final Runnable sendData1 = new Runnable() {
        public void run() {
            try {
                if (wifi_menu.this.checkWifiCon()) {
                    wifi_menu.this.counter = 0;
                    wifi_menu.this.counter1++;
                    if (wifi_menu.this.counter1 == 1) {
                        wifi_menu.this.service = new Intent(wifi_menu.this, WifiService.class);
                        wifi_menu.this.startService(wifi_menu.this.service);
                        if (wifi_menu.this.progressDialog.isShowing()) {
                            wifi_menu.this.progressDialog.setMessage("Create Socket");
                        }
                        wifi_menu.this.handler.postDelayed(this, 1000);
                    } else if (wifi_menu.this.counter1 < 1 || (wifi_menu.this.counter1 > 1 && wifi_menu.this.counter1 < 3)) {
                        wifi_menu.this.handler.postDelayed(this, 1000);
                    } else {
                        if (wifi_menu.this.progressDialog.isShowing()) {
                            wifi_menu.this.progressDialog.dismiss();
                        }
                        wifi_menu.this.setResult(-1, new Intent());
                        wifi_menu.this.finish();
                        wifi_menu.this.startActivityForResult(new Intent(wifi_menu.this.getApplicationContext(), main_menu.class), 0);
                    }
                } else {
                    wifi_menu.this.counter++;
                    wifi_menu.this.handler.postDelayed(this, 1000);
                }
                if (wifi_menu.this.counter == 6) {
                    wifi_menu wifi_menu = wifi_menu.this;
                    wifi_menu.this.counter1 = 0;
                    wifi_menu.counter = 0;
                    wifi_menu.this.handler.removeCallbacks(wifi_menu.this.sendData1);
                    Toast.makeText(wifi_menu.this, "WiFi Not Connected", 0).show();
                    if (wifi_menu.this.progressDialog.isShowing()) {
                        wifi_menu.this.progressDialog.dismiss();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Intent service;
    SharedPreferences sharedPreferences;
    EditText ssid;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_menu);
        startActivity(new Intent("android.settings.WIFI_SETTINGS"));
        ((Button) findViewById(R.id.wifi)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                wifi_menu.this.runOnUiThread(new Runnable() {
                    public void run() {
                        wifi_menu.this.progressDialog = new ProgressDialog(wifi_menu.this);
                        wifi_menu.this.progressDialog.setMessage("Loading...");
                        wifi_menu.this.progressDialog.setTitle("Connecting To WiFi");
                        wifi_menu.this.progressDialog.setProgressStyle(0);
                        wifi_menu.this.progressDialog.show();
                        wifi_menu.this.progressDialog.setCancelable(true);
                    }
                });
                wifi_menu.this.handler.removeCallbacks(wifi_menu.this.sendData1);
                wifi_menu wifi_menu = wifi_menu.this;
                wifi_menu.this.counter1 = 0;
                wifi_menu.counter = 0;
                wifi_menu.this.handler.post(wifi_menu.this.sendData1);
            }
        });
    }

    public void connectToWifi(String networkSSID, String networkPass) {
        try {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService("wifi");
            WifiConfiguration wc = new WifiConfiguration();
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            wc.SSID = "\"" + networkSSID + "\"";
            wc.preSharedKey = "\"" + networkPass + "\"";
            wc.status = 2;
            wc.allowedProtocols.set(0);
            wc.allowedKeyManagement.set(1);
            assignHighestPriority(wc, wifiManager);
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            int netId = wifiManager.addNetwork(wc);
            if (netId == -1) {
                netId = getExistingNetworkId(wc.SSID);
            }
            wifiManager.enableNetwork(netId, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getExistingNetworkId(String SSID) {
        List<WifiConfiguration> configuredNetworks = ((WifiManager) getApplicationContext().getSystemService("wifi")).getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration existingConfig : configuredNetworks) {
                if (existingConfig.SSID.equals(SSID)) {
                    return existingConfig.networkId;
                }
            }
        }
        return -1;
    }

    private void assignHighestPriority(WifiConfiguration config, WifiManager wifiManager) {
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration existingConfig : configuredNetworks) {
                if (config.priority <= existingConfig.priority) {
                    config.priority = existingConfig.priority + 1;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.ssid = (EditText) findViewById(R.id.ssid);
        this.password = (EditText) findViewById(R.id.password);
        this.ssid.setText(this.dataSSID);
        this.password.setText("********");
        this.ssid.setText(((WifiManager) getApplicationContext().getSystemService("wifi")).getConnectionInfo().getSSID().replace("\"", ""));
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public boolean checkWifiCon() {
        WifiInfo wifiInfo;
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService("wifi");
        if (!wifiManager.isWifiEnabled() || (wifiInfo = wifiManager.getConnectionInfo()) == null || wifiInfo.getNetworkId() == -1) {
            return false;
        }
        return true;
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }
}
