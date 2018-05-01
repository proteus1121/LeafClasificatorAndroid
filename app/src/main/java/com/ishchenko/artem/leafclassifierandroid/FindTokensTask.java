package com.ishchenko.artem.leafclassifierandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ishchenko.artem.gfx.ImageProcessor;
import com.ishchenko.artem.gfx.LeafImage;

import net.windward.android.awt.image.BufferedImage;
import net.windward.android.imageio.ImageIO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Artem on 01.05.2018.
 */
class FindTokensTask extends AsyncTask<Void, String, Void> {
    private ImageProcessingFragment imageProcessingFragment;
    private View view;
    private TextView progressText;
    private ProgressBar progressBar;
    private int threshold;
    private int distance;
    private int minLine;
    private LeafImage leafImage;

    public FindTokensTask(ImageProcessingFragment imageProcessingFragment, View view, int threshold, int distance, int minLine, LeafImage leafImage) {
        this.imageProcessingFragment = imageProcessingFragment;
        this.view = view;
        this.threshold = threshold;
        this.distance = distance;
        this.minLine = minLine;
        this.leafImage = leafImage;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        Button findTokens = view.findViewById(R.id.findTokens);
        progressText = view.findViewById(R.id.progressText);
        progressBar = view.findViewById(R.id.progressBar);

        // first we disable the button
        imageProcessingFragment.getActivity().runOnUiThread(() -> findTokens.setEnabled(false));

        // Now we perform the Image processing
        ImageProcessor imgProc = new ImageProcessor(leafImage.getImage(), imageProcessingFragment.getContext());

        publishProgress("Edge detection...", "5");

        imgProc.edgeDetect(threshold * 10);
        publishProgress("Thinning...", "20");

        imgProc.thinning();
        publishProgress("Line checking...", "40");

        imgProc.checkLines(minLine * 10);
        publishProgress("Distance points...", "60");

        imgProc.markPoints(distance * 10);
        publishProgress("Searching tokens...", "80");

        // now we calculate the tokens of the image by calculating
        // the angles
        imgProc.calcAngels();

        publishProgress("finished.", "100");

        // set the TextField for the amount of tokens
        ArrayList leafTokens = imgProc.getTokens();
        leafImage.setTokens(leafTokens);

        ImageView imageView = view.findViewById(R.id.imageView);
        BufferedImage bufferedImage = ImageProcessor.toBufferedImage(imgProc.getImage());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            Bitmap bmp = BitmapFactory.decodeByteArray(imageInByte, 0, imageInByte.length);
            leafImage.setBitmap(bmp);
            imageProcessingFragment.getActivity().runOnUiThread(() -> imageView.setImageBitmap(bmp));
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageProcessingFragment.getActivity().runOnUiThread(() -> findTokens.setEnabled(true));

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
