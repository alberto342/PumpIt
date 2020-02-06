package com.albert.fitness.pumpit.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExerciseRepository {
    private ExerciseDAO exerciseObjDAO;
    private ExerciseCategoryDAO exerciseCategoryDAO;
    private ExerciseSecondaryCategoryDAO secondaryCategoryDAO;
    private LiveData<List<ExerciseCategory>> exerciseCategory;
    private LiveData<List<Exercise>> exercise;
    private Executor executor;

    public ExerciseRepository(Application application) {
        executor = Executors.newFixedThreadPool(5);
        ExerciseDatabase exerciseDatabase = ExerciseDatabase.getInstance(application);
        exerciseCategoryDAO = exerciseDatabase.exerciseCategoryDAO();
        exerciseObjDAO = exerciseDatabase.exerciseObjDAO();
    }

    public LiveData<List<Exercise>> allExercise() {
        return exerciseObjDAO.getAllExercise();
    }

    public LiveData<List<ExerciseCategory>> getCategories() {
        return exerciseCategoryDAO.getAllCategory();
    }

    public LiveData<List<ExerciseSecondaryCategory>> getSecondaryCategories() {
        return secondaryCategoryDAO.getAllSecondaryCategory();
    }

    public LiveData<Exercise> getExerciseById(int id) {
        return exerciseObjDAO.getExerciseById(id);
    }

    public LiveData<List<Exercise>> getExercise(int categoryId) {
        return exerciseObjDAO.getExercise(categoryId);
    }

    public LiveData<List<Exercise>> getExerciseBySecondaryCategory(int categoryId) {
        return exerciseObjDAO.getExerciseBySecondaryCategory(categoryId);
    }

    public LiveData<List<Exercise>> getQueryAllExercise(String query) {
        return exerciseObjDAO.getQueryAllExercise(query);
    }

    public LiveData<List<Exercise>> getQueryExerciseList(String queryText, int id) {
        return exerciseObjDAO.getQueryExerciseList(queryText, id);
    }

    public  LiveData<List<Exercise>> getQueryBySecondaryCategory(String queryText, int id) {
        return exerciseObjDAO.getQueryBySecondaryCategory(queryText, id);
    }


    public void insertExercise(Exercise exercise) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> exerciseObjDAO.insert(exercise));
    }


}
