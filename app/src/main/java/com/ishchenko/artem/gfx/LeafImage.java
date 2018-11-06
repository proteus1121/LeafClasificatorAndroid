/*
 * Leaves Recognition - a neuronal network based recognition of leaf images
 * Copyright (C) 2001 Jens Langner, LightSpeed Communications GbR
 *
 * LightSpeed Communications GbR
 * Lannerstrasse 1
 * 01219 Dresden
 * Germany
 * http://www.light-speed.de/
 * <Jens.Langner@light-speed.de>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * $Id$
 */

package com.ishchenko.artem.gfx;

import android.graphics.Bitmap;

import com.ishchenko.artem.tools.Utils;

import net.windward.android.awt.Image;
import net.windward.android.awt.image.BufferedImage;
import net.windward.android.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import lombok.EqualsAndHashCode;

/**
 * class that represents a image of a single leaf with all
 * tokens and information about this image.
 */
@EqualsAndHashCode
public class LeafImage {
    private Bitmap imageBitmap = null;
    private ArrayList tokens = null;
    private String filename = null;
    private LeafSpecies species = null;

    public LeafImage(Bitmap imageBitmap, String path) {
        this.imageBitmap = imageBitmap;
        filename = path;
        tokens = new ArrayList();
    }

    public Bitmap getBitmap() {
        return imageBitmap;
    }

    public void setBitmap(Bitmap btmp) {
        imageBitmap = btmp;
    }

    public void setSpecies(LeafSpecies species) {
        this.species = species;
    }

    public LeafSpecies getSpecies() {
        return species;
    }

    public Image getImage() {
        try {
            BufferedImage bimg = ImageIO.read(new File(filename));
            int width = 400;
            int height = (bimg.getHeight() / bimg.getWidth()) * width;
            // resize original image
            return bimg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addToken(LeafToken token) {
        tokens.add(token);
    }

    public void setTokens(ArrayList tokens) {
        this.tokens = tokens;
    }

    /**
     * getTokens()
     * <p>
     * returns the tokens in a double[] vector
     */
    public double[] getTokens(int inputs) {
        double[] tokenVector = new double[inputs];

        for (int i = 0; i < inputs; i++) {
            if (i < tokens.size()) {
                LeafToken token = (LeafToken) tokens.get(i);

                // Fill the tokenVector first with the cosinus
                // and then with the sinus value
                if (i % 2 == 0) tokenVector[i] = token.getCOS();
                else tokenVector[i] = token.getSIN();

            } else tokenVector[i] = -1.0;
        }

        return tokenVector;
    }

    /**
     * getTokens()
     * <p>
     * returns the tokens in a double[] vector
     */
    public double[] getTokens() {
        return getTokens(tokens.size() * 2);
    }

    public LeafToken getToken(int idx) {
        return (LeafToken) tokens.get(idx);
    }

    public int numTokens() {
        return tokens.size();
    }

    public void clearTokens() {
        tokens.clear();
    }

    public File getFileName() {
        return new File(filename);
    }

    public String getName() {
        return filename;
    }

    public void setName(String filename) {
        File oldFileName = getFileName();
        String newFileName = getFileName().getParent() + File.separator + filename + Utils.getFileExtension(oldFileName);
        File newFile = new File(newFileName);
        oldFileName.renameTo(newFile);
        this.filename = newFileName;
    }
}