package juken.android.com.juken_5.folder_fuel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import juken.android.com.juken_5.R;

public class FileArrayAdapter extends ArrayAdapter<Option> {
    private Context c;
    private int id;
    private List<Option> items;

    public FileArrayAdapter(Context context, int textViewResourceId, List<Option> objects) {
        super(context, textViewResourceId, objects);
        this.c = context;
        this.id = textViewResourceId;
        this.items = objects;
    }

    public Option getItem(int i) {
        return this.items.get(i);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = ((LayoutInflater) this.c.getSystemService("layout_inflater")).inflate(this.id, (ViewGroup) null);
        }
        Option o = this.items.get(position);
        if (o != null) {
            TextView t1 = (TextView) v.findViewById(R.id.TextView01);
            TextView t2 = (TextView) v.findViewById(R.id.TextView02);
            if (t1 != null) {
                t1.setText(o.getName());
            }
            if (t2 != null) {
                t2.setText(o.getData());
            }
        }
        return v;
    }
}
