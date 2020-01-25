package com.albert.fitness.pumpit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.albert.fitness.pumpit.model.Exercise;
import com.albert.fitness.pumpit.model.ExerciseCategory;
import com.albert.fitness.pumpit.model.ExerciseRepository;

import java.util.List;

public class WelcomeActivityViewModel extends AndroidViewModel {

    private ExerciseRepository repository;
    private LiveData<List<Exercise>> allExercise;
    private LiveData<Exercise> exerciseById;
    private LiveData<List<ExerciseCategory>> allCategories;
    private LiveData<List<Exercise>> exerciseOfSelectedCategory;
    private LiveData<List<Exercise>> secondaryCategory;


    public WelcomeActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseRepository(application);
    }

    public LiveData<List<Exercise>> getAllExercise() {
        return repository.allExercise();
    }

    public LiveData<List<ExerciseCategory>> getAllCategories() {
        return repository.getCategories();
    }

    public LiveData<Exercise> getExerciseById(int id) {
        return  repository.getExerciseById(id);
    }

    public LiveData<List<Exercise>> getExerciseOfASelectedCategory(int categoryId) {
        return  repository.getExercise(categoryId);
    }

    public LiveData<List<Exercise>> getExerciseOfASelectedSecondaryCategory(int categoryId) {
        return  repository.getExerciseBySecondaryCategory(categoryId);
    }
}
