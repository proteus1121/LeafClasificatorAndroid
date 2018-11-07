package com.ishchenko.artem.leafclassifierandroid;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ishchenko.artem.tools.ProjectEnv;

import java.io.File;

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
        publishProgress("Reading leaf library", "5");
        File file = new File(directory, WelcomeActivity.CACHE_NAME);
        if (file.exists() && WelcomeActivity.projectEnv == null) {
            WelcomeActivity.projectEnv = new ProjectEnv();
            WelcomeActivity.projectEnv.Open(file);
            publishProgress("Completed", "100");
        }
        return null;
    }
}
