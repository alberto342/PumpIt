package com.albert.fitness.pumpit.model.nutrition;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_favorite_table")
public class FoodFavorite {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name ="is_favorite")
    private Boolean isFavorite;

    public FoodFavorite() {
    }

    public FoodFavorite(int id, Boolean isFavorite) {
        this.id = id;
        this.isFavorite = isFavorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }
}
