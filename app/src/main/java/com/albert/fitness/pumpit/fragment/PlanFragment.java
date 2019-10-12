package com.albert.fitness.pumpit.fragment;


import android.annotation.SuppressLint;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.WorkoutPlanAdapter;
import com.albert.fitness.pumpit.model.WorkoutPlanObj;
import com.albert.fitness.pumpit.model.WorkoutPlans;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.utils.SwipeHelper;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;
import com.albert.fitness.pumpit.workout.CustomPlanActivity;
import com.albert.fitness.pumpit.workout.EditCustomPlanActivity;

import java.util.List;
import java.util.Objects;

import fitness.albert.com.pumpit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private final String TAG = "PlanFragment";
    public static List<WorkoutPlans> workoutPlansList;
    public static String planName;
    public static String routineName; // check in EditCustomPlanActivity
    private WorkoutPlanAdapter planAdapter;

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
        //  getPlanFormFb();
        //  initRecyclerView();
        //  swipe();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CustomPlanViewModel customPlanViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);
        customPlanViewModel.getAllPlan().observe(this, new Observer<List<WorkoutPlanObj>>() {
            @Override
            public void onChanged(List<WorkoutPlanObj> workoutPlanObjs) {
                if(!workoutPlanObjs.isEmpty()) {
                    workoutPlanObjs.get(0).getRoutineType();
                    initRecyclerView(workoutPlanObjs);

                    PrefsUtils prefsUtils = new PrefsUtils(getActivity(), "exercise");
                    prefsUtils.saveData("default_plan", workoutPlanObjs.get(0).getRoutineName());
                }
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
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
                                //    deleteItem(pos);
                                planAdapter.notifyDataSetChanged();
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

//    private void deleteItem(final int position) {
//        workoutAdapter = new WorkoutPlanAdapter(getActivity(), workoutPlansList);
//        workoutPlansList.remove(position);
//        mRecyclerView.removeViewAt(position);
//        workoutAdapter.notifyItemRemoved(position);
//        workoutAdapter.notifyItemRangeChanged(position, workoutPlansList.size());
//    }



//data blding
//    private void initRecyclerView(List<WorkoutPlanObj> workoutPlanObj) {
//        // RecyclerView view;
//        Log.d(TAG, "initRecyclerView: init WorkoutPlan recyclerView" + mRecyclerView);
//        @SuppressLint("WrongConstant") LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(layoutManager);
//        PlanAdapter planAdapter = new PlanAdapter();
//        planAdapter.setItems((ArrayList<WorkoutPlanObj>) workoutPlanObj);
//        mRecyclerView.setAdapter(planAdapter);
//    }


    private void initRecyclerView(List<WorkoutPlanObj> workoutPlanObj) {
        // RecyclerView view;
        Log.d(TAG, "initRecyclerView: init WorkoutPlan recyclerView" + mRecyclerView);
        @SuppressLint("WrongConstant") LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        planAdapter = new WorkoutPlanAdapter(getActivity(), workoutPlanObj);
        mRecyclerView.setAdapter(planAdapter);
    }
}
