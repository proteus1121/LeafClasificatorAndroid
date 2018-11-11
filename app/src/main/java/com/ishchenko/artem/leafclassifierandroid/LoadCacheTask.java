package com.ishchenko.artem.leafclassifierandroid;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ishchenko.artem.tools.ProjectEnv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LoadCacheTask extends AsyncTask<Void, String, Void> {

    private TextView textView;
    private ProgressBar progressBar;
    private File directory;

    public LoadCacheTask(TextView textView, ProgressBar progressBar, File directory) {
        this.textView = textView;
        this.progressBar = progressBar;
        this.directory = directory;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        textView.setText(values[0]);
        progressBar.setProgress(Integer.parseInt(values[1]));
    }

    @Override
    protected Void doInBackground(Void... voids) {
        publishProgress("Reading leaf library...", "5");
        File file = new File(directory, WelcomeActivity.CACHE_NAME);
        WelcomeActivity.projectEnv = new ProjectEnv();
        if (!file.exists()) {
            Resources r = textView.getContext().getResources();
            InputStream is = r.openRawResource(R.raw.dataset);
            try {
                try (OutputStream output = new FileOutputStream(file)) {
                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
                    int read;

                    while ((read = is.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }

                    output.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        publishProgress("Completed...", "100");
        WelcomeActivity.projectEnv.Open(file);
        return null;
    }
}
