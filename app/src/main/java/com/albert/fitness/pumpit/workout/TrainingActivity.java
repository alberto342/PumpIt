package com.albert.fitness.pumpit.workout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.new_adapter.TrainingAdapter;
import com.albert.fitness.pumpit.model.Exercise;
import com.albert.fitness.pumpit.model.Training;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;
import com.albert.fitness.pumpit.viewmodel.WelcomeActivityViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.databinding.ActivityTrainingBinding;

public class TrainingActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TrainingAdapter trainingAdapter;
    private List<Training> trainingList = new ArrayList<>();
    private final String TAG = "TrainingActivity";
    private List<Exercise> exerciseList = new ArrayList<>();
    private WelcomeActivityViewModel activityViewModel;
    private CustomPlanViewModel planViewModel;


    // TODO: 2019-10-17 new with move for delete not remove exercise from the screen only on the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTrainingBinding activityTrainingBinding = DataBindingUtil.setContentView(this, R.layout.activity_training);
        setTitle("Training");
        mRecyclerView = activityTrainingBinding.rvTraining;
        planViewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);
        activityViewModel = ViewModelProviders.of(this).get(WelcomeActivityViewModel.class);
        getTraining();
        //itemTouchHelper();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom_add_exercise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_custom_add_exercise) {
            startActivity(new Intent(TrainingActivity.this, AddExerciseActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getTraining() {
        PrefsUtils prefsUtils = new PrefsUtils(this, PrefsUtils.EXERCISE);
        int id = prefsUtils.getInt("workoutId", 0);

        Log.d(TAG, "getTraining id: " + id);

        planViewModel.getTrainingByWorkoutId(id).observe(this, new Observer<List<Training>>() {
            @Override
            public void onChanged(List<Training> trainings) {
                if (trainings.isEmpty()) {
                    Log.d(TAG, "getTraining is empty");
                } else {
                    trainingList = trainings;
                    for (Training t : trainings) {
                        getExercise(t.getExerciseId());
                        Log.d(TAG, "getTraining ExerciseId: " + t.getExerciseId());
                    }
                }
            }
        });
    }

    private void getExercise(int exerciseId) {
        Log.d(TAG, "getExercise: " + exerciseId);
        activityViewModel.getExerciseById(exerciseId).observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                if (exercise != null) {
                    Log.d(TAG, "getExercise: " + exercise.getExerciseName());
                    exerciseList.add(new Exercise(exercise.getExerciseName(), exercise.getImgName()));
                    initRecyclerView();
                }
            }
        });
    }


    // TODO: 2019-10-16 need to fix it onMove change the position of the training


    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        trainingAdapter = new TrainingAdapter(exerciseList);
        trainingAdapter.setItems((ArrayList<Training>) trainingList);
        mRecyclerView.setAdapter(trainingAdapter);
        Log.d(TAG, "initRecyclerView: init recyclerView" + mRecyclerView);
        trainingAdapter.setListener(new TrainingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Training item, int i) {
                Log.d(TAG, "onClick: " + exerciseList.get(i).getExerciseName());
                Intent intent = new Intent(TrainingActivity.this, ShowExerciseImgActivity.class);
                intent.putExtra("exerciseName", exerciseList.get(i).getExerciseName());
                intent.putExtra("imgName", exerciseList.get(i).getImgName());
                startActivity(intent);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(trainingList, from, to);
                trainingAdapter.notifyItemMoved(from, to);
                return true;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                planViewModel.deleteTraining(trainingList.get(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(mRecyclerView);
    }


//    public void initRecyclerView() {
//        @SuppressLint("WrongConstant")
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(layoutManager);
//        trainingAdapter = new TrainingAdapter(this, trainingList, exerciseList);
//        trainingAdapter.notifyDataSetChanged();
//        mRecyclerView.setAdapter(trainingAdapter);
//        Log.d(TAG, "initRecyclerView: init recyclerView" + mRecyclerView);
//    }
}
