package com.albert.fitness.pumpit.workout;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.albert.fitness.pumpit.adapter.exercise_adapter.ExerciseAdapter;
import com.albert.fitness.pumpit.model.TrackerExercise;
import com.albert.fitness.pumpit.model.Training;
import com.albert.fitness.pumpit.model.UserRegister;
import com.albert.fitness.pumpit.utils.PrefsUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import fitness.albert.com.pumpit.R;


public class TrackerExerciseFragment extends Fragment {

    private LinearLayout container;
    private ArrayList<CharSequence> trackerList;
    private final String TAG = "AndroidDynamicView";
    private EditText weight, reps, setsRest, exerciseRest;
    private Button btnAddTracker, saveTracker;
   // private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PrefsUtils prefsUtilsGetActivity = new PrefsUtils();
    private PrefsUtils prefsUtilsDefaultExercise = new PrefsUtils();
    private boolean workoutNameExist;

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

        init(view);

        saveClick();

        //set tracker exercise
        btnAddTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }


    private void init(View view) {
        trackerList = new ArrayList<>();
        weight = view.findViewById(R.id.et_set_weight);
        reps = view.findViewById(R.id.et_reps);
        btnAddTracker = view.findViewById(R.id.btn_add_tracker);
        container = view.findViewById(R.id.container_tracker);
        saveTracker = view.findViewById(R.id.btn_save_tracker);
        setsRest = view.findViewById(R.id.et_rest_between_sets);
        exerciseRest = view.findViewById(R.id.et_sec_rest_after_exercise);
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
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) newView.getParent()).removeView(newView);
                trackerList.remove(newText);
                trackerList.remove(newTex2);

            }
        });
        container.addView(newView);
        trackerList.add(newText);
        trackerList.add(newTex2);
    }

    //save all date into firebase
    private void saveClick() {
        saveTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveData();
            }
        });
    }


    private void receiveData() {
        PrefsUtils prefsUtilsGetID = new PrefsUtils();
        prefsUtilsGetID.createSharedPreferencesFiles(getActivity(), "exercise");
        final String getPlanId = prefsUtilsGetID.getString("planName", "");

        prefsUtilsGetActivity.createSharedPreferencesFiles(getActivity(), PrefsUtils.START_WORKOUT);
        String activity1 = prefsUtilsGetActivity.getString("activity", "");
        String activity2 = prefsUtilsGetActivity.getString("activity2", "");

//        String rest = setsRest.getText().toString();
//        String[] parts = rest.split(" ");
//        int restBetweenSet = Integer.parseInt(parts[0]);
//
//        String restExercise = exerciseRest.getText().toString();
//        String[] part = restExercise.split(" ");
//        int restAfterExercise = Integer.parseInt(part[0]);

        final List<TrackerExercise> weightList = new ArrayList<>();
        List<TrackerExercise> repNumberList = new ArrayList<>();
        List<TrackerExercise> trackerExerciseList = new ArrayList<>();

        for (int i = 0; i < trackerList.size(); i++) {
            switch (i % 2) {
                case 0:
                    weightList.add(new TrackerExercise(Float.valueOf(trackerList.get(i).toString())));
                    break;
                case 1:
                    repNumberList.add(new TrackerExercise(Integer.valueOf(trackerList.get(i).toString())));
                    break;
            }
        }

        for (int i = 0; i < weightList.size(); i++) {
            trackerExerciseList.add(new TrackerExercise(repNumberList.get(i).getRepNumber(), weightList.get(i).getWeight()));
        }


        final Training training = new Training(ExerciseAdapter.exerciseImg,ExerciseAdapter.exerciseName,
                Integer.valueOf(setsRest.getText().toString()),Integer.valueOf(exerciseRest.getText().toString()),UserRegister.getTodayDate(),
                trackerList.size() / 2,0);






        if (activity1.equals("StartWorkoutActivity") && activity2.equals("ShowExerciseResultActivity")) {
        //    saveTrackerFromStartWorkout(getPlanId, training);
        } else {
        //    saveTrackerIntoFb(getPlanId, training);
        }
    }


