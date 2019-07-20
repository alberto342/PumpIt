package fitness.albert.com.pumpit.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import fitness.albert.com.pumpit.fragment.FragmentNavigationActivity;
import fitness.albert.com.pumpit.LoginActivity;
import fitness.albert.com.pumpit.model.PrefsUtils;
import fitness.albert.com.pumpit.model.UserRegister;
import fitness.albert.com.pumpit.R;

public class GoalActivity extends AppCompatActivity {

    private PrefsUtils prefsUtils = new PrefsUtils();

    private ImageView loseWeight;
    private ImageView getFitter;
    private ImageView gainMuscle;
    private ImageView signIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        getSupportActionBar().hide();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        prefsUtils.createSharedPreferencesFiles(this, UserRegister.SharedPreferencesFile);

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
                String programSelected = "program_selected";
                switch (v.getId()) {
                    case R.id.btn_lose_weight:
                        prefsUtils.saveData(programSelected, "lose_weight");
                        nextActivity();
                        break;

                    case R.id.btn_gain_muscle:
                        prefsUtils.saveData(programSelected, "gain_muscle");
                        nextActivity();
                        break;

                    case R.id.btn_get_fitter:
                        prefsUtils.saveData(programSelected, "get_fitter");
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
}
