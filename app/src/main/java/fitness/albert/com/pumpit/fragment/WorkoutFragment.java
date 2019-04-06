package fitness.albert.com.pumpit.fragment;


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

import fitness.albert.com.pumpit.workout.CustomPlanActivity;
import fitness.albert.com.pumpit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutFragment extends Fragment {

    private boolean exerciseExist = true;
    private TextView exerciseName, level, workoutComplete, emptyExercise;
    private ImageView btnAdd;

    public WorkoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ImageView findWorkout;
        init(view);

        if(haveExercise()) {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), CustomPlanActivity.class));
                }
            });
        }

        findWorkout = view.findViewById(R.id.btn_find_workout);
        findWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FindWorkoutFragment();
                loadFragment(fragment);
            }
        });
    }

    private void init(View view){
        emptyExercise = view.findViewById(R.id.empty_exersis);
        level = view.findViewById(R.id.level);
        workoutComplete = view.findViewById(R.id.workout_complate);
        exerciseName = view.findViewById(R.id.workout_name);
        btnAdd = view.findViewById(R.id.btn_add);
    }



    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private boolean haveExercise() {
        if(exerciseExist) {
            level.setVisibility(TextView.INVISIBLE);
            workoutComplete.setVisibility(TextView.INVISIBLE);
            exerciseName.setVisibility(TextView.INVISIBLE);
            return true;
        } else {
            emptyExercise.setVisibility(TextView.INVISIBLE);
            btnAdd.setVisibility(View.INVISIBLE);
            return false;
        }
    }


}
