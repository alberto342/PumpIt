package fitness.albert.com.pumpit.workout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fitness.albert.com.pumpit.Adapter.ExerciseAdapter.ExerciseAdapter;
import fitness.albert.com.pumpit.Adapter.TrainingAdapter;
import fitness.albert.com.pumpit.Model.FinishTraining;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.TrackerExercise;
import fitness.albert.com.pumpit.Model.UserRegister;
import fitness.albert.com.pumpit.R;

public class WorkoutStartActivity extends AppCompatActivity implements View.OnClickListener {

    //android dynamically

    private final String TAG = "WorkoutStartActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView exerciseName, timerUp, btnAddNewSet, btnRemoveSte, exercisesLeft, countReps;
    private ImageView timeRest, editStartWorkout, imgExercise, nextExercise, playWorkout, stopWorkout, ivIsFinish;
    private CountDownTimer countDownTimer;
    private AlertDialog dialog;
    private long timeWhenStopped = 0;
    private Chronometer mChronometer;
    private boolean isStop, countDownTimerIsRunning, finishIsOkSelected;
    private LinearLayout container;
    private int countAddSets = 0, countExercisesLeft = 1;
    private EditText weight, reps;
    private View rowView;
    private List<TrackerExercise> trackerExerciseList = new ArrayList<>();
    private TrackerExercise trackerExercise = new TrackerExercise();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_start);

        init();

        setupChronometer();


        onAddField(rowView);

    }

    @SuppressLint("SetTextI18n")
    private void init() {
        mChronometer = findViewById(R.id.chronometer);
        timeRest = findViewById(R.id.iv_time_rest);
        editStartWorkout = findViewById(R.id.iv_edite_start_workout);
        imgExercise = findViewById(R.id.iv_exercise_img_start_workout);
        nextExercise = findViewById(R.id.iv_next_exercise_next_workout);
        playWorkout = findViewById(R.id.iv_play_workout);
        stopWorkout = findViewById(R.id.iv_stop_workout);
        exerciseName = findViewById(R.id.tv_exercise_name_start_wokout);
        timerUp = findViewById(R.id.tv_start_workout_time_up);
        container = findViewById(R.id.ll_container_reps);
        btnAddNewSet = findViewById(R.id.tv_add_new_set);
        btnRemoveSte = findViewById(R.id.tv_remove_set);
        exercisesLeft = findViewById(R.id.tv_exercise_left);

        // onAddField(container);

        assert StartWorkoutActivity.trainingList.get(TrainingAdapter.posit).getExerciseName() != null;
        exerciseName.setText(StartWorkoutActivity.trainingList.get(TrainingAdapter.posit).getExerciseName());

        //exercise img
        try {
            String imgFile = "file:///android_asset/images/" + StartWorkoutActivity.trainingList.get(TrainingAdapter.posit).getImgName();

            Glide.with(this)
                    .asGif()
                    .load(imgFile)
                    .into(imgExercise);

            Log.d(TAG, "Img successfully loaded " + ExerciseAdapter.exerciseImg);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //exercise left
        exercisesLeft.setText(countExercisesLeft + "/" + StartWorkoutActivity.trainingList.size());

        timeRest.setOnClickListener(this);
        editStartWorkout.setOnClickListener(this);
        playWorkout.setOnClickListener(this);
        stopWorkout.setOnClickListener(this);
        nextExercise.setOnClickListener(this);
        btnAddNewSet.setOnClickListener(this);
        btnRemoveSte.setOnClickListener(this);
    }

    private void initRestTimerLayout(View dialogView) {
        //layout object
        TextView s15 = dialogView.findViewById(R.id.tv_s15);
        TextView s20 = dialogView.findViewById(R.id.tv_s20);
        TextView s30 = dialogView.findViewById(R.id.tv_s30);
        TextView s40 = dialogView.findViewById(R.id.tv_s40);
        TextView s50 = dialogView.findViewById(R.id.tv_s50);
        TextView s60 = dialogView.findViewById(R.id.tv_s60);
        TextView s70 = dialogView.findViewById(R.id.tv_s70);
        TextView s80 = dialogView.findViewById(R.id.tv_s80);
        TextView s90 = dialogView.findViewById(R.id.tv_s90);
        TextView s100 = dialogView.findViewById(R.id.tv_s100);
        TextView s105 = dialogView.findViewById(R.id.tv_s105);
        TextView s120 = dialogView.findViewById(R.id.tv_s120);
        TextView s135 = dialogView.findViewById(R.id.tv_s135);
        TextView s150 = dialogView.findViewById(R.id.tv_s150);
        TextView s175 = dialogView.findViewById(R.id.tv_s175);
        TextView min3 = dialogView.findViewById(R.id.tv_3min);
        TextView min4 = dialogView.findViewById(R.id.tv_4min);
        TextView min5 = dialogView.findViewById(R.id.tv_5min);
        ImageView btnCancel = dialogView.findViewById(R.id.btn_cancel_rest_timer);

        s15.setOnClickListener(this);
        s20.setOnClickListener(this);
        s30.setOnClickListener(this);
        s40.setOnClickListener(this);
        s50.setOnClickListener(this);
        s60.setOnClickListener(this);
        s70.setOnClickListener(this);
        s80.setOnClickListener(this);
        s90.setOnClickListener(this);
        s100.setOnClickListener(this);
        s105.setOnClickListener(this);
        s120.setOnClickListener(this);
        s135.setOnClickListener(this);
        s150.setOnClickListener(this);
        s175.setOnClickListener(this);
        min3.setOnClickListener(this);
        min4.setOnClickListener(this);
        min5.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initLayoutReps(View rowView) {
        countReps = rowView.findViewById(R.id.tv_count_reps_added);
        weight = rowView.findViewById(R.id.et_wight_set_workout);
        reps = rowView.findViewById(R.id.et_reps_set_workout);
        ivIsFinish = rowView.findViewById(R.id.iv_finish_workout);
    }


    @SuppressLint("SetTextI18n")
    private void setupChronometer() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.setText("00:00:00");

        //start
        timeWhenStopped = mChronometer.getBase() - SystemClock.elapsedRealtime();
        mChronometer.start();

        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometerChanged) {
                //mChronometer = chronometerChanged;
                long time = SystemClock.elapsedRealtime() - chronometerChanged.getBase();
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String t = (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
                chronometerChanged.setText(t);
            }
        });
    }


    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //stop
            case R.id.iv_stop_workout:
                startStopChronometer(R.id.iv_stop_workout);
                break;
            //rest timer
            case R.id.iv_time_rest:
                if (countDownTimerIsRunning) {
                    countDownTimer.cancel();
                    countDownTimerIsRunning = false;
                } else {
                    addLayoutTimeRestSelected();
                    countDownTimerIsRunning = true;
                }
                break;
            //edit
            case R.id.iv_edite_start_workout:
                break;
            //play workout / resume
            case R.id.iv_play_workout:
                startStopChronometer(R.id.iv_play_workout);
                break;
            //next exercise
            case R.id.iv_next_exercise_next_workout:
                nextExerciseClicked();
                break;
            //add new set
            case R.id.tv_add_new_set:
                onAddField(v);
                btnRemoveSte.setVisibility(View.VISIBLE);
                break;
            //remove set
            case R.id.tv_remove_set:
                container.removeView(rowView);
                btnRemoveSte.setVisibility(View.INVISIBLE);
                btnAddNewSet.setVisibility(View.VISIBLE);
                countAddSets--;
                if (countAddSets < 0) {
                    countAddSets = 0;
                }
                break;
            case R.id.tv_s15:
                countDownTimer(15000);
                dialog.dismiss();
                break;
            case R.id.tv_s20:
                countDownTimer(20000);
                dialog.dismiss();
                break;
            case R.id.tv_s30:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(30000);
                dialog.dismiss();
                break;
            case R.id.tv_s40:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(40000);
                dialog.dismiss();
                break;
            case R.id.tv_s50:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(50000);
                dialog.dismiss();
                break;
            case R.id.tv_s60:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(60000);
                dialog.dismiss();
                break;
            case R.id.tv_s70:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(70000);
                dialog.dismiss();
                break;
            case R.id.tv_s80:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(80000);
                dialog.dismiss();
                break;
            case R.id.tv_s90:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(900000);
                dialog.dismiss();
                break;
            case R.id.tv_s100:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(100000);
                dialog.dismiss();
                break;
            case R.id.tv_s105:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(105000);
                dialog.dismiss();
                break;
            case R.id.tv_s120:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(120000);
                dialog.dismiss();
                break;
            case R.id.tv_s135:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(135000);
                dialog.dismiss();
                break;
            case R.id.tv_s150:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(150000);
                dialog.dismiss();
                break;
            case R.id.tv_s175:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(175000);
                dialog.dismiss();
                break;
            case R.id.tv_3min:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(180000);
                dialog.dismiss();
                break;
            case R.id.tv_4min:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(240000);
                dialog.dismiss();
                break;
            case R.id.tv_5min:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(300000);
                dialog.dismiss();
                break;
            case R.id.btn_cancel_rest_timer:
                assert countDownTimer != null;
                countDownTimer.cancel();
                countDownTimer.onFinish();
                dialog.dismiss();
                break;
        }
    }


    @SuppressLint("SetTextI18n")
    public void startStopChronometer(int view) {
        //play
        if (view == R.id.iv_play_workout) {
            playWorkout.setVisibility(View.INVISIBLE);
            stopWorkout.setImageResource(R.mipmap.ic_pause);

            mChronometer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
            mChronometer.start();
        }

        //pause
        if (view == R.id.iv_stop_workout && !isStop) {

            stopWorkout.setImageResource(R.mipmap.ic_stop);
            playWorkout.setVisibility(View.VISIBLE);

            mChronometer.stop();
            timeWhenStopped = SystemClock.elapsedRealtime() - mChronometer.getBase();

        } else if (view == R.id.iv_stop_workout) {
            //stop
            mChronometer.setBase(SystemClock.elapsedRealtime());
            timeWhenStopped = 0;
            isStop = false;
            mChronometer.setText("00:00:00");
        }
    }


    private void chronoStart() {
        // on first start
        if (timeWhenStopped == 0)
            mChronometer.setBase(SystemClock.elapsedRealtime());
            // on resume after pause
        else {
            long intervalOnPause = (SystemClock.elapsedRealtime() - timeWhenStopped);
            mChronometer.setBase(mChronometer.getBase() + intervalOnPause);
        }
        mChronometer.start();
    }


    private void countDownTimer(long millisInFuture) {
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            public void onTick(long millisUntilFinished) {

                timerUp.setText("" + String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                timerUp.setText("");
            }
        }.start();
    }


    private void setExerciseIntoFb() {

        if (weight.getText().toString().isEmpty() || weight.getText() == null && reps.getText().toString().isEmpty() || reps.getText() == null) {
            countAddSets--;
        }

        for (int i = 0; i < trackerExerciseList.size(); i++) {
            Log.d(TAG, "exercise recording: " + trackerExerciseList.get(i).getWeight());
        }
        FinishTraining finishTraining = new FinishTraining(exerciseName.getText().toString(),
                trackerExerciseList, 0,
                StartWorkoutActivity.trainingList.get(countExercisesLeft).getRestBetweenSet(),
                StartWorkoutActivity.trainingList.get(countExercisesLeft).getRestAfterExercise(),
                StartWorkoutActivity.trainingList.get(countExercisesLeft).getImgName(),
                UserRegister.getTodayData(), StartWorkoutActivity.trainingList.get(countExercisesLeft).isFavorite(),
                mChronometer.getText().toString(), 0, "", "", "", 0f, 0);

        db.collection(FinishTraining.TRAINING_LOG).document(FireBaseInit.getEmailRegister())
                .collection(UserRegister.getTodayData()).add(finishTraining).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Document successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
            }
        });
    }


    private void addLayoutTimeRestSelected() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.layout_select_rest_time, null);

        dialogBuilder.setView(dialogView);
        initRestTimerLayout(dialogView);

        dialog = dialogBuilder.create();
        dialog.show();
    }


    @SuppressLint("SetTextI18n")
    private void nextExerciseClicked() {

        //save into fb
        setExerciseIntoFb();

        if (countExercisesLeft == StartWorkoutActivity.trainingList.size()) {
            onClick(stopWorkout);
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer.onFinish();
            }
        } else {
            //exercise name
            exerciseName.setText(StartWorkoutActivity.trainingList.get(countExercisesLeft).getExerciseName());

            //exercise img
            try {
                String imgFile = "file:///android_asset/images/" + StartWorkoutActivity.trainingList.get(countExercisesLeft).getImgName();

                Glide.with(this)
                        .asGif()
                        .load(imgFile)
                        .into(imgExercise);
                Log.d(TAG, "Img successfully loaded " + ExerciseAdapter.exerciseImg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            container.removeAllViews();
            //set timer
            countDownTimer(StartWorkoutActivity.trainingList.get(countExercisesLeft - 1).getRestAfterExercise() * 1000);

            //count for exercise left
            countExercisesLeft++;
            exercisesLeft.setText(countExercisesLeft + "/" + StartWorkoutActivity.trainingList.size());

            trackerExerciseList.clear();
            countAddSets = 0;
            onAddField(rowView);
        }
    }


    private boolean setWeightAndReptIntoList() {

        boolean weightIsText = weight.getText() == null || weight.getText().toString().isEmpty();

        boolean repsIsText = reps.getText() == null || reps.getText().toString().isEmpty();

        if (weight.getText() == null || weight.getText().toString().isEmpty() && weight.getHint() == null) {
            weight.setError("please enter weight");
            return false;
        }

        if (reps.getText() == null || reps.getText().toString().isEmpty() && reps.getHint() == null) {
            reps.setError("please enter reps");
            return false;
        }


        if (weightIsText) {
            trackerExercise.setWeight(Float.valueOf(weight.getHint().toString()));
        } else {
            trackerExercise.setWeight(Float.valueOf(weight.getText().toString()));
        }


        if (repsIsText) {
            trackerExercise.setRepNumber(Integer.valueOf(reps.getHint().toString()));
        } else {
            trackerExercise.setRepNumber(Integer.valueOf(reps.getText().toString()));
        }

        trackerExerciseList.add(trackerExercise);


        for (int i = 0; i < trackerExerciseList.size(); i++) {
            Log.d(TAG, "trackerExerciseList: " + trackerExerciseList.get(i).getWeight());
        }
        return true;
    }


    //add sets exercise
    @SuppressLint("InflateParams")
    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.layout_start_workout_reps, null);

        initLayoutReps(rowView);

        container.addView(rowView, container.getChildCount() - 1);
        countReps.setText(String.valueOf(countAddSets + 1));

        for (int i = 0; i < StartWorkoutActivity.trainingList.size(); i++) {
            if (countExercisesLeft - 1 < StartWorkoutActivity.trainingList.size()) {
                for (int j = 0; j < StartWorkoutActivity.trainingList.get(i).getTrackerExercises().size(); j++) {
                    if (countAddSets < StartWorkoutActivity.trainingList.get(countExercisesLeft - 1).getTrackerExercises().size()) {
                        weight.setHint(String.valueOf(StartWorkoutActivity.trainingList.get(countExercisesLeft - 1).getTrackerExercises().get(countAddSets).getWeight()));
                        reps.setHint(String.valueOf(StartWorkoutActivity.trainingList.get(countExercisesLeft - 1).getTrackerExercises().get(countAddSets).getRepNumber()));
                    }
                }
            }
        }

        countAddSets++;
        btnRemoveSte.setVisibility(View.VISIBLE);
        btnAddNewSet.setVisibility(View.INVISIBLE);

        Log.d(TAG, "countAddSets: " + countAddSets);

        ivIsFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finishIsOkSelected && setWeightAndReptIntoList()) {
                    ivIsFinish.setImageResource(R.mipmap.ic_ok_selected);
                    onAddField(findViewById(R.id.ll_container_reps));
                    countDownTimer(StartWorkoutActivity.trainingList.get(TrainingAdapter.posit).getRestBetweenSet() * 1000);
                    finishIsOkSelected = false;
                } else {
                    ivIsFinish.setImageResource(R.mipmap.ic_ok);
                    finishIsOkSelected = true;
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer.onFinish();
                    }
                }
            }
        });
        Log.d(TAG, "trainingList : " + StartWorkoutActivity.trainingList.get(TrainingAdapter.posit).getExerciseName()
                + "\nImg Name: " + StartWorkoutActivity.trainingList.get(TrainingAdapter.posit).getImgName());
    }


    public void onDelete(View v) {
        container.removeView((View) v.getParent());
    }


}
