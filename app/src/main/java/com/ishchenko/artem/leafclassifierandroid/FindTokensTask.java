package com.ishchenko.artem.leafclassifierandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ishchenko.artem.gfx.ImageProcessor;
import com.ishchenko.artem.gfx.LeafImage;

import net.windward.android.awt.image.BufferedImage;
import net.windward.android.imageio.ImageIO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * Created by Artem on 01.05.2018.
 */
class FindTokensTask extends AsyncTask<Void, String, Void> {
    private AbstractLeafClassifierFragment fragment;
    private View view;
    private TextView progressText;
    private ProgressBar progressBar;
    private Button findTokens;
    private int threshold;
    private int distance;
    private int minLine;
    private LeafImage leafImage;

    BiConsumer<String, String> publishProgress = (s, s2) -> publishProgress(s, s2);

    public FindTokensTask(AbstractLeafClassifierFragment imageProcessingFragment, View view, LeafImage leafImage) {
        this.fragment = imageProcessingFragment;
        this.view = view;

        SeekBar threshold = view.findViewById(R.id.threshold);
        SeekBar distance = view.findViewById(R.id.distance);
        SeekBar minLine = view.findViewById(R.id.minLine);
        this.threshold = threshold.getProgress();
        this.distance = distance.getProgress();
        this.minLine = minLine.getProgress();
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
