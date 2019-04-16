package fitness.albert.com.pumpit.workout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.Adapter.WorkoutAdapter;
import fitness.albert.com.pumpit.Adapter.WorkoutPlanAdapter;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.Workout;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;

public class WorkoutActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "WorkoutActivity";
    private TextView tvNameOfPlan, tvNameOfPlanSmall, tvActiveWorkout, tvChangePlan;
    public static String workoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        init();

        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        getPlanFormFb();


        progressdialog.hide();

        isActivatedPlan();

    }

    private void init() {
        tvNameOfPlan = findViewById(R.id.tv_name_of_plan);
        tvNameOfPlanSmall = findViewById(R.id.tv_name_of_plan_s);
        tvActiveWorkout = findViewById(R.id.tv_active_workout);
        tvChangePlan = findViewById(R.id.tv_change_plan);
    }


    private void isActivatedPlan() {
        boolean isActivate = false;

        if (isActivate) {
            tvChangePlan.setVisibility(View.VISIBLE);
            tvActiveWorkout.setVisibility(View.VISIBLE);
            tvNameOfPlanSmall.setVisibility(View.VISIBLE);
            tvNameOfPlan.setVisibility(View.INVISIBLE);
        } else {
            tvChangePlan.setVisibility(View.INVISIBLE);
            tvActiveWorkout.setVisibility(View.INVISIBLE);
            tvNameOfPlanSmall.setVisibility(View.INVISIBLE);
            tvNameOfPlan.setVisibility(View.VISIBLE);
        }
    }

    private void getPlanFormFb() {

        final List<Workout> workoutList = new ArrayList<>();

        //get workout id
        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null) {
                            workoutId = task.getResult().getDocuments().get(WorkoutPlanAdapter.posit).getId();

                            //set plan name
                              WorkoutPlans workoutPlans = task.getResult().getDocuments().get(WorkoutPlanAdapter.posit).toObject(WorkoutPlans.class);
                              assert workoutPlans != null;
                              tvNameOfPlan.setText(workoutPlans.getRoutineName());
                              tvNameOfPlanSmall.setText(workoutPlans.getRoutineName());

                                db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).document(workoutId).collection(Workout.WORKOUT)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                if (task.isSuccessful() && task.getResult() != null) {

                                                    for (int i = 0; i < task.getResult().size(); i++) {
                                                        Workout workout = task.getResult().getDocuments().get(i).toObject(Workout.class);
                                                        workoutList.add(workout);

                                                        initRecyclerView(workoutList);

                                                        Log.d(TAG, "Workout - DocumentSnapshot written with ID: " + task.getResult().getDocuments().get(i).getData());
                                                    }
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






    private void initRecyclerView(List<Workout> workoutList) {

        final String TAG = "WorkoutActivity";
        RecyclerView view;

        view = findViewById(R.id.rv_workout);
        WorkoutAdapter workoutAdapter;

        Log.d(TAG, "initRecyclerView: init workout recyclerView" + view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view.setLayoutManager(layoutManager);

        workoutAdapter = new WorkoutAdapter(this, workoutList);
        view.setAdapter(workoutAdapter);
    }
}
