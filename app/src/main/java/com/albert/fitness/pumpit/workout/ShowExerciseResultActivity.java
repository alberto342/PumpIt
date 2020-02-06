package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.new_adapter.ExerciseAdapter;
import com.albert.fitness.pumpit.model.Exercise;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.WelcomeActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.databinding.ActivityShowExerciseResultBinding;


public class ShowExerciseResultActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<Exercise> exerciseList;
    private PrefsUtils prefsUtils;
    private WelcomeActivityViewModel welcomeActivityViewModel;
    private ExerciseAdapter exerciseAdapter;
    private RecyclerView mRecyclerView;
    int category, secondaryCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityShowExerciseResultBinding exerciseResultBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_show_exercise_result);
        setTitle(getExerciseType() + " Exercise");
        mRecyclerView = exerciseResultBinding.rvExerciseResult;
        pref();
        welcomeActivityViewModel = ViewModelProviders.of(this).get(WelcomeActivityViewModel.class);
        setUpLoadExercise();
    }

    private void setUpLoadExercise() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            category = extras.getInt("category");
            secondaryCategory = extras.getInt("category2");

            if (category == 0) {
                loadAllExercise();
            } else if (secondaryCategory == 1) {
                loadExerciseByCategory(category);
            } else {
                loadExerciseBySecondaryCategory(secondaryCategory);
            }
        }
    }

    private void loadAllExercise() {
        welcomeActivityViewModel.getAllExercise().observe(this, exercises -> {
            exerciseList = (ArrayList<Exercise>) exercises;
            initRecyclerView();
        });
    }


    private void loadExerciseByCategory(int category) {
        welcomeActivityViewModel.getExerciseOfASelectedCategory(category)
                .observe(this, exercises -> {
                    exerciseList = (ArrayList<Exercise>) exercises;
                    initRecyclerView();
                });
    }


    private void loadExerciseBySecondaryCategory(int category) {
        welcomeActivityViewModel.getExerciseOfASelectedSecondaryCategory(category)
                .observe(this, exercises -> {
                    exerciseList = (ArrayList<Exercise>) exercises;
                    initRecyclerView();
                });
    }


    private String getExerciseType() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getString("exerciseType");
        }
        return null;
    }

    private void pref() {
        prefsUtils = new PrefsUtils(this, PrefsUtils.START_WORKOUT);
        String activity = prefsUtils.getString("activity", "");
        if (activity.equals("StartWorkoutActivity")) {
            prefsUtils.saveData("activity2", "ShowExerciseResultActivity");
        }
    }


    @SuppressLint("LongLogTag")
    private void initRecyclerView() {
        final String TAG = "ShowExerciseResultActivity";
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        exerciseAdapter = new ExerciseAdapter();
        exerciseAdapter.setItems(exerciseList);
        mRecyclerView.setAdapter(exerciseAdapter);
        Log.d(TAG, "initRecyclerView: init recyclerView" + mRecyclerView);
        exerciseAdapter.setListener(item -> {
            Log.d(TAG, "Exercise position: " + item.getExerciseId());
            Intent i = new Intent(ShowExerciseResultActivity.this, ExerciseDetailActivity.class);
            i.putExtra("exerciseId", item.getExerciseId());
            i.putExtra("exerciseName", item.getExerciseName());
            i.putExtra("imgUrl", item.getImgName());
            startActivity(i);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_search_exercise, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.menu_cus_add_exercise == item.getItemId()) {
            Intent i = new Intent(this, CustomAddExerciseActivity.class);
            i.putExtra("category", getExerciseType());
            startActivity(i);
            //startActivity(new Intent(this, CustomAddExerciseActivity.class));
        }
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String searchText = "%" + newText + "%";
        List<Exercise> newList = new ArrayList<>();

        if (category == 0) {
            welcomeActivityViewModel.getQueryAllExercise(searchText)
                    .observe(this, exercises -> {
                        if (!exercises.isEmpty()) {
                            newList.addAll(exercises);
                            exerciseAdapter.updateList(newList);
                        }
                    });
        } else if (secondaryCategory == 1) {
            welcomeActivityViewModel.getQueryExerciseList(searchText, category)
                    .observe(this, exercises -> {
                        if (!exercises.isEmpty()) {
                            newList.addAll(exercises);
                            exerciseAdapter.updateList(newList);
                        }
                    });
        } else {
            welcomeActivityViewModel.getQueryBySecondaryCategory(searchText, secondaryCategory)
                    .observe(this, exercises -> {
                        if (!exercises.isEmpty()) {
                            newList.addAll(exercises);
                            exerciseAdapter.updateList(newList);
                        }
                    });
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        prefsUtils.removeSingle(this, PrefsUtils.START_WORKOUT, "activity2");
        finish();
    }
}
