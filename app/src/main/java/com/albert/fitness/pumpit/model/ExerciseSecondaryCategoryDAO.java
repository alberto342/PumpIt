package com.albert.fitness.pumpit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseSecondaryCategoryDAO {

    @Insert
    void insert(ExerciseSecondaryCategory item);


    @Query("SELECT * FROM exercise_secondary_category_table")
    LiveData<List<ExerciseSecondaryCategory>> getAllSecondaryCategory();
}
