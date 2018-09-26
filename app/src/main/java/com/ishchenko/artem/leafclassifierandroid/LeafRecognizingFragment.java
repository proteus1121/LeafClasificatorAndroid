package com.ishchenko.artem.leafclassifierandroid;

/**
 * Created by Artem on 24.03.2018.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ishchenko.artem.gfx.LeafImage;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import mehdi.sakout.fancybuttons.FancyButton;

public class LeafRecognizingFragment extends AbstractLeafClassifierFragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private static final String title = "Leaf Recognizing";

    int pageNumber;
    LeafImage leafImageForRecognizing;

    static LeafRecognizingFragment newInstance(int page) {
        LeafRecognizingFragment leafRecognizingFragment = new LeafRecognizingFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        leafRecognizingFragment.setArguments(arguments);
        return leafRecognizingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leaf_recognizing_fragment, null);
        FancyButton addImage = view.findViewById(R.id.addImage);
        FancyButton recognizeImage = view.findViewById(R.id.recognize);
        FancyButton findTokens = view.findViewById(R.id.findTokens);
        ImageView imageView = view.findViewById(R.id.imageView);

        addImage.setOnClickListener((e) -> {
            PickImageDialog.build(new PickSetup()).setOnPickResult(r -> {
                leafImageForRecognizing = new LeafImage(r.getBitmap(), r.getPath());
                imageView.setImageBitmap(leafImageForRecognizing.getBitmap());
            }).show(getActivity());

            LeafClassifier.getProjectEnv().setModified();
        });

        findTokens.setOnClickListener((e) -> {
            if (leafImageForRecognizing == null)
            {
                return;
            }
            new FindTokensTask(this, view, leafImageForRecognizing).execute();
        });

        recognizeImage.setOnClickListener((e) -> {
            if (leafImageForRecognizing == null || LeafClassifier.getProjectEnv() == null)
            {
                return;
            }
            new RecognitionTask(this, view, leafImageForRecognizing).execute();
        });

        return view;
    }

    @Override
    public String getTitle() {
        return title;
    }
}