package fitness.albert.com.pumpit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fitness.albert.com.pumpit.Model.WorkoutPlans;

public class CustomPlanActivity extends AppCompatActivity {

    private Spinner spDaysWeek, spDifficultyLevel, spDayType;
    private EditText etRoutineDescription, etRoutineName;
    private ImageView btnCreateWorkout;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "CustomPlanActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_plan);

        getSupportActionBar().hide();

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
    }

    public void addItemsIntoSpinner() {

        List<String> daysWeekList = new ArrayList<>();
        List<String> difficultyLevelList = new ArrayList<>();
        List<String> dayTypeList = new ArrayList<>();

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

        ArrayAdapter<String> daysWeekAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, daysWeekList);
        daysWeekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDaysWeek.setAdapter(daysWeekAdapter);


        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, difficultyLevelList);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDifficultyLevel.setAdapter(difficultyAdapter);


        ArrayAdapter<String> dayTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dayTypeList);
        dayTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDayType.setAdapter(dayTypeAdapter);
    }



    private void addDataIntoFireBase() {

        String daysWeek = null, difficultyLevel = null, dayType = null, routineDescription, routineName;

        if (spDaysWeek.getSelectedItem() != null && spDifficultyLevel.getSelectedItem() != null && spDayType.getSelectedItem() != null) {
            daysWeek = spDaysWeek.getSelectedItem().toString();
            difficultyLevel = spDifficultyLevel.getSelectedItem().toString();
            dayType = spDayType.getSelectedItem().toString();
        }

        routineDescription = etRoutineDescription.getText().toString();
        routineName = etRoutineName.getText().toString();

        WorkoutPlans workoutPlans = new WorkoutPlans(routineName, daysWeek, difficultyLevel, dayType, routineDescription, getTodayDate());

        db.collection(WorkoutPlans.WORKOUT_PLANS)
                .add(workoutPlans)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    private void onClick() {
        btnCreateWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataIntoFireBase();
            }
        });
    }


    private String getTodayDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        return df.format(c);
    }


}
