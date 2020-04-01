package cosc341.group4.prescriptionpal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class TodayListAdapter extends BaseExpandableListAdapter {
    private HashMap<String, List<String>> mStringListHashMap;
    private HashMap<String, Boolean> mStringBooleanHashMap;
    private String[] mListHeaderGroup;
    private String[] checkBoxArray;

    public TodayListAdapter(HashMap<String, List<String>> stringListHashMap, HashMap <String, Boolean> stringBooleanHashMap) {
        mStringListHashMap = stringListHashMap;
        mStringBooleanHashMap = stringBooleanHashMap;
        mListHeaderGroup = mStringListHashMap.keySet().toArray(new String[0]);
        checkBoxArray = mStringBooleanHashMap.keySet().toArray(new String[0]);
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

    public Boolean getBoolean(int groupPosition){
        return mStringBooleanHashMap.get(checkBoxArray[groupPosition]);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_expandable_list_group, parent, false);

        TextView textView = convertView.findViewById(R.id.today_prescription_group);
        textView.setText(String.valueOf(getGroup(groupPosition)));
        CheckBox confirm = convertView.findViewById(R.id.today_confirm_checkbox);
        confirm.setChecked(getBoolean(groupPosition));
        if (HomepageActivity.CARETAKER_MODE){
            confirm.setText("Administered");
        }
        //Use the content description to determine which checkbox is activated later
        confirm.setContentDescription(String.valueOf(getGroup(groupPosition)));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_expandable_list_item, parent, false);

        TextView textView = convertView.findViewById(R.id.today_prescription_item);
        textView.setText(String.valueOf(getChild(groupPosition, childPosition)));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
