package com.ishchenko.artem.leafclassifierandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.ishchenko.artem.tools.ProjectEnv;

import java.util.LinkedHashMap;
import java.util.Map;


public class LeafClassifier extends FragmentActivity {
    static final int PAGE_COUNT = 3;

    ViewPager pager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaf_clasificator);

        pager = findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(3);
    }

    public static ProjectEnv getProjectEnv() {
        return WelcomeActivity.projectEnv;
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private final Map<Integer, AbstractLeafClassifierFragment> FRAGMENTS = new LinkedHashMap<>();

        private MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            FRAGMENTS.put(0, LeafRecognizingFragment.newInstance(0));
            FRAGMENTS.put(1, LeafLibraryFragment.newInstance(1));
            FRAGMENTS.put(2, NeutronNetworkFragment.newInstance(2));
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
