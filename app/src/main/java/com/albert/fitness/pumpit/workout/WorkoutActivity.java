package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.new_adapter.WorkoutAdapter;
import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.UserRegister;
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
    private List<WorkoutObj> workoutList;
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

        customPlanViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);

        //save into pref
        PrefsUtils prefsUtils = new PrefsUtils(WorkoutActivity.this, PrefsUtils.SETTINGS_PREFERENCES_FILE);
        prefsUtils.saveData("workout_plan_id", workoutId);
        prefsUtils.saveData("routine_name", routine);
        prefsUtils.saveData("difficulty_level", def);

        //check the default
        isActivatedPlan();

        getWorkout(id, size, workoutId);
        initRecyclerView();
        //setupSwipeMenu();
        swipe();

    }

    private void getWorkout(final int id, final int size , final String workoutId) {
        final PrefsUtils prefsUtils = new PrefsUtils(this, PrefsUtils.EXERCISE);
        final boolean isFirstTime = prefsUtils.getBoolean("first_time " + workoutId, true);
        if (id != -1 && size != -1) {
            customPlanViewModel.getWorkoutNAmeOfASelectedPlan(id).observe(this, workout -> {
                workoutList = workout;
                if (workout.isEmpty() && isFirstTime) {
                    for (int i = 0; i <= size; i++) {
                        int num = i + 1;
                        WorkoutObj workoutObj = new WorkoutObj( "Day " + num,
                                "Workout " + num, UserRegister.getTodayDate(), id);
                        customPlanViewModel.addNewWorkout(workoutObj);
                        prefsUtils.saveData("first_time " + workoutId, false);
                    }
                }
                initRecyclerView();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_add, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.add_plans == item.getItemId()) {
            setEditLayout(-1);
        }
        return true;
    }


    private void isActivatedPlan() {
        PrefsUtils prefsUtils = new PrefsUtils(this, PrefsUtils.EXERCISE);
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
        WorkoutObj workout = new WorkoutObj(workoutDayName, workoutDay, UserRegister.getTodayDate(), id);
        customPlanViewModel.addNewWorkout(workout);
        Log.d(TAG, "DocumentSnapshot successfully saved this day");
    }

    private void setEditLayout(final int i) {
        final WorkoutObj[] workout = new WorkoutObj[1];

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.add_workout_days, null);

        dialogBuilder.setView(dialogView);

        final EditText workoutName = dialogView.findViewById(R.id.et_edit_workout_day_name);
        final Spinner pickWorkoutDay = dialogView.findViewById(R.id.sp_pick_a_workout_day);
        final ImageView save = dialogView.findViewById(R.id.btn_ok_workout_day);
        final ImageView cancel = dialogView.findViewById(R.id.iv_exit_custom_plan);

        final AlertDialog dialog = dialogBuilder.create();

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


        if (i != -1) {
            customPlanViewModel.getWorkoutById(workoutList.get(i).getWorkoutId()).observe(this, workoutObj -> {
                workout[0] = workoutObj;
                workoutName.setText(workoutObj.getWorkoutDayName());
            });
        }

        save.setOnClickListener(v -> {
            if (workoutName.getText().toString().isEmpty()) {
                workoutName.setError("Please enter Workout Day Name");
            } else if (i == -1) {
                saveDay(workoutName.getText().toString(), pickWorkoutDay.getSelectedItem().toString());
            } else {
                updateItemIfExisting(workoutName.getText().toString(), pickWorkoutDay.getSelectedItem().toString(), workout[0]);
            }
            //Check if the day existing


            dialog.dismiss();
        });
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    //CreateDialog if the day exists
    public void onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The day already exists Please select another day")
                .setCancelable(false)
                .setNegativeButton("OK", (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void swipe() {
        new SwipeHelper(this, mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        // R.color.colorAccent,
                        Color.parseColor("#d50000"),
                        pos -> {
                            customPlanViewModel.deleteWorkout(workoutList.get(pos));
                            deleteFromPref();
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        0,
                        //R.color.md_green_500,
                        Color.parseColor("#4caf50"),
                        pos -> {
                            setEditLayout(pos);
                            Log.d(TAG, "WorkoutDayName: " + workoutList.get(pos).getWorkoutDayName());
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


    private void deleteFromPref() {
        PrefsUtils prefsUtils = new PrefsUtils();
        prefsUtils.removeSingle(this, PrefsUtils.DEFAULT_EXERCISE, "e_" + Event.getDayName());
    }


    private void updateItemIfExisting(final String workoutName, final String workoutDay, WorkoutObj workout) {
        workout.setWorkoutDayName(workoutName);
        workout.setWorkoutDay(workoutDay);
        customPlanViewModel.updateWorkout(workout);

        Log.d(TAG, "WorkoutDayName: " + workoutName + " Successfully update");
        workoutList.clear();
    }

    private void changeDefault() {
        PrefsUtils prefsUtils = new PrefsUtils(this, PrefsUtils.EXERCISE);
        prefsUtils.removeSingle(this, PrefsUtils.EXERCISE, "default_plan");
        prefsUtils.saveData("default_plan", tvNameOfPlan.getText().toString());
        prefsUtils.saveData("default_plan_id",id);
    }

    private void initRecyclerView() {
        final String TAG = "WorkoutActivity";
        mRecyclerView = findViewById(R.id.rv_workout);
        Log.d(TAG, "initRecyclerView: initView workout mRecyclerView" + mRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        WorkoutAdapter workoutAdapter = new WorkoutAdapter(this);
        workoutAdapter.setItems((ArrayList<WorkoutObj>) workoutList);
        mRecyclerView.setAdapter(workoutAdapter);
        workoutAdapter.setListener(item -> {
            PrefsUtils prefsUtils = new PrefsUtils(WorkoutActivity.this, PrefsUtils.EXERCISE);
            prefsUtils.saveData("workoutId", item.getWorkoutId());
            startActivity(new Intent(WorkoutActivity.this, TrainingActivity.class));
            finish();
        });
    }
}
