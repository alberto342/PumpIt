package com.albert.fitness.pumpit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseObjDAO {

    @Insert
    void insert(ExerciseObj exerciseObj);

    @Query("SELECT * FROM exercise_table")
    LiveData<List<ExerciseObj>> getAllExercise();

    @Query("SELECT * FROM exercise_table WHERE category_id == :categoryId")
    LiveData<List<ExerciseObj>> getExercise(int categoryId);
}
