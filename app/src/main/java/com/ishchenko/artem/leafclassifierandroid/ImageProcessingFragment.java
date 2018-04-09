package com.ishchenko.artem.leafclassifierandroid;

/**
 * Created by Artem on 24.03.2018.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ishchenko.artem.gfx.ImageProcessor;
import com.ishchenko.artem.gfx.LeafImage;
import com.ishchenko.artem.gfx.LeafSpecies;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static com.ishchenko.artem.leafclassifierandroid.LeafClassifier.projectEnv;

public class ImageProcessingFragment extends LeafClassifierFragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    public static final String title = "Image processing";

    int pageNumber;
    int backColor;
    LeafImage actualImage;
    ExpandableListView treePanel;
    List<Pair<String, List<String>>> spaces = new LinkedList<>();
    ;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_proceesing_fragment, null);

        Button addImage = view.findViewById(R.id.addImage);
        treePanel = view.findViewById(R.id.treePanel);
        LeafAdapter adapter = new LeafAdapter(getContext(), spaces);
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

                            actualImage = new LeafImage(r.getBitmap(), r.getPath());
                            spaces.get(spaceId).second.add(r.getPath());
                            ((LeafSpecies) projectEnv.getLeafSpecies().get(spaceId)).addImage(actualImage);

//                if (lImage != null && lImage.getImage() != null) {
                            // We add the leaf leafImage to the tree and to the
                            // project enviroment
//                LeafSpecies lSpecies = (LeafSpecies) treePanel.addChild(lImage);

                            // now add this Leaf leafImage to the species
//                if (lSpecies != null) {
//                    lSpecies.addImage(lImage);
//                }
                            projectEnv.setModified();
//                lrec.updateProjectEnv();
//                }
                        }).show(getActivity());
                    })
                    .setNegativeButton("Cancel", (dialog, whichButton) -> {
                    })
                    .show();


//             Create a file chooser
//                        JFileChooser fc = new JFileChooser();
//                        fc.addChoosableFileFilter(new ImageFilter());
//                        fc.setAccessory(new ImagePreview(fc));
//
//                        int returnVal = fc.showOpenDialog(lrec);
////
//                        if (returnVal == JFileChooser.APPROVE_OPTION) {
//                            File file = fc.getSelectedFile();
//
//                        }

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
        Button findTokens = view.findViewById(R.id.findTokens);
        SeekBar threshold = view.findViewById(R.id.threshold);
        SeekBar distance = view.findViewById(R.id.distance);
        SeekBar minLine = view.findViewById(R.id.minLine);
        ImageView imageView = view.findViewById(R.id.imageView);
        findTokens.setOnClickListener(e -> {
            findTokensProcess(view);
        });

        return view;
    }

    private void findTokensProcess(View view) {
        SeekBar threshold = view.findViewById(R.id.threshold);
        SeekBar distance = view.findViewById(R.id.distance);
        SeekBar minLine = view.findViewById(R.id.minLine);
        new MyTask(view, threshold.getProgress(), distance.getProgress(), minLine.getProgress()).execute();

    }

    @Override
    public String getTitle() {
        return title;
    }

    class MyTask extends AsyncTask<Void, String, Void> {
        View view;
        TextView progressText;
        ProgressBar progressBar;
        int threshold;
        int distance;
        int minLine;

        public MyTask(View view, int threshold, int distance, int minLine) {
            this.view = view;
            this.threshold = threshold;
            this.distance = distance;
            this.minLine = minLine;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (actualImage == null) {
                return null;
            }

            Button findTokens = view.findViewById(R.id.findTokens);
            progressText = view.findViewById(R.id.progressText);
            progressBar = view.findViewById(R.id.progressBar);

            // first we disable the button
            getActivity().runOnUiThread(() -> findTokens.setEnabled(false));

            // Now we perform the Image processing
            ImageProcessor imgProc = new ImageProcessor(actualImage.getImage(), ImageProcessingFragment.this.getContext());

//            imgControlBar.setValue(1);
            publishProgress("Edge detection...", "5");
//
            imgProc.edgeDetect(threshold * 10);
//            procImagePanel.setImage(imgProc.getImage(procIma gePanel));
            publishProgress("Thinning...", "20");

            imgProc.thinning();
//            procImagePanel.setImage(imgProc.getImage(procImagePanel));
            publishProgress("Line checking...", "40");

//            lrec.refresh();
            imgProc.checkLines(minLine * 10);
            publishProgress("Distance points...", "60");

//            lrec.setStatusField(getString("STATUS_DISTANCE"));
//            lrec.refresh();
            imgProc.markPoints(distance * 10);
//            procImagePanel.setImage(imgProc.getImage(procImagePanel));
            publishProgress("Searching tokens...", "80");

            // now we calculate the tokens of the image by calculating
            // the angles
//            lrec.refresh();
            imgProc.calcAngels();
//            procImagePanel.setImage(imgProc.getImage(procImagePanel));
//            imageView.setImageBitmap();setImage(imgProc.getImage());
            publishProgress("finished.", "100");

            // set the TextField for the amount of tokens
            ArrayList leafTokens = imgProc.getTokens();
            actualImage.setTokens(leafTokens);

//            updateProjectEnv();

//            lrec.setStatusField(getString("STATUS_FINISHED"));

            // at the end we enable the button again
            getActivity().runOnUiThread(() -> findTokens.setEnabled(true));

//            projectEnv.setModified();
//            lrec.updateProjectEnv();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressText.setText(values[0]);
            progressBar.setProgress(Integer.parseInt(values[1]));
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}