package com.albert.fitness.pumpit.workout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.albert.fitness.pumpit.helper.ImageUtils;
import com.albert.fitness.pumpit.model.Exercise;
import com.albert.fitness.pumpit.viewmodel.WelcomeActivityViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fitness.albert.com.pumpit.R;

public class CustomAddExerciseActivity extends AppCompatActivity implements ImageUtils.ImageAttachmentListener {

    private final String TAG = "CustomAddExerciseActivity";
    private EditText exerciseName, descriptionExercise;
    private Spinner spMuscleGroup, spMuscleGroup2;
    private ImageView imageViewLoad;
    private ImageUtils imageutils;
    private String imgName;
    private boolean isSecondaryCategory;
    private int secondaryId = 0;
    private int categoryId = 0;
    private WelcomeActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_add_exercise);
        viewModel = ViewModelProviders.of(this).get(WelcomeActivityViewModel.class);
        imageutils = new ImageUtils(this);
        init();
        Objects.requireNonNull(getSupportActionBar()).hide();
        setSpinner();
    }

    private void init() {
        exerciseName = findViewById(R.id.et_custom_add_exercise_name);
        spMuscleGroup = findViewById(R.id.sp_custom_exercise_muscle_group);
        spMuscleGroup2 = findViewById(R.id.sp_custom_exercise_muscle_group2);
        descriptionExercise = findViewById(R.id.et_custom_add_exercise_exercise_description);
        ImageView btnCreateExercise = findViewById(R.id.btn_create_my_exercise);
        imageViewLoad = findViewById(R.id.iv_loader_exercise);

        btnCreateExercise.setOnClickListener(v -> saveDate());
        imageViewLoad.setOnClickListener(v -> {
            if (editTxtIsEmpty(exerciseName.getText().toString(), descriptionExercise.getText().toString())) {
                imageutils.imagepicker(1);
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
        imageutils.getImage(imgName, path);
        Log.d(TAG, "Success img uploaded: " + path);
    }


    private void setSpinner() {
        List<String> muscleGroupList = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        String categoryType = "";
        if (extras != null) {
            categoryType = extras.getString("category", "");

           if(!categoryType.isEmpty()) {
               muscleGroupList.add(0, categoryType);
               muscleGroupList.add(categoryType);
           }
        }

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

        for (int i = 0; i < muscleGroupList.size(); i++) {
            if(muscleGroupList.get(i).equals(muscleGroupList.get(0))){
                muscleGroupList.remove(i);
            }
        }

        ArrayAdapter<String> daysWeekAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, muscleGroupList);
        daysWeekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMuscleGroup.setAdapter(daysWeekAdapter);
        spMuscleGroup2.setAdapter(daysWeekAdapter);
    }

    @SuppressLint("LongLogTag")
    private void saveDate() {
        String exerciseName = this.exerciseName.getText().toString();
        String exerciseDescription = descriptionExercise.getText().toString();
        String muscleGroup = spMuscleGroup.getSelectedItem().toString();
        String muscleGroup2 = spMuscleGroup2.getSelectedItem().toString();

        getCategoryId(muscleGroup);

        if (categoryId != 0) {
            viewModel.setExercise(new Exercise(exerciseName, imgName, categoryId, secondaryId));
        }

        Log.d(TAG, "Success saved ");
        //startActivity(new Intent(CustomAddExerciseActivity.this, ShowExerciseResultActivity.class));
        finish();
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

    private void getCategoryId(String category) {
        switch (category) {
            case "Abs":
                isSecondaryCategory = false;
                categoryId = 1;
            case "Back":
                isSecondaryCategory = false;
                categoryId = 8;
            case "Biceps":
                isSecondaryCategory = true;
                categoryId = 6;
                secondaryId = 2;
            case "Cardio":
                categoryId = 4;
                isSecondaryCategory = false;
            case "Calf":
            case "Thigh":
                isSecondaryCategory = true;
                categoryId = 5;
                secondaryId = 5;
            case "Chest":
                isSecondaryCategory = false;
                categoryId = 7;
            case "Forearm":
                isSecondaryCategory = true;
                secondaryId = 4;
                categoryId = 6;
            case "Glutes":
                isSecondaryCategory = true;
                secondaryId = 7;
                categoryId = 5;
            case "Shoulder":
                isSecondaryCategory = false;
                categoryId = 3;
            case "Stretch":
                isSecondaryCategory = false;
                categoryId = 2;
            case "Triceps":
                isSecondaryCategory = true;
                secondaryId = 3;
                categoryId = 6;
        }
    }
}
