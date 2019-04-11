package fitness.albert.com.pumpit.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
        switch (id) {

            case  R.id.btn_back:
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

            case  R.id.btn_front:
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
                categorySelected = "Chest";
                category2Selected = "null";
                startActivity(new Intent(this, ShowExerciseResult.class));
                break;

            case R.id.tv_shoulder:
                categorySelected = "Shoulders";
                category2Selected = "null";
                startActivity(new Intent(this, ShowExerciseResult.class));
                break;

            case R.id.tv_abs:
                categorySelected = "Abs";
                category2Selected = "null";
                startActivity(new Intent(this, ShowExerciseResult.class));
                break;

            case R.id.tv_thigh:
                categorySelected = "Legs";
                category2Selected = "null";
                startActivity(new Intent(this, ShowExerciseResult.class));
                break;

            case R.id.tv_all:
                categorySelected = "All";
                category2Selected = "null";
                startActivity(new Intent(this, ShowExerciseResult.class));
                break;

            case R.id.tv_biceps:
                categorySelected = "Arms";
                category2Selected = "biceps";
                startActivity(new Intent(this, ShowExerciseResult.class));
                break;

            case R.id.tv_forearm:
                categorySelected = "Arms";
                category2Selected = "forearms";
                startActivity(new Intent(this, ShowExerciseResult.class));
                break;

            case R.id.tv_cardio:
                categorySelected = "Cardio";
                category2Selected = "null";
                startActivity(new Intent(this, ShowExerciseResult.class));
                break;

            case R.id.tv_triceps:
                categorySelected = "Arms";
                category2Selected = "triceps";
                startActivity(new Intent(this, ShowExerciseResult.class));
                break;

            case R.id.tv_glutes:
                categorySelected = "Legs";
                category2Selected = "glutes";
                startActivity(new Intent(this, ShowExerciseResult.class));
                break;

            case R.id.tv_back:
                categorySelected = "Back";
                category2Selected = "null";
                startActivity(new Intent(this, ShowExerciseResult.class));
                break;

            case R.id.tv_calf:
                categorySelected = "Legs";
                category2Selected = "calf";
                startActivity(new Intent(this, ShowExerciseResult.class));
                break;

            case R.id.tv_stretch:
                categorySelected = "Stretch";
                category2Selected = "null";
                startActivity(new Intent(this, ShowExerciseResult.class));
                break;
        }
    }
}
