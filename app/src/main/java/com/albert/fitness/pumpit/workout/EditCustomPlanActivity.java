package com.albert.fitness.pumpit.workout;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.albert.fitness.pumpit.model.WorkoutPlanObj;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;

public class EditCustomPlanActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "EditCustomPlanActivity";
    private EditText etRoutineName, etRoutineDescription;
    private ImageView ivDifficulty1, ivDifficulty2, ivDifficulty3, ivGeneralFitness, ivBulking, ivCutting, ivSportSpecific;
    private Spinner spDayType;
    private String type;
    private String difficultyLevel;
    private CustomPlanViewModel planViewModel;
    private WorkoutPlanObj workoutPlanObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_custom_plan);
        setTitle("Custom Plan");
        init();
        setSpinnerDayType();
        getData();
    }

    private void init() {
        etRoutineName = findViewById(R.id.et_edit_routine_name);
        etRoutineDescription = findViewById(R.id.et_edite_routine_description);
        ivDifficulty1 = findViewById(R.id.iv_difficulty_1);
        ivDifficulty2 = findViewById(R.id.iv_difficulty_2);
        ivDifficulty3 = findViewById(R.id.iv_difficulty_3);
        ivGeneralFitness = findViewById(R.id.iv_general_fitness);
        ivBulking = findViewById(R.id.iv_bulking);
        ivCutting = findViewById(R.id.iv_cutting);
        ivSportSpecific = findViewById(R.id.iv_sport_specific);
        spDayType = findViewById(R.id.sp_edit_day_type);

        ivDifficulty1.setOnClickListener(this);
        ivDifficulty2.setOnClickListener(this);
        ivDifficulty3.setOnClickListener(this);
        ivGeneralFitness.setOnClickListener(this);
        ivBulking.setOnClickListener(this);
        ivCutting.setOnClickListener(this);
        ivSportSpecific.setOnClickListener(this);
    }


    private void setSpinnerDayType() {
        List<String> dayTypeList = new ArrayList<>();
        dayTypeList.add("Day of Week");
        dayTypeList.add("Numerical");

        ArrayAdapter<String> daysTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dayTypeList);
        daysTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDayType.setAdapter(daysTypeAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_custom_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_ok_plan) {
            updateDataFb();
            Log.d(TAG, "Save Successfully");
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        planViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int planId = extras.getInt("planId");
            planViewModel.getPlan(planId).observe(this, new Observer<WorkoutPlanObj>() {
                @Override
                public void onChanged(WorkoutPlanObj workoutPlan) {
                    Log.i(TAG, "getData: " + workoutPlan.getRoutineName());
                    workoutPlanObj = workoutPlan;

                    etRoutineName.setText(workoutPlan.getRoutineName());
                    etRoutineDescription.setText(workoutPlan.getRoutineDescription());
                    getDifficultyLevel(workoutPlan.getDifficultyLevel());
                    getType(workoutPlan.getRoutineType());
                }
            });
        }
    }


    private void updateDataFb() {
        workoutPlanObj.setRoutineName(etRoutineName.getText().toString());
        workoutPlanObj.setRoutineDescription(etRoutineDescription.getText().toString());
        if (type != null) {
            workoutPlanObj.setRoutineType(type);
        }
        if (difficultyLevel != null) {
            workoutPlanObj.setDifficultyLevel(difficultyLevel);
        }
        planViewModel.updateWorkoutPlan(workoutPlanObj);
        finish();
    }


    private void getDifficultyLevel(String df) {
        switch (df) {
            case "Advanced":
                ivDifficulty3.setImageResource(R.mipmap.ic_star_black);

            case "Intermediate":
                ivDifficulty2.setImageResource(R.mipmap.ic_star_black);

            case "Beginner":
                ivDifficulty1.setImageResource(R.mipmap.ic_star_black);
        }
    }


    private void getType(String type) {
        switch (type) {
            case "General Fitness":
                ivGeneralFitness.setImageResource(R.mipmap.ic_general_fitness_selected);
                break;
            case "Bulking":
                ivBulking.setImageResource(R.mipmap.ic_bulking_selected);
                break;
            case "Cutting":
                ivCutting.setImageResource(R.mipmap.ic_scale_selected);
                break;
            case "Sport Specific":
                ivSportSpecific.setImageResource(R.mipmap.ic_sport_specific_selected);
                break;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //selectDifficultyLevel
            case R.id.iv_difficulty_3:
                ivDifficulty3.setImageResource(R.mipmap.ic_star_white);
                ivDifficulty2.setImageResource(R.mipmap.ic_star_white);
                ivDifficulty1.setImageResource(R.mipmap.ic_star_white);
                difficultyLevel = "Advanced";
                break;
            case R.id.iv_difficulty_2:
                ivDifficulty1.setImageResource(R.mipmap.ic_star_white);
                ivDifficulty2.setImageResource(R.mipmap.ic_star_white);
                ivDifficulty3.setImageResource(R.mipmap.ic_star_black);
                difficultyLevel = "Intermediate";
                break;
            case R.id.iv_difficulty_1:
                ivDifficulty1.setImageResource(R.mipmap.ic_star_white);
                ivDifficulty3.setImageResource(R.mipmap.ic_star_black);
                ivDifficulty2.setImageResource(R.mipmap.ic_star_black);
                difficultyLevel = "Beginner";
                break;

            //selectType
            case R.id.iv_general_fitness:
                ivGeneralFitness.setImageResource(R.mipmap.ic_general_fitness_selected);
                ivBulking.setImageResource(R.mipmap.ic_bulking);
                ivCutting.setImageResource(R.mipmap.ic_scale);
                ivSportSpecific.setImageResource(R.mipmap.ic_sport_specific);
                type = "General Fitness";
                break;
            case R.id.iv_bulking:
                ivGeneralFitness.setImageResource(R.mipmap.ic_general_fitness);
                ivBulking.setImageResource(R.mipmap.ic_bulking_selected);
                ivCutting.setImageResource(R.mipmap.ic_scale);
                ivSportSpecific.setImageResource(R.mipmap.ic_sport_specific);
                type = "Bulking";
                break;
            case R.id.iv_cutting:
                ivGeneralFitness.setImageResource(R.mipmap.ic_general_fitness);
                ivBulking.setImageResource(R.mipmap.ic_bulking);
                ivCutting.setImageResource(R.mipmap.ic_scale_selected);
                ivSportSpecific.setImageResource(R.mipmap.ic_sport_specific);
                type = "Cutting";
                break;
            case R.id.iv_sport_specific:
                ivGeneralFitness.setImageResource(R.mipmap.ic_general_fitness);
                ivBulking.setImageResource(R.mipmap.ic_bulking);
                ivCutting.setImageResource(R.mipmap.ic_scale);
                ivSportSpecific.setImageResource(R.mipmap.ic_sport_specific_selected);
                type = "Sport Specific";
                break;
        }
    }
}
