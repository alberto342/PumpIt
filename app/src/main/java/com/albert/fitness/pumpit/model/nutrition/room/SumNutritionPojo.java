package com.albert.fitness.pumpit.model.nutrition.room;

import androidx.room.ColumnInfo;

public class SumNutritionPojo {

    private float calories;
    private float carb;
    private float cholesterol;
    private float fat;
    private float protein;
    private String measure;
    private int qty;

    @ColumnInfo(name = "serving_weight_grams")
    private int servingWeightGrams;

    public SumNutritionPojo() {
    }

    public SumNutritionPojo(float calories, float carb, float cholesterol, float fat, float protein) {
        this.calories = calories;
        this.carb = carb;
        this.cholesterol = cholesterol;
        this.fat = fat;
        this.protein = protein;
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

    public int getServingWeightGrams() {
        return servingWeightGrams;
    }

    public void setServingWeightGrams(int servingWeightGrams) {
        this.servingWeightGrams = servingWeightGrams;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getCarb() {
        return carb;
    }

    public void setCarb(float carb) {
        this.carb = carb;
    }

    public float getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(float cholesterol) {
        this.cholesterol = cholesterol;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }
}
