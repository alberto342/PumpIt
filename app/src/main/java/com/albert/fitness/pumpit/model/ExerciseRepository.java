package com.albert.fitness.pumpit.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExerciseRepository {
    private ExerciseObjDAO exerciseObjDAO;
    private ExerciseCategoryDAO exerciseCategoryDAO;
    private LiveData<List<ExerciseCategory>> exerciseCategory;
    private LiveData<List<ExerciseObj>> exercise;
    private Executor executor;

    public ExerciseRepository(Application application) {
        executor = Executors.newFixedThreadPool(5);
        ExerciseDatabase exerciseDatabase = ExerciseDatabase.getInstance(application);
        exerciseCategoryDAO = exerciseDatabase.exerciseCategoryDAO();
        exerciseObjDAO = exerciseDatabase.exerciseObjDAO();
    }

    public LiveData<List<ExerciseCategory>> getCategories() {
        return exerciseCategoryDAO.getAllCategory();
    }

    public LiveData<List<ExerciseObj>> getBooks(int categoryId) {
        return exerciseObjDAO.getExercise(categoryId);
    }
}
