package fitness.albert.com.pumpit.WelcomeActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import fitness.albert.com.pumpit.LoginActivity;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.FragmentNavigationActivity;
import fitness.albert.com.pumpit.Model.UserRegister;

public class GoalActivity extends AppCompatActivity {

    private SharedPreferences SPSaveTheCounter;
    private SharedPreferences.Editor editor;

    private ImageView loseWeight;
    private ImageView getFitter;
    private ImageView gainMuscle;
    private ImageView signIn;
    private FirebaseAuth auth;

    private final String programSelected = "programSelected";
    private UserRegister user = new UserRegister();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        createSharedPreferencesFiles();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, FragmentNavigationActivity.class));
            finish();
        }

        findViews();
        setOnClickListener();
    }


    private void findViews() {
        loseWeight = findViewById(R.id.btn_lose_weight);
        gainMuscle = findViewById(R.id.btn_gain_muscle);
        getFitter = findViewById(R.id.btn_get_fitter);
        signIn = findViewById(R.id.btn_sign_in);
    }

    private void setOnClickListener() {
        loseWeight.setOnClickListener(onClickListener);
        gainMuscle.setOnClickListener(onClickListener);
        getFitter.setOnClickListener(onClickListener);
        signIn.setOnClickListener(onClickListener);
    }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_lose_weight:
                        sharedPreferencesSaveData(programSelected,"lose_weight");
                        nextActivity();
                        break;

                    case R.id.btn_gain_muscle:
                        sharedPreferencesSaveData(programSelected, "gain_muscle");
                        nextActivity();
                        break;

                    case R.id.btn_get_fitter:
                        sharedPreferencesSaveData(programSelected, "get_fitter");
                        nextActivity();
                        break;

                    case R.id.btn_sign_in:
                        signInActivity();
                        break;
                }
            }
        };

    private void nextActivity() {
        startActivity(new Intent(this, GenderActivity.class));
    }


    private void signInActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }


    @SuppressLint("WrongConstant")
    private void createSharedPreferencesFiles() {
        SPSaveTheCounter = getSharedPreferences("userInfo",MODE_NO_LOCALIZED_COLLATORS);
    }

    private void sharedPreferencesSaveData(String key, String stringObj) {
        editor = SPSaveTheCounter.edit();
        try {
            editor.putString(key, stringObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }
}
