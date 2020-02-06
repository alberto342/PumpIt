package com.albert.fitness.pumpit.workout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fitness.albert.com.pumpit.R;

public class ExerciseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBar toolbar;
    private ImgExerciseFragment imgExerciseFragment;
    private InstructionsExerciseFragment instructionsExerciseFragment;
    private TrackerExerciseFragment trackerExerciseFragment;
    //private final String TAG = "ExerciseDetailActivity";
    private ImageView imgFavorite;
    public static boolean isFavoriteSelected = false;
    private String exerciseName, imgName;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exercise_detail);

        toolbar = getSupportActionBar();
        assert toolbar != null;
        getExerciseById();
        toolbar.setTitle(exerciseName);

        imgFavorite = findViewById(R.id.iv_add_favorite);
        imgFavorite.setOnClickListener(this);

        imgExerciseFragment = new ImgExerciseFragment();
        instructionsExerciseFragment = new InstructionsExerciseFragment();
        trackerExerciseFragment = new TrackerExerciseFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_workout_tracker, imgExerciseFragment).commit();
        setTheFragmentSwitch();
    }

    private void getExerciseById() {
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            id = b.getInt("exerciseId");
            exerciseName = b.getString("exerciseName");
            imgName = b.getString("imgUrl");
        }
    }

    public String getExerciseName() {
        return imgName;
    }


    public int getExerciseId() {
        return id;
    }


    private void setTheFragmentSwitch() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_exercise_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.nav_img_exercise:
                    toolbar.setTitle(exerciseName);
                    transaction.replace(R.id.container_workout_tracker, imgExerciseFragment).commit();
                    return true;

                case R.id.nav_instructions:
                    toolbar.setTitle("Instructions");
                    transaction.replace(R.id.container_workout_tracker, instructionsExerciseFragment).commit();
                    return true;

                case R.id.nav_set_peps:
                    toolbar.setTitle("Workout Tracker");
                    transaction.replace(R.id.container_workout_tracker, trackerExerciseFragment).commit();
                    return true;
            }
            return true;
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_add_favorite) {
            if (!isFavoriteSelected) {
                imgFavorite.setImageResource(R.mipmap.ic_star_black);
                isFavoriteSelected = true;
            } else {
                imgFavorite.setImageResource(R.mipmap.ic_star_white);
                isFavoriteSelected = false;
            }
        }
    }
}
