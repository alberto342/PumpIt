package com.albert.fitness.pumpit.model.nutrition;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Foods {

    public static final String SHARED_PREFERENCES_FILE = "meal";
    public static final String DINNER = "dinner";
    public static final String BREAKFAST = "breakfast";
    public static final String LUNCH = "lunch";
    public static final String SNACK = "snack";
    public static final String NUTRITION = "nutrition";
    public static final String All_NUTRITION = "all_nutrition";
    public static final String WATER_TRACKER = "water_tracker";

    public Foods() {
    }


    @SerializedName("serving_unit")
    private String servingUnit;

    @SerializedName("nf_total_carbohydrate")
    private float nfTotalCarbohydrate;

    @SerializedName("nf_sodium")
    private float nfSodium;

    @SerializedName("nf_p")
    private float nfP;

    @SerializedName("alt_measures")
    private List<AltMeasures> altMeasures;

    @SerializedName("nf_sugars")
    private float nfSugars;

    @SerializedName("nf_calories")
    private float nfCalories;

    @SerializedName("brand_name")
    private String brandName;

    private String lat;

    private Metadata metadata;

    private Tags tags;

    @SerializedName("sub_recipe")
    private String subRecipe;

    @SerializedName("ndb_no")
    private String ndbNo;

    @SerializedName("food_name")
    private String foodName;

    @SerializedName("meal_type")
    private String mealType;

    @SerializedName("nf_saturated_fat")
    private float nfSaturatedFat;

    @SerializedName("nf_protein")
    private float nfProtein;

    @SerializedName("nf_total_fat")
    private float nfTotalFat;

    @SerializedName("consumed_at")
    private String consumedAt;

    @SerializedName("nf_dietary_fiber")
    private float nfDietaryFiber;

    @SerializedName("nf_cholesterol")
    private float nfCholesterol;

    private Photo photo;

    private String upc;

    @SerializedName("nf_potassium")
    private float nfPotassium;

    @SerializedName("nix_brand_name")
    private String nixBrandName;

    @SerializedName("full_nutrients")
    private List<FullNutrients> fullNutrients;

    @SerializedName("nix_item_id")
    private String nixItemId;

    private String source;

    @SerializedName("nix_item_name")
    private String nixItemName;

    @SerializedName("serving_weight_grams")
    private float servingWeightGrams;

    @SerializedName("nix_brand_id")
    private String nixBrandId;

    @SerializedName("serving_qty")
    private int servingQty;

    public String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }

    public float getNfTotalCarbohydrate() {
        return nfTotalCarbohydrate;
    }

    public void setNfTotalCarbohydrate(float nfTotalCarbohydrate) {
        this.nfTotalCarbohydrate = nfTotalCarbohydrate;
    }

    public float getNfSodium() {
        return nfSodium;
    }

    public void setNfSodium(float nfSodium) {
        this.nfSodium = nfSodium;
    }

    public float getNfP() {
        return nfP;
    }

    public void setNfP(float nfP) {
        this.nfP = nfP;
    }

    public List<AltMeasures> getAltMeasures() {
        return altMeasures;
    }

    public float getNfSugars() {
        return nfSugars;
    }

    public void setNfSugars(float nfSugars) {
        this.nfSugars = nfSugars;
    }

    public float getNfCalories() {
        return nfCalories;
    }

    public void setNfCalories(float nfCalories) {
        this.nfCalories = nfCalories;
    }

    public Tags getTags() {
        return tags;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public float getNfSaturatedFat() {
        return nfSaturatedFat;
    }

    public void setNfSaturatedFat(float nfSaturatedFat) {
        this.nfSaturatedFat = nfSaturatedFat;
    }

    public float getNfProtein() {
        return nfProtein;
    }

    public void setNfProtein(float nfProtein) {
        this.nfProtein = nfProtein;
    }

    public float getNfTotalFat() {
        return nfTotalFat;
    }

    public void setNfTotalFat(float nfTotalFat) {
        this.nfTotalFat = nfTotalFat;
    }

    public float getNfDietaryFiber() {
        return nfDietaryFiber;
    }

    public void setNfDietaryFiber(float nfDietaryFiber) {
        this.nfDietaryFiber = nfDietaryFiber;
    }

    public float getNfCholesterol() {
        return nfCholesterol;
    }

    public void setNfCholesterol(float nfCholesterol) {
        this.nfCholesterol = nfCholesterol;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public float getNfPotassium() {
        return nfPotassium;
    }

    public void setNfPotassium(float nfPotassium) {
        this.nfPotassium = nfPotassium;
    }

    public List<FullNutrients> getFullNutrients() {
        return fullNutrients;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public float getServingWeightGrams() {
        return servingWeightGrams;
    }

    public void setServingWeightGrams(float servingWeightGrams) {
        this.servingWeightGrams = servingWeightGrams;
    }

    public int getServingQty() {
        return servingQty;
    }

    public void setServingQty(int servingQty) {
        this.servingQty = servingQty;
    }

    @NonNull
    @Override
    public String toString() {
        return "ClassPojo [servingUnit = " + servingUnit + ", nfTotalCarbohydrate = " + nfTotalCarbohydrate + ", nfSodium = " + nfSodium + ", nfP = " + nfP + ", AltMeasures = " + altMeasures + ", nfSugars = " + nfSugars + ", nfCalories = " + nfCalories + ", brandName = " + brandName + ", lat = " + lat + ", metadata = " + metadata + ", tags = " + tags + ", subRecipe = " + subRecipe + ", ndbNo = " + ndbNo + ", foodName = " + foodName + ", mealType = " + mealType + ", nfSaturatedFat = " + nfSaturatedFat + ", nfTotalFat = " + nfTotalFat + ", consumedAt = " + consumedAt + ", nfDietaryFiber = " + nfDietaryFiber + ", nfCholesterol = " + nfCholesterol + ", photo = " + photo + ", upc = " + upc + ", nfPotassium = " + nfPotassium + ", nixBrandName = " + nixBrandName + ", FullNutrients = " + fullNutrients + ", nixItemId = " + nixItemId + ", source = " + source + ", nixItemName = " + nixItemName + ", servingWeightGrams = " + servingWeightGrams + ", nixBrandId = " + nixBrandId + ", servingQty = " + servingQty + "]";
    }


}
