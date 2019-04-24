package fitness.albert.com.pumpit.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.UserRegister;
import fitness.albert.com.pumpit.Model.Workout;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;

public class CustomPlanActivity extends AppCompatActivity {

    private Spinner spDaysWeek, spDifficultyLevel, spDayType, spRoutineType;
    private EditText etRoutineDescription, etRoutineName;
    private ImageView btnCreateWorkout;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "CustomPlanActivity";
    private String workoutNameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_plan);

        Objects.requireNonNull(getSupportActionBar()).hide();

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
        addIntoSpinner(dayTypeList,spDayType );
        addIntoSpinner(routineTypeList, spRoutineType);
    }


    private void addIntoSpinner(List<String> list, Spinner spinner) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }




    private void addDataIntoFireBase() {
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

                    Log.d(TAG, "Get spinner index" + spDaysWeek.getSelectedItemPosition());

            dayType = spDayType.getSelectedItem().toString();
        }

        final int dayWeekPosition = spDaysWeek.getSelectedItemPosition() + 1;
        routineDescription = etRoutineDescription.getText().toString();
        routineName = etRoutineName.getText().toString();


        WorkoutPlans workoutPlans = new WorkoutPlans(routineName, daysWeek, difficultyLevel, routineType,dayType, routineDescription, UserRegister.getTodayData(), dayWeekPosition);


        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME)
                .add(workoutPlans).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                if(task.isSuccessful() && task.getResult() != null) {
                    workoutNameId = task.getResult().getId();
                }


                for (int i = 1; i <= dayWeekPosition; i++) {

                    Workout workout = new Workout("Workout " + i, "Day " + i, 0, 0);

                    db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).document(workoutNameId).collection(Workout.WORKOUT_DAY_NAME)
                            .document("Workout " + i).set(workout)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "DocumentSnapshot successfully saved");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to save " + e);
                                }
                            });
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


    private void onClick() {
        btnCreateWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataIntoFireBase();
                startActivity(new Intent(CustomPlanActivity.this, WorkoutPlansActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
