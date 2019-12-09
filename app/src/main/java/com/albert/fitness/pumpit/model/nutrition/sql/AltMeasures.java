package com.albert.fitness.pumpit.model.nutrition.sql;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "alt_measures_table", foreignKeys = @ForeignKey(entity = FoodsObj.class,
        parentColumns = "food_id", childColumns = "food_id", onDelete = CASCADE))
public class AltMeasures {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "alt_measures_id")
    private int altMeasuresId;

    @ColumnInfo(name = "food_id")
    private int foodId;

    private String measure;

    private int qty;

    @ColumnInfo(name = "serving_weight")
    private int servingWeight;

    public int getAltMeasuresId() {
        return altMeasuresId;
    }

    public void setAltMeasuresId(int altMeasuresId) {
        this.altMeasuresId = altMeasuresId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getServingWeight() {
        return servingWeight;
    }

    public void setServingWeight(int servingWeight) {
        this.servingWeight = servingWeight;
    }
}
