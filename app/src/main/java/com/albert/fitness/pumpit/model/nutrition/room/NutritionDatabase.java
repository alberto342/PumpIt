package com.albert.fitness.pumpit.model.nutrition.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {FoodsObj.class, AltMeasures.class, FullNutrition.class, Nutrition.class,
        Photo.class, Tags.class, FoodLog.class, WaterTracker.class}, version = 1)
public abstract class
NutritionDatabase extends RoomDatabase {

    public abstract FoodsDAO foodsDAO();

    public abstract AltMeasuresDAO altMeasuresDAO();

    public abstract FullNutritionDAO fullNutritionDAO();

    public abstract NutritionDAO nutritionDAO();

    public abstract PhotoDAO photoDAO();

    public abstract TagsTAO tagsTAO();

    public abstract FoodLogDAO foodLogDAO();

    public abstract WaterTrackerDAO waterTrackerDAO();

    private static NutritionDatabase instance;

    public static synchronized NutritionDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NutritionDatabase.class, "nutrition_database")
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
        private FoodsDAO foodsDAO;
        private AltMeasuresDAO altMeasuresDAO;
        private FullNutritionDAO fullNutritionDAO;
        private NutritionDAO nutritionDAO;
        private PhotoDAO photoDAO;
        private TagsTAO tagsTAO;
        private FoodLogDAO foodLogDAO;
        private WaterTrackerDAO waterTrackerDAO;

        public InitialDataAsyncTask(NutritionDatabase nutritionDatabase) {
            foodsDAO = nutritionDatabase.foodsDAO();
            altMeasuresDAO = nutritionDatabase.altMeasuresDAO();
            fullNutritionDAO = nutritionDatabase.fullNutritionDAO();
            nutritionDAO = nutritionDatabase.nutritionDAO();
            photoDAO = nutritionDatabase.photoDAO();
            tagsTAO = nutritionDatabase.tagsTAO();
            foodLogDAO = nutritionDatabase.foodLogDAO();
            waterTrackerDAO = nutritionDatabase.waterTrackerDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }


}
