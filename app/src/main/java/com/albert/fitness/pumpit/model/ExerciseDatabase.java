package com.albert.fitness.pumpit.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Database(entities = {ExerciseCategory.class, ExerciseSecondaryCategory.class, Exercise.class}, version = 1)
public abstract class ExerciseDatabase extends RoomDatabase {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public abstract ExerciseCategoryDAO exerciseCategoryDAO();

    public abstract ExerciseSecondaryCategoryDAO secondaryCategoryDAO();

    public abstract ExerciseDAO exerciseObjDAO();

    private static ExerciseDatabase instance;


    public static synchronized ExerciseDatabase getInstance(Context context) {
        if (instance == null) {
            mContext = context;
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ExerciseDatabase.class, "exercise_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    private static Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new InitialDataAsyncTask(instance).execute();
        }
    };

    private static class InitialDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private ExerciseCategoryDAO exerciseCategoryDAO;
        private ExerciseDAO exerciseObjDAO;
        private ExerciseSecondaryCategoryDAO secondaryCategoryDAO;

        public InitialDataAsyncTask(ExerciseDatabase exerciseDatabase) {
            exerciseCategoryDAO = exerciseDatabase.exerciseCategoryDAO();
            exerciseObjDAO = exerciseDatabase.exerciseObjDAO();
            secondaryCategoryDAO = exerciseDatabase.secondaryCategoryDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Exercise exerciseObj = new Exercise();

            ExerciseCategory exerciseCategory1 = new ExerciseCategory("Abs");
            ExerciseCategory exerciseCategory2 = new ExerciseCategory("Stretch");
            ExerciseCategory exerciseCategory3 = new ExerciseCategory("Shoulders");
            ExerciseCategory exerciseCategory4 = new ExerciseCategory("Cardio");
            ExerciseCategory exerciseCategory5 = new ExerciseCategory("Legs");
            ExerciseCategory exerciseCategory6 = new ExerciseCategory("Arms");
            ExerciseCategory exerciseCategory7 = new ExerciseCategory("Chest");
            ExerciseCategory exerciseCategory8 = new ExerciseCategory("Back");

            ExerciseSecondaryCategory secondaryCategory1 = new ExerciseSecondaryCategory("null");
            ExerciseSecondaryCategory secondaryCategory2 = new ExerciseSecondaryCategory("Biceps");
            ExerciseSecondaryCategory secondaryCategory3 = new ExerciseSecondaryCategory("Triceps");
            ExerciseSecondaryCategory secondaryCategory4 = new ExerciseSecondaryCategory("Forearms");
            ExerciseSecondaryCategory secondaryCategory5 = new ExerciseSecondaryCategory("Calf");
            ExerciseSecondaryCategory secondaryCategory6 = new ExerciseSecondaryCategory("Biceps & Triceps");
            ExerciseSecondaryCategory secondaryCategory7 = new ExerciseSecondaryCategory("Glutes");

            exerciseCategoryDAO.insert(exerciseCategory1);
            exerciseCategoryDAO.insert(exerciseCategory2);
            exerciseCategoryDAO.insert(exerciseCategory3);
            exerciseCategoryDAO.insert(exerciseCategory4);
            exerciseCategoryDAO.insert(exerciseCategory5);
            exerciseCategoryDAO.insert(exerciseCategory6);
            exerciseCategoryDAO.insert(exerciseCategory7);
            exerciseCategoryDAO.insert(exerciseCategory8);

            secondaryCategoryDAO.insert(secondaryCategory1);
            secondaryCategoryDAO.insert(secondaryCategory2);
            secondaryCategoryDAO.insert(secondaryCategory3);
            secondaryCategoryDAO.insert(secondaryCategory4);
            secondaryCategoryDAO.insert(secondaryCategory5);
            secondaryCategoryDAO.insert(secondaryCategory6);
            secondaryCategoryDAO.insert(secondaryCategory7);


            try {
                InputStream inputreaderImg = mContext.getAssets().open("img_name.txt");
                BufferedReader buffTypeImg = new BufferedReader(new InputStreamReader(inputreaderImg));

                InputStream inputreaderExercise = mContext.getAssets().open("exercise.txt");
                BufferedReader buffTypeExercise = new BufferedReader(new InputStreamReader(inputreaderExercise));

                InputStream inputreaderCategory = mContext.getAssets().open("category.txt");
                BufferedReader buffTypeCategory = new BufferedReader(new InputStreamReader(inputreaderCategory));

                InputStream inputreaderCategory2 = mContext.getAssets().open("category2.txt");
                BufferedReader buffTypeCategory2 = new BufferedReader(new InputStreamReader(inputreaderCategory2));

                boolean hasNextLine = true;
                while (hasNextLine) {
                    String getReadImg = buffTypeImg.readLine();
                    exerciseObj.setImgName(getReadImg);

                    String getReadExercise = buffTypeExercise.readLine();
                    exerciseObj.setExerciseName(getReadExercise);

                    String getReadCategory = buffTypeCategory.readLine();

                    //Set Category Id
                    if (getReadCategory != null) {
                        switch (getReadCategory) {
                            case "Abs":
                                exerciseObj.setCategoryId(1);
                                break;
                            case "Stretch":
                                exerciseObj.setCategoryId(2);
                                break;
                            case "Shoulders":
                                exerciseObj.setCategoryId(3);
                                break;
                            case "Cardio":
                                exerciseObj.setCategoryId(4);
                                break;
                            case "Legs":
                                exerciseObj.setCategoryId(5);
                                break;
                            case "Arms":
                                exerciseObj.setCategoryId(6);
                                break;
                            case "Chest":
                                exerciseObj.setCategoryId(7);
                                break;
                            case "Back":
                                exerciseObj.setCategoryId(8);
                                break;
                        }
                    }

                    String getReadCategory2 = buffTypeCategory2.readLine();

                    //Set Secondary Category
                    if (getReadCategory2 != null) {
                        switch (getReadCategory2) {
                            case "null":
                                exerciseObj.setSecondaryCategoryId(1);
                                break;
                            case "biceps":
                                exerciseObj.setSecondaryCategoryId(2);
                                break;
                            case "triceps":
                                exerciseObj.setSecondaryCategoryId(3);
                                break;
                            case "forearms":
                                exerciseObj.setSecondaryCategoryId(4);
                                break;
                            case "calf":
                                exerciseObj.setSecondaryCategoryId(5);
                                break;
                            case "biceps&triceps":
                                exerciseObj.setSecondaryCategoryId(6);
                                break;
                            case "glutes":
                                exerciseObj.setSecondaryCategoryId(7);
                                break;
                        }
                    }
                    exerciseObjDAO.insert(exerciseObj);

                    hasNextLine = getReadImg != null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }
}
