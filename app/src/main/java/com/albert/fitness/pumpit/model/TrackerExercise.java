package com.albert.fitness.pumpit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tracker_exercise_table")
public class TrackerExercise {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tracker_id")
    private int trackerId;
    @ColumnInfo(name = "reps_number")
    private int repsNumber;
    private float weight;
    @ColumnInfo(name = "training_id")
    private int trainingId;

    @ColumnInfo(name = "finish_training_id")
    private int finishTrainingId;

    public TrackerExercise() {
    }

    public TrackerExercise(int repsNumber, float weight, int trainingId) {
        this.repsNumber = repsNumber;
        this.weight = weight;
        this.trainingId = trainingId;
    }

    public TrackerExercise(float weight, int repsNumber, int finishTrainingId) {
        this.repsNumber = repsNumber;
        this.weight = weight;
        this.finishTrainingId = finishTrainingId;
    }

    public TrackerExercise(int repNumber, float weight) {
        this.repsNumber = repNumber;
        this.weight = weight;
    }

    public int getFinishTrainingId() {
        return finishTrainingId;
    }

    public void setFinishTrainingId(int finishTrainingId) {
        this.finishTrainingId = finishTrainingId;
    }

    public int getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(int trackerId) {
        this.trackerId = trackerId;
    }

    public int getRepsNumber() {
        return repsNumber;
    }

    public void setRepsNumber(int repsNumber) {
        this.repsNumber = repsNumber;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    public TrackerExercise(float weight) {
        this.weight = weight;
    }

    public TrackerExercise(int repNumber) {
        this.repsNumber = repNumber;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
