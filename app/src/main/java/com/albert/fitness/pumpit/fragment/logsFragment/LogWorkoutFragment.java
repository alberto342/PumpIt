package com.albert.fitness.pumpit.fragment.logsFragment;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.FinishWorkoutAdapter;
import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.Exercise;
import com.albert.fitness.pumpit.model.QueryFinishWorkout;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.utils.SwipeHelper;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;
import com.albert.fitness.pumpit.viewmodel.WelcomeActivityViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fitness.albert.com.pumpit.R;


public class LogWorkoutFragment extends Fragment {

    private final String TAG = "LogWorkoutFragment";
    private RecyclerView rvWorkout;
    private CustomPlanViewModel customPlanViewModel;
    private WelcomeActivityViewModel welcomeActivityViewModel;
    private List<Exercise> exerciseList = new ArrayList<>();
    private Map<String, Object> mapFinishWorkouts = new HashMap<>();
    private List<QueryFinishWorkout> queryFinishWorkoutsList = new ArrayList<>();
    private FinishWorkoutAdapter finishWorkoutAdapter;
    private String date = LogFragment.date;
    public static List<Integer> finishIdList = new ArrayList<>();

    public LogWorkoutFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_workout, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customPlanViewModel = ViewModelProviders.of(getActivity()).get(CustomPlanViewModel.class);
        welcomeActivityViewModel = ViewModelProviders.of(getActivity()).get(WelcomeActivityViewModel.class);
        init(view);
        getWorkout();
        swipe();
        Log.d(TAG, "date of exercise: " + LogFragment.date);
    }

    private void init(View view) {
        rvWorkout = view.findViewById(R.id.rv_log_workout);
    }


    private void getWorkout() {
        Set<Integer> newExerciseId = new HashSet<>();
        customPlanViewModel.getFinishWorkout(date)
                .observe(this, queryFinishWorkouts -> {
                    if(queryFinishWorkouts.size() == 0) {
                        listIsEmpty();
                    }

                    queryFinishWorkoutsList.addAll(queryFinishWorkouts);
                    for (QueryFinishWorkout finishWorkout : queryFinishWorkouts) {
                        newExerciseId.add(finishWorkout.getExerciseId());

                        finishIdList.add(finishWorkout.getFinishId());
                        Log.d(TAG, "getWorkout: " + finishWorkout.getFinishId()); ////**** working


                        //finishWorkout.getTrackerId()
                    }
                    for (int exerciseId : newExerciseId) {
                        welcomeActivityViewModel.getExerciseById(exerciseId)
                                .observe(this, exercise -> {
                                    if (exercise != null) {
                                        exerciseList.add(exercise);
                                        customPlanViewModel.getFinishWorkoutByDateAndExerciseId(date, exerciseId)
                                                .observe(this, queryFinishWorkoutList -> {
                                                    if (!queryFinishWorkoutList.isEmpty()) {
                                                        for (int i = 0; i < queryFinishWorkoutList.size(); i++) {
                                                            if (exercise.getExerciseName() != null) {
                                                                mapFinishWorkouts.put(exercise.getExerciseName() + i, queryFinishWorkoutList.get(i).getWeight());
                                                                mapFinishWorkouts.put(exercise.getExerciseName() + " rept " + i, queryFinishWorkoutList.get(i).getRepsNumber());
                                                            }
                                                        }
                                                        mapFinishWorkouts.values().removeAll(Collections.singleton(null));
                                                        initRecyclerView();
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init FinishWorkout recyclerView" + rvWorkout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvWorkout.setLayoutManager(layoutManager);
        finishWorkoutAdapter = new FinishWorkoutAdapter(getActivity(), exerciseList, mapFinishWorkouts);
        rvWorkout.setAdapter(finishWorkoutAdapter);
    }


    private void swipe() {
        new SwipeHelper(getContext(), rvWorkout) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        // R.color.colorAccent,
                        Color.parseColor("#d50000"),
                        pos -> {
                            deleteItem(pos);
                            finishWorkoutAdapter.removeItem(pos);
                        }
                ));
//                underlayButtons.add(new SwipeHelper.UnderlayButton(
//                        "Edit",
//                        0,
//                        //R.color.md_green_500,
//                        Color.parseColor("#4caf50"),
//                        pos -> {
//
//                        }
//                ));
            }
        };
    }


    private void deleteItem(int pos) {
        int exerciseId = queryFinishWorkoutsList.get(pos).getExerciseId();
        customPlanViewModel.getFinishWorkout(date)
                .observe(this, queryFinishWorkoutList -> {
                    if(!queryFinishWorkoutList.isEmpty()) {
                        for(QueryFinishWorkout workout : queryFinishWorkoutList) {
                            if(exerciseId == workout.getExerciseId()) {
                                customPlanViewModel.deleteFinishTrainingByFinishId(workout.getFinishId());
                                //customPlanViewModel.deleteTrackerExerciseByTrackerId(workout.getTrackerId());
                            }
                        }
                    }
                });
    }

    private void listIsEmpty() {
        PrefsUtils p = new PrefsUtils(getContext(), PrefsUtils.HAVE_WORKOUT_EVENT);
        PrefsUtils prefsUtils = new PrefsUtils(getContext(), PrefsUtils.HAVE_NUTRITION_EVENT);
        p.saveData(date, true);
        boolean haveNutrition = prefsUtils.getBoolean(date, false);

        if(haveNutrition) {
            Event.removeEvent(getContext());
        }
    }
}
