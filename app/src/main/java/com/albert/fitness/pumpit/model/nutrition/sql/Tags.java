package com.albert.fitness.pumpit.model.nutrition.sql;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "tags_table", foreignKeys = @ForeignKey(entity = FoodsObj.class,
        parentColumns = "food_id", childColumns = "food_id", onDelete = CASCADE))
public class Tags {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tag_id")
    private int tagId;

    @ColumnInfo(name = "food_id")
    private int foodId;

    @ColumnInfo(name = "food_group")
    private int foodGroup;

    private String item;

    private String measure;

    private int quantity;

    @ColumnInfo(name = "api_tag_id")
    private int tagIdApi;

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getFoodGroup() {
        return foodGroup;
    }

    public void setFoodGroup(int foodGroup) {
        this.foodGroup = foodGroup;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTagIdApi() {
        return tagIdApi;
    }

    public void setTagIdApi(int tagIdApi) {
        this.tagIdApi = tagIdApi;
    }
}
