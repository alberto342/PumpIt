package com.albert.fitness.pumpit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DateDAO {

    @Insert
    void insert(DateObj item);

    @Update
    void update(DateObj item);

    @Delete
    void delete(DateObj item);

    @Query("SELECT * FROM date_table")
    LiveData<List<DateObj>> getAllDate();
}
