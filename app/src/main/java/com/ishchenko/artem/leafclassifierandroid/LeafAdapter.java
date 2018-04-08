package com.ishchenko.artem.leafclassifierandroid;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Artem on 08.04.2018.
 */

public class LeafAdapter extends BaseExpandableListAdapter {

    private List<Pair<String, List<String>>> spaces;
    private Context mContext;

    public LeafAdapter(Context context, List<Pair<String, List<String>>> spaces) {
        mContext = context;
        this.spaces = spaces;
    }

    @Override
    public int getGroupCount() {
        return spaces.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return spaces.get(groupPosition).second.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return spaces.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return spaces.get(groupPosition).second.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_view, null);
        }

        if (isExpanded) {
            //Изменяем что-нибудь, если текущая Group раскрыта
        } else {
            //Изменяем что-нибудь, если текущая Group скрыта
        }

        TextView textGroup = (TextView) convertView.findViewById(R.id.textGroup);
        textGroup.setText(spaces.get(groupPosition).first);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_view, null);
        }

        TextView textChild = convertView.findViewById(R.id.textChild);
        textChild.setText(spaces.get(groupPosition).second.get(childPosition));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}