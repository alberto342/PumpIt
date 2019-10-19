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

    @ColumnInfo(name = "workout_day_name")
    private String workoutDayName;

    @ColumnInfo(name = "workout_day")
    private String workoutDay;

    private String date;

    @ColumnInfo(name = "workout_plan_id")
    private int workoutPlanId;


    public WorkoutObj() {
    }

    public WorkoutObj(String workoutDayName, String workoutDay, String date, int workoutPlanId) {
        this.workoutDayName = workoutDayName;
        this.workoutDay = workoutDay;
        this.date = date;
        this.workoutPlanId = workoutPlanId;
    }

    public WorkoutObj(int workoutId, String workoutDayName, String workoutDay, String date, int workoutPlanId) {
        this.workoutId = workoutId;
        this.workoutDayName = workoutDayName;
        this.workoutDay = workoutDay;
        this.date = date;
        this.workoutPlanId = workoutPlanId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
