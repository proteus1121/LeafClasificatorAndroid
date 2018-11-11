package com.ishchenko.artem.leafclassifierandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import net.windward.android.awt.Image;

import com.ishchenko.artem.gfx.ImageProcessor;

import net.windward.android.awt.image.BufferedImage;
import net.windward.android.imageio.ImageIO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {
    private ImageUtils()
    {}

    public static Bitmap convertFromImageToBitmap(Image image)
    {
        BufferedImage bufferedImage = ImageProcessor.toBufferedImage(image);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            return BitmapFactory.decodeByteArray(imageInByte, 0, imageInByte.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
