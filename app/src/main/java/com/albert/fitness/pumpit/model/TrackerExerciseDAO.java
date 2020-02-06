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

    @Query("DELETE FROM tracker_exercise_table WHERE training_id ==:id")
    void deleteTrackerExerciseByTrainingId(int id);

    @Query("DELETE FROM tracker_exercise_table WHERE tracker_id ==:id")
    void deleteTrackerExerciseByTrackerId(int id);

    @Query("SELECT * FROM tracker_exercise_table")
    LiveData<List<TrackerExercise>> getAllTrackerExercise();

    @Query("SELECT * FROM tracker_exercise_table WHERE training_id==:id")
    LiveData<List<TrackerExercise>> getTrackerExercises(int id);

    @Query("SELECT * FROM tracker_exercise_table WHERE finish_training_id==:id")
    LiveData<List<TrackerExercise>> getAllTrackerByFinishTrainingId(int id);

    @Query("SELECT * FROM tracker_exercise_table WHERE finish_training_id = 0")
    LiveData<List<TrackerExercise>> getAllTrackerWhereFinishIdZero();
}
