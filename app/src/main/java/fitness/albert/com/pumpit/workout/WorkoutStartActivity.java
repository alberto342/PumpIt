package fitness.albert.com.pumpit.workout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

import fitness.albert.com.pumpit.R;

public class WorkoutStartActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "WorkoutStartActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView timer, exerciseName, timerUp;
    private ImageView timeRest, editStartWorkout, imgExercise, nextExercise, playWorkout, stopWorkout;
    CountDownTimer countDownTimer;
    //layout object
    private TextView s15, s20, s30, s40, s50, s60, s70, s80, s90, s100, s105, s120, s135, s150, s175, min3, min4, min5;
    private ImageView btnCancel;
    private AlertDialog dialog;
    private long timeWhenStopped = 0;
    private Chronometer mChronometer;
    private boolean isStop;
    private boolean countDownTimerIsRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_start);

        init();

        setupChronometer();
    }

    private void init() {
        timer = findViewById(R.id.tv_timer);
        mChronometer = findViewById(R.id.chronometer);
        timeRest = findViewById(R.id.iv_time_rest);
        editStartWorkout = findViewById(R.id.iv_edite_start_workout);
        imgExercise = findViewById(R.id.iv_exercise_img_start_workout);
        nextExercise = findViewById(R.id.iv_next_exercise_start_workout);
        playWorkout = findViewById(R.id.iv_play_workout);
        stopWorkout = findViewById(R.id.iv_stop_workout);
        exerciseName = findViewById(R.id.tv_exercise_name_start_wokout);
        timerUp = findViewById(R.id.tv_start_workout_time_up);

        timeRest.setOnClickListener(this);
        editStartWorkout.setOnClickListener(this);
        playWorkout.setOnClickListener(this);
        stopWorkout.setOnClickListener(this);
        nextExercise.setOnClickListener(this);
    }


    private void initRestTimerLayout(View dialogView) {
        s15 = dialogView.findViewById(R.id.tv_s15);
        s20 = dialogView.findViewById(R.id.tv_s20);
        s30 = dialogView.findViewById(R.id.tv_s30);
        s40 = dialogView.findViewById(R.id.tv_s40);
        s50 = dialogView.findViewById(R.id.tv_s50);
        s60 = dialogView.findViewById(R.id.tv_s60);
        s70 = dialogView.findViewById(R.id.tv_s70);
        s80 = dialogView.findViewById(R.id.tv_s80);
        s90 = dialogView.findViewById(R.id.tv_s90);
        s100 = dialogView.findViewById(R.id.tv_s100);
        s105 = dialogView.findViewById(R.id.tv_s105);
        s120 = dialogView.findViewById(R.id.tv_s120);
        s135 = dialogView.findViewById(R.id.tv_s135);
        s150 = dialogView.findViewById(R.id.tv_s150);
        s175 = dialogView.findViewById(R.id.tv_s175);
        min3 = dialogView.findViewById(R.id.tv_3min);
        min4 = dialogView.findViewById(R.id.tv_4min);
        min5 = dialogView.findViewById(R.id.tv_5min);
        btnCancel = dialogView.findViewById(R.id.btn_cancel_rest_timer);

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

                // mChronometer = chronometerChanged;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_stop_workout:
                startStopChronometer(R.id.iv_stop_workout);
                break;

            case R.id.iv_time_rest:
                if(countDownTimerIsRunning) {
                    countDownTimer.cancel();
                    countDownTimerIsRunning = false;

                } else {
                    addLayoutTimeRestSelected();
                    countDownTimerIsRunning = true;
                }
                break;
            case R.id.iv_edite_start_workout:
                break;
            case R.id.iv_play_workout:
                startStopChronometer(R.id.iv_play_workout);
                break;
            case R.id.iv_next_exercise_start_workout:
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
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(30000);
                dialog.dismiss();
                break;
            case R.id.tv_s40:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(40000);
                dialog.dismiss();
                break;
            case R.id.tv_s50:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(50000);
                dialog.dismiss();
                break;
            case R.id.tv_s60:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(60000);
                dialog.dismiss();
                break;
            case R.id.tv_s70:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(70000);
                dialog.dismiss();
                break;
            case R.id.tv_s80:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(80000);
                dialog.dismiss();
                break;
            case R.id.tv_s90:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(900000);
                dialog.dismiss();
                break;
            case R.id.tv_s100:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(100000);
                dialog.dismiss();
                break;
            case R.id.tv_s105:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(105000);
                dialog.dismiss();
                break;
            case R.id.tv_s120:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(120000);
                dialog.dismiss();
                break;
            case R.id.tv_s135:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(135000);
                dialog.dismiss();
                break;
            case R.id.tv_s150:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(150000);
                dialog.dismiss();
                break;
            case R.id.tv_s175:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(175000);
                dialog.dismiss();
                break;
            case R.id.tv_3min:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(180000);
                dialog.dismiss();
                break;
            case R.id.tv_4min:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(240000);
                dialog.dismiss();
                break;
            case R.id.tv_5min:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                countDownTimer(300000);
                dialog.dismiss();
                break;
            case R.id.btn_cancel_rest_timer:
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
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
                timerUp.setText("done!");
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


}
