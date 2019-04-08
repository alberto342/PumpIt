package fitness.albert.com.pumpit.workout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.Adapter.WorkoutAdapter;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.Workout;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;

public class WorkoutActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "WorkoutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        getPlanFormFb();

    }



    private void getPlanFormFb() {
        final List<Workout> workoutList = new ArrayList<>();

        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).document("znAnjovxgohOx3g47BrN").collection(Workout.WORKOUT)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressdialog.hide();

                        if(task.isSuccessful() && task.getResult() != null) {

                            for(int i = 0 ; i<task.getResult().size(); i++) {

                                Workout workout = task.getResult().getDocuments().get(i).toObject(Workout.class);
                                workoutList.add(workout);

                                initRecyclerView(workoutList);


                                Log.d(TAG, "DocumentSnapshot written with ID: " + task.getResult().getDocuments().get(i).getData());
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
    }




    private void initRecyclerView(List<Workout>workoutList) {

        final String TAG = "WorkoutActivity";
        RecyclerView view;

        view = findViewById(R.id.rv_workout);

        WorkoutAdapter workoutAdapter;

        Log.d(TAG, "initRecyclerView: init food recyclerView" + view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view.setLayoutManager(layoutManager);

        workoutAdapter = new WorkoutAdapter(this, workoutList);
        view.setAdapter(workoutAdapter);
    }
}
