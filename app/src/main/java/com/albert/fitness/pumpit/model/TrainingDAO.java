package com.albert.fitness.pumpit.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TrainingDAO {

    @Insert
    void insert(Training item);

    @Update
    void update(Training item);

    @Delete
    void delete(Training item);

    @Insert
    void insertTrainingAndTracker(Training Training, List<TrackerExercise> trackerExercises);

    @Query("SELECT * FROM training_table")
    LiveData<List<Training>> getAllTraining();

    @Query("SELECT MAX(training_id) from training_table")
    LiveData<Integer> getLastTrainingId();

    @Query("SELECT * FROM training_table WHERE workout_id==:id")
    LiveData<List<Training>> getTrainings(int id);

    @Query("SELECT * FROM training_table WHERE exercise_id==:id ORDER BY index_of_Training ASC")
    LiveData<List<Training>> getExercise(int id);

    @Query("SELECT * FROM training_table INNER JOIN workout_table ON workout_day==:dayOfToday AND training_table.workout_id = workout_table.workout_id")
    LiveData<List<Training>> getAllTrainingByDate(String dayOfToday);

    @Query("UPDATE training_table SET index_of_Training=:newPosition WHERE index_of_Training =:position")
    void updateTrainingPosition(int newPosition, int position);
}
