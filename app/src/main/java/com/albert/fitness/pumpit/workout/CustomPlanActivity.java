package com.albert.fitness.pumpit.workout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.albert.fitness.pumpit.model.UserRegister;
import com.albert.fitness.pumpit.model.WorkoutPlanObj;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fitness.albert.com.pumpit.R;

public class CustomPlanActivity extends AppCompatActivity {

    private CustomPlanViewModel customPlanViewModel;
    private Spinner spDaysWeek, spDifficultyLevel, spDayType, spRoutineType;
    private EditText etRoutineDescription, etRoutineName;
    private ImageView btnCreateWorkout;
    //private String workoutNameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_plan);
        Objects.requireNonNull(getSupportActionBar()).hide();

        customPlanViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);

        init();
        addItemsIntoSpinner();
        onClick();
    }

    private void init() {
        spDaysWeek = findViewById(R.id.spinner_days_week);
        spDifficultyLevel = findViewById(R.id.spinner_difficulty_level);
        spDayType = findViewById(R.id.spinner_day_type);
        etRoutineDescription = findViewById(R.id.et_routine_description);
        etRoutineName = findViewById(R.id.et_routine_name);
        btnCreateWorkout = findViewById(R.id.btn_create_workout);
        spRoutineType = findViewById(R.id.sp_routine_type);
    }

    public void addItemsIntoSpinner() {

        List<String> daysWeekList = new ArrayList<>();
        List<String> difficultyLevelList = new ArrayList<>();
        List<String> dayTypeList = new ArrayList<>();
        List<String> routineTypeList = new ArrayList<>();

        daysWeekList.add("1 day / week");
        daysWeekList.add("2 day / week");
        daysWeekList.add("3 day / week");
        daysWeekList.add("4 day / week");
        daysWeekList.add("5 day / week");
        daysWeekList.add("6 day / week");
        daysWeekList.add("7 day / week");

        difficultyLevelList.add("Beginner");
        difficultyLevelList.add("Intermediate");
        difficultyLevelList.add("Advanced");

        dayTypeList.add("Day of Week (eg. Monday-Friday)");
        dayTypeList.add("Numerical (eg. Day 1, Day 2)");

        routineTypeList.add("General Fitness");
        routineTypeList.add("Bulking");
        routineTypeList.add("Cutting");
        routineTypeList.add("Sport Specific");


        addIntoSpinner(daysWeekList, spDaysWeek);
        addIntoSpinner(difficultyLevelList, spDifficultyLevel);
        addIntoSpinner(dayTypeList, spDayType);
        addIntoSpinner(routineTypeList, spRoutineType);
    }


    private void addIntoSpinner(List<String> list, Spinner spinner) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }


    private void saveData() {
        String daysWeek = null;
        String difficultyLevel = null;
        String dayType = null;
        String routineType = null;
        String routineDescription;
        final String routineName;

        if (spDaysWeek.getSelectedItem() != null && spDifficultyLevel.getSelectedItem() != null && spDayType.getSelectedItem() != null && spRoutineType != null) {
            daysWeek = spDaysWeek.getSelectedItem().toString();
            difficultyLevel = spDifficultyLevel.getSelectedItem().toString();
            routineType = spRoutineType.getSelectedItem().toString();

            String TAG = "CustomPlanActivity";
            Log.d(TAG, "Get spinner index" + spDaysWeek.getSelectedItemPosition());

            dayType = spDayType.getSelectedItem().toString();
        }

        final int dayWeekPosition = spDaysWeek.getSelectedItemPosition();
        routineDescription = etRoutineDescription.getText().toString();
        routineName = etRoutineName.getText().toString();

        //check if have default exercise
        PrefsUtils prefsUtils = new PrefsUtils(this, "exercise");
        prefsUtils.saveData("default_plan", routineName);

        WorkoutPlanObj workoutPlan = new WorkoutPlanObj(routineName, daysWeek
                , difficultyLevel, routineType, dayType, routineDescription, UserRegister.getTodayDate(), dayWeekPosition);


        customPlanViewModel.AddNewPlan(workoutPlan);

       // receivePlanId(dayWeekPosition);

    }


//    private void receivePlanId(final int position) {
//        customPlanViewModel.getAllPlan().observe(this, new Observer<List<WorkoutPlanObj>>() {
//            @Override
//            public void onChanged(List<WorkoutPlanObj> workoutPlanObjs) {
//                int planId;
//                if(workoutPlanObjs.isEmpty()) {
//                    planId = 0;
//                } else {
//                    planId = workoutPlanObjs.get(workoutPlanObjs.size()-1).getPlanId() +1;
//                }
//                for (int i = 0; i <= position; i++) {
//                    int num = i+1;
//                    WorkoutObj workoutObj = new WorkoutObj("", "Day " + num, "Workout " + num, planId);
//                    customPlanViewModel.addNewWorkout(workoutObj);
//                }
//            }
//        });
//    }

    private void onClick() {
        btnCreateWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
               // startActivity(new Intent(CustomPlanActivity.this, PlanFragment.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
