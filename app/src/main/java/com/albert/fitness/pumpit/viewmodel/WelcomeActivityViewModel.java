package com.albert.fitness.pumpit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.albert.fitness.pumpit.model.ExerciseCategory;
import com.albert.fitness.pumpit.model.ExerciseObj;
import com.albert.fitness.pumpit.model.ExerciseRepository;

import java.util.List;

public class WelcomeActivityViewModel extends AndroidViewModel {

    private ExerciseRepository repository;
    private LiveData<List<ExerciseCategory>> allCategories;
    private LiveData<List<ExerciseObj>> exerciseOfSelectedCategory;


    public WelcomeActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseRepository(application);
    }

    public LiveData<List<ExerciseCategory>> getAllCategories() {
        return allCategories=repository.getCategories();
    }

    public LiveData<List<ExerciseObj>> getExerciseOfASelectedCategory(int categoryId) {
        return  exerciseOfSelectedCategory=repository.getBooks(categoryId);
    }
}
