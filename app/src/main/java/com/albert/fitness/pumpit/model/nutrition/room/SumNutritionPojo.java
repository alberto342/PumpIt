package com.albert.fitness.pumpit.model.nutrition.room;

public class SumNutritionPojo {

    private float calories;
    private float carb;
    private float cholesterol;
    private float fat;
    private float protein;



    public SumNutritionPojo() {
    }

    public SumNutritionPojo(float calories, float carb, float cholesterol, float fat, float protein) {
        this.calories = calories;
        this.carb = carb;
        this.cholesterol = cholesterol;
        this.fat = fat;
        this.protein = protein;
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
