package fitness.albert.com.pumpit.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fitness.albert.com.pumpit.adapter.WorkoutPlanAdapter;
import fitness.albert.com.pumpit.model.FireBaseInit;
import fitness.albert.com.pumpit.model.PrefsUtils;
import fitness.albert.com.pumpit.model.SwipeHelper;
import fitness.albert.com.pumpit.model.Workout;
import fitness.albert.com.pumpit.model.WorkoutPlans;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.workout.CustomPlanActivity;
import fitness.albert.com.pumpit.workout.EditCustomPlanActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "PlanFragment";
    public static List<WorkoutPlans> workoutPlansList;
    public static String planName;
    public static String routineName; // chack in EditCustomPlanActivity
    private WorkoutPlanAdapter workoutAdapter;


    public PlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        mRecyclerView = view.findViewById(R.id.fr_rv_workout_plans);
        setHasOptionsMenu(true);
        getPlanFormFb();
        initRecyclerView();
        swipe();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_plans) {
            startActivity(new Intent(getActivity(), CustomPlanActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }



    private void swipe() {
        new SwipeHelper(getContext(), mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        // R.color.colorAccent,
                        Color.parseColor("#d50000"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                deleteItem(pos);
                                deleteFromFb(pos);
                                workoutAdapter.notifyDataSetChanged();
                            }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        0,
                        //R.color.md_green_500,
                        Color.parseColor("#4caf50"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                routineName = workoutPlansList.get(pos).getRoutineName();
                                Log.d(TAG, "pos: " + pos + " Workout Name: " + workoutPlansList.get(pos).getRoutineName());

                                Intent i = new Intent(Objects.requireNonNull(getActivity()).getBaseContext(), EditCustomPlanActivity.class);
                                startActivity(i);
                            }
                        }
                ));
            }
        };
    }

    private void deleteItem(final int position) {
        workoutAdapter = new WorkoutPlanAdapter(getActivity(), workoutPlansList);
        workoutPlansList.remove(position);
        mRecyclerView.removeViewAt(position);
        workoutAdapter.notifyItemRemoved(position);
        workoutAdapter.notifyItemRangeChanged(position, workoutPlansList.size());
    }


    private void deleteFromFb(final int position) {

        db.collection(WorkoutPlans.WORKOUT_PLANS).
                document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            final String id = task.getResult().getDocuments().get(position).getId();

                            //find all
                            db.collection(WorkoutPlans.WORKOUT_PLANS).
                                    document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME)
                                    .document(id).collection(Workout.WORKOUT_DAY_NAME).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && task.getResult() != null) {
                                                for (int i = 0; i < task.getResult().size(); i++) {
                                                    String workoutDayId = task.getResult().getDocuments().get(i).getId();

                                                    db.collection(WorkoutPlans.WORKOUT_PLANS).
                                                            document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME)
                                                            .document(id).collection(Workout.WORKOUT_DAY_NAME).document(workoutDayId).delete();
                                                }
                                            }
                                        }
                                    });

                            db.collection(WorkoutPlans.WORKOUT_PLANS).
                                    document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME)
                                    .document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                }
                            });
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "isFailure: " + e);
                    }
                });
    }


    private void getPlanFormFb() {
        workoutPlansList = new ArrayList<>();

        final ProgressDialog progressdialog = new ProgressDialog(getActivity());
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

                                assert workoutPlans != null;
                                planName = workoutPlans.getRoutineName();

                                initRecyclerView();
                            }
                            if (workoutPlansList.size() == 1) {
                                PrefsUtils prefsUtils = new PrefsUtils();
                                prefsUtils.createSharedPreferencesFiles(getActivity(), "exercise");
                                prefsUtils.saveData("default_plan", workoutPlansList.get(0).getRoutineName());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
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
        // RecyclerView view;
        Log.d(TAG, "initRecyclerView: init WorkoutPlan recyclerView" + mRecyclerView);

        @SuppressLint("WrongConstant") LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        workoutAdapter = new WorkoutPlanAdapter(getActivity(), workoutPlansList);
        mRecyclerView.setAdapter(workoutAdapter);
    }
}
