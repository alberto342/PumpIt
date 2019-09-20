package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.CustomExerciseAdapter;
import com.albert.fitness.pumpit.adapter.ExerciseAdapter.ExerciseAdapter;
import com.albert.fitness.pumpit.model.CustomExerciseName;
import com.albert.fitness.pumpit.model.Exercise;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;

import com.albert.fitness.pumpit.model.PrefsUtils;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class ShowExerciseResultActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Realm realm;
    RecyclerView recyclerView;
    private List<Exercise> realmList = new ArrayList<>();
    private ExerciseAdapter exerciseAdapter;
    private PrefsUtils prefsUtils = new PrefsUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exercise_result);
        setTitle(getExerciseType());

        //SETUP REEALM
        RealmConfiguration config = new RealmConfiguration.Builder().name(Exercise.REALM_FILE_GYM).deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(config);
        //Realm.setDefaultConfiguration(config);

        pref();
        //  initRecyclerView();

    }

    private String getExerciseType() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getString("exerciseType");
        }
        return null;
    }

    private void pref() {
        prefsUtils.createSharedPreferencesFiles(this, PrefsUtils.START_WORKOUT);
        String activity = prefsUtils.getString("activity", "");
        if (activity.equals("StartWorkoutActivity")) {
            prefsUtils.saveData("activity2", "ShowExerciseResultActivity");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
        getCustomExercise();
    }

    @SuppressLint("WrongConstant")
    private void initRecyclerView() {

        recyclerView = findViewById(R.id.rv_exercise_result);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // realm = Realm.getDefaultInstance();
        recyclerView(this, realmList, AddExerciseActivity.categorySelected, AddExerciseActivity.category2Selected);
    }


    @SuppressLint("LongLogTag")
    public void recyclerView(Context context, List<Exercise> exerciseList, String category, String category2) {

        final String TAG = "ShowExerciseResultActivity";
        //realm.getSchema();
        RealmQuery<Exercise> query = realm.where(Exercise.class);

        if (category.contains("All")) {
            query.findAll();
        } else if (category2.contains("null")) {
            query.equalTo("category", category);
        } else {
            query.equalTo("category_2", category2);
        }

        RealmResults<Exercise> result = query.findAll();

        exerciseList.addAll(result);

        exerciseAdapter = new ExerciseAdapter(context, exerciseList);
        recyclerView.setAdapter(exerciseAdapter);

        Log.d(TAG, "initRecyclerView: init recyclerView" + recyclerView);
    }

    @SuppressLint("WrongConstant")
    private void getCustomExercise() {
        //SETUP REEALM
        RealmConfiguration config = new RealmConfiguration.Builder().name(CustomExerciseName.REALM_FILE_EXERCISE).deleteRealmIfMigrationNeeded().build();
        Realm realmExercise = Realm.getInstance(config);

        String category = AddExerciseActivity.categorySelected;

        RealmQuery<CustomExerciseName> query = realmExercise.where(CustomExerciseName.class);

        query.equalTo("muscle_group", category);

        RealmResults<CustomExerciseName> result = query.findAll();

        List<CustomExerciseName> customExerciseNameList = new ArrayList<>(result);

        RecyclerView recyclerView = findViewById(R.id.rv_custom_exercise);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if (!customExerciseNameList.isEmpty()) {
            CustomExerciseAdapter customExerciseAdapter = new CustomExerciseAdapter(this, customExerciseNameList);
            recyclerView.setAdapter(customExerciseAdapter);
        }
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

        for (Exercise name : realmList) {
            if (name.getName().toLowerCase().contains(userInput)) {
                newList.add(name);
            }
        }
        exerciseAdapter.updateList(newList);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onBackPressed() {
        AddExerciseActivity.categorySelected = "null";
        AddExerciseActivity.category2Selected = "null";
        prefsUtils.removeSingle(this,PrefsUtils.START_WORKOUT, "activity2");
        finish();
    }
}
