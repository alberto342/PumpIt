package com.albert.fitness.pumpit.welcome;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.albert.fitness.pumpit.fragment.FragmentNavigationActivity;
import com.albert.fitness.pumpit.model.ExerciseCategory;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.WelcomeActivityViewModel;

import java.util.List;

import fitness.albert.com.pumpit.R;


public class WelcomeActivity extends AppCompatActivity {

//    private static final String TAG = "WelcomeActivity" ;
//    private List<ExerciseCategory> exerciseCategoryList;
//    private List<Exercise> exerciseObjList;
//    private ExerciseCategory selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        WelcomeActivityViewModel welcomeActivityViewModel = ViewModelProviders.of(this).get(WelcomeActivityViewModel.class);

        //  exerciseCategoryList = exerciseCategories;
        //                for (ExerciseCategory exerciseCategory : exerciseCategories) {
        //                    Log.i(TAG, "category name: " + exerciseCategory.getCategoryName());
        //                }
        welcomeActivityViewModel.getAllCategories().observe(this, this::nextActivity);
    }

    private void nextActivity(List<ExerciseCategory> exerciseCategories) {
        PrefsUtils prefsUtils = new PrefsUtils(this, PrefsUtils.SETTINGS_PREFERENCES_FILE);
        String name = prefsUtils.getString("name", "");
        if (exerciseCategories.isEmpty() || name.isEmpty()) {
            startActivity(new Intent(this, GoalActivity.class));
            finish();
        } else if (!exerciseCategories.isEmpty() && !name.isEmpty()) {
            startActivity(new Intent(this, FragmentNavigationActivity.class));
            finish();
        }
    }
}
