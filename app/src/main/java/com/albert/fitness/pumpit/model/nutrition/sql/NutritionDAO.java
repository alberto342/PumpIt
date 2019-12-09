package com.albert.fitness.pumpit.model.nutrition.sql;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NutritionDAO {

    @Insert
    void insert(Nutrition item);

    @Update
    void update(Nutrition item);

    @Delete
    void delete(Nutrition item);

    @Query("SELECT * FROM nutrition_table")
    LiveData<List<Nutrition>> getAllNutrition();


    @Query("SELECT * FROM nutrition_table WHERE nutrition_id==:id")
    LiveData<List<Nutrition>> getNutritions(int id);

}
