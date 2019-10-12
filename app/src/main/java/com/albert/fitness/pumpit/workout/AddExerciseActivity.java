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
    public static String categorySelected;
    public static String category2Selected;

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
                categorySelected = "Chest";
                category2Selected = "null";
                startActivity(intent);
                break;

            case R.id.tv_shoulder:
                intent.putExtra("exerciseType", "Shoulders");
                categorySelected = "Shoulders";
                category2Selected = "null";
                startActivity(intent);
                break;

            case R.id.tv_abs:
                intent.putExtra("exerciseType", "Abs");
                categorySelected = "Abs";
                category2Selected = "null";
                startActivity(intent);
                break;

            case R.id.tv_thigh:
                intent.putExtra("exerciseType", "Thigh");
                categorySelected = "Legs";
                category2Selected = "null";
                startActivity(intent);
                break;

            case R.id.tv_all:
                intent.putExtra("exerciseType", "All Exercises");
                categorySelected = "All";
                category2Selected = "null";
                startActivity(intent);
                break;

            case R.id.tv_biceps:
                intent.putExtra("exerciseType", "Biceps");
                categorySelected = "Arms";
                category2Selected = "biceps";
                startActivity(intent);
                break;

            case R.id.tv_forearm:
                intent.putExtra("exerciseType", "Forearms");
                categorySelected = "Arms";
                category2Selected = "forearms";
                startActivity(intent);
                break;

            case R.id.tv_cardio:
                intent.putExtra("exerciseType", "Cardio");
                categorySelected = "Cardio";
                category2Selected = "null";
                startActivity(intent);
                break;

            case R.id.tv_triceps:
                intent.putExtra("exerciseType", "Triceps");
                categorySelected = "Arms";
                category2Selected = "triceps";
                startActivity(intent);
                break;

            case R.id.tv_glutes:
                intent.putExtra("exerciseType", "Glutes");
                categorySelected = "Legs";
                category2Selected = "glutes";
                startActivity(intent);
                break;

            case R.id.tv_back:
                intent.putExtra("exerciseType", "Back");
                categorySelected = "Back";
                category2Selected = "null";
                startActivity(intent);
                break;

            case R.id.tv_calf:
                intent.putExtra("exerciseType", "Calf");
                categorySelected = "Legs";
                category2Selected = "calf";
                startActivity(intent);
                break;

            case R.id.tv_stretch:
                intent.putExtra("exerciseType", "Stretch");
                categorySelected = "Stretch";
                category2Selected = "null";
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
