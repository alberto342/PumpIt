package com.albert.fitness.pumpit.model.nutrition.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "food_log_table", foreignKeys = @ForeignKey(entity = FoodsObj.class,
        parentColumns = "food_id", childColumns = "food_id", onDelete = CASCADE))
public class FoodLog {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "log_id")
    private int logId;

    @ColumnInfo(name = "food_id")
    private int foodId;

    @ColumnInfo(name = "alt_measures_id")
    private int altMeasuresId;

    private int qty;

    @ColumnInfo(name = "eat_type")
    private String eatType;

    private String date;

    public FoodLog() {
    }

    public FoodLog(int foodId, int altMeasuresId, int qty, String eatType, String date) {
        this.foodId = foodId;
        this.altMeasuresId = altMeasuresId;
        this.qty = qty;
        this.eatType = eatType;
        this.date = date;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getAltMeasuresId() {
        return altMeasuresId;
    }

    public void setAltMeasuresId(int altMeasuresId) {
        this.altMeasuresId = altMeasuresId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getEatType() {
        return eatType;
    }

    public void setEatType(String eatType) {
        this.eatType = eatType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
