package com.albert.fitness.pumpit.model.nutrition.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "full_nutrition_table", foreignKeys = @ForeignKey(entity = FoodsObj.class,
        parentColumns = "food_id", childColumns = "food_id", onDelete = CASCADE))
public class FullNutrition {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "fn_id")
    private int fullNutritionId;

    @ColumnInfo(name = "food_id")
    private int foodId;

    @ColumnInfo(name = "atter_id")
    private int atterId;

    private float value;

    public FullNutrition() {
    }

    public FullNutrition(int foodId, int atterId, float value) {
        this.foodId = foodId;
        this.atterId = atterId;
        this.value = value;
    }

    public int getFullNutritionId() {
        return fullNutritionId;
    }

    public void setFullNutritionId(int fullNutritionId) {
        this.fullNutritionId = fullNutritionId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getAtterId() {
        return atterId;
    }

    public void setAtterId(int atterId) {
        this.atterId = atterId;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
