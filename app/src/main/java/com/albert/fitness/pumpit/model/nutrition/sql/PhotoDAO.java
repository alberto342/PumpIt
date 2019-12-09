package com.albert.fitness.pumpit.model.nutrition.sql;

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
    LiveData<List<Photo>> getPhotos(int id);

}
