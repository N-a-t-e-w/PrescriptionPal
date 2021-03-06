package cosc341.group4.prescriptionpal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class PrescriptionListAdapter extends BaseExpandableListAdapter {
    private HashMap<String, List<String>> mStringListHashMap;
    private String[] mListHeaderGroup;
    private String[] ButtonArray;
    public PrescriptionListAdapter(HashMap<String, List<String>> stringListHashMap) {
        mStringListHashMap = stringListHashMap;
        mListHeaderGroup = mStringListHashMap.keySet().toArray(new String[0]);
        ButtonArray = mStringListHashMap.keySet().toArray(new String[0]);
    }

    @Override
    public int getGroupCount() {
        return mListHeaderGroup.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mStringListHashMap.get(mListHeaderGroup[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListHeaderGroup[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mStringListHashMap.get(mListHeaderGroup[groupPosition]).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition*childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescriptions_expandable_list_group, parent, false);

        TextView textView = convertView.findViewById(R.id.prescriptions_prescription_group);
        textView.setText(String.valueOf(getGroup(groupPosition)));
        Button editPrescript = convertView.findViewById(R.id.editPrescriptBtn);
        editPrescript.setContentDescription(String.valueOf(getGroup(groupPosition)));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescriptions_expandable_list_item, parent, false);

        TextView textView = convertView.findViewById(R.id.prescriptions_prescription_item);
        textView.setText(String.valueOf(getChild(groupPosition, childPosition)));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
