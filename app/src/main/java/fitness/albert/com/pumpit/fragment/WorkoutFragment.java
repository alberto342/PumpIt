package fitness.albert.com.pumpit.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.SavePref;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.workout.CustomPlanActivity;
import fitness.albert.com.pumpit.workout.FindWorkoutActivity;
import fitness.albert.com.pumpit.workout.StartWorkoutActivity;
import fitness.albert.com.pumpit.workout.WorkoutPlansActivity;


public class WorkoutFragment extends Fragment implements View.OnClickListener {


    private boolean exerciseExist;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView exerciseName, level, workoutComplete, emptyExercise, seeWorkout;
    private ImageView btnAdd, findWorkout, btnStartWorkout;

    public WorkoutFragment() {
        // Required empty public constructor
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
        init(view);

        seeWorkout.setOnClickListener(this);
        findWorkout.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnStartWorkout.setOnClickListener(this);

        loadExerciseFromFb();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadExerciseFromFb();
    }

    private void init(View view) {
        seeWorkout = view.findViewById(R.id.tv_see_workout);
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
//
//                Fragment fragment = new FindWorkoutFragment();
//                loadFragment(fragment);
                break;
            case R.id.tv_see_workout:
                startActivity(new Intent(getActivity(), WorkoutPlansActivity.class));
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

                        if (task.getResult() != null)
                            exerciseExist = task.getResult().getDocuments().size() > 0;

                        haveExercise(exerciseExist);
                        progressdialog.hide();
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
            SavePref savePref = new SavePref();
            savePref.createSharedPreferencesFiles(getContext(), SavePref.EXERCISE);

            seeWorkout.setVisibility(TextView.VISIBLE);
            emptyExercise.setVisibility(TextView.INVISIBLE);
            btnAdd.setVisibility(View.INVISIBLE);
            level.setVisibility(TextView.VISIBLE);
            workoutComplete.setVisibility(TextView.VISIBLE);
            exerciseName.setVisibility(TextView.VISIBLE);

            if (!savePref.getString(SavePref.DEFAULT_PLAN, "").isEmpty()) {
                routineName = savePref.getString(SavePref.DEFAULT_PLAN, "");
                Log.d(TAG, "routineName: " + routineName);

                exerciseName.setText(routineName);
                exerciseName.setTextColor(Color.WHITE);
            }

            savePref.saveData("defaultExercise", true);

        } else {
            btnAdd.setVisibility(View.VISIBLE);
            emptyExercise.setVisibility(TextView.VISIBLE);
            seeWorkout.setVisibility(TextView.INVISIBLE);
            level.setVisibility(TextView.INVISIBLE);
            workoutComplete.setVisibility(TextView.INVISIBLE);
            exerciseName.setVisibility(TextView.INVISIBLE);
        }
    }
}
