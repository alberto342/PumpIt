package com.albert.fitness.pumpit.model.nutrition.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodLogDAO {

    @Insert
    void insert(FoodLog item);

    @Update
    void update(FoodLog item);

    @Delete
    void delete(FoodLog item);

    @Query("SELECT * FROM food_log_table")
    LiveData<List<FoodLog>> getAllFoodLog();


    @Query("SELECT * FROM food_log_table WHERE food_id==:id")
    LiveData<List<FoodLog>> getFoodLogs(int id);


    @Query("select * from food_log_table WHERE log_id==:id")
    LiveData<FoodLog> getFoodLogByLogId(int id);

    @Query("SELECT SUM(calories) AS calories, SUM(total_carbohydrate) AS carb ,SUM(Protein) AS protein, " +
            "SUM(cholesterol) AS cholesterol, SUM(total_fat) AS fat FROM food_log_table " +
            "INNER JOIN nutrition_table ON food_log_table.food_id = nutrition_table.food_id " +
            "WHERE food_log_table.date ==:date")
    LiveData<SumNutritionPojo> getSumOfNutritionByDate(String date);


//    @Query("SELECT SUM(calories) AS calories, SUM(total_carbohydrate) AS carb ,SUM(Protein) AS protein, " +
//            "SUM(cholesterol) AS cholesterol, SUM(total_fat) AS fat FROM food_log_table " +
//            "INNER JOIN nutrition_table ON food_log_table.food_id = nutrition_table.food_id " +
//            "WHERE food_log_table.date ==:date AND food_log_table.eat_type ==:type")
//    LiveData<SumNutritionPojo> getSumOfNutritionByDateAndMealType(String date, String type);


    @Query("SELECT SUM(calories ) AS calories, " +
            "SUM(total_carbohydrate ) AS carb ," +
            "SUM(Protein ) AS protein, " +
            "SUM(cholesterol ) AS cholesterol, " +
            "SUM(total_fat ) AS fat " +
            "FROM food_log_table " +
            "INNER JOIN nutrition_table ON food_log_table.food_id = nutrition_table.food_id " +
            "INNER JOIN alt_measures_table ON food_log_table.food_id = alt_measures_table.food_id " +
            "AND food_log_table.alt_measures_id = alt_measures_table.alt_measures_id " +
            "WHERE food_log_table.date ==:date " +
            "AND food_log_table.eat_type ==:type " +
            "AND alt_measures_table.measure != 'g'")
    LiveData<SumNutritionPojo> getSumOfNutritionByDateAndMealType(String date, String type);


    @Query("SELECT SUM(calories * alt_measures_table.serving_weight * alt_measures_table.qty / nutrition_table.serving_weight_grams / 100) AS calories, " +
            "SUM(total_carbohydrate * alt_measures_table.serving_weight * alt_measures_table.qty / nutrition_table.serving_weight_grams / 100) AS carb, " +
            "SUM(Protein * alt_measures_table.serving_weight * alt_measures_table.qty / nutrition_table.serving_weight_grams / 100) AS protein, " +
            "SUM(cholesterol * alt_measures_table.serving_weight * alt_measures_table.qty / nutrition_table.serving_weight_grams / 100) AS cholesterol, " +
            "SUM(total_fat * alt_measures_table.serving_weight * alt_measures_table.qty / nutrition_table.serving_weight_grams / 100) AS fat " +
            "FROM food_log_table INNER JOIN nutrition_table ON food_log_table.food_id = nutrition_table.food_id " +
            "INNER JOIN alt_measures_table ON food_log_table.food_id = alt_measures_table.food_id " +
            "AND food_log_table.alt_measures_id = alt_measures_table.alt_measures_id " +
            "WHERE food_log_table.date ==:date AND food_log_table.eat_type==:type AND alt_measures_table.measure = 'g'")
    LiveData<SumNutritionPojo> getSumOfNutritionByDateAndMealTypeWithGrams(String date, String type);


}
