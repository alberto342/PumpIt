package com.albert.fitness.pumpit.model;

import com.google.gson.annotations.SerializedName;

public class Common {

    @SerializedName("food_name")
    private String foodName;
    private String image;
    @SerializedName("tag_id")
    private int tagId;
    @SerializedName("tag_name")
    private String tagName;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
