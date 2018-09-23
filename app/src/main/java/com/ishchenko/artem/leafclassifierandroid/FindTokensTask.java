package com.ishchenko.artem.leafclassifierandroid;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ishchenko.artem.gfx.LeafImage;

import java.util.function.BiConsumer;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Artem on 01.05.2018.
 */
class FindTokensTask extends AsyncTask<Void, String, Void> {
    private AbstractLeafClassifierFragment fragment;
    private View view;
    private TextView progressText;
    private ProgressBar progressBar;
    private FancyButton findTokens;
    private LeafImage leafImage;

    BiConsumer<String, String> publishProgress = (s, s2) -> publishProgress(s, s2);

    public FindTokensTask(AbstractLeafClassifierFragment fragment, View view, LeafImage leafImage) {
        this.fragment = fragment;
        this.view = view;
        this.leafImage = leafImage;

        this.findTokens = view.findViewById(R.id.findTokens);
        this.progressText = view.findViewById(R.id.progressText);
        this.progressBar = view.findViewById(R.id.progressBar);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        // first we disable the button
        fragment.getActivity().runOnUiThread(() -> findTokens.setEnabled(false));
        FindTokensUtils.findTokens(view, publishProgress, leafImage, fragment);

        fragment.getActivity().runOnUiThread(() -> findTokens.setEnabled(true));

        TextView nameText = view.findViewById(R.id.nameText);
        TextView sizeText = view.findViewById(R.id.sizeText);
        TextView tokensText = view.findViewById(R.id.tokensText);
        fragment.getActivity().runOnUiThread(() -> updateLeafInfo(leafImage, nameText, sizeText, tokensText));
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

    private void updateLeafInfo(LeafImage leafImageForRecognizing, TextView nameText, TextView sizeText, TextView tokensText){
        nameText.setText(leafImageForRecognizing.getFileName().getName());
        sizeText.setText(String.valueOf(leafImageForRecognizing.getFileName().length()) + " " + fragment.getString(R.string.BYTES));
        tokensText.setText(String.valueOf(leafImageForRecognizing.numTokens()));
        nameText.setText(leafImageForRecognizing.getFileName().getName());
    }
}
