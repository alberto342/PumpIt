package com.albert.fitness.pumpit.model.nutrition.sql;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AltMeasuresDAO {

    @Insert
    void insert(AltMeasures item);

    @Update
    void update(AltMeasures item);

    @Delete
    void delete(AltMeasures item);

    @Query("SELECT * FROM alt_measures_table")
    LiveData<List<AltMeasures>> getAllAltMeasures();


    @Query("SELECT * FROM alt_measures_table WHERE alt_measures_id==:id")
    LiveData<List<AltMeasures>> getAltMeasuress(int id);

}
