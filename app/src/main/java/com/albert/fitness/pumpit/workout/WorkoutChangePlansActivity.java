package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.WorkoutPlanAdapter;
import com.albert.fitness.pumpit.model.WorkoutPlanObj;
import com.albert.fitness.pumpit.model.WorkoutRepository;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;

import java.util.List;

import fitness.albert.com.pumpit.R;
import it.shadowsheep.recyclerviewswipehelper.RecyclerViewSwipeHelper;

public class WorkoutChangePlansActivity extends AppCompatActivity
        implements RecyclerViewSwipeHelper.RecyclerViewSwipeHelperDelegate {

    private CustomPlanViewModel customPlanViewModel;
    private List<WorkoutPlanObj> workoutPlanList;
    private WorkoutRepository repository;
    private RecyclerView mRecyclerView;
    private final String TAG = "WorkoutChangePlansActivity";
    public static String planName;
    public static String routineName; // check in EditCustomPlanActivity
    //private WorkoutPlanAdapter workoutAdapter;
    private WorkoutPlanAdapter workoutAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plans);
        customPlanViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);

        getAllPlans();
        //setupSwipeMenu();
    }

    private void getAllPlans() {
        customPlanViewModel.getAllPlan().observe(this, new Observer<List<WorkoutPlanObj>>() {
            @Override
            public void onChanged(List<WorkoutPlanObj> workoutPlan) {
                workoutPlanList = workoutPlan;
                initRecyclerView();
            }
        });
    }




//    private void setupSwipeMenu() {
//        new RecyclerViewSwipeHelper(this, mRecyclerView, this);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_add, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_plans) {
            startActivity(new Intent(this, CustomPlanActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean showButton(int rowPosition, int buttonIndex) {
        return true;
    }

    @Override
    public int buttonWidth() {
        return 0;
    }

    @Override
    public void setupSwipeButtons(RecyclerView.ViewHolder viewHolder,
                                  List<RecyclerViewSwipeHelper.SwipeButton> swipeButtons) {
        //swipe delete
        swipeButtons.add(new RecyclerViewSwipeHelper.SwipeButton(
                getBaseContext(),
                0,
                0,
                R.drawable.ic_delete,
                R.dimen.ic_delete_size,
                R.color.colorAccent,
                new RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        //  deleteItem(pos);
                     //   deleteFromFb(pos);
                    }
                }
        ));
        //swipe edit
        swipeButtons.add(new RecyclerViewSwipeHelper.SwipeButton(
                getBaseContext(),
                0,
                0,
                R.drawable.ic_edit_action,
                R.dimen.ic_delete_size,
                R.color.md_green_500,
                new RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onClick(int pos) {
                        routineName = workoutPlanList.get(pos).getRoutineName();
                        Log.d(TAG, "pos: " + pos + " Workout Name: " + workoutPlanList.get(pos).getRoutineName());

                        Intent i = new Intent(getBaseContext(), EditCustomPlanActivity.class);
                        startActivity(i);
                    }
                }
        ));
    }


//    private void deleteItem(final int position) {
//        workoutAdapter = new WorkoutPlanAdapter(this, workoutPlansList);
//        workoutPlansList.remove(position);
//        mRecyclerView.removeViewAt(position);
//        workoutAdapter.notifyItemRemoved(position);
//        workoutAdapter.notifyItemRangeChanged(position, workoutPlansList.size());
//    }


    @SuppressLint("LongLogTag")
    private void initRecyclerView() {
        // RecyclerView view;
        mRecyclerView = findViewById(R.id.rv_workout_plans);
        Log.d(TAG, "initRecyclerView: init WorkoutPlan recyclerView" + mRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        workoutAdapter = new WorkoutPlanAdapter(this, workoutPlanList);
        mRecyclerView.setAdapter(workoutAdapter);
    }
}
