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

import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.UserRegister;
import com.albert.fitness.pumpit.model.Workout;
import com.albert.fitness.pumpit.model.WorkoutPlans;
import com.albert.fitness.pumpit.utils.FireBaseInit;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.workout.CustomPlanActivity;
import com.albert.fitness.pumpit.workout.FindWorkoutActivity;
import com.albert.fitness.pumpit.workout.StartWorkoutActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import fitness.albert.com.pumpit.R;


public class WorkoutFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "WorkoutFragment";
    private boolean exerciseExist;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView tvExerciseName, tvWorkoutComplete, tvEmptyExercise;
    private ImageView btnAdd, ivFindWorkout, btnStartWorkout;
    private PrefsUtils prefsUtils;
    private int exComplete = 0;

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
        checkIfDateIsChange();

        ivFindWorkout.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnStartWorkout.setOnClickListener(this);
       // loadExerciseFromFb();
    }

    private void checkIfDateIsChange() {
        String todayData = prefsUtils.getString("today_date", "");

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
        btnAdd = view.findViewById(R.id.btn_add);
        btnStartWorkout = view.findViewById(R.id.btn_workout);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
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


//    private void loadExerciseFromFb() {
//        final ProgressDialog progressdialog = new ProgressDialog(getContext());
//        progressdialog.setMessage("Please Wait....");
//        progressdialog.show();
//
//        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
//                .collection(WorkoutPlans.WORKOUT_NAME).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        progressdialog.hide();
//
//                        if (task.isSuccessful() && !task.getResult().getDocuments().isEmpty()) {
//                            exerciseExist = task.getResult().getDocuments().size() > 0;
//                            haveExercise(exerciseExist);
//                            progressdialog.hide();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "onFailure: " + e);
//                    }
//                });
//    }


    //check if have default plan
    private void haveExercise(boolean exerciseExisting) {

        final String routineName;
        final String TAG = "WorkoutFragment";

        if (exerciseExisting) {
            checkHowManyExercises();

            tvEmptyExercise.setVisibility(TextView.INVISIBLE);
            btnAdd.setVisibility(View.INVISIBLE);
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
            btnAdd.setVisibility(View.VISIBLE);
            tvEmptyExercise.setVisibility(TextView.VISIBLE);
            tvWorkoutComplete.setVisibility(TextView.INVISIBLE);
            tvExerciseName.setVisibility(TextView.INVISIBLE);
        }
    }


    private void checkHowManyExercises() {

        Event event = new Event();
        final String planName = prefsUtils.getString("planName", "");
        exComplete = prefsUtils.getInt("exercise_complete", 0);

        if (!planName.isEmpty()) {
            db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                    .collection(WorkoutPlans.WORKOUT_NAME).document(planName)
                    .collection(Workout.WORKOUT_DAY_NAME)
                    .whereEqualTo("workoutDay", event.getDayName()).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {

                                for (int i = 0; i < task.getResult().size(); i++) {
                                    String id = task.getResult().getDocuments().get(i).getId();
                                    Log.d(TAG, "onComplete get id of exercise: " + id);

                                    db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                                            .collection(WorkoutPlans.WORKOUT_NAME).document(planName)
                                            .collection(Workout.WORKOUT_DAY_NAME).document(id)
                                            .collection(Workout.EXERCISE_NAME).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @SuppressLint("SetTextI18n")
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if (!queryDocumentSnapshots.isEmpty()) {
                                                        int sizeOfExercise = queryDocumentSnapshots.getDocuments().size();
                                                        tvWorkoutComplete.setText(exComplete + "/" + sizeOfExercise + " Workout complete");
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "onFailure check size of exercise ");
                                        }
                                    });
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "onFailure: check How Many Exercises " + e);
                }
            });
        }
    }
}
