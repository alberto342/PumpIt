package fitness.albert.com.pumpit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fitness.albert.com.pumpit.Model.SavePref;
import fitness.albert.com.pumpit.fragment.LogsFragment;
import fitness.albert.com.pumpit.fragment.NutritionFragment;
import fitness.albert.com.pumpit.fragment.PlanFragment;
import fitness.albert.com.pumpit.fragment.WorkoutFragment;
import fitness.albert.com.pumpit.fragment.profile.ProfileFragment;

public class FragmentNavigationActivity extends AppCompatActivity {


    private ActionBar toolbar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private NutritionFragment nutritionFragment;
    private PlanFragment planFragment;
    private WorkoutFragment workoutFragment;
    private LogsFragment logsFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_navigation);

        toolbar = getSupportActionBar();
        toolbar.setTitle("Workout");

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        //get current user
        getCurrentUser();

        nutritionFragment = new NutritionFragment();
        planFragment = new PlanFragment();
        workoutFragment = new WorkoutFragment();
        logsFragment = new LogsFragment();
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
                        transaction.replace(R.id.frame_container, nutritionFragment).commit();
                        return true;

                    case R.id.navigation_plan:
                        toolbar.setTitle("Plan");
                        transaction.replace(R.id.frame_container, planFragment).commit();
                        return true;

                    case R.id.navigation_workout:
                        toolbar.setTitle("Workout / Nutrition Plan");
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
        if (prevActivity.equals("ShowFoodBeforeAddedActivity")) {
            SavePref savePref = new SavePref();
            savePref.removeAll(this, "activity");
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
