package fitness.albert.com.pumpit.workout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.fragment.logsFragment.LogFragment;
import fitness.albert.com.pumpit.model.FinishTraining;
import fitness.albert.com.pumpit.model.FireBaseInit;

public class ShowLogExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "ShowLogExerciseActivity";
    private LinearLayout container;
    private TextView set, exerciseName;
    private EditText reps, weight;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_log_exercise);

        setTitle("Log Exercise");
        initView();
        getExtra();
    }

    private void initView() {
        container = findViewById(R.id.contener_layout_log);
        exerciseName = findViewById(R.id.log_show_exercise_name);
        ImageView save = findViewById(R.id.btn_save_logExercise);
        ImageView btnPlus = findViewById(R.id.btn_plus_log);
        ImageView btnMinus = findViewById(R.id.btn_minus_log);

        save.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_logExercise:
                saveChangeIntoFb();
                break;
            case R.id.btn_plus_log:
                addLayoutSetsAndRep();
                break;
            case R.id.btn_minus_log:
                if (container.getChildCount() > 1) {
                    container.removeViewAt(container.getChildCount() - 1);
                }
        }
    }


    @SuppressLint("SetTextI18n")
    private void getExtra() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            exerciseName.setText(extras.getString("exerciseName"));
            int size = extras.getInt("trackerExercisesSize");
            for (int i = 0; i < size; i++) {
                addLayoutSetsAndRep();

                reps.setText(String.valueOf(extras.getInt("repNumber" + i)));
                weight.setText(String.valueOf(extras.getFloat("weight" + i)));

                Log.d(TAG, "reps: " + reps.getText());
            }
        }
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    private void addLayoutSetsAndRep() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_log_sets_and_rep, null);
        initLayoutReps(rowView);
        container.addView(rowView, container.getChildCount() - 1);
        set.setText("Set " + container.getChildCount());
        Log.d(TAG, "Layout log added successfully");
    }

    private void initLayoutReps(View rowView) {
        set = rowView.findViewById(R.id.tv_log_sets_num);
        reps = rowView.findViewById(R.id.et_log_reps);
        weight = rowView.findViewById(R.id.et_log_weight);
    }


    // TODO: 2019-06-21 update fb

    private void saveChangeIntoFb() {
        final String[] docId = new String[1];

        db.collection(FinishTraining.TRAINING_LOG).document(FireBaseInit.getEmailRegister())
                .collection(LogFragment.date).whereEqualTo("exerciseName", exerciseName.getText().toString())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        docId[0] = document.getId();
                        Log.d(TAG, "document getId " + document.getId() + " => " + document.getData());
                    }
                }
            }
        });

//         DocumentReference doc = db.collection(FinishTraining.TRAINING_LOG).document(FireBaseInit.getEmailRegister())
//                .collection(UserRegister.getTodayDate()).document(docId[0]);








    }
}
