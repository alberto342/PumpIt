package fitness.albert.com.pumpit.WelcomeActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import fitness.albert.com.pumpit.Model.PrefsUtils;
import fitness.albert.com.pumpit.Model.UserRegister;
import fitness.albert.com.pumpit.R;

public class GenderActivity extends AppCompatActivity {

    PrefsUtils prefsUtils = new PrefsUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        getSupportActionBar().hide();
        init();
        prefsUtils.createSharedPreferencesFiles(this, UserRegister.SharedPreferencesFile);
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
                    boolean isMale = false;
                    prefsUtils.saveData("isMale", isMale);
                    nextActivity();
                    break;

                case R.id.btn_male:
                    isMale = true;
                    prefsUtils.saveData("isMale", isMale);
                    nextActivity();
                    break;
            }
        }
    };


    private void nextActivity() {
        startActivity(new Intent(GenderActivity.this, AgeActivity.class));
    }


}
