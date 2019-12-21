package com.albert.fitness.pumpit.model.nutrition.room;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "nutrition_table", foreignKeys = @ForeignKey(entity = FoodsObj.class,
        parentColumns = "food_id", childColumns = "food_id", onDelete = CASCADE))
public class Nutrition {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "nutrition_id")
    private int nutritionId;

    @ColumnInfo(name = "food_id")
    private int foodId;

    @ColumnInfo(name = "type_id")
    private int mealType;

    @ColumnInfo(name = "calories")
    private float nfCalories;

    @ColumnInfo(name = "dietary_fiber")
    private float dietary_fiber;

    @ColumnInfo(name = "cholesterol")
    private float nfCholesterol;

    @ColumnInfo(name = "protein")
    private float nfProtein;

    @ColumnInfo(name = "saturated_fat")
    private float nfSaturatedFat;

    @ColumnInfo(name = "total_fat")
    private float nfTotalFat;

    @ColumnInfo(name = "sugars")
    private float nfSugars;

    @ColumnInfo(name = "total_carbohydrate")
    private float nfTotalCarbohydrate;

    @ColumnInfo(name = "serving_qty")
    private int servingQty;

    @ColumnInfo(name = "serving_unit")
    private String servingUnit;

    @ColumnInfo(name = "serving_weight_grams")
    private float servingWeightGrams;

    private String source;

    public Nutrition() {
    }

    public Nutrition(int foodId, float nfCalories, float dietary_fiber,
                     float nfCholesterol, float nfProtein, float nfSaturatedFat, float nfTotalFat,
                     float nfSugars, float nfTotalCarbohydrate, int servingQty, String servingUnit,
                     int servingWeightGrams, String source) {
        this.foodId = foodId;
        this.mealType = mealType;
        this.nfCalories = nfCalories;
        this.dietary_fiber = dietary_fiber;
        this.nfCholesterol = nfCholesterol;
        this.nfProtein = nfProtein;
        this.nfSaturatedFat = nfSaturatedFat;
        this.nfTotalFat = nfTotalFat;
        this.nfSugars = nfSugars;
        this.nfTotalCarbohydrate = nfTotalCarbohydrate;
        this.servingQty = servingQty;
        this.servingUnit = servingUnit;
        this.servingWeightGrams = servingWeightGrams;
        this.source = source;
    }

    public int getNutritionId() {
        return nutritionId;
    }

    public void setNutritionId(int nutritionId) {
        this.nutritionId = nutritionId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getTypeId() {
        return mealType;
    }

    public void setTypeId(int typeId) {
        this.mealType = typeId;
    }

    public float getNfCalories() {
        return nfCalories;
    }

    public void setNfCalories(float nfCalories) {
        this.nfCalories = nfCalories;
    }

    public float getDietary_fiber() {
        return dietary_fiber;
    }

    public void setDietary_fiber(float dietary_fiber) {
        this.dietary_fiber = dietary_fiber;
    }

    public float getNfProtein() {
        return nfProtein;
    }

    public void setNfProtein(float nfProtein) {
        this.nfProtein = nfProtein;
    }

    public float getNfSaturatedFat() {
        return nfSaturatedFat;
    }

    public void setNfSaturatedFat(float nfSaturatedFat) {
        this.nfSaturatedFat = nfSaturatedFat;
    }

    public float getNfSugars() {
        return nfSugars;
    }

    public void setNfSugars(float nfSugars) {
        this.nfSugars = nfSugars;
    }

    public int getServingQty() {
        return servingQty;
    }

    public void setServingQty(int servingQty) {
        this.servingQty = servingQty;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getMealType() {
        return mealType;
    }

    public void setMealType(int mealType) {
        this.mealType = mealType;
    }

    public float getNfCholesterol() {
        return nfCholesterol;
    }

    public void setNfCholesterol(float nfCholesterol) {
        this.nfCholesterol = nfCholesterol;
    }

    public float getNfTotalFat() {
        return nfTotalFat;
    }

    public void setNfTotalFat(float nfTotalFat) {
        this.nfTotalFat = nfTotalFat;
    }

    public float getNfTotalCarbohydrate() {
        return nfTotalCarbohydrate;
    }

    public void setNfTotalCarbohydrate(float nfTotalCarbohydrate) {
        this.nfTotalCarbohydrate = nfTotalCarbohydrate;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }

    public float getServingWeightGrams() {
        return servingWeightGrams;
    }

    public void setServingWeightGrams(float servingWeightGrams) {
        this.servingWeightGrams = servingWeightGrams;
    }
}
