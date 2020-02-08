package com.albert.fitness.pumpit.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.Training;
import com.albert.fitness.pumpit.model.UserRegister;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;
import com.albert.fitness.pumpit.workout.CustomPlanActivity;
import com.albert.fitness.pumpit.workout.FindWorkoutActivity;
import com.albert.fitness.pumpit.workout.StartWorkoutActivity;

import java.util.List;

import fitness.albert.com.pumpit.R;


public class WorkoutFragment extends Fragment implements View.OnClickListener {

    private TextView tvExerciseName, tvWorkoutComplete, tvEmptyExercise;
    private ImageView ivFindWorkout, btnStartWorkout, ivFitness;
    private PrefsUtils prefsUtils;
    private int exComplete = 0;
    private CustomPlanViewModel customPlanViewModel;

    public WorkoutFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolBar();
        init(view);
        prefsUtils = new PrefsUtils(getActivity(), PrefsUtils.EXERCISE);
        setFitnessImg();
        checkIfDateIsChange();
        customPlanViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);

        ivFindWorkout.setOnClickListener(this);
        tvEmptyExercise.setOnClickListener(this);
        btnStartWorkout.setOnClickListener(this);
        getAllTrainingByDate();

        // loadExerciseFromFb();
    }

    private void checkIfDateIsChange() {
        String todayData = prefsUtils.getString("today_date", "");
        String planName = prefsUtils.getString("default_plan", "");

        if (!planName.isEmpty()) {
            tvExerciseName.setText(planName);
        }

        if (!todayData.equals(UserRegister.getTodayDate())) {
            prefsUtils.saveData("exercise_complete", 0);
        }
    }


    private void setToolBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        assert activity.getSupportActionBar() != null;
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorDarkBlue)));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //  loadExerciseFromFb();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    private void init(View view) {
        ivFindWorkout = view.findViewById(R.id.btn_find_workout);
        tvEmptyExercise = view.findViewById(R.id.empty_exersis);
        tvWorkoutComplete = view.findViewById(R.id.workout_complate);
        tvExerciseName = view.findViewById(R.id.workout_name);
        btnStartWorkout = view.findViewById(R.id.btn_workout);
        ivFindWorkout = view.findViewById(R.id.hd_fitness_pic);
    }

    private void setFitnessImg() {
        PrefsUtils p = new PrefsUtils(getContext(), PrefsUtils.SETTINGS_PREFERENCES_FILE);
       boolean isMale =  p.getBoolean("is_male", false);

       if(isMale) {
           ivFindWorkout.setImageResource(R.mipmap.hd_men_fitness);
       } else {
           ivFindWorkout.setImageResource(R.mipmap.hd_female_fitness);
       }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.empty_exersis:
                startActivity(new Intent(getActivity(), CustomPlanActivity.class));
                break;
            case R.id.btn_find_workout:
                startActivity(new Intent(getActivity(), FindWorkoutActivity.class));
                break;
            case R.id.btn_workout:
                startActivity(new Intent(getActivity(), StartWorkoutActivity.class));
                break;
        }
    }

    private void getAllTrainingByDate() {
        exComplete = prefsUtils.getInt("exercise_complete", 0);
       // Set<Integer>exerciseId = new HashSet<>();
        customPlanViewModel.getAllTrainingByDate(Event.getDayName()).observe(this, new Observer<List<Training>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<Training> trainings) {

                if (trainings.isEmpty()) {
                    haveExercise(false);
                } else {
                    haveExercise(true);

                    if(exComplete > trainings.size()) {
                        tvWorkoutComplete.setText("Successfully all exercises complete!");
                    } else {
                        tvWorkoutComplete.setText(exComplete + "/" + trainings.size() + " Workout complete");
                    }
                }
            }
        });
    }


    //check if have default plan
    private void haveExercise(boolean exerciseExisting) {
        final String routineName;
        final String TAG = "WorkoutFragment";
        if (exerciseExisting) {
            tvEmptyExercise.setVisibility(TextView.INVISIBLE);
            tvWorkoutComplete.setVisibility(TextView.VISIBLE);
            tvExerciseName.setVisibility(TextView.VISIBLE);

            if (!prefsUtils.getString(PrefsUtils.DEFAULT_PLAN, "").isEmpty()) {
                routineName = prefsUtils.getString(PrefsUtils.DEFAULT_PLAN, "");
                Log.d(TAG, "routineName: " + routineName);

                tvExerciseName.setText(routineName);
                tvExerciseName.setTextColor(Color.WHITE);
            }
            prefsUtils.saveData("defaultExercise", true);
        } else {
            tvEmptyExercise.setVisibility(TextView.VISIBLE);
            tvWorkoutComplete.setVisibility(TextView.INVISIBLE);
            tvExerciseName.setVisibility(TextView.INVISIBLE);
        }
    }
}
