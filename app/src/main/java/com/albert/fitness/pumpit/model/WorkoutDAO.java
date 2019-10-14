package com.albert.fitness.pumpit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkoutDAO {

    @Insert
    void insert(WorkoutObj item);

    @Update
    void update(WorkoutObj item);

    @Delete
    void delete(WorkoutObj item);

    @Query("SELECT * FROM workout_table")
    LiveData<List<WorkoutObj>> getAllWorkout();

    @Query("SELECT * FROM workout_table WHERE workout_plan_id==:id ORDER BY workout_day_name ASC")
    LiveData<List<WorkoutObj>> getWorkout(int id);
}
