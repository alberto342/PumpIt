package com.albert.fitness.pumpit.model.nutrition.room;

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

    @Insert
    void insert(List<Nutrition> nutritionList);

    @Update
    void update(Nutrition item);

    @Delete
    void delete(Nutrition item);

    @Query("SELECT * FROM nutrition_table")
    LiveData<List<Nutrition>> getAllNutrition();

    @Query("SELECT * FROM nutrition_table WHERE nutrition_id==:id")
    LiveData<List<Nutrition>> getNutrition(int id);

    @Query("SELECT * FROM nutrition_table WHERE food_id==:id")
    LiveData<Nutrition> getNutritionByFoodId(int id);

    @Query("SELECT nutrition_id, nutrition_table.food_id, type_id, calories, dietary_fiber, " +
            "cholesterol, protein, saturated_fat, total_fat, sugars, total_carbohydrate, " +
            "serving_qty, serving_unit, serving_weight_grams, source " +
            "FROM food_log_table " +
            "INNER JOIN nutrition_table " +
            "ON food_log_table.food_id = nutrition_table.food_id " +
            "WHERE food_log_table.date ==:date AND food_log_table.eat_type ==:type")
    LiveData<List<Nutrition>> getNutritionByDateAndMealType(String date, String type);
}
