package fitness.albert.com.pumpit.workout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fitness.albert.com.pumpit.Adapter.TrainingAdapter;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.SavePref;
import fitness.albert.com.pumpit.Model.TrackerExercise;
import fitness.albert.com.pumpit.Model.Training;
import fitness.albert.com.pumpit.Model.Workout;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;

public class StartWorkoutActivity extends AppCompatActivity {

    private final String TAG = "StartWorkoutActivity";
    private RecyclerView mRecyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String workoutNameId;
    private TextView emptyWorkout;
    private ProgressDialog progressdialog;
    private ImageView btnStartWorkout;
    private List<TrackerExercise> trackerExerciseList;
    public static List<Training> trainingList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);

        init();

        getDayWorkout();

        setBtnStartWorkout();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        TextView todayExercises;
        todayExercises = findViewById(R.id.tv_today_exercise);
        mRecyclerView = findViewById(R.id.rv_start_workout);
        emptyWorkout = findViewById(R.id.tv_empty_workout);
        btnStartWorkout = findViewById(R.id.btn_start_workout);

        todayExercises.setText("Your exercises for today (" + getDayOfTheWeek() + ")");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_add, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_plans) {
            startActivity(new Intent(this, AddExerciseActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void getDayWorkout() {
        final List<WorkoutPlans> workoutPlansList = new ArrayList<>();
        final List<Workout> workoutList = new ArrayList<>();
        trainingList = new ArrayList<>();
        trackerExerciseList = new ArrayList<>();

        final String[] day = new String[1];
        final String[] part1 = new String[1];

        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        //get workout id
        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            for (int i = 0; i < task.getResult().size(); i++) {
                                WorkoutPlans workoutPlans = task.getResult().getDocuments().get(i).toObject(WorkoutPlans.class);
                                workoutPlansList.add(workoutPlans);


                                SavePref savePref = new SavePref();
                                savePref.createSharedPreferencesFiles(StartWorkoutActivity.this, "exercise");
                                String routineName = savePref.getString("default_plan", "");

                                Log.d(TAG, routineName);
                                Log.d(TAG, workoutPlansList.get(i).getRoutineName());

                                if (routineName.equals(workoutPlansList.get(i).getRoutineName())) {
                                    workoutNameId = task.getResult().getDocuments().get(i).getId();
                                }
                            }

                            Log.d(TAG, "Successfully get workout Name Id: " + workoutNameId);



                            //todo if the user logout.. its not working
                            db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                                    .collection(WorkoutPlans.WORKOUT_NAME).document(workoutNameId)
                                    .collection(Workout.WORKOUT_DAY_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful() && task.getResult() != null) {
                                        for (int i = 0; i < task.getResult().size(); i++) {

                                            Workout workout = task.getResult().getDocuments().get(i).toObject(Workout.class);
                                            workoutList.add(workout);

                                            String splitWorkoutDayName = workoutList.get(i).getWorkoutDay();
                                            String[] parts = splitWorkoutDayName.split(" ");
                                            part1[0] = parts[0];

                                            Date d = new Date();

                                            if (part1[0].equals("Day")) {
                                                day[0] = getDayOfTheWeekBeWorkout(d.getDay());

                                            } else {
                                                day[0] = getDayOfTheWeek();
                                            }


                                            if (workoutList.get(i).getWorkoutDay().equals(day[0])) {

                                                String workoutId = task.getResult().getDocuments().get(i).getId();

                                                Log.d(TAG, "Successfully get workout ID: " + workoutId);

                                                db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME)
                                                        .document(workoutNameId).collection(Workout.WORKOUT_DAY_NAME)
                                                        .document(workoutId).collection(Workout.EXERCISE_NAME).get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful() && task.getResult() != null) {

                                                                    for (int i = 0; i < task.getResult().size(); i++) {

                                                                        Training training = task.getResult().getDocuments().get(i).toObject(Training.class);
                                                                        TrackerExercise trackerExercise = task.getResult().getDocuments().get(i).toObject(TrackerExercise.class);
                                                                        trackerExerciseList.add(trackerExercise);
                                                                        trainingList.add(training);



                                                                        initRecyclerView(trainingList, trackerExerciseList);

                                                                        Log.d(TAG, "getWorkoutDay: " + workoutList.get(i).getWorkoutDay() + "\n" + "Exercise Size: " + trainingList.size());
                                                                    }
                                                                }
                                                                //if have exercise today
                                                                if (!trainingList.isEmpty()) {
                                                                    btnStartWorkout.setVisibility(View.VISIBLE);
                                                                    emptyWorkout.setVisibility(View.INVISIBLE);
                                                                } else {
                                                                    emptyWorkout.setVisibility(View.VISIBLE);
                                                                    btnStartWorkout.setVisibility(View.INVISIBLE);
                                                                }

                                                                Log.d(TAG, "successfully get Workout");

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d(TAG, "Failure: " + e);
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failure" + e);
                                }
                            });
                        } else {
                            Log.d(TAG, "ERROR to received Id");
                        }

                        progressdialog.hide();
                    }
                });
    }


    private void initRecyclerView(List<Training> trainingList, List<TrackerExercise> trackerList) {

        TrainingAdapter trainingAdapter;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        trainingAdapter = new TrainingAdapter(this, trainingList, trackerList);
        mRecyclerView.setAdapter(trainingAdapter);

        Log.d(TAG, "initRecyclerView: init recyclerView" + mRecyclerView);
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

    private String getDayOfTheWeekBeWorkout(int getDay) {
        switch (getDay) {
            case 0:
                return "Day 1";
            case 1:
                return "Day 2";
            case 2:
                return "Day 3";
            case 3:
                return "Day 4";
            case 4:
                return "Day 5";
            case 5:
                return "Day 6";
            case 6:
                return "Day 7";
        }
        return "";
    }

    private void setBtnStartWorkout() {
        btnStartWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartWorkoutActivity.this, WorkoutStartActivity.class));

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressdialog != null && progressdialog.isShowing()) {
            progressdialog.cancel();
        }
    }
}
