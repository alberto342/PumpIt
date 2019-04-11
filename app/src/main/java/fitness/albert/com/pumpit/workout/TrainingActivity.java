package fitness.albert.com.pumpit.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import fitness.albert.com.pumpit.R;

public class TrainingActivity extends AppCompatActivity {

    private ImageView btnAddExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        init();
        onClick();
    }

    private void init() {
        btnAddExercise = findViewById(R.id.btn_add_exercise);
    }

    private void onClick() {
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrainingActivity.this, AddExerciseActivity.class));
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_edit_training);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
