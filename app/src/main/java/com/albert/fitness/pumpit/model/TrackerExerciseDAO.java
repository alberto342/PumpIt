package com.albert.fitness.pumpit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TrackerExerciseDAO {

    @Insert
    void insert(TrackerExercise item);

    @Update
    void update(TrackerExercise item);

    @Delete
    void delete(TrackerExercise item);

    @Query("SELECT * FROM tracker_exercise_table")
    LiveData<List<TrackerExercise>> getAllTrackerExercise();


    @Query("SELECT * FROM tracker_exercise_table WHERE tracker_id==:id")
    LiveData<List<TrackerExercise>> getTrackerExercises(int id);
}
