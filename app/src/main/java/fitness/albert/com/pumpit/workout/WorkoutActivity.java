package fitness.albert.com.pumpit.workout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.Adapter.WorkoutAdapter;
import fitness.albert.com.pumpit.Adapter.WorkoutPlanAdapter;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.SavePref;
import fitness.albert.com.pumpit.Model.Training;
import fitness.albert.com.pumpit.Model.UserRegister;
import fitness.albert.com.pumpit.Model.Workout;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;
import it.shadowsheep.recyclerviewswipehelper.RecyclerViewSwipeHelper;

public class WorkoutActivity extends AppCompatActivity
        implements RecyclerViewSwipeHelper.RecyclerViewSwipeHelperDelegate, View.OnClickListener {

    private final String TAG = "WorkoutActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Workout> workoutList;
    private TextView tvNameOfPlan, tvNameOfPlanSmall, tvActiveWorkout, tvChangePlan;
    private ImageView ivActivityPlan;
    /**
     *
     */
    private RecyclerView view;
    private WorkoutAdapter workoutAdapter;
    public static String workoutId;
    private SavePref savePref = new SavePref();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        init();

        getPlanFormFb();

        initRecyclerView();

        setupSwipeMenu();
    }

    private void init() {
        workoutList = new ArrayList<>();
        tvNameOfPlan = findViewById(R.id.tv_name_of_plan);
        tvNameOfPlanSmall = findViewById(R.id.tv_name_of_plan_s);
        tvActiveWorkout = findViewById(R.id.tv_active_workout);
        tvChangePlan = findViewById(R.id.tv_change_plan);
        ivActivityPlan = findViewById(R.id.btn_set_as_activity_plan);

        tvChangePlan.setOnClickListener(this);
        ivActivityPlan.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_add, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.add_plans == item.getItemId()) {
            setEditLayout("");
        }
        return true;
    }


    private void isActivatedPlan() {

        savePref.createSharedPreferencesFiles(this, SavePref.EXERCISE);
        boolean defaultExercise = savePref.getBoolean(SavePref.DEFAULT_EXERCISE, false);

        String routineName = savePref.getString(SavePref.DEFAULT_PLAN, "");

        if (defaultExercise && tvNameOfPlan.getText().toString().equals(routineName)) {
            tvChangePlan.setVisibility(View.VISIBLE);
            tvActiveWorkout.setVisibility(View.VISIBLE);
            tvNameOfPlanSmall.setVisibility(View.VISIBLE);
            tvNameOfPlan.setVisibility(View.INVISIBLE);
            ivActivityPlan.setVisibility(View.INVISIBLE);
        } else {
            tvChangePlan.setVisibility(View.INVISIBLE);
            tvActiveWorkout.setVisibility(View.INVISIBLE);
            tvNameOfPlanSmall.setVisibility(View.INVISIBLE);
            tvNameOfPlan.setVisibility(View.VISIBLE);
            ivActivityPlan.setVisibility(View.VISIBLE);
        }
    }


    private void getPlanFormFb() {

        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        //get workout id
        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            workoutId = task.getResult().getDocuments().get(WorkoutPlanAdapter.posit).getId();

                            //set plan name
                            WorkoutPlans workoutPlans = task.getResult().getDocuments().get(WorkoutPlanAdapter.posit).toObject(WorkoutPlans.class);
                            assert workoutPlans != null;
                            tvNameOfPlan.setText(workoutPlans.getRoutineName());
                            tvNameOfPlanSmall.setText(workoutPlans.getRoutineName());

                            //check the default
                            isActivatedPlan();

                            db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME)
                                    .document(workoutId).collection(Workout.WORKOUT_DAY_NAME).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && task.getResult() != null) {

                                                for (int i = 0; i < task.getResult().size(); i++) {
                                                    Workout workout = task.getResult().getDocuments().get(i).toObject(Workout.class);
                                                    workoutList.add(workout);

                                                    initRecyclerView();

                                                    Log.d(TAG, "Workout - DocumentSnapshot written with ID: " + task.getResult().getDocuments().get(i).getData());
                                                }
                                                progressdialog.hide();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                            Log.d(TAG, "Successful id: " + task.getResult().getDocuments().get(WorkoutPlanAdapter.posit).getId());

                        } else {
                            Log.d(TAG, "ERROR to received Id");
                        }
                    }
                });
    }


    private void saveDay(String workoutDayName, String workoutDay) {
        Workout workout = new Workout(UserRegister.getTodayData(), workoutDayName, workoutDay, 0, 0);
        if (!getWorkPlanId().equals("null")) {
            db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                    .collection(WorkoutPlans.WORKOUT_NAME).document(getWorkPlanId()).collection(Workout.WORKOUT_DAY_NAME).add(workout)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            workoutList.clear();
                            getPlanFormFb();
                            Log.d(TAG, "DocumentSnapshot successfully saved this day");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }


    private String getWorkPlanId() {
        SavePref savePref = new SavePref();
        savePref.createSharedPreferencesFiles(this, SavePref.EXERCISE);
        return savePref.getString(SavePref.PLAN_NAME, "null");
    }


    private void setEditLayout(final String workoutDayName) {

        final boolean[] isEditSelected = new boolean[1];

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.add_workout_days, null);

        dialogBuilder.setView(dialogView);

        final EditText workoutName = dialogView.findViewById(R.id.et_edit_workout_day_name);
        final Spinner pickWorkoutDay = dialogView.findViewById(R.id.sp_pick_a_workout_day);
        final ImageView save = dialogView.findViewById(R.id.btn_ok_workout_day);
        final ImageView cancel = dialogView.findViewById(R.id.iv_exit_custom_plan);

        final AlertDialog dialog = dialogBuilder.create();

        if (!workoutDayName.isEmpty()) {
            workoutName.setText(workoutDayName);
            isEditSelected[0] = true;
        }

        //set spinner
        List<String> workoutDayList = new ArrayList<>();
        workoutDayList.add("Monday");
        workoutDayList.add("Tuesday");
        workoutDayList.add("Wednesday");
        workoutDayList.add("Thursday");
        workoutDayList.add("Friday");
        workoutDayList.add("Saturday");
        workoutDayList.add("Sunday");
        workoutDayList.add("No Specific Day");

        ArrayAdapter<String> daysWeekAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, workoutDayList);
        daysWeekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickWorkoutDay.setAdapter(daysWeekAdapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (workoutName.getText().toString().isEmpty()) {
                    workoutName.setError("Please enter Workout Day Name");
                }
                if (!isEditSelected[0]) {
                    saveDay(workoutName.getText().toString(), pickWorkoutDay.getSelectedItem().toString());
                } else {
                    updateItem(workoutDayName, workoutName.getText().toString(), pickWorkoutDay.getSelectedItem().toString());
                    isEditSelected[0] = false;
                }
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void setupSwipeMenu() {
        new RecyclerViewSwipeHelper(this, view, this);
    }


    @Override
    public boolean showButton(int rowPosition, int buttonIndex) {
        return true;
    }

    @Override
    public int buttonWidth() {
        return 0;
    }


    //todo its not removed after deleted items
    @Override
    public void setupSwipeButtons(RecyclerView.ViewHolder viewHolder,
                                  List<RecyclerViewSwipeHelper.SwipeButton> swipeButtons) {
        //swipe delete
        swipeButtons.add(new RecyclerViewSwipeHelper.SwipeButton(
                getBaseContext(),
                0,
                0,
                R.drawable.ic_delete,
                R.dimen.ic_delete_size,
                R.color.colorAccent,
                new RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        deleteFromFirebase(pos);
                        deleteItem(pos);
                        initRecyclerView();
                    }
                }
        ));
        //swipe edit
        swipeButtons.add(new RecyclerViewSwipeHelper.SwipeButton(
                getBaseContext(),
                0,
                0,
                R.drawable.ic_edit_action,
                R.dimen.ic_delete_size,
                R.color.md_green_500,
                new RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        setEditLayout(workoutList.get(pos).getWorkoutDayName());
                        Log.d(TAG, "WorkoutDayName: " + workoutList.get(pos).getWorkoutDayName());
                    }
                }
        ));
    }


    private void deleteItem(final int position) {
        workoutAdapter = new WorkoutAdapter(this, workoutList);
        workoutList.remove(position);
        view.removeViewAt(position);
        workoutAdapter.notifyItemRemoved(position);
        workoutAdapter.notifyItemRangeChanged(position, workoutList.size());
    }


    private void deleteFromFirebase(final int position) {
        final List<Training> trainingList = new ArrayList<>();

        //find exercise id
        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                .collection(WorkoutPlans.WORKOUT_NAME).document(getWorkPlanId()).collection(Workout.WORKOUT_DAY_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            final String id = task.getResult().getDocuments().get(position).getId();

                            Log.d(TAG, "Workout day name id: " + id);

                            //deleted the workout days
                            db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                                    .collection(WorkoutPlans.WORKOUT_NAME).document(getWorkPlanId()).collection(Workout.WORKOUT_DAY_NAME)
                                    .document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");

                                    //find exercise in workout day name
                                    db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                                            .collection(WorkoutPlans.WORKOUT_NAME).document(getWorkPlanId()).collection(Workout.WORKOUT_DAY_NAME)
                                            .document(id).collection(Workout.EXERCISE_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && task.getResult() != null) {
                                                for (int i = 0; i < task.getResult().size(); i++) {
                                                    Training training = task.getResult().getDocuments().get(i).toObject(Training.class);
                                                    trainingList.add(training);
                                                }
                                            }
                                            //delete exercise from workout days
                                            for (int i = 0; i < trainingList.size(); i++) {
                                                db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                                                        .collection(WorkoutPlans.WORKOUT_NAME).document(getWorkPlanId()).collection(Workout.WORKOUT_DAY_NAME)
                                                        .document(id).collection(Workout.EXERCISE_NAME)
                                                        .document(trainingList.get(i).getExerciseName())
                                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "Exercise successfully deleted!");
                                                    }
                                                });
                                            }
                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e(TAG, "Failure: " + e);
                                                }
                                            });
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "isFailure: " + e);
                                        }
                                    });
                        }
                    }
                });
    }


    private void updateItem(final String workoutDayName, final String workoutName, final String workoutDay) {

        final List<Workout> newWorkoutList = new ArrayList<>();

        //get item id
        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                .collection(WorkoutPlans.WORKOUT_NAME).document(getWorkPlanId()).collection(Workout.WORKOUT_DAY_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (int i = 0; i < task.getResult().size(); i++) {

                                Workout workout = task.getResult().getDocuments().get(i).toObject(Workout.class);
                                newWorkoutList.add(workout);

                                if (workoutDayName.equals(newWorkoutList.get(i).getWorkoutDayName())) {
                                    String id = task.getResult().getDocuments().get(i).getId();

                                    Log.d(TAG, "WorkoutName id: " + id);

                                    //update item
                                    DocumentReference workoutRef = db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                                            .collection(WorkoutPlans.WORKOUT_NAME).document(getWorkPlanId()).collection(Workout.WORKOUT_DAY_NAME).document(id);

                                    workoutRef.update("workoutDayName", workoutName);
                                    workoutRef.update("workoutDay", workoutDay);

                                    Log.d(TAG, "WorkoutDayName: " + workoutName + " Successfully update");

                                    workoutList.clear();

                                    getPlanFormFb();
                                } else {
                                    Log.d(TAG, "Not found WorkoutName equal");
                                }
                            }
                        }
                    }
                });
    }

    private void changeDefault() {
        savePref.createSharedPreferencesFiles(this, "exercise");
        savePref.removeSingle(this,"exercise","default_plan");
        savePref.saveData("default_plan", tvNameOfPlan.getText().toString());

    }


    private void initRecyclerView() {

        final String TAG = "WorkoutActivity";

        view = findViewById(R.id.rv_workout);

        Log.d(TAG, "initRecyclerView: init workout recyclerView" + view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view.setLayoutManager(layoutManager);

        workoutAdapter = new WorkoutAdapter(this, workoutList);
        view.setAdapter(workoutAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change_plan:
                startActivity(new Intent(WorkoutActivity.this, WorkoutPlansActivity.class));
                finish();
                break;
            case R.id.btn_set_as_activity_plan:
                changeDefault();
                finish();
                startActivity(getIntent());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
