package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.albert.fitness.pumpit.fragment.FragmentNavigationActivity;
import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.FinishTraining;
import com.albert.fitness.pumpit.model.TDEECalculator;
import com.albert.fitness.pumpit.model.TrackerExercise;
import com.albert.fitness.pumpit.model.UserRegister;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;

import java.util.List;

import fitness.albert.com.pumpit.R;

public class FinisherWorkoutActivity extends AppCompatActivity {

    private static final String TAG = "FinisherWorkoutActivity";
    private TDEECalculator tdeeCalculator = new TDEECalculator();
    private TextView tvNewRecord;
    private TextView tvTrainingRecord;
    private TextView tvActualRecord;
    private TextView tvRest;
    private TextView tvWaste;
    private TextView tvCompleteExercise;
    private TextView tvTotalWeightCmp;
    private int trackerExercises = 0;
    private PrefsUtils prefsUtils;
    private CustomPlanViewModel planViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finisher_workout);
        setTitle("Finished Workout");
        planViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);
        prefsUtils = new PrefsUtils(FinisherWorkoutActivity.this, PrefsUtils.EXERCISE);
        initView();
        getRecord();
    }


    private void getRecord() {
        planViewModel.getLastFinishTrainingById().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                planViewModel.getFinishTrainingById(integer).observe(FinisherWorkoutActivity.this, new Observer<FinishTraining>() {
                    @Override
                    public void onChanged(FinishTraining finishTraining) {
                        if (finishTraining != null) {
                            getTrackerExercise(finishTraining.getFinishId());
                            tvTrainingRecord.setText(finishTraining.getChrTotalTraining());
                            prefsUtils.saveData("today_date", Event.getTodayData());
                            totalTimeRest();
                            totalTimeWaste();
                            totalActual();
                        }
                    }
                });
            }
        });
    }

    private void getTrackerExercise(int id) {
        final float[] totalWeight = {0};
        planViewModel.getAllTrackerByFinishTrainingId(id).observe(this, new Observer<List<TrackerExercise>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<TrackerExercise> trackerExercises) {
                if (!trackerExercises.isEmpty()) {
                    tvCompleteExercise.setText(String.valueOf(trackerExercises.size()));
                    prefsUtils.saveData("exercise_complete", trackerExercises.size());

                    for (TrackerExercise t : trackerExercises) {
                        totalWeight[0] += t.getWeight();
                    }
                    tvTotalWeightCmp.setText(totalWeight[0] + " kg");
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        tvNewRecord = findViewById(R.id.tv_new_record);
        tvTrainingRecord = findViewById(R.id.tv_training_record);
        tvActualRecord = findViewById(R.id.tv_actual_record);
        tvRest = findViewById(R.id.tv_rest);
        tvWaste = findViewById(R.id.tv_waste);
        tvCompleteExercise = findViewById(R.id.tv_complete_exercise);
        tvTotalWeightCmp = findViewById(R.id.tv_total_weight_cmp);
        TextView tvDate = findViewById(R.id.tv_date_of_day);
        tvDate.setText("○ " + UserRegister.getTodayDate() + " ○");
        Button finish = findViewById(R.id.btn_finished_workout);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FinisherWorkoutActivity.this, FragmentNavigationActivity.class));
                finish();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void totalTimeRest() {
        int totalRestAfterExercise = 0;
        int totalRestBetweenSet = 0;

//        for (int i = 0; i < finishTrainingList.size(); i++) {
//            totalRestBetweenSet += finishTrainingList.get(i).getRestBetweenSet() * finishTrainingList.get(i).getTrackerExercises().size();
//            totalRestAfterExercise += finishTrainingList.get(i).getRestAfterExercise();
//        }
        totalRestBetweenSet = totalRestBetweenSet + totalRestAfterExercise;

        tvRest.setText(intIntoChronometer(totalRestBetweenSet * 1000));
    }


    private void totalTimeWaste() {
        int totalTrainingRecord = tdeeCalculator.splitChronometer(TAG, tvTrainingRecord.getText().toString());
        int totalTimeRest = tdeeCalculator.splitChronometer(TAG, tvRest.getText().toString());
        int totalTimeWaste;


//        for (int i = 0; i < finishTrainingList.size(); i++) {
//            trackerExercises += finishTrainingList.get(i).getTrackerExercises().size() * 45;
//        }

        totalTimeWaste = totalTrainingRecord - totalTimeRest - trackerExercises;

        tvWaste.setText(intIntoChronometer(totalTimeWaste));
    }


    private void totalActual() {
        // training - rest - waste = actual
        int training = tdeeCalculator.splitChronometer(TAG, tvTrainingRecord.getText().toString());
        int rest = tdeeCalculator.splitChronometer(TAG, tvRest.getText().toString());
        int waste = tdeeCalculator.splitChronometer(TAG, tvWaste.getText().toString());
        int actual = training - rest - waste;
        tvActualRecord.setText(intIntoChronometer(actual));
    }


    @SuppressLint("SetTextI18n")
    private String intIntoChronometer(int time) {
        int h = time / 3600000;
        int m = (time - h * 3600000) / 60000;
        int s = (time - h * 3600000 - m * 60000) / 1000;
        String hh = h < 10 ? "0" + h : h + "";
        String mm = m < 10 ? "0" + m : m + "";
        String ss = s < 10 ? "0" + s : s + "";
        Log.d(TAG, "totalTimeRest: " + hh + ":" + mm + ":" + ss);
        return hh + ":" + mm + ":" + ss;
    }


}
