package com.albert.fitness.pumpit.model;

import androidx.room.ColumnInfo;

public class QueryFinishWorkout {

    @ColumnInfo(name = "chr_total_training")
    private String chronometer;
    @ColumnInfo(name = "total_calories_burned")
    private int caloriesBurned;
    @ColumnInfo(name = "time")
    private String totalTime;
    @ColumnInfo(name = "exercise_id")
    private int exerciseId;
    @ColumnInfo(name = "rest_after_exercise")
    private int restAfterExercise;
    @ColumnInfo(name = "rest_between_set")
    private int restBetweenSet;
    @ColumnInfo(name = "size_of_rept")
    private int sizeOfRept;
    @ColumnInfo(name = "reps_number")
    private int repsNumber;
    private float weight;
    @ColumnInfo(name = "tracker_id")
    private int trackerId;
    @ColumnInfo(name = "finish_id")
    private int finishId;

    public String getChronometer() {
        return chronometer;
    }

    public void setChronometer(String chronometer) {
        this.chronometer = chronometer;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getRestAfterExercise() {
        return restAfterExercise;
    }

    public void setRestAfterExercise(int restAfterExercise) {
        this.restAfterExercise = restAfterExercise;
    }

    public int getRestBetweenSet() {
        return restBetweenSet;
    }

    public void setRestBetweenSet(int restBetweenSet) {
        this.restBetweenSet = restBetweenSet;
    }

    public int getSizeOfRept() {
        return sizeOfRept;
    }

    public void setSizeOfRept(int sizeOfRept) {
        this.sizeOfRept = sizeOfRept;
    }

    public int getRepsNumber() {
        return repsNumber;
    }

    public void setRepsNumber(int repsNumber) {
        this.repsNumber = repsNumber;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(int trackerId) {
        this.trackerId = trackerId;
    }

    public int getFinishId() {
        return finishId;
    }

    public void setFinishId(int finishId) {
        this.finishId = finishId;
    }
}
