package fitness.albert.com.pumpit.workout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.fragment.FragmentNavigationActivity;
import fitness.albert.com.pumpit.model.FinishTraining;
import fitness.albert.com.pumpit.model.FireBaseInit;
import fitness.albert.com.pumpit.model.PrefsUtils;
import fitness.albert.com.pumpit.model.UserRegister;

public class FinisherWorkoutActivity extends AppCompatActivity {

    private static final String TAG = "FinisherWorkoutActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<FinishTraining> finishTrainingList = new ArrayList<>();
    private TextView tvNewRecord;
    private TextView tvTrainingRecord;
    private TextView tvActualRecord;
    private TextView tvRest;
    private TextView tvWaste;
    private TextView tvCompleteExercise;
    private TextView tvTotalWeightCmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finisher_workout);
        setTitle("Finished Workout");
        initView();
        getRecordFromFb();

    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        tvNewRecord = findViewById(R.id.tv_new_record);
        tvTrainingRecord = findViewById(R.id.tv_training_record);
        tvActualRecord = findViewById(R.id.tv_actual_record);
        tvRest = findViewById(R.id.tv_rest);
        tvWaste = findViewById(R.id.tv_waste);
        tvCompleteExercise = findViewById(R.id.tv_complete_exercise);
        tvTotalWeightCmp = findViewById(R.id.tv_total_weight_cmp);
        TextView tvDate = findViewById(R.id.tv_date_of_day);
        tvDate.setText("○ " + UserRegister.getTodayData() + " ○");
        Button finish = findViewById(R.id.btn_finished_workout);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FinisherWorkoutActivity.this, FragmentNavigationActivity.class));
                finish();
            }
        });
    }

    private void getRecordFromFb() {
        final ProgressDialog progressdialog = new ProgressDialog(FinisherWorkoutActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();


        final PrefsUtils prefsUtils = new PrefsUtils();
        prefsUtils.createSharedPreferencesFiles(this, PrefsUtils.SETTINGS_PREFERENCES_FILE);
        final int[] totalWeight = {0};

        db.collection(FinishTraining.TRAINING_LOG).document(FireBaseInit.getEmailRegister())
                .collection(UserRegister.getTodayData()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressdialog.hide();

                        if (task.isSuccessful() && task.getResult() != null) {

                            FinishTraining chrTotalTraining = task.getResult().getDocuments().get(task.getResult().size() - 1).toObject(FinishTraining.class);

                            tvTrainingRecord.setText(chrTotalTraining.getChrTotalTraining());
                            tvCompleteExercise.setText(String.valueOf(task.getResult().size()));

                            prefsUtils.saveData("exerciseComplete",String.valueOf(task.getResult().size()));

                            for (int i = 0; i < task.getResult().size(); i++) {
                                FinishTraining finishTraining = task.getResult().getDocuments().get(i).toObject(FinishTraining.class);
                                finishTrainingList.add(finishTraining);

                                for(int r=0; r< finishTraining.getTrackerExercises().size(); r++) {
                                    totalWeight[0] += finishTraining.getTrackerExercises().get(r).getWeight();
                                }
                            }
                            tvTotalWeightCmp.setText(totalWeight[0] + " kg");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "get failed with: " + e);
            }
        });
    }


}
