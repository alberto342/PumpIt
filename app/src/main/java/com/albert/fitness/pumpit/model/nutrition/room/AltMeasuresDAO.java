package com.albert.fitness.pumpit.model.nutrition.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AltMeasuresDAO {

    @Insert
    void insert(AltMeasures item);

    @Insert
    void insert(List<AltMeasures> item);

    @Update
    void update(AltMeasures item);

    @Delete
    void delete(AltMeasures item);

    @Query("SELECT * FROM alt_measures_table")
    LiveData<List<AltMeasures>> getAllAltMeasures();


    @Query("SELECT * FROM alt_measures_table WHERE alt_measures_id==:id")
    LiveData<AltMeasures> getAltMeasures(int id);

    @Query("SELECT * FROM alt_measures_table WHERE food_id==:id")
    LiveData<List<AltMeasures>> getAltMeasuresByFoodId(int id);

    @Query("SELECT alt_measures_id FROM alt_measures_table INNER JOIN nutrition_table " +
            "ON alt_measures_table.food_id==:id " +
            "AND alt_measures_table.measure = nutrition_table.serving_unit")
    LiveData<Integer> getAltMeasuresIdByNutritionTable(int id);


    @Query("SELECT MAX(alt_measures_id) AS alt_measures_id FROM alt_measures_table " +
            "INNER JOIN nutrition_table ON alt_measures_table.food_id==:id " +
            "AND alt_measures_table.measure ==:measure")
    LiveData<Integer> getAltMeasuresIdByFoodIdAndMeasureType(int id, String measure);

    @Query("SELECT measure, food_log_table.qty, seq, serving_weight, serving_weight_grams " +
            "FROM alt_measures_table " +
            "INNER JOIN food_log_table " +
            "ON alt_measures_table.alt_measures_id = food_log_table.alt_measures_id " +
            "INNER JOIN nutrition_table " +
            "ON alt_measures_table.food_id = nutrition_table.food_id " +
            "WHERE food_log_table.date==:date " +
            "AND food_log_table.eat_type ==:type")
    LiveData<List<QueryAltMeasures>> getAltMeasuresQtyAndWeight(String date, String type);
}
