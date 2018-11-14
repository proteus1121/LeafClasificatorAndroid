package com.ishchenko.artem.leafclassifierandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ishchenko.artem.tools.ProjectEnv;

public class WelcomeActivity extends AppCompatActivity {
    public static final String CACHE_NAME = "LeafRecognizingCache";
    public static ProjectEnv projectEnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        View rootView = getWindow().getDecorView().getRootView();

        TextView textView = rootView.findViewById(R.id.progressText);
        ProgressBar progressBar = rootView.findViewById(R.id.progressBar);

        Activity ac = this;
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    Void execute = new LoadCacheTask(textView, progressBar, getFilesDir(), ac).execute().get();
                } catch (Exception ignored) {

                } finally {

                    Intent i = new Intent(WelcomeActivity.this,
                            LeafClassifier.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
