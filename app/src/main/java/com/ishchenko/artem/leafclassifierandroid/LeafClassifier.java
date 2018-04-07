package com.ishchenko.artem.leafclassifierandroid;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.ishchenko.artem.tools.ProjectEnv;

import java.util.LinkedHashMap;
import java.util.Map;


public class LeafClassifier extends FragmentActivity {

    static final String TAG = "myLogs";
    static final int PAGE_COUNT = 3;

    ViewPager pager;
    PagerAdapter pagerAdapter;
    public static ProjectEnv projectEnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        projectEnv = new ProjectEnv(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaf_clasificator);

        pager = findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public ProjectEnv getProjectEnv() {
        return projectEnv;
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private final Map<Integer, LeafClassifierFragment> FRAGMENTS = new LinkedHashMap<>();

        private MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            FRAGMENTS.put(0, ImageProcessingFragment.newInstance(0));
            FRAGMENTS.put(1, NeutronNetworkFragment.newInstance(1));
            FRAGMENTS.put(2, ImageOperationsFragment.newInstance(2));
        }

        @Override
        public Fragment getItem(int position) {
            return FRAGMENTS.get(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return FRAGMENTS.get(position).getTitle();
        }
    }
}
