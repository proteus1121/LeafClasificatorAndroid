package com.ishchenko.artem.leafclassifierandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    Void execute = new LoadCacheTask(textView, progressBar, getFilesDir()).execute().get();
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
