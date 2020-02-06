package com.albert.fitness.pumpit.fragment.logsFragment;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;

public class LogTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_tab);

        init();
    }

    private void init() {
        TabLayout tabs = findViewById(R.id.tablayout_logs);
        ViewPager viewPager = findViewById(R.id.vp_logs);

        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new LogNutritionFragment(), "Nutrition Log");
        viewPagerAdapter.addFragment(new LogWorkoutFragment(), "Workout Log");
        viewPager.setAdapter(viewPagerAdapter);
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }

        void addFragment(Fragment fragment, String name) {
            fragmentList.add(fragment);
            fragmentTitles.add(name);
        }
    }



}
