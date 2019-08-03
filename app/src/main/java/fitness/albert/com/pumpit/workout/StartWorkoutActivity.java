package fitness.albert.com.pumpit.workout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.adapter.TrainingAdapter;
import fitness.albert.com.pumpit.helper.SimpleItemTouchHelperCallback;
import fitness.albert.com.pumpit.model.FireBaseInit;
import fitness.albert.com.pumpit.model.PrefsUtils;
import fitness.albert.com.pumpit.model.SwipeHelper;
import fitness.albert.com.pumpit.model.TrackerExercise;
import fitness.albert.com.pumpit.model.Training;
import fitness.albert.com.pumpit.model.Workout;
import fitness.albert.com.pumpit.model.WorkoutPlans;

public class StartWorkoutActivity extends AppCompatActivity {

    private final String TAG = "StartWorkoutActivity";
    private RecyclerView mRecyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String workoutNameId;
    private String workoutId;
    private TextView emptyWorkout;
    private ProgressDialog progressdialog;
    private ImageView btnStartWorkout;
    private List<TrackerExercise> trackerExerciseList = new ArrayList<>();
    public static List<Training> trainingList = new ArrayList<>();
    private TrainingAdapter trainingAdapter;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);

        init();
        getDayWorkout();
        setBtnStartWorkout();
        swipe();
    }


    @SuppressLint("SetTextI18n")
    private void init() {
        TextView todayExercises;
        todayExercises = findViewById(R.id.tv_today_exercise);
        mRecyclerView = findViewById(R.id.rv_start_workout);
        emptyWorkout = findViewById(R.id.tv_empty_workout);
        btnStartWorkout = findViewById(R.id.btn_start_workout);
        setTitle("Your exercises for today (" + getDayOfTheWeek() + ")");

        todayExercises.setText("Your exercises for today (" + getDayOfTheWeek() + ")");
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


    private void getDayWorkout() {
        final List<WorkoutPlans> workoutPlansList = new ArrayList<>();
        final List<Workout> workoutList = new ArrayList<>();
        final String[] day = new String[1];
        final String[] part1 = new String[1];

        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        trainingList.clear();

        //get workout id
        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                .collection(WorkoutPlans.WORKOUT_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            for (int i = 0; i < task.getResult().size(); i++) {
                                WorkoutPlans workoutPlans = task.getResult().getDocuments().get(i).toObject(WorkoutPlans.class);
                                workoutPlansList.add(workoutPlans);

                                PrefsUtils prefsUtils = new PrefsUtils();
                                prefsUtils.createSharedPreferencesFiles(StartWorkoutActivity.this, "exercise");
                                String routineName = prefsUtils.getString("default_plan", "");

                                Log.d(TAG, "routineName: " + routineName + workoutPlansList.get(i).getRoutineName());

                                if (routineName.equals(workoutPlansList.get(i).getRoutineName())) {
                                    workoutNameId = task.getResult().getDocuments().get(i).getId();
                                }
                            }

                            if (workoutNameId == null) {
                                startActivity(new Intent(StartWorkoutActivity.this, ChangePlanActivity.class));
                                finish();
                            } else {
                                Log.d(TAG, "Successfully get workout name id: " + workoutNameId);

                                db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                                        .collection(WorkoutPlans.WORKOUT_NAME).document(workoutNameId)
                                        .collection(Workout.WORKOUT_DAY_NAME).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                                                            workoutId = task.getResult().getDocuments().get(i).getId();

                                                            Log.d(TAG, "Successfully get workout ID: " + workoutId);

                                                            db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                                                                    .collection(WorkoutPlans.WORKOUT_NAME).document(workoutNameId)
                                                                    .collection(Workout.WORKOUT_DAY_NAME).document(workoutId)
                                                                    .collection(Workout.EXERCISE_NAME).get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful() && task.getResult() != null) {
                                                                                boolean trainingListIsEmpty;

                                                                                for (int i = 0; i < task.getResult().size(); i++) {
                                                                                    Training training = task.getResult().getDocuments().get(i).toObject(Training.class);
                                                                                    TrackerExercise trackerExercise = task.getResult().getDocuments().get(i).toObject(TrackerExercise.class);
                                                                                    trackerExerciseList.add(trackerExercise);
                                                                                    trainingList.add(training);
                                                                                    initRecyclerView();
                                                                                }

                                                                                trainingListIsEmpty = trainingList.size() > 0;
                                                                                Log.d(TAG, "Exercise Size: " + trainingList.size());
                                                                                btnStartWorkout.setVisibility(View.VISIBLE);
                                                                                emptyWorkout.setVisibility(View.INVISIBLE);

                                                                                if (!trainingListIsEmpty) {
                                                                                    emptyWorkout.setVisibility(View.VISIBLE);
                                                                                    btnStartWorkout.setVisibility(View.INVISIBLE);
                                                                                }
                                                                            }
                                                                            Log.d(TAG, "Successfully get workout");
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.d(TAG, "Failure get workout: " + e);
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
                            }
                        } else {
                            Log.d(TAG, "ERROR to received Id");
                        }
                        progressdialog.hide();
                    }
                });
    }


    private void swipe() {
        new SwipeHelper(this, mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(final RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#d50000"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onClick(int pos) {
                                deleteItemFromFb(pos);
                                updateNumberOfExercise();
                                trainingAdapter.notifyDataSetChanged();
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
                                addLayoutRepsAndSets(pos);
                            }
                        }
                ));
            }


        };
    }

    // TODO: 2019-05-10 check if exercise have on fb befor add

    private void deleteItem(final int position) {
        // trainingAdapter = new TrainingAdapter(this, trainingList, trackerExerciseList);
        trainingList.remove(position);
        mRecyclerView.removeViewAt(position);
        trainingAdapter.notifyItemRemoved(position);
        trainingAdapter.notifyItemRangeChanged(position, trainingList.size());
    }

    private void deleteItemFromFb(final int position) {
        db.collection(WorkoutPlans.WORKOUT_PLANS)
                .document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME)
                .document(workoutNameId).collection(Workout.WORKOUT_DAY_NAME)
                .document(workoutId).collection(Workout.EXERCISE_NAME).document(trainingList.get(position).getExerciseName()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        deleteItem(position);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error deleting document", e);
            }
        });
    }


    private void updateNumberOfExercise() {
        db.collection(WorkoutPlans.WORKOUT_PLANS)
                .document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME)
                .document(workoutNameId).collection(Workout.WORKOUT_DAY_NAME)
                .document(workoutId).update("numOfExercise", trainingList.size());
    }


    private void addLayoutRepsAndSets(int position) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.dialog_recive_reps_and_sets, null);

        ImageView btnDoneSetsReps = dialogView.findViewById(R.id.btn_done_edit_sets_reps);
        ImageView btnCancel = dialogView.findViewById(R.id.iv_cancel_weight_reps);

        for (int i = 0; i < trainingList.get(position).getSizeOfRept(); i++) {
            //add weight and set into layout
            addWeightAndSetIntoLayout(dialogView, position, i);
        }

        btnDoneSetsReps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWeightAndRepsIntoFb();
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

    private void addWeightAndSetIntoLayout(View view, int position, int i) {

        LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View rowView = inf.inflate(R.layout.layout_weight_and_reps, null);
        final LinearLayout linearLayout = view.findViewById(R.id.linear_set_reps);
        linearLayout.addView(rowView);

        EditText etWeight = view.findViewById(R.id.et_weight);
        EditText etReps = view.findViewById(R.id.et_reps);

        etWeight.setText(String.valueOf(trainingList.get(position).getTrackerExercises().get(i).getWeight()));
        etReps.setText(String.valueOf(trainingList.get(position).getTrackerExercises().get(i).getRepNumber()));

//            DocumentReference reference = db.collection(WorkoutPlans.WORKOUT_PLANS)
//                    .document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME)
//                    .document(workoutNameId).collection(Workout.WORKOUT_DAY_NAME)
//                    .document(workoutId).collection(Workout.EXERCISE_NAME).document(trainingList.get(position).getExerciseName());
//
//            reference.update("repNumber", etReps.getText().toString());
//            reference.update("weight", etWeight.getText().toString());
    }


    private void setWeightAndRepsIntoFb() {

    }


    private void initRecyclerView() {
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        trainingAdapter = new TrainingAdapter(this, trainingList, trackerExerciseList);
        trainingAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(trainingAdapter);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(trainingAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        Log.d(TAG, "initRecyclerView: init recyclerView " + mRecyclerView);
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
