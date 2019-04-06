package fitness.albert.com.pumpit.workout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fitness.albert.com.pumpit.Adapter.ExerciseAdapter.ExerciseAdapter;
import fitness.albert.com.pumpit.R;

public class ShowExerciseItemActivity extends AppCompatActivity {

    private ImageView exerciseImg, favorite, log, plan;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exercise_item);

        init();
    }

    private void init() {
        exerciseImg = findViewById(R.id.iv_exercise_img);
        favorite = findViewById(R.id.iv_add_favorite);
        log = findViewById(R.id.iv_log);
        plan = findViewById(R.id.iv_add_plan);
        name = findViewById(R.id.tv_name);

        name.setText(ExerciseAdapter.exerciseName);

        try {
            String imgFile = "file:///android_asset/images/" + ExerciseAdapter.exerciseImg;

            Glide.with(this)
                    .asGif()
                    .load(imgFile)
                    .into(exerciseImg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
