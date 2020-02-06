package com.albert.fitness.pumpit.model.nutrition.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PhotoDAO {

    @Insert
    void insert(Photo item);

    @Update
    void update(Photo item);

    @Delete
    void delete(Photo item);

    @Query("SELECT * FROM photo_table")
    LiveData<List<Photo>> getAllPhoto();


    @Query("SELECT * FROM photo_table WHERE photo_id==:id")
    LiveData<Photo> getPhotos(int id);


    @Query("SELECT * FROM photo_table WHERE food_id==:id")
    LiveData<Photo> getPhotosByFoodId(int id);

    @Query("SELECT highres,thumb,photo_id,photo_table.food_id FROM food_log_table INNER JOIN photo_table " +
            "ON food_log_table.food_id = photo_table.food_id " +
            "WHERE food_log_table.date ==:date AND food_log_table.eat_type==:type")
    LiveData<List<Photo>> getPhotosByDateAndMealType(String date, String type);
}
