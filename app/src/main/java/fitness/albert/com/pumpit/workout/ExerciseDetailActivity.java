package fitness.albert.com.pumpit.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.Adapter.ExerciseAdapter.ExerciseAdapter;
import fitness.albert.com.pumpit.Adapter.WorkoutAdapter;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.SavePref;
import fitness.albert.com.pumpit.Model.Training;
import fitness.albert.com.pumpit.Model.Workout;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;

public class ExerciseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView exerciseImg, favorite, log, plan;
    private TextView name;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "ExerciseDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exercise_detail);

        init();

        layoutadd();
    }

    private void init() {
        exerciseImg = findViewById(R.id.iv_exercise_img);
        favorite = findViewById(R.id.iv_add_favorite);
        log = findViewById(R.id.iv_log);
        plan = findViewById(R.id.iv_add_exercise_into_plan);
        name = findViewById(R.id.tv_name);

        favorite.setOnClickListener(this);
        log.setOnClickListener(this);
        plan.setOnClickListener(this);

        name.setText(ExerciseAdapter.exerciseName);

        try {
            String imgFile = "file:///android_asset/images/" + ExerciseAdapter.exerciseImg;

            Glide.with(this)
                    .asGif()
                    .load(imgFile)
                    .into(exerciseImg);

            Log.d(TAG, "Img successfully loaded " + ExerciseAdapter.exerciseImg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_log:
                startActivity(new Intent(this, ExerciseLogsActivity.class));
                finish();
                break;

            case R.id.iv_add_favorite:
                favorite.setImageResource(R.mipmap.ic_favorite_selected);
                break;

            case R.id.iv_add_exercise_into_plan:
                setDataIntoFb();
                break;
        }
    }



    private void setDataIntoFb() {

        final SavePref savePref = new SavePref();

        List<Training> trainingList = new ArrayList<>();

        Log.d(TAG, "Get SharedPreferencesFiles  - exercise");

        savePref.createSharedPreferencesFiles(this, "exercise");
        String getPlanId = savePref.getString("planName", "");


       // Training training = new Training(ExerciseAdapter.exerciseName,0,0,20, ExerciseAdapter.exerciseImg, UserRegister.getTodayData(),false, 5.6f);

        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(Workout.WORKOUT_NAME).document(getPlanId).collection(Workout.WORKOUT).
                document(WorkoutAdapter.workoutDayName).collection(ExerciseAdapter.exerciseName)
                .add(trainingList).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                savePref.removeSingle(ExerciseDetailActivity.this, "exercise", "planName");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error writing document", e);
                    }
                });
    }



//    private void initRecyclerView(List<Training> trackerList) {
//
//        final String TAG = "WorkoutActivity";
//        RecyclerView view;
//
//        view = findViewById(R.id.rv_tracker);
//
//        WorkoutTrackerAdapter workoutAdapter;
//
//        Log.d(TAG, "initRecyclerView: init workout recyclerView" + view);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        view.setLayoutManager(layoutManager);
//
//        workoutAdapter = new WorkoutTrackerAdapter(this, trackerList);
//        view.setAdapter(workoutAdapter);
//    }


    private void layoutadd() {



        setContentView(R.layout.item_workout_tracker);
        ViewGroup parent = findViewById(R.id.cl_workout_tracker);


    }



    @Override
    public void onBackPressed() {
        WorkoutAdapter.workoutDayName = "null";
        finish();
    }
}
