package com.albert.fitness.pumpit.model.nutrition.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodsDAO {

    @Insert
    void insert(FoodsObj item);

    @Update
    void update(FoodsObj item);

    @Delete
    void delete(FoodsObj item);

    @Insert
    void insertAllNutrition(List<FoodsObj> foodsObj,
                            List<Nutrition> nutritionList,
                            List<FullNutrition> fullNutritionList,
                            List<AltMeasures> altMeasuresList,
                            List<Photo> photoList,
                            List<Tags> tagsList);

    @Query("SELECT * FROM foods_table")
    LiveData<List<FoodsObj>> getAllFoodsObj();

    @Query("SELECT * FROM foods_table WHERE food_id==:id")
    LiveData<List<FoodsObj>> getFoodsObjs(int id);

    @Query("SELECT max(food_id) FROM foods_table")
    LiveData<Integer> getLastFoodId();


    @Query("SELECT food_id FROM foods_table where food_name==:name")
    LiveData<Integer> getFoodIdByName(String name);


    @Query("SELECT log_id, food_name, qty, thumb, calories, protein, serving_unit, total_carbohydrate " +
            "FROM food_log_table, foods_table " +
            "INNER JOIN photo_table ON food_log_table.food_id = photo_table.food_id " +
            "INNER JOIN nutrition_table ON food_log_table.food_id = nutrition_table.food_id " +
            "WHERE food_log_table.date ==:date AND food_log_table.eat_type==:type " +
            "AND foods_table.food_id = nutrition_table.food_id")
    LiveData<List<QueryNutritionItem>> getNutritionItem(String date, String type);

}
