package com.albert.fitness.pumpit.workout;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.NewTrackerExerciseAdapter;
import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.TrackerExercise;
import com.albert.fitness.pumpit.model.Training;
import com.albert.fitness.pumpit.model.UserRegister;
import com.albert.fitness.pumpit.model.WorkoutObj;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;
import com.albert.fitness.pumpit.viewmodel.WelcomeActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fitness.albert.com.pumpit.R;


public class TrackerExerciseFragment extends Fragment {

    private static final String TAG = "TrackerExerciseFragment";
    private CustomPlanViewModel planViewModel;
    private WelcomeActivityViewModel activityViewModel;
    private ArrayList<CharSequence> trackerList;
    private List<TrackerExercise> trackerExerciseList = new ArrayList<>();
    private TextView tvWeight;
    private EditText weight, reps, restBetweenSets, restAfterExercise;
    private Button btnAddTracker;
    private PrefsUtils prefsUtilsGetActivity, prefsUtils;
    private int trainingId, workoutId, sizeOfTraining, exerciseId;
    private boolean isInTraining = true, createWorkout = true, isWeightGone;
    private RecyclerView mRecyclerView;


    // TODO: 2019-10-17 Need to add index_of_training


    public TrackerExerciseFragment() {


    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tracker_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        planViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);
        activityViewModel = ViewModelProviders.of(this).get(WelcomeActivityViewModel.class);
        ExerciseDetailActivity resultActivity = (ExerciseDetailActivity) getActivity();
        exerciseId = resultActivity.getExerciseId();
        prefsUtils = new PrefsUtils(getActivity(), "exercise");
        workoutId = prefsUtils.getInt("workoutId", 0);
        init(view);
        getDefRestAfterAndBetweenExercise();
        getCategories();
        getLastTraining();
        setTrackerExercise();
        getTraining();
    }


    private void init(View view) {
        trackerList = new ArrayList<>();
        weight = view.findViewById(R.id.et_set_weight);
        reps = view.findViewById(R.id.et_reps);
        tvWeight = view.findViewById(R.id.tv_weight_tx);
        btnAddTracker = view.findViewById(R.id.btn_add_tracker);
        mRecyclerView = view.findViewById(R.id.rv_tracker_exercise_ad);
        restBetweenSets = view.findViewById(R.id.et_rest_between_sets);
        restAfterExercise = view.findViewById(R.id.et_sec_rest_after_exercise);
    }

    private void getDefRestAfterAndBetweenExercise() {
        PrefsUtils prefsUtils = new PrefsUtils(getActivity(), PrefsUtils.SETTINGS_PREFERENCES_FILE);
        String restBetweenSets = prefsUtils.getString("rest_between_sets", "");
        String restAfterExercise = prefsUtils.getString("rest_after_exercise", "");

        if(!restBetweenSets.isEmpty()) {
            this.restBetweenSets.setText(restBetweenSets);
        }
        if(!restAfterExercise.isEmpty()) {
            this.restAfterExercise.setText(restAfterExercise);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.m_save_tracker) {
            setData();
        }
        return true;
    }

    private void getTraining() {
        planViewModel.getTrainingByWorkoutId(workoutId)
                .observe(this, trainings -> {
                    if (trainings.isEmpty()) {

                        sizeOfTraining = 0;
                    } else {
                        sizeOfTraining = trainings.size();
                    }
                });
    }

    private void getCategories() {
        activityViewModel.getExerciseById(exerciseId)
                .observe(this, exercise -> {
                    if (exercise != null) {
                        if (exercise.getCategoryId() == 4 || exercise.getCategoryId() == 2) {
                            weight.setVisibility(View.GONE);
                            tvWeight.setVisibility(View.GONE);
                            isWeightGone = true;
                        }
                    }
                });
    }

    private void setTrackerExercise() {
        final int[] iReps = new int[1];
        final float[] fWeight = new float[1];

        btnAddTracker.setOnClickListener(v -> {
            //set error
            if (weight.getText().toString().isEmpty() && reps.getText().toString().isEmpty() && !isWeightGone) {
                setError(weight, "Enter weight");
                setError(reps, "Enter reps");
                return;
            }

            if (weight.getText().toString().isEmpty() && !isWeightGone) {
                setError(weight, "Enter weight");
                return;
            }
            if (reps.getText().toString().isEmpty()) {
                setError(reps, "Enter reps");
                return;
            }

            iReps[0] = Integer.parseInt(reps.getText().toString());
            fWeight[0] = isWeightGone ? 0f : Float.valueOf(weight.getText().toString());

            if (iReps[0] == 0) {
                reps.setText("");
                setError(reps, "0 Not acceptable");
                return;
            }

            if (fWeight[0] == 0.0 && !isWeightGone) {
                weight.setText("");
                setError(weight, "0.0 Not acceptable");
                return;
            }

            //add into list
            trackerExerciseList.add(new TrackerExercise(iReps[0], fWeight[0]));
            initRecyclerView();
            weight.setText("");
            reps.setText("");
        });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView " + trackerExerciseList);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        NewTrackerExerciseAdapter exerciseAdapter = new NewTrackerExerciseAdapter(trackerExerciseList, isWeightGone);
        mRecyclerView.setAdapter(exerciseAdapter);
        exerciseAdapter.setOnItemClickListener(((position, v) -> {
            if (v.getTag().equals(R.drawable.ic_delete_black)) {
                exerciseAdapter.deleteItem(position);
            }
        }));
    }


    private void setData() {
        prefsUtilsGetActivity = new PrefsUtils(getActivity(), PrefsUtils.START_WORKOUT);
        String activity1 = prefsUtilsGetActivity.getString("activity", "");
        String activity2 = prefsUtilsGetActivity.getString("activity2", "");

        Training training = new Training(exerciseId, Integer.valueOf(restBetweenSets.getText().toString()),
                Integer.valueOf(this.restAfterExercise.getText().toString()), trackerList.size() / 2,
                UserRegister.getTodayDate(), sizeOfTraining, workoutId);

        for (int i = 0; i < trackerExerciseList.size(); i++) {
            trackerExerciseList.get(i).setTrainingId(trainingId);
        }

        if (activity1.equals("StartWorkoutActivity") && activity2.equals("ShowExerciseResultActivity")) {
            saveTrackerFromStartWorkout(training);
        } else {
            saveTraining(training);
        }
    }


    private void saveTraining(final Training training) {
        planViewModel.addNewTrainingAndTracker(training, trackerExerciseList);
        Log.d(TAG, "onComplete: success saved  workout tracker");
        Intent intent = new Intent(getActivity(), TrainingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }


    private void getLastTraining() {
        planViewModel.getLastId().observe(this, integer -> {
            if (integer != null && isInTraining) {
                //saveTracker(integer);
                prefsUtils.saveData("training_id", integer);
                trainingId = integer + 1;
                isInTraining = false;
            } else if (prefsUtils.getInt("training_id", -1) == -1) {
                trainingId = 1;
            } else {
                trainingId = prefsUtils.getInt("training_id", 1) + 1;
            }
        });
    }

    private void saveTrackerFromStartWorkout(final Training training) {
        planViewModel.getWorkoutByWorkoutDay(Event.getDayName())
                .observe(this, workoutObj -> {
                    if (workoutObj == null) {
                        createWorkoutDayName();
                    }
                    getWorkoutOfCurrentDayAndAddTraining(training);
                    goToStartWorkoutActivity();
                });
    }

    private void createWorkoutDayName() {
        int getPlanId = prefsUtils.getInt("default_plan_id", -1);
        if (getPlanId != -1) {
            planViewModel.addNewWorkout(new WorkoutObj(Event.getWorkoutDayName(), Event.getDayName(),
                    Event.getTodayData(), getPlanId));
        }
    }

    private void getWorkoutOfCurrentDayAndAddTraining(final Training training) {
        planViewModel.getWorkoutByWorkoutDay(Event.getDayName()).observe(this, workoutObj -> {
            if (workoutObj != null && createWorkout) {
                Log.d(TAG, "getWorkoutId: " + workoutObj.getWorkoutId());
                training.setWorkoutId(workoutObj.getWorkoutId());

                // executor = Executors.newFixedThreadPool(5);
                Executor myExecutor = Executors.newSingleThreadExecutor();
                myExecutor.execute(() -> {
                    planViewModel.addNewTrainingAndTracker(training, trackerExerciseList);
                    createWorkout = false;
                });
            }
        });
    }


    private void goToStartWorkoutActivity() {
        prefsUtilsGetActivity.removeAll(getActivity(), PrefsUtils.START_WORKOUT);
        Intent intent = new Intent(getActivity(), StartWorkoutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }

    private void setError(EditText text, String errorMessage) {
        if (text.getText().toString().isEmpty()) {
            text.setError(errorMessage);
        }
    }


}
