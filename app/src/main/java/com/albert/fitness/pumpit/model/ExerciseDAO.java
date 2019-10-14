package com.albert.fitness.pumpit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseDAO {

    @Insert
    void insert(Exercise exerciseObj);

    @Query("SELECT * FROM exercise_table")
    LiveData<List<Exercise>> getAllExercise();

    @Query("SELECT * FROM exercise_table WHERE exercise_id == :exerciseId")
    LiveData<Exercise> getExerciseById(int exerciseId);

    @Query("SELECT * FROM exercise_table WHERE category_id == :categoryId")
    LiveData<List<Exercise>> getExercise(int categoryId);

    @Query("SELECT * FROM exercise_table WHERE secondary_category_id == :categoryId")
    LiveData<List<Exercise>> getExerciseBySecondaryCategory(int categoryId);
}
