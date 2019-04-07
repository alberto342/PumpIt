package fitness.albert.com.pumpit.workout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import fitness.albert.com.pumpit.Adapter.WorkoutAdapter;
import fitness.albert.com.pumpit.Adapter.WorkoutPlanAdapter;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;

public class WorkoutActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "WorkoutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
    }

    private void setWorkout() {

        int position = WorkoutPlanAdapter.posit;

            int dayPosition = WorkoutPlansActivity.workoutPlansList.get(position).getDaysWeekPosition();

            for(int i = 1 ; i <= dayPosition ; i++) {

                WorkoutPlans plans = new WorkoutPlans();

                plans.setDaysWeek("Day " + i);

            }




    }


    private void saveToFb() {
        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).document(WorkoutPlanAdapter.fireId).set(WorkoutPlans.class)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    private void initRecyclerView() {

        final String TAG = "WorkoutActivity";
        RecyclerView view;

        view = findViewById(R.id.rv_workout);

        WorkoutAdapter workoutAdapter;

        Log.d(TAG, "initRecyclerView: init food recyclerView" + view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view.setLayoutManager(layoutManager);

        workoutAdapter = new WorkoutAdapter(this, WorkoutPlansActivity.workoutPlansList);
        view.setAdapter(workoutAdapter);
    }
}
