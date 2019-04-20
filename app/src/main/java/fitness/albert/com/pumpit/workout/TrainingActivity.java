package fitness.albert.com.pumpit.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fitness.albert.com.pumpit.Adapter.TrainingAdapter;
import fitness.albert.com.pumpit.Adapter.WorkoutAdapter;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.SavePref;
import fitness.albert.com.pumpit.Model.TrackerExercise;
import fitness.albert.com.pumpit.Model.Training;
import fitness.albert.com.pumpit.Model.Workout;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;

public class TrainingActivity extends AppCompatActivity {

    private ImageView btnAddExercise;
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

        init();
        onClick();

        getTrainingFromFb();

        countExercise();

       // itemTouchHelper();
    }

    private void init() {
        trackerExerciseList = new ArrayList<>();
        trainingList = new ArrayList<>();

        rvTraining = findViewById(R.id.rv_training);
        btnAddExercise = findViewById(R.id.btn_add_exercise);
    }

    private void onClick() {
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrainingActivity.this, AddExerciseActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
       // MenuItem menuItem = menu.findItem(R.id.menu_edit_training);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.menu_edit_training) {
            itemTouchHelper();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getTrainingFromFb() {


        db.collection(Workout.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(Workout.WORKOUT_NAME)
                .document(WorkoutActivity.workoutId).collection(Workout.WORKOUT_DAY_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null) {
                           String workoutDayNameId =  task.getResult().getDocuments().get(WorkoutAdapter.pos).getId();

                            db.collection(Workout.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(Workout.WORKOUT_NAME)
                                    .document(WorkoutActivity.workoutId).collection(Workout.WORKOUT_DAY_NAME).document(workoutDayNameId)
                                    .collection(Workout.EXERCISE_NAME).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && task.getResult() != null) {

                                                for(int i = 0 ; i < task.getResult().size(); i++) {
                                                    Training training = task.getResult().getDocuments().get(i).toObject(Training.class);
                                                    trainingList.add(training);
                                                    TrackerExercise trackerExercise = task.getResult().getDocuments().get(i).toObject(TrackerExercise.class);
                                                    trackerExerciseList.add(trackerExercise);

                                                    initRecyclerView(trainingList, trackerExerciseList);
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






//        db.collection(Workout.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(Workout.WORKOUT_NAME)
//                .document(WorkoutActivity.workoutId).collection(Workout.WORKOUT_DAY_NAME).document(WorkoutAdapter.workoutDayName)
//                .collection(Workout.EXERCISE_NAME).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful() && task.getResult() != null) {
//
//                            for(int i = 0 ; i < task.getResult().size(); i++) {
//                                Training training = task.getResult().getDocuments().get(i).toObject(Training.class);
//                                trainingList.add(training);
//                                TrackerExercise trackerExercise = task.getResult().getDocuments().get(i).toObject(TrackerExercise.class);
//                                trackerExerciseList.add(trackerExercise);
//
//                                initRecyclerView(trainingList, trackerExerciseList);
//                            }
//
//                            Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getDocuments());
//                        } else {
//                            Log.d(TAG, "No such document");
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "get failed with ", e);
//                    }
//                });
    }




    private void countExercise() {

        final SavePref savePref = new SavePref();

        savePref.createSharedPreferencesFiles(this, "exercise");
        String getPlanId = savePref.getString("planName", "");

        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(Workout.WORKOUT_NAME).document(getPlanId).collection(Workout.WORKOUT_DAY_NAME).
                document(WorkoutAdapter.workoutDayName).collection(Workout.EXERCISE_NAME).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(!queryDocumentSnapshots.isEmpty()) {
                            Log.d(TAG, "Exercise workout count: " + queryDocumentSnapshots.size());
                        }
                    }
                });
    }


    public void initRecyclerView(List<Training> trainingList, List<TrackerExercise> trackerList) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTraining.setLayoutManager(layoutManager);

        trainingAdapter = new TrainingAdapter(this, trainingList, trackerList);
        rvTraining.setAdapter(trainingAdapter);

        Log.d(TAG, "initRecyclerView: init recyclerView" + rvTraining);
    }


    //method for move and del item
    private void itemTouchHelper() {

        ItemTouchHelper helper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
                ItemTouchHelper.DOWN | ItemTouchHelper.UP,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
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


    @Override
    public void onBackPressed() {
        finish();
    }
}
