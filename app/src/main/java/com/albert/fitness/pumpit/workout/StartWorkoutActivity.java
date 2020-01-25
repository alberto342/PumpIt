package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.new_adapter.TrainingAdapter;
import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.Exercise;
import com.albert.fitness.pumpit.model.TrackerExercise;
import com.albert.fitness.pumpit.model.Training;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.utils.SwipeHelper;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;
import com.albert.fitness.pumpit.viewmodel.WelcomeActivityViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.databinding.ActivityStartWorkoutBinding;

public class StartWorkoutActivity extends AppCompatActivity {

    private final String TAG = "StartWorkoutActivity";
    private RecyclerView mRecyclerView;
    private TextView emptyWorkout;
    private ImageView btnStartWorkout;
    private List<TrackerExercise> trackerExerciseList = new ArrayList<>();
    private List<Exercise> exerciseList = new ArrayList<>();
    private List<Training> trainingList = new ArrayList<>();
    private AlertDialog dialog;
    private CustomPlanViewModel planViewModel;
    private TrainingAdapter trainingAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStartWorkoutBinding startWorkoutBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_start_workout);
        mRecyclerView = startWorkoutBinding.rvStartWorkout;
        init();
        planViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);
        dateIsChange();
        getWorkout();
        checkIfHavePlans();
        setBtnStartWorkout();
        //swipe();
    }


    @SuppressLint("SetTextI18n")
    private void init() {
        TextView todayExercises = findViewById(R.id.tv_today_exercise);
        emptyWorkout = findViewById(R.id.tv_empty_workout);
        btnStartWorkout = findViewById(R.id.btn_start_workout);
        setTitle("Your exercises for today (" + getDayOfTheWeek() + ")");
        todayExercises.setText("Your exercises for today (" + getDayOfTheWeek() + ")");
    }

    private void dateIsChange() {
        PrefsUtils p = new PrefsUtils(this, PrefsUtils.EXERCISE);
        PrefsUtils prefDoneExercise = new PrefsUtils(this, PrefsUtils.DONE_EXERCISE);
        String date = p.getString("exercise_date", "");

        if(!date.equals(Event.getTodayData())) {
            prefDoneExercise.removeAll(this, PrefsUtils.DONE_EXERCISE);
        }
    }

    private void checkIfHavePlans() {
        PrefsUtils prefsUtils = new PrefsUtils(this, PrefsUtils.EXERCISE);
        String defPlan = prefsUtils.getString("default_plan", "");
        if (defPlan.equals("")) {
            alertDialogHavePlan();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_add, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_plans) {
            PrefsUtils prefsUtils = new PrefsUtils();
            prefsUtils.createSharedPreferencesFiles(this, PrefsUtils.START_WORKOUT);
            prefsUtils.saveData("activity", "StartWorkoutActivity");
            startActivity(new Intent(this, AddExerciseActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NewApi")
    private void getWorkout() {
        SharedPreferences prefs = this.getSharedPreferences(PrefsUtils.DONE_EXERCISE, Context.MODE_PRIVATE);
        Map<String, ?> prefsKeys = prefs.getAll();
        List<Integer> exerciseId = new ArrayList<>();

        planViewModel.getAllTrainingByDate(Event.getDayName())
                .observe(this, trainings -> {
                    if (trainings.isEmpty()) {
                        btnStartWorkout.setVisibility(View.INVISIBLE);
                        emptyWorkout.setVisibility(View.VISIBLE);
                    } else {

                        // trainingList = trainings;

                        for (Map.Entry<String, ?> entry : prefsKeys.entrySet()) {
                            Log.d("map values", entry.getKey() + ": " +
                                    entry.getValue().toString());

                            if (entry.getValue().toString().equals("true")) {
                                String stKey = entry.getKey();
                                String[] parts = stKey.split(" ");
                                int exId = Integer.parseInt(parts[1]);
                                exerciseId.add(exId);
                            }
                        }
                        for (Training tr : trainings) {
                            if (!exerciseId.contains(tr.getExerciseId())) {
                                trainingList.add(tr);
                                getExercise(tr.getExerciseId());
                            }
                        }

                        if(trainingList.size() > 0) {
                            btnStartWorkout.setVisibility(View.VISIBLE);
                            emptyWorkout.setVisibility(View.INVISIBLE);
                        } else {
                            btnStartWorkout.setVisibility(View.INVISIBLE);
                            emptyWorkout.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }


    private void getExercise(final int exerciseId) {
        WelcomeActivityViewModel activityViewModel = ViewModelProviders.of(this).get(WelcomeActivityViewModel.class);
        activityViewModel.getExerciseById(exerciseId)
                .observe(this, exercise -> {
                    if (exercise != null) {
                        exerciseList.add(new Exercise(exercise.getExerciseName(), exercise.getImgName()));
                        initRecyclerView();
                    }
                });
    }


    private void swipe() {
        new SwipeHelper(this, mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(final RecyclerView.ViewHolder viewHolder, final List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#d50000"),
                        pos -> {
                            planViewModel.deleteTraining(trainingList.get(pos));
                            planViewModel.deleteTrackerExerciseByTrainingId(trainingList.get(pos).getTrainingId());
                        }
                ));
//                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                        "Edit",
//                        0,
//                        Color.parseColor("#4caf50"),
//                        pos -> getTracker(pos)
//                ));
            }
        };
    }


    private String getDayOfTheWeek() {
        Date d = new Date();
        switch (d.getDay()) {
            case 0:
                return "Sunday";
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
        }
        return null;
    }

    private void setBtnStartWorkout() {
        btnStartWorkout.setOnClickListener(v ->
                startActivity(new Intent(StartWorkoutActivity.this, WorkoutStartActivity.class)));

    }


    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        trainingAdapter = new TrainingAdapter(exerciseList);
        trainingAdapter.setItems((ArrayList<Training>) trainingList);
        mRecyclerView.setAdapter(trainingAdapter);
        Log.d(TAG, "initRecyclerView: init recyclerView" + mRecyclerView);

        trainingAdapter.setListener((item, i) -> {

        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            int dragFrom = -1;
            int dragTo = -1;

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                        0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();

                if (dragFrom == -1) {
                    dragFrom = from;
                }
                dragTo = to;

                trainingAdapter.notifyItemMoved(from, to);
                return true;
            }

            private void reallyMoved(int from, int to) {
                //planViewModel.updateTrainingPosition(from, to);
                //planViewModel.updateTrainingPosition(to, from);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                planViewModel.deleteTraining(trainingList.get(i));
                planViewModel.deleteTrackerExerciseByTrainingId(trainingList.get(i).getTrainingId());
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return false;
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
                    reallyMoved(dragFrom, dragTo);
                }
                dragFrom = dragTo = -1;
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void alertDialogHavePlan() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("Don't have any plansֿֿ, Do you want to add ?\n Note: if your not add plan your c'not add exercise !!!")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    startActivity(new Intent(StartWorkoutActivity.this, CustomPlanActivity.class));
                    finish();
                })
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
