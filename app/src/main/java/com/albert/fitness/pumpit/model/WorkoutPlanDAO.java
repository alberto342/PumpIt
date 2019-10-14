package com.albert.fitness.pumpit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkoutPlanDAO {

    @Insert
    void insert(WorkoutPlanObj workoutPlanObj);

    @Update
    void update(WorkoutPlanObj workoutPlanObj);

    @Delete
    void delete(WorkoutPlanObj workoutPlanObj);

    @Query("SELECT * FROM plan_table")
    LiveData<List<WorkoutPlanObj>> getAllPlans();

    @Query("SELECT * FROM plan_table WHERE plan_id ==:planId")
    LiveData<WorkoutPlanObj> getPlan(int planId);

    @Query("SELECT * FROM plan_table WHERE date_id==:dateId")
    LiveData<List<WorkoutPlanObj>> getPlansByDate(int dateId);
}
