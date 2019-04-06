package fitness.albert.com.pumpit.workout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fitness.albert.com.pumpit.Adapter.WorkoutPlanAdapter;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;

public class WorkoutPlansActivity extends AppCompatActivity  {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "WorkoutPlansActivity";
    private List<WorkoutPlans> workoutPlansList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plans);

        getPlanFormFb();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_add, menu);
       // MenuItem menuItem = menu.findItem(R.id.add_plans);
        return true;
    }


    private void getPlanFormFb() {

        workoutPlansList = new ArrayList<>();

        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        db.collection(WorkoutPlans.WORKOUT_PLANS).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        progressdialog.hide();

                        if (task.isSuccessful()) {

                            for (int i = 0; i < Objects.requireNonNull(task.getResult()).getDocuments().size(); i++) {
                                WorkoutPlans workoutPlans = task.getResult().getDocuments().get(i).toObject(WorkoutPlans.class);
                                workoutPlansList.add(workoutPlans);

                                initRecyclerView();

                                Log.d(TAG, "DocumentSnapshot data: " + Objects.requireNonNull(task.getResult()).getDocuments());
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


    private void initRecyclerView() {

        RecyclerView view;

        view = findViewById(R.id.rv_workout_plans);

        WorkoutPlanAdapter workoutAdapter;

        Log.d(TAG, "initRecyclerView: init food recyclerView" + view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view.setLayoutManager(layoutManager);

        workoutAdapter = new WorkoutPlanAdapter(this, workoutPlansList);
        view.setAdapter(workoutAdapter);
    }
}
