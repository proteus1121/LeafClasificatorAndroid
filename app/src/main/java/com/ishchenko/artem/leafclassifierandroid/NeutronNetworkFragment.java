package com.ishchenko.artem.leafclassifierandroid;

/**
 * Created by Artem on 24.03.2018.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ishchenko.artem.nnetwork.BackProp;

import mehdi.sakout.fancybuttons.FancyButton;

public class NeutronNetworkFragment extends AbstractLeafClassifierFragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    private static final String title = "Neutron Network";

    static NeutronNetworkFragment newInstance(int page) {
        NeutronNetworkFragment imageProcessingFragment = new NeutronNetworkFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        imageProcessingFragment.setArguments(arguments);
        return imageProcessingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.neutron_network_fragment, null);
        FancyButton train = view.findViewById(R.id.train);
        TextView error = view.findViewById(R.id.error);
        TextView leafImages = view.findViewById(R.id.leafImages);
//        TextView leafSpecies = view.findViewById(R.id.leafSpecies);
        TextView maxTokens = view.findViewById(R.id.maxTokens);

        String textError;
        train.setOnClickListener(view1 -> {
            new TrainNetworkTask(view).execute();
        });

        updateViews(error, leafImages, null, maxTokens);

        return view;
    }

    private void updateViews(TextView error, TextView leafImages, TextView leafSpecies, TextView maxTokens) {
        String textError;
        BackProp network = LeafClassifier.getProjectEnv().getNetwork();
        if (network != null) {
            textError = String.valueOf(network.getAbsError());
            error.setText(textError);
        }
        leafImages.setText(String.valueOf(LeafClassifier.getProjectEnv().numLeafImages()));
//        leafSpecies.setText(String.valueOf(LeafClassifier.getProjectEnv().getLeafSpecies()));
        maxTokens.setText(String.valueOf(LeafClassifier.getProjectEnv().getMaxToken()));
    }

    @Override
    public String getTitle() {
        return title;
    }
}