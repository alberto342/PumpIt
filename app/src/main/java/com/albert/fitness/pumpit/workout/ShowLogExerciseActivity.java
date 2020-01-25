package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.LogTrackerExerciseAdapter;
import com.albert.fitness.pumpit.fragment.logsFragment.LogWorkoutFragment;
import com.albert.fitness.pumpit.model.TrackerExercise;
import com.albert.fitness.pumpit.utils.SwipeHelper;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;

public class ShowLogExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private LogTrackerExerciseAdapter trackerExerciseAdapter;
    private List<TrackerExercise> trackerExerciseList = new ArrayList<>();
    private CustomPlanViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_log_exercise);
        viewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);
        setTitle("Log Exercise");
        initView();
        getExtra();
        swipe();
    }

    private void initView() {
        ImageView save = findViewById(R.id.btn_save_logExercise);
        ImageView btnPlus = findViewById(R.id.btn_plus_log);
        ImageView btnMinus = findViewById(R.id.btn_minus_log);
        recyclerView = findViewById(R.id.rv_log_tracker);

        save.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_logExercise:
                saveChange();
                break;
            case R.id.btn_plus_log:
                trackerExerciseList.add(new TrackerExercise(0, 0.0f));
                initRecyclerView();
                break;
            case R.id.btn_minus_log:
                if (trackerExerciseList.size() > 1) {
                    trackerExerciseList.remove(trackerExerciseList.size() - 1);
                    initRecyclerView();
                }
        }
    }

    @SuppressLint("SetTextI18n")
    private void getExtra() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.setTitle(extras.getString("exerciseName"));
            int size = extras.getInt("trackerExercisesSize");
            for (int i = 0; i < size; i++) {
                int repNumber = extras.getInt("repNumber" + i);
                float weight = extras.getFloat("weight" + i);
                trackerExerciseList.add(new TrackerExercise(repNumber, weight));
                initRecyclerView();
            }
        }
    }

    private void initRecyclerView() {
        String TAG = "ShowLogExerciseActivity";
        Log.d(TAG, "initRecyclerView: init FinishWorkout recyclerView" + recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        trackerExerciseAdapter = new LogTrackerExerciseAdapter(trackerExerciseList);
        recyclerView.setAdapter(trackerExerciseAdapter);
    }


    private void swipe() {
        new SwipeHelper(this, recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        // R.color.colorAccent,
                        Color.parseColor("#d50000"),
                        pos -> {
                            trackerExerciseAdapter.deleteItem(pos);
                            Log.d("iiii", "instantiateUnderlayButton: " + LogWorkoutFragment.finishIdList.get(pos));
                           // viewModel.deleteFinishTrainingByFinishId(LogWorkoutFragment.finishIdList.get(pos));
                        }
                ));
            }
        };
    }


    private void saveChange() {

        for (int i = 0; i < trackerExerciseList.size(); i++) {
            Log.d("222", "saveChange: " + trackerExerciseList.get(i).getTrackerId());

            Log.d("222", "saveChange: " + trackerExerciseList.get(i).getWeight());

        }


//        for (int i = 0; i < trackerExerciseList.size(); i++) {
//            if (trackerExerciseList.get(i).getTrackerId() == 0) {
//                int id = trackerExerciseList.get(0).getTrackerId();
//                trackerExerciseList.get(i).setTrackerId(id);
//                viewModel.updateTracker(trackerExerciseList.get(i));
//            } else {
//                viewModel.addNewTracker(trackerExerciseList.get(i));
//               // viewModel.updateTracker(trackerExerciseList.get(i));
//            }
//        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
