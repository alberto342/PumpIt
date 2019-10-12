package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.TrainingAdapter;
import com.albert.fitness.pumpit.adapter.WorkoutAdapter;
import com.albert.fitness.pumpit.model.TrackerExercise;
import com.albert.fitness.pumpit.model.Training;
import com.albert.fitness.pumpit.model.Workout;
import com.albert.fitness.pumpit.model.WorkoutPlans;
import com.albert.fitness.pumpit.utils.FireBaseInit;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fitness.albert.com.pumpit.R;

public class TrainingActivity extends AppCompatActivity {

    private RecyclerView rvTraining;
    private TrainingAdapter trainingAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<TrackerExercise> trackerExerciseList;
    private List<Training> trainingList;
    private final String TAG = "TrainingActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        setTitle("Training");

        init();
        countExercise();
        getTrainingFromFb();
        itemTouchHelper();
    }


    private void init() {
        trackerExerciseList = new ArrayList<>();
        trainingList = new ArrayList<>();
        rvTraining = findViewById(R.id.rv_training);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom_add_exercise, menu);
        // MenuItem menuItem = menu.findItem(R.id.menu_edit_training);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_custom_add_exercise) {
            startActivity(new Intent(TrainingActivity.this, AddExerciseActivity.class));
            finish();
//            startActivity(new Intent(this, CustomAddExerciseActivity.class));
//            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getTrainingFromFb() {
        Log.d(TAG, "getTrainingFromFb: " + WorkoutActivity.workoutId);
        db.collection(Workout.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                .collection(Workout.WORKOUT_NAME).document(WorkoutActivity.workoutId)
                .collection(Workout.WORKOUT_DAY_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String workoutDayNameId = task.getResult().getDocuments().get(WorkoutAdapter.pos).getId();
                            Log.d(TAG, "onComplete workoutDayNameId: " + workoutDayNameId);
                            db.collection(Workout.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(Workout.WORKOUT_NAME)
                                    .document(WorkoutActivity.workoutId).collection(Workout.WORKOUT_DAY_NAME).document(workoutDayNameId)
                                    .collection(Workout.EXERCISE_NAME).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && task.getResult() != null) {
                                                for (int i = 0; i < task.getResult().size(); i++) {
                                                    Training training = task.getResult().getDocuments().get(i).toObject(Training.class);
                                                    TrackerExercise trackerExercise = task.getResult().getDocuments().get(i).toObject(TrackerExercise.class);
                                                    trackerExerciseList.add(trackerExercise);
                                                    trainingList.add(training);
                                                    initRecyclerView();
                                                }
                                                Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getDocuments());
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "get failed with ", e);
                                        }
                                    });
                        }
                    }
                });
    }

    private void countExercise() {
        final PrefsUtils prefsUtils = new PrefsUtils();

        prefsUtils.createSharedPreferencesFiles(this, "exercise");
        final String getPlanId = prefsUtils.getString("planName", "");

        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                .collection(Workout.WORKOUT_NAME).document(getPlanId)
                .collection(Workout.WORKOUT_DAY_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        Workout workout = task.getResult().getDocuments().get(i).toObject(Workout.class);
                        assert workout != null;
                        if (workout.getWorkoutDayName().equals(WorkoutAdapter.workoutDayName)) {

                            final String id = task.getResult().getDocuments().get(i).getId();

                            db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                                    .collection(Workout.WORKOUT_NAME).document(getPlanId).collection(Workout.WORKOUT_DAY_NAME).document(id)
                                    .collection(Workout.EXERCISE_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    int totalTimeTraining = 0;

                                    if (task.isSuccessful() && task.getResult() != null) {

                                        for (int i = 0; i < task.getResult().size(); i++) {
                                            int countRestAfterExercise = 0;
                                            int countRestBetweenSet = 0;
                                            int sizeOfRept = 0;
                                            int exerciseTime = 33;

                                            Training training = task.getResult().getDocuments().get(i).toObject(Training.class);
                                            assert training != null;
                                            countRestAfterExercise += training.getRestAfterExercise();
                                            countRestBetweenSet += training.getRestBetweenSet();
                                            sizeOfRept += training.getSizeOfRept();

                                            totalTimeTraining += (exerciseTime * sizeOfRept) + (countRestBetweenSet * sizeOfRept) + countRestAfterExercise;

                                            Log.d(TAG, "totalTimeTraining: " + totalTimeTraining + " countRestAfterExercise: "
                                                    + countRestAfterExercise + " countRestBetweenSet " + countRestBetweenSet + " sizeOfRept: " + sizeOfRept);
                                        }
                                        DocumentReference documentReference = db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                                                .collection(Workout.WORKOUT_NAME).document(getPlanId)
                                                .collection(Workout.WORKOUT_DAY_NAME).document(id);

                                        documentReference.update("lengthTraining", totalTimeTraining);
                                        documentReference.update("numOfExercise", task.getResult().size());
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Failed to get all exercise size: " + e);
                                }
                            });
                        }
                    }
                }
            }
        });
    }


    public void initRecyclerView() {

        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTraining.setLayoutManager(layoutManager);
        trainingAdapter = new TrainingAdapter(this, trainingList, trackerExerciseList);
        trainingAdapter.notifyDataSetChanged();
        rvTraining.setAdapter(trainingAdapter);
        Log.d(TAG, "initRecyclerView: init recyclerView" + rvTraining);
    }


    //method for move and del item
    private void itemTouchHelper() {

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
                ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(trainingList, from, to);
                trainingAdapter.notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                trainingList.remove(viewHolder.getAdapterPosition());
                trainingAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        helper.attachToRecyclerView(rvTraining);
        rvTraining.setNestedScrollingEnabled(false);
    }
}
