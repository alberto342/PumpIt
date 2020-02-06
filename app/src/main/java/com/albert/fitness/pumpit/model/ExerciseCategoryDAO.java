package com.albert.fitness.pumpit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseCategoryDAO {

    @Insert
    void insert(ExerciseCategory exerciseCategory);

    @Query("SELECT * FROM exercise_category_table")
    LiveData<List<ExerciseCategory>> getAllCategory();

}
