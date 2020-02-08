package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.albert.fitness.pumpit.fragment.FragmentNavigationActivity;
import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.TDEECalculator;
import com.albert.fitness.pumpit.model.TrackerExercise;
import com.albert.fitness.pumpit.model.UserRegister;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;

import fitness.albert.com.pumpit.R;

public class FinisherWorkoutActivity extends AppCompatActivity {

    private static final String TAG = "FinisherWorkoutActivity";
    private TDEECalculator tdeeCalculator = new TDEECalculator();
    //private TextView tvNewRecord;
    private TextView tvTrainingRecord;
    private TextView tvActualRecord;
    private TextView tvRest;
    private TextView tvWaste;
    private TextView tvCompleteExercise;
    private TextView tvTotalWeightCmp;
    private CustomPlanViewModel planViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finisher_workout);
        setTitle("Finished Workout");
        planViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);
        initView();

        updateTrackerExercise();
        getSumOfWeight();
        getRecord();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        //tvNewRecord = findViewById(R.id.tv_new_record);
        tvTrainingRecord = findViewById(R.id.tv_training_record);
        tvActualRecord = findViewById(R.id.tv_actual_record);
        tvRest = findViewById(R.id.tv_rest);
        tvWaste = findViewById(R.id.tv_waste);
        tvCompleteExercise = findViewById(R.id.tv_complete_exercise);
        tvTotalWeightCmp = findViewById(R.id.tv_total_weight_cmp);
        TextView tvDate = findViewById(R.id.tv_date_of_day);
        tvDate.setText("○ " + UserRegister.getTodayDate() + " ○");
        Button finish = findViewById(R.id.btn_finished_workout);
        finish.setOnClickListener(view -> {
            startActivity(new Intent(FinisherWorkoutActivity.this, FragmentNavigationActivity.class));
            finish();
        });
    }

    private void updateTrackerExercise() {
        planViewModel.getAllTrackerWhereFinishIdZero()
                .observe(this, trackerExercises -> {
                    if (!trackerExercises.isEmpty()) {
                        for (final TrackerExercise exercise : trackerExercises) {

                            planViewModel.getMaxIdFromFinishTraining()
                                    .observe(FinisherWorkoutActivity.this, id -> {
                                        if (id != null) {
                                            exercise.setFinishTrainingId(id);
                                            //  trackerExercise.setFinishTrainingId(id);
                                            Log.d(TAG, "getMaxIdFromFinishTraining: " + id);
                                        } else {
                                            exercise.setFinishTrainingId(1);
                                            Log.d(TAG, "getMaxIdFromFinishTraining: " + id);
                                        }
                                    });
                            planViewModel.updateTracker(exercise);
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void getRecord() {
        PrefsUtils prefsUtils = new PrefsUtils(FinisherWorkoutActivity.this, PrefsUtils.EXERCISE);
        planViewModel.getFinishWorkout(Event.getTodayData())
                .observe(this, queryFinishWorkouts -> {
                    if (!queryFinishWorkouts.isEmpty()) {
                        prefsUtils.saveData("today_date", Event.getTodayData());

                        tvCompleteExercise.setText(String.valueOf(queryFinishWorkouts.size()));
                        tvTrainingRecord.setText(queryFinishWorkouts.get(queryFinishWorkouts.size() - 1).getChronometer());
                        prefsUtils.saveData("exercise_complete",queryFinishWorkouts.size());

                        totalTimeWaste();
                        totalActual();
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void getSumOfWeight() {
        planViewModel.getFinishWorkoutSum(Event.getTodayData())
               .observe(this, sumOfTrackerExercise -> {
                   if(sumOfTrackerExercise != null) {
                       tvTotalWeightCmp.setText(sumOfTrackerExercise.getSumWeightAndReps() + " kg");
                       tvRest.setText(intIntoChronometer(sumOfTrackerExercise.getSumRest() * 1000));
                   }
               });
    }


    private void totalTimeWaste() {
        int totalTrainingRecord = tdeeCalculator.splitChronometer(TAG, tvTrainingRecord.getText().toString());
        int totalTimeRest = tdeeCalculator.splitChronometer(TAG, tvRest.getText().toString());
        int totalTimeWaste;
        int trackerExercises = 0;
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
