package fitness.albert.com.pumpit.WelcomeActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.Model.UserRegister;

public class GenderActivity extends AppCompatActivity {

    private SharedPreferences SPSaveTheCounter;
    private SharedPreferences.Editor editor;

    private ImageView female;
    private ImageView male;
    private boolean isMale = false;

    private UserRegister user = new UserRegister();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        getSupportActionBar().hide();

        init();
//        this.user.getSharedPreferencesFiles();
        createSharedPreferencesFiles();
    }


    private void init() {
        male = findViewById(R.id.btn_male);
        female = findViewById(R.id.btn_female);

        male.setOnClickListener(onClickListener);
        female.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_female:
                    isMale = false;
//                    user.saveData("isMale", isMale);
                    sharedPreferencesSaveData("isMale", isMale);
                    nextActivity();
                    break;

                case R.id.btn_male:
                    isMale = true;
                    sharedPreferencesSaveData("isMale", isMale);
                    nextActivity();
                    break;
            }
        }
    };


    private void nextActivity() {
        startActivity(new Intent(GenderActivity.this, AgeActivity.class));
    }

    @SuppressLint("WrongConstant")
    private void createSharedPreferencesFiles() {
        SPSaveTheCounter = getSharedPreferences("userInfo",MODE_NO_LOCALIZED_COLLATORS);
    }

    private void sharedPreferencesSaveData(String key, Boolean isMale) {
        editor = SPSaveTheCounter.edit();
        try {
            editor.putBoolean(key, isMale);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }
}
