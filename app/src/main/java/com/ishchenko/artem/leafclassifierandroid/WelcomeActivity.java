package com.ishchenko.artem.leafclassifierandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ishchenko.artem.tools.ProjectEnv;

import java.io.File;

public class WelcomeActivity extends AppCompatActivity {
    public static final String CACHE_NAME = "LeafRecognizingCache";
    public static ProjectEnv projectEnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    File directory = getFilesDir();
                    File file = new File(directory, CACHE_NAME);
                    if (file.exists() && projectEnv == null) {
                        projectEnv = new ProjectEnv();
                        projectEnv.Open(file);
                    }

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
