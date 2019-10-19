package com.albert.fitness.pumpit.workout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.albert.fitness.pumpit.utils.PrefsUtils;

import java.util.Objects;

import fitness.albert.com.pumpit.R;

public class AddExerciseActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView btnBack, btnFront, imgFace;
    private TextView chest, shoulder, abs, thigh, allExercise, biceps, forearm, cardio, stretch;
    private TextView triceps, glutes, back, calf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        Objects.requireNonNull(getSupportActionBar()).hide();

        init();
        onClickListener();
    }

    private void init() {
        imgFace = findViewById(R.id.iv_img_face);
        btnBack = findViewById(R.id.btn_back);
        btnFront = findViewById(R.id.btn_front);
        chest = findViewById(R.id.tv_chest);
        shoulder = findViewById(R.id.tv_shoulder);
        abs = findViewById(R.id.tv_abs);
        thigh = findViewById(R.id.tv_thigh);
        allExercise = findViewById(R.id.tv_all);
        biceps = findViewById(R.id.tv_biceps);
        forearm = findViewById(R.id.tv_forearm);
        cardio = findViewById(R.id.tv_cardio);
        triceps = findViewById(R.id.tv_triceps);
        glutes = findViewById(R.id.tv_glutes);
        back = findViewById(R.id.tv_back);
        calf = findViewById(R.id.tv_calf);
        stretch = findViewById(R.id.tv_stretch);
    }


    private void onClickListener() {
        imgFace.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnFront.setOnClickListener(this);
        chest.setOnClickListener(this);
        shoulder.setOnClickListener(this);
        abs.setOnClickListener(this);
        thigh.setOnClickListener(this);
        allExercise.setOnClickListener(this);
        biceps.setOnClickListener(this);
        forearm.setOnClickListener(this);
        cardio.setOnClickListener(this);
        triceps.setOnClickListener(this);
        glutes.setOnClickListener(this);
        back.setOnClickListener(this);
        calf.setOnClickListener(this);
        stretch.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        Intent intent = new Intent(this, ShowExerciseResultActivity.class);

        switch (id) {
            case R.id.btn_back:
                imgFace.setImageResource(R.mipmap.back_view_exercise);
                btnFront.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.INVISIBLE);

                chest.setVisibility(View.INVISIBLE);
                abs.setVisibility(View.INVISIBLE);
                thigh.setVisibility(View.INVISIBLE);
                biceps.setVisibility(View.INVISIBLE);
                forearm.setVisibility(View.INVISIBLE);

                triceps.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                calf.setVisibility(View.VISIBLE);
                glutes.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_front:
                imgFace.setImageResource(R.mipmap.front_view_exercise);
                btnFront.setVisibility(View.INVISIBLE);
                btnBack.setVisibility(View.VISIBLE);

                chest.setVisibility(View.VISIBLE);
                abs.setVisibility(View.VISIBLE);
                thigh.setVisibility(View.VISIBLE);
                biceps.setVisibility(View.VISIBLE);
                forearm.setVisibility(View.VISIBLE);

                triceps.setVisibility(View.INVISIBLE);
                back.setVisibility(View.INVISIBLE);
                calf.setVisibility(View.INVISIBLE);
                glutes.setVisibility(View.INVISIBLE);
                break;

            case R.id.tv_chest:
                intent.putExtra("exerciseType", "Chest");
                intent.putExtra("category", 7);
                intent.putExtra("category2", 1);
                startActivity(intent);
                break;

            case R.id.tv_shoulder:
                intent.putExtra("exerciseType", "Shoulder");
                intent.putExtra("category", 3);
                intent.putExtra("category2", 1);
                startActivity(intent);
                break;

            case R.id.tv_abs:
                intent.putExtra("exerciseType", "Abs");
                intent.putExtra("category", 1);
                intent.putExtra("category2", 1);
                startActivity(intent);
                break;

            case R.id.tv_thigh:
                intent.putExtra("exerciseType", "Thigh");
                intent.putExtra("category", 5);
                intent.putExtra("category2", 1);
                startActivity(intent);
                break;

            case R.id.tv_all:
                intent.putExtra("exerciseType", "All");
                intent.putExtra("category", 0);
                intent.putExtra("category2", 1);
                startActivity(intent);
                break;

            case R.id.tv_biceps:
                intent.putExtra("exerciseType", "Biceps");
                intent.putExtra("category", 6);
                intent.putExtra("category2", 2);
                startActivity(intent);
                break;

            case R.id.tv_forearm:
                intent.putExtra("exerciseType", "Forearm");
                intent.putExtra("category", 6);
                intent.putExtra("category2", 4);
                startActivity(intent);
                break;

            case R.id.tv_cardio:
                intent.putExtra("exerciseType", "Cardio");
                intent.putExtra("category", 4);
                intent.putExtra("category2", 1);
                startActivity(intent);
                break;

            case R.id.tv_triceps:
                intent.putExtra("exerciseType", "Triceps");
                intent.putExtra("category", 6);
                intent.putExtra("category2", 3);
                startActivity(intent);
                break;

            case R.id.tv_glutes:
                intent.putExtra("exerciseType", "Glutes");
                intent.putExtra("category", 5);
                intent.putExtra("category2", 7);
                startActivity(intent);
                break;

            case R.id.tv_back:
                intent.putExtra("exerciseType", "Back");
                intent.putExtra("category", 8);
                intent.putExtra("category2", 1);
                startActivity(intent);
                break;

            case R.id.tv_calf:
                intent.putExtra("exerciseType", "Calf");
                intent.putExtra("category", 5);
                intent.putExtra("category2", 5);
                startActivity(intent);
                break;

            case R.id.tv_stretch:
                intent.putExtra("exerciseType", "Stretch");
                intent.putExtra("category", 2);
                intent.putExtra("category2", 1);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PrefsUtils prefsUtils = new PrefsUtils();
        prefsUtils.removeSingle(this, PrefsUtils.START_WORKOUT, "activity");
    }
}
