package juken.android.com.juken_5;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapterLive extends BaseExpandableListAdapter {
    private static final int[] EMPTY_STATE_SET = new int[0];
    private static final int[] GROUP_EXPANDED_STATE_SET = {16842920};
    private static final int[][] GROUP_STATE_SETS = {EMPTY_STATE_SET, GROUP_EXPANDED_STATE_SET};
    ExpandableListView expandList;
    private Context mContext;
    private HashMap<ExpandedMenuModel, List<String>> mListDataChild;
    private List<ExpandedMenuModel> mListDataHeader;

    public ExpandableListAdapterLive(Context context, List<ExpandedMenuModel> listDataHeader, HashMap<ExpandedMenuModel, List<String>> listChildData, ExpandableListView mView) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        this.expandList = mView;
    }

    public int getGroupCount() {
        Log.d("GROUPCOUNT", String.valueOf(this.mListDataHeader.size()));
        return this.mListDataHeader.size();
    }

    public int getChildrenCount(int groupPosition) {
        if (groupPosition != 2) {
            return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).size();
        }
        return 0;
    }

    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    public Object getChild(int groupPosition, int childPosition) {
        Log.d("CHILD", ((String) this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).get(childPosition)).toString());
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).get(childPosition);
    }

    public long getGroupId(int groupPosition) {
        return (long) groupPosition;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }

    public boolean hasStableIds() {
        return false;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ExpandedMenuModel headerTitle = (ExpandedMenuModel) getGroup(groupPosition);
        if (convertView == null) {
            convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.listheader_live, (ViewGroup) null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.submenu);
        lblListHeader.setTypeface((Typeface) null, 1);
        lblListHeader.setText(headerTitle.getIconName());
        ((ImageView) convertView.findViewById(R.id.iconimage)).setImageResource(headerTitle.getIconImg());
        return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.list_submenu, (ViewGroup) null);
        }
        ((TextView) convertView.findViewById(R.id.submenu)).setText(childText);
        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
