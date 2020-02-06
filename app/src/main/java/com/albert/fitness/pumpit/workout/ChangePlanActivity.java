package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;
import com.albert.fitness.pumpit.adapter.ChangePlanAdapter;
import com.albert.fitness.pumpit.utils.FireBaseInit;
import com.albert.fitness.pumpit.model.WorkoutPlans;

public class ChangePlanActivity extends AppCompatActivity {

    private final String TAG = "ChangePlanFragment";
    private RecyclerView mRecyclerView;
    private List<WorkoutPlans> workoutPlansList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_plan);

        init();

        getPlanFromFb();
        initRecyclerView();
    }

    private void init() {
        mRecyclerView = findViewById(R.id.rv_change_plan);
    }

    private void getPlanFromFb() {

        workoutPlansList = new ArrayList<>();

        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        progressdialog.hide();

                        if (task.isSuccessful() && task.getResult() != null) {
                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                                WorkoutPlans workoutPlans = task.getResult().getDocuments().get(i).toObject(WorkoutPlans.class);
                                workoutPlansList.add(workoutPlans);

                                initRecyclerView();
                            }
                        } else {
                            Log.i(TAG, "Error getting documents: ", task.getException());
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
        Log.d(TAG, "initRecyclerView: init ChangePlan recyclerView" + mRecyclerView);

        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        ChangePlanAdapter changePlanAdapter = new ChangePlanAdapter(this, workoutPlansList);
        mRecyclerView.setAdapter(changePlanAdapter);
    }
}
