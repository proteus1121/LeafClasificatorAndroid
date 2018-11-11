package com.ishchenko.artem.leafclassifierandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.ishchenko.artem.gfx.ImageProcessor;
import com.ishchenko.artem.gfx.LeafImage;

import net.windward.android.awt.image.BufferedImage;
import net.windward.android.imageio.ImageIO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class FindTokensUtils {

    public static void findTokens(View view, BiConsumer<String, String> publishProgress, LeafImage leafImage, AbstractLeafClassifierFragment fragment){

        publishProgress.accept("Load Image...", "5");
        // Now we perform the Image processing
        ImageProcessor imgProc = new ImageProcessor(leafImage.getImage(), view.getContext());

        SeekBar threshold = view.findViewById(R.id.threshold);
        SeekBar distance = view.findViewById(R.id.distance);
        SeekBar minLine = view.findViewById(R.id.minLine);
        ImageView imageView = view.findViewById(R.id.imageView);

        publishProgress.accept("Edge detection...", "10");
        imgProc.edgeDetect(threshold.getProgress() * 10);

        publishProgress.accept("Thinning...", "20");

        imgProc.thinning();
        publishProgress.accept("Line checking...", "40");

        imgProc.checkLines(minLine.getProgress() * 10);
        publishProgress.accept("Distance points...", "60");

        imgProc.markPoints(distance.getProgress() * 10);
        publishProgress.accept("Searching tokens...", "70");

        // now we calculate the tokens of the image by calculating
        // the angles
        imgProc.calcAngels();

        // set the TextField for the amount of tokens
        ArrayList leafTokens = imgProc.getTokens();
        leafImage.setTokens(leafTokens);

        Bitmap bitmap = ImageUtils.convertFromImageToBitmap(imgProc.getImage());
        leafImage.setBitmap(bitmap);
        fragment.getActivity().runOnUiThread(() -> imageView.setImageBitmap(bitmap));
        publishProgress.accept("finished.", "100");
    }
}