//    private void saveTrackerIntoFb(final String getPlanId, final Training training) {
//        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(Workout.WORKOUT_NAME)
//                .document(getPlanId).collection(Workout.WORKOUT_DAY_NAME).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            final String workoutDayNameId = task.getResult().getDocuments().get(WorkoutAdapter.pos).getId();
//                            db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(Workout.WORKOUT_NAME)
//                                    .document(getPlanId).collection(Workout.WORKOUT_DAY_NAME)
//                                    .document(workoutDayNameId).collection(Workout.EXERCISE_NAME)
//                                    .document(ExerciseAdapter.exerciseName)
//                                    .set(training)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            Log.d(TAG, "onComplete: success saved  workout tracker");
//                                            Intent intent = new Intent(getActivity(), TrainingActivity.class);
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            startActivity(intent);
//                                            Objects.requireNonNull(getActivity()).finish();
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Log.d(TAG, "Error writing document", e);
//                                        }
//                                    });
//                        }
//                    }
//                });
//    }


//    private void saveTrackerFromStartWorkout(final String getPlanId, final Training training) {
//        prefsUtilsDefaultExercise.createSharedPreferencesFiles(getActivity(), PrefsUtils.DEFAULT_EXERCISE);
//        final String defaultExercise = prefsUtilsDefaultExercise.getString("e_" + currentDayName(), "");
//
//        if (defaultExercise.isEmpty()) {
//            createWorkoutDayName(getPlanId);
//        } else {
//            db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(Workout.WORKOUT_NAME)
//                    .document(getPlanId).collection(Workout.WORKOUT_DAY_NAME)
//                    .document(defaultExercise).collection(Workout.EXERCISE_NAME)
//                    .document(ExerciseAdapter.exerciseName).set(training)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            updateWorkoutDayName(getPlanId, defaultExercise);
//                            prefsUtilsGetActivity.removeAll(getActivity(), PrefsUtils.START_WORKOUT);
//                            Intent intent = new Intent(getActivity(), StartWorkoutActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            Objects.requireNonNull(getActivity()).finish();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.i(TAG, "onFailure: " + e.getMessage());
//                        }
//                    });
//        }
//    }


//    private void createWorkoutDayName(String getPlanId) {
//        Log.i(TAG, "createWorkoutDayName:  workoutNameExist: " + workoutNameExist);
//
//        prefsUtilsDefaultExercise.createSharedPreferencesFiles(getActivity(), PrefsUtils.DEFAULT_EXERCISE);
//
//        Workout workout = new Workout(UserRegister.getTodayDate(), dayWorkout(), currentDayName(), 0, 0);
//        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(Workout.WORKOUT_NAME)
//                .document(getPlanId).collection(Workout.WORKOUT_DAY_NAME).add(workout)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//
//                prefsUtilsDefaultExercise.saveData("e_" + currentDayName(), documentReference.getId());
//                workoutNameExist = true;
//                Log.d(TAG, "onSuccess:  Create Workout Day Name");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.i(TAG, "onFailure: " + e.getMessage());
//            }
//        });
//    }

//    private void updateWorkoutDayName(final String getPlanId, final String workoutId) {
//
//        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
//                .collection(Workout.WORKOUT_NAME).document(getPlanId)
//                .collection(Workout.WORKOUT_DAY_NAME).document(workoutId)
//                .collection(Workout.EXERCISE_NAME)
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                int sizeOfExercise = task.getResult().size();
//
//                DocumentReference reference = db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(Workout.WORKOUT_NAME)
//                        .document(getPlanId).collection(Workout.WORKOUT_DAY_NAME).document(workoutId);
//                reference.update("numOfExercise", sizeOfExercise);
//            }
//        });
//
//
//    }


    private String currentDayName() {
        Calendar calendar = Calendar.getInstance();
        String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Log.d(TAG, "currentDayName: " + " The day is: " + days[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        return days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }

    private String dayWorkout() {
        Calendar calendar = Calendar.getInstance();
        String[] days = new String[]{"Workout 1", "Workout 2", "Workout 3", "Workout 4", "Workout 5", "Workout 6", "Workout 7"};
        Log.d(TAG, "currentDayName: " + " The day is: " + days[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        return days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }


    private void setError(EditText text, String errorMessage) {
        if (text.getText().toString().isEmpty()) {
            text.setError(errorMessage);
        }
    }


}
