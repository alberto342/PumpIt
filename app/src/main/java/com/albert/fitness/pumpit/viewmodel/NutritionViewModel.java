package com.albert.fitness.pumpit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.albert.fitness.pumpit.model.nutrition.sql.FoodsObj;
import com.albert.fitness.pumpit.model.nutrition.sql.NutritionRepository;

import java.util.List;

public class NutritionViewModel extends AndroidViewModel {

    private NutritionRepository repository;
    private LiveData<List<FoodsObj>> food;


    public NutritionViewModel(@NonNull Application application) {
        super(application);
        repository = new NutritionRepository(application);
    }

    public LiveData<List<FoodsObj>> getAllFoods() {
        return food = repository.getAllFoods();
    }





}
