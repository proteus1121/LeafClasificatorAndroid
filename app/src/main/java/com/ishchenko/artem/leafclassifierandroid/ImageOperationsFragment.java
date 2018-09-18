package com.ishchenko.artem.leafclassifierandroid;

/**
 * Created by Artem on 24.03.2018.
 */

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ishchenko.artem.gfx.ImageProcessor;
import com.ishchenko.artem.gfx.LeafImage;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import mehdi.sakout.fancybuttons.FancyButton;

public class ImageOperationsFragment extends AbstractLeafClassifierFragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private static final String title = "Image operations";

    int pageNumber;
    LeafImage leafImageForRecognizing;

    static ImageOperationsFragment newInstance(int page) {
        ImageOperationsFragment imageProcessingFragment = new ImageOperationsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        imageProcessingFragment.setArguments(arguments);
        return imageProcessingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_operations_fragment, null);
        Button addImage = view.findViewById(R.id.addImage);
        FancyButton recognizeImage = view.findViewById(R.id.recognize);
        ImageView imageView = view.findViewById(R.id.imageView);

        addImage.setOnClickListener((e) -> {
            PickImageDialog.build(new PickSetup()).setOnPickResult(r -> {
                leafImageForRecognizing = new LeafImage(r.getBitmap(), r.getPath());
                imageView.setImageBitmap(leafImageForRecognizing.getBitmap());
            }).show(getActivity());

            LeafClassifier.getProjectEnv().setModified();
        });

        recognizeImage.setOnClickListener((e) -> {
            new RecognitionTask(this, view, leafImageForRecognizing).execute();
        });

        return view;
    }

    @Override
    public String getTitle() {
        return title;
    }
}