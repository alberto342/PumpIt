package fitness.albert.com.pumpit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        getSupportActionBar().hide();
    }
}
