package com.albert.fitness.pumpit.model.nutrition.sql;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodsDAO {

    @Insert
    void insert(FoodsObj item);

    @Update
    void update(FoodsObj item);

    @Delete
    void delete(FoodsObj item);

    @Query("SELECT * FROM foods_table")
    LiveData<List<FoodsObj>> getAllFoodsObj();


    @Query("SELECT * FROM foods_table WHERE food_id==:id")
    LiveData<List<FoodsObj>> getFoodsObjs(int id);

}
