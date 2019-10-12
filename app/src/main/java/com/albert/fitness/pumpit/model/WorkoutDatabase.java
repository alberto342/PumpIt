package com.albert.fitness.pumpit.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {WorkoutPlanObj.class, WorkoutObj.class, DateObj.class, Training.class}, version = 1)
public abstract class WorkoutDatabase extends RoomDatabase {

    public abstract WorkoutPlanDAO workoutPlanDAO();

    public abstract WorkoutDAO workoutDAO();

    public abstract DateDAO dateDAO();

    public abstract TrainingDAO trainingDAO();

    private static WorkoutDatabase instance;

    public static synchronized WorkoutDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    WorkoutDatabase.class, "workout_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }


    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new InitialDataAsyncTask(instance).execute();
        }
    };

    private static class InitialDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private WorkoutPlanDAO workoutPlanDAO;
        private WorkoutDAO workoutDAO;
        private DateDAO dateDAO;
        private TrainingDAO trainingDAO;

        public InitialDataAsyncTask(WorkoutDatabase workoutDatabase) {
            workoutPlanDAO = workoutDatabase.workoutPlanDAO();
            workoutDAO = workoutDatabase.workoutDAO();
            dateDAO = workoutDatabase.dateDAO();
            trainingDAO = workoutDatabase.trainingDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }


}
