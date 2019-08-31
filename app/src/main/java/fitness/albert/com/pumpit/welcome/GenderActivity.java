package fitness.albert.com.pumpit.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.model.PrefsUtils;

public class GenderActivity extends AppCompatActivity {

    private PrefsUtils prefsUtils = new PrefsUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        getSupportActionBar().hide();
        init();
        prefsUtils.createSharedPreferencesFiles(this, PrefsUtils.SETTINGS_PREFERENCES_FILE);
    }


    private void init() {
        ImageView male = findViewById(R.id.btn_male);
        ImageView female = findViewById(R.id.btn_female);
        male.setOnClickListener(onClickListener);
        female.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_female:
                    prefsUtils.saveData("is_male", false);
                    nextActivity();
                    break;
                case R.id.btn_male:
                    prefsUtils.saveData("is_male", true);
                    nextActivity();
                    break;
            }
        }
    };


    private void nextActivity() {
        startActivity(new Intent(GenderActivity.this, AgeActivity.class));
    }


}
