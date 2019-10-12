package com.albert.fitness.pumpit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TrainingDAO {

    @Insert
    void insert(Training item);

    @Update
    void update(Training item);

    @Delete
    void delete(Training item);

    @Query("SELECT * FROM training_table")
    LiveData<List<Training>> getAllTraining();


    @Query("SELECT * FROM training_table WHERE workout_id==:id")
    LiveData<List<Training>> getTrainings(int id);

}
