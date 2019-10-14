package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.new_adapter.WorkoutAdapter;
import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.Workout;
import com.albert.fitness.pumpit.model.WorkoutObj;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.utils.SwipeHelper;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;

public class WorkoutActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "WorkoutActivity";
    private CustomPlanViewModel customPlanViewModel;
    private List<Workout> workoutList;
    private TextView tvNameOfPlan, tvNameOfPlanSmall, tvActiveWorkout, tvChangePlan;
    private ImageView ivActivityPlan;
    private RecyclerView mRecyclerView;
    public static String workoutId;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        setTitle("Plan");
        initView();

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        int size = intent.getIntExtra("workoutSize", -1);
        String routine = intent.getStringExtra("routineName");
        String def = intent.getStringExtra("difficultyLevel");
        tvNameOfPlan.setText(routine);
        tvNameOfPlanSmall.setText(routine);


        //save into pref
        PrefsUtils prefsUtils = new PrefsUtils(WorkoutActivity.this, PrefsUtils.SETTINGS_PREFERENCES_FILE);
        prefsUtils.saveData("workout_plan_id", workoutId);
        prefsUtils.saveData("routine_name", routine);
        prefsUtils.saveData("difficulty_level", def);

        //check the default
        isActivatedPlan();

        customPlanViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);

        getWorkout(id, size);

        //  getPlanFormFb();
        //  initRecyclerView();
        // swipe();
        // setupSwipeMenu();
    }


    private void getWorkout(final int id, final int size) {
        if (id != -1 && size != -1) {
            customPlanViewModel.getWorkoutNAmeOfASelectedPlan(id).observe(this, new Observer<List<WorkoutObj>>() {
                @Override
                public void onChanged(List<WorkoutObj> workoutObjs) {
                    if (workoutObjs.isEmpty()) {
                        for (int i = 0; i <= size; i++) {
                            int num = i + 1;
                            WorkoutObj workoutObj = new WorkoutObj("", "Day " + num, "Workout " + num, id);
                            customPlanViewModel.addNewWorkout(workoutObj);
                        }
                    }
                    for (WorkoutObj workoutObj : workoutObjs) {
                        Log.d(TAG, "getWorkout: " + workoutObj.getWorkoutDayName());
                        initRecyclerView(workoutObjs);
                    }
                }
            });
        }
    }


    private void initView() {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_add, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.add_plans == item.getItemId()) {
            setEditLayout("");
        }
        return true;
    }


    private void isActivatedPlan() {
        PrefsUtils prefsUtils = new PrefsUtils(this, PrefsUtils.EXERCISE);
        boolean defaultExercise = prefsUtils.getBoolean(PrefsUtils.DEFAULT_EXERCISE, false);
        String routineName = prefsUtils.getString(PrefsUtils.DEFAULT_PLAN, "");
        int sizeOfWorkout = prefsUtils.getInt("sizeOfWorkoutPlan", -1);

        if (tvNameOfPlan.getText().toString().equals(routineName) && sizeOfWorkout == 1) {
            tvChangePlan.setVisibility(View.INVISIBLE);
            tvActiveWorkout.setVisibility(View.VISIBLE);
            tvNameOfPlanSmall.setVisibility(View.VISIBLE);
            tvNameOfPlan.setVisibility(View.INVISIBLE);
            ivActivityPlan.setVisibility(View.GONE);
        } else if (tvNameOfPlan.getText().toString().equals(routineName)) {
            tvChangePlan.setVisibility(View.VISIBLE);
            tvActiveWorkout.setVisibility(View.VISIBLE);
            tvNameOfPlanSmall.setVisibility(View.VISIBLE);
            tvNameOfPlan.setVisibility(View.INVISIBLE);
            ivActivityPlan.setVisibility(View.GONE);
        } else {
            tvChangePlan.setVisibility(View.INVISIBLE);
            tvActiveWorkout.setVisibility(View.INVISIBLE);
            tvNameOfPlanSmall.setVisibility(View.INVISIBLE);
            tvNameOfPlan.setVisibility(View.VISIBLE);
            ivActivityPlan.setVisibility(View.VISIBLE);
        }
    }


    private void saveDay(String workoutDayName, String workoutDay) {
        WorkoutObj workout = new WorkoutObj("00:00:00", workoutDayName, workoutDay, id);
        customPlanViewModel.addNewWorkout(workout);
        Log.d(TAG, "DocumentSnapshot successfully saved this day");
    }

    private String getWorkPlanId() {
        PrefsUtils prefsUtils = new PrefsUtils();
        prefsUtils.createSharedPreferencesFiles(this, PrefsUtils.EXERCISE);
        return prefsUtils.getString(PrefsUtils.PLAN_NAME, "null");
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

                //Check if the day existing
//                db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
//                        .collection(WorkoutPlans.WORKOUT_NAME)
//                        .document(workoutId).collection(Workout.WORKOUT_DAY_NAME)
//                        .whereEqualTo("workoutDay", pickWorkoutDay.getSelectedItem().toString()).get()
//                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                            @Override
//                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                Log.d(TAG, "onSuccess: " + queryDocumentSnapshots.isEmpty());
//
//                                if (queryDocumentSnapshots.isEmpty()) {
//                                    if (!isEditSelected[0]) {
//                                        saveDay(workoutName.getText().toString(), pickWorkoutDay.getSelectedItem().toString());
//                                    } else {
//                                        updateItem(workoutDayName, workoutName.getText().toString(), pickWorkoutDay.getSelectedItem().toString());
//                                        isEditSelected[0] = false;
//                                    }
//                                } else {
//                                    Log.d(TAG, "SetEditLayout day is existing");
//                                    onCreateDialog();
//                                }
//                            }
//                        });
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

    //CreateDialog if the day exists
    public void onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The day already exists Please select another day")
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


//    private void setupSwipeMenu() {
//        new RecyclerViewSwipeHelper(this, mRecyclerView, this);
//    }


//    @Override
//    public boolean showButton(int rowPosition, int buttonIndex) {
//        return true;
//    }
//
//    @Override
//    public int buttonWidth() {
//        return 0;
//    }


    //todo its not removed after deleted items

    private void swipe() {
        new SwipeHelper(this, mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        // R.color.colorAccent,
                        Color.parseColor("#d50000"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                deleteFromPref();

                                deleteItem(pos);
                                //workoutAdapter.notifyDataSetChanged();
                            }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        0,
                        //R.color.md_green_500,
                        Color.parseColor("#4caf50"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                setEditLayout(workoutList.get(pos).getWorkoutDayName());
                                Log.d(TAG, "WorkoutDayName: " + workoutList.get(pos).getWorkoutDayName());
                            }
                        }
                ));
            }
        };
    }


