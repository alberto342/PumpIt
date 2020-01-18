package com.albert.fitness.pumpit.model;

import androidx.room.ColumnInfo;

public class QueryLogWorkout {

    private String time;
    @ColumnInfo(name = "size_of_rept")
    private int rept;
    @ColumnInfo(name = "rest_between_set")
    private int restBetweenSet;
    @ColumnInfo(name = "rest_after_exercise")
    private int restAfterExercise;
    @ColumnInfo(name = "reps_number")
    private int reps;
    private float weight;
    @ColumnInfo(name = "exercise_id")
    private int exerciseId;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRept() {
        return rept;
    }

    public void setRept(int rept) {
        this.rept = rept;
    }

    public int getRestBetweenSet() {
        return restBetweenSet;
    }

    public void setRestBetweenSet(int restBetweenSet) {
        this.restBetweenSet = restBetweenSet;
    }

    public int getRestAfterExercise() {
        return restAfterExercise;
    }

    public void setRestAfterExercise(int restAfterExercise) {
        this.restAfterExercise = restAfterExercise;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }
}
