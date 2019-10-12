package com.albert.fitness.pumpit.workout;

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

import com.albert.fitness.pumpit.adapter.new_adapter.WorkoutPlanAdapter;
import com.albert.fitness.pumpit.model.WorkoutObj;
import com.albert.fitness.pumpit.model.WorkoutPlanObj;
import com.albert.fitness.pumpit.model.WorkoutPlans;
import com.albert.fitness.pumpit.model.WorkoutRepository;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;
import it.shadowsheep.recyclerviewswipehelper.RecyclerViewSwipeHelper;

public class WorkoutPlansActivity extends AppCompatActivity
        implements RecyclerViewSwipeHelper.RecyclerViewSwipeHelperDelegate, WorkoutPlanAdapter.Interaction {

    private CustomPlanViewModel customPlanViewModel;
    private ArrayList<WorkoutPlanObj> workoutPlanList;
    private WorkoutRepository repository;
    private RecyclerView mRecyclerView;
    private final String TAG = "WorkoutPlansActivity";
    public static List<WorkoutPlans> workoutPlansList;
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


      //  getPlanFormFb();



        //setupSwipeMenu();
    }

    private void getAllPlans() {
        customPlanViewModel.getAllWorkouts().observe(this, new Observer<List<WorkoutObj>>() {
            @Override
            public void onChanged(List<WorkoutObj> workoutObjs) {
               for(WorkoutObj workoutObj : workoutObjs) {
                   Log.i(TAG, "getAllPlans: " + workoutObj.getWorkoutDay());
                   initRecyclerView();
               }
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
                    @Override
                    public void onClick(int pos) {
                        routineName = workoutPlansList.get(pos).getRoutineName();
                        Log.d(TAG, "pos: " + pos + " Workout Name: " + workoutPlansList.get(pos).getRoutineName());

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




//    private void getPlanFormFb() {
//        workoutPlansList = new ArrayList<>();
//
//        final ProgressDialog progressdialog = new ProgressDialog(this);
//        progressdialog.setMessage("Please Wait....");
//        progressdialog.show();
//
//        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
//                                WorkoutPlans workoutPlans = task.getResult().getDocuments().get(i).toObject(WorkoutPlans.class);
//                                workoutPlansList.add(workoutPlans);
//
//                                assert workoutPlans != null;
//                                planName = workoutPlans.getRoutineName();
//
//                                initRecyclerView();
//                            }
//                            if (workoutPlansList.size() == 1) {
//                                PrefsUtils prefsUtils = new PrefsUtils();
//                                prefsUtils.createSharedPreferencesFiles(WorkoutPlansActivity.this, "exercise");
//                                prefsUtils.saveData("default_plan", workoutPlansList.get(0).getRoutineName());
//                            }
//
//                            progressdialog.hide();
//
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//    }


//    private void initRecyclerView() {
//        // RecyclerView view;
//        mRecyclerView = findViewById(R.id.rv_workout_plans);
//        Log.d(TAG, "initRecyclerView: init WorkoutPlan recyclerView" + mRecyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(layoutManager);
//        workoutAdapter = new WorkoutPlanAdapter(this, workoutPlansList);
//        mRecyclerView.setAdapter(workoutAdapter);
//    }


    private void initRecyclerView() {
        // RecyclerView view;
        mRecyclerView = findViewById(R.id.rv_workout_plans);
        Log.d(TAG, "initRecyclerView: init WorkoutPlan recyclerView" + mRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        workoutAdapter = new WorkoutPlanAdapter(this);
        workoutAdapter.submitList(workoutPlanList);
        mRecyclerView.setAdapter(workoutAdapter);
    }


    @Override
    public void onItemSelected(int position, @NotNull WorkoutPlanObj item) {
        startActivity(new Intent(this, WorkoutActivity.class));
    }
}
