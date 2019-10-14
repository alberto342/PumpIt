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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.exercise_adapter.ExerciseAdapter;
import com.albert.fitness.pumpit.model.Exercise;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.WelcomeActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;


public class ShowExerciseResultActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<Exercise> exerciseList;
    private PrefsUtils prefsUtils;
    private WelcomeActivityViewModel welcomeActivityViewModel;
    private ExerciseAdapter exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exercise_result);
        setTitle(getExerciseType());
        pref();
        welcomeActivityViewModel = ViewModelProviders.of(this).get(WelcomeActivityViewModel.class);
        setUpLoadExercise();
    }

    private void setUpLoadExercise() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int category = extras.getInt("exerciseType");
            int secondaryCategory = extras.getInt("category2");

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
        welcomeActivityViewModel.getAllExercise().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                exerciseList = (ArrayList<Exercise>) exercises;
                initRecyclerView();
            }
        });
    }


    private void loadExerciseByCategory(int category) {
        welcomeActivityViewModel.getExerciseOfASelectedCategory(category).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                exerciseList = (ArrayList<Exercise>) exercises;
                initRecyclerView();
            }
        });
    }


    private void loadExerciseBySecondaryCategory(int category) {
        welcomeActivityViewModel.getExerciseOfASelectedSecondaryCategory(category).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                exerciseList = (ArrayList<Exercise>) exercises;
                initRecyclerView();
            }
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


    @SuppressLint({"WrongConstant", "LongLogTag"})
    private void initRecyclerView() {
        final String TAG = "ShowExerciseResultActivity";
        RecyclerView recyclerView = findViewById(R.id.rv_exercise_result);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        exerciseAdapter = new ExerciseAdapter(this, exerciseList);
        recyclerView.setAdapter(exerciseAdapter);

        Log.d(TAG, "initRecyclerView: init recyclerView" + recyclerView);
    }


    //   private void getCustomExercise() {
    //SETUP REEALM
//        RealmConfiguration config = new RealmConfiguration.Builder().name(CustomExerciseName.REALM_FILE_EXERCISE).deleteRealmIfMigrationNeeded().build();
//        Realm realmExercise = Realm.getInstance(config);
//
//        String category = AddExerciseActivity.categorySelected;
//
//        RealmQuery<CustomExerciseName> query = realmExercise.where(CustomExerciseName.class);
//
//        query.equalTo("muscle_group", category);
//
//        RealmResults<CustomExerciseName> result = query.findAll();
//
//        List<CustomExerciseName> customExerciseNameList = new ArrayList<>(result);
//
//        RecyclerView recyclerView = findViewById(R.id.rv_custom_exercise);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//
//        if (!customExerciseNameList.isEmpty()) {
//            CustomExerciseAdapter customExerciseAdapter = new CustomExerciseAdapter(this, customExerciseNameList);
//            recyclerView.setAdapter(customExerciseAdapter);
//        }
    //   }


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
            startActivity(new Intent(this, CustomAddExerciseActivity.class));
        }
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        List<Exercise> newList = new ArrayList<>();

        for (Exercise name : exerciseList) {
            if (name.getExerciseName().toLowerCase().contains(userInput)) {
                newList.add(name);
            }
        }
        exerciseAdapter.updateList(newList);
        return true;
    }


    @Override
    public void onBackPressed() {
        prefsUtils.removeSingle(this, PrefsUtils.START_WORKOUT, "activity2");
        finish();
    }
}
