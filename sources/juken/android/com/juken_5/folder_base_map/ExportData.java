package juken.android.com.juken_5.folder_base_map;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportData {
    Activity thisActivity;

    public ExportData(Activity activity) {
        this.thisActivity = activity;
    }

    public void writeToFile(String nama_file) {
        File path = new File(Environment.getExternalStorageDirectory() + "/JUKEN");
        if (!path.exists()) {
            path.mkdir();
        }
        File file = new File(path + "/" + (nama_file.replaceAll(".txt", "") + ".txt"));
        try {
            file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);
            stream.write("aaa".getBytes());
            stream.flush();
            stream.close();
            Toast.makeText(this.thisActivity.getApplicationContext(), "file saved", 1).show();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
