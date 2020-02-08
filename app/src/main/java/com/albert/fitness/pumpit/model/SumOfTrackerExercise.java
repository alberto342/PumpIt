package com.albert.fitness.pumpit.model;

import androidx.room.ColumnInfo;

public class SumOfTrackerExercise {

    @ColumnInfo(name = "sum_weight_reps")
    private float sumWeightAndReps;
    @ColumnInfo(name = "sum_rest")
    private int sumRest;

    public float getSumWeightAndReps() {
        return sumWeightAndReps;
    }

    public void setSumWeightAndReps(float sumWeightAndReps) {
        this.sumWeightAndReps = sumWeightAndReps;
    }

    public int getSumRest() {
        return sumRest;
    }

    public void setSumRest(int sumRest) {
        this.sumRest = sumRest;
    }
}
