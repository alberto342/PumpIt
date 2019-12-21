package com.albert.fitness.pumpit.model.nutrition.room;

import androidx.room.ColumnInfo;

public class QueryNutritionItem {

    @ColumnInfo(name = "log_id")
    private int logId;

    @ColumnInfo(name = "food_name")
    private String foodName;
    private int qty;
    private String thumb;
    private float calories;
    private float protein;
    @ColumnInfo(name = "serving_unit")
    private String servingUnit;
    @ColumnInfo(name = "total_carbohydrate")
    private float totalCarbohydrate;

    public QueryNutritionItem() {
    }

    public QueryNutritionItem(String foodName, int qty, String thumb, float calories, float protein, String servingUnit, float totalCarbohydrate) {
        this.foodName = foodName;
        this.qty = qty;
        this.thumb = thumb;
        this.calories = calories;
        this.protein = protein;
        this.servingUnit = servingUnit;
        this.totalCarbohydrate = totalCarbohydrate;
    }

    public float getTotalCarbohydrate() {
        return totalCarbohydrate;
    }

    public void setTotalCarbohydrate(float totalCarbohydrate) {
        this.totalCarbohydrate = totalCarbohydrate;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }
}
