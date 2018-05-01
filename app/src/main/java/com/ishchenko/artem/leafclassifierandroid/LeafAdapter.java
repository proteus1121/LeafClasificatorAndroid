package com.ishchenko.artem.leafclassifierandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Artem on 08.04.2018.
 */

public class LeafAdapter extends BaseExpandableListAdapter {

    private List<Pair<String, List<String>>> spaces;
    private Context mContext;
    private View view;

    public LeafAdapter(Context context, List<Pair<String, List<String>>> spaces, View view) {
        this.mContext = context;
        this.spaces = spaces;
        this.view = view;
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

        TextView textGroup = convertView.findViewById(R.id.textGroup);
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
        String imageName = spaces.get(groupPosition).second.get(childPosition);
        textChild.setText(imageName);

        ImageView imageView = view.findViewById(R.id.imageView);
        convertView.setOnClickListener(v -> {
            Bitmap image = LeafClassifier.getProjectEnv().getLeafSpecies().get(groupPosition).getImage(childPosition).getBitmap();

            imageView.setImageBitmap(image);

        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}