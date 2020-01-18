package com.albert.fitness.pumpit.model.nutrition.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WaterTrackerDAO {

    @Insert
    void insert(WaterTracker item);

    @Update
    void update(WaterTracker item);

    @Delete
    void delete(WaterTracker item);

    @Query("SELECT * FROM water_tracker_table")
    LiveData<List<WaterTracker>> getAllWaterTracker();


    @Query("SELECT * FROM water_tracker_table WHERE tracker_id==:id")
    LiveData<List<WaterTracker>> getWaterTrackers(int id);

    @Query("SELECT * from water_tracker_table WHERE date==:date ")
    LiveData<WaterTracker> getWaterTracker(String date);

}
