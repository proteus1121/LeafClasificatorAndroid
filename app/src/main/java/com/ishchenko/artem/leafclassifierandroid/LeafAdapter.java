package com.ishchenko.artem.leafclassifierandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishchenko.artem.gfx.LeafImage;
import com.ishchenko.artem.gfx.LeafSpecies;
import com.ishchenko.artem.tools.LeafSpaceContainer;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Artem on 08.04.2018.
 */

public class LeafAdapter extends BaseExpandableListAdapter {

    private List<LeafSpaceContainer> spaces;
    private Context mContext;
    private ExpandableListView view;
    private AbstractLeafClassifierFragment fragment;

    public LeafAdapter(AbstractLeafClassifierFragment fragment, List<LeafSpaceContainer> spaces, ExpandableListView view) {
        this.fragment = fragment;
        this.mContext = fragment.getContext();
        this.spaces = spaces;
        this.view = view;
    }

    @Override
    public int getGroupCount() {
        return spaces.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return spaces.get(groupPosition).getLeafsName().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return spaces.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return spaces.get(groupPosition).getLeafsName().get(childPosition);
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

        convertView.setOnClickListener(v -> {
            if (isExpanded) {
                view.collapseGroup(groupPosition);
            } else {
                view.expandGroup(groupPosition);
            }
        });

        TextView textGroup = convertView.findViewById(R.id.textGroup);
        textGroup.setText(spaces.get(groupPosition).getSpaceName());
        TextView classText = fragment.getView().findViewById(R.id.classText);

        Button addInSpace = convertView.findViewById(R.id.add_in_space);

        addInSpace.setOnClickListener((e) -> {
            PickImageDialog.newInstance(new PickSetup()).setOnPickResult(r -> {

                LeafImage leafImage = new LeafImage(r.getBitmap(), r.getPath());
                List<String> path = Arrays.asList(r.getPath().split("/"));
                spaces.get(groupPosition).getLeafsName().add(path.get(path.size() - 1));
                LeafSpecies leafSpecies = LeafClassifier.getProjectEnv().getLeafSpecies().get(groupPosition);
                leafSpecies.addImage(leafImage);
                leafImage.setSpecies(leafSpecies);
                new FindTokensTask(fragment, fragment.getView(), leafImage).execute();
                classText.setText(leafImage.getSpecies().getName());
                this.notifyDataSetChanged();
                LeafClassifier.getProjectEnv().setModified();

            }).show(fragment.getFragmentManager());
        });

        Button editSpace = convertView.findViewById(R.id.edit_space);

        editSpace.setOnClickListener((e) -> {
            EditText classNameField = new EditText(fragment.getContext());
            new AlertDialog.Builder(fragment.getContext())
                    .setTitle("Class name")
                    .setMessage("Please enter a class name:")
                    .setView(classNameField)
                    .setPositiveButton("Enter", (dialog, whichButton) -> {
                        String spaceName = classNameField.getText().toString();
                        LeafSpecies lSpecies = LeafClassifier.getProjectEnv().getLeafSpecies().get(groupPosition);
                        lSpecies.setName(spaceName);
                        spaces.get(groupPosition).setSpaceName(spaceName);
                        this.notifyDataSetChanged();
                        LeafClassifier.getProjectEnv().setModified();
                    })
                    .setNegativeButton("Cancel", (dialog, whichButton) -> {
                    })
                    .show();
        });

        Button deleteSpace = convertView.findViewById(R.id.delete_space);

        deleteSpace.setOnClickListener((e) -> {
            new AlertDialog.Builder(fragment.getContext())
                    .setTitle("Confirm")
                    .setMessage("Are you sure? that want to delete " + textGroup.getText() + "?")
                    .setPositiveButton("Enter", (dialog, whichButton) -> {
                        LeafClassifier.getProjectEnv().getLeafSpecies().remove(groupPosition);
                        spaces.remove(groupPosition);
                        this.notifyDataSetChanged();
                        LeafClassifier.getProjectEnv().setModified();
                    })
                    .setNegativeButton("Cancel", (dialog, whichButton) -> {
                    })
                    .show();
        });

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
        String imageName = spaces.get(groupPosition).getLeafsName().get(childPosition);
        textChild.setText(imageName);

        ImageView imageView = fragment.getView().findViewById(R.id.imageView);
        TextView classText = fragment.getView().findViewById(R.id.classText);
        TextView nameText = fragment.getView().findViewById(R.id.nameText);
        TextView sizeText = fragment.getView().findViewById(R.id.sizeText);
        TextView tokensText = fragment.getView().findViewById(R.id.tokensText);
        convertView.setOnClickListener(v -> {
            LeafImage leaf = LeafClassifier.getProjectEnv().getLeafSpecies().get(groupPosition).getImage(childPosition);
            Bitmap image = leaf.getBitmap();
            updateLeafInfo(leaf, nameText, sizeText, tokensText, classText);
            imageView.setImageBitmap(image);
        });

        Button deleteLeaf = convertView.findViewById(R.id.delete_leaf);

        deleteLeaf.setOnClickListener((e) -> {
            new AlertDialog.Builder(fragment.getContext())
                    .setTitle("Confirm")
                    .setMessage("Are you sure? that want to delete " + textChild.getText() + "?")
                    .setPositiveButton("Enter", (dialog, whichButton) -> {
                        LeafClassifier.getProjectEnv().getLeafSpecies().get(groupPosition).getImages().remove(childPosition);
                        spaces.get(groupPosition).getLeafsName().remove(childPosition);
                        this.notifyDataSetChanged();
                        LeafClassifier.getProjectEnv().setModified();
                    })
                    .setNegativeButton("Cancel", (dialog, whichButton) -> {
                    })
                    .show();
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void updateLeafInfo(LeafImage leafImageForRecognizing, TextView nameText, TextView sizeText, TextView tokensText, TextView classText){
        nameText.setText(leafImageForRecognizing.getFileName().getName());
        sizeText.setText(String.valueOf(leafImageForRecognizing.getFileName().length()) + " " + fragment.getString(R.string.BYTES));
        tokensText.setText(String.valueOf(leafImageForRecognizing.numTokens()));
        nameText.setText(leafImageForRecognizing.getFileName().getName());
        classText.setText(leafImageForRecognizing.getSpecies().getName());
    }
}