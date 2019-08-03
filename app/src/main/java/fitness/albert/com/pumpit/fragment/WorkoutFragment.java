package fitness.albert.com.pumpit.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.model.FireBaseInit;
import fitness.albert.com.pumpit.model.PrefsUtils;
import fitness.albert.com.pumpit.model.WorkoutPlans;
import fitness.albert.com.pumpit.workout.CustomPlanActivity;
import fitness.albert.com.pumpit.workout.FindWorkoutActivity;
import fitness.albert.com.pumpit.workout.StartWorkoutActivity;


public class WorkoutFragment extends Fragment implements View.OnClickListener {

    private boolean exerciseExist;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView exerciseName, level, workoutComplete, emptyExercise;
    private ImageView btnAdd, findWorkout, btnStartWorkout;

    public WorkoutFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setToolBar();
        init(view);
        findWorkout.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnStartWorkout.setOnClickListener(this);
        loadExerciseFromFb();
    }

    private void setToolBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        assert activity.getSupportActionBar() != null;
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorDarkBlue)));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadExerciseFromFb();
    }

    private void init(View view) {
        findWorkout = view.findViewById(R.id.btn_find_workout);
        emptyExercise = view.findViewById(R.id.empty_exersis);
        level = view.findViewById(R.id.level);
        workoutComplete = view.findViewById(R.id.workout_complate);
        exerciseName = view.findViewById(R.id.workout_name);
        btnAdd = view.findViewById(R.id.btn_add);
        btnStartWorkout = view.findViewById(R.id.btn_workout);
    }


//    private void loadFragment(Fragment fragment) {
//        if (getFragmentManager() != null) {
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.replace(R.id.frame_container, fragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                startActivity(new Intent(getActivity(), CustomPlanActivity.class));
                break;
            case R.id.btn_find_workout:
                startActivity(new Intent(getActivity(), FindWorkoutActivity.class));
                break;
            case R.id.btn_workout:
                startActivity(new Intent(getActivity(), StartWorkoutActivity.class));
                break;
        }
    }


    private void loadExerciseFromFb() {
        final ProgressDialog progressdialog = new ProgressDialog(getContext());
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressdialog.hide();

                        if (task.getResult() != null && !task.getResult().getDocuments().isEmpty()) {
                            exerciseExist = task.getResult().getDocuments().size() > 0;
                            haveExercise(exerciseExist);
                            progressdialog.hide();
                        }


                    //    haveExercise(exerciseExist);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void haveExercise(boolean exerciseExist) {

        final String routineName;
        final String TAG = "WorkoutFragment";

        if (exerciseExist) {
            PrefsUtils prefsUtils = new PrefsUtils();
            prefsUtils.createSharedPreferencesFiles(getActivity(), PrefsUtils.EXERCISE);

            emptyExercise.setVisibility(TextView.INVISIBLE);
            btnAdd.setVisibility(View.INVISIBLE);
            level.setVisibility(TextView.VISIBLE);
            workoutComplete.setVisibility(TextView.VISIBLE);
            exerciseName.setVisibility(TextView.VISIBLE);

            if (!prefsUtils.getString(PrefsUtils.DEFAULT_PLAN, "").isEmpty()) {
                routineName = prefsUtils.getString(PrefsUtils.DEFAULT_PLAN, "");
                Log.d(TAG, "routineName: " + routineName);

                exerciseName.setText(routineName);
                exerciseName.setTextColor(Color.WHITE);
            }
            prefsUtils.saveData("defaultExercise", true);
        } else {
            btnAdd.setVisibility(View.VISIBLE);
            emptyExercise.setVisibility(TextView.VISIBLE);
            level.setVisibility(TextView.INVISIBLE);
            workoutComplete.setVisibility(TextView.INVISIBLE);
            exerciseName.setVisibility(TextView.INVISIBLE);
        }
    }

}
