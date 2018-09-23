package com.ishchenko.artem.leafclassifierandroid;

/**
 * Created by Artem on 24.03.2018.
 */

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.ishchenko.artem.gfx.LeafImage;
import com.ishchenko.artem.gfx.LeafSpecies;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import mehdi.sakout.fancybuttons.FancyButton;

public class LeafLibraryFragment extends AbstractLeafClassifierFragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    public static final String title = "Leaf Library";

    int pageNumber;
    ExpandableListView treePanel;

    static LeafLibraryFragment newInstance(int page) {
        LeafLibraryFragment leafLibraryFragment = new LeafLibraryFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        leafLibraryFragment.setArguments(arguments);
        return leafLibraryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leaf_library_fragment, null);

        treePanel = view.findViewById(R.id.treePanel);
        List<Pair<String, List<String>>> spaces = LeafClassifier.getProjectEnv().getLeafSpecies().stream()
                .map(leafSpecies -> Pair.create(leafSpecies.getName(),
                        leafSpecies.getImages().stream().map(leafImage -> leafImage.getFileName().getName()).collect(Collectors.toList()))).collect(Collectors.toList());
        LeafAdapter adapter = new LeafAdapter(this, spaces, treePanel);
        treePanel.setAdapter(adapter);

        FancyButton addSpace = view.findViewById(R.id.addSpace);

        addSpace.setOnClickListener((e) -> {
            EditText classNameField = new EditText(this.getContext());
            new AlertDialog.Builder(this.getContext())
                    .setTitle("Class name")
                    .setMessage("Please enter a class name:")
                    .setView(classNameField)
                    .setPositiveButton("Enter", (dialog, whichButton) -> {
                        String spaceName = classNameField.getText().toString();
                        LeafSpecies lSpecies = new LeafSpecies(spaceName);
                        spaces.add(Pair.create(spaceName, new LinkedList<>()));
                        LeafClassifier.getProjectEnv().addLeafSpecies(lSpecies);
                        LeafClassifier.getProjectEnv().setModified();
                    })
                    .setNegativeButton("Cancel", (dialog, whichButton) -> {
                    })
                    .show();
        });

        Button save = view.findViewById(R.id.save);

        save.setOnClickListener((e) -> {
            File directory = view.getContext().getFilesDir();
            File file = new File(directory, LeafClassifier.CACHE_NAME);
            LeafClassifier.getProjectEnv().Save(file);
        });

        return view;
    }

    @Override
    public String getTitle() {
        return title;
    }
}