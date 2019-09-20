package com.albert.fitness.pumpit.welcome;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.albert.fitness.pumpit.model.ExerciseCategory;
import com.albert.fitness.pumpit.model.ExerciseObj;
import com.albert.fitness.pumpit.viewmodel.WelcomeActivityViewModel;

import java.util.List;

import fitness.albert.com.pumpit.R;


public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";
    private WelcomeActivityViewModel welcomeActivityViewModel;
    private List<ExerciseCategory> exerciseCategoryList;
    private List<ExerciseObj> exerciseObjList;
    private ExerciseCategory selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeActivityViewModel = ViewModelProviders.of(this).get(WelcomeActivityViewModel.class);

        welcomeActivityViewModel.getAllCategories().observe(this, new Observer<List<ExerciseCategory>>() {
            @Override
            public void onChanged(List<ExerciseCategory> exerciseCategories) {
                exerciseCategoryList = (List<ExerciseCategory>) exerciseCategories;
                for(ExerciseCategory exerciseCategory : exerciseCategories) {
                    Log.i(TAG, "category name: " + exerciseCategory.getCategoryName());
                }
            }
        });


    }


}
