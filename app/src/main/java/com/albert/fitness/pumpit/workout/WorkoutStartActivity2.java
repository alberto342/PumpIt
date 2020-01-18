package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.Exercise;
import com.albert.fitness.pumpit.model.FinishTraining;
import com.albert.fitness.pumpit.model.TDEECalculator;
import com.albert.fitness.pumpit.model.TrackerExercise;
import com.albert.fitness.pumpit.model.Training;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;
import com.albert.fitness.pumpit.viewmodel.WelcomeActivityViewModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fitness.albert.com.pumpit.R;

public class WorkoutStartActivity2 extends AppCompatActivity implements OnClickListener {

    private final String TAG = "WorkoutStartActivity";
    private TextView tvExerciseName, tvTimerUp, btnAddNewSet, btnRemoveSte, tvExercisesLeft, tvCountReps;
    private ImageView imgExercise, ivPlayWorkout, ivStopWorkout, ivIsFinish, ivTimeRest;
    private CountDownTimer countDownTimer;
    private AlertDialog dialog;
    private long timeWhenStopped = 0;
    private Chronometer mChronometer;
    private boolean isPause, countDownTimerIsRunning, finishIsOkSelected, isKeyboardShowing;
    private LinearLayout container;
    private int countAddSets = 0, countExercisesLeft = 1;
    private EditText etWeight, etReps;
    private View vRowView;
    private ArrayList<TrackerExercise> trackerExerciseList;
    private ArrayList<TrackerExercise> newTrackerList = new ArrayList<>();
    private ArrayList<Training> trainingList;
    private ArrayList<Exercise> exerciseList = new ArrayList<>();
    private CustomPlanViewModel planViewModel;
    private WelcomeActivityViewModel activityViewModel;
    private TrackerExercise trackerExercise = new TrackerExercise();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_start);
        planViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);
        activityViewModel = ViewModelProviders.of(this).get(WelcomeActivityViewModel.class);
        getTraining();
        initView();
        checkKeyboard();
        setupChronometer();
    }

    private void getTraining() {
        planViewModel.getAllTrainingByDate(Event.getDayName())
                .observe(this, new Observer<List<Training>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<Training> trainings) {
                if (!trainings.isEmpty()) {
                    if(countExercisesLeft <= trainings.size()) {
                        getTracker(trainings.get(countExercisesLeft-1).getTrainingId());
                        Log.d(TAG, "getTraining: " + trainings.get(countExercisesLeft-1).getTrainingId());
                    }

                    tvExercisesLeft.setText(countExercisesLeft + "/" + trainings.size());

                    trainingList = (ArrayList<Training>) trainings;
                    for (Training t : trainings) {
                        getExercise(t.getExerciseId());
                    }
                    exerciseImg(countExercisesLeft);
                }
            }
        });
    }

    private void getExercise(final int exerciseId) {
        activityViewModel.getExerciseById(exerciseId).observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                if (exercise != null) {
                    exerciseList.add(new Exercise(exercise.getExerciseName(), exercise.getImgName()));
                    Log.d(TAG, "getExercise: " + exercise.getExerciseName());
                }
            }
        });
    }

    private void getTracker(int id) {
        planViewModel.getTrackerExerciseByTraining(id)
                .observe(this, new Observer<List<TrackerExercise>>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onChanged(List<TrackerExercise> trackerExercises) {
                        if (!trackerExercises.isEmpty()) {
                            trackerExerciseList = (ArrayList<TrackerExercise>) trackerExercises;
                            Log.d(TAG, "get tracker: " + trackerExercises.get(0).getWeight());

                            //exercise left
                            if (countAddSets < trackerExerciseList.size()) {
                                onAddField(container);
                            }
                        }
                    }
                });
    }


    private void initView() {
        mChronometer = findViewById(R.id.chronometer);
        ivTimeRest = findViewById(R.id.iv_time_rest);
        ImageView editStartWorkout = findViewById(R.id.iv_edite_start_workout);
        imgExercise = findViewById(R.id.iv_exercise_img_start_workout);
        ImageView nextExercise = findViewById(R.id.iv_next_exercise_next_workout);
        ivPlayWorkout = findViewById(R.id.iv_play_workout);
        ivStopWorkout = findViewById(R.id.iv_stop_workout);
        tvExerciseName = findViewById(R.id.tv_exercise_name_start_wokout);
        tvTimerUp = findViewById(R.id.tv_start_workout_time_up);
        container = findViewById(R.id.ll_container_reps);
        btnAddNewSet = findViewById(R.id.tv_add_new_set);
        btnRemoveSte = findViewById(R.id.tv_remove_set);
        tvExercisesLeft = findViewById(R.id.tv_exercise_left);

        ivTimeRest.setOnClickListener(this);
        editStartWorkout.setOnClickListener(this);
        ivPlayWorkout.setOnClickListener(this);
        ivStopWorkout.setOnClickListener(this);
        nextExercise.setOnClickListener(this);
        btnAddNewSet.setOnClickListener(this);
        btnRemoveSte.setOnClickListener(this);
    }


    private void checkKeyboard() {
        final LinearLayout contentView = findViewById(R.id.ly_play_stop);
        final LinearLayout llImg = findViewById(R.id.ly_img);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        contentView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = contentView.getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;

                        Log.d(TAG, "keypadHeight = " + keypadHeight);

                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                llImg.setVisibility(View.GONE);
                                contentView.setVisibility(View.GONE);
                            }
                        } else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                llImg.setVisibility(View.VISIBLE);
                                contentView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    private void exerciseImg(int position) {
        Log.d(TAG, "exerciseImg size of exerciseList: " + exerciseList.size());

        try {
            String imgFile = "file:///android_asset/images/" + exerciseList.get(position).getImgName();

            Glide.with(this)
                    .asGif()
                    .load(imgFile)
                    .into(imgExercise);

            tvExerciseName.setText(exerciseList.get(position).getExerciseName());

            Log.d(TAG, "Img successfully loaded " + exerciseList.get(position).getExerciseName());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        tvCountReps = rowView.findViewById(R.id.tv_count_reps_added);
        etWeight = rowView.findViewById(R.id.et_wight_set_workout);
        etReps = rowView.findViewById(R.id.et_reps_set_workout);
        ivIsFinish = rowView.findViewById(R.id.iv_finish_workout);
    }


    @SuppressLint("SetTextI18n")
    private void setupChronometer() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.setText("00:00:00");

        //start
        timeWhenStopped = mChronometer.getBase() - SystemClock.elapsedRealtime();
        mChronometer.start();

        mChronometer.setOnChronometerTickListener(
                new Chronometer.OnChronometerTickListener() {
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
                    countDownTimerIsRunning = false;
                    countDownTimer.cancel();
                } else {
                    countDownTimerIsRunning = true;
                    addLayoutTimeRestSelected();
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

                if (countExercisesLeft == trainingList.size()) {
                    startActivity(new Intent(this, FinisherWorkoutActivity.class));
                    finish();
                }

                nextExerciseClicked();
                break;
            //add new set
            case R.id.tv_add_new_set:
                onAddField(v);
                btnRemoveSte.setVisibility(View.VISIBLE);
                break;
            //remove set
            case R.id.tv_remove_set:
                container.removeView(vRowView);
                btnRemoveSte.setVisibility(View.INVISIBLE);
                btnAddNewSet.setVisibility(View.VISIBLE);
                countAddSets--;
                if (countAddSets < 0) {
                    countAddSets = 0;
                }
                break;
            case R.id.tv_s15:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(15000);
                dialog.dismiss();
                break;
            case R.id.tv_s20:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
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
                if (countDownTimer == null) {
                    countDownTimer(0);
                }
                countDownTimer.cancel();
                countDownTimer.onFinish();
                dialog.dismiss();
                break;
        }
    }


    @SuppressLint("SetTextI18n")
    public void startStopChronometer(int view) {
        LinearLayout llDuration = findViewById(R.id.ly_duration);

        //play
        if (view == R.id.iv_play_workout) {
            isPause = false;
            llDuration.setBackgroundResource(R.color.background);
            if (countDownTimer == null) {
                countDownTimer(0);
            }
            ivPlayWorkout.setVisibility(View.INVISIBLE);
            ivTimeRest.setImageResource(R.mipmap.ic_timer);
            ivStopWorkout.setImageResource(R.mipmap.ic_pause);
            mChronometer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
            mChronometer.start();
            countDownTimer.start();
        }
        //pause
        if (view == R.id.iv_stop_workout && !isPause) {
            isPause = true;
            llDuration.setBackgroundResource(R.color.red_900);
            ivStopWorkout.setImageResource(R.mipmap.ic_stop);
            ivPlayWorkout.setVisibility(View.VISIBLE);
            ivTimeRest.setImageResource(R.mipmap.ic_timer_stop);
            mChronometer.stop();
            timeWhenStopped = SystemClock.elapsedRealtime() - mChronometer.getBase();
        } else if (isPause) {
            isPause = false;
            //  whatFinished();
        }
    }


    private void countDownTimer(long millisInFuture) {
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            public void onTick(long millisUntilFinished) {
                tvTimerUp.setText("" + String.format("%d :%d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                tvTimerUp.setText("");
            }
        }.start();
    }


    // TODO: 2019-10-19 calculation cal burned
    private void saveExercise() {
        PrefsUtils prefsUtils = new PrefsUtils(this, PrefsUtils.SETTINGS_PREFERENCES_FILE);
        float weight = prefsUtils.getFloat("weight", 1f);
        TDEECalculator tdeeCalculator = new TDEECalculator();

        int chronometerInMin = tdeeCalculator.splitChronometer(TAG, mChronometer.getText().toString());
        int caloriesBurned = (int) (tdeeCalculator.caloriesBurned(weight) * chronometerInMin);
        String exerciseImgName;

        if (countExercisesLeft == 1) {
            exerciseImgName = exerciseList.get(0).getImgName();
            Log.d(TAG, "test Exercise Img: " + exerciseImgName);
        } else {
            exerciseImgName = exerciseList.get(countExercisesLeft - 1).getImgName();
        }

        if (etWeight.getText().toString().isEmpty() || etWeight.getText() == null || etReps.getText().toString().isEmpty() || etReps.getText() == null) {
            countAddSets--;
        }

        for (int i = 0; i < trackerExerciseList.size(); i++) {
            Log.d(TAG, "exercise recording: " + trackerExerciseList.get(i).getWeight());
        }

        boolean isBlow = countExercisesLeft < trainingList.size();

        boolean isIn = true;
        while (isIn) {
            Log.d(TAG, "isBlow: " + isBlow);
            if (!isBlow) {
                countExercisesLeft--;
                isBlow = countExercisesLeft < trainingList.size();
                Log.d(TAG, "countExercisesLeft: " + countExercisesLeft + " blow " + trainingList.size());
            } else {
                isIn = false;
            }
        }
        Log.d(TAG, "countExercise: " + countExercisesLeft);

        String currentImgName = exerciseImgName != null ? exerciseImgName : exerciseList.get(countExercisesLeft - 1).getImgName();


        //Update training
        Training training = trainingList.get(countExercisesLeft);
//        int restAfterExercise = training.getRestAfterExercise();
//        int restBetweenSet = training.getRestBetweenSet();
        int trainingId = training.getTrainingId();
//
//        trainingList.get(countExercisesLeft).setRestAfterExercise(restAfterExercise);
//        trainingList.get(countExercisesLeft).setRestBetweenSet(restBetweenSet);
//        trainingList.get(countExercisesLeft).setSizeOfRept(trackerExerciseList.size());
//        planViewModel.updateTraining(training);


        //Insert new finish training
        FinishTraining finishTraining = new FinishTraining(mChronometer.getText().toString(), caloriesBurned, trainingId);
        planViewModel.addNewFinishTrainingAndTracker(finishTraining, newTrackerList);
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
        saveExercise();

        if (countDownTimer == null) {
            countDownTimer(0);
        }
        countDownTimer.cancel();
        countDownTimer.onFinish();

        Log.d(TAG, "nextExerciseClicked - countExercisesLeft: " + countExercisesLeft + " training size: " + trainingList.size());

        if (countExercisesLeft == trainingList.size() || countExercisesLeft == 0) {
            onClick(ivStopWorkout);
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer.onFinish();
            }
        } else {
            //exercise name
            tvExerciseName.setText(exerciseList.get(countExercisesLeft).getExerciseName());

            //exercise img
            exerciseImg(countExercisesLeft);

            container.removeAllViews();
            //set timer
            Log.d(TAG, "countExercisesLeft:" + countExercisesLeft);
            countDownTimer(trainingList.get(countExercisesLeft).getRestAfterExercise() * 1000);

            //count for exercise left
            countExercisesLeft++;
            tvExercisesLeft.setText(countExercisesLeft + "/" + trainingList.size());

            trackerExerciseList.clear();
            countAddSets = 0;
            onAddField(vRowView);
        }
    }


    private boolean setWeightAndReptIntoList() {
        boolean weightIsText = etWeight.getText() == null || etWeight.getText().toString().isEmpty();
        boolean repsIsText = etReps.getText() == null || etReps.getText().toString().isEmpty();

        if (etWeight.getText() == null || etWeight.getText().toString().isEmpty() && etWeight.getHint() == null) {
            etWeight.setError("Please enter etWeight");
            return false;
        }

        if (etReps.getText() == null || etReps.getText().toString().isEmpty() && etReps.getHint() == null) {
            etReps.setError("Please enter etReps");
            return false;
        }

        if (weightIsText) {
            trackerExercise.setWeight(Float.valueOf(etWeight.getHint().toString()));
        } else {
            trackerExercise.setWeight(Float.valueOf(etWeight.getText().toString()));
        }

        if (repsIsText) {
            trackerExercise.setRepsNumber(Integer.valueOf(etReps.getHint().toString()));
        } else {
            trackerExercise.setRepsNumber(Integer.valueOf(etReps.getText().toString()));
        }

//        if (trainingList.size() > 1) {
//            trackerExercise.setFinishTrainingId(trainingList.get(countExercisesLeft).getTrainingId());
//        } else {
//            trackerExercise.setFinishTrainingId(trainingList.get(0).getTrainingId());
//        }

        newTrackerList.add(trackerExercise);


        for (int i = 0; i < newTrackerList.size(); i++) {
            Log.d(TAG, "getNewTrackerList: " + newTrackerList.get(i).getWeight());
        }
        return true;
    }

    //add sets exercise
    @SuppressLint("InflateParams")
    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vRowView = inflater.inflate(R.layout.layout_start_workout_reps, null);
        initLayoutReps(vRowView);

        container.addView(vRowView, container.getChildCount() - 1);
        tvCountReps.setText(String.valueOf(countAddSets + 1));

        if (countAddSets < trackerExerciseList.size()) {
            etWeight.setHint(String.valueOf(trackerExerciseList.get(countAddSets).getWeight()));
            etReps.setHint(String.valueOf(trackerExerciseList.get(countAddSets).getRepsNumber()));
        }

        getTraining();

        countAddSets++;
        btnRemoveSte.setVisibility(View.VISIBLE);
        btnAddNewSet.setVisibility(View.INVISIBLE);

        ivIsFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finishIsOkSelected && setWeightAndReptIntoList()) {
                    ivIsFinish.setImageResource(R.mipmap.ic_ok_selected);
                    onAddField(findViewById(R.id.ll_container_reps));
                    countDownTimer(trainingList.get(0).getRestBetweenSet() * 1000);
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
    }

    private void whatFinished() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you what to finish ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(WorkoutStartActivity2.this, FinisherWorkoutActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ivPlayWorkout.setVisibility(View.INVISIBLE);
                        ivStopWorkout.setImageResource(R.mipmap.ic_pause);
                        mChronometer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        mChronometer.start();
                        countDownTimer.start();
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
