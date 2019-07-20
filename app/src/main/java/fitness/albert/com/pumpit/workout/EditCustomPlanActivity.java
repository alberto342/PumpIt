package fitness.albert.com.pumpit.workout;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.model.FireBaseInit;
import fitness.albert.com.pumpit.model.WorkoutPlans;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.fragment.PlanFragment;

public class EditCustomPlanActivity extends AppCompatActivity implements View.OnClickListener {


    private final String TAG = "EditCustomPlanActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText etRoutineName, etRoutineDescription;
    private ImageView ivDifficulty1, ivDifficulty2, ivDifficulty3, ivGeneralFitness, ivBulking, ivCutting, ivSportSpecific;
    private Spinner spDayType;
    private String type;
    private String difficultyLevel;
    private List<WorkoutPlans> workoutPlansList;
    private String workoutPlanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_custom_plan);

        init();

        setSpinnerDayType();

        getDataFromFb();
    }

    private void init() {
        workoutPlansList = new ArrayList<>();
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


    private void getDataFromFb() {

        CollectionReference reference = db.collection(WorkoutPlans.WORKOUT_PLANS).
                document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME);

        Query query = reference.whereEqualTo("routineName", PlanFragment.routineName);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        WorkoutPlans workoutPlans = task.getResult().getDocuments().get(i).toObject(WorkoutPlans.class);
                        workoutPlansList.add(workoutPlans);

                        etRoutineName.setText(workoutPlansList.get(i).getRoutineName());
                        etRoutineDescription.setText(workoutPlansList.get(i).getRoutineDescription());
                        getDifficultyLevel(workoutPlansList.get(i).getDifficultyLevel());
                        getType(workoutPlansList.get(i).getRoutineType());
                        Log.d(TAG, "Doc ID: " + task.getResult().getDocuments().get(i).getId() + "\ngetAllData: " + task.getResult().getDocuments());
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure " + e);
                    }
                });
    }


    private void updateDataFb() {
        DocumentReference workoutRef = db.collection(WorkoutPlans.WORKOUT_PLANS).
                document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).document(workoutPlanId);

        workoutRef.update("routineName", etRoutineName.getText().toString());
        workoutRef.update("routineDescription", etRoutineDescription.getText().toString());
        if (type != null) {
            workoutRef.update("routineType", type);
        }
        if (difficultyLevel != null) {
            workoutRef.update("difficultyLevel", difficultyLevel);
        }

        startActivity(new Intent(this, WorkoutPlansActivity.class));
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
