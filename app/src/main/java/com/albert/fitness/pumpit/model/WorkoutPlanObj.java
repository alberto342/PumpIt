package com.albert.fitness.pumpit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "plan_table")
public class WorkoutPlanObj {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "plan_id")
    private int planId;

    @ColumnInfo(name = "routine_name")
    private String routineName;

    @ColumnInfo(name = "days_week")
    private String daysWeek;

    @ColumnInfo(name = "difficulty_level")
    private String difficultyLevel;

    @ColumnInfo(name = "routine_type")
    private String routineType;

    @ColumnInfo(name = "day_type")
    private String dayType;

    @ColumnInfo(name = "routine_description")
    private String routineDescription;

    private String date;

    @ColumnInfo(name = "days_week_position")
    private int daysWeekPosition;


    public WorkoutPlanObj() {
    }


    @Ignore
    public WorkoutPlanObj(String routineName, String daysWeek, String difficultyLevel,
                          String routineType, String dayType, String routineDescription, String date,
                          int daysWeekPosition) {
        this.routineName = routineName;
        this.daysWeek = daysWeek;
        this.difficultyLevel = difficultyLevel;
        this.routineType = routineType;
        this.dayType = dayType;
        this.routineDescription = routineDescription;
        this.date = date;
        this.daysWeekPosition = daysWeekPosition;
    }

    @Ignore
    public WorkoutPlanObj(int planId, String routineName, String daysWeek, String difficultyLevel,
                          String routineType, String dayType, String routineDescription, String date,
                          int daysWeekPosition) {
        this.planId = planId;
        this.routineName = routineName;
        this.daysWeek = daysWeek;
        this.difficultyLevel = difficultyLevel;
        this.routineType = routineType;
        this.dayType = dayType;
        this.routineDescription = routineDescription;
        this.date = date;
        this.daysWeekPosition = daysWeekPosition;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public String getDaysWeek() {
        return daysWeek;
    }

    public void setDaysWeek(String daysWeek) {
        this.daysWeek = daysWeek;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getRoutineType() {
        return routineType;
    }

    public void setRoutineType(String routineType) {
        this.routineType = routineType;
    }

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public String getRoutineDescription() {
        return routineDescription;
    }

    public void setRoutineDescription(String routineDescription) {
        this.routineDescription = routineDescription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDaysWeekPosition() {
        return daysWeekPosition;
    }

    public void setDaysWeekPosition(int daysWeekPosition) {
        this.daysWeekPosition = daysWeekPosition;
    }
}
