package com.albert.fitness.pumpit.model.nutrition.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "photo_table", foreignKeys = @ForeignKey(entity = FoodsObj.class,
        parentColumns = "food_id", childColumns = "food_id", onDelete = CASCADE))
public class Photo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "photo_id")
    private int photoId;

    @ColumnInfo(name = "food_id")
    private int foodId;

    private String highres;

    private String thumb;

    public Photo() {
    }

    public Photo(int foodId, String highres, String thumb) {
        this.foodId = foodId;
        this.highres = highres;
        this.thumb = thumb;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getHighres() {
        return highres;
    }

    public void setHighres(String highres) {
        this.highres = highres;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
