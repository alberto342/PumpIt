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

    private static final String TAG = "ShowLogExerciseActivity";
    private RecyclerView recyclerView;
    private LogTrackerExerciseAdapter trackerExerciseAdapter;
    private List<TrackerExercise> trackerExerciseList = new ArrayList<>();
    private CustomPlanViewModel viewModel;
    int trainingId;
    public static boolean isShowLogExercise;

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
            trainingId = extras.getInt("trainingId");
            for (int i = 0; i < size; i++) {
                int repNumber = extras.getInt("repNumber" + i);
                float weight = extras.getFloat("weight" + i);
                if(repNumber > 0 && weight > 0.0) {
                    trackerExerciseList.add(new TrackerExercise(repNumber, weight));
                }
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
                            if (pos < LogWorkoutFragment.finishIdList.size()) {
                                Log.d(TAG, "instantiateUnderlayButton : Delete Finish Training: " + LogWorkoutFragment.finishIdList.get(pos));
                                viewModel.deleteFinishTrainingByFinishId(LogWorkoutFragment.trackerIdList.get(pos));
                            }
                            trackerExerciseAdapter.deleteItem(pos);
                        }
                ));
            }
        };
    }


    private void saveChange() {
        int finishId = LogWorkoutFragment.finishIdList.get(0);
        for (int i = 0; i < trackerExerciseList.size(); i++) {
            trackerExerciseList.get(i).setTrainingId(trainingId);
            trackerExerciseList.get(i).setFinishTrainingId(finishId);
            if(i < LogWorkoutFragment.trackerIdList.size()) {
                trackerExerciseList.get(i).setTrackerId(LogWorkoutFragment.trackerIdList.get(i));
                viewModel.updateTracker(trackerExerciseList.get(i));
            } else {
                if(trackerExerciseList.get(i).getWeight() > 0.0 && trackerExerciseList.get(i).getRepsNumber() > 0) {
                    viewModel.addNewTracker(trackerExerciseList.get(i));
                }
            }
        }
        Log.d(TAG, "saveChange: success saved tracker exercise: ");
        isShowLogExercise = true;
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
