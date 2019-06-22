package fitness.albert.com.pumpit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fitness.albert.com.pumpit.Model.PrefsUtils;
import fitness.albert.com.pumpit.fragment.NutritionFragment;
import fitness.albert.com.pumpit.fragment.PlanFragment;
import fitness.albert.com.pumpit.fragment.WorkoutFragment;
import fitness.albert.com.pumpit.fragment.logsFragment.LogFragment;
import fitness.albert.com.pumpit.fragment.profile.ProfileFragment;

public class FragmentNavigationActivity extends AppCompatActivity {

    private ActionBar toolbar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private NutritionFragment nutritionFragment;
    private PlanFragment planFragment;
    private WorkoutFragment workoutFragment;
    private LogFragment logsFragment;
    private ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_navigation);

        toolbar = getSupportActionBar();
        assert toolbar != null;
        toolbar.setTitle("Workout");

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        getCurrentUser();

        nutritionFragment = new NutritionFragment();
        planFragment = new PlanFragment();
        workoutFragment = new WorkoutFragment();
        logsFragment = new LogFragment();
        profileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, workoutFragment).commit();

        setTheFragmentSwitch();
    }

    private void getCurrentUser() {
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(FragmentNavigationActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    private void setTheFragmentSwitch() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()) {
                    case R.id.navigation_nutrition:
                        toolbar.setTitle("Nutrition");
                        nutritionFragment = new NutritionFragment();
                        transaction.replace(R.id.frame_container, nutritionFragment).commit();
                        return true;

                    case R.id.navigation_plan:
                        toolbar.setTitle("Plan");
                        transaction.replace(R.id.frame_container, planFragment).commit();
                        return true;

                    case R.id.navigation_workout:
                        toolbar.setTitle("Workout");
                        transaction.replace(R.id.frame_container, workoutFragment).commit();
                        return true;

                    case R.id.navigation_logs:
                        toolbar.setTitle("Logs");
                        transaction.replace(R.id.frame_container, logsFragment).commit();
                        return true;

                    case R.id.navigation_profile:
                        toolbar.setTitle("Profile");
                        transaction.replace(R.id.frame_container, profileFragment).commit();
                        return true;
                }
                return true;
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("activity", Context.MODE_PRIVATE);
        String prevActivity = pref.getString("FROM_ACTIVITY", "");

        //check if activity come from ShowFoodBeforeAddedActivity
        assert prevActivity != null;
        if (prevActivity.equals("ShowFoodBeforeAddedActivity")) {
            PrefsUtils prefsUtils = new PrefsUtils();
            prefsUtils.removeAll(this, "activity");
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, nutritionFragment).commit();
        }
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }


}
