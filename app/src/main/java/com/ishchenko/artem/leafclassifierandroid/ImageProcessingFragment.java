package com.ishchenko.artem.leafclassifierandroid;

/**
 * Created by Artem on 24.03.2018.
 */

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ishchenko.artem.gfx.LeafImage;
import com.ishchenko.artem.gfx.LeafSpecies;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.Random;

public class ImageProcessingFragment extends LeafClassifierFragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    public static final String title = "Image processing";

    int pageNumber;
    int backColor;
    Bitmap leafImage;
    String leafPath;

    static ImageProcessingFragment newInstance(int page) {
        ImageProcessingFragment imageProcessingFragment = new ImageProcessingFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        imageProcessingFragment.setArguments(arguments);
        return imageProcessingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);

        Random rnd = new Random();
        backColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_proceesing_fragment, null);

        Button addImage = view.findViewById(R.id.addImage);

        addImage.setOnClickListener((e) -> {
            PickImageDialog.build(new PickSetup()).setOnPickResult(r -> {
                leafImage = r.getBitmap();
                leafPath = r.getPath();
                LeafImage lImage = new LeafImage(leafImage, leafPath);


                if (lImage != null && lImage.getImage() != null) {
                    // We add the leaf leafImage to the tree and to the
                    // project enviroment
//                LeafSpecies lSpecies = (LeafSpecies) treePanel.addChild(lImage);

                    // now add this Leaf leafImage to the species
//                if (lSpecies != null) {
//                    lSpecies.addImage(lImage);
//                }
                    LeafClassifier.projectEnv.setModified();
//                lrec.updateProjectEnv();
                }
            }).show(getActivity());
//             Create a file chooser
//                        JFileChooser fc = new JFileChooser();
//                        fc.addChoosableFileFilter(new ImageFilter());
//                        fc.setAccessory(new ImagePreview(fc));
//
//                        int returnVal = fc.showOpenDialog(lrec);
////
//                        if (returnVal == JFileChooser.APPROVE_OPTION) {
//                            File file = fc.getSelectedFile();
//
//                        }

        });

        Button addSpave = view.findViewById(R.id.addSpace);
        Button rename = view.findViewById(R.id.rename);
        Button delete = view.findViewById(R.id.delete);


        return view;
    }

    @Override
    public String getTitle() {
        return title;
    }
}