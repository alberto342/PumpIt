package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.NewTrackerExerciseAdapter;
import com.albert.fitness.pumpit.model.TrackerExercise;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;
import com.albert.fitness.pumpit.viewmodel.WelcomeActivityViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;

public class EditTrackerExercise extends AppCompatActivity {
    private static final String TAG = "EditTrackerExercise";
    private ActionBar toolbar;
    private ImageView exerciseImg;
    private EditText etRestBetweenSet, etSecRest, etWeight, etReps;
    private TextView tvWeight;
    private Button btnAddTracker;
    private RecyclerView mRecyclerView;
    private CustomPlanViewModel viewModel;
    private WelcomeActivityViewModel activityViewModel;
    private List<TrackerExercise> trackerExerciseList = new ArrayList<>();
    private List<TrackerExercise> deletedItem = new ArrayList<>();
    private boolean isWeightGone;
    private int exerciseId, trainingId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tracker_exercise);
        toolbar = getSupportActionBar();
        assert toolbar != null;
        viewModel = ViewModelProviders.of(this).get(CustomPlanViewModel.class);
        activityViewModel = ViewModelProviders.of(this).get(WelcomeActivityViewModel.class);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();
        receiveDate();
        setTrackerExercise();
    }


    private void initView() {
        exerciseImg = findViewById(R.id.exercise_img_tracker);
        View layoutTrackerExercise = findViewById(R.id.ly_edit_tracker);
        etRestBetweenSet = layoutTrackerExercise.findViewById(R.id.et_rest_between_sets);
        etSecRest = layoutTrackerExercise.findViewById(R.id.et_sec_rest_after_exercise);
        etWeight = layoutTrackerExercise.findViewById(R.id.et_set_weight);
        etReps = layoutTrackerExercise.findViewById(R.id.et_reps);
        btnAddTracker = layoutTrackerExercise.findViewById(R.id.btn_add_tracker);
        mRecyclerView = layoutTrackerExercise.findViewById(R.id.rv_tracker_exercise_ad);
        tvWeight = layoutTrackerExercise.findViewById(R.id.tv_weight_tx);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.m_save_tracker) {
            setData();
        }
        return true;
    }


    private void receiveDate() {
        Bundle extras = getIntent().getExtras();
        String imgName = null, exerciseName = null;
        int restBetweenSet = 0, restAfterExercise = 0;


        if (extras != null) {
            imgName = extras.getString("imgName");
            exerciseName = extras.getString("exerciseName");
            trainingId = extras.getInt("trainingId");
            exerciseId = extras.getInt("exerciseId");
            restBetweenSet = extras.getInt("restBetweenSet");
            restAfterExercise = extras.getInt("restAfterExercise");
        }

        if(restBetweenSet != 0 && restAfterExercise != 0 ) {
            etRestBetweenSet.setText(String.valueOf(restBetweenSet));
                etSecRest.setText(String.valueOf(restAfterExercise));
        }
        toolbar.setTitle(exerciseName);
        getExercise(exerciseId);
        getTrackerExercise(trainingId);


        try {
            String imgFile = "file:///android_asset/images/" + imgName;

            Glide.with(this)
                    .asGif()
                    .load(imgFile)

                    .listener(new RequestListener<GifDrawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<GifDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GifDrawable resource, Object model,
                                                       Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(exerciseImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getExercise(int exerciseId) {
        activityViewModel.getExerciseById(exerciseId)
                .observe(this, exercise -> {
                    if (exercise != null) {
                        if (exercise.getCategoryId() == 4 || exercise.getCategoryId() == 2) {
                            etWeight.setVisibility(View.GONE);
                            tvWeight.setVisibility(View.GONE);
                            isWeightGone = true;
                        }
                    }
                });
    }

    private void getTrackerExercise(int trainingId) {
        viewModel.getTrackerExerciseByTraining(trainingId)
                .observe(this, trackerExercises -> {
                    if (!trackerExercises.isEmpty()) {
                        trackerExerciseList.addAll(trackerExercises);
                        initRecyclerView();
                    }
                });
    }

    private void setTrackerExercise() {
        final int[] iReps = new int[1];
        final float[] fWeight = new float[1];

        btnAddTracker.setOnClickListener(v -> {
            //set error
            if (etWeight.getText().toString().isEmpty() && etReps.getText().toString().isEmpty() && !isWeightGone) {
                setError(etWeight, "Enter weight");
                setError(etReps, "Enter reps");
                return;
            }

            if (etWeight.getText().toString().isEmpty() && !isWeightGone) {
                setError(etWeight, "Enter weight");
                return;
            }
            if (etReps.getText().toString().isEmpty()) {
                setError(etReps, "Enter reps");
                return;
            }

            iReps[0] = Integer.parseInt(etReps.getText().toString());
            fWeight[0] = isWeightGone ? 0f : Float.valueOf(etWeight.getText().toString());

            if (iReps[0] == 0) {
                etReps.setText("");
                setError(etReps, "0 Not acceptable");
                return;
            }

            if (fWeight[0] == 0.0 && !isWeightGone) {
                etWeight.setText("");
                setError(etWeight, "0.0 Not acceptable");
                return;
            }


            //add into list
            trackerExerciseList.add(new TrackerExercise(iReps[0], fWeight[0], trainingId));
            initRecyclerView();
            etWeight.setText("");
            etReps.setText("");
        });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView " + trackerExerciseList);
        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        NewTrackerExerciseAdapter exerciseAdapter = new NewTrackerExerciseAdapter(trackerExerciseList, isWeightGone);
        mRecyclerView.setAdapter(exerciseAdapter);
        exerciseAdapter.setOnItemClickListener(((position, v) -> {
            if (v.getTag().equals(R.drawable.ic_delete_black)) {
                deletedItem.add(trackerExerciseList.get(position));
                exerciseAdapter.deleteItem(position);
            }
        }));
    }

    private void setError(EditText text, String errorMessage) {
        if (text.getText().toString().isEmpty()) {
            text.setError(errorMessage);
        }
    }


    private void setData() {
        int restBetweenSet = Integer.valueOf(etRestBetweenSet.getText().toString());
        int restAfter = Integer.valueOf(etSecRest.getText().toString());

        //      Add / Update Tracker Exercise
        for (int i = 0; i < trackerExerciseList.size(); i++) {
            if (trackerExerciseList.get(i).getTrackerId() == 0) {
                viewModel.addNewTracker(trackerExerciseList.get(i));
            } else {
                viewModel.updateTracker(trackerExerciseList.get(i));
            }
        }
        // Delete tracker
        for (int i = 0; i < deletedItem.size(); i++) {
            if (deletedItem.get(i).getTrackerId() > 0) {
                viewModel.deleteTrackerExerciseByTrackerId(deletedItem.get(i).getTrackerId());
            }
        }

        // Update Training rest time
        viewModel.updateTrainingRestExercise(restBetweenSet, restAfter, trainingId);

        Log.d(TAG, "onComplete: success saved  workout tracker");
        finish();
    }


}
