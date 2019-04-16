package fitness.albert.com.pumpit.workout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;

import fitness.albert.com.pumpit.Adapter.ExerciseAdapter.ExerciseAdapter;
import fitness.albert.com.pumpit.R;

public class ExerciseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBar toolbar;
    private ImgExerciseFragment imgExerciseFragment;
    private InstructionsExerciseFragment instructionsExerciseFragment;
    private TrackerExerciseFragment trackerExerciseFragment;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "ExerciseDetailActivity";
    private ImageView imgFavorite;
    public static boolean isFavoriteSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exercise_detail);

        toolbar = getSupportActionBar();
        assert toolbar != null;
        toolbar.setTitle(ExerciseAdapter.exerciseName);

        imgFavorite = findViewById(R.id.iv_add_favorite);
        imgFavorite.setOnClickListener(this);

        imgExerciseFragment = new ImgExerciseFragment();
        instructionsExerciseFragment = new InstructionsExerciseFragment();
        trackerExerciseFragment = new TrackerExerciseFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_workout_tracker, imgExerciseFragment).commit();

        setTheFragmentSwitch();
    }


    private void setTheFragmentSwitch() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_exercise_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()) {
                    case R.id.nav_img_exercise:
                        toolbar.setTitle(ExerciseAdapter.exerciseName);
                        transaction.replace(R.id.container_workout_tracker, imgExerciseFragment).commit();
                        return true;

                    case R.id.nav_instructions:
                        toolbar.setTitle("Instructions");
                        transaction.replace(R.id.container_workout_tracker, instructionsExerciseFragment).commit();
                        return true;

                    case R.id.nav_set_peps:
                        toolbar.setTitle("Workout Tracker");
                        transaction.replace(R.id.container_workout_tracker, trackerExerciseFragment).commit();
                        return true;
                }
                return true;
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_add_favorite:
                if (!isFavoriteSelected) {
                    imgFavorite.setImageResource(R.mipmap.ic_star_black);
                    isFavoriteSelected = true;
                } else {
                    imgFavorite.setImageResource(R.mipmap.ic_star_white);
                    isFavoriteSelected = false;
                }
                break;
        }
    }


    //method for save exercise and favorite, if selected, need to see if save button is click
//    private void saveDataIntoFb() {
//
//        final SavePref savePref = new SavePref();
//
//        List<Training> trainingList = new ArrayList<>();
//
//        Log.d(TAG, "Get SharedPreferencesFiles  - exercise");
//
//        savePref.createSharedPreferencesFiles(this, "exercise");
//        String getPlanId = savePref.getString("planName", "");
//
//
//        // Training training = new Training(ExerciseAdapter.exerciseName,0,0,20, ExerciseAdapter.exerciseImg, UserRegister.getTodayData(),false, 5.6f);
//
//        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(Workout.WORKOUT_NAME).document(getPlanId).collection(Workout.WORKOUT).
//                document(WorkoutAdapter.workoutDayName).collection(ExerciseAdapter.exerciseName)
//                .add(trainingList).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentReference> task) {
//                Log.d(TAG, "DocumentSnapshot successfully written!");
//                savePref.removeSingle(ExerciseDetailActivity.this, "exercise", "planName");
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "Error writing document", e);
//                    }
//                });
//    }


    @Override
    public void onBackPressed() {
        finish();
    }
}