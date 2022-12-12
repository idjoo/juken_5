package juken.android.com.juken_5;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static String[] PERMISSION_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private static final int REQUEST_DISCOVERABLE = 2;
    private static final int REQUEST_DISCOVERY = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] array1 = {"Bahasa", "English"};
    int bahasa_dipilih = 0;
    private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    /* access modifiers changed from: private */
    public Handler handler = new Handler();
    final CharSequence[] items = {"Bahasa", "English (US)"};
    int pos = 0;
    ProgressDialog progressDialog;
    /* access modifiers changed from: private */
    public final Runnable sendData = new Runnable() {
        public void run() {
            try {
                if (MainActivity.this.pos > 1) {
                    MainActivity.this.pos = 0;
                    MainActivity.this.handler.removeCallbacks(MainActivity.this.sendData);
                    if (MainActivity.this.progressDialog.isShowing()) {
                        MainActivity.this.progressDialog.dismiss();
                    }
                    MainActivity.this.startActivity(new Intent(MainActivity.this, send.class));
                    MainActivity.this.finish();
                } else {
                    MainActivity.this.handler.postDelayed(this, 1000);
                }
                MainActivity.this.pos++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        getWindow().addFlags(128);
        verify(this);
        if (Build.VERSION.SDK_INT >= 23) {
            fuckMarshMallow();
            statusCheck();
        }
        StaticClass.saveFuel = false;
        StaticClass.saveBM = false;
        StaticClass.saveIG = false;
        StaticClass.saveIT = false;
        MappingHandle.NamaFileFuel = "Fuel Correction";
        MappingHandle.NamaFileBM = "Base Map";
        MappingHandle.NamaFileIG = "Ignition Timing";
        MappingHandle.NamaFileIT = "Injector Timing";
        ((TextView) findViewById(R.id.version)).setText("Ver : 2.2.0 - 2021");
        final TextView text = (TextView) findViewById(R.id.bahasa);
        final ImageButton language = (ImageButton) findViewById(R.id.language);
        language.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int posisi;
                String bahasa = PreferenceManager.getDefaultSharedPreferences(MainActivity.this.getApplicationContext()).getString("bahasa", "bahasa1");
                if (bahasa.equals("in")) {
                    posisi = 0;
                } else if (bahasa.equals("en")) {
                    posisi = 1;
                } else {
                    posisi = 0;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setSingleChoiceItems(MainActivity.this.items, posisi, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        MainActivity.this.bahasa_dipilih = item;
                    }
                });
                builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (MainActivity.this.bahasa_dipilih == 0) {
                            language.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.bahasa_in));
                            text.setText("Bahasa :");
                            MainActivity.this.SetLanguage("bahasa", "in");
                        } else {
                            language.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.bahasa_en));
                            text.setText("Language :");
                            MainActivity.this.SetLanguage("bahasa", "en");
                        }
                        LanguageHelper.onAttach1(MainActivity.this.getApplicationContext());
                    }
                });
                builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
        try {
            String bahasa = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("bahasa", "bahasa1");
            if (bahasa.equals("in")) {
                language.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bahasa_in));
                text.setText("Bahasa :");
                SetLanguage("bahasa", "in");
            } else if (bahasa.equals("en")) {
                language.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bahasa_en));
                text.setText("Language :");
                SetLanguage("bahasa", "en");
            } else {
                language.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bahasa_in));
                text.setText("Bahasa :");
                SetLanguage("bahasa", "in");
            }
            LanguageHelper.onAttach1(getApplicationContext());
        } catch (Exception e) {
            language.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bahasa_in));
            text.setText("Bahasa :");
            SetLanguage("bahasa", "in");
            LanguageHelper.onAttach1(getApplicationContext());
        }
        ((ImageButton) findViewById(R.id.next)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (MainActivity.this.cekMem() < 25.0d) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle((CharSequence) "Alert");
                    alert.setMessage((CharSequence) "Available Memory < 25%, continue program can cause fatal error");
                    alert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            StaticClass.TipeKoneksi = "none";
                            MainActivity.this.startActivity(new Intent(MainActivity.this, main_menu.class));
                            MainActivity.this.finish();
                        }
                    });
                    alert.setNegativeButton((CharSequence) "CANCEL", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.create().show();
                    return;
                }
                StaticClass.TipeKoneksi = "none";
                MainActivity.this.startActivity(new Intent(MainActivity.this, main_menu.class));
                MainActivity.this.finish();
            }
        });
        ((ImageButton) findViewById(R.id.wifi)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MainActivity.this.cekMem() < 25.0d) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle((CharSequence) "Alert");
                    alert.setMessage((CharSequence) "Available Memory < 25%, continue program can cause fatal error");
                    alert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            StaticClass.TipeKoneksi = "wifi";
                            MainActivity.this.startActivity(new Intent(MainActivity.this, wifi_menu.class));
                            MainActivity.this.finish();
                        }
                    });
                    alert.setNegativeButton((CharSequence) "CANCEL", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.create().show();
                    return;
                }
                StaticClass.TipeKoneksi = "wifi";
                MainActivity.this.startActivity(new Intent(MainActivity.this, wifi_menu.class));
                MainActivity.this.finish();
            }
        });
        ((ImageButton) findViewById(R.id.bluetooth)).setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                if (MainActivity.this.cekMem() < 25.0d) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle((CharSequence) "Alert");
                    alert.setMessage((CharSequence) "Available Memory < 25%, continue program can cause fatal error");
                    alert.setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    MainActivity.this.progressDialog = new ProgressDialog(MainActivity.this);
                                    MainActivity.this.progressDialog.setMessage("Preparing Bluetooth...");
                                    MainActivity.this.progressDialog.setTitle("Loading");
                                    MainActivity.this.progressDialog.setProgressStyle(0);
                                    MainActivity.this.progressDialog.show();
                                    MainActivity.this.progressDialog.setCancelable(true);
                                }
                            });
                            StaticClass.TipeKoneksi = "bt";
                            MainActivity.this.On_Off(v);
                            MainActivity.this.pos = 0;
                            MainActivity.this.handler.removeCallbacks(MainActivity.this.sendData);
                            MainActivity.this.handler.post(MainActivity.this.sendData);
                        }
                    });
                    alert.setNegativeButton((CharSequence) "CANCEL", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.create().show();
                    return;
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        MainActivity.this.progressDialog = new ProgressDialog(MainActivity.this);
                        MainActivity.this.progressDialog.setMessage("Preparing Bluetooth...");
                        MainActivity.this.progressDialog.setTitle("Loading");
                        MainActivity.this.progressDialog.setProgressStyle(0);
                        MainActivity.this.progressDialog.show();
                        MainActivity.this.progressDialog.setCancelable(true);
                    }
                });
                StaticClass.TipeKoneksi = "bt";
                MainActivity.this.On_Off(v);
                MainActivity.this.pos = 0;
                MainActivity.this.handler.removeCallbacks(MainActivity.this.sendData);
                MainActivity.this.handler.post(MainActivity.this.sendData);
            }
        });
    }

    /* access modifiers changed from: private */
    public void SetLanguage(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void On_Off(View view) {
        if (!this.bluetooth.isEnabled()) {
            this.bluetooth.enable();
            Toast.makeText(getApplicationContext(), "Bluetooth turned on", 0).show();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        SavePreferences("ack_awal", "1");
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString(key, value);
        editor.commit();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        freeMemory();
    }

    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                Map<String, Integer> perms = new HashMap<>();
                perms.put("android.permission.ACCESS_FINE_LOCATION", 0);
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], Integer.valueOf(grantResults[i]));
                }
                if (perms.get("android.permission.ACCESS_FINE_LOCATION").intValue() == 0) {
                    Toast.makeText(this, "All Permission GRANTED", 0).show();
                    return;
                }
                Toast.makeText(this, "One or More Permissions are DENIED Exiting App", 0).show();
                finish();
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }

    public static void verify(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, PERMISSION_STORAGE, 1);
        }
    }

    @TargetApi(23)
    private void fuckMarshMallow() {
        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();
        if (!addPermission(permissionsList, "android.permission.ACCESS_FINE_LOCATION")) {
            permissionsNeeded.add("Show Location");
        }
        if (permissionsList.size() <= 0) {
            return;
        }
        if (permissionsNeeded.size() > 0) {
            String message = "App need access to " + permissionsNeeded.get(0);
            for (int i = 1; i < permissionsNeeded.size(); i++) {
                message = message + ", " + permissionsNeeded.get(i);
            }
            showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.requestPermissions((String[]) permissionsList.toArray(new String[permissionsList.size()]), 1);
                }
            });
            return;
        }
        requestPermissions((String[]) permissionsList.toArray(new String[permissionsList.size()]), 1);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this).setMessage((CharSequence) message).setPositiveButton((CharSequence) "OK", okListener).setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) null).create().show();
    }

    @TargetApi(23)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != 0) {
            permissionsList.add(permission);
            if (!shouldShowRequestPermissionRationale(permission)) {
                return false;
            }
        }
        return true;
    }

    public void statusCheck() {
        if (!((LocationManager) getSystemService("location")).isProviderEnabled("gps")) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Your GPS seems to be disabled, do you want to enable it?").setCancelable(false).setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        }).setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private String getMemoryInfo() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) getSystemService("activity")).getMemoryInfo(memoryInfo);
        Runtime runtime = Runtime.getRuntime();
        StringBuilder builder = new StringBuilder();
        builder.append("Available Memory : ").append(memoryInfo.availMem / 1000000).append("\n");
        builder.append("Total Memory : ").append(memoryInfo.totalMem).append("\n");
        builder.append("Runtime Max Memory : ").append(runtime.maxMemory()).append("\n");
        builder.append("Runtime Total Memory : ").append(runtime.totalMemory()).append("\n");
        builder.append("Runtime Free Memory : ").append(runtime.freeMemory()).append("\n");
        return builder.toString();
    }

    private long availMem() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) getSystemService("activity")).getMemoryInfo(memoryInfo);
        Log.d("Avail Mem ", String.valueOf(memoryInfo.availMem));
        return memoryInfo.availMem;
    }

    /* access modifiers changed from: private */
    public double cekMem() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ((ActivityManager) getSystemService("activity")).getMemoryInfo(mi);
        double nativeHeapSize = (double) mi.totalMem;
        double nativeHeapFreeSize = (double) mi.availMem;
        double percentAvail = (((double) mi.availMem) / ((double) mi.totalMem)) * 100.0d;
        double usedMemInBytes = nativeHeapSize - nativeHeapFreeSize;
        double usedMemInPercentage = (100.0d * usedMemInBytes) / nativeHeapSize;
        Log.d("Heap size", String.valueOf(nativeHeapSize));
        Log.d("Heap free size", String.valueOf(nativeHeapFreeSize));
        Log.d("Mem Bytes", String.valueOf(usedMemInBytes));
        Log.d("Percent Avail", String.valueOf(percentAvail));
        Log.d("used Mem %", String.valueOf(usedMemInPercentage));
        return usedMemInPercentage;
    }
}
