package juken.android.com.juken_5;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class send extends ListActivity {
    /* access modifiers changed from: private */
    public BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
    /* access modifiers changed from: private */
    public List<BluetoothDevice> _devices = new ArrayList();
    /* access modifiers changed from: private */
    public volatile boolean _discoveryFinished;
    private BroadcastReceiver _discoveryReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            send.this.unregisterReceiver(send.this._foundReceiver);
            send.this.unregisterReceiver(this);
            boolean unused = send.this._discoveryFinished = true;
        }
    };
    private Runnable _discoveryWorkder = new Runnable() {
        public void run() {
            if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.requestPermissions(send.this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 1);
                ActivityCompat.requestPermissions(send.this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 2);
            }
            BluetoothAdapter unused = send.this._bluetooth;
            BluetoothAdapter.getDefaultAdapter().startDiscovery();
            while (!send.this._discoveryFinished) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public BroadcastReceiver _foundReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            send.this._devices.add((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE"));
            send.this.showDevices();
        }
    };
    private Handler _handler = new Handler();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(4, 4);
        setContentView(R.layout.discovery);
        if (!this._bluetooth.isEnabled()) {
            finish();
            return;
        }
        registerReceiver(this._discoveryReceiver, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
        registerReceiver(this._foundReceiver, new IntentFilter("android.bluetooth.device.action.FOUND"));
        SamplesUtils.indeterminate(this, this._handler, "Scanning...", this._discoveryWorkder, new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                while (send.this._bluetooth.isDiscovering()) {
                    send.this._bluetooth.cancelDiscovery();
                }
                boolean unused = send.this._discoveryFinished = true;
            }
        }, true);
    }

    /* access modifiers changed from: protected */
    public void showDevices() {
        List<String> list = new ArrayList<>();
        int size = this._devices.size();
        for (int i = 0; i < size; i++) {
            StringBuilder b = new StringBuilder();
            BluetoothDevice d = this._devices.get(i);
            b.append(d.getAddress());
            b.append(10);
            b.append(d.getName());
            list.add(b.toString());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 17367043, list);
        this._handler.post(new Runnable() {
            public void run() {
                send.this.setListAdapter(adapter);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onListItemClick(ListView l, View v, int position, long id) {
        SavePreferences("android.bluetooth.device.extra.DEVICE", this._devices.get(position).toString());
        finish();
        startActivity(new Intent(v.getContext(), main_menu.class));
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }
}
