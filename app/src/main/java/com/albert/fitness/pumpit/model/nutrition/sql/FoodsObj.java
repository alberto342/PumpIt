package com.albert.fitness.pumpit.model.nutrition.sql;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;


@Entity(tableName = "foods_table")
public class FoodsObj {

    public static final String SHARED_PREFERENCES_FILE = "meal";
    public static final String DINNER = "dinner";
    public static final String BREAKFAST = "breakfast";
    public static final String LUNCH = "lunch";
    public static final String SNACK = "snack";
    public static final String NUTRITION = "nutrition";
    public static final String All_NUTRITION = "all_nutrition";
    public static final String WATER_TRACKER = "water_tracker";


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "food_id")
    private int foodId;

    @ColumnInfo(name = "food_name")
    private String foodName;

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodsObj)) return false;
        FoodsObj foodsObj = (FoodsObj) o;
        return getFoodId() == foodsObj.getFoodId() &&

                getFoodName().equals(foodsObj.getFoodName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFoodId(), getFoodName());
    }
}
