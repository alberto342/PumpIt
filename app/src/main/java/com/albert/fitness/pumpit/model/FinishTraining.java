package com.albert.fitness.pumpit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "finish_training_table", foreignKeys = @ForeignKey(entity = Training.class,
        parentColumns = "training_id", childColumns = "training_id", onDelete = CASCADE))
public class FinishTraining {

    public static final String TRAINING_LOG = "trainingLog";
    public static final String TRAINING_NAME = "trainingName";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "finish_id")
    private int finishId;
    @ColumnInfo(name = "chr_total_training")
    private String chrTotalTraining;
    @ColumnInfo(name = "total_calories_burned")
    private int caloriesBurned;
    private String date;
    private String time;

    @ColumnInfo(name = "training_id")
    private int trainingId;

    public FinishTraining() {
    }

    public FinishTraining(String chrTotalTraining, int caloriesBurned, int trainingId) {
        this.chrTotalTraining = chrTotalTraining;
        this.caloriesBurned = caloriesBurned;
        this.date = Event.getTodayData();
        this.time = Event.getCurrentTime();
        this.trainingId = trainingId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public int getFinishId() {
        return finishId;
    }

    public void setFinishId(int finishId) {
        this.finishId = finishId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getChrTotalTraining() {
        return chrTotalTraining;
    }

    public void setChrTotalTraining(String chrTotalTraining) {
        this.chrTotalTraining = chrTotalTraining;
    }


}
