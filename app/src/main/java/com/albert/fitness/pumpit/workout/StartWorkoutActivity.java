package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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
    TrainingAdapter trainingAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStartWorkoutBinding startWorkoutBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_start_workout);
        mRecyclerView = startWorkoutBinding.rvStartWorkout;
        init();
        planViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);
        getWorkout();
        checkIfHavePlans();
        setBtnStartWorkout();
        swipe();
    }


    @SuppressLint("SetTextI18n")
    private void init() {
        TextView todayExercises = findViewById(R.id.tv_today_exercise);
        emptyWorkout = findViewById(R.id.tv_empty_workout);
        btnStartWorkout = findViewById(R.id.btn_start_workout);
        setTitle("Your exercises for today (" + getDayOfTheWeek() + ")");
        todayExercises.setText("Your exercises for today (" + getDayOfTheWeek() + ")");
    }

    private void checkIfHavePlans() {
        PrefsUtils prefsUtils = new PrefsUtils(this, PrefsUtils.EXERCISE);
        int defPlan = prefsUtils.getInt("default_plan_id", -1);
        if(defPlan == -1) {
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

    private void getWorkout() {
        planViewModel.getAllTrainingByDate(Event.getDayName()).observe(this, new Observer<List<Training>>() {
            @Override
            public void onChanged(List<Training> trainings) {
                if (trainings.isEmpty()) {
                    btnStartWorkout.setVisibility(View.INVISIBLE);
                    emptyWorkout.setVisibility(View.VISIBLE);
                } else {
                    btnStartWorkout.setVisibility(View.VISIBLE);
                    emptyWorkout.setVisibility(View.INVISIBLE);
                    trainingList = trainings;
                    for (Training t : trainings) {
                        getExercise(t.getExerciseId());
                    }
                }
            }
        });
    }


    private void getExercise(final int exerciseId) {
        WelcomeActivityViewModel activityViewModel = ViewModelProviders.of(this).get(WelcomeActivityViewModel.class);
        activityViewModel.getExerciseById(exerciseId).observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                if (exercise != null) {
                    exerciseList.add(new Exercise(exercise.getExerciseName(), exercise.getImgName()));
                    initRecyclerView();
                }
            }
        });
    }

    private void getTracker(final int i) {
        planViewModel.getTrackerExerciseByTraining(trainingList.get(i).getTrainingId())
                .observe(this, new Observer<List<TrackerExercise>>() {
                    @Override
                    public void onChanged(List<TrackerExercise> trackerExercises) {
                        if (!trackerExercises.isEmpty()) {
                            trackerExerciseList = trackerExercises;
                            addLayoutRepsAndSets(i);
                        }
                    }
                });
    }

    // TODO: 2019-10-18 on deleted not the current exercise remove from the screen

    private void swipe() {
        new SwipeHelper(this, mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(final RecyclerView.ViewHolder viewHolder, final List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#d50000"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onClick(int pos) {
                                planViewModel.deleteTraining(trainingList.get(pos));
                            }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        0,
                        Color.parseColor("#4caf50"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                getTracker(pos);
                            }
                        }
                ));
            }
        };
    }


    private void addLayoutRepsAndSets(int position) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.dialog_recive_reps_and_sets, null);

        ImageView btnDoneSetsReps = dialogView.findViewById(R.id.btn_done_edit_sets_reps);
        ImageView btnCancel = dialogView.findViewById(R.id.iv_cancel_weight_reps);

        for (int i = 0; i < trainingList.get(position).getSizeOfRept(); i++) {
            addWeightAndSetIntoLayout(dialogView, i);
        }

        // TODO: 2019-10-17 check how to save this
        btnDoneSetsReps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWeightAndSetIntoLayout(dialogView, -1);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void addWeightAndSetIntoLayout(View view, final int i) {
        LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View rowView = inf.inflate(R.layout.layout_weight_and_reps, null);
        final LinearLayout linearLayout = view.findViewById(R.id.linear_set_reps);
        linearLayout.addView(rowView);

        final EditText etWeight = view.findViewById(R.id.et_weight);
        final EditText etReps = view.findViewById(R.id.et_reps);

        final TrackerExercise trackerExercise = new TrackerExercise(trackerExerciseList.get(i).getRepsNumber(),
                trackerExerciseList.get(i).getWeight(), trainingList.get(i).getTrainingId());
        trackerExercise.setTrackerId(trackerExerciseList.get(i).getTrackerId());


        if (i == -1) {
            Log.d(TAG, "addWeightAndSetIntoLayout: " + etWeight.getText().toString());
        } else {
            etWeight.setText(String.valueOf(trackerExerciseList.get(i).getWeight()));
            etReps.setText(String.valueOf(trackerExerciseList.get(i).getRepsNumber()));
        }
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

//    private String getDayOfTheWeekBeWorkout(int getDay) {
//        switch (getDay) {
//            case 0:
//                return "Day 1";
//            case 1:
//                return "Day 2";
//            case 2:
//                return "Day 3";
//            case 3:
//                return "Day 4";
//            case 4:
//                return "Day 5";
//            case 5:
//                return "Day 6";
//            case 6:
//                return "Day 7";
//        }
//        return "";
//    }

    private void setBtnStartWorkout() {
        btnStartWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartWorkoutActivity.this, WorkoutStartActivity.class));
            }
        });
    }




    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        trainingAdapter = new TrainingAdapter(exerciseList);
        trainingAdapter.setItems((ArrayList<Training>) trainingList);
        mRecyclerView.setAdapter(trainingAdapter);
        Log.d(TAG, "initRecyclerView: init recyclerView" + mRecyclerView);
        trainingAdapter.setListener(new TrainingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Training item, int i) {

            }
        });
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                int from = viewHolder.getAdapterPosition();
//                int to = target.getAdapterPosition();
//                Collections.swap(trainingList, from, to);
//                trainingAdapter.notifyItemMoved(from, to);
//                return true;
//            }
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//                planViewModel.deleteTraining(trainingList.get(viewHolder.getAdapterPosition()));
//            }
//        }).attachToRecyclerView(mRecyclerView);
    }

    private void alertDialogHavePlan() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("Don't have any plansֿֿ, Do you want to add ?\n Note: if your not add plan your c'not add exercise !!!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(StartWorkoutActivity.this,CustomPlanActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


//    private void initRecyclerView() {
//        @SuppressLint("WrongConstant")
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(layoutManager);
//        trainingAdapter = new TrainingAdapter(this, trainingList, exerciseList);
//        trainingAdapter.notifyDataSetChanged();
//        mRecyclerView.setAdapter(trainingAdapter);
//
//        ItemTouchHelper.Callback callback =
//                new SimpleItemTouchHelperCallback(trainingAdapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(mRecyclerView);
//
//        Log.d(TAG, "initRecyclerView: init recyclerView " + mRecyclerView);
//    }
}
