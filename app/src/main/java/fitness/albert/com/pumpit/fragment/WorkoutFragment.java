package fitness.albert.com.pumpit.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.workout.CustomPlanActivity;
import fitness.albert.com.pumpit.workout.WorkoutPlansActivity;


public class WorkoutFragment extends Fragment implements View.OnClickListener {

    private boolean exerciseExist;
    //private final String TAG = "WorkoutFragment";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView exerciseName, level, workoutComplete, emptyExercise, seeWorkout;
    private ImageView btnAdd, findWorkout;

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
    }


    private void loadFragment(Fragment fragment) {
        if (getFragmentManager() != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                startActivity(new Intent(getActivity(), CustomPlanActivity.class));
                break;
            case R.id.btn_find_workout:
                Fragment fragment = new FindWorkoutFragment();
                loadFragment(fragment);
                break;
            case R.id.tv_see_workout:
                startActivity(new Intent(getActivity(), WorkoutPlansActivity.class));
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
        if (exerciseExist) {
            seeWorkout.setVisibility(TextView.VISIBLE);
            emptyExercise.setVisibility(TextView.INVISIBLE);
            btnAdd.setVisibility(View.INVISIBLE);
            level.setVisibility(TextView.VISIBLE);
            workoutComplete.setVisibility(TextView.VISIBLE);
            exerciseName.setVisibility(TextView.VISIBLE);

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
