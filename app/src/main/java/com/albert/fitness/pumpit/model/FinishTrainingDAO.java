package com.albert.fitness.pumpit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FinishTrainingDAO {

    @Insert
    void insert(FinishTraining item);

    @Update
    void update(FinishTraining item);

    @Delete
    void delete(FinishTraining item);

    @Insert
    void insertFinishTrainingAndTracker(FinishTraining finishTraining, List<TrackerExercise> trackerExercises);

    @Query("SELECT * FROM finish_training_table")
    LiveData<List<FinishTraining>> getAllFinishTraining();

    @Query("SELECT * FROM finish_training_table WHERE date==:date")
    LiveData<FinishTraining> getFinishTrainingsByDate(String date);

    @Query("SELECT max(finish_id) FROM finish_training_table")
    LiveData<Integer> getLastFinishTrainingById();

    @Query(("SELECT * FROM finish_training_table WHERE finish_id ==:id"))
    LiveData<FinishTraining> getFinishTrainingById(int id);


    @Query("SELECT * FROM finish_training_table WHERE training_id==:id")
    LiveData<List<FinishTraining>> getFinishTrainings(int id);

    @Query("SELECT MAX(finish_id) FROM finish_training_table")
    LiveData<Integer> getMaxIdFromFinishTraining();

    @Query("SELECT exercise_id FROM finish_training_table " +
            "INNER JOIN training_table ON finish_training_table.training_id = training_table.training_id " +
            "WHERE finish_training_table.date ==:date")
    LiveData<List<Integer>> getExerciseIdByDate(String date);


    @Query("SELECT time, rest_between_set, rest_after_exercise, size_of_rept, reps_number, weight, exercise_id " +
            "FROM finish_training_table " +
            "INNER JOIN training_table ON finish_training_table.training_id = training_table.training_id " +
            "INNER JOIN tracker_exercise_table ON finish_training_table.finish_id = tracker_exercise_table.finish_training_id " +
            "WHERE finish_training_table.date ==:date")
    LiveData<List<QueryLogWorkout>> getLogWorkout(String date);


    @Query("SELECT chr_total_training, total_calories_burned, time, exercise_id,rest_after_exercise, " +
            "rest_between_set, size_of_rept, reps_number, weight " +
            "FROM finish_training_table " +
            "INNER JOIN training_table ON finish_training_table.training_id = training_table.training_id " +
            "INNER JOIN tracker_exercise_table ON training_table.training_id = tracker_exercise_table.training_id " +
            "WHERE finish_training_table.date ==:date " +
            "ORDER BY exercise_id")
    LiveData<List<QueryFinishWorkout>> getFinishWorkout(String date);

    @Query("SELECT chr_total_training, total_calories_burned, time, exercise_id,rest_after_exercise, rest_between_set, size_of_rept, reps_number, weight FROM finish_training_table " +
            "INNER JOIN training_table ON finish_training_table.training_id = training_table.training_id " +
            "INNER JOIN tracker_exercise_table ON training_table.training_id = tracker_exercise_table.training_id " +
            "WHERE finish_training_table.date ==:date " +
            "AND exercise_id==:id " +
            "ORDER BY exercise_id")
    LiveData<List<QueryFinishWorkout>> getFinishWorkoutByDateAndExerciseId(String date, int id);

}
