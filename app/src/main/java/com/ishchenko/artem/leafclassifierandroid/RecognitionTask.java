package com.ishchenko.artem.leafclassifierandroid;

import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ishchenko.artem.gfx.LeafImage;
import com.ishchenko.artem.gfx.LeafSpecies;
import com.ishchenko.artem.nnetwork.BackProp;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import static java.util.Collections.reverseOrder;

public class RecognitionTask extends AsyncTask<Void, String, Void> {
    private View view;
    private TextView progressText;
    private ProgressBar progressBar;
    LeafImage leafImageForRecognizing;
    private AbstractLeafClassifierFragment fragment;

    BiConsumer<String, String> publishProgress = (s, s2) -> publishProgress(s, s2);

    public RecognitionTask(AbstractLeafClassifierFragment fragment, View view, LeafImage leafImageForRecognizing) {
        this.view = view;
        progressText = view.findViewById(R.id.progressText);
        progressBar = view.findViewById(R.id.progressBar);
        this.leafImageForRecognizing = leafImageForRecognizing;
        this.fragment = fragment;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        TableLayout tableLayout = view.findViewById(R.id.probabilitiesTable);
        fragment.getActivity().runOnUiThread(() -> {
            tableLayout.removeAllViews();
            TableRow row = getTableRow("Leaf Name", "Probability");
            tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        });

        // Now we recognize the image with the trained neutronal network
        BackProp nNetwork = LeafClassifier.getProjectEnv().getNetwork();

        double[] resultID = nNetwork.propagate(leafImageForRecognizing.getTokens(nNetwork.numInput()));

        /*
        for(int i=0; i < nNetwork.numOutput(); i++)
        {
          System.out.println("OUT["+i+"]: "+test[i]);
        }
        */

        publishProgress("classifying.", "85");

        ArrayList leafSpecies = LeafClassifier.getProjectEnv().getLeafSpecies();
        double error = 0.0;

        int size = leafSpecies.size();

        TreeMap<String, Double> results = new TreeMap<>();

        for (int i = 0; i < size; i++, error = 0.0) {
            LeafSpecies lSpecies = (LeafSpecies) leafSpecies.get(i);

            //System.out.println("LeafSpecies "+i+" ["+lSpecies.getName()+"]:");

            double[] ID = lSpecies.getID();

            for (int j = 0; j < nNetwork.numOutput(); j++) {
                //System.out.println(" ["+j+"]: "+ID[j]);

                if (ID[j] >= resultID[j]) {
                    error += ID[j] - resultID[j];
                } else error += resultID[j] - ID[j];

            }
            double probabilityValue = (100.0 - (100 / size) * error);
            results.put(lSpecies.getName(), probabilityValue);
        }

        results.entrySet().stream().
                sorted(reverseOrder(Map.Entry.comparingByValue())).forEach((entry) -> {
            TableRow row = getTableRow(entry.getKey(), String.format("%.2f", entry.getValue()));
            fragment.getActivity().runOnUiThread(() -> {
                tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            });
        });
        publishProgress("finished.", "100");

        return null;
    }

    private TableRow getTableRow(String left, String right) {
        TableRow row = new TableRow(view.getContext());
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView space = new TextView(view.getContext());
        space.setGravity(Gravity.CENTER_HORIZONTAL);
        space.setText(left);
        space.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        row.addView(space);

        TextView probability = new TextView(view.getContext());
        probability.setText(right);
        probability.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        probability.setGravity(Gravity.CENTER_HORIZONTAL);
        row.addView(probability);
        return row;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        progressText.setText(values[0]);
        progressBar.setProgress(Integer.parseInt(values[1]));
    }
}
