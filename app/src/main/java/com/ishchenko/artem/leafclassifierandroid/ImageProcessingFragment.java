package com.ishchenko.artem.leafclassifierandroid;

/**
 * Created by Artem on 24.03.2018.
 */

import android.app.AlertDialog;
import android.graphics.Color;
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
import android.widget.ExpandableListView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.ishchenko.artem.gfx.LeafImage;
import com.ishchenko.artem.gfx.LeafSpecies;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.ishchenko.artem.leafclassifierandroid.LeafClassifier.projectEnv;

public class ImageProcessingFragment extends LeafClassifierFragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    public static final String title = "Image processing";

    int pageNumber;
    int backColor;
    ExpandableListView treePanel;
//    List<Pair<String, List<String>>> spaces = new LinkedList<>();


    static ImageProcessingFragment newInstance(int page) {
        ImageProcessingFragment imageProcessingFragment = new ImageProcessingFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        imageProcessingFragment.setArguments(arguments);
        return imageProcessingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);

        Random rnd = new Random();
        backColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_proceesing_fragment, null);

        Button addImage = view.findViewById(R.id.addImage);
        treePanel = view.findViewById(R.id.treePanel);
        List<Pair<String, List<String>>> spaces = projectEnv.getLeafSpecies().stream()
                .map(leafSpecies -> Pair.create(leafSpecies.getName(),
                        leafSpecies.getImages().stream().map(leafImage -> leafImage.getFileName().getName()).collect(Collectors.toList()))).collect(Collectors.toList());
        LeafAdapter adapter = new LeafAdapter(getContext(), spaces, view);
        treePanel.setAdapter(adapter);

        addImage.setOnClickListener((e) -> {

            String[] spacesNames = new String[spaces.size()];
            for (int i = 0; i < spaces.size(); i++) {
                spacesNames[i] = spaces.get(i).first;
            }
            ArrayAdapter<String> adapterArray = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spacesNames);
            adapterArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner classNameField = new Spinner(getContext());
            classNameField.setAdapter(adapterArray);

            new AlertDialog.Builder(this.getContext())
                    .setTitle("Choose a class")
                    .setMessage("Please choose a class for a leaf:")
                    .setView(classNameField)
                    .setPositiveButton("Enter", (dialog, whichButton) -> {
                        int spaceId = classNameField.getSelectedItemPosition();

                        PickImageDialog.build(new PickSetup()).setOnPickResult(r -> {

                            LeafImage leafImage = new LeafImage(r.getBitmap(), r.getPath());
                            List<String> path = Arrays.asList(r.getPath().split("/"));
                            spaces.get(spaceId).second.add(path.get(path.size() - 1));
                            projectEnv.getLeafSpecies().get(spaceId).addImage(leafImage);
                            findTokensProcess(view, leafImage);

                            projectEnv.setModified();

                        }).show(getActivity());
                    })
                    .setNegativeButton("Cancel", (dialog, whichButton) -> {
                    })
                    .show();

        });

        Button addSpace = view.findViewById(R.id.addSpace);

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
                        projectEnv.addLeafSpecies(lSpecies);
                        projectEnv.setModified();
                    })
                    .setNegativeButton("Cancel", (dialog, whichButton) -> {
                    })
                    .show();
        });

        Button rename = view.findViewById(R.id.rename);
        Button delete = view.findViewById(R.id.delete);
        SeekBar threshold = view.findViewById(R.id.threshold);
        SeekBar distance = view.findViewById(R.id.distance);
        SeekBar minLine = view.findViewById(R.id.minLine);


        return view;
    }

    private void findTokensProcess(View view, LeafImage leafImage) {
        SeekBar threshold = view.findViewById(R.id.threshold);
        SeekBar distance = view.findViewById(R.id.distance);
        SeekBar minLine = view.findViewById(R.id.minLine);
        new FindTokensTask(this, view, threshold.getProgress(), distance.getProgress(), minLine.getProgress(), leafImage).execute();
    }

    @Override
    public String getTitle() {
        return title;
    }

}