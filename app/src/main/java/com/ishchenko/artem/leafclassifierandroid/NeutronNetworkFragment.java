package com.ishchenko.artem.leafclassifierandroid;

/**
 * Created by Artem on 24.03.2018.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        Button train = view.findViewById(R.id.train);
        train.setOnClickListener(view1 -> new TrainNetworkTask(view).execute());
        return view;
    }

    @Override
    public String getTitle() {
        return title;
    }
}