package com.ishchenko.artem.leafclassifierandroid;

import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ishchenko.artem.gfx.LeafImage;
import com.ishchenko.artem.gfx.LeafSpecies;
import com.ishchenko.artem.nnetwork.BackProp;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Artem on 01.05.2018.
 */
class TrainNetworkTask extends AsyncTask<Void, String, Void> {
    private View view;
    private TextView progressText;
    private ProgressBar progressBar;
    private TextView error;

    public TrainNetworkTask(View view) {
        this.view = view;
        progressText = view.findViewById(R.id.progressText);
        progressBar = view.findViewById(R.id.progressBar);
        error = view.findViewById(R.id.error);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        // get the properties of the network first

        publishProgress("Initializing...", "0");

        EditText inputNeurons = view.findViewById(R.id.inputNeurons);
        EditText hiddenNeurons = view.findViewById(R.id.hiddenNeurons);
        EditText outputNeurons = view.findViewById(R.id.outputNeurons);
        EditText learnRateField = view.findViewById(R.id.learnRate);
        EditText momentumField = view.findViewById(R.id.momentum);
        EditText maxSteps = view.findViewById(R.id.maxSteps);

        GraphView graph = view.findViewById(R.id.graph);
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{});
        graph.addSeries(series);

        int inputs = Integer.parseInt(inputNeurons.getText().toString());
        int hiddens = Integer.parseInt(hiddenNeurons.getText().toString());
        int outputs = Integer.parseInt(outputNeurons.getText().toString());
        double learnRate = Double.parseDouble(learnRateField.getText().toString());
        double momentum = Double.parseDouble(momentumField.getText().toString());
        int steps = Integer.parseInt(maxSteps.getText().toString());

        // change progressbar
//        netProgressBar.setMaximum(steps);

        // Now we create a new BackProp object
        // for doing the neuronal network stuff
        BackProp nNetwork = new BackProp(inputs, hiddens, outputs);

        // Set the network in the project environment
        LeafClassifier.getProjectEnv().setNetwork(nNetwork);

        // set learnrate & momentum to user settings
        nNetwork.setAlpha(learnRate);
        nNetwork.setMomentum(momentum);

        ArrayList<LeafSpecies> leafSpecies = LeafClassifier.getProjectEnv().getLeafSpecies();

        for (int i = 0; i < leafSpecies.size(); i++) {
            LeafSpecies lSpecies = leafSpecies.get(i);

            double[] outputVals = new double[outputs];

            // create a random ID as long as the output neurons
            for (int j = 0; j < outputs; j++) {
                if (j <= i) {
                    outputVals[j] = 1.0;
                } else outputVals[j] = 0.0;
            }

            lSpecies.setID(outputVals);
        }

        double sumError = 0;

        // Now we create the ArrayList of all Images that
        // will be shuffled later

        ArrayList imageList = new ArrayList();

        for (int j = 0; j < leafSpecies.size(); j++) {
            LeafSpecies lSpecies = leafSpecies.get(j);

            for (int k = 0; k < lSpecies.numImages(); k++) {
                LeafImage lImage = lSpecies.getImage(k);

                // set the Species of this image now!
                lImage.setSpecies(lSpecies);

                imageList.add(lImage);
            }
        }

        // steps for training
        for (int i = 0; i < steps; i++) {
            sumError = 0.0;

            int progress = (i * 100) / steps;
            publishProgress("Training... step " + i + "/" + steps, String.valueOf(progress));

            // now we have to shuffle the tokenList to get
            // a randomness in the training process
            Collections.shuffle(imageList);

            for (int j = 0; j < imageList.size(); j++) {
                LeafImage lImage = (LeafImage) imageList.get(j);

                sumError += nNetwork.learnVector(lImage.getTokens(inputs), lImage.getSpecies().getID());
            }

//            errorPanel.addError(sumError);
            series.appendData(new DataPoint(i, sumError), true, 100);
        }
        error.setText(String.valueOf(sumError));
        publishProgress("finished", "100");

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
