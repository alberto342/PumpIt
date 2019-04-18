package fitness.albert.com.pumpit.workout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;

public class EditCustomPlanActivity extends AppCompatActivity {


    private final String TAG = "EditCustomPlanActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText etRoutineName, etRoutineDescription;
    private ImageView ivDifficulty1, ivDifficulty2, ivDifficulty3, ivGeneralFitness, ivBulking, ivCutting, ivSportSpecific;
    private Spinner spDayType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_custom_plan);

        init();

        setSpinnerDayType();

        getDataFromFb();
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
            updateDataFb(etRoutineName.getText().toString());
            Log.d(TAG, "Save Successfully");
        }

        return super.onOptionsItemSelected(item);
    }


    private void getDataFromFb() {
        db.collection(WorkoutPlans.WORKOUT_PLANS).
                document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME)
                .document(WorkoutPlansActivity.routineName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    WorkoutPlans workoutPlans = task.getResult().toObject(WorkoutPlans.class);

                    if (workoutPlans != null) {
                        etRoutineName.setText(workoutPlans.getRoutineName());
                        etRoutineDescription.setText(workoutPlans.getRoutineDescription());
                        getDifficultyLevel(workoutPlans.getDifficultyLevel());

                        Log.d(TAG, "getData: " + workoutPlans.getRoutineName());
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


    private void updateDataFb(String newRoutineName) {
        DocumentReference workoutRef = db.collection(WorkoutPlans.WORKOUT_PLANS).
                document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).document(WorkoutPlansActivity.routineName);

        WorkoutPlans workoutPlans = new WorkoutPlans();

        workoutRef.update(workoutPlans.getRoutineName(), newRoutineName);

    }


    private void getDifficultyLevel(String df) {
        switch (df) {
            case "Beginner":
                ivDifficulty1.setImageResource(R.mipmap.ic_star_black);
            case "Intermediate":
                ivDifficulty2.setImageResource(R.mipmap.ic_star_black);

            case "Advanced":
                ivDifficulty3.setImageResource(R.mipmap.ic_star_black);
        }
    }



}
