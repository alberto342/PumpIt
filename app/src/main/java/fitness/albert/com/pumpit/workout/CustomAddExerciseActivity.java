package fitness.albert.com.pumpit.workout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.helper.ImageUtils;
import fitness.albert.com.pumpit.model.CustomExerciseName;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CustomAddExerciseActivity extends AppCompatActivity implements ImageUtils.ImageAttachmentListener {

    private final String TAG = "CustomAddExerciseActivity";
    private Realm realm;
    private EditText exerciseName, descriptionExercise;
    private Spinner spMuscleGroup, spMuscleGroup2;
    private ImageView imageViewLoad;
    private ImageUtils imageutils;
    private String imgName;
    private String imgPatch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_add_exercise);

        imageutils = new ImageUtils(this);

        init();

        Objects.requireNonNull(getSupportActionBar()).hide();

        //SETUP REEALM
        RealmConfiguration config = new RealmConfiguration.Builder().name(CustomExerciseName.REALM_FILE_EXERCISE).deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(config);

        setSpinner();
    }

    private void init() {
        exerciseName = findViewById(R.id.et_custom_add_exercise_name);
        spMuscleGroup = findViewById(R.id.sp_custom_exercise_muscle_group);
        spMuscleGroup2 = findViewById(R.id.sp_custom_exercise_muscle_group2);
        descriptionExercise = findViewById(R.id.et_custom_add_exercise_exercise_description);
        ImageView btnCreateExercise = findViewById(R.id.btn_create_my_exercise);
        imageViewLoad = findViewById(R.id.iv_loader_exercise);

        btnCreateExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDateIntoRealm();
            }
        });
        imageViewLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTxtIsEmpty(exerciseName.getText().toString(), descriptionExercise.getText().toString())) {
                    imageutils.imagepicker(1);
                }
            }
        });
    }


    // The is 3 methods is for upload image into the exercise
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageutils.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {

        // Bitmap bitmap = file;
        //String file_name = filename;
        imgName = exerciseName.getText().toString().toLowerCase();
        imgName = imgName.replace(" ", "_");
        imgName = imgName + ".jpg";

        imageViewLoad.setImageBitmap(file);

        String path = Environment.getExternalStorageDirectory() + File.separator + "Pumpit/ExercisePictures" + File.separator;
        imageutils.createImage(file, imgName, path, false);

        imgPatch = path;

        imageutils.getImage(imgName, path);

       // imageViewLoad.setImageBitmap(imageutils.getImage(imgName, path));

        Log.d(TAG, "Success img uploaded: " + path);
    }



    private void setSpinner() {
        List<String> muscleGroupList = new ArrayList<>();
        muscleGroupList.add("Abs");
        muscleGroupList.add("Back");
        muscleGroupList.add("Biceps");
        muscleGroupList.add("Cardio");
        muscleGroupList.add("Calf");
        muscleGroupList.add("Chest");
        muscleGroupList.add("Forearm");
        muscleGroupList.add("Glutes");
        muscleGroupList.add("Shoulder");
        muscleGroupList.add("Stretch");
        muscleGroupList.add("Triceps");
        muscleGroupList.add("Thigh");

        ArrayAdapter<String> daysWeekAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, muscleGroupList);
        daysWeekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMuscleGroup.setAdapter(daysWeekAdapter);
        spMuscleGroup2.setAdapter(daysWeekAdapter);
    }

    @SuppressLint("LongLogTag")
    private void saveDateIntoRealm() {
        String exercise = exerciseName.getText().toString();
        String exerciseDescription = descriptionExercise.getText().toString();
        String muscleGroup = spMuscleGroup.getSelectedItem().toString();
        String muscleGroup2 = spMuscleGroup2.getSelectedItem().toString();


        //spMuscleGroup.getSelectedItem().equals(AddExerciseActivity.categorySelected);
        //spMuscleGroup2.getSelectedItem().equals(AddExerciseActivity.category2Selected);


        final CustomExerciseName customExerciseName = new CustomExerciseName(exercise, muscleGroup, muscleGroup2, exerciseDescription, imgName, imgPatch);

        if (editTxtIsEmpty(exercise, exerciseDescription)) {

            realm.getSchema();

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {

                    Number currentIdNum = realm.where(CustomExerciseName.class).max("id");
                    int nextId;

                    if(currentIdNum == null) {
                        nextId = 1;
                    } else {
                        nextId = currentIdNum.intValue() + 1;
                    }

                    customExerciseName.setId(nextId);

                    realm.insertOrUpdate(customExerciseName);

                    Log.d(TAG,"Success saved into realm");
                    startActivity(new Intent(CustomAddExerciseActivity.this, ShowExerciseResult.class));
                    finish();
                }
            });
        }
    }

    private boolean editTxtIsEmpty(String exercise, String exerciseDescription) {
        if (exercise.isEmpty()) {
            setError(exerciseName, "Enter Name of Exercise");
            return false;
        }
        if (exerciseDescription.isEmpty()) {
            setError(descriptionExercise, "Enter description of Exercise");
            return false;
        }
        return true;
    }

    private void setError(EditText editText, String errorTxt) {
        editText.setError(errorTxt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
