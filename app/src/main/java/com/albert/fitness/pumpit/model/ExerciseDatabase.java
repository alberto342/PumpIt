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

@Database(entities = {ExerciseCategory.class, ExerciseObj.class}, version = 1)
public abstract class ExerciseDatabase extends RoomDatabase {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public abstract ExerciseCategoryDAO exerciseCategoryDAO();

    public abstract ExerciseObjDAO exerciseObjDAO();

    private static ExerciseDatabase instance;

    public static synchronized ExerciseDatabase getInstance(Context context) {
        if (instance == null) {
            mContext = context;
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ExerciseDatabase.class, "nutrition_database")
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
        private ExerciseObjDAO exerciseObjDAO;


        public InitialDataAsyncTask(ExerciseDatabase exerciseDatabase) {

            exerciseCategoryDAO = exerciseDatabase.exerciseCategoryDAO();
            exerciseObjDAO = exerciseDatabase.exerciseObjDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ExerciseCategory exerciseCategory = new ExerciseCategory();
            ExerciseObj exerciseObj = new ExerciseObj();

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
                    exerciseCategory.setCategoryName(getReadCategory);

                    String getReadCategory2 = buffTypeCategory2.readLine();
                    exerciseCategory.setSecondaryCategory(getReadCategory2);

                    exerciseCategoryDAO.insert(exerciseCategory);
                    exerciseObjDAO.insert(exerciseObj);

                    hasNextLine = getReadImg != null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
