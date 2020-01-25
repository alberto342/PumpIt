package com.albert.fitness.pumpit.workout;


import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.TrackerExercise;
import com.albert.fitness.pumpit.model.Training;
import com.albert.fitness.pumpit.model.UserRegister;
import com.albert.fitness.pumpit.model.WorkoutObj;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fitness.albert.com.pumpit.R;


public class TrackerExerciseFragment extends Fragment {

    private static final String TAG = "TrackerExerciseFragment";
    private LinearLayout container;
    private CustomPlanViewModel planViewModel;
    private ArrayList<CharSequence> trackerList;
    private List<TrackerExercise> trackerExerciseList = new ArrayList<>();
    private List<Float> weightList = new ArrayList<>();
    private List<Integer> repNumberList = new ArrayList<>();
    private EditText weight, reps, restBetweenSets, restAfterExercise;
    private Button btnAddTracker;
    private PrefsUtils prefsUtilsGetActivity, prefsUtils;
    private int trainingId, workoutId, sizeOfTraining, exerciseId;
    private boolean isInTraining = true, createWorkout = true;


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
        ExerciseDetailActivity resultActivity = (ExerciseDetailActivity) getActivity();
        exerciseId = resultActivity.getExerciseId();
        prefsUtils = new PrefsUtils(getActivity(), "exercise");
        workoutId = prefsUtils.getInt("workoutId", 0);
        init(view);
        setTrackerExercise();
        getLastTraining();
        getTraining();
    }


    private void init(View view) {
        trackerList = new ArrayList<>();
        weight = view.findViewById(R.id.et_set_weight);
        reps = view.findViewById(R.id.et_reps);
        btnAddTracker = view.findViewById(R.id.btn_add_tracker);
        container = view.findViewById(R.id.container_tracker);
        restBetweenSets = view.findViewById(R.id.et_rest_between_sets);
        restAfterExercise = view.findViewById(R.id.et_sec_rest_after_exercise);
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
        planViewModel.getTrainingByWorkoutId(workoutId).observe(this, trainings -> {
            if (trainings.isEmpty()) {
                sizeOfTraining = 0;
            } else {
                sizeOfTraining = trainings.size();
            }
        });
    }

    private void setTrackerExercise() {
        btnAddTracker.setOnClickListener(v -> {
            if (weight.getText().toString().isEmpty() && reps.getText().toString().isEmpty()) {
                setError(weight, "Enter weight");
                return;
            }
            if (reps.getText().toString().isEmpty()) {
                setError(reps, "Enter reps");
                return;
            }
            addNewView(weight.getText().toString(), reps.getText().toString());
            weight.setText("");
            reps.setText("");
        });
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState()");
        outState.putCharSequenceArrayList("KEY_ITEMS", trackerList);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            Log.i(TAG, "onRestoreInstanceState()");
            ArrayList<CharSequence> savedItemList = savedInstanceState.getCharSequenceArrayList("KEY_ITEMS");
            if (savedItemList != null) {
                for (CharSequence s : savedItemList) {
                    addNewView(s, s);
                }
            }
        }
    }


    //method to add tracker in the view page
    private void addNewView(final CharSequence newText, final CharSequence newTex2) {
        LayoutInflater layoutInflater = (LayoutInflater) Objects.requireNonNull(getActivity()).getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View newView = layoutInflater.inflate(R.layout.row_workout_tracker, null);
        TextView tvWeight = newView.findViewById(R.id.tv_weight);
        tvWeight.setText(newText);
        TextView tvReps = newView.findViewById(R.id.tv_reps);
        tvReps.setText(newTex2);
        final TextView tvCount = newView.findViewById(R.id.tv_count);
        if (trackerList.size() == 0) {
            tvCount.setText(String.valueOf(1));
        } else {
            tvCount.setText(String.valueOf(trackerList.size() / 2 + 1));
        }

        ImageButton buttonRemove = newView.findViewById(R.id.iv_remove_tracker);
        buttonRemove.setOnClickListener(v -> {
            ((LinearLayout) newView.getParent()).removeView(newView);
            trackerList.remove(newText);
            trackerList.remove(newTex2);
        });
        container.addView(newView);
        trackerList.add(newText);
        trackerList.add(newTex2);
    }


    private void setData() {
        prefsUtilsGetActivity = new PrefsUtils(getActivity(), PrefsUtils.START_WORKOUT);
        String activity1 = prefsUtilsGetActivity.getString("activity", "");
        String activity2 = prefsUtilsGetActivity.getString("activity2", "");

        for (int i = 0; i < trackerList.size(); i++) {
            switch (i % 2) {
                case 0:
                    weightList.add(Float.valueOf(trackerList.get(i).toString()));
                    break;
                case 1:
                    repNumberList.add(Integer.valueOf(trackerList.get(i).toString()));
                    break;
            }
        }

        Training training = new Training(exerciseId, Integer.valueOf(restBetweenSets.getText().toString()),
                Integer.valueOf(this.restAfterExercise.getText().toString()), trackerList.size() / 2,
                UserRegister.getTodayDate(), sizeOfTraining, workoutId);

        for (int i = 0; i < weightList.size(); i++) {
            TrackerExercise trackerExercise = new TrackerExercise(repNumberList.get(i), weightList.get(i), trainingId);
            trackerExerciseList.add(trackerExercise);
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
        planViewModel.getWorkoutByWorkoutDay(Event.getDayName()).observe(this, workoutObj -> {
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
