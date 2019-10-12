package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.albert.fitness.pumpit.fragment.FragmentNavigationActivity;
import com.albert.fitness.pumpit.model.FinishTraining;
import com.albert.fitness.pumpit.model.TDEECalculator;
import com.albert.fitness.pumpit.model.UserRegister;
import com.albert.fitness.pumpit.utils.FireBaseInit;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;

public class FinisherWorkoutActivity extends AppCompatActivity {

    private static final String TAG = "FinisherWorkoutActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<FinishTraining> finishTrainingList = new ArrayList<>();
    private TDEECalculator tdeeCalculator = new TDEECalculator();
    private TextView tvNewRecord;
    private TextView tvTrainingRecord;
    private TextView tvActualRecord;
    private TextView tvRest;
    private TextView tvWaste;
    private TextView tvCompleteExercise;
    private TextView tvTotalWeightCmp;
    private int trackerExercises = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finisher_workout);
        setTitle("Finished Workout");
        initView();

        getRecordFromFb();
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

    private void getRecordFromFb() {
        final ProgressDialog progressdialog = new ProgressDialog(FinisherWorkoutActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();
        final int[] totalWeight = {0};
        final PrefsUtils prefsUtils = new PrefsUtils();
        prefsUtils.createSharedPreferencesFiles(this, PrefsUtils.EXERCISE);

        db.collection(FinishTraining.TRAINING_LOG).document(FireBaseInit.getEmailRegister())
                .collection(UserRegister.getTodayDate()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressdialog.hide();

                        if (task.isSuccessful() && task.getResult() != null) {

                            FinishTraining chrTotalTraining = task.getResult().getDocuments().get(task.getResult().size() - 1).toObject(FinishTraining.class);

                            tvTrainingRecord.setText(chrTotalTraining.getChrTotalTraining());
                            tvCompleteExercise.setText(String.valueOf(task.getResult().size()));

                            prefsUtils.saveData("exercise_complete", task.getResult().size());
                            prefsUtils.saveData("today_date", UserRegister.getTodayDate());

                            for (int i = 0; i < task.getResult().size(); i++) {
                                FinishTraining finishTraining = task.getResult().getDocuments().get(i).toObject(FinishTraining.class);
                                finishTrainingList.add(finishTraining);

//                                for (int r = 0; r < finishTraining.getTrackerExercises().size(); r++) {
//                                    totalWeight[0] += finishTraining.getTrackerExercises().get(r).getWeight();
//                                }
                            }
                            tvTotalWeightCmp.setText(totalWeight[0] + " kg");

                            totalTimeRest();
                            totalTimeWaste();
                            totalActual();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "get failed with: " + e);
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
