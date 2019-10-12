package com.albert.fitness.pumpit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "workout_table", foreignKeys = @ForeignKey(entity = WorkoutPlanObj.class,
        parentColumns = "plan_id", childColumns = "workout_plan_id", onDelete = CASCADE))
public class WorkoutObj {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_id")
    private int workoutId;

    @ColumnInfo(name = "length_training")
    private String lengthTraining;

    @ColumnInfo(name = "workout_day_name")
    private String workoutDayName;

    @ColumnInfo(name = "workout_day")
    private String workoutDay;

    @ColumnInfo(name = "workout_plan_id")
    private int workoutPlanId;


    public WorkoutObj() {
    }

    public WorkoutObj(String lengthTraining, String workoutDayName, String workoutDay, int workoutPlanId) {
        this.lengthTraining = lengthTraining;
        this.workoutDayName = workoutDayName;
        this.workoutDay = workoutDay;
        this.workoutPlanId = workoutPlanId;
    }

    public WorkoutObj(int workoutId, String lengthTraining, String workoutDayName, String workoutDay, int workoutPlanId) {
        this.workoutId = workoutId;
        this.lengthTraining = lengthTraining;
        this.workoutDayName = workoutDayName;
        this.workoutDay = workoutDay;
        this.workoutPlanId = workoutPlanId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public String getLengthTraining() {
        return lengthTraining;
    }

    public void setLengthTraining(String lengthTraining) {
        this.lengthTraining = lengthTraining;
    }

    public String getWorkoutDayName() {
        return workoutDayName;
    }

    public void setWorkoutDayName(String workoutDayName) {
        this.workoutDayName = workoutDayName;
    }

    public String getWorkoutDay() {
        return workoutDay;
    }

    public void setWorkoutDay(String workoutDay) {
        this.workoutDay = workoutDay;
    }

    public int getWorkoutPlanId() {
        return workoutPlanId;
    }

    public void setWorkoutPlanId(int workoutPlanId) {
        this.workoutPlanId = workoutPlanId;
    }
}
