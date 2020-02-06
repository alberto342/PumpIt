package com.albert.fitness.pumpit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "training_table", foreignKeys = @ForeignKey(entity = WorkoutObj.class,
        parentColumns = "workout_id", childColumns = "workout_id", onDelete = CASCADE))
public class Training {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "training_id")
    private int trainingId;

    @ColumnInfo(name = "exercise_id")
    private int exerciseId;

    @ColumnInfo(name = "rest_between_set")
    private int restBetweenSet;

    @ColumnInfo(name = "rest_after_exercise")
    private int restAfterExercise;

    private String date;

    @ColumnInfo(name = "size_of_rept")
    private int sizeOfRept;

    @ColumnInfo(name = "index_of_Training")
    private int indexOfTraining;

    @ColumnInfo(name = "workout_id")
    private int workoutId;


    public Training() {
    }

    public Training(int exerciseId, int restBetweenSet, int restAfterExercise, int sizeOfRept, String date,int index, int workoutId) {
        this.exerciseId = exerciseId;
        this.restBetweenSet = restBetweenSet;
        this.restAfterExercise = restAfterExercise;
        this.date = date;
        this.sizeOfRept = sizeOfRept;
        this.indexOfTraining = index;
        this.workoutId = workoutId;
    }

    public Training(int exerciseId, int sizeOfRept, int restBetweenSet, int restAfterExercise,int index, String date) {
        this.exerciseId = exerciseId;
        this.restBetweenSet = restBetweenSet;
        this.restAfterExercise = restAfterExercise;
        this.date = date;
        this.indexOfTraining = index;
        this.sizeOfRept = sizeOfRept;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getSizeOfRept() {
        return sizeOfRept;
    }

    public void setSizeOfRept(int sizeOfRept) {
        this.sizeOfRept = sizeOfRept;
    }

    public int getRestAfterExercise() {
        return restAfterExercise;
    }

    public void setRestAfterExercise(int restAfterExercise) {
        this.restAfterExercise = restAfterExercise;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRestBetweenSet() {
        return restBetweenSet;
    }

    public void setRestBetweenSet(int restBetweenSet) {
        this.restBetweenSet = restBetweenSet;
    }

    public int getIndexOfTraining() {
        return indexOfTraining;
    }

    public void setIndexOfTraining(int indexOfTraining) {
        this.indexOfTraining = indexOfTraining;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }
}
