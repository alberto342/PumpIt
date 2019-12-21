package com.albert.fitness.pumpit.model.nutrition.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FullNutritionDAO {

    @Insert
    void insert(FullNutrition item);

    @Insert
    void insert(List<FullNutrition> item);

    @Update
    void update(FullNutrition item);

    @Delete
    void delete(FullNutrition item);

    @Query("SELECT * FROM full_nutrition_table")
    LiveData<List<FullNutrition>> getAllFullNutrition();

    @Query("SELECT * FROM full_nutrition_table WHERE food_id==:id")
    LiveData<List<FullNutrition>> getFullNutritiionByFoodId(int id);


    @Query("SELECT * FROM full_nutrition_table WHERE fn_id==:id")
    LiveData<List<FullNutrition>> getFullNutrition(int id);

}
