package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.TrackerExerciseAdapter;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fitness.albert.com.pumpit.R;

public class WorkoutStartActivity extends AppCompatActivity implements OnClickListener {

    private final String TAG = "WorkoutStartActivity";
    private TextView tvExerciseName, tvTimerUp, tvExercisesLeft;
    private ImageView imgExercise, ivPlayWorkout, ivStopWorkout, ivTimeRest;
    private CountDownTimer countDownTimer;
    private AlertDialog dialog;
    private long timeWhenStopped = 0;
    private Chronometer mChronometer;
    private boolean isPause, countDownTimerIsRunning, isKeyboardShowing;
    private int countExercisesLeft = 1;
    private ArrayList<TrackerExercise> trackerExerciseList;
    private ArrayList<Training> trainingList = new ArrayList<>();
    private ArrayList<Exercise> exerciseList = new ArrayList<>();
    private CustomPlanViewModel planViewModel;
    private WelcomeActivityViewModel activityViewModel;
    private TrackerExercise trackerExercise = new TrackerExercise();
    private RecyclerView recyclerView;
    private TrackerExerciseAdapter trackerExerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_start);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        planViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);
        activityViewModel = ViewModelProviders.of(this).get(WelcomeActivityViewModel.class);
        getTraining();
        initView();
        checkKeyboard();
        setupChronometer();
    }


    @SuppressLint("SetTextI18n")
    private void getTraining() {
        SharedPreferences prefs = this.getSharedPreferences(PrefsUtils.DONE_EXERCISE, Context.MODE_PRIVATE);
        Map<String, ?> prefsKeys = prefs.getAll();
        List<Integer> exerciseId = new ArrayList<>();

        planViewModel.getAllTrainingByDate(Event.getDayName())
                .observe(this, trainings -> {
                    if (!trainings.isEmpty()) {

                        for (Map.Entry<String, ?> entry : prefsKeys.entrySet()) {
                            if (entry.getValue().toString().equals("true")) {
                                String stKey = entry.getKey();
                                String[] parts = stKey.split(" ");
                                int exId = Integer.parseInt(parts[1]);
                                exerciseId.add(exId);
                            }
                        }

                        for(Training tr: trainings) {
                            if (!exerciseId.contains(tr.getExerciseId())) {
                                trainingList.add(tr);
                            }
                        }

                        
                        if (countExercisesLeft <= trainingList.size()) {
                            getTracker(trainingList.get(countExercisesLeft - 1).getTrainingId());
                            Log.d(TAG, "getTraining: " + trainingList.get(countExercisesLeft - 1).getTrainingId());
                        }
                        tvExercisesLeft.setText(countExercisesLeft + "/" + trainingList.size());

                        for(Training tr: trainingList) {
                            getExercise(tr.getExerciseId());
                        }

                    }
                });
    }

    private void getExercise(final int exerciseId) {
        activityViewModel.getExerciseById(exerciseId)
                .observe(this, exercise -> {
                    if (exercise != null) {
                        exerciseList.add(new Exercise(exercise.getExerciseName(), exercise.getImgName()));
                        Log.d(TAG, "getExercise: " + exercise.getExerciseName());
                        exerciseImg(countExercisesLeft - 1);
                    }
                });
    }

    private void getTracker(int id) {
        planViewModel.getTrackerExerciseByTraining(id)
                .observe(this, trackerExercises -> {
                    if (!trackerExercises.isEmpty()) {
                        trackerExerciseList = (ArrayList<TrackerExercise>) trackerExercises;
                        Log.d(TAG, "get tracker: " + trackerExercises.get(0).getWeight());
                        initRecyclerView();
                    }
                });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView " + trackerExercise);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        trackerExerciseList.add(new TrackerExercise(0, 0));
        trackerExerciseAdapter = new TrackerExerciseAdapter(trackerExerciseList);
        recyclerView.setAdapter(trackerExerciseAdapter);

        trackerExerciseAdapter.setOnItemClickListener(
                (position, v) -> {
                    if (v.getTag().equals(R.drawable.ic_remove_circle_white_24dp)) {
                        trackerExerciseAdapter.deleteItem(position);
                    } else {
                        trackerExerciseAdapter.addItem(new TrackerExercise(0, 0));
                    }
                });
    }


    private void initView() {
        recyclerView = findViewById(R.id.rv_adapter);
        mChronometer = findViewById(R.id.chronometer);
        ivTimeRest = findViewById(R.id.iv_time_rest);
        ImageView editStartWorkout = findViewById(R.id.iv_edite_start_workout);
        imgExercise = findViewById(R.id.iv_exercise_img_start_workout);
        ImageView nextExercise = findViewById(R.id.iv_next_exercise_next_workout);
        ivPlayWorkout = findViewById(R.id.iv_play_workout);
        ivStopWorkout = findViewById(R.id.iv_stop_workout);
        tvExerciseName = findViewById(R.id.tv_exercise_name_start_wokout);
        tvTimerUp = findViewById(R.id.tv_start_workout_time_up);
        tvExercisesLeft = findViewById(R.id.tv_exercise_left);

        ivTimeRest.setOnClickListener(this);
        editStartWorkout.setOnClickListener(this);
        ivPlayWorkout.setOnClickListener(this);
        ivStopWorkout.setOnClickListener(this);
        nextExercise.setOnClickListener(this);
    }


    private void checkKeyboard() {
        final LinearLayout contentView = findViewById(R.id.ly_play_stop);
        final LinearLayout llImg = findViewById(R.id.ly_img);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(
                () -> {
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


    @SuppressLint("SetTextI18n")
    private void setupChronometer() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.setText("00:00:00");

        //start
        timeWhenStopped = mChronometer.getBase() - SystemClock.elapsedRealtime();
        mChronometer.start();

        mChronometer.setOnChronometerTickListener(
                chronometerChanged -> {
                    //mChronometer = chronometerChanged;
                    long time = SystemClock.elapsedRealtime() - chronometerChanged.getBase();
                    int h = (int) (time / 3600000);
                    int m = (int) (time - h * 3600000) / 60000;
                    int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                    String t = (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
                    chronometerChanged.setText(t);
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
                // onAddField(v);
                break;
            //remove set
            case R.id.tv_remove_set:
//                countAddSets--;
//                if (countAddSets < 0) {
//                    countAddSets = 0;
//                }
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


    private void addLayoutTimeRestSelected() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.layout_select_rest_time, null);
        dialogBuilder.setView(dialogView);
        initRestTimerLayout(dialogView);
        dialog = dialogBuilder.create();
        dialog.show();
    }


    @SuppressLint({"SetTextI18n", "NewApi"})
    private void nextExerciseClicked() {
        PrefsUtils prefsUtils = new PrefsUtils(this, PrefsUtils.SETTINGS_PREFERENCES_FILE);
        float weight = prefsUtils.getFloat("weight", 1f);
        TDEECalculator tdeeCalculator = new TDEECalculator();
        List<TrackerExercise> newTrackerExerciseList = new ArrayList<>();

        int chronometerInMin = tdeeCalculator.splitChronometer(TAG, mChronometer.getText().toString());
        int caloriesBurned = (int) (tdeeCalculator.caloriesBurned(weight) * chronometerInMin);

//        for (int i = 0; i < trackerExerciseList.size(); i++) {
//            if (trackerExerciseList.get(i).getRepsNumber() == 0 || trackerExerciseList.get(i).getWeight() == 0.0) {
//                trackerExerciseList.remove(i);
//            }
//        }

        trackerExerciseList.removeIf(x -> x.getRepsNumber() == 0 || x.getWeight() == 0.0);


        //Update training
        Training training = trainingList.get(countExercisesLeft - 1);


        for (int i = 0; i < trackerExerciseList.size(); i++) {
//            if (trackerExerciseList.get(i).getTrackerId() != 0) {
//                trackerExerciseList.get(i).setTrackerId(0);
//            }


            if (trackerExerciseList.get(i).getTrackerId() == 0) {
                trackerExerciseList.get(i).setTrainingId(training.getTrainingId());
                newTrackerExerciseList.add(trackerExerciseList.get(i));
            } else {
                planViewModel.updateTracker(trackerExerciseList.get(i));
            }
        }

        //Insert new finish training
        FinishTraining finishTraining = new FinishTraining(mChronometer.getText().toString(), caloriesBurned, training.getTrainingId());
        planViewModel.addNewFinishTrainingAndTracker(finishTraining, newTrackerExerciseList);
        Event.saveEvent(this);
        saveDoneExercise();
        countExercisesLeft++;
        getTraining();
    }

    private void saveDoneExercise() {
        PrefsUtils prefsUtils = new PrefsUtils(this, PrefsUtils.DONE_EXERCISE);
        prefsUtils.saveData(Event.getTodayData() + " " + trainingList.get(countExercisesLeft - 1).getExerciseId(),true);

        PrefsUtils saveExerciseData = new PrefsUtils(this, PrefsUtils.EXERCISE);
        saveExerciseData.saveData("exercise_date", Event.getTodayData());
        saveExerciseData.saveData("exercise_complete",countExercisesLeft);
    }


    private void whatFinished() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you what to finish ?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    startActivity(new Intent(WorkoutStartActivity.this, FinisherWorkoutActivity.class));
                    finish();
                })
                .setNegativeButton("No", (dialogInterface, i) -> {
                    ivPlayWorkout.setVisibility(View.INVISIBLE);
                    ivStopWorkout.setImageResource(R.mipmap.ic_pause);
                    mChronometer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    mChronometer.start();
                    countDownTimer.start();
                    dialogInterface.cancel();
                });
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
