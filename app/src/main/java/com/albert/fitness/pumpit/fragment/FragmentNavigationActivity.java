package com.albert.fitness.pumpit.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.albert.fitness.pumpit.fragment.logsFragment.LogFragment;
import com.albert.fitness.pumpit.fragment.profile.ProfileFragment;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.welcome.GoalActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import fitness.albert.com.pumpit.R;

public class FragmentNavigationActivity extends AppCompatActivity {

    private ActionBar toolbar;
    private NutritionFragment nutritionFragment;
    private PlanFragment planFragment;
    private WorkoutFragment workoutFragment;
    private LogFragment logsFragment;
    private ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_navigation);
        checkFromWhereActivityFrom();
        toolbar = getSupportActionBar();
        assert toolbar != null;
        toolbar.setTitle("Workout");
        getCurrentUser();
        nutritionFragment = new NutritionFragment();
        planFragment = new PlanFragment();
        workoutFragment = new WorkoutFragment();
        logsFragment = new LogFragment();
        profileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, workoutFragment).commit();
        setTheFragmentSwitch();
    }

    private void checkFromWhereActivityFrom() {
        PrefsUtils prefsUtils = new PrefsUtils(this, "activity");
        String prevActivity = prefsUtils.getString("FROM_ACTIVITY", "");

        //check if activity come from ShowFoodBeforeAddedActivity
        assert prevActivity != null;
        if (prevActivity.equals("ShowFoodBeforeAddedActivity")) {
            prefsUtils.removeAll(this, "activity");
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, nutritionFragment).commit();
        }
    }

    private void getCurrentUser() {
        PrefsUtils prefsUtils = new PrefsUtils();
        prefsUtils.createSharedPreferencesFiles(this, PrefsUtils.SETTINGS_PREFERENCES_FILE);
        String name   = prefsUtils.getString("name", "");

        if (name.isEmpty()) {
            startActivity(new Intent(FragmentNavigationActivity.this, GoalActivity.class));
            finish();
        }

    }

    private void setTheFragmentSwitch() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()) {
                    case R.id.navigation_nutrition:
                        toolbar.setTitle("Nutrition");
                        nutritionFragment = new NutritionFragment();
                        transaction.replace(R.id.frame_container, nutritionFragment).commit();
                        return true;

                    case R.id.navigation_plan:
                        toolbar.setTitle("Plan");
                        transaction.replace(R.id.frame_container, planFragment).commit();
                        return true;

                    case R.id.navigation_workout:
                        toolbar.setTitle("Workout");
                        transaction.replace(R.id.frame_container, workoutFragment).commit();
                        return true;

                    case R.id.navigation_logs:
                        toolbar.setTitle("Logs");
                        transaction.replace(R.id.frame_container, logsFragment).commit();
                        return true;

                    case R.id.navigation_profile:
                        toolbar.setTitle("Profile");
                        transaction.replace(R.id.frame_container, profileFragment).commit();
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you what to Exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FragmentNavigationActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
