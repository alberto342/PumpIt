package com.albert.fitness.pumpit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise_favorite_table")
public class ExerciseFavorite {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "favorite_id")
    private int favoriteId;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    public ExerciseFavorite() {
    }

    public ExerciseFavorite(int favoriteId, boolean isFavorite) {
        this.favoriteId = favoriteId;
        this.isFavorite = isFavorite;
    }

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
