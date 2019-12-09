package com.albert.fitness.pumpit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FinishTrainingDAO {

    @Insert
    void insert(FinishTraining item);

    @Update
    void update(FinishTraining item);

    @Delete
    void delete(FinishTraining item);

    @Insert
    void insertFinishTrainingAndTracker(FinishTraining finishTraining, List<TrackerExercise> trackerExercises);

    @Query("SELECT * FROM finish_training_table")
    LiveData<List<FinishTraining>> getAllFinishTraining();

    @Query("SELECT * FROM finish_training_table WHERE date==:date")
    LiveData<FinishTraining> getFinishTrainingsByDate(String date);

    @Query("SELECT max(finish_id) FROM finish_training_table")
    LiveData<Integer> getLastFinishTrainingById();

    @Query(("SELECT * FROM finish_training_table WHERE finish_id ==:id"))
    LiveData<FinishTraining> getFinishTrainingById(int id);


    @Query("SELECT * FROM finish_training_table WHERE training_id==:id")
    LiveData<List<FinishTraining>> getFinishTrainings(int id);

}