//    @Override
//    public void setupSwipeButtons(RecyclerView.ViewHolder viewHolder,
//                                  List<RecyclerViewSwipeHelper.SwipeButton> swipeButtons) {
//        //swipe delete
//        swipeButtons.add(new RecyclerViewSwipeHelper.SwipeButton(
//                getBaseContext(),
//                0,
//                0,
//                R.drawable.ic_delete,
//                R.dimen.ic_delete_size,
//                R.color.colorAccent,
//                new RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener() {
//                    @Override
//                    public void onClick(int pos) {
//                        deleteItem(pos);
//                        deleteFromFirebase(pos);
//                        workoutAdapter.notifyDataSetChanged();
//                    }
//                }
//        ));
//        //swipe edit
//        swipeButtons.add(new RecyclerViewSwipeHelper.SwipeButton(
//                getBaseContext(),
//                0,
//                0,
//                R.drawable.ic_edit_action,
//                R.dimen.ic_delete_size,
//                R.color.md_green_500,
//                new RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener() {
//                    @Override
//                    public void onClick(int pos) {
//                        setEditLayout(workoutList.get(pos).getWorkoutDayName());
//                        Log.d(TAG, "WorkoutDayName: " + workoutList.get(pos).getWorkoutDayName());
//                    }
//                }
//        ));
//    }


    private void deleteItem(final int position) {
        //   workoutAdapter = new WorkoutAdapter(this, workoutList);
        workoutList.remove(position);
        mRecyclerView.removeViewAt(position);
        // workoutAdapter.notifyItemRemoved(position);
        // workoutAdapter.notifyItemRangeChanged(position, workoutList.size());
    }


    private void deleteFromPref() {
        PrefsUtils prefsUtils = new PrefsUtils();
        prefsUtils.removeSingle(this, PrefsUtils.DEFAULT_EXERCISE, "e_" + Event.getDayName());
    }


    private void updateItem(final String workoutDayName, final String workoutName, final String workoutDay) {

        final List<Workout> newWorkoutList = new ArrayList<>();

        //get item id
//        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
//                .collection(WorkoutPlans.WORKOUT_NAME).document(getWorkPlanId())
//                .collection(Workout.WORKOUT_DAY_NAME).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            for (int i = 0; i < task.getResult().size(); i++) {
//
//                                Workout workout = task.getResult().getDocuments().get(i).toObject(Workout.class);
//                                newWorkoutList.add(workout);
//
//                                if (workoutDayName.equals(newWorkoutList.get(i).getWorkoutDayName())) {
//                                    String id = task.getResult().getDocuments().get(i).getId();
//
//                                    Log.d(TAG, "WorkoutName id: " + id);
//
//                                    //update item
//                                    DocumentReference workoutRef = db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
//                                            .collection(WorkoutPlans.WORKOUT_NAME).document(getWorkPlanId()).collection(Workout.WORKOUT_DAY_NAME).document(id);
//
//                                    workoutRef.update("workoutDayName", workoutName);
//                                    workoutRef.update("workoutDay", workoutDay);
//                                    Log.d(TAG, "WorkoutDayName: " + workoutName + " Successfully update");
//                                    workoutList.clear();
//                                } else {
//                                    Log.d(TAG, "Not found WorkoutName equal");
//                                }
//                            }
//                        }
//                    }
//                });
    }

    private void changeDefault() {
        PrefsUtils prefsUtils = new PrefsUtils(this, "exercise");
        prefsUtils.removeSingle(this, "exercise", "default_plan");
        prefsUtils.saveData("default_plan", tvNameOfPlan.getText().toString());
    }

    private void initRecyclerView(List<WorkoutObj> workoutList) {
        final String TAG = "WorkoutActivity";
        mRecyclerView = findViewById(R.id.rv_workout);
        Log.d(TAG, "initRecyclerView: initView workout mRecyclerView" + mRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        WorkoutAdapter workoutAdapter = new WorkoutAdapter();
        workoutAdapter.setItems((ArrayList<WorkoutObj>) workoutList);
        mRecyclerView.setAdapter(workoutAdapter);
        workoutAdapter.setListener(new WorkoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WorkoutObj item) {
                PrefsUtils prefsUtils = new PrefsUtils(WorkoutActivity.this, PrefsUtils.EXERCISE);
                prefsUtils.saveData("workoutId", item.getWorkoutId());
                startActivity(new Intent(WorkoutActivity.this, TrainingActivity.class));
                finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change_plan:
                startActivity(new Intent(WorkoutActivity.this, WorkoutChangePlansActivity.class));
                finish();
                break;
            case R.id.btn_set_as_activity_plan:
                changeDefault();
                finish();
                startActivity(getIntent());
                break;
        }
    }


}
